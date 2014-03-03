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
			Socket socket = new Socket("www.hattrick.org",80);
//			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
//			//outToServer.writeUTF("GET /index.html HTTP/1.0"+"\n"+"host: www.example.com"+"\n"+"");
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("GET / HTTP/1.0");
			pw.println("host: www.hattrick.org");
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
			HTTPVersion=1.0;
		else if(type.contains("1.1"))
			HTTPVersion=1.1;
		else{
			System.out.println("The type is not applicable");
		}
	}
	

}

