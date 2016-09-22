package Controlador;


import Entidades.Empleados;
import Entidades.VWVacaPendientesEmpleados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVWVacaPendientesEmpleadosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlEmplVacaPendiente implements Serializable {

   @EJB
   AdministrarVWVacaPendientesEmpleadosInterface administrarVWVacaPendientesEmpleados;
   private List<VWVacaPendientesEmpleados> listVacaPendientes;
   private List<VWVacaPendientesEmpleados> filtrarListVacaPendientes;
   private VWVacaPendientesEmpleados vacaPendienteSeleccionada;
   private List<VWVacaPendientesEmpleados> listVacaDisfrutadas;
   private List<VWVacaPendientesEmpleados> filtrarListVacaDisfrutadas;
   private VWVacaPendientesEmpleados vacaDisfrutadaSeleccionada;
   private int tipoTabla, tipoListaPendientes, tipoListaDisfrutadas;
   private int banderaPendientes, banderaDisfrutadas;
   private int casillaPendiente, casillaDisfrutada;
   //////
   private Empleados empleado;
   //
   private List<VWVacaPendientesEmpleados> listModificacionesTablaPendientes;
   private List<VWVacaPendientesEmpleados> listModificacionesTablaDisfrutadas;
   //////
   private List<VWVacaPendientesEmpleados> listCrearTablaPendientes;
   private List<VWVacaPendientesEmpleados> listCrearTablaDisfrutadas;
   //////
   private List<VWVacaPendientesEmpleados> listBorrarTablaPendientes;
   private List<VWVacaPendientesEmpleados> listBorrarTablaDisfrutadas;
   //
   private VWVacaPendientesEmpleados editarVacacion;
   private VWVacaPendientesEmpleados nuevaVacacion;
   private VWVacaPendientesEmpleados duplicarVacacion;
   //
   private String nombreTabla, nombreXML;
   //
   private BigInteger totalDiasPendientes;
   private BigDecimal diasProvisionados;
   //
   private Column vacacionesDP, vacacionesFechaInicial, vacacionesFechaFinal, vacacionesDPD, vacacionesFechaInicialD, vacacionesFechaFinalD;
   private Date fechaAño1900;
   private Date fechaIniP, fechaFinP;
   private Date fechaIniD, fechaFinD;
   private String altoTabla1, altoTabla2;
   private boolean guardado;
   //Fecha Contratacion
   private Date fechaContratacion;
   private String fechaContratacionText;
   private int k;
   //Tabla - Nuevo - Duplicar
   private int tipoActualizacion;
   //Validar fechas
   VWVacaPendientesEmpleados regVacaAuxiliar;
   private String infoRegistroD, infoRegistroP;
   private DataTable tablaC;

   public ControlEmplVacaPendiente() {
      k = 0;
      guardado = true;
      listVacaDisfrutadas = null;
      listVacaPendientes = null;
      tipoTabla = 0;
      banderaPendientes = 0;
      banderaDisfrutadas = 0;
      tipoListaPendientes = 0;
      tipoListaDisfrutadas = 0;
      casillaPendiente = 0;
      casillaDisfrutada = 0;
      editarVacacion = new VWVacaPendientesEmpleados();
      nuevaVacacion = new VWVacaPendientesEmpleados();
      duplicarVacacion = new VWVacaPendientesEmpleados();
      listModificacionesTablaPendientes = new ArrayList<VWVacaPendientesEmpleados>();
      listModificacionesTablaDisfrutadas = new ArrayList<VWVacaPendientesEmpleados>();
      listCrearTablaPendientes = new ArrayList<VWVacaPendientesEmpleados>();
      listCrearTablaDisfrutadas = new ArrayList<VWVacaPendientesEmpleados>();
      listBorrarTablaPendientes = new ArrayList<VWVacaPendientesEmpleados>();
      listBorrarTablaDisfrutadas = new ArrayList<VWVacaPendientesEmpleados>();
      nombreTabla = ":formExportarVS:datosVSEmpleadoExportar";
      nombreXML = "VacacionesPendientesXML";
      totalDiasPendientes = BigInteger.valueOf(0);
      diasProvisionados = BigDecimal.valueOf(0);
      altoTabla1 = "275";
      altoTabla2 = "275";
      regVacaAuxiliar = null;
      tipoActualizacion = -1;
      fechaAño1900 = new Date();
      //Año 1900
      fechaAño1900.setYear(0);
      fechaAño1900.setMonth(1);
      fechaAño1900.setDate(1);
      fechaContratacionText = "";
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarVWVacaPendientesEmpleados.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      empleado = administrarVWVacaPendientesEmpleados.obtenerEmpleado(sec);

      fechaContratacion = administrarVWVacaPendientesEmpleados.obtenerFechaFinalContratacionEmpleado(empleado.getSecuencia());
      int mes = fechaContratacion.getMonth();
      if (mes < 9) {
         fechaContratacionText = "" + fechaContratacion.getDate() + "/0" + (mes + 1) + "/" + (fechaContratacion.getYear() + 1900);
      } else {
         fechaContratacionText = "" + fechaContratacion.getDate() + "/" + (mes + 1) + "/" + (fechaContratacion.getYear() + 1900);
      }
      getListVacaDisfrutadas();
      getListVacaPendientes();
      if (listVacaPendientes != null) {
         if (!listVacaPendientes.isEmpty()) {
            vacaPendienteSeleccionada = listVacaPendientes.get(0);
         }
      }
   }

   public void modificacionesTablas() {
      if (tipoTabla == 1) {//Para listVacaPendientes
         VWVacaPendientesEmpleados vacaPendiente = vacaPendienteSeleccionada;
         listModificacionesTablaPendientes.add(vacaPendiente);

         totalDiasPendientes = BigInteger.valueOf(0);
         getTotalDiasPendientes();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:totalDiasP");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (tipoTabla == 2) {//Para listVacaDisfrutadas
         VWVacaPendientesEmpleados vacaPendiente = vacaDisfrutadaSeleccionada;
         listModificacionesTablaDisfrutadas.add(vacaPendiente);

         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public boolean validarFechasRegistroPendientes() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = true;
      if (tipoActualizacion == 0) {
         //Si nuevaVacacion F Inicial y F Final es despues de 1900
         if (regVacaAuxiliar.getInicialcausacion().after(fechaAño1900) && regVacaAuxiliar.getInicialcausacion().before(regVacaAuxiliar.getFinalcausacion())) {
            // Si nuevaVacacion F Inicial y F Final son despues de fechaFinalContratacion
            if ((regVacaAuxiliar.getInicialcausacion().after(fechaContratacion) || regVacaAuxiliar.getInicialcausacion().equals(fechaContratacion)) && regVacaAuxiliar.getFinalcausacion().after(fechaContratacion)) {
               retorno = validarTraslapos(regVacaAuxiliar);
            } else {
               retorno = false;
               RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
               RequestContext.getCurrentInstance().execute("PF('errorFechaContratacion').show()");
            }
         } else {
            retorno = false;
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      }
      if (tipoActualizacion == 1) {//Para una nueva vacacionP
         //Si nuevaVacacion F Inicial y F Final es despues de 1900
         if (nuevaVacacion.getInicialcausacion().after(fechaAño1900) && nuevaVacacion.getInicialcausacion().before(nuevaVacacion.getFinalcausacion())) {
            int n = nuevaVacacion.getInicialcausacion().getMonth();
            // Si nuevaVacacion F Inicial y F Final son despues de fechaFinalContratacion
            if (nuevaVacacion.getInicialcausacion().after(fechaContratacion) && nuevaVacacion.getFinalcausacion().after(fechaContratacion)) {
               retorno = validarTraslapos(nuevaVacacion);
            } else {
               retorno = false;
               RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
               RequestContext.getCurrentInstance().execute("PF('errorFechaContratacion').show()");
            }
         } else {
            retorno = false;
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      }
      if (tipoActualizacion == 2) {//Para Duplicar una vacacionP
         //Si nuevaVacacion F Inicial y F Final es despues de 1900
         if (duplicarVacacion.getInicialcausacion().after(fechaAño1900) && duplicarVacacion.getInicialcausacion().before(duplicarVacacion.getFinalcausacion())) {
            // Si nuevaVacacion F Inicial y F Final son despues de fechaFinalContratacion
            if (duplicarVacacion.getInicialcausacion().after(fechaContratacion) && duplicarVacacion.getFinalcausacion().after(fechaContratacion)) {
               retorno = validarTraslapos(duplicarVacacion);
            } else {
               retorno = false;
               RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
               RequestContext.getCurrentInstance().execute("PF('errorFechaContratacion').show()");
            }
         } else {
            retorno = false;
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroDisfrutadas() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = true;
      //Si nuevaVacacion F Inicial y F Final es despues de 1900
      if (regVacaAuxiliar.getInicialcausacion().after(fechaAño1900) && regVacaAuxiliar.getInicialcausacion().before(regVacaAuxiliar.getFinalcausacion())) {
         // Si nuevaVacacion F Inicial y F Final son despues de fechaFinalContratacion
         if (regVacaAuxiliar.getInicialcausacion().after(fechaContratacion) && regVacaAuxiliar.getFinalcausacion().after(fechaContratacion)) {
            retorno = validarTraslapos(regVacaAuxiliar);
         } else {
            retorno = false;
            RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechaContratacion').show()");
         }
      } else {
         retorno = false;
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
      }
      return retorno;
   }

   public boolean validarTraslapos(VWVacaPendientesEmpleados registro) {
      RequestContext context = RequestContext.getCurrentInstance();
      //Fechas para validar
      int dia = fechaContratacion.getDate();
      int mes = fechaContratacion.getMonth();
      int anio = registro.getInicialcausacion().getYear();
      Date fechaInicioAux = new Date(anio, mes, dia);
      Date fechaFinalAux = new Date((anio + 1), mes, dia);

      boolean respuesta = true;
      //Si F inicial es igual o despues de la F contratacion(mes y dia solamente) y F final es antes de (1 año despues de la inicial)
      //Es decir que el periodo registrado este en el rango de 1 año, tomando como inicio el mismo dia y mes de contratacion para añosn posteriores 

      if ((registro.getInicialcausacion().equals(fechaInicioAux) || registro.getInicialcausacion().after(fechaInicioAux))
              && (registro.getFinalcausacion().before(fechaFinalAux))) {
         int error = 0;
         for (int i = 0; i < listVacaDisfrutadas.size(); i++) {
            //Si la fecha inicial y final a registrar son despues de las fechas de todos los registros anteriores
            if (registro.getInicialcausacion().after(listVacaDisfrutadas.get(i).getFinalcausacion())
                    && registro.getInicialcausacion().after(listVacaDisfrutadas.get(i).getInicialcausacion())) {
            } else {
               error++;
            }
         }
         for (int i = 0; i < listVacaPendientes.size(); i++) {
            //Si la fecha inicial y final a registrar son despues de las fechas de todos los registros anteriores
            if (registro.getInicialcausacion().after(listVacaPendientes.get(i).getFinalcausacion())
                    && registro.getInicialcausacion().after(listVacaPendientes.get(i).getInicialcausacion())) {
            } else {
               error++;
            }
         }
         if (error == 0) {
            respuesta = true;
         } else {
            respuesta = false;
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechaContratacionTras').show()");
         }
      } else {
         respuesta = false;
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorFechaContratacionMargenAnio').show()");
      }
      return respuesta;
   }

   public void modificarFechasDisfrutadas(VWVacaPendientesEmpleados vDisfrutada, int colum) {
      RequestContext context = RequestContext.getCurrentInstance();
      // LLENAR registro auxiliar para validaciones de la lista con o sin filtro
      vacaDisfrutadaSeleccionada = vDisfrutada;
      regVacaAuxiliar = vacaDisfrutadaSeleccionada;

      boolean revertir = false;//Por si no cumple alguna validacion
      //Si las fechas son nulas
      if (regVacaAuxiliar.getInicialcausacion() != null && regVacaAuxiliar.getFinalcausacion() != null) {
         tipoActualizacion = 0;
         //Si la fecha inicial y final cumplen las validaciones
         if (validarFechasRegistroDisfrutadas()) {
            cambiarIndex(2, vacaDisfrutadaSeleccionada, colum);
            modificacionesTablas();
         }
      } else {
         revertir = true;
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
      regVacaAuxiliar = null;
      if (revertir) {
         vacaDisfrutadaSeleccionada.setInicialcausacion(fechaFinD);
         vacaDisfrutadaSeleccionada.setFinalcausacion(fechaIniD);

      }
   }

   public void modificarFechasPendientes(VWVacaPendientesEmpleados vPendiente, int colum) {
      RequestContext context = RequestContext.getCurrentInstance();
      // LLENAR registro auxiliar para validaciones de la lista con o sin filtro
      vacaPendienteSeleccionada = vPendiente;
      regVacaAuxiliar = vacaPendienteSeleccionada;

      boolean revertir = false;//Por si no cumople alguna validacion
      //Si las fechas son nulas
      if (regVacaAuxiliar.getInicialcausacion() != null && regVacaAuxiliar.getFinalcausacion() != null) {
         tipoActualizacion = 0;
         //Si la fecha inicial y final cumplen las validaciones
         if (validarFechasRegistroPendientes()) {
            cambiarIndex(1, vacaPendienteSeleccionada, colum);
            modificacionesTablas();
         } else {
            revertir = true;
         }
      } else {
         revertir = true;
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
      regVacaAuxiliar = null;
      if (revertir) {
         vacaPendienteSeleccionada.setInicialcausacion(fechaIniP);
         vacaPendienteSeleccionada.setFinalcausacion(fechaFinP);

      }
   }

   public void limpiarNuevoRegistro() {
      nuevaVacacion = new VWVacaPendientesEmpleados();
      tipoActualizacion = -1;
   }

   public void agregarNuevoPendientes() {
      //Si Fueron ingresados todos los campos obligatorios
      if (nuevaVacacion.getInicialcausacion() != null && nuevaVacacion.getFinalcausacion() != null && nuevaVacacion.getDiaspendientes() != null) {
         tipoActualizacion = 1;
         regVacaAuxiliar = vacaPendienteSeleccionada;
         if (validarFechasRegistroPendientes()) {
            if (tipoListaPendientes == 1) {//Si la tabla tiene filtro
               FacesContext c = FacesContext.getCurrentInstance();
               vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
               vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
               vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
               vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
               vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               altoTabla1 = "275";
               RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
               banderaPendientes = 0;
               filtrarListVacaPendientes = null;
               tipoListaPendientes = 0;
            }
            BigInteger k1;
            k1 = BigInteger.valueOf(1);

            nuevaVacacion.setSecuencia(k1);
            nuevaVacacion.setEstado("ABIERTO");
            nuevaVacacion.setEmpleado(empleado.getSecuencia());
            //Si la tabla vaca pendientes no tiene registros
            if (listVacaPendientes == null) {
               listVacaPendientes = new ArrayList<VWVacaPendientesEmpleados>();
            }
            listVacaPendientes.add(nuevaVacacion);
            listCrearTablaPendientes.add(nuevaVacacion);
            vacaPendienteSeleccionada = listVacaPendientes.get(listVacaPendientes.size() - 1);
            contarRegistrosP();
            contarRegistrosD();
            nuevaVacacion = new VWVacaPendientesEmpleados();
            RequestContext context = RequestContext.getCurrentInstance();
            getTotalDiasPendientes();
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:totalDiasP");
            RequestContext.getCurrentInstance().update("form:informacionRegistroP");
            RequestContext.getCurrentInstance().update("form:informacionRegistroD");
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVP').hide()");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else {//Si hay datos obligatorios nulos
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void duplicarRegistroTabla() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vacaDisfrutadaSeleccionada == null && vacaPendienteSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (vacaDisfrutadaSeleccionada != null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroP').show()");
         }
         if (vacaPendienteSeleccionada != null) {
            duplicarVacacion = new VWVacaPendientesEmpleados();
            duplicarVacacion.setEmpleado(empleado.getSecuencia());
            duplicarVacacion.setEstado("ABIERTO");
            System.out.println("vacaPendienteSeleccionada: " + vacaPendienteSeleccionada);
            duplicarVacacion.setDiaspendientes(vacaPendienteSeleccionada.getDiaspendientes());
            duplicarVacacion.setInicialcausacion(vacaPendienteSeleccionada.getInicialcausacion());
            duplicarVacacion.setFinalcausacion(vacaPendienteSeleccionada.getFinalcausacion());

            //Dialogo Duplicar VacaPendiente
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVP");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVP').show()");
         }
      }
   }

   public void confirmarDuplicarPendientes() {
      if (duplicarVacacion.getInicialcausacion() != null && duplicarVacacion.getFinalcausacion() != null && duplicarVacacion.getDiaspendientes() != null) {
         tipoActualizacion = 2;
         regVacaAuxiliar = vacaPendienteSeleccionada;
         if (validarFechasRegistroPendientes()) {
            k++;
            BigInteger l = BigInteger.valueOf(k);
            duplicarVacacion.setSecuencia(l);
            if (tipoListaPendientes == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
               vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
               vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
               vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
               vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               altoTabla1 = "275";
               RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
               banderaPendientes = 0;
               filtrarListVacaPendientes = null;
               tipoListaPendientes = 0;
            }
            duplicarVacacion.setEstado("ABIERTO");
            listCrearTablaPendientes.add(duplicarVacacion);
            listVacaPendientes.add(duplicarVacacion);
            vacaPendienteSeleccionada = listVacaPendientes.get(listVacaPendientes.size() - 1);
            contarRegistrosP();
            contarRegistrosD();
            duplicarVacacion = new VWVacaPendientesEmpleados();
            RequestContext context = RequestContext.getCurrentInstance();
            getTotalDiasPendientes();
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:totalDiasP");
            RequestContext.getCurrentInstance().update("form:informacionRegistroP");
            RequestContext.getCurrentInstance().update("form:informacionRegistroD");
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVP').hide()");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarDuplicadoVacaPendiente() {
      duplicarVacacion = new VWVacaPendientesEmpleados();
      tipoActualizacion = -1;
   }

   public void eliminarRegistroTabla() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vacaDisfrutadaSeleccionada == null && vacaPendienteSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (tipoTabla == 1) {
            if (!listCrearTablaPendientes.isEmpty() && listCrearTablaPendientes.contains(vacaPendienteSeleccionada)) {
               listCrearTablaPendientes.remove(vacaPendienteSeleccionada);
            } else if (!listModificacionesTablaPendientes.isEmpty() && listModificacionesTablaPendientes.contains(vacaPendienteSeleccionada)) {
               listModificacionesTablaPendientes.remove(vacaPendienteSeleccionada);
               listBorrarTablaPendientes.add(vacaPendienteSeleccionada);
            } else {
               VWVacaPendientesEmpleados vacaPendiente = vacaPendienteSeleccionada;
               listBorrarTablaPendientes.add(vacaPendiente);
               listVacaPendientes.remove(vacaPendienteSeleccionada);
               if (tipoListaPendientes == 1) {
                  filtrarListVacaPendientes.remove(vacaPendienteSeleccionada);
               }
            }
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            getTotalDiasPendientes();
            RequestContext.getCurrentInstance().update("form:totalDiasP");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         if (tipoTabla == 2) {
            if (!listCrearTablaDisfrutadas.isEmpty() && listCrearTablaDisfrutadas.contains(vacaDisfrutadaSeleccionada)) {
               listCrearTablaDisfrutadas.remove(vacaDisfrutadaSeleccionada);
            } else if (!listModificacionesTablaDisfrutadas.isEmpty() && listModificacionesTablaDisfrutadas.contains(vacaDisfrutadaSeleccionada)) {
               listModificacionesTablaDisfrutadas.remove(vacaDisfrutadaSeleccionada);
               listBorrarTablaDisfrutadas.add(vacaDisfrutadaSeleccionada);
            } else {
               VWVacaPendientesEmpleados vacaDisfrutada = vacaDisfrutadaSeleccionada;
               listBorrarTablaDisfrutadas.add(vacaDisfrutada);
               listVacaDisfrutadas.remove(vacaDisfrutadaSeleccionada);
               if (tipoListaDisfrutadas == 1) {
                  filtrarListVacaDisfrutadas.remove(vacaDisfrutadaSeleccionada);
               }
            }
            RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      vacaPendienteSeleccionada = null;
      vacaDisfrutadaSeleccionada = null;
      contarRegistrosP();
      contarRegistrosD();
      RequestContext.getCurrentInstance().update("form:informacionRegistroD");
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
   }

   public void cancelarModificaciones() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (tipoListaDisfrutadas == 1) {
         vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
         vacacionesDPD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
         altoTabla2 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaDisfrutadas = 0;
         filtrarListVacaDisfrutadas = null;
         tipoListaDisfrutadas = 0;
      }
      if (tipoListaPendientes == 1) {
         vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
         vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
         vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
         vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         altoTabla1 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         banderaPendientes = 0;
         filtrarListVacaPendientes = null;
         tipoListaPendientes = 0;
      }
      listBorrarTablaDisfrutadas.clear();
      listBorrarTablaPendientes.clear();
      listCrearTablaDisfrutadas.clear();
      listCrearTablaPendientes.clear();
      listModificacionesTablaDisfrutadas.clear();
      listModificacionesTablaPendientes.clear();
      listVacaDisfrutadas = null;
      listVacaPendientes = null;
      casillaDisfrutada = -1;
      casillaPendiente = -1;
      vacaDisfrutadaSeleccionada = null;
      vacaPendienteSeleccionada = null;
      tipoTabla = -1;
      regVacaAuxiliar = null;
      tipoActualizacion = -1;
      guardado = true;

      recargarListas();
      contarRegistrosD();
      contarRegistrosP();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:totalDiasP");
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
      RequestContext.getCurrentInstance().update("form:informacionRegistroD");
      RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
      RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
      RequestContext.getCurrentInstance().update("form:totalDiasP");

   }

   public void activarCtrlF11() {
      if (vacaPendienteSeleccionada != null) {
         filtradoVacacionesPendientes();
      }
      if (vacaDisfrutadaSeleccionada != null) {
         filtradoVacacionesDisfrutadas();
      }
   }

   public void filtradoVacacionesPendientes() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaPendientes == 0) {
         vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
         vacacionesDP.setFilterStyle("width: 85% !important");
         vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
         vacacionesFechaFinal.setFilterStyle("width: 85% !important");
         vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
         vacacionesFechaInicial.setFilterStyle("width: 85% !important");
         altoTabla1 = "255";
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         banderaPendientes = 1;
      } else if (banderaPendientes == 1) {
         vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
         vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
         vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
         vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         altoTabla1 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         banderaPendientes = 0;
         filtrarListVacaPendientes = null;
         tipoListaPendientes = 0;
      }
   }

   public void filtradoVacacionesDisfrutadas() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaDisfrutadas == 0) {
         vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
         vacacionesDPD.setFilterStyle("width: 85% !important");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("width: 85% !important");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("width: 85% !important");
         altoTabla2 = "255";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaDisfrutadas = 1;
      } else if (banderaDisfrutadas == 1) {
         vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
         vacacionesDPD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
         altoTabla2 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaDisfrutadas = 0;
         filtrarListVacaDisfrutadas = null;
         tipoListaDisfrutadas = 0;
      }
   }

   public void cambiarIndex(int tabla, VWVacaPendientesEmpleados vaca, int casilla) {
      if (tabla == 1) {
         tipoTabla = 1;
         vacaPendienteSeleccionada = vaca;
         casillaPendiente = casilla;
         casillaDisfrutada = -1;
         fechaIniP = vacaPendienteSeleccionada.getInicialcausacion();
         fechaFinP = vacaPendienteSeleccionada.getFinalcausacion();

         if (banderaDisfrutadas == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
            vacacionesDPD.setFilterStyle("display: none; visibility: hidden;");
            vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
            vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
            vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
            vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "275";
            RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
            banderaDisfrutadas = 0;
            filtrarListVacaDisfrutadas = null;
            tipoListaDisfrutadas = 0;
         }
         vacaDisfrutadaSeleccionada = null;
         RequestContext.getCurrentInstance().execute("PF('datosVacacionesDEmpleado').unselectAllRows()");
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
      }
      if (tabla == 2) {
         tipoTabla = 2;
         vacaDisfrutadaSeleccionada = vaca;
         casillaDisfrutada = casilla;
         casillaPendiente = -1;
         if (banderaPendientes == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
            vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
            vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
            vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
            vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "275";
            RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
            banderaPendientes = 0;
            filtrarListVacaPendientes = null;
            tipoListaPendientes = 0;
         }
         vacaPendienteSeleccionada = null;
         RequestContext.getCurrentInstance().execute("PF('datosVacacionesPEmpleado').unselectAllRows()");
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
      }
   }

   public void guardarGeneral() {
      FacesContext c = FacesContext.getCurrentInstance();
      RequestContext context = RequestContext.getCurrentInstance();
      if (banderaPendientes == 1) {
         vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDP");
         vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
         altoTabla1 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaPendientes = 0;
         filtrarListVacaPendientes = null;
         tipoListaPendientes = 0;
      }
      if (banderaDisfrutadas == 1) {
         vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
         vacacionesDPD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
         altoTabla2 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaDisfrutadas = 0;
         filtrarListVacaDisfrutadas = null;
         tipoListaDisfrutadas = 0;
      }
      guardarCambiosVPendientes();
      guardarCambiosVDisfrutadas();
      vacaPendienteSeleccionada = null;
      vacaDisfrutadaSeleccionada = null;
      recargarListas();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
      RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
      RequestContext.getCurrentInstance().update("form:totalDiasP");
   }

   public void guardarCambiosVPendientes() {
      int ms = 0;
      if (!listBorrarTablaPendientes.isEmpty()) {
         for (int i = 0; i < listBorrarTablaPendientes.size(); i++) {
            administrarVWVacaPendientesEmpleados.borrarVacaPendiente(listBorrarTablaPendientes.get(i));
         }
         ms++;
      }
      if (!listCrearTablaPendientes.isEmpty()) {
         for (int i = 0; i < listCrearTablaPendientes.size(); i++) {
            administrarVWVacaPendientesEmpleados.crearVacaPendiente(listCrearTablaPendientes.get(i));
         }
         ms++;
      }
      if (!listModificacionesTablaPendientes.isEmpty()) {
         for (int i = 0; i < listModificacionesTablaPendientes.size(); i++) {
            administrarVWVacaPendientesEmpleados.editarVacaPendiente(listModificacionesTablaPendientes.get(i));
         }
         ms++;
      }
      listBorrarTablaPendientes.clear();
      listCrearTablaPendientes.clear();
      listModificacionesTablaPendientes.clear();
      tipoActualizacion = -1;
      contarRegistrosP();
      contarRegistrosD();
      if (ms > 0) {
         FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Otros Certificados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
      RequestContext.getCurrentInstance().update("form:informacionRegistroD");
   }

   public void guardarCambiosVDisfrutadas() {
      int ms = 0;
      if (!listBorrarTablaDisfrutadas.isEmpty()) {
         for (int i = 0; i < listBorrarTablaDisfrutadas.size(); i++) {
            administrarVWVacaPendientesEmpleados.borrarVacaPendiente(listBorrarTablaDisfrutadas.get(i));
         }
         ms++;
      }
      if (!listCrearTablaDisfrutadas.isEmpty()) {
         for (int i = 0; i < listCrearTablaDisfrutadas.size(); i++) {
            administrarVWVacaPendientesEmpleados.crearVacaPendiente(listCrearTablaDisfrutadas.get(i));
         }
         ms++;
      }
      if (!listModificacionesTablaDisfrutadas.isEmpty()) {
         for (int i = 0; i < listModificacionesTablaDisfrutadas.size(); i++) {
            administrarVWVacaPendientesEmpleados.editarVacaPendiente(listModificacionesTablaDisfrutadas.get(i));
         }
         ms++;
      }
      tipoActualizacion = -1;
      contarRegistrosD();
      contarRegistrosP();

      if (ms > 0) {
         FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Otros Certificados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistroD");
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (tipoListaDisfrutadas == 1) {
         vacacionesDPD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesDPD");
         vacacionesDPD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicialD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaInicialD");
         vacacionesFechaInicialD.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinalD = (Column) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado:vacacionesFechaFinalD");
         vacacionesFechaFinalD.setFilterStyle("display: none; visibility: hidden;");
         altoTabla2 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesDEmpleado");
         banderaDisfrutadas = 0;
         filtrarListVacaDisfrutadas = null;
         tipoListaDisfrutadas = 0;
      }
      if (tipoListaPendientes == 1) {
         vacacionesDP = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesDP");
         vacacionesDP.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaFinal");
         vacacionesFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         vacacionesFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado:vacacionesFechaInicial");
         vacacionesFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         altoTabla1 = "275";
         RequestContext.getCurrentInstance().update("form:datosVacacionesPEmpleado");
         banderaPendientes = 0;
         filtrarListVacaPendientes = null;
         tipoListaPendientes = 0;
      }
      listBorrarTablaDisfrutadas.clear();
      listBorrarTablaPendientes.clear();
      listCrearTablaDisfrutadas.clear();
      listCrearTablaPendientes.clear();
      listModificacionesTablaDisfrutadas.clear();
      listModificacionesTablaPendientes.clear();
      listVacaDisfrutadas = null;
      listVacaPendientes = null;
      casillaDisfrutada = -1;
      casillaPendiente = -1;
      vacaDisfrutadaSeleccionada = null;
      vacaPendienteSeleccionada = null;
      tipoTabla = -1;
      regVacaAuxiliar = null;
      tipoActualizacion = -1;
      guardado = true;

      recargarListas();
      listBorrarTablaDisfrutadas.clear();
      listBorrarTablaPendientes.clear();
      listCrearTablaDisfrutadas.clear();
      listCrearTablaPendientes.clear();
      listModificacionesTablaDisfrutadas.clear();
      listModificacionesTablaPendientes.clear();
      listVacaDisfrutadas = null;
      listVacaPendientes = null;
      casillaDisfrutada = -1;
      casillaPendiente = -1;
      vacaDisfrutadaSeleccionada = null;
      vacaPendienteSeleccionada = null;
      tipoTabla = -1;
      tipoActualizacion = -1;
      totalDiasPendientes = BigInteger.valueOf(0);
      diasProvisionados = BigDecimal.valueOf(0);
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vacaDisfrutadaSeleccionada == null && vacaPendienteSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (tipoTabla == 1) {
            editarVacacion = vacaPendienteSeleccionada;
            if (casillaPendiente == 1) {
               //Dialogo Dias Pendientes
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasPendientesP");
               RequestContext.getCurrentInstance().execute("PF('editarDiasPendientesP').show()");
            }
            if (casillaPendiente == 2) {
               //Dialogo Fecha Inicial
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialP').show()");
            }
            if (casillaPendiente == 3) {
               //Dialogo Fecha Final
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalP').show()");
            }
         }
         if (tipoTabla == 2) {
            editarVacacion = vacaDisfrutadaSeleccionada;
            if (casillaDisfrutada == 1) {
               //Dialogo Dias Pendientes
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasPendientesD");
               RequestContext.getCurrentInstance().execute("PF('editarDiasPendientesD').show()");
            }
            if (casillaDisfrutada == 2) {
               //Dialogo Fecha Inicial
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
            }
            if (casillaDisfrutada == 3) {
               //Dialogo Fecha Final
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
            }
         }
      }
   }

   public String exportXML() {
      if (vacaPendienteSeleccionada != null) {
         nombreTabla = ":formExportarVP:datosVPEmpleadoExportar";
         nombreXML = "VacacionesPendientesXML";
      }
      if (vacaDisfrutadaSeleccionada != null) {
         nombreTabla = ":formExportarVD:datosVDEmpleadoExportar";
         nombreXML = "VacacionesDisfrutadasXML";
      }
      return nombreTabla;
   }

   public void validarExportPDF() throws IOException {
      if (vacaPendienteSeleccionada != null) {
         exportPDFVP();
      }
      if (vacaDisfrutadaSeleccionada != null) {
         exportPDFVD();
      }
   }

   public void exportPDFVP() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVP:datosVPEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VacacionesPendientesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDFVD() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVD:datosVDEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VacacionesDisfrutadasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarExportXLS() throws IOException {
      if (vacaDisfrutadaSeleccionada != null) {
         exportXLSVP();
      }
      if (vacaPendienteSeleccionada != null) {
         exportXLSVD();
      }
   }

   public void exportXLSVP() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVP:datosVPEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VacacionesPendientesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLSVD() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVD:datosVDEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VacacionesDisfrutadasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrarP() {
      if (tipoListaPendientes == 0) {
         tipoListaPendientes = 1;
      }
      vacaPendienteSeleccionada = null;
      contarRegistrosP();
   }

   public void eventoFiltrarD() {
      if (tipoListaDisfrutadas == 0) {
         tipoListaDisfrutadas = 1;
      }
      vacaDisfrutadaSeleccionada = null;
      contarRegistrosD();
   }

   public void contarRegistrosP() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroP");
   }

   public void contarRegistrosD() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroD");
   }

   public void recargarListas() {
      try {
         listVacaPendientes = new ArrayList<VWVacaPendientesEmpleados>();
         listVacaPendientes = administrarVWVacaPendientesEmpleados.vacaPendientesPendientes(empleado);
         listVacaDisfrutadas = new ArrayList<VWVacaPendientesEmpleados>();
         listVacaDisfrutadas = administrarVWVacaPendientesEmpleados.vacaPendientesDisfrutadas(empleado);
         BigInteger totalDias = BigInteger.valueOf(0);
         if (listVacaPendientes != null) {
            for (int i = 0; i < listVacaPendientes.size(); i++) {
               totalDias.add(listVacaPendientes.get(i).getDiaspendientes());
            }
         }
         setTotalDiasPendientes(totalDias);
      } catch (Exception e) {
         System.out.println("Error en recargarListas : " + e.toString());
      }
   }

   public void recordarSeleccionD() {
      if (vacaDisfrutadaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVacacionesDEmpleado");
         tablaC.setSelection(vacaDisfrutadaSeleccionada);
      }
   }

   public void recordarSeleccionP() {
      if (vacaPendienteSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVacacionesPEmpleado");
         tablaC.setSelection(vacaPendienteSeleccionada);
      }
   }

   public List<VWVacaPendientesEmpleados> getFiltrarListVacaPendientes() {
      return filtrarListVacaPendientes;
   }

   public void setFiltrarListVacaPendientes(List<VWVacaPendientesEmpleados> filtrarListVacaPendientes) {
      this.filtrarListVacaPendientes = filtrarListVacaPendientes;
   }

   public List<VWVacaPendientesEmpleados> getFiltrarListVacaDisfrutadas() {
      return filtrarListVacaDisfrutadas;
   }

   public void setFiltrarListVacaDisfrutadas(List<VWVacaPendientesEmpleados> filtrarListVacaDisfrutadas) {
      this.filtrarListVacaDisfrutadas = filtrarListVacaDisfrutadas;
   }

   public VWVacaPendientesEmpleados getEditarVacacion() {
      return editarVacacion;
   }

   public void setEditarVacacion(VWVacaPendientesEmpleados editarVacaPendiente) {
      this.editarVacacion = editarVacaPendiente;
   }

   public List<VWVacaPendientesEmpleados> getListModificacionesTablaPendientes() {
      return listModificacionesTablaPendientes;
   }

   public void setListModificacionesTablaPendientes(List<VWVacaPendientesEmpleados> listModificacionesTablaPendientes) {
      this.listModificacionesTablaPendientes = listModificacionesTablaPendientes;
   }

   public List<VWVacaPendientesEmpleados> getListModificacionesTablaDisfrutadas() {
      return listModificacionesTablaDisfrutadas;
   }

   public void setListModificacionesTablaDisfrutadas(List<VWVacaPendientesEmpleados> listModificacionesTablaDisfrutadas) {
      this.listModificacionesTablaDisfrutadas = listModificacionesTablaDisfrutadas;
   }

   public List<VWVacaPendientesEmpleados> getListCrearTablaPendientes() {
      return listCrearTablaPendientes;
   }

   public void setListCrearTablaPendientes(List<VWVacaPendientesEmpleados> listCrearTablaPendientes) {
      this.listCrearTablaPendientes = listCrearTablaPendientes;
   }

   public List<VWVacaPendientesEmpleados> getListCrearTablaDisfrutadas() {
      return listCrearTablaDisfrutadas;
   }

   public void setListCrearTablaDisfrutadas(List<VWVacaPendientesEmpleados> listCrearTablaDisfrutadas) {
      this.listCrearTablaDisfrutadas = listCrearTablaDisfrutadas;
   }

   public List<VWVacaPendientesEmpleados> getListBorrarTablaPendientes() {
      return listBorrarTablaPendientes;
   }

   public void setListBorrarTablaPendientes(List<VWVacaPendientesEmpleados> listBorrarTablaPendientes) {
      this.listBorrarTablaPendientes = listBorrarTablaPendientes;
   }

   public List<VWVacaPendientesEmpleados> getListBorrarTablaDisfrutadas() {
      return listBorrarTablaDisfrutadas;
   }

   public void setListBorrarTablaDisfrutadas(List<VWVacaPendientesEmpleados> listBorrarTablaDisfrutadas) {
      this.listBorrarTablaDisfrutadas = listBorrarTablaDisfrutadas;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public BigInteger getTotalDiasPendientes() {
      if (totalDiasPendientes.compareTo(BigInteger.valueOf(0)) == 0) {
         totalDiasPendientes = BigInteger.valueOf(0);
         if (listVacaPendientes != null) {
            for (int i = 0; i < listVacaPendientes.size(); i++) {
               totalDiasPendientes = totalDiasPendientes.add(listVacaPendientes.get(i).getDiaspendientes());
            }
         }
      }
      return totalDiasPendientes;
   }

   public void setTotalDiasPendientes(BigInteger totalDiasPendientes) {
      this.totalDiasPendientes = totalDiasPendientes;
   }

   public List<VWVacaPendientesEmpleados> getListVacaPendientes() {
      try {
         if (listVacaPendientes == null) {
            if (empleado.getSecuencia() != null) {
               listVacaPendientes = administrarVWVacaPendientesEmpleados.vacaPendientesPendientes(empleado);
            }
         }
         return listVacaPendientes;
      } catch (Exception e) {
         System.out.println("Error getListVacaPendientes : " + e.toString());
         return null;
      }
   }

   public void setListVacaPendientes(List<VWVacaPendientesEmpleados> listVacaPendientes) {
      this.listVacaPendientes = listVacaPendientes;
   }

   public List<VWVacaPendientesEmpleados> getListVacaDisfrutadas() {
      try {
         if (empleado != null) {
            if (listVacaDisfrutadas == null) {
               listVacaDisfrutadas = administrarVWVacaPendientesEmpleados.vacaPendientesDisfrutadas(empleado);
            }
         }
         return listVacaDisfrutadas;
      } catch (Exception e) {
         System.out.println("Error getListVacaDisfrutadas : " + e.toString());
         return null;
      }
   }

   public void setListVacaDisfrutadas(List<VWVacaPendientesEmpleados> listVacaDisfrutadas) {
      this.listVacaDisfrutadas = listVacaDisfrutadas;
   }

   public VWVacaPendientesEmpleados getNuevaVacacion() {
      return nuevaVacacion;
   }

   public void setNuevaVacacion(VWVacaPendientesEmpleados nuevaVacacion) {
      this.nuevaVacacion = nuevaVacacion;
   }

   public VWVacaPendientesEmpleados getDuplicarVacacion() {
      return duplicarVacacion;
   }

   public void setDuplicarVacacion(VWVacaPendientesEmpleados duplicarVacacion) {
      this.duplicarVacacion = duplicarVacacion;
   }

   public BigDecimal getDiasProvisionados() {
      try {
         if (empleado != null) {
            diasProvisionados = administrarVWVacaPendientesEmpleados.diasProvisionadosEmpleado(empleado);
         }
         return diasProvisionados;
      } catch (Exception e) {
         System.out.println("Error getDiasProvisionados : " + e.toString());
         return null;
      }
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setDiasProvisionados(BigDecimal diasProvisionados) {
      this.diasProvisionados = diasProvisionados;
   }

   public VWVacaPendientesEmpleados getVacaPendienteSeleccionada() {
      return vacaPendienteSeleccionada;
   }

   public void setVacaPendienteSeleccionada(VWVacaPendientesEmpleados vacaPendienteSeleccionada) {
      this.vacaPendienteSeleccionada = vacaPendienteSeleccionada;
   }

   public VWVacaPendientesEmpleados getVacaDisfrutadaSeleccionada() {
      return vacaDisfrutadaSeleccionada;
   }

   public void setVacaDisfrutadaSeleccionada(VWVacaPendientesEmpleados vacaDisfrutadaSeleccionada) {
      this.vacaDisfrutadaSeleccionada = vacaDisfrutadaSeleccionada;
   }

   public String getAltoTabla1() {
      return altoTabla1;
   }

   public String getAltoTabla2() {
      return altoTabla2;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public Date getFechaContratacion() {
      return fechaContratacion;
   }

   public void setFechaContratacion(Date fechaContratacion) {
      this.fechaContratacion = fechaContratacion;
   }

   public String getFechaContratacionText() {
      return fechaContratacionText;
   }

   public void setFechaContratacionText(String fechaContratacionText) {
      this.fechaContratacionText = fechaContratacionText;
   }

   public String getInfoRegistroD() {
      return infoRegistroD;
   }

   public String getInfoRegistroP() {
      return infoRegistroP;
   }
}
