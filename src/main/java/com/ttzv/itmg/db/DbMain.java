package com.ttzv.itmg.db;

import javax.naming.NamingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class DbMain {

    public static void main(String[] args) {
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
       //DbCon dbCon = new DbCon();

       //dbCon.globalSearch("Filia", 10);
      // dbCon.globalSearch("Małachowski", 0);

       // Path testpath = Paths.get(System.getProperty("user.home"))
             //   .resolve("AppData")
            //    .resolve("Local");
       // System.out.println(testpath);

        DbCon dbCon = new DbCon();
        try {
            dbCon.update("users", "samaccountname=" + PgStatement.apostrophied("tzwak"), "phone='test1'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
