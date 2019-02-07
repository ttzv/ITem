package db;

import java.sql.*;
import java.util.Properties;

public class TestCon {



    public static void main(String[] args) throws SQLException {

        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","admin");
        Connection conn = DriverManager.getConnection(url, props);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM t1");
        while (rs.next())
        {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
        }
        rs.close();
        st.close();

    }
}
