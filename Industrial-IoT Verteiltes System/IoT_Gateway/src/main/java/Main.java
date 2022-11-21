import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {

    static ArrayList<Long> UDPRTTs = new ArrayList<>();
    static ArrayList<Long> TCPRTTs = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        //Run this once after 10 Minutes for average RTT
        Timer timer = new Timer(600000, new ActionListener() { // 300000 ms = 5 min
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //UDP RTT
                UDPRTTs = UdpClient.getRTT();
                long AmountOfUDPRTTs = UDPRTTs.size();
                long UDPRTTsSummedUp = 0;
                long UDPRTTsAverage = 0;
                for(int i = 0; i < UDPRTTs.size(); i++){
                    UDPRTTsSummedUp += UDPRTTs.get(i);
                }
                UDPRTTsAverage = UDPRTTsSummedUp / AmountOfUDPRTTs;

                //TCP RTT
                TCPRTTs = TcpClient.getRTT();
                long AmountOfTCPRTTs = TCPRTTs.size();
                long TCPRTTsSummedUp = 0;
                long TCPRTTsAverage = 0;
                for(int i = 0; i < TCPRTTs.size(); i++){
                    TCPRTTsSummedUp += TCPRTTs.get(i);
                }
                TCPRTTsAverage = TCPRTTsSummedUp / AmountOfTCPRTTs;


                //File for UDP
                //create File
                File file1 = new File("UDPRTT.txt");

                //create File writer class
                FileWriter fw = null;
                try {
                    fw = new FileWriter(file1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //create a print writer class
                PrintWriter pw = new PrintWriter(fw);
                String UDPRTTFinal = String.valueOf(UDPRTTsAverage);
                String UDPRTTFinalAmount = String.valueOf(AmountOfUDPRTTs);
                pw.println("Average RTT Time for UDP Socket after 10 minutes: ");
                pw.println(UDPRTTFinal + "ns");
                pw.println("Received Packets: ");
                pw.println(UDPRTTFinalAmount);
                System.out.println("Average RTT Time for UDP Socket after 10 minutes: " + UDPRTTFinal);
                System.out.println("Received Packets: " + UDPRTTFinalAmount);
                pw.close();


                //File for TCP
                //create File
                File file2 = new File("TCPRTT.txt");

                //create File writer class
                FileWriter fw2 = null;
                try {
                    fw2 = new FileWriter(file2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //create a print writer class
                PrintWriter pw2 = new PrintWriter(fw2);
                String TCPRTTFinal = String.valueOf(TCPRTTsAverage);
                String TCPRTTFinalAmount = String.valueOf(AmountOfTCPRTTs);
                pw2.println("Average RTT Time for TCP Socket after 10 minutes: ");
                pw2.println(TCPRTTFinal + "ns");
                pw2.println("Received Packets: ");
                pw2.println(TCPRTTFinalAmount);
                System.out.println("Average RTT Time for UDP Socket after 10 minutes: " + TCPRTTFinal);
                System.out.println("Received Packets: " + TCPRTTFinalAmount);
                pw2.close();

            }
        });
        timer.setRepeats(false); // Only execute once
        timer.start(); // Go go go!

        UdpClient udp_client = new UdpClient();
        TcpClient tcp_client = new TcpClient();

        while(true){
            try {

                // get Data from Sensor A
                String rcv_A_data = udp_client.get_A_values();
                TimeUnit.SECONDS.sleep(1);

                // Send received data from Sensor A to the http server
                tcp_client.communicateWithCloud(rcv_A_data);
                TimeUnit.SECONDS.sleep(1);

                // get Sensor B Data from Adaptor
                String rcv_B_data = udp_client.get_B_values();
                TimeUnit.SECONDS.sleep(1);

                // Send received Sensor B Data to the http server
                tcp_client.communicateWithCloud(rcv_B_data);
                TimeUnit.SECONDS.sleep(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
