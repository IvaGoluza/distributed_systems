package hr.fer.tel.rassus.sensorsReadings;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RPCserver {

    private static final Logger logger = Logger.getLogger(RPCserver.class.getName());

    private Server server;
    private final ReadingService service;
    private final int port;

    private boolean isRPCserverActive = false;

    public RPCserver(ReadingService service, int port) {
        this.service = service;
        this.port = port;
    }

    public void start() throws IOException {

        server = ServerBuilder.forPort(port)
                .addService(service)
                .build()
                .start();

        isRPCserverActive = true;
        logger.info("Server started on " + port);

        //  Clean shutdown of server in case of JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("[JVM is shutting down] => [Shutting down gRPC server]");
            try {
                RPCserver.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("[Server shut down]");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            isRPCserverActive = false;
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public boolean isRPCserverActive() {
        return isRPCserverActive;
    }


}
