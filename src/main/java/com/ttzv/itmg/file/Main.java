package com.ttzv.itmg.file;

import com.ttzv.itmg.db.PgStatement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:","Lista.txt");
        System.out.println(path);
        File file = new File(path.toString());
        System.out.println(file.exists());

        /*
        File[] files = file.listFiles();
        System.out.println(Arrays.asList(files));
        */
        Loader loader = new Loader();

        ArrayList<String[]> arrayList = new ArrayList<>();
        loader.load(file);
        loader.contentToArray().forEach(s -> arrayList.add(s.split(";")));

        /*if (strings.length <= 3) {
            System.out.println(strings[0] + " " + strings[1] + " " + strings[2]);
        } else {
            System.out.println(strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3]);
        }*/
        arrayList.forEach(Main::genStatement);



        /*String stosave = "Once upon a time\n" +
                "Some weird shit happened\n\n" +
                "and i had to test if this stream writer actually works";
        Saver saver = new Saver(stosave);
        saver.setExtension(saver.HTML);
        try {
            saver.saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    public static void genStatement(String[] values){
        String query = "UPDATE users SET ";
        int cnt = 1;
        while (cnt < values.length){
            if(cnt == 1){
                query = query.concat("position=" + PgStatement.apostrophied(values[cnt]));
            }
            if(cnt == 2){
                query = query.concat(", usermphone=" + PgStatement.apostrophied(values[cnt]));
            }
            if (cnt == 3){
                query = query.concat(", userphone=" + PgStatement.apostrophied(values[cnt]) + " ");
            }

            cnt++;
            //System.out.println(cnt);
        }

        query = query.concat(" WHERE displayname=" + PgStatement.apostrophied(values[0]) + ";");
        if(values.length>=3)
        System.out.println(query);
    }
}
