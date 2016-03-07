package workflowOffice.controller;

import workflowOffice.model.Person;
import workflowOffice.model.Position;
import workflowOffice.model.Positions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс отвечающий за формирование отчета
 */
public class ReportController {
    public static final ReportController INSTANCE = new ReportController();
    private static Accountant generalAccountant; //главный бухгалтер
    private Map<Person, Set<Position>> personList; //список сотрудников
    private Set<Freelancer> freelancersList; //список фрилансеров
    private String fileName; //имя файла для сохранения отчета
    private List<String> dataFile; //данные для записи в файл

    /**
     * Доступ к контроллеру осуществляется через INSTANCE
     */
    private ReportController() {}

    /**
     * Метод запускает работу контроллера
     */
    public void runReportController() {
        fileName = "report.txt";
        dataFile = new ArrayList<>();
        personList = PersonController.INSTANCE.getPersonList(); //получаем список сотрудников
        freelancersList = PersonController.INSTANCE.getFreelancers(); //получаем список фрилансеров

        dataFile.add("---------------- КОЛИЧЕСТВО РАБОТНИКОВ ----------------");
        amountPersons(); //добавляем данные о работниках
        dataFile.add("-------------------------------------------------------");
        dataFile.add("---------------- ВЫПОЛНЕННАЯ РАБОТА -------------------");
        amountPersonsWork(); //добавляем информацию о выполненной работе
        dataFile.add("-------------------------------------------------------");
        dataFile.add("---------------- ВСЕГО ВЫПЛАЧЕНО ----------------------");
        amountPersonsSalary(); //добавляем отчет о выданной зарплате
        dataFile.add("-------------------------------------------------------");

        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            for (String writeStr : dataFile) {
                fileWriter.write(writeStr); //записываем данные в файл
                fileWriter.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Возникла ошибка при записи в файл! " + e.getMessage());
        }
    }

