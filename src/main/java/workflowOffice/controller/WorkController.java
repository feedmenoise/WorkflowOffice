package workflowOffice.controller;

import workflowOffice.model.Person;
import workflowOffice.model.Position;
import workflowOffice.model.Positions.Accountant;
import workflowOffice.main.Company;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Класс отвечающий за работу всех сотрудников
 */
public class WorkController {
    public static final WorkController INSTANCE = new WorkController();

    private Map<Person, Set<Position>> personList; //список сотрудников
    private static Set<Position> necessaryPositions; //список обязательных должностей
    private static Accountant generalAccountant; //главный бухгалтер

    /**
     * Доступ к контроллеру осуществляется через INSTANCE
     */
    private WorkController() {}

    /**
     * Метод запускает работу контроллера
     */
    public void runWorkController() {
        necessaryPositions = createNecessaryPositions();
        //задаем список обязательных должностей
        PersonController.INSTANCE.setNecessaryPositions(necessaryPositions);
        //задаем максимальное кол-во директоров
        PersonController.INSTANCE.setCountDirectorsPositions(Company.MAX_AMOUNT_DIRECTORS_POSITIONS);
        //создаем сотрудников
        PersonController.INSTANCE.runPersonController();

        personList = PersonController.INSTANCE.getPersonList(); //получаем список работников
        //получаем главного бухгалтера
        Person personGeneralAccountant = PersonController.INSTANCE.selectionRandomAccountant(personList);
        generalAccountant = (Accountant) personGeneralAccountant.getListPositions().get(Position.Accountant);

        try {
            workMonth(); //запуск моделирования работы компании в течении месяца
        } catch (InterruptedException e) {
            System.out.println("Возникла ошибка во время моделирования! " + e.getMessage());
        }
    }

    /**
     * Метод, запускающий работу сотрудников в течении месяца
     *
     * @throws InterruptedException
     */
    private void workMonth() throws InterruptedException {
        //запуск всех сотрудников
        for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) person.getKey().start();

        for (int i = 0; i < Company.MAX_WORKING_WEEKS; i++) {
            for (int j = 0; j < Company.MAX_WORKING_DAYS; j++) {
                DirectorsController.INSTANCE.runDirectorsController();
                Thread.sleep(Company.MAX_WORKING_HOURS);
            }
            //в конце недели выплачиается зарплата
            generalAccountant.payWeekSalary(personList, PersonController.INSTANCE.getFreelancers());
        }

        ReportController.INSTANCE.runReportController(); //создаем отчет

        //остановка всех сотрудников в конце месяца
        for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) person.getKey().setStopWork(true);
    }

    /**
     * Метод создает список обязательных должностей
     *
     * @return список должностей
     */
    public Set<Position> createNecessaryPositions() {
        Set<Position> list = new HashSet<>();
        list.add(Position.Director);
        list.add(Position.Manager);
        list.add(Position.Accountant);
        return list;
    }

    /**
     * Метод возвращает главного бухгалтера
     *
     * @return экземпляр класса Accountant
     */
    public Accountant getGeneralAccountant() {
        return generalAccountant;
    }
}
