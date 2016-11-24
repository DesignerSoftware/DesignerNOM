/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.*;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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
public class ControlNovedadesConceptos implements Serializable {

   @EJB
   AdministrarNovedadesConceptosInterface administrarNovedadesConceptos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarFormulaConceptoInterface administrarFormulaConcepto;
   //LISTA NOVEDADES
   private List<Novedades> listaNovedades;
   private List<Novedades> filtradosListaNovedades;
   private Novedades novedadSeleccionada;
   private Novedades novedadBackup;
   //LISTA QUE NO ES LISTA - 1 SOLO ELEMENTO
   private List<Conceptos> listaConceptosNovedad;
   private List<Conceptos> filtradosListaConceptosNovedad;
   private Conceptos conceptoSeleccionado; //Seleccion Mostrar
   //editar celda
   private Novedades editarNovedades;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<Novedades> listaNovedadesCrear;
   public Novedades nuevaNovedad;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<Novedades> listaNovedadesModificar;
   //Borrar Novedades
   private List<Novedades> listaNovedadesBorrar;
   //Autocompletar
   private String codigoEmpleado, nitTercero, formula, descripcionConcepto, descripcionPeriodicidad, nombreTercero;
   private Date FechaFinal;
   // private Short CodigoPeriodicidad;
   private String CodigoPeriodicidad;
   private BigDecimal Saldo;
   private Integer HoraDia, MinutoHora;
   //L.O.V Conceptos
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoslistaConceptos;
   private Conceptos conceptoSeleccionadoLov;
   //L.O.V Empleados
   private List<Empleados> lovEmpleados;
   private List<Empleados> filtradoslistaEmpleados;
   private Empleados empleadoSeleccionadoLov;
   //L.O.V PERIODICIDADES
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtradoslistaPeriodicidades;
   private Periodicidades periodicidadSeleccionada;
   //L.O.V TERCEROS
   private List<Terceros> lovTerceros;
   private List<Terceros> filtradoslistaTerceros;
   private Terceros terceroSeleccionado;
   //L.O.V FORMULAS
   private List<Formulas> lovFormulas;
   private List<Formulas> filtradoslistaFormulas;
   private Formulas formulaSeleccionadaLov;
   //Columnas Tabla NOVEDADES
   private Column nCEmpleadoCodigo, nCEmpleadoNombre, nCFechasInicial, nCFechasFinal,
           nCValor, nCSaldo, nCPeriodicidadCodigo, nCDescripcionPeriodicidad, nCTercerosNit,
           nCTercerosNombre, nCFormulas, nCHorasDias, nCMinutosHoras, nCTipo;
   //Duplicar
   private Novedades duplicarNovedad;
   //USUARIO
   private String alias;
   private Usuarios usuarioBD;
   //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
   private int resultado;
   private boolean todas;
   private boolean actuales;
   private String altoTabla;
   private String infoRegistroConceptos;
   private String infoRegistroNovedades;
   private String infoRegistroLovConceptos;
   private String infoRegistroLovFormulas;
   private String infoRegistroLovEmpleados;
   private String infoRegistroLovPeriodicidades;
   private String infoRegistroLovTerceros;
   private BigDecimal valor;
   private boolean activarLOV;
   public String paginaAnterior;

