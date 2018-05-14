import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public static final int DEFAULT_PORT = 5000;
    private static final String DEFAULT_HOST_NAME = "localhost";
    private ServerSocket serverSocket;

    /**
     * Creates server with default port 5000 and listens on localhost
     */
    public Server() throws IOException {
        logger.log(Level.INFO, String.format("Creating socket on adresss %s on port %d", DEFAULT_HOST_NAME, DEFAULT_PORT));
        this.serverSocket = new ServerSocket(DEFAULT_PORT, 10, InetAddress.getByName(DEFAULT_HOST_NAME));
    }

    /**
     * @param adress        - server adress
     * @param port          - server port
     * @param maxConnection - max client number accepted by sarver
     */
    public Server(String adress, int port, int maxConnection) throws IOException {
        this.serverSocket = new ServerSocket(port, maxConnection, InetAddress.getByName(adress));
    }

    public void waitForConnection() throws IOException {
        logger.info(String.format("Server is listening on port %d", serverSocket.getLocalPort(), serverSocket));
        Socket socket = null;

        //po stronie klienta gniazdo ma metode connect
        //po stronie serwera gniazdo musi miec metode accept, jest to metoda blokujaca
        //czyli serwer na tej metodzie się zawiesi, wątek będzie czekał az ktos sie polaczy
        //jak to nastapi, to zostanie utworzony obiekt gniazdo
        while ((serverSocket.accept()) != null) {
            talkWithClient(socket);
        }

    }

    private void talkWithClient(Socket socket) {
        logger.info("Client connected... Talking with the client");
        Thread clientThread = new Thread(() -> {
            try {
                Worker worker = new Worker(socket);
                worker.setRequestListener(new MessageRequestListener());
                worker.startCommunication();
            } catch (IOException | ClassNotFoundException e) {
                logger.info("Error on client thread" + e.getMessage());
            }
        });
        clientThread.start();
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }
}
