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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import eu.kostia.gtkjfilechooser.GKeyFile.Group;

public class GKeyFileTest  {
	private GKeyFile settings;

	private File fileini;

	public GKeyFileTest() throws Exception{
		String path = "/" + this.getClass().getPackage().getName().replace('.', '/');
		fileini = new File(getClass().getResource(path + "/example.ini").getFile());
		assertTrue(fileini.exists());
		settings = new GKeyFile(fileini);
	}

	@Test
	public void testGetSimpleString() throws Exception {
		Group group = settings.getGroup("First Group");
		System.out.println(group);
		String value1 = group.getString("key1");
		assertEquals("value1", value1);
		String value5 = group.getString("key5");
		assertEquals("value5", value5);
	}

	@Test
	public void testGetDouble() throws Exception {
		Group group = settings.getGroup("Third Group");
		System.out.println(group);
		double value1 = group.getDouble("double");
		assertEquals(10.5, value1, 1e-5);
	}

	@Test
	public void testGetBoolean() throws Exception {
		Group group = settings.getGroup("Third Group");
		System.out.println(group);
		Boolean value = group.getBoolean("boolean");
		assertEquals(true, value);
	}

	@Test
	public void testGetInteger() throws Exception {
		Group group = settings.getGroup("Third Group");
		System.out.println(group);
		int value = group.getInteger("integer");
		assertEquals(10, value);
	}

