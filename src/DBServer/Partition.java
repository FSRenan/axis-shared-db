package DBServer;

public class Partition {
    private String ip = "localhost";
    private int port;
    private boolean error_partition;

    public Partition(int port){
        this.port = port;
    }

    public Partition(String ip, int port){
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
        return error_partition;
    }

    public void setError_partition(boolean error_partition) {
        this.error_partition = error_partition;
    }
}