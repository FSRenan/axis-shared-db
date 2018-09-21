package Conection;

import DBServer.CRUD.DBActions;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rferreira
 */
public class Post implements Serializable {
    
    DBActions actions = new DBActions();
    
    //Operators
    private ArrayList values;
    private String table;
    private String column;
    private String where;
    private int command;

    //Values for insert
    public void insert(String table, ArrayList values) {
        this.table = table;
        this.values = values;
        this.command = actions.getConst_Insert();
    }
    //Values for select
    public void select(String table, ArrayList values, String where) {
        this.table = table;
        this.values = values;
        this.where = where;
        this.command = actions.getConst_Select();
    }
    //Values for update
    public void update(String table, ArrayList values, String where) {
        this.table = table;
        this.values = values;
        this.where = where;
        this.command = actions.getConst_Update();
    }
    //Values for delete
    public void delete(String table, String where) {
        this.table = table;
        this.where = where;
        this.command = actions.getConst_Delete();
    }

    public ArrayList getValues() {
        return values;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getWhere() {
        return where;
    }

    public int getCommand() {
        return command;
    }

}
