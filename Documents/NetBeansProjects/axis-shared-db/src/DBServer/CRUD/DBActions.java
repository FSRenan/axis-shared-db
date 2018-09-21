package DBServer.CRUD;

import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class DBActions implements Serializable{ 

    //Constraints
    private final int const_Insert = 0;
    private final int const_Select = 1;
    private final int const_Update = 2;
    private final int const_Delete = 3;

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
