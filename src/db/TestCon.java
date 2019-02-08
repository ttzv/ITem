package db;

import ad.LDAPParser;
import ad.User;

import javax.naming.NamingException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class TestCon {



    public static void main(String[] args) throws SQLException {

        String url = "jdbc:postgresql://localhost/jitmg";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","admin");
        Connection conn = DriverManager.getConnection(url, props);
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
            String query = PgStatement.insert("users", columns, PgStatement.apostrophied( (Object[]) u.getRow()));
            st.executeUpdate(query);

        }




        /*while (rs.next())
        {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
        }*/
        st.close();

    }
}
