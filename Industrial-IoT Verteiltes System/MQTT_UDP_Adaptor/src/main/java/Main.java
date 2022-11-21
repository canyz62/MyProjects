import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main {

    static ArrayList<String> MQTTRTTs = new ArrayList<>();
    static int MQTTCounter = 1 ;

    public static void main(String[] args) throws Exception {

        Timer timer = new Timer(60000, new ActionListener() { // 60000ms = 60s
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //UDP RTT
                MQTTRTTs = UdpServer.getRTT();
                long AmountOfMQTT = MQTTRTTs.size();

                //File for UDP
                //create File
                File file1 = new File("MQTTRTT.txt");

                //create a print writer class
                PrintWriter pw = null;
                try {
                    if (!file1.exists()) file1.createNewFile();
                    pw = new PrintWriter(new FileOutputStream(file1, true));

                    String MQTTFinal = String.valueOf(AmountOfMQTT);
                    pw.println("Run (every 60 seconds): " + MQTTCounter);
                    pw.println("MQTT Messages Total: " + MQTTFinal + " Messages");
                    pw.println("MQTT Messages this run: " + UdpServer.getSingledataSize() + " Messages");
                    pw.println("Average after " + MQTTCounter + " runs: " + AmountOfMQTT/MQTTCounter);
                    System.out.println("Run (every 60 seconds): " + MQTTCounter);
                    System.out.println("MQTT Messages Total: " + MQTTFinal + " Messages");
                    System.out.println("MQTT Messages this run: " + UdpServer.getSingledataSize() + " Messages");
                    System.out.println("Average after " + MQTTCounter + " runs: " + AmountOfMQTT/MQTTCounter);
                    MQTTCounter++;
                    UdpServer.singledata.clear();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (pw != null) {
                        pw.flush();
                        pw.close();
                    }
                }

            }
        });
        timer.setRepeats(true); // Only execute once
        timer.start(); // Go go go!

        MQTT_Subscriber mqttSubscriber = new MQTT_Subscriber();
        mqttSubscriber.connect();

        UdpServer udpserver = new UdpServer();
        udpserver.start();
    }
}
