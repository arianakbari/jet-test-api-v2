package test.jet.game.domain.services;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import test.jet.game.domain.events.IEventPublisher;
import test.jet.game.domain.factories.IGameFactory;
import test.jet.game.domain.repositories.IGameRepository;

public class GameServiceTest {

    @Mock
    private IGameRepository repository;
    @Mock
    private IGameFactory gameFactory;
    @Mock
    private IEventPublisher eventPublisher;

    @InjectMocks
    private GameService gameService;
}
