package Connection;

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
    private Where where;
    private int command;

    //Values for insert
    public void insert(String table, ArrayList values) {
        this.table = table;
        this.values = values;
        this.command = actions.getConst_Insert();
    }

    //Values for select
    public void select(String table, Where where) {
        this.table = table;
        this.where = where;
        this.command = actions.getConst_Select();
    }

    //Values for update
    public void update(String table, ArrayList values, Where where) {
        this.table = table;
        this.values = values;
        this.where = where;
        this.command = actions.getConst_Update();
    }

    //Values for delete
    public void delete(String table, Where where) {
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

    public Where getWhere() {
        return where;
    }

    public int getCommand() {
        return command;
    }


}
