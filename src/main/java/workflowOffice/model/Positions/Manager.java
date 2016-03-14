package workflowOffice.model.Positions;

import workflowOffice.model.Employee;

/**
 * Класс должности Менеджер
 */
public class Manager extends APosition implements Employee {
    private static int fixedRate; //фиксированная ставка
    private int countTasks = 0; //счетчик выполненных заданий
    private double salary; //зарплата
    private double allHoursWorked; //отработанные часы - для отчета

    public Manager(String name) {
        super(name);
    }

    /**
     * Метод в котором выполняется работа должности
     */
    @Override
    public void getToWork() {
        countTasks++;
        //выполняет свою работу
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
        this.allHoursWorked += allHoursWorked;
    }

    @Override
    public void setFixedRate(int fixedRate) {
        this.fixedRate = fixedRate;
    }

    public static int getFixedRate() {
        return fixedRate;
    }

    public int getCountTasks() {
        return countTasks;
    }

    public double getSalary() {
        return salary;
    }


}
