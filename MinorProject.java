import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.ArrayList;

import edu.princeton.cs.introcs.StdDraw;

public class MinorProject {
    public static void main(String[] args) throws IOException, InterruptedException {

        var myUSBPort = "COM3";
        // initializing the arduino board

        var arduinoDevice = new FirmataDevice(myUSBPort);
        arduinoDevice.start();
        arduinoDevice.ensureInitializationIsDone();


        // pin for the sensor
        Pin sensorObject = arduinoDevice.getPin(14);



        // Setting button to be the input
        Pin buttonObject = arduinoDevice.getPin(6);
        buttonObject.setMode(Pin.Mode.INPUT);


        // Setting the water pump to be the output
        Pin pumpObject = arduinoDevice.getPin(7);
        pumpObject.setMode(Pin.Mode.OUTPUT);



        // Initialize the OLED

        I2CDevice i2cObject = arduinoDevice.getI2CDevice((byte) 0x3C);
        SSD1306 ledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);

        ledObject.init();

        ArrayList<Float> dataList = new ArrayList<>();

        // graph that shows sensor value vs time

        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(0, 1023);
        StdDraw.setPenRadius(0.0002);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(50, 1000, "Sensor Value VS Time");
        StdDraw.line(0,0 , 0, 1000);
        StdDraw.line(0, 0, 100, 0);
        StdDraw.text(50, -30, "Time");
        StdDraw.text(-50, 500, "Sensor Values");


        double sample = 0.0;



        //loop that can turn the pump on and off according to the moisture values

        while (true) {

            ledObject.getCanvas().drawString(0, 0, "Moisture " + sensorObject.getValue());
            ledObject.display();

            StdDraw.text(sample,(double)sensorObject.getValue(),"*" );

            Thread.sleep(500);

            if (sample<100){
                sample++;
            }

            if (sensorObject.getValue() > 530) {
                pumpObject.setValue(1);
                ledObject.getCanvas().drawString(0, 10, "Pump is Turned On!");
                ledObject.display();
                pumpObject.setValue(0);
            }

            else if (sensorObject.getValue()>= 400) {

                pumpObject.setValue(0);
                ledObject.getCanvas().drawString(0, 10, "Pump is Turned off!");
                ledObject.display();
            }

            else {
                pumpObject.setValue(0);

                ledObject.getCanvas().drawString(0, 10, "Pump is turned off!");
                ledObject.display();
            }



        }



    }


}






