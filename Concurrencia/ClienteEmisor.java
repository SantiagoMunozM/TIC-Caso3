public class ClienteEmisor extends Thread {
    private final String nombre;
    private final Buzon buzonEntrada;
    private final int numMensajes;

    public ClienteEmisor(String nombre, Buzon buzonEntrada, int numMensajes) {
        this.nombre = nombre;
        this.buzonEntrada = buzonEntrada;
        this.numMensajes = numMensajes;
    }

    @Override
    public void run() {
        try {
            // Enviar mensaje de inicio
            buzonEntrada.depositar(new Mensaje("INICIO_" + nombre, nombre, TipoMensaje.INICIO, false));

            // Generar correos
            for (int i = 1; i <= numMensajes; i++) {
                boolean esSpam = Math.random() < 0.5;
                Mensaje m = new Mensaje(nombre + "_msg" + i, nombre, TipoMensaje.CORREO, esSpam);
                buzonEntrada.depositar(m);
                System.out.println(nombre + " envio " + m);
            }

            // Mensaje de fin
            buzonEntrada.depositar(new Mensaje("FIN_" + nombre, nombre, TipoMensaje.FIN, false));
            System.out.println(nombre + " termino su envio.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
