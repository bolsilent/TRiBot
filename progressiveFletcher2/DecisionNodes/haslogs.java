package scripts.progressiveFletcher2.DecisionNodes;

import org.tribot.api2007.Inventory;
import scripts.progressiveFletcher2.Tree_Framework.DecisionNode;

public class haslogs extends DecisionNode {

    @Override
    public boolean isValid() {
        return Inventory.find( "Logs" ).length > 0 && Inventory.find( "Logs" ) != null;
    }
}
