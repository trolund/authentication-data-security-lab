package server.printer;

import shared.dto.Job;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Printer implements Runnable, IPrinter {

    private final Queue<Job> queue;
    private String status = "idle";
    private String name = "";


    public Printer(String name) {
        this.name = name;
        queue = new PriorityBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true){
            try {
                if(haveWork()){
                    printCurrentJob();
                }
                // moving paper...
                status = "idle";
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public Queue<Job> getQueue() {
        return queue;
    }

    private void printCurrentJob(){
        // do the printing?
        try {
            status = "printing......";
            Thread.sleep(5000);
            queue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean haveWork(){
        return !queue.isEmpty();
    }

    @Override
    public void addJob(Job job){
        queue.add(job);
    }
}
