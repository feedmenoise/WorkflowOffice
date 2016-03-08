package workflowOffice.model.Positions;

import workflowOffice.model.Employee;
import workflowOffice.model.Person;
import workflowOffice.model.Position;

import java.util.Map;
import java.util.Set;

/**
 * Класс должности Бухгалтер
 */
public class Accountant extends APosition implements Employee {
    private int fixedRate; //фиксированная ставка
    private double allSalary = 0; //всего выплачено сотрудникам
    private double allHoursWorked; //отработанные часы - для отчета
    private double salary; //зарплата
    private double allSalaryFreelancers = 0; //всего выплачено фрилансерам
    private int countTasks = 0; //счетчик выполненных заданий

    public Accountant(String name) {
        super(name);
    }

    /**
     * Метод в котором выполняется работа должности
     */
    @Override
    public void getToWork() {
        //выполняет свою работу
        countTasks++;
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

    /**
     * Начисление зарплаты всем сотрудникам компании
     */
    public void payWeekSalary(Map<Person, Set<Position>> personsList) {
        for (Map.Entry<Person, Set<Position>> person : personsList.entrySet())
            allSalary += person.getKey().paySalary();
    }

    /**
     * Начисление зарплаты фрилансерам
     */
    public void payDaySalary(Set<Freelancer> freelancersList) {
        for (Freelancer freelancer : freelancersList) {
            allSalaryFreelancers += freelancer.paySalary();
        }
    }


    public double getAllSalary() {
        return allSalary;
    }

    public double getAllSalaryFreelancers() {
        return allSalaryFreelancers;
    }

    public double getSalary() {
        return salary;
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

    @Override
    public void setFixedRate(int fixedRate) {
        this.fixedRate = fixedRate;
    }

    @Override
    public int getFixedRate() {
        return fixedRate;
    }
}
