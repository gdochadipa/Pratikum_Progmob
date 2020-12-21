package co.ocha.pratikum_progmob.model;

import java.util.List;

public class AddressResponse {
    private List<AddressModel> result;

    public List<AddressModel> getResult() {
        return result;
    }

    public void setResult(List<AddressModel> result) {
        this.result = result;
    }
}
