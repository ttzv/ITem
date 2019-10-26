package com.ttzv.item.activeDirectory;

import com.ttzv.item.utility.Utility;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.List;

public class UserDaoLdapImpl implements EntityDAO<User> {

    private LDAPParser ldapParser;
    //todo: allow changing below parameters in runtime
    private final String searchBase;
    private final String searchFilter;
    private String[] searchAttributes;
    private int searchControlsScope;

    public UserDaoLdapImpl() {
        this.searchBase = "ou=Pracownicy,dc=atal,dc=local";
        this.searchFilter = "(&(objectClass=user))";
        this.searchAttributes = new String[]{"objectGUID", "givenName", "sn", "displayName", "samAccountName", "userAccountControl", "mail", "whenCreated", "distinguishedName", "whenChanged"};
        this.searchControlsScope = SearchControls.SUBTREE_SCOPE;
        this.ldapParser = LDAPParser.getLdapParser();
    }

    public List<List<String>> getResults(){
        this.ldapParser.queryLdap(LDAPParser.builder()
                .setSearchBase(searchBase)
                .setSearchFilter(searchFilter)
                .setSearchAttributes(searchAttributes)
                .setSearchControlsScope(searchControlsScope)
                .validate());
        return this.ldapParser.getResults();
    }

    @Override
    public List<User> getAllEntities() {
        List<User> allUsers = new ArrayList<>();
        for (List<String> list :
                getResults()) {
            allUsers.add(new User(DynamicEntity.newDynamicEntity().process(list)));
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


}
