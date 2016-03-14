package workflowOffice.model;

/**
 * Интерфейс сотрудников с фиксированной ставкой
 */
public interface Employee {

    /**
     * Метод, устанавливающий фиксированную ставку
     *
     * @param fixedRate ставка
     */
    void setFixedRate(int fixedRate);
    
}
