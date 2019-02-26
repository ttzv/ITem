package db;

import ad.LDAPParser;
import ad.User;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class DbMain {

    public static void main(String[] args) throws SQLException {
        DbCon dbCon = new DbCon();
        dbCon.ldapToDb();
    }
}
