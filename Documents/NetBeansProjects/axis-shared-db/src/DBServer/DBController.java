package DBServer;

import Connection.Connect;
import Connection.Get;
import Connection.Post;
import DBServer.CRUD.DBActions;
import java.io.*;
import java.net.*;

/**
 *
 * @author rferreira
 */
public class DBController {

    static ServerSocket server_socket;
    static Socket client_socket;

    public DBController() {
        try {
            server_socket = new ServerSocket(9600);
            System.out.println("BDServer Created");
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
        while (connect()) {

            //Receive Client request
            post = (Post) Connect.receive(client_socket);
            
            //Execute Client request
            dbActions.execute(post, get);
            
            //Setting Return
            Connect.send(client_socket, get);

//            try {
//                client_socket.close();
//                server_socket.close();
//            } catch (IOException ex) {
//                System.out.println("*BDServer: Socket close failed >> ERROR: " + ex);
//            }
        }
    }

    static boolean connect() {
        boolean ret;
        try {
            client_socket = server_socket.accept();              // fase de conexão
            ret = true;
        } catch (IOException ex) {
            System.out.println("*BDServer: Connection failed >> ERROR: " + ex);
            ret = false;
        }
        return ret;
    }

}
