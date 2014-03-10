package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
						this.put=true;
						System.out.println("4");
						
					}
					else{
						try{
						outToClient.writeBytes("200 OK" + "\n" + readFile(this.parser.getUri()) + "\n" + "EOS"+"\n");
						System.out.println("5");
						}
						catch(IOException i){
							outToClient.writeBytes(" HTTP/" + this.parser.gethTTPVersion() + " 404 NOT FOUND" + "\n" + "EOS"+"\n");
							System.out.println(" HTTP/" + this.parser.gethTTPVersion() + " 404 NOT FOUND" + "\n" + "EOS" +"\n");
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
		System.out.println(input);
		String[] tokens = input.split(" ");
		int p= 0;
		String toReturn="";
		boolean execute=false;

		System.out.println("tokens:" +tokens.length);
		for(int i=0; i< tokens.length; i++){
		if(execute && i+2<tokens.length){
			 toReturn+= tokens[i+2] + "\n";
			 System.out.println(tokens[i+2]);
			 System.out.println("i:" +i);
		}
		System.out.println("ii:" +i);
		System.out.println("Execute:" + execute);
		System.out.println(tokens[i]);
		if(tokens[i].contains("Content-Length:")){
			execute=true;
		}
		
		}
		System.out.println("toReturn:" +toReturn);
		return toReturn;
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
