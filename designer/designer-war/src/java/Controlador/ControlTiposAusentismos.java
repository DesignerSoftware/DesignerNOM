/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Tiposausentismos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposAusentismosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlTiposAusentismos implements Serializable {

    @EJB
    AdministrarTiposAusentismosInterface administrarTiposAusentismos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Tiposausentismos> listTiposAusentismos;
    private List<Tiposausentismos> filtrarTiposAusentismos;
    private List<Tiposausentismos> crearTiposAusentismos;
    private List<Tiposausentismos> modificarTiposAusentismos;
    private List<Tiposausentismos> borrarTiposAusentismos;
    private Tiposausentismos nuevoTiposAusentismos;
    private Tiposausentismos duplicarTiposAusentismos;
    private Tiposausentismos editarTiposAusentismos;
    private Tiposausentismos tiposAusentismosSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLOV;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion, paginaAnterior;
    //filtrado table
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;

    public ControlTiposAusentismos() {
        listTiposAusentismos = null;
        crearTiposAusentismos = new ArrayList<Tiposausentismos>();
        modificarTiposAusentismos = new ArrayList<Tiposausentismos>();
        borrarTiposAusentismos = new ArrayList<Tiposausentismos>();
        permitirIndex = true;
        editarTiposAusentismos = new Tiposausentismos();
        nuevoTiposAusentismos = new Tiposausentismos();
        duplicarTiposAusentismos = new Tiposausentismos();
        guardado = true;
        tamano = 270;
        activarLOV = true;
        System.out.println("controlTiposAusentismos Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposAusentismos PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposAusentismos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        getListTiposAusentismos();
        if (listTiposAusentismos != null) {
            if (!listTiposAusentismos.isEmpty()) {
                tiposAusentismosSeleccionado = listTiposAusentismos.get(0);
            }
        }

    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
           contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposAusentismos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(Tiposausentismos tipo, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            tiposAusentismosSeleccionado = tipo;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = tiposAusentismosSeleccionado.getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = tiposAusentismosSeleccionado.getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                tiposAusentismosSeleccionado.getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = tiposAusentismosSeleccionado.getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = tiposAusentismosSeleccionado.getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                tiposAusentismosSeleccionado.getSecuencia();
            }

        }
    }

    public void asignarIndex(Tiposausentismos tipo, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposAusentismos.asignarIndex \n");
            tiposAusentismosSeleccionado = tipo;
            tipoActualizacion = LND;

        } catch (Exception e) {
            System.out.println("ERROR ControlTiposAusentismos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            bandera = 0;
            filtrarTiposAusentismos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposAusentismos.clear();
        crearTiposAusentismos.clear();
        modificarTiposAusentismos.clear();
        tiposAusentismosSeleccionado = null;
        k = 0;
        listTiposAusentismos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposAusentismos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            bandera = 0;
            filtrarTiposAusentismos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposAusentismos.clear();
        crearTiposAusentismos.clear();
        modificarTiposAusentismos.clear();
        tiposAusentismosSeleccionado = null;
        k = 0;
        listTiposAusentismos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            bandera = 0;
            filtrarTiposAusentismos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposAusentismos(Tiposausentismos tipo, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        tiposAusentismosSeleccionado = tipo;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                    if (tiposAusentismosSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    } else if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                    if (tiposAusentismosSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (tiposAusentismosSeleccionado.getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposAusentismos.isEmpty()) {
                            modificarTiposAusentismos.add(tiposAusentismosSeleccionado);
                        } else if (!modificarTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                            modificarTiposAusentismos.add(tiposAusentismosSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                } else {
                    if (tiposAusentismosSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    } else if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                    if (tiposAusentismosSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (tiposAusentismosSeleccionado.getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                }
            } else if (!crearTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                if (tiposAusentismosSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    banderita = true;
                }

                if (tiposAusentismosSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (tiposAusentismosSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposAusentismos.isEmpty()) {
                        modificarTiposAusentismos.add(tiposAusentismosSeleccionado);
                    } else if (!modificarTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                        modificarTiposAusentismos.add(tiposAusentismosSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else {
                if (tiposAusentismosSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    tiposAusentismosSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    banderita = true;
                }

                if (tiposAusentismosSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (tiposAusentismosSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tiposAusentismosSeleccionado.setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {

                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposAusentismos() {

        if (tiposAusentismosSeleccionado != null) {
            System.out.println("Entro a borrandoTiposAusentismos");
            if (!modificarTiposAusentismos.isEmpty() && modificarTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                int modIndex = modificarTiposAusentismos.indexOf(tiposAusentismosSeleccionado);
                modificarTiposAusentismos.remove(modIndex);
                borrarTiposAusentismos.add(tiposAusentismosSeleccionado);
            } else if (!crearTiposAusentismos.isEmpty() && crearTiposAusentismos.contains(tiposAusentismosSeleccionado)) {
                int crearIndex = crearTiposAusentismos.indexOf(tiposAusentismosSeleccionado);
                crearTiposAusentismos.remove(crearIndex);
            } else {
                borrarTiposAusentismos.add(tiposAusentismosSeleccionado);
            }
            listTiposAusentismos.remove(tiposAusentismosSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposAusentismos.remove(tiposAusentismosSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            tiposAusentismosSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarClasesAusentimosTipoAusentismo;
        BigInteger contarSOAusentimosTipoAusentismo;

        try {
            System.err.println("Control Secuencia de ControlTiposAusentismos ");
            if (tipoLista == 0) {
                contarClasesAusentimosTipoAusentismo = administrarTiposAusentismos.contarClasesAusentimosTipoAusentismo(tiposAusentismosSeleccionado.getSecuencia());
                contarSOAusentimosTipoAusentismo = administrarTiposAusentismos.contarSOAusentimosTipoAusentismo(tiposAusentismosSeleccionado.getSecuencia());
            } else {
                contarClasesAusentimosTipoAusentismo = administrarTiposAusentismos.contarClasesAusentimosTipoAusentismo(tiposAusentismosSeleccionado.getSecuencia());
                contarSOAusentimosTipoAusentismo = administrarTiposAusentismos.contarSOAusentimosTipoAusentismo(tiposAusentismosSeleccionado.getSecuencia());
            }
            if (contarClasesAusentimosTipoAusentismo.equals(new BigInteger("0"))
                    && contarSOAusentimosTipoAusentismo.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposAusentismos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                tiposAusentismosSeleccionado = null;
                contarClasesAusentimosTipoAusentismo = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposAusentismos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposAusentismos.isEmpty() || !crearTiposAusentismos.isEmpty() || !modificarTiposAusentismos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposAusentismos");
            if (!borrarTiposAusentismos.isEmpty()) {
                administrarTiposAusentismos.borrarTiposAusentismos(borrarTiposAusentismos);
                //mostrarBorrados
                registrosBorrados = borrarTiposAusentismos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposAusentismos.clear();
            }
            if (!modificarTiposAusentismos.isEmpty()) {
                administrarTiposAusentismos.modificarTiposAusentismos(modificarTiposAusentismos);
                modificarTiposAusentismos.clear();
            }
            if (!crearTiposAusentismos.isEmpty()) {
                administrarTiposAusentismos.crearTiposAusentismos(crearTiposAusentismos);
                crearTiposAusentismos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposAusentismos = null;
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        tiposAusentismosSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (tiposAusentismosSeleccionado != null) {
            if (tipoLista == 0) {
                editarTiposAusentismos = tiposAusentismosSeleccionado;
            }
            if (tipoLista == 1) {
                editarTiposAusentismos = tiposAusentismosSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposAusentismos() {
        System.out.println("agregarNuevoTiposAusentismos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposAusentismos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {

            for (int x = 0; x < listTiposAusentismos.size(); x++) {
                if (listTiposAusentismos.get(x).getCodigo().equals(nuevoTiposAusentismos.getCodigo())) {
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
        if (nuevoTiposAusentismos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposAusentismos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
                bandera = 0;
                filtrarTiposAusentismos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposAusentismos.setSecuencia(l);

            crearTiposAusentismos.add(nuevoTiposAusentismos);
            listTiposAusentismos.add(nuevoTiposAusentismos);
            tiposAusentismosSeleccionado = nuevoTiposAusentismos;
            nuevoTiposAusentismos = new Tiposausentismos();
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            contarRegistros();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposAusentismos').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoTipoAusentismo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoAusentismo').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposAusentismos() {
        System.out.println("limpiarNuevoTiposAusentismos");
        nuevoTiposAusentismos = new Tiposausentismos();

    }

    public void duplicandoTiposAusentismos() {
        System.out.println("duplicandoTiposAusentismos");
        if (tiposAusentismosSeleccionado != null) {
            duplicarTiposAusentismos = new Tiposausentismos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposAusentismos.setSecuencia(l);
                duplicarTiposAusentismos.setCodigo(tiposAusentismosSeleccionado.getCodigo());
                duplicarTiposAusentismos.setDescripcion(tiposAusentismosSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposAusentismos.setSecuencia(l);
                duplicarTiposAusentismos.setCodigo(tiposAusentismosSeleccionado.getCodigo());
                duplicarTiposAusentismos.setDescripcion(tiposAusentismosSeleccionado.getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAusentismos').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposAusentismos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposAusentismos.getDescripcion());

        if (duplicarTiposAusentismos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposAusentismos.size(); x++) {
                if (listTiposAusentismos.get(x).getCodigo().equals(duplicarTiposAusentismos.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposAusentismos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposAusentismos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposAusentismos.getSecuencia() + "  " + duplicarTiposAusentismos.getCodigo());
            if (crearTiposAusentismos.contains(duplicarTiposAusentismos)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposAusentismos.add(duplicarTiposAusentismos);
            crearTiposAusentismos.add(duplicarTiposAusentismos);
            tiposAusentismosSeleccionado = duplicarTiposAusentismos;
            RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAusentismos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAusentismos");
                bandera = 0;
                filtrarTiposAusentismos = null;
                tipoLista = 0;
            }
            duplicarTiposAusentismos = new Tiposausentismos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAusentismos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarTipoAusentismo");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarTipoAusentismo').show()");
        }
    }

    public void limpiarDuplicarTiposAusentismos() {
        duplicarTiposAusentismos = new Tiposausentismos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSAUSENTISMOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSAUSENTISMOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tiposAusentismosSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposAusentismosSeleccionado.getSecuencia(), "TIPOSAUSENTISMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            System.out.println("resultado: " + resultado);
            if (resultado == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSAUSENTISMOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*/GETS Y SETS*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Tiposausentismos> getListTiposAusentismos() {
        if (listTiposAusentismos == null) {
            listTiposAusentismos = administrarTiposAusentismos.consultarTiposAusentismos();
        }
        return listTiposAusentismos;
    }

    public void setListTiposAusentismos(List<Tiposausentismos> listTiposAusentismos) {
        this.listTiposAusentismos = listTiposAusentismos;
    }

    public List<Tiposausentismos> getFiltrarTiposAusentismos() {
        return filtrarTiposAusentismos;
    }

    public void setFiltrarTiposAusentismos(List<Tiposausentismos> filtrarTiposAusentismos) {
        this.filtrarTiposAusentismos = filtrarTiposAusentismos;
    }

    public Tiposausentismos getNuevoTiposAusentismos() {
        return nuevoTiposAusentismos;
    }

    public void setNuevoTiposAusentismos(Tiposausentismos nuevoTiposAusentismos) {
        this.nuevoTiposAusentismos = nuevoTiposAusentismos;
    }

    public Tiposausentismos getDuplicarTiposAusentismos() {
        return duplicarTiposAusentismos;
    }

    public void setDuplicarTiposAusentismos(Tiposausentismos duplicarTiposAusentismos) {
        this.duplicarTiposAusentismos = duplicarTiposAusentismos;
    }

    public Tiposausentismos getEditarTiposAusentismos() {
        return editarTiposAusentismos;
    }

    public void setEditarTiposAusentismos(Tiposausentismos editarTiposAusentismos) {
        this.editarTiposAusentismos = editarTiposAusentismos;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Tiposausentismos getTiposAusentismosSeleccionado() {
        return tiposAusentismosSeleccionado;
    }

    public void setTiposAusentismosSeleccionado(Tiposausentismos tiposAusentismosSeleccioando) {
        this.tiposAusentismosSeleccionado = tiposAusentismosSeleccioando;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposAusentismos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
