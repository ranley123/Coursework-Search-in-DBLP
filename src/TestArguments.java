import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TestArguments {
	static Map<String, String> arguments = new HashMap<>();
	static String path = "D:\\workspace\\W09\\";
	
	public static boolean arguments(String[] args, int index) {
		return args[index].equals("--search") || args[index].equals("--cache") || args[index].equals("--query");
	}
	public static boolean values(String[] args, int index) {
		return args[index].equals("publication") ||args[index].equals("author") || args[index].equals("venue");
	}
	
	public static void checkCache(String[] args, int index) {
		if(index == args.length) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else if(arguments(args, index)) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else {
			Path p = Paths.get(path + args[index]);
			if (!Files.exists(p)) {
				System.out.println("Invalid directory " + args[index]);
			    System.out.println("Malformed command line arguments.");
			    return;
		    }
			arguments.put(args[index - 1], args[index]);
		}
	}
	
	public static void checkSearch(String[] args, int index) {// index = i+1
		//when the action is the last argument
		if(index == args.length) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		//when the action is followed by an action but not a valid value
		else if(arguments(args, index)) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		//when the object is not "publication" or "author" or "venue"
		else if(!values(args, index)) {
			System.out.println("Invalid value for " + args[index - 1] + ": " + args[index]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else {
			if(args[index].equals("publication")) {
				args[index] = "publ";
			}
			arguments.put(args[index - 1], args[index]);
		}
	}
	
	public static void checkMissing(String[] args, int index) {
		if(index == args.length) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else if(arguments(args, index)) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
	}
	
	public static void checkQuery(String[] args, int index) {
		if(index == args.length) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else if(arguments(args, index)) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			return;
		}
		else {
			arguments.put(args[index - 1], args[index]);
		}
	}
	
	
		
		public static void main(String[] args) {
			if(args.length != 6) {
				for(int i = 0; i < args.length; i = i + 2) {
					
				}
			}
			
			for(int i = 0; i < args.length; i = i + 2) {
				if(args[i].equals("--search")) {
					checkSearch(args, i+1);
				}
				if(args[i].equals("--cache")) {
					checkCache(args, i+1);
				}
				if(args[i].equals("--query")) {
					checkQuery(args, i+1);
				}
			}
			
			
			String cache = arguments.get("--cache");
			String search = arguments.get("--search");
			String query = arguments.get("--query");
		System.out.println(cache + search + query);
		try {
			Read process = new Read(cache, search, query);
			process.doing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
