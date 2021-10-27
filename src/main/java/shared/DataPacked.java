package shared;

import java.io.Serializable;

public class DataPacked<T> implements Serializable, IAuth {

    private Integer token;
    private T payload;

    public DataPacked(Integer token, T payload) {
        this.token = token;
        this.payload = payload;
    }

    public DataPacked(T payload) {
        this.payload = payload;
    }

    public DataPacked(Integer token) {
        this.token = token;
        this.payload = null;
    }

    @Override
    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
