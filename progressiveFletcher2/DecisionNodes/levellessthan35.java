package scripts.progressiveFletcher2.DecisionNodes;

import org.tribot.api2007.Skills;
import scripts.progressiveFletcher2.Tree_Framework.DecisionNode;

public class levellessthan35 extends DecisionNode {

    @Override
    public boolean isValid() {
        return Skills.SKILLS.FLETCHING.getActualLevel() >= 20 && Skills.SKILLS.FLETCHING.getActualLevel() < 35;
    }
}
