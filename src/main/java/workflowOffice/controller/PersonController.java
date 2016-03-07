package workflowOffice.controller;

import workflowOffice.model.Employee;
import workflowOffice.model.Person;
import workflowOffice.model.Position;
import workflowOffice.model.Positions.*;
import workflowOffice.model.Сontractor;
import workflowOffice.main.Company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * Класс контролирующий работу сотрудников компании
 */
public final class PersonController {
    public static final PersonController INSTANCE = new PersonController();
    private static final SecureRandom random = Company.random; // for random enums

    private Map<Person, Set<Position>> personList; //список сотрудников
    private Set<Position> necessaryPositions; //список обязательных должностей
    private Set<Freelancer> freelancers; //список фрилансеров
    private int countDirectorsPositions; //счетчик директоров

    /**
     * Доступ к контроллеру осуществляется через INSTANCE
     */
    private PersonController() {}

    /**
     * Метод запускающий работу контроллера
     */
    public void runPersonController() {
        personList = createRandomPerson(); //создаем список сотрудников
        freelancers = new HashSet<>(); //создаем список фрилансеров

        //проверка, что в фирме есть необходимые должности
        if (necessaryPositions != null)
            for (Position position : necessaryPositions)
                personList = editingPersonPositions(personList, position);
    }

    /**
     * Метод создает список сотрудников
     */
    protected Map<Person, Set<Position>> createRandomPerson() {
        Map<Person, Set<Position>> list = new HashMap<>();
        int countPersons = random.nextInt(Company.MAX_AMOUNT_PERSONS) + Company.MIN_AMOUNT_PERSONS;

        for (int i = 1; i <= countPersons; i++) {
            Person person = createPerson("Сотрудник №" + i);
            Set<Position> positionList = setRandomPositions();
            person.setListPositions(createPositionsForPerson(positionList)); //задаем список должностей

            list.put(person, positionList); //добавляем сотрудника в список
        }
        return list;
    }

    /**
     * Метод создает эекземпляры классов соответстующих должностей для сотрудника
     *
     * @param posList список должностей определенного сотрудника
     * @return список экземпляров с должностями
     */
    protected Map<Position, APosition> createPositionsForPerson(Set<Position> posList) {
        Map<Position, APosition> positionMap = new HashMap<>();
        for (Position pos : posList) {
            int countPos = pos.ordinal();
            switch (countPos) {
                case 0:
                    positionMap.put(pos, new Programmer("Programmer"));
                    break;
                case 1:
                    positionMap.put(pos, new Designer("Designer"));
                    break;
                case 2:
                    positionMap.put(pos, new Tester("Tester"));
                    break;
                case 3:
                    positionMap.put(pos, new Manager("Manager"));
                    break;
                case 4:
                    positionMap.put(pos, new Director("Director"));
                    break;
                case 5:
                    positionMap.put(pos, new Accountant("Accountant"));
                    break;
                default:
                    break;
            }
        }
        //устанавливаем размер зарплаты
        for (Map.Entry<Position, APosition> positionEntry : positionMap.entrySet()) {
            if (positionEntry.getValue() instanceof Сontractor)
                ((Сontractor) positionEntry.getValue())
                        .setHourlyRate(random.nextInt(Company.MAX_HOURLY_RATE) + Company.MIN_HOURLY_RATE);
            else if (positionEntry.getValue() instanceof Employee)
                ((Employee) positionEntry.getValue())
                        .setFixedRate(random.nextInt(Company.MAX_FIXED_RATE) + Company.MIN_FIXED_RATE);
        }
        return positionMap;
    }

    /**
     * Метод создает сотрудника и задает ему необходимые параметры
     * (время выполнения одного задания и ко-во рабочих часов в день)
     *
     * @param name имя сотрудника
     * @return экземпляр класса Person
     */
    public Person createPerson(String name) {
        Person person = new Person(name);
        //кол-во времени на выполнение одного задания
        person.setAmountHoursOneInstructions(new BigDecimal(random.nextFloat()
                + Company.MIN_WORKING_HOURS)
                .setScale(2, RoundingMode.UP)
                .floatValue());
        //кол-во рабочих часов в месяц
        person.setWorkHoursPerMonth(random.nextInt(Company.MAX_WORKING_HOURS_PER_MONTH)
                + Company.MIN_WORKING_HOURS_PER_MONTH);
        return person;
    }

