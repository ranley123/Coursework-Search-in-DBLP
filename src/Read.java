/**
 * Take the arguments from user and  
 * Download the xml response and read it into DOM
 * Print the details of Authors and use the second call
 * Print the details of Publications
 * Print the details of venue
 * @author 170011474
 * @date 10th April
 * @version 1.0
 */

import java.io.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Read{
	String cache, search, query; // remember to change publication to publ
	String filename;
	String path;
	InputStream stream;
	Document doc;
	File file;
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	/*
	 * Construct the URL by using the arguments
	 * Check existing: if exists, directly read into DOM
	 * if not, save xml file into local cache file and read into DOM
	 * 
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	private void setup() throws IOException, ParserConfigurationException, SAXException {
		path = "D:\\workspace\\W09\\" + cache + "\\";
		String link = "http://dblp.org/search/" + search + "/api?q=" + URLEncoder.encode(query, "UTF-8") + "&h=40&c=0";
		URL url = new URL(link);
		filename = URLEncoder.encode(link, "UTF-8");
		path = path + filename + ".xml";
		File file = new File(path);
		
		if(!file.exists()) {
			saveFile(url, path);
		}
		readIntoDOM(path);
	}
	
	/*
	 * Read the file into DOM
	 * @param path the string which represents the location of the file
	 * @throws ParserConfigurationException, SAXException
	 */

 void readIntoDOM(String path) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(path);
		DocumentBuilder db = dbf.newDocumentBuilder();
	    doc = db.parse(file);
	    doc.getDocumentElement().normalize();
	}
	
	/*
	 * Save the xml response from online URL into a local file
	 * Use BufferedReader and PrintWriter use line by line
	 * @param url an absolute URL giving the base location of the searching result
	 * @param path the string which represents the location of the file
	 * @throws IOException, ParserConfigurationException, SAXException
	 */
	protected void saveFile(URL url, String path) throws IOException, ParserConfigurationException, SAXException {
		stream = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		PrintWriter writer = new PrintWriter(path);
		while((line = reader.readLine()) != null) {
			writer.println(line);
		}
		stream.close();
		reader.close();
		writer.close();
	}
	
	/*
	 * Invoke different actions depending on the argument "search"
	 * Use a switch statement
	 * @throws Exception
	 */
	private void actions() throws Exception {
		switch(search) {
		case "author": authorDetails(); break;
		case "publ": publicationDetails(); break;
		case "venue": venueDetails(); break;
		default: System.out.println("Invalid search action!");
		}
	}
	
	/*
	 * Get Every Node which includes "author"
	 * Get get URL information for the second call
	 * @throws Exception
	 */
	private void authorDetails() throws Exception{
		NodeList hits = doc.getElementsByTagName("author");
		for(int i = 0; i < hits.getLength(); i++) {
			Node link = hits.item(i).getNextSibling();
			while(!link.getNodeName().equals("url")) {
				link = link.getNextSibling();
			}
			String secondLink = link.getTextContent() + ".xml";
		    secondCall(secondLink);
		}
	}
	
	/*
	 * Use the second link to make the second call and read it into DOM directly
	 * Get every person and get name
	 * Use Attributes to get information
	 * @param link the link from the first call
	 * @throws ParserConfigurationException, IOException, SAXException
	 */
	private void secondCall(String link) throws ParserConfigurationException, IOException, SAXException {
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
	    	String authorName = attributes.getNamedItem("name").getNodeValue();
	    	String publ = attributes.getNamedItem("n").getNodeValue();
	    	
	    	String number;
	    	Element element = (Element)person;
	    	NodeList children = element.getElementsByTagName("coauthors");
	    	if(children.getLength() == 0) {
	    		number = "0";
	    	}
	    	else {
	    		NamedNodeMap coauthors = children.item(0).getAttributes();
	    	    number = coauthors.getNamedItem("n").getNodeValue();
	    	}
	    	System.out.println(authorName + " - " + publ + " publications with " + number + " co-authors.");
	    }
	}
	
	/*
	 * Print the details of publication and the number of authors
	 */
	private void publicationDetails() {
		NodeList hits = doc.getElementsByTagName("info");
		for(int i = 0; i < hits.getLength(); i++) {
			Element hit = (Element)hits.item(i);
			int number = 0;
			if(hit.getFirstChild().getNodeName().equals("authors")) {
				NodeList authors = hit.getElementsByTagName("author");
				number = authors.getLength();
			}
			 
			String title = hit.getElementsByTagName("title").item(0).getTextContent();
			System.out.println(title + " (number of authors: " + number + ")");
		}
	}
	
	/*
	 * Print details of venue
	 */
	private void venueDetails() {
		NodeList hits = doc.getElementsByTagName("venue");
		for(int i = 0; i < hits.getLength(); i++) {
			Node hit = hits.item(i);
			String venue = hit.getTextContent();
			
			System.out.println(venue);
		}
	}
	
	/*
	 * The whole process
	 * @throws Exception
	 */
	public void doing() throws Exception{
			setup();
			actions();
	}
	
	public Read(String cache, String search, String query) {
		this.cache = cache;
		this.search = search;
		this.query = query;
	}
	
	
	/*public static void main(String [] args) {
		Read read = new Read("cache", "author", "jack cole");
		try {
			read.doing();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
}
