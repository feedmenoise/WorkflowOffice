package workflowOffice.model.Positions;

/**
 * Абстрактный класс должностей
 */
public abstract class APosition {
    String namePositions; //имя должности

    public APosition(String name) {
        this.namePositions = name;
    }

    /**
     * Метод в котором выполняется работа должности
     */
    public abstract void getToWork();

    /**
     * Метод для получения зарплаты
     *
     * @return сумму зарплаты
     */
    public abstract double paySalary();

    public String getNamePositions() {
        return namePositions;
    }

    /**
     * Метод возвращает количество отработанных часов за все время
     *
     * @return кол-во часов
     */
    public abstract double getAllHoursWorked();

    /**
     * Метод добавляет отработанные часы
     *
     * @param allHoursWorked кол-во часов
     */
    public abstract void addAllHoursWorked(double allHoursWorked);
}
