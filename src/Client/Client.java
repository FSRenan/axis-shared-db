//Cliente já envia o comando ao servidor e retorna o objeto get
package Client;

import Connection.Connect;
import Connection.Get;
import Connection.Post;
import Connection.Where;

import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author rferreira
 */
public class Client {

    static Socket socket;

    //CONSTRAINTS
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final String COMMAND_SELECT = "select";
    private static final String COMMAND_INSERT = "insert";
    private static final String COMMAND_UPDATE = "update";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_INFO = "info";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_DEFAULT = "default";

    public static void main(String[] args) throws IOException {
        String arrayAux[];
        Get get;
        Post post = new Post();
        Scanner scanner;
        boolean clientEnable = true;
        String command = "";


        //Set BLUE color to println
        System.out.print(ANSI_BLUE);
        System.out.println("******************** BEM-VINDO AO AXIS-SHARED-DB ******************** ");

        //Set RESET color to println
        System.out.print(ANSI_RESET);

        while (clientEnable) {
            Where where = null;
            scanner = new Scanner(System.in);

            System.out.println("DIGITE A QUERY E PRESSIONE ENTER: <info>");
            command = scanner.next();

            switch (command) {
                //Select command
                case COMMAND_SELECT:
                    System.out.print(">select > Deseja utilizar WHERE? <S/N>");
                    command = scanner.next();

                    if (command.equalsIgnoreCase("S")) {
                        //Receive WHERE
                        System.out.print(">select > Digite o WHERE: (Exemplo <Idade.txt=12>)");
                        command = scanner.next();

                        where = new Where(command);
                    }
                    post.select("Person", where);
                    break;
                //Insert command
                case COMMAND_INSERT:
                    System.out.print(">insert > Digite o insert: (Exemplo <idade, cpf, nome>)");
                    command = scanner.next();
                    arrayAux = command.split(",");
                    post.insert("Person", new ArrayList(Arrays.asList(arrayAux[0], arrayAux[1], arrayAux[2])));
                    break;
                //Update command
                case COMMAND_UPDATE:
                    //Receive WHERE
                    System.out.print(">update > Digite o WHERE: (Exemplo <Idade.txt=12>)");
                    command = scanner.next();
                    where = new Where(command);

                    //Receive fields to update
                    System.out.print(">update > Digite o valor dos novos campos: (Exemplo <idade, cpf, nome>)");
                    command = scanner.next();
                    arrayAux = command.split(",");

                    post.update("Person", new ArrayList(Arrays.asList(arrayAux[0], arrayAux[1], arrayAux[2])), where);
                    break;
                //Delete command
                case COMMAND_DELETE:
                    System.out.print("> delete > Esse comando vai apagar todas as informações já salvas! Deseja continuar? <S/N>");
                    command = scanner.next();

                    if (command.equalsIgnoreCase("S"))
                        post.delete("Person", null);
                    else
                        command = COMMAND_DEFAULT;
                    break;
                case COMMAND_INFO:
                    System.err.println("COMANDOS PERMITIDOS: <select>|<insert>|<update>|<delete> \n <stop> PARA PARAR A EXECUÇÃO");
                    command = COMMAND_DEFAULT;
                    break;
                case COMMAND_STOP:
                    clientEnable = false;
                    command = COMMAND_DEFAULT;
                    break;
                default:
                    System.err.println("COMANDO INVÁLIDO! O COMANDO <" + command + "> não foi reconhecido!");
                    command = COMMAND_DEFAULT;
                    break;
            }

            if (!command.equalsIgnoreCase(COMMAND_DEFAULT)) {
                socket = new Socket("localhost", 9600);
                //Sending commands
                Connect.send(socket, post);
                //Get BDServer return
                get = (Get) Connect.receive(socket);
                System.out.println("*Cliente* Retorno server: \n" + get.getMsg());

                socket.close();
            }

        }
    }
}
