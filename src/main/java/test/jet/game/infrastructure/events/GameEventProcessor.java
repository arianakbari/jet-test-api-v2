package test.jet.game.infrastructure.events;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import test.jet.game.domain.events.GameEvent;
import test.jet.game.domain.events.IEventProcessor;
import test.jet.game.domain.services.INotificationService;

@Component
@AllArgsConstructor
public class GameEventProcessor implements IEventProcessor<GameEvent> {

    private INotificationService notificationService;

    @Override
    @EventListener(GameEvent.class)
    @Async
    public void process(GameEvent event) {
        notificationService.send(event.getPlayerId().getValue(), event.getGame(), event.getType());
    }
}
