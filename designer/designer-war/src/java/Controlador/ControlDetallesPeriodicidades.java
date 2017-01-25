/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.DetallesPeriodicidades;
import Entidades.Periodicidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetallesPeriodicidadesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlDetallesPeriodicidades")
@SessionScoped
public class ControlDetallesPeriodicidades implements Serializable {

    @EJB
    AdministrarDetallesPeriodicidadesInterface administrarDetallesPeriodicidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<DetallesPeriodicidades> listaDetallesP;
    private List<DetallesPeriodicidades> listaDetallesPFiltrar;
    private List<DetallesPeriodicidades> listaDetallesPCrear;
    private List<DetallesPeriodicidades> listaDetallesPModificar;
    private List<DetallesPeriodicidades> listaDetallesPBorrar;
    private DetallesPeriodicidades detallePSeleccionado;
    private DetallesPeriodicidades duplicarDetalleP;
    private DetallesPeriodicidades nuevoDetalleP;
    private DetallesPeriodicidades editarDetalleP;
    private Periodicidades periodicidadActual;
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private Column anio, mes, dia, tipodia;
    private int cualCelda, tipoLista;
    private boolean guardado;
    private BigInteger l;
    private int k;
    private String altoTabla;
    private String infoRegistro, paginaanterior;
    private boolean activarLOV;

