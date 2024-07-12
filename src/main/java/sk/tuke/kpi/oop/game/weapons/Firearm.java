package sk.tuke.kpi.oop.game.weapons;

public abstract class Firearm {

    private int currentAmmo;
    private int maximumAmmo;

    public Firearm(int initAmmo, int maxAmmo) {
        this.currentAmmo = initAmmo;
        this.maximumAmmo = maxAmmo;
    }

    public Firearm(int initAmmo) {
        this.currentAmmo = initAmmo;
        this.maximumAmmo = initAmmo;
    }

    public int getAmmo() {
        return currentAmmo;
    }

    public int getMaximumAmmo() {
        return maximumAmmo;
    }

    public void reload(int amount) {
        currentAmmo = Math.min(currentAmmo + amount, maximumAmmo);
    }

    protected abstract Fireable createBullet();

    public Fireable fire() {
        if (getAmmo() > 0) {
            currentAmmo--;
            return createBullet();
        }
        return null;
    }

    public void addAmmo(int amount) {
        // Add 'amount' ammo, but not exceeding the maximum ammo capacity
        currentAmmo = Math.min(currentAmmo + amount, maximumAmmo);
    }

    public void substractAmmo(int amount) {
        // Add 'amount' ammo, but not exceeding the maximum ammo capacity
        currentAmmo = Math.min(currentAmmo - amount, maximumAmmo);
    }
}