	@Test
	public void testAnotherGroup() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		String value2 = group.getString("key2");
		assertEquals("value2", value2);
	}

	@Test
	public void testGetStringEscaping() throws Exception {
		Group group = settings.getGroup("First Group");
		System.out.println(group);
		String value1 = group.getString("Name");
		assertEquals("Key File Example\tthis value shows\nescaping, also for slash \\.", value1);
	}

	@Test
	public void testGetStringLeadingSpaces() throws Exception {
		Group group = settings.getGroup("First Group");
		System.out.println(group);
		String value1 = group.getString("leading_spaces");
		assertEquals(" You can preserve leading spaces ", value1);
	}

	@Test
	public void testGetLocaleStringCountry() throws Exception {
		Group group = settings.getGroup("First Group");
		System.out.println(group);
		String value = group.getLocaleString("Welcome", Locale.FRANCE);
		assertEquals("Bonjour", value);
	}

	@Test
	public void testGetLocaleStringLang() throws Exception {
		Group group = settings.getGroup("First Group");
		System.out.println(group);
		String value = group.getLocaleString("Welcome", Locale.GERMAN);
		assertEquals("Hallo", value);
	}

	@Test
	public void testGetStringList() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<String> value = group.getStringList("list1");
		System.out.println(value);
		assertEquals(createList("a","b","c","x,y","d"), value);
	}

	@Test
	public void testGetStringListSpaces() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<String> value = group.getStringList("list3");
		System.out.println(value);
		assertEquals(createList("a","b","c"), value);
	}

	@Test
	public void testGetIntegerList() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<Integer> value = group.getIntegerList("Numbers");
		System.out.println(value);
		assertEquals(createList(2,20,-200,0), value);
	}

	@Test
	public void testGetIntegerSpaces() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<Integer> value = group.getIntegerList("list2");
		System.out.println(value);
		assertEquals(createList(1, 0 , -1), value);
	}

	@Test
	public void testGetBooleanList() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<Boolean> value = group.getBooleanList("Booleans");
		System.out.println(value);
		assertEquals(createList(true, false, true, true), value);
	}

	@Test
	public void testGetDoubleList() throws Exception {
		Group group = settings.getGroup("Another Group");
		System.out.println(group);
		List<Double> value = group.getDoubleList("Doubles");
		System.out.println(value);
		assertEquals("[1.5, 2.3, 4.0, 10.9]", value.toString());
	}

	@Test
	public void testSave0() throws Exception {
		StringWriter w = new StringWriter();
		settings.save(w);
		System.out.println(w);

		String expected = "\n"+//nl
		"[First Group]\n"+//nl
		"key1=value1\n"+//nl
		"key5=value5\n"+//nl
		"Name=Key File Example\\tthis value shows\\nescaping, also for slash \\.\n"+//nl
		"leading_spaces=\\sYou can preserve leading spaces\\s\n"+//nl
		"Welcome=Hello\n"+//nl
		"Welcome[de]=Hallo\n"+//nl
		"Welcome[fr_FR]=Bonjour\n"+//nl
		"Welcome[it]=Ciao\n"+//nl
		"Welcome[be@latin]=Hello\n"+//nl
		"\n"+//nl
		"[Another Group]\n"+//nl
		"Numbers=2;20;-200;0\n"+//nl
		"Booleans=true;false;true;true\n"+//nl
		"Doubles=1.5, 2.3, 4, 10.9\n"+//nl
		"key2=value2\n"+//nl
		"list1=a,b,c,x\\,y,d\n"+//nl
		"list2=1 ; 0 ; -1\n"+//nl
		"list3=a ; b ; c\n"+//nl
		"\n"+//nl
		"[Third Group]\n"+//nl
		"double=10.5\n"+//nl
		"boolean=true\n"+//nl
		"integer=10\n";//nl
		assertEquals(expected, w.toString());
	}

	@Test
	public void testSave1() throws Exception {
		assertTrue(fileini.exists());
		GKeyFile gKeyFile = new GKeyFile(fileini);
		gKeyFile.getGroup("First Group").setString("Welcome", "Benvenuto");
		StringWriter w = new StringWriter();
		gKeyFile.save(w);
		System.out.println(w);

		String expected = "\n"+//nl
		"[First Group]\n"+//nl
		"key1=value1\n"+//nl
		"key5=value5\n"+//nl
		"Name=Key File Example\\tthis value shows\\nescaping, also for slash \\.\n"+//nl
		"leading_spaces=\\sYou can preserve leading spaces\\s\n"+//nl
		"Welcome=Benvenuto\n"+//nl
		"Welcome[de]=Hallo\n"+//nl
		"Welcome[fr_FR]=Bonjour\n"+//nl
		"Welcome[it]=Ciao\n"+//nl
		"Welcome[be@latin]=Hello\n"+//nl
		"\n"+//nl
		"[Another Group]\n"+//nl
		"Numbers=2;20;-200;0\n"+//nl
		"Booleans=true;false;true;true\n"+//nl
		"Doubles=1.5, 2.3, 4, 10.9\n"+//nl
		"key2=value2\n"+//nl
		"list1=a,b,c,x\\,y,d\n"+//nl
		"list2=1 ; 0 ; -1\n"+//nl
		"list3=a ; b ; c\n"+//nl
		"\n"+//nl
		"[Third Group]\n"+//nl
		"double=10.5\n"+//nl
		"boolean=true\n"+//nl
		"integer=10\n";//nl
		assertEquals(expected, w.toString());
	}

	@Test
	public void testSave2() throws Exception {
		assertTrue(fileini.exists());
		GKeyFile gKeyFile = new GKeyFile(fileini);
		List<String> list = gKeyFile.getGroup("Another Group").getStringList("list1");
		assertEquals("x,y", list.get(3));
		list.set(3, "z");
		gKeyFile.getGroup("Another Group").setStringList("list1", list);
		StringWriter w = new StringWriter();
		gKeyFile.save(w);
		System.out.println(w);

		String expected = "\n"+//nl
		"[First Group]\n"+//nl
		"key1=value1\n"+//nl
		"key5=value5\n"+//nl
		"Name=Key File Example\\tthis value shows\\nescaping, also for slash \\.\n"+//nl
		"leading_spaces=\\sYou can preserve leading spaces\\s\n"+//nl
		"Welcome=Hello\n"+//nl
		"Welcome[de]=Hallo\n"+//nl
		"Welcome[fr_FR]=Bonjour\n"+//nl
		"Welcome[it]=Ciao\n"+//nl
		"Welcome[be@latin]=Hello\n"+//nl
		"\n"+//nl
		"[Another Group]\n"+//nl
		"Numbers=2;20;-200;0\n"+//nl
		"Booleans=true;false;true;true\n"+//nl
		"Doubles=1.5, 2.3, 4, 10.9\n"+//nl
		"key2=value2\n"+//nl
		"list1=a, b, c, z, d\n"+//nl
		"list2=1 ; 0 ; -1\n"+//nl
		"list3=a ; b ; c\n"+//nl
		"\n"+//nl
		"[Third Group]\n"+//nl
		"double=10.5\n"+//nl
		"boolean=true\n"+//nl
		"integer=10\n";//nl
		assertEquals(expected, w.toString());
	}

	@Test
	public void testSave3() throws Exception {
		assertTrue(fileini.exists());
		GKeyFile gKeyFile = new GKeyFile(fileini);
		List<String> list = gKeyFile.getGroup("Another Group").getStringList("list1");
		assertEquals("x,y", list.get(3));
		list.set(3, "z,k");
		gKeyFile.getGroup("Another Group").setStringList("list1", list);
		StringWriter w = new StringWriter();
		gKeyFile.save(w);
		System.out.println(w);

		String expected = "\n"+//nl
		"[First Group]\n"+//nl
		"key1=value1\n"+//nl
		"key5=value5\n"+//nl
		"Name=Key File Example\\tthis value shows\\nescaping, also for slash \\.\n"+//nl
		"leading_spaces=\\sYou can preserve leading spaces\\s\n"+//nl
		"Welcome=Hello\n"+//nl
		"Welcome[de]=Hallo\n"+//nl
		"Welcome[fr_FR]=Bonjour\n"+//nl
		"Welcome[it]=Ciao\n"+//nl
		"Welcome[be@latin]=Hello\n"+//nl
		"\n"+//nl
		"[Another Group]\n"+//nl
		"Numbers=2;20;-200;0\n"+//nl
		"Booleans=true;false;true;true\n"+//nl
		"Doubles=1.5, 2.3, 4, 10.9\n"+//nl
		"key2=value2\n"+//nl
		"list1=a, b, c, z\\,k, d\n"+//nl
		"list2=1 ; 0 ; -1\n"+//nl
		"list3=a ; b ; c\n"+//nl
		"\n"+//nl
		"[Third Group]\n"+//nl
		"double=10.5\n"+//nl
		"boolean=true\n"+//nl
		"integer=10\n";//nl
		assertEquals(expected, w.toString());
	}

	@Test
	public void testNewGroup() throws Exception {
		assertTrue(fileini.exists());
		GKeyFile gKeyFile = new GKeyFile(fileini);
		gKeyFile.createGroup("New Group");
		gKeyFile.getGroup("New Group").setStringList("newlist", createList("hello", "world", "everyone"));
		gKeyFile.getGroup("New Group").setInteger("newint", 99);
		gKeyFile.getGroup("New Group").setDouble("newdouble", 10.5);
		gKeyFile.getGroup("New Group").setDoubleList("newDoublelist", createList(10.10, 10.90, 50.0));

		StringWriter w = new StringWriter();
		gKeyFile.save(w);
		System.out.println(w);
		assertTrue(w.toString().indexOf("[New Group]") != -1);
		assertTrue(w.toString().indexOf("newlist=hello, world, everyone") != -1);
		assertTrue(w.toString().indexOf("newint=99") != -1);
		assertTrue(w.toString().indexOf("newdouble=10.5") != -1);
		assertTrue(w.toString().indexOf("newDoublelist=10.1, 10.9, 50.0") != -1);
	}

	@Test
	public void testNewGKeyFile() throws Exception {
		File file = new File(System.getProperty("java.io.tmpdir")+File.separator+"temp.ini");
		GKeyFile gKeyFile = new GKeyFile(file);

		gKeyFile.createGroup("First Group");
		gKeyFile.getGroup("First Group").setString("key1", "string1");
		gKeyFile.getGroup("First Group").setString("key2", " string1 ");

		gKeyFile.createGroup("New Group");
		gKeyFile.getGroup("New Group").setStringList("newlist", createList("hello", "world", "everyone"));
		gKeyFile.getGroup("New Group").setInteger("newint", 99);
		gKeyFile.getGroup("New Group").setDouble("newdouble", 10.5);
		gKeyFile.getGroup("New Group").setDoubleList("newDoublelist", createList(10.10, 10.90, 50.0));

		gKeyFile.save();
	}

	private <T> List<T> createList(T...elements) {
		List<T> list = new ArrayList<T>();
		for (T element : elements) {
			list.add(element);
		}

		return list;
	}

}
