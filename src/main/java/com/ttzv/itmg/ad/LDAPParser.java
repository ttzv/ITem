package com.ttzv.itmg.ad;

import com.ttzv.itmg.properties.Cfg;
import com.ttzv.itmg.pwSafe.PHolder;
import com.ttzv.itmg.utility.Utility;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LDAPParser
{
    private static DirContext ldapContext;
    private String ldap_URL;
    private String ldap_port;
    private String ad_adminUser;
    private char[] ad_adminPass;
    private List<User> usersDataList;

    public List<User> getUsersDataList() {
        if(!usersDataList.isEmpty()) {
            return usersDataList;
        } else {
            return null;
        }
    }

    public void loadCfgCredentials(){
        this.setLdap_URL(Cfg.getInstance().retrieveProp(Cfg.LDAP_URL));
        this.setLdap_port(Cfg.getInstance().retrieveProp(Cfg.LDAP_PORT));
        this.setAd_adminUser(Cfg.getInstance().retrieveProp(Cfg.LDAP_ACC));
        this.setAd_adminPass(PHolder.ldap);
    }

    private String domainControllerName; //placeholder

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

    public void initializeLdapContext() throws NamingException{

        Properties ldapEnv = new Properties();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); //always the same
        //ldapEnv.put(Context.PROVIDER_URL,  "ldap://ataladc1.atal.local:389");
        ldapEnv.put(Context.PROVIDER_URL,  "ldap://" + ldap_URL + ":" + ldap_port);
        //ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put("java.naming.ldap.attributes.binary", "objectGUID");

        //ldapEnv.put(Context.SECURITY_PRINCIPAL, "CN=Serwis,CN=Users,DC=atal,DC=local");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, ad_adminUser);
        //ldapEnv.put(Context.SECURITY_CREDENTIALS, "");
        ldapEnv.put(Context.SECURITY_CREDENTIALS, new String(ad_adminPass));

        //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
        ldapContext = new InitialDirContext(ldapEnv);
    }

    public int queryLdap() {


        SearchControls searchCtls = new SearchControls();

        //Specify the attributes to return
        String returnedAtts[] = {"objectGUID", "sn", "givenName", "displayName", "samAccountName", "userAccountControl", "mail", "whenCreated","distinguishedName","whenChanged"};
        searchCtls.setReturningAttributes(returnedAtts);

        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //specify the LDAP search filter
        String searchFilter = "(&(objectClass=user))";

        //Specify the Base for the search
        String searchBase = "ou=Pracownicy,dc=atal,dc=local";
        //initialize counter to total the results
        int totalResults = 0;
        // create ArrayList to store parsed data
        usersDataList = new ArrayList<>();
        // Search for objects using the filter
        NamingEnumeration<SearchResult> answer = null;
        try {

            answer = ldapContext.search(searchBase, searchFilter, searchCtls);

            //Loop through the search results
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();

                totalResults++;

                //System.out.println(sr.getName());
                Attributes attrs = sr.getAttributes();

                /*
                System.out.println(attrs.get("givenName") + " "
                        + attrs.get("sn") + " "
                        + attrs.get("displayName") + " "
                        + attrs.get("samAccountName") + " "
                        + attrs.get("userAccountControl") + " ");
                */

                //System.out.println(attrs);

                Attribute attr_objectGUID;
                Attribute attr_samAccountName;
                Attribute attr_givenName;
                Attribute attr_sn;
                Attribute attr_displayName;
                Attribute attr_userAccountControl;
                Attribute attr_mail;
                Attribute attr_whenCreated;
                Attribute attr_distinguishedName;
                Attribute attr_whenChanged;

                String objectGUID;
                String samAccountName;
                String givenName;
                String sn;
                String displayName;
                String userAccountControl;
                String mail;
                String whenCreated;
                String distinguishedName;
                String whenChanged;

                if( ( attr_objectGUID = attrs.get("objectGUID") ) == null)
                {
                    objectGUID = "null";
                } else {
                    objectGUID = Utility.formatObjectGUID(attr_objectGUID.get());
                }

                if( ( attr_samAccountName = attrs.get("samAccountName")) == null)
                {
                    samAccountName = "null";
                } else {
                    samAccountName = attr_samAccountName.get().toString();
                }

                if( ( attr_givenName = attrs.get("givenName")) == null)
                {
                    givenName = "null";
                } else {
                   givenName = attr_givenName.get().toString();
                }

                if( ( attr_sn = attrs.get("sn") ) == null)
                {
                    sn = "null";
                } else {
                    sn = attr_sn.get().toString();
                }

                if( ( attr_displayName = attrs.get("displayName") ) == null)
                {
                    displayName = "null";
                } else {
                    displayName = attr_displayName.get().toString();
                }

                if( ( attr_userAccountControl = attrs.get("userAccountControl") ) == null)
                {
                    userAccountControl = "null";
                } else {
                    userAccountControl = attr_userAccountControl.get().toString();
                }

                if( ( attr_mail = attrs.get("mail") ) == null)
                {
                    mail = "null";
                } else {
                    mail = attr_mail.get().toString();
                }

                if( ( attr_whenCreated= attrs.get("whenCreated") ) == null)
                {
                    whenCreated = "null";
                } else {
                    whenCreated = attr_whenCreated.get().toString();
                }

                String city;
                if( ( attr_distinguishedName = attrs.get("distinguishedName") ) == null)
                {
                    distinguishedName = "null";
                } else {
                    distinguishedName = attr_distinguishedName.get().toString();
                }
                city = cityNameToId(Utility.extractCityFromDn(distinguishedName));

                if( ( attr_whenChanged = attrs.get("whenChanged") ) == null)
                {
                    whenChanged = "null";
                } else {
                    whenChanged = attr_whenChanged.get().toString();
                }

                usersDataList.add(new User(objectGUID, samAccountName, givenName, sn, displayName, userAccountControl, mail, whenCreated, city, whenChanged));
               // System.out.println(distinguishedName);
            }


            ldapContext.close();
        } catch (NamingException e1) {
            e1.printStackTrace();
        }

        return totalResults;
    }

    private String cityNameToId(String city){
        switch (city){
            case "Cieszyn":
                return "c001";
            case "Katowice":
                return "c002";
            case "Krakow":
                return "c003";
            case "Warszawa":
                return "c004";
            case "Poznan":
                return "c005";
            case "Gdansk":
                return "c006";
            case "Wroclaw":
                return "c007";
            case "Lodz":
                return "c008";
            default:
                return "ERVAL";
        }
    }
}







