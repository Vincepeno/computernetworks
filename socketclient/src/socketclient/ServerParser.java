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

	
	private String command;
	//The main domain name
	private String uri1;
	public String getUri1() {
		return uri1;
	}

	public String getUri2() {
		return uri2;
	}

	public int getPort() {
		return port;
	}

	public double gethTTPVersion() {
		return hTTPVersion;
	}

	public String getPutInput() {
		return putInput;
	}

	//The rest of the uri
	private String uri2="";
	private int port;
	private double hTTPVersion;
	private boolean put=false;
	private String putInput;

	public ServerParser(String inputSentence){
		parse(inputSentence);
	}

	/*
	 * Split the input from the commandline into the different arguments.
	 */
	public void parse(String inputSentence){
		String input = inputSentence.toLowerCase();
		String[] tokens = input.split(" ");
		if(tokens.length == 4){
			command = tokens[0].toUpperCase();
			//Split the uri in the main domain name and the rest
			String[] tokensUri = tokens[1].split("/", 2);
			uri1 = tokensUri[0];
			//If the inputSentence only has a main domain name as URI, uri2 will be empty
			if(tokensUri.length == 2){
				uri2 = tokensUri[1];
			}
			port = Integer.parseInt(tokens[2]);
			hTTPVersion = Double.parseDouble(tokens[3]);		
			this.commandParse();
		}
		else{
			System.out.println("Wrong input in commandline.");
		}
	}
	


	public void commandParse(){
		if(command.equals("GET") || command.equals("HEAD")){
			//this.getHTML();
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
		else if(command.equals("PUT") || command.equals("POST")){
				put = true;
				this.putInput = command + " " + uri1 + uri2 + " HTTP/" + hTTPVersion + "\n" 
						+ "From: localhost" + "\n" +
						"User-Agent: HTTPTool/" + hTTPVersion + "\n" +
						"Content-Type: Text" + "\n" +
						"Content-Length:" +"\n" +
						"\n"
						
						;
				
		}

		}
	
	public String completePut(String sentence){
		return command + " " + uri1 + uri2 + " HTTP/" + hTTPVersion + "\n" 
				+ "From: localhost" + "\n" +
				"User-Agent: HTTPTool/" + hTTPVersion + "\n" +
				"Content-Type: Text" + "\n" +
				"Content-Length:" +"\n" +
				"\n"  +sentence + "\n"   +"EOS" + "\n";
	}
	
	public boolean getPut(){
		return this.put;
	}
	
	public String getCommand(){
		return this.command;
	}

	public String makePut(){
//		if(command.equals("Put"))
		return "";
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

		public void parseHTTPType(String type){
			if(type.contains("1.0"))
				this.hTTPVersion=1.0;
			else if(type.contains("1.1"))
				this.hTTPVersion=1.1;
			else{
				System.out.println("The type is not applicable");
			}
}


}

