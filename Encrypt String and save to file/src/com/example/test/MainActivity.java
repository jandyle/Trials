package com.example.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity{
private String encryptedFileName = "Enc_File.txt";
private static String algorithm = "AES";
static SecretKey yourKey = null;
TextView txt1,txt2;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    txt1 = (TextView) findViewById(R.id.txtView1);
    txt2 = (TextView) findViewById(R.id.txtView2);
    saveFile("Hello From CoderzHeaven asaksjalksjals");
    decodeFile(); 

}
 
public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    final int iterations = 1000; 

    // Generate a 256-bit key
    final int outputKeyLength = 256;

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    return secretKey;
}

public static SecretKey generateKey() throws NoSuchAlgorithmException {
    // Generate a 256-bit key
    final int outputKeyLength = 256;
    SecureRandom secureRandom = new SecureRandom();
    // Do *not* seed secureRandom! Automatically seeded from system entropy.
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(outputKeyLength, secureRandom);
    yourKey = keyGenerator.generateKey();
    return yourKey;
}

public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {
    byte[] encrypted = null;
    byte[] data = yourKey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
            algorithm);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
            new byte[cipher.getBlockSize()]));
    encrypted = cipher.doFinal(fileData);
    return encrypted;
}

public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {
    byte[] decrypted = null;
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(
            new byte[cipher.getBlockSize()]));
    decrypted = cipher.doFinal(fileData);
    return decrypted;
}

void saveFile(String stringToSave) {
    try {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator, encryptedFileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file));
        yourKey = generateKey();
        byte[] filesBytes = encodeFile(yourKey, stringToSave.getBytes());
        
        bos.write(filesBytes);
        bos.flush();
        bos.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

void decodeFile() {

    try {
        byte[] decodedData = decodeFile(yourKey, readFile());
        String str = new String(decodedData);
        txt1.setText("UNDECODED FILE CONTENTS : " + readFile());
        txt2.setText("DECODED FILE CONTENTS : " + str);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public byte[] readFile() {
    byte[] contents = null;

    File file = new File(Environment.getExternalStorageDirectory()
            + File.separator, encryptedFileName);
    int size = (int) file.length();
    contents = new byte[size];
    try {
        BufferedInputStream buf = new BufferedInputStream(
                new FileInputStream(file));
        try {
            buf.read(contents);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return contents;
}

}