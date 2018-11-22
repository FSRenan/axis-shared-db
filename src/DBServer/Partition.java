package DBServer;

import java.io.*;

public class Partition {
    private String ip = "localhost";
    private int port;
    private final String LOG_FILE = "particaoComErro.txt";

    public Partition(int port) {
        this.port = port;
    }

    public Partition(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isError_partition() {
        return isError_LogFile();
    }

    public void setError_partition(boolean error_partition) {
        if (error_partition)
            updateLogFile();
        else
            deleteLogFile();
    }

    //Get log file info
    private boolean isError_LogFile() {
        try {
            if (new File(LOG_FILE).exists()) {
                String line;
                String[] ipAdress;
                BufferedReader br = new BufferedReader(new FileReader(LOG_FILE));

                if ((line = br.readLine()) != null) {
                    br.close();
                    ipAdress = line.split(":");
                    return ipAdress[0].equals(ip) && Integer.parseInt(ipAdress[1]) == port;
                }
            }
        } catch (IOException e) {
            System.err.println("*Partition > Ocorreu um erro ao ler o LOG");
        }
        return false;
    }

    //Update log file
    private void updateLogFile() {
        try {
            FileWriter fw = new FileWriter(LOG_FILE);
            fw.write(ip + ":" + port);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println("*Partition > Ocorreu um erro ao atualizar o LOG");
        }
    }

    //Delete log file
    private void deleteLogFile() {
        File file = new File(LOG_FILE);
        file.delete();
    }
}