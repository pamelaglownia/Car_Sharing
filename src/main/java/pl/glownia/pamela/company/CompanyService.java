package pl.glownia.pamela.company;

import pl.glownia.pamela.DataBaseConnection;

import java.util.ArrayList;
import java.util.List;

public class CompanyService {
    private List<Company> companies;
    private final CompanyRepository companyRepository;

    public CompanyService(DataBaseConnection dataBase) {
        companies = new ArrayList<>();
        companyRepository = new CompanyRepository(companies);
        companyRepository.createTable(dataBase);
    }

    public void addNewCompany(String companyName) {
        companyRepository.insertRecordToTable(getCompanyId(), companyName);
    }

    private int getCompanyId() {
        companies = companyRepository.readRecords();
        if (companies.isEmpty()) {
            return 1;
        } else {
            return companies.size() + 1;
        }
    }

    public int getCompaniesListSize() {
        companies = companyRepository.readRecords();
        return companies.size();
    }

    public void getAll() {
        companies = companyRepository.readRecords();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose the company:");
            companies.forEach(System.out::println);
            System.out.println("0. Back");
        }
    }

    public boolean isEmptyList() {
        companies = companyRepository.readRecords();
        return companies.isEmpty();
    }

    public int chooseTheCompany(int chosenCompany) {
        if (companies.isEmpty() || chosenCompany == 0) {
            return 0;
        } else {
            return companies.get(chosenCompany - 1).getId();
        }
    }

    public void deleteCompanyFromList(int companyToDelete) {
        if (companyToDelete != 0) {
            companyRepository.deleteCompany(companyToDelete);
        } else {
            System.out.println("You can't delete company with cars.");
        }
    }

    public void getCompanyName(int userDecision) {
        companies = companyRepository.readRecords();
        String chosenCompanyName = companies.get(userDecision - 1).getName();
        System.out.println("'" + chosenCompanyName + "' company:");
    }

    public void closeCompanyConnection() {
        companyRepository.closeConnection();
    }
}