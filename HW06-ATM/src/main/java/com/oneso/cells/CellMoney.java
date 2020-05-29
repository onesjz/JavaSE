package com.oneso.cells;

import com.oneso.money.Money;

public class CellMoney implements Cell {

  private final Money money;
  private int count;

  public CellMoney(Money money) {
    this.money = money;
  }

  @Override
  public Money getType() {
    return money;
  }

  @Override
  public void putMoney() {
    count++;
  }

  @Override
  public int pickMoney() {
    if (count == 0) {
      return 0;
    }
    return count--;
  }

  @Override
  public int getMoney() {
    return money.getValue() * count;
  }

  @Override
  public int getCount() {
    return count;
  }
}
