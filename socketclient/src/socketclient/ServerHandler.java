package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerHandler implements Runnable {
	Socket connectionSocket;

	public ServerHandler(Socket connectionSocket){
		this.connectionSocket=connectionSocket;
	}

	@Override
	public void run() {
		try{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connectionSocket.getInputStream())); 
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 
			String clientSentence = inFromClient.readLine(); 
			ServerParser parser = new ServerParser(clientSentence);
			System.out.println("Received: " + clientSentence); 
			String capsSentence = clientSentence.toUpperCase() + '\n'; 
			outToClient.writeBytes(capsSentence);
		
		}
		catch(IOException ex){
			
		}
	}

}
