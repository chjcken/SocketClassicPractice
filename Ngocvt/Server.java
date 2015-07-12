package com.oknion.Socket;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Dung newFixedThreadPool
 * 5 client, Server 5 thread
 * Không đồng bộ
 * Will be optimize soon
 */
public class Server {

	public static void main(String[] args) throws IOException {
		NetworkService server = new NetworkService(3533, 5);
		Thread thread = new Thread(server);
		thread.start();
		Thread thread1 = new Thread(new Client("localhost", 3533, 5));
		thread1.start();

	}

}

class NetworkService implements Runnable {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public NetworkService(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		pool = Executors.newCachedThreadPool();
	}

	public void run() { // run the service
		try {
			for (;;) {
				pool.execute(new Handler(serverSocket.accept()));
			}
		} catch (IOException ex) {
			pool.shutdown();
		}
	}

	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

}

class Handler implements Runnable {
	private final Socket socket;

	Handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		OutputStream out = null;
		InputStream in = null;
		DataOutputStream dataOut = null;
		DataInputStream dataIn = null;
		DataOutputStream outF = null;
		String s = "";
		try {

			in = socket.getInputStream();
			dataIn = new DataInputStream(in);
			outF = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(socket.getPort() + ".txt")));
			while (!s.equals("end")) {
				s = dataIn.readUTF();
				// System.out.println(s);
				outF.writeUTF(s);
			}
			outF.flush();
			System.err.println("Response to client");
			out = socket.getOutputStream();
			dataOut = new DataOutputStream(out);
			dataOut.writeUTF("complete!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				dataOut.close();
				dataIn.close();
				outF.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

class HandlerClient implements Runnable {
	private final Socket socket;

	public HandlerClient(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		OutputStream out = null;
		InputStream in = null;
		DataOutputStream dataOut = null;
		DataInputStream dataIn = null;
		int dem = 0;
		try {

			out = socket.getOutputStream();
			dataOut = new DataOutputStream(out);
			while (dem < 50) {
				dataOut.writeUTF("100char");
				dataOut.flush();
				dem++;
			}
			dataOut.writeUTF("end");
			in = socket.getInputStream();
			dataIn = new DataInputStream(in);
			try {
				while (true) {
					System.out.println("Send data " + dataIn.readUTF());
				}
			} catch (EOFException e) {
				System.out.println("Close connection!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				out.close();
				in.close();
				dataOut.close();
				dataIn.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}

class Client implements Runnable {

	private final ExecutorService pool;
	String host = null;
	int port;

	public Client(String host, int port, int nThreads) {
		pool = Executors.newFixedThreadPool(nThreads);
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Socket socket = null;
			try {
				socket = new Socket(host, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pool.execute(new HandlerClient(socket));
		}
		pool.shutdown();
		try {
			pool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("InterruptedException");
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(
				"Avg time for 100 client to send 50 lines of data to server :" + (end - start) / 100 + "milliseconds");
	}

}
