package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.messages.Topic;

import java.util.Objects;
import java.util.function.Predicate;

public class Observing<T, A extends Actor> implements Behaviour<A> {
    private final Topic<T> topic;
    private final Predicate<T> predicate;
    private final Behaviour<A> delegate;

    public Observing(Topic<T> topic, Predicate<T> predicate, Behaviour<A> delegate) {
        this.topic = topic;
        this.predicate = predicate;
        this.delegate = delegate;
    }

    @Override
    public void setUp(A actor) {
        if (actor == null) {
            return;
        }
        Objects.requireNonNull(actor.getScene()).getMessageBus().subscribe(topic, a -> {
            if (predicate.test(a)) {
                delegate.setUp(actor);
            }
        });
    }
}

// PROBLEM je 5.9 hotove aj to posledne?
