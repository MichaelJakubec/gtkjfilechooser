package eu.kostia.gtkjfilechooser;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

public class FileSearchTest {
	
	@Test
	@Ignore("Test started with main")
	public void testStartedAndStopped() throws Exception {
		FileSearch search = new FileSearch(System.getProperty("user.home"), "test", new FileSearch.FileSearchHandler(){
			@Override
			public void found(File file) {
				System.out.println(file.getPath());				
			}

			@Override
			public void finished(Status status) {
				System.out.println("Search " + (status == Status.COMPLETED ? "completed!" : "interruped!"));						
			}			
		});
		
		search.start();
		
		Thread.sleep(2000);
		
		search.stop();
	}
	
	@Test
	@Ignore("Test started with main")
	public void testStarted() throws Exception {
		FileSearch search = new FileSearch(".", "test", new FileSearch.FileSearchHandler(){
			@Override
			public void found(File file) {
				System.out.println(file.getName());				
			}

			@Override
			public void finished(Status status) {
				System.out.println("Search " + (status == Status.COMPLETED ? "completed!" : "interruped!"));						
			}			
		});
		
		search.start();
	}

	public static void main(String[] args) throws Exception {
		FileSearchTest test = new FileSearchTest();
//		test.testStartedAndStopped();		
		test.testStarted();
	}
}
