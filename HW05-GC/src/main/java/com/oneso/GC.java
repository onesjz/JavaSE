package com.oneso;

import com.oneso.mBeans.Benchmark;
import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

class GC {

  void start(int size, boolean sleep) throws Exception {
    Statistic statistic = new Statistic();
    statistic.setSleep(sleep);
    System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
    switchOnMonitoring(statistic);
    long beginTime = System.currentTimeMillis();

    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = new ObjectName("com.oneso.mBeans:type=Benchmark");

    Benchmark benchmark = new Benchmark();
    mbs.registerMBean(benchmark, name);
    benchmark.setSize(size);

    benchmark.run(sleep);
    long endTime = (System.currentTimeMillis() - beginTime) / 1000;
    statistic.setSpentTime(endTime);
    statistic.saveFileLog();
    System.out.println(statistic);
  }

  private void switchOnMonitoring(Statistic statistic) {
    List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
    for (GarbageCollectorMXBean gcbean : gcbeans) {
      System.out.println("GC Name: " + gcbean.getName());
      statistic.setName(gcbean.getName());
      NotificationEmitter emitter = (NotificationEmitter) gcbean;
      NotificationListener listener = ((notification, handback) -> {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
          GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
          String gcName = info.getGcName();
          String gcAction = info.getGcAction();
          String gcCause = info.getGcCause();

          long startTime = info.getGcInfo().getStartTime();
          long duration = info.getGcInfo().getDuration();

          if (gcAction.contains("minor")) {
            statistic.addYoung();
            statistic.setTimePauseYoung(duration);
          } else if (gcAction.contains("major")) {
            statistic.addOld();
            statistic.setTimePauseOld(duration);
          }

          System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
        }
      });
      emitter.addNotificationListener(listener, null, null);
    }
  }
}
