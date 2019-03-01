package db;

import ad.LDAPParser;
import ad.User;
import properties.Cfg;
import ui.settingsWindow.PHolder;

import javax.naming.NamingException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DbCon {

    private Connection conn;
    private String dbUrl;
    private String dbUser;
    private char[] dbPass;

    public boolean initConnection() throws SQLException{
        String dbUrl = "jdbc:postgresql://" + this.dbUrl;

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", new String(dbPass));

        this.conn = DriverManager.getConnection(dbUrl, props);

        return true;
    }

    public void ldapToDb() throws SQLException {
        Statement st = conn.createStatement();

        LDAPParser ldapParser = new LDAPParser();
        try {
            ldapParser.initializeLdapContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        ldapParser.queryLdap();
        List<User> ldapusers = ldapParser.getUsersDataList();
        String[] columns = {"samaccountname", "givenname", "sn", "displayname", "useraccountcontrol", "mail", "whenCreated"};
        for (User u : ldapusers) {
            //String qpart ="(" +"'"+u.getSamAccountName()+"'" +","+ "'"+u.getGivenName()+"'" +","+ "'"+u.getSn()+"'" +","+ "'"+u.getDisplayName()+"'" +","+ "'"+u.getUserAccountControl()+"'"+")";
            String query = PgStatement.insert("users", columns, PgStatement.apostrophied(u.getRow())); //hopefully this works in the same way
            st.executeUpdate(query);
        }

        st.close();
    }

    public void updateDbValues(String column) throws SQLException {
        Statement st = conn.createStatement();

    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public char[] getDbPass() {
        return dbPass;
    }

    public void setDbPass(char[] dbPass) {
        this.dbPass = dbPass;
    }
}