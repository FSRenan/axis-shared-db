package DBServer.Partitions;

import DBServer.CRUD.DBActions;
import FileManager.*;

import java.util.ArrayList;
import java.util.List;

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
}
