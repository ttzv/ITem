package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser;
import com.ttzv.item.entity.DynamicEntity;
import com.ttzv.item.entity.EntityDAO;
import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.ldap.LdapParser;
import com.ttzv.item.properties.Cfg;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoLdapImpl implements EntityDAO<ADUser> {

    private LdapParser ldapParser;
    private final String searchBase;
    private final String searchFilter;
    private final String[] searchAttributes;
    private final int searchControlsScope;
    private final Cfg AppConfiguration = Cfg.getInstance();

    public UserDaoLdapImpl() throws NamingException, IOException, GeneralSecurityException {
        this.searchBase = AppConfiguration.retrieveProp(Cfg.LDAP_SEARCH_BASE);
        this.searchFilter = "(&(objectClass=user))";
        this.searchAttributes = new String[]{"objectGUID", "givenName", "sn", "displayName", "sAMAccountName", "userAccountControl", "mail", "whenCreated", "distinguishedName", "objectSid", "lockoutTime"};
        this.searchControlsScope = SearchControls.SUBTREE_SCOPE;
        this.ldapParser = LdapParser.getLdapParser();
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
    public List<ADUser> getAllEntities() throws NamingException, IOException, GeneralSecurityException {
        List<ADUser> allADUsers = new ArrayList<>();
        for (List<String> list :
                getResults()) {
            allADUsers.add(new ADUser(DynamicEntity.newDynamicEntity()
                    .process(list)
            ));
        }
        return allADUsers;
    }

    @Override
    public ADUser getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(ADUser entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(ADUser entity) {
        return false;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<ADUser> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }


}
