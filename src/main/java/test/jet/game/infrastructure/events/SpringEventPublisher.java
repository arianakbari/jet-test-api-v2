package test.jet.game.infrastructure.events;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import test.jet.game.domain.events.IEventPublisher;

@Component
@AllArgsConstructor
public class SpringEventPublisher implements IEventPublisher {

    private ApplicationEventPublisher eventPublisher;

    @Override
    public <T> void publish(T event) {
        eventPublisher.publishEvent(event);
    }
}
