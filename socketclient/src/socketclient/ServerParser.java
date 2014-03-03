package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerParser {

	String client;
	String command;
	//The main domain name
	String uri1;
	//The rest of the uri
	String uri2="";
	int port;
	double hTTPVersion;

	public ServerParser(String inputSentence){
		parse(inputSentence);
	}

	/*
	 * Split the input from the commandline into the different arguments.
	 */
	public void parse(String inputSentence){
		String input = inputSentence.toLowerCase();
		String[] tokens = input.split(" ");
		if(tokens.length == 5){
			client = tokens[0];
			command = tokens[1].toUpperCase();
			//Split the uri in the main domain name and the rest
			String[] tokensUri = tokens[2].split("/", 2);
			uri1 = tokensUri[0];
			//If the inputSentence only has a main domain name as URI, uri2 will be empty
			if(tokensUri.length == 2){
				uri2 = tokensUri[1];
			}
			port = Integer.parseInt(tokens[3]);
			hTTPVersion = Double.parseDouble(tokens[4]);		
			this.commandParse();
		}
		else{
			System.out.println("Wrong input in commandline.");
		}
	}
	


	public void commandParse(){
		if(command.equals("GET") || command.equals("HEAD")){
			this.getHTML();
		}
		//			else if(command.equals("get")){
		////				byte[] encoded;
		////				try {
		////					encoded = Files.readAllBytes(Paths.get(uri));
		////					String content = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		////					System.out.println(content);
		////				} catch (IOException e) {
		////					// TODO Auto-generated catch block
		////					e.printStackTrace();
		////				}
		//			}
		else if(command.equals("put")){

		}
		else if(command.equals("post")){

		}

	}

	public void getHTML(){
		try {
			Socket socket = new Socket(uri1,port);
			//			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
			//			//outToServer.writeUTF("GET /index.html HTTP/1.0"+"\n"+"host: www.example.com"+"\n"+"");
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(command + " /" + uri2 + " HTTP/" + hTTPVersion); //"GET / HTTP/1.0";
			pw.println("host: " + uri1);
			pw.println();
			pw.flush();
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//Extra toegevoegd: readline leest enkel eerste zin van document --> while lus
			String line;
			while ((line = inFromServer.readLine()) != null) {
				System.out.println(line);
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	public void parseHTTPType(String type){
	//		if(type.contains("1.0"))
	//			HTTPVersion=1.0;
	//		else if(type.contains("1.1"))
	//			HTTPVersion=1.1;
	//		else{
	//			System.out.println("The type is not applicable");
	//		}
//}


}

