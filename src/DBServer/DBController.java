package DBServer;

import Connection.Connect;
import Connection.Get;
import Connection.Post;
import DBServer.CRUD.DBActions;
import DBServer.Partitions.DBPartitionActions;
import DBServer.Partitions.DBPartitionCommandGet;
import DBServer.Partitions.DBPartitionCommandPost;
import FileManager.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author rferreira
 */
public class DBController {

    static ServerSocket controller_socker_receive;
    static Socket socker_send_partition;
    static Socket client_socket;

    private static final FileManager fileManager = new FileManager();

    //Constraints
    private static final int GET_PARTITIONS_INFO = 0;
    private static final int UPDATE_PARTITIONS_INFO = 1;

    private static final int PORT_PARTITION1 = 9700;
    private static final int PORT_PARTITION2 = 9800;
    private static final int PORT_PARTITION3 = 9900;

    public DBController() {
        try {
            controller_socker_receive = new ServerSocket(9600);
            System.out.println("BDServer Created");

            //Conection to send info
            //socker_send_partition1 = new Socket("localhost", 9700);
            //PARTICOES NOVA
            //socker_send_partition2 = new Socket("localhost", 9800);
            //socker_send_partition3 = new Socket("localhost", 9900);
        } catch (IOException ex) {
            System.err.println("*DBServer: Creating server failed >> ERROR: " + ex);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        DBActions dbActions = new DBActions();

        //Initialize DBServer Socket
        new DBController();

        Post post;
        Get get = new Get();

        //checkDBPartitions();
        while (connect()) {
            //Receive Client request
            post = (Post) Connect.receive(client_socket);

            //Create files from partitions
            checkDBPartitions(post.getTable());

            //Execute Client request
            dbActions.execute(post, get);

            //Update Partitions
            updateDBPartitions(post.getTable());    
            
            //Delete Controller Files Buffer
            
            //Setting Return
            Connect.send(client_socket, get);

        }
    }

    //Conexao com cliente
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

    public static void checkDBPartitions(String table) throws IOException {
        ArrayList<Person> persons = new ArrayList();
        DBPartitionCommandGet get;
        boolean searchPartition3 = false;

        //PARTITION 1
        get = sendPartitionInfo(GET_PARTITIONS_INFO, PORT_PARTITION1, table, persons);
        if (get.getStatus() == 0) {
            persons = get.getPersons();
        } else {
            searchPartition3 = true;
        }

        //PARTITION 2
        get = sendPartitionInfo(GET_PARTITIONS_INFO, PORT_PARTITION2, table, persons);
        if (get.getStatus() == 0) {
            persons = get.getPersons();
        } else {
            searchPartition3 = true;
        }

        //PARTITION 3
        if (searchPartition3) {
            get = sendPartitionInfo(GET_PARTITIONS_INFO, PORT_PARTITION3, table, persons);
            persons = get.getPersons();
        }

        //CREATE PERSON FILES TEMPORARY
        fileManager.writePersons(persons, table);
    }

    public static void updateDBPartitions(String table) throws IOException {
        ArrayList<Person> persons = fileManager.getPersons(table);
        DBPartitionCommandGet get;

        //PARTITION 1
        get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, PORT_PARTITION1, table, persons);
        //PARTITION 2
        get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, PORT_PARTITION2, table, persons);
        //PARTITION 3
        get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, PORT_PARTITION3, table, persons);
        
        
    }

    public static DBPartitionCommandGet sendPartitionInfo(int command, int port, String table, ArrayList<Person> persons) throws IOException {
        DBPartitionCommandPost postPartition;
        DBPartitionCommandGet get;

        socker_send_partition = new Socket("localhost", port);
        postPartition = new DBPartitionCommandPost(command, table, persons);
        Connect.send(socker_send_partition, postPartition);
        get = (DBPartitionCommandGet) Connect.receive(socker_send_partition);

        //Verificar se n da pau****************
        socker_send_partition.close();
        
        return get;
    }

}
