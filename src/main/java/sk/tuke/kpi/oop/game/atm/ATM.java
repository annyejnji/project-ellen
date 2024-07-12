package sk.tuke.kpi.oop.game.atm;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class ATM extends AbstractActor {
    private static final String ATM_ANIMATION = "sprites/atm.png";

    private TransactionStrategy transactionStrategy;

    public ATM() {
        Animation atmAnimation = new Animation(ATM_ANIMATION);
        setAnimation(atmAnimation);
        int amount = 10;
        transactionStrategy = new HealthToAmmo(amount);
    }

    public void setTransactionStrategy(TransactionStrategy strategy) {
        this.transactionStrategy = strategy;
    }

    public void performTransaction() {
        if (transactionStrategy != null) {
            Ripley ripley = Objects.requireNonNull(getScene()).getFirstActorByType(Ripley.class);
            if (ripley != null) {
                transactionStrategy.executeTransaction(ripley);
            }
        }
    }
}
