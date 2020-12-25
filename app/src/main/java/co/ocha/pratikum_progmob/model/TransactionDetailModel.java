package co.ocha.pratikum_progmob.model;

public class TransactionDetailModel {
    private int id, book_id, transaction_id, qty, price;

    public TransactionDetailModel(int id, int book_id, int transaction_id, int qty, int price) {
        this.id = id;
        this.book_id = book_id;
        this.transaction_id = transaction_id;
        this.qty = qty;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
