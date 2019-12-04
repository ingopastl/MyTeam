package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

import java.util.List;

public class IfBallNotOnOurSide extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();

        if (agent.getCommander().getFieldSide() == EFieldSide.LEFT) {
            if (ballPosition.getX() > 0) {
                return BTStatus.SUCCESS;
            }
        } else {
            if (ballPosition.getX() <= 0) {
                return BTStatus.SUCCESS;
            }
        }
        return BTStatus.FAILURE;
    }

}
