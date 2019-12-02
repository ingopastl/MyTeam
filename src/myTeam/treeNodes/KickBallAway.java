package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class KickBallAway extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPos = agent.getFieldPerception().getBall().getPosition();

        if (agent.isAlignedTo(ballPos)) {
            if (agent.isCloseTo(ballPos, 3.0)) {
                //da um chute com forca maxima (100)
                agent.getCommander().doKickToPoint(100.0d, agent.getGoalPosition());
                return BTStatus.SUCCESS;
            } else {
                //corre com forca intermediaria (porque esta perto da bola)
                agent.getCommander().doDashBlocking(100.0d);
            }
        } else {
            agent.getCommander().doTurnToPoint(ballPos);
        }

        return BTStatus.RUNNING;
    }

}