package application;

import blockcipher.BlockCipher;
import ecc.ECPoint;
import eccDS.ECCDS;
import eccDS.Receiver;
import eccDS.Sender;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ahmad
 */
public class EmailController {
    
    // Final paramater, yet
    BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
    BigInteger a = new BigInteger("6277101735386680763835789423207666416083908700390324961276");
    BigInteger b = new BigInteger("2455155546008943817740293915197451784769108058161191238065");
    BigInteger baseX = new BigInteger("602046282375688656758213480587526111916698976636884684818");
    BigInteger baseY = new BigInteger("174050332293622031404857552280219410364023488927386650641");
    BigInteger order = new BigInteger("6277101735386680763835789423176059013767194773182842284081");   

    ECCDS eccds = new ECCDS(p, a, b, baseX, baseY, order);
    
    public EmailController(){
    	
    }
    
    // Key will be automatically generated and will be saved to the cipPass.txt
    public String encryptButton(String message){
        byte[] plaintext = new byte[1048576];
        byte[] key = new byte[32];
        
        Random random = new Random(System.currentTimeMillis());
        random.nextBytes(key);
        saveFile("cipPass.txt", new String(key));
        
        plaintext = message.getBytes();
        
        BlockCipher bc = new BlockCipher();
        bc.mode = 1;
        
        byte[] crypt = bc.encrypt(plaintext, key);
        return new String(crypt);
    }
    
    public String decryptButton(String cipherText) throws FileNotFoundException{
        BlockCipher bc = new BlockCipher();
        bc.mode = 1;
        
        byte[] key = new byte[32];
        key = readFile("cipPass.txt").getBytes();
        
        byte[] plainText = bc.decrypt(cipherText.getBytes(), key);
        
        return new String(plainText);
    }
    
    // return array of String, String[0] = value of r, String[1] = value of s
    public String[] generateSignatureButton(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        
        Sender sender = new Sender(eccds);
        sender.generatePrivateKey();
        
        // value for encription
        BigInteger[] bigSignature = sender.generateSignature(message);
        
        String[] stringSignature = new String[2];
        stringSignature[0] = bigSignature[0].toString();
        stringSignature[1] = bigSignature[1].toString();
        
        return stringSignature;
    }
    
    public boolean verifyButton(String[] hashedMessage){
        Sender sender = new Sender(eccds);
        sender.generatePrivateKey();
        ECPoint Q = sender.generatePublicKey();
        ECPoint P = sender.getBasePoint();
        
        BigInteger[] message = new BigInteger[2];
        message[0] = new BigInteger(hashedMessage[0]);
        message[1] = new BigInteger(hashedMessage[1]);
        
        // value for validation
        Receiver receiver = new Receiver(eccds, baseX, message, Q, P);
        
        return receiver.signatureVerification();
    }
    
    public static void saveFile(String FileName, String Content){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(FileName, "UTF-8");
            writer.println(Content);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    public static String readFile(String FileName) throws FileNotFoundException{
        try(BufferedReader br = new BufferedReader(new FileReader(FileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        }
        catch(Exception e){
            return null;
        }
    }
    
//    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException{
//        EmailController controller = new EmailController();
//        
//        String[] val = controller.generateSignatureButton("ahmad shahab ganteng");
//        System.out.println(Arrays.toString(val));
//        
//        String[] ah = new String[2];
//        ah[0]= "123"; ah[1] = "44";
//        if(controller.verifyButton(ah)){
//            System.out.println("verified");
//        }
//        else{
//            System.out.println("no");
//        }
//    }
}
