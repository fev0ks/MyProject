package scienceWork.Tests;

import org.junit.Test;
import scienceWork.dataBase.ManagerDB;

public class TestManagerDB {
    @Test
    public void dirToDBTest() {
        ManagerDB managerDB = ManagerDB.getInstance();
        assert (managerDB.dirToDB(null).equalsIgnoreCase("ошибка файла!"));
        assert (true);
        System.out.println();
    }
}
