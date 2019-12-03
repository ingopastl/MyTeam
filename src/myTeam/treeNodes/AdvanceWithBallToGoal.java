package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

import java.util.List;


public class AdvanceWithBallToGoal extends BTNode<Player> {

	@Override
	public BTStatus tick(Player agent) {
		Vector2D ballPos = agent.getFieldPerception().getBall().getPosition();

		//condicao ruim extrema: longe da bola
		if (!agent.isCloseTo(ballPos, 10.0)) {
			agent.setHasBall(false);
			Player.getBroadcastMessagesListInstance().add("LostBall");
			return BTStatus.FAILURE;
		}

		if (agent.isCloseTo(ballPos, 3.0) && agent.isCloseTo(agent.getGoalPosition(), 20)) {
			return BTStatus.SUCCESS;
		}

		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				agent.getCommander().doKickToPoint(40.0d, agent.getGoalPosition()); //da um toque adiante (forca baixa)
				return BTStatus.SUCCESS;
			} else {
				agent.getCommander().doDashBlocking(100.0d); //chega mais perto da bola
			}
		} else {
			agent.getCommander().doTurnToPoint(ballPos);
		}

		return BTStatus.RUNNING;
	}
	
}
