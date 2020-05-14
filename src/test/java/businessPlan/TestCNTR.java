package businessPlan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.BusinessPlan;
import models.CNTRAssessment;
import models.Section;

class TestCNTR
{

	@Test
	void testCNTR()
	{
		// check tree structure
		BusinessPlan BP = new CNTRAssessment();
		assertEquals("Centre College Institutional Mission Statement", BP.root.getName());
		Section testCur = BP.root;
		assertEquals("Program Mission Statement", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		assertEquals("Program Goals and Student Learning Objective", testCur.children.get(0).name);
		testCur = testCur.children.get(0);
		// check addSection()
		BP.addSection(testCur);
		assertEquals(0, testCur.children.size());
		BP.addSection(new Section("wrong"));// will never happen,just for test
		BP.addSection(BP.root);// now have two objectives
		assertEquals(2, BP.root.getChildren().size());

		Section current = BP.root.children.get(0);// the old objective
		Section cur = BP.root.children.get(1);// the new objective
		cur.setContent("New Branch");
		assertEquals("New Branch", cur.getContent());
		BP.addSection(BP.root);// now have three objectives
		BP.deleteSection(current);// deleted the old one
		assertEquals(2, BP.root.getChildren().size());// left two
		BP.deleteSection(BP.root.children.get(1));
		BP.deleteSection(BP.root.children.get(0));// print out error message
	}

}
