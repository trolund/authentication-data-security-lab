package shared.dto;

import java.io.Serializable;

public class StatusParams implements Serializable {
    private String printer;

    public StatusParams(String printerName) {
        printer = printerName;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
