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

    public DbCon() throws SQLException {

        String dbUrl = "jdbc:postgresql://" + Cfg.getInstance().retrieveProp(Cfg.DB_URL);
        String dbUser = Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN);

        Properties props = new Properties();
        props.setProperty("user",dbUser);
        props.setProperty("password", new String(PHolder.db));

        this.conn = DriverManager.getConnection(dbUrl, props);

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
        String[] columns = {"samaccountname", "givenname", "sn", "displayname", "useraccountcontrol"};
        for(User u : ldapusers){
            //String qpart ="(" +"'"+u.getSamAccountName()+"'" +","+ "'"+u.getGivenName()+"'" +","+ "'"+u.getSn()+"'" +","+ "'"+u.getDisplayName()+"'" +","+ "'"+u.getUserAccountControl()+"'"+")";
            String query = PgStatement.insert("users", columns, PgStatement.apostrophied( (Object[]) u.getRow())); //hopefully this works in the same way
            st.executeUpdate(query);

        }

        st.close();
    }

}
