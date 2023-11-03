package hr.fer.tel.rassus.sensorsReadings;

import hr.fer.tel.rassus.sensorsReadings.webClient.HttpClient;
import hr.fer.tel.rassus.sensorsReadings.webClient.model.Reading;
import hr.fer.tel.rassus.sensorsReadings.webClient.model.Sensor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SensorDevice {

    private static final Logger logger = Logger.getLogger(SensorDevice.class.getName());

    private final RPCserver rpcServer;

    private RPCclient rpcClient;

    private final HttpClient httpClient;

    private static long startTime = System.currentTimeMillis();

    private static List<CSVRecord> sensorReadings = null;

    public SensorDevice(int port) {
        this.rpcServer = new RPCserver(new ReadingService(), port);
        this.httpClient = new HttpClient();
    }

    public static long getStartTime() {
        return startTime;
    }

    public static List<CSVRecord> getSensorReadings() {
        return sensorReadings;
    }

    public static boolean isOccupied(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return false;
        } catch (IOException e) {
            // if given port is occupied
            return true;
        }
    }

    public static List<Double> getGeoCoordinates() {
        Random random = new Random();
        double minLo = 15.87;
        double maxLo = 16.0;
        double minLa = 45.75;
        double maxLa = 45.85;
        double longitude = minLo + (maxLo - minLo) * random.nextDouble();
        double latitude = minLa + (maxLa - minLa) * random.nextDouble();
        return List.of(latitude, longitude);
    }

    public static Reading generateReading(long activeMiliSeconds, List<CSVRecord> sensorReadings) {
        int row = (int) (((activeMiliSeconds / 1000) % 100) + 1);
        if (row < sensorReadings.size()) {
            CSVRecord currentSensorReading = sensorReadings.get(row);
            return new Reading(currentSensorReading.get("Temperature").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("Temperature")),
                    currentSensorReading.get("Pressure").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("Pressure")),
                    currentSensorReading.get("Humidity").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("Humidity")),
                    currentSensorReading.get("CO").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("CO")),
                    currentSensorReading.get("NO2").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("NO2")),
                    currentSensorReading.get("SO2").isEmpty() ? 0.0 : Double.parseDouble(currentSensorReading.get("SO2")));
        } else return null;
    }

    private static Reading calibrate(Reading current, Reading neighbour) {

        if(neighbour == null) return current;
        Reading calibrated = new Reading();
        if(current.getTemperature() == 0.0 || neighbour.getTemperature() == 0.0) calibrated.setTemperature(current.getTemperature() + neighbour.getTemperature());
        else calibrated.setTemperature((current.getTemperature() + neighbour.getTemperature())/2);

        if(current.getPressure() == 0.0 || neighbour.getPressure() == 0.0) calibrated.setPressure(current.getPressure() + neighbour.getPressure());
        else calibrated.setPressure((current.getPressure() + neighbour.getPressure())/2);

        if(current.getHumidity() == 0.0 || neighbour.getHumidity() == 0.0) calibrated.setHumidity(current.getHumidity() + neighbour.getHumidity());
        else calibrated.setHumidity((current.getHumidity() + neighbour.getHumidity())/2);

        if(current.getCo() == 0.0 || neighbour.getCo() == 0.0) calibrated.setCo(current.getCo() + neighbour.getCo());
        else calibrated.setCo((current.getCo() + neighbour.getCo())/2);

        if(current.getNo2() == 0.0 || neighbour.getNo2() == 0.0) calibrated.setNo2(current.getNo2() + neighbour.getNo2());
        else calibrated.setNo2((current.getNo2() + neighbour.getNo2())/2);

        if(current.getSo2() == 0.0 || neighbour.getSo2() == 0.0) calibrated.setSo2(current.getSo2() + neighbour.getSo2());
        else calibrated.setSo2((current.getSo2() + neighbour.getSo2())/2);

        return calibrated;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        String csvFilePath = "./src/main/java/hr/fer/tel/rassus/sensorsReadings/sensorReadingsData/readings.csv";    // for csv file reading
        FileReader fileReader = new FileReader(csvFilePath);
        CSVParser csvParser = CSVParser.parse(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrailingDelimiter());
        sensorReadings = csvParser.getRecords();

        // 1. find non-occupied port
        int port = 8080;
        while(isOccupied(port)) {
            port++;
        }
        logger.info("Non-occupied port for this sensor: " + port);
        // 2. create sensor device: rcp server, rcp client and web client
        SensorDevice sensorDevice = new SensorDevice(port);

        // 3. start rcp server -> start the "sensor device"
        sensorDevice.rpcServer.start();

        // 4. get sensor's geocoordinates and register sensor (using web server)
        List<Double> geoCoordinates = getGeoCoordinates();
        Sensor sensorForReg = new Sensor(geoCoordinates.get(0), geoCoordinates.get(1), "127.0.0.1", (long) port);
        Integer sensorId = sensorDevice.httpClient.newSensor(sensorForReg);
        if(sensorId == null) {                // if for some reason sensor has not been saved on web server
            sensorDevice.rpcServer.stop();
            return;
        }
        logger.info("Sensor registered on web server. SensorID: " + sensorId + "\n" + sensorForReg);

        // 5. get nearest neighbour sensor from web server and init rcp client for nearest sensor rcp server if there is a neighbour
        Sensor nearestSensor = sensorDevice.httpClient.getNearestSensor(Long.valueOf(sensorId));
        boolean hasNeighbour = nearestSensor != null;
        if(hasNeighbour) {
            logger.info("Sensor " + sensorId + " has the nearest neighbour sensor available: " + nearestSensor + "\nOpening rpc channel with this neighbour sensor.");
            sensorDevice.rpcClient = new RPCclient(nearestSensor.getIp(), nearestSensor.getPort().intValue());
        } else logger.info("There is no active sensor in the nearby - there will not be any calibration.");

        while(sensorDevice.rpcServer.isRPCserverActive()
                && (hasNeighbour == (sensorDevice.rpcClient != null && sensorDevice.rpcClient.isRPCclientActive()))) {

            if(!hasNeighbour) {        // don't deprive the poor first sensor -> waiting for another sensor to become active
                nearestSensor = sensorDevice.httpClient.getNearestSensor(Long.valueOf(sensorId));
                hasNeighbour = nearestSensor != null;
                if(hasNeighbour) {
                    logger.info("Sensor " + sensorId + " has the nearest neighbour sensor available: " + nearestSensor + "\nOpening rpc channel with this neighbour sensor.");
                    sensorDevice.rpcClient = new RPCclient(nearestSensor.getIp(), nearestSensor.getPort().intValue());
                }
            }

            // 6. make current sensor reading (from the file based on time of client activity)
            Reading currentSensorReading = generateReading(System.currentTimeMillis() - startTime, sensorReadings);
            logger.info("Sensor has it's current readings: " + currentSensorReading);

            // 7. get readings from nearest neighbour sensor using rcp
            Reading neighbourReadings = null;
            if(hasNeighbour) {
                Empty requestReading = Empty.newBuilder().build();
                try {
                    neighbourReadings = sensorDevice.rpcClient.requestReading(requestReading);
                    if(neighbourReadings != null) logger.info("Got current readings from the neighbour sensor: " + neighbourReadings);
                    else logger.info("[Neighbour sensor rcp server not running] Readings data will be saved without calibration.");
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }

            // 8. calibrate current sensor readings with neighbour sensor readings
            Reading calibratedReadings = calibrate(currentSensorReading, neighbourReadings);
            logger.info(neighbourReadings != null ? "Calibrated readings: " + calibratedReadings : "Readings (no calibration): " + calibratedReadings);

            // 9. save calibrated readings in database using web server
            Integer readingId = sensorDevice.httpClient.newReading(Long.valueOf(sensorId), calibratedReadings);

            Thread.sleep(10000);
        }

        assert sensorDevice.rpcClient != null;
        sensorDevice.rpcClient.stop();                // shutdown in which preexisting calls continue but new calls are cancelled
        sensorDevice.rpcServer.blockUntilShutdown();  // wait for the rcp server to shut down

    }

}
