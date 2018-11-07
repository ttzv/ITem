package file;

import java.io.*;

public class Loader {

    private BufferedReader bufferedReader;

    public Loader(File file){
        try {
            bufferedReader = new BufferedReader(new FileReader(file.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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
