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
					System.out.println("2");
					this.put=false;
					clientSentence = this.parser.completePut(clientSentence);
					saveString(clientSentence);
				}
				else{
					System.out.println("3");
					this.parser = new ServerParser(clientSentence);
					if(parser.getPut()==true){
						this.put=true;
						System.out.println("4");
					}
					else{
						outToClient.writeBytes(readFile(this.parser.getUri()) + "\n" + "EOS");
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


	public void saveString(String input){
//		C:/Users/Vince/Desktop/b.html
//		String[] tokens = input.split(" ");
//		String path= tokens[1];
		System.out.println(parser.getUri());
		File newTextFile = new File(parser.getUri());
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(newTextFile);
			fileWriter.write(input);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
