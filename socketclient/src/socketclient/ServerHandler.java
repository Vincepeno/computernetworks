package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerHandler implements Runnable {
	Socket connectionSocket;
	private boolean put=false;
	private ServerParser parser;

	public ServerHandler(Socket connectionSocket){
		this.connectionSocket=connectionSocket;
	}

	@Override
	public void run() {
		try{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connectionSocket.getInputStream())); 
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 


			while(connectionSocket !=null){
				String clientSentence = inFromClient.readLine();
				if(clientSentence.contains("HTTP")){
				System.out.println("1");
				System.out.println(clientSentence);
				if(this.put==true){
					try{
						System.out.println("2");
						this.put=false;
						System.out.println(this.parser.getUri());
						String save = this.parser.completePut(clientSentence);
						saveString(save);
					}
					catch(IOException i){
						outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 500 Server Error"  + "\n" + "EOS"+"\n");
					}
					System.out.println();
					outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 200 OK" + "\n" +  "EOS"+ "\n");
				}
				else{
					System.out.println("3");
					this.parser = new ServerParser(clientSentence);
					parser.setLocalUri();

					System.out.println("4");
					if(parser.getPut()==true){

						System.out.println("put");
						this.put=true;
						outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 200 OK"  + "\n" + 
								"Specify your content:" + "\n" + "EOS"+"\n");
					}
					else if(this.parser.getCommand().equals("GET")){
						System.out.println(this.parser.getUri());
						try{
							outToClient.writeBytes(makeGet(readFile(this.parser.getUri())));
							System.out.println("5");
						}
						catch(IOException i){
							System.out.println("exception");
							outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 404 NOT FOUND" + "\n" + "EOS"+"\n" +"\n");
						}
					}
					else{
						try{
							outToClient.writeBytes(makeHead(readFile(this.parser.getUri())) + "\n" + "EOS" + "\n");
							System.out.println("5");
						}
						catch(IOException i){
							outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 404 NOT FOUND" + "\n" + "EOS"+"\n");
						}

					}
				}
				System.out.println("6");
				outToClient.flush();
			}}


			connectionSocket.close();
			System.out.println("The End :(");
		}
		catch(Exception ex){



		}}


	public void saveString(String input) throws IOException{
		System.out.println(parser.getUri());
		File newTextFile = new File(parser.getUri());
		FileWriter fileWriter;
		fileWriter = new FileWriter(newTextFile);
		fileWriter.write(putParse(input));
		fileWriter.close();
		System.out.println("saveString succeeded");

	}

	private String putParse(String input){
		String[] tokens = input.split("\n");
		int p= 0;
		String toReturn="";
		boolean execute=false;
		for(int i=0; i< tokens.length; i++){
			if(execute && i+2<tokens.length){
				toReturn+= tokens[i+1] + "\n";
			}
			if(tokens[i].contains("Content-Length:")){
				execute=true;
			}
		}
		return toReturn;
	}

	private String makeGet(String content){
		return  makeHead(content)+ "\n" + "\n" 
				+content  + "EOS"+"\n" ;
	}
	private String makeHead(String content){
		return "HTTP/"+this.parser.gethTTPVersion()+ " 200 OK"+ "\n" +
				"Date: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date()) + "\n" +
				"Content-Type: text/html" + "\n" + 
				"Content-Length: " + content.length();
	}




	private String readFile(String uri) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (uri));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}

		return stringBuilder.toString();
	}



}
