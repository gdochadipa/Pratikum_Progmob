package co.ocha.pratikum_progmob.model;

import java.util.List;

public class TransactionResponse {
    private List<TransactionModel> result;

    public List<TransactionModel> getResult() {
        return result;
    }

    public void setResult(List<TransactionModel> result) {
        this.result = result;
    }
}
