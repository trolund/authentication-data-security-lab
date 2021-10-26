package shared.dto;

import java.io.Serializable;

public class QueueParams implements Serializable {
    private String printer;

    public QueueParams(String printerName) {
        printer = printerName;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
