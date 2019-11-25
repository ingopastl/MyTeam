package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class IfCloseToGoal extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        if (agent.isCloseTo(agent.getGoalPosition(), 20)) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
