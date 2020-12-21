package co.ocha.pratikum_progmob.model;

import java.util.List;

public class CartModel {
    private int id, user_id, book_id, qty;
    private  String status;

    public CartModel(int id, int user_id, int book_id, int qty, String status) {
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.qty = qty;
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

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
