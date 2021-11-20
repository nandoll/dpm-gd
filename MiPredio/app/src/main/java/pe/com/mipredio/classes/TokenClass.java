package pe.com.mipredio.classes;
import com.auth0.android.jwt.JWT;

public class TokenClass {
    private String nombre;
    private String username;
    private String rol;
    private String especialidad;

    public TokenClass(String token) {
        if(token != null){
            JWT jwt = new JWT(token);
            this.nombre = jwt.getClaim("_nombre").asString();
            this.username = jwt.getClaim("_username").asString();
            this.rol = jwt.getClaim("_rol").asString();
            this.especialidad = jwt.getClaim("_especialidad").asString();
        }

    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
