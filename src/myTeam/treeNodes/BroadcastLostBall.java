package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EPlayerState;
import myTeam.Player;

public class BroadcastLostBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {

        PlayerPerception agentSelfPerception = agent.getSelfPerception();

        agent.setHasBall(false);
        Player.getBroadcastMessagesListInstance().add("LostBall");

        return BTStatus.SUCCESS;
    }

}