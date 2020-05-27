package com.oneso.control;

import com.oneso.cells.Cell;
import com.oneso.cells.CellMoney;
import com.oneso.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ManagerCells should")
class ManagerCellsTest {

    Manager manager;

    @BeforeEach
    void setUp() {
        manager = new ManagerCells();
    }

    @Test
    @DisplayName("Should update cell")
    void updateCell() {
        manager.updateCell(Money.ONE_HUNDRED, Cell::putMoney);
        Optional<Cell> actual = manager.getCells().stream().findFirst();

        assertTrue(actual.isPresent());
    }

    @Test
    @DisplayName("Should get cell")
    void getCell() {
        Money expected = new CellMoney(Money.ONE_HUNDRED).getType();
        manager.updateCell(Money.ONE_HUNDRED, Cell::putMoney);
        Money actual = manager.getCells().stream().findFirst().orElse(new CellMoney(Money.FIVE)).getType();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should get cells")
    void getCells() {
        manager.updateCell(Money.FIVE, Cell::putMoney);
        manager.updateCell(Money.ONE_HUNDRED, Cell::putMoney);

        assertNotEquals(0, manager.getCells().size());
    }
}