import java.io.IOException;

public class Client implements Runnable {

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        String responseLine;
        try
        {
            while ((responseLine = ThriftServer.is.readLine()) != null)
            {
                System.out.println("\n"+responseLine);
                if (responseLine.equalsIgnoreCase("GLOBAL_COMMIT")==true || responseLine.equalsIgnoreCase("GLOBAL_ABORT")==true )
                {
                    break;
                }
            }
            ThriftServer.closed=true;
        }
        catch (IOException e)
        {
            System.err.println("IOException:  " + e);
        }
    }
}
