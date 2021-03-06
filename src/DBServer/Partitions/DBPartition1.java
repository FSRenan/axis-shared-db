package DBServer.Partitions;

import Connection.Connect;
import FileManager.Person;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author rferreira
 */
public class DBPartition1 {

    static ServerSocket server_socker_;
    static Socket client_socket;

    //CONSTRAINTS
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final int PORT = 9700;

    //Request status
    private static final int getPartitionsInfo = 0;
    private static final int updatePartitionsInfo = 1;

    //Partition number
    private static final int partition = 1;

    private static DBPartitionActions partitionActions = new DBPartitionActions(partition);

    public DBPartition1() {
        try {
            server_socker_ = new ServerSocket(PORT);
            System.out.print(ANSI_BLUE);
            System.out.println("******************** DBPARTITION1 ******************** ");
            System.out.print(ANSI_RESET);
            System.out.println("\n\n*Running...");
        } catch (IOException ex) {
            System.err.println("*DBPartition1: Creating server failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        //Initialize DBServer Socket
        new DBPartition1();

        DBPartitionCommandPost post;
        DBPartitionCommandGet get = new DBPartitionCommandGet();
        ArrayList<Person> persons;

        while (connect()) {
            //Receive Client request
            post = (DBPartitionCommandPost) Connect.receive(client_socket);

            if (partitionActions.checkFileNames(post.getTable())) {
                //Get partition info
                if (post.getPartitionRequest() == getPartitionsInfo) {
                    persons = partitionActions.getPersonsInfo(post.getTable(), partition, post.getPersons());
                    get.setPersons(persons);

                    if (persons.isEmpty()) {
                        get.setStatus(1);
                    } else {
                        get.setStatus(0);
                    }
                }
                //Update partition info
                else if (post.getPartitionRequest() == updatePartitionsInfo) {
                    persons = post.getPersons();
                    partitionActions.writePersons(persons, post.getTable(), partition);
                }
            } else {
                get.setStatus(1);
            }

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
