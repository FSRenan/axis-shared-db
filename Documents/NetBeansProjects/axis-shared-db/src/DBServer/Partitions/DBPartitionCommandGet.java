package DBServer.Partitions;

import Connection.*;
import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class DBPartitionCommandGet implements Serializable {

    private String msg;
    private int status;

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
