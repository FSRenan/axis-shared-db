/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBServer.Partitions;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Renan Ferreira
 */
public class DBPartitionCommandPost  implements Serializable{

    private ArrayList<String> files = new ArrayList();

    public DBPartitionCommandPost(ArrayList<String> files) {
        this.files = files;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

}