    public ControlDetallesPeriodicidades() {
        aceptar = true;
        tipoLista = 0;
        nuevoDetalleP = new DetallesPeriodicidades();
        nuevoDetalleP.setAno(Short.valueOf("0"));
        nuevoDetalleP.setMes(Short.valueOf("0"));
        nuevoDetalleP.setDia(Short.valueOf("0"));
        nuevoDetalleP.setTipodia("M");
        duplicarDetalleP = new DetallesPeriodicidades();
        duplicarDetalleP.setAno(Short.valueOf("0"));
        duplicarDetalleP.setMes(Short.valueOf("0"));
        duplicarDetalleP.setDia(Short.valueOf("0"));
        duplicarDetalleP.setTipodia("M");
        listaDetallesPBorrar = new ArrayList<DetallesPeriodicidades>();
        listaDetallesPCrear = new ArrayList<DetallesPeriodicidades>();
        listaDetallesPModificar = new ArrayList<DetallesPeriodicidades>();
        detallePSeleccionado = null;
        listaDetallesP = null;
        k = 0;
        altoTabla = "270";
        activarLOV = true;
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }
      
   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "cargo"XXX;
        //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
 //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
   //      } else if (pag.equals("rastrotablaH")) {
     //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
       //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
   //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRastros.obtenerConexion(ses.getId());
            administrarDetallesPeriodicidades.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina, BigInteger secuenciaPeriodicidad) {
        paginaanterior = pagina;
        periodicidadActual = administrarDetallesPeriodicidades.consultarPeriodicidadPorSecuencia(secuenciaPeriodicidad);
        listaDetallesP = null;
        getListaDetallesP();
        if (listaDetallesP != null) {
            if (!listaDetallesP.isEmpty()) {
                detallePSeleccionado = listaDetallesP.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(DetallesPeriodicidades detallesp, int celda) {
        detallePSeleccionado = detallesp;
        cualCelda = celda;
        detallePSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            detallePSeleccionado.getAno();
        } else if (cualCelda == 1) {
            detallePSeleccionado.getMes();
        } else if (cualCelda == 2) {
            detallePSeleccionado.getDia();
        }
    }

    public void agregarNuevoDetalleP() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 0;
            listaDetallesPFiltrar = null;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        nuevoDetalleP.setSecuencia(l);
        nuevoDetalleP.setPeriodicidad(periodicidadActual);
        listaDetallesPCrear.add(nuevoDetalleP);
        listaDetallesP.add(nuevoDetalleP);
        detallePSeleccionado = nuevoDetalleP;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosDetallesP");
        limpiarNuevoDetalleP();
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetallesP').hide()");
    }

    public void modificarDetallesP(DetallesPeriodicidades detallesp) {
        detallePSeleccionado = detallesp;
        if (!listaDetallesPCrear.contains(detallePSeleccionado)) {
            if (listaDetallesPModificar.isEmpty()) {
                listaDetallesPModificar.add(detallePSeleccionado);
            } else if (!listaDetallesPModificar.contains(detallePSeleccionado)) {
                listaDetallesPModificar.add(detallePSeleccionado);
            }
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDetallesP");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("width: 85% !important");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("width: 85% !important");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("width: 85% !important");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("width: 85% !important");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 0;
            listaDetallesPFiltrar = null;
            tipoLista = 0;
        }
    }

    public void limpiarNuevoDetalleP() {
        nuevoDetalleP = new DetallesPeriodicidades();
        nuevoDetalleP.setAno(Short.valueOf("0"));
        nuevoDetalleP.setMes(Short.valueOf("0"));
        nuevoDetalleP.setDia(Short.valueOf("0"));
        nuevoDetalleP.setTipodia("M");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void editarCelda() {
        if (detallePSeleccionado != null) {
            editarDetalleP = detallePSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAnio");
                RequestContext.getCurrentInstance().execute("PF('editarAnio').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMes");
                RequestContext.getCurrentInstance().execute("PF('editarMes').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDia");
                RequestContext.getCurrentInstance().execute("PF('editarDia').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarD() {
        if (detallePSeleccionado != null) {
            duplicarDetalleP = new DetallesPeriodicidades();
            k++;
            l = BigInteger.valueOf(k);
            duplicarDetalleP.setSecuencia(l);
            duplicarDetalleP.setPeriodicidad(detallePSeleccionado.getPeriodicidad());
            duplicarDetalleP.setAno(detallePSeleccionado.getAno());
            duplicarDetalleP.setDia(detallePSeleccionado.getDia());
            duplicarDetalleP.setMes(detallePSeleccionado.getMes());
            duplicarDetalleP.setTipodia(detallePSeleccionado.getTipodia());
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleP");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetallesP').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 0;
            listaDetallesPFiltrar = null;
            tipoLista = 0;
        }
        listaDetallesP.add(duplicarDetalleP);
        listaDetallesPCrear.add(duplicarDetalleP);
        detallePSeleccionado = duplicarDetalleP;
        contarRegistros();
        duplicarDetalleP = new DetallesPeriodicidades();
        RequestContext.getCurrentInstance().update("form:datosDetallesP");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetallesP').hide()");
    }

    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listaDetallesPBorrar.isEmpty()) {
                    administrarDetallesPeriodicidades.borrarDetallePeriodicidad(listaDetallesPBorrar);
                    listaDetallesPBorrar.clear();
                }
                if (!listaDetallesPCrear.isEmpty()) {
                    administrarDetallesPeriodicidades.crearDetallePeriodicidad(listaDetallesPCrear);
                    listaDetallesPCrear.clear();
                }
                if (!listaDetallesPModificar.isEmpty()) {
                    administrarDetallesPeriodicidades.modificarDetallePeriodicidad(listaDetallesPModificar);
                    listaDetallesPModificar.clear();
                }
                System.out.println("Se guardaron los datos con exito");
                listaDetallesP = null;
                getListaDetallesP();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosDetallesP");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                k = 0;
            }
            detallePSeleccionado = null;
        } catch (Exception e) {
            System.out.println("entró al catch... error en el guardado de detalles periodicidades " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Error en el guardado, por favor intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 0;
            listaDetallesPFiltrar = null;
            tipoLista = 0;
        }
        listaDetallesPBorrar.clear();
        listaDetallesPCrear.clear();
        listaDetallesPModificar.clear();
        detallePSeleccionado = null;
        listaDetallesP = null;
        guardado = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            anio = (Column) c.getViewRoot().findComponent("form:datosDetallesP:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:datosDetallesP:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            dia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            tipodia = (Column) c.getViewRoot().findComponent("form:datosDetallesP:tipodia");
            tipodia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            bandera = 0;
            listaDetallesPFiltrar = null;
            tipoLista = 0;
        }

        listaDetallesPBorrar.clear();
        listaDetallesPCrear.clear();
        listaDetallesPModificar.clear();
        k = 0;
        listaDetallesP = null;
        detallePSeleccionado = null;
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDetallesP");
    }

    public void limpiarDuplicarDetallesP() {
        duplicarDetalleP = new DetallesPeriodicidades();
    }

    public void borrarDetallesP() {
        if (detallePSeleccionado != null) {
            if (!listaDetallesPModificar.isEmpty() && listaDetallesPModificar.contains(detallePSeleccionado)) {
                int modIndex = listaDetallesPModificar.indexOf(detallePSeleccionado);
                listaDetallesPModificar.remove(modIndex);
                listaDetallesPBorrar.add(detallePSeleccionado);
            } else if (!listaDetallesPCrear.isEmpty() && listaDetallesPCrear.contains(detallePSeleccionado)) {
                int crearIndex = listaDetallesPCrear.indexOf(detallePSeleccionado);
                listaDetallesPCrear.remove(crearIndex);
            } else {
                listaDetallesPBorrar.add(detallePSeleccionado);
            }
            listaDetallesP.remove(detallePSeleccionado);
            if (tipoLista == 1) {
                listaDetallesPFiltrar.remove(detallePSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosDetallesP");
            detallePSeleccionado = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DetallesPeriodicidadesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DetallesPeriodicidadesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (detallePSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(detallePSeleccionado.getSecuencia(), "DETALLESPERIODICIDADES");
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
        } else if (administrarRastros.verificarHistoricosTabla("DETALLESPERIODICIDADES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void seleccionarTipoDia(String estadoTipoDia, DetallesPeriodicidades detallesp) {
        detallePSeleccionado = detallesp;
        if (estadoTipoDia.equals("SEMANAL")) {
            detallePSeleccionado.setTipodia("S");
        } else if (estadoTipoDia.equals("MENSUAL")) {
            detallePSeleccionado.setTipodia("M");
        } else if (estadoTipoDia.equals("ANUAL")) {
            detallePSeleccionado.setTipodia("A");
        }

        if (!listaDetallesPCrear.contains(detallePSeleccionado)) {
            if (listaDetallesPModificar.isEmpty()) {
                listaDetallesPModificar.add(detallePSeleccionado);
            } else if (!listaDetallesPModificar.contains(detallePSeleccionado)) {
                listaDetallesPModificar.add(detallePSeleccionado);
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosDetallesP");
    }

    public void seleccionarTipoDiaNuevoyDuplicado(String estadoTipoDia, int tiponuevo) {
        if (tiponuevo == 1) {
            if (estadoTipoDia.equals("SEMANAL")) {
                nuevoDetalleP.setTipodia("S");
            } else if (estadoTipoDia.equals("MENSUAL")) {
                nuevoDetalleP.setTipodia("M");
            } else if (estadoTipoDia.equals("ANUAL")) {
                nuevoDetalleP.setTipodia("A");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDia");
        } else if (tiponuevo == 2) {

            if (estadoTipoDia.equals("SEMANAL")) {
                duplicarDetalleP.setTipodia("S");
            } else if (estadoTipoDia.equals("MENSUAL")) {
                duplicarDetalleP.setTipodia("M");
            } else if (estadoTipoDia.equals("ANUAL")) {
                duplicarDetalleP.setTipodia("A");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDia");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    ///GETS Y SETS///////
    public List<DetallesPeriodicidades> getListaDetallesP() {
        if (listaDetallesP == null) {
            if (periodicidadActual.getSecuencia() != null) {
                listaDetallesP = administrarDetallesPeriodicidades.consultarDetallesPeriodicidades(periodicidadActual.getSecuencia());
            }
        }

        return listaDetallesP;
    }

    public void setListaDetallesP(List<DetallesPeriodicidades> listaDetallesP) {
        this.listaDetallesP = listaDetallesP;
    }

    public DetallesPeriodicidades getDetallePSeleccionado() {
        return detallePSeleccionado;
    }

    public void setDetallePSeleccionado(DetallesPeriodicidades detallePSeleccionado) {
        this.detallePSeleccionado = detallePSeleccionado;
    }

    public DetallesPeriodicidades getDuplicardetallePSeleccionado() {
        return duplicarDetalleP;
    }

    public void setDuplicardetallePSeleccionado(DetallesPeriodicidades duplicarDetalleP) {
        this.duplicarDetalleP = duplicarDetalleP;
    }

    public DetallesPeriodicidades getNuevodetallePSeleccionado() {
        return nuevoDetalleP;
    }

    public void setNuevodetallePSeleccionado(DetallesPeriodicidades nuevoDetalleP) {
        this.nuevoDetalleP = nuevoDetalleP;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDetallesP");
        infoRegistro = String.valueOf(tabla.getRowCount());
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

    public List<DetallesPeriodicidades> getListaDetallesPFiltrar() {
        return listaDetallesPFiltrar;
    }

    public void setListaDetallesPFiltrar(List<DetallesPeriodicidades> listaDetallesPFiltrar) {
        this.listaDetallesPFiltrar = listaDetallesPFiltrar;
    }

    public DetallesPeriodicidades getDuplicarDetalleP() {
        return duplicarDetalleP;
    }

    public void setDuplicarDetalleP(DetallesPeriodicidades duplicarDetalleP) {
        this.duplicarDetalleP = duplicarDetalleP;
    }

    public DetallesPeriodicidades getNuevoDetalleP() {
        return nuevoDetalleP;
    }

    public void setNuevoDetalleP(DetallesPeriodicidades nuevoDetalleP) {
        this.nuevoDetalleP = nuevoDetalleP;
    }

    public DetallesPeriodicidades getEditarDetalleP() {
        return editarDetalleP;
    }

    public void setEditarDetalleP(DetallesPeriodicidades editarDetalleP) {
        this.editarDetalleP = editarDetalleP;
    }
    
    
    
}
