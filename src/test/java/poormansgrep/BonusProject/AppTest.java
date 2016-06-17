package poormansgrep.BonusProject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

	@Test
	public void createFile() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("index.txt", "UTF-8");
		writer.println("the red cross");
		writer.close();
	}

	@Test
	public void checkFile() throws FileNotFoundException, IOException {
		File file = new File("index.txt");
		Assert.assertEquals(file.length() == 0 && file.exists(), "index.txt");
	}

	@Test
	public void testCommands() throws Exception {
		String[] commands = new String[] { "-l", "-i" };
		Assert.assertEquals(commands, outContent.toByteArray());
	}

	/**
	 * Two testing methods testing if the output lines equals the expected
	 * string
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGrepReturn() throws FileNotFoundException, IOException {
		// to create file and write to it use this example
		try {
			byte[] byteContent = "red".getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Grep.GrepTest(new String[] { "red", "./test.txt" });
		Assert.assertEquals("red", outContent.toString());
	}

	@Test
	public void testGrepReturn2() throws FileNotFoundException, IOException {
		try {
			byte[] byteContent = "red\nred".getBytes();
			fop.write(byteContent);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Grep.GrepTest(new String[] { "red", "./test.txt" });
		Assert.assertEquals("red\nred", outContent.toString());
	}
}