/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManager;

import Connection.Where;


import java.io.*;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * @author rferreira
 */
public class FileManager implements Serializable {

    private FileInfo fileInfo = new FileInfo();
    private final String COLUMN_AGE = "age.txt";
    private final String COLUMN_CPF = "cpf.txt";
    private final String COLUMN_NAME = "name.txt";

    //Update values
    public ArrayList<Person> updatePerson(String table, ArrayList<String> values, Where where) {
        ArrayList<Person> persons = getPersons(table);
        switch (where.getColumn()) {
            case COLUMN_AGE:
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getAge() == Integer.parseInt(where.getValue())) {
                        persons.set(i, new Person(Integer.parseInt(values.get(0)), values.get(1), values.get(2)));
                    }
                }
                break;
            case COLUMN_CPF:
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getCpf().equals(where.getValue())) {
                        persons.set(i, new Person(Integer.parseInt(values.get(0)), values.get(1), values.get(2)));
                    }
                }
                break;
            case COLUMN_NAME:
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
            System.err.println("*FileManager: writeInsert failed >> ERROR: " + ex);
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

                readFile.close();
                fileReader.close();
            } catch (IOException ex) {
                System.out.println("*FileManager: readSelect failed >> ERROR: " + ex);
            }
        }
        return persons;
    }

    //Return all the persons selected with where
    public ArrayList<Person> getPersons(String table, Where where) {
        ArrayList<Person> persons = getPersons(table);

        if (where != null) {
            //Remove valores do array pela condicao do WHERE
            switch (where.getColumn()) {
                case COLUMN_AGE:
                    persons.removeIf(person -> (person.getAge() != Integer.parseInt(where.getValue())));
                    break;
                case COLUMN_CPF:
                    persons.removeIf(person -> (!person.getCpf().equalsIgnoreCase(where.getValue())));
                    break;
                case COLUMN_NAME:
                    persons.removeIf(person -> (!person.getName().equalsIgnoreCase(where.getValue())));
                    break;
            }
        }
        return persons;
    }

    //Return all the persons in the table Person
    public ArrayList<Person> getPersonsAdicionalPartitionsInfo(String table, ArrayList<Person> persons) {
        List<String> fileNames = fileInfo.getFileNames(table);

        for (int i = 0; i < fileNames.size(); i++) {
            try {
                FileReader fileReader = new FileReader(fileInfo.getFilePath(table, fileNames.get(i)));
                BufferedReader readFile = new BufferedReader(fileReader);

                addPersonInfoToArray(persons, readFile, fileNames.get(i));

                readFile.close();
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
        Person person;
        try {
            String line = readFile.readLine();

            while (line != null) {
                switch (fileName) {
                    case COLUMN_AGE:
                        if (persons.size() > indexPerson)
                            persons.get(indexPerson).setAge(Integer.parseInt(line));
                        else {
                            person = new Person();
                            person.setAge(Integer.parseInt(line));
                            persons.add(indexPerson, person);
                        }
                        break;
                    case COLUMN_CPF:
                        if (persons.size() > indexPerson)
                            persons.get(indexPerson).setCpf(line);
                        else {
                            person = new Person();
                            person.setCpf(line);
                            persons.add(indexPerson, person);
                        }
                        break;
                    case COLUMN_NAME:
                        if (persons.size() > indexPerson)
                            persons.get(indexPerson).setName(line);
                        else {
                            person = new Person();
                            person.setName(line);
                            persons.add(indexPerson, person);
                        }
                        break;
                }
                line = readFile.readLine();
                indexPerson++;
            }
        } catch (IOException ex) {
            System.err.println("*FileManager: addPersonInfoToArray failed >> ERROR: " + ex);
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
            System.err.println("*FileManager: writeInsert failed >> ERROR: " + ex);
        }
    }

    //Create DATA folder
    public void createDataFolder(String table) throws IOException {
        String tablePath = fileInfo.getPath() + File.separator + table;

        new File(tablePath).mkdirs();
        createDataFiles(tablePath, "age.txt");
        createDataFiles(tablePath, "cpf.txt");
        createDataFiles(tablePath, "name.txt");
    }

    //Create DATA Files
    public void createDataFiles(String table, String column) throws IOException {
        File file = new File(table + File.separator + column);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    //Delete DATA folder
    public void deleteDataFolder(String table) {
        String path = fileInfo.getPath() + File.separator + table;
        File folder = new File(path);

        deleteDir(folder);
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        if (!file.delete()) System.err.println("deleteDir failed: path " + file.getAbsolutePath());

    }

    public String getColumnAge() {
        return COLUMN_AGE;
    }

    public String getColumnCpf() {
        return COLUMN_CPF;
    }

    public String getColumnName() {
        return COLUMN_NAME;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }


}
