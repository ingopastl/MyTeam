package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import myTeam.Player;

public class IfWithBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        if (agent.hasBall()) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}