package inputsystem.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class FaceOutputThread extends Thread {

	private InetAddress address;
	private int port;
	
	public FaceOutputThread(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		super.run();
		
		Socket socket = null;
		
		try {
			socket = new Socket(address, port);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			
		} catch(IOException ex) {
			
		}
		
		System.out.println("reader thread exited");
	}

}
