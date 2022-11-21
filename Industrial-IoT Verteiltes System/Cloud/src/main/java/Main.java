import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.thrift.transport.TTransportException;

import javax.swing.*;

public class Main {

    static ArrayList<Long> ThriftRTTs = new ArrayList<>();

    public static void main(String[] args) throws TTransportException {

        //Run this once after 10 Minutes for average RTT
        Timer timer = new Timer(600000, new ActionListener() { // 300000 ms = 5 min
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Thrift RTT
                ThriftRTTs = TcpServer.getRTT();
                long AmountOfThriftRTTs = ThriftRTTs.size();
                long ThriftRTTsSummedUp = 0;
                long ThriftRTTsAverage = 0;
                for(int i = 0; i < ThriftRTTs.size(); i++){
                    ThriftRTTsSummedUp += ThriftRTTs.get(i);
                }
                ThriftRTTsAverage = ThriftRTTsSummedUp / AmountOfThriftRTTs;

                //File for Thrift
                //create File
                File file1 = new File("ThriftRTT.txt");

                //create File writer class
                FileWriter fw = null;
                try {
                    fw = new FileWriter(file1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //create a print writer class
                PrintWriter pw = new PrintWriter(fw);
                String ThriftRTTFinal = String.valueOf(ThriftRTTsAverage);
                String ThriftRTTFinalAmount = String.valueOf(AmountOfThriftRTTs);
                pw.println("Average RTT Time for Thrift Socket after 10 minutes: ");
                pw.println(ThriftRTTFinal + "ns");
                pw.println("Received Packets: ");
                pw.println(ThriftRTTFinalAmount);
                System.out.println("Average RTT Time for Thrift Socket after 10 minutes: " + ThriftRTTFinal);
                System.out.println("Received Packets: " + ThriftRTTFinalAmount);
                pw.close();
            }
        });
        timer.setRepeats(false); // Only execute once
        timer.start(); // Go go go!


        final int COORDINATOR_PORT = Integer.parseInt(System.getenv("COORDINATOR_PORT"));
        final String COORDINATOR_IP = System.getenv("COORDINATOR_IP");
        ExecutorService pool = Executors.newFixedThreadPool(50);

        //// Thrift Client ////
        TSocket thrift_socket = new TSocket(COORDINATOR_IP, COORDINATOR_PORT);
        thrift_socket.open();
        ThriftService.Client thrift_client = new ThriftService.Client(new TBinaryProtocol(thrift_socket));

        try (ServerSocket tcp_socket = new ServerSocket(TcpServer.GATE_CLOUD_PORT))
        {
            while (true) {
                try {
                    Socket accepted_tcp_connection = tcp_socket.accept();
                    Callable<Void> task = new TcpServer.MultiThreaded_TCP(accepted_tcp_connection, thrift_client);
                    pool.submit(task);
                } catch (IOException ex) {

                } catch (TTransportException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server");
        }
    }
}


// To Run multiple Threads go to :
// https://stackoverflow.com/a/41461744