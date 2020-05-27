package com.oneso.cells;

import com.oneso.money.Money;

public interface Cell {

  Money getType();

  void putMoney();

  int pickMoney();

  int getMoney();

  int getCount();
}
