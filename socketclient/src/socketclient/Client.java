package socketclient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
class Client 
{ 
	private double HTTP11=1.1;
	ArrayList<String> imageUrls = new ArrayList<>();
	public Client() throws Exception 
	{ 
		Socket clientSocket = new Socket("localhost", 6789); 
		while(HTTP11==1.1){

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));  
			System.out.println("1");
			System.out.println("2");
			String sentence = inFromUser.readLine();
			System.out.println("3");
			ServerParser parser = new ServerParser(sentence);
			HTTP11 = parser.gethTTPVersion();
			if(parser.getPut() == true){
				outToServer.writeBytes(sentence + '\n'); 
				System.out.println("FROM SERVER: Specify the content:");
				String content = inFromUser.readLine();
				System.out.println("10");
				outToServer.writeBytes(content + '\n'); 
				outToServer.flush();
				String modifiedSentence;
				while(!(modifiedSentence = inFromServer.readLine()).contains("EOS")){
					System.out.println("FROM SERVER: " + modifiedSentence ); 
				}
			}
			else if (parser.getCommand().equals("GET") || parser.getCommand().equals("HEAD")){
				String webpage = getHTML(parser);
				System.out.println(webpage);
				System.out.println("The page contains " + imageUrls.size() + " image(s).");
				this.saveImage(parser);		
				System.out.println("All images are saved.");


			}
			outToServer.flush();
			//			System.out.println("Client:" + sentence);
			//			outToServer.writeBytes(sentence + '\n'); 
			//			System.out.println("4");
			//			System.out.println("5");
		}
		clientSocket.close(); 

	}
	/*
	 * Save the images to the disk. 
	 */
	private void saveImage(ServerParser parser) {
		System.out.println("Saving the images...");
		//Loop through all imageUrls
		for(int i=0; i<imageUrls.size(); i++){
			String uri1;
			String uri2;
			try {
				String[] tokensUri = imageUrls.get(i).split("/", 2);
				//If the domain name is already in the url, we split it 
				if(tokensUri[0].contains(".")){
					uri1 = tokensUri[0];
					uri2 = tokensUri[1];
				}
				//If the domain name is not in the url, we get it from the parser
				else{
					uri1 = parser.getUri1();
					uri2 = imageUrls.get(i);
				}
				//Make a new file to save the image
				PrintWriter out = new PrintWriter("Image_" + (i+1) + ".txt");
				//Create a GET-request for the imageUrl. 
				Socket socket = new Socket(uri1,80);
				PrintWriter pw = new PrintWriter(socket.getOutputStream());
				pw.println("GET" + " /" + uri2 + " HTTP/1.0");
				pw.println("host: " + uri1);
				pw.println();
				pw.flush();
				BufferedReader inFromServer1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;
				//Boolean to check of the recevied Html-code already started with the real image code (not the header info)
				boolean imageCodeIsStarted = false;
				while ((line = inFromServer1.readLine()) != null) {
					//Only save the code from the real image 
					if(imageCodeIsStarted){
						out.println(line);
					}
					if(!imageCodeIsStarted && line.isEmpty()){
						imageCodeIsStarted = true;
					}
				}
				//Close the txt file
				out.close();
				socket.close();
				System.out.println("Image " + (i+1) + " is saved.");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Retrieve the requested HTML-code.
	 */
	public String getHTML(ServerParser parser){

		String page = "";

		try {
			Socket socket = new Socket(parser.getUri1(),parser.getPort());
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			//Send the request
			pw.println(parser.getCommand()+ " /" + parser.getUri2() + " HTTP/" +parser.gethTTPVersion()); 
			pw.println("host: " + parser.getUri1());
			pw.println();
			pw.flush();
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			//Add the received HTML-code to the page-string
			while ((line = inFromServer.readLine()) != null) {
				page += line + "\n"; 
				//Find image Urls in the html-code 
				if(line.contains("<img")){
					//Search the position of the imageUrl in the incoming line 
					int begin = line.indexOf("src=") + 5;
					int end = line.indexOf(" ", begin) - 1;
					//Get url of the image
					String imageUrl = line.substring(begin, end);
					//Remove http:// prefix
					if(imageUrl.startsWith("http://")){
						imageUrl = imageUrl.substring(7);
					}
					//Add url in list
					imageUrls.add(imageUrl);
				}
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}
}
