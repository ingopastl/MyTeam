package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

public class IfNotOnOffensivePosition extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        PlayerPerception agentSelfPerception = agent.getSelfPerception();
        Vector2D offensivePosition = new Vector2D( - agent.getHomePosition().getX(), agent.getHomePosition().getY());

        if (!agentSelfPerception.getPosition().equals(offensivePosition)) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }

}
