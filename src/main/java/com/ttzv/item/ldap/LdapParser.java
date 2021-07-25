package com.ttzv.item.ldap;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import com.ttzv.item.utility.Utility;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.*;

public class LdapParser
{
    private DirContext ldapContext;
    private String ldap_URL;
    private String ldap_port;
    private String ad_adminUser;
    private char[] ad_adminPass;
    private List<ADUser> usersDataList;
    private int totalResults;
    private List<Map<String, String>> results;
    public static final String LDAP_SEPARATOR = Utility.DEFAULT_ENTITY_SEPARATOR;

    public List<ADUser> getUsersDataList() {
        if(!usersDataList.isEmpty()) {
            return usersDataList;
        } else {
            return null;
        }
    }

    private void loadCfgCredentials() throws IOException, GeneralSecurityException {
        this.setLdap_URL(Cfg.getInstance().retrieveProp(Cfg.LDAP_URL));
        this.setLdap_port(Cfg.getInstance().retrieveProp(Cfg.LDAP_PORT));
        this.setAd_adminUser(Cfg.getInstance().retrieveProp(Cfg.LDAP_ACC));
        this.setAd_adminPass(Crypt.newCrypt("lCr").read());
    }

    public String getLdap_URL() {
        return ldap_URL;
    }

    public void setLdap_URL(String ldap_URL) {
        this.ldap_URL = ldap_URL;
    }

    public String getAd_adminUser() {
        return ad_adminUser;
    }

    public void setAd_adminUser(String ad_adminUser) {
        this.ad_adminUser = ad_adminUser;
    }

    public char[] getAd_adminPass() {
        return ad_adminPass;
    }

    public void setAd_adminPass(char[] ad_adminPass) {
        this.ad_adminPass = ad_adminPass;
    }

    public String getLdap_port() {
        return ldap_port;
    }

    public void setLdap_port(String ldap_port) {
        this.ldap_port = ldap_port;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Map<String, String>> getResults() {
        return results;
    }

    public static QueryBuilder builder(){
        return new QueryBuilder();
    }

    private void initializeLdapContext() throws NamingException, UnknownHostException {

        Properties ldapEnv = new Properties();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); //always the same
        ldapEnv.put(Context.PROVIDER_URL,  "ldap://" + ldap_URL + ":" + ldap_port);
        ldapEnv.put("java.naming.ldap.attributes.binary", "objectSid objectGUID");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, ad_adminUser);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, new String(ad_adminPass));
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");

        ldapContext = new InitialDirContext(ldapEnv);
    }

    /**
     * Get preconfigured LdapParser. Configuration is retrieved from .properties file and can be freely modified (preferably by some UI)
     * @return new LdapParser configured by .properties file and ready to use.
     */
    public static LdapParser getLdapParser() throws NamingException, IOException, GeneralSecurityException {
        LdapParser ldapParser = new LdapParser();
        ldapParser.loadCfgCredentials();
        ldapParser.initializeLdapContext();
        return ldapParser;
    }

    /**
     * Method used to retrieve information from LDAP with given Query. Because LDAP query consists of many different parameters
     * this method requires QueryBuilder object - a convenience set of methods that should ease building a query and searching through LDAP
     * At the end of building Query call validate() method to check if all required parameters were set.
     * @param queryBuilder LDAPParser.QueryBuilder object used to build LDAP query - entry method is LDAPParser.QueryBuilder.builder()
     * @return Number of results retrieved from LDAP
     */
    public int queryLdap(QueryBuilder queryBuilder) throws NamingException {
        if(queryBuilder.isValidated()) {
            NamingEnumeration<SearchResult> answer;
            results = new ArrayList<>();
            totalResults = 0;
            answer = ldapContext.search(queryBuilder.getSearchBase(), queryBuilder.getSearchFilter(), queryBuilder.getSearchControls());
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                totalResults++;
                Attributes returnedAttrs = sr.getAttributes();
                Map<String, String> map = new HashMap<>();
                Arrays.stream(queryBuilder.getSearchAttributes()).forEach(s -> {
                    try {
                        map.put(s, "");
                        if (s.equals("objectGUID")) map.put(s, Utility.formatObjectGUID(returnedAttrs.get(s).get()));
                        else if (s.equals("objectSid")) map.put(s, Utility.formatObjectSid(returnedAttrs.get(s).get()));
                        else {
                            Optional<Attribute> optAttrVal = Optional.ofNullable(returnedAttrs.get(s));
                            optAttrVal.ifPresent(attribute -> {
                                try {
                                    map.put(s, attribute.get().toString());
                                } catch (NamingException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch(NamingException e){
                        e.printStackTrace();
                    }
                });
                results.add(map);
            }
            return totalResults;
        } else {
            System.err.println("Query not validated or missing query parameters - check QueryBuilder.validate() method");
            return -1;
        }
    }

    public void closeContext() throws NamingException {
        this.ldapContext.close();
    }

    public static class QueryBuilder {

        private String[] searchAttributes;
        private String searchFilter;
        private String searchBase;
        private SearchControls searchControls;
        private boolean isValidated;

        public QueryBuilder() {
            this.isValidated = false;
        }

        public boolean isValidated() {
            return isValidated;
        }

        public String[] getSearchAttributes() {
            return searchAttributes;
        }

        public QueryBuilder setSearchAttributes(String... attrs){
            this.searchAttributes = attrs;
            return this;
        }

        public String getSearchFilter() {
            return searchFilter;
        }

        public QueryBuilder setSearchFilter(String searchFilter) {
            this.searchFilter = searchFilter;
            return this;
        }

        public String getSearchBase() {
            return searchBase;
        }

        public QueryBuilder setSearchBase(String searchBase) {
            this.searchBase = searchBase;
            return this;
        }

        public SearchControls getSearchControls() {
            return searchControls;
        }

        public QueryBuilder setSearchControlsScope(int SCOPE) {
            this.searchControls = buildSearchControls(getSearchAttributes(), SCOPE);
            return this;
        }

        public SearchControls buildSearchControls (String[] searchAttributes, int SCOPE){
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(searchAttributes);
            searchControls.setSearchScope(SCOPE);
            return searchControls;
        }

        public QueryBuilder validate() {
            boolean validated;
            ArrayList<String> parameters = new ArrayList<>();

            if (this.getSearchAttributes() != null) {
                isValidated = true;
            } else {
                parameters.add("SearchAttributes");
                isValidated = false;
            }

            if (this.getSearchBase() != null) {
                isValidated = true;
            } else {
                parameters.add("SearchBase");
                isValidated = false;
            }

            if (this.getSearchControls() != null) {
                isValidated = true;
            } else {
                parameters.add("SearchControls");
                isValidated = false;
            }

            if (this.getSearchFilter() != null) {
                isValidated = true;
            } else {
                parameters.add("SearchFilter");
                isValidated = false;
            }

            if (isValidated) {
                return this;
            } else {
                System.err.println("Required parameter/s: " + parameters);
                return null;
            }
        }
    }
}







