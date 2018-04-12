/*
 * This class is for Extention: get information from another API: Wikipedia
 * @author 170011474
 * @version 1.0
 */
import java.net.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;

public class ReadEx {
	String query;
	String link = "https://en.wikipedia.org/w/api.php?format=xml&action=opensearch&search=";
	InputStream stream;
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	Document doc;
	URL url;
	
	private void setup() throws UnsupportedEncodingException, MalformedURLException{
		link = link + URLEncoder.encode(query, "UTF-8");
		url = new URL(link);
	}
	
	private void readIntoDOM(InputStream stream, URL url) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder db = dbf.newDocumentBuilder();
		stream = url.openStream();
		 doc = db.parse(stream);
		 doc.getDocumentElement().normalize();
		 stream.close();
	}
	
	private void saveFile(URL url) throws IOException, ParserConfigurationException, SAXException {
		stream = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		while((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		stream.close();
	}
	
	/*
	 * Print the number of results
	 * Print the query information and description
	 */
	private void details() {
		NodeList hits = doc.getElementsByTagName("Text");
		int number = hits.getLength();
		System.out.println(query + " - " + number + " results altogether");
		for(int i = 0; i < hits.getLength(); i++) {
			Node hit = hits.item(i);
			String text = hit.getTextContent();
			
			Node next = hit.getNextSibling().getNextSibling();
			String description = next.getTextContent();
			
			System.out.println(text);
			System.out.println(description);
			System.out.println();
		}
	}
	public void doing() throws Exception{
		setup();
		readIntoDOM(stream,url);
		saveFile(url);
		details();
	}
	
	public ReadEx(String query) {
		this.query = query;
	}
}
