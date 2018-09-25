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
public class DBServer {

    static ServerSocket server_socket;
    static Socket client_socket;

    public DBServer() {
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
        new DBServer();

        Post post;
        Get get = new Get();
        while (connect()) {

            //Receive Cliente request
            post = (Post) Connect.receive(client_socket);
            System.out.println("exe");
            dbActions.execute(post, get);
            System.out.println("passou");
            //Setting Return
            get.setStatus(0);
            get.setMsg("Server value: " + post.getValues().get(0) + " Table: " + post.getTable() + " MSG: " + get.getMsg());

            Connect.send(client_socket, get);
            System.out.println("BDSERVER RECEIVED: values(" + post.getValues().get(0) + ")");

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
