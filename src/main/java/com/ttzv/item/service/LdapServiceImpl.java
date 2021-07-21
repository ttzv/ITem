package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser;
import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.ldap.LdapParser;
import com.ttzv.item.properties.Cfg;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

public class LdapServiceImpl implements LdapService{

    private LdapParser ldapParser;
    private final String searchBase;
    private final String searchFilter;
    private final String[] searchAttributes;
    private final int searchControlsScope;
    private final Cfg AppConfiguration = Cfg.getInstance();

    public LdapServiceImpl() throws GeneralSecurityException, NamingException, IOException {
        searchBase = AppConfiguration.retrieveProp(Cfg.LDAP_SEARCH_BASE);
        searchFilter = "(&(objectClass=user))";
        searchAttributes = new String[]{"objectGUID", "givenName", "sn", "displayName", "sAMAccountName", "userAccountControl", "mail", "whenCreated", "distinguishedName", "objectSid", "lockoutTime"};
        searchControlsScope = SearchControls.SUBTREE_SCOPE;
        ldapParser = LdapParser.getLdapParser();
        ldapParser.closeContext();//redundancy with method getResults() to check connection when creating dao.
    }

    public List<List<String>> getResults() throws NamingException, IOException, GeneralSecurityException {
        this.ldapParser = LdapParser.getLdapParser();
        this.ldapParser.queryLdap(LdapParser.builder()
                .setSearchBase(searchBase)
                .setSearchFilter(searchFilter)
                .setSearchAttributes(searchAttributes)
                .setSearchControlsScope(searchControlsScope)
                .validate());
        ldapParser.closeContext();
        return this.ldapParser.getResults();
    }

    @Override
    public List<ADUser_n> getAll() {
        ADUser_n adUser = new ADUser_n();
        adUser.setObjectGUID("");
        adUser.setGivenName("");
        adUser.setSn("");
        adUser.setDisplayName("");
        adUser.setSAMAccountName("");
        adUser.setWhenCreated(new Date());
        adUser.setEmail("");
        adUser.setDistinguishedName("");
        adUser.setObjectSid("");
        adUser.setUserAccountControl("");
        adUser.setLockoutTime(new Date());
        return null;
    }
}
