package Connection;

import java.io.Serializable;

/**
 *
 * @author Renan Ferreira
 */
public class Where implements Serializable{

    private String column;
    private String value;

    public Where(String column, String value) {
        this.column = column;
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
