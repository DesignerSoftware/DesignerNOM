/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.GruposSalariales;
import Entidades.VigenciasGruposSalariales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGrupoSalarialInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlGrupoSalarial implements Serializable {

    @EJB
    AdministrarGrupoSalarialInterface administrarGrupoSalarial;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<GruposSalariales> listGruposSalariales;
    private List<GruposSalariales> filtrarListGruposSalariales;
    private GruposSalariales grupoSalarialTablaSeleccionada;
    ///////
    private List<VigenciasGruposSalariales> listVigenciasGruposSalariales;
    private List<VigenciasGruposSalariales> filtrarListVigenciasGruposSalariales;
    private VigenciasGruposSalariales vigenciaTablaSeleccionada;
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaVGS;
    //Columnas Tabla 
    private Column gsCodigo, gsDescripcion, gsSalario, vgsFechaVigencia, vgsValor;
    //Otros
    private boolean aceptar;
    private int index, indexVGS, indexAux;
    //modificar
    private List<GruposSalariales> listGrupoSalarialModificar;
    private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialModificar;
    private boolean guardado, guardadoVGS;
    private boolean cambiosPagina;
    //crear 
    private GruposSalariales nuevoGrupoSalarial;
    private VigenciasGruposSalariales nuevoVigenciaGrupoSalarial;
    private List<GruposSalariales> listGrupoSalarialCrear;
    private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<GruposSalariales> listGrupoSalarialBorrar;
    private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialBorrar;
    //editar celda
    private GruposSalariales editarGrupoSalarial;
    private VigenciasGruposSalariales editarVigenciaGrupoSalarial;
    private int cualCelda, tipoLista, cualCeldaVigencia, tipoListaVigencia;
    //duplicar
    private GruposSalariales duplicarGrupoSalarial;
    private VigenciasGruposSalariales duplicarVigenciaGrupoSalarial;
    private BigInteger secRegistro, secRegistroVigencia;
    private BigInteger backUpSecRegistro, backUpSecRegistroVigencia;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private BigInteger valorAux;
    private Date fechaVigencia;
    //
    private String paginaAnterior;
    //
    private String altoTablaGrupo, altoTablaVigencia;
    //
    private String auxDescripcionGrupo;

    public ControlGrupoSalarial() {
        cambiosPagina = true;
        altoTablaGrupo = "160";
        altoTablaVigencia = "120";
        indexVGS = -1;
        backUpSecRegistro = null;
        listGruposSalariales = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listGrupoSalarialBorrar = new ArrayList<GruposSalariales>();
        listVigenciaGrupoSalarialModificar = new ArrayList<VigenciasGruposSalariales>();
        listVigenciaGrupoSalarialBorrar = new ArrayList<VigenciasGruposSalariales>();
        //crear aficiones
        listGrupoSalarialCrear = new ArrayList<GruposSalariales>();
        listVigenciaGrupoSalarialCrear = new ArrayList<VigenciasGruposSalariales>();
        k = 0;
        //modificar aficiones
        listGrupoSalarialModificar = new ArrayList<GruposSalariales>();
        //editar
        editarGrupoSalarial = new GruposSalariales();
        editarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        editarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        cualCelda = -1;
        cualCeldaVigencia = -1;
        tipoListaVigencia = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        guardadoVGS = true;
        //Crear VC
        nuevoGrupoSalarial = new GruposSalariales();
        nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        duplicarGrupoSalarial = new GruposSalariales();
        duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        secRegistro = null;
        secRegistroVigencia = null;
        backUpSecRegistroVigencia = null;
        nombreTabla = ":formExportarG:datosGruposSalarialesExportar";
        nombreXML = "GruposSalarialesXML";

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGrupoSalarial.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String page) {
        paginaAnterior = page;
        listGruposSalariales = null;
        getListGruposSalariales();
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void modificarGrupoSalarial(int indice) {
        boolean existeDescripcion = true;
        if (tipoLista == 0) {
            if (listGruposSalariales.get(indice).getDescripcion() == null) {
                existeDescripcion = false;
            } else {
                if (listGruposSalariales.get(indice).getDescripcion().isEmpty()) {
                    existeDescripcion = false;
                }
            }
        } else {
            if (filtrarListGruposSalariales.get(indice).getDescripcion() == null) {
                existeDescripcion = false;
            } else {
                if (filtrarListGruposSalariales.get(indice).getDescripcion().isEmpty()) {
                    existeDescripcion = false;
                }
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (existeDescripcion == true) {
            if (tipoLista == 0) {
                if (!listGrupoSalarialCrear.contains(listGruposSalariales.get(indice))) {
                    if (listGrupoSalarialModificar.isEmpty()) {
                        listGrupoSalarialModificar.add(listGruposSalariales.get(indice));
                    } else if (!listGrupoSalarialModificar.contains(listGruposSalariales.get(indice))) {
                        listGrupoSalarialModificar.add(listGruposSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                if (!listGrupoSalarialCrear.contains(filtrarListGruposSalariales.get(indice))) {
                    if (listGrupoSalarialModificar.isEmpty()) {
                        listGrupoSalarialModificar.add(filtrarListGruposSalariales.get(indice));
                    } else if (!listGrupoSalarialModificar.contains(filtrarListGruposSalariales.get(indice))) {
                        listGrupoSalarialModificar.add(filtrarListGruposSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        //RequestContext.getCurrentInstance().update("form:aceptar");
                    }
                }
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            if (tipoLista == 0) {
                listGruposSalariales.get(index).setDescripcion(auxDescripcionGrupo);
            } else {
                filtrarListGruposSalariales.get(index).setDescripcion(auxDescripcionGrupo);
            }
            PrimefacesContextUI.ejecutar("PF('errorRegDescripcion').show()");
        }
        index = -1;
        secRegistro = null;
        RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");

    }

    public void modificarVigenciaGrupoSalarial(int indice) {
        boolean validarDatosNull = true;
        if (tipoListaVigencia == 0) {
            if (listVigenciasGruposSalariales.get(indice).getFechavigencia() == null || listVigenciasGruposSalariales.get(indice).getValor() == null) {
                validarDatosNull = false;
            }
        } else {
            if (filtrarListVigenciasGruposSalariales.get(indice).getFechavigencia() == null || filtrarListVigenciasGruposSalariales.get(indice).getValor() == null) {
                validarDatosNull = false;
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarDatosNull == true) {
            if (tipoListaVigencia == 0) {
                listVigenciasGruposSalariales.get(indice).setValor(valorAux);
                if (!listVigenciaGrupoSalarialCrear.contains(listVigenciasGruposSalariales.get(indice))) {
                    if (listVigenciaGrupoSalarialModificar.isEmpty()) {
                        listVigenciaGrupoSalarialModificar.add(listVigenciasGruposSalariales.get(indice));
                    } else if (!listVigenciaGrupoSalarialModificar.contains(listVigenciasGruposSalariales.get(indice))) {
                        listVigenciaGrupoSalarialModificar.add(listVigenciasGruposSalariales.get(indice));
                    }
                    if (guardadoVGS == true) {
                        guardadoVGS = false;
                    }
                }
            }
            if (tipoListaVigencia == 1) {
                filtrarListVigenciasGruposSalariales.get(indice).setValor(valorAux);
                if (!listVigenciaGrupoSalarialCrear.contains(filtrarListVigenciasGruposSalariales.get(indice))) {
                    if (listVigenciaGrupoSalarialModificar.isEmpty()) {
                        listVigenciaGrupoSalarialModificar.add(filtrarListVigenciasGruposSalariales.get(indice));
                    } else if (!listVigenciaGrupoSalarialModificar.contains(filtrarListVigenciasGruposSalariales.get(indice))) {
                        listVigenciaGrupoSalarialModificar.add(filtrarListVigenciasGruposSalariales.get(indice));
                    }
                    if (guardadoVGS == true) {
                        guardadoVGS = false;
                    }
                }
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            if (tipoListaVigencia == 0) {
                listVigenciasGruposSalariales.get(indice).setFechavigencia(fechaVigencia);
                listVigenciasGruposSalariales.get(indice).setValor(valorAux);
            } else {
                filtrarListVigenciasGruposSalariales.get(indice).setFechavigencia(fechaVigencia);
                filtrarListVigenciasGruposSalariales.get(indice).setValor(valorAux);
            }
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
        indexVGS = -1;
        secRegistroVigencia = null;
        RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
    }

    public void modificarFechas(int i, int c) {
        VigenciasGruposSalariales auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = listVigenciasGruposSalariales.get(i);
        }
        if (tipoLista == 1) {
            auxiliar = filtrarListVigenciasGruposSalariales.get(i);
        }
        if (auxiliar.getFechavigencia() != null) {
            cambiarIndiceVigencia(i, c);
            modificarVigenciaGrupoSalarial(i);
        } else {
            if (tipoLista == 0) {
                listVigenciasGruposSalariales.get(i).setFechavigencia(fechaVigencia);
            }
            if (tipoLista == 1) {
                filtrarListVigenciasGruposSalariales.get(i).setFechavigencia(fechaVigencia);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoVGS == true) {
            index = indice;
            cualCelda = celda;
            indexAux = indice;
            if (tipoLista == 0) {
                secRegistro = listGruposSalariales.get(index).getSecuencia();
                auxDescripcionGrupo = listGruposSalariales.get(index).getDescripcion();
            } else {
                secRegistro = filtrarListGruposSalariales.get(index).getSecuencia();
                auxDescripcionGrupo = filtrarListGruposSalariales.get(index).getDescripcion();
            }
            listVigenciasGruposSalariales = null;
            getListVigenciasGruposSalariales();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            if (banderaVGS == 1) {
                vgsValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
                vgsValor.setFilterStyle("display: none; visibility: hidden;");
                vgsFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
                vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                banderaVGS = 0;
                filtrarListVigenciasGruposSalariales = null;
                tipoListaVigencia = 0;
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardarSinSalida");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void cambiarIndiceVigencia(int indice, int celda) {
        indexVGS = indice;
        index = -1;
        cualCeldaVigencia = celda;
        if (tipoListaVigencia == 0) {
            secRegistroVigencia = listVigenciasGruposSalariales.get(indexVGS).getSecuencia();
            fechaVigencia = listVigenciasGruposSalariales.get(indexVGS).getFechavigencia();
            valorAux = listVigenciasGruposSalariales.get(indexVGS).getValor();
        } else {
            secRegistroVigencia = filtrarListVigenciasGruposSalariales.get(indexVGS).getSecuencia();
            fechaVigencia = filtrarListVigenciasGruposSalariales.get(indexVGS).getFechavigencia();
            valorAux = filtrarListVigenciasGruposSalariales.get(indexVGS).getValor();
        }
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGrupo = "160";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            ////
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
        }
    }
    //GUARDAR

    public void guardarGeneral() {
        if (cambiosPagina == false) {
            guardarCambiosGrupoSalarial();
            guardarCambiosVigenciaGrupoSalarial();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listGrupoSalarialBorrar.isEmpty()) {
                    for (int i = 0; i < listGrupoSalarialBorrar.size(); i++) {
                        administrarGrupoSalarial.borrarGruposSalariales(listGrupoSalarialBorrar);
                    }
                    listGrupoSalarialBorrar.clear();
                }
                if (!listGrupoSalarialCrear.isEmpty()) {
                    for (int i = 0; i < listGrupoSalarialCrear.size(); i++) {
                        administrarGrupoSalarial.crearGruposSalariales(listGrupoSalarialCrear);
                    }
                    listGrupoSalarialCrear.clear();
                }
                if (!listGrupoSalarialModificar.isEmpty()) {
                    administrarGrupoSalarial.editarGruposSalariales(listGrupoSalarialModificar);
                    listGrupoSalarialModificar.clear();
                }
                listGruposSalariales = null;
                RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                guardado = true;
                k = 0;
                index = -1;
                secRegistro = null;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Grupo Salarial con Éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosGrupoSalarial : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Grupo Salarial, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosVigenciaGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardadoVGS == false) {
                if (!listVigenciaGrupoSalarialBorrar.isEmpty()) {
                    for (int i = 0; i < listVigenciaGrupoSalarialBorrar.size(); i++) {
                        administrarGrupoSalarial.borrarVigenciasGruposSalariales(listVigenciaGrupoSalarialBorrar);
                    }
                    listVigenciaGrupoSalarialBorrar.clear();
                }
                if (!listVigenciaGrupoSalarialCrear.isEmpty()) {
                    for (int i = 0; i < listVigenciaGrupoSalarialCrear.size(); i++) {
                        administrarGrupoSalarial.crearVigenciasGruposSalariales(listVigenciaGrupoSalarialCrear);
                    }
                    listVigenciaGrupoSalarialCrear.clear();
                }
                if (!listVigenciaGrupoSalarialModificar.isEmpty()) {
                    administrarGrupoSalarial.editarVigenciasGruposSalariales(listVigenciaGrupoSalarialModificar);
                    listVigenciaGrupoSalarialModificar.clear();
                }
                listVigenciasGruposSalariales = null;
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                guardadoVGS = true;
                k = 0;
                indexVGS = -1;
                secRegistroVigencia = null;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Vigencia Grupo Salarial con Éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosGrupoSalarial : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Vigencia Grupo Salarial, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacionGeneral() {
        RequestContext context = RequestContext.getCurrentInstance();
        cancelarModificacionGrupoSalarial();
        RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
        cancelarModificacionVigenciaGrupoSalarial();
        RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificacionGrupoSalarial() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGrupo = "160";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            ////
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
        }
        listGrupoSalarialBorrar.clear();
        listGrupoSalarialCrear.clear();
        listGrupoSalarialModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposSalariales = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
    }

    public void cancelarModificacionVigenciaGrupoSalarial() {
        if (banderaVGS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaVigencia = "120";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("display: none; visibility: hidden;");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            banderaVGS = 0;
            filtrarListVigenciasGruposSalariales = null;
            tipoListaVigencia = 0;
        }
        listVigenciaGrupoSalarialBorrar.clear();
        listVigenciaGrupoSalarialCrear.clear();
        listVigenciaGrupoSalarialModificar.clear();
        indexVGS = -1;
        secRegistroVigencia = null;
        k = 0;
        listVigenciasGruposSalariales = null;
        guardadoVGS = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarGrupoSalarial = listGruposSalariales.get(index);
            }
            if (tipoLista == 1) {
                editarGrupoSalarial = filtrarListGruposSalariales.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionD");
                PrimefacesContextUI.ejecutar("PF('editarDescripcionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSalarioD");
                PrimefacesContextUI.ejecutar("PF('editarSalarioD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexVGS >= 0) {
            if (tipoListaVigencia == 0) {
                editarVigenciaGrupoSalarial = listVigenciasGruposSalariales.get(indexVGS);
            }
            if (tipoListaVigencia == 1) {
                editarVigenciaGrupoSalarial = listVigenciasGruposSalariales.get(indexVGS);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaVigencia == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaD");
                PrimefacesContextUI.ejecutar("PF('editarFechaVigenciaD').show()");
                cualCeldaVigencia = -1;
            } else if (cualCeldaVigencia == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
                PrimefacesContextUI.ejecutar("PF('editarValorD').show()");
                cualCeldaVigencia = -1;
            }
            indexVGS = -1;
            secRegistroVigencia = null;
        }

    }

    public void dialogoNuevoRegistro() {
        if (guardado == true && guardadoVGS == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            int tam = 0;
            if (listGruposSalariales != null) {
                tam = listGruposSalariales.size();
            }
            int tam2 = 0;
            if (listVigenciasGruposSalariales != null) {
                tam2 = listVigenciasGruposSalariales.size();
            }
            if (tam == 0 || tam2 == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
                PrimefacesContextUI.ejecutar("PF('verificarNuevoRegistro').show()");
            } else {
                if (index >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGrupoSalarial");
                    PrimefacesContextUI.ejecutar("PF('NuevoRegistroGrupoSalarial').show()");
                }
                if (indexVGS >= 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaGrupoSalarial");
                    PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciaGrupoSalarial').show()");
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardarSinSalida");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
        }
    }

    //CREAR 
    public void agregarNuevoGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGrupoSalarial.getDescripcion() != null) {
            if (!nuevoGrupoSalarial.getDescripcion().isEmpty()) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTablaGrupo = "160";
                    gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
                    gsCodigo.setFilterStyle("display: none; visibility: hidden;");
                    gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
                    gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
                    gsSalario.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                    bandera = 0;
                    filtrarListGruposSalariales = null;
                    tipoLista = 0;
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
                k++;
                l = BigInteger.valueOf(k);
                nuevoGrupoSalarial.setSecuencia(l);
                listGrupoSalarialCrear.add(nuevoGrupoSalarial);
                listGruposSalariales.add(nuevoGrupoSalarial);
                nuevoGrupoSalarial = new GruposSalariales();
                RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                if (guardado == true) {
                    guardado = false;
                }
                index = -1;
                secRegistro = null;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroGrupoSalarial').hide()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRegDescripcion').show()");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRegDescripcion').show()");

        }
    }

    public void agregarNuevoVigenciaGrupoSalarial() {
        if (nuevoVigenciaGrupoSalarial.getFechavigencia() != null && nuevoVigenciaGrupoSalarial.getValor() != null) {
            if (banderaVGS == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaVigencia = "120";
                vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
                vgsValor.setFilterStyle("display: none; visibility: hidden;");
                vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
                vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                banderaVGS = 0;
                filtrarListVigenciasGruposSalariales = null;
                tipoListaVigencia = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciaGrupoSalarial.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoVigenciaGrupoSalarial.setGruposalarial(listGruposSalariales.get(indexAux));
            } else {
                nuevoVigenciaGrupoSalarial.setGruposalarial(filtrarListGruposSalariales.get(indexAux));
            }
            listVigenciaGrupoSalarialCrear.add(nuevoVigenciaGrupoSalarial);
            if (listVigenciasGruposSalariales == null) {
                listVigenciasGruposSalariales = new ArrayList<VigenciasGruposSalariales>();
            }
            listVigenciasGruposSalariales.add(nuevoVigenciaGrupoSalarial);
            nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciaGrupoSalarial.hide();");
            if (guardadoVGS == true) {
                guardadoVGS = false;
            }
            indexVGS = -1;
            secRegistroVigencia = null;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void limpiarNuevaGrupoSalarial() {
        nuevoGrupoSalarial = new GruposSalariales();
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaVigenciaGrupoSalarial() {
        nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        indexVGS = -1;
        secRegistroVigencia = null;
    }

    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarGrupoSalarialM();
        }
        if (indexVGS >= 0) {
            duplicarVigenciaGrupoSalarialM();
        }
    }

    public void duplicarGrupoSalarialM() {
        if (index >= 0) {
            duplicarGrupoSalarial = new GruposSalariales();

            if (tipoLista == 0) {
                duplicarGrupoSalarial.setCodigo(listGruposSalariales.get(index).getCodigo());
                duplicarGrupoSalarial.setDescripcion(listGruposSalariales.get(index).getDescripcion());
                duplicarGrupoSalarial.setEscalafonsalarial(listGruposSalariales.get(index).getEscalafonsalarial());
                duplicarGrupoSalarial.setSalario(listGruposSalariales.get(index).getSalario());
            }
            if (tipoLista == 1) {
                duplicarGrupoSalarial.setCodigo(filtrarListGruposSalariales.get(index).getCodigo());
                duplicarGrupoSalarial.setDescripcion(filtrarListGruposSalariales.get(index).getDescripcion());
                duplicarGrupoSalarial.setEscalafonsalarial(filtrarListGruposSalariales.get(index).getEscalafonsalarial());
                duplicarGrupoSalarial.setSalario(filtrarListGruposSalariales.get(index).getSalario());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroGrupoSalarial').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void duplicarVigenciaGrupoSalarialM() {

        if (indexVGS >= 0) {
            duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();

            if (tipoListaVigencia == 0) {
                duplicarVigenciaGrupoSalarial.setFechavigencia(listVigenciasGruposSalariales.get(indexVGS).getFechavigencia());
                duplicarVigenciaGrupoSalarial.setGruposalarial(listVigenciasGruposSalariales.get(indexVGS).getGruposalarial());
                duplicarVigenciaGrupoSalarial.setValor(listVigenciasGruposSalariales.get(indexVGS).getValor());
            }
            if (tipoListaVigencia == 1) {
                duplicarVigenciaGrupoSalarial.setFechavigencia(filtrarListVigenciasGruposSalariales.get(indexVGS).getFechavigencia());
                duplicarVigenciaGrupoSalarial.setGruposalarial(filtrarListVigenciasGruposSalariales.get(indexVGS).getGruposalarial());
                duplicarVigenciaGrupoSalarial.setValor(filtrarListVigenciasGruposSalariales.get(indexVGS).getValor());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVigenciaGrupoSalarial').show()");
            indexVGS = -1;
            secRegistroVigencia = null;
        }

    }

    public void confirmarDuplicarGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarGrupoSalarial.getDescripcion() != null) {
            if (!duplicarGrupoSalarial.getDescripcion().isEmpty()) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarGrupoSalarial.setSecuencia(l);
                listGruposSalariales.add(duplicarGrupoSalarial);
                listGrupoSalarialCrear.add(duplicarGrupoSalarial);
                RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTablaGrupo = "160";
                    gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
                    gsCodigo.setFilterStyle("display: none; visibility: hidden;");
                    gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
                    gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
                    gsSalario.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                    bandera = 0;
                    filtrarListGruposSalariales = null;
                    tipoLista = 0;
                }
                duplicarGrupoSalarial = new GruposSalariales();
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroGrupoSalarial').hide()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRegDescripcion').show()");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRegDescripcion').show()");

        }
    }

    public void confirmarDuplicarVigenciaGrupoSalarial() {
        if (duplicarVigenciaGrupoSalarial.getFechavigencia() != null && duplicarVigenciaGrupoSalarial.getValor() != null) {
            if (tipoLista == 0) {
                duplicarVigenciaGrupoSalarial.setGruposalarial(listGruposSalariales.get(indexAux));
            } else {
                duplicarVigenciaGrupoSalarial.setGruposalarial(filtrarListGruposSalariales.get(indexAux));
            }
            if (listVigenciasGruposSalariales == null) {
                listVigenciasGruposSalariales = new ArrayList<VigenciasGruposSalariales>();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaGrupoSalarial.setSecuencia(l);
            listVigenciasGruposSalariales.add(duplicarVigenciaGrupoSalarial);
            listVigenciaGrupoSalarialCrear.add(duplicarVigenciaGrupoSalarial);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVigenciaGrupoSalarial').hide()");
            indexVGS = -1;
            secRegistroVigencia = null;
            if (guardadoVGS == true) {
                guardadoVGS = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (banderaVGS == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaVigencia = "120";
                vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
                vgsValor.setFilterStyle("display: none; visibility: hidden;");
                vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
                vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                bandera = 0;
                filtrarListGruposSalariales = null;
                tipoLista = 0;
            }
            duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void limpiarDuplicarGrupoSalarial() {
        duplicarGrupoSalarial = new GruposSalariales();
    }

    public void limpiarDuplicarVigenciaGrupoSalarial() {
        duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    public void verificarRegistroBorrar() {
        if (index >= 0) {
            if (listVigenciasGruposSalariales != null) {
                if (listVigenciasGruposSalariales.isEmpty()) {
                    borrarGrupoSalarial();
                } else {
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('errorBorrarRegistro').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexVGS >= 0) {
            borrarVigenciaGrupoSalarial();
        }
    }

    public void borrarGrupoSalarial() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listGrupoSalarialModificar.isEmpty() && listGrupoSalarialModificar.contains(listGruposSalariales.get(index))) {
                    int modIndex = listGrupoSalarialModificar.indexOf(listGruposSalariales.get(index));
                    listGrupoSalarialModificar.remove(modIndex);
                    listGrupoSalarialBorrar.add(listGruposSalariales.get(index));
                } else if (!listGrupoSalarialCrear.isEmpty() && listGrupoSalarialCrear.contains(listGruposSalariales.get(index))) {
                    int crearIndex = listGrupoSalarialCrear.indexOf(listGruposSalariales.get(index));
                    listGrupoSalarialCrear.remove(crearIndex);
                } else {
                    listGrupoSalarialBorrar.add(listGruposSalariales.get(index));
                }
                listGruposSalariales.remove(index);
            }
            if (tipoLista == 1) {
                if (!listGrupoSalarialModificar.isEmpty() && listGrupoSalarialModificar.contains(filtrarListGruposSalariales.get(index))) {
                    int modIndex = listGrupoSalarialModificar.indexOf(filtrarListGruposSalariales.get(index));
                    listGrupoSalarialModificar.remove(modIndex);
                    listGrupoSalarialBorrar.add(filtrarListGruposSalariales.get(index));
                } else if (!listGrupoSalarialCrear.isEmpty() && listGrupoSalarialCrear.contains(filtrarListGruposSalariales.get(index))) {
                    int crearIndex = listGrupoSalarialCrear.indexOf(filtrarListGruposSalariales.get(index));
                    listGrupoSalarialCrear.remove(crearIndex);
                } else {
                    listGrupoSalarialBorrar.add(filtrarListGruposSalariales.get(index));
                }
                int VCIndex = listGruposSalariales.indexOf(filtrarListGruposSalariales.get(index));
                listGruposSalariales.remove(VCIndex);
                filtrarListGruposSalariales.remove(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarVigenciaGrupoSalarial() {
        if (indexVGS >= 0) {
            if (tipoListaVigencia == 0) {
                if (!listVigenciaGrupoSalarialModificar.isEmpty() && listVigenciaGrupoSalarialModificar.contains(listVigenciasGruposSalariales.get(indexVGS))) {
                    int modIndex = listVigenciaGrupoSalarialModificar.indexOf(listVigenciasGruposSalariales.get(indexVGS));
                    listVigenciaGrupoSalarialModificar.remove(modIndex);
                    listVigenciaGrupoSalarialBorrar.add(listVigenciasGruposSalariales.get(indexVGS));
                } else if (!listVigenciaGrupoSalarialCrear.isEmpty() && listVigenciaGrupoSalarialCrear.contains(listVigenciasGruposSalariales.get(indexVGS))) {
                    int crearIndex = listVigenciaGrupoSalarialCrear.indexOf(listVigenciasGruposSalariales.get(indexVGS));
                    listVigenciaGrupoSalarialCrear.remove(crearIndex);
                } else {
                    listVigenciaGrupoSalarialBorrar.add(listVigenciasGruposSalariales.get(indexVGS));
                }
                listVigenciasGruposSalariales.remove(indexVGS);
            }
            if (tipoListaVigencia == 1) {
                if (!listVigenciaGrupoSalarialModificar.isEmpty() && listVigenciaGrupoSalarialModificar.contains(filtrarListVigenciasGruposSalariales.get(indexVGS))) {
                    int modIndex = listVigenciaGrupoSalarialModificar.indexOf(filtrarListVigenciasGruposSalariales.get(indexVGS));
                    listVigenciaGrupoSalarialModificar.remove(modIndex);
                    listVigenciaGrupoSalarialBorrar.add(filtrarListVigenciasGruposSalariales.get(indexVGS));
                } else if (!listVigenciaGrupoSalarialCrear.isEmpty() && listVigenciaGrupoSalarialCrear.contains(filtrarListVigenciasGruposSalariales.get(indexVGS))) {
                    int crearIndex = listVigenciaGrupoSalarialCrear.indexOf(filtrarListVigenciasGruposSalariales.get(indexVGS));
                    listVigenciaGrupoSalarialCrear.remove(crearIndex);
                } else {
                    listVigenciaGrupoSalarialBorrar.add(filtrarListVigenciasGruposSalariales.get(indexVGS));
                }
                int VCIndex = listVigenciasGruposSalariales.indexOf(filtrarListVigenciasGruposSalariales.get(indexVGS));
                listVigenciasGruposSalariales.remove(VCIndex);
                filtrarListVigenciasGruposSalariales.remove(indexVGS);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            indexVGS = -1;
            secRegistroVigencia = null;

            if (guardadoVGS == true) {
                guardadoVGS = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (index >= 0) {
            if (bandera == 0) {
                altoTablaGrupo = "140";
                gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
                gsCodigo.setFilterStyle("width: 85% !important");
                gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
                gsDescripcion.setFilterStyle("width: 85% !important");
                gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
                gsSalario.setFilterStyle("width: 85% !important");
                RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                bandera = 1;
            } else if (bandera == 1) {
                altoTablaGrupo = "160";
                gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
                gsCodigo.setFilterStyle("display: none; visibility: hidden;");
                gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
                gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
                gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
                gsSalario.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
                bandera = 0;
                filtrarListGruposSalariales = null;
                tipoLista = 0;
            }
        }
        if (indexVGS >= 0) {
            if (banderaVGS == 0) {
                altoTablaVigencia = "98";
                vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
                vgsValor.setFilterStyle("width: 180px");
                vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
                vgsFechaVigencia.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                banderaVGS = 1;
            } else if (banderaVGS == 1) {
                altoTablaVigencia = "120";
                vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
                vgsValor.setFilterStyle("display: none; visibility: hidden;");
                vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
                vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
                banderaVGS = 0;
                filtrarListVigenciasGruposSalariales = null;
                tipoListaVigencia = 0;
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGrupo = "160";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
        }

        if (banderaVGS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaVigencia = "120";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("display: none; visibility: hidden;");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            banderaVGS = 0;
            filtrarListVigenciasGruposSalariales = null;
            tipoListaVigencia = 0;
        }

        listGrupoSalarialBorrar.clear();
        listGrupoSalarialCrear.clear();
        listGrupoSalarialModificar.clear();
        listVigenciaGrupoSalarialBorrar.clear();
        listVigenciaGrupoSalarialCrear.clear();
        listVigenciaGrupoSalarialModificar.clear();
        index = -1;
        indexAux = -1;
        indexVGS = -1;
        secRegistroVigencia = null;
        secRegistro = null;
        k = 0;
        listGruposSalariales = null;
        listVigenciasGruposSalariales = null;
        guardado = true;
        guardadoVGS = true;
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDF_GS();
        }
        if (indexVGS >= 0) {
            exportPDF_VGS();
        }
    }

    public void exportPDF_GS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarG:datosGruposSalarialesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GruposSalarialesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_VGS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVG:datosVigenciaGruposSalarialesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasGruposSalarialesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexVGS = -1;
        secRegistroVigencia = null;
    }

    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_GS();
        }
        if (indexVGS >= 0) {
            exportXLS_VGS();
        }
    }

    public void exportXLS_GS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVG:datosVigenciaGruposSalarialesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GruposSalarialesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_VGS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSetsEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasGruposSalarialesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexVGS = -1;
        secRegistroVigencia = null;
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
        if (indexVGS >= 0) {
            if (tipoListaVigencia == 0) {
                tipoListaVigencia = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        if (listVigenciasGruposSalariales == null || listGruposSalariales == null) {
        } else {
            if (index >= 0) {
                verificarRastroGrupoSalarial();
                index = -1;
            }
            if (indexVGS >= 0) {
                verificarRastroVigenciaGrupoSalarial();
                indexVGS = -1;
            }
        }
    }

    public void verificarRastroGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listGruposSalariales != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "GRUPOSSALARIALES");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "GruposSalariales";
                    msnConfirmarRastro = "La tabla GRUPOSSALARIALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
            if (administrarRastros.verificarHistoricosTabla("GRUPOSSALARIALES")) {
                nombreTablaRastro = "GruposSalariales";
                msnConfirmarRastroHistorico = "La tabla GRUPOSSALARIALES tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void verificarRastroVigenciaGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listVigenciasGruposSalariales != null) {
            if (secRegistroVigencia != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroVigencia, "VIGENCIASGRUPOSSALARIALES");
                backUpSecRegistroVigencia = secRegistroVigencia;
                backUp = secRegistroVigencia;
                secRegistroVigencia = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "VigenciasGruposSalariales";
                    msnConfirmarRastro = "La tabla VIGENCIASGRUPOSSALARIALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASGRUPOSSALARIALES")) {
                nombreTablaRastro = "VigenciasGruposSalariales";
                msnConfirmarRastroHistorico = "La tabla VIGENCIASGRUPOSSALARIALES tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        indexVGS = -1;
    }

    //GETTERS AND SETTERS
    public List<GruposSalariales> getListGruposSalariales() {
        try {
            if (listGruposSalariales == null) {
                listGruposSalariales = administrarGrupoSalarial.listGruposSalariales();
            }
            return listGruposSalariales;
        } catch (Exception e) {
            System.out.println("Error...!! getListGruposSalariales : " + e.toString());
            return null;
        }
    }

    public void setListGruposSalariales(List<GruposSalariales> ListGruposSalariales) {
        this.listGruposSalariales = ListGruposSalariales;
    }

    public List<GruposSalariales> getFiltrarListGruposSalariales() {
        return filtrarListGruposSalariales;
    }

    public void setFiltrarListGruposSalariales(List<GruposSalariales> ListGruposSalariales) {
        this.filtrarListGruposSalariales = ListGruposSalariales;
    }

    public GruposSalariales getNuevoGrupoSalarial() {
        return nuevoGrupoSalarial;
    }

    public void setNuevoGrupoSalarial(GruposSalariales GrupoSalarial) {
        this.nuevoGrupoSalarial = GrupoSalarial;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public GruposSalariales getEditarGrupoSalarial() {
        return editarGrupoSalarial;
    }

    public void setEditarGrupoSalarial(GruposSalariales GrupoSalarial) {
        this.editarGrupoSalarial = GrupoSalarial;
    }

    public GruposSalariales getDuplicarGrupoSalarial() {
        return duplicarGrupoSalarial;
    }

    public void setDuplicarGrupoSalarial(GruposSalariales duplicarGrupoSalarial) {
        this.duplicarGrupoSalarial = duplicarGrupoSalarial;
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

    public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
        this.backUpSecRegistro = BackUpSecRegistro;
    }

    public List<VigenciasGruposSalariales> getListVigenciasGruposSalariales() {
        if (listVigenciasGruposSalariales == null) {
            if (index >= 0) {
                if (tipoLista == 0) {
                    listVigenciasGruposSalariales = administrarGrupoSalarial.lisVigenciasGruposSalarialesSecuencia(listGruposSalariales.get(index).getSecuencia());
                } else {
                    listVigenciasGruposSalariales = administrarGrupoSalarial.lisVigenciasGruposSalarialesSecuencia(filtrarListGruposSalariales.get(index).getSecuencia());
                }
            }
        }
        return listVigenciasGruposSalariales;
    }

    public void setListVigenciasGruposSalariales(List<VigenciasGruposSalariales> listVigenciasGruposSalariales) {
        this.listVigenciasGruposSalariales = listVigenciasGruposSalariales;
    }

    public List<VigenciasGruposSalariales> getFiltrarListVigenciasGruposSalariales() {
        return filtrarListVigenciasGruposSalariales;
    }

    public void setFiltrarListVigenciasGruposSalariales(List<VigenciasGruposSalariales> filtrarListVigenciasGruposSalariales) {
        this.filtrarListVigenciasGruposSalariales = filtrarListVigenciasGruposSalariales;
    }

    public List<GruposSalariales> getListGrupoSalarialModificar() {
        return listGrupoSalarialModificar;
    }

    public void setListGrupoSalarialModificar(List<GruposSalariales> listGrupoSalarialModificar) {
        this.listGrupoSalarialModificar = listGrupoSalarialModificar;
    }

    public List<GruposSalariales> getListGrupoSalarialCrear() {
        return listGrupoSalarialCrear;
    }

    public void setListGrupoSalarialCrear(List<GruposSalariales> listGrupoSalarialCrear) {
        this.listGrupoSalarialCrear = listGrupoSalarialCrear;
    }

    public List<GruposSalariales> getListGrupoSalarialBorrar() {
        return listGrupoSalarialBorrar;
    }

    public void setListGrupoSalarialBorrar(List<GruposSalariales> listGrupoSalarialBorrar) {
        this.listGrupoSalarialBorrar = listGrupoSalarialBorrar;
    }

    public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialModificar() {
        return listVigenciaGrupoSalarialModificar;
    }

    public void setListVigenciaGrupoSalarialModificar(List<VigenciasGruposSalariales> listVigenciaGrupoSalariaModificar) {
        this.listVigenciaGrupoSalarialModificar = listVigenciaGrupoSalariaModificar;
    }

    public VigenciasGruposSalariales getNuevoVigenciaGrupoSalarial() {
        return nuevoVigenciaGrupoSalarial;
    }

    public void setNuevoVigenciaGrupoSalarial(VigenciasGruposSalariales nuevoVigenciaGrupoSalarial) {
        this.nuevoVigenciaGrupoSalarial = nuevoVigenciaGrupoSalarial;
    }

    public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialCrear() {
        return listVigenciaGrupoSalarialCrear;
    }

    public void setListVigenciaGrupoSalarialCrear(List<VigenciasGruposSalariales> listVigenciaGrupoSalarialCrear) {
        this.listVigenciaGrupoSalarialCrear = listVigenciaGrupoSalarialCrear;
    }

    public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialBorrar() {
        return listVigenciaGrupoSalarialBorrar;
    }

    public void setListVigenciaGrupoSalarialBorrar(List<VigenciasGruposSalariales> listVigenciaGrupoSalarialBorrar) {
        this.listVigenciaGrupoSalarialBorrar = listVigenciaGrupoSalarialBorrar;
    }

    public VigenciasGruposSalariales getEditarVigenciaGrupoSalarial() {
        return editarVigenciaGrupoSalarial;
    }

    public void setEditarVigenciaGrupoSalarial(VigenciasGruposSalariales editarVigenciaGrupoSalarial) {
        this.editarVigenciaGrupoSalarial = editarVigenciaGrupoSalarial;
    }

    public VigenciasGruposSalariales getDuplicarVigenciaGrupoSalarial() {
        return duplicarVigenciaGrupoSalarial;
    }

    public void setDuplicarVigenciaGrupoSalarial(VigenciasGruposSalariales duplicarVigenciaGrupoSalarial) {
        this.duplicarVigenciaGrupoSalarial = duplicarVigenciaGrupoSalarial;
    }

    public BigInteger getSecRegistroVigencia() {
        return secRegistroVigencia;
    }

    public void setSecRegistroVigencia(BigInteger secRegistroVigencia) {
        this.secRegistroVigencia = secRegistroVigencia;
    }

    public BigInteger getBackUpSecRegistroVigencia() {
        return backUpSecRegistroVigencia;
    }

    public void setBackUpSecRegistroVigencia(BigInteger backUpSecRegistroVigencia) {
        this.backUpSecRegistroVigencia = backUpSecRegistroVigencia;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        if (index >= 0) {
            nombreTabla = ":formExportarG:datosGruposSalarialesExportar";
            nombreXML = "GruposSalarialesXML";
        }
        if (indexVGS >= 0) {
            nombreTabla = ":formExportarVG:datosVigenciaGruposSalarialesExportar";
            nombreXML = "VigenciasGruposSalarialesXML";
        }
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public GruposSalariales getGrupoSalarialTablaSeleccionada() {
        getListGruposSalariales();
        if (listGruposSalariales != null) {
            int tam = listGruposSalariales.size();
            if (tam > 0) {
                grupoSalarialTablaSeleccionada = listGruposSalariales.get(0);
            }
        }
        return grupoSalarialTablaSeleccionada;
    }

    public void setGrupoSalarialTablaSeleccionada(GruposSalariales grupoSalarialTablaSeleccionada) {
        this.grupoSalarialTablaSeleccionada = grupoSalarialTablaSeleccionada;
    }

    public VigenciasGruposSalariales getVigenciaTablaSeleccionada() {
        getListVigenciasGruposSalariales();
        if (listVigenciasGruposSalariales != null) {
            int tam = listVigenciasGruposSalariales.size();
            if (tam > 0) {
                vigenciaTablaSeleccionada = listVigenciasGruposSalariales.get(0);
            }
        }
        return vigenciaTablaSeleccionada;
    }

    public void setVigenciaTablaSeleccionada(VigenciasGruposSalariales vigenciaTablaSeleccionada) {
        this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
    }

    public String getAltoTablaGrupo() {
        return altoTablaGrupo;
    }

    public void setAltoTablaGrupo(String algoTablaGrupo) {
        this.altoTablaGrupo = algoTablaGrupo;
    }

    public String getAltoTablaVigencia() {
        return altoTablaVigencia;
    }

    public void setAltoTablaVigencia(String altoTablaVigencia) {
        this.altoTablaVigencia = altoTablaVigencia;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

}
