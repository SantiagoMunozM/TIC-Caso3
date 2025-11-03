package Concurrencia;
import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrada {
    private final Queue<Mensaje> cola = new LinkedList<>();
    private final int capacidad; 

    public BuzonEntrada(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Mensaje m, String nombreRemitente) throws InterruptedException {
        while (cola.size() >= capacidad) {
            wait(); // Espera pasiva
        }
        cola.add(m);
        System.out.println(nombreRemitente + " envió " + m);
        notify();
    }

    public synchronized Mensaje extraer() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // Espera pasiva
        }
        Mensaje m = cola.poll();
        notify();
        return m;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }

    public synchronized boolean estaLleno () {
        return cola.size() == capacidad;
    }

    public synchronized int tamanio() {
        return cola.size();
    }


}
