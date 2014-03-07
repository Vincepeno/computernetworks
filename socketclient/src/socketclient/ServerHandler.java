package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

			while(true){



				System.out.println("1");
				String clientSentence = inFromClient.readLine(); 
				if(this.put==true){
					System.out.println("2");
					this.put=false;
					System.out.println(this.parser.getPutInput() + clientSentence  + "\n" + "\n"  +"endofPut");
				}
				else{
					System.out.println("3");
					this.parser = new ServerParser(clientSentence);
					if(parser.getPut()==true){
						this.put=true;
						System.out.println("4");
					}
					else if(this.parser.getCommand().equals("GET") || this.parser.getCommand().equals("HEAD")){
						getHTML();
						System.out.println("5");
					}
				}
				System.out.println("6");
				System.out.println("Received: " + clientSentence); 
				String capsSentence = clientSentence.toUpperCase(); 
				System.out.println(capsSentence);
				outToClient.writeBytes("hi");
				outToClient.flush();
				System.out.println("7");}}
		catch(IOException ex){

		}

	}


	public void saveString(String input){
		String[] tokens = input.split(" ");
		String path= tokens[1];
		File newTextFile = new File(path);
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

	public void getHTML(){
		try {
			Socket socket = new Socket(this.parser.getUri1(),this.parser.getPort());
			//			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
			//			//outToServer.writeUTF("GET /index.html HTTP/1.0"+"\n"+"host: www.example.com"+"\n"+"");
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(this.parser.getCommand()+ " /" + this.parser.getUri2() + " HTTP/" + this.parser.gethTTPVersion()); //"GET / HTTP/1.0";
			pw.println("host: " + this.parser.getUri1());
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

}
