package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;

public class Wrench extends BreakableTool<DefectiveLight> implements Collectible {
    private static final String WRENCH_ANIMATION = "sprites/wrench.png";

    public Wrench() {
        super(2);
        Animation wrenchAnimation = new Animation(WRENCH_ANIMATION);
        setAnimation(wrenchAnimation);
    }

    @Override
    public void useWith(DefectiveLight defectiveLight) {
        if (defectiveLight == null) {
            return;
        }
        boolean repaired = defectiveLight.repair();

        if (repaired) {
            super.useWith(defectiveLight);
        }
    }

    @Override
    public Class<DefectiveLight> getUsingActorClass() {
        return DefectiveLight.class;
    }
}
