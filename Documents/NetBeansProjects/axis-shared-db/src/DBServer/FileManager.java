/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBServer;

import java.io.BufferedWriter;
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

    //Values for insert
    public void writeInsert(String table, ArrayList<String> values, String fileName) {
        try {
            FileWriter fw = new FileWriter(path + "\\" + table + "\\" + fileName);
            fw.write(values.get(0));
            fw.flush();
            fw.close();
            
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
