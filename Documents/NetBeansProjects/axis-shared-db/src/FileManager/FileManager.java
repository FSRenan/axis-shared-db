/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rferreira
 */
public class FileManager implements Serializable {

    private FileInfo fileInfo = new FileInfo();

    //Values for insert
    public void writeInsert(String table, ArrayList<String> values) {
        try {
            List<String> fileNames = fileInfo.getFileNames(table);
            for (int i = 0; i < fileNames.size(); i++) {
                FileWriter fw = new FileWriter(fileInfo.getFilePath(table, fileNames.get(i)), true);
                fw.append(values.get(i) + "" + "\r\n");
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            System.out.println("*FileManager: writeInsert failed >> ERROR: " + ex);
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
                    case "age.txt":
                        Person person = new Person();
                        person.setAge(Integer.parseInt(line));
                        persons.add(indexPerson, person);
                        break;
                    case "cpf.txt":
                        persons.get(indexPerson).setCpf(line);
                        break;
                    case "name.txt":
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

}
