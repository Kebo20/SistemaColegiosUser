/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.UnsupportedEncodingException;
import org.eclipse.persistence.internal.oxm.conversion.Base64;


/**
 *
 * @author kevin
 */
public class Base64ED {

    public Base64ED() {
    }
    
    
    public static String encriptar(String s) throws UnsupportedEncodingException{
//        return Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
          byte[] encode =Base64.base64Encode(s.getBytes("utf-8"));
//         return  encode;
         return new String(encode, "utf-8");
    }
    
    public static String desencriptar(String s) throws UnsupportedEncodingException{
//        byte[] decode = Base64.getDecoder().decode(s.getBytes());
         byte[] decode = Base64.base64Decode(s.getBytes());
        return new String(decode, "utf-8");
    }
   
}
