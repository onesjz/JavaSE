package com.oneso.control;

import com.oneso.cells.Cell;
import com.oneso.cells.CellMoney;
import com.oneso.money.Money;

import java.util.*;
import java.util.function.Consumer;

public class ManagerCells implements Manager {

  private final List<Cell> cells = new ArrayList<>();

  @Override
  public void updateCell(Money money, Consumer<Cell> func) {

    Optional<Cell> cell = cells.stream().filter(c -> c.getType().equals(money)).findFirst();

    if (cell.isPresent()) {
      func.accept(cell.get());
    } else {
      func.accept(createCell(money));
    }
  }

  @Override
  public Cell getCell(Money money) {
    return cells.stream().filter(c -> c.getType().equals(money)).findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Cell> getCells() {
    return Collections.unmodifiableList(cells);
  }

  private Cell createCell(Money money) {

    var cellMoney = new CellMoney(money);
    cells.add(cellMoney);
    cells.sort((a, b) -> b.getType().getValue() - a.getType().getValue());

    return cellMoney;
  }
}
