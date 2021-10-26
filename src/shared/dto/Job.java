package shared.dto;

import java.io.Serializable;

public class Job implements Serializable {

    public Job() {
    }

    public Job(String fileName, String printer) {
        this.fileName = fileName;
        this.printer = printer;
    }

    private Integer jobID;
    private String fileName;
    private String printer;

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    @Override
    public String toString() {
        return "[" + jobID + "]" + " [printer = " + printer +" ]" + " [filename = " + fileName +" ]";
    }
}
