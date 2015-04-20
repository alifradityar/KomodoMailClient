/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eccDS;

import ecc.ECPoint;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.util.Random;
import sha1.SHA1;

/**
 *
 * @author Ahmad
 */
public class Sender {
    
    // the curve and all stuffs behind it
    private ECCDS eccds;
    
    // private key
    private BigInteger d;
    
    public Sender(ECCDS eccds){
        this.eccds = eccds;
    }
    
    public void generatePrivateKey(){
        Random rnd = new Random();
        do {
            d = new BigInteger(eccds.getOrder().bitLength(), rnd);
        }while (d.compareTo(eccds.getOrder().subtract(new BigInteger("1"))) >= 0);
    }
    
    public ECPoint getBasePoint(){
        return new ECPoint(eccds.getBaseX(),eccds.getBaseY());
    }
    
    public ECPoint generatePublicKey(){
        return eccds.getCurve().multiply(getBasePoint(), d);
    }
    
    public BigInteger[] generateSignature(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        // Generate a hash message here
        SHA1 sha = new SHA1();
        sha.insertBytes(message.getBytes());
        sha.calculateHash();
        int a[] = sha.getHash();
        
        
        byte[] bytesofmessage = message.getBytes("UTF-8");
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesofmessage);
        BigInteger hashed = new BigInteger(thedigest);
        
        
        // Signature parameter r and s
        BigInteger s = null, r = null, k = null;
        
        Random rnd = new Random();
        do{
            do{
                do{
                    k = new BigInteger(eccds.getOrder().bitLength(), rnd);
                }while(k.compareTo(eccds.getOrder().subtract(new BigInteger("1"))) >= 0);
                
                ECPoint kP = eccds.getCurve().multiply(generatePublicKey(), k);
                BigInteger x1 = kP.x;
                BigInteger y1 = kP.y;
                System.out.println("x1: "+ x1 + "\ny1: " + y1);
                r = x1.mod(eccds.getOrder());
            
            }while(r.equals(BigInteger.ZERO));
            
            BigInteger kInv = k.modInverse(eccds.getOrder());
            s = (hashed.add(d.multiply(r)));
            s = kInv.multiply(s).mod(eccds.getOrder());
            System.out.println("nilai lama s: "+ s);
            
            //s = k.modInverse(k)
        }while(s.equals(BigInteger.ZERO));
        
        BigInteger[] rs = new BigInteger[2];
        rs[0] = r;
        rs[1] = s;
        
        return rs;
    }
}
