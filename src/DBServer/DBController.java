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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final int PORT = 9600;
    public static final int PARTITIONS_NUMBER = 3;

    private static final int GET_PARTITIONS_INFO = 0;
    private static final int UPDATE_PARTITIONS_INFO = 1;

    //Partitions
    private static ArrayList<Partition> partitions = new ArrayList<Partition>();

    public DBController() {
        try {
            controller_socker_receive = new ServerSocket(PORT);
            System.out.print(ANSI_BLUE);

            System.out.println("******************** BDSERVER ******************** ");
            insertPartitionsInfo();//Receive partitions info;

            System.out.print(ANSI_RESET);

            System.out.println("*Running...");

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

        //CHECK PARTITIONS INFORMATION AND ADD IN persons ARRAY
        for (int i = 0; i < PARTITIONS_NUMBER; i++) {
            try {
                get = sendPartitionInfo(GET_PARTITIONS_INFO, partitions.get(i).getIp(), partitions.get(i).getPort(), table, persons);
                if (get.getStatus() == 0) {
                    persons = get.getPersons();
                } else {
                    searchPartition3 = !searchPartition3;
                }

            } catch (Exception e) {
                System.err.println("*DBController > Ocorreu um erro ao obter as informações da PARTIÇÃO " + (i + 1));
                searchPartition3 = !searchPartition3;
            }
        }

        //CREATE PERSON FILES TEMPORARY
        fileManager.writePersons(persons, table);
    }

    public static void updateDBPartitions(String table) throws IOException {
        ArrayList<Person> persons = fileManager.getPersons(table);
        DBPartitionCommandGet get;

        for (int i = 0; i < PARTITIONS_NUMBER; i++) {
            try {
                get = sendPartitionInfo(UPDATE_PARTITIONS_INFO, partitions.get(i).getIp(), partitions.get(i).getPort(), table, persons);
            } catch (Exception e) {
                partitions.get(i).setError_partition(true);
                System.err.println("*DBController > Ocorreu um erro ao atualizar a PARTIÇÃO " + (i + 1));
            }
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

    public static void insertPartitionsInfo() {
        String ip;
        int port = 9700;
        Partition partitionToAdd;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n>> ENTER IP OF PARTITIONS");

        for (int i = 0; i < PARTITIONS_NUMBER; i++) {
            //Input IP partition
            System.out.print("Partition" + (i + 1) + "> localhost: ");
            ip = scanner.nextLine();

            if (!ip.isEmpty()) partitionToAdd = new Partition(ip, port);
            else partitionToAdd = new Partition(port);

            //Add in Array Partition info
            partitions.add(partitionToAdd);

            port += 100;
        }

        //Show partitions info
        System.out.println("\n> IPs: ");
        for (int i = 0; i < PARTITIONS_NUMBER; i++) {
            System.out.println("Partition" + (i + 1) + ">" + partitions.get(i).getIp() + ":" + partitions.get(i).getPort());
        }

        System.out.println("\n");
    }

}
