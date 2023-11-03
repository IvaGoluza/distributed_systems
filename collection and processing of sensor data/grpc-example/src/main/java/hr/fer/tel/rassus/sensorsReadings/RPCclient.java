package hr.fer.tel.rassus.sensorsReadings;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RPCclient {

    private static final Logger logger = Logger.getLogger(RPCclient.class.getName());

    private boolean isRPCclientActive = false;
    private final ManagedChannel channel;
    private final ReadingGrpc.ReadingBlockingStub readingBlockingStub;

    public RPCclient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.readingBlockingStub = ReadingGrpc.newBlockingStub(channel);
        this.isRPCclientActive = true;
        logger.info("Open channel for gRPC communication between sensors. gRPC client started for gRPC server on port " + port);
    }

    public void stop() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        isRPCclientActive = false;
    }

    public boolean isRPCclientActive() {
        return isRPCclientActive;
    }

    public Reading requestReading(Empty requestReading) {

        logger.info("Sending request for nearest neighbour sensor readings.");
        try {
            ReadingMsg response = readingBlockingStub.requestReading(requestReading);  // request reading using stub

            logger.info("Received readings from nearest sensor neighbour:\n[Temperature]: " + response.getTemperature() + "\n[Pressure]: " + response.getPressure() + "\n[Humidity]: " + response.getHumidity() + "\n[CO]: " + response.getCo() + "\n[NO2]: " + response.getNo2() + "\n[SO2]: " + response.getSo2());
            return new Reading(response.getTemperature(), response.getPressure(), response.getHumidity(), response.getCo(), response.getNo2(), response.getSo2());
        } catch (StatusRuntimeException e) {
            logger.info("[RPC failed - neighbour sensor is no longer running] " + e.getMessage());
            return null;
        }
    }

}
