package DBServer.CRUD;

import Connection.Get;
import Connection.Post;
import FileManager.FileManager;
import FileManager.Person;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;


/**
 *
 * @author rferreira
 */
public class DBActions implements Serializable {

    //Constraints
    private final int const_Insert = 0;
    private final int const_Select = 1;
    private final int const_Update = 2;
    private final int const_Delete = 3;
    private final FileManager fileManager = new FileManager();

    public void execute(Post post, Get get) {

        switch (post.getCommand()) {
            case const_Insert:
                System.out.println("entrou insert");
                insert(post, get);
                break;
            case const_Select:
                select(post, get);
                break;
            case const_Update:
                update(post, get);
                break;
            case const_Delete:
                delete(post, get);
                break;
        }
    }

    //Insert Age, cpf, name
    public void insert(Post post, Get get) {
        //Tabelas sao pastas, colunas sao arquivos, backp sera copiar arquivo, na hr
        //de recuperar ele copia
        fileManager.writeInsert(post.getTable(), post.getValues(), true);
        get.setMsg("\n **SUCESSO NO INSERT");
    }

    //Select
    public void select(Post post, Get get) {
        String select = "";
        ArrayList<Person> persons = fileManager.getPersons(post.getTable());

        for (Person person : persons) {
            select += "*******************";
            select += person.getAllInfo();
        }
        get.setMsg(select);

    }

    public void update(Post post, Get get) {

        ArrayList<Person> persons = fileManager.updatePerson(post.getTable(), post.getValues(), post.getWhere());
        fileManager.writePersons(persons, post.getTable());
        
        get.setMsg("\n **SUCESSO NO UPDATE");
    }

    public void delete(Post post, Get get) {
        fileManager.cleanFiles(post.getTable());
        get.setMsg("\n **SUCESSO NO DELETE");
    }

    public int getConst_Insert() {
        return const_Insert;
    }

    public int getConst_Select() {
        return const_Select;
    }

    public int getConst_Update() {
        return const_Update;
    }

    public int getConst_Delete() {
        return const_Delete;
    }

}