package pe.com.mipredio.classes;

public class MapClass {
    private Double latitude;
    private Double longitude;

    private String address; // Direccion del predio
    private String ubigeo; // Ubigeo del predio
    private String phone; // Nro de contacto
    private String person; // Nombre de la persona

    public MapClass(Double latitude, Double longitude, String address, String ubigeo, String phone, String person) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.ubigeo = ubigeo;
        this.phone = phone;
        this.person = person;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
