import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    //boolean closed = false, inputFromAll = false;
    List<ClientThread> thread;
    List<String> data;

    int entry_id = 0;       //WICHTIG

    static DatabaseManager dbm = new DatabaseManager();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5000);

        //// Data Base ////
        dbm.createDatabase("Sensors Database");
        Database db = dbm.getDatabaseHashMap().get("Sensors Database");
        db.createTable("Sensors Table");

        //TCP Server
        Socket clientSocket = null;

        //int DATABASE_PORT = Integer.parseInt(System.getenv("DATABASE_PORT"));
        int DATABASE2_PORT = Integer.parseInt(System.getenv("DATABASE2_PORT"));


        Main server = new Main();
        try(ServerSocket serverSocket = new ServerSocket(DATABASE2_PORT))
        {
            while (true)
            {
                try {
                    System.out.println("Socket starting");
                    clientSocket = serverSocket.accept();

                    //ClientThread clientThread = new ClientThread(server, clientSocket);

                    Callable<Void> task = new ClientThread(server, clientSocket);
                    pool.submit(task);

                    //clientThread.start();
                } catch (IOException e) { }
            }

        } catch (IOException e) {
            System.out.println("Couldn't start server " + e);
        }
    }
}


class ClientThread implements Callable<Void>
{
    Socket clientSocket = null;
    Main server;
    BufferedReader inputLine = null;
    PrintWriter out_socket = null;



    public ClientThread(Main server, Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    // @SuppressWarnings("deprecation")
/*    public void run()
    {
        try {

            ///
            inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out_socket = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            ///

            //out_socket.println("2 Phase Commit Starts.");

            while (true)
            {
                System.out.println("Reading Entry from Coordinator");
                String received_msg = inputLine.readLine();

                Database db = Main.dbm.getDatabaseHashMap().get("Sensors Database");
                Table table = db.getTableHashMap().get("Sensors Table");


                String[] parts = received_msg.split("_");
                String RPCType = parts[0];
                String received_ID = parts[1];
                String received_SensorType = parts[2];
                String received_TemperaturValue = parts[3];
                String received_HumidityValue= parts[4];
                String received_FluidValue = parts[5];


                HashMap<String,String> entry = new HashMap<>();
                entry.put("Sensor Type",received_SensorType);
                entry.put("Temperature",received_TemperaturValue);
                entry.put("Humidity",received_HumidityValue);
                entry.put("Fluid",received_FluidValue);

                HashMap<String,String> columnsMap;

                System.out.println("Entry readed");

                if(table.checkEntry(received_ID) == false )
                {
                    out_socket.println("ABORT");
                    System.out.println("Entry aborted");

                }
                else
                {
                    out_socket.println("COMMIT");
                    System.out.println("Entry committed");
                }

                String  finalchoice = inputLine.readLine();

                if (finalchoice.equalsIgnoreCase("ABORTED"))
                {
                    System.out.println("ABORT. Since aborted we will not wait for inputs from other clients.");
                    System.out.println("Aborted....");

                    for (int i = 0; i < (server.thread).size(); i++) {
                        ((server.thread).get(i)).out_socket.println("GLOBAL_ABORT");
                        ((server.thread).get(i)).inputLine.close();
                        ((server.thread).get(i)).inputLine.close();
                    }
                    break;
                }
                if (finalchoice.equalsIgnoreCase("COMMITTED"))
                {
                    System.out.println("COMMITTING...");

                    switch (RPCType) {
                        case "createEntry":
                            table.createEntry(received_ID, entry);
                            table.show();

                            break;
                        case "readEntry":

                            break;
                        case "updateEntry":

                            break;
                        case "deleteEntry":

                            break;
                    }
                } // commit
            } // while
            //server.closed = true;
            //clientSocket.close();
        } catch (IOException e) { }

    }*/

    @Override
    public Void call() throws Exception {
        try {

            ///
            boolean readEntryTrue = false;
            boolean updateEntryTrue = false;
            boolean deleteEntryTrue = false;
            HashMap<String, String> entry = new HashMap<>();
            HashMap<String, String> testentry = new HashMap<>();
            inputLine = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out_socket = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            ///

            //out_socket.println("2 Phase Commit Starts.");

            while (true)
            {
                System.out.println("Reading Entry from Coordinator");
                String received_msg = inputLine.readLine();

                Database db = Main.dbm.getDatabaseHashMap().get("Sensors Database");
                Table table = db.getTableHashMap().get("Sensors Table");

                //TEST für Konsistenz
                /*String testID = "4";
                testentry.put("Sensor Type", "A");
                testentry.put("Temperature", "35");
                testentry.put("Humidity", "60");
                testentry.put("Fluid", "2");
                table.createEntry(testID,testentry);*/


                String[] parts = received_msg.split("_");
                String RPCType = parts[0];
                String received_ID = parts[1];

                if(RPCType.equals("readEntry")){
                    readEntryTrue = true;
                }else if(RPCType.equals("deleteEntry")){
                    deleteEntryTrue = true;
                }else {
                    String received_SensorType = parts[2];
                    String received_TemperaturValue = parts[3];
                    String received_HumidityValue = parts[4];
                    String received_FluidValue = parts[5];

                    entry.put("Sensor Type", received_SensorType);
                    entry.put("Temperature", received_TemperaturValue);
                    entry.put("Humidity", received_HumidityValue);
                    entry.put("Fluid", received_FluidValue);

                    System.out.println("Entry read.");
                }

                if(table.checkEntry(received_ID) == false)
                {
                    if(RPCType.equals("updateEntry") || RPCType.equals("readEntry") ||RPCType.equals("deleteEntry")){
                        updateEntryTrue = true;
                        //table.updateEntry(received_ID,entry);
                        out_socket.println("COMMIT");
                        System.out.println("Entry committed");
                    }else{
                        out_socket.println("ABORT");
                        System.out.println("Entry aborted");
                    }
                }
                else
                {
                    if(RPCType.equals("updateEntry") || RPCType.equals("readEntry") ||RPCType.equals("deleteEntry")){
                        out_socket.println("ABORT");
                        System.out.println("Entry aborted");
                    }else {
                        out_socket.println("COMMIT");
                        System.out.println("Entry committed");
                    }
                }

                String finalchoice = inputLine.readLine();

                if (finalchoice.equalsIgnoreCase("ABORTED"))
                {
                    System.out.println("ABORT. Since aborted we will not wait for inputs from other clients.");
                    System.out.println("Aborted....");

                    for (int i = 0; i < (server.thread).size(); i++) {
                        ((server.thread).get(i)).out_socket.println("GLOBAL_ABORT");
                        ((server.thread).get(i)).inputLine.close();
                        ((server.thread).get(i)).inputLine.close();
                    }
                    break;
                }
                if (finalchoice.equalsIgnoreCase("COMMITTED"))
                {
                    System.out.println("COMMITTING...");

                    if (RPCType.equals("createEntry")) {
                        table.createEntry(received_ID, entry);
                        table.show();
                    } else if(readEntryTrue){
                        HashMap <String,String> read = table.readEntry(received_ID);
                        System.out.println(read);
                        out_socket.println();
                    } else if (updateEntryTrue) {
                        table.updateEntry(received_ID, entry);
                        table.show();
                    } else if (deleteEntryTrue) {
                        table.deleteEntry(received_ID);
                        table.show();
                    }
                }
                // commit
            } // while
            //server.closed = true;
            //clientSocket.close();
        } catch (IOException e) { }
        return null;
    }
}