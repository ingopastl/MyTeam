package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import myTeam.Player;

public class TeleportHome extends BTNode<Player> {

	@Override
	public BTStatus tick(Player agent) {
		agent.getCommander().doMoveBlocking(agent.getHomePosition());
		return BTStatus.SUCCESS;
	}

}
