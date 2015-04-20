/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sha1;

/**
 *
 * @author Fahziar
 */

import ecc.ECPoint;
import ecc.EllipticCurve;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class Main {
    
    public static BigInteger toBigInteger(int[] data) {
        byte[] array = new byte[data.length * 4];
        ByteBuffer bbuf = ByteBuffer.wrap(array);
        IntBuffer ibuf = bbuf.asIntBuffer();
        ibuf.put(data);
        return new BigInteger(array);
    }
    
//    
//    public static void main(String args[]) throws Exception
//    {
//        String input = "abcdefasdsadasdasdasdadasdaasdsadasdasds";
//               
//        System.out.println(input.getBytes().length);
//        
//        SHA1 sha1 = new SHA1();
//        sha1.insertBytes(input.getBytes());
//        sha1.calculateHash();
//        int[] out = sha1.getHash();
//        
//        System.out.format("%d%d%d%d%d\n", out[0], out[1], out[2], out[3], out[4]);
//    }
}
