import Util.Command;
import Util.MySocket;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServer implements Runnable {

    MySocket mySocket;
    Socket socket;
    static Map<String, String> checkSumTable = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public FileServer() {
        mySocket = new MySocket();
    }

    public void run()
    {
        while (true) {
            try {
                socket = mySocket.accept();
                Command command = new Command(socket, getMetaDataArrFromClient());

                switch(command.getCommand()) {
                    case "add": {
                        command.addFile(mySocket, getCheckSumAndCheckIfItExists(command));
                        break;
                    }
                    case "rm":{
                        command.removeFile(getFileNameAndCheckIfItExists(command));
                        break;
                    }
                    case "ls":
                    {
                        command.listDir(mySocket);
                        break;
                    }
                    case "wc":
                    {
                        command.wordCount(mySocket);
                        break;
                    }
                    case "freq-words":
                    {
                        command.freqWords(mySocket);
                        break;
                    }
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,"Exception in run of class FileServer");
                e.printStackTrace();
            }
        }
    }


    private String[] getMetaDataArrFromClient()
    {
        String checksumPlusMetaData =  mySocket.getCheckSumFromClient(socket);
        return checksumPlusMetaData.split(":", 4);
    }

    private Boolean getCheckSumAndCheckIfItExists(Command command)
    {
        try {
            String checksum = command.getChecksum();
            File f = command.getFile();
            int size = command.getFileSize();
            if (checkSumTable.containsKey(checksum) && checkSumTable.get(checksum).equals(f.getName()))
                return true;
            else
                checkSumTable.put(checksum, f.getName());

        }catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in getCheckSumAndCheckIfItExists of class FileServer");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private Boolean getFileNameAndCheckIfItExists(Command command)
    {
        try {
            String fileName = command.getChecksum();
            for (Map.Entry<String, String> entry : checkSumTable.entrySet()) {
                if (fileName.equals(entry.getValue())) {
                    checkSumTable.remove(entry.getKey());
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in getFileNameAndCheckIfItExists of class FileServer");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args)
    {
        MyExecutor myExecutor= new MyExecutor(new FileServer());
    }

}
