package pl.glownia.pamela;

import java.util.List;

interface CompanyDao {
    void insertRecordToTable(String companyName);

    List<Company> readRecords();

    void getAllCompanies();
}