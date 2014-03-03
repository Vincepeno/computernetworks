package socketclient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class WerkendExampleAfblijven {

	public static void main(String argv[]) throws Exception {
		Socket s = new Socket(InetAddress.getByName("wikipedia.be"), 80);
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET / HTTP/1.1");
		pw.println("Host: wikipedia.be");
		pw.println();
		pw.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String t;
		while((t = br.readLine()) != null) System.out.println(t);
		br.close();
	}
}


