package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EPlayerState;
import easy_soccer_lib.utils.Vector2D;
import myTeam.Player;

import java.util.List;

public class IfTeamDoesntHaveBall extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        List<String> broadcastList = Player.getBroadcastMessagesListInstance();
        if (broadcastList.size() > 0) {
            if (!broadcastList.get(broadcastList.size() - 1).equals("TeamHasBall")) {
                return BTStatus.SUCCESS;
            }
        }
        return BTStatus.FAILURE;
    }

}
