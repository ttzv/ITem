package com.ttzv.item.dao;

import com.ttzv.item.entity.DynamicEntity;
import com.ttzv.item.entity.EntityDAO;
import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.entity.User;
import com.ttzv.item.parser.LDAPParser;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoLdapImpl implements EntityDAO<User> {

    private LDAPParser ldapParser;
    //todo: allow changing below parameters in runtime, decouple
    private final String searchBase;
    private final String searchFilter;
    private String[] searchAttributes;
    private int searchControlsScope;
    private KeyMapper keyMapper;

    public UserDaoLdapImpl() throws NamingException, IOException, GeneralSecurityException {
        this.searchBase = "ou=Pracownicy,dc=atal,dc=local";
        this.searchFilter = "(&(objectClass=user))";
        this.searchAttributes = new String[]{"objectGUID", "givenName", "sn", "displayName", "sAMAccountName", "userAccountControl", "mail", "whenCreated", "distinguishedName", /*"whenChanged"*/};
        this.searchControlsScope = SearchControls.SUBTREE_SCOPE;
        this.ldapParser = LDAPParser.getLdapParser();
        ldapParser.closeContext();//redundancy with method getResults() to check connection when creating dao.
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class);
    }

    public List<List<String>> getResults() throws NamingException, IOException, GeneralSecurityException {
            this.ldapParser = LDAPParser.getLdapParser();
            this.ldapParser.queryLdap(LDAPParser.builder()
                .setSearchBase(searchBase)
                .setSearchFilter(searchFilter)
                .setSearchAttributes(searchAttributes)
                .setSearchControlsScope(searchControlsScope)
                .validate());
            ldapParser.closeContext();
        return this.ldapParser.getResults();

    }

    @Override
    public List<User> getAllEntities() throws NamingException, IOException, GeneralSecurityException {
        List<User> allUsers = new ArrayList<>();
        for (List<String> list :
                getResults()) {
            allUsers.add(new User(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return allUsers;
    }

    @Override
    public User getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(User entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(User entity) {
        return false;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<User> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }


}
