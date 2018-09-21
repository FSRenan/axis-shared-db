package Conection;

import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class Post implements Serializable {

    private char operacao;

    public Post() {
        operacao = '5';
    }

    public char getOperacao() {
        return operacao;
    }

    public void setOperacao(char operacao) {
        this.operacao = operacao;
    }
}
