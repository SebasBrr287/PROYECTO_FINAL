package negocio;

import modelo.*;
import java.util.ArrayList;
import java.util.List;

public class GestorRutas {
    private List<Ruta> rutas;
    public List<Ruta> getRutas() {
        return rutas;
    }

    public GestorRutas() {
        this.rutas = new ArrayList<>();
        cargarRutasIniciales();
    }

    private void cargarRutasIniciales() {
        Ruta r1 = new Ruta("C1", "Corredor Central Norte", "La Ofelia", "Quitumbe", "OPERATIVA");
        Parada p1 = new Parada(101, "Terminal La Ofelia", 0.0, 15.0);
        Parada p2 = new Parada(102, "Estación La Carolina", 0.0, 8.5);
        Parada p3 = new Parada(103, "Terminal Quitumbe", 0.0, 0.0);
        r1.agregarParada(p1);
        r1.agregarParada(p2);
        r1.agregarParada(p3);

        Itinerario it1 = new Itinerario(1, "La Ofelia", "Quitumbe", "Más rápido", "08:00", 0.35);
        it1.agregarTramo(new Tramo(1, "Troncal", 15.0, 42.0));
        r1.agregarItinerario(it1);

        Bus bus1 = new Bus("BUS-01", "PCI-1024", 80, "OPERATIVO");
        bus1.agregarItinerario(it1);
        r1.agregarBus(bus1);

        Ruta r2 = new Ruta("C2", "Corredor Central Sur", "La Marín", "Quitumbe", "OPERATIVA");
        Parada p2_1 = new Parada(201, "Estación La Marín", 0.0, 9.0);
        Parada p2_2 = new Parada(202, "Estación El Recreo", 0.0, 4.5);
        Parada p2_3 = new Parada(203, "Terminal Quitumbe", 0.0, 0.0);
        r2.agregarParada(p2_1);
        r2.agregarParada(p2_2);
        r2.agregarParada(p2_3);

        Itinerario it2 = new Itinerario(2, "La Marín", "Quitumbe", "Más rápido", "08:15", 0.35);
        it2.agregarTramo(new Tramo(1, "Troncal", 9.0, 25.0));
        r2.agregarItinerario(it2);

        Bus bus2 = new Bus("BUS-02", "PCI-2048", 80, "OPERATIVO");
        bus2.agregarItinerario(it2);
        r2.agregarBus(bus2);

        rutas.add(r1);
        rutas.add(r2);
    }

    public boolean agregarRuta(Ruta ruta) {
        if (ruta != null && buscarRutaPorCodigo(ruta.getCodigoLinea()) == null) {
            rutas.add(ruta);
            return true;
        }
        return false;
    }

    public Ruta buscarRutaPorCodigo(String codigo) {
        for (Ruta r : rutas) {
            if (r.getCodigoLinea().equalsIgnoreCase(codigo)) {
                return r;
            }
        }
        return null;
    }

    public String listarRutasActivas() {
        String resultado = "";
        for (Ruta r : rutas) {
            if (r.getEstado().startsWith("OPERATIVA")) {
                resultado += "[" + r.getCodigoLinea() + "] "
                        + r.getNombre() + " (" + r.getSectorOrigen() + " -> " + r.getSectorDestino() + ")"
                        + " | Distancia: " + String.format("%.2f", r.calcularDistanciaTotal()) + " km\n";
            }
        }
        if (resultado.isEmpty()) {
            return "No se encontraron rutas directas ni combinaciones disponibles.";
        }
        return resultado;
    }

    public String listarTodasLasRutas() {
        if (rutas.isEmpty()) {
            return "No hay rutas registradas.";
        }
        String resultado = "";
        for (Ruta r : rutas) {
            resultado += "[" + r.getCodigoLinea() + "] "
                    + r.getNombre() + " (" + r.getSectorOrigen() + " -> " + r.getSectorDestino() + ")"
                    + " | Estado: " + r.getEstado() + "\n";
        }
        return resultado;
    }
}