package inputsystem;

import java.net.InetAddress;
import java.net.UnknownHostException;

import inputsystem.io.FaceInputThread;

public class InputSystem {

	public static void main(String[] args) {
		
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			int port = 1932;
			
			FaceInputThread inputThread = new FaceInputThread(address, port);
			
			inputThread.start();
			
			try {
				inputThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Bye");

	}

}
