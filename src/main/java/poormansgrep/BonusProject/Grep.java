package poormansgrep.BonusProject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.Scanner;

import poormansgrep.BonusProject.Search;
public class Grep {
	/**
	 * Implements grep options -l and -i and uses singleSearch and multiSearch of class search.
	 * Returns nothing but a console output regarding the options and given textfile/s 
	 * as input.
	 * 
	 * @param commands
	 * 			input as array given via console
	 * @throws FileNotFoundException
	 * 			when given path is wrong or file does not exist
	 * @throws IOException
	 */
	static BufferedReader source;
	static FileInputStream fin;
	static Scanner sc;
	public static void GrepTest(String[] commands) throws FileNotFoundException, IOException{
		String[] str = {"",""}, fileName = new String[2];
		String key = "";
		Boolean l = false,i = false;
		int j2 = 0, fileCount = 0;
		int save = 0;
		Path p;
		try{
		if (commands.length == 0){
			System.out.print("No Commands were given, for usage give commands in following form:\n\n"
								+ "grep [-i] [-l] searchString [file 1] [file 2]\n"
								+ "whereas -i is 'case sensitive' and -l 'output filename'");
			throw new Exception();
		} else {
			for(int j = 0; j< commands.length; j++){
				if (commands[j].charAt(0)== '-'){
					if(commands[j].toCharArray().length>2){
						System.out.print("Unknown command");
						throw new Exception();
					}
					switch (commands[j]) {
					case "-i":
						i = true;
						j2++;
						save = j;
						break;
					case "-l":
						l = true;
						j2++;
						break;
					default:
						break;						
					}
					
				} else if (j==j2) {
					key = commands[j];
				} else if (commands[j].contains(".txt")){
					fileCount++;
				}
			}
				switch (fileCount) {
				case 1:
					if(commands[save].equals("-l")){
						System.out.print("Wrong command for single file");
						throw new Exception();
					}
					fin = new FileInputStream(new File(commands[commands.length-1]));
					sc = new Scanner(fin, "UTF-8");
					while(sc.hasNextLine()){
						str[0] = str[0] + (sc.hasNextLine() ? sc.nextLine()+ "\n": "");
					}
					sc.close();
					System.out.print(Search.singleSearch(key, str[0], i));
					break;
				case 2:
					// extract file name
					for(int f = 0; f<2; f++){
						p = Paths.get(commands[commands.length-2 + f]);
						fileName[f] = p.getFileName().toString();
					}
					// first file input
					fin = new FileInputStream(new File(commands[commands.length-2]));
					sc = new Scanner(fin, "UTF-8");
					while(sc.hasNextLine()){
						str[0] = str[0] + (sc.hasNextLine() ? sc.nextLine() : "") + "\n";
					}
					sc.close();
					fin.close();
					// second file input
					fin = new FileInputStream(new File(commands[commands.length-1]));
					sc = new Scanner(fin, "UTF-8");
					while(sc.hasNextLine()){
						str[1] = str[1] + (sc.hasNextLine() ? sc.nextLine() : "") + "\n";
					}
					sc.close();
					fin.close();
					System.out.print(Search.multiSearch(key, str, i,l, fileName));
					break;
				default:
				    ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    PrintStream ps = new PrintStream(baos);
				    PrintStream old = System.out;
				    System.setOut(ps);
				    System.out.print("");
				    System.out.flush();
				    System.setOut(old);
				    str[0] = baos.toString();
				    if(str[0].isEmpty()){
				    	System.out.print("No textfile was given or empty piping");
				    	throw new Exception();
				    }
					System.out.println(Search.singleSearch(key, str[0], i));
					break;
				}
			}
		} catch (FileNotFoundException e){
			System.out.print("File not found");
		} catch (Exception e) {
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		String[] commands = args;
		GrepTest(commands);
		
		//following code is only for testing purpose
		
		int i = 0;
		for(String s: args){
			if(i==0){
				System.out.println("\nThe following commands were given to GrepTest");
			}
			System.out.println(s);
			commands[i] = s;
			i++;
		}

		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		System.out.println(outContent.toString());
	}
}
