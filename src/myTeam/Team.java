package myTeam;

import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;

public class Team extends AbstractTeam {

    public Team(String teamName) {
        super(teamName, 8, true);
    }

    @Override
    protected void launchPlayer(int ag, PlayerCommander commander) {
        double x, y;

        switch (ag) {
            case 0:
                x = -50.0d;
                y = 0.0d;
                break;
            case 1:
                x = -29.0d;
                y = -20.0d;
                break;
            case 2:
                x = -30.0d;
                y = 0.0d;
                break;
            case 3:
                x = -29.0d;
                y = 20.0d;
                break;
            case 4:
                x = -19.0d;
                y = -10.0d;
                break;
            case 5:
                x = -7.0d;
                y = -7.0d;
                break;
            case 6:
                x = -19.0d;
                y = 10.0d;
                break;
            case 7:
                x = -7.0d;
                y = 7.0d;
                break;
            default:
                x = -37.0d;
                y = 0;
        }

        Player player = new Player(commander, new Vector2D(x, y), ag);
        player.start();
    }
}
