package interfaz;

import modelo.*;
import negocio.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner sc;
    private GestorUsuarios gestorUsuarios;
    private GestorRutas gestorRutas;
    private GestorPlanificacion gestorPlanificacion;
    private Itinerario ultimoItinerario;

    public MenuPrincipal() {
        this.sc = new Scanner(System.in);
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorRutas = new GestorRutas();
        this.gestorPlanificacion = new GestorPlanificacion();
        this.ultimoItinerario = null;
    }

    public void iniciar() {
        int opc;
        do {
            System.out.println("\n BUSQ - ASISTENTE DE MOVILIDAD DMQ ");
            System.out.println("\n1. Registrarse");
            System.out.println("2. Iniciar Sesión");
            System.out.println("3. Salir");
            opc = leerEntero("\nSeleccione una opción (1-3): ");

            if (opc == 1) {
                registrarPasajeroUI();
            } else if (opc == 2) {
                procesarLogin();
            } else if (opc != 3) {
                System.out.println("Opción fuera del menú. Ingrese un número válido (1-3).");
            }
        } while (opc != 3);
        System.out.println("\nGracias por utilizar BusQ!.");
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número válido del menú.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un valor decimal válido (ej. 0.35).");
            }
        }
    }

    private String leerSoloLetras(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("Entrada inválida. Este campo no puede estar vacío.");
                continue;
            }

            boolean soloLetras = true;
            for (int i = 0; i < entrada.length(); i++) {
                char c = entrada.charAt(i);
                if (!Character.isLetter(c) && c != ' ') {
                    soloLetras = false;
                    break;
                }
            }

            if (soloLetras) {
                return entrada;
            } else {
                System.out.println("Entrada inválida. Solo se permiten letras y espacios.");
            }
        }
    }

    private String leerCorreoValido(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String correo = sc.nextLine().trim();

            if (correo.isEmpty()) {
                System.out.println("Entrada inválida. El correo no puede estar vacío.");
                continue;
            }

            int posArroba = correo.indexOf('@');
            int posPunto = correo.lastIndexOf('.');

            if (posArroba <= 0 || posArroba != correo.lastIndexOf('@')) {
                System.out.println("Correo inválido. Debe contener un solo '@', sin ir al inicio.");
                continue;
            }

            if (posPunto <= posArroba + 1 || posPunto >= correo.length() - 1) {
                System.out.println("Correo inválido. Debe contener un '.' después del '@' (ej. usuario@udla.edu.ec).");
                continue;
            }

            return correo;
        }
    }

    private String leerCarnetConadis() {
        while (true) {
            System.out.print("Ingrese los 10 dígitos del carnet CONADIS: ");
            String carnet = sc.nextLine().trim();

            if (carnet.length() != 10) {
                System.out.println("Carnet inválido. Debe tener exactamente 10 dígitos.");
                continue;
            }

            boolean todosDigitos = true;
            for (int i = 0; i < carnet.length(); i++) {
                if (!Character.isDigit(carnet.charAt(i))) {
                    todosDigitos = false;
                    break;
                }
            }

            if (todosDigitos) {
                return carnet;
            } else {
                System.out.println("Carnet inválido. Solo debe contener números.");
            }
        }
    }

    private void registrarPasajeroUI() {
        System.out.println("\n** REGISTRO DE PASAJERO **");
        String nombre = leerSoloLetras("Nombre completo: ");
        String correo = leerCorreoValido("Correo electrónico: ");
        System.out.print("Contraseña (mínimo 8 caracteres): ");
        String pass = sc.nextLine();
        String sector = leerSoloLetras("Sector de residencia: ");

        int edad;
        do {
            edad = leerEntero("Ingrese su edad: ");
            if (edad < 0 || edad > 120) {
                System.out.println("Edad inválida. Ingrese una edad entre 0 y 120 años.");
            }
        } while (edad < 0 || edad > 120);

        String tipoPerfil;
        double subsidio;
        if (edad < 18) {
            tipoPerfil = "Estudiante";
            subsidio = 0.50;
        } else if (edad <= 65) {
            tipoPerfil = "General";
            subsidio = 0.0;
        } else {
            tipoPerfil = "Adulto Mayor";
            subsidio = 0.50;
        }

        System.out.println("\n¿Tiene alguna discapacidad?");
        System.out.println("1. Sí");
        System.out.println("0. No");

        int opcDiscapacidad;
        do {
            opcDiscapacidad = leerEntero("Ingrese un número del menú (1 o 0): ");
            if (opcDiscapacidad != 1 && opcDiscapacidad != 0) {
                System.out.println("Opción fuera del menú. Ingrese 1 si tiene discapacidad o 0 si no tiene.");
            }
        } while (opcDiscapacidad != 1 && opcDiscapacidad != 0);

        if (opcDiscapacidad == 1) {
            System.out.println("\n--- VERIFICACIÓN DE DISCAPACIDAD ---");
            String nombreDiscapacidad = leerSoloLetras("Ingrese el nombre de la discapacidad: ");
            double porcentajeDiscapacidad;
            do {
                porcentajeDiscapacidad = leerDecimal("Ingrese el porcentaje de discapacidad (%): ");
                if (porcentajeDiscapacidad <= 0 || porcentajeDiscapacidad > 100) {
                    System.out.println("Porcentaje inválido. Ingrese un valor mayor a 0 y hasta 100.");
                }
            } while (porcentajeDiscapacidad <= 0 || porcentajeDiscapacidad > 100);

            String carnetConadis = leerCarnetConadis();
            System.out.println("Discapacidad registrada: " + nombreDiscapacidad
                    + " (" + String.format("%.1f", porcentajeDiscapacidad) + "%) | Carnet CONADIS: " + carnetConadis);

            tipoPerfil = "Con Discapacidad";
            subsidio = 0.7143;
        }

        PerfilTarifario perfil = new PerfilTarifario(tipoPerfil, subsidio, true);
        String resultado = gestorUsuarios.registrarPasajero(nombre, correo, pass, sector, perfil);
        System.out.println(resultado);
    }

    private void procesarLogin() {
        System.out.print("\n**Inicio de Sesion**");
        System.out.print("\nCorreo: ");
        String correo = sc.nextLine().trim();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        Usuario usuario = gestorUsuarios.autenticar(correo, pass);
        if (usuario == null) {
            System.out.println("\nError: usuario o contraseña incorrectos.");
            return;
        }

        System.out.println("\nBienvenido, " + usuario.getNombreCompleto() + "!");
        if (usuario instanceof Pasajero) {
            menuPasajero((Pasajero) usuario);
        } else if (usuario instanceof Administrador) {
            menuAdministrador((Administrador) usuario);
        }
    }

    private void menuPasajero(Pasajero pasajero) {
        int opc;
        do {
            System.out.println("\n** MENÚ PASAJERO **");
            System.out.println("1. Consultar catálogo de rutas disponibles");
            System.out.println("2. Consultar tarifa según mi perfil");
            System.out.println("3. Planificar itinerario óptimo y calcular ETA");
            System.out.println("4. Guardar ruta favorita");
            System.out.println("5. Consultar mis rutas favoritas");
            System.out.println("6. Eliminar ruta favorita");
            System.out.println("7. Cerrar sesión");
            opc = leerEntero("\nSeleccione una opción (1-7): ");

            switch (opc) {
                case 1:
                    System.out.println("\n** CATÁLOGO DE RUTAS DMQ **");
                    System.out.println(gestorRutas.listarRutasActivas());
                    break;
                case 2:
                    double tarifaBase = leerDecimal("\nIngrese la tarifa base del pasaje ($): ");
                    double tarifaFinal = pasajero.consultarTarifa(tarifaBase);
                    System.out.println("Perfil: " + pasajero.getPerfilTarifario().getTipoPerfil()
                            + " | Tarifa a pagar: $" + String.format("%.2f", tarifaFinal));
                    break;
                case 3:
                    List<Ruta> rutasOperativas = new ArrayList<>();
                    for (Ruta r : gestorRutas.getRutas()) {
                        if (r.getEstado().startsWith("OPERATIVA")) {
                            boolean yaAgregada = false;
                            for (Ruta existente : rutasOperativas) {
                                if (existente.getCodigoLinea().equalsIgnoreCase(r.getCodigoLinea())) {
                                    yaAgregada = true;
                                    break;
                                }
                            }
                            if (!yaAgregada) {
                                rutasOperativas.add(r);
                            }
                        }
                    }

                    if (rutasOperativas.isEmpty()) {
                        System.out.println("\nNo se encontraron rutas directas ni combinaciones disponibles.");
                        break;
                    }

                    System.out.println("\n** RUTAS DISPONIBLES - SELECCIONE UNA **");
                    for (int i = 0; i < rutasOperativas.size(); i++) {
                        Ruta rOp = rutasOperativas.get(i);
                        System.out.println((i + 1) + ". [" + rOp.getCodigoLinea() + "] " + rOp.getNombre()
                                + " (" + rOp.getSectorOrigen() + " -> " + rOp.getSectorDestino() + ")");
                    }

                    int opcRuta;
                    do {
                        opcRuta = leerEntero("Ingrese el número de la ruta (1-" + rutasOperativas.size() + "): ");
                        if (opcRuta < 1 || opcRuta > rutasOperativas.size()) {
                            System.out.println("Opción fuera del menú. Ingrese un número entre 1 y " + rutasOperativas.size() + ".");
                        }
                    } while (opcRuta < 1 || opcRuta > rutasOperativas.size());

                    Ruta ruta = rutasOperativas.get(opcRuta - 1);

                    System.out.println("\nCriterio de optimización:");
                    System.out.println("1. Más rápido");
                    System.out.println("2. Menos transbordos");
                    System.out.println("3. Menos caminata");

                    int opcCriterio;
                    do {
                        opcCriterio = leerEntero("Ingrese un número del menú (1-3): ");
                        if (opcCriterio < 1 || opcCriterio > 3) {
                            System.out.println("Opción fuera del menú. Ingrese un número válido (1-3).");
                        }
                    } while (opcCriterio < 1 || opcCriterio > 3);

                    String criterio = "Más rápido";
                    if (opcCriterio == 2) {
                        criterio = "Menos transbordos";
                    } else if (opcCriterio == 3) {
                        criterio = "Menos caminata";
                    }

                    if (pasajero.yaRecorrioConCriterio(ruta, criterio)) {
                        System.out.println("\nYa generaste y registraste la ruta [" + ruta.getCodigoLinea()
                                + "] con el criterio '" + criterio + "'. No se puede repetir dos veces.");
                        break;
                    }

                    ultimoItinerario = gestorPlanificacion.generarItinerarioOptimo(ruta, criterio);
                    pasajero.registrarRutaRecorrida(ruta, criterio);

                    double costoFinal = ultimoItinerario.calcularCostoTotal(pasajero.getPerfilTarifario());
                    double etaMin = gestorPlanificacion.calcularETA(ruta, 30.0);

                    System.out.println("\n** ITINERARIO GENERADO **");
                    System.out.println("Ruta: [" + ruta.getCodigoLinea() + "] " + ruta.getNombre());
                    System.out.println("Origen: " + ultimoItinerario.getOrigen() + " -> Destino: " + ultimoItinerario.getDestino());
                    System.out.println("Criterio aplicado: " + criterio);
                    System.out.println("Duración estimada: " + ultimoItinerario.getDuracionEstimadaMin() + " min");
                    System.out.println("Costo total con perfil (" + pasajero.getPerfilTarifario().getTipoPerfil() + "): $" + String.format("%.2f", costoFinal));
                    System.out.println("ETA estimado para llegada: " + String.format("%.2f", etaMin) + " min");
                    break;
                case 4:
                    guardarRutaFavoritaUI(pasajero);
                    break;
                case 5:
                    consultarRutasFavoritasUI(pasajero);
                    break;
                case 6:
                    eliminarRutaFavoritaUI(pasajero);
                    break;
                case 7:
                    System.out.println("Cerrando sesión de pasajero...");
                    break;
                default:
                    System.out.println("Opción fuera del menú. Ingrese un número válido (1-7).");
                    break;
            }
        } while (opc != 7);
    }

    private void guardarRutaFavoritaUI(Pasajero pasajero) {
        List<Ruta> recorridas = pasajero.getRutasRecorridas();
        if (recorridas.isEmpty()) {
            System.out.println("\nAún no has recorrido ninguna ruta. Planifica una ruta en la opción 3 primero.");
            return;
        }

        System.out.println("\n** RUTAS RECORRIDAS - SELECCIONE CUAL GUARDAR COMO FAVORITA **");
        for (int i = 0; i < recorridas.size(); i++) {
            Ruta r = recorridas.get(i);
            String crit = pasajero.getCriterioRecorrido(i);
            System.out.println((i + 1) + ". [" + r.getCodigoLinea() + "] " + r.getNombre()
                    + " (" + r.getSectorOrigen() + " -> " + r.getSectorDestino() + ") - Criterio: " + crit);
        }

        int seleccion;
        do {
            seleccion = leerEntero("Ingrese el número de la ruta a guardar (1-" + recorridas.size() + "): ");
            if (seleccion < 1 || seleccion > recorridas.size()) {
                System.out.println("Opción fuera del menú. Ingrese un número entre 1 y " + recorridas.size() + ".");
            }
        } while (seleccion < 1 || seleccion > recorridas.size());

        Ruta rutaElegida = recorridas.get(seleccion - 1);
        String criterioElegido = pasajero.getCriterioRecorrido(seleccion - 1);

        System.out.print("Ingrese el nombre personalizado para su ruta favorita: ");
        String nombreFav = sc.nextLine().trim();
        if (nombreFav.isEmpty()) {
            nombreFav = rutaElegida.getNombre();
        }

        if (pasajero.yaExisteRutaFavorita(nombreFav, rutaElegida.getSectorOrigen(), rutaElegida.getSectorDestino(), criterioElegido)) {
            System.out.println("Esta ruta con ese criterio o nombre ya está guardada en tus favoritas. No se puede ingresar nuevamente.");
            return;
        }

        Itinerario itinerarioGuardar = gestorPlanificacion.generarItinerarioOptimo(rutaElegida, criterioElegido);

        RutaFavorita rf = new RutaFavorita(nombreFav, "2026-11-24");
        rf.agregarItinerario(itinerarioGuardar);
        pasajero.guardarRutaFavorita(rf);

        System.out.println("Ruta favorita '" + rf.getNombrePersonalizado() + "' guardada exitosamente. Total favoritas: "
                + pasajero.contarRutasFavoritas());
    }

    private void consultarRutasFavoritasUI(Pasajero pasajero) {
        System.out.println("\n** MIS RUTAS FAVORITAS (" + pasajero.contarRutasFavoritas() + ") **");
        if (pasajero.getRutasFavoritas().isEmpty()) {
            System.out.println("Aún no tienes rutas favoritas guardadas.");
            return;
        }

        for (int i = 0; i < pasajero.getRutasFavoritas().size(); i++) {
            RutaFavorita rf = pasajero.getRutasFavoritas().get(i);
            System.out.println((i + 1) + ". " + rf.getNombrePersonalizado() + " (Guardada: " + rf.getFechaGuardado() + ")");
            for (Itinerario it : pasajero.consultarItinerarios(rf)) {
                System.out.println("   - " + it.getOrigen() + " -> " + it.getDestino()
                        + "\nCriterio: " + it.getCriterioOptimizacion()
                        + "\nDuración: " + it.getDuracionEstimadaMin() + " min"
                        + "\nCosto perfil: $" + String.format("%.2f", it.calcularCostoTotal(pasajero.getPerfilTarifario())));
            }
        }

        RutaFavorita ultima = pasajero.consultarUltimaRutaFavorita();
        if (ultima != null) {
            System.out.println("\nÚltima ruta favorita agregada: " + ultima.getNombrePersonalizado());
        }
    }

    private void eliminarRutaFavoritaUI(Pasajero pasajero) {
        List<RutaFavorita> favoritas = pasajero.getRutasFavoritas();
        if (favoritas.isEmpty()) {
            System.out.println("\nNo tienes rutas favoritas guardadas para eliminar.");
            return;
        }

        System.out.println("\n** ELIMINAR RUTA FAVORITA **");
        for (int i = 0; i < favoritas.size(); i++) {
            RutaFavorita rf = favoritas.get(i);
            System.out.println((i + 1) + ". " + rf.getNombrePersonalizado() + " (Guardada: " + rf.getFechaGuardado() + ")");
        }

        int seleccion;
        do {
            seleccion = leerEntero("Seleccione el número de la ruta favorita a eliminar (1-" + favoritas.size() + "): ");
            if (seleccion < 1 || seleccion > favoritas.size()) {
                System.out.println("Opción fuera del menú. Ingrese un número entre 1 y " + favoritas.size() + ".");
            }
        } while (seleccion < 1 || seleccion > favoritas.size());

        RutaFavorita eliminada = favoritas.get(seleccion - 1);
        if (pasajero.eliminarRutaFavorita(seleccion - 1)) {
            System.out.println("Ruta favorita '" + eliminada.getNombrePersonalizado() + "' eliminada correctamente.");
        } else {
            System.out.println("No se pudo eliminar la ruta favorita seleccionada.");
        }
    }

    private void menuAdministrador(Administrador admin) {
        int opc;
        do {
            System.out.println("\n** MENÚ ADMINISTRADOR **");
            System.out.println("1. Registrar nueva ruta en el catálogo");
            System.out.println("2. Actualizar ruta existente");
            System.out.println("3. Suspender ruta del catálogo");
            System.out.println("4. Listar todas las rutas");
            System.out.println("5. Cerrar sesión");
            opc = leerEntero("\nSeleccione una opción (1-5): ");

            switch (opc) {
                case 1:
                    System.out.print("\nCódigo de línea (ej. C2): ");
                    String cod = sc.nextLine().trim();
                    System.out.print("Nombre de ruta: ");
                    String nom = sc.nextLine().trim();
                    System.out.print("Sector de origen: ");
                    String orig = sc.nextLine().trim();
                    System.out.print("Sector de destino: ");
                    String dest = sc.nextLine().trim();

                    Ruta nuevaRuta = new Ruta(cod, nom, orig, dest, "OPERATIVA");
                    if (gestorRutas.agregarRuta(nuevaRuta)) {
                        admin.registrarRuta(nuevaRuta);
                        System.out.println("Ruta registrada exitosamente en el catálogo del DMQ.");
                    } else {
                        System.out.println("Error: Ya existe una ruta con ese código.");
                    }
                    break;
                case 2:
                    System.out.print("\nIngrese el código de la ruta a actualizar: ");
                    String codAct = sc.nextLine().trim();
                    Ruta rutaAct = gestorRutas.buscarRutaPorCodigo(codAct);
                    if (rutaAct != null) {
                        System.out.print("Nuevo nombre de ruta (o Enter para mantener): ");
                        String nuevoNom = sc.nextLine().trim();
                        if (!nuevoNom.isEmpty() && !nuevoNom.equals(".")) {
                            rutaAct.setNombre(nuevoNom);
                        }

                        System.out.print("Nuevo sector de origen (o Enter para mantener): ");
                        String nuevoOrig = sc.nextLine().trim();
                        if (!nuevoOrig.isEmpty() && !nuevoOrig.equals(".")) {
                            rutaAct.setSectorOrigen(nuevoOrig);
                        }

                        System.out.print("Nuevo sector de destino (o Enter para mantener): ");
                        String nuevoDest = sc.nextLine().trim();
                        if (!nuevoDest.isEmpty() && !nuevoDest.equals(".")) {
                            rutaAct.setSectorDestino(nuevoDest);
                        }

                        System.out.println("\nNuevo estado de la ruta:");
                        System.out.println("0. Mantener estado actual (" + rutaAct.getEstado() + ")");
                        System.out.println("1. OPERATIVA");
                        System.out.println("2. DESVIADA");
                        System.out.println("3. SUSPENDIDA");

                        int opcEstado;
                        do {
                            opcEstado = leerEntero("Ingrese un número del menú (0-3): ");
                            if (opcEstado < 0 || opcEstado > 3) {
                                System.out.println("Opción fuera del menú. Ingrese un número válido (0-3).");
                            }
                        } while (opcEstado < 0 || opcEstado > 3);

                        if (opcEstado == 1) {
                            rutaAct.actualizarEstado("OPERATIVA");
                        } else if (opcEstado == 2) {
                            rutaAct.actualizarEstado("DESVIADA");
                        } else if (opcEstado == 3) {
                            rutaAct.actualizarEstado("SUSPENDIDA");
                        }

                        admin.actualizarRuta(rutaAct);
                        System.out.println("Trazado de ruta actualizado.");
                    } else {
                        System.out.println("Ruta no encontrada.");
                    }
                    break;
                case 3:
                    System.out.print("\nIngrese el código de la ruta a suspender: ");
                    String codSusp = sc.nextLine().trim();
                    Ruta r = gestorRutas.buscarRutaPorCodigo(codSusp);
                    if (r != null) {
                        System.out.print("Motivo de suspensión: ");
                        String motivo = sc.nextLine().trim();
                        admin.suspenderRuta(r, motivo);
                        System.out.println("Ruta suspendida y deshabilitada del catálogo de opciones del usuario.");
                    } else {
                        System.out.println("Ruta no encontrada.");
                    }
                    break;
                case 4:
                    System.out.println("\n** LISTADO GENERAL DE RUTAS **");
                    System.out.println(gestorRutas.listarTodasLasRutas());
                    break;
                case 5:
                    System.out.println("Cerrando sesión de administrador...");
                    break;
                default:
                    System.out.println("Opción fuera del menú. Ingrese un número válido (1-5).");
                    break;
            }
        } while (opc != 5);
    }
}