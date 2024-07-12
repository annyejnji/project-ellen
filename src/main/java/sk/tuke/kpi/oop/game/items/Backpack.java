package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private final List<Collectible> backpackItems = new ArrayList<>();
    private final String name;
    private final int capacity;

    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public @NotNull List<Collectible> getContent() {
        return List.copyOf(backpackItems);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return backpackItems.size();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void add(@NotNull Collectible actor) {
        if (backpackItems.size() >= capacity) {
            throw new IllegalStateException(name + "is full");
        }
        backpackItems.add(actor);
    }

    @Override
    public void remove(@NotNull Collectible actor) {
        backpackItems.remove(actor);
    }

    @Override
    public @Nullable Collectible peek() {
        if (backpackItems.isEmpty()) {
            return null;
        }
        return backpackItems.get(backpackItems.size() - 1);
    }

    @Override
    public void shift() {
        if (!backpackItems.isEmpty()) {
            Collectible lastItem = backpackItems.remove(backpackItems.size() - 1);
            backpackItems.add(0, lastItem);
        }
    }

    @NotNull
    @Override
    public Iterator<Collectible> iterator() {
        return backpackItems.iterator();
    }
}
