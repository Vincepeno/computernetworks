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
	private String uri;

	public ServerParser(String inputSentence){
		parse(inputSentence);
	}

	/*
	 * Split the input from the commandline into the different arguments.
	 */
	public void parse(String input){
		String[] tokens = input.split(" ");

		System.out.println("command" +tokens[0]);
		if(tokens.length==3 || tokens.length==4){
			command = tokens[0].toUpperCase();
			//Split the uri in the main domain name and the rest
			String[] tokensUri = tokens[1].split("/", 2);
			uri = tokens[1];
			uri1 = tokensUri[0];
			//If the inputSentence only has a main domain name as URI, uri2 will be empty
			if(tokensUri.length == 2){
				uri2 = tokensUri[1];
			}
		if(tokens.length == 4){
			port = Integer.parseInt(tokens[2]);
			parseHTTPType(tokens[3]);
		}
		else{
			parseHTTPType(input);
			port = 80;
		}
		this.commandParse();
		}
		else{
			System.out.println("Wrong input in commandline.");
			System.out.println(tokens.length);
		}
	}
	public String getUri(){
		return this.uri;
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
//				this.putInput = command + " " + uri1 + uri2 + " HTTP/" + hTTPVersion + "\n" 
//						+ "From: localhost" + "\n" +
//						"User-Agent: HTTPTool/" + hTTPVersion + "\n" +
//						"Content-Type: Text" + "\n" +
//						"Content-Length:" +"\n" +
//						"\n"
//						
//						;
				
		}

		}
	
	public String completePut(String sentence){
		return command + " " + uri1 + uri2 + " HTTP/" + hTTPVersion + "\n" 
				+ "From: localhost" + "\n" +
				"User-Agent: HTTPTool/" + hTTPVersion + "\n" +
				"Content-Type: Text" + "\n" +
				"Content-Length: " + sentence.length()+"\n" +
				"\n"  +sentence + "\n"+ "\n"   +"EOS" + "\n";
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

