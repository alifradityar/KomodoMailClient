/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eccDS;

import ecc.ECC;
import ecc.ECPoint;
import ecc.EllipticCurve;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ahmad
 */

public class ECCDS {
    // stands for primeNumber
    private BigInteger p;
    
    // stands for parameter of curve
    private BigInteger a;
    private BigInteger b;
    
    // stands for base of encryption
    private BigInteger baseX;
    private BigInteger baseY;
    
    // stands for order of curve
    private BigInteger order;
    
    // The curve
    private EllipticCurve curve;
    
    public ECCDS(BigInteger p, BigInteger a, BigInteger b, BigInteger baseX, BigInteger baseY, BigInteger order){
        this.p = p;
        this.a = a;
        this.b = b;
        this.baseX = baseX;
        this.baseY = baseY;
        this.order = order;
        
        curve = new EllipticCurve(a, b, p);
    }
    
    /**
     * @return the p
     */
    public BigInteger getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(BigInteger p) {
        this.p = p;
    }

    /**
     * @return the a
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(BigInteger a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public BigInteger getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(BigInteger b) {
        this.b = b;
    }

    /**
     * @return the baseX
     */
    public BigInteger getBaseX() {
        return baseX;
    }

    /**
     * @param baseX the baseX to set
     */
    public void setBaseX(BigInteger baseX) {
        this.baseX = baseX;
    }

    /**
     * @return the baseY
     */
    public BigInteger getBaseY() {
        return baseY;
    }

    /**
     * @param baseY the baseY to set
     */
    public void setBaseY(BigInteger baseY) {
        this.baseY = baseY;
    }

    /**
     * @return the order
     */
    public BigInteger getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(BigInteger order) {
        this.order = order;
    }

    /**
     * @return the curve
     */
    public EllipticCurve getCurve() {
        return curve;
    }

    /**
     * @param curve the curve to set
     */
    public void setCurve(EllipticCurve curve) {
        this.curve = curve;
    }
    
    
    public static void main(String args[]) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger a = new BigInteger("6277101735386680763835789423207666416083908700390324961276");
        BigInteger b = new BigInteger("2455155546008943817740293915197451784769108058161191238065");
        BigInteger baseX = new BigInteger("602046282375688656758213480587526111916698976636884684818");
        BigInteger baseY = new BigInteger("174050332293622031404857552280219410364023488927386650641");
        BigInteger order = new BigInteger("6277101735386680763835789423176059013767194773182842284081");   
        
        
        ECCDS eccds = new ECCDS(p, a, b, baseX, baseY, order);
        
        Sender sender = new Sender(eccds);
        sender.generatePrivateKey();
        
        // value for encription
        BigInteger[] message = sender.generateSignature("a");
        ECPoint Q = sender.generatePublicKey();
        ECPoint P = sender.getBasePoint();
        
        System.out.println(message[0] + "\n" + message[1]);
        // value for validation
        Receiver receiver = new Receiver(eccds, baseX, message, Q, P);
        receiver.signatureVerification();
    }
    
}
