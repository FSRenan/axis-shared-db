package Cliente;

import java.net.Socket;
import Conection.*;
import java.io.IOException;

/**
 *
 * @author rferreira
 */
public class Client {

    static Socket socket;

    public Client() {
        try {
            //Conection initialize
            socket = new Socket("localhost", 9600);
        } catch (IOException ex) {
            System.out.println("*Client: Socket initialize failed >> ERROR: " + ex);
        }
    }

    public static void main(String[] args) {
        //Initialize Client Socket
        new Client();
        
        Get get;
        Post post = new Post();

        //Sending commands
        Conect.send(socket, post);

        //Get BDServer return
        get = (Get) Conect.receive(socket);
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("*Client: Socket close failed >> ERROR: " + ex);
        }
    }

}
