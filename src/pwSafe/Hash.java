package pwSafe;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Hash {

    private char[] password;
    private String hExt;
    private Path hashPath;

    public Hash(char[] password) {
        this.hExt = ".jb";
        this.hashPath = FileSystems.getDefault().getPath("cfg");
        this.password = password;
    }

    public void writeHash(String fn) throws IOException {
        Path r = hashPath.resolve(fn + hExt);
        if(!Files.exists(r)) {
            Files.createFile(r);
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(r.toFile())));
        writer.write(BCrypt.hashpw(Arrays.toString(password) , BCrypt.gensalt() ) );
        writer.close();
    }

    public char[] readHash(String fn) throws IOException {
        Path r = hashPath.resolve(fn + hExt);
        if(Files.exists(r)){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(r.toFile())));
            return bufferedReader.readLine().toCharArray();
        }
        return null;
    }
}
