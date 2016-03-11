package workflowOffice.main;

import workflowOffice.controller.WorkController;
import workflowOffice.model.Position;
import java.security.SecureRandom;

/**
 * Класс контроллирует работу компании
 */
public class Company {
    public static final SecureRandom random = new SecureRandom();

    public static final int MAX_WORKING_HOURS = 8; //кол-во рабочих часов в день (от 1 до 8)
    public static final int MIN_WORKING_HOURS = 1;

    public static final int MAX_WORKING_HOURS_PER_WEEK = 40; //кол-во рабочих часов в неделю

    public static final int MAX_WORKING_HOURS_PER_MONTH = 160; //кол-во рабочих часов в месяц (от 20 до 160)
    public static final int MIN_WORKING_HOURS_PER_MONTH = 20;

    public static final int MAX_AMOUNT_POSITIONS = Position.values().length; //кол-во должностей (от 1 до 7)
    public static final int MIN_AMOUNT_POSITIONS = 1;

    public static final int MAX_AMOUNT_PERSONS = 90; //кол-во сотрудников (от 10 до 100)
    public static final int MIN_AMOUNT_PERSONS = 10;
    public static final int MAX_AMOUNT_DIRECTORS_POSITIONS = 5; //максимальное кол-во директоров

    public static final int MAX_WORKING_DAYS = 5; //максимальное кол-во рабочих дней в неделю
    public static final int MAX_WORKING_WEEKS = 4; //максимальное кол-во рабочих недель в месяц

    public static final int MAX_HOURLY_RATE = 100; //почасовая ставка (от 25 до 100)
    public static final int MIN_HOURLY_RATE = 25;

    public static final int MAX_FIXED_RATE = 4000; //фиксированная ставка (от 1000 до 4000)
    public static final int MIN_FIXED_RATE = 1000;

    public static final int ONE_HOUR = 1; //один час

    public static void main(String[] args) {
        WorkController.INSTANCE.runWorkController(); //запускаем работу
    }
}
