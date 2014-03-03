package socketclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Example {
	public static void main(String argv[]) throws Exception {
		Socket s = new Socket("www.example.com", 80);
		DataOutputStream outToServer = new DataOutputStream(s.getOutputStream()); 

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
		outToServer.writeBytes("GET www.example.com/index.html HTTP/1.0" + '\n' +'\n'); 
		String returned = inFromServer.readLine();
		while( returned != null){
		System.out.println(returned);
		returned = inFromServer.readLine();
		}	
		outToServer.flush();
		s.close();
//		 bufferedreader \\writebytes
	}
}
