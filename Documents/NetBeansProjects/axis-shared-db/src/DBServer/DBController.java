package DBServer;

import Connection.Connect;
import Connection.Get;
import Connection.Post;
import DBServer.CRUD.DBActions;
import DBServer.Partitions.DBPartitionCommandGet;
import DBServer.Partitions.DBPartitionCommandPost;
import FileManager.FileManager;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rferreira
 */
public class DBController {

    static ServerSocket controller_socker_receive;
    static Socket socker_send_partition1;
    static Socket socker_send_partition2;
    static Socket socker_send_partition3;
    static Socket client_socket;

    public DBController() {
        try {
            controller_socker_receive = new ServerSocket(9600);
            System.out.println("BDServer Created");

            //Conection to send info
            socker_send_partition1 = new Socket("localhost", 9700);
            socker_send_partition2 = new Socket("localhost", 9800);
            socker_send_partition3 = new Socket("localhost", 9900);

        } catch (IOException ex) {
            System.out.println("*DBServer: Creating server failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        DBActions dbActions = new DBActions();

        //Initialize DBServer Socket
        new DBController();

        Post post;
        Get get = new Get();
        checkDBPartitions();

        while (connect()) {
            //Receive Client request
            post = (Post) Connect.receive(client_socket);

            //Create files from partitions
            checkDBPartitions();
            
            //Execute Client request
            dbActions.execute(post, get);

            //Update DBPartitions and clean local files
            updateDBPartitions();

            //Setting Return
            Connect.send(client_socket, get);

//            try {
//                client_socket.close();
//                controller_socker_receive.close();
//            } catch (IOException ex) {
//                System.out.println("*BDServer: Socket close failed >> ERROR: " + ex);
//            }
        }
    }

    static boolean connect() {
        boolean ret;
        try {
            client_socket = controller_socker_receive.accept();              // fase de conexão
            ret = true;
        } catch (IOException ex) {
            System.out.println("*BDServer: Connection failed >> ERROR: " + ex);
            ret = false;
        }
        return ret;
    }

    public static void checkDBPartitions() {
        FileManager fileManager = new FileManager();
        DBPartitionCommandGet get;
        ArrayList<String> files;

        files = new ArrayList(Arrays.asList(fileManager.getColumnAge(), fileManager.getColumnCpf()));

        //Execute partition 1 request
        Connect.send(socker_send_partition1, new DBPartitionCommandPost(files));

        //Get partition 1  return
        get = (DBPartitionCommandGet) Connect.receive(socker_send_partition1);

        System.out.println("*****STATUS DBPART1: " + get.getStatus());

    }

    public static void updateDBPartitions() {

    }

}
