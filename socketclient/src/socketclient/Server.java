package socketclient;
import java.io.*; 
import java.net.*; 
import javax.xml.ws.handler.*;
class Server 
{ 
	public static void main(String argv[]) throws Exception 
	{ 
		ServerSocket welcomeSocket = new ServerSocket(80); 
		while(true) 
		{ 
			Socket connectionSocket = welcomeSocket.accept(); 
			if (connectionSocket != null){
				ServerHandler handler= new ServerHandler(connectionSocket);
				Thread thread = new Thread(handler);
				thread.start();} 
		} 
	} 
} 
