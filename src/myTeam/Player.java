package myTeam;

import behavior_tree.BTNode;
import behavior_tree.Selector;
import behavior_tree.Sequence;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.MatchPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import myTeam.treeNodes.*;
import myTeam.treeNodes.matchStateVerifiers.*;

import java.util.List;

public class Player extends Thread {
    private BTNode<Player> behaviorTree;

    private PlayerCommander commander;

    private Vector2D homePosition;
    private Vector2D goalPosition;
    private int uniformNumber;

    private PlayerPerception selfPerception;
    private FieldPerception fieldPerception;
    private MatchPerception matchPerception;


    public Player(PlayerCommander commander, Vector2D homePosition, int uniformNumber) {
        this.commander = commander;
        this.homePosition = homePosition;
        this.uniformNumber = uniformNumber;

        this.behaviorTree = buildTree();
    }

    public int getUniformNumber() {
        return uniformNumber;
    }

    public PlayerCommander getCommander() {
        return commander;
    }

    public Vector2D getHomePosition() {
        return homePosition;
    }

    public Vector2D getGoalPosition() {
        return goalPosition;
    }

    public PlayerPerception getSelfPerception() {
        return selfPerception;
    }

    public FieldPerception getFieldPerception() {
        return fieldPerception;
    }

    public MatchPerception getMatchPerception() {
        return matchPerception;
    }

    public void updatePerceptions() {
        this.fieldPerception = commander.perceiveFieldBlocking();
        this.selfPerception = commander.perceiveSelfBlocking();
        this.matchPerception = commander.perceiveMatchBlocking();
    }

    @Override
    public void run() {
        System.out.println(">> 1. Waiting initial perceptions...");
        updatePerceptions();

        System.out.println(">> 2. Moving to initial position...");
        commander.doMoveBlocking(this.homePosition);
        updatePerceptions();

        if (selfPerception.getSide() == EFieldSide.LEFT) {
            goalPosition = new Vector2D(52.0d, 0);
        } else {
            goalPosition = new Vector2D(-52.0d, 0);
            //homePosition.setX(- homePosition.getX()); //inverte, porque somente no move as coordenadas sao espelhadas independente de lado
            //homePosition.setY(- homePosition.getY());
        }

        System.out.println(">> 4. Now starting...");
        while (commander.isActive()) {

            behaviorTree.tick(this);

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updatePerceptions(); //non-blocking
        }

        System.out.println(">> 5. Terminated!");
    }

    private BTNode<Player> buildTree() {
        if (getUniformNumber() == 0) {
            return buildGoalieTree();
        } else {
            return buildRegularTree();
        }
    }

    private BTNode<Player> buildRegularTree() {

        Sequence<Player> afterGoalRight = new Sequence<>("After Goal Right");
        afterGoalRight.add(new IfAfterGoalRight());
        afterGoalRight.add(new ReturnToHome());

        Sequence<Player> afterGoalLeft = new Sequence<>("After Goal Left");
        afterGoalLeft.add(new IfAfterGoalLeft());
        afterGoalLeft.add(new ReturnToHome());

        Sequence<Player> kickOffLeft = new Sequence<>("Kick Off Left");
        kickOffLeft.add(new IfKickOffLeft());
        kickOffLeft.add(new IfTeamInTheLeftSide());
        kickOffLeft.add(new IfClosestPlayerToBall());
        kickOffLeft.add(new GoGetTheBall());
        kickOffLeft.add(new PassBallToNearestPlayer());

        Sequence<Player> kickOffRight = new Sequence<>("Kick Off Right");
        kickOffRight.add(new IfKickOffRight());
        kickOffRight.add(new IfTeamInTheRightSide());
        kickOffRight.add(new IfClosestPlayerToBall());
        kickOffRight.add(new GoGetTheBall());
        kickOffRight.add(new PassBallToNearestPlayer());

        Sequence<Player> tryToScore = new Sequence<>();
        tryToScore.add(new IfCloseToGoal());
        tryToScore.add(new KickToScore());

        Selector<Player> attacking = new Selector<>();
        attacking.add(tryToScore);
        attacking.add(new AdvanceWithBallToGoal());

        Sequence<Player> playOn = new Sequence<>("Play On");
        playOn.add(new IfPlayOn());
        playOn.add(new IfClosestPlayerToBall());
        playOn.add(new GoGetTheBall());
        playOn.add(attacking);


        Selector<Player> root = new Selector<>("Root");
        root.add(afterGoalLeft);
        root.add(afterGoalRight);
        root.add(kickOffLeft);
        root.add(kickOffRight);
        root.add(playOn);

        return root;
    }

    private BTNode<Player> buildGoalieTree() {

        Sequence<Player> afterGoalRight = new Sequence<>("After Goal Right");
        afterGoalRight.add(new IfAfterGoalRight());
        afterGoalRight.add(new ReturnToHome());

        Sequence<Player> afterGoalLeft = new Sequence<>("After Goal Left");
        afterGoalLeft.add(new IfAfterGoalLeft());
        afterGoalLeft.add(new ReturnToHome());

        Sequence<Player> playOn = new Sequence<>("Play On");
        playOn.add(new IfPlayOn());
        playOn.add(new FollowYBall());

        Sequence<Player> defend = new Sequence<>("Defend");
        defend.add(new IfCloseToBall());
        defend.add(new GoGetTheBall());
        defend.add(new CatchBall());

        Selector<Player> root = new Selector<>("Root");
        root.add(afterGoalLeft);
        root.add(afterGoalRight);
        root.add(defend);
        root.add(playOn);

        return root;
    }

    public Vector2D getNearestTeammatePosition() {
        Vector2D ballPosition = fieldPerception.getBall().getPosition();
        List<PlayerPerception> myTeam = fieldPerception.getTeamPlayers(selfPerception.getSide());
        myTeam.remove(selfPerception);

        PlayerPerception closestPlayer = selfPerception;
        double closestDistance = Double.MAX_VALUE;

        for (PlayerPerception player : myTeam) {
            double playerDistance = player.getPosition().distanceTo(selfPerception.getPosition());
            if (playerDistance < closestDistance) {
                closestDistance = playerDistance;
                closestPlayer = player;
            }
        }

        return closestPlayer.getPosition();
    }

    public boolean isCloseTo(Vector2D pos) {
        return isCloseTo(pos, 1.5);
    }

    public boolean isCloseTo(Vector2D pos, double minDistance) {
        Vector2D myPos = selfPerception.getPosition();
        return pos.distanceTo(myPos) < minDistance;
    }

    public boolean isAlignedTo(Vector2D position) {
        return isAlignedTo(position, 12.0);
    }

    public boolean isAlignedTo(Vector2D position, double minAngle) {
        if (minAngle < 0) minAngle = -minAngle;

        Vector2D myPos = selfPerception.getPosition();

        if (position == null || myPos == null) {
            return false;
        }

        double angle = selfPerception.getDirection().angleFrom(position.sub(myPos));
        return angle < minAngle && angle > -minAngle;
    }
}