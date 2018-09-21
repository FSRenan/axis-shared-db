package Conection;

import java.net.*;
import java.io.*;

/**
 *
 * @author rferreira
 */
public class Conect {

    public static void send(Socket socket, Object object) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(object);

        } catch (IOException ex) {
            System.out.println("*Conect: send method failed >> ERROR: " + ex);
        }
    }

    public static Object receive(Socket socket) {

        Object object = null;

        try {
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            object = is.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("*Conect: receive method failed >> ERROR: " + ex);
        }
        return object;
    }
}
