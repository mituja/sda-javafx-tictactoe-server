import pl.sda.poznan.Message;

/**
 * Interface sends right message according to received message
 */
public interface RequestListener {

    Message onMessageReceived(Message request);
}