   public ControlNovedadesConceptos() {
      permitirIndex = true;
      listaNovedades = new ArrayList<Novedades>();
      lovEmpleados = null;
      lovFormulas = null;
      lovEmpleados = null;
      lovPeriodicidades = null;
      lovConceptos = null;
      listaConceptosNovedad = null;
      todas = false;
      actuales = true;
      novedadSeleccionada = null;
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      listaNovedadesBorrar = new ArrayList<Novedades>();
      listaNovedadesCrear = new ArrayList<Novedades>();
      listaNovedadesModificar = new ArrayList<Novedades>();
      //Crear VC
      nuevaNovedad = new Novedades();
      //nuevaNovedad.setFechafinal(new Date());
      nuevaNovedad.setFormula(new Formulas());
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.setFechareporte(new Date());
      nuevaNovedad.setTipo("FIJA");
      altoTabla = "140";
      valor = new BigDecimal(Integer.toString(0));
      nuevaNovedad.setValortotal(valor);
      infoRegistroConceptos = "0";
      infoRegistroNovedades = "0";
      infoRegistroLovConceptos = "0";
      infoRegistroLovFormulas = "0";
      infoRegistroLovEmpleados = "0";
      infoRegistroLovPeriodicidades = "0";
      infoRegistroLovTerceros = "0";
      activarLOV = true;
      paginaAnterior = "";
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNovedadesConceptos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarFormulaConcepto.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPagina(String pagina) {
      paginaAnterior = pagina;
      getListaConceptosNovedad();
      if (listaConceptosNovedad != null) {
         conceptoSeleccionado = listaConceptosNovedad.get(0);
      }
      if (conceptoSeleccionado != null) {
         listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
      }
      novedadSeleccionada = null;
   }

   public String retornarPagina() {
      anularBotonLOV();
      return paginaAnterior;
   }

   //Ubicacion Celda Arriba 
   public void cambiarConcepto() {
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      //{
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         listaNovedades.clear();
         System.out.println("Concepto Seleccionado : " + conceptoSeleccionado.getDescripcion());
         llenarTablaNovedades();
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
         //}
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
   }

   public void limpiarListas() {
      listaNovedadesCrear.clear();
      listaNovedadesBorrar.clear();
      listaNovedadesModificar.clear();
      guardado = true;
      listaNovedades.clear();
      anularBotonLOV();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarCambioEmpleados() {
      filtradoslistaEmpleados = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void cancelarCambioPeriodicidades() {
      filtradoslistaPeriodicidades = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodicidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void cancelarCambioFormulas() {
      filtradoslistaFormulas = null;
      formulaSeleccionadaLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVFormulas");
      RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarF");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
   }

   public void cancelarCambioConceptos() {
      filtradoslistaConceptos = null;
      conceptoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioTerceros() {
      filtradoslistaTerceros = null;
      terceroSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
   }

   public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
      anularBotonLOV();
      if (tipoNuevo == 1) {
         if (tipo.equals("FIJA")) {
            nuevaNovedad.setTipo("FIJA");
         } else if (tipo.equals("OCASIONAL")) {
            nuevaNovedad.setTipo("OCASIONAL");
         } else if (tipo.equals("PAGO POR FUERA")) {
            nuevaNovedad.setTipo("PAGO POR FUERA");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
      } else {
         if (tipo.equals("FIJA")) {
            duplicarNovedad.setTipo("FIJA");
         } else if (tipo.equals("OCASIONAL")) {
            duplicarNovedad.setTipo("OCASIONAL");
         } else if (tipo.equals("PAGO POR FUERA")) {
            duplicarNovedad.setTipo("PAGO POR FUERA");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
      }
   }

   public void seleccionarTipo(String estadoTipo, int indice, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();
      anularBotonLOV();
      if (estadoTipo.equals("FIJA")) {
         novedadSeleccionada.setTipo("FIJA");
      } else if (estadoTipo.equals("OCASIONAL")) {
         novedadSeleccionada.setTipo("OCASIONAL");
      } else if (estadoTipo.equals("PAGO POR FUERA")) {
         novedadSeleccionada.setTipo("PAGO POR FUERA");
      }

      if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
         if (listaNovedadesModificar.isEmpty()) {
            listaNovedadesModificar.add(novedadSeleccionada);
         } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
            listaNovedadesModificar.add(novedadSeleccionada);
         }
      }
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
   }

   public void asignarIndex(Novedades novedad, int dlg, int LND) {

      novedadSeleccionada = novedad;
      tipoActualizacion = LND;
      if (dlg == 0) {
         contarRegistrosEmpleados();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (dlg == 2) {
         contarRegistrosLovFormulas();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (dlg == 3) {
         contarRegistrosPeriodicidades();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (dlg == 4) {
         contarRegistrosTerceros();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      } else {
         anularBotonLOV();
      }
   }

   public void asignarIndex(int dlg, int LND) {
      anularBotonLOV();
      tipoActualizacion = LND;
      if (dlg == 0) {
         contarRegistrosEmpleados();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosLovConceptos();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (dlg == 2) {
         contarRegistrosLovFormulas();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (dlg == 3) {
         contarRegistrosPeriodicidades();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (dlg == 4) {
         contarRegistrosTerceros();
         activarBotonLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
   }

   public void mostrarTodos() {

      if (!listaConceptosNovedad.isEmpty()) {
         listaConceptosNovedad.clear();
      }
      //listaEmpleadosNovedad = listaValEmpleados;
      if (listaConceptosNovedad != null) {
         for (int i = 0; i < lovConceptos.size(); i++) {
            listaConceptosNovedad.add(lovConceptos.get(i));
         }
      }
      conceptoSeleccionado = listaConceptosNovedad.get(0);
      llenarTablaNovedades();
      contarRegistrosConceptos();
      contarRegistrosNovedades();
      anularBotonLOV();
      RequestContext.getCurrentInstance().update("form:datosConceptos");

      listaNovedades.clear();
      filtradosListaConceptosNovedad = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void actualizarConceptosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      Conceptos c = conceptoSeleccionadoLov;

      if (!listaConceptosNovedad.isEmpty()) {
         listaConceptosNovedad.clear();
         listaConceptosNovedad.add(c);
         conceptoSeleccionado = listaConceptosNovedad.get(0);
      } else {
         listaConceptosNovedad.add(c);
      }
      empleadoSeleccionadoLov = null;
      llenarTablaNovedades();
      contarRegistrosConceptos();
      filtradosListaConceptosNovedad = null;
      aceptar = true;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      RequestContext.getCurrentInstance().update("form:datosConceptos");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   //AUTOCOMPLETAR
   public void modificarNovedades(Novedades novedad, String confirmarCambio, String valorConfirmar) {

      novedadSeleccionada = novedad;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (novedadSeleccionada.getFechafinal().compareTo(novedadSeleccionada.getFechainicial()) < 0) {
         System.out.println("La fecha Final es Menor que la Inicial");
         RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
         RequestContext.getCurrentInstance().execute("PF('fechas').show()");
         novedadSeleccionada.setFechainicial(novedadBackup.getFechainicial());
         novedadSeleccionada.setFechafinal(novedadBackup.getFechafinal());
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      }

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {

            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         novedadSeleccionada.getFormula().setNombresFormula(formula);
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombresFormula().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
            lovFormulas.clear();
            getLovFormulas();
         } else {
            permitirIndex = false;
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         novedadSeleccionada.getTercero().setNitalternativo(nitTercero);

         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));
            lovTerceros.clear();
            getLovTerceros();
         } else {
            permitirIndex = false;
            contarRegistrosTerceros();
            RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {

         novedadSeleccionada.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);

         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toString().toUpperCase())) {

               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
            lovEmpleados.clear();
            getLovEmpleados();
         } else {
            permitirIndex = false;
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {

         novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);

         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if ((lovPeriodicidades.get(i).getCodigoStr()).startsWith(valorConfirmar.toString().toUpperCase())) {

               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            permitirIndex = false;
            contarRegistrosPeriodicidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
   }

   public void actualizarEmpleados() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setEmpleado(empleadoSeleccionadoLov);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setEmpleado(empleadoSeleccionadoLov);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setEmpleado(empleadoSeleccionadoLov);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaEmpleados = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void actualizarFormulas() {
      // verificarFormulaConcepto(seleccionConceptos.getSecuencia());
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setFormula(formulaSeleccionadaLov);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setFormula(formulaSeleccionadaLov);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setFormula(formulaSeleccionadaLov);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaFormulas = null;
      formulaSeleccionadaLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVFormulas");
      RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarF");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
   }

   public void actualizarPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setPeriodicidad(periodicidadSeleccionada);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setPeriodicidad(periodicidadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setPeriodicidad(periodicidadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaPeriodicidades = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodicidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void actualizarTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setTercero(terceroSeleccionado);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setTercero(terceroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setTercero(terceroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaTerceros = null;
      terceroSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
   }

   //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
   public void cambiarIndice(Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
//        if (permitirIndex == true) {
      cualCelda = celda;
      novedadBackup = novedadSeleccionada;
      if (cualCelda == 0) {
         activarBotonLOV();
         codigoEmpleado = novedadSeleccionada.getEmpleado().getCodigoempleadoSTR();
      } else if (cualCelda == 1) {
         activarBotonLOV();
         descripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
      } else if (cualCelda == 3) {
         anularBotonLOV();
         FechaFinal = novedadSeleccionada.getFechafinal();
      } else if (cualCelda == 5) {
         anularBotonLOV();
         Saldo = novedadSeleccionada.getSaldo();
      } else if (cualCelda == 6) {
         activarBotonLOV();
         CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
      } else if (cualCelda == 7) {
         activarBotonLOV();
         descripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
      } else if (cualCelda == 8) {
         activarBotonLOV();
         nitTercero = novedadSeleccionada.getTercero().getNitalternativo();
      } else if (cualCelda == 9) {
         activarBotonLOV();
         nombreTercero = novedadSeleccionada.getTercero().getNombre();
      } else if (cualCelda == 10) {
         activarBotonLOV();
         formula = novedadSeleccionada.getFormula().getNombrelargo();
      } else if (cualCelda == 11) {
         anularBotonLOV();
         HoraDia = novedadSeleccionada.getUnidadesparteentera();
         MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
      } else {
         anularBotonLOV();
      }
//        }
   }

   //GUARDAR
   public void guardarCambiosNovedades() {
      int pasas = 0;
      if (guardado == false) {
         getResultado();
         if (resultado > 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:solucionesFormulas");
            RequestContext.getCurrentInstance().execute("PF('solucionesFormulas').show()");
            listaNovedadesBorrar.clear();
         }
         if (!listaNovedadesBorrar.isEmpty() && pasas == 0) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesBorrar.get(i).setPeriodicidad(null);
               }
               if (listaNovedadesBorrar.get(i).getTercero().getSecuencia() == null) {
                  listaNovedadesBorrar.get(i).setTercero(null);
               }
               if (listaNovedadesBorrar.get(i).getSaldo() == null) {
                  listaNovedadesBorrar.get(i).setSaldo(null);
               }
               if (listaNovedadesBorrar.get(i).getUnidadesparteentera() == null) {
                  listaNovedadesBorrar.get(i).setUnidadesparteentera(null);
               }
               if (listaNovedadesBorrar.get(i).getUnidadespartefraccion() == null) {
                  listaNovedadesBorrar.get(i).setUnidadespartefraccion(null);
               }
               administrarNovedadesConceptos.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            listaNovedadesBorrar.clear();
         }
         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesCrear.get(i).setPeriodicidad(null);
               }
               if (listaNovedadesCrear.get(i).getTercero().getSecuencia() == null) {
                  listaNovedadesCrear.get(i).setTercero(null);
               }
               if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                  listaNovedadesCrear.get(i).setPeriodicidad(null);
               }
               if (listaNovedadesCrear.get(i).getSaldo() == null) {
                  listaNovedadesCrear.get(i).setSaldo(null);
               }
               if (listaNovedadesCrear.get(i).getUnidadesparteentera() == null) {
                  listaNovedadesCrear.get(i).setUnidadesparteentera(null);
               }
               if (listaNovedadesCrear.get(i).getUnidadespartefraccion() == null) {
                  listaNovedadesCrear.get(i).setUnidadespartefraccion(null);
               }
               administrarNovedadesConceptos.crearNovedades(listaNovedadesCrear.get(i));
            }
            listaNovedadesCrear.clear();
         }
         if (!listaNovedadesModificar.isEmpty()) {
            administrarNovedadesConceptos.modificarNovedades(listaNovedadesModificar);
            listaNovedadesModificar.clear();
         }
         listaNovedades.clear();
         RequestContext context = RequestContext.getCurrentInstance();
         llenarTablaNovedades();
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadConcepto() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaNovedad.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         System.out.println("Fecha Inicial");
         pasa++;
      }

      if (nuevaNovedad.getEmpleado().getCodigoempleado().equals(BigInteger.valueOf(0))) {
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         System.out.println("Empleado");
         pasa++;
      }

      if (nuevaNovedad.getFormula().getNombrelargo().equals("")) {
         mensajeValidacion = mensajeValidacion + " * Formula\n";
         System.out.println("Formula");
         pasa++;
      }
      if (nuevaNovedad.getValortotal() == null) {
         mensajeValidacion = mensajeValidacion + " * Valor\n";
         System.out.println("Valor");
         pasa++;
      }

      if (nuevaNovedad.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         System.out.println("Tipo");
         pasa++;
      }

      if (nuevaNovedad.getEmpleado() != null && pasa == 0) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (nuevaNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {

               if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getEmpleado().getFechacreacion()) < 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
                  RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
                  System.out.println("Inconsistencia Empleado");
                  pasa2++;
               }
            }
         }
      }

      if (nuevaNovedad.getFechafinal() != null && nuevaNovedad.getFechainicial() != null) {
         if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
            RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            System.out.println("Dialogo de Fechas culas");
            pasa2++;
         }
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadConcepto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadConcepto').show()");
      }

      if (pasa == 0 && pasa2 == 0) {
         System.out.println("Todo esta Bien");
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevaNovedad.setSecuencia(l);

         // OBTENER EL TERMINAL 
         HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
         String equipo = null;
         java.net.InetAddress localMachine = null;
         if (request.getRemoteAddr().startsWith("127.0.0.1")) {
            try {
               localMachine = java.net.InetAddress.getLocalHost();

            } catch (UnknownHostException ex) {
               Logger.getLogger(ControlNovedadesConceptos.class
                       .getName()).log(Level.SEVERE, null, ex);
            }
            equipo = localMachine.getHostAddress();
         } else {
            equipo = request.getRemoteAddr();
         }
         try {
            localMachine = java.net.InetAddress.getByName(equipo);

         } catch (UnknownHostException ex) {
            Logger.getLogger(ControlNovedadesConceptos.class
                    .getName()).log(Level.SEVERE, null, ex);
         }

         getAlias();
         getUsuarioBD();
         nuevaNovedad.setConcepto(conceptoSeleccionado);
         nuevaNovedad.setTerminal(localMachine.getHostName());
         nuevaNovedad.setUsuarioreporta(usuarioBD);
         listaNovedadesCrear.add(nuevaNovedad);
         listaNovedades.add(nuevaNovedad);
         novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
         contarRegistrosNovedades();

         nuevaNovedad = new Novedades();
         nuevaNovedad.setFormula(new Formulas());
         nuevaNovedad.setTercero(new Terceros());
         nuevaNovedad.setFechareporte(new Date());
         nuevaNovedad.setPeriodicidad(new Periodicidades());
         nuevaNovedad.setTipo("FIJA");

         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevaNovedadConcepto').hide()");
      } else {
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadosCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosNombres");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadosNombres').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValores");
            RequestContext.getCurrentInstance().execute("PF('editarValores').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldos");
            RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesCodigos').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesDescripciones').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNit");
            RequestContext.getCurrentInstance().execute("PF('editarTercerosNit').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNombres");
            RequestContext.getCurrentInstance().execute("PF('editarTercerosNombres').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
            RequestContext.getCurrentInstance().execute("PF('editarFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasDias");
            RequestContext.getCurrentInstance().execute("PF('editarHorasDias').show()");
            cualCelda = -1;
         } else if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMinutosHoras");
            RequestContext.getCurrentInstance().execute("PF('editarMinutosHoras').show()");
            cualCelda = -1;
         } else if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
            RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (novedadSeleccionada != null) {
         if (cualCelda == 0) {
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 6) {
            contarRegistrosPeriodicidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 8) {
            contarRegistrosTerceros();
            RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 10) {
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
         nCEmpleadoCodigo.setFilterStyle("width: 85% !important;");
         nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
         nCEmpleadoNombre.setFilterStyle("width: 85% !important");
         nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
         nCFechasInicial.setFilterStyle("width: 85% !important;");
         nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
         nCFechasFinal.setFilterStyle("width: 85% !important;");
         nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
         nCValor.setFilterStyle("width: 85% !important;");
         nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
         nCSaldo.setFilterStyle("width: 85% !important;");
         nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
         nCPeriodicidadCodigo.setFilterStyle("width: 85% !important;");
         nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
         nCDescripcionPeriodicidad.setFilterStyle("width: 85% !important;");
         nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
         nCTercerosNit.setFilterStyle("width: 85% !important;");
         nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
         nCTercerosNombre.setFilterStyle("width: 85% !important;");
         nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
         nCFormulas.setFilterStyle("width: 85% !important;");
         nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
         nCHorasDias.setFilterStyle("width: 85% !important;");
         nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
         nCMinutosHoras.setFilterStyle("width: 85% !important;");
         nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
         nCTipo.setFilterStyle("width: 85% !important;");
         altoTabla = "120";

         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "NovedadesConceptosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesConceptosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO NOVEDAD
   public void limpiarNuevaNovedad() {
      nuevaNovedad = new Novedades();
      nuevaNovedad.setFormula(new Formulas());
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre(" ");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre(" ");
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion(" ");
      nuevaNovedad.setTipo("FIJA");
      nuevaNovedad.setUsuarioreporta(new Usuarios());
      nuevaNovedad.setTerminal(" ");
      nuevaNovedad.setFechareporte(new Date());
      resultado = 0;
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Encargaturas
    */
   public void limpiarduplicarNovedades() {
      duplicarNovedad = new Novedades();
      duplicarNovedad.setPeriodicidad(new Periodicidades());
      duplicarNovedad.getPeriodicidad().setNombre(" ");
      duplicarNovedad.setTercero(new Terceros());
      duplicarNovedad.getTercero().setNombre(" ");
      duplicarNovedad.setConcepto(new Conceptos());
      duplicarNovedad.getConcepto().setDescripcion(" ");
      duplicarNovedad.setTipo("FIJA");
      duplicarNovedad.setUsuarioreporta(new Usuarios());
      duplicarNovedad.setTerminal(" ");
      duplicarNovedad.setFechareporte(new Date());
   }

   //BORRAR NOVEDADES
   public void borrarNovedades() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
            listaNovedadesModificar.remove(novedadSeleccionada);
            listaNovedadesBorrar.add(novedadSeleccionada);
         } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
            listaNovedadesCrear.remove(novedadSeleccionada);
         } else {
            listaNovedadesBorrar.add(novedadSeleccionada);
         }
         listaNovedades.remove(novedadSeleccionada);
         if (tipoLista == 1) {
            filtradosListaNovedades.remove(novedadSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
         novedadSeleccionada = null;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         contarRegistrosNovedades();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //DUPLICAR ENCARGATURA
   public void duplicarCN() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         duplicarNovedad = new Novedades();

         duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
         duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
         duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
         duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
         duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
         duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
         duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
         duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
         duplicarNovedad.setTercero(novedadSeleccionada.getTercero());
         duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
         duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
         duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
         duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
         duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
         duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() throws UnknownHostException {

      int pasa2 = 0;
      int pasa = 0;
      Empleados emp = new Empleados();
      Empleados emp2 = new Empleados();
      RequestContext context = RequestContext.getCurrentInstance();

      if (duplicarNovedad.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarNovedad.getEmpleado().getPersona().getNombreCompleto().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      if (duplicarNovedad.getFormula().getNombrelargo() == null) {
         mensajeValidacion = mensajeValidacion + " * Formula\n";
         pasa++;
      }
      if (duplicarNovedad.getValortotal() == null) {
         mensajeValidacion = mensajeValidacion + " * Valor\n";
         pasa++;
      }

      if (duplicarNovedad.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " * Tipo\n";
         pasa++;
      }

      for (int i = 0; i < lovEmpleados.size(); i++) {
         if (duplicarNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {

            if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getEmpleado().getFechacreacion()) < 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
               RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
               pasa2++;
            }
         }
      }

      if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
         RequestContext.getCurrentInstance().execute("PF('fechas').show()");
         pasa2++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadConcepto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadConcepto').show()");
      }
      if (pasa2 == 0) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarNovedad.setSecuencia(l);

         listaNovedades.add(duplicarNovedad);
         listaNovedadesCrear.add(duplicarNovedad);
         novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
         contarRegistrosNovedades();

         RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            restaurarTabla();
         }

         // OBTENER EL TERMINAL 
         HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
         String equipo = null;
         java.net.InetAddress localMachine = null;
         if (request.getRemoteAddr().startsWith("127.0.0.1")) {
            localMachine = java.net.InetAddress.getLocalHost();
            equipo = localMachine.getHostAddress();
         } else {
            equipo = request.getRemoteAddr();
         }
         localMachine = java.net.InetAddress.getByName(equipo);
         getAlias();
         getUsuarioBD();
         duplicarNovedad.setTerminal(localMachine.getHostName());
         duplicarNovedad.setConcepto(conceptoSeleccionado);
         duplicarNovedad = new Novedades();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').hide()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("EMPLEADO")) {
         if (tipoNuevo == 1) {
            codigoEmpleado = nuevaNovedad.getEmpleado().getCodigoempleadoSTR();
         } else if (tipoNuevo == 2) {
            codigoEmpleado = duplicarNovedad.getEmpleado().getCodigoempleadoSTR();
         }
      } else if (Campo.equals("CODIGO")) {
         if (tipoNuevo == 1) {
            CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
         } else if (tipoNuevo == 2) {
            CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
         }
      } else if (Campo.equals("NIT")) {
         if (tipoNuevo == 1) {
            nitTercero = nuevaNovedad.getTercero().getNitalternativo();
         } else if (tipoNuevo == 2) {
            nitTercero = duplicarNovedad.getTercero().getNitalternativo();
         }
      } else if (Campo.equals("FORMULAS")) {
         if (tipoNuevo == 1) {
            formula = nuevaNovedad.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            formula = duplicarNovedad.getFormula().getNombrelargo();
         }
      }

   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevaNovedad.getFormula().setNombrelargo(formula);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getFormula().setNombrelargo(formula);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().update("form:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         if (tipoNuevo == 1) {
            nuevaNovedad.getTercero().setNitalternativo(nitTercero);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getTercero().setNitalternativo(nitTercero);
         }

         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
            }
            lovTerceros.clear();
            getLovTerceros();
         } else {
            contarRegistrosTerceros();
            RequestContext.getCurrentInstance().update("form:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");

            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
         if (tipoNuevo == 1) {
            nuevaNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         }
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
            }
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            contarRegistrosPeriodicidades();
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
         if (tipoNuevo == 1) {
            nuevaNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
         }

         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
            }
            lovEmpleados.clear();
            getLovEmpleados();
         } else {
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().update("form:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
            }
         }
      }
   }

   //RASTROS 
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//            } else {
//                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//            }
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      restaurarTabla();
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      if (!listaConceptosNovedad.isEmpty()) {
         conceptoSeleccionado = listaConceptosNovedad.get(0);
      }
      novedadSeleccionada = null;
