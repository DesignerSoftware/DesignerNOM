/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Recordatorios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRecordatoriosInterface;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlProverbio implements Serializable {

    @EJB
    AdministrarRecordatoriosInterface administrarRecordatorios;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //LISTA PROVERBIOS
    private List<Recordatorios> listaProverbios;
    private List<Recordatorios> filtradosListaProverbios;
    private Recordatorios proverbioSeleccionado;
    //LISTA MENSAJES
    private List<Recordatorios> listaMensajesUsuario;
    private List<Recordatorios> filtradosListaMensajesUsuario;
    private Recordatorios mensajeUsuarioSeleccionado;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado, guardarOk;
    //Editar Celda
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista, tipoListaNF;
    //ALTO SCROLL TABLA
    private String altoTabla, altoTablaNF;
    private boolean cambiosPagina;
    //Crear Proverbio
    private List<Recordatorios> listaProverbiosCrear;
    public Recordatorios nuevoProverbio;
    public Recordatorios duplicarProverbio;
    private int k;
    private BigInteger l;
    private int m;
    private BigInteger n;
    //Crear Mensaje Usuario
    private List<Recordatorios> listaMensajesUsuariosCrear;
    public Recordatorios nuevoRegistroMensajesUsuarios;
    public Recordatorios duplicarRegistroMensajesUsuarios;
    //OTROS
    private int banderaNF;
    //Modificar Novedades
    private List<Recordatorios> listaProverbiosModificar;
    //Borrar Novedades
    private List<Recordatorios> listaProverbiosBorrar;
    //Cual Tabla
    private int CualTabla;
    //editar celda
    private Recordatorios editarProverbios;
    //Columnas Tabla Proverbio
    private Column pMensaje;
    //Columnas Tabla Detalles Tipos Cotizantes
    private Column mAno, mMes, mDia, mMensaje;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Cual Insertar
    private String cualInsertar;
    //Cual Nuevo Update
    private String cualNuevo;
    //Modificar Detalles Tipos Cotizantes
    private List<Recordatorios> listaMensajesUsuariosModificar;
    private List<Recordatorios> listaMensajesUsuariosBorrar;
    private String paginaAnterior, mensaje;
    private int ano, dia, mes;
    private List<Short> anios;
    private Short anioactual;
    private String infoRegistroProverbios, infoRegistroMsgUsuario;

    public ControlProverbio() {
        cambiosPagina = true;
        permitirIndex = true;
        listaProverbios = null;
        listaMensajesUsuario = null;
        permitirIndex = true;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        altoTabla = "115";
        altoTablaNF = "115";
        tipoListaNF = 0;
        tablaImprimir = ":formExportar:datosProverbiosExportar";
        nombreArchivo = "ProverbiosXML";
        k = 0;
        cualInsertar = ":formularioDialogos:NuevoRegistroProverbio";
        cualNuevo = ":formularioDialogos:nuevoRegistroProverbio";
        //Crear Vigencia Formal
        nuevoProverbio = new Recordatorios();
        duplicarProverbio = new Recordatorios();
        nuevoProverbio.setTipo("PROVERBIO");
        nuevoRegistroMensajesUsuarios = new Recordatorios();
        nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");
        duplicarRegistroMensajesUsuarios = new Recordatorios();
        //secuenciaPersona = BigInteger.valueOf(10668967);
        aceptar = true;
        listaMensajesUsuariosBorrar = new ArrayList<Recordatorios>();
        listaMensajesUsuariosCrear = new ArrayList<Recordatorios>();
        listaMensajesUsuariosModificar = new ArrayList<Recordatorios>();
        listaProverbiosBorrar = new ArrayList<Recordatorios>();
        listaProverbiosCrear = new ArrayList<Recordatorios>();
        listaProverbiosModificar = new ArrayList<Recordatorios>();
        //Inicializar LOVS
        proverbioSeleccionado = null;
        m = 0;
        Date datofecha = new Date();
        anioactual = new Short(String.valueOf(datofecha.getYear()));
        anios = new ArrayList<Short>();
        for (int i = (anioactual - 10); i < (anioactual + 10); i++) {
            Short n = new Short(String.valueOf(i + 1900));
            anios.add(n);
        }
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRecordatorios.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
       
        paginaAnterior = pagina;
        listaProverbios = null;
        listaMensajesUsuario = null;
        getListaMensajesUsuario();
        getListaProverbios();
        if (listaProverbios != null) {
            if(!listaProverbios.isEmpty())
            modificarInfoRegistroProverbios(listaProverbios.size());
        } else {
            modificarInfoRegistroProverbios(0);
        }

        if (listaMensajesUsuario != null) {
            if(!listaMensajesUsuario.isEmpty())
            modificarInfoRegistroMsgUsuarios(listaMensajesUsuario.size());
        } else {
            modificarInfoRegistroMsgUsuarios(0);
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    //GUARDAR
    public void guardarCambiosProverbios() {
        System.out.println("guardarCambiosProverbios : ");
        System.out.println("listaProverbios" + listaProverbios);
        System.out.println("listaProverbiosCrear" + listaProverbiosCrear);
        System.out.println("listaProverbiosBorrar" + listaProverbiosBorrar);
        System.out.println(" listaProverbiosModificar " + listaProverbiosModificar);
        System.out.println("guardarMensajeUsuarios");
        System.out.println("listaMensajesUsuario" + listaMensajesUsuario);
        System.out.println("listaMensajesUsuariosCrear" + listaMensajesUsuariosCrear);
        System.out.println("listaMensajesUsuariosBorrar" + listaMensajesUsuariosBorrar);
        System.out.println("listaMensajesUsuariosModificar" + listaMensajesUsuariosModificar);

        if (guardado == false) {
            if (!listaProverbiosBorrar.isEmpty()) {
                for (int i = 0; i < listaProverbiosBorrar.size(); i++) {
                    administrarRecordatorios.borrar(listaProverbiosBorrar.get(i));
                }
                listaProverbiosBorrar.clear();
            }
        }
        if (!listaProverbiosCrear.isEmpty()) {
            for (int i = 0; i < listaProverbiosCrear.size(); i++) {
                administrarRecordatorios.crear(listaProverbiosCrear.get(i));
            }

        }
        listaProverbiosCrear.clear();
        if (!listaProverbiosModificar.isEmpty()) {
            administrarRecordatorios.modificar(listaProverbiosModificar);
            listaProverbiosModificar.clear();
        }
        if (guardado == false) {
            if (!listaMensajesUsuariosBorrar.isEmpty()) {
                for (int i = 0; i < listaMensajesUsuariosBorrar.size(); i++) {
                    administrarRecordatorios.borrarMU(listaMensajesUsuariosBorrar.get(i));
                    listaMensajesUsuariosBorrar.clear();
                }
            }
            if (!listaMensajesUsuariosCrear.isEmpty()) {
                for (int i = 0; i < listaMensajesUsuariosCrear.size(); i++) {
                    administrarRecordatorios.crearMU(listaMensajesUsuariosCrear.get(i));
                }
            }
            listaMensajesUsuariosCrear.clear();
        }
        if (!listaMensajesUsuariosModificar.isEmpty()) {
            administrarRecordatorios.modificarMU(listaMensajesUsuariosModificar);
            listaMensajesUsuariosModificar.clear();
        }
        listaMensajesUsuario = null;
        listaProverbios = null;
        getListaMensajesUsuario();
        getListaProverbios();
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosProverbios");
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
        guardado = true;
        permitirIndex = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimefacesContextUI.actualizar("form:growl");
        mensajeUsuarioSeleccionado = null;
        proverbioSeleccionado = null;
    }

    //Ubicacion Celda.
    public void cambiarIndice(Recordatorios proverbio, int celda) {
        if (permitirIndex == true) {
            proverbioSeleccionado = proverbio;
            cualCelda = celda;
            CualTabla = 0;
            mensajeUsuarioSeleccionado = null;
            if (tipoLista == 0) {
                proverbioSeleccionado.getSecuencia();

            } else {
                proverbioSeleccionado.getSecuencia();
            }
        }
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(Recordatorios msgUsuario, int celdaNF) {

        if (permitirIndex == true) {
            mensajeUsuarioSeleccionado = msgUsuario;
            cualCelda = celdaNF;
            CualTabla = 1;
            tablaImprimir = ":formExportar:datosMensajesUsuariosExportar";
            cualNuevo = ":formularioDialogos:nuevoMensajeUsuarios";
            cualInsertar = "formularioDialogos:NuevoRegistroMensajeUsuario";
            nombreArchivo = "MensajeUsuarioXML";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:exportarXML");
            if (tipoListaNF == 0) {
                mensajeUsuarioSeleccionado.getSecuencia();
            } else {
                mensajeUsuarioSeleccionado.getSecuencia();
            }
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoProverbio() {
        nuevoProverbio = new Recordatorios();
        nuevoProverbio.setTipo("PROVERBIO");
        proverbioSeleccionado = null;
        proverbioSeleccionado = null;

    }

    //FILTRADO
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0 && CualTabla == 0) {
            altoTabla = "95";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosProverbios");
            bandera = 1;
        } else if (bandera == 1 && CualTabla == 0) {
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
        } else if (banderaNF == 0 && CualTabla == 1) {
            altoTablaNF = "91";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("width: 85%");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("width: 85%");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("width: 85%");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
            banderaNF = 1;
        } else if (banderaNF == 1 && CualTabla == 1) {
            altoTablaNF = "115";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("display: none; visibility: hidden;");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("display: none; visibility: hidden;");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("display: none; visibility: hidden;");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProverbiosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "ProverbiosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMensajesUsuariosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "MensajesUsuariosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            proverbioSeleccionado = null;
        }
    }

    public void exportXLS() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProverbiosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "ProverbiosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMensajesUsuariosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "MensajesUsuariosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void tablaNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if ((listaProverbios.isEmpty() || listaMensajesUsuario.isEmpty())) {
            PrimefacesContextUI.actualizar("formularioDialogos:elegirTabla");
            PrimefacesContextUI.ejecutar("PF('elegirTabla').show()");
        } else if (CualTabla == 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroProverbio");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroProverbio').show()");
        } else if (CualTabla == 1) {
            Short year = new Short(String.valueOf(anioactual));
            nuevoRegistroMensajesUsuarios.setAno(year);
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroMensajeUsuario");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroMensajeUsuario').show()");
        }
    }

    //AUTOCOMPLETAR
    public void modificarProverbio(Recordatorios proverbio, String confirmarCambio, String valorConfirmar) {
        proverbioSeleccionado = proverbio;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaProverbiosCrear.contains(proverbioSeleccionado)) {

                    if (listaProverbiosModificar.isEmpty()) {
                        listaProverbiosModificar.add(proverbioSeleccionado);
                    } else if (!listaProverbiosModificar.contains(proverbioSeleccionado)) {
                        listaProverbiosModificar.add(proverbioSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");

                    }
                }

            } else {
                if (!listaProverbiosCrear.contains(proverbioSeleccionado)) {

                    if (listaProverbiosCrear.isEmpty()) {
                        listaProverbiosCrear.add(proverbioSeleccionado);
                    } else if (!listaProverbiosCrear.contains(proverbioSeleccionado)) {
                        listaProverbiosCrear.add(proverbioSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }
            }
            PrimefacesContextUI.actualizar("form:datosProverbios");
        }
    }

    //AUTOCOMPLETAR
    public void modificarMensajeUsuario(Recordatorios msgUsuario, String confirmarCambio, String valorConfirmar) {
        mensajeUsuarioSeleccionado = msgUsuario;
        int coincidencias = 0;
        int indiceNFUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {

                    if (listaMensajesUsuariosModificar.isEmpty()) {
                        listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                    } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                        listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }

            } else {
                if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {

                    if (listaMensajesUsuariosCrear.isEmpty()) {
                        listaMensajesUsuariosCrear.add(mensajeUsuarioSeleccionado);
                    } else if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                        listaMensajesUsuariosCrear.add(mensajeUsuarioSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }
            }
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (proverbioSeleccionado != null && CualTabla == 0) {
            if (tipoLista == 0) {
                editarProverbios = proverbioSeleccionado;
            }
            if (tipoLista == 1) {
                editarProverbios = proverbioSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarMensajes");
                PrimefacesContextUI.ejecutar("PF('editarMensajes').show()");
                cualCelda = -1;
            }
            proverbioSeleccionado = null;
        } else if (mensajeUsuarioSeleccionado != null && CualTabla == 1) {
            if (tipoListaNF == 0) {
                editarProverbios = mensajeUsuarioSeleccionado;
            }
            if (tipoListaNF == 1) {
                editarProverbios = mensajeUsuarioSeleccionado;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarAños");
                PrimefacesContextUI.ejecutar("PF('editarAños').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarMeses");
                PrimefacesContextUI.ejecutar("PF('editarMeses').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDias");
                PrimefacesContextUI.ejecutar("PF('editarDias').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarMensajes2");
                PrimefacesContextUI.ejecutar("PF('editarMensajes2').show()");
                cualCelda = -1;
            }
            mensajeUsuarioSeleccionado = null;
        }
        proverbioSeleccionado = null;
    }

    public void dialogoProverbios() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosProverbios");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroProverbio').show()");
    }

    public void dialogoMensajesUsuarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroMensajeUsuario').show()");
    }

    //CREAR NUEVO PROVERBIO
    public void agregarNuevoProverbio() {
        int pasa = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (pasa == 0) {
            if (bandera == 1 && CualTabla == 0) {
                altoTabla = "115";
                pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
                pMensaje.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosProverbios");
                bandera = 0;
                filtradosListaProverbios = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoProverbio.setSecuencia(l);
            nuevoProverbio.setTipo("PROVERBIO");

            listaProverbiosCrear.add(nuevoProverbio);
            listaProverbios.add(nuevoProverbio);
            proverbioSeleccionado = nuevoProverbio;
            modificarInfoRegistroProverbios(listaProverbios.size());
            nuevoProverbio = new Recordatorios();
            PrimefacesContextUI.actualizar("form:datosProverbios");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroProverbio').hide()");
        } else {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevoProverbio");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoProverbio').show()");
        }
    }

    public void agregarNuevoMensajeUsuario() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (pasa == 0) {
            if (bandera == 1 && CualTabla == 0) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaNF = "115";
                mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
                mAno.setFilterStyle("display: none; visibility: hidden;");
                mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
                mMes.setFilterStyle("display: none; visibility: hidden;");
                mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
                mDia.setFilterStyle("display: none; visibility: hidden;");
                mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
                mMensaje.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
                banderaNF = 0;
                filtradosListaMensajesUsuario = null;
                tipoListaNF = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS FORMALES.
            k++;
            l = BigInteger.valueOf(k);
            nuevoRegistroMensajesUsuarios.setSecuencia(l);
            nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");
            listaMensajesUsuariosCrear.add(nuevoRegistroMensajesUsuarios);
            listaMensajesUsuario.add(nuevoRegistroMensajesUsuarios);
            modificarInfoRegistroMsgUsuarios(listaMensajesUsuario.size());
            mensajeUsuarioSeleccionado = nuevoRegistroMensajesUsuarios;
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");

            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            nuevoRegistroMensajesUsuarios = new Recordatorios();
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroMensajeUsuario').hide()");
        } else {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevoMensajeUsuario");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoMensajeUsuario').show()");
        }
    }

    //DUPLICAR PROVERBIO
    public void duplicarP() {
        if (proverbioSeleccionado != null && CualTabla == 0) {
            duplicarProverbio = new Recordatorios();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarProverbio.setSecuencia(l);
                duplicarProverbio.setMensaje(proverbioSeleccionado.getMensaje());
                duplicarProverbio.setTipo(proverbioSeleccionado.getTipo());
            }
            if (tipoLista == 1) {
                duplicarProverbio.setSecuencia(l);
                duplicarProverbio.setMensaje(proverbioSeleccionado.getMensaje());
                duplicarProverbio.setTipo(proverbioSeleccionado.getTipo());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroProverbio");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroProverbio').show()");
        } else if (mensajeUsuarioSeleccionado != null && CualTabla == 1) {
            duplicarRegistroMensajesUsuarios = new Recordatorios();
            m++;
            n = BigInteger.valueOf(m);
            if (tipoListaNF == 0) {
                duplicarRegistroMensajesUsuarios.setSecuencia(n);
                duplicarRegistroMensajesUsuarios.setAno(mensajeUsuarioSeleccionado.getAno());
                duplicarRegistroMensajesUsuarios.setMes(mensajeUsuarioSeleccionado.getMes());
                duplicarRegistroMensajesUsuarios.setDia(mensajeUsuarioSeleccionado.getDia());
                duplicarRegistroMensajesUsuarios.setTipo(mensajeUsuarioSeleccionado.getTipo());
                duplicarRegistroMensajesUsuarios.setMensaje(mensajeUsuarioSeleccionado.getMensaje());
            }
            if (tipoListaNF == 1) {
                duplicarRegistroMensajesUsuarios.setSecuencia(n);
                duplicarRegistroMensajesUsuarios.setAno(mensajeUsuarioSeleccionado.getAno());
                duplicarRegistroMensajesUsuarios.setMes(mensajeUsuarioSeleccionado.getMes());
                duplicarRegistroMensajesUsuarios.setDia(mensajeUsuarioSeleccionado.getDia());
                duplicarRegistroMensajesUsuarios.setTipo(mensajeUsuarioSeleccionado.getTipo());
                duplicarRegistroMensajesUsuarios.setMensaje(mensajeUsuarioSeleccionado.getMensaje());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroMensajeUsuario");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroMensajeUsuario').show()");

        }
    }

    public void seleccionarAno(String estadoAno, int indice, int celda) {
        mensajeUsuarioSeleccionado.setAno(new Short(estadoAno));
//        if (tipoLista == 0) {
////            if (estadoAno != null) {
////                if (estadoAno.equals("2005")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2005"));
////                } else if (estadoAno.equals("2006")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2006"));
////                } else if (estadoAno.equals("2007")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2007"));
////                } else if (estadoAno.equals("2008")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2008"));
////                } else if (estadoAno.equals("2009")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2009"));
////                } else if (estadoAno.equals("2010")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2010"));
////                } else if (estadoAno.equals("2011")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2011"));
////                } else if (estadoAno.equals("2012")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2012"));
////                } else if (estadoAno.equals("2013")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2013"));
////                } else if (estadoAno.equals("2014")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2014"));
////                } else if (estadoAno.equals("2015")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2015"));
////                } else if (estadoAno.equals("2016")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2016"));
////                } else if (estadoAno.equals("2017")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2017"));
////                } else if (estadoAno.equals("2018")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2018"));
////                } else if (estadoAno.equals("2019")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2019"));
////                } else if (estadoAno.equals("2020")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2020"));
////                } else if (estadoAno.equals("2021")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2021"));
////                } else if (estadoAno.equals("2022")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2022"));
////                } else if (estadoAno.equals("2023")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2023"));
////                } else if (estadoAno.equals("2024")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2024"));
////                } else if (estadoAno.equals("2025")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("2025"));
////                } else if (estadoAno.equalsIgnoreCase("TODOS LOS AÑOS")) {
////                    mensajeUsuarioSeleccionado.setAno(new Short("0"));
////                }
////            } else {
////                mensajeUsuarioSeleccionado.setAno(null);
////            }
        if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
            if (listaMensajesUsuariosModificar.isEmpty()) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            }
        }
