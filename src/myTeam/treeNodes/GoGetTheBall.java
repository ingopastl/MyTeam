package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class GoGetTheBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        Vector2D ballPosition = agent.getFieldPerception().getBall().getPosition();

        if (!agent.isCloseTo(ballPosition, 10.0)) {
            return BTStatus.FAILURE;
        }

        //condicao desejada: perto da bola (dist < 3)
        if (agent.isCloseTo(ballPosition, 3.0)) {
            return BTStatus.SUCCESS;
        }

        //corre atras da bola e da um pequeno toque
        if (agent.isAlignedTo(ballPosition)) {
            agent.getCommander().doDashBlocking(100.0d);
        } else {
            agent.getCommander().doTurnToPointBlocking(ballPosition);
        }
        return BTStatus.RUNNING;
    }
}
