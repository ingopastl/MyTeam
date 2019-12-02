package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class DashToOffensivePosition extends BTNode<Player> {
    @Override
    public BTStatus tick(Player agent) {
        Vector2D offensivePosition;
        if (agent.getSelfPerception().getSide() == EFieldSide.LEFT) {
            offensivePosition = new Vector2D(agent.getOffensivePosition().getX(), agent.getOffensivePosition().getY());
        } else {
            offensivePosition = new Vector2D(-agent.getOffensivePosition().getX(), -agent.getOffensivePosition().getY());
        }

        if (agent.isAlignedTo(offensivePosition)) {
            agent.getCommander().doDashBlocking(60.0d);
        } else {
            agent.getCommander().doTurnToPoint(offensivePosition);
        }

        return BTStatus.SUCCESS;
    }
}
