package workflowOffice.model.Positions;

import workflowOffice.main.Company;
import workflowOffice.controller.PersonController;
import workflowOffice.model.Employee;
import workflowOffice.model.Person;
import workflowOffice.model.Position;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;

/**
 * Класс должности Директор
 */
public class Director extends APosition implements Employee {
    private int fixedRate; //фиксированная ставка
    private Map<Position, String> taskList; //список распоряжений для сотрудников
    private int countTasks = 0; //счетчик выполненных заданий
    private double salary; //зарплата
    private double allHoursWorked = 0; //отработанные часы - для отчета
    private Map<Person, Set<Position>> personList; //список сотрудников под руководством данного директора
    private static final SecureRandom random = Company.random;

    public Director(String name) {
        super(name);
    }

    /**
     * Метод в котором выполняется работа должности
     */
    @Override
    public void getToWork() {
        countTasks++;
        allHoursWorked += Company.ONE_HOUR;
        if ((taskList != null) && (personList != null)) {
            //раздает всем задания
            int amountTasks = random.nextInt(taskList.size()) + 1; //выбираем случайное количество распоряжений
            boolean isTherePerformer = false; //флаг наличия исполнителя

            for (int i = 0; i < amountTasks; i++) {
                int x = random.nextInt(Position.values().length); //выбор случайной должности
                if (Position.values()[x] == Position.Director) continue; //если должность директора идем дальше
                Position currentPosition = Position.values()[x];

                for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) {
                    Person currentPerson = person.getKey(); //текущий сотрудник

                    if (!currentPerson.isBusy()
                            && currentPerson.isWork()
                            && person.getValue().contains(currentPosition)) {
                        currentPerson.performTask(currentPosition, taskList.get(currentPosition)); //даем задание
                        isTherePerformer = true; //исполнитель есть
                    }
                }
                if (!isTherePerformer) { //если никто не взялся за задание нанимаем фрилансера
                    PersonController.INSTANCE.createNewFreelancer().getToWork();
                }
            }
        }
    }

    /**
     * Метод для получения зарплаты
     *
     * @return сумму зарплаты
     */
    @Override
    public double paySalary() {
        salary += getFixedRate();
        return getFixedRate();
    }

    @Override
    public double getAllHoursWorked() {
        return allHoursWorked;
    }

    @Override
    public void addAllHoursWorked(double allHoursWorked) {
        this.allHoursWorked = allHoursWorked;
    }

    @Override
    public void setFixedRate(int fixedRate) {
        this.fixedRate = fixedRate;
    }

    @Override
    public int getFixedRate() {
        return fixedRate;
    }

    public void setPersonList(Map<Person, Set<Position>> personList) {
        this.personList = personList;
    }

    public Map<Person, Set<Position>> getPersonList() {
        return personList;
    }

    public void setTaskList(Map<Position, String> taskList) {
        this.taskList = taskList;
    }

    public int getCountTasks() {
        return countTasks;
    }

    public double getSalary() {
        return salary;
    }
}
