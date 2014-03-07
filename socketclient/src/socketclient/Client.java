package socketclient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
class Client 
{ 
	public static void main(String argv[]) throws Exception 
	{ 
		Socket clientSocket = new Socket("localhost", 6789); 
		while(true){
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in)); 
			

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		System.out.println("1");
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
			System.out.println("2");
			String sentence = inFromUser.readLine();
			System.out.println("3");
			ServerParser parser = new ServerParser(sentence);
			if(parser.getPut() == true){
				outToServer.writeBytes(sentence + '\n'); 
				System.out.println("FROM SERVER: Specify the content:");
				String content = inFromUser.readLine();
				System.out.println("10");
				outToServer.writeBytes(content + '\n'); 
			}
			System.out.println("Client:" + sentence);
			outToServer.writeBytes(sentence + '\n'); 
			System.out.println("4");
			String modifiedSentence;
			while((modifiedSentence = inFromServer.readLine()) != null){
				System.out.println("FROM SERVER: " + modifiedSentence ); 
			}
			System.out.println("5");
		}
		//clientSocket.close(); 

	}
}
