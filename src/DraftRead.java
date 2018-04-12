import java.io.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DraftRead{
	String cache, search, query; // remember to change publication to publ
	String filename;
	String path;
	InputStream stream;
	Document doc;
	File file;
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	private void setup() throws IOException, ParserConfigurationException, SAXException {
		/*search = "author";
		query = "jack cole";*/
		/*search = "publ";
		query = "SQL";*/
		/*search = "venue";
		query = "United";*/
		// path = "/home/CS1003/W09-Practical/";
		path = "D:\\workspace\\W09\\";
		cache = path + cache + "\\";
		
		String link = "http://dblp.org/search/" + search + "/api?" + "q=" + URLEncoder.encode(query, "UTF-8") + "&h=40&c=0";
		URL url = new URL(link);
		filename = URLEncoder.encode(link, "UTF-8");
		path = cache + filename;
		file = new File(path + ".xml");
		
		if(file.exists()) {
			readIntoDOM(file);
		}
		else {
			readIntoDOM(stream, url);
		    saveFile(url, path);
		}
	}
	
	private void readIntoDOM(File f) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(f);
	    doc.getDocumentElement().normalize();
	}
	
	private void readIntoDOM(InputStream stream, URL url) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = dbf.newDocumentBuilder();
		stream = url.openStream();
	    doc = db.parse(stream);
	    doc.getDocumentElement().normalize();
	    stream.close();
	}
	
	private void saveFile(URL url, String filename) throws IOException, ParserConfigurationException, SAXException {
		stream = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		PrintWriter writer = new PrintWriter(filename);
		while((line = reader.readLine()) != null) {
			writer.println(line);
			System.out.println(line);
		}
		stream.close();
	}
	
	private void actions() throws Exception {
		switch(search) {
		case "author": authorDetails(); break;
		case "publ": publicationDetails(); break;
		case "venue": venueDetails(); break;
		default: System.out.println("Invalid search action!");
		}
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
	
	private void publicationDetails() {
		NodeList hits = doc.getElementsByTagName("authors");
		
		for(int i = 0; i < hits.getLength(); i++) {
			Element hit = (Element)hits.item(i);
			NodeList authors = hit.getElementsByTagName("author");
			int number = authors.getLength();
			
			String title = hits.item(i).getNextSibling().getTextContent();
			System.out.println(title + " - " + number + " authors.");
		}
	}
	
	private void venueDetails() {
		NodeList hits = doc.getElementsByTagName("venue");
		for(int i = 0; i < hits.getLength(); i++) {
			Node hit = hits.item(i);
			String venue = hit.getTextContent();
			
			System.out.println(venue);
		}
	}
	
	public void doing() throws Exception{
			setup();
			actions();
	}
	
	public DraftRead(String cache, String search, String query) {
		this.cache = cache;
		this.search = search;
		this.query = query;
	}
	
    public DraftRead() {}
	
	public static void main(String [] args) {
		DraftRead read = new DraftRead();
		try {
			read.doing();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}