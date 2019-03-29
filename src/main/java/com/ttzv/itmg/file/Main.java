package com.ttzv.itmg.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:","KONTAKTY");
        System.out.println(path);
        File file = new File(path.toString());
        System.out.println(file.exists());

        File[] files = file.listFiles();
        System.out.println(Arrays.asList(files));
        Loader loader = new Loader();

        for (File f:files) {
            loader.load(f);
            System.out.println(loader.contentToArray().get(6).split(":")[1]);
        }
    }
}
