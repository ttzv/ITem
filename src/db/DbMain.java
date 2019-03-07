package db;

import ad.LDAPParser;
import ad.User;
import utility.Utility;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class DbMain {

    public static void main(String[] args) throws SQLException, NamingException {
/*
        char[] ldapPass = {'K','i','a','n','o','@','1','2'};
        LDAPParser ldapParser = new LDAPParser();
        ldapParser.setLdap_URL("ataladc1.atal.local");
        ldapParser.setLdap_port("389");
        ldapParser.setAd_adminUser("CN=Serwis,CN=Users,DC=atal,DC=local");
        ldapParser.setAd_adminPass(ldapPass);
        ldapParser.initializeLdapContext();

        char[] dbPass = {'a','d','m','i','n'};
        DbCon dbCon = new DbCon(ldapParser);
        dbCon.setDbUrl("localhost/jitmg");
        dbCon.setDbUser("postgres");
        dbCon.setDbPass(dbPass);
        dbCon.initConnection();

        System.out.println(dbCon.getNewestUser());

        List<User> newusers = dbCon.getNewUsers();

        System.out.println("Added: " + newusers.size() + " new users:\n " + newusers);

        System.out.println(dbCon.getNewestUser());

        dbCon.updateUsersInfo();
*/

        //System.out.println( Utility.extractCityFromDn("CN=Magdalena Kwiatkowska,OU=Gdansk,OU=Pracownicy,DC=atal,DC=local") );

    }
}
