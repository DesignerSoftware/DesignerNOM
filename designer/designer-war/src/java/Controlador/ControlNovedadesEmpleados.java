/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.PruebaEmpleados;
import Entidades.Terceros;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesEmpleadosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
//import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * @author Viktor
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesEmpleados implements Serializable {

   @EJB
   AdministrarNovedadesEmpleadosInterface administrarNovedadesEmpleados;
   @EJB
   AdministrarFormulaConceptoInterface administrarFormulaConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //SECUENCIA DE LA PERSONA
//    private BigInteger secuenciaEmpleado;
   //LISTA NOVEDADES
   private List<Novedades> listaNovedades;
   private List<Novedades> filtradosListaNovedades;
   private Novedades novedadSeleccionada;
   //LISTA QUE NO ES LISTA
   private List<PruebaEmpleados> listaEmpleadosNovedad;
   private List<PruebaEmpleados> filtrarListaEmpleadosNovedad;
   private PruebaEmpleados empleadoSeleccionado; //Seleccion Mostrar
   //LOV EMPLEADOS
   private List<PruebaEmpleados> lovEmpleados;
   private List<PruebaEmpleados> filtrarLovEmpleados;
   private PruebaEmpleados empleadoSeleccionadoLov;
   //editar celda
   private Novedades editarNovedades;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
//    private BigInteger secRegistro;
   private boolean guardado;
   //Crear Novedades
   private List<Novedades> listaNovedadesCrear;
   private Novedades nuevaNovedad;
   private int k;
   private BigInteger l;
   //Modificar Novedades
   private List<Novedades> listaNovedadesModificar;
   //Borrar Novedades
   private List<Novedades> listaNovedadesBorrar;
   //Autocompletar
   private String CodigoConcepto;
   private String NitTercero, Formula, DescripcionConcepto, DescripcionPeriodicidad, NombreTercero;
   private Date FechaFinal;
   private String CodigoPeriodicidad;
   private BigDecimal Saldo;
   private Integer HoraDia, MinutoHora;
   //L.O.V CONCEPTOS
   private List<Conceptos> listaConceptos;
   private List<Conceptos> filtradoslistaConceptos;
   private Conceptos seleccionConceptos;
   BigInteger secconcepto;
   //L.O.V Periodicidades
   private List<Periodicidades> listaPeriodicidades;
   private List<Periodicidades> filtradoslistaPeriodicidades;
   private Periodicidades seleccionPeriodicidades;
   //L.O.V TERCEROS
   private List<Terceros> listaTerceros;
   private List<Terceros> filtradoslistaTerceros;
   private Terceros seleccionTerceros;
   //L.O.V FORMULAS
   private List<Formulas> listaFormulas;
   private List<Formulas> filtradoslistaFormulas;
   private Formulas seleccionFormulas;
   //Columnas Tabla NOVEDADES
   private Column nEConceptoCodigo, nEConceptoDescripcion, nEFechasInicial, nEFechasFinal,
           nEValor, nESaldo, nEPeriodicidadCodigo, nEDescripcionPeriodicidad, nETercerosNit,
           nETercerosNombre, nEFormulas, nEHorasDias, nEMinutosHoras, nETipo;
   //Duplicar
   private Novedades duplicarNovedad;
   //USUARIO
   private String alias;
   private Usuarios usuarioBD;
   //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
   private int resultado;
   private boolean todas;
   private boolean actuales;
   //
   private boolean activoBtnAcumulado;
   //
   private Novedades actualNovedadTabla;
   private String altoTabla;
   private String altoTablaReg;
   BigDecimal valor = new BigDecimal(Integer.toString(0));
   private String infoRegistro, infoRegistroConceptos, infoRegistroPeriodicidad, infoRegistroFormulas, infoRegistroTerceros, infoRegistrosEmpleadosNovedades, infoRegistroEmpleadosLOV;
   //Controlar el cargue de muchos empleados
   private boolean cargarTodos;
   private int cantidadEmpleadosNov;
   private boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNovedadesEmpleados() {
      actualNovedadTabla = new Novedades();
      activoBtnAcumulado = true;
      permitirIndex = true;
      listaNovedades = null;
      lovEmpleados = null;
      listaFormulas = null;
      listaConceptos = null;
      todas = false;
      actuales = true;
      listaPeriodicidades = null;
      listaEmpleadosNovedad = null;
      aceptar = true;
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      listaNovedadesBorrar = new ArrayList<Novedades>();
      listaNovedadesCrear = new ArrayList<Novedades>();
      listaNovedadesModificar = new ArrayList<Novedades>();
      //Crear VC
      nuevaNovedad = new Novedades();
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre("");
      nuevaNovedad.getPeriodicidad().setCodigoStr("");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre("");
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion("");
      nuevaNovedad.getConcepto().setCodigoSTR("0");
      nuevaNovedad.setTipo("FIJA");
      nuevaNovedad.setUsuarioreporta(new Usuarios());
      nuevaNovedad.setTerminal(" ");
      nuevaNovedad.setFechareporte(new Date());
      altoTabla = "145";
      nuevaNovedad.setValortotal(valor);
      cargarTodos = false;
      cantidadEmpleadosNov = 0;
      CodigoConcepto = "0";
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

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
         String pagActual = "novedadempleado";
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
         administrarNovedadesEmpleados.obtenerConexion(ses.getId());
         administrarFormulaConcepto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaEmpleadosNovedad();
         if (listaEmpleadosNovedad != null) {
            if (!listaEmpleadosNovedad.isEmpty()) {
               empleadoSeleccionado = listaEmpleadosNovedad.get(0);
            }
         }
         listaNovedades = null;
         getListaNovedades();
         contarRegistros();
         if (listaNovedades != null) {
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         }
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      anularBotonLOV();
      cerrarFiltrado();
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
//        k = 0;
      listaNovedades = null;
      guardado = true;
      permitirIndex = true;
      resultado = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void salir() {
      anularBotonLOV();
      cerrarFiltrado();
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
//        empleadoSeleccionado = null;
      novedadSeleccionada = null;
      listaEmpleadosNovedad = null;
      lovEmpleados = null;
      listaNovedades = null;
      resultado = 0;
      guardado = true;
      permitirIndex = true;
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
   }

   public void cerrarFiltrado() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
         nEConceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
         nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
         nEConceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
         nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
         nEFechasInicial.setFilterStyle("display: none; visibility: hidden;");
         nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
         nEFechasFinal.setFilterStyle("display: none; visibility: hidden;");
         nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
         nEValor.setFilterStyle("display: none; visibility: hidden;");
         nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
         nESaldo.setFilterStyle("display: none; visibility: hidden;");
         nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
         nEPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
         nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
         nEDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
         nETercerosNit.setFilterStyle("display: none; visibility: hidden;");
         nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
         nETercerosNombre.setFilterStyle("display: none; visibility: hidden;");
         nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
         nEFormulas.setFilterStyle("display: none; visibility: hidden;");
         nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
         nEHorasDias.setFilterStyle("display: none; visibility: hidden;");
         nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
         nEMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
         nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
         nETipo.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "145";
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 0;
         filtradosListaNovedades = null;
         tipoLista = 0;
      }
   }

   //Ubicacion Celda Arriba 
   public void cambiarEmpleado() {
      listaNovedades = null;
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         System.out.println("empleadoSeleccionado : " + empleadoSeleccionado);
         novedadSeleccionada = null;
         activoBtnAcumulado = true;
         getListaNovedades();
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         contarRegistros();
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
   }

   public void limpiarListas() {
      listaNovedadesCrear.clear();
      listaNovedadesBorrar.clear();
      listaNovedadesModificar.clear();
//        secuenciaEmpleado = empleadoSeleccionado.getId();
      listaNovedades = null;
      guardado = true;
      getListaNovedades();
      System.out.println("listaNovedades Valor" + listaNovedades.get(0).getValortotal());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
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

   public void seleccionarTipo(String estadoTipo, Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
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
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   //GUARDAR
   public void guardarCambiosNovedades() {
//        Empleados emp = new Empleados();
      if (guardado == false) {
         System.out.println("Realizando Operaciones Novedades");

         getResultado();
         System.out.println("Resultado: " + resultado);
         if (resultado > 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:solucionesFormulas");
            RequestContext.getCurrentInstance().execute("PF('solucionesFormulas').show()");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               System.out.println("Borrando..." + listaNovedadesBorrar.size());

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
               administrarNovedadesEmpleados.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            System.out.println("Entra");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               System.out.println("Creando...");

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
               if (listaNovedadesCrear.get(i).getValortotal() == null) {
                  listaNovedadesCrear.get(i).setValortotal(new BigDecimal(0));
               }
               System.out.println(listaNovedadesCrear.get(i).getTipo());
               administrarNovedadesEmpleados.crearNovedades(listaNovedadesCrear.get(i));
            }
            System.out.println("LimpiaLista");
            listaNovedadesCrear.clear();
         }
         if (!listaNovedadesModificar.isEmpty()) {
            administrarNovedadesEmpleados.modificarNovedades(listaNovedadesModificar);
            listaNovedadesModificar.clear();
         }

         System.out.println("Se guardaron los datos con exito");
         listaNovedades = null;
         RequestContext context = RequestContext.getCurrentInstance();
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadEmpleado() throws UnknownHostException {
      int pasa = 0;
      int pasa2 = 0;
      Empleados emp = new Empleados();
//        Empleados emp2 = new Empleados();
      RequestContext context = RequestContext.getCurrentInstance();

      System.out.println("nuevaNovedad Fechainicial : " + nuevaNovedad.getFechainicial());
      System.out.println("Concepto : " + nuevaNovedad.getConcepto().getSecuencia());
      System.out.println("Concepto codigo : " + nuevaNovedad.getConcepto().getCodigoSTR());
      System.out.println("Formula : " + nuevaNovedad.getFormula().getSecuencia());
      System.out.println("Formula NL :" + nuevaNovedad.getFormula().getNombrelargo() + "...");
      System.out.println("Periodicidad : " + nuevaNovedad.getPeriodicidad().getNombre());
      System.out.println("getTipo() : " + nuevaNovedad.getTipo());

      if (nuevaNovedad.getFechainicial() == null || nuevaNovedad.getConcepto().getCodigoSTR() == null || nuevaNovedad.getConcepto().getCodigoSTR().equals("0")
              || nuevaNovedad.getFormula().getNombrelargo() == null || nuevaNovedad.getFormula().getNombrelargo().equals("") || nuevaNovedad.getTipo() == null
              || ((nuevaNovedad.getValortotal() == null || nuevaNovedad.getValortotal() == valor) && nuevaNovedad.getUnidadesparteentera() == null && nuevaNovedad.getUnidadespartefraccion() == null)) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      } else {
         Date fechaIngreso = administrarNovedadesEmpleados.vigenciaTipoContratoSecEmpleado(empleadoSeleccionado.getId());

         if (fechaIngreso != null) {
            if (nuevaNovedad.getFechainicial() != null) {
               if (nuevaNovedad.getFechainicial().compareTo(fechaIngreso) < 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
                  RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
                  pasa2++;
               }
            } else if (nuevaNovedad.getFechafinal() != null) {
               if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
                  RequestContext.getCurrentInstance().execute("PF('fechas').show()");
                  pasa2++;
               }
            }
         } else {
            System.err.println("La fecha de ingreso retorno null");
            pasa2++;
         }

         if (pasa2 == 0) {
            System.out.println("Paso todas las validaciones");
            cerrarFiltrado();
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevaNovedad.setSecuencia(l);

//            for (int i = 0; i < lovEmpleados.size(); i++) {
//                if (empleadoSeleccionado.getId().compareTo(lovEmpleados.get(i).getId()) == 0) {
//                    emp = administrarNovedadesEmpleados.elEmpleado(lovEmpleados.get(i).getId());
            emp = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());
//                }
//            }

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
            System.out.println("Alias: " + alias);
            getUsuarioBD();
            System.out.println("UsuarioBD: " + usuarioBD);
            nuevaNovedad.setTerminal(localMachine.getHostName());
            nuevaNovedad.setUsuarioreporta(usuarioBD);
            nuevaNovedad.setEmpleado(emp); //Envia empleado
            System.out.println("Empleado enviado: " + emp.getPersona().getNombreCompleto());
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(nuevaNovedad);
            contarRegistros();
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
            nuevaNovedad = new Novedades();
            nuevaNovedad.setPeriodicidad(new Periodicidades());
            nuevaNovedad.getPeriodicidad().setNombre("");
            nuevaNovedad.getPeriodicidad().setCodigoStr("");
            nuevaNovedad.setTercero(new Terceros());
            nuevaNovedad.getTercero().setNombre("");
            nuevaNovedad.setConcepto(new Conceptos());
            nuevaNovedad.getConcepto().setDescripcion("");
            nuevaNovedad.getConcepto().setCodigoSTR("0");
            nuevaNovedad.setTipo("FIJA");
            nuevaNovedad.setUsuarioreporta(new Usuarios());
            nuevaNovedad.setTerminal(" ");
            nuevaNovedad.setFechareporte(new Date());

            System.out.println("nuevaNovedad : " + nuevaNovedad.getFechareporte());
            activoBtnAcumulado = true;
            RequestContext.getCurrentInstance().update("form:ACUMULADOS");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevaNovedadEmpleado').hide()");
         }
      }
   }

   public void cambioFecha(Date fecha) {
      System.out.println("feche : " + fecha);
      System.out.println("controlNovedadesEmpleados.getNuevaNovedad().getFechainicial() : " + nuevaNovedad.getFechainicial());
   }

   public void confirmarDuplicar() throws UnknownHostException {

      int pasa2 = 0;
      int pasa = 0;
//        Empleados emp = new Empleados();
      Empleados emp2 = new Empleados();
      RequestContext context = RequestContext.getCurrentInstance();

      System.out.println("duplicarNovedad Fechainicial : " + duplicarNovedad.getFechainicial());
      System.out.println("Concepto : " + duplicarNovedad.getConcepto().getSecuencia());
      System.out.println("Formula : " + duplicarNovedad.getFormula().getSecuencia());
      System.out.println("Periodicidad : " + duplicarNovedad.getPeriodicidad().getNombre());
      System.out.println("getTipo() : " + duplicarNovedad.getTipo());

      if (duplicarNovedad.getFechainicial() == null || duplicarNovedad.getEmpleado() == null
              || duplicarNovedad.getFormula().getNombrelargo() == null || duplicarNovedad.getTipo() == null
              || (duplicarNovedad.getValortotal() == null && duplicarNovedad.getUnidadesparteentera() == null && duplicarNovedad.getUnidadespartefraccion() == null)) {
         pasa++;
      }

      System.out.println("confirmarDuplicar() pasa : " + pasa);

      if (pasa == 0) {
         emp2 = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

         if (duplicarNovedad.getFechainicial() != null) {
            if (duplicarNovedad.getFechainicial().compareTo(emp2.getFechacreacion()) < 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
               RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
               pasa2++;
            }
         } else if (duplicarNovedad.getFechafinal() != null) {
            if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
               RequestContext.getCurrentInstance().execute("PF('fechas').show()");
               pasa2++;
            }
         }

         if (pasa2 == 0) {
            listaNovedades.add(duplicarNovedad);
            listaNovedadesCrear.add(duplicarNovedad);
            contarRegistros();
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));

            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cerrarFiltrado();

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
            System.out.println("Alias: " + alias);
            getUsuarioBD();
            System.out.println("UsuarioBD: " + usuarioBD);
            duplicarNovedad.setTerminal(localMachine.getHostName());

            duplicarNovedad = new Novedades();
            activoBtnAcumulado = true;
            RequestContext.getCurrentInstance().update("form:ACUMULADOS");
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroNovedad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }
   }

   public void asignarIndex(int cualLista, int tipoAct) {

      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      tipoActualizacion = tipoAct;

      if (cualLista == 0) {
         if (cargarTodos) {
            listaEmpleadosNovedad = null;
            cargarTodosEmpleados();
            cargarTodos = false;
         }
         contarRegistrosLovEmpleados();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      } else if (cualLista == 1) {
         cargarLOVConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualLista == 2) {
         cargarLOVFormulas();
         contarRegistrosFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (cualLista == 3) {
         cargarLOVPeriodicidades();
         contarRegistrosPeriodicidad();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualLista == 4) {
         cargarLOVTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
      System.out.println("cual celda :" + cualLista);
      System.out.println("tipo Actualización :" + tipoActualizacion);
   }

   public void asignarIndex(Novedades novedad, int cualLista, int tipoAct) {

      novedadSeleccionada = novedad;
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      tipoActualizacion = tipoAct;

      if (cualLista == 1) {
         activarBotonLOV();
         cargarLOVConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualLista == 2) {
         activarBotonLOV();
         cargarLOVFormulas();
         contarRegistrosFormulas();
         RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      } else if (cualLista == 3) {
         activarBotonLOV();
         cargarLOVPeriodicidades();
         contarRegistrosPeriodicidad();
         RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualLista == 4) {
         activarBotonLOV();
         cargarLOVTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      } else {
         anularBotonLOV();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //AUTOCOMPLETAR
   public void modificarNovedades(Novedades novedad, String confirmarCambio, String valor) {

      novedadSeleccionada = novedad;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
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
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLOVFormulas();
         novedadSeleccionada.getFormula().setNombresFormula(Formula);
         for (int i = 0; i < listaFormulas.size(); i++) {
            if (listaFormulas.get(i).getNombresFormula().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setFormula(listaFormulas.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         cargarLOVTerceros();
         novedadSeleccionada.getTercero().setNitalternativo(NitTercero);
         for (int i = 0; i < listaTerceros.size(); i++) {
            if (listaTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setTercero(listaTerceros.get(indiceUnicoElemento));

         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         }

      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarLOVConceptos();

      } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {
         cargarLOVConceptos();
         novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
         for (int i = 0; i < listaPeriodicidades.size(); i++) {
            if ((listaPeriodicidades.get(i).getCodigoStr()).startsWith(valor.toString().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadSeleccionada.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
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
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void activarCtrlF11() {
      System.out.println("TipoLista= " + tipoLista);
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         System.out.println("Activar");
         System.out.println("TipoLista= " + tipoLista);
         nEConceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoCodigo");
         nEConceptoCodigo.setFilterStyle("width: 85% !important");
         nEConceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEConceptoDescripcion");
         nEConceptoDescripcion.setFilterStyle("width: 85% !important");
         nEFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasInicial");
         nEFechasInicial.setFilterStyle("width: 85% !important");
         nEFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechasFinal");
         nEFechasFinal.setFilterStyle("width: 85% !important");
         nEValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEValor");
         nEValor.setFilterStyle("width: 85% !important");
         nESaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESaldo");
         nESaldo.setFilterStyle("width: 85% !important");
         nEPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodicidadCodigo");
         nEPeriodicidadCodigo.setFilterStyle("width: 85% !important");
         nEDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDescripcionPeriodicidad");
         nEDescripcionPeriodicidad.setFilterStyle("width: 85% !important");
         nETercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNit");
         nETercerosNit.setFilterStyle("width: 85% !important");
         nETercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETercerosNombre");
         nETercerosNombre.setFilterStyle("width: 85% !important");
         nEFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFormulas");
         nEFormulas.setFilterStyle("width: 85% !important");
         nEHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEHorasDias");
         nEHorasDias.setFilterStyle("width: 85% !important");
         nEMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEMinutosHoras");
         nEMinutosHoras.setFilterStyle("width: 85% !important");
         nETipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nETipo");
         nETipo.setFilterStyle("width: 85% !important");
         altoTabla = "125";
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 1;
         tipoLista = 1;
      } else {
         System.out.println("Desactivar");
         System.out.println("TipoLista= " + tipoLista);
         cerrarFiltrado();
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (novedadSeleccionada != null) {
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         if (cualCelda == 0) {
            cargarLOVConceptos();
            RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 6) {
            cargarLOVPeriodicidades();
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 8) {
            cargarLOVTerceros();
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 10) {
            cargarLOVFormulas();
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
         }
      }
   }
   //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.

   public void cambiarIndice(Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();
         activoBtnAcumulado = false;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         cualCelda = celda;
         actualNovedadTabla = novedadSeleccionada;
         if (cualCelda == 0) {
            activarBotonLOV();
            CodigoConcepto = novedadSeleccionada.getConcepto().getCodigoSTR();
         } else if (cualCelda == 1) {
            activarBotonLOV();
            DescripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
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
            DescripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
         } else if (cualCelda == 8) {
            activarBotonLOV();
            NitTercero = novedadSeleccionada.getTercero().getNitalternativo();
         } else if (cualCelda == 9) {
            activarBotonLOV();
            NombreTercero = novedadSeleccionada.getTercero().getNombre();
         } else if (cualCelda == 10) {
            anularBotonLOV();
            HoraDia = novedadSeleccionada.getUnidadesparteentera();
         } else if (cualCelda == 11) {
            anularBotonLOV();
            MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
         } else {
            anularBotonLOV();
         }
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;

         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosDescripciones");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosDescripciones').show()");
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
            RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
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
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            CodigoConcepto = nuevaNovedad.getConcepto().getCodigoSTR();
         } else if (tipoNuevo == 2) {
            CodigoConcepto = duplicarNovedad.getConcepto().getCodigoSTR();
         }
         System.out.println("tipoNuevo: " + tipoNuevo);
         System.out.println("campo:" + Campo);
      } else if (Campo.equals("CODIGO")) {
         if (tipoNuevo == 1) {
            CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
         } else if (tipoNuevo == 2) {
            CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
         }
      } else if (Campo.equals("NIT")) {
         if (tipoNuevo == 1) {
            NitTercero = nuevaNovedad.getTercero().getNitalternativo();
         } else if (tipoNuevo == 2) {
            NitTercero = duplicarNovedad.getTercero().getNitalternativo();
         }
      } else if (Campo.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            Formula = nuevaNovedad.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            Formula = duplicarNovedad.getFormula().getNombrelargo();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         cargarLOVFormulas();
         if (tipoNuevo == 1) {
            nuevaNovedad.getFormula().setNombrelargo(Formula);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getFormula().setNombrelargo(Formula);
         }
         for (int i = 0; i < listaFormulas.size(); i++) {
            if (listaFormulas.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setFormula(listaFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setFormula(listaFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         cargarLOVTerceros();
         if (tipoNuevo == 1) {
            nuevaNovedad.getTercero().setNitalternativo(NitTercero);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getTercero().setNitalternativo(NitTercero);
         }

         for (int i = 0; i < listaTerceros.size(); i++) {
            if (listaTerceros.get(i).getNitalternativo() != null) {
               if (listaTerceros.get(i).getNitalternativo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
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
         cargarLOVPeriodicidades();
         if (tipoNuevo == 1) {
            nuevaNovedad.getPeriodicidad().setCodigoStr(nuevaNovedad.getPeriodicidad().getCodigoStr());
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getPeriodicidad().setCodigoStr(duplicarNovedad.getPeriodicidad().getCodigoStr());
         }

         for (int i = 0; i < listaPeriodicidades.size(); i++) {
            if (listaPeriodicidades.get(i).getCodigoStr().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriocidadDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriocidadDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriocidadDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriocidadDescripcion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         cargarLOVConceptos();
         if (tipoNuevo == 1) {
            nuevaNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         } else if (tipoNuevo == 2) {
            duplicarNovedad.getConcepto().setCodigoSTR(CodigoConcepto);
         }
         for (int i = 0; i < listaConceptos.size(); i++) {
            if (listaConceptos.get(i).getCodigoSTR().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaNovedad.setConcepto(listaConceptos.get(indiceUnicoElemento));
               Formulas formula = verificarFormulaConcepto(nuevaNovedad.getConcepto().getSecuencia());
               nuevaNovedad.setFormula(formula);
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarNovedad.setConcepto(listaConceptos.get(indiceUnicoElemento));
               Formulas formula = verificarFormulaConcepto(duplicarNovedad.getConcepto().getSecuencia());
               duplicarNovedad.setFormula(formula);
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         } else {
            RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConceptoDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConceptoDescripcion");
            }
         }
      }
   }

   public void actualizarEmpleadosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
//        PruebaEmpleados pe = administrarNovedadesEmpleados.novedadEmpleado(empleadoSeleccionadoLov.getId());
//        if (pe != null) {
//            listaEmpleadosNovedad.add(pe);
//        } else {
//            pe = new PruebaEmpleados();
//            pe.setId(empleadoSeleccionadoLov.getId());
//            pe.setCodigo(empleadoSeleccionadoLov.getCodigo());
//            pe.setNombre(empleadoSeleccionadoLov.getNombre());
//            pe.setValor(null);
//            pe.setTipo(empleadoSeleccionadoLov.getTipo());
//            listaEmpleadosNovedad.add(pe);
//        }
      System.out.println("empleadoSeleccionadoLov : " + empleadoSeleccionadoLov + "  " + empleadoSeleccionadoLov.getNombre());
      listaEmpleadosNovedad.clear();
      listaEmpleadosNovedad.add(empleadoSeleccionadoLov);
      empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      contarRegistrosLovEmpleados();
//            secuenciaEmpleado = empleadoSeleccionadoLov.getId();
      listaNovedades = null;
      getListaNovedades();

      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      //RequestContext.getCurrentInstance().update("form:datosEmpleados");
      //RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      contarRegistros();
      contarRegistrosEmpleados();

      filtrarListaEmpleadosNovedad = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void actualizarFormulas() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("seleccionFormulas: " + seleccionFormulas);
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setFormula(seleccionFormulas);
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
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         System.out.println("seleccionFormulas: " + seleccionFormulas);
         nuevaNovedad.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");

      }
      filtradoslistaFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      Formulas formula;
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
         novedadSeleccionada.setFormula(formula);

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
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");

      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
         nuevaNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");

      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setConcepto(seleccionConceptos);
         formula = verificarFormulaConcepto(seleccionConceptos.getSecuencia());
         duplicarNovedad.setFormula(formula);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaConceptos = null;
      seleccionConceptos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public Formulas verificarFormulaConcepto(BigInteger secCon) {
      List<FormulasConceptos> formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
      Formulas formulaR = new Formulas();
      BigInteger autoFormula;
      cargarLOVFormulas();
      if (formulasConceptoSel != null) {
         if (!formulasConceptoSel.isEmpty()) {
            autoFormula = formulasConceptoSel.get(0).getFormula();
         } else {
            autoFormula = new BigInteger("4621544");
         }
      } else {
         autoFormula = new BigInteger("4621544");
      }

      for (int i = 0; i < listaFormulas.size(); i++) {
         if (autoFormula.equals(listaFormulas.get(i).getSecuencia())) {
            formulaR = listaFormulas.get(i);
         }
      }
      return formulaR;
   }

   public void actualizarPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setPeriodicidad(seleccionPeriodicidades);
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
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setPeriodicidad(seleccionPeriodicidades);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setPeriodicidad(seleccionPeriodicidades);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaPeriodicidades = null;
      seleccionPeriodicidades = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
   }

   public void actualizarTerceros() {
      listaNovedadesModificar.add(novedadSeleccionada);
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSeleccionada.setTercero(seleccionTerceros);
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaNovedad.setTercero(seleccionTerceros);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setTercero(seleccionTerceros);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
      }
      filtradoslistaTerceros = null;
      seleccionTerceros = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
   }

   public void mostrarTodos() {
      System.out.println("controlNovedadesEmpleados.mostrarTodos...");
      if (cargarTodos) {
         listaEmpleadosNovedad = null;
         cargarTodosEmpleados();
         cargarTodos = false;
      }

      listaEmpleadosNovedad.clear();
      if (lovEmpleados != null) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovEmpleados.get(i));
         }
      }
      listaNovedades = null;
      empleadoSeleccionado = null;
      getListaNovedades();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      contarRegistros();
      contarRegistrosEmpleados();
      activoBtnAcumulado = true;
      filtrarListaEmpleadosNovedad = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   //DUPLICAR ENCARGATURA
   public void duplicarEN() {
      if (novedadSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         duplicarNovedad = new Novedades();
         k++;
         l = BigInteger.valueOf(k);
         Empleados emple = administrarNovedadesEmpleados.elEmpleado(empleadoSeleccionado.getId());

         duplicarNovedad.setSecuencia(l);
         duplicarNovedad.setEmpleado(emple);
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

         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').show()");
      }
   }

   public void cancelarCambioEmpleados() {
      filtrarLovEmpleados = null;
      empleadoSeleccionadoLov = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");

      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");

   }

   public void cancelarCambioFormulas() {
      filtradoslistaFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovFormulas:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
   }

   public void cancelarCambioConceptos() {
      filtradoslistaConceptos = null;
      seleccionConceptos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovConceptos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioPeriodicidades() {
      filtradoslistaPeriodicidades = null;
      seleccionPeriodicidades = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void cancelarCambioTerceros() {
      filtradoslistaTerceros = null;
      seleccionTerceros = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovTerceros:LOVTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
   }

   public void todasNovedades() {
      System.out.println("Ingrese a todasNovedades()");
      listaNovedades.clear();
      listaNovedades = administrarNovedadesEmpleados.todasNovedades(empleadoSeleccionado.getId());
      activoBtnAcumulado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      todas = true;
      actuales = false;
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
      System.out.println("Sali de todasNovedades()");

   }

   public void actualesNovedades() {
      listaNovedades.clear();
      listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
      activoBtnAcumulado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      todas = false;
      actuales = true;
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:TODAS");
      RequestContext.getCurrentInstance().update("form:ACTUALES");
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "NovedadesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      RequestContext contexto = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      contexto.update("form:ACUMULADOS");
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      RequestContext contexto = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      contexto.update("form:ACUMULADOS");
   }

   //LIMPIAR NUEVO REGISTRO NOVEDAD
   public void limpiarNuevaNovedad() {
      nuevaNovedad = new Novedades();
      nuevaNovedad.setPeriodicidad(new Periodicidades());
      nuevaNovedad.getPeriodicidad().setNombre("");
      nuevaNovedad.getPeriodicidad().setCodigoStr("");
      nuevaNovedad.setTercero(new Terceros());
      nuevaNovedad.getTercero().setNombre("");
      nuevaNovedad.setConcepto(new Conceptos());
      nuevaNovedad.getConcepto().setDescripcion("");
      nuevaNovedad.getConcepto().setCodigoSTR("0");
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
      duplicarNovedad.getPeriodicidad().setCodigoStr(" ");
      duplicarNovedad.setTercero(new Terceros());
      duplicarNovedad.getTercero().setNombre(" ");
      duplicarNovedad.setConcepto(new Conceptos());
      duplicarNovedad.getConcepto().setDescripcion(" ");
      duplicarNovedad.getConcepto().setCodigoSTR("0");
      duplicarNovedad.setTipo("FIJA");
      duplicarNovedad.setUsuarioreporta(new Usuarios());
      duplicarNovedad.setTerminal(" ");
      duplicarNovedad.setFechareporte(new Date());
   }

   //RASTROS 
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      activoBtnAcumulado = true;
      RequestContext.getCurrentInstance().update("form:ACUMULADOS");
      System.out.println("lol");
      if (novedadSeleccionada != null) {
         System.out.println("lol 2");
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
         System.out.println("resultado: " + result);
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

   //BORRAR NOVEDADES
   public void borrarNovedades() {
      if (novedadSeleccionada != null) {
         activoBtnAcumulado = true;
         RequestContext.getCurrentInstance().update("form:ACUMULADOS");
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
         contarRegistrosLovEmpleados();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         novedadSeleccionada = null;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void cargarLOVConceptos() {
      if (listaConceptos == null || listaConceptos.isEmpty()) {
         listaConceptos = administrarNovedadesEmpleados.lovConceptos();
      }
   }

   public void cargarLOVPeriodicidades() {
      if (listaPeriodicidades == null || listaPeriodicidades.isEmpty()) {
         listaPeriodicidades = administrarNovedadesEmpleados.lovPeriodicidades();
      }
   }

   public void cargarLOVFormulas() {
      if (listaFormulas == null || listaFormulas.isEmpty()) {
         listaFormulas = administrarNovedadesEmpleados.lovFormulas();
      }
   }

   public void cargarLOVTerceros() {
      if (listaTerceros == null || listaTerceros.isEmpty()) {
         listaTerceros = administrarNovedadesEmpleados.lovTerceros();
      }
   }

   public void cargarTodosEmpleados() {
      if (listaEmpleadosNovedad == null) {
         listaEmpleadosNovedad = administrarNovedadesEmpleados.empleadosNovedades();
         lovEmpleados = new ArrayList<PruebaEmpleados>();
         for (int i = 0; i < listaEmpleadosNovedad.size(); i++) {
            lovEmpleados.add(listaEmpleadosNovedad.get(i));
         }
      }
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadSeleccionada = null;
      anularBotonLOV();
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosEmpleados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
   }

   public void contarRegistrosLovEmpleados() {
      RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleadosLOV");
   }

   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("formLovConceptos:infoRegistroConceptos");
   }

   public void contarRegistrosTerceros() {
      RequestContext.getCurrentInstance().update("formLovTerceros:infoRegistroTerceros");
   }

   public void contarRegistrosFormulas() {
      RequestContext.getCurrentInstance().update("formLovFormulas:infoRegistroFormulas");
   }

   public void contarRegistrosPeriodicidad() {
      RequestContext.getCurrentInstance().update("formLovPeriodicidad:infoRegistroPeriodicidad");
   }

   //------------------------INICIO GET's & SET's-----------------------------
   public void setListaEmpleadosNovedad(List<PruebaEmpleados> listaEmpleados) {
      this.listaEmpleadosNovedad = listaEmpleados;
   }

   public List<PruebaEmpleados> getFiltrarListaEmpleadosNovedad() {
      return filtrarListaEmpleadosNovedad;
   }

   public void setFiltrarListaEmpleadosNovedad(List<PruebaEmpleados> filtradosListaEmpleadosNovedad) {
      this.filtrarListaEmpleadosNovedad = filtradosListaEmpleadosNovedad;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public PruebaEmpleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(PruebaEmpleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   //LISTA NOVEDADES
   public List<Novedades> getListaNovedades() {
      if (listaNovedades == null && empleadoSeleccionado != null) {
         listaNovedades = administrarNovedadesEmpleados.novedadesEmpleado(empleadoSeleccionado.getId());
      }
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
   //L.O.V PERIODICIDADES

   public List<Periodicidades> getListaPeriodicidades() {
      return listaPeriodicidades;
   }

   public void setListaPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      this.listaPeriodicidades = listaPeriodicidades;
   }

   public List<Periodicidades> getFiltradoslistaPeriodicidades() {
      return filtradoslistaPeriodicidades;
   }

   public void setFiltradoslistaPeriodicidades(List<Periodicidades> filtradoslistaPeriodicidades) {
      this.filtradoslistaPeriodicidades = filtradoslistaPeriodicidades;
   }

   public Periodicidades getSeleccionPeriodicidades() {
      return seleccionPeriodicidades;
   }

   public void setSeleccionPeriodicidades(Periodicidades seleccionPeriodicidades) {
      this.seleccionPeriodicidades = seleccionPeriodicidades;
   }
   //L.O.V CONCEPTOS

   public List<Conceptos> getListaConceptos() {
      return listaConceptos;
   }

   public void setListaConceptos(List<Conceptos> listaConceptos) {
      this.listaConceptos = listaConceptos;
   }
   //L.O.V FORMULAS

   public List<Formulas> getListaFormulas() {
      return listaFormulas;
   }

   public void setListaFormulas(List<Formulas> listaFormulas) {
      this.listaFormulas = listaFormulas;
   }

   public List<Conceptos> getFiltradoslistaConceptos() {
      return filtradoslistaConceptos;
   }

   public void setFiltradoslistaConceptos(List<Conceptos> filtradoslistaConceptos) {
      this.filtradoslistaConceptos = filtradoslistaConceptos;
   }

   public Conceptos getSeleccionConceptos() {
      return seleccionConceptos;
   }

   public void setSeleccionConceptos(Conceptos seleccionConceptos) {
      this.seleccionConceptos = seleccionConceptos;
   }

   public List<Formulas> getFiltradoslistaFormulas() {
      return filtradoslistaFormulas;
   }

   public void setFiltradoslistaFormulas(List<Formulas> filtradoslistaFormulas) {
      this.filtradoslistaFormulas = filtradoslistaFormulas;
   }

   public Formulas getSeleccionFormulas() {
      return seleccionFormulas;
   }

   public void setSeleccionFormulas(Formulas seleccionFormulas) {
      this.seleccionFormulas = seleccionFormulas;
   }
   //L.O.V TERCEROS

   public List<Terceros> getListaTerceros() {
      return listaTerceros;
   }

   public void setListaTerceros(List<Terceros> listaTerceros) {
      this.listaTerceros = listaTerceros;
   }

   public List<Terceros> getFiltradoslistaTerceros() {
      return filtradoslistaTerceros;
   }

   public void setFiltradoslistaTerceros(List<Terceros> filtradoslistaTerceros) {
      this.filtradoslistaTerceros = filtradoslistaTerceros;
   }

   public Terceros getSeleccionTerceros() {
      return seleccionTerceros;
   }

   public void setSeleccionTerceros(Terceros seleccionTerceros) {
      this.seleccionTerceros = seleccionTerceros;
   }

   public Novedades getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(Novedades nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
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

   public List<PruebaEmpleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         cargarTodosEmpleados();
      }
      return listaEmpleadosNovedad;
   }

   public String getAlias() {
      alias = administrarNovedadesEmpleados.alias();
      System.out.println("Alias: " + alias);
      return alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public Usuarios getUsuarioBD() {
      getAlias();
      usuarioBD = administrarNovedadesEmpleados.usuarioBD(alias);
      return usuarioBD;
   }

   public void setUsuarioBD(Usuarios usuarioBD) {
      this.usuarioBD = usuarioBD;
   }

   public int getResultado() {
      if (!listaNovedadesBorrar.isEmpty()) {
         for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
            resultado = administrarNovedadesEmpleados.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
         }
      }
      return resultado;
   }

   public boolean isTodas() {
      return todas;
   }

   public boolean isActuales() {
      return actuales;
   }

   public boolean isActivoBtnAcumulado() {
      return activoBtnAcumulado;
   }

   public void setActivoBtnAcumulado(boolean activoBtnAcumulado) {
      this.activoBtnAcumulado = activoBtnAcumulado;
   }

   public Novedades getActualNovedadTabla() {
      return actualNovedadTabla;
   }

   public void setActualNovedadTabla(Novedades actualNovedadTabla) {
      this.actualNovedadTabla = actualNovedadTabla;
   }

   public Novedades getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaReg() {
      if (altoTabla.equals("125")) {
         altoTablaReg = "5";
      } else {
         altoTablaReg = "6";
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public PruebaEmpleados getEmpleadoSeleccionadoLov() {
      return empleadoSeleccionadoLov;
   }

   public void setEmpleadoSeleccionadoLov(PruebaEmpleados empleadoSeleccionadoLov) {
      this.empleadoSeleccionadoLov = empleadoSeleccionadoLov;
   }

   public List<PruebaEmpleados> getFiltrarLovEmpleados() {
      return filtrarLovEmpleados;
   }

   public void setFiltrarLovEmpleados(List<PruebaEmpleados> filtrarLovEmpleados) {
      this.filtrarLovEmpleados = filtrarLovEmpleados;
   }

   public List<PruebaEmpleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<PruebaEmpleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovConceptos:LOVConceptos");
      if (filtradoslistaConceptos != null) {
         if (filtradoslistaConceptos.size() == 1) {
            seleccionConceptos = filtradoslistaConceptos.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();PF('LOVConceptos').selectRow(0);");
         } else {
            seleccionConceptos = null;
            RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();");
         }
      } else {
         seleccionConceptos = null;
         aceptar = true;
      }
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public String getInfoRegistroPeriodicidad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPeriodicidad:LOVPeriodicidades");
      if (filtradoslistaPeriodicidades != null) {
         if (filtradoslistaPeriodicidades.size() == 1) {
            seleccionPeriodicidades = filtradoslistaPeriodicidades.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();PF('LOVPeriodicidades').selectRow(0);");
         } else {
            seleccionPeriodicidades = null;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();");
         }
      } else {
         seleccionPeriodicidades = null;
         aceptar = true;
      }
      infoRegistroPeriodicidad = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodicidad;
   }

   public void setInfoRegistroPeriodicidad(String infoRegistroPeriodicidad) {
      this.infoRegistroPeriodicidad = infoRegistroPeriodicidad;
   }

   public String getInfoRegistroFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovFormulas:LOVFormulas");
      if (filtradoslistaFormulas != null) {
         if (filtradoslistaFormulas.size() == 1) {
            seleccionFormulas = filtradoslistaFormulas.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();PF('LOVFormulas').selectRow(0);");
         } else {
            seleccionFormulas = null;
            RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();");
         }
      } else {
         seleccionFormulas = null;
         aceptar = true;
      }
      infoRegistroFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulas;
   }

   public void setInfoRegistroFormulas(String infoRegistroFormulas) {
      this.infoRegistroFormulas = infoRegistroFormulas;
   }

   public String getInfoRegistroTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTerceros:LOVTerceros");
      if (filtradoslistaTerceros != null) {
         if (filtradoslistaTerceros.size() == 1) {
            seleccionTerceros = filtradoslistaTerceros.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
         } else {
            seleccionTerceros = null;
            RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
         }
      } else {
         seleccionTerceros = null;
         aceptar = true;
      }
      infoRegistroTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceros;
   }

   public void setInfoRegistroTerceros(String infoRegistroTerceros) {
      this.infoRegistroTerceros = infoRegistroTerceros;
   }

   public String getInfoRegistrosEmpleadosNovedades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
      infoRegistrosEmpleadosNovedades = String.valueOf(tabla.getRowCount());
      return infoRegistrosEmpleadosNovedades;
   }

   public void setInfoRegistrosEmpleadosNovedades(String infoRegistrosEmpleadosNovedades) {
      this.infoRegistrosEmpleadosNovedades = infoRegistrosEmpleadosNovedades;
   }

   public String getInfoRegistroEmpleadosLOV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
      if (filtrarLovEmpleados != null) {
         if (filtrarLovEmpleados.size() == 1) {
            empleadoSeleccionadoLov = filtrarLovEmpleados.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            empleadoSeleccionadoLov = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         empleadoSeleccionadoLov = null;
         aceptar = true;
      }
      infoRegistroEmpleadosLOV = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosLOV;
   }

   public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
      this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
