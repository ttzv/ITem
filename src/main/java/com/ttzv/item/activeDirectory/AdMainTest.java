package com.ttzv.item.activeDirectory;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.utility.Utility;

import java.io.IOException;
import java.text.ParseException;

public class AdMainTest {
    public static void main(String[] args) throws ParseException {

        try {
            Cfg.getInstance().init(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDaoLdapImpl userDAO = new UserDaoLdapImpl();
        userDAO.getResults();
/*        User user = new User("object_1" ,
                                    "object_2" ,
                                    "object_3" ,
                                    "object_4" ,
                                    "object_5" ,
                                    "object_6" ,
                                    "object_7" ,
                                    "object_8");

        System.out.println(user.getUserGUID());

        System.out.println(Arrays.asList(user.getComplete()));

        System.out.println(Arrays.asList(PgStatement.apostrophied(UserData.getAllColumns())));*/

    String date = "20150930135426";
    String rdate = "20160107091549.0Z";


        System.out.println(Utility.ldapStringToDate(date));
    }
}
