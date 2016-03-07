package workflowOffice.model.Positions;

import workflowOffice.model.Сontractor;

/**
 * Класс Фрилансер (удаленный сотрудник)
 */
public class Freelancer extends APosition implements Сontractor {
    private int hourlyRate; //почасовая ставка
    private double hoursWorked; //отработанные часы - для почасовой оплаты
    private double allHoursWorked; //отработанные часы - для отчета
    private double salary; //зарплата
    private float amountHoursOneInstructions; //кол-во часов на выполнение одного задания
    private int countTasks = 0; //счетчик выполненных заданий

    public Freelancer(String name) {
        super(name);
        hoursWorked = 0;
    }

    /**
     * Метод в котором выполняется работа должности
     */
    @Override
    public void getToWork() {
        hoursWorked += amountHoursOneInstructions; //считаем отработанное время
        allHoursWorked += amountHoursOneInstructions;
        countTasks++;
    }

    /**
     * Метод расчитывающий сумму зарплаты исходя из количества отработанных часов
     * и почасовой ставки
     *
     * @return salary
     */
    @Override
    public double calcSalary() {
        return hourlyRate * hoursWorked;
    }

    /**
     * Метод для получения зарплаты
     *
     * @return сумму зарплаты
     */
    @Override
    public double paySalary() {
        salary += calcSalary();
        double taskSalary = calcSalary();
        hoursWorked = 0;
        return taskSalary;
    }

    @Override
    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public void setAmountHoursOneInstructions(float amountHoursOneInstructions) {
        this.amountHoursOneInstructions = amountHoursOneInstructions;
    }

    public int getCountTasks() {
        return countTasks;
    }

    @Override
    public double getAllHoursWorked() {
        return allHoursWorked;
    }

    @Override
    public void addAllHoursWorked(double allHoursWorked) {
        this.allHoursWorked += allHoursWorked;
    }

    public double getSalary() {
        return salary;
    }

}
