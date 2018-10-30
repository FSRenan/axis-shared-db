package DBServer.Partitions;

import Connection.*;
import FileManager.Person;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author rferreira
 */
public class DBPartitionCommandGet implements Serializable {

    private ArrayList<Person> persons;
    private String msg;
    private int status;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    /*
     *0-Operacao bem sucedida
     *1-Erro
     *
     */
}
