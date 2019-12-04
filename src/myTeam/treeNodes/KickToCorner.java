package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class KickToCorner extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPos = agent.getFieldPerception().getBall().getPosition();
        Vector2D center;
        if (agent.getCommander().getFieldSide() == EFieldSide.LEFT)
            center = new Vector2D(42, 0);
        else
            center = new Vector2D(-42, 0);

        if (agent.isAlignedTo(ballPos)) {
            if (agent.isCloseTo(ballPos, 1.0)) {
                //da um chute com forca maxima (100)
                agent.getCommander().doKickToPoint(80.0d, center);
                agent.getSelfPerception().setState(EPlayerState.NULL);
                return BTStatus.SUCCESS;
            }
        } else {
            agent.getCommander().doTurnToPoint(ballPos);
        }

        return BTStatus.RUNNING;
    }
}
