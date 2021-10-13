package shared.dto;

import java.io.Serializable;

public class TopQueueParams implements Serializable {

    private String printer;
    private int job;

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }
}
