package Concurrencia;

public class FiltroSpam extends Thread {
    private int id;
    private final BuzonEntrada buzonEntrada;
    private final BuzonEntrega buzonEntrega;
    private final BuzonCuarentena buzonCuarentena;
    
    private static int totalClientes = 0;
    private static int clientesFinalizados = 0;

    public FiltroSpam(BuzonEntrada entrada, BuzonEntrega entrega, BuzonCuarentena cuarentena, int id) {
        this.buzonEntrada = entrada;
        this.buzonEntrega = entrega;
        this.buzonCuarentena = cuarentena;
        this.id = id;

    }

    public synchronized void incrementarTotalClientes() {
        FiltroSpam.totalClientes +=1;
    }

    public synchronized boolean incrementarClientesFinalizados() {
        clientesFinalizados += 1;
        return clientesFinalizados == totalClientes;
    }

    @Override
    public void run() {
        try {
            while (clientesFinalizados<totalClientes || totalClientes == 0) {
                Mensaje m = buzonEntrada.extraer();
                if (m!= null) {
                    // Cuando llega un mensaje de inicio
                    if (m.tipo == TipoMensaje.INICIO) {
                        System.out.printf("Filtro %d detectó inicio de %s\n",id, m.remitente);
                        incrementarTotalClientes();
                    }

                    // Cuando llega un mensaje de fin
                    else if (m.tipo == TipoMensaje.FIN) {
                        
                        boolean ultimoClienteFinalizado = incrementarClientesFinalizados();

                        if (ultimoClienteFinalizado) {
                            System.out.println("Buzón de entrada vacío, enviando mensaje de fin a buzón de cuarentena");

                            // Si todo esta vacio enviar los FIN
                            
                            buzonCuarentena.depositar(
                                new Mensaje("FIN_CUARENTENA", "Sistema", TipoMensaje.FIN, false)
                            );

                            while (!buzonCuarentena.estaVacio()) {
                                Thread.yield();
                            }

                            while (buzonEntrega.estaLleno()) {
                                Thread.yield();
                            }

                            System.out.println("Buzón de cuarentena vacío, enviando mensaje de fin a buzón de entrega");

                            buzonEntrega.depositar(
                                new Mensaje("FIN_ENTREGA", "Sistema", TipoMensaje.FIN, false)
                            );
                              
                        }


                    }

                    // Cuando llega un correo normal
                    else {
                        if (m.esSpam) {
                            buzonCuarentena.depositar(m);
                            System.out.printf("Filtro %d envió mensaje a cuarentena: %s\n", id, m.id);
                        } else {
                            while (buzonEntrega.estaLleno()) {
                                Thread.yield();
                            }
                            buzonEntrega.depositar(m);
                            System.out.printf("Filtro %d aprobó mensaje valido: %s\n",id, m.id);
                        }
                    }
                }
            }
            System.out.printf("Filtro %d terminó ejecución\n", id);


        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
