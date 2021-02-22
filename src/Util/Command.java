package Util;

import java.io.File;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Command {
    public Socket socket;
    public String[] arr;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String WORDCOUNT = "/Users/sriramns/IdeaProjects/RedHat/FileServer/wordcount.sh";
    private static final String FREQWORDS = "/Users/sriramns/IdeaProjects/RedHat/FileServer/freqWords.sh";

    public Command(Socket socket, String[] arr) {
        this.socket = socket;
        this.arr = arr;
    }

    public String getCommand()
    {
        return arr[0].toLowerCase(Locale.ROOT);
    }

    public String getChecksum()
    {
        return arr[1];
    }

    public File getFile()
    {
        return new File(arr[2]);
    }

    public int getFileSize()
    {
        return Integer.parseInt(arr[3]);
    }

    public void addFile(MySocket mySocket, boolean checkSumExists)
    {
        try {
            //only if file does not exist
            if (!checkSumExists) {
                LOGGER.log(Level.INFO,"File does not exist. Hence adding it");
                mySocket.sendCheckSumExistsResponse(socket, false);
                mySocket.saveFile(socket, (getFile()).getName() , getFileSize());
            } else {
                LOGGER.log(Level.INFO,"File already exists");
                mySocket.sendCheckSumExistsResponse(socket, true);
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Exception in addFile of class Command");
            e.printStackTrace();
        }
    }

    public void removeFile( boolean fileNameExists)
    {
        try {
            //remove if file exists
            if (fileNameExists) {
                Runtime.getRuntime().exec("rm " + getChecksum());
                LOGGER.log(Level.INFO,"File is removed");
            } else
                LOGGER.log(Level.INFO,"File does not exist , hence can't be removed");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Exception in removeFile of class Command");
            e.printStackTrace();
        }
    }

    public void listDir(MySocket mySocket)
    {
        try {
            mySocket.sendListDirectoryContents(Runtime.getRuntime().exec("ls"), socket);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Exception in listDir of class Command");
            e.printStackTrace();
        }
    }
    public void wordCount(MySocket mySocket)
    {
        try {
            mySocket.sendWordCountContents(Runtime.getRuntime().exec(new String[]{WORDCOUNT}), socket);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Exception in wordCount of class Command");
            e.printStackTrace();
        }
    }
    public void freqWords(MySocket mySocket)
    {
        try {
            mySocket.sendWordCountContents(Runtime.getRuntime().exec(new String[]{FREQWORDS}), socket);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Exception in freqWords of class Command");
            e.printStackTrace();
        }
    }
}
