package myTeam.treeNodes.matchStateVerifiers;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EMatchState;
import myTeam.Player;

public class IfBeforeKickOff extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        if (agent.getMatchPerception().getState() == EMatchState.BEFORE_KICK_OFF) {
            return BTStatus.SUCCESS;
        } else {
            return BTStatus.FAILURE;
        }
    }

}
