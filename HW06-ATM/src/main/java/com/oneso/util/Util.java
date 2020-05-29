package com.oneso.util;

import com.oneso.cells.Cell;
import com.oneso.control.Manager;

public class Util {

  private Util() {
  }

  public static int pickValue(int count, Manager manager) {
    int currentCount = count;

    for (Cell temp : manager.getCells()) {
      currentCount = pick(temp, currentCount);
    }

    if (currentCount == 0) {
      return count;
    } else {
      return count - currentCount;
    }
  }

  private static int pick(Cell cell, int count) {
    if (count == 0) {
      return 0;
    }

    int value = cell.getType().getValue();
    int newCount = 0;
    int getCount = 0;

    if (value <= count && cell.getCount() != 0) {
      newCount = count - value;
      getCount += value;
      cell.pickMoney();
      newCount = pick(cell, newCount);

    }
    if (getCount == 0) {
      return count;
    } else {
      return newCount;
    }
  }
}