//        k = 0;
      listaNovedades = new ArrayList<Novedades>();
      guardado = true;
      permitirIndex = true;
      resultado = 0;
      contarRegistrosNovedades();
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      RequestContext.getCurrentInstance().update("form:datosConceptos");
      mostrarTodos();
   }

   public void salir() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      listaNovedades.clear();
      lovConceptos = null;
      listaConceptosNovedad = null;
      lovEmpleados = null;
      lovFormulas = null;
      lovPeriodicidades = null;
      lovTerceros = null;
      tipoLista = 0;
      novedadSeleccionada = null;
//        k = 0;
      resultado = 0;
      guardado = true;
      permitirIndex = true;
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();

      nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
      nCEmpleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
      nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
      nCEmpleadoNombre.setFilterStyle("display: none; visibility: hidden;");
      nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
      nCFechasInicial.setFilterStyle("display: none; visibility: hidden;");
      nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
      nCFechasFinal.setFilterStyle("display: none; visibility: hidden;");
      nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
      nCValor.setFilterStyle("display: none; visibility: hidden;");
      nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
      nCSaldo.setFilterStyle("display: none; visibility: hidden;");
      nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
      nCPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
      nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
      nCDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
      nCTercerosNit.setFilterStyle("display: none; visibility: hidden;");
      nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
      nCTercerosNombre.setFilterStyle("display: none; visibility: hidden;");
      nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
      nCFormulas.setFilterStyle("display: none; visibility: hidden;");
      nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
      nCHorasDias.setFilterStyle("display: none; visibility: hidden;");
      nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
      nCMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
      nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
      nCTipo.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "140";

      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      bandera = 0;
      filtradosListaNovedades = null;
      tipoLista = 0;
   }

   public void todasNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesConceptos.todasNovedadesConcepto(conceptoSeleccionado.getSecuencia());
      todas = true;
      actuales = false;
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      contarRegistrosNovedades();
   }

   public void actualesNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
      todas = false;
      actuales = true;
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      contarRegistrosNovedades();
   }

   public void abrirDialogo() {
      Formulas formula;
      formula = verificarFormulaConcepto(conceptoSeleccionado.getSecuencia());
      nuevaNovedad.setFormula(formula);
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      RequestContext.getCurrentInstance().execute("PF('NuevaNovedadConcepto').show()");

   }

   //CARGAR FORMULA AUTOMATICAMENTE
   public Formulas verificarFormulaConcepto(BigInteger secCon) {
      List<FormulasConceptos> formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
      Formulas formulaR = new Formulas();
      BigInteger autoFormula;

      if (formulasConceptoSel != null) {
         if (!formulasConceptoSel.isEmpty()) {
            autoFormula = formulasConceptoSel.get(0).getFormula();
         } else {
            autoFormula = new BigInteger("4621544");
         }
      } else {
         autoFormula = new BigInteger("4621544");
      }

      for (int i = 0; i < lovFormulas.size(); i++) {
         if (autoFormula.equals(lovFormulas.get(i).getSecuencia())) {
            formulaR = lovFormulas.get(i);
         }
      }
      return formulaR;
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      anularBotonLOV();
      contarRegistrosNovedades();
      novedadSeleccionada = null;
   }

   // Conteo de registros : 
   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosNovedades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
   }

   public void contarRegistrosLovConceptos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConceptos");
   }

   public void contarRegistrosLovFormulas() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormulas");
   }

   public void contarRegistrosEmpleados() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
   }

   public void contarRegistrosPeriodicidades() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPeriodicidades");
   }

   public void contarRegistrosTerceros() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTerceros");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void llenarTablaNovedades() {
      if (conceptoSeleccionado != null) {
         listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
      }
      novedadSeleccionada = null;
      anularBotonLOV();
      contarRegistrosNovedades();
      RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
   }

   //GETTER & SETTER
   public List<Conceptos> getListaConceptosNovedad() {
      if (listaConceptosNovedad == null) {
         listaConceptosNovedad = administrarNovedadesConceptos.Conceptos();
      }
      return listaConceptosNovedad;
   }

   public void setListaConceptosNovedad(List<Conceptos> listaConceptosNovedad) {
      this.listaConceptosNovedad = listaConceptosNovedad;
   }

   public List<Conceptos> getFiltradosListaConceptosNovedad() {
      return filtradosListaConceptosNovedad;
   }

   public void setFiltradosListaConceptosNovedad(List<Conceptos> filtradosListaConceptosNovedad) {
      this.filtradosListaConceptosNovedad = filtradosListaConceptosNovedad;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos seleccionMostrar) {
      this.conceptoSeleccionado = seleccionMostrar;
   }

   public List<Novedades> getListaNovedades() {
      return listaNovedades;
   }

   public void setListaNovedades(List<Novedades> listaNovedades) {
      this.listaNovedades = listaNovedades;
   }

   public List<Novedades> getFiltradosListaNovedades() {
      return filtradosListaNovedades;
   }

   public void setFiltradosListaNovedades(List<Novedades> filtradosListaNovedades) {
      this.filtradosListaNovedades = filtradosListaNovedades;
   }

   public List<Novedades> getListaNovedadesCrear() {
      return listaNovedadesCrear;
   }

   public void setListaNovedadesCrear(List<Novedades> listaNovedadesCrear) {
      this.listaNovedadesCrear = listaNovedadesCrear;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public List<Novedades> getListaNovedadesModificar() {
      return listaNovedadesModificar;
   }

   public void setListaNovedadesModificar(List<Novedades> listaNovedadesModificar) {
      this.listaNovedadesModificar = listaNovedadesModificar;
   }

   public List<Novedades> getListaNovedadesBorrar() {
      return listaNovedadesBorrar;
   }

   public void setListaNovedadesBorrar(List<Novedades> listaNovedadesBorrar) {
      this.listaNovedadesBorrar = listaNovedadesBorrar;
   }

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarNovedadesConceptos.lovEmpleados();
      }
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> listaEmpleados) {
      this.lovEmpleados = listaEmpleados;
   }

   public List<Empleados> getFiltradoslistaEmpleados() {
      return filtradoslistaEmpleados;
   }

   public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
      this.filtradoslistaEmpleados = filtradoslistaEmpleados;
   }

   public Empleados getEmpleadoSeleccionadoLov() {
      return empleadoSeleccionadoLov;
   }

   public void setEmpleadoSeleccionadoLov(Empleados seleccionEmpleados) {
      this.empleadoSeleccionadoLov = seleccionEmpleados;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarNovedadesConceptos.lovPeriodicidades();
      }
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      this.lovPeriodicidades = listaPeriodicidades;
   }

   public List<Periodicidades> getFiltradoslistaPeriodicidades() {
      return filtradoslistaPeriodicidades;
   }

   public void setFiltradoslistaPeriodicidades(List<Periodicidades> filtradoslistaPeriodicidades) {
      this.filtradoslistaPeriodicidades = filtradoslistaPeriodicidades;
   }

   public Periodicidades getPeriodicidadSeleccionada() {
      return periodicidadSeleccionada;
   }

   public void setPeriodicidadSeleccionada(Periodicidades seleccionPeriodicidades) {
      this.periodicidadSeleccionada = seleccionPeriodicidades;
   }

   public List<Terceros> getLovTerceros() {
      if (lovTerceros == null) {
         lovTerceros = administrarNovedadesConceptos.lovTerceros();
      }
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> listaTerceros) {
      this.lovTerceros = listaTerceros;
   }

   public List<Terceros> getFiltradoslistaTerceros() {
      return filtradoslistaTerceros;
   }

   public void setFiltradoslistaTerceros(List<Terceros> filtradoslistaTerceros) {
      this.filtradoslistaTerceros = filtradoslistaTerceros;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros seleccionTerceros) {
      this.terceroSeleccionado = seleccionTerceros;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarNovedadesConceptos.lovFormulas();
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> listaFormulas) {
      this.lovFormulas = listaFormulas;
   }

   public List<Formulas> getFiltradoslistaFormulas() {
      return filtradoslistaFormulas;
   }

   public void setFiltradoslistaFormulas(List<Formulas> filtradoslistaFormulas) {
      this.filtradoslistaFormulas = filtradoslistaFormulas;
   }

   public Formulas getFormulaSeleccionadaLov() {
      return formulaSeleccionadaLov;
   }

   public void setFormulaSeleccionadaLov(Formulas seleccionFormulas) {
      this.formulaSeleccionadaLov = seleccionFormulas;
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarNovedadesConceptos.Conceptos();
      }
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> listaConceptos) {
      this.lovConceptos = listaConceptos;
   }

   public List<Conceptos> getFiltradoslistaConceptos() {
      return filtradoslistaConceptos;
   }

   public void setFiltradoslistaConceptos(List<Conceptos> filtradoslistaConceptos) {
      this.filtradoslistaConceptos = filtradoslistaConceptos;
   }

   public Conceptos getConceptoSeleccionadoLov() {
      return conceptoSeleccionadoLov;
   }

   public void setConceptoSeleccionadoLov(Conceptos seleccionConceptos) {
      this.conceptoSeleccionadoLov = seleccionConceptos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public int getResultado() {
      if (!listaNovedadesBorrar.isEmpty()) {
         for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
            resultado = administrarNovedadesConceptos.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
         }
      }
      return resultado;
   }

   public void setResultado(int resultado) {
      this.resultado = resultado;
   }
