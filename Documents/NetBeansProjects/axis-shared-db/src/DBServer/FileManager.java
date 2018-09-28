/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBServer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lab101p2
 */
public class FileManager implements Serializable {

    private final String path = System.getProperty("user.dir") + "\\Data";

    //Read Select
    public String ReadSelect(String table, ArrayList values, String where) {
        String select = "";
        System.out.println("Entrou read");
        ArrayList<Person> persons = getPersons(table);
        System.out.println("passou getperson");
        if (where.isEmpty()) {

        }

        for (Person person : persons) {
            select += "Name: " + person.getName() + "\n";
            select += "Age: " + person.getAge() + "\n";
            select += "cpf: " + person.getCpf() + "\n\n";
        }

        return select;
    }

    public ArrayList<Person> getPersons(String table) {
        List<String> fileNames = getFileNames(Paths.get(path + "\\" + table));
        ArrayList<Person> persons = new ArrayList();
        System.out.println("Get Persoin");
        for (int i = 0; i < fileNames.size(); i++) {
            try {
                FileReader fileReader = new FileReader(path + "\\" + table + "\\" + fileNames.get(i));
                BufferedReader readFile = new BufferedReader(fileReader);

                int indexPerson = 0;

                String line = readFile.readLine();

                while (line != null) {
                    System.out.println("File: " + fileNames.get(i));
                    System.out.println(line);

                    switch (fileNames.get(i)) {
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
                fileReader.close();
            } catch (IOException ex) {
                System.out.println("*readSelect: reading failed >> ERROR: " + ex);
            }
        }

        return persons;
    }

    //Values for insert
    public void writeInsert(String table, ArrayList<String> values) {
        try {
            List<String> fileNames = getFileNames(Paths.get(path + "\\" + table));
            for (int i = 0; i < fileNames.size(); i++) {
                FileWriter fw = new FileWriter(path + "\\" + table + "\\" + fileNames.get(i), true);
                fw.append(values.get(i) + "" + "\r\n");
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            System.out.println("*writeInsert: writeInsert failed >> ERROR: " + ex);
        }
    }

    private List<String> getFileNames(Path dir) {
        List<String> fileNames = new ArrayList();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

            for (Path file : stream) {
                fileNames.add(file.getFileName() + "");
            }
        } catch (IOException ex) {
            System.out.println("*FileManager: getFileNames failed >> ERROR: " + ex);
        }

        return fileNames;
    }
}
