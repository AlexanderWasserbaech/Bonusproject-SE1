package poormansgrep.BonusProject;

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

	private static FileInputStream fin;
	private static Scanner sc;

	/**
	 * Implements grep options -l and -i and uses singleSearch and multiSearch
	 * of class search. Returns nothing but a console output regarding the
	 * options and given textfile/s or pipe as input.
	 * 
	 * @param commands
	 *            input as array given via console
	 * @throws FileNotFoundException
	 *             when given path is wrong or file does not exist
	 * @throws IOException
	 */
	public static void grepTest(String[] commands) {
		String[] str = { "", "" }, fileName = new String[2];
		String key = "";
		Boolean l = false, i = false;
		int j2 = 0, fileCount = 0, keyPos = 0;
		int save = 0;
		Path p;
		try {
			if (commands == null || commands.length == 0) { // catch empty commands array
				throw new NullPointerException();
			} else {
				// evaluate given commands
				for (int j = 0; j < commands.length; j++) {
					if (commands[j].charAt(0) == '-') {
						if (commands[j].toCharArray().length != 2) {
							throw new IllegalArgumentException();
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
							throw new IllegalArgumentException();
						}

					} else if (j == j2) {
						key = commands[j];
						keyPos = j;
					} else if (commands[j].contains(".txt")) {
						fileCount++;
					}
				}
				// check how many files were given
				switch (fileCount) {
				case 1:
					if (commands.length == keyPos + 3) {
						System.out.print("One filename or path was corrupt");
						throw new Exception();
					}
					if (commands[save].equals("-l")) {
						System.out.print("Wrong command for single file");
						throw new Exception();
					}
					fin = new FileInputStream(new File(commands[commands.length - 1]));
					sc = new Scanner(fin, "UTF-8");
					while (sc.hasNextLine()) {
						str[0] = str[0] + (sc.hasNextLine() ? sc.nextLine() + "\n" : "");
					}
					sc.close();
					System.out.print(Search.singleSearch(key, str[0], i));
					break;
				case 2:
					// extract file name
					for (int f = 0; f < 2; f++) {
						p = Paths.get(commands[commands.length - 2 + f]);
						fileName[f] = p.getFileName().toString();
					}
					// first file input
					fin = new FileInputStream(new File(commands[commands.length - 2]));
					sc = new Scanner(fin, "UTF-8");
					while (sc.hasNextLine()) {
						str[0] = str[0] + (sc.hasNextLine() ? sc.nextLine() : "") + "\n";
					}
					sc.close();
					fin.close();
					// second file input
					fin = new FileInputStream(new File(commands[commands.length - 1]));
					sc = new Scanner(fin, "UTF-8");
					while (sc.hasNextLine()) {
						str[1] = str[1] + (sc.hasNextLine() ? sc.nextLine() : "") + "\n";
					}
					sc.close();
					fin.close();
					System.out.print(Search.multiSearch(key, str, i, l, fileName));
					break;
				// Piping or no file input
				default:
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(baos);
					PrintStream old = System.out;
					System.setOut(ps);
					System.out.print("");
					System.out.flush();
					System.setOut(old);
					str[0] = baos.toString();
					if (str[0].isEmpty()) {
						System.out.print("No textfile was given or empty piping");
						throw new Exception();
					}
					System.out.println(Search.singleSearch(key, str[0], i));
					break;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.print("File not found");
		} catch (IllegalArgumentException e) {
			System.out.print("Unknown command");
		} catch (NullPointerException e) {
			System.out.print("No Commands were given, for usage give commands in following form:\n\n"
					+ "grep [-i] [-l] searchString [file 1] [file 2],\n"
					+ "whereas -i is 'case sensitive' and -l 'output filename'");
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String[] commands = args;
		grepTest(commands);
		System.out.println("\n");
	}
}
