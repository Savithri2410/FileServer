package Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket {
    private static final int PORT = 1988;
    private ServerSocket ss;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final int BUF_SIZE = 1024;

    public MySocket()
    {
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket accept()
    {
        try {
            return ss.accept();
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in accept of class MySocket");
        }
        return null;
    }

    public String getCheckSumFromClient(Socket socket)
    {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            return dis.readUTF();
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in getCheckSumFromClient of class MySocket");
            return null;
        }
    }

    public void sendCheckSumExistsResponse(Socket socket, boolean exists)  {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if (exists)
                dos.writeUTF("1");
            else
                dos.writeUTF("0");
            dos.flush();
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in sendCheckSumExistsResponse of class MySocket");
            e.printStackTrace();
        }
    }

    public void saveFile(Socket clientSock, String fileName, int fileSize) throws IOException{

        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileName);

        byte[] buffer = new byte[BUF_SIZE];
        int read;
        int totalRead = 0;
        int remaining = fileSize;
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }
        System.out.println("Receiving file");
        fos.flush();
        fos.close();
        dis.close();
    }

    public void sendListDirectoryContents(Process p, Socket socket){
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            StringBuilder strbuilder = new StringBuilder();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String str = stdInput.readLine();
            while(str!=null)
            {
                strbuilder.append(System.getProperty("line.separator"));
                strbuilder.append(str);
                str = stdInput.readLine();
            }

            dos.writeUTF(strbuilder.toString());
            dos.flush();
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in sendListDirectoryContents of class MySocket");
        }
    }

    public void sendWordCountContents(Process p , Socket socket){
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            StringBuilder strbuilder = new StringBuilder();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String str = stdInput.readLine();
            while(str!=null)
            {
                strbuilder.append(System.getProperty("line.separator"));
                strbuilder.append(str);
                str = stdInput.readLine();
            }

            dos.writeUTF(strbuilder.toString());
            dos.flush();
        }
        catch(Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception in sendWordCountContents of class MySocket");
        }
    }
}
