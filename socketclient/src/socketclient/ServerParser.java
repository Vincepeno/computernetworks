package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerParser {

	String command;
	String uri;
	int port;
	double HTTPVersion;

	public ServerParser(String inputSentence){
		Parse(inputSentence);
	}

	public void Parse(String inputSentence){
//		String input = inputSentence.toLowerCase();
//		String[] tokens = input.split(" ");
//		if(tokens.length == 4){
//			uri = tokens[1];
//			command = tokens[0];
//			//CommandParse(command);
//			try{
//			int port = Integer.parseInt(tokens[2] );
//			}
//			catch(Exception e){
//				//the port should be an integer!!
//			}
//			parseHTTPType(tokens[3]);
//		}
		getWebpage();
	}
	public void CommandParse(String command){
		if(command.equals("head")){

		}
		else if(command.equals("get")){
			byte[] encoded;
			try {
				encoded = Files.readAllBytes(Paths.get(uri));
				String content = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
				System.out.println(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(command.equals("put")){

		}
		else if(command.equals("post")){

		}

	}
	
	public void getWebpage(){
		try {
			
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
			outToServer.writeUTF("GET www.google.com/index.html 80 HTTP\1.0"); 
			Socket socket = new Socket("www.google.be",80);
			// HTTP/1.0 400 Bad Request
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String modifiedSentence = inFromServer.readLine();
			System.out.println(modifiedSentence);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseHTTPType(String type){
		if(type.contains("1.0"))
			HTTPVersion=1.0;
		else if(type.contains("1.1"))
			HTTPVersion=1.1;
		else{
			System.out.println("The type is not applicable");
		}
	}
	

}
