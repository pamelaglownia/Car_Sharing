package pl.glownia.pamela;

import java.sql.Connection;

public class DataBaseConnection {

    private final UserDecision userDecision = new UserDecision();
    private final CarSharingJDBC dataBase = new CarSharingJDBC();

    void createConnection() {
        String dataBaseFileName = userDecision.getDataBaseName();
        dataBase.createDataBase(dataBaseFileName);
    }

    public Connection getConnection() {
        return dataBase.getConnection();
    }
}