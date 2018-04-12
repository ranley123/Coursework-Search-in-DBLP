import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class UseXsl {
	private static void transform(Source xmlDoc, Source xslDoc) {
		StringWriter sw = new StringWriter();
		
		try {
			FileWriter fw = new FileWriter("product.html");
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xslDoc);
			transformer.transform(xmlDoc, new StreamResult(sw));
			fw.write(sw.toString());
			fw.close();
			
			System.out.println("Sccessfully generate html file");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String []args) {
		Source xmlDoc = new StreamSource(new File("http%3A%2F%2Fdblp.org%2Fsearch%2Fauthor%2Fapi%3Fq%3Djack%2Bcole%26h%3D40%26c%3D0.xml"));
		Source xslDoc = new StreamSource("product.xsl");
		transform(xmlDoc, xslDoc);
	}
}
