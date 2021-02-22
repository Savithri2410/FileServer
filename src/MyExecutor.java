import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyExecutor {
    ExecutorService executorService;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    FileServer myFileServer;

    public MyExecutor(FileServer fs)
    {
        myFileServer = fs;
        init(fs);
    }

    private void init(FileServer fs)
    {
        executorService = Executors.newFixedThreadPool(2);
        try {
            executorService.submit(fs);
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in init of class MyExecutor");
            e.printStackTrace();
        }
        finally {
            shutdown();
        }
    }
    private void shutdown()
    {
        executorService.shutdown();
    }
}
