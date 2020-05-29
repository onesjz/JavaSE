package com.oneso.money;

public enum Money {

  FIVE(5),
  TEN(10),
  ONE_HUNDRED(100),
  FIVE_HUNDRED(500);

  private final int value;

  Money(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
