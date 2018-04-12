/*
 * W09 Extension 3 variant: Instead of the text format, output result as an HTML document in a table format
 * This class is to read "publication" information into HTML
 * 
 * @author 170011474
 * @version 1.0
 */
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.xml.sax.SAXException;
import java.net.URL;
import java.net.URLEncoder;

public class ReadIntoHTML{
	String query;
	String path;
	
	/*
	 * Construct the URL by using the argument
	 * Check existing
	 * @throws IOException, ParserConfigurationException, SAXException, TransformerException
	 */
	public void setup() throws IOException, ParserConfigurationException, SAXException, TransformerException {
		path = "D:\\workspace\\W09\\html\\";
		String link = "http://dblp.org/search/publ/api?q=" + URLEncoder.encode(query, "UTF-8") + "&h=40&c=0";
		URL url = new URL(link);
		String filename = URLEncoder.encode(link, "UTF-8");
		path = path + filename;
		//create a new File object that can be checked about the existing condition
		File file = new File(path + ".xml");
		
		if(!file.exists()) {
			saveFile(url, path + ".xml");
			Source xmlDoc = new StreamSource(new File(path + ".xml"));
			Source xslDoc = new StreamSource("product.xsl");
			transform(xmlDoc, xslDoc);
			//clickable();
		}
		else {
			System.out.println("file exists, just open the html file");
		}
	}
	
	/*
	 * Save file from the URL into the xml file
	 * @param url an absolute URL giving the base location of the searching result
	 * @param path the string which represents the location of the file
	 * @throws IOException, ParserConfigurationException, SAXException, TransformerException
	 */
	private void saveFile(URL url, String path) throws IOException, ParserConfigurationException, SAXException {
		InputStream stream = url.openStream();
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
	 * Transform xml file into another html file by using the xsl file that I wrote
	 * Prompt the string when all things have been done
	 * @param xmlDoc the original file of the xml file
	 * @param xslDoc the xsl file that I wrote to give the style sheet
	 * @throws IOException, TransformerException
	 */
	private void transform(Source xmlDoc, Source xslDoc) throws IOException, TransformerException {
		StringWriter sw = new StringWriter();
		FileWriter fw = new FileWriter(path + ".html");
		//transform xml to html by using the xslDoc
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(xslDoc);
		transformer.transform(xmlDoc, new StreamResult(sw));
		
		fw.write(sw.toString());
		fw.close();
		System.out.println("Sccessfully generate html file, now you can open it");
	}
	
	/*private void clickable() throws IOException {
		File file = new File(path + ".html");
		Document doc = Jsoup.parse(file, "UTF-8");
		doc.normalise();
		Elements elements = doc.getElementsByTag("tr");
		if(elements.size() > 1) {
			for(int i = 1; i < elements.size(); i++) {
				Element element = elements.get(i).child(5);
				String link = element.text();
				
				element.remove();
				Element newElement = elements.get(i).appendElement("td");
				newElement.addClass("colfmt");
				newElement.appendElement("a").attr("href", "\"" + link + "\"");
				newElement.text(link);
				System.out.println(newElement.child(1).tagName());

			}
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.write(doc.html());
			writer.flush();
			writer.close();
			System.out.println("hyperlink can be used");
		}
	}*/
	
	public ReadIntoHTML(String query) {
		this.query = query;
	}
}