//Terminal y Usuario

   public String getAlias() {
      alias = administrarNovedadesConceptos.alias();
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public Usuarios getUsuarioBD() {
      getAlias();
      usuarioBD = administrarNovedadesConceptos.usuarioBD(alias);
      return usuarioBD;
   }

   public void setUsuarioBD(Usuarios usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public Novedades getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(Novedades nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
   }

   public boolean isTodas() {
      return todas;
   }

   public boolean isActuales() {
      return actuales;
   }

   public Novedades getEditarNovedades() {
      return editarNovedades;
   }

   public void setEditarNovedades(Novedades editarNovedades) {
      this.editarNovedades = editarNovedades;
   }

   public Novedades getDuplicarNovedad() {
      return duplicarNovedad;
   }

   public void setDuplicarNovedad(Novedades duplicarNovedad) {
      this.duplicarNovedad = duplicarNovedad;
   }

   public Novedades getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
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

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public String getInfoRegistroNovedades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesConcepto");
      infoRegistroNovedades = String.valueOf(tabla.getRowCount());
      return infoRegistroNovedades;
   }

   public String getInfoRegistroLovConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVConceptos");
      infoRegistroLovConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovConceptos;
   }

   public String getInfoRegistroLovFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVFormulas");
      infoRegistroLovFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroLovFormulas;
   }

   public String getInfoRegistroLovEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
      infoRegistroLovEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroLovEmpleados;
   }

   public String getInfoRegistroLovPeriodicidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPeriodicidades");
      infoRegistroLovPeriodicidades = String.valueOf(tabla.getRowCount());
      return infoRegistroLovPeriodicidades;
   }

   public String getInfoRegistroLovTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTerceros");
      infoRegistroLovTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTerceros;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
