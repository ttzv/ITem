package com.ttzv.itmg.ad;

import com.ttzv.itmg.db.PgStatement;

import java.util.Arrays;

public class AdMainTest {
    public static void main(String[] args) {
        User user = new User("object_1" ,
                                    "object_2" ,
                                    "object_3" ,
                                    "object_4" ,
                                    "object_5" ,
                                    "object_6" ,
                                    "object_7" ,
                                    "object_8");

        System.out.println(user.getUserGUID());

        System.out.println(Arrays.asList(user.getComplete()));

        System.out.println(Arrays.asList(PgStatement.apostrophied(UserData.getAllColumns())));
    }
}
