import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ThriftServer {


    static Socket clientSocket = null;
    static PrintStream os = null;
    static DataInputStream is = null;
    static BufferedReader inputLine = null;
    static Socket clientSocket2 = null;
    static PrintStream os2 = null;
    static DataInputStream is2 = null;
    static BufferedReader inputLine2 = null;

    static boolean closed = false;

    public static String DATABASE_IP = System.getenv("DATABASE_IP");
    public static int DATABASE_PORT = Integer.parseInt(System.getenv("DATABASE_PORT"));


    public static String DATABASE2_IP = System.getenv("DATABASE2_IP");
    public static int DATABASE2_PORT = Integer.parseInt(System.getenv("DATABASE2_PORT"));

    public final static int COORDINATOR_PORT = Integer.parseInt(System.getenv("COORDINATOR_PORT"));

    private static int entry_id = 0;
    public static class MessageHandler implements ThriftService.Iface {
        @Override
        public boolean createEntry(SensorData sensorData) throws TException {
            System.out.println("[Server] received: " + sensorData);

            String RPCType = "createEntry";
            String SensorType = sensorData.getSensorType();
            String TemperatureValue = sensorData.getTemperatureValue();
            String HumidityValue = sensorData.getHumidityValue();
            String FluidValue = sensorData.getFluidValue();

            String TransportString = RPCType + "_"+ entry_id + "_" + SensorType + "_" + TemperatureValue + "_" + HumidityValue + "_" + FluidValue;
            entry_id++;

            try {
                clientSocket = new Socket(DATABASE_IP, DATABASE_PORT);
                inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new DataInputStream(clientSocket.getInputStream());

                clientSocket2 = new Socket(DATABASE2_IP, DATABASE2_PORT);
                inputLine2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
                os2 = new PrintStream(clientSocket2.getOutputStream());
                is2 = new DataInputStream(clientSocket2.getInputStream());

            } catch (Exception e) {
                System.out.println("Exception occurred : "+e.getMessage());
            }

            if ((clientSocket != null && os != null && is != null) && (clientSocket2 != null && os2 != null && is2 != null))
            {
                try
                {
                    os.println(TransportString);
                    os2.println(TransportString);

                    String vote = inputLine.readLine();
                    String vote2 = inputLine2.readLine();

                    if(vote.equals("ABORT") || vote2.equals("ABORT")){
                        System.out.println("Redundant PK, Aborted.");
                        os.println("ABORTED");
                        os2.println("ABORTED");
                        os.close();
                        os2.close();
                        is.close();
                        is2.close();
                        clientSocket.close();
                        clientSocket2.close();
                    }else if (vote.equals("COMMIT") && vote2.equals("COMMIT")){
                        System.out.println("No Redundant PK, Committed.");
                        os.println("COMMITTED");
                        os2.println("COMMITTED");
                    }
                } catch (IOException e)
                {
                    System.err.println("IOException:  " + e);
                }
            }
            return true;
        }

        @Override
        public Map<String,String> readEntry(String received_ID) throws TException {

            String RPCType = "readEntry";

            String TransportString = RPCType + "_"+ received_ID;

            try {
                clientSocket = new Socket(DATABASE_IP, DATABASE_PORT);
                inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new DataInputStream(clientSocket.getInputStream());

                clientSocket2 = new Socket(DATABASE2_IP, DATABASE2_PORT);
                inputLine2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
                os2 = new PrintStream(clientSocket2.getOutputStream());
                is2 = new DataInputStream(clientSocket2.getInputStream());

            } catch (Exception e) {
                System.out.println("Exception occurred : "+e.getMessage());
            }

            if ((clientSocket != null && os != null && is != null) && (clientSocket2 != null && os2 != null && is2 != null))
            {
                try
                {
                    os.println(TransportString);
                    os2.println(TransportString);

                    String vote = inputLine.readLine();
                    String vote2 = inputLine2.readLine();

                    if(vote.equals("ABORT") || vote2.equals("ABORT")){
                        System.out.println("Can not read Entry. Aborted.");
                        os.println("ABORTED");
                        os2.println("ABORTED");
                        os.close();
                        os2.close();
                        is.close();
                        is2.close();
                        clientSocket.close();
                        clientSocket2.close();
                    }else if (vote.equals("COMMIT") && vote2.equals("COMMIT")){
                        String readEntry = inputLine.readLine();
                        System.out.println(readEntry);
                        os.println("COMMITTED");
                        os2.println("COMMITTED");
                    }
                } catch (IOException e)
                {
                    System.err.println("IOException:  " + e);
                }
            }
            return null;

        }

        @Override
        public String updateEntry(String received_ID, Map<String, String> newEntry) throws TException {

            String RPCType = "updateEntry";
            String SensorType = newEntry.get("Sensor Type");
            String TemperatureValue = newEntry.get("Temperature");
            String HumidityValue = newEntry.get("Humidity");
            String FluidValue = newEntry.get("Fluid");

            String TransportString = RPCType + "_"+ received_ID + "_" + SensorType + "_" + TemperatureValue + "_" + HumidityValue + "_" + FluidValue;

            try {
                clientSocket = new Socket(DATABASE_IP, DATABASE_PORT);
                inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new DataInputStream(clientSocket.getInputStream());

                clientSocket2 = new Socket(DATABASE2_IP, DATABASE2_PORT);
                inputLine2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
                os2 = new PrintStream(clientSocket2.getOutputStream());
                is2 = new DataInputStream(clientSocket2.getInputStream());

            } catch (Exception e) {
                System.out.println("Exception occurred : "+e.getMessage());
            }

            if ((clientSocket != null && os != null && is != null) && (clientSocket2 != null && os2 != null && is2 != null))
            {
                try
                {
                    os.println(TransportString);
                    os2.println(TransportString);

                    String vote = inputLine.readLine();
                    String vote2 = inputLine2.readLine();

                    if(vote.equals("ABORT") || vote2.equals("ABORT")){
                        System.out.println("Can not make update. Aborted.");
                        os.println("ABORTED");
                        os2.println("ABORTED");
                        os.close();
                        os2.close();
                        is.close();
                        is2.close();
                        clientSocket.close();
                        clientSocket2.close();
                    }else if (vote.equals("COMMIT") && vote2.equals("COMMIT")){
                        System.out.println("Committed updated Entry.");
                        os.println("COMMITTED");
                        os2.println("COMMITTED");
                    }
                } catch (IOException e)
                {
                    System.err.println("IOException:  " + e);
                }
            }
            return null;
        }

        @Override
        public String deleteEntry(String received_ID) throws TException {

            String RPCType = "deleteEntry";

            String TransportString = RPCType + "_"+ received_ID;

            try {
                clientSocket = new Socket(DATABASE_IP, DATABASE_PORT);
                inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintStream(clientSocket.getOutputStream());
                is = new DataInputStream(clientSocket.getInputStream());

                clientSocket2 = new Socket(DATABASE2_IP, DATABASE2_PORT);
                inputLine2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
                os2 = new PrintStream(clientSocket2.getOutputStream());
                is2 = new DataInputStream(clientSocket2.getInputStream());

            } catch (Exception e) {
                System.out.println("Exception occurred : "+e.getMessage());
            }

            if ((clientSocket != null && os != null && is != null) && (clientSocket2 != null && os2 != null && is2 != null))
            {
                try
                {
                    os.println(TransportString);
                    os2.println(TransportString);

                    String vote = inputLine.readLine();
                    String vote2 = inputLine2.readLine();

                    if(vote.equals("ABORT") || vote2.equals("ABORT")){
                        System.out.println("Can not delete Entry. Aborted.");
                        os.println("ABORTED");
                        os2.println("ABORTED");
                        os.close();
                        os2.close();
                        is.close();
                        is2.close();
                        clientSocket.close();
                        clientSocket2.close();
                    }else if (vote.equals("COMMIT") && vote2.equals("COMMIT")){
                        System.out.println("Committed to delete Entry.");
                        os.println("COMMITTED");
                        os2.println("COMMITTED");
                    }
                } catch (IOException e)
                {
                    System.err.println("IOException:  " + e);
                }
            }
            return null;
        }
    }

    ThriftServer() {
        try{

            TServerSocket transsvr = new TServerSocket(COORDINATOR_PORT);
            TProcessor proc = new ThriftService.Processor<>(new MessageHandler());
            TServer server = new TSimpleServer(new TServer.Args(transsvr).processor(proc));

            System.out.println("[Server] waiting for connections");

            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}