package poormansgrep.BonusProject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

	/**
	 * Creates Outputstream of console and creates test file for use in several
	 * tests
	 */
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		try {
			file = new File("./input.txt");
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

	/**
	 * Test Checks if the testing file itself exists
	 */
	@Test
	public void checkTestFileExists() {
		Assert.assertTrue(file.exists());
	}

	/**
	 * Test checks for exceptions when null or empty array given to grepTest
	 */
	@Test
	public void testNullOrEmptyArray() {
		Grep.grepTest(null);
		String[] commands = new String[0];
		Grep.grepTest(commands);
	}

	/**
	 * Test testGrep with huge textfile
	 */
	@Test
	public void testGrepHugeFile() {
		String content = HugeString.hugeString;
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] commands = { "vel", "./input.txt" };
		Grep.grepTest(commands);
	}

	/**
	 * Check if GrepTest can handle an empty file
	 */
	@Test
	public void testGrepEmptyFile() {
		String[] commands = { "red", "./input.txt" };
		String content = "", expected = "No match.";
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Grep.grepTest(commands);
		String actual = outContent.toString();
		Assert.assertEquals(expected, actual);
		System.setOut(null);
		outContent.reset();
	}

	/**
	 * Test checks if special characters are found by testGrep
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testSpecialCharacters() {
		String[][] commands = { { "!", "./input.txt" }, { "§", "./input.txt" }, { "$", "./input.txt" },
				{ "%", "./input.txt" }, { "&", "./input.txt" }, { "(", "./input.txt" }, { "}", "./input.txt" },
				{ "_", "./input.txt" }, { "#", "./input.txt" }, { "*", "./input.txt" }, { "~", "./input.txt" }, };
		String[] expected = { "!", "§", "$", "%", "&", "(", "}", "_", "#", "*", "~" };
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
		for (int i = 0; i < expected.length; i++) {
			System.setOut(new PrintStream(outContent));
			Grep.grepTest(commands[i]);
			actuals[i] = outContent.toString();
			System.setOut(null);
			outContent.reset();
		}
		Assert.assertArrayEquals(expected, actuals);

	}

	/**
	 * testing testGrep with wrong command input
	 */
	@Test
	public void testWrongCommandInput() {
		String content = "test";
		String[][] commands = { { "-k", "red", "./input.txt" }, { "cd", "red" }, { "-jk", "red" }, { "-kda", "red" } };
		String[] expecteds = { "Unknown command", "No textfile was given or empty piping", "Unknown command",
				"Unknown command" };
		String[] actuals = new String[expecteds.length];
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < expecteds.length; i++) {
			System.setOut(new PrintStream(outContent));
			Grep.grepTest(commands[i]);
			actuals[i] = outContent.toString();
			System.setOut(null);
			outContent.reset();
		}
		Assert.assertArrayEquals(expecteds, actuals);
	}

	/**
	 * Testing method to check sysout of TestGrep with many different command
	 * inputs and wrong input
	 */
	@Test
	public void testGrepReturnSingleFile() {
		String[][] commands = { { "red", "./input.txt" }, { "-i", "red", "./input.txt" },
				{ "-l", "red", "./input.txt" }, { "blue", "./input.txt" }, { "red", "./wronginput.txt" }, { "red" },
				{ "-adwdawda", "red", "./input.txt" }, { "red", "./input.tct" } };
		String[] expected = { "Red wine is tasty\nThe red cross acts worldwide", "The red cross acts worldwide",
				"Wrong command for single file", "No match.", "File not found", "No textfile was given or empty piping",
				"Unknown command", "No textfile was given or empty piping" };
		String[] actuals = new String[expected.length];
		String content = "Roses are nice flowers.\n" + "Red wine is tasty\n" + "The red cross acts worldwide\n"
				+ "Mayflower used to be a ship.";
		try {
			byte[] byteContent = content.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < expected.length; i++) {
			System.setOut(new PrintStream(outContent));
			Grep.grepTest(commands[i]);
			actuals[i] = outContent.toString();
			System.setOut(null);
			outContent.reset();
		}
		Assert.assertArrayEquals(expected, actuals);
	}

	/**
	 * Testing TestGrep for two files with many different command inputs
	 */
	@Test
	public void testGrepReturnTwoFiles() {
		File file2;
		String[][] commands = { { "red", "./input.txt", "./inputSecond.txt" },
				{ "-i", "red", "./input.txt", "./inputSecond.txt" },
				{ "-l", "red", "./input.txt", "./inputSecond.txt" }, { "blue", "./input.txt", "./inputSecond.txt" },
				{ "red", "./wronginput.txt", "./inputSecond.txt" }, { "red" },
				{ "-adwdawda", "red", "./input.txt", "./inputSecond.txt" },
				{ "red", "./input.tct", "./inputSecond.txt" } };
		String[] expected = {
				"input.txt: Red wine is tasty\ninput.txt: The red cross acts worldwide\n"
						+ "inputSecond.txt: Errors will show up in red.",
				"input.txt: The red cross acts worldwide\n" + "inputSecond.txt: Errors will show up in red.",
				"input.txt\n" + "inputSecond.txt", "No match.", "File not found",
				"No textfile was given or empty piping", "Unknown command", "One filename or path was corrupt" };
		String[] actuals = new String[expected.length];
		String content = "Roses are nice flowers.\n" + "Red wine is tasty\n" + "The red cross acts worldwide\n"
				+ "Mayflower used to be a ship.", content2 = "Errors will show up in red.\n" + "Let's start bug fixing";
		try {
			file2 = new File("./inputSecond.txt");
			FileOutputStream fop2 = new FileOutputStream(file2);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			byte[] byteContent = content.getBytes(), byteContent2 = content2.getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
			fop2.write(byteContent2);
			fop2.flush();
			fop2.close();

			for (int i = 0; i < expected.length; i++) {
				System.setOut(new PrintStream(outContent));
				Grep.grepTest(commands[i]);
				actuals[i] = outContent.toString();
				System.setOut(null);
				outContent.reset();
			}
			if (fop2 != null) {
				fop2.close();
				file2.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertArrayEquals(expected, actuals);
	}
}