package shared.dto;

import java.io.Serializable;

public class ReadConfigParams implements Serializable {
    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
