package com.ttzv.itmg.ad;

import com.ttzv.itmg.utility.Utility;

import java.text.ParseException;

public class AdMainTest {
    public static void main(String[] args) throws ParseException {
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
