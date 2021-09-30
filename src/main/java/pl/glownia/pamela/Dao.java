package pl.glownia.pamela;

import java.util.List;

interface Dao<T> {
    void createTable(CarSharingJDBC dataBase);

    void insertRecordToTable(CarSharingJDBC database, String record);

    List<T> readRecords(CarSharingJDBC database);

    void getAll(CarSharingJDBC database, List<T> list);
}