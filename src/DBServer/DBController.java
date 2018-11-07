package DBServer;

import static Client.Client.ANSI_BLUE;
import static Client.Client.ANSI_RESET;
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
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * @author rferreira
 */
public class DBController {

    static ServerSocket controller_socker_receive;
    static Socket socker_send_partition;
    static Socket client_socket;

    private static final FileManager fileManager = new FileManager();

    //CONSTRAINTS
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final int GET_PARTITIONS_INFO = 0;
    private static final int UPDATE_PARTITIONS_INFO = 1;

    private static boolean inputIPs = false;
    private static String ip_partition1 = "localhost";
    private static String ip_partition2 = "localhost";
    private static String ip_partition3 = "localhost";
    private static int port_partition1 = 9700;
    private static int port_partition2 = 9800;
    private static int port_partition3 = 9900;

    public DBController() {
        try {
            controller_socker_receive = new ServerSocket(9600);
            //Set BLUE color to println
            System.out.print(ANSI_BLUE);
            System.out.println("******************** BDSERVER ******************** ");
            insertPartitionsIP();
            //Set RESET color to println
            System.out.print(ANSI_RESET);
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

        while (connect()) {
            //Receive Client request
            post = (Post) Connect.receive(client_socket);

            //Create temporary folder
            fileManager.createDataFolder(post.getTable());

            //Create files from partitions
            checkDBPartitions(post.getTable());

            //Execute Client request
            dbActions.execute(post, get);

            //Update Partitions
            updateDBPartitions(post.getTable());

            //Delete Controller Files Buffer
            fileManager.deleteDataFolder(post.getTable());

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
        try {
            get = sendPartitionInfo(GET_PARTITIONS_INFO, ip_partition1, port_partition1, table, persons);
            if (get.getStatus() == 0) {
                persons = get.getPersons();
            } else {
                searchPartition3 = !searchPartition3;
            }

        } catch (Exception e) {
            System.out.println("*DBController > Ocorreu um erro ao obter as informações da PARTIÇÃO 1");
            e.printStackTrace();
            searchPartition3 = !searchPartition3;
        }

        //PARTITION 2
        try {
            get = sendPartitionInfo(GET_PARTITIONS_INFO, ip_partition2, port_partition2, table, persons);
            if (get.getStatus() == 0) {
                persons = get.getPersons();
            } else {
                searchPartition3 = !searchPartition3;
            }
        } catch (Exception e) {
            System.out.println("*DBController > Ocorreu um erro ao obter as informações da PARTIÇÃO 2");
            e.printStackTrace();
            searchPartition3 = !searchPartition3;
        }

        //PARTITION 3
        if (searchPartition3) {
            get = sendPartitionInfo(GET_PARTITIONS_INFO, ip_partition3, port_partition3, table, persons);
            persons = get.getPersons();
        }

        //CREATE PERSON FILES TEMPORARY
        fileManager.writePersons(persons, table);
    }

    public static void updateDBPartitions(String table) throws IOException {
        ArrayList<Person> persons = fileManager.getPersons(table);
        DBPartitionCommandGet get;

        //PARTITION 1
        try {
            get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, ip_partition1, port_partition1, table, persons);
        } catch (Exception e) {
            System.out.println("*DBController > Ocorreu um erro ao atualizar a PARTIÇÃO 1");
            e.printStackTrace();
        }
        //PARTITION 2
        try {
            get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, ip_partition2, port_partition2, table, persons);
        } catch (Exception e) {
            System.out.println("*DBController > Ocorreu um erro ao atualizar a PARTIÇÃO 2");
            e.printStackTrace();
        }
        //PARTITION 3
        try {
            get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, ip_partition3, port_partition3, table, persons);
        } catch (Exception e) {
            System.out.println("*DBController > Ocorreu um erro ao atualizar a PARTIÇÃO 3");
            e.printStackTrace();
        }
    }

    public static DBPartitionCommandGet sendPartitionInfo(int command, String ip, int port, String table, ArrayList<Person> persons) throws IOException {
        DBPartitionCommandPost postPartition;
        DBPartitionCommandGet get;

        socker_send_partition = new Socket(ip, port);

        postPartition = new DBPartitionCommandPost(command, table, persons);
        Connect.send(socker_send_partition, postPartition);
        get = (DBPartitionCommandGet) Connect.receive(socker_send_partition);

        socker_send_partition.close();

        return get;
    }

    public static void insertPartitionsIP() {
        String ip;
        Scanner scanner = new Scanner(System.in);

        System.out.println(">> ENTER IP OF PARTITIONS");

        System.out.print("Partition1> localhost: ");
        ip = scanner.nextLine();
        if (!ip.isEmpty()) {
            ip_partition1 = ip;
        }

        System.out.print("Partition2> localhost: ");
        ip = scanner.nextLine();
        if (!ip.isEmpty()) {
            ip_partition2 = ip;
        }

        System.out.print("Partition3> localhost: ");
        ip = scanner.nextLine();
        if (!ip.isEmpty()) {
            ip_partition3 = ip;
        }

        System.out.println(
                "> IPs: "
                + "\nPartition1> " + ip_partition1 + ":" + port_partition1
                + "\nPartition2> " + ip_partition2 + ":" + port_partition2
                + "\nPartition3> " + ip_partition3 + ":" + port_partition3);
    }

}
