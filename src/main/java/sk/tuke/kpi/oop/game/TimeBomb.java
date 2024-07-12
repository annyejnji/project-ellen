package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.util.Objects;

public class TimeBomb extends AbstractActor {
    private boolean isActivated = false;
    private static final String BOMB_BEFORE_ACTIVATION_ANIMATION = "sprites/bomb.png";
    private static final String BOMB_AFTER_ACTIVATION_ANIMATION = "sprites/bomb_activated.png";
    private static final String BOMB_DETONATION_ANIMATION = "sprites/small_explosion.png";
    private final Animation bombActivatedAnimation;
    private final Animation bombDetonationAnimation;
    private final ActionSequence<TimeBomb> schedule;

    public TimeBomb(float timeInSecondsTillDetonation) {
        this.schedule = new ActionSequence<>(new Wait<>(timeInSecondsTillDetonation), new Invoke<>(this::detonate));
        Animation bombAnimation = new Animation(BOMB_BEFORE_ACTIVATION_ANIMATION, 16, 16);
        bombActivatedAnimation = new Animation(BOMB_AFTER_ACTIVATION_ANIMATION, 16, 16, timeInSecondsTillDetonation / 4, Animation.PlayMode.ONCE);
        bombDetonationAnimation = new Animation(BOMB_DETONATION_ANIMATION, 16, 16, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(bombAnimation);
    }

    public void activate() {
        this.isActivated = true;
        setAnimation(bombActivatedAnimation);
        schedule.scheduleFor(this);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void remove() {
        Objects.requireNonNull(getScene()).removeActor(this);
    }

    public void detonate() {
        setAnimation(bombDetonationAnimation);

        new ActionSequence<>(
            new When<>(() -> (getAnimation().getFrameCount() - 1) == getAnimation().getCurrentFrameIndex(),
                new Invoke<>(this::remove)
            )
        ).scheduleFor(this);
    }


    /* public void activate() {
         if (!isActivated) {
             isActivated = true;
             setAnimation(bombActivatedAnimation);

             new ActionSequence<TimeBomb>(
                 new Wait<>(this.timeInSecondsTillDetonation),
                 new Invoke<>(actor -> actor.setAnimation(bombDetonationAnimation)),
                 new Wait<>(bombDetonationAnimation.getFrameCount() * bombDetonationAnimation.getFrameDuration()),
                 new Invoke<>(this::explode)
             ).scheduleFor(this);
         }
     }*/
/*
    protected void explode() {
        Scene scene = getScene();
        if (scene != null) {
            scene.removeActor(this);
        }
    }

    public void activate() {
        setAnimation(bombActivatedAnimation);

        new ActionSequence<>(
            new When<>(() -> (getAnimation().getFrameCount() - 1) == getAnimation().getCurrentFrameIndex(),
                new Invoke<>(this::explode)
            )
        ).scheduleFor(this);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public Animation getBombDetonationAnimation() {
        return bombDetonationAnimation;
    }

    public void remove() {
        Objects.requireNonNull(getScene()).removeActor(this);
    }*/

}
