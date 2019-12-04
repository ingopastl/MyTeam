package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class KickToScore extends BTNode<Player> {

	@Override
	public BTStatus tick(Player agent) {
		Vector2D ballPos = agent.getFieldPerception().getBall().getPosition();

		if (!agent.isCloseTo(ballPos, 3.0) || !agent.isCloseTo(agent.getGoalPosition(), 20)) {
			return BTStatus.FAILURE;
		}
		
		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				//da um chute com forca maxima (100)
				agent.getCommander().doKickToPointBlocking(100.0d, ballPos);
				return BTStatus.SUCCESS;
			} else {
				//corre com forca intermediaria (porque esta perto da bola)
				agent.getCommander().doDashBlocking(60.0d);
			}
		} else {
			agent.getCommander().doTurnToPointBlocking(ballPos);
		}
		
		return BTStatus.RUNNING;
	}

}
