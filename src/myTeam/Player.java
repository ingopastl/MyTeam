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

import java.util.ArrayList;
import java.util.List;

public class Player extends Thread {
    private BTNode<Player> behaviorTree;

    private PlayerCommander commander;

    private Vector2D homePosition;
    private Vector2D offensivePosition;
    private Vector2D defensivePosition;
    private Vector2D goalPosition;

    private int uniformNumber;
    private boolean hasBall;

    private PlayerPerception selfPerception;
    private FieldPerception fieldPerception;
    private MatchPerception matchPerception;

    private static List<String> broadcastMessagesList;

    public static List<String> getBroadcastMessagesListInstance() {
        if (broadcastMessagesList == null) {
            broadcastMessagesList = new ArrayList<>();
        }
        return broadcastMessagesList;
    }

    public Player(PlayerCommander commander, Vector2D homePosition, int uniformNumber) {
        this.commander = commander;
        this.homePosition = homePosition;
        this.uniformNumber = uniformNumber;
        this.hasBall = false;
        this.behaviorTree = buildTree();

        switch (this.uniformNumber) {
            case 1:
                this.offensivePosition = new Vector2D(-4, -21);
                this.defensivePosition = this.homePosition;
                break;
            case 2:
                this.offensivePosition = new Vector2D(-10, 0);
                this.defensivePosition = this.homePosition;
                break;
            case 3:
                this.offensivePosition = new Vector2D(-4, 20);
                this.defensivePosition = this.homePosition;
                break;
            case 4:
                this.offensivePosition = new Vector2D(28, -20);
                this.defensivePosition = this.homePosition;
                break;
            case 5:
                this.offensivePosition = new Vector2D(42, -9);
                this.defensivePosition = this.homePosition;
                break;
            case 6:
                this.offensivePosition = new Vector2D(28, 20);
                this.defensivePosition = this.homePosition;
                break;
            case 7:
                this.offensivePosition = new Vector2D(42, 9);
                this.defensivePosition = this.homePosition;
                break;

        }
    }

    public Vector2D getOffensivePosition() {
        return offensivePosition;
    }

    public void setOffensivePosition(Vector2D offensivePosition) {
        this.offensivePosition = offensivePosition;
    }

    public Vector2D getDefensivePosition() {
        return defensivePosition;
    }

    public void setDefensivePosition(Vector2D defensivePosition) {
        this.defensivePosition = defensivePosition;
    }

    public boolean hasBall() {
        return hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
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

    private BTNode<Player> buildOffensiveTree() {
        //Sequence<Player> pass = new Sequence<>();
        //pass.add(new IfEnemyIsApproachingAndFarFromGoal());
        //pass.add(new PassBallToNearestPlayer());

        Sequence<Player> advance = new Sequence<>();
        advance.add(new AdvanceWithBallToGoal());
        advance.add(new KickToScore());

        Selector<Player> advanceOrPass = new Selector<>();
        //advanceOrPass.add(pass);
        advanceOrPass.add(advance);

        Sequence<Player> intercept = new Sequence<>("Closest to Ball");
        intercept.add(new IfClosestToBall());
        intercept.add(new GoGetTheBall());
        intercept.add(advanceOrPass);

        Sequence<Player> dashToOffensivePosition = new Sequence<>("Go to Offensive Position");
        dashToOffensivePosition.add(new DashToOffensivePosition());

        Selector<Player> behavior = new Selector<>("Attack Behavior");
        behavior.add(intercept);
        behavior.add(dashToOffensivePosition);

        Sequence<Player> offensive = new Sequence<>("Offensive tree");
        offensive.add(new IfBallNotOnOurSide());
        offensive.add(behavior);

        return offensive;
    }

    private BTNode<Player> buildDefensiveTree() {
        Sequence<Player> intercept = new Sequence<>();
        intercept.add(new IfClosestToBall());
        intercept.add(new GoGetTheBall());
        intercept.add(new AdvanceWithBallToGoal());

        Sequence<Player> dashToDefensivePosition = new Sequence<>();
        dashToDefensivePosition.add(new DashToDefensivePosition());

        Selector<Player> defensive = new Selector<>("Defensive tree");
        defensive.add(intercept);
        defensive.add(dashToDefensivePosition);

        return defensive;
    }

    private BTNode<Player> buildRegularTree() {

        Sequence<Player> afterGoalRight = new Sequence<>("After Goal Right");
        afterGoalRight.add(new IfAfterGoalRight());
        afterGoalRight.add(new TeleportHome());


        Sequence<Player> afterGoalLeft = new Sequence<>("After Goal Left");
        afterGoalLeft.add(new IfAfterGoalLeft());
        afterGoalLeft.add(new TeleportHome());


        Sequence<Player> closest = new Sequence<>();
        closest.add(new IfClosestToBall());
        closest.add(new MoveToTheLeftOfBall());
        closest.add(new IfSecondPlayerReady());
        closest.add(new GoGetTheBall());
        closest.add(new PassBallToNearestPlayer());

        Sequence<Player> secondClosest = new Sequence<>();
        secondClosest.add(new IfSecondClosestToBall());
        secondClosest.add(new MoveToTheRightOfBall());
        secondClosest.add(new BroadcastSecondPlayerReady());

        Selector<Player> freeKickFaultLeftBehavior = new Selector<>();
        freeKickFaultLeftBehavior.add(closest);
        freeKickFaultLeftBehavior.add(secondClosest);

        Sequence<Player> freeKickFaultLeft = new Sequence<>();
        freeKickFaultLeft.add(new IfTeamOnTheLeftSide());
        freeKickFaultLeft.add(freeKickFaultLeftBehavior);


        Sequence<Player> kickOffLeft = new Sequence<>("Kick Off Left");
        kickOffLeft.add(new IfKickOffLeft());
        kickOffLeft.add(new IfTeamOnTheLeftSide());
        kickOffLeft.add(new IfClosestToBall());
        kickOffLeft.add(new GoGetTheBall());
        kickOffLeft.add(new PassBallToNearestPlayer());


        Sequence<Player> kickOffRight = new Sequence<>("Kick Off Right");
        kickOffRight.add(new IfKickOffRight());
        kickOffRight.add(new IfTeamOnTheRightSide());
        kickOffRight.add(new IfClosestToBall());
        kickOffRight.add(new GoGetTheBall());
        kickOffRight.add(new PassBallToNearestPlayer());


        Selector<Player> root = new Selector<>("Root");
        root.add(afterGoalLeft);
        root.add(afterGoalRight);

        root.add(kickOffLeft);
        root.add(kickOffRight);
        root.add(buildOffensiveTree());
        root.add(buildDefensiveTree());

        return root;
    }

    private BTNode<Player> buildGoalieTree() {

        Sequence<Player> afterGoalRight = new Sequence<>("After Goal Right");
        afterGoalRight.add(new IfAfterGoalRight());
        afterGoalRight.add(new TeleportHome());

        Sequence<Player> afterGoalLeft = new Sequence<>("After Goal Left");
        afterGoalLeft.add(new IfAfterGoalLeft());
        afterGoalLeft.add(new TeleportHome());

        Sequence<Player> playOn = new Sequence<>("Play On");
        playOn.add(new IfPlayOn());
        playOn.add(new FollowYBall());

        Sequence<Player> defend = new Sequence<>("Defend");
        defend.add(new IfCloseToBall());
        defend.add(new KickBallAway());

        Selector<Player> root = new Selector<>("Root");
        root.add(afterGoalLeft);
        root.add(afterGoalRight);
        root.add(defend);
        root.add(playOn);

        return root;
    }

    public Vector2D getNearestTeammatePosition() {
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
