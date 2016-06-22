package poormansgrep.BonusProject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing for grep class which calls itself the search class tests are for
 * several in and output issues
 */
public class AppTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	static FileOutputStream fop = null;
	static File file;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		try {
			file = new File("./test.txt");
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		outContent.reset();
		try {
			if (fop != null) {
				fop.close();
				file.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void createFile() throws FileNotFoundException, UnsupportedEncodingException {
//		PrintWriter writer = new PrintWriter("index.txt", "UTF-8");
//		writer.println("the red cross");
//		writer.close();
//	}
//
//	@Test
//	public void checkFile() throws FileNotFoundException, IOException {
//		File file = new File("index.txt");
//		Assert.assertEquals(file.length() == 0 && file.exists(), "index.txt");
//	}
//
//	@Test
//	public void testCommands() throws Exception {
//		String[] commands = new String[] { "-l", "-i" };
//		Assert.assertEquals(commands, outContent.toByteArray());
//	}
	
	@Test
	public void testSpecialCharacters() throws FileNotFoundException, IOException{
		String[][] commands = {{"!","./test.txt"},{"§","./test.txt"},{"$","./test.txt"},
				{"%","./test.txt"},{"&","./test.txt"},{"(","./test.txt"},
				{"}","./test.txt"},{"_","./test.txt"},
				{"#","./test.txt"},{"*","./test.txt"},{"~","./test.txt"},};
		String[] expected = {"!","§","$","%","&","(","}","_","#","*","~"};
		String[] actuals = new String[expected.length];
		String content = "!\n§\n$\n%\n&\n(\n}\n_\n-\n#\n*\n~";
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i<expected.length;i++){
			System.setOut(new PrintStream(outContent));
			Grep.GrepTest(commands[i]);
			actuals[i] = outContent.toString();
			System.setOut(null);
			outContent.reset();
		}
		Assert.assertArrayEquals(expected, actuals);
		
	}
	/**
	 * Check if GrepTest can handle an empty file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testGrepEmptyFile() throws FileNotFoundException, IOException{
		String[] commands = {"red","./test.txt"};
		String content = "", expected = "";
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Grep.GrepTest(commands);
		String actual = outContent.toString();
		Assert.assertEquals(expected, actual);
		System.setOut(null);
		outContent.reset();
	}
	/**
	 * Testing method to check sysout of TestGrep with sometimes wrong command input
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGrepReturnSingleFile() throws FileNotFoundException, IOException {
		// to create file and write to it use this example
		String[][] commands = {{"red","./test.txt"},{"-i","red","./test.txt"},{"-l","red","./test.txt"},
					{"blue","./test.txt"},{"red","./testfalsch.txt"},{"red"},{"-adwdawda","red","./test.txt"},
					{"red","./test.tct"}};
		String[] expected = {"Red is a nice color,\neven red roses are better than",
							"even red roses are better than",
							"Wrong command for single file",
							"blue roses.\n" + "Just Kidding, there are no blue roses",
							"File not found",
							"No textfile was given or empty piping",
							"Unknown command",
							"No textfile was given or empty piping"};
		String[] actuals = new String[expected.length];
		String content = "Red is a nice color,\n"
				+ "atleast for some,\n"
				+ "even red roses are better than\n"
				+ "blue roses.\n"
				+ "Just Kidding, there are no blue roses";
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i<expected.length;i++){
			System.setOut(new PrintStream(outContent));
			Grep.GrepTest(commands[i]);
			actuals[i] = outContent.toString();
			System.setOut(null);
			outContent.reset();
		}
		Assert.assertArrayEquals(expected, actuals);
	}
	
	/**
	 * Testing TestGrep for two files
	 */
	@Test
	public void testGrepReturnTwoFiles(){
		File file2;
		String[][] commands = {{"red","./test.txt","./test2.txt"},{"-i","red","./test.txt","./test2.txt"},{"-l","red","./test.txt","./test2.txt"},
			{"blue","./test.txt","./test2.txt"},{"red","./testfalsch.txt","./test2.txt"},{"red"},{"-adwdawda","red","./test.txt","./test2.txt"},
			{"red", "./test.tct","./test2.txt"}};
		String[] expected = {"test.txt: Red is a nice color,\ntest.txt: even red roses are better than\n"
				+ "test2.txt: red roses.\ntest2.txt: Just Kidding, there are no red roses",
				"test.txt: even red roses are better than\n"
				+ "test2.txt: red roses.\n"
				+ "test2.txt: Just Kidding, there are no red roses",
				"test.txt\n"
				+ "test2.txt",
				"test.txt: blue roses.\n" + "test.txt: Just Kidding, there are no blue roses\n" + "test2.txt: Blue is a nice color,\n"
						+ "test2.txt: even blue roses are better than",
				"File not found",
				"No textfile was given or empty piping",
				"Unknown command",
				"One filename or path was corrupt"}; //TODO Only one File gets detected!!
		String[] actuals = new String[expected.length];
		String content = "Red is a nice color,\n"
				+ "atleast for some,\n"
				+ "even red roses are better than\n"
				+ "blue roses.\n"
				+ "Just Kidding, there are no blue roses",
				content2 = "Blue is a nice color,\n"
					+ "atleast for some,\n"
					+ "even blue roses are better than\n"
					+ "red roses.\n"
					+ "Just Kidding, there are no red roses";
		try {
			file2 = new File("./test2.txt");
			FileOutputStream fop2 = new FileOutputStream(file2);
			if(!file2.exists()){
				file2.createNewFile();
			}
			byte[] byteContent = content.getBytes(), byteContent2 = content2.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
			fop2.write(byteContent2);
			fop2.flush();
			fop2.close();
			
			for(int i = 0; i<expected.length;i++){
				System.setOut(new PrintStream(outContent));
				Grep.GrepTest(commands[i]);
				actuals[i] = outContent.toString();
				System.setOut(null);
				outContent.reset();
			}
			if(fop2 != null){
				fop2.close();
				file2.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		Assert.assertArrayEquals(expected, actuals);	
	}
}