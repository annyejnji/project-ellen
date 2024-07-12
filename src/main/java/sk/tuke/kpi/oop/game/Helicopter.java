package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.util.Objects;

public class Helicopter extends AbstractActor {
    private static final String HELICOPTER_ANIMATION = "sprites/heli.png";
    private final int playerEnergy;
    private final int movementSpeed;
    private boolean pursuing;

    public Helicopter() {
        Animation helicopterAnimation = new Animation(HELICOPTER_ANIMATION, 64, 64, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(helicopterAnimation);
        playerEnergy = 100;
        movementSpeed = 2;
        pursuing = false;
    }

    public void searchAndDestroy() {
        if (!pursuing) {
            pursuing = true;
            new Loop<>(new Invoke<>(this::internalSearchAndDestroy)).scheduleFor(this);
        }
    }

    private void internalSearchAndDestroy() {
        Scene scene = Objects.requireNonNull(getScene());
        Player player = findNearestPlayer(scene);

        if (player == null) {
            pursuing = false;
            return;
        }

        int playerX = player.getPosX();
        int playerY = player.getPosY();
        int helicopterX = getPosX();
        int helicopterY = getPosY();

        double distance = Math.sqrt(Math.pow(playerX - helicopterX, 2) + Math.pow(playerY - helicopterY, 2));

        if (distance > 0) {
            int dx = (int) ((playerX - helicopterX) / distance * movementSpeed);
            int dy = (int) ((playerY - helicopterY) / distance * movementSpeed);

            moveHelicopter(dx, dy);
        }

        int attackRadius = 2;
        if (Math.abs(playerX - helicopterX) <= attackRadius && Math.abs(playerY - helicopterY) <= attackRadius) {
            player.setEnergy(player.getEnergy() - 1);
        }
    }

    private Player findNearestPlayer(Scene scene) {
        Objects.requireNonNull(scene);
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        int helicopterX = getPosX();
        int helicopterY = getPosY();

        for (Actor actor : scene.getActors()) {
            if (actor instanceof Player) {
                Player player = (Player) actor;
                int playerX = player.getPosX();
                int playerY = player.getPosY();

                double distance = Math.sqrt(Math.pow(playerX - helicopterX, 2) + Math.pow(playerY - helicopterY, 2));

                if (distance < nearestDistance) {
                    nearestPlayer = player;
                    nearestDistance = distance;
                }
            }
        }

        return nearestPlayer;
    }

    private void moveHelicopter(int dx, int dy) {

        Scene scene = getScene();
        assert scene != null;

        int originalX = getPosX();
        int originalY = getPosY();

        int newX = originalX + dx;
        int newY = originalY + dy;

        setPosition(newX, newY);
    }

    public int getPlayerEnergy() {
        return playerEnergy;
    }
}
