/*******************************************************************************
 * Copyright (c) 2010 Costantino Cerbo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Costantino Cerbo - initial API and implementation
 ******************************************************************************/
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
