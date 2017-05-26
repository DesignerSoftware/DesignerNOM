package Controlador;

import Entidades.Recordatorios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRecordatoriosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Date;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import utilidades.AgnosMesesDiasNumeros;

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

    private List<Recordatorios> listaProverbios;
    private List<Recordatorios> filtradosListaProverbios;
    private Recordatorios proverbioSeleccionado;
    private List<Recordatorios> listaMensajesUsuario;
    private List<Recordatorios> filtradosListaMensajesUsuario;
    private Recordatorios mensajeUsuarioSeleccionado;
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    private boolean guardado, guardarOk;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista, tipoListaNF;
    private String altoTabla, altoTablaNF;
    private boolean cambiosPagina;
    private List<Recordatorios> listaProverbiosCrear;
    public Recordatorios nuevoProverbio;
    public Recordatorios duplicarProverbio;
    private int k;
    private BigInteger l;
    private int m;
    private BigInteger n;
    private List<Recordatorios> listaMensajesUsuariosCrear;
    public Recordatorios nuevoRegistroMensajesUsuarios;
    public Recordatorios duplicarRegistroMensajesUsuarios;
    private int banderaNF;
    private List<Recordatorios> listaProverbiosModificar;
    private List<Recordatorios> listaProverbiosBorrar;
    private int CualTabla;
    private Recordatorios editarProverbios;
    private Column pMensaje;
    private Column mAno, mMes, mDia, mMensaje;
    private String tablaImprimir, nombreArchivo;
    private String cualInsertar;
    private String cualNuevo;
    private List<Recordatorios> listaMensajesUsuariosModificar;
    private List<Recordatorios> listaMensajesUsuariosBorrar;
    private String mensaje;
    private int ano, dia, mes;
    private List<Short> anios;
    private Short anioactual;
    private String infoRegistroProverbios, infoRegistroMsgUsuario;
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String paginaAnterior = "nominaf";

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
        nuevoProverbio = new Recordatorios();
        duplicarProverbio = new Recordatorios();
        nuevoProverbio.setTipo("PROVERBIO");
        nuevoRegistroMensajesUsuarios = new Recordatorios();
        nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");
        duplicarRegistroMensajesUsuarios = new Recordatorios();
        aceptar = true;
        listaMensajesUsuariosBorrar = new ArrayList<>();
        listaMensajesUsuariosCrear = new ArrayList<>();
        listaMensajesUsuariosModificar = new ArrayList<>();
        listaProverbiosBorrar = new ArrayList<>();
        listaProverbiosCrear = new ArrayList<>();
        listaProverbiosModificar = new ArrayList<>();
        proverbioSeleccionado = null;
        m = 0;
        anioactual = (short) Calendar.getInstance().get(Calendar.YEAR);
        anios = new ArrayList<>();
        for (int i = (anioactual - 10); i < (anioactual + 10); i++) {
            anios.add(new Short(String.valueOf(i + 1900)));
        }
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

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

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaProverbios = null;
        listaMensajesUsuario = null;
        getListaMensajesUsuario();
        getListaProverbios();

    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "proverbio";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaProverbios = null;
        listaMensajesUsuario = null;
        getListaMensajesUsuario();
        getListaProverbios();
        if (listaProverbios != null) {
            if (!listaProverbios.isEmpty()) {
                proverbioSeleccionado = listaProverbios.get(0);
            }
        }
    }

    //GUARDAR
    public void guardarCambiosProverbios() {
        if (guardado == false) {
            if (!listaProverbiosBorrar.isEmpty()) {
                for (int i = 0; i < listaProverbiosBorrar.size(); i++) {
                    administrarRecordatorios.borrar(listaProverbiosBorrar.get(i));
                }
                listaProverbiosBorrar.clear();
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

            if (!listaMensajesUsuariosModificar.isEmpty()) {
                administrarRecordatorios.modificarMU(listaMensajesUsuariosModificar);
                listaMensajesUsuariosModificar.clear();
            }
        }
        listaMensajesUsuario = null;
        listaProverbios = null;
        getListaMensajesUsuario();
        getListaProverbios();
        RequestContext.getCurrentInstance().update("form:datosProverbios");
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
        mensajeUsuarioSeleccionado = null;
        proverbioSeleccionado = null;
    }

    //Ubicacion Celda.
    public void cambiarIndice(Recordatorios proverbio, int celda) {
        if (permitirIndex) {
            proverbioSeleccionado = proverbio;
            cualCelda = celda;
            CualTabla = 0;
            mensajeUsuarioSeleccionado = null;
            proverbioSeleccionado.getSecuencia();
        }
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(Recordatorios msgUsuario, int celdaNF) {

        if (permitirIndex) {
            mensajeUsuarioSeleccionado = msgUsuario;
            cualCelda = celdaNF;
            CualTabla = 1;
            tablaImprimir = ":formExportar:datosMensajesUsuariosExportar";
            cualNuevo = ":formularioDialogos:nuevoMensajeUsuarios";
            cualInsertar = "formularioDialogos:NuevoRegistroMensajeUsuario";
            nombreArchivo = "MensajeUsuarioXML";
            RequestContext.getCurrentInstance().update("form:exportarXML");
            mensajeUsuarioSeleccionado.getSecuencia();
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
        if (bandera == 0 && banderaNF == 0) {
            altoTabla = "95";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosProverbios");
            bandera = 1;
            altoTablaNF = "91";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("width: 85% !important");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("width: 85% !important");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("width: 85% !important");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
            banderaNF = 1;
        } else if (banderaNF == 1 && bandera == 1) {
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
            altoTablaNF = "115";
            mAno = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mAno");
            mAno.setFilterStyle("display: none; visibility: hidden;");
            mMes = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMes");
            mMes.setFilterStyle("display: none; visibility: hidden;");
            mDia = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mDia");
            mDia.setFilterStyle("display: none; visibility: hidden;");
            mMensaje = (Column) c.getViewRoot().findComponent("form:datosMensajesUsuarios:mMensaje");
            mMensaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
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
        if ((listaProverbios.isEmpty() || listaMensajesUsuario.isEmpty())) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        } else if (CualTabla == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroProverbio");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProverbio').show()");
        } else if (CualTabla == 1) {
            nuevoRegistroMensajesUsuarios.setAno(anioactual);
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroMensajeUsuario");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroMensajeUsuario').show()");
        }
    }

    //AUTOCOMPLETAR
    public void modificarProverbio(Recordatorios proverbio) {
        proverbioSeleccionado = proverbio;
        if (!listaProverbiosCrear.contains(proverbioSeleccionado)) {
            if (listaProverbiosModificar.isEmpty()) {
                listaProverbiosModificar.add(proverbioSeleccionado);
            } else if (!listaProverbiosModificar.contains(proverbioSeleccionado)) {
                listaProverbiosModificar.add(proverbioSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosProverbios");
    }

    //AUTOCOMPLETAR
    public void modificarMensajeUsuario(Recordatorios msgUsuario) {
        mensajeUsuarioSeleccionado = msgUsuario;
        if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {

            if (listaMensajesUsuariosModificar.isEmpty()) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
    }

    public void editarCelda() {
        if (proverbioSeleccionado != null && CualTabla == 0) {
            editarProverbios = proverbioSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMensajes");
                RequestContext.getCurrentInstance().execute("PF('editarMensajes').show()");
                cualCelda = -1;
            }
        } else if (mensajeUsuarioSeleccionado != null && CualTabla == 1) {
            editarProverbios = mensajeUsuarioSeleccionado;
            switch (cualCelda) {
                case 0:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAños");
                    RequestContext.getCurrentInstance().execute("PF('editarAños').show()");
                    cualCelda = -1;
                    break;
                case 1:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMeses");
                    RequestContext.getCurrentInstance().execute("PF('editarMeses').show()");
                    cualCelda = -1;
                    break;
                case 2:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDias");
                    RequestContext.getCurrentInstance().execute("PF('editarDias').show()");
                    cualCelda = -1;
                    break;
                case 3:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMensajes2");
                    RequestContext.getCurrentInstance().execute("PF('editarMensajes2').show()");
                    cualCelda = -1;
                    break;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void dialogoProverbios() {
        RequestContext.getCurrentInstance().update("form:datosProverbios");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProverbio').show()");
    }

    public void dialogoMensajesUsuarios() {
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroMensajeUsuario').show()");
    }

    //CREAR NUEVO PROVERBIO
    public void agregarNuevoProverbio() {
        int pasa = 0;
        FacesContext c = FacesContext.getCurrentInstance();
        if (pasa == 0) {
            if (bandera == 1 && CualTabla == 0) {
                altoTabla = "115";
                pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
                pMensaje.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosProverbios");
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
            contarRegistrosProverbios();
            nuevoProverbio = new Recordatorios();
            RequestContext.getCurrentInstance().update("form:datosProverbios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProverbio').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoProverbio");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoProverbio').show()");
        }
    }

    public void agregarNuevoMensajeUsuario() {
        int pasa = 0;
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
                RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
                banderaNF = 0;
                filtradosListaMensajesUsuario = null;
                tipoListaNF = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoRegistroMensajesUsuarios.setSecuencia(l);
            nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");
            listaMensajesUsuariosCrear.add(nuevoRegistroMensajesUsuarios);
            listaMensajesUsuario.add(nuevoRegistroMensajesUsuarios);
            contarRegistrosMensajes();
            mensajeUsuarioSeleccionado = nuevoRegistroMensajesUsuarios;
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            nuevoRegistroMensajesUsuarios = new Recordatorios();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroMensajeUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoMensajeUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoMensajeUsuario').show()");
        }
    }

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
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroProverbio");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProverbio').show()");
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
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroMensajeUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroMensajeUsuario').show()");

        }
    }

    public void seleccionarAno(String estadoAno, Recordatorios msgUsuario) {
        mensajeUsuarioSeleccionado = msgUsuario;
        if (estadoAno != null) {
            if (estadoAno.equalsIgnoreCase("2005")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2005"));
            } else if (estadoAno.equalsIgnoreCase("2006")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2006"));
            } else if (estadoAno.equalsIgnoreCase("2007")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2007"));
            } else if (estadoAno.equalsIgnoreCase("2008")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2008"));
            } else if (estadoAno.equalsIgnoreCase("2009")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2009"));
            } else if (estadoAno.equalsIgnoreCase("2010")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2010"));
            } else if (estadoAno.equalsIgnoreCase("2011")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2011"));
            } else if (estadoAno.equalsIgnoreCase("2012")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2012"));
            } else if (estadoAno.equalsIgnoreCase("2013")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2013"));
            } else if (estadoAno.equalsIgnoreCase("2014")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2014"));
            } else if (estadoAno.equalsIgnoreCase("2015")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2015"));
            } else if (estadoAno.equalsIgnoreCase("2016")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2016"));
            } else if (estadoAno.equalsIgnoreCase("2017")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2017"));
            } else if (estadoAno.equalsIgnoreCase("2018")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2018"));
            } else if (estadoAno.equalsIgnoreCase("2019")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2019"));
            } else if (estadoAno.equalsIgnoreCase("2020")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2020"));
            } else if (estadoAno.equalsIgnoreCase("2021")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2021"));
            } else if (estadoAno.equalsIgnoreCase("2022")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2022"));
            } else if (estadoAno.equalsIgnoreCase("2023")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2023"));
            } else if (estadoAno.equalsIgnoreCase("2024")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2024"));
            } else if (estadoAno.equalsIgnoreCase("2025")) {
                mensajeUsuarioSeleccionado.setAno(new Short("2025"));
            } else if (estadoAno.equalsIgnoreCase("TODOS LOS AÑOS")) {
                mensajeUsuarioSeleccionado.setAno(new Short("0"));
            }
        }
        if (!listaMensajesUsuariosCrear.contains(mensajeUsuarioSeleccionado)) {
            if (listaMensajesUsuariosModificar.isEmpty()) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            } else if (!listaMensajesUsuariosModificar.contains(mensajeUsuarioSeleccionado)) {
                listaMensajesUsuariosModificar.add(mensajeUsuarioSeleccionado);
            }
        }
        guardado = false;
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");

    }

    public void seleccionarMes(String estadoMes, Recordatorios msgUsuario) {
        HashMap meses = AgnosMesesDiasNumeros.getMeses();
        mensajeUsuarioSeleccionado = msgUsuario;
        if (estadoMes != null) {
            if (meses.containsKey(estadoMes.toUpperCase())) {
                mensajeUsuarioSeleccionado.setMes((short) meses.get(estadoMes.toUpperCase()));
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
        guardado = false;
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");

    }

    public void seleccionarDia(String estadoDia, Recordatorios msgUsuario) {
        HashMap diasLetras = AgnosMesesDiasNumeros.getDias();
        mensajeUsuarioSeleccionado = msgUsuario;
        if (estadoDia != null) {
            if (diasLetras.containsKey(estadoDia.toUpperCase())) {
                mensajeUsuarioSeleccionado.setDia((short) diasLetras.get(estadoDia.toUpperCase()));
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
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");

    }

    public void seleccionarTipoNuevoAno(String estadoAno, int tipoNuevo) {
        HashMap agnosLetras = AgnosMesesDiasNumeros.getAgnos(Calendar.getInstance().get(Calendar.YEAR));
        if (tipoNuevo == 1) {
            if (estadoAno != null) {
                if (agnosLetras.containsKey(estadoAno.toUpperCase())) {
                    nuevoRegistroMensajesUsuarios.setAno((short) agnosLetras.get(estadoAno.toUpperCase()));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setAno(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAno");
        } else {
            if (estadoAno != null) {
                if (agnosLetras.containsKey(estadoAno.toUpperCase())) {
                    duplicarRegistroMensajesUsuarios.setAno((short) agnosLetras.get(estadoAno.toUpperCase()));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setAno(null);
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAno");
        }

    }

    public void seleccionarTipoNuevoMes(String estadoMes, int tipoNuevo) {
        HashMap meses = AgnosMesesDiasNumeros.getMeses();
        if (tipoNuevo == 1) {
            if (estadoMes != null) {
                if (meses.containsKey(estadoMes.toUpperCase())) {
                    nuevoRegistroMensajesUsuarios.setMes((short) meses.get(estadoMes.toUpperCase()));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setMes(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMes");
        } else {
            if (estadoMes != null) {
                if (meses.containsKey(estadoMes.toUpperCase())) {
                    nuevoRegistroMensajesUsuarios.setMes((short) meses.get(estadoMes.toUpperCase()));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setMes(null);
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMes");
        }

    }

    public void seleccionarTipoNuevoDia(String estadoDia, int tipoNuevo) {
        HashMap dias = AgnosMesesDiasNumeros.getDias();
        if (tipoNuevo == 1) {
            if (estadoDia != null) {
                if (dias.containsKey(estadoDia.toUpperCase())) {
                    nuevoRegistroMensajesUsuarios.setDia((short) dias.get(estadoDia.toUpperCase()));
                }
            } else {
                nuevoRegistroMensajesUsuarios.setDia(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDia");
        } else {
            if (estadoDia != null) {
                if (dias.containsKey(estadoDia.toUpperCase())) {
                    duplicarRegistroMensajesUsuarios.setDia((short) dias.get(estadoDia.toUpperCase()));
                }
            } else {
                duplicarRegistroMensajesUsuarios.setDia(null);
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDia");
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
                contarRegistrosProverbios();
                RequestContext.getCurrentInstance().update("form:datosProverbios");
                proverbioSeleccionado = null;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
                contarRegistrosMensajes();
                RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
                cambiosPagina = false;
                mensajeUsuarioSeleccionado = null;
                proverbioSeleccionado = null;
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void verificarRastro() {
        if (CualTabla == 0) {
            if (proverbioSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(proverbioSeleccionado.getSecuencia(), "RECORDATORIOS");
                switch (resultado) {
                    case 1:
                        RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                        break;
                    case 2:
                        RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                        break;
                    case 3:
                        RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                        break;
                    case 4:
                        RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                        break;
                    case 5:
                        RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                        break;
                    default:
                        break;
                }
            } else if (administrarRastros.verificarHistoricosTabla("RECORDATORIOS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        } else if (mensajeUsuarioSeleccionado != null) {
            int resultadoNF = administrarRastros.obtenerTabla(mensajeUsuarioSeleccionado.getSecuencia(), "RECORDATORIOS");
            switch (resultadoNF) {
                case 1:
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
                    break;
                case 2:
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
                    break;
                case 3:
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
                    break;
                case 4:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
                    break;
                case 5:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
                    break;
                default:
                    break;
            }
        } else if (administrarRastros.verificarHistoricosTabla("RECORDATORIOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
        }

    }

    public void limpiarNuevoMensajeUsuario() {
        nuevoRegistroMensajesUsuarios = new Recordatorios();
        nuevoRegistroMensajesUsuarios.setTipo("RECORDATORIO");
    }

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
        contarRegistrosProverbios();
        cambiosPagina = false;
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProverbios");
            bandera = 0;
            filtradosListaProverbios = null;
            tipoLista = 0;
        }
        RequestContext.getCurrentInstance().update("form:datosProverbios");
        duplicarProverbio = new Recordatorios();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroProverbio");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProverbio').hide()");
    }

    public void confirmarDuplicarNF() {
        listaMensajesUsuariosCrear.add(duplicarRegistroMensajesUsuarios);
        listaMensajesUsuario.add(duplicarRegistroMensajesUsuarios);
        contarRegistrosMensajes();
        mensajeUsuarioSeleccionado = duplicarRegistroMensajesUsuarios;
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
        RequestContext.getCurrentInstance().update("form:DuplicarRegistroMensajeUsuario");
        duplicarRegistroMensajesUsuarios = new Recordatorios();
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroMensajeUsuario");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroMensajeUsuario').hide()");

    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProverbios");
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
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
            banderaNF = 0;
            filtradosListaMensajesUsuario = null;
            tipoListaNF = 0;
        }
        listaProverbiosBorrar.clear();
        listaProverbiosCrear.clear();
        listaProverbiosModificar.clear();
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
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosProverbios");
        RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "115";
            pMensaje = (Column) c.getViewRoot().findComponent("form:datosProverbios:pMensaje");
            pMensaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProverbios");
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
            RequestContext.getCurrentInstance().update("form:datosMensajesUsuarios");
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
        navegar("atras");
    }

    public List<String> complete(String query) {
        List<String> results = new ArrayList<>();
        int aux = Integer.parseInt(query);
        for (int i = 0; i < 10; i++) {
            results.add((Integer.toString(aux + i)));
        }
        return results;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosProverbios();
    }

    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
        contarRegistrosMensajes();
    }

    public void contarRegistrosProverbios() {
        RequestContext.getCurrentInstance().update("form:infoRegistroProverbio");
    }

    public void contarRegistrosMensajes() {
        RequestContext.getCurrentInstance().update("form:infoRegistroMsgUsuarios");
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosProverbios");
        infoRegistroProverbios = String.valueOf(tabla.getRowCount());
        return infoRegistroProverbios;
    }

    public void setInfoRegistroProverbios(String infoRegistroProverbios) {
        this.infoRegistroProverbios = infoRegistroProverbios;
    }

    public String getInfoRegistroMsgUsuario() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMensajesUsuarios");
        infoRegistroMsgUsuario = String.valueOf(tabla.getRowCount());
        return infoRegistroMsgUsuario;
    }

    public void setInfoRegistroMsgUsuario(String infoRegistroMsgUsuario) {
        this.infoRegistroMsgUsuario = infoRegistroMsgUsuario;
    }

}
