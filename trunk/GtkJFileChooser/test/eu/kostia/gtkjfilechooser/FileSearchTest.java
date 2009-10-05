package eu.kostia.gtkjfilechooser;

import java.io.File;

public class FileSearchTest {
	
	public void testStart() throws Exception {
		FileSearch search = new FileSearch(System.getProperty("user.home"), "test", new FileSearch.FileSearchHandler(){
			@Override
			public void found(File file) {
				System.out.println(file.getPath());				
			}			
		});
		
		search.start();
		
		Thread.sleep(2000);
		
		search.stop();
	}

	public static void main(String[] args) throws Exception {
		FileSearchTest test = new FileSearchTest();
		test.testStart();		
	}
}
