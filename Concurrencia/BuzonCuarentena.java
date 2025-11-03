package Concurrencia;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonCuarentena {
    private final Queue<Mensaje> cola = new LinkedList<>();
    

    public BuzonCuarentena() {
    }

    public synchronized void depositar(Mensaje m) throws InterruptedException {
        cola.add(m);
    }

    public synchronized Mensaje extraer () {
        Mensaje m = cola.poll();
        return m;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }


}
