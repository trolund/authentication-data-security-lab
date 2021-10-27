package shared;

import java.io.Serializable;

public class DataPacked<T> implements Serializable, IAuth {

    private String token;
    private T payload;

    public DataPacked(String token, T payload) {
        this.token = token;
        this.payload = payload;
    }

    public DataPacked(T payload) {
        this.payload = payload;
    }

    public DataPacked(String token) {
        this.token = token;
        this.payload = null;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
