package socketclient;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
class Client 
{ 
	private double HTTP11=1.1;
	ArrayList<String> imageUrls = new ArrayList<>();
	boolean start=true;
	Socket clientSocket;
	public Client(String command, String uri, String port, String version) throws Exception 
	{ 	
		String sentence = command + " " + uri +" " + port + " " + version;
		ServerParser parser = new ServerParser(sentence); 
		clientSocket = new Socket(parser.getUri1(),parser.getPort()); 
		//aanpassen miss
		showIntro();

		while(HTTP11==1.1){
			System.out.println("We're happy to forfill your command:");
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));  
			System.out.println("1");

			if(!start){
				sentence = inFromUser.readLine();
				System.out.println("3");
				parser = new ServerParser(sentence);
			}
			HTTP11 = parser.gethTTPVersion();

			System.out.println("parsercommand: "+parser.getCommand());
			System.out.println("localget: "+getLocalGet(parser.getUri()));
			
			if(parser.getPut() == true){
				outToServer.writeBytes(sentence + '\n'); 
				String modifiedSentence=null;
				boolean error=false;
				while(!(modifiedSentence = inFromServer.readLine()).contains("EOS")){
					System.out.println("FROM SERVER: " + modifiedSentence ); 
					if(modifiedSentence.contains("500")){
						error=true;
					}
				}


				if(!error){
					System.out.println("waiting");
					String content = inFromUser.readLine();
					System.out.println("10");
					outToServer.writeBytes(content + '\n'); 
					outToServer.flush();
					String modifiedSentences=null;
					while(!(modifiedSentences = inFromServer.readLine()).contains("EOS")){
						System.out.println("FROM SERVER: " + modifiedSentences ); 
					}
				}

			}
			else if (parser.getCommand().equals("GET") || parser.getCommand().equals("HEAD")){
				if(getLocalGet(parser.getUri())){
					outToServer.writeBytes(sentence + '\n');
					String modifiedSentence=null;
					while(!(modifiedSentence = inFromServer.readLine()).contains("EOS")){
						System.out.println("FROM SERVER: " + modifiedSentence ); 
					}
				}
				else{
					String webpage = getHTML(parser);
					System.out.println("webpage:" +webpage);
					System.out.println("The page contains " + imageUrls.size() + " image(s).");
					this.saveImage(parser);		
					System.out.println("All images are saved.");
				}


			}
			start=false;
			outToServer.flush();
			//						System.out.println("Client:" + sentence);
			//						outToServer.writeBytes(sentence + '\n'); 
			System.out.println("4");
			System.out.println("5");
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
			String imageUrl = imageUrls.get(i);
			String[] tokensUri = imageUrls.get(i).split("/", 2);
			//If the domain name is already in the url, we split it 
			if(!tokensUri[0].contains(".")){
				imageUrl = parser.getUri1() + "/" + imageUrls.get(i);
			}

			//Add http:// prefix
			imageUrl = "http://" + imageUrl;

			BufferedImage image = null;
			try{
				URL url =new URL(imageUrl);
				image = ImageIO.read(url);

				//Save png images
				if(imageUrl.contains(".png")){
					ImageIO.write(image, "png",new File("Image_" + (i+1) + ".png"));
					System.out.println("Image " + (i+1) + " is saved.");
				}

				//Save jpg images
				else if(imageUrl.contains(".jpg")){
					ImageIO.write(image, "jpg",new File("Image_" + (i+1) + ".jpg"));
					System.out.println("Image " + (i+1) + " is saved.");
				}

				//Save gif images
				else if(imageUrl.contains(".gif")){
					ImageIO.write(image, "gif",new File("Image_" + (i+1) + ".gif"));
					System.out.println("Image " + (i+1) + " is saved.");
				}

				//Save jpeg images
				else if(imageUrl.contains(".jpeg")){
					ImageIO.write(image, "jpg",new File("Image_" + (i+1) + ".jpeg"));
					System.out.println("Image " + (i+1) + " is saved.");
				}

				//Image format not recognized
				else{
					System.out.println("Image " + (i+1) + " can't be saved");
				}

			}
			catch(IOException e){
				System.out.println("Image " + (i+1) + " can't be saved");
			}


		}

	}

	/**
	 * Retrieve the requested HTML-code.
	 */
	public String getHTML(ServerParser parser){

		String page = "";

		try {
			//			Socket socket = new Socket(parser.getUri1(),parser.getPort());
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
			//Send the request
			pw.println(parser.getCommand()+ " /" + parser.getUri2() + " HTTP/" +parser.gethTTPVersion()); 
			pw.println("host: " + parser.getUri1());
			pw.println();
			pw.flush();
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
			//			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;
	}

	private void showIntro(){
		System.out.println("Welcome to our client-serverpgrogram");
		System.out.println("All input commando's require 4 fields seperated by a space");
		System.out.println("The input data requires only the text");
	}

	private boolean getLocalGet(String uri){
		if(uri.contains("www")){
			return false;
		}
		return true;
	}
}
