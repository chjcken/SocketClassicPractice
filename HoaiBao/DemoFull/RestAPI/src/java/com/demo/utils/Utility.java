/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author hoaibao
 */
public class Utility {

    public static boolean isNotNull(String txt) {
        // System.out.println("Inside isNotNull");
        return txt != null && txt.trim().length() >= 0 ? true : false;
    }

    public static String constructJSON(String tag, boolean status) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("tag", tag);
            obj.put("status", new Boolean(status));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        }
        return obj.toString();
    }

    public static String constructJSON(String tag, boolean status, String err_msg) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("tag", tag);
            obj.put("status", new Boolean(status));
            obj.put("error_msg", err_msg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        }
        return obj.toString();
    }

    public static List<String> ReadFileClient() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        List<String> strs = new ArrayList<String>();
        FileInputStream fis = new FileInputStream("/home/hoaibao/client.txt");
        InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(reader);
        String s;
        do {
            s = null;
            s = br.readLine();
            System.out.println(s);
            strs.add(s);
        } while (s != null);
        br.close();
        return strs;
    }

    public static boolean WriteFileResult(List<String> results, String filePath) {
        Writer writer = null;
        try {
            // Using OutputStreamWriter you don't have to convert the String to byte[]
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            for (String line : results) {
                line += System.getProperty("line.separator");
                writer.write(line);
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
}
