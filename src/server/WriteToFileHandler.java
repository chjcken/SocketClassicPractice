package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WriteToFileHandler implements Runnable{
	private BlockingQueue<String> bq;
	private FileWriter fw;
    private BufferedWriter bw;
	/* what will this runnable do?
	 * it's a consumer of queue
	 * it take code in queue if available and write to one file named result.txt
	 * */
	public WriteToFileHandler(BlockingQueue<String> bq) {
		this.bq = bq;
		File file = new File("result/result.txt");
		try {
			if (!file.exists())
                file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
	        bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("error when prepare file to write");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (bq == null){
			System.err.println("Blocking Queue is not initilized yet");
			return;
		}
		String msg;
		
		try {
			while (true){
				msg = bq.take();
				//System.out.print(msg);
				bw.write(msg);
				bw.flush();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
//	private void closeFile(){
//		try {
//			if (bw != null)			
//				bw.close();
//			if (fw != null)
//	            fw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//	}

}
