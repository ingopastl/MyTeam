package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import myTeam.Player;

public class IfCloseToBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        if (agent.isCloseTo(agent.getFieldPerception().getBall().getPosition(), 10)) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
