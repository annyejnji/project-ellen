package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Firearm;

public class Ammo extends AbstractActor implements Usable<Armed> {
    private static final String AMMO_ANIMATION = "sprites/ammo.png";

    public Ammo() {
        Animation ammoAnimation = new Animation(AMMO_ANIMATION);
        setAnimation(ammoAnimation);
    }

    @Override
    public void useWith(Armed actor) {
        if (actor == null) {
            return;
        }

        Firearm firearm = actor.getFirearm();
        if (firearm.getAmmo() < firearm.getMaximumAmmo()) {
            firearm.reload(50);

            Scene scene = actor.getScene();
            if (scene != null) {
                scene.removeActor(this);
            }
        }
    }

    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }
}
