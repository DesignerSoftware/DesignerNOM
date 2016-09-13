package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.ActualUsuario;
import Entidades.EersCabeceras;
import Entidades.EersDetalles;
import Entidades.EersFlujos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarATAprobacionHEInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlATAprobacionHE implements Serializable {

    @EJB
    AdministrarATAprobacionHEInterface administrarATAprobacionHE;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private Empleados empleadoActualProceso;
    private String infoRegistroEmpleado;

    private List<EersCabeceras> listaEersCabeceras;
    private List<EersCabeceras> filtrarListaEersCabeceras;
    private EersCabeceras cabeceraSeleccionada;
    private String auxCabeceraEstructura;
    private Date auxCabeceraFechaPago;

    private List<EersFlujos> listaFlujos;
    private List<EersFlujos> filtrarListaFlujos;
    private EersFlujos flujoSeleccionado;

    private List<EersDetalles> listaDetalles;
    private List<EersDetalles> filtrarListaDetalles;
    private EersDetalles detalleSeleccionado;

    private List<Estructuras> lovEstructuras;
    private List<Estructuras> filtrarLovEstructuras;
    private Estructuras estructuraSeleccionada;
    private String infoRegistroEstructura;

    private ActualUsuario actualUsuario;

    private int indexCabecera, cualCeldaCabecera;
    private int indexFlujo, cualCeldaFlujo;
    private int indexDetalle, cualCeldaDetalle;
    private int banderaCabecera, tipoListaCabecera;
    private int banderaFlujo, tipoListaFlujo;
    private int banderaDetalle, tipoListaDetalle;
    private String altoTablaCabecera;
    private String altoTablaFlujo;
    private String altoTablaDetalle;

    private List<EersCabeceras> listEersCabecerasModificar;

    private EersCabeceras editarCabecera;
    private EersFlujos editarFlujo;
    private EersDetalles editarDetalle;

    private boolean guardado;
    private boolean permitirIndexCabecera;
    private BigInteger secRegistro;
    private BigInteger backUpSecRegistro;
    private Date fechaParametro;
    private boolean aceptar;
    private String paginaAnterior;
    private String nombreTablaXML, nombreArchivoXML;
    private boolean activarBuscar, activarMostrarTodos;

    //
    private int numeroScrollCabecera;
    private int rowsCabecera;
    // 
    private String altoDivTablaInferiorIzquierda, altoDivTablaInferiorDerecha;
    private String topDivTablaInferiorIzquierda, topDivTablaInferiorDerecha;
    //
    private Column cabeceraAprobado, cabeceraEstructura, cabeceraNumDocumento, cabeceraNombreEmpleado, cabeceraHoras, cabeceraMinutos, cabeceraNovedad, cabeceraEstado, cabeceraPaso, cabeceraFechaPago, cabeceraObservaciones;
    private Column flujoProceso, flujoFecha;
    private Column detalleConcepto, detalleFecha, detalleObservaciones;

    public ControlATAprobacionHE() {
        actualUsuario = null;

        altoDivTablaInferiorIzquierda = "95px";
        topDivTablaInferiorIzquierda = "37px";

        altoDivTablaInferiorDerecha = "95px";
        topDivTablaInferiorDerecha = "37px";

        activarBuscar = false;
        activarMostrarTodos = true;

        listaEersCabeceras = null;
        cabeceraSeleccionada = new EersCabeceras();
        empleadoActualProceso = null;
        listaEersCabeceras = null;
        flujoSeleccionado = new EersFlujos();
        listaDetalles = null;
        detalleSeleccionado = new EersDetalles();

        listEersCabecerasModificar = new ArrayList<EersCabeceras>();

        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovEstructuras = null;
        estructuraSeleccionada = new Estructuras();

        editarCabecera = new EersCabeceras();
        editarFlujo = new EersFlujos();
        editarDetalle = new EersDetalles();

        indexCabecera = -1;
        indexFlujo = -1;
        indexDetalle = -1;
        cualCeldaCabecera = -1;
        cualCeldaFlujo = -1;
        cualCeldaDetalle = -1;
        banderaCabecera = 0;
        banderaFlujo = 0;
        banderaDetalle = 0;
        tipoListaCabecera = 0;
        tipoListaFlujo = 0;
        tipoListaDetalle = 0;
        altoTablaCabecera = "80";
        altoTablaFlujo = "110";
        altoTablaDetalle = "85";

        guardado = true;
        permitirIndexCabecera = true;
        aceptar = true;

        numeroScrollCabecera = 505;
        rowsCabecera = 20;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarATAprobacionHE.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            numeroScrollCabecera = 505;
            rowsCabecera = 20;
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlATAprobacionHE: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String page) {
        paginaAnterior = page;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public int obtenerNumeroScrollCabecera() {
        return numeroScrollCabecera;
    }

    public void pruebaRemota() {
        RequestContext context = RequestContext.getCurrentInstance();
        int tam = 0;
        if (tipoListaCabecera == 0) {
            tam = listaEersCabeceras.size();
        } else {
            tam = filtrarListaEersCabeceras.size();
        }
        if (rowsCabecera < tam) {
            rowsCabecera = rowsCabecera + 20;
            numeroScrollCabecera = numeroScrollCabecera + 500;
            PrimefacesContextUI.ejecutar("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().update("form:PanelTotal");
        }
    }

    public void modificarCabecera(int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaCabecera == 0) {
            if (listEersCabecerasModificar.isEmpty()) {
                listEersCabecerasModificar.add(listaEersCabeceras.get(indice));
            } else if (!listEersCabecerasModificar.contains(listaEersCabeceras.get(indice))) {
                listEersCabecerasModificar.add(listaEersCabeceras.get(indice));
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            indexCabecera = -1;
            secRegistro = null;
        } else {
            if (listEersCabecerasModificar.isEmpty()) {
                listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indice));
            } else if (!listEersCabecerasModificar.contains(filtrarListaEersCabeceras.get(indice))) {
                listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indice));
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            indexCabecera = -1;
            secRegistro = null;
        }
        RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
        RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
    }

    public void modificarCabecera(int indice, String confirmarCambio, String valorConfirmar) {
        indexCabecera = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
            if (tipoListaCabecera == 0) {
                listaEersCabeceras.get(indice).getEstructuraaprueba().setNombre(auxCabeceraEstructura);
            } else {
                filtrarListaEersCabeceras.get(indice).getEstructuraaprueba().setNombre(auxCabeceraEstructura);
            }
            for (int i = 0; i < lovEstructuras.size(); i++) {
                if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaCabecera == 0) {
                    listaEersCabeceras.get(indice).setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                } else {
                    filtrarListaEersCabeceras.get(indice).setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                }
                lovEstructuras.clear();
                getLovEstructuras();
            } else {
                permitirIndexCabecera = false;
                RequestContext.getCurrentInstance().update("formEstructura:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
            }
        }
        if (coincidencias == 1) {
            if (tipoListaCabecera == 0) {
                if (listEersCabecerasModificar.isEmpty()) {
                    listEersCabecerasModificar.add(listaEersCabeceras.get(indice));
                } else if (!listEersCabecerasModificar.contains(listaEersCabeceras.get(indice))) {
                    listEersCabecerasModificar.add(listaEersCabeceras.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexCabecera = -1;
                secRegistro = null;
            } else {
                if (listEersCabecerasModificar.isEmpty()) {
                    listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indice));
                } else if (!listEersCabecerasModificar.contains(filtrarListaEersCabeceras.get(indice))) {
                    listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexCabecera = -1;
                secRegistro = null;
            }
        }
        RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
        RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
    }

    public boolean validarFechasRegistroCabecera(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            EersCabeceras auxiliar = null;
            if (tipoListaCabecera == 0) {
                auxiliar = listaEersCabeceras.get(indexCabecera);
            } else {
                auxiliar = filtrarListaEersCabeceras.get(indexCabecera);
            }
            if (auxiliar.getFechapago() != null) {
                if (auxiliar.getFechapago().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechaCabecera(int i, int c) {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean retorno = false;
        indexCabecera = i;
        retorno = validarFechasRegistroCabecera(0);
        if (retorno == true) {
            cambiarIndiceCabecera(i, c);
            modificarCabecera(i);
        } else {
            if (tipoListaCabecera == 0) {
                listaEersCabeceras.get(i).setFechapago(auxCabeceraFechaPago);
            } else {
                filtrarListaEersCabeceras.get(i).setFechapago(auxCabeceraFechaPago);
            }
            RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
            RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
            PrimefacesContextUI.ejecutar("PF('errorFechaCabecera').show()");
        }
    }

    public void posicionCabecera() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceCabecera(indice, columna);
    }

    public void cambiarIndiceCabecera(int i, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndexCabecera == true) {
            indexCabecera = i;
            cualCeldaCabecera = celda;
            lovEstructuras = null;
            indexFlujo = -1;
            indexDetalle = -1;

            listaFlujos = null;
            listaDetalles = null;

            if (tipoListaCabecera == 0) {
                secRegistro = listaEersCabeceras.get(indexCabecera).getSecuencia();
                auxCabeceraEstructura = listaEersCabeceras.get(indexCabecera).getEstructuraaprueba().getNombre();
                auxCabeceraFechaPago = listaEersCabeceras.get(indexCabecera).getFechapago();
                //
                if (cualCeldaCabecera >= 0 && cualCeldaCabecera <= 3) {
                    cabeceraSeleccionada = listaEersCabeceras.get(indexCabecera);
                    RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
                } else if (cualCeldaCabecera >= 4) {
                    cabeceraSeleccionada = listaEersCabeceras.get(indexCabecera);
                    RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
                }
                //
                lovEstructuras = administrarATAprobacionHE.lovEstructuras(listaEersCabeceras.get(indexCabecera).getEerestado().getSecuencia());
            } else {
                secRegistro = filtrarListaEersCabeceras.get(indexCabecera).getSecuencia();
                auxCabeceraEstructura = filtrarListaEersCabeceras.get(indexCabecera).getEstructuraaprueba().getNombre();
                auxCabeceraFechaPago = filtrarListaEersCabeceras.get(indexCabecera).getFechapago();
                //
                if (cualCeldaCabecera >= 0 && cualCeldaCabecera <= 3) {
                    cabeceraSeleccionada = filtrarListaEersCabeceras.get(indexCabecera);
                    RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
                } else if (cualCeldaCabecera >= 4) {
                    cabeceraSeleccionada = filtrarListaEersCabeceras.get(indexCabecera);
                    RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
                }
                //
                lovEstructuras = administrarATAprobacionHE.lovEstructuras(filtrarListaEersCabeceras.get(indexCabecera).getEerestado().getSecuencia());
            }

            if (banderaFlujo == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
                flujoFecha.setFilterStyle("display: none; visibility: hidden;");
                flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
                flujoProceso.setFilterStyle("display: none; visibility: hidden;");
                altoTablaFlujo = "110";
                RequestContext.getCurrentInstance().update("form:datosFlujo");
                banderaFlujo = 0;
                filtrarListaFlujos = null;
                tipoListaFlujo = 0;
            }
            if (banderaDetalle == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
                detalleFecha.setFilterStyle("display: none; visibility: hidden;");
                detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
                detalleObservaciones.setFilterStyle("display: none; visibility: hidden;");
                altoTablaDetalle = "85";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalle = 0;
                filtrarListaDetalles = null;
                tipoListaDetalle = 0;
            }
            getListaFlujos();
            getListaDetalles();
            RequestContext.getCurrentInstance().update("form:datosFlujo");
        }
    }

    public void posicionFlujo() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceFlujo(indice, columna);
    }

    public void cambiarIndiceFlujo(int i, int celda) {
        indexCabecera = -1;
        indexFlujo = i;
        cualCeldaFlujo = celda;
        indexDetalle = -1;
        if (tipoListaFlujo == 0) {
            secRegistro = listaFlujos.get(indexFlujo).getSecuencia();
        } else {
            secRegistro = filtrarListaFlujos.get(indexFlujo).getSecuencia();
        }
    }

    public void posicionDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceDetalle(indice, columna);
    }

    public void cambiarIndiceDetalle(int i, int celda) {
        indexCabecera = -1;
        indexFlujo = -1;
        indexDetalle = i;
        cualCeldaDetalle = celda;
        if (tipoListaDetalle == 0) {
            secRegistro = listaDetalles.get(indexDetalle).getSecuencia();
        } else {
            secRegistro = filtrarListaDetalles.get(indexDetalle).getSecuencia();
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexCabecera >= 0) {
            if (tipoListaCabecera == 0) {
                editarCabecera = listaEersCabeceras.get(indexCabecera);
            } else {
                editarCabecera = filtrarListaEersCabeceras.get(indexCabecera);
            }
            if (cualCeldaCabecera == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraAprobado");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraAprobado').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraSiguiente");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraSiguiente').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraNumDocumento");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraNumDocumento').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmplNombre");
                PrimefacesContextUI.ejecutar("PF('editarEmplNombre').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraHoras");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraHoras').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraMinuto");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraMinuto').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraNovedad");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraNovedad').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraEstado");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraEstado').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraPaso");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraPaso').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraFechaPago");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraFechaPago').show()");
                cualCeldaCabecera = -1;
            }
            if (cualCeldaCabecera == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCabeceraObservacion");
                PrimefacesContextUI.ejecutar("PF('editarCabeceraObservacion').show()");
                cualCeldaCabecera = -1;
            }
            indexCabecera = -1;
            secRegistro = null;
        }
        if (indexFlujo >= 0) {
            if (tipoListaFlujo == 0) {
                editarFlujo = listaFlujos.get(indexFlujo);
            } else {
                editarFlujo = filtrarListaFlujos.get(indexFlujo);
            }
            if (cualCeldaFlujo == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFlujoProceso");
                PrimefacesContextUI.ejecutar("PF('editarFlujoProceso').show()");
                cualCeldaFlujo = -1;
            }
            if (cualCeldaFlujo == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFlujoFecha");
                PrimefacesContextUI.ejecutar("PF('editarFlujoFecha').show()");
                cualCeldaFlujo = -1;
            }
            indexFlujo = -1;
            secRegistro = null;
        }
        if (indexDetalle >= 0) {
            if (tipoListaDetalle == 0) {
                editarDetalle = listaDetalles.get(indexDetalle);
            } else {
                editarDetalle = filtrarListaDetalles.get(indexDetalle);
            }
            if (cualCeldaDetalle == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleConcepto");
                PrimefacesContextUI.ejecutar("PF('editarDetalleConcepto').show()");
                cualCeldaDetalle = -1;
            }
            if (cualCeldaDetalle == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleFecha");
                PrimefacesContextUI.ejecutar("PF('editarDetalleFecha').show()");
                cualCeldaDetalle = -1;
            }
            if (cualCeldaDetalle == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleObservacion");
                PrimefacesContextUI.ejecutar("PF('editarDetalleObservacion').show()");
                cualCeldaDetalle = -1;
            }
            indexDetalle = -1;
            secRegistro = null;
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexCabecera >= 0) {
            if (cualCeldaCabecera == 3) {
                RequestContext.getCurrentInstance().update("formEstructura:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
                cualCeldaCabecera = -1;
            }
            secRegistro = null;
        }
    }

    public void asignarIndex(int indice, int dialogo) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexCabecera >= 0) {
            indexCabecera = indice;
            if (dialogo == 0) {
                RequestContext.getCurrentInstance().update("formEstructura:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
            }
        }
    }

    public void cancelarModificacion() {

        if (banderaCabecera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            cabeceraAprobado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraAprobado");
            cabeceraAprobado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstructura = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraEstructura");
            cabeceraEstructura.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNumDocumento = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNumDocumento");
            cabeceraNumDocumento.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNombreEmpleado");
            cabeceraNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraHoras = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraHoras");
            cabeceraHoras.setFilterStyle("display: none; visibility: hidden;");
            cabeceraMinutos = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraMinutos");
            cabeceraMinutos.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNovedad = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNovedad");
            cabeceraNovedad.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraEstado");
            cabeceraEstado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraPaso = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraPaso");
            cabeceraPaso.setFilterStyle("display: none; visibility: hidden;");
            cabeceraFechaPago = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraFechaPago");
            cabeceraFechaPago.setFilterStyle("display: none; visibility: hidden;");
            cabeceraObservaciones = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraObservaciones");
            cabeceraObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaCabecera = "80";
            banderaCabecera = 0;
            filtrarListaEersCabeceras = null;
            tipoListaCabecera = 0;

            RequestContext.getCurrentInstance().update("form:tablaSuperiorIzquierda");
            RequestContext.getCurrentInstance().update("form:tablaSuperiorDerecha");

            altoDivTablaInferiorIzquierda = "95px";
            topDivTablaInferiorIzquierda = "37px";

            altoDivTablaInferiorDerecha = "95px";
            topDivTablaInferiorDerecha = "37px";

            RequestContext.getCurrentInstance().update("form:PanelTotal");
        }

        if (banderaFlujo == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
            flujoFecha.setFilterStyle("display: none; visibility: hidden;");
            flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
            flujoProceso.setFilterStyle("display: none; visibility: hidden;");
            altoTablaFlujo = "110";
            RequestContext.getCurrentInstance().update("form:datosFlujo");
            banderaFlujo = 0;
            filtrarListaFlujos = null;
            tipoListaFlujo = 0;
        }

        if (banderaDetalle == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
            detalleFecha.setFilterStyle("display: none; visibility: hidden;");
            detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
            detalleObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalle = "85";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalle = 0;
            filtrarListaDetalles = null;
            tipoListaDetalle = 0;
        }

        listaEersCabeceras = null;
        cargarDatosNuevosCabecera(1);

        listaFlujos = null;
        listaDetalles = null;

        indexCabecera = -1;
        indexFlujo = -1;
        indexDetalle = -1;
        cualCeldaCabecera = -1;
        cualCeldaFlujo = -1;
        cualCeldaDetalle = -1;
        banderaCabecera = 0;
        banderaFlujo = 0;
        banderaDetalle = 0;
        tipoListaCabecera = 0;
        tipoListaFlujo = 0;
        tipoListaDetalle = 0;
        altoTablaCabecera = "80";
        altoTablaFlujo = "110";
        altoTablaDetalle = "85";

        guardado = true;
        secRegistro = null;
        aceptar = true;

        activarBuscar = false;
        activarMostrarTodos = true;

        numeroScrollCabecera = 505;
        rowsCabecera = 20;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:PanelTotal");
    }

    public void salir() {

        if (banderaCabecera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            cabeceraAprobado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraAprobado");
            cabeceraAprobado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstructura = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraEstructura");
            cabeceraEstructura.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNumDocumento = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNumDocumento");
            cabeceraNumDocumento.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNombreEmpleado");
            cabeceraNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraHoras = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraHoras");
            cabeceraHoras.setFilterStyle("display: none; visibility: hidden;");
            cabeceraMinutos = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraMinutos");
            cabeceraMinutos.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNovedad = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNovedad");
            cabeceraNovedad.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraEstado");
            cabeceraEstado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraPaso = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraPaso");
            cabeceraPaso.setFilterStyle("display: none; visibility: hidden;");
            cabeceraFechaPago = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraFechaPago");
            cabeceraFechaPago.setFilterStyle("display: none; visibility: hidden;");
            cabeceraObservaciones = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraObservaciones");
            cabeceraObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaCabecera = "80";
            banderaCabecera = 0;
            filtrarListaEersCabeceras = null;
            tipoListaCabecera = 0;

            RequestContext.getCurrentInstance().update("form:tablaSuperiorIzquierda");
            RequestContext.getCurrentInstance().update("form:tablaSuperiorDerecha");

            altoDivTablaInferiorIzquierda = "95px";
            topDivTablaInferiorIzquierda = "37px";

            altoDivTablaInferiorDerecha = "95px";
            topDivTablaInferiorDerecha = "37px";

            RequestContext.getCurrentInstance().update("form:PanelTotal");
        }

        if (banderaFlujo == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
            flujoFecha.setFilterStyle("display: none; visibility: hidden;");
            flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
            flujoProceso.setFilterStyle("display: none; visibility: hidden;");
            altoTablaFlujo = "110";
            RequestContext.getCurrentInstance().update("form:datosFlujo");
            banderaFlujo = 0;
            filtrarListaFlujos = null;
            tipoListaFlujo = 0;
        }

        if (banderaDetalle == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
            detalleFecha.setFilterStyle("display: none; visibility: hidden;");
            detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
            detalleObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalle = "85";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalle = 0;
            filtrarListaDetalles = null;
            tipoListaDetalle = 0;
        }

        listaEersCabeceras = null;
        listaFlujos = null;
        listaDetalles = null;

        indexCabecera = -1;
        indexFlujo = -1;
        indexDetalle = -1;
        cualCeldaCabecera = -1;
        cualCeldaFlujo = -1;
        cualCeldaDetalle = -1;
        banderaCabecera = 0;
        banderaFlujo = 0;
        banderaDetalle = 0;
        tipoListaCabecera = 0;
        tipoListaFlujo = 0;
        tipoListaDetalle = 0;
        altoTablaCabecera = "80";
        altoTablaFlujo = "110";
        altoTablaDetalle = "85";

        guardado = true;
        secRegistro = null;
        aceptar = true;

        activarBuscar = false;
        activarMostrarTodos = true;

        numeroScrollCabecera = 505;
        rowsCabecera = 20;
    }

    public void guardadoGeneral() {
        if (guardado == false) {
            guardarCambiosCabecera();
        }
    }

    public void guardarCambiosCabecera() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listEersCabecerasModificar.isEmpty()) {
                administrarATAprobacionHE.editarEersCabeceras(listEersCabecerasModificar);
                listEersCabecerasModificar.clear();
            }
            listaEersCabeceras = null;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            indexCabecera = -1;
            secRegistro = null;
            cancelarModificacion();
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Conceptos A Aprobar con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosCabecera Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Conceptos A Aprobar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void dispararDialogoBuscarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void mostrarTodosEmpleados() {
        if (guardado == true) {
            cancelarModificacion();
            numeroScrollCabecera = 505;
            rowsCabecera = 20;
            cargarDatosNuevosCabecera(1);
        } else {
            RequestContext.getCurrentInstance().execute("confirmarGuardar').show()");
        }
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (banderaCabecera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            cabeceraAprobado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraAprobado");
            cabeceraAprobado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstructura = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraEstructura");
            cabeceraEstructura.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNumDocumento = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNumDocumento");
            cabeceraNumDocumento.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNombreEmpleado");
            cabeceraNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraHoras = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraHoras");
            cabeceraHoras.setFilterStyle("display: none; visibility: hidden;");
            cabeceraMinutos = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraMinutos");
            cabeceraMinutos.setFilterStyle("display: none; visibility: hidden;");
            cabeceraNovedad = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNovedad");
            cabeceraNovedad.setFilterStyle("display: none; visibility: hidden;");
            cabeceraEstado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraEstado");
            cabeceraEstado.setFilterStyle("display: none; visibility: hidden;");
            cabeceraPaso = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraPaso");
            cabeceraPaso.setFilterStyle("display: none; visibility: hidden;");
            cabeceraFechaPago = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraFechaPago");
            cabeceraFechaPago.setFilterStyle("display: none; visibility: hidden;");
            cabeceraObservaciones = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraObservaciones");
            cabeceraObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaCabecera = "80";
            banderaCabecera = 0;
            filtrarListaEersCabeceras = null;
            tipoListaCabecera = 0;

            RequestContext.getCurrentInstance().update("form:tablaSuperiorIzquierda");
            RequestContext.getCurrentInstance().update("form:tablaSuperiorDerecha");

            altoDivTablaInferiorIzquierda = "95px";
            topDivTablaInferiorIzquierda = "37px";

            altoDivTablaInferiorDerecha = "95px";
            topDivTablaInferiorDerecha = "37px";

            RequestContext.getCurrentInstance().update("form:PanelTotal");
        }

        if (banderaFlujo == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
            flujoFecha.setFilterStyle("display: none; visibility: hidden;");
            flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
            flujoProceso.setFilterStyle("display: none; visibility: hidden;");
            altoTablaFlujo = "110";
            RequestContext.getCurrentInstance().update("form:datosFlujo");
            banderaFlujo = 0;
            filtrarListaFlujos = null;
            tipoListaFlujo = 0;
        }

        if (banderaDetalle == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
            detalleFecha.setFilterStyle("display: none; visibility: hidden;");
            detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
            detalleObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalle = "85";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalle = 0;
            filtrarListaDetalles = null;
            tipoListaDetalle = 0;
        }

        listaEersCabeceras = null;
        listaFlujos = null;
        empleadoActualProceso = empleadoSeleccionado;
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        indexCabecera = -1;
        secRegistro = null;

        activarBuscar = true;
        activarMostrarTodos = false;

        numeroScrollCabecera = 505;
        rowsCabecera = 20;
        /*
         RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
         RequestContext.getCurrentInstance().update("formEmpleado:lovEmpleado");
         RequestContext.getCurrentInstance().update("formEmpleado:aceptarE");*/
        context.reset("formEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:panelTotal");

        cargarDatosNuevosCabecera(2);
    }

    public void cargarDatosNuevosCabecera(int opcion) {
        try {
            if (opcion == 1) {
                listaEersCabeceras = administrarATAprobacionHE.obtenerTotalesEersCabeceras();
            }
            if (opcion == 2) {
                listaEersCabeceras = administrarATAprobacionHE.obtenerEersCabecerasPorEmpleado(empleadoActualProceso.getSecuencia());
            }
            if (listaEersCabeceras != null) {
                for (int i = 0; i < listaEersCabeceras.size(); i++) {
                    if (listaEersCabeceras.get(i).getEstructuraaprueba() == null) {
                        listaEersCabeceras.get(i).setEstructuraaprueba(new Estructuras());
                    }
                }
            }
            Thread.sleep(2000L);
            RequestContext.getCurrentInstance().update("form:PanelTotal");
            System.out.println("Ejecuto Time");
            RequestContext.getCurrentInstance().execute("operacionEnProceso').hide()");

        } catch (Exception e) {
            System.out.println("Error cargarDatosNuevos Controlador : " + e.toString());
        }
    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        indexCabecera = -1;
        secRegistro = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");
    }

    public void actualizarEstructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaCabecera == 0) {
            listaEersCabeceras.get(indexCabecera).setEstructuraaprueba(estructuraSeleccionada);
            if (listEersCabecerasModificar.isEmpty()) {
                listEersCabecerasModificar.add(listaEersCabeceras.get(indexCabecera));
            } else if (!listEersCabecerasModificar.contains(listaEersCabeceras.get(indexCabecera))) {
                listEersCabecerasModificar.add(listaEersCabeceras.get(indexCabecera));
            }

        } else {
            filtrarListaEersCabeceras.get(indexCabecera).setEstructuraaprueba(estructuraSeleccionada);
            if (listEersCabecerasModificar.isEmpty()) {
                listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indexCabecera));
            } else if (!listEersCabecerasModificar.contains(filtrarListaEersCabeceras.get(indexCabecera))) {
                listEersCabecerasModificar.add(filtrarListaEersCabeceras.get(indexCabecera));
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosCabecera");
        permitirIndexCabecera = true;
        filtrarLovEstructuras = null;
        estructuraSeleccionada = null;
        aceptar = true;
        indexCabecera = -1;
        secRegistro = null;
        /*
         RequestContext.getCurrentInstance().update("formEstructura:EstructuraDialogo");
         RequestContext.getCurrentInstance().update("formEstructura:lovEstructura");
         RequestContext.getCurrentInstance().update("formEstructura:aceptarEA");*/
        context.reset("formEstructura:lovEstructura:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");
    }

    public void cancelarCambioEstructura() {
        filtrarLovEstructuras = null;
        estructuraSeleccionada = null;
        aceptar = true;
        indexCabecera = -1;
        secRegistro = null;
        permitirIndexCabecera = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formEstructura:lovEstructura:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");
    }

    public void eventoFiltrar() {
        if (indexCabecera >= 0) {
            if (tipoListaCabecera == 0) {
                tipoListaCabecera = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
            RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
        }
        if (indexFlujo >= 0) {
            if (tipoListaFlujo == 0) {
                tipoListaFlujo = 1;
            }
        }
        if (indexDetalle >= 0) {
            if (tipoListaDetalle == 0) {
                tipoListaDetalle = 1;
            }
        }
    }

    public void organizarTablas() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tablaInferiorIzquierda");
        RequestContext.getCurrentInstance().update("form:tablaInferiorDerecha");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (indexCabecera >= 0) {
            if (banderaCabecera == 0) {
                cabeceraAprobado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraAprobado");
                cabeceraAprobado.setFilterStyle("width: 66px");
                cabeceraEstructura = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraEstructura");
                cabeceraEstructura.setFilterStyle("width: 66px");
                cabeceraNumDocumento = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNumDocumento");
                cabeceraNumDocumento.setFilterStyle("width: 66px");
                cabeceraNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNombreEmpleado");
                cabeceraNombreEmpleado.setFilterStyle("width: 66px");
                cabeceraHoras = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraHoras");
                cabeceraHoras.setFilterStyle("width: 66px");
                cabeceraMinutos = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraMinutos");
                cabeceraMinutos.setFilterStyle("width: 66px");
                cabeceraNovedad = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNovedad");
                cabeceraNovedad.setFilterStyle("width: 66px");
                cabeceraEstado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraEstado");
                cabeceraEstado.setFilterStyle("width: 66px");
                cabeceraPaso = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraPaso");
                cabeceraPaso.setFilterStyle("width: 66px");
                cabeceraFechaPago = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraFechaPago");
                cabeceraFechaPago.setFilterStyle("width: 66px");
                cabeceraObservaciones = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraObservaciones");
                cabeceraObservaciones.setFilterStyle("width: 66px");
                altoTablaCabecera = "62";
                banderaCabecera = 1;

                RequestContext.getCurrentInstance().update("form:tablaSuperiorIzquierda");
                RequestContext.getCurrentInstance().update("form:tablaSuperiorDerecha");

                altoDivTablaInferiorIzquierda = "77px";
                altoDivTablaInferiorDerecha = "77px";

                topDivTablaInferiorIzquierda = "59px";
                topDivTablaInferiorDerecha = "59px";

                RequestContext.getCurrentInstance().update("form:PanelTotal");

            } else if (banderaCabecera == 1) {
                //CERRAR FILTRADO
                cabeceraAprobado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraAprobado");
                cabeceraAprobado.setFilterStyle("display: none; visibility: hidden;");
                cabeceraEstructura = (Column) c.getViewRoot().findComponent("form:tablaSuperiorIzquierda:cabeceraEstructura");
                cabeceraEstructura.setFilterStyle("display: none; visibility: hidden;");
                cabeceraNumDocumento = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNumDocumento");
                cabeceraNumDocumento.setFilterStyle("display: none; visibility: hidden;");
                cabeceraNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNombreEmpleado");
                cabeceraNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
                cabeceraHoras = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraHoras");
                cabeceraHoras.setFilterStyle("display: none; visibility: hidden;");
                cabeceraMinutos = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraMinutos");
                cabeceraMinutos.setFilterStyle("display: none; visibility: hidden;");
                cabeceraNovedad = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraNovedad");
                cabeceraNovedad.setFilterStyle("display: none; visibility: hidden;");
                cabeceraEstado = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraEstado");
                cabeceraEstado.setFilterStyle("display: none; visibility: hidden;");
                cabeceraPaso = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraPaso");
                cabeceraPaso.setFilterStyle("display: none; visibility: hidden;");
                cabeceraFechaPago = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraFechaPago");
                cabeceraFechaPago.setFilterStyle("display: none; visibility: hidden;");
                cabeceraObservaciones = (Column) c.getViewRoot().findComponent("form:tablaSuperiorDerecha:cabeceraObservaciones");
                cabeceraObservaciones.setFilterStyle("display: none; visibility: hidden;");
                altoTablaCabecera = "80";
                banderaCabecera = 0;
                filtrarListaEersCabeceras = null;
                tipoListaCabecera = 0;

                RequestContext.getCurrentInstance().update("form:tablaSuperiorIzquierda");
                RequestContext.getCurrentInstance().update("form:tablaSuperiorDerecha");

                altoDivTablaInferiorIzquierda = "95px";
                topDivTablaInferiorIzquierda = "37px";

                altoDivTablaInferiorDerecha = "95px";
                topDivTablaInferiorDerecha = "37px";

                RequestContext.getCurrentInstance().update("form:PanelTotal");
            }
        }
        if (indexFlujo >= 0) {
            if (banderaFlujo == 0) {
                flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
                flujoFecha.setFilterStyle("width: 85%;");
                flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
                flujoProceso.setFilterStyle("width: 85%;");
                altoTablaFlujo = "88";
                RequestContext.getCurrentInstance().update("form:datosFlujo");
                banderaFlujo = 1;
            } else if (banderaFlujo == 1) {
                flujoFecha = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoFecha");
                flujoFecha.setFilterStyle("display: none; visibility: hidden;");
                flujoProceso = (Column) c.getViewRoot().findComponent("form:datosFlujo:flujoProceso");
                flujoProceso.setFilterStyle("display: none; visibility: hidden;");
                altoTablaFlujo = "110";
                RequestContext.getCurrentInstance().update("form:datosFlujo");
                banderaFlujo = 0;
                filtrarListaFlujos = null;
                tipoListaFlujo = 0;
            }
        }
        if (indexDetalle >= 0) {
            if (banderaDetalle == 0) {
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("width: 85%;");
                detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
                detalleFecha.setFilterStyle("width: 85%;");
                detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
                detalleObservaciones.setFilterStyle("width: 85%;");
                altoTablaFlujo = "63";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalle = 1;
            } else if (banderaDetalle == 1) {
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleFecha = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFecha");
                detalleFecha.setFilterStyle("display: none; visibility: hidden;");
                detalleObservaciones = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleObservaciones");
                detalleObservaciones.setFilterStyle("display: none; visibility: hidden;");
                altoTablaDetalle = "85";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalle = 0;
                filtrarListaDetalles = null;
                tipoListaDetalle = 0;
            }
        }
    }

    public void exportPDF() throws IOException {
        if (indexCabecera >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosCabeceraExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "Conceptos_A_Aprobar_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexCabecera = -1;
            secRegistro = null;
        }
        if (indexFlujo >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosFlujoExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "FlujosAprobados_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexCabecera = -1;
            secRegistro = null;
        }
        if (indexDetalle >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosDetalleExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "Detalles_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexDetalle = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (indexCabecera >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosCabeceraExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "Conceptos_A_Aprobar_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexCabecera = -1;
            secRegistro = null;
        }
        if (indexFlujo >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosFlujoExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "FlujosAprobados_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexFlujo = -1;
            secRegistro = null;
        }
        if (indexDetalle >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosDetalleExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "Detalles_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexDetalle = -1;
            secRegistro = null;
        }
    }

    public String obtenerTablaXML() {
        if (indexCabecera >= 0) {
            nombreTablaXML = ":formExportar:datosCabeceraExportar";
        }
        if (indexFlujo >= 0) {
            nombreTablaXML = ":formExportar:datosFlujoExportar";
        }
        if (indexDetalle >= 0) {
            nombreTablaXML = ":formExportar:datosDetalleExportar";
        }
        return nombreTablaXML;
    }

    public String obtenerNombreArchivoXML() {
        if (indexCabecera >= 0) {
            nombreArchivoXML = "Conceptos_A_Aprobar_XML";
        }
        if (indexFlujo >= 0) {
            nombreArchivoXML = "FlujosAprobados_XML";
        }
        if (indexDetalle >= 0) {
            nombreArchivoXML = "Detalles_XML";
        }
        return nombreArchivoXML;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void validarRastro() {
        if (indexCabecera >= 0) {
            verificarRastroCabecera();
        }
    }

    public void verificarRastroCabecera() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaEersCabeceras != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "EERSCABECERAS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("EERSCABECERAS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        indexCabecera = -1;
    }

    public void guardarSalir() {
        guardadoGeneral();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    //GET - SET
    public List<Empleados> getLovEmpleados() {
        lovEmpleados = administrarATAprobacionHE.lovEmpleados();
        if (lovEmpleados != null) {
            infoRegistroEmpleado = "Cantidad de registros : " + lovEmpleados.size();
        } else {
            infoRegistroEmpleado = "Cantidad de registros : 0";
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Empleados getEmpleadoActualProceso() {
        if (empleadoActualProceso == null) {
            getLovEmpleados();
            if (lovEmpleados != null) {
                if (lovEmpleados.size() > 0) {
                    empleadoActualProceso = lovEmpleados.get(0);
                }
            }
        }
        return empleadoActualProceso;
    }

    public void setEmpleadoActualProceso(Empleados empleadoActualProceso) {
        this.empleadoActualProceso = empleadoActualProceso;
    }

    public List<EersCabeceras> getListaEersCabeceras() {
        if (listaEersCabeceras == null) {
            listaEersCabeceras = administrarATAprobacionHE.obtenerTotalesEersCabeceras();
            if (listaEersCabeceras != null) {
                for (int i = 0; i < listaEersCabeceras.size(); i++) {
                    if (listaEersCabeceras.get(i).getEstructuraaprueba() == null) {
                        listaEersCabeceras.get(i).setEstructuraaprueba(new Estructuras());
                    }
                }
            }
        }

        return listaEersCabeceras;
    }

    public void setListaEersCabeceras(List<EersCabeceras> listaEersCabeceras) {
        this.listaEersCabeceras = listaEersCabeceras;
    }

    public List<EersCabeceras> getFiltrarListaEersCabeceras() {
        return filtrarListaEersCabeceras;
    }

    public void setFiltrarListaEersCabeceras(List<EersCabeceras> filtrarListaEersCabeceras) {
        this.filtrarListaEersCabeceras = filtrarListaEersCabeceras;
    }

    public EersCabeceras getCabeceraSeleccionada() {
        return cabeceraSeleccionada;
    }

    public void setCabeceraSeleccionada(EersCabeceras cabeceraSeleccionada) {
        this.cabeceraSeleccionada = cabeceraSeleccionada;
    }

    public List<Estructuras> getLovEstructuras() {
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public List<Estructuras> getFiltrarLovEstructuras() {
        return filtrarLovEstructuras;
    }

    public void setFiltrarLovEstructuras(List<Estructuras> filtrarLovEstructuras) {
        this.filtrarLovEstructuras = filtrarLovEstructuras;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
        this.estructuraSeleccionada = estructuraSeleccionada;
    }

    public String getAltoTablaCabecera() {
        return altoTablaCabecera;
    }

    public void setAltoTablaCabecera(String altoTablaCabecera) {
        this.altoTablaCabecera = altoTablaCabecera;
    }

    public EersCabeceras getEditarCabecera() {
        return editarCabecera;
    }

    public void setEditarCabecera(EersCabeceras editarCabecera) {
        this.editarCabecera = editarCabecera;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isActivarBuscar() {
        return activarBuscar;
    }

    public void setActivarBuscar(boolean activarBuscar) {
        this.activarBuscar = activarBuscar;
    }

    public boolean isActivarMostrarTodos() {
        return activarMostrarTodos;
    }

    public void setActivarMostrarTodos(boolean activarMostrarTodos) {
        this.activarMostrarTodos = activarMostrarTodos;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getInfoRegistroEstructura() {
        getLovEstructuras();
        if (lovEstructuras != null) {
            infoRegistroEstructura = "Cantidad de registros : " + lovEstructuras.size();
        } else {
            infoRegistroEstructura = "Cantidad de registros : 0";
        }
        return infoRegistroEstructura;
    }

    public void setInfoRegistroEstructura(String infoRegistroEstructura) {
        this.infoRegistroEstructura = infoRegistroEstructura;
    }

    public int getNumeroScrollCabecera() {
        return numeroScrollCabecera;
    }

    public void setNumeroScrollCabecera(int numeroPrueba) {
        this.numeroScrollCabecera = numeroPrueba;
    }

    public int getRowsCabecera() {
        return rowsCabecera;
    }

    public void setRowsCabecera(int rowsCabecera) {
        this.rowsCabecera = rowsCabecera;
    }

    public String getAltoDivTablaInferiorIzquierda() {
        return altoDivTablaInferiorIzquierda;
    }

    public void setAltoDivTablaInferiorIzquierda(String altoDivTablaInferiorIzquierda) {
        this.altoDivTablaInferiorIzquierda = altoDivTablaInferiorIzquierda;
    }

    public String getAltoDivTablaInferiorDerecha() {
        return altoDivTablaInferiorDerecha;
    }

    public void setAltoDivTablaInferiorDerecha(String altoDivTablaInferiorDerecha) {
        this.altoDivTablaInferiorDerecha = altoDivTablaInferiorDerecha;
    }

    public String getTopDivTablaInferiorIzquierda() {
        return topDivTablaInferiorIzquierda;
    }

    public void setTopDivTablaInferiorIzquierda(String topDivTablaInferiorIzquierda) {
        this.topDivTablaInferiorIzquierda = topDivTablaInferiorIzquierda;
    }

    public String getTopDivTablaInferiorDerecha() {
        return topDivTablaInferiorDerecha;
    }

    public void setTopDivTablaInferiorDerecha(String topDivTablaInferiorDerecha) {
        this.topDivTablaInferiorDerecha = topDivTablaInferiorDerecha;
    }

    public ActualUsuario getActualUsuario() {
        if (actualUsuario == null) {
            actualUsuario = administrarATAprobacionHE.obtenerActualUsuarioSistema();
        }
        return actualUsuario;
    }

    public void setActualUsuario(ActualUsuario actualUsuario) {
        this.actualUsuario = actualUsuario;
    }

    public List<EersFlujos> getListaFlujos() {
        if (listaFlujos == null) {
            if (indexCabecera >= 0) {
                if (tipoListaCabecera == 0) {
                    listaFlujos = administrarATAprobacionHE.obtenerFlujosEersCabecera(listaEersCabeceras.get(indexCabecera).getSecuencia());
                } else {
                    listaFlujos = administrarATAprobacionHE.obtenerFlujosEersCabecera(filtrarListaEersCabeceras.get(indexCabecera).getSecuencia());
                }
            }
        }
        return listaFlujos;
    }

    public void setListaFlujos(List<EersFlujos> listaFlujos) {
        this.listaFlujos = listaFlujos;
    }

    public List<EersFlujos> getFiltrarListaFlujos() {
        return filtrarListaFlujos;
    }

    public void setFiltrarListaFlujos(List<EersFlujos> filtrarListaFlujos) {
        this.filtrarListaFlujos = filtrarListaFlujos;
    }

    public EersFlujos getFlujoSeleccionado() {
        return flujoSeleccionado;
    }

    public void setFlujoSeleccionado(EersFlujos flujoSeleccionado) {
        this.flujoSeleccionado = flujoSeleccionado;
    }

    public String getAltoTablaFlujo() {
        return altoTablaFlujo;
    }

    public void setAltoTablaFlujo(String altoTablaFlujo) {
        this.altoTablaFlujo = altoTablaFlujo;
    }

    public List<EersCabeceras> getListEersCabecerasModificar() {
        return listEersCabecerasModificar;
    }

    public void setListEersCabecerasModificar(List<EersCabeceras> listEersCabecerasModificar) {
        this.listEersCabecerasModificar = listEersCabecerasModificar;
    }

    public EersFlujos getEditarFlujo() {
        return editarFlujo;
    }

    public void setEditarFlujo(EersFlujos editarFlujo) {
        this.editarFlujo = editarFlujo;
    }

    public List<EersDetalles> getListaDetalles() {
        if (listaDetalles == null) {
            if (indexCabecera >= 0) {
                if (tipoListaCabecera == 0) {
                    listaDetalles = administrarATAprobacionHE.obtenerDetallesEersCabecera(listaEersCabeceras.get(indexCabecera).getSecuencia());
                } else {
                    listaDetalles = administrarATAprobacionHE.obtenerDetallesEersCabecera(filtrarListaEersCabeceras.get(indexCabecera).getSecuencia());
                }
            }
        }
        return listaDetalles;
    }

    public void setListaDetalles(List<EersDetalles> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public List<EersDetalles> getFiltrarListaDetalles() {
        return filtrarListaDetalles;
    }

    public void setFiltrarListaDetalles(List<EersDetalles> filtrarListaDetalles) {
        this.filtrarListaDetalles = filtrarListaDetalles;
    }

    public EersDetalles getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(EersDetalles detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public String getAltoTablaDetalle() {
        return altoTablaDetalle;
    }

    public void setAltoTablaDetalle(String altoTablaDetalle) {
        this.altoTablaDetalle = altoTablaDetalle;
    }

    public EersDetalles getEditarDetalle() {
        return editarDetalle;
    }

    public void setEditarDetalle(EersDetalles editarDetalle) {
        this.editarDetalle = editarDetalle;
    }

}
