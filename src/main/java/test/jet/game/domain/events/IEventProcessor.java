package test.jet.game.domain.events;

public interface IEventProcessor<T> {
    void process(T event);
}
