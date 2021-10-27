package server.printer;

import shared.dto.Job;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class Printer implements Runnable, IPrinter {

    private Queue<Job> queue = null;
    private String name = null;

    private String status = "idle";

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

    public void moveOnTop(int jobID){
        List<Job> jobs = queue.stream().collect(Collectors.toList());

        Job theJobToMove = jobs.stream()
                .filter(job -> job.getJobID() == jobID)
                .findAny()
                .orElse(null);

        jobs.remove(theJobToMove);
        jobs.add(0, theJobToMove);

        queue.clear();
        queue.addAll(jobs);
    }

    public void reset(){
        queue.clear();
    }

    private void printCurrentJob(){
        // do the printing?
        try {
            Job currentJob = queue.peek();
            status = "printing job: " + currentJob.getFileName();
            Thread.sleep(5000);
            queue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return status;
    }

    public synchronized boolean haveWork(){
        return !queue.isEmpty();
    }

    @Override
    public void addJob(Job job){
        queue.add(job);
    }
}
