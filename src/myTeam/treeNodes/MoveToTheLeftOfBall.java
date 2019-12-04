package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class MoveToTheLeftOfBall extends BTNode<Player> {
    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();
        Vector2D target = new Vector2D(ballPosition.getX() - 10, ballPosition.getY());

        if (agent.isCloseTo(target, 2)) {
            return BTStatus.SUCCESS;
        }

        if (agent.isAlignedTo(target)) {
            if (agent.isCloseTo(target, 3.0d)) {
                agent.getCommander().doDashBlocking(5.0d);
            } else {
                agent.getCommander().doDashBlocking(80.0d);
            }
        } else {
            agent.getCommander().doTurnToPointBlocking(target);
        }

        return BTStatus.RUNNING;
    }
}
