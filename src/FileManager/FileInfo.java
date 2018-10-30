package FileManager;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Renan Ferreira
 */
public class FileInfo implements Serializable {

    private static String path = System.getProperty("user.dir") + "\\Data";

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        FileInfo.path = path;
    }

    //Return a List with all the files in a folder
    public List<String> getFileNames(String table) {
        List<String> fileNames = new ArrayList();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(this.path + "\\" + table));

            for (Path file : stream) {
                fileNames.add(file.getFileName() + "");
            }
        } catch (IOException ex) {
            System.err.println("*FileManager: getFileNames failed >> ERROR: " + ex);
        }

        return fileNames;
    }

    //Return the path to a text file
    public String getFilePath(String table, String fileName) {
        return path + "\\" + table + "\\" + fileName;
    }
}