//        } else {
//            if (estadoAno != null) {
//                if (estadoAno.equals("2005")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2005"));
//                } else if (estadoAno.equals("2006")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2006"));
//                } else if (estadoAno.equals("2007")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2007"));
//                } else if (estadoAno.equals("2008")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2008"));
//                } else if (estadoAno.equals("2009")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2009"));
//                } else if (estadoAno.equals("2010")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2010"));
//                } else if (estadoAno.equals("2011")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2011"));
//                } else if (estadoAno.equals("2012")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2012"));
//                } else if (estadoAno.equals("2013")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2013"));
//                } else if (estadoAno.equals("2014")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2014"));
//                } else if (estadoAno.equals("2015")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2015"));
//                } else if (estadoAno.equals("2016")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2016"));
//                } else if (estadoAno.equals("2017")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2017"));
//                } else if (estadoAno.equals("2018")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2018"));
//                } else if (estadoAno.equals("2019")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2019"));
//                } else if (estadoAno.equals("2020")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2020"));
//                } else if (estadoAno.equals("2021")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2021"));
//                } else if (estadoAno.equals("2022")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2022"));
//                } else if (estadoAno.equals("2023")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2023"));
//                } else if (estadoAno.equals("2024")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2024"));
//                } else if (estadoAno.equals("2025")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("2025"));
//                } else if (estadoAno.equalsIgnoreCase("TODOS LOS AÑOS")) {
//                    mensajeUsuarioSeleccionado.setAno(new Short("0"));
//                }
//            } else {
//                mensajeUsuarioSeleccionado.setAno(null);
//            }
//            if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
//                if (listaMensajesUsuariosModificar.isEmpty()) {
//                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
//                } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
//                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
//                }
//            }
//        }
//        if (guardado == true) {
//            guardado = false;
//            PrimefacesContextUI.actualizar("form:ACEPTAR");
//        }
//        RequestContext context = RequestContext.getCurrentInstance();
//        cambiosPagina = false;
//        PrimefacesContextUI.actualizar("form:ACEPTAR");
//        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");

    }

    public void seleccionarMes(String estadoMes, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoMes != null) {
                if (estadoMes.equalsIgnoreCase("ENERO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("1"));
                } else if (estadoMes.equalsIgnoreCase("FEBRERO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("2"));
                } else if (estadoMes.equalsIgnoreCase("MARZO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("3"));
                } else if (estadoMes.equalsIgnoreCase("ABRIL")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("4"));
                } else if (estadoMes.equalsIgnoreCase("MAYO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("5"));
                } else if (estadoMes.equalsIgnoreCase("JUNIO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("6"));
                } else if (estadoMes.equalsIgnoreCase("JULIO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("7"));
                } else if (estadoMes.equalsIgnoreCase("AGOSTO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("8"));
                } else if (estadoMes.equalsIgnoreCase("SEPTIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("9"));
                } else if (estadoMes.equalsIgnoreCase("OCTUBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("10"));
                } else if (estadoMes.equalsIgnoreCase("NOVIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("11"));
                } else if (estadoMes.equalsIgnoreCase("DICIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("12"));
                } else if (estadoMes.equalsIgnoreCase("TODOS LOS MESES")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("0"));
                }
            } else {
                mensajeUsuarioSeleccionado.setMes(null);
            }
            if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                if (listaMensajesUsuariosModificar.isEmpty()) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                }
            }
        } else {
            if (estadoMes != null) {
                if (estadoMes.equalsIgnoreCase("ENERO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("1"));
                } else if (estadoMes.equalsIgnoreCase("FEBRERO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("2"));
                } else if (estadoMes.equalsIgnoreCase("MARZO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("3"));
                } else if (estadoMes.equalsIgnoreCase("ABRIL")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("4"));
                } else if (estadoMes.equalsIgnoreCase("MAYO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("5"));
                } else if (estadoMes.equalsIgnoreCase("JUNIO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("6"));
                } else if (estadoMes.equalsIgnoreCase("JULIO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("7"));
                } else if (estadoMes.equalsIgnoreCase("AGOSTO")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("8"));
                } else if (estadoMes.equalsIgnoreCase("SEPTIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("9"));
                } else if (estadoMes.equalsIgnoreCase("OCTUBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("10"));
                } else if (estadoMes.equalsIgnoreCase("NOVIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("11"));
                } else if (estadoMes.equalsIgnoreCase("DICIEMBRE")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("12"));
                } else if (estadoMes.equalsIgnoreCase("TODOS LOS MESES")) {
                    mensajeUsuarioSeleccionado.setMes(new Short("0"));
                }
            } else {
                mensajeUsuarioSeleccionado.setMes(null);
            }
            if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                if (listaMensajesUsuariosModificar.isEmpty()) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");

        }
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = false;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");

    }

    public void seleccionarDia(String estadoDia, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoDia != null) {
                if (estadoDia.equalsIgnoreCase("01")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("1"));
                } else if (estadoDia.equalsIgnoreCase("02")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("2"));
                } else if (estadoDia.equalsIgnoreCase("03")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("3"));
                } else if (estadoDia.equalsIgnoreCase("04")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("4"));
                } else if (estadoDia.equalsIgnoreCase("05")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("5"));
                } else if (estadoDia.equalsIgnoreCase("06")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("6"));
                } else if (estadoDia.equalsIgnoreCase("07")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("7"));
                } else if (estadoDia.equalsIgnoreCase("08")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("8"));
                } else if (estadoDia.equalsIgnoreCase("09")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("9"));
                } else if (estadoDia.equalsIgnoreCase("10")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("10"));
                } else if (estadoDia.equalsIgnoreCase("11")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("11"));
                } else if (estadoDia.equalsIgnoreCase("12")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("12"));
                } else if (estadoDia.equalsIgnoreCase("13")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("13"));
                } else if (estadoDia.equalsIgnoreCase("14")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("14"));
                } else if (estadoDia.equalsIgnoreCase("15")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("15"));
                } else if (estadoDia.equalsIgnoreCase("16")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("16"));
                } else if (estadoDia.equalsIgnoreCase("17")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("17"));
                } else if (estadoDia.equalsIgnoreCase("18")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("18"));
                } else if (estadoDia.equalsIgnoreCase("19")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("19"));
                } else if (estadoDia.equalsIgnoreCase("20")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("20"));
                } else if (estadoDia.equalsIgnoreCase("21")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("21"));
                } else if (estadoDia.equalsIgnoreCase("22")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("22"));
                } else if (estadoDia.equalsIgnoreCase("23")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("23"));
                } else if (estadoDia.equalsIgnoreCase("24")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("24"));
                } else if (estadoDia.equalsIgnoreCase("25")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("25"));
                } else if (estadoDia.equalsIgnoreCase("26")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("26"));
                } else if (estadoDia.equalsIgnoreCase("27")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("27"));
                } else if (estadoDia.equalsIgnoreCase("28")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("28"));
                } else if (estadoDia.equalsIgnoreCase("29")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("29"));
                } else if (estadoDia.equalsIgnoreCase("30")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("30"));
                } else if (estadoDia.equalsIgnoreCase("31")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("31"));
                } else if (estadoDia.equalsIgnoreCase("TODOS LOS DIAS")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("0"));
                }
            } else {
                mensajeUsuarioSeleccionado.setDia(null);
            }
            if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                if (listaMensajesUsuariosModificar.isEmpty()) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                }
            }
        } else {
            if (estadoDia != null) {
                if (estadoDia.equalsIgnoreCase("01")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("1"));
                } else if (estadoDia.equalsIgnoreCase("02")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("2"));
                } else if (estadoDia.equalsIgnoreCase("03")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("3"));
                } else if (estadoDia.equalsIgnoreCase("04")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("4"));
                } else if (estadoDia.equalsIgnoreCase("05")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("5"));
                } else if (estadoDia.equalsIgnoreCase("06")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("6"));
                } else if (estadoDia.equalsIgnoreCase("07")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("7"));
                } else if (estadoDia.equalsIgnoreCase("08")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("8"));
                } else if (estadoDia.equalsIgnoreCase("09")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("9"));
                } else if (estadoDia.equalsIgnoreCase("10")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("10"));
                } else if (estadoDia.equalsIgnoreCase("11")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("11"));
                } else if (estadoDia.equalsIgnoreCase("12")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("12"));
                } else if (estadoDia.equalsIgnoreCase("13")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("13"));
                } else if (estadoDia.equalsIgnoreCase("14")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("14"));
                } else if (estadoDia.equalsIgnoreCase("15")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("15"));
                } else if (estadoDia.equalsIgnoreCase("16")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("16"));
                } else if (estadoDia.equalsIgnoreCase("17")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("17"));
                } else if (estadoDia.equalsIgnoreCase("18")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("18"));
                } else if (estadoDia.equalsIgnoreCase("19")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("19"));
                } else if (estadoDia.equalsIgnoreCase("20")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("20"));
                } else if (estadoDia.equalsIgnoreCase("21")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("21"));
                } else if (estadoDia.equalsIgnoreCase("22")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("22"));
                } else if (estadoDia.equalsIgnoreCase("23")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("23"));
                } else if (estadoDia.equalsIgnoreCase("24")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("24"));
                } else if (estadoDia.equalsIgnoreCase("25")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("25"));
                } else if (estadoDia.equalsIgnoreCase("26")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("26"));
                } else if (estadoDia.equalsIgnoreCase("27")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("27"));
                } else if (estadoDia.equalsIgnoreCase("28")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("28"));
                } else if (estadoDia.equalsIgnoreCase("29")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("29"));
                } else if (estadoDia.equalsIgnoreCase("30")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("30"));
                } else if (estadoDia.equalsIgnoreCase("31")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("31"));
                } else if (estadoDia.equalsIgnoreCase("TODOS LOS DIAS")) {
                    mensajeUsuarioSeleccionado.setDia(new Short("0"));
                }
            } else {
                mensajeUsuarioSeleccionado.setMes(null);
            }
            if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                if (listaMensajesUsuariosModificar.isEmpty()) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                    listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = false;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");

    }

    public void seleccionarTipoNuevoAno(String estadoAno, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (estadoAno != null) {
                if (estadoAno.equals("2005")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2005"));
                } else if (estadoAno.equals("2006")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2006"));
                } else if (estadoAno.equals("2007")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2007"));
                } else if (estadoAno.equals("2008")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2008"));
                } else if (estadoAno.equals("2009")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2009"));
                } else if (estadoAno.equals("2010")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2010"));
                } else if (estadoAno.equals("2011")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2011"));
                } else if (estadoAno.equals("2012")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2012"));
                } else if (estadoAno.equals("2013")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2013"));
                } else if (estadoAno.equals("2014")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2014"));
                } else if (estadoAno.equals("2015")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2015"));
                } else if (estadoAno.equals("2016")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2016"));
                } else if (estadoAno.equals("2017")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2017"));
                } else if (estadoAno.equals("2018")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2018"));
                } else if (estadoAno.equals("2019")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2019"));
                } else if (estadoAno.equals("2020")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2020"));
                } else if (estadoAno.equals("2021")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2021"));
                } else if (estadoAno.equals("2022")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2022"));
                } else if (estadoAno.equals("2023")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2023"));
                } else if (estadoAno.equals("2024")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2024"));
                } else if (estadoAno.equals("2025")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("2025"));
                } else if (estadoAno.equalsIgnoreCase("TODOS LOS AÑOS")) {
                    nuevoRegistroMensajesUsuarios.setAno(new Short("0"));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setAno(null);
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoAno");
        } else {
            if (estadoAno != null) {
                if (estadoAno.equals("2005")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2005"));
                } else if (estadoAno.equals("2006")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2006"));
                } else if (estadoAno.equals("2007")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2007"));
                } else if (estadoAno.equals("2008")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2008"));
                } else if (estadoAno.equals("2009")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2009"));
                } else if (estadoAno.equals("2010")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2010"));
                } else if (estadoAno.equals("2011")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2011"));
                } else if (estadoAno.equals("2012")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2012"));
                } else if (estadoAno.equals("2013")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2013"));
                } else if (estadoAno.equals("2014")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2014"));
                } else if (estadoAno.equals("2015")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2015"));
                } else if (estadoAno.equals("2016")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2016"));
                } else if (estadoAno.equals("2017")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2017"));
                } else if (estadoAno.equals("2018")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2018"));
                } else if (estadoAno.equals("2019")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2019"));
                } else if (estadoAno.equals("2020")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2020"));
                } else if (estadoAno.equals("2021")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2021"));
                } else if (estadoAno.equals("2022")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2022"));
                } else if (estadoAno.equals("2023")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2023"));
                } else if (estadoAno.equals("2024")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2024"));
                } else if (estadoAno.equals("2025")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("2025"));
                } else if (estadoAno.equalsIgnoreCase("TODOS LOS AÑOS")) {
                    duplicarRegistroMensajesUsuarios.setAno(new Short("0"));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setAno(null);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarAno");
        }

    }

    public void seleccionarTipoNuevoMes(String estadoMes, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (estadoMes != null) {
                if (estadoMes.equalsIgnoreCase("ENERO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("1"));
                } else if (estadoMes.equalsIgnoreCase("FEBRERO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("2"));
                } else if (estadoMes.equalsIgnoreCase("MARZO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("3"));
                } else if (estadoMes.equalsIgnoreCase("ABRIL")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("4"));
                } else if (estadoMes.equalsIgnoreCase("MAYO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("5"));
                } else if (estadoMes.equalsIgnoreCase("JUNIO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("6"));
                } else if (estadoMes.equalsIgnoreCase("JULIO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("7"));
                } else if (estadoMes.equalsIgnoreCase("AGOSTO")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("8"));
                } else if (estadoMes.equalsIgnoreCase("SEPTIEMBRE")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("9"));
                } else if (estadoMes.equalsIgnoreCase("OCTUBRE")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("10"));
                } else if (estadoMes.equalsIgnoreCase("NOVIEMBRE")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("11"));
                } else if (estadoMes.equalsIgnoreCase("DICIEMBRE")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("12"));
                } else if (estadoMes.equalsIgnoreCase("TODOS LOS MESES")) {
                    nuevoRegistroMensajesUsuarios.setMes(new Short("0"));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setMes(null);
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoMes");
        } else {
            if (estadoMes != null) {
                if (estadoMes.equalsIgnoreCase("ENERO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("1"));
                } else if (estadoMes.equalsIgnoreCase("FEBRERO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("2"));
                } else if (estadoMes.equalsIgnoreCase("MARZO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("3"));
                } else if (estadoMes.equalsIgnoreCase("ABRIL")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("4"));
                } else if (estadoMes.equalsIgnoreCase("MAYO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("5"));
                } else if (estadoMes.equalsIgnoreCase("JUNIO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("6"));
                } else if (estadoMes.equalsIgnoreCase("JULIO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("7"));
                } else if (estadoMes.equalsIgnoreCase("AGOSTO")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("8"));
                } else if (estadoMes.equalsIgnoreCase("SEPTIEMBRE")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("9"));
                } else if (estadoMes.equalsIgnoreCase("OCTUBRE")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("10"));
                } else if (estadoMes.equalsIgnoreCase("NOVIEMBRE")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("11"));
                } else if (estadoMes.equalsIgnoreCase("DICIEMBRE")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("12"));
                } else if (estadoMes.equalsIgnoreCase("TODOS LOS MESES")) {
                    duplicarRegistroMensajesUsuarios.setMes(new Short("0"));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setMes(null);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarMes");
        }

    }

    public void seleccionarTipoNuevoDia(String estadoDia, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (estadoDia != null) {
                if (estadoDia.equalsIgnoreCase("01")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("1"));
                } else if (estadoDia.equalsIgnoreCase("02")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("2"));
                } else if (estadoDia.equalsIgnoreCase("03")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("3"));
                } else if (estadoDia.equalsIgnoreCase("04")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("4"));
                } else if (estadoDia.equalsIgnoreCase("05")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("5"));
                } else if (estadoDia.equalsIgnoreCase("06")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("6"));
                } else if (estadoDia.equalsIgnoreCase("07")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("7"));
                } else if (estadoDia.equalsIgnoreCase("08")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("8"));
                } else if (estadoDia.equalsIgnoreCase("09")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("9"));
                } else if (estadoDia.equalsIgnoreCase("10")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("10"));
                } else if (estadoDia.equalsIgnoreCase("11")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("11"));
                } else if (estadoDia.equalsIgnoreCase("12")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("12"));
                } else if (estadoDia.equalsIgnoreCase("13")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("13"));
                } else if (estadoDia.equalsIgnoreCase("14")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("14"));
                } else if (estadoDia.equalsIgnoreCase("15")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("15"));
                } else if (estadoDia.equalsIgnoreCase("16")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("16"));
                } else if (estadoDia.equalsIgnoreCase("17")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("17"));
                } else if (estadoDia.equalsIgnoreCase("18")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("18"));
                } else if (estadoDia.equalsIgnoreCase("19")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("19"));
                } else if (estadoDia.equalsIgnoreCase("20")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("20"));
                } else if (estadoDia.equalsIgnoreCase("21")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("21"));
                } else if (estadoDia.equalsIgnoreCase("22")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("22"));
                } else if (estadoDia.equalsIgnoreCase("23")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("23"));
                } else if (estadoDia.equalsIgnoreCase("24")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("24"));
                } else if (estadoDia.equalsIgnoreCase("25")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("25"));
                } else if (estadoDia.equalsIgnoreCase("26")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("26"));
                } else if (estadoDia.equalsIgnoreCase("27")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("27"));
                } else if (estadoDia.equalsIgnoreCase("28")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("28"));
                } else if (estadoDia.equalsIgnoreCase("29")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("29"));
                } else if (estadoDia.equalsIgnoreCase("30")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("30"));
                } else if (estadoDia.equalsIgnoreCase("31")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("31"));
                } else if (estadoDia.equalsIgnoreCase("TODOS LOS DIAS")) {
                    nuevoRegistroMensajesUsuarios.setDia(new Short("0"));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setDia(null);
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoDia");
        } else {
            if (estadoDia != null) {
                if (estadoDia.equalsIgnoreCase("01")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("1"));
                } else if (estadoDia.equalsIgnoreCase("02")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("2"));
                } else if (estadoDia.equalsIgnoreCase("03")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("3"));
                } else if (estadoDia.equalsIgnoreCase("04")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("4"));
                } else if (estadoDia.equalsIgnoreCase("05")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("5"));
                } else if (estadoDia.equalsIgnoreCase("06")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("6"));
                } else if (estadoDia.equalsIgnoreCase("07")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("7"));
                } else if (estadoDia.equalsIgnoreCase("08")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("8"));
                } else if (estadoDia.equalsIgnoreCase("09")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("9"));
                } else if (estadoDia.equalsIgnoreCase("10")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("10"));
                } else if (estadoDia.equalsIgnoreCase("11")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("11"));
                } else if (estadoDia.equalsIgnoreCase("12")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("12"));
                } else if (estadoDia.equalsIgnoreCase("13")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("13"));
                } else if (estadoDia.equalsIgnoreCase("14")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("14"));
                } else if (estadoDia.equalsIgnoreCase("15")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("15"));
                } else if (estadoDia.equalsIgnoreCase("16")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("16"));
                } else if (estadoDia.equalsIgnoreCase("17")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("17"));
                } else if (estadoDia.equalsIgnoreCase("18")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("18"));
                } else if (estadoDia.equalsIgnoreCase("19")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("19"));
                } else if (estadoDia.equalsIgnoreCase("20")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("20"));
                } else if (estadoDia.equalsIgnoreCase("21")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("21"));
                } else if (estadoDia.equalsIgnoreCase("22")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("22"));
                } else if (estadoDia.equalsIgnoreCase("23")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("23"));
                } else if (estadoDia.equalsIgnoreCase("24")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("24"));
                } else if (estadoDia.equalsIgnoreCase("25")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("25"));
                } else if (estadoDia.equalsIgnoreCase("26")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("26"));
                } else if (estadoDia.equalsIgnoreCase("27")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("27"));
                } else if (estadoDia.equalsIgnoreCase("28")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("28"));
                } else if (estadoDia.equalsIgnoreCase("29")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("29"));
                } else if (estadoDia.equalsIgnoreCase("30")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("30"));
                } else if (estadoDia.equalsIgnoreCase("31")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("31"));
                } else if (estadoDia.equalsIgnoreCase("TODOS LOS DIAS")) {
                    duplicarRegistroMensajesUsuarios.setDia(new Short("0"));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setDia(null);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarDia");
        }

    }

    //BORRAR VIGENCIA FORMAL
    public void borrarProverbio() {

        if (proverbioSeleccionado != null) {
            if (CualTabla == 0) {
                if (!listaProverbiosModificar.isEmpty() && listaProverbiosModificar.contains(proverbioSeleccionado)) {
                    int modIndex = listaProverbiosModificar.indexOf(proverbioSeleccionado);
                    listaProverbiosModificar.remove(modIndex);
                    listaProverbiosBorrar.add(proverbioSeleccionado);
                } else if (!listaProverbiosCrear.isEmpty() && listaProverbiosCrear.contains(proverbioSeleccionado)) {
                    int crearIndex = listaProverbiosCrear.indexOf(proverbioSeleccionado);
                    listaProverbiosCrear.remove(crearIndex);
                } else {
                    listaProverbiosBorrar.add(proverbioSeleccionado);
                }
                listaProverbios.remove(proverbioSeleccionado);
                if (tipoLista == 1) {
                    filtradosListaProverbios.remove(proverbioSeleccionado);
                }
                modificarInfoRegistroProverbios(listaProverbios.size());
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosProverbios");
                proverbioSeleccionado = null;
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");

                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            } else if (CualTabla == 1) {
                if (!listaMensajesUsuariosModificar.isEmpty() && listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                    int modIndex = listaMensajesUsuariosModificar.indexOf(mensajeUsuarioSeleccionado);
                    listaMensajesUsuariosModificar.remove(modIndex);
                    listaMensajesUsuariosBorrar.add(mensajeUsuarioSeleccionado);
                } else if (!listaMensajesUsuariosCrear.isEmpty() && listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
                    int crearIndex = listaMensajesUsuariosCrear.indexOf(mensajeUsuarioSeleccionado);
                    listaMensajesUsuariosCrear.remove(crearIndex);
                } else {
                    listaMensajesUsuariosBorrar.add(mensajeUsuarioSeleccionado);
                }
                listaMensajesUsuario.remove(mensajeUsuarioSeleccionado);
                if (tipoListaNF == 1) {
                    filtradosListaMensajesUsuario.remove(mensajeUsuarioSeleccionado);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistroMsgUsuarios(listaMensajesUsuario.size());
                PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                mensajeUsuarioSeleccionado = null;
                proverbioSeleccionado = null;
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }
    }

    public void verificarRastro() {
        if (CualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (proverbioSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(proverbioSeleccionado.getSecuencia(), "RECORDATORIOS");
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
                if (administrarRastros.verificarHistoricosTabla("RECORDATORIOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (mensajeUsuarioSeleccionado != null) {
                int resultadoNF = administrarRastros.obtenerTabla(mensajeUsuarioSeleccionado.getSecuencia(), "RECORDATORIOS");
                if (resultadoNF == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDBNF').show()");
                } else if (resultadoNF == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroNF').show()");
                } else if (resultadoNF == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastroNF').show()");
                } else if (resultadoNF == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastroNF').show()");
                } else if (resultadoNF == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastroNF').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("RECORDATORIOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistoricoNF').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistoricoNF').show()");
                }
            }
        }

    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoMensajeUsuario() {
        nuevoRegistroMensajesUsuarios = new Recordatorios();
        nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");

    }

    //LIMPIAR DUPLICAR
    public void limpiarDuplicarProverbio() {
        duplicarProverbio = new Recordatorios();
    }

    public void limpiarDuplicarRegistroMensajeUsuario() {
        duplicarRegistroMensajesUsuarios = new Recordatorios();
    }

    public void confirmarDuplicar() {
        listaProverbiosCrear.add(duplicarProverbio);
        listaProverbios.add(duplicarProverbio);
        proverbioSeleccionado = duplicarProverbio;
        modificarInfoRegistroProverbios(listaProverbios.size());
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = false;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
        }
        PrimefacesContextUI.actualizar("form:datosProverbios");
        duplicarProverbio = new Recordatorios();
        PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroProverbio");
        PrimefacesContextUI.ejecutar("PF('DuplicarRegistroProverbio').hide()");
    }

    public void confirmarDuplicarNF() {
        listaMensajesUsuariosCrear.add(duplicarRegistroMensajesUsuarios);
        listaMensajesUsuario.add(duplicarRegistroMensajesUsuarios);
        modificarInfoRegistroMsgUsuarios(listaMensajesUsuario.size());
        mensajeUsuarioSeleccionado = duplicarRegistroMensajesUsuarios;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaNF = "115";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("display: none; visibility: hidden;");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("display: none; visibility: hidden;");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("display: none; visibility: hidden;");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
        PrimefacesContextUI.actualizar("form:DuplicarRegistroMensajeUsuario");
        duplicarRegistroMensajesUsuarios = new Recordatorios();
        PrimefacesContextUI.actualizar("formularioDialogos:duplicarRegistroMensajeUsuario");
        PrimefacesContextUI.ejecutar("PF('DuplicarRegistroMensajeUsuario').hide()");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
        }
        if (banderaNF == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaNF = "115";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("display: none; visibility: hidden;");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("display: none; visibility: hidden;");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("display: none; visibility: hidden;");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
        listaProverbiosBorrar.clear();
        listaProverbiosCrear.clear();
        listaProverbiosModificar.clear();
        proverbioSeleccionado = null;
        proverbioSeleccionado = null;

        listaProverbios = null;

        listaMensajesUsuariosBorrar.clear();
        listaMensajesUsuariosCrear.clear();
        listaMensajesUsuariosModificar.clear();
        mensajeUsuarioSeleccionado = null;

        listaMensajesUsuario = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosProverbios");
        PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
        }
        if (banderaNF == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaNF = "115";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("display: none; visibility: hidden;");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("display: none; visibility: hidden;");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("display: none; visibility: hidden;");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
        listaProverbiosBorrar.clear();
        listaProverbiosCrear.clear();
        listaProverbiosModificar.clear();
        proverbioSeleccionado = null;
        proverbioSeleccionado = null;
        listaProverbios = null;
        listaMensajesUsuariosBorrar.clear();
        listaMensajesUsuariosCrear.clear();
        listaMensajesUsuariosModificar.clear();
        mensajeUsuarioSeleccionado = null;
        listaMensajesUsuario = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;
    }

    public List<String> complete(String query) {
        List<String> results = new ArrayList<String>();

        int aux = Integer.parseInt(query);

        for (int i = 0; i < 10; i++) {
            results.add((Integer.toString(aux + i)));
        }

        return results;
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistroProverbios(filtradosListaProverbios.size());
    }

    //EVENTO FILTRARNF
    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
        modificarInfoRegistroMsgUsuarios(filtradosListaMensajesUsuario.size());
    }

    public void eventoFiltrarMsgUsuarios() {
        modificarInfoRegistroMsgUsuarios(filtradosListaMensajesUsuario.size());
    }

    public void modificarInfoRegistroProverbios(int valor) {
        infoRegistroProverbios = String.valueOf(valor);
        PrimefacesContextUI.actualizar("form:infoRegistroProverbio");
    }

    public void modificarInfoRegistroMsgUsuarios(int valor) {
        infoRegistroMsgUsuario = String.valueOf(valor);
        PrimefacesContextUI.actualizar("form:infoRegistroMsgUsuarios");
    }

    //Getter & Setters
    public Recordatorios getEditarProverbios() {
        return editarProverbios;
    }

    public void setEditarProverbios(Recordatorios editarProverbios) {
        this.editarProverbios = editarProverbios;
    }

    public List<Recordatorios> getListaProverbios() {
        if (listaProverbios == null) {
            listaProverbios = administrarRecordatorios.recordatorios();
            if(listaProverbios != null){
            if (!listaProverbios.isEmpty()) {
                proverbioSeleccionado = listaProverbios.get(0);
            }
            }
        }
        return listaProverbios;
    }

    public void setListaProverbios(List<Recordatorios> listaProverbios) {
        this.listaProverbios = listaProverbios;
    }

    public List<Recordatorios> getFiltradosListaProverbios() {
        return filtradosListaProverbios;
    }

    public void setFiltradosListaProverbios(List<Recordatorios> filtradosListaProverbios) {
        this.filtradosListaProverbios = filtradosListaProverbios;
    }

    public List<Recordatorios> getListaMensajesUsuario() {
        if (listaMensajesUsuario == null) {
            listaMensajesUsuario = administrarRecordatorios.mensajesRecordatorios();
           if(listaMensajesUsuario!= null){
            if (!listaMensajesUsuario.isEmpty()) {
                mensajeUsuarioSeleccionado = listaMensajesUsuario.get(0);
            }
           }
        }
        return listaMensajesUsuario;
    }

    public void setListaMensajesUsuario(List<Recordatorios> listaMensajesUsuario) {
        this.listaMensajesUsuario = listaMensajesUsuario;
    }

    public List<Recordatorios> getFiltradosListaMensajesUsuario() {
        return filtradosListaMensajesUsuario;
    }

    public void setFiltradosListaMensajesUsuario(List<Recordatorios> filtradosListaMensajesUsuario) {
        this.filtradosListaMensajesUsuario = filtradosListaMensajesUsuario;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaNF() {
        return altoTablaNF;
    }

    public void setAltoTablaNF(String altoTablaNF) {
        this.altoTablaNF = altoTablaNF;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Recordatorios getNuevoProverbio() {
        return nuevoProverbio;
    }

    public void setNuevoProverbio(Recordatorios nuevoProverbio) {
        this.nuevoProverbio = nuevoProverbio;
    }

    public Recordatorios getDuplicarProverbio() {
        return duplicarProverbio;
    }

    public void setDuplicarProverbio(Recordatorios duplicarProverbio) {
        this.duplicarProverbio = duplicarProverbio;
    }

    public Recordatorios getNuevoRegistroMensajesUsuarios() {
        return nuevoRegistroMensajesUsuarios;
    }

    public void setNuevoRegistroMensajesUsuarios(Recordatorios nuevoRegistroMensajesUsuarios) {
        this.nuevoRegistroMensajesUsuarios = nuevoRegistroMensajesUsuarios;
    }

    public Recordatorios getDuplicarRegistroMensajesUsuarios() {
        return duplicarRegistroMensajesUsuarios;
    }

    public void setDuplicarRegistroMensajesUsuarios(Recordatorios duplicarRegistroMensajesUsuarios) {
        this.duplicarRegistroMensajesUsuarios = duplicarRegistroMensajesUsuarios;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public Recordatorios getProverbioSeleccionado() {
        return proverbioSeleccionado;
    }

    public void setProverbioSeleccionado(Recordatorios proverbioSeleccionado) {
        this.proverbioSeleccionado = proverbioSeleccionado;
    }

    public Recordatorios getMensajeUsuarioSeleccionado() {
        return mensajeUsuarioSeleccionado;
    }

    public void setMensajeUsuarioSeleccionado(Recordatorios mensajeUsuarioSeleccionado) {
        this.mensajeUsuarioSeleccionado = mensajeUsuarioSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Short> getAnios() {
        return anios;
    }

    public void setAnios(List<Short> anios) {
        this.anios = anios;
    }

    public Short getAnioactual() {
        return anioactual;
    }

    public void setAnioactual(Short anioactual) {
        this.anioactual = anioactual;
    }

    public String getInfoRegistroProverbios() {
        return infoRegistroProverbios;
    }

    public void setInfoRegistroProverbios(String infoRegistroProverbios) {
        this.infoRegistroProverbios = infoRegistroProverbios;
    }

    public String getInfoRegistroMsgUsuario() {
        return infoRegistroMsgUsuario;
    }

    public void setInfoRegistroMsgUsuario(String infoRegistroMsgUsuario) {
        this.infoRegistroMsgUsuario = infoRegistroMsgUsuario;
    }

}
