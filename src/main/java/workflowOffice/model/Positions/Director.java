package workflowOffice.model.Positions;

import workflowOffice.main.Company;
import workflowOffice.controller.PersonController;
import workflowOffice.model.Employee;
import workflowOffice.model.Person;
import workflowOffice.model.Position;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Класс должности Директор
 */
public class Director extends APosition implements Employee {
    private static int fixedRate; //фиксированная ставка
    private Map<Position, String> taskList; //список распоряжений для сотрудников
    private int countTasks = 0; //счетчик выполненных заданий
    private double salary; //зарплата
    private double allHoursWorked = 0; //отработанные часы - для отчета
    private Map<Person, Set<Position>> personList; //список сотрудников под руководством данного директора
    private static final SecureRandom random = Company.random;
    private Map<Position, String> currentTaskList = new HashMap<>(); //список текущих распоряжений для сотрудников
    private Map<Position, Integer> taskListPriority = new HashMap<>(); //список приоритетов распоряжений для сотрудников

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

            //создаем список текущих распоряжений для сотрудников
            for (int i = 0; i < amountTasks; i++) {
                int x = random.nextInt(Position.values().length); //выбор случайной должности
                if (Position.values()[x] == Position.Director) continue; //если должность директора идем дальше
                //иначе добавляем в список
                currentTaskList.put(Position.values()[x], taskList.get(Position.values()[x]));
            }

            //задаем приоритеры заданий
            taskListPriority.put(Position.Accountant, random.nextInt(5)+1);
            taskListPriority.put(Position.Manager, random.nextInt(5)+1);
            taskListPriority.put(Position.Programmer, random.nextInt(5)+1);
            taskListPriority.put(Position.Designer, random.nextInt(5)+1);
            taskListPriority.put(Position.Tester, random.nextInt(5)+1);
            taskListPriority.put(Position.Cleaner, random.nextInt(5)+1);
            
            for (Map.Entry<Person, Set<Position>> person : personList.entrySet()) {
                Person currentPerson = person.getKey(); //текущий сотрудник
                Position position = null; //текущее задание
                int currentPriority = 0; //приоритет текущего задания

                if (!currentPerson.isBusy() && currentPerson.isWork()) {

                    //проходим по списку заданий
                    for (Map.Entry<Position, String> task : currentTaskList.entrySet()) {
                        int posRate = 0;
                        int taskRate = 0;

                        if (person.getValue().contains(task.getKey())) {

                            if (taskListPriority.get(task.getKey()) > currentPriority) {
                                position = task.getKey(); //запоминаем задание с большим приоритетом
                                currentPriority = taskListPriority.get(task.getKey());
                            }
                            if (taskListPriority.get(task.getKey()) == currentPriority) {
                                //Получаем стоимость оплаты текущей позиции

                                try {
                                    //Получаем стоимость оплаты заданий
                                    posRate = getRate(position);
                                    //проверяем стоимость заданий
                                    taskRate = getRate(task.getKey());
                                } catch (NullPointerException e){
                                    System.out.println("Во время проверки стоимости задания произошла ошибка!" + e.getMessage());
                                }
                                //Сравниваем стоимость
                                //Если оплата текущей позиции больше, то идем дальше
                                if (posRate > taskRate) {
                                    continue;
                                }
                                //иначе запоминаем более оплачиваемое задание
                                else position = task.getKey();

                            } else continue;
                        }
                    }

                    if (position != null) {
                        currentPerson.performTask(position, taskList.get(position)); //даем задание
                        isTherePerformer = true; //исполнитель есть
                    }
                }

                if (!isTherePerformer) { //если исполнителя нет, задание идет на фриланс
                    if (currentTaskList.containsKey(Position.Cleaner)) {
                        currentTaskList.remove(Position.Cleaner);
                    } else
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

    public static int getFixedRate() {
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

    public int getRate(Position position) {
        int rate = 0;
        if (position.equals(Position.Programmer)) {
            rate = Programmer.getHourlyRate();
        } else if (position.equals(Position.Designer)) {
            rate = Designer.getHourlyRate();
        } else if (position.equals(Position.Tester)) {
            rate = Tester.getHourlyRate();
        } else if (position.equals(Position.Manager)) {
            rate = Manager.getFixedRate();
        } else if (position.equals(Position.Accountant)) {
            rate = Accountant.getFixedRate();
        } else if (position.equals(Position.Cleaner)) {
            rate = Cleaner.getHourlyRate();
        }
        return rate;
    }
}
