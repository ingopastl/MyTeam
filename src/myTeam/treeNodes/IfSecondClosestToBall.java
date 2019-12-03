package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

import java.util.List;

public class IfSecondClosestToBall extends BTNode<Player> {
    @Override
    public BTStatus tick(Player agent) {
        PlayerPerception selfPerc = agent.getSelfPerception();
        FieldPerception fieldPerc = agent.getFieldPerception();

        Vector2D ballPosition = fieldPerc.getBall().getPosition();
        List<PlayerPerception> myTeam = fieldPerc.getTeamPlayers(selfPerc.getSide());

        PlayerPerception closestPlayer = agent.getSelfPerception();
        double closestDistance = Double.MAX_VALUE;

        PlayerPerception secondClosestPlayer = agent.getSelfPerception();

        for (PlayerPerception player : myTeam) {
            double playerDistance = player.getPosition().distanceTo(ballPosition);
            if (playerDistance < closestDistance) {
                secondClosestPlayer = closestPlayer;
                closestDistance = playerDistance;
                closestPlayer = player;
            }
        }

        if (secondClosestPlayer.getUniformNumber() == selfPerc.getUniformNumber()) {
            return BTStatus.SUCCESS;
        } else {
            return BTStatus.FAILURE;
        }
    }
}
