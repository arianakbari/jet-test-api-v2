package test.jet.game.domain.services;

public interface INotificationService {
    <D> void send(String to, D data, String event);
    String subscribe(String to, String socketId);
}
