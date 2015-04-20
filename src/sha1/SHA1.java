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
public class SHA1 {

        
    private long messageLength;
    
    private final int H[] = new int[5];
    
    private final byte[] messageChunk = new byte[64];
    private int messageChunkLength;
    
    public SHA1()
    {
        H[0] = 0x67452301;
        H[1] = 0xefcdab89;
        H[2] = 0x98badcfe;
        H[3] = 0x10325476;
        H[4] = 0xc3d2e1f0;
        
        messageChunkLength = 0;
        messageLength = 0;
    }
    
    public void reset()
    {
        H[0] = 0x67452301;
        H[1] = 0xefcdab89;
        H[2] = 0x98badcfe;
        H[3] = 0x10325476;
        H[4] = 0xc3d2e1f0;
        
        messageChunkLength = 0;
        messageLength = 0;
    }
    
    public void insertBytes(byte[] input)
    {
        int i;
        for(i=0; i<input.length; i++)
        {
            messageChunk[messageChunkLength] = input[i];
            messageChunkLength++;
            messageLength = messageLength + 8;
            if (messageChunkLength == 64)
            {
                processBlock();
                messageChunkLength = 0;
            }
        }
    }
    
    public void calculateHash()
    {
        addPadding();
        processBlock();
        
    }
    
    public int[] getHash()
    {
        return H;
    }
    
    private int ROTL(int bits, int shift)
    {
        return (bits << shift | bits >>> (32 - shift));
    }
    
    private int toInt(byte[] bytes, int offset) {
        assert(offset < 64);
        int ret = 0;
        for (int i=offset; i< offset + 4; i++) 
        {
            ret <<= 8;
            ret |= (int)bytes[i] & 0xFF;
        }
        return ret;
    }
    
    private void processBlock()
    {
        //Prepare message schedule
        int W[] = new int[80];
        
        int i;
        for(i=0; i<80; i++)
        {
            if (i < 16)
            {
                W[i] = toInt(messageChunk, i * 4);
            } else {
                W[i] = ROTL(W[i-3] ^ W[i-8] ^ W[i-14] ^ W[i-16], 1);
                //int x = W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16];
                //W[i] = (x << 1 | x >>> 31);      
            }
        }
        
        int a = H[0];
        int b = H[1];
        int c = H[2];
        int d = H[3];
        int e = H[4];
        
        for (i=0; i<80; i++)
        {
            int k, f;
            
            if (i <= 19)
            {
                f = (b & c) | ((~b) & d);
                k = 0x5A827999;
            } else if ((20 <=i ) && (i <=39))
            {
                f = b ^ c ^ d;
                k = 0x6ED9EBA1;
            } else if ((40 <= i) && (i <= 59))
            {   
                f = (b & c) | (b & d) | (c & d);
                k = 0x8F1BBCDC;
            } else {
                f = b ^ c ^ d;
                k = 0xCA62C1D6;
            }
            
            int temp = (ROTL(a, 5)) + f + e + k + W[i];
            e = d;
            d = c;
            c = ROTL(b, 30);
            b = a;
            a = temp;
        }
        
        H[0] += a;
        H[1] += b;
        H[2] += c;
        H[3] += d;
        H[4] += e;
    }
    
    private void addPadding()
    {
        if(messageChunkLength > 56)
        {
            messageChunk[messageChunkLength] = (byte)0x80;
            messageChunkLength++;
            while (messageChunkLength < 64)
            {
                messageChunk[messageChunkLength] = 0x0;
                messageChunkLength++;
            }
            
            processBlock();
            messageChunkLength = 0;
            while (messageChunkLength < 56)
            {
                messageChunk[messageChunkLength] = 0x0;
                messageChunkLength++;
            }
        } else {
            messageChunk[messageChunkLength] = (byte) 0x80;
            messageChunkLength++;
            while(messageChunkLength < 56)
            {
                messageChunk[messageChunkLength] = 0;
                messageChunkLength++;
            }
        }
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 56) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 48) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 40) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 32) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 24) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 16) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) ((messageLength >> 8) & 0xFF);
        messageChunkLength++;
        
        messageChunk[messageChunkLength] = (byte) (messageLength & 0xFF);
        messageChunkLength++;
        
    }
    
}
