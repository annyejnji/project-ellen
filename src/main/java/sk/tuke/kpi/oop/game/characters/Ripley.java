package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

import java.util.Objects;

public class Ripley extends AbstractActor implements Alive, Movable, Keeper, Armed {
    private int speed;
    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);

    private int energy;
    private int ammo;
    private final Backpack backpack;
    private Health health;
    private Firearm gun;
    private boolean armorIsSet = false;

    private static final String PLAYER_DIE_ANIMATION = "sprites/player_die.png";
    private static final String RIPLEY_ANIMATION = "sprites/player.png";
    private final Animation ripleyAnimation;
    private final Animation playerDieAnimation;


    public Ripley() {
        super("Ellen");
        this.energy = 100;
        this.speed = 2;
        this.health = new Health(100);
        this.ripleyAnimation = new Animation(RIPLEY_ANIMATION, 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        this.playerDieAnimation = new Animation(PLAYER_DIE_ANIMATION, 32, 32, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(ripleyAnimation);
        ripleyAnimation.stop();
        this.backpack = new Backpack("Ripley's backpack", 10);
        this.gun = new Gun(200);
        health.onFatigued(() -> {
            setAnimation(playerDieAnimation);
            Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED, this);
            getScene().cancelActions(this);
        });
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void startedMoving(Direction direction) {
        float angle = direction.getAngle();
        ripleyAnimation.setRotation(angle);
        ripleyAnimation.play();
    }

    @Override
    public void stoppedMoving() {
        ripleyAnimation.stop();
    }

    public int getEnergy() {
        return energy;
    }


    public void setEnergy(int energy) {
        if (energy < 0) {
            this.energy = 0;
        } else if (energy > 100) {
            this.energy = 100;
        } else {
            this.energy = energy;
        }
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = Math.max(0, Math.min(ammo, 500));
    }

    @Override
    public Backpack getBackpack() {
        return backpack;
    }

    public void showRipleyState() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText("Energy: " + getHealth(), 100, yTextPos);
        scene.getGame().getOverlay().drawText("Ammo: " + getFirearm().getAmmo(), 250, yTextPos);
    }

    public void decreaseEnergy() {
        if (health.getValue() > 0) {
            health.drain(1);
        }

        if (health.getValue() == 0) {
            setAnimation(playerDieAnimation);
            playerDieAnimation.play();
            Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED, this);
            getScene().cancelActions(this);
        }
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public Firearm getFirearm() {
        return gun;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        this.gun = weapon;
    }

    public boolean isArmor() {
        return armorIsSet;
    }

    public void setArmor(boolean armorIsSet) {
        this.armorIsSet = armorIsSet;
    }
}


