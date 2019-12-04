package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class MarkBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPos = agent.getFieldPerception().getBall().getPosition();
        Vector2D direction = new Vector2D(ballPos.getX(), agent.getSelfPerception().getPosition().getY());

        if (agent.isAlignedTo(direction)) {
            agent.getCommander().doDashBlocking(100.0d);
        } else {
            agent.getCommander().doTurnToPointBlocking(direction);
        }

        return BTStatus.RUNNING;
    }

}
