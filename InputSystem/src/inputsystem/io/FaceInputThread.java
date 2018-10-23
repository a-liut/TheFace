package inputsystem.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class FaceInputThread extends Thread
{
	private InetAddress address;
	private int port;
	
	public FaceInputThread(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		super.run();
		
		System.out.println("Input thread starts");
		
		Socket inSocket = null, outSocket = null;
		
		try {
			inSocket = new Socket(address, port);
			outSocket = new Socket(address, port);
			
			DataOutputStream out = new DataOutputStream(outSocket.getOutputStream());
			ParsedInputStream in = new ParsedInputStream(inSocket.getInputStream());
			
			sendToServer(out, "CONNECT elfticket testclient", null);
			
			System.out.println("reading...");
			
			String userInput = null;
			while((userInput = in.readLine()) != null) {
				System.out.println("READ: " + userInput);
				switch(userInput) {
				case "CONNECTED\n":
					byte[] data = "{\"class\" : \"iristk.system.Event\",\"event_name\" : \"action.speech\",\"event_id\" : \"my_unique_id_123\",\"text\" : \"Hello there\"}".getBytes();
					sendToServer(out, "EVENT action.speech", data);
					sendToServer(out, "CLOSE", null);
					break;
				}
			}
			
			in.close();
			out.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("reader thread exited");
	}
	

	private void sendToServer(DataOutputStream outToServer, String header, byte payload[]) throws IOException {
		synchronized ( outToServer ) {
			if ( header != null && header.length() > 0 ) {
				outToServer.writeBytes(header);
			}
			if ( payload != null && payload.length > 0 ) {
				outToServer.write(payload);
			}
		}
	}
	

public class ParsedInputStream extends FilterInputStream {

	public ParsedInputStream(InputStream stream) {
		super(stream);
	}
	
	public String readTo(int... stopChars) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		READ:
		while (true) {
			b = super.read();
			if (b == -1) break READ;
			for (int c : stopChars) {
				if (b == c) break READ;
			}
			baos.write(b);
		}
		return baos.toString();
	}
	
	public String readLine() throws IOException {
		return readTo(10, 13);
	}

}


}
