/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Operandos;
import Entidades.TiposConstantes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposConstantesInterface;
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
public class ControlTipoConstante implements Serializable {

    @EJB
    AdministrarTiposConstantesInterface administrarTiposConstantes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private String tOperando;
    private Operandos operando;//LISTA INFOREPORTES
    private List<TiposConstantes> listaTiposConstantes;
    private List<TiposConstantes> filtradosListaTiposConstantes;
    private TiposConstantes tipoConstanteSeleccionado;
    //L.O.V INFOREPORTES
    private List<TiposConstantes> lovlistaTiposConstantes;
    private List<TiposConstantes> lovfiltradoslistaTiposConstantes;
    private TiposConstantes operandosSeleccionado;
    //editar celda
    private TiposConstantes editarTiposConstantes;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private BigInteger secRegistro;
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<TiposConstantes> listaTiposConstantesCrear;
    public TiposConstantes nuevoTipoConstante;
    public TiposConstantes duplicarTipoConstante;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<TiposConstantes> listaTiposConstantesModificar;
    //Borrar Novedades
    private List<TiposConstantes> listaTiposConstantesBorrar;
    //AUTOCOMPLETAR
    private String Formula;
    //Columnas Tabla Ciudades
    private Column tiposConstantesTipos, tiposConstantesIniciales, tiposConstantesFinales, tiposConstantesReales, tiposConstantesDates, tiposConstantesCadenas;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //Booleanos para activar casillas en la Tabla
    private boolean numericoB;
    private boolean fechaB;
    private boolean cadenaB;
    //Booleanos para activar casillas en nuevo registro
    private boolean numericoBN;
    private boolean fechaBN;
    private boolean cadenaBN;
    //Booleanos para activar casillas en duplicar registro
    private boolean numericoBD;
    private boolean fechaBD;
    private boolean cadenaBD;

