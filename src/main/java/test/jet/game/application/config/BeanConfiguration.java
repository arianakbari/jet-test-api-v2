package test.jet.game.application.config;

import com.pusher.rest.Pusher;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.jet.game.domain.events.IEventPublisher;
import test.jet.game.domain.factories.IGameFactory;
import test.jet.game.domain.repositories.IGameRepository;
import test.jet.game.domain.services.GameService;
import test.jet.game.domain.services.IGameService;

@Configuration
public class BeanConfiguration {

    @Value("${pusher.appId}")
    private String pusherAppId;

    @Value("${pusher.apiKey}")
    private String pusherApiKey;

    @Value("${pusher.apiSecret}")
    private String pusherSecret;

    @Bean
    public IGameService gameService(IGameRepository gameRepository, IGameFactory gameFactory, IEventPublisher eventPublisher) {
        return new GameService(gameRepository, gameFactory, eventPublisher);
    }

    @Bean
    public Pusher pusher() {
        var pusher = new Pusher(pusherAppId, pusherApiKey, pusherSecret);
        pusher.setCluster("eu");
        return pusher;
    }
}
