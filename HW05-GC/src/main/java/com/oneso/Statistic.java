package com.oneso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для ведения статистики
 */
public class Statistic {

  private String name;

  private long timePauseOld;
  private long timePauseYoung;
  private long spentTime;
  private boolean sleep;

  private long youngGc;
  private long oldGc;

  void setName(String name) {

    if(name.equals("G1 Young Generation") || name.equals("G1 Old Generation")) {
      this.name = "G1";
    }

    if(name.equals("PS MarkSweep") || name.equals("PS Scavenge")) {
      this.name = "Parallel Collector";
    }

    if(name.equals("Copy") || name.equals("MarkSweepCompact")) {
      this.name = "Serial Collector";
    }
  }

  void setTimePauseOld(long timePauseOld) {
    this.timePauseOld += timePauseOld;
  }

  void setTimePauseYoung(long timePauseYoung) {
    this.timePauseYoung += timePauseYoung;
  }

  void setSpentTime(long spentTime) {
    this.spentTime = spentTime;
  }

  void setSleep(boolean sleep) {
    this.sleep = sleep;
  }

  void addYoung() {
    this.youngGc += 1;
  }

  void addOld() {
    this.oldGc += 1;
  }

  void saveFileLog() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM_HH:mm:ss");
    File file = new File("./HW05-GC/logs" + File.separator + "GC_" + simpleDateFormat.format(new Date()) + "_report.log");

    try (FileOutputStream outputStream = new FileOutputStream(file, true);
         OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

      writer.write(this.toString());
    } catch (IOException e) {
      System.out.println("Не удалось записать файл");
    }
  }

  @SuppressWarnings("StringBufferReplaceableByString")
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("GC Name: ").append(name).append("\n");
    builder.append("Count Young: ").append(youngGc).append("\n");
    builder.append("Count Old: ").append(oldGc).append("\n");
    builder.append("Use Sleep: ").append(sleep).append("\n");
    builder.append("Average Time Pause Old: ").append(timePauseOld / oldGc).append("\n");
    builder.append("Average Time Pause Young: ").append(timePauseYoung / youngGc).append("\n");
    builder.append("Spent Time: ").append(spentTime).append("\n");

    return builder.toString();
  }
}
