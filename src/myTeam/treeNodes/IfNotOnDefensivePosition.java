package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;
import myTeam.Player;

public class IfNotOnDefensivePosition extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        PlayerPerception agentSelfPerception = agent.getSelfPerception();
        if (!agentSelfPerception.getPosition().equals(agent.getDefensivePosition())) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }

}
