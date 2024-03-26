package test.jet.game.domain.events;

public interface IEventPublisher {
    <T> void publish(T event);
}
