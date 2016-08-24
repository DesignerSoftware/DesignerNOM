/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Deportes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarDeportesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDeportes implements Serializable {

    @EJB
    AdministrarDeportesInterface administrarDeportes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Deportes> listDeportes;
    private List<Deportes> filtrarDeportes;
    private List<Deportes> crearDeportes;
    private List<Deportes> modificarDeportes;
    private List<Deportes> borrarDeportes;
    private Deportes nuevoDeporte;
    private Deportes duplicarDeporte;
    private Deportes editarDeportes;
    private Deportes deporteSeleccionado;
    private int tamano;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro, paginaanterior;
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlDeportes() {
        listDeportes = null;
        crearDeportes = new ArrayList<Deportes>();
        modificarDeportes = new ArrayList<Deportes>();
        borrarDeportes = new ArrayList<Deportes>();
        permitirIndex = true;
        editarDeportes = new Deportes();
        nuevoDeporte = new Deportes();
        duplicarDeporte = new Deportes();
        a = null;
        guardado = true;
        tamano = 270;
        paginaanterior = "";
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDeportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaanterior = pagina;
        listDeportes = null;
        getListDeportes();
        contarRegistros();
        deporteSeleccionado = listDeportes.get(0);
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(Deportes deporte, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            deporteSeleccionado = deporte;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = deporteSeleccionado.getCodigo();
                }
                if (cualCelda == 1) {
                    backUpDescripcion = deporteSeleccionado.getNombre();
                }
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = deporteSeleccionado.getCodigo();
                }
                if (cualCelda == 1) {
                    backUpDescripcion = deporteSeleccionado.getNombre();
                }

                deporteSeleccionado.getSecuencia();

            }
        }
        System.out.println("Indice: " + deporteSeleccionado + " Celda: " + cualCelda);
    }

    public void asignarIndex(Deportes deporte, int LND, int dig) {
        try {
            deporteSeleccionado = deporte;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            //contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR CONTROLDEPORTES.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        contarRegistros();
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            //contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarDeportes.clear();
        crearDeportes.clear();
        modificarDeportes.clear();
        k = 0;
        listDeportes = null;
        deporteSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListDeportes();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:infoRegistro");
        context.update("form:datosDeporte");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
        }

        borrarDeportes.clear();
        crearDeportes.clear();
        modificarDeportes.clear();
        deporteSeleccionado = null;
        k = 0;
        listDeportes = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosDeporte");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 246;
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDeporte");
            bandera = 0;
            filtrarDeportes = null;
            tipoLista = 0;
        }
    }

    public void modificarDeporte(Deportes deporte, String confirmarCambio, String valorConfirmar) {
        deporteSeleccionado = deporte;
        int contador = 0;
        int codigoVacio = 0;
        boolean coincidencias = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {

            if (deporteSeleccionado.getCodigo() == null || deporteSeleccionado.getCodigo().equals(codigoVacio)) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                deporteSeleccionado.setCodigo(backUpCodigo);
            } else {

                for (int j = 0; j < listDeportes.size(); j++) {
                    if (deporteSeleccionado.getSecuencia() != listDeportes.get(j).getSecuencia()) {
                        if (deporteSeleccionado.getCodigo().equals(listDeportes.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    coincidencias = false;
                    deporteSeleccionado.setCodigo(backUpCodigo);
                } else {
                    coincidencias = true;
                }
            }
        }

        if (confirmarCambio.equalsIgnoreCase("M")) {

            if (deporteSeleccionado.getNombre() == null || deporteSeleccionado.getNombre().isEmpty()) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                deporteSeleccionado.setNombre(backUpDescripcion);
                coincidencias = false;
            }
            for (int j = 0; j < listDeportes.size(); j++) {
                if (deporteSeleccionado.getSecuencia() != listDeportes.get(j).getSecuencia()) {
                    if (deporteSeleccionado.getNombre().equals(listDeportes.get(j).getNombre())) {
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "MOTIVO REPETIDOS";
                coincidencias = false;
                deporteSeleccionado.setCodigo(backUpCodigo);
            } else {
                coincidencias = true;
            }
        }

        if (coincidencias == true) {
            if (!crearDeportes.contains(deporteSeleccionado)) {
                if (!modificarDeportes.contains(deporteSeleccionado)) {
                    modificarDeportes.add(deporteSeleccionado);
                }
            }

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        } else {
            context.update("form:validacionModificar");
            context.execute("validacionModificar.show()");
        }

        context.update("form:datosDeporte");
        context.update("form:ACEPTAR");
    }

    public void borrarDeporte() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {

            if (tipoLista == 0) {
                System.out.println("Entro a BorrarDeporte");
                if (!modificarDeportes.isEmpty() && modificarDeportes.contains(deporteSeleccionado)) {
                    modificarDeportes.remove(modificarDeportes.indexOf(deporteSeleccionado));
                    borrarDeportes.add(deporteSeleccionado);
                } else if (!crearDeportes.isEmpty() && crearDeportes.contains(deporteSeleccionado)) {
                    crearDeportes.remove(crearDeportes.indexOf(deporteSeleccionado));
                } else {
                    borrarDeportes.add(deporteSeleccionado);
                }
                listDeportes.remove(deporteSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarDeportes.remove(deporteSeleccionado);
                listDeportes.remove(deporteSeleccionado);
            }
            modificarinfoRegistro(listDeportes.size());
            context.update("form:infoRegistro");
            context.update("form:datosDeporte");
            deporteSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
        }

    }

    private BigInteger verificarBorradoVigenciasDeportes;
    private BigInteger contadorDeportesPersonas;
    private BigInteger contadorParametrosInformes;

    public void verificarBorrado() {
        try {
            System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
            System.out.println("secuencia borrado : " + deporteSeleccionado.getSecuencia());
            if (tipoLista == 0) {
                System.out.println("secuencia borrado : " + deporteSeleccionado.getSecuencia());
                verificarBorradoVigenciasDeportes = administrarDeportes.contarVigenciasDeportesDeporte(deporteSeleccionado.getSecuencia());
                contadorDeportesPersonas = administrarDeportes.contarPersonasDeporte(deporteSeleccionado.getSecuencia());
                contadorParametrosInformes = administrarDeportes.contarParametrosInformesDeportes(deporteSeleccionado.getSecuencia());
            } else {
                System.out.println("secuencia borrado : " + deporteSeleccionado.getSecuencia());
                verificarBorradoVigenciasDeportes = administrarDeportes.contarVigenciasDeportesDeporte(deporteSeleccionado.getSecuencia());
                contadorDeportesPersonas = administrarDeportes.contarPersonasDeporte(deporteSeleccionado.getSecuencia());
                contadorParametrosInformes = administrarDeportes.contarParametrosInformesDeportes(deporteSeleccionado.getSecuencia());
            }
            if (!verificarBorradoVigenciasDeportes.equals(new BigInteger("0")) && !contadorDeportesPersonas.equals(new BigInteger("0")) && !contadorParametrosInformes.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                deporteSeleccionado = null;

                verificarBorradoVigenciasDeportes = new BigInteger("-1");
                contadorDeportesPersonas = new BigInteger("-1");
                contadorParametrosInformes = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrarDeporte();
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlDepotes verificarBorrado ERROR " + e);
        }
    }

    public void guardarDeportes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            if (!borrarDeportes.isEmpty()) {
                administrarDeportes.borrarDeportes(borrarDeportes);
                //mostrarBorrados
                registrosBorrados = borrarDeportes.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarDeportes.clear();
            }
            if (!crearDeportes.isEmpty()) {
                administrarDeportes.crearDeportes(crearDeportes);
                crearDeportes.clear();
            }
            if (!modificarDeportes.isEmpty()) {
                administrarDeportes.modificarDeportes(modificarDeportes);
                modificarDeportes.clear();
            }
            listDeportes = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            context.update("form:growl");
            context.update("form:datosDeporte");
            k = 0;
        }
        deporteSeleccionado = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (deporteSeleccionado != null) {
            if (tipoLista == 0) {
                editarDeportes = deporteSeleccionado;
            }
            if (tipoLista == 1) {
                editarDeportes = deporteSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editCodigo");
                context.execute("editCodigo.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editDescripcion");
                context.execute("editDescripcion.show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void agregarNuevoDeportes() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoDeporte.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoDeporte.getCodigo());

            for (int x = 0; x < listDeportes.size(); x++) {
                if (listDeportes.get(x).getCodigo() == nuevoDeporte.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoDeporte.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                tamano = 270;
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDeporte");
                bandera = 0;
                filtrarDeportes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoDeporte.setSecuencia(l);
            crearDeportes.add(nuevoDeporte);
            deporteSeleccionado = nuevoDeporte;
            listDeportes.add(nuevoDeporte);
            nuevoDeporte = new Deportes();
            modificarinfoRegistro(listDeportes.size());
            context.update("form:infoRegistro");
            context.update("form:datosDeporte");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("nuevoRegistroDeporte.hide()");
        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoDeportes() {
        nuevoDeporte = new Deportes();
        deporteSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicarDeportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {
            duplicarDeporte = new Deportes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarDeporte.setSecuencia(l);
                duplicarDeporte.setCodigo(deporteSeleccionado.getCodigo());
                duplicarDeporte.setNombre(deporteSeleccionado.getNombre());
            }
            if (tipoLista == 1) {
                duplicarDeporte.setSecuencia(l);
                duplicarDeporte.setCodigo(deporteSeleccionado.getCodigo());
                duplicarDeporte.setNombre(deporteSeleccionado.getNombre());
            }
            context.update("formularioDialogos:duplicarDepor");
            context.execute("duplicarRegistroDeporte.show()");
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLDEPORTES");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        System.err.println("ConfirmarDuplicar codigo " + duplicarDeporte.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarDeporte.getNombre());

        if (duplicarDeporte.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listDeportes.size(); x++) {
                if (listDeportes.get(x).getCodigo() == duplicarDeporte.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " No pueden existir códigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarDeporte.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarDeporte.getSecuencia() + "  " + duplicarDeporte.getCodigo());
            if (crearDeportes.contains(duplicarDeporte)) {
                System.out.println("Ya lo contengo.");
            }
            listDeportes.add(duplicarDeporte);
            crearDeportes.add(duplicarDeporte);
            context.update("form:datosDeporte");
            deporteSeleccionado = duplicarDeporte;
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");

            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosDeporte:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosDeporte:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDeporte");
                bandera = 0;
                filtrarDeportes = null;
                tipoLista = 0;
            }
            duplicarDeporte = new Deportes();
            modificarinfoRegistro(listDeportes.size());
            RequestContext.getCurrentInstance().execute("duplicarRegistroDeporte.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarDeporte");
            context.execute("validacionDuplicarDeporte.show()");
        }
    }

    public void limpiarDuplicarDeportes() {
        duplicarDeporte = new Deportes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DEPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        deporteSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DEPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        deporteSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (deporteSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(deporteSeleccionado.getSecuencia(), "DEPORTES");
            System.out.println("resultado: " + resultado);
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("DEPORTES")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            modificarinfoRegistro(filtrarDeportes.size());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:infoRegistro");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLDEPORTES eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarinfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void recordarSeleccionDeporte() {
        if (deporteSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosDeportes");
            tablaC.setSelection(deporteSeleccionado);
        }
    }

    public void contarRegistros() {
        if (listDeportes != null) {
            modificarinfoRegistro(listDeportes.size());
        } else {
            modificarinfoRegistro(0);
        }
    }

    //------------------------------------------------------------------------------
    public List<Deportes> getListDeportes() {
        if (listDeportes == null) {
            listDeportes = administrarDeportes.consultarDeportes();
        }
        return listDeportes;
    }

    public void setListDeportes(List<Deportes> listDeportes) {
        this.listDeportes = listDeportes;
    }

    public List<Deportes> getFiltrarDeportes() {
        return filtrarDeportes;
    }

    public void setFiltrarDeportes(List<Deportes> filtrarDeportes) {
        this.filtrarDeportes = filtrarDeportes;
    }

    public Deportes getNuevoDeporte() {
        return nuevoDeporte;
    }

    public void setNuevoDeporte(Deportes nuevoDeporte) {
        this.nuevoDeporte = nuevoDeporte;
    }

    public Deportes getDuplicarDeporte() {
        return duplicarDeporte;
    }

    public void setDuplicarDeporte(Deportes duplicarDeporte) {
        this.duplicarDeporte = duplicarDeporte;
    }

    public Deportes getEditarDeportes() {
        return editarDeportes;
    }

    public void setEditarDeportes(Deportes editarDeportes) {
        this.editarDeportes = editarDeportes;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getPaginaanterior() {
        return paginaanterior;
    }

    public void setPaginaanterior(String paginaanterior) {
        this.paginaanterior = paginaanterior;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Deportes getDeporteSeleccionado() {
        return deporteSeleccionado;
    }

    public void setDeporteSeleccionado(Deportes deporteSeleccionado) {
        this.deporteSeleccionado = deporteSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
