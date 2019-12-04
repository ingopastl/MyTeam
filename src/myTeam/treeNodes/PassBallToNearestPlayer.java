package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class PassBallToNearestPlayer extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();
        Vector2D nearestTeammatePosition = agent.getNearestTeammatePosition();

        //condicao ruim extrema: longe demais da bola
        if (!agent.isCloseTo(ballPosition, 3.0)) {
            return BTStatus.FAILURE;
        }

        if (agent.isAlignedTo(ballPosition)) {
            if (agent.isCloseTo(ballPosition, 1.0)) {
                agent.getCommander().doKickToPointBlocking(50.0d, nearestTeammatePosition);
                return BTStatus.SUCCESS;
            } else {
                //corre com forca intermediaria (porque esta perto da bola)
                agent.getCommander().doDashBlocking(60.0d);
                return BTStatus.RUNNING;
            }
        } else {
            agent.getCommander().doTurnToPointBlocking(ballPosition);
            return BTStatus.RUNNING;
        }
    }

}
