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
public class DBPartition3 {

    static ServerSocket server_socker_;
    static Socket client_socket;

    //Request status
    private static final int getPartitionsInfo = 0;
    private static final int updatePartitionsInfo = 1;

    //Partition number
    private static final int partition = 3;

    private static DBPartitionActions partitionActions = new DBPartitionActions(partition);

    public DBPartition3() {
        try {
            server_socker_ = new ServerSocket(9900);
            System.out.println("DBPartition3 Created");

        } catch (IOException ex) {
            System.err.println("*DBPartition3: Creating server failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        //Initialize DBServer Socket
        new DBPartition3();

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
