package sk.tuke.kpi.oop.game.atm;

import sk.tuke.kpi.oop.game.characters.Ripley;

public class HealthToAmmo implements TransactionStrategy {

    private final int amount;

    public HealthToAmmo(int amount) {
        this.amount = amount;
    }

    @Override
    public void executeTransaction(Ripley ripley) {
        if (ripley.getHealth().getValue() > 10 && ripley.getFirearm().getAmmo() <= 190) {
            ripley.getHealth().drain(amount);
            ripley.getFirearm().addAmmo(amount);
        }
    }
}
