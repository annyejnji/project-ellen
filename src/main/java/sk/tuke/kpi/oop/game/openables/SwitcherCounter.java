package sk.tuke.kpi.oop.game.openables;

public class SwitcherCounter {
    private static SwitcherCounter instance;
    private int total;
    private int activated;

    public static SwitcherCounter getInstance() {
        if (instance == null) {
            instance = new SwitcherCounter();
        }
        return instance;
    }

    public void register() {
        total++;
    }

    public void activate() {
        if (activated < total) {
            activated++;
        }
    }

    public boolean allActivated() {
        return activated == total;
    }
}
