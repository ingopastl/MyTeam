package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import myTeam.Player;

public class IfTeamOnTheLeftSide extends BTNode<Player> {
    @Override
    public BTStatus tick(Player agent) {
        if (agent.getSelfPerception().getSide() == EFieldSide.LEFT) {
            return BTStatus.SUCCESS;
        } else {
            return BTStatus.FAILURE;
        }
    }
}
