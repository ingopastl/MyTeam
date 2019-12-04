package myTeam;

import bt_team.BTreeTeam;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        Team team1 = new Team("Mine");
        Team team2 = new Team("Mini");
        //BTreeTeam team2 = new BTreeTeam("Enemy");

        team1.launchTeamAndServer();
        //team2.launchTeam();
    }
}
