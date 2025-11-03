package Concurrencia;
import java.util.Random;

public class Mensaje {
    public String id;
    public String remitente;
    public TipoMensaje tipo;
    public boolean esSpam;
    public int tiempoCuarentena;

    public Mensaje(String id, String remitente, TipoMensaje tipo, boolean esSpam) {
        this.id = id;
        this.remitente = remitente;
        this.tipo = tipo;
        this.esSpam = esSpam;

        if (tipo == TipoMensaje.CORREO && esSpam) {
            this.tiempoCuarentena = new Random().nextInt(10000) + 10000; 
        } else {
            this.tiempoCuarentena = 0;
        }
    }

    @Override
    public String toString() {
        String texto = "[" + tipo + "] " + id;
        if (esSpam) {
            texto = texto + " (SPAM)";
        }
        return texto;
    }

}
