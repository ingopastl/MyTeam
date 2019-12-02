package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import myTeam.Player;

import java.util.List;

public class IfEnemyIsApproaching extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        PlayerPerception selfPerc = agent.getSelfPerception();
        FieldPerception fieldPerc = agent.getFieldPerception();

        List<PlayerPerception> enemyTeam;
        if (selfPerc.getSide() == EFieldSide.LEFT) {
            enemyTeam = fieldPerc.getTeamPlayers(EFieldSide.RIGHT);
        } else {
            enemyTeam = fieldPerc.getTeamPlayers(EFieldSide.LEFT);
        }

        for (PlayerPerception enemy : enemyTeam) {
            double enemyDistance = enemy.getPosition().distanceTo(selfPerc.getPosition());
            if (enemyDistance < 5.0) {
                return BTStatus.SUCCESS;
            }
        }
		return BTStatus.FAILURE;
    }
}
