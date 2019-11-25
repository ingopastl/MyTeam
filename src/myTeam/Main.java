package myTeam;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        Team team1 = new Team("A");
        Team team2 = new Team("B");

        team1.launchTeamAndServer();
        team2.launchTeam();
    }
}
