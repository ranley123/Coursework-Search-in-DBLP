/**
 * Main method to run the program
 * check arguments: cache, search, query
 * 
 * @author 170011474
 * @date 10th April
 * @version 1.0
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.Map;
public class W09Practical {
	static Map<String, String> arguments = new HashMap<>();
	static String path = "D:\\workspace\\W09\\";
	
	/*
	 * Check if the args[index] is valid arguments: --search, --cache, and --query
	 * @param args the array which has to be checked
	 * @param index the index of the array
	 */
	public static boolean arguments(String[] args, int index) {
		return args[index].equals("--search") || args[index].equals("--cache") || args[index].equals("--query");
	}
	
	/*
	 * Check if the args[index] followin the "--search" is valid: publication, author, or venue
	 * @param args the array which has to be checked
	 * @param index the index of the array
	 */
	public static boolean values(String[] args, int index) {
		return args[index].equals("publication") ||args[index].equals("author") || args[index].equals("venue");
	}
	
	/*
	 * Check cache
	 * 1. if it has missing value
	 * 2. if it has invalid value, for example, the program cannot find the cache
	 * @param args the array which has to be checked
	 * @param index
	 */
	public static void checkCache(String[] args, int index) {
		String line = args[index];
		String cacheResult = "";
		char letter;
		for(int i = 0; i < args[index].length(); i++) {
			letter = line.charAt(i);
			if(Character.isLetter(letter)) {
				cacheResult = cacheResult + letter;
			}
		}
		
			Path p = Paths.get(path + cacheResult);
			if (!Files.exists(p)) {
				System.out.println("Invalid directory " + args[index]);
			    System.exit(0);
		    }
			arguments.put(args[index - 1], cacheResult);
		
	}
	
	public static void checkSearch(String[] args, int index) {// index = i+1
		if(!values(args, index)) {
			System.out.println("Invalid value for " + args[index - 1] + ": " + args[index]);
			System.out.println("Malformed command line arguments.");
			System.exit(0);
		}
		else {
			if(args[index].equals("publication")) {
				args[index] = "publ";
			}
			arguments.put(args[index - 1], args[index]);
		}
	}
	
	public static void checkQuery(String[] args, int index) {
		arguments.put(args[index - 1], args[index]);
	}
	
	public static void checkMissing(String[] args, int index) {
		if(index == args.length) {
			System.out.println("Missing value for " + args[index - 1]);
			System.out.println("Malformed command line arguments.");
			System.exit(0);
		}
		else if(arguments(args, index)) {
			if(args[index - 1].equals("--cache")) {
				System.out.println("Cache directory doesn't exist: " + args[index]);
			}
			else {
				System.out.println("Missing value for " + args[index - 1]);
				System.out.println("Malformed command line arguments.");
			}
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		if(args.length != 6) {
			for(int i = 0; i < args.length; i = i + 2) {
				checkMissing(args, i + 1);
			}
		}
		else {
			for(int i = 0; i < args.length; i = i + 2) {
			if(args[i].equals("--search")) {
				checkSearch(args, i+1);
			}
			else if(args[i].equals("--cache")) {
				if(arguments(args, i+1)) {
					arguments.put(args[i], "");
					i = i - 1;
				}
				else {
					checkCache(args, i+1);
				}
			}
			else if(args[i].equals("--query")) {
				checkQuery(args, i+1);
			}
		}
		}
		String cache = arguments.get("--cache");
		String search = arguments.get("--search");
		String query = arguments.get("--query");
		
		try {
			Read process = new Read(cache, search, query);
			process.doing();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
