package workflowOffice.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс проверяет enum класс Positions
 */
public class PositionTest {

    @Test
    public void testEnumPositions() {
        assertTrue(Position.values().length > 0);
    }
}