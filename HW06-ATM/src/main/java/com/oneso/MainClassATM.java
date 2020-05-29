package com.oneso;

import com.oneso.control.ManagerCells;
import com.oneso.money.Money;

public class MainClassATM {

  public static void main(String[] args) {

    var atm = new ATM(new ManagerCells());

    // Вносим деньги
    add(atm, Money.TEN, 10);
    add(atm, Money.FIVE, 5);
    add(atm, Money.ONE_HUNDRED, 10);
    add(atm, Money.FIVE_HUNDRED, 5);
    // Проверяем количество
    atm.getCount();

    // Снимаем деньги
    atm.getMoney(1500);

    // Проверяем количество
    atm.getCount();
  }

  public static void add(ATM atm, Money money, int count) {
    for (int i = 0; i < count; i++) {
      atm.addMoney(money);
    }
  }
}
