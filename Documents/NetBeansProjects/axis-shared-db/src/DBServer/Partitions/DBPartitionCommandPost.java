/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBServer.Partitions;

import FileManager.Person;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Renan Ferreira
 */
public class DBPartitionCommandPost implements Serializable {

    //0-Solicita as informacoes
    //1-Atualiza informacoes
    private ArrayList<Person> persons;
    private int partitionRequest;
    private String table;

    public DBPartitionCommandPost(int partitionRequest, String table, ArrayList<Person> persons) {
        this.partitionRequest = partitionRequest;
        this.table = table;
        this.persons = persons;
    }

    public int getPartitionRequest() {
        return partitionRequest;
    }

    public void setPartitionRequest(int partitionRequest) {
        this.partitionRequest = partitionRequest;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}
