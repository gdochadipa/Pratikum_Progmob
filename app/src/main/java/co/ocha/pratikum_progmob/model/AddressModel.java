package co.ocha.pratikum_progmob.model;

public class AddressModel {
    private int id, status;
    private  String address, district,  province;

    public AddressModel(int id, String address, String district, String province, int status) {
        this.id = id;
        this.address = address;
        this.district = district;
        this.province = province;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
