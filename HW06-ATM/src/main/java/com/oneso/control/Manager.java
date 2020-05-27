package com.oneso.control;

import com.oneso.cells.Cell;
import com.oneso.money.Money;

import java.util.List;
import java.util.function.Consumer;

public interface Manager {

  void updateCell(Money money, Consumer<Cell> func);

  Cell getCell(Money money);

  List<Cell> getCells();
}
