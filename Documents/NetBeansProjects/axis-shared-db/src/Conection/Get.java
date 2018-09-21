package Conection;

import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class Get implements Serializable {

    private String resultado;
    private int status;

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
