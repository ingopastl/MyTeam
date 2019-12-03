package myTeam.treeNodes;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import myTeam.Player;

import java.util.List;

public class IfSecondPlayerReady extends BTNode<Player> {

    @Override
    public BTStatus tick(Player agent) {
        List<String> broadcastList = Player.getBroadcastMessagesListInstance();

        if (broadcastList.size() == 0) {
            broadcastList.add("");
            return BTStatus.FAILURE;
        }

        if (broadcastList.get(broadcastList.size() - 1).equals("SecondReady")) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
