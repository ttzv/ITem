package db;

import ad.User;

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
*/
       /* char[] dbPass = {'a','d','m','i','n'};
        DbCon dbCon = new DbCon();
        dbCon.setDbUrl("localhost/jitmg");
        dbCon.setDbUser("postgres");
        dbCon.setDbPass(dbPass);
        dbCon.initConnection();

        //System.out.println(dbCon.getNewestUser());

        //List<User> newusers = dbCon.updateUsersTable();

        //System.out.println("Added: " + newusers.size() + " new users:\n " + newusers);

        //System.out.println(dbCon.getNewestUser());

        //dbCon.updateUsersInfo();
        dbCon.getNewUsers(10);


        //System.out.println( Utility.extractCityFromDn("CN=Magdalena Kwiatkowska,OU=Gdansk,OU=Pracownicy,DC=atal,DC=local") );
*/
       DbCon dbCon = new DbCon();

       dbCon.globalSearch("Cieszyn", 10);
        dbCon.globalSearch("tzwak", 0);
    }
}
