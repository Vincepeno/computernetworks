package socketclient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
class Client 
{ 
	private double HTTP11=1.1;
	public Client() throws Exception 
	{ 
		Socket clientSocket = new Socket("localhost", 6789); 
		while(HTTP11==1.1){

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));  
		System.out.println("1");
			System.out.println("2");
			String sentence = inFromUser.readLine();
			System.out.println("3");
			ServerParser parser = new ServerParser(sentence);
			HTTP11 = parser.gethTTPVersion();
			if(parser.getPut() == true){
				outToServer.writeBytes(sentence + '\n'); 
				System.out.println("FROM SERVER: Specify the content:");
				String content = inFromUser.readLine();
				System.out.println("10");
				outToServer.writeBytes(content + '\n'); 
				String modifiedSentence;
				while((modifiedSentence = inFromServer.readLine()) != null){
					System.out.println("FROM SERVER: " + modifiedSentence ); 
				}
			}
			else if (parser.getCommand().equals("GET") || parser.getCommand().equals("HEAD")){
				String webpage = getHTML(parser);
				System.out.println(webpage);
			}
//			System.out.println("Client:" + sentence);
//			outToServer.writeBytes(sentence + '\n'); 
//			System.out.println("4");
//			System.out.println("5");
		}
		clientSocket.close(); 

	}
	public String getHTML(ServerParser parser){

		String page = "";
		try {
			Socket socket = new Socket(parser.getUri1(),parser.getPort());
			//			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
			//			//outToServer.writeUTF("GET /index.html HTTP/1.0"+"\n"+"host: www.example.com"+"\n"+"");
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(parser.getCommand()+ " /" + parser.getUri2() + " HTTP/" +parser.gethTTPVersion()); //"GET / HTTP/1.0";
			pw.println("host: " + parser.getUri1());
			pw.println();
			pw.flush();
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Extra toegevoegd: readline leest enkel eerste zin van document --> while lus
			String line;
			while ((line = inFromServer.readLine()) != null) {
				page += line + "\n"; 
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}
}
