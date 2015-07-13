/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rest.services;

import com.demo.utils.Utility;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author hoaibao
 */
@Path("/login")
public class LoginService {

    // HTTP Get Method
    @GET
    // Path: http://localhost/<appln-folder-name>/login/dologin
    @Path("/dologin")
    // Produces JSON as response
    @Produces(MediaType.APPLICATION_JSON) 
    // Query parameters are parameters: http://localhost/<appln-folder-name>/webresources/login/dologin?username=abc&password=xyz
    public String doLogin(@QueryParam("username") String uname, @QueryParam("password") String pwd){
        String response = "";
        if(checkCredentials(uname, pwd)){
            response = Utility.constructJSON("login",true);
        }else{
            response = Utility.constructJSON("login", false, "Incorrect Email or Password");
        }
    return response;        
    }
 
    /**
     * Method to check whether the entered credential is valid
     * 
     * @param uname
     * @param pwd
     * @return
     */
    private boolean checkCredentials(String uname, String pwd){
        System.out.println("Inside checkCredentials");
        boolean result = false;
        if(Utility.isNotNull(uname) && Utility.isNotNull(pwd)){
            try {// connect db 
               if(uname.equals("admin") && pwd.equals("admin"))
                   result = true;
            } catch (Exception e) {             
                result = false;
            }
        }else{
            result = false;
        }
 
        return result;
    }
}