    /**
     * Метод задает сотруднику список должностей случайным образом
     */
    protected Set<Position> setRandomPositions() {
        Set<Position> list = new HashSet<>();
        int amountPositions = random.nextInt(Company.MAX_AMOUNT_POSITIONS) + Company.MIN_AMOUNT_POSITIONS; //количество должностей

        for (int i = 0; i <= amountPositions; i++) {
            int x = random.nextInt(Position.values().length); //выбор случайной должности
            if (Position.values()[x] == Position.Director) { //если выпала должность директора
                //проверяем, что директора еще требуются
                if (countDirectorsPositions > 0) {
                    list.clear(); //удаляем все предыдущие должности
                    list.add(Position.values()[x]); //добавляем должность директора
                    countDirectorsPositions--;
                }
            } else list.add(Position.values()[x]); //добавление в список должность
        }
        return list;
    }

    /**
     * Метод добавляет сотрудника с необходимой должностью
     * в случае если такого нет в штате
     *
     * @param list     список сотрудников
     * @param position должность
     */
    protected Map<Person, Set<Position>> editingPersonPositions(Map<Person, Set<Position>> list, Position position) {
        if (!isContainsPosition(list, position)) {
            Set<Position> positionList = new HashSet<>();
            positionList.add(position);
            list.put(createPerson("Сотрудник №" + (list.size() + 1)), positionList);
        }
        return list;
    }

    /**
     * Метод проверяет наличие определенной должности у сотрудников
     *
     * @param list     список сотрудников
     * @param position должность
     */
    protected boolean isContainsPosition(Map<Person, Set<Position>> list, Position position) {
        for (Map.Entry<Person, Set<Position>> person : list.entrySet())
            if (person.getValue().contains(position)) return true;
        return false;
    }

    /**
     * Метод возвращает список сотрудников
     */
    public Map<Person, Set<Position>> getPersonList() {
        return personList;
    }

    /**
     * Метод задающий список обязательных должностей
     *
     * @param necessaryPositions список должностей
     */
    protected void setNecessaryPositions(Set<Position> necessaryPositions) {
        this.necessaryPositions = necessaryPositions;
    }

    /**
     * Метод возвращает список обязательных должностей
     *
     * @return список должностей
     */
    protected Set<Position> getNecessaryPositions() {
        return necessaryPositions;
    }

    /**
     * Метод выбирает из списка сотрудников случайного сотрудника с должностью бухгалтера
     *
     * @param personList список всех сотрудников
     * @return accountant бухгалтера
     */
    protected Person selectionRandomAccountant(Map<Person, Set<Position>> personList) {
        List<Person> listAccountant = new ArrayList<>();
        Person personAccountant;

        //выбираем всех бухгалтеров
        for (Map.Entry<Person, Set<Position>> person : personList.entrySet())
            if (person.getValue().contains(Position.Accountant)) listAccountant.add(person.getKey());
        //выбираем случайного бухгалтера
        personAccountant = listAccountant.get(random.nextInt(listAccountant.size()));

        return personAccountant;
    }

    /**
     * Метод нанимает нового фрилансера и добавляет его в список
     *
     * @return фрилансер
     */
    public Freelancer createNewFreelancer() {
        String name = String.valueOf("Freelancer №" + (freelancers.size() + 1));
        Freelancer freelancer = new Freelancer(name);
        //устанавливаем кол-во времени на выполнение одного задания
        freelancer.setAmountHoursOneInstructions(new BigDecimal(random.nextFloat() + Company.MIN_WORKING_HOURS)
                .setScale(2, RoundingMode.UP)
                .floatValue());
        //устанавливаем почасовую оплату
        freelancer.setHourlyRate(random.nextInt(Company.MAX_HOURLY_RATE) + Company.MIN_HOURLY_RATE);
        //добавляем в список фрилансеров
        freelancers.add(freelancer);

        return freelancer;
    }

    /**
     * Метод возвращает список фрилансеров
     *
     * @return список фрилансеров
     */
    public Set<Freelancer> getFreelancers() {
        return freelancers;
    }

    /**
     * Метод возвращает заданное кол-во директоров
     *
     * @return максимальное кол-во директоров
     */
    public int getCountDirectorsPositions() {
        return countDirectorsPositions;
    }

    /**
     * Метод устанавливает максимальное количество директоров
     *
     * @param countDirectorsPositions счетчик директоров
     */
    public void setCountDirectorsPositions(int countDirectorsPositions) {
        this.countDirectorsPositions = countDirectorsPositions;
    }
}
