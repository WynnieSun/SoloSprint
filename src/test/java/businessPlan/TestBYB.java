package businessPlan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.BYBPlan;
import models.BusinessPlan;
import models.Section;

class TestBYB
{

	@Test
	void testBYB()
	{
		// check the structure of tree
		BusinessPlan BP = new BYBPlan();
		assertEquals("BYB Mission Statement", BP.root.getName());
		Section testCur = BP.root;//"BYB Mission Statement"
		assertEquals("BYB Objectives", testCur.children.get(0).name);
		testCur = testCur.children.get(0);//"BYB Objectives"
		assertEquals("BYB Plan", testCur.children.get(0).name);
		testCur = testCur.children.get(0);//"BYB Plan"

		// check addSection()
		BP.addSection(testCur);
		assertEquals(0, testCur.children.size());
		BP.addSection(new Section("wrong"));// will never happen,just for test
		BP.addSection(BP.root);// now have two Objectives
		assertEquals(2, BP.root.getChildren().size());

		Section current = BP.root.children.get(0);// the old objective
		Section cur = BP.root.children.get(1);// the new objective
		cur.setContent("New Branch");
		assertEquals("New Branch", cur.getContent());
		BP.addSection(BP.root);// now have three objectives
		// check deleteSection()
		BP.deleteSection(current);// deleted the old one
		assertEquals(2, BP.root.getChildren().size());// left two
		BP.deleteSection(BP.root.children.get(1));
		BP.deleteSection(BP.root.children.get(0));// print out error message

	}

}
