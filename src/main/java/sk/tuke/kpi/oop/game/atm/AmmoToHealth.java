package sk.tuke.kpi.oop.game.atm;

import sk.tuke.kpi.oop.game.characters.Ripley;

public class AmmoToHealth implements TransactionStrategy {

    private final int amount;

    public AmmoToHealth(int amount) {
        this.amount = amount;
    }

    @Override
    public void executeTransaction(Ripley ripley) {
        if (ripley.getHealth().getValue() < 91) {
            ripley.getFirearm().substractAmmo(amount);
            ripley.getHealth().refill(amount);
        }
    }

}

