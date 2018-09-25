package Connection;

import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class Get implements Serializable {

    private String select;
    private String msg;
    private int status;

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /*
     *0-Operacao bem sucedida
     *1-Erro 
     *
     */
}
