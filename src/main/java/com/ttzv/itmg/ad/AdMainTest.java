package com.ttzv.itmg.ad;

import com.ttzv.itmg.db.PgStatement;

import java.util.Arrays;

public class AdMainTest {
    public static void main(String[] args) {
        User user = new User("guid" ,
                                    "name" ,
                                    "random" ,
                                    "a" ,
                                    "s" ,
                                    "d" ,
                                    "f" ,
                                    "g" ,
                                    "h" ,
                                    "j" ,
                                    "1" ,
                                    "2" ,
                                    "G");

        System.out.println(user.getUserGUID());

        System.out.println(Arrays.asList(user.getComplete()));

        System.out.println(Arrays.asList(PgStatement.apostrophied(UserData.getAllColumns())));
    }
}
