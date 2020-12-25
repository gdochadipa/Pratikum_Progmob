package co.ocha.pratikum_progmob.model;

public class TransactionModel {
    private int id, user_id, total;
    private  String timeout, status, address_id;

    public TransactionModel(int id, int user_id, String address_id, int total, String timeout, String status) {
        this.id = id;
        this.user_id = user_id;
        this.address_id = address_id;
        this.total = total;
        this.timeout = timeout;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
