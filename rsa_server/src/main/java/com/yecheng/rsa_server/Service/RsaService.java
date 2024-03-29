package com.yecheng.rsa_server.Service;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletInputStream;

import org.springframework.stereotype.Service;

@Service
public class RsaService {
    
    //解密使用自己私钥
    public String doDecryption(InputStream input) throws IOException {
        //读取输入内容
        StringBuilder content = new StringBuilder();
        byte[] b = new byte[1024];
        int len = -1;
        while ( (len = input.read(b)) > 0) {
            content.append(new String(b, 0, len));
        }
        String c = content.toString();
        if(c.length() == 0) return "";

        //读取私钥
        BufferedReader keyReader = new BufferedReader(new FileReader("/root/code/docker_compose/rsa/client/privateKey.txt"));
        BigInteger n = new BigInteger(keyReader.readLine());
        keyReader.readLine();
        BigInteger privateKey = new BigInteger(keyReader.readLine());

        //解密数据
        return doDecryption(c, privateKey, n);

    }
    
    //加密使用对方公钥
    public void doEncryption(ServletInputStream input, OutputStream output) throws IOException {
        
        //读取输入内容
        StringBuilder content = new StringBuilder();
        byte[] b = new byte[1024];
        int len = -1;
        while ( (len = input.read(b)) > 0) {
            content.append(new String(b, 0, len));
        }
        String plaint = content.toString();
        if(plaint.length() == 0) return;

        //读取公钥
        BufferedReader reader = new BufferedReader(new FileReader("/root/code/docker_compose/rsa/server/publicKey.txt"));
        BigInteger n = new BigInteger(reader.readLine());
        BigInteger publicKey = new BigInteger(reader.readLine());
        reader.close();

        //加密数据
        String c = doEncryption(plaint, publicKey, n);

        //写数据
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        writer.write(c);
        writer.close();
    } 

    public List<BigInteger> getKeys(BigInteger value) {
        List<BigInteger> keys = new ArrayList<>();

        while(true) {
           BigInteger s0 = BigInteger.ONE;
           BigInteger s1 = BigInteger.ZERO;
           BigInteger t0 = BigInteger.ZERO;
           BigInteger t1 = BigInteger.ONE;

           BigInteger kn = new BigInteger(value.toString());

           int randomBit = 80 + (int)(Math.random() * 40);
           BigInteger e = kn.shiftRight(randomBit);
           BigInteger result = new BigInteger(e.toString());
           BigInteger r = null;

           BigInteger q = null;
           while(!e.equals(BigInteger.ZERO)) {
               r = kn.mod(e);
               q = kn.divide(e);
               
               kn = e;
               e = r;

               BigInteger si1 = s0.subtract(q.multiply(s1));
               BigInteger ti1 = t0.subtract(q.multiply(t1));

               s0 = s1; t0 = t1;
               s1 = si1; t1 = ti1;
           }

           if (kn.equals(BigInteger.ONE)) {
               keys.add(result);
               keys.add(t0);
               return keys;
           }
        }
    }
    
    private String doEncryption(String plaint, BigInteger publicKey,  BigInteger n) {
        StringBuilder builder = new StringBuilder();
        List<BigInteger> cList = new LinkedList<>();
        for(int i = 0; i < plaint.length(); i = i + 2) {
            //将明文分成两个字符为一组
            char a = plaint.charAt(i);
        
            char b;
            BigInteger m = null;
            if(i + 1 < plaint.length()){
                b = plaint.charAt(i + 1);
                m = new BigInteger( ( (((int)a) << 16 ) + b) + "");
            } else {
                m = new BigInteger((int)a + "");
            }
            
            
            //计算分组c = (m ^ e) mod n，使用临时数组保存
            BigInteger c = m.modPow(publicKey, n);
            cList.add(c);

            //计算c的字节数
            int cLength = c.toByteArray().length;

            builder.append((char)cLength);
        }   
        builder.append((char)(-1));

        for(int i = 0; i < cList.size(); ++ i) {
            byte[] theByte = cList.get(i).toByteArray();
            for (byte b : theByte) { 
                builder.append((char)b);
            }
        }
        
        return builder.toString();          
    }
   
    private String doDecryption(String cip, BigInteger privateKey, BigInteger n) {
        StringBuilder result = new StringBuilder();

        //保存所有分组的长度
        char[] cipChar = cip.toCharArray();
        List<Integer> cLengths = new LinkedList<>();
        
        int index = 0;
        while(true) {
            char cByte = cipChar[index];
            if (cByte == 65535) {
                break;
            }
            cLengths.add((int)cByte);
            ++ index;
        }
        ++ index;

        //读取对应分组字节，解密
        for(int i = 0; i < cLengths.size(); ++ i) {
            int size = cLengths.get(i);

            int j = 0;
            char[] cGroup = new char[size];
            
            //读取对应分组字节
            int end = index + size;
            while(index < end) {
                cGroup[j++] = cipChar[index++];
            }

            byte[] cByte = new byte[size];
            for(int k = 0; k < size ; ++ k) {  
               cByte[k] = (byte)cGroup[k];
            }

            BigInteger c = new BigInteger(cByte);

            //解密
            BigInteger m = c.modPow(privateKey, n);
            int mInt = Integer.parseInt(m.toString());

            if(m.toString().length() <= 1) {
                result.append((char)mInt);
            } else {
                result.append((char)(mInt >> 16));
                result.append((char)mInt);
            }        
        }
        
        return result.toString();
    }
}
