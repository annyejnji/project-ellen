package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;

public class Fire<A extends Armed> extends AbstractAction<A> {
    public Fire() {
        super();
    }

    @Override
    public void execute(float deltaTime) {
        A actor = getActor();
        if (actor == null) {
            setDone(true);
            return;
        }

        Scene scene = actor.getScene();
        if (scene == null) {
            setDone(true);
            return;
        }

        Fireable bullet = actor.getFirearm().fire();
        if (bullet == null) {
            setDone(true);
            return;
        }

        Direction direction = Direction.fromAngle(actor.getAnimation().getRotation());

        int bulletX = actor.getPosX() + (actor.getWidth() / 2) - (bullet.getWidth() / 2);
        int bulletY = actor.getPosY() + (actor.getHeight() / 2) - (bullet.getHeight() / 2);

        actor.getScene().addActor(bullet, bulletX, bulletY);

        new Move<Fireable>(direction, Short.MAX_VALUE).scheduleFor(bullet);

        setDone(true);
    }
}
