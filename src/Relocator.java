import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Relocates a .m3u playlist to a different directory
 * @author Nicholas Gadjali
 */
public class Relocator {
	/**
	 * Singleton Scanner
	 */
	public static final Scanner s = new Scanner(System.in);
	
	/**
	 * Text Mode Main Method
	 * @param args Playlist Filename
	 */
	public static void main(String[] args) {
		String filename = ""; //Find & verify file
		File file = null;
		if (args.length != 1) {
			System.out.println("Invalid command-line input.");
			System.out.print("Enter playlist filename: ");
			filename = s.nextLine();
			file = findFile(filename);
			while (file == null) {
				System.out.println("Did not find that file.  Try again.");
				System.out.print("Enter playlist filename: ");
				filename = s.nextLine();
				file = findFile(filename);
			}
		}
		
		Relocator r = new Relocator(file);
		if (r.write()) System.out.println("Operation Successful");
		else System.out.println("An Error Occured");
		
		s.close();
	}
	
	/**
	 * Find the file with the directory specified
	 * @param s The path to the file
	 * @return File if found, null if not found
	 */
	public static File findFile(String s) {
		File f = new File(s);
		if (f.exists()) return f;
		else return null;
	}
	
	/**
	 * Constructor for Relocator
	 * @param f The file to process
	 */
	public Relocator(File f) {
		this.origFile = f;
		this.data = new LinkedList<>();
		if (readFile(f)) System.out.println("File Read Successfully.");
		else System.out.println("File Read Was Unsuccessfull");
		
		System.out.print("What is the current path to the folder?: ");
		this.from = s.nextLine();
		System.out.print("What is the new path?: ");
		String checkEnd = s.nextLine();
		if (checkEnd.substring(checkEnd.length() - 1).equals(from.substring(from.length() - 1)))
			this.to = checkEnd;
		else {
			switch (checkEnd.substring(checkEnd.length() - 1)) {
			case "\\":
				this.to = checkEnd.substring(0, checkEnd.length() - 1);
				break;
			default:
				this.to = checkEnd + "\\";
			}
		}
	}
	
	/**
	 * Constructor for Relocator
	 * @param f The file to process
	 * @param from The current playlist folder path
	 * @param to The new desired folder path
	 */
	public Relocator(File f, String from, String to) {
		this.origFile = f;
		this.data = new LinkedList<>();
		readFile(f);
		this.from = from;
		String checkEnd = to;
		if (checkEnd.substring(checkEnd.length() - 1).equals(from.substring(from.length() - 1)))
			this.to = checkEnd;
		else {
			switch (checkEnd.substring(checkEnd.length() - 1)) {
			case "\\":
				this.to = checkEnd.substring(0, checkEnd.length() - 1);
				break;
			default:
				this.to = checkEnd + "\\";
			}
		}
	}
	
	/**
	 * Read file data and store into LinkedList instance variable
	 * @param f The file to read
	 * @return If successful
	 */
	private boolean readFile(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = "";
			while ((s = br.readLine()) != null) {
				data.add(s);
			}
			br.close();
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			ie.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Replace all instances of /from/ found in the playlist with /to/
	 * @return If successful
	 */
	public boolean write() {
		String savePath = origFile.getAbsolutePath();
		String saveName = "rel_" + origFile.getName();
		savePath = savePath.replace(origFile.getName(), saveName);
		//System.out.println("Saving to: " + savePath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(savePath)));
			while (!data.isEmpty()) {
				String temp = data.removeFirst();
				temp = temp.replace(from, to);
				bw.write(temp);
				if (!data.isEmpty()) bw.write("\n");
			}
			
			bw.close();
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			ie.printStackTrace();
			return false;
		}
		return true;
	}
	
	private final File origFile;
	private LinkedList<String> data;
	private final String from;
	private final String to;
}
