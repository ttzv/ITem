package com.ttzv.itmg.pwSafe;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class Crypt {

    private String hExt;
    private String hExtK;
    private Path pwPath;
    private SecretKeySpec key;
    private String name;

    public Crypt(String name){

        this.hExt = ".dat";
        this.hExtK = ".kdat";
        this.pwPath = FileSystems.getDefault().getPath("cfg");
        this.name = name;

    }

    public boolean exists () {
        Path r = pwPath.resolve(name + hExt);
        Path rk = pwPath.resolve(name + hExtK);

        return Files.exists(r) && Files.exists(rk);
    }

    public boolean erase() throws IOException {
        if(exists()){
            Files.delete(pwPath.resolve(name + hExt));
            Files.delete(pwPath.resolve(name + hExtK));
            return true;
        }
        return false;
    }

    private void init(String p) throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] ps = p.toCharArray();
        byte[] salt = new byte[1024];
        Random r = new Random();
        r.nextBytes(salt);
        int iterationCount = 40000;
        int keyLength = 256;

        key = createSecretKey(ps,
                salt, iterationCount, keyLength);
    }


    public void safeStore(String p) throws IOException, GeneralSecurityException {
        init(p);
        //pass
        Path r = pwPath.resolve(name + hExt);
        if(!Files.exists(r)) {
            Files.createFile(r);
        }
        FileOutputStream writer = new FileOutputStream(r.toFile());
        writer.write(encrypt(p,key).getBytes());
        writer.close();
        //key
        r = pwPath.resolve(name + hExtK);
        if(!Files.exists(r)) {
            Files.createFile(r);
        }
        writer = new FileOutputStream(r.toFile());
        writer.write(key.getEncoded());
        writer.close();
    }

    public char[] read() throws IOException, GeneralSecurityException {
        //pass
        String pass = "";
        Path r = pwPath.resolve(name + hExt);
        if(Files.exists(r)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(r.toFile())));
            pass = reader.readLine();
            reader.close();
        }
        //key
        byte[] key = {};
        r = pwPath.resolve(name + hExtK);
        if(Files.exists(r)){
            key = Files.readAllBytes(r);
        }

        return decrypt(pass, new SecretKeySpec(key, "AES")).toCharArray();
    }


    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }

}
