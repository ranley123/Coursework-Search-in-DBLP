/*
 * Main class to run Extensions:
 * 1. Read information from Wikipedia Extension
 * 2. Output html file from xml file in a table Extension
 * Check the usage
 * @author 170011474
 * @version 1.0
 */
public class W09Extension {
	public static void main(String[] args) throws Exception {
		//check the usage and promt a proper error message
		if(args.length != 2) {
			System.out.println("Usage: java W09Extension <publication> <query in wikipedia>");
			System.exit(0);
		}
		
		try {
			ReadIntoHTML read = new ReadIntoHTML(args[0]);
		    read.setup();
			
		    ReadEx read2 = new ReadEx(args[1]);
		    read2.doing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
