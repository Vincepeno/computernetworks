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
			System.out.println("page");
			getLocalPage();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connectionSocket.getInputStream())); 
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream()); 

			String clientSentence;
			while((clientSentence = inFromClient.readLine()) != null){
				System.out.println("1");
				if(this.put==true){
					System.out.println("2");
					this.put=false;
					clientSentence = this.parser.getPutInput() + clientSentence  + "\n" + "\n"  +"EOS" +"\n";
					outToClient.writeBytes(clientSentence);
				}
				else{
					System.out.println("3");
					this.parser = new ServerParser(clientSentence);
					if(parser.getPut()==true){
						this.put=true;
						System.out.println("4");
					}
					else{
						//outToClient.writeBytes(getLocalPage());
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
	
	public void getLocalPage(){
		String path = this.parser.getUri1() + this.parser.getUri2();

		StringBuilder content=new StringBuilder(1024);
		FileReader fr;
		try {
			fr = new FileReader("C:/Users/Vince/Desktop/a.html");

			BufferedReader br= new BufferedReader(fr);
			String s;
			while((s=br.readLine())!=null)
			    {
			    content.append(s);
			    } 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(content);
		
	}



}
