package Concurrencia;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonEntrega {
    private final Queue<Mensaje> cola = new LinkedList<>();
    private final int capacidad; 

    public BuzonEntrega(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Mensaje m) throws InterruptedException {
        cola.add(m);
    }

    public synchronized Mensaje extraer() throws InterruptedException {
        Mensaje m = cola.poll();
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
