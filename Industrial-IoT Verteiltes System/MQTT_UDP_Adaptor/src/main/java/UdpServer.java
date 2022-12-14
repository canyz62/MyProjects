
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class UdpServer extends Thread {
    public static ArrayList<String> data = new ArrayList<>();
    public static ArrayList<String> singledata = new ArrayList<>();

    @Override
    public void run (){
        try {
            new UdpServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static final int BUFFER_LENGTH = 1500;
    public UdpServer() throws Exception{

        //data.add("B_0_0_0");
        System.out.println("SIZE OF MQTT DATA : " + data.size());
        int ADAPTOR_PORT = Integer.parseInt(System.getenv("ADAPTOR_PORT"));
        DatagramSocket socket = new DatagramSocket(ADAPTOR_PORT);
        System.out.println("running...");

        while(true) {

            // receive Requests
            byte[] buffer = new byte[BUFFER_LENGTH];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Receiving Requests ...");
            socket.receive(packet);
            String message = new String(buffer).trim();
            System.out.println("Received Request from IoT-Gateway: " + message + " – at: " + java.time.LocalTime.now());

            // send Sensor Data to IoT-Gateway
            buffer = data.get(data.size()-1).getBytes();
            //data.clear();
            packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort() );
            socket.send(packet);

        }
    }

    public static ArrayList<String> getRTT(){
        return data;
    }

    public static int getSingledataSize(){
        return singledata.size();
    }

}
