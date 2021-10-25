package server.printer;

import shared.dto.Job;

import java.util.HashMap;
import java.util.Queue;

public interface IPrinter {
    boolean haveWork();
    void addJob(Job job);
    String getName();
    Queue<Job> getQueue();
}
