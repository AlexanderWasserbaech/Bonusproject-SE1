package poormansgrep.BonusProject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
//<<<<<<< HEAD
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

//=======
//>>>>>>> origin/master

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	   @Before
	   public void setUpStreams() {
	       System.setOut(new PrintStream(outContent));
	   }

	   @After
	   public void cleanUpStreams() {
	       System.setOut(null);
	       outContent.reset();
	   }
	   
	@Test
	public void createFile() throws FileNotFoundException, UnsupportedEncodingException{	
		PrintWriter writer = new PrintWriter("index.txt", "UTF-8");
		writer.println("the red cross");
		writer.close();
	}
		
	@Test
	public void checkFile() throws FileNotFoundException, IOException{	
		File file = new File ("index.txt");
		Assert.assertEquals(file.length() == 0 && file.exists(), "index.txt");
	}
	
	@Test 
	public void testCommands() throws Exception{
		String[] commands = new String [] {"-l", "-i"};
		Assert.assertEquals(commands, outContent.toByteArray());
	}
	  
    /**
     * Two testing methods testing if the output lines equals the expected string
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGrepReturn() throws FileNotFoundException, IOException {
       Grep.GrepTest(new String[]{"red","D:/workspace/test.txt"});
       Assert.assertEquals("red",outContent.toString());
    }
    @Test
    public void testGrepReturn2() throws FileNotFoundException, IOException {
    	Grep.GrepTest(new String[]{"red","D:/workspace/test2.txt"});
        Assert.assertEquals("red\nred",outContent.toString());
    }
}