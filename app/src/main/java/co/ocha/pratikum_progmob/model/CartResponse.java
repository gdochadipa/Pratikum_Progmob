package co.ocha.pratikum_progmob.model;

import java.util.List;

public class CartResponse {
    private List<CartModel> result;
    private String status;

    public List<CartModel> getResult() {
        return result;
    }

    public void setResult(List<CartModel> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
