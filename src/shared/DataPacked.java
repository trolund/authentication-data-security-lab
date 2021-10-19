package shared;

import java.io.Serializable;

public class DataPacked<T> implements Serializable, IAuth {

    private int token;
    private T payload;

    public DataPacked(int token, T payload) {
        this.token = token;
        this.payload = payload;
    }

    public DataPacked(T payload) {
        this.payload = payload;
    }

    public DataPacked(int token) {
        this.token = token;
        this.payload = null;
    }

    @Override
    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
