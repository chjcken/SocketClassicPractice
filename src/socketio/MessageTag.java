/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketio;

import java.util.Random;

/**
 *
 * @author datbt
 */
public class MessageTag {
    public static String HELLO = "HELLO";
    public static String USERNAME = "USERNAME";
    public static String ERROR = "ERROR";
    public static String NEXT = "NEXT";
    public static String STOP = "STOP";
    public static String OK = "OK";
    //public static String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static String generateCode(){
    	String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rand = new Random();
		String s = "";
		for (int i = 0; i<99; ++i){
			s += ALPHABET.charAt(rand.nextInt(100)%52);
		}
		
		return s;
	}
}
