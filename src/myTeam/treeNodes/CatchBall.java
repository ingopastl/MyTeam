package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class CatchBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();

        if (agent.isAlignedTo(ballPosition)) {
            agent.getCommander().doCatchBlocking(0);
            return BTStatus.SUCCESS;
        } else {
            agent.getCommander().doTurnToPointBlocking(ballPosition);
            return BTStatus.RUNNING;
        }
    }

}
