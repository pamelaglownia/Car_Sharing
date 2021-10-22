package pl.glownia.pamela.company;

import pl.glownia.pamela.DataBaseConnection;
import pl.glownia.pamela.Input;

import java.util.ArrayList;
import java.util.List;

public class CompanyService {
    private List<Company> companies;
    private final CompanyRepository companyRepository;
    private final Input input = new Input();

    public CompanyService(DataBaseConnection dataBase) {
        companies = new ArrayList<>();
        companyRepository = new CompanyRepository(companies);
        companyRepository.createTable(dataBase);
    }

    public void addNewCompany() {
        int companyId = getCompanyId();
        System.out.println("Enter the company name:");
        String companyName = input.getNewItem();
        companyRepository.insertRecordToTable(companyId, companyName);
    }

    private int getCompanyId() {
        companies = companyRepository.readRecords();
        if (companies.isEmpty()) {
            return 1;
        } else {
            return companies.size() + 1;
        }
    }

    private void getAll() {
        companies = companyRepository.readRecords();
        System.out.println("Choose the company:");
        companies.forEach(System.out::println);
        System.out.println("0. Back");
    }

    public boolean isEmptyList() {
        companies = companyRepository.readRecords();
        return companies.isEmpty();
    }

    public int chooseTheCompany() {
        if (isEmptyList()) {
            System.out.println("The company list is empty!");
            return 0;
        } else {
            getAll();
        }
        int decision = input.takeUserDecision(0, companies.size());
        return companies.stream()
                .filter(company -> company.getId() == decision)
                .mapToInt(Company::getId)
                .findFirst().orElse(0);
    }

    public void deleteCompanyFromList(int companyToDelete) {
        if (companyToDelete != 0) {
            companyRepository.deleteCompany(companyToDelete);
        }
    }

    public void getCompanyName(int userDecision) {
        companies = companyRepository.readRecords();
        String chosenCompanyName = companies.stream()
                .filter(company -> company.getId() == userDecision)
                .map(Company::getName)
                .findFirst().orElse(null);
        System.out.println("'" + chosenCompanyName + "' company:");
    }

    public void closeCompanyConnection() {
        companyRepository.closeConnection();
    }
}