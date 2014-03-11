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

			String clientSentence;
			while((clientSentence = inFromClient.readLine()) != null){
				System.out.println("1");
				if(this.put==true){
					try{
					System.out.println("2");
					this.put=false;
					clientSentence = this.parser.completePut(clientSentence);
					saveString(clientSentence);
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
					if(parser.getPut()==true){
						System.out.println("4");
						System.out.println(this.parser.getUri1());
						if(isDirectory(parser.getUri())){
							this.put=true;
						    outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 200 OK"  + "\n" + 
						    							"Specify your content:" + "\n" + "EOS"+"\n");
						}
						else
							outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 500 Server Error"  + "\n" + "EOS"+"\n");
						
					}
					else if(this.parser.getCommand().equals("GET")){
						try{
						outToClient.writeBytes(makeGet(readFile(this.parser.getUri())));
						System.out.println("5");
						}
						catch(IOException i){
							outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 404 NOT FOUND" + "\n" + "EOS"+"\n");
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
				//System.out.println("Received: " + clientSentence); 
				//String capsSentence = clientSentence.toUpperCase(); 
				//System.out.println(capsSentence);

				outToClient.flush();
				System.out.println("7");
				}


			connectionSocket.close();
			System.out.println("The End :(");
		}
		catch(Exception ex){



		}}


	public void saveString(String input) throws IOException{
//		C:/Users/Vince/Desktop/b.html
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
	
	private boolean isDirectory(String uri){
		String[] tokens = uri.split("/");
				String directory="";
		for(int i =0; i<tokens.length -1; i++){
			directory+=tokens[i] +"/";
		}
		if(tokens.length==1 && !tokens.equals("C:"))
			return false;
		Path path = Paths.get(directory);
		return Files.isDirectory(path);
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
	
	//private void saveFile



}
