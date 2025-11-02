public class FiltroSpam extends Thread {
    private final Buzon buzonEntrada;
    private final Buzon buzonEntrega;
    private final Buzon buzonCuarentena;
    private final int totalClientes;
    private final int totalServidores;
    private static int contFinClientes = 0;

    public FiltroSpam(Buzon entrada, Buzon entrega, Buzon cuarentena, int totalClientes, int totalServidores) {
        this.buzonEntrada = entrada;
        this.buzonEntrega = entrega;
        this.buzonCuarentena = cuarentena;
        this.totalClientes = totalClientes;
        this.totalServidores = totalServidores;

    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje m = buzonEntrada.extraer();

                // Cuando llega un mensaje de inicio
                if (m.tipo == TipoMensaje.INICIO) {
                    System.out.println("Filtro detecto inicio de " + m.remitente);
                }

                // Cuando llega un mensaje de fin
                else if (m.tipo == TipoMensaje.FIN) {
                    boolean ultimoCliente = false;

                    synchronized (FiltroSpam.class) {
                        contFinClientes++;
                        if (contFinClientes == totalClientes) {
                            ultimoCliente = true; // este filtro es el último que ve el fin
                        }
                    }

                    if (ultimoCliente) {
                        System.out.println("Esperando que se vacien los buzones antes del cierre...");

                        synchronized (buzonEntrada) {
                            buzonEntrada.esperarHastaVacio();
                        }
                        synchronized (buzonCuarentena) {
                            buzonCuarentena.esperarHastaVacio();
                        }

                        // Si todo esta vacio enviar los FIN
                        for (int i = 0; i < totalServidores; i++) {
                            buzonEntrega.depositar(
                                new Mensaje("FIN_ENTREGA_" + i, "Sistema", TipoMensaje.FIN, false)
                            );
                        }

                        buzonCuarentena.depositar(
                            new Mensaje("FIN_CUARENTENA", "Sistema", TipoMensaje.FIN, false)
                        );

                        System.out.println("Filtro envio FIN global");
                        break;
                    }


                }

                // Cuando llega un correo normal
                else {
                    if (m.esSpam) {
                        buzonCuarentena.depositar(m);
                        System.out.println("Filtro envio mensaje a cuarentena: " + m.id);
                    } else {
                        buzonEntrega.depositar(m);
                        System.out.println("Filtro aprobo mensaje valido: " + m.id);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
