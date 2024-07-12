package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ChainBomb extends TimeBomb {
    public ChainBomb(float secondsTillDetonation) {
        super(secondsTillDetonation);
    }

    @Override
    public void detonate() {
        if (getScene() == null) {
            return;
        }
        final int RADIUS = 50;

        Ellipse2D ellipse = new Ellipse2D.Float(this.getPosX() + (this.getWidth() / 2) - RADIUS,
            this.getPosY() + (this.getHeight() / 2) - RADIUS, 100, 100);

        for (Actor actor : getScene().getActors()) {
            if (actor instanceof ChainBomb) {
                Rectangle2D rectangle = new Rectangle2D.Float(actor.getPosX(), actor.getPosY(), actor.getWidth(), actor.getHeight());
                boolean intersects = ellipse.intersects(rectangle);
                ChainBomb chainBomb = (ChainBomb) actor;
                if (intersects && !chainBomb.isActivated()) {
                    chainBomb.activate();
                }

                super.detonate();
            }
        }
    }

    @Override
    public void activate() {
        if (!isActivated()) {
            super.activate();
        }
    }
}
