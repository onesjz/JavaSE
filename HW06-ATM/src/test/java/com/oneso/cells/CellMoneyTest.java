package com.oneso.cells;

import com.oneso.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cell money should")
class CellMoneyTest {

    Cell cell;

    @BeforeEach
    void setUp() {
        cell = new CellMoney(Money.ONE_HUNDRED);
    }

    @Test
    @DisplayName("Should get type")
    void getType() {
        assertEquals(Money.ONE_HUNDRED, cell.getType());
    }

    @Test
    @DisplayName("Should add money")
    void putMoney() {
        cell.putMoney();
        assertEquals(1, cell.getCount());
    }

    @Test
    @DisplayName("Should delete money")
    void pickMoney() {
        cell.putMoney();
        int expected = cell.getCount();
        cell.pickMoney();

        assertNotEquals(expected, cell.getCount());
    }

    @Test
    @DisplayName("Should get sum")
    void getMoney() {
        cell.putMoney();
        assertEquals(Money.ONE_HUNDRED.getValue(), cell.getMoney());
    }

    @Test
    @DisplayName("Should get count")
    void getCount() {
        assertEquals(0, cell.getCount());
        cell.putMoney();
        assertEquals(1, cell.getCount());
    }
}