    public ControlTipoConstante() {
        cambiosPagina = true;
        nuevoTipoConstante = new TiposConstantes();
        nuevoTipoConstante.setFechainicial(new Date());
        permitirIndex = true;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaTiposConstantesBorrar = new ArrayList<TiposConstantes>();
        listaTiposConstantesCrear = new ArrayList<TiposConstantes>();
        listaTiposConstantesModificar = new ArrayList<TiposConstantes>();
        altoTabla = "270";
        duplicarTipoConstante = new TiposConstantes();
        nuevoTipoConstante.setFechainicial(new Date());
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposConstantes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct "+ this.getClass().getName() +": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    public void recibirDatosOperando(BigInteger secuenciaOperando, String tipoOperando, Operandos operandoRegistro) {
        secOperando = secuenciaOperando;
        tOperando = tipoOperando;
        operando = operandoRegistro;
        listaTiposConstantes = null;
        getListaTiposConstantes();
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                secRegistro = listaTiposConstantes.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaTiposConstantes.get(index).getSecuencia();
            }
        }
    }

    //BORRAR CIUDADES
    public void borrarTipoConstante() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaTiposConstantesModificar.isEmpty() && listaTiposConstantesModificar.contains(listaTiposConstantes.get(index))) {
                    int modIndex = listaTiposConstantesModificar.indexOf(listaTiposConstantes.get(index));
                    listaTiposConstantesModificar.remove(modIndex);
                    listaTiposConstantesBorrar.add(listaTiposConstantes.get(index));
                } else if (!listaTiposConstantesCrear.isEmpty() && listaTiposConstantesCrear.contains(listaTiposConstantes.get(index))) {
                    int crearIndex = listaTiposConstantesCrear.indexOf(listaTiposConstantes.get(index));
                    listaTiposConstantesCrear.remove(crearIndex);
                } else {
                    listaTiposConstantesBorrar.add(listaTiposConstantes.get(index));
                }
                listaTiposConstantes.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaTiposConstantesModificar.isEmpty() && listaTiposConstantesModificar.contains(filtradosListaTiposConstantes.get(index))) {
                    int modIndex = listaTiposConstantesModificar.indexOf(filtradosListaTiposConstantes.get(index));
                    listaTiposConstantesModificar.remove(modIndex);
                    listaTiposConstantesBorrar.add(filtradosListaTiposConstantes.get(index));
                } else if (!listaTiposConstantesCrear.isEmpty() && listaTiposConstantesCrear.contains(filtradosListaTiposConstantes.get(index))) {
                    int crearIndex = listaTiposConstantesCrear.indexOf(filtradosListaTiposConstantes.get(index));
                    listaTiposConstantesCrear.remove(crearIndex);
                } else {
                    listaTiposConstantesBorrar.add(filtradosListaTiposConstantes.get(index));
                }
                int CIndex = listaTiposConstantes.indexOf(filtradosListaTiposConstantes.get(index));
                listaTiposConstantes.remove(CIndex);
                filtradosListaTiposConstantes.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "250";
            tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
            tiposConstantesTipos.setFilterStyle("width: 85% !important;");
            tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
            tiposConstantesIniciales.setFilterStyle("width: 85% !important;");
            tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
            tiposConstantesFinales.setFilterStyle("width: 85% !important;");
            tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
            tiposConstantesReales.setFilterStyle("width: 85% !important;");
            tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
            tiposConstantesDates.setFilterStyle("width: 85% !important;");
            tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
            tiposConstantesCadenas.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
            tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
            tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
            tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
            tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
            tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
            tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            bandera = 0;
            filtradosListaTiposConstantes = null;
            tipoLista = 0;
        }
    }

    public void salir() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
            tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
            tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
            tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
            tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
            tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
            tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            bandera = 0;
            filtradosListaTiposConstantes = null;
            tipoLista = 0;
        }
        listaTiposConstantesBorrar.clear();
        listaTiposConstantesCrear.clear();
        listaTiposConstantesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposConstantes = null;
        guardado = true;
        permitirIndex = true;

    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposConstantes = listaTiposConstantes.get(index);
            }
            if (tipoLista == 1) {
                editarTiposConstantes = filtradosListaTiposConstantes.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
                PrimefacesContextUI.ejecutar("PF('editarTipos').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
                PrimefacesContextUI.ejecutar("PF('editarFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
                PrimefacesContextUI.ejecutar("PF('editarFechasFinales').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresReales");
                PrimefacesContextUI.ejecutar("PF('editarValoresReales').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresDates");
                PrimefacesContextUI.ejecutar("PF('editarValoresDates').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresCadenas");
                PrimefacesContextUI.ejecutar("PF('editarValoresCadenas').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
            tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
            tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
            tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
            tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
            tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
            tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
            tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            bandera = 0;
            filtradosListaTiposConstantes = null;
            tipoLista = 0;
        }

        listaTiposConstantesBorrar.clear();
        listaTiposConstantesCrear.clear();
        listaTiposConstantesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposConstantes = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
    }

    //DUPLICAR Operando
    public void duplicarTC() {
        if (index >= 0) {
            duplicarTipoConstante = new TiposConstantes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoConstante.setSecuencia(l);
                duplicarTipoConstante.setFechainicial(listaTiposConstantes.get(index).getFechainicial());
                duplicarTipoConstante.setFechafinal(listaTiposConstantes.get(index).getFechafinal());
                duplicarTipoConstante.setOperando(listaTiposConstantes.get(index).getOperando());
                duplicarTipoConstante.setTipo(listaTiposConstantes.get(index).getTipo());
                duplicarTipoConstante.setValorreal(listaTiposConstantes.get(index).getValorreal());
                duplicarTipoConstante.setValordate(listaTiposConstantes.get(index).getValordate());
                duplicarTipoConstante.setValorstring(listaTiposConstantes.get(index).getValorstring());
            }
            if (tipoLista == 1) {
                duplicarTipoConstante.setSecuencia(l);
                duplicarTipoConstante.setFechainicial(filtradosListaTiposConstantes.get(index).getFechainicial());
                duplicarTipoConstante.setFechafinal(filtradosListaTiposConstantes.get(index).getFechafinal());
                duplicarTipoConstante.setOperando(filtradosListaTiposConstantes.get(index).getOperando());
                duplicarTipoConstante.setTipo(filtradosListaTiposConstantes.get(index).getTipo());
                duplicarTipoConstante.setValorreal(filtradosListaTiposConstantes.get(index).getValorreal());
                duplicarTipoConstante.setValordate(filtradosListaTiposConstantes.get(index).getValordate());
                duplicarTipoConstante.setValorstring(filtradosListaTiposConstantes.get(index).getValorstring());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoConstante");
            PrimefacesContextUI.ejecutar("PF('DuplicarTipoConstante').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaTiposConstantes.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "TIPOSCONSTANTES");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (result == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (result == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (result == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (result == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSCONSTANTES")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    //GUARDAR
    public void guardarCambiosTiposConstantes() {

        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaTiposConstantesBorrar.isEmpty()) {
                for (int i = 0; i < listaTiposConstantesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaTiposConstantesBorrar.size());
                    administrarTiposConstantes.borrarTiposConstantes(listaTiposConstantesBorrar.get(i));
                }
                System.out.println("Entra");
                listaTiposConstantesBorrar.clear();
            }

            if (!listaTiposConstantesCrear.isEmpty()) {
                for (int i = 0; i < listaTiposConstantesCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarTiposConstantes.crearTiposConstantes(listaTiposConstantesCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaTiposConstantesCrear.clear();
            }
            if (!listaTiposConstantesModificar.isEmpty()) {
                for (int i = 0; i < listaTiposConstantesModificar.size(); i++) {
                    administrarTiposConstantes.modificarTiposConstantes(listaTiposConstantesModificar.get(i));
                }
                listaTiposConstantesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaTiposConstantes = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

    public void seleccionarTipo(String estadoTipo, int indice, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoLista == 0) {
            if (estadoTipo.equals("NUMERICO")) {
                listaTiposConstantes.get(indice).setTipo("N");
                numericoB = false;
                cadenaB = true;
                fechaB = true;
                listaTiposConstantes.get(indice).setValordate(null);
                listaTiposConstantes.get(indice).setValorstring(null);
            } else if (estadoTipo.equals("FECHA")) {
                listaTiposConstantes.get(indice).setTipo("F");
                numericoB = true;
                cadenaB = true;
                fechaB = false;
                listaTiposConstantes.get(indice).setValorreal(null);
                listaTiposConstantes.get(indice).setValorstring(null);

            } else if (estadoTipo.equals("CADENA")) {
                listaTiposConstantes.get(indice).setTipo("C");
                numericoB = true;
                cadenaB = true;
                fechaB = false;
                listaTiposConstantes.get(indice).setValordate(null);
                listaTiposConstantes.get(indice).setValorreal(null);
            }

            if (!listaTiposConstantesCrear.contains(listaTiposConstantes.get(indice))) {
                if (listaTiposConstantesModificar.isEmpty()) {
                    listaTiposConstantesModificar.add(listaTiposConstantes.get(indice));
                } else if (!listaTiposConstantesModificar.contains(listaTiposConstantes.get(indice))) {
                    listaTiposConstantesModificar.add(listaTiposConstantes.get(indice));
                }
            }
        } else {
            if (estadoTipo.equals("NUMERICO")) {
                filtradosListaTiposConstantes.get(indice).setTipo("N");
                numericoB = false;
                cadenaB = true;
                fechaB = true;
                filtradosListaTiposConstantes.get(indice).setValordate(null);
                filtradosListaTiposConstantes.get(indice).setValorstring(null);
            } else if (estadoTipo.equals("FECHA")) {
                filtradosListaTiposConstantes.get(indice).setTipo("F");
                numericoB = true;
                cadenaB = true;
                fechaB = false;
                filtradosListaTiposConstantes.get(indice).setValorreal(null);
                filtradosListaTiposConstantes.get(indice).setValorstring(null);
            } else if (estadoTipo.equals("CADENA")) {
                filtradosListaTiposConstantes.get(indice).setTipo("C");
                numericoB = true;
                cadenaB = true;
                fechaB = false;
                filtradosListaTiposConstantes.get(indice).setValordate(null);
                filtradosListaTiposConstantes.get(indice).setValorreal(null);
            }
            if (!listaTiposConstantesCrear.contains(filtradosListaTiposConstantes.get(indice))) {
                if (listaTiposConstantesModificar.isEmpty()) {
                    listaTiposConstantesModificar.add(filtradosListaTiposConstantes.get(indice));
                } else if (!listaTiposConstantesModificar.contains(filtradosListaTiposConstantes.get(indice))) {
                    listaTiposConstantesModificar.add(filtradosListaTiposConstantes.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }
        RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
    }

    public void seleccionarTipoNuevoTipoConstante(String estadoTipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipo.equals("NUMERICO")) {
                nuevoTipoConstante.setTipo("N");
                numericoBN = false;
                cadenaBN = true;
                fechaBN = true;
                nuevoTipoConstante.setValordate(null);
                nuevoTipoConstante.setValorstring(null);
            } else if (estadoTipo.equals("FECHA")) {
                nuevoTipoConstante.setTipo("F");
                numericoBN = true;
                cadenaBN = true;
                fechaBN = false;
                nuevoTipoConstante.setValorreal(null);
                nuevoTipoConstante.setValorstring(null);
            } else if (estadoTipo.equals("CADENA")) {
                nuevoTipoConstante.setTipo("C");
                numericoBN = true;
                cadenaBN = true;
                fechaBN = false;
                nuevoTipoConstante.setValordate(null);
                nuevoTipoConstante.setValorreal(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorReal");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorDate");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorCadena");
        } else {
            if (estadoTipo.equals("NUMERICO")) {
                duplicarTipoConstante.setTipo("N");
                numericoBD = false;
                cadenaBD = true;
                fechaBD = true;
                duplicarTipoConstante.setValordate(null);
                duplicarTipoConstante.setValorstring(null);
            } else if (estadoTipo.equals("FECHA")) {
                duplicarTipoConstante.setTipo("F");
                numericoBD = true;
                cadenaBD = true;
                fechaBD = false;
                duplicarTipoConstante.setValorreal(null);
                duplicarTipoConstante.setValorstring(null);
            } else if (estadoTipo.equals("CADENA")) {
                duplicarTipoConstante.setTipo("C");
                numericoBD = true;
                cadenaBD = true;
                fechaBD = false;
                duplicarTipoConstante.setValordate(null);
                duplicarTipoConstante.setValorreal(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorReal");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorDate");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorCadena");
        }

    }

    public void revisarTipo(int indice) {
        if (listaTiposConstantes.get(indice).getTipo().equals("N")) {
            numericoB = false;
            fechaB = true;
            cadenaB = true;
        } else if (listaTiposConstantes.get(indice).getTipo().equals("F")) {
            numericoB = true;
            fechaB = false;
            cadenaB = true;
        } else if (listaTiposConstantes.get(indice).getTipo().equals("C")) {
            numericoB = true;
            fechaB = true;
            cadenaB = false;
        }
    }

    public void revisarTipoN() {
        if (nuevoTipoConstante.getTipo().equals("N")) {
            numericoBN = false;
            fechaBN = true;
            cadenaBN = true;
        } else if (nuevoTipoConstante.getTipo().equals("F")) {
            numericoBN = true;
            fechaBN = false;
            cadenaBN = true;
        } else if (nuevoTipoConstante.getTipo().equals("C")) {
            numericoBN = true;
            fechaBN = true;
            cadenaBN = false;
        }
    }

    public void revisarTipoD() {
        if (duplicarTipoConstante.getTipo().equals("N")) {
            numericoBD = false;
            fechaBD = true;
            cadenaBD = true;
        } else if (duplicarTipoConstante.getTipo().equals("F")) {
            numericoBD = true;
            fechaBD = false;
            cadenaBD = true;
        } else if (duplicarTipoConstante.getTipo().equals("C")) {
            numericoBD = true;
            fechaBD = true;
            cadenaBD = false;
        }
    }

//AUTOCOMPLETAR
    public void modificarTiposConstantes(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposConstantesCrear.contains(listaTiposConstantes.get(index))) {

                    if (listaTiposConstantesModificar.isEmpty()) {
                        listaTiposConstantesModificar.add(listaTiposConstantes.get(index));
                    } else if (!listaTiposConstantesModificar.contains(listaTiposConstantes.get(index))) {
                        listaTiposConstantesModificar.add(listaTiposConstantes.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaTiposConstantesCrear.contains(filtradosListaTiposConstantes.get(index))) {

                    if (listaTiposConstantesCrear.isEmpty()) {
                        listaTiposConstantesCrear.add(filtradosListaTiposConstantes.get(index));
                    } else if (!listaTiposConstantesCrear.contains(filtradosListaTiposConstantes.get(index))) {
                        listaTiposConstantesCrear.add(filtradosListaTiposConstantes.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void agregarNuevoTipoConstante() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTipoConstante.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (nuevoTipoConstante.getFechafinal() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
            pasa++;
        }

        if (nuevoTipoConstante.getFechainicial() != null && nuevoTipoConstante.getFechafinal() != null) {
            if (nuevoTipoConstante.getFechafinal().before(nuevoTipoConstante.getFechainicial())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
                pasa2++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoConstante");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoConstante').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                altoTabla = "270";
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
                tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
                tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
                tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
                tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
                tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
                tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
                bandera = 0;
                filtradosListaTiposConstantes = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoConstante.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoTipoConstante.setOperando(operando);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaTiposConstantesCrear.add(nuevoTipoConstante);
            listaTiposConstantes.add(nuevoTipoConstante);
            nuevoTipoConstante = new TiposConstantes();
            RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoTipoConstante').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarTipoConstante.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoConstante");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoTipoConstante').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                altoTabla = "270";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
                tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
                tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
                tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
                tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
                tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
                tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
                tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
                bandera = 0;
                filtradosListaTiposConstantes = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //Falta Ponerle el Operando al cual se agregará
            duplicarTipoConstante.setOperando(operando);
            listaTiposConstantes.add(duplicarTipoConstante);
            listaTiposConstantesCrear.add(duplicarTipoConstante);

            index = -1;
            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            duplicarTipoConstante = new TiposConstantes();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoConstante");
            PrimefacesContextUI.ejecutar("PF('DuplicarTipoConstante').hide()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposConstantesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposConstantesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposConstantesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposConstantesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoTiposConstantes() {
        nuevoTipoConstante = new TiposConstantes();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR Duplicar REGISTRO CIUDAD
    public void limpiarDuplicarTiposConstantes() {
        duplicarTipoConstante = new TiposConstantes();
        index = -1;
        secRegistro = null;
    }

    //Getter & Setter
    public List<TiposConstantes> getListaTiposConstantes() {
        if (listaTiposConstantes == null) {
            System.out.println("secOperando" + secOperando);
            System.out.println("tOperando" + tOperando);
            System.out.println("operando seleccionado" + operando);
            listaTiposConstantes = administrarTiposConstantes.buscarTiposConstantes(secOperando, tOperando);
        }
        return listaTiposConstantes;
    }

    public void setListaTiposConstantes(List<TiposConstantes> listaTiposConstantes) {
        this.listaTiposConstantes = listaTiposConstantes;
    }

    public List<TiposConstantes> getFiltradosListaTiposConstantes() {
        return filtradosListaTiposConstantes;
    }

    public void setFiltradosListaTiposConstantes(List<TiposConstantes> filtradosListaTiposConstantes) {
        this.filtradosListaTiposConstantes = filtradosListaTiposConstantes;
    }

    public TiposConstantes getEditarTiposConstantes() {
        return editarTiposConstantes;
    }

    public void setEditarTiposConstantes(TiposConstantes editarTiposConstantes) {
        this.editarTiposConstantes = editarTiposConstantes;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    public void setAceptarEditar(boolean aceptarEditar) {
        this.aceptarEditar = aceptarEditar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public TiposConstantes getNuevoTipoConstante() {
        return nuevoTipoConstante;
    }

    public void setNuevoTipoConstante(TiposConstantes nuevoTipoConstante) {
        this.nuevoTipoConstante = nuevoTipoConstante;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public BigInteger getSecOperando() {
        return secOperando;
    }

    public void setSecOperando(BigInteger secOperando) {
        this.secOperando = secOperando;
    }

    public String gettOperando() {
        return tOperando;
    }

    public void settOperando(String tOperando) {
        this.tOperando = tOperando;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public TiposConstantes getDuplicarTipoConstante() {
        return duplicarTipoConstante;
    }

    public void setDuplicarTipoConstante(TiposConstantes duplicarTipoConstante) {
        this.duplicarTipoConstante = duplicarTipoConstante;
    }

    public boolean isNumericoB() {
        return numericoB;
    }

    public void setNumericoB(boolean numericoB) {
        this.numericoB = numericoB;
    }

    public boolean isFechaB() {
        return fechaB;
    }

    public void setFechaB(boolean fechaB) {
        this.fechaB = fechaB;
    }

    public boolean isCadenaB() {
        return cadenaB;
    }

    public void setCadenaB(boolean cadenaB) {
        this.cadenaB = cadenaB;
    }

    public boolean isNumericoBN() {
        return numericoBN;
    }

    public void setNumericoBN(boolean numericoBN) {
        this.numericoBN = numericoBN;
    }

    public boolean isFechaBN() {
        return fechaBN;
    }

    public void setFechaBN(boolean fechaBN) {
        this.fechaBN = fechaBN;
    }

    public boolean isCadenaBN() {
        return cadenaBN;
    }

    public void setCadenaBN(boolean cadenaBN) {
        this.cadenaBN = cadenaBN;
    }

    public boolean isNumericoBD() {
        return numericoBD;
    }

    public void setNumericoBD(boolean numericoBD) {
        this.numericoBD = numericoBD;
    }

    public boolean isFechaBD() {
        return fechaBD;
    }

    public void setFechaBD(boolean fechaBD) {
        this.fechaBD = fechaBD;
    }

    public boolean isCadenaBD() {
        return cadenaBD;
    }

    public void setCadenaBD(boolean cadenaBD) {
        this.cadenaBD = cadenaBD;
    }

    public TiposConstantes getTipoConstanteSeleccionado() {
        return tipoConstanteSeleccionado;
    }

    public void setTipoConstanteSeleccionado(TiposConstantes tipoConstanteSeleccionado) {
        this.tipoConstanteSeleccionado = tipoConstanteSeleccionado;
    }

}
