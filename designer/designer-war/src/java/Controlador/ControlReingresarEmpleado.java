/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarReingresarEmpleadoInterface;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import Entidades.Personas;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlReingresarEmpleado implements Serializable {

   private static Logger log = Logger.getLogger(ControlReingresarEmpleado.class);

   @EJB
   AdministrarReingresarEmpleadoInterface administrarReingresarEmpleado;
   //LOV EMPLEADOS
   private List<Empleados> lovEmpleados;
   private List<Empleados> filtradoLovEmpleados;
   private Empleados empleadoSeleccionado;
   //LOV ESTRUCTURAS
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> filtradoLovEstructuras;
   private Estructuras estructuraSeleccionada;
   //Campos del Formulario
   private Empleados empleado;
   private Estructuras estructura;
   private Date fechaReingreso;
   private Date fechaFinContrato;
   private boolean aceptar;
   public String infoRegistroEmpleados;
   public String infoRegistroEstructuras;
   private String mensajeValidacion;
   public Date fechaRetiro;
   public String mostrarFechaRetiro;
   private SimpleDateFormat formato;
   public String nombre;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ListasRecurrentes listasRecurrentes;

   public ControlReingresarEmpleado() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      lovEmpleados = null;
      lovEstructuras = null;
      empleado = new Empleados();
      formato = new SimpleDateFormat("dd/MM/yyyy");
      estructura = new Estructuras();
      aceptar = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovEmpleados = null;
      lovEstructuras = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarReingresarEmpleado.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
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
      String pagActual = "reingresarempleado";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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

   public void cancelarModificaciones() {
      empleado = new Empleados();
      estructura = new Estructuras();
      fechaReingreso = null;
      fechaFinContrato = null;
      RequestContext.getCurrentInstance().update("form:nombreEmpleado");
      RequestContext.getCurrentInstance().update("form:estructura");
      RequestContext.getCurrentInstance().update("form:PanelTotal");
      RequestContext.getCurrentInstance().update("form:fechaReingreso");
      RequestContext.getCurrentInstance().update("form:fechaFinContrato");
   }

   public void salir() {
      limpiarListasValor();
      empleado = new Empleados();
      estructura = new Estructuras();
      fechaReingreso = null;
      fechaFinContrato = null;
      RequestContext.getCurrentInstance().update("form:nombreEmpleado");
      RequestContext.getCurrentInstance().update("form:estructura");
      RequestContext.getCurrentInstance().update("form:fechaReingreso");
      RequestContext.getCurrentInstance().update("form:fechaFinContrato");
   }

   //AUTOCOMPLETAR
   public void modificarReingreso(String confirmarCambio, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())
                    || lovEmpleados.get(i).getCodigoempleado().toString().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            setEmpleado(lovEmpleados.get(indiceUnicoElemento));
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
         }
      } else if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
         for (int i = 0; i < lovEstructuras.size(); i++) {
            if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())
                    || lovEstructuras.get(i).getCodigo().toString().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            setEstructura(lovEstructuras.get(indiceUnicoElemento));
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
         }
      }
   }

   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
   public void asignarIndex(int dlg) {
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
      }
   }

   public void actualizarEmpleados() {
      RequestContext context = RequestContext.getCurrentInstance();
      empleado = empleadoSeleccionado;
      empleadoSeleccionado = null;
      aceptar = true;
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
      RequestContext.getCurrentInstance().update("form:nombreEmpleado");
      RequestContext.getCurrentInstance().update("form:nombreEmpleadoC");
   }

   public void actualizarEstructuras() {
      RequestContext context = RequestContext.getCurrentInstance();
      estructura = estructuraSeleccionada;
      estructuraSeleccionada = null;
      aceptar = true;
      context.reset("formularioDialogos:LOVEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructuras");
      RequestContext.getCurrentInstance().update("form:estructura");
      RequestContext.getCurrentInstance().update("form:estructuraC");
   }

   public void validarFechaNull() {
      if (fechaReingreso != null) {
         verificarFecha();
      } else {
         log.info("Nula Fecha ");
      }
   }

   public void verificarFecha() {
      formato = new SimpleDateFormat("dd/MM/yyyy");
      if (empleado.getPersona() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:seleccioneEmpleado");
         RequestContext.getCurrentInstance().execute("PF('seleccioneEmpleado').show()");
         fechaReingreso = null;
         RequestContext.getCurrentInstance().update("form:fechaReingreso");
      } else {
         fechaRetiro = administrarReingresarEmpleado.obtenerFechaRetiro(empleado.getSecuencia());
         log.info("formato: " + formato);
         log.info("fechaRetiro: " + fechaRetiro);
         mostrarFechaRetiro = formato.format(fechaRetiro);
         log.info("mostrarFechaRetiro: " + mostrarFechaRetiro);
         if (fechaReingreso.before(fechaRetiro)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      }
   }

   public void irPaso3() {
      RequestContext.getCurrentInstance().update("formularioDialogos:paso3");
      RequestContext.getCurrentInstance().execute("PF('paso3').show()");
   }

   public void reingresarEmpleado() {
      int pasa = 0;
      nombre = new String();
      mensajeValidacion = new String();
      log.info("ControlReingresarEmpleado.reingresarEmpleado() nombre: " + empleado.getNombrePersona() + ", documento: " + empleado.getNumeroDocumentoPersona());
      if (empleado.getNumeroDocumentoPersona() == null) {
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      if (estructura.getNombre() == null || estructura.getNombre().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Estructura\n";
         pasa++;
      }
      if (fechaReingreso == null) {
         mensajeValidacion = mensajeValidacion + "* Fecha Reingreso";
         pasa++;
      }
      log.info("ControlReingresarEmpleado.reingresarEmpleado() pasa: " + pasa);
      if (pasa == 0) {
         fechaRetiro = administrarReingresarEmpleado.obtenerFechaRetiro(empleado.getSecuencia());
         mostrarFechaRetiro = formato.format(fechaRetiro);
         if (fechaReingreso.before(fechaRetiro)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:paso1");
            RequestContext.getCurrentInstance().execute("PF('paso1').show()");
            nombre = empleado.getNombreCompleto();
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionReingreso");
         RequestContext.getCurrentInstance().execute("PF('validacionReingreso').show()");
      }
   }

   public void reingresoEmpleado() {
      try {
         administrarReingresarEmpleado.reintegrarEmpleado(empleado.getCodigoempleado(), estructura.getCentrocosto().getCodigo(), fechaReingreso, estructura.getCentrocosto().getEmpresa().getCodigo(), fechaFinContrato);
         RequestContext.getCurrentInstance().update("formularioDialogos:exito");
         listasRecurrentes.limpiarListasEmpleados();
         RequestContext.getCurrentInstance().execute("PF('exito').show()");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 1");
         empleado = new Empleados();
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 2");
         estructura = new Estructuras();
         fechaReingreso = null;
         fechaFinContrato = null;
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 3");
         RequestContext.getCurrentInstance().update("form:nombreEmpleado");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 4");
         RequestContext.getCurrentInstance().update("form:estructura");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 5");
         RequestContext.getCurrentInstance().update("form:PanelTotal");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 6");
         RequestContext.getCurrentInstance().update("form:fechaReingreso");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 7");
         RequestContext.getCurrentInstance().update("form:fechaFinContrato");
         log.info("ControlReingresarEmpleado.reingresoEmpleado() 8");
      } catch (Exception e) {
         log.warn("Error en borrar al empleado");
         RequestContext.getCurrentInstance().update("formularioDialogos:error");
         RequestContext.getCurrentInstance().execute("PF('error').show()");
      }
   }

   public void cancelarCambioEmpleados() {
      empleadoSeleccionado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void cancelarCambioEstructuras() {
      estructuraSeleccionada = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void contarRegistrosLovEstructuras() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEstructuras");
   }

   public void contarRegistrosLovEmpleados() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
   }

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarReingresarEmpleado.listaEmpleados();
      }
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public Estructuras getEstructura() {

      return estructura;
   }

   public void setEstructura(Estructuras estructura) {
      this.estructura = estructura;
   }

   public Date getFechaReingreso() {
      return fechaReingreso;
   }

   public void setFechaReingreso(Date fechaReingreso) {
      this.fechaReingreso = fechaReingreso;
   }

   public Date getFechaFinContrato() {
      return fechaFinContrato;
   }

   public void setFechaFinContrato(Date fechaFinContrato) {
      this.fechaFinContrato = fechaFinContrato;
   }

   public List<Empleados> getFiltradoLovEmpleados() {
      return filtradoLovEmpleados;
   }

   public void setFiltradoLovEmpleados(List<Empleados> filtradoLovEmpleados) {
      this.filtradoLovEmpleados = filtradoLovEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Estructuras> getLovEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarReingresarEmpleado.listaEstructuras();
      }
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public List<Estructuras> getFiltradoLovEstructuras() {
      return filtradoLovEstructuras;
   }

   public void setFiltradoLovEstructuras(List<Estructuras> filtradoLovEstructuras) {
      this.filtradoLovEstructuras = filtradoLovEstructuras;
   }

   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public String getInfoRegistroEstructuras() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEstructuras");
      infoRegistroEstructuras = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructuras;
   }

   public void setInfoRegistroEstructuras(String infoRegistroEstructuras) {
      this.infoRegistroEstructuras = infoRegistroEstructuras;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getMostrarFechaRetiro() {
      return mostrarFechaRetiro;
   }

   public void setMostrarFechaRetiro(String mostrarFechaRetiro) {
      this.mostrarFechaRetiro = mostrarFechaRetiro;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

}
