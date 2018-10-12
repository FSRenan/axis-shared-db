package DBServer.Partitions;

import Connection.Connect;
import Connection.Post;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author rferreira
 */
public class DBPartitiion1 {

    static ServerSocket server_socker_;
    static Socket client_socket;

    public DBPartitiion1() {
        try {
            server_socker_ = new ServerSocket(9700);
            System.out.println("DBPartition1 Created");

        } catch (IOException ex) {
            System.out.println("*DBPartition1: Creating server failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        //Initialize DBServer Socket
        new DBPartitiion1();

        DBPartitionCommandPost post;
        DBPartitionCommandGet get = new DBPartitionCommandGet();

        while (connect()) {

            //Receive Client request
            post = (DBPartitionCommandPost) Connect.receive(client_socket);

            
            get.setStatus(9999);
            //Setting Return
            Connect.send(client_socket, get);

        }

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
