package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

import java.util.List;

public class IfTeamHasBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        PlayerPerception agentSelfPerception = agent.getSelfPerception();
        FieldPerception agentFieldPerception = agent.getFieldPerception();
        Vector2D ballPosition = agentFieldPerception.getBall().getPosition();

        List<PlayerPerception> myTeam = agentFieldPerception.getTeamPlayers(agentSelfPerception.getSide());

        for (PlayerPerception player : myTeam) {
            if (player.getState() == EPlayerState.HAS_BALL) {
                return BTStatus.SUCCESS;
            }
        }
        return BTStatus.FAILURE;
    }

}
