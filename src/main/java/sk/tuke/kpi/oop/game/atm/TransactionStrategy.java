package sk.tuke.kpi.oop.game.atm;

import sk.tuke.kpi.oop.game.characters.Ripley;

public interface TransactionStrategy {
    void executeTransaction(Ripley ripley);
}
