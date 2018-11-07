package DBServer.Partitions;

import DBServer.CRUD.DBActions;
import FileManager.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author rferreira
 */
public class DBPartitionActions extends FileManager {
    private final FileManager fileManager = new FileManager();

    public DBPartitionActions(int partition) {
        FileInfo.setPath(FileInfo.getPath() + "\\Partition" + partition);
    }

    public ArrayList<Person> getPersonsInfo(String table, int partition, ArrayList<Person> persons) {
        if (persons.isEmpty())
            persons = fileManager.getPersons(table);
        else
            persons = fileManager.getPersonsAdicionalPartitionsInfo(table, persons);//Add to array aditional info

        System.out.println("PARTITION " + partition + ": array size = " + persons.size());

        return persons;
    }

    public boolean checkFileNames(String table) {
        List<String> fileNames = getFileInfo().getFileNames(table);
        int columns = 0;

        for (String fileName : fileNames) {
            if (getColumnAge().equals(fileName) || getColumnCpf().equals(fileName) || getColumnName().equals(fileName)) {
                columns++;
            }
        }
        return columns == 2;

    }

    public void writePersons(ArrayList<Person> persons, String table, int partition) {

        boolean firstPersonInserted = false;
        ArrayList<String> values = new ArrayList<>();

        if (persons.isEmpty()) {
            cleanFiles(table);
        } else {
            //Só grava o ultimo, o false deve estar no primeiro
            for (Person person : persons) {
                switch (partition) {
                    case 1:
                        values = new ArrayList(Arrays.asList(person.getAge() + "", person.getCpf()));
                        break;
                    case 2:
                        values = new ArrayList(Arrays.asList(person.getAge() + "", person.getName()));
                        break;
                    case 3:
                        values = new ArrayList(Arrays.asList(person.getCpf(), person.getName()));
                        break;
                }
                writeInsert(table, values, firstPersonInserted);
                firstPersonInserted = true;
            }
        }
    }

}
