package shared.dto;

import java.io.Serializable;

public class PrintParams implements Serializable {
    private String filename;
    private String printer;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
