package co.ocha.pratikum_progmob.model;

import java.util.List;

public class BookResponse {
    private List<BookModel> result;

    public List<BookModel> getResult() {
        return result;
    }

    public void setResult(List<BookModel> result) {
        this.result = result;
    }
}
