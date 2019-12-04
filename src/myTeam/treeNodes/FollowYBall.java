package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class FollowYBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();

        Vector2D followPosition = new Vector2D(- agent.getGoalPosition().getX(), ballPosition.getY());

        if (agent.isAlignedTo(followPosition)) {
            if (followPosition.getY() > -8.0d && followPosition.getY() < 8.0d) {
                agent.getCommander().doDashBlocking(90.0d);
                return BTStatus.SUCCESS;
            }
//            agent.getCommander().doDashBlocking(100.0d);
//            return BTStatus.SUCCESS;
        } else {
            agent.getCommander().doTurnToPointBlocking(followPosition);
            return BTStatus.RUNNING;
        }

        return BTStatus.FAILURE;
    }
}
