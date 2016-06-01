package hello.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
  private final static int NUM_CONSUMERS = 5;
  
  private static List<Consumer> consumers;
  private static LinkedList<Runnable> bufferedList;
  private int length;
  
  private ThreadPool(int numBuffered) {
    bufferedList = new LinkedList<>();
    length = numBuffered;
    consumers = new ArrayList<>();
    for (int i = 0; i < NUM_CONSUMERS; i++) {
      consumers.add(new Consumer(bufferedList));
    }
    for (Consumer consumer : consumers) {
      consumer.start();
    }
  }
  
  public static ThreadPool threadPoolFactory(int numBuffered) {
    return new ThreadPool(numBuffered);
  }
  
  public void execute(Runnable run) {
    synchronized (bufferedList) {
      if (!isFull()) {
        System.out.println("Pool is not full");
        bufferedList.add(run);
      } else {
        try {
          System.out.println("It's full and waiting.");
          bufferedList.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        bufferedList.add(run);
      }
      bufferedList.notifyAll();
    }
  }
  
  private boolean isFull() {
    return length == bufferedList.size();
  }
}

class Consumer extends Thread {
  private Runnable executor;
  private LinkedList<Runnable> list;
  
  public Consumer(LinkedList<Runnable> bufferedList) {
    list = bufferedList;
  }
  
  @Override
  public void run() {
    while (true) {
      synchronized (list) {
        while (list.isEmpty()) {
          try {
            list.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        executor = list.removeFirst();
        executor.run();
        System.out.println(Thread.currentThread() + " is running and release list");
        list.notifyAll();
      }
    }
  }
}
