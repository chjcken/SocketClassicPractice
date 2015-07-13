/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import test.Client;

/**
 *
 * @author hoaibao
 */
@Path("/upload")
public class FileUploadDownloadService {
     public static final String FILE_PATH = "/home/hoaibao/kq.txt";
     
    @POST
    @Path("/uploadfile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
     @Produces("text/plain")  
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws InterruptedException {
        String fileLocation = "/home/hoaibao/" + fileDetail.getFileName();
        //saving file  
        try {
            FileOutputStream out = new FileOutputStream(new File(fileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(new File(fileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f  = new File("/home/hoaibao/client.txt");
        if(!f.exists())
           return Response.status(Status.NOT_FOUND).build();
        
        int kq = Client.RunClient();
       if(kq==0)
            return Response.status(Status.NOT_FOUND).build();
        File file = new File(FILE_PATH);  
        Response.ResponseBuilder response = Response.ok((Object) file);  
        response.header("Content-Disposition","attachment; filename=\"KETQUA.txt\"");  
        return response.build();  
    }
    
}
