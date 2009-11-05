/*******************************************************************************
 * Copyright 2009 Costantino Cerbo.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact me at c.cerbo@gmail.com if you need additional information or
 * have any questions.
 *******************************************************************************/
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
