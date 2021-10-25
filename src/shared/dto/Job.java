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

}
