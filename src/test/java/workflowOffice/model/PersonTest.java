package workflowOffice.model;

import workflowOffice.controller.PersonController;
import workflowOffice.model.Positions.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Тестирует работу класса Person
 */
public class PersonTest {
    private static Person person;

    /**
     * Метод инициализирующий объект класса Person
     * вызывается перед тестами
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        person = new Person("TestPerson");
    }

    /**
     * Тест проверяет корректно ли задается имя person при создании
     */
    @Test
    public void testGetPersonName() {
        assertEquals("TestPerson", person.getPersonName());
    }

    /**
     * Тест проверяет, что сотрудник сразу после создания не занят
     */
    @Test
    public void testIsBusy() {
        person = new Person("TestPerson");
        assertFalse(person.isBusy());
    }

    /**
     * Тест проверяет корректно ли задается кол-во рабочих часов
     */
    @Test
    public void testSetWorkHoursPerDay() {
        float hours = 20.1f;
        person.setWorkHoursPerMonth(hours);
        assertEquals(hours, person.getWorkHoursPerMonth(), 0.0f);
    }

    /**
     * Тест проверяет корректно ли задается кол-во времени на выполнение одного задания
     */
    @Test
    public void testSetAmountHoursOneInstructions() {
        float hours = 1.1f;
        person.setAmountHoursOneInstructions(hours);
        assertEquals(hours, person.getAmountHoursOneInstructions(), 0.0f);
    }

    /**
     * Тест проверяет корректное сохранение распоряжения в списке заданий
     */
    @Test
    public void testGetTaskList(){
        person = PersonController.INSTANCE.createPerson("TestPerson");
        Map<Position, APosition> listPositions = new HashMap<>();
        listPositions.put(Position.Tester, new Tester("Tester"));
        person.setListPositions(listPositions);
        String task = "task for Tester";
        person.performTask(Position.Tester, task);

        assertEquals(task, person.getTaskList().get(0));
    }

    /**
     * Тест проверяет корректный подсчет зарплаты для сотрудника с фиксированной ставкой
     */
    @Test
    public void testPaySalaryEmployee() {
        person = PersonController.INSTANCE.createPerson("TestPerson");
        Map<Position, APosition> listPositions = new HashMap<>();
        String task = "task for Manager";
        int fixedRate = 4000;
        final Manager manager = new Manager("Manager");
        manager.setFixedRate(fixedRate);

        listPositions.put(Position.Manager, manager);
        person.setListPositions(listPositions);
        person.performTask(Position.Manager, task);

        assertEquals(fixedRate, person.paySalary(), 0.0f);
    }

    /**
     * Тест проверяет корректный подсчет зарплаты для сотрудника с почасовой оплатой
     */
    @Test
    public void testPaySalaryContractor() {
        person = PersonController.INSTANCE.createPerson("TestPerson");
        Map<Position, APosition> listPositions = new HashMap<>();
        String task = "task for Programmer";
        int hourlyRate = 25;
        final Programmer programmer = new Programmer("Programmer");
        programmer.setHourlyRate(hourlyRate);
        person.setAmountHoursOneInstructions(2);

        listPositions.put(Position.Programmer, programmer);
        person.setListPositions(listPositions);
        person.performTask(Position.Programmer, task);

        assertEquals(hourlyRate * 2, person.paySalary(), 0.0f);
    }

    /**
     * Тест проверяет корректность работы метода isWork
     * (Данный метод возвращает true если сотрудник готов к работе)
     * @throws Exception
     */
    @Test
    public void testIsWork() throws Exception {
        person = PersonController.INSTANCE.createPerson("TestPerson");
        person.setAmountHoursOneInstructions(2);
        person.setWorkHoursPerMonth(50);
        person.start();
        Thread.sleep(50);
        person.setStopWork(true);

        assertTrue(person.isWork());
    }

    /**
     * Тест проверяет работу метода setStopWork
     */
    @Test
    public void testSetStopWork() throws Exception {
        person = PersonController.INSTANCE.createPerson("TestPerson");
        person.setStopWork(true);

        assertTrue(person.isStopWork());
    }

    /**
     * Тест проверяет корркетную работу класса со списокм должностей
     */
    @Test
    public void testGetListPositions() {
        person = PersonController.INSTANCE.createPerson("TestPerson");
        Map<Position, APosition> listPositions = new HashMap<>();
        listPositions.put(Position.Programmer, new Designer("Designer"));
        person.setListPositions(listPositions);

        assertEquals(1, person.getListPositions().size());
    }

    /**
     * Тест проверяет корректность работы переопределенного метода hashCode
     */
    @Test
    public void testHashCode() {
        Person person1 = PersonController.INSTANCE.createPerson("TestPerson1");
        person1.setWorkHoursPerMonth(50);

        Person person2 = PersonController.INSTANCE.createPerson("TestPerson2");
        person2.setWorkHoursPerMonth(50);

        assertNotSame(person1.hashCode(), person2.hashCode());
    }

    /**
     * Тест проверяет корректность работы переопределенного метода equals
     */
    @Test
    public void testEquals() {
        Person person1 = PersonController.INSTANCE.createPerson("TestPerson1");
        person1.setWorkHoursPerMonth(50);

        Person person2 = PersonController.INSTANCE.createPerson("TestPerson2");
        person2.setWorkHoursPerMonth(50);

        assertFalse(person1.equals(person2));
    }
}