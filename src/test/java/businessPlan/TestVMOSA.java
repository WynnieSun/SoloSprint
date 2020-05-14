package businessPlan;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.BusinessPlan;
import models.Section;
import models.VMOSA;

class TestVMOSA
{

	// This test is used to test all the methods of the section
	@Test
	void testSection()
	{
		Section testParent = new Section("Parent");
		Section testNew = new Section("Test");
		Section testChild = new Section("Child");
		Section tChild = new Section("Child2");
		// check getContent() and setContent()
		assertEquals("", testNew.getContent());
		testNew.setContent("Hello World");
		assertEquals("Hello World", testNew.getContent());
		// check getName(), getChildren(),getParent()
		assertEquals(0, testNew.getChildren().size());
		assertEquals("Test", testNew.getName());
		assertEquals(null, testNew.getParent());
		// check setParent()
		testNew.setParent(testParent);
		assertEquals(testParent.name, testNew.getParent().name);
		assertEquals(testParent.content, testNew.getParent().content);
		// check(( getName() ))and setName()
		testNew.setName("New Name");
		assertEquals("New Name", testNew.getName());
		// check addChild() and deleteChild()
		testNew.addChild(testChild);
		assertEquals(1, testNew.getChildren().size());
		testNew.addChild(tChild);
		assertEquals(2, testNew.getChildren().size());
		//åº”è¯¥å†�æ›´è¯¦ç»†ç‚¹ test childçš„nameï¼Œcontentï¼Œè€Œä¸�æ˜¯å�•å�•çš„getChildren().size()
		testNew.deleteChild(tChild);
		assertEquals(1, testNew.getChildren().size());
		//ä¸€æ ·çš„é—®é¢˜ ä½ ä¹Ÿä¸�æ˜¯è‡ªå·±deleteçš„æ˜¯ä¸�æ˜¯è¿™ä¸ªspecificçš„tchild
		
		// check setChildren() and getChildren()
		ArrayList<Section> children = new ArrayList<Section>();
		children.add(testParent);
		children.add(testNew);
		children.add(testChild);
		children.add(tChild);
		Section tChild2 = new Section("Child2");
		tChild2.setChildren(children);
		ArrayList<Section> children2 = tChild2.getChildren();
		assertEquals(children.size(), children2.size());
		for (int i = 0; i < children.size(); i++)
		{
			assertEquals(children.get(i).name, children2.get(i).name);
			assertEquals(children.get(i).content, children2.get(i).content);
		}
		//

	}

	// this is used to test VMOSA's methods
	@Test
	void testVMOSA()
	{
		// check the structure of the tree
		BusinessPlan BP = new VMOSA();
		assertEquals(BP.root.getName(), "Vision");
		Section testCur = BP.root;
		assertEquals("Mission", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		assertEquals("Objective", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		assertEquals("Strategy", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		assertEquals("Action Plan", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		// check addSection()
		BP.addSection(testCur);
		assertEquals(0, testCur.children.size());// boundary test, can not add a section for the last section
		BP.addSection(new Section("wrong"));// will never happen,just for test
		BP.addSection(BP.root);// now have two missions
		assertEquals(2, BP.root.getChildren().size());
		Section current = BP.root.children.get(0);
		Section cur = BP.root.children.get(1);
		cur.setContent("New Branch");
		assertEquals("New Branch", cur.getContent());
		BP.addSection(BP.root);// now the root should have 3 children
		// check deleteSection()
		BP.deleteSection(current);
		assertEquals(2, BP.root.getChildren().size());
		BP.deleteSection(BP.root.children.get(1));
		BP.deleteSection(BP.root.children.get(0));// print out error message
		// BP.encodeToXML("test.txt");

	}

	@Test
	void testXMLencoder()
	{
		BusinessPlan BP = new VMOSA();
		BP.root.setContent("Hello");
		Section current = BP.root.children.get(0);
		current.setContent("World");
		BP.addSection(current);// current is mission//çœ‹ä¸�å¤ªæ‡‚
		BP.encodeToXML("test.txt");
	}

	@Test
	void TestXMLdecoder()
	{
		// two business plans are the same when each section is the same
		// two sections are the same when the name, content and their children are the
		// same.
		BusinessPlan BP = new VMOSA();
		Section current = BP.root;
		// give content to each Section
		current.setContent("0");
		for (int i = 1; i < 5; i++)
		{
			current = current.children.get(0);
			current.setContent(Integer.toString(i));

		}
		// encode the business plan
		BP.encodeToXML("test2.txt");
		// decode the business plan
		BusinessPlan plan = BP.decodeFromXML("test2.txt");
		// first check the size of each Section's children
		Section cur1 = BP.root;
		Section cur2 = plan.root;
		assertEquals(BP.root.name, plan.root.name);
		assertEquals(BP.root.content, plan.root.content);

		for (int i = 0; i < 4; i++)
		{
			assertEquals(cur1.children.size(), cur2.children.size());
			cur2 = cur2.children.get(0);
			cur1 = cur1.children.get(0);
		}

		assertEquals(cur1.children.size(), cur2.children.size());
		// then check the name and the content are the same
		cur1 = BP.root;
		cur2 = plan.root;
		assertEquals(cur1.name, cur2.name);
		assertEquals(cur1.content, cur2.content);
		for (int i = 1; i < 5; i++)
		{
			cur1 = cur1.children.get(0);
			cur2 = cur2.children.get(0);
			assertEquals(cur1.name, cur2.name);
			assertEquals(cur1.content, cur2.content);
		}
		// since sections from the two business plan are the same, we can conclude that
		// their children are the same.
		assertThrows(NullPointerException.class,()->{BP.encodeToXML("");});
		//test if the file is null. the file not found exception is connected to serialize, we cannot test it.
		assertThrows(NullPointerException.class,()->{BP.decodeFromXML("file.txt");});
		
	}

}
