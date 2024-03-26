package test.jet.game.infrastructure.services;

import com.pusher.rest.Pusher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private Pusher pusher;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void givenToAndDataAndEvent_whenSend_then() {
        given(pusher.trigger("private-user-channel-to", "some-event", null)).willReturn(null);

        Assertions.assertDoesNotThrow(() -> {
            notificationService.send("to", null, "some-event");
        });
    }

    @Test
    public void givenToAndSocketId_whenSubscribe_thenChannelAuth() {
        given(pusher.authenticate("", "private-user-channel-to")).willReturn("");

        var result = notificationService.subscribe("to", "");

        Assertions.assertEquals(result, "");
    }
}
