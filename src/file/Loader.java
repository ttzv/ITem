package file;

import java.io.*;

public class Loader {

    private BufferedReader bufferedReader;

    public Loader(String path) throws IOException {
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public StringBuilder readContent() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String stringLine;
        while ((stringLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(stringLine);
        }
        return stringBuilder;
    }
}
