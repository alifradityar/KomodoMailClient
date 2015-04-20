/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eccDS;

import ecc.ECPoint;
import java.math.BigInteger;

/**
 *
 * @author Ahmad
 */
public class Receiver {
    
    // the curve and stuffs behind it
    private ECCDS eccds;
    
    // the already hashed message
    private BigInteger hashedMessage;
    
    // the sender signatures
    private BigInteger[] rs;
    
    // the sender public key
    private ECPoint Q;
    
    // the base point
    private ECPoint P;
    
    public Receiver(ECCDS eccds, BigInteger hashedMessage, BigInteger[] rs, ECPoint Q, ECPoint P){
        this.eccds = eccds;
        this.hashedMessage = hashedMessage;
        this.rs = rs;
        this.Q = Q;
        this.P = P;
    }

    public boolean signatureVerification(){
        
        // check value of r and s are in 1 , n-1
        
        BigInteger w = (rs[1].modInverse(eccds.getOrder()));
        
        //GANTII
        hashedMessage = new BigInteger("123123");
        
        BigInteger u1 = (hashedMessage.multiply(w)).mod(eccds.getOrder());
        BigInteger u2 = (rs[0].multiply(w)).mod(eccds.getOrder());
        
        ECPoint checked = eccds.getCurve().add(eccds.getCurve().multiply(P, u1), eccds.getCurve().multiply(Q, u2));
        
        BigInteger v = checked.x.mod(eccds.getOrder());
        
        System.out.println("v : " + v);
        System.out.println("rs[0] : " + rs[0]);
        
        if(v.equals(rs[0])){
            System.out.println("verified");
            return true;
        }
        else{
            System.out.println("unverified");
            return false;
        }
    }
    
    /**
     * @return the eccds
     */
    public ECCDS getEccds() {
        return eccds;
    }

    /**
     * @param eccds the eccds to set
     */
    public void setEccds(ECCDS eccds) {
        this.eccds = eccds;
    }

    /**
     * @return the hashedMessage
     */
    public BigInteger getHashedMessage() {
        return hashedMessage;
    }

    /**
     * @param hashedMessage the hashedMessage to set
     */
    public void setHashedMessage(BigInteger hashedMessage) {
        this.hashedMessage = hashedMessage;
    }

    /**
     * @return the rs
     */
    public BigInteger[] getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(BigInteger[] rs) {
        this.rs = rs;
    }
    
}
