package pe.com.mipredio.model;

public class TechnicalProfessionalModel {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String celular;
    private String grado;
    private  String especialidad;
    private String correo;
    private String foto;

    public TechnicalProfessionalModel() {

    }


    public TechnicalProfessionalModel(String nombres, String especialidad, String correo, String foto) {
        this.nombres = nombres;
        this.especialidad = especialidad;
        this.correo = correo;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public TechnicalProfessionalModel(Integer id, String nombres, String apellidos, String dni, String celular, String grado, String especialidad) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.celular = celular;
        this.grado = grado;
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
}
