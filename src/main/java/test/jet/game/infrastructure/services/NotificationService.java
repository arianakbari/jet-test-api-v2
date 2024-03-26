package test.jet.game.infrastructure.services;

import com.pusher.rest.Pusher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import test.jet.game.domain.services.INotificationService;

@Service
@AllArgsConstructor
public class NotificationService implements INotificationService {

    private Pusher pusher;

    @Override
    public <D> void send(String to, D data, String event) {
        pusher.trigger(getChannelName(to), event, data);
    }

    @Override
    public String subscribe(String to, String socketId) {
        return pusher.authenticate(socketId, getChannelName(to));
    }

    private String getChannelName(String to) {
        return "private-user-channel-" + to;
    }
}
