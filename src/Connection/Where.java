package Connection;

import java.io.Serializable;

/**
 * @author Renan Ferreira
 */
public class Where implements Serializable {

    private String column;
    private String value;

    public Where(String where) {
        String arrayAux[];
        arrayAux = where.split("=");
        this.column = arrayAux[0] + ".txt";
        this.value = arrayAux[1];
    }

    public Where(String column, String value) {

        this.column = column + ".txt";
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
