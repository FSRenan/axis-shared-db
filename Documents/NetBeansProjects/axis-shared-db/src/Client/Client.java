//Cliente já envia o comando ao servidor e retorna o objeto get
package Client;

import Connection.Connect;
import Connection.Get;
import Connection.Post;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

        // post.select("Cliente", new ArrayList(Arrays.asList("luma")), "");
        //post.insert("Person", new ArrayList(Arrays.asList("21", "799898898", "Renan")));
        
        post.select("Person", new ArrayList(Arrays.asList("21", "3789", "48787teste")), "");
        //Sending commands
        Connect.send(socket, post);

        //Get BDServer return
        get = (Get) Connect.receive(socket);
        
        System.out.println("*Cliente* Retorno server: \n" + get.getMsg());

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("*Client: Socket close failed >> ERROR: " + ex);
        }
    }

}
