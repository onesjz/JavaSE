package com.oneso;

import com.oneso.cells.Cell;
import com.oneso.control.Manager;
import com.oneso.money.Money;
import com.oneso.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ATM {

  private static final Logger log = LogManager.getLogger(ATM.class);

  private final Manager manager;

  public ATM(Manager manager) {
    this.manager = manager;
  }

  public void getMoney(int count) {
    int pickCount = Util.pickValue(count, manager);
    log.debug("Pick ${}", pickCount);
  }

  public void addMoney(Money money) {
    manager.updateCell(money, Cell::putMoney);
    log.debug("Put ${}", money.getValue());
  }

  public void getCount() {
    log.debug("Count: {}", manager.getCells().stream().map(Cell::getMoney).reduce(0, Integer::sum));
  }
}
