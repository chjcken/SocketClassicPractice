package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User {
	private String NEWLINE = "\n";
	private String username;
	private FileWriter fileWriter = null;
	private BufferedWriter bufferWriter = null;
	private boolean isOK = true;
	private int numOfCode = 0;
	
	public User(String username){
		if (username == null || username.equals("")){
			isOK = false;
			return;
		}
			
		this.username = username;
		File file = new File("result/" + username + ".txt");
		
		
		try {
			if (!file.exists())
				file.createNewFile();
			fileWriter = new FileWriter(file);
			bufferWriter = new BufferedWriter(fileWriter);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error when create user io: " + e.getMessage());
			isOK = false;
		}
		
	}
	
	public void writeCodeToFile(String code){
		try {
			bufferWriter.write(code + NEWLINE);
			numOfCode++;
		} catch (IOException e) {
			isOK = false;
		}
	}
	
	
	public void closeWriter(){
		
		try {
			if (bufferWriter != null){
				bufferWriter.flush();
				bufferWriter.close();
			}
			
			if (fileWriter != null)
				fileWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isOK(){
		return isOK;
	}

	public String getUsername() {
		return username;
	}
	
	public int getNumOfCode(){
		return numOfCode;
	}

}
