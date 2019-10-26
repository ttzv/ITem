package com.ttzv.item.activeDirectory;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.utility.Utility;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LDAPParser
{
    private DirContext ldapContext;
    private String ldap_URL;
    private String ldap_port;
    private String ad_adminUser;
    private char[] ad_adminPass;
    private List<User> usersDataList;
    private int totalResults;
    private List<List<String>> results;
    public static final String LDAP_SEPARATOR = ":";

    public List<User> getUsersDataList() {
        if(!usersDataList.isEmpty()) {
            return usersDataList;
        } else {
            return null;
        }
    }

    public void loadCfgCredentials() {
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

    public List<List<String>> getResults() {
        return results;
    }

    public static QueryBuilder builder(){
        return new QueryBuilder();
    }

    public void initializeLdapContext() throws NamingException{

        Properties ldapEnv = new Properties();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); //always the same
        ldapEnv.put(Context.PROVIDER_URL,  "ldap://" + ldap_URL + ":" + ldap_port);
        ldapEnv.put("java.naming.ldap.attributes.binary", "objectGUID");
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
    public static LDAPParser getLdapParser(){
        LDAPParser ldapParser = new LDAPParser();
        ldapParser.loadCfgCredentials();
        try {
            ldapParser.initializeLdapContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return ldapParser;
    }

    /**
     * Method used to retrieve information from LDAP with given Query. Because LDAP query consists of many different parameters
     * this method requires QueryBuilder object - a convenience set of methods that should ease building a query and searching through LDAP
     * At the end of building Query call validate() method to check if all required parameters were set and
     * @param queryBuilder LDAPParser.QueryBuilder object used to build LDAP query - entry method is LDAPParser.QueryBuilder.builder()
     * @return Count of results retrieved from LDAP
     */
    public int queryLdap(QueryBuilder queryBuilder) {
        if(queryBuilder.isValidated()) {
            NamingEnumeration<SearchResult> answer = null;
            results = new ArrayList<>();
            totalResults = 0;
            try {
                answer = ldapContext.search(queryBuilder.getSearchBase(), queryBuilder.getSearchFilter(), queryBuilder.getSearchControls());
                while (answer.hasMoreElements()) {
                    SearchResult sr = answer.next();
                    totalResults++;
                    Attributes returnedAttrs = sr.getAttributes();
                    List<String> returnedAttributeList = Arrays.stream(queryBuilder.getSearchAttributes())
                            .map(s -> {
                                //each defined attribute is retrieved and collected in list with corresponding attribute identifier, null attributes are changed to empty strings
                                        try {
                                            return (returnedAttrs.get(s) == null) ? (s + LDAP_SEPARATOR + "") : (s + LDAP_SEPARATOR + returnedAttrs.get(s).get().toString());
                                        } catch (NamingException e) {
                                            e.printStackTrace();
                                        }
                                        return s;
                                    }
                            )
                            .collect(Collectors.toList());
                    results.add(returnedAttributeList);
                }
                ldapContext.close();
            } catch (NamingException e1) {
                e1.printStackTrace();
            }
            return totalResults;
        } else {
            System.err.println("Query not validated or missing query parameters - check QueryBuilder.validate() method");
            return -1;
        }
    }

    protected static class QueryBuilder {

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







