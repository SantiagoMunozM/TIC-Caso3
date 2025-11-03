package Concurrencia;

public class ClienteEmisor extends Thread {
    private final String nombre;
    private final BuzonEntrada buzonEntrada;
    private final int numMensajes;

    public ClienteEmisor(String nombre, BuzonEntrada buzonEntrada, int numMensajes) {
        this.nombre = nombre;
        this.buzonEntrada = buzonEntrada;
        this.numMensajes = numMensajes;
    }

    @Override
    public void run() {
        try {
            // Enviar mensaje de inicio
            buzonEntrada.depositar(new Mensaje("INICIO_" + nombre, nombre, TipoMensaje.INICIO, false), this.nombre);

            // Generar correos
            for (int i = 1; i <= numMensajes; i++) {
                boolean esSpam = Math.random() < 0.5;
                Mensaje m = new Mensaje(nombre + "_msg" + i, nombre, TipoMensaje.CORREO, esSpam);
                buzonEntrada.depositar(m, this.nombre);
            }

            // Mensaje de fin
            buzonEntrada.depositar(new Mensaje("FIN_" + nombre, nombre, TipoMensaje.FIN, false), this.nombre);
            System.out.println(nombre + " termino su envio.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
