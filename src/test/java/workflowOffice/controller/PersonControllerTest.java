package workflowOffice.controller;

import workflowOffice.model.Person;
import workflowOffice.model.Position;
import workflowOffice.model.Positions.APosition;
import workflowOffice.model.Positions.Freelancer;
import workflowOffice.main.Company;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Тестирует работу контроллера класса Person
 */
public class PersonControllerTest {

    /**
     * Тест проверяет наличие списка сотрудников после запуска
     * класса контроллера
     */
    @Test
    public void testRunPersonController() {
        PersonController.INSTANCE.runPersonController();
        assertNotNull(PersonController.INSTANCE.getPersonList());
        assertNotNull(PersonController.INSTANCE.getFreelancers());
    }

    /**
     * Тест проверяет корректно ли задается список обязательных должностей
     */
    @Test
    public void testSetNecessaryPositions() {
        Set<Position> positions = new HashSet<>();
        positions.add(Position.Accountant);
        positions.add(Position.Designer);
        PersonController.INSTANCE.setNecessaryPositions(positions);

        assertNotNull(PersonController.INSTANCE.getNecessaryPositions());
    }

    /**
     * Тест проверяет корректное установление случайных должностей
     */
    @Test
    public void testSetRandomPositions() {
        assertNotNull(PersonController.INSTANCE.setRandomPositions());
    }

    /**
     * Тест проверяет корректное создание сотрудника
     */
    @Test
    public void testCreatePerson() {
        assertNotNull(PersonController.INSTANCE.createPerson("TestPerson"));
    }

    /**
     * Тест проверяет корректное редактирование списка сотрудников
     */
    @Test
    public void testListPersonsAfterEditingPersonPositions() {
        Map<Person, Set<Position>> testList = new HashMap<>();
        Set<Position> positions = new HashSet<>();
        positions.add(Position.Accountant);
        positions.add(Position.Designer);

        testList.put(PersonController.INSTANCE.createPerson("TestPerson"), positions);

        int testListCount = testList.size();
        PersonController.INSTANCE.editingPersonPositions(testList, Position.Director);
        int testListCountAfterEditing = testList.size();

        assertEquals(testListCount + 1, testListCountAfterEditing);
    }

    /**
     * Тест проверяет корректное создание списка сотрудников
     */
    @Test
    public void testCreateRandomPerson() {
        assertNotNull(PersonController.INSTANCE.createRandomPerson());
    }

    /**
     * Тест проверяет корректность работы метода isContainsPosition()
     */
    @Test
    public void testIsContainsPosition() {
        Map<Person, Set<Position>> testList = PersonController.INSTANCE.createRandomPerson();
        Person testPerson = PersonController.INSTANCE.createPerson("TestPerson");
        Set<Position> positionList = new HashSet<>();
        positionList.add(Position.Director);
        testList.put(testPerson, positionList); //добавляем в список сотрудника с определенной должностью

        assertTrue(PersonController.INSTANCE.isContainsPosition(testList, Position.Director));
    }

    /**
     * Тест проверяет корректность создания списка с экземплярами должностей
     */
    @Test
    public void testCreatePositionsForPerson() {
        Set<Position> positionList = new HashSet<>();
        positionList.add(Position.Designer);
        positionList.add(Position.Accountant);
        positionList.add(Position.Manager);
        positionList.add(Position.Programmer);
        positionList.add(Position.Tester);

        Map<Position, APosition> positionMap = PersonController.INSTANCE.createPositionsForPerson(positionList);

        for (Map.Entry<Position, APosition> positionEntry : positionMap.entrySet()) {
            String positionName = positionEntry.getKey().toString();
            String positionClassName = positionEntry.getValue().getNamePositions();

            assertEquals(positionName, positionClassName);
        }
    }

    /**
     * Тест проверяет создание списка с экземплярами должностей после создания сотрудника
     */
    @Test
    public void testCreateRandomPersonWithCreatePositionsForPerson() {
        Map<Person, Set<Position>> testList = PersonController.INSTANCE.createRandomPerson();
        for (Map.Entry<Person, Set<Position>> person : testList.entrySet()) {

            assertNotNull(person.getKey().getListPositions());
        }
    }

    /**
     * Тест проверяет корректность выбора случайного бухгалтера
     */
    @Test
    public void testSelectionRandomAccountant() {
        Map<Person, Set<Position>> testList = new HashMap<>();
        Set<Position> positions1 = new HashSet<>();
        positions1.add(Position.Accountant);

        testList.put(PersonController.INSTANCE.createPerson("Accountant"), positions1);

        Set<Position> positions2 = new HashSet<>();
        positions2.add(Position.Accountant);
        testList.put(PersonController.INSTANCE.createPerson("Accountant"), positions2);

        Set<Position> positions3 = new HashSet<>();
        positions3.add(Position.Designer);
        testList.put(PersonController.INSTANCE.createPerson("Designer"), positions3);

        Person accountant = PersonController.INSTANCE.selectionRandomAccountant(testList);

        assertEquals(Position.Accountant.toString(), accountant.getPersonName());
    }

    /**
     * Тест проверяет кооректное осздание и добавления фрилансера
     */
    @Test
    public void testCreateNewFreelancer() {
        PersonController.INSTANCE.runPersonController();
        for (int i = 1; i <= 3; i++) {
            Freelancer freelancer = PersonController.INSTANCE.createNewFreelancer();
            assertEquals("Freelancer №" + i, freelancer.getNamePositions());
        }
        assertEquals(3, PersonController.INSTANCE.getFreelancers().size());
    }

    /**
     * Тест проверяет корректность установки счетчика директоров
     */
    @Test
    public void testGetCountDirectorsPositions() {
        PersonController.INSTANCE.setCountDirectorsPositions(Company.MAX_AMOUNT_DIRECTORS_POSITIONS);
        int countDirectors = PersonController.INSTANCE.getCountDirectorsPositions();

        assertEquals(Company.MAX_AMOUNT_DIRECTORS_POSITIONS, countDirectors);
    }
}