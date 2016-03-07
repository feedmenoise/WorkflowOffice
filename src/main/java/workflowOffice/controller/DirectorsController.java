package workflowOffice.controller;

import workflowOffice.model.Person;
import workflowOffice.model.Position;
import workflowOffice.model.Positions.Director;
import workflowOffice.main.Company;

import java.util.*;

/**
 * Класс управляющий директорами
 */
public class DirectorsController {
    public static final DirectorsController INSTANCE = new DirectorsController();
    private static Map<Person, Set<Position>> personList; //список всех сотрудников
    private static List<Person> directorsList; //список директоров
    private static final Map<Position, String> taskList = new HashMap<>(); //список распоряжений

    /**
     * Доступ к контроллеру осуществляется через INSTANCE
     */
    private DirectorsController() {}

    static { //заполнение списка распоряжений для сотрудников
        taskList.put(Position.Programmer, "писать код");
        taskList.put(Position.Designer, "рисовать макет");
        taskList.put(Position.Tester, "тестировать программу");
        taskList.put(Position.Manager, "продавать услуги");
        taskList.put(Position.Accountant, "составить отчетность");
        taskList.put(Position.Cleaner, "выполнять уборку");
    }

    /**
     * Метод, запускающий работу DirectorsController
     */
    public void runDirectorsController() {
        personList = PersonController.INSTANCE.getPersonList(); //получаем список работников
        directorsList = selectionOfDirectors(personList); //получаем список директоров
        //распределение сотрудников между директорами
        distributionOfPersons(directorsList, selectionOfPersonsOtherDirectors(personList));
        //задание всем сотрудникам списка распоряжений
        setDirectorsTaskList(directorsList, taskList);

        //запуск работы директоров
        for (int i = 0; i < Company.MAX_WORKING_HOURS; i++) {
            workDirectors(directorsList);
            try {
                Thread.sleep(Company.ONE_HOUR); //задания раздаются директорами через каждый час
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод передает всем директорами список распоряжений
     *
     * @param directorsList список директоров
     * @param taskList      список заданий
     */
    protected void setDirectorsTaskList(List<Person> directorsList, Map<Position, String> taskList) {
        for (Person director : directorsList) {
            Director personDirector = (Director) director.getListPositions().get(Position.Director);
            //передаем список распоряжений
            personDirector.setTaskList(taskList);
        }
    }

    /**
     * Метод распределяет всех сотрудников между директорами
     *
     * @param directorsList            список директоров
     * @param personListOtherDirectors список сотрудников кроме директоров
     */
    protected void distributionOfPersons(List<Person> directorsList, Map<Person, Set<Position>> personListOtherDirectors) {
        int countListPersons = directorsList.size(); //кол-во списков
        int countPersonsInList = personListOtherDirectors.size() / countListPersons; //кол-во сотрудников в списке
        int countOtherPersonsInList = personListOtherDirectors.size() % countListPersons;

        Iterator<Map.Entry<Person, Set<Position>>> iterator = personListOtherDirectors.entrySet().iterator();

        //передаем всем директорам список их сотрудников
        for (Person director : directorsList) {
            Director personDirector = (Director) director.getListPositions().get(Position.Director);
            Map<Person, Set<Position>> list = getSubListPersons(iterator, countPersonsInList);
            if (list.size() > 0) personDirector.setPersonList(list);
        }

        //если остались еще не распределенные работники
        if (countOtherPersonsInList > 0) {
            for (Person director : directorsList) {
                Director personDirector = (Director) director.getListPositions().get(Position.Director);
                //передаем всем директорам список их сотрудников
                Map<Person, Set<Position>> list = getSubListPersons(iterator, countPersonsInList);
                if (list.size() > 0) {
                    Map<Person, Set<Position>> tmpList = personDirector.getPersonList();
                    //добавляем к существующему списку оставшихся сотрудников
                    for (Map.Entry<Person, Set<Position>> person : list.entrySet()) {
                        tmpList.put(person.getKey(), person.getValue());
                        countOtherPersonsInList--;
                    }
                    personDirector.setPersonList(tmpList);
                }
            }
        }
    }

    /**
     * Метод формирует подсписок сотрудников для директора
     *
     * @param iterator           итератор списка всех сотрудников (кроме директоров)
     * @param countPersonsInList кол-во сотрудников в списке
     * @return подсписок сотрудников для директора
     */
    protected Map<Person, Set<Position>> getSubListPersons(Iterator<Map.Entry<Person, Set<Position>>> iterator,
                                                           int countPersonsInList) {
        Map<Person, Set<Position>> list = new HashMap<>();
        for (int i = 0; i < countPersonsInList; i++) {
            if (iterator.hasNext()) {
                Map.Entry<Person, Set<Position>> thisEntry = iterator.next();
                list.put(thisEntry.getKey(), thisEntry.getValue());
            }
        }
        return list;
    }

    /**
     * Метод раздает всем директорам задание
     *
     * @param list список директоров
     */
    protected void workDirectors(List<Person> list) {
        for (Person director : list)
            if (!director.isBusy() && director.isWork())
                director.performTask(Position.Director, "раздать распоряжения сотрудникам");
    }

    /**
     * Метод выбирает из списка сотрудников только с должностью директора
     *
     * @param personList список всех сотрудников
     * @return list список сотрудников с должностью директора
     */
    protected List<Person> selectionOfDirectors(Map<Person, Set<Position>> personList) {
        List<Person> list = new ArrayList<>();
        for (Map.Entry<Person, Set<Position>> person : personList.entrySet())
            if (person.getValue().contains(Position.Director)) {
                person.getKey().setAmountHoursOneInstructions(Company.ONE_HOUR); //задаем время выполнение задания 1 час
                list.add(person.getKey());
            }
        return list;
    }

    /**
     * Метод выбирает из списка всех сотрудников кроме директоров
     *
     * @param personList список всех сотрудников
     * @return list список сотрудников не с должностью директора
     */
    protected Map<Person, Set<Position>> selectionOfPersonsOtherDirectors(Map<Person, Set<Position>> personList) {
        Map<Person, Set<Position>> list = new HashMap<>();
        for (Map.Entry<Person, Set<Position>> person : personList.entrySet())
            if (!person.getValue().contains(Position.Director)) {
                list.put(person.getKey(), person.getValue());
            }
        return list;
    }

}
