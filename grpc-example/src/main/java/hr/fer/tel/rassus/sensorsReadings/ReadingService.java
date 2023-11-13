package hr.fer.tel.rassus.sensorsReadings;

import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;

import java.util.logging.Logger;

public class ReadingService extends ReadingGrpc.ReadingImplBase{

    private static final Logger logger = Logger.getLogger(ReadingService.class.getName());

    @Override
    public void requestReading(
            hr.fer.tel.rassus.sensorsReadings.Empty request,
            io.grpc.stub.StreamObserver<hr.fer.tel.rassus.sensorsReadings.ReadingMsg> responseObserver) {
        logger.info("Got a new sensor readings request. ");

        // generate current sensor readings
        Reading reading = SensorDevice.generateReading(System.currentTimeMillis() - SensorDevice.getStartTime(), SensorDevice.getSensorReadings());

        // Create response
        assert reading != null;
        ReadingMsg response = ReadingMsg.newBuilder()
                .setTemperature(reading.getTemperature())
                .setPressure(reading.getPressure())
                .setHumidity(reading.getHumidity())
                .setCo(reading.getCo())
                .setSo2(reading.getSo2())
                .setNo2(reading.getNo2())
                .build();
        // Send response
        responseObserver.onNext(
                response
        );
        logger.info("Sending my current readings to the neighbour sensor:\n[Temperature]: " + response.getTemperature() + "\n[Pressure]: " + response.getPressure() + "\n[Humidity]: " + response.getHumidity() + "\n[CO]: " + response.getCo() + "\n[NO2]: " + response.getNo2() + "\n[SO2]: " + response.getSo2());
        // Send a notification of successful stream completion.
        responseObserver.onCompleted();

    }


}
