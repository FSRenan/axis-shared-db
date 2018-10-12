/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManager;

import Connection.Where;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author rferreira
 */


public class FileManager implements Serializable {

    private FileInfo fileInfo = new FileInfo();
    private final String columnAge = "age.txt";
    private final String columnCpf = "cpf.txt";
    private final String columnName = "name.txt";

    //Update values
    public ArrayList<Person> updatePerson(String table, ArrayList<String> values, Where where) {
        ArrayList<Person> persons = getPersons(table);
        switch (where.getColumn()) {
            case columnAge:
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getAge() == Integer.parseInt(where.getValue())) {
                        persons.set(i, new Person(Integer.parseInt(values.get(0)), values.get(1), values.get(2)));
                    }
                }
                break;
            case columnCpf:
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getCpf().equals(where.getValue())) {
                        persons.set(i, new Person(Integer.parseInt(values.get(0)), values.get(1), values.get(2)));
                    }
                }
                break;
            case columnName:
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getName().equals(where.getValue())) {
                        persons.set(i, new Person(Integer.parseInt(values.get(0)), values.get(1), values.get(2)));
                    }
                }
                break;
        }
        return persons;

    }

    //Values for insert
    public void writeInsert(String table, ArrayList<String> values, boolean concatFileInfo) {
        try {
            List<String> fileNames = fileInfo.getFileNames(table);
            for (int i = 0; i < fileNames.size(); i++) {
                FileWriter fw = new FileWriter(fileInfo.getFilePath(table, fileNames.get(i)), concatFileInfo);

                fw.append(values.get(i) + "" + "\r\n");
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            System.out.println("*FileManager: writeInsert failed >> ERROR: " + ex);
        }
    }

    public void writePersons(ArrayList<Person> persons, String table) {

        boolean firstPersonInserted = false;
        //Só grava o ultimo, o false deve estar no primeiro
        for (Person person : persons) {
            writeInsert(table, new ArrayList(Arrays.asList(person.getAge() + "", person.getCpf(), person.getName())), firstPersonInserted);
            firstPersonInserted = true;
        }
    }

    //Return all the persons in the table Person
    public ArrayList<Person> getPersons(String table) {
        List<String> fileNames = fileInfo.getFileNames(table);
        ArrayList<Person> persons = new ArrayList();

        for (int i = 0; i < fileNames.size(); i++) {
            try {
                FileReader fileReader = new FileReader(fileInfo.getFilePath(table, fileNames.get(i)));
                BufferedReader readFile = new BufferedReader(fileReader);

                addPersonInfoToArray(persons, readFile, fileNames.get(i));

                fileReader.close();
            } catch (IOException ex) {
                System.out.println("*FileManager: readSelect failed >> ERROR: " + ex);
            }
        }
        return persons;
    }

    //Add person info in array
    public void addPersonInfoToArray(ArrayList<Person> persons, BufferedReader readFile, String fileName) {
        int indexPerson = 0;

        try {
            String line = readFile.readLine();

            while (line != null) {
                switch (fileName) {
                    case columnAge:

                        Person person = new Person();
                        person.setAge(Integer.parseInt(line));
                        persons.add(indexPerson, person);
                        break;
                    case columnCpf:
                        persons.get(indexPerson).setCpf(line);
                        break;
                    case columnName:
                        persons.get(indexPerson).setName(line);
                        break;
                }
                line = readFile.readLine();
                indexPerson++;
            }
        } catch (IOException ex) {
            System.out.println("*FileManager: addPersonInfoToArray failed >> ERROR: " + ex);
        }

    }
    
   //Clean all the files
    public void cleanFiles(String table) {
        try {
            List<String> fileNames = fileInfo.getFileNames(table);
            for (int i = 0; i < fileNames.size(); i++) {
                FileWriter fw = new FileWriter(fileInfo.getFilePath(table, fileNames.get(i)));
                fw.write("");
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            System.out.println("*FileManager: writeInsert failed >> ERROR: " + ex);
        }
    }

    public String getColumnAge() {
        return columnAge;
    }

    public String getColumnCpf() {
        return columnCpf;
    }

    public String getColumnName() {
        return columnName;
    }

}
