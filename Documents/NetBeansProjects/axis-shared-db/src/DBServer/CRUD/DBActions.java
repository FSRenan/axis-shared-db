package DBServer.CRUD;

import Conection.*;
import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class DBActions implements Serializable {

    //Constraints
    private final int const_Insert = 0;
    private final int const_Select = 1;
    private final int const_Update = 2;
    private final int const_Delete = 3;

    public void execute(Post post, Get get) {

        switch (post.getCommand()) {
            case const_Insert:
                insert(post, get);
                break;
            case const_Select:
                select(post, get);
                break;
            case const_Update:
                update(post, get);
                break;
            case const_Delete:
                delete(post, get);
                break;
        }
    }

    public void insert(Post post, Get get) {
        //Tabelas sao pastas, colunas sao arquivos, backp sera copiar arquivo, na hr
        //de recuperar ele copia
    }

    public void select(Post post, Get get) {

    }

    public void update(Post post, Get get) {

    }

    public void delete(Post post, Get get) {

    }

    public int getConst_Insert() {
        return const_Insert;
    }

    public int getConst_Select() {
        return const_Select;
    }

    public int getConst_Update() {
        return const_Update;
    }

    public int getConst_Delete() {
        return const_Delete;
    }

}
