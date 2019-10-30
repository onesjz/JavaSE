package com.oneso.mBeans;

import java.util.LinkedList;
import java.util.List;

/**
 * Простой класс для нагрузки GC
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "InfiniteLoopStatement"})
public class Benchmark implements BenchmarkMBean {

  private volatile int size = 0;

  public void run(boolean sleep) throws OutOfMemoryError {
    List<String> array = new LinkedList<>();

    try {
      while (true) {
        int local = size;
        for (int i = 0; i < local; i++) {
          array.add("");

          if (i != 0 && i % 2 == 0) {
            array.remove(0);
          }
          // Смотрим на график ( закомментить 23-25 )
          //array.remove(0);
        }
        if (sleep) {
          Thread.sleep(5000);
        }
      }
    } catch (Throwable ignored) {}
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }
}
