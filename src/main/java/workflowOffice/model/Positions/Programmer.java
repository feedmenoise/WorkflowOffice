package workflowOffice.model.Positions;

import workflowOffice.main.Company;
import workflowOffice.model.Сontractor;

/**
 * Класс должности Программист
 */
public class Programmer extends APosition implements Сontractor {
    private int hourlyRate; //почасовая ставка
    private double hoursWorked; //отработанные часы - для почасовой оплаты
    private double allHoursWorked; //отработанные часы - для отчета
    private double salary; //зарплата
    private float amountHoursOneInstructions; //кол-во часов на выполнение одного задания
    private int countTasks = 0; //счетчик выполненных заданий
    private double calcDoubleSalary; //для счета зарплаты

    public Programmer(String name) {
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
        if (hoursWorked > Company.MAX_WORKING_HOURS_PER_WEEK) {
            calcDoubleSalary = ((hoursWorked-Company.MAX_WORKING_HOURS_PER_WEEK)*hourlyRate)*2 + ((hoursWorked - (hoursWorked-Company.MAX_WORKING_HOURS_PER_WEEK))*hourlyRate);
        }
        else
            calcDoubleSalary = hourlyRate * hoursWorked;
        return calcDoubleSalary;
    }

    /**
     * Метод для получения зарплаты
     *
     * @return сумму зарплаты
     */
    @Override
    public double paySalary() {
        salary += calcSalary();
        double weekSalary = calcSalary();
        hoursWorked = 0;
        return weekSalary;
    }

    @Override
    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double getAllHoursWorked() {
        return allHoursWorked;
    }

    @Override
    public void setAmountHoursOneInstructions(float amountHoursOneInstructions) {
        this.amountHoursOneInstructions = amountHoursOneInstructions;
    }

    @Override
    public void addAllHoursWorked(double allHoursWorked) {
        this.allHoursWorked = allHoursWorked;
    }

    public int getCountTasks() {
        return countTasks;
    }

    public double getSalary() {
        return salary;
    }
}
