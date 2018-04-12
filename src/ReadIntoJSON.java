/*
 * This class is to obtain JSON format file from online
 * Use Json to parse the object
 * @author 170011474
 * @version 1.0
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadIntoJSON{
	String cache, search, query;
	String filename;
	String path;
	InputStream stream;
	JsonObject object;
	private void setup() throws IOException, ParserConfigurationException, SAXException {
		path = "D:\\workspace\\W09\\" + cache + "\\";
		String link = "http://dblp.org/search/" + search + "/api?q=" + URLEncoder.encode(query, "UTF-8") + "&format=json&h=40&c=0";
		URL url = new URL(link);
		filename = URLEncoder.encode(link, "UTF-8");
		path = path + filename + ".json";
		File file = new File(path);
		
		if(!file.exists()) {
			saveFile(url, path);
		}
		readIntoDOM(path);
	}
	
	protected void readIntoDOM(String path) throws IOException, ParserConfigurationException, SAXException {
		JsonReader reader = Json.createReader(new BufferedReader(new FileReader(path)));
		object = reader.readObject();
		reader.close();
		
	}
	
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
	 * This method is to obtain the second call information. Since the secondlink cannot 
	 * get JSON response, so still keep in XML file
	 * @param object the object which has been parsed by Json
	 * @throws Exception
	 */
	private void authorDetails(JsonObject object) throws Exception{
		JsonObject hit = object.getJsonObject("result").getJsonObject("hits");
		JsonArray hits =hit.getJsonArray("hit");
		for(int i = 0; i < hits.size(); i++) {
			JsonObject info = hits.getJsonObject(i).getJsonObject("info");
			String name = info.getString("author");
			String secondLink = info.getString("url") + ".xml";
			secondCall(secondLink, name);
		}
	}
	
	private void secondCall(String link, String name) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
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
	    	System.out.println(name + " - " + publ + " publications with " + number + " co-authors.");
	    }
	}
	
	private void publicationDetails() {
		JsonObject hit = object.getJsonObject("result").getJsonObject("hits");
		JsonArray hits =hit.getJsonArray("hit");
		String title;
		int number = 0;
		for(int i = 0; i < hits.size(); i++) {
			JsonObject info = hits.getJsonObject(i).getJsonObject("info");
			title = info.getString("title");
			
			JsonArray authors = info.getJsonObject("authors").getJsonArray("author");
			number = authors.size();
			System.out.println(title + " (number of authors: " + number + ")");
		}
	}
	
	private void venueDetails() {
		JsonObject hit = object.getJsonObject("result").getJsonObject("hits");
		JsonArray hits =hit.getJsonArray("hit");
		for(int i = 0; i < hits.size(); i++) {
			JsonObject info = hits.getJsonObject(i).getJsonObject("info");
			String venue = info.getString("venue");
			System.out.println(venue);
		}
	}
	
	public void doing() throws Exception {
		setup();
		actions();
	}
	private void actions() throws Exception {
		switch(search) {
		case "author": authorDetails(object); break;
		case "publ": publicationDetails(); break;
		case "venue": venueDetails(); break;
		default: System.out.println("Invalid search action!");
		}
	}

	public ReadIntoJSON(String cache, String search, String query) {
		this.cache = cache;
		this.search = search;
		this.query = query;
	}
	
	public static void main(String[] args) {
		//in order to make it clear in the W09Extension.java, I put the main class of this extension
		//in this class directly, just change the arguments below and you can find
		//the files including xml and json in "json" directory
		ReadIntoJSON read = new ReadIntoJSON("json", "author", "jack cole");
		try {
			read.doing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
