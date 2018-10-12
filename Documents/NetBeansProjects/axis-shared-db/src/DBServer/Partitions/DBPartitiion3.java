package DBServer.Partitions;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author rferreira
 */
public class DBPartitiion3 {

    static ServerSocket server_socker_;
    static Socket client_socket;

    public DBPartitiion3() {
        try {
            server_socker_ = new ServerSocket(9900);
            System.out.println("DBPartition3 Created");

        } catch (IOException ex) {
            System.out.println("*DBServer: Creating server failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        //Initialize DBServer Socket
        new DBPartitiion3();

    }

    static boolean connect() {
        boolean ret;
        try {
            client_socket = server_socker_.accept();              // fase de conexão
            ret = true;
        } catch (IOException ex) {
            System.out.println("*BDServer: Connection failed >> ERROR: " + ex);
            ret = false;
        }
        return ret;
    }

}
