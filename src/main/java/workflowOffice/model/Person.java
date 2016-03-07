package workflowOffice.model;

import workflowOffice.model.Positions.APosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс Сотрудник
 */
public class Person extends Thread {
    private final String personName; //имя сотрудника
    private volatile boolean isBusy; //проверка занят ли сотрудник
    private volatile boolean isTask; //проверка есть ли задание
    private volatile boolean stopWork; //флаг для остановки работы

    private float workHoursPerMonth; //кол-во рабочих часов
    private double allHoursWorked; //отработанные часы - для отчета
    private float amountHoursOneInstructions; //кол-во часов на выполнение одного задания
    private Map<Position, APosition> listPositions; //список должностей

    private List<String> taskList; //список распоряжений к выполнению
    private float workHours; //внутренний счетчик рабочих часов


    public Person(String name) {
        this.personName = name;
        this.isBusy = false;
        this.isTask = false;
        this.stopWork = false;
        this.taskList = new ArrayList<>();
    }

    /**
     * Метод запускает выполнение распоряжения
     *
     * @param task распоряжение
     */
    public void performTask(Position position, String task) {
        this.taskList.add(task);
        this.isTask = true;
        allHoursWorked += amountHoursOneInstructions;
        APosition aPosition = listPositions.get(position);
        //если должность с почасовой оплатой передаем время на выполнение задания
        if (aPosition instanceof Сontractor) {
            ((Сontractor) aPosition).setAmountHoursOneInstructions(amountHoursOneInstructions);
        } else if (position != Position.Director)
            aPosition.addAllHoursWorked(amountHoursOneInstructions);

        aPosition.getToWork(); //выполнить работу
    }

    @Override
    public void run() {
        workHours = workHoursPerMonth; //инициализируем счетчик рабочих часов
        while (workHours > 0) {
            while (!isTask && !stopWork) Thread.yield(); //ждем получения задания
            if (isTask) { //если есть задание
                this.isBusy = true; //сотрудник занят
                try {
                    Thread.sleep((long) amountHoursOneInstructions); //время выполнения задания
                } catch (InterruptedException e) {
                    System.out.println("Во время выполнения задания произошла ошибка!" + e.getMessage());
                }
                workHours -= amountHoursOneInstructions; //высчитываем отработанные часы
                isBusy = false; //сотрудник освободился
                isTask = false; //задание выполнено
            } else if (stopWork) break;
        }
    }

    /**
     * Метод, проверяющий занят ли сотрудник выполнением распоряжения
     *
     * @return возвращает true если сотрудник занят
     */
    public boolean isBusy() {
        return isBusy;
    }

    /**
     * Метод проверяет, что количество времени необходимое на выполнение распоряжения
     * не превышает количество оставшихся рабочих часов
     *
     * @return готов ли сотрудник выполнить распоряжение
     */
    public boolean isWork() {
        return amountHoursOneInstructions <= workHours;
    }

    /**
     * Метод считает заработанную сумму со всех должностей
     *
     * @return salary сумма зарплаты
     */
    public double paySalary() {
        double salary = 0;
        for (Map.Entry<Position, APosition> positionEntry : listPositions.entrySet())
            salary += positionEntry.getValue().paySalary();
        return salary;
    }


    public void setStopWork(boolean stopWork) {
        this.stopWork = stopWork;
    }

    public String getPersonName() {
        return personName;
    }

    public void setWorkHoursPerMonth(float workHoursPerDay) {
        this.workHoursPerMonth = workHoursPerDay;
    }

    public float getWorkHoursPerMonth() {
        return workHoursPerMonth;
    }

    public void setAmountHoursOneInstructions(float amountHoursOneInstructions) {
        this.amountHoursOneInstructions = amountHoursOneInstructions;
    }

    public float getAmountHoursOneInstructions() {
        return amountHoursOneInstructions;
    }

    public void setListPositions(Map<Position, APosition> listPositions) {
        this.listPositions = listPositions;
    }

    public Map<Position, APosition> getListPositions() {
        return listPositions;
    }

    public List<String> getTaskList() {
        return taskList;
    }

    public boolean isStopWork() {
        return stopWork;
    }

    public double getAllHoursWorked() {
        return allHoursWorked;
    }

    /**
     * Метод equals переопределен для корректной работы списка сотрудников
     * где ключом является экземпляр класса Person
     *
     * @param o объект для сравнения
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (Float.compare(person.workHoursPerMonth, workHoursPerMonth) != 0) return false;
        return !(personName != null ? !personName.equals(person.personName) : person.personName != null);
    }

    /**
     * Метод hashCode переопределен для корректной работы списка сотрудников
     * где ключом является экземпляр класса Person
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        int result = personName != null ? personName.hashCode() : 0;
        result = 31 * result + (workHoursPerMonth != +0.0f ? Float.floatToIntBits(workHoursPerMonth) : 0);
        return result;
    }

}
