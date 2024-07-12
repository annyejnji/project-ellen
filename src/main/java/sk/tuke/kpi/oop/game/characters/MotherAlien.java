package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class MotherAlien extends Alien {
    private static final String MOTHERALIEN_ANIMATION = "sprites/mother.png";

    public MotherAlien() {
        this(200, null);
    }

    public MotherAlien(int healthValue, Behaviour<? super Alien> behaviour) {
        super(healthValue, behaviour);
        Animation motherAlienAnimation = new Animation(MOTHERALIEN_ANIMATION, 112, 162, 0.2f);
        setAnimation(motherAlienAnimation);
    }


}
