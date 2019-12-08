package com.ttzv.itemBootstrap;

import org.update4j.Configuration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Bootstrap {

    public static final String CONFIG_PATH = "business/config/config.xml";

    public static void main(String[] args) throws IOException {

        BootConfig.getInstance().init(null);
        BootConfig.getInstance().saveFile();



        if(!Files.exists(Paths.get(CONFIG_PATH))) {
            Files.createDirectories(Paths.get(CONFIG_PATH).getParent());
        }

        Configuration config = null;

        try (InputStream in = new URL(BootConfig.getInstance().retrieveProp(BootConfig.CONFIG_URL)).openStream()) {
            config = Configuration.read(new InputStreamReader(in));

            //saving xml for offline mode
            String xml = config.toString();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CONFIG_PATH));
            writer.write(xml);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            try {   //if offline or config doesn't exist
                System.err.println("You are offline or URL is invalid. Reading latest config from:\n" + CONFIG_PATH);
                config = Configuration.read(Files.newBufferedReader(Paths.get(CONFIG_PATH)));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("You are offline and local configuration file does not exist or given url's/paths are invalid. Please connect to internet and run this application again.");
            }
        }

        config.update();

        config.launch();

    }

}
