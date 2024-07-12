package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

// Po zobratí ochráni ripley pred alienmi (nebudú jej po styku s ňou znižovať zdravie) na 10 sekúnd.

public class Armor extends AbstractActor implements Usable<Armed> {
    private static final String ARMOR_ANIMATION = "sprites/armor.png";
    private boolean used;

    public Armor() {
        Animation armorAnimation = new Animation(ARMOR_ANIMATION);
        setAnimation(armorAnimation);
        used = false;
    }

    @Override
    public void useWith(Armed actor) {
        if (!used && actor instanceof Ripley) {
            Ripley ripley = (Ripley) actor;
            ripley.setArmor(true);
            used = true;

            new ActionSequence<>(
                new Wait<>(10),
                new Invoke<>(() -> {
                    ripley.setArmor(false);
                })
            ).scheduleFor(actor);

            Objects.requireNonNull(getScene()).removeActor(this);
        }
    }

    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }
}
