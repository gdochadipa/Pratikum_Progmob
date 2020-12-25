package co.ocha.pratikum_progmob.model;

import java.util.List;

public class TransactionDetailResponse {
    private List<TransactionDetailModel> result;

    public List<TransactionDetailModel> getResult() {
        return result;
    }

    public void setResult(List<TransactionDetailModel> result) {
        this.result = result;
    }
}
