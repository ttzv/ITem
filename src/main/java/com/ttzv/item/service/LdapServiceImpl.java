package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.ldap.LdapParser;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<Map<String, String>> getResults() throws NamingException, IOException, GeneralSecurityException {
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
        List<Map<String, String>> users = null;
        try {
            users = getResults();
        } catch (NamingException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        List<ADUser_n> allUsers = new ArrayList<>();
        for (Map<String, String> userMap:
            users){
            ADUser_n adUser = new ADUser_n();
            adUser.setObjectGUID(userMap.get("objectGUID"));
            adUser.setGivenName(userMap.get("givenName"));
            adUser.setSn(userMap.get("sn"));
            adUser.setDisplayName(userMap.get("displayName"));
            adUser.setSAMAccountName(userMap.get("sAMAccountName"));
            adUser.setWhenCreated(Utility.parseDate(userMap.get("whenCreated"), Utility.ldapDateFormat()));
            adUser.setEmail(userMap.get("mail"));
            adUser.setDistinguishedName(userMap.get("distinguishedName"));
            adUser.setObjectSid(userMap.get("objectSid"));
            adUser.setUserAccountControl(userMap.get("userAccountControl"));
            adUser.setLockoutTime(Utility.parseLockoutTimestamp(userMap.get("lockoutTime")));
            allUsers.add(adUser);
        }
        return allUsers;
    }

    @Override
    public void unlockADUser(ADUser_n adUser) {
        ModificationItem[] mods = new ModificationItem[1];
        DirContext ctx = null;
        try {
            ctx = ldapParser.getLdapContext();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Attribute A0 = new BasicAttribute("lockoutTime", "0");
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, A0);
        try {
            ctx.modifyAttributes(adUser.getDistinguishedName(), mods);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
