package models;


public class BYBPlan extends BusinessPlan
{

	private static final long serialVersionUID = 1570409654792228146L;

	// create an empty tree of BYB plan
	public BYBPlan()
	{
		this.type="BYBPlan";
		this.isEditable=true;
		//MS->Objective->Plan
		root = new Section("BYB Mission Statement");
		Section objective = new Section("BYB Objectives");
		Section plan = new Section("BYB Plan");
		root.addChild(objective);
		objective.addChild(plan);
		plan.setParent(objective);
		objective.setParent(root);
	}

	// BYB version of add a new Section to the business plan
	public void addSection(Section parent)
	{
		while (parent.name != "BYB Plan")
		{
			if (parent.name == "BYB Mission Statement")
			{
				Section child = new Section("BYB Objective");
				child.setParent(parent);
				parent.addChild(child);
				//parent = child;
			} else if (parent.name == "BYB Objective")
			{
				Section child = new Section("BYB Plan");
				child.setParent(parent);
				parent.addChild(child);
				//parent = child; 
			} else
			{
				System.out.println("ERROR: INVALID SECTION! ! !");
				return;
			}

		}

	}

}
