import java.io.*;
import java.net.*;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class TestRead{
	String cache, search, query; // remember to change publication to publ
	String filename;
	String path;
	InputStream stream;
	Document doc;
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	private void setup() throws IOException, ParserConfigurationException, SAXException {
		path = "D:\\workspace\\W09\\" + cache + "\\";
		
		String link = "http://dblp.org/search/" + search + "/api?" + "q=" + URLEncoder.encode(query, "UTF-8") + "&h=40&c=0";
		URL url = new URL(link);
		filename = URLEncoder.encode(link, "UTF-8");
		path = path + filename + ".xml";
		File file = new File(path);
		
		if(!file.exists()) {
			saveFile(url, path);
		}
		readIntoDOM(path);
	}
	
	private void readIntoDOM(String path) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(path);
		DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(file);
	    doc.getDocumentElement().normalize();
	}
	
	
	private void test(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line;
		
		while((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();
	}
	
	private void saveFile(URL url, String path) throws IOException, ParserConfigurationException, SAXException {
		stream = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		PrintWriter writer = new PrintWriter(path);
		while((line = reader.readLine()) != null) {
			writer.println(line);
			System.out.println(line);
		}
		stream.close();
		
		reader.close();
		writer.close();
	}
	
	
	private void authorDetails() throws Exception{
		NodeList hits = doc.getElementsByTagName("author");
		for(int i = 0; i < hits.getLength(); i++) {
			Node authorName = hits.item(i);
			
			Node sibling = authorName.getNextSibling();
			String secondLink = sibling.getTextContent() + ".xml";
			secondCall(secondLink);
		}
	}
	
	private void secondCall(String link) throws Exception {
		URL secondUrl = new URL(link);
	    DocumentBuilder db2 = dbf.newDocumentBuilder();
	    stream = secondUrl.openStream();
	    Document doc2 = db2.parse(stream);
	    stream.close();
	    
	    doc2.getDocumentElement().normalize();
	    NodeList persons = doc2.getElementsByTagName("dblpperson");
	    for(int i = 0; i < persons.getLength(); i++) {
	    	Node person = persons.item(i);
	    	NamedNodeMap attributes = person.getAttributes();
	    	Node name = attributes.getNamedItem("name");
	    	String authorName = name.getNodeValue();
	    	
	    	Node attribute = attributes.getNamedItem("n");
	    	String publ = attribute.getNodeValue();
	    	
	    	Element element = (Element)person;
	    	Node child = element.getElementsByTagName("coauthors").item(0);
	    	NamedNodeMap coauthors = child.getAttributes();
	    	String number = coauthors.getNamedItem("n").getNodeValue();
	    	
	    	System.out.println(authorName + " - " + publ + " publications with " + number + " co-authors.");
	    }
	}
	
	public void doing() throws Exception{
			setup();
			authorDetails();
	}
	
	public TestRead(String cache, String search, String query) {
		this.cache = cache;
		this.search = search;
		this.query = query;
	}
	
	
	public static void main(String [] args) {
		TestRead read = new TestRead("cache", "author", "jack cole");
		try {
			read.doing();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}