    /**
     * Метод подсчитывает количество работников
     */
    protected void amountPersons() {
        int persons = personList.size();
        int directors = 0;
        int programmers = 0;
        int designers = 0;
        int testers = 0;
        int managers = 0;
        int accountants = 0;
        int freelancers = freelancersList.size();

        for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) {
            Set<Position> positionSet = person.getValue();
            for (Position position : positionSet) {
                switch (position.toString()) {
                    case "Director":
                        directors++;
                        break;
                    case "Programmer":
                        programmers++;
                        break;
                    case "Designer":
                        designers++;
                        break;
                    case "Tester":
                        testers++;
                        break;
                    case "Manager":
                        managers++;
                        break;
                    case "Accountant":
                        accountants++;
                        break;
                    default:
                        break;
                }
            }
        }
        dataFile.add("Сотрудников :             " + persons);
        dataFile.add("   Из них с должностью :");
        dataFile.add("      Директора :         " + directors);
        dataFile.add("      Программиста :      " + programmers);
        dataFile.add("      Дизайнера :         " + designers);
        dataFile.add("      Тестировщика :      " + testers);
        dataFile.add("      Менеджера :         " + managers);
        dataFile.add("      Бухгалтера :        " + accountants);
        dataFile.add("Фрилансеров :             " + freelancers);
    }

    /**
     * Метод считывает данные о выполненной работе
     */
    protected void amountPersonsWork() {
        int personsTasks = 0;
        int freelancersTasks = freelancersList.size();

        int directorsTasks = 0;
        int programmersTasks = 0;
        int designersTasks = 0;
        int testersTasks = 0;
        int managersTasks = 0;
        int accountantsTasks = 0;

        double personsHours = 0;
        double freelancersHours = 0;

        double directorsHours = 0;
        double programmersHours = 0;
        double designersHours = 0;
        double testersHours = 0;
        double managersHours = 0;
        double accountantsHours = 0;

        for (Freelancer freelancer : freelancersList) {
            freelancersHours += freelancer.getAllHoursWorked();
        }

        for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) {
            personsTasks += person.getKey().getTaskList().size();
            personsHours += person.getKey().getAllHoursWorked();
            Map<Position, APosition> listPositions = person.getKey().getListPositions();
            for (Map.Entry<Position, APosition> personAPositionEntry : listPositions.entrySet()) {
                switch (personAPositionEntry.getKey().toString()) {
                    case "Director":
                        Director director = (Director) personAPositionEntry.getValue();
                        directorsTasks += director.getCountTasks();
                        directorsHours += director.getAllHoursWorked();
                        break;
                    case "Programmer":
                        Programmer programmer = (Programmer) personAPositionEntry.getValue();
                        programmersTasks += programmer.getCountTasks();
                        programmersHours += programmer.getAllHoursWorked();
                        break;
                    case "Designer":
                        Designer designer = (Designer) personAPositionEntry.getValue();
                        designersTasks += designer.getCountTasks();
                        designersHours += designer.getAllHoursWorked();
                        break;
                    case "Tester":
                        Tester tester = (Tester) personAPositionEntry.getValue();
                        testersTasks += tester.getCountTasks();
                        testersHours += tester.getAllHoursWorked();
                        break;
                    case "Manager":
                        Manager manager = (Manager) personAPositionEntry.getValue();
                        managersTasks += manager.getCountTasks();
                        managersHours += manager.getAllHoursWorked();
                        break;
                    case "Accountant":
                        Accountant accountant = (Accountant) personAPositionEntry.getValue();
                        accountantsTasks += accountant.getCountTasks();
                        accountantsHours += accountant.getAllHoursWorked();
                        break;
                    default:
                        break;
                }
            }
        }

        dataFile.add("                   Отработано часов   Выполнено заданий");
        dataFile.add(String
                .format("Сотрудниками :            %.2f            %d", personsHours, personsTasks));
        dataFile.add("   Из них :");
        dataFile.add(String
                .format("      Директорами :       %.2f            %d", directorsHours, directorsTasks));
        dataFile.add(String
                .format("      Программистами :    %.2f            %d", programmersHours, programmersTasks));
        dataFile.add(String
                .format("      Дизайнерами :       %.2f            %d", designersHours, designersTasks));
        dataFile.add(String
                .format("      Тестировщиками :    %.2f            %d", testersHours, testersTasks));
        dataFile.add(String
                .format("      Менеджерами :       %.2f            %d", managersHours, managersTasks));
        dataFile.add(String
                .format("      Бухгалтерами :      %.2f            %d", accountantsHours, accountantsTasks));
        dataFile.add(String
                .format("Фрилансерами :            %.2f            %d", freelancersHours, freelancersTasks));
    }

    /**
     * Метод считывает информацию о выплаченной зарплате
     */
    protected void amountPersonsSalary() {
        generalAccountant = WorkController.INSTANCE.getGeneralAccountant();
        double personsSalary = generalAccountant.getAllSalary();
        double freelancersSalary = generalAccountant.getAllSalaryFreelancers();

        double directorsSalary = 0;
        double programmersSalary = 0;
        double designersSalary = 0;
        double testersSalary = 0;
        double managersSalary = 0;
        double accountantsSalary = 0;

        for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) {
            Map<Position, APosition> listPositions = person.getKey().getListPositions();
            for (Map.Entry<Position, APosition> personAPositionEntry : listPositions.entrySet()) {
                switch (personAPositionEntry.getKey().toString()) {
                    case "Director":
                        Director director = (Director) personAPositionEntry.getValue();
                        directorsSalary += director.getSalary();
                        break;
                    case "Programmer":
                        Programmer programmer = (Programmer) personAPositionEntry.getValue();
                        programmersSalary += programmer.getSalary();
                        break;
                    case "Designer":
                        Designer designer = (Designer) personAPositionEntry.getValue();
                        designersSalary += designer.getSalary();
                        break;
                    case "Tester":
                        Tester tester = (Tester) personAPositionEntry.getValue();
                        testersSalary += tester.getSalary();
                        break;
                    case "Manager":
                        Manager manager = (Manager) personAPositionEntry.getValue();
                        managersSalary += manager.getSalary();
                        break;
                    case "Accountant":
                        Accountant accountant = (Accountant) personAPositionEntry.getValue();
                        accountantsSalary += accountant.getSalary();
                        break;
                    default:
                        break;
                }
            }
        }

        dataFile.add(String.format("Сотрудникам :             %.2f", personsSalary));
        dataFile.add("   Из них :");
        dataFile.add(String.format("      Директорам :        %.2f", directorsSalary));
        dataFile.add(String.format("      Программистам :     %.2f", programmersSalary));
        dataFile.add(String.format("      Дизайнерам :        %.2f", designersSalary));
        dataFile.add(String.format("      Тестировщикам :     %.2f", testersSalary));
        dataFile.add(String.format("      Менеджерам :        %.2f", managersSalary));
        dataFile.add(String.format("      Бухгалтерам :       %.2f", accountantsSalary));
        dataFile.add(String.format("Фрилансерам :             %.2f", freelancersSalary));
    }

}
