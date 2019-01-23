package file;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Loader {

    private BufferedReader bufferedReader;

    public Loader(){

    }

    public Loader(File file){
        load(file);
    }

    public void load (File file){
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file.getPath()), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };

    public StringBuilder readContent() {
        StringBuilder stringBuilder = new StringBuilder();
        String stringLine;
        try {
            while ((stringLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(stringLine);
            }
            return stringBuilder;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
