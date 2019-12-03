package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class DashToDefensivePosition extends BTNode<Player> {
    @Override
    public BTStatus tick(Player agent) {
        Vector2D defensivePosition;
        if (agent.getSelfPerception().getSide() == EFieldSide.LEFT) {
            defensivePosition = new Vector2D(agent.getDefensivePosition().getX(), agent.getDefensivePosition().getY());
        } else {
            defensivePosition = new Vector2D(-agent.getDefensivePosition().getX(), -agent.getDefensivePosition().getY());
        }

        if (agent.isAlignedTo(defensivePosition)) {
            if (agent.isCloseTo(defensivePosition, 3.0d)) {
                agent.getCommander().doDashBlocking(5.0d);
            } else {
                agent.getCommander().doDashBlocking(80.0d);
            }
        } else {
            agent.getCommander().doTurnToPoint(defensivePosition);
            return BTStatus.RUNNING;
        }

        return BTStatus.SUCCESS;
    }
}
