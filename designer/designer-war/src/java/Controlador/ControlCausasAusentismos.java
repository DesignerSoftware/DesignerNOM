/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Administrar.AdministrarCausasAusentismos;
import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCausasAusentismosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlCausasAusentismos implements Serializable {

   @EJB
   AdministrarCausasAusentismosInterface administrarCausasAusentismos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Causasausentismos> listaCausasAusentismos;
   private List<Causasausentismos> filtrarCausasAusentismos;
   private List<Causasausentismos> listaCausasAusentismosCrear;
   private String mensajeValidacion;
   private List<Causasausentismos> listaCausasAusentismosModificar;
   private List<Causasausentismos> listaCausasAusentismosBorrar;
   //LISTA DE VALORES DE CLASES AUSENTISMOS FALTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   private List<Clasesausentismos> lovClasesAusentismos;
   private List<Clasesausentismos> lovFiltradoClasesAusentismos;
   private Clasesausentismos clasesAusentismosSeleccionado;
   //NUEVO, DUPLICADO EDITAR Y SLECCIONADA
   private Causasausentismos nuevaCausasAusentismos;
   private Causasausentismos duplicarCausasAusentismos;
   private Causasausentismos editarCausasAusentismos;
   private Causasausentismos causasAusentismoSeleccionado;
   public String altoTabla;
   public String infoRegistroClasesausentismos;
   //AutoCompletar
   private boolean permitirIndex, activarBotonLov;
   private String claseAusentismo;
   //Tabla a Imprimir
   private String tablaImprimir, nombreArchivo;
   private Column Codigo, Descripcion, Clase, OrigenIncapacidad, PorcentajeLiquidacion, RestaDiasIncapacidad, FormaLiquidacion,
           Remunerada, DescuentaTiempoContinuo, DescuentaTiempoSoloPS, GarantizaBaseSalario;
   public String infoRegistro;
   ///////////////////////////////////////////////
   //////////PRUEBAS UNITARIAS COMPONENTES////////
   ///////////////////////////////////////////////
   public String buscarNombre;
   public boolean buscador;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private Short codiguin;
   private boolean aceptar, guardado;
   //RASTRO
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlCausasAusentismos() {

      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      listaCausasAusentismos = null;
      listaCausasAusentismosCrear = new ArrayList<Causasausentismos>();
      listaCausasAusentismosModificar = new ArrayList<Causasausentismos>();
      listaCausasAusentismosBorrar = new ArrayList<Causasausentismos>();
      lovClasesAusentismos = new ArrayList<Clasesausentismos>();
      activarBotonLov = true;
      cualCelda = -1;
      nuevaCausasAusentismos = new Causasausentismos();
      nuevaCausasAusentismos.setClase(new Clasesausentismos());
      duplicarCausasAusentismos = new Causasausentismos();
      duplicarCausasAusentismos.setClase(new Clasesausentismos());
      causasAusentismoSeleccionado = null;
      k = 0;
      altoTabla = "270";
      guardado = true;
      buscador = false;
      tablaImprimir = ":formExportar:datosCausasAusentismosExportar";
      nombreArchivo = "CausasAusentismosXML";

      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      getListaCausasAusentismos();
      lovClasesAusentismos = null;
      if (listaCausasAusentismos != null) {
         if (!listaCausasAusentismos.isEmpty()) {
            causasAusentismoSeleccionado = listaCausasAusentismos.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      getListaCausasAusentismos();
      lovClasesAusentismos = null;
      if (listaCausasAusentismos != null) {
         if (!listaCausasAusentismos.isEmpty()) {
            causasAusentismoSeleccionado = listaCausasAusentismos.get(0);
         }
      }
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
         String pagActual = "causaausentismo";
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
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarCausasAusentismos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //ACTIVAR F11
   public void activarCtrlF11() {
      System.out.println("TipoLista= " + tipoLista);
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         System.out.println("Activar");
         Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
         Codigo.setFilterStyle("width: 85% !important");
         Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
         Descripcion.setFilterStyle("width: 85% !important");
         Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
         Clase.setFilterStyle("");
         OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
         OrigenIncapacidad.setFilterStyle("width: 85% !important");
         PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
         PorcentajeLiquidacion.setFilterStyle("width: 85% !important");
         RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
         RestaDiasIncapacidad.setFilterStyle("width: 85% !important");
         FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
         FormaLiquidacion.setFilterStyle("width: 85% !important");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         bandera = 1;
         tipoLista = 1;
         System.out.println("TipoLista= " + tipoLista);
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
         Codigo.setFilterStyle("display: none; visibility: hidden;");
         Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
         Descripcion.setFilterStyle("display: none; visibility: hidden;");
         Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
         Clase.setFilterStyle("display: none; visibility: hidden;");
         OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
         OrigenIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
         PorcentajeLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
         RestaDiasIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
         FormaLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         altoTabla = "270";
         bandera = 0;
         filtrarCausasAusentismos = null;
         tipoLista = 0;
         System.out.println("TipoLista= " + tipoLista);

      }
   }

   //UBICACION CELDA
   public void cambiarIndice(Causasausentismos causa, int celda) {
      if (permitirIndex == true) {
         causasAusentismoSeleccionado = causa;
         cualCelda = celda;
         tablaImprimir = ":formExportar:datosCausasAusentismosExportar";
         nombreArchivo = "CausasAusentismosXML";
         if (tipoLista == 0) {
            deshabilitarBotonLov();
            codiguin = causasAusentismoSeleccionado.getCodigo();
            causasAusentismoSeleccionado.getSecuencia();
            if (cualCelda == 2) {
               habilitarBotonLov();
               claseAusentismo = causasAusentismoSeleccionado.getClase().getDescripcion();
            } else {
               deshabilitarBotonLov();
            }
         } else {
            codiguin = causasAusentismoSeleccionado.getCodigo();
            causasAusentismoSeleccionado.getSecuencia();
            deshabilitarBotonLov();
            if (cualCelda == 2) {
               habilitarBotonLov();
               claseAusentismo = causasAusentismoSeleccionado.getClase().getDescripcion();
            } else {
               deshabilitarBotonLov();
            }
         }
      }

   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (causasAusentismoSeleccionado != null) {
         if (tipoLista == 0) {
            editarCausasAusentismos = causasAusentismoSeleccionado;
         }
         if (tipoLista == 1) {
            editarCausasAusentismos = causasAusentismoSeleccionado;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
            RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarClase");
            RequestContext.getCurrentInstance().execute("PF('editarClase').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeLiquidacion");
            RequestContext.getCurrentInstance().execute("PF('editarPorcentajeLiquidacion').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarRestaDiasIncapacidad");
            RequestContext.getCurrentInstance().execute("PF('editarRestaDiasIncapacidad').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro");
      }
   }

   //SELECCIONAR ORIGEN INCAPACIDAD
   public void seleccionarOrigenIncapacidad(String estadoOrigenIncapacidad, int indice, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();

      if (tipoLista == 0) {
         if (estadoOrigenIncapacidad != null) {
            if (estadoOrigenIncapacidad.equals("AT")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("AT");
            } else if (estadoOrigenIncapacidad.equals("EG")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("EG");
            } else if (estadoOrigenIncapacidad.equals("EP")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("EP");
            } else if (estadoOrigenIncapacidad.equals("MA")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("MA");
            } else if (estadoOrigenIncapacidad.equals("OT")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("OT");
            } else if (estadoOrigenIncapacidad.equals(" ")) {
               causasAusentismoSeleccionado.setOrigenincapacidad(null);
            }
         } else {
            causasAusentismoSeleccionado.setOrigenincapacidad(null);
         }
         if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            if (listaCausasAusentismosModificar.isEmpty()) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            }
         }
      } else {
         if (estadoOrigenIncapacidad != null) {
            if (estadoOrigenIncapacidad.equals("AT")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("AT");
            } else if (estadoOrigenIncapacidad.equals("EG")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("EG");
            } else if (estadoOrigenIncapacidad.equals("EP")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("EP");
            } else if (estadoOrigenIncapacidad.equals("MA")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("MA");
            } else if (estadoOrigenIncapacidad.equals("OT")) {
               causasAusentismoSeleccionado.setOrigenincapacidad("OT");
            } else if (estadoOrigenIncapacidad.equals(" ")) {
               causasAusentismoSeleccionado.setOrigenincapacidad(null);
            }
         } else {
            causasAusentismoSeleccionado.setOrigenincapacidad(null);
         }
         if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            if (listaCausasAusentismosModificar.isEmpty()) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            }
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

      RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
      System.out.println("Subtipo: " + causasAusentismoSeleccionado.getOrigenincapacidad());
   }

   //NUEVO Y DUPLICADO, REGISTRO DE ORIGEN INCAPACIDAD
   public void seleccionarNuevoOrigenIncapacidad(String estadoOrigenIncapacidad, int tipoNuevo) {
      RequestContext context = RequestContext.getCurrentInstance();

      if (tipoNuevo == 1) {
         if (estadoOrigenIncapacidad != null) {
            if (estadoOrigenIncapacidad.equals("AT")) {
               nuevaCausasAusentismos.setOrigenincapacidad("AT");
            } else if (estadoOrigenIncapacidad.equals("EG")) {
               nuevaCausasAusentismos.setOrigenincapacidad("EG");
            } else if (estadoOrigenIncapacidad.equals("EP")) {
               nuevaCausasAusentismos.setOrigenincapacidad("EP");
            } else if (estadoOrigenIncapacidad.equals("MA")) {
               nuevaCausasAusentismos.setOrigenincapacidad("MA");
            } else if (estadoOrigenIncapacidad.equals("OT")) {
               nuevaCausasAusentismos.setOrigenincapacidad("OT");
            } else if (estadoOrigenIncapacidad.equals(" ")) {
               nuevaCausasAusentismos.setOrigenincapacidad(null);
            }
         } else {
            nuevaCausasAusentismos.setOrigenincapacidad(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOrigenIncapacidad");

      } else {
         if (estadoOrigenIncapacidad != null) {
            if (estadoOrigenIncapacidad.equals("AT")) {
               duplicarCausasAusentismos.setOrigenincapacidad("AT");
            } else if (estadoOrigenIncapacidad.equals("EG")) {
               duplicarCausasAusentismos.setOrigenincapacidad("EG");
            } else if (estadoOrigenIncapacidad.equals("EP")) {
               duplicarCausasAusentismos.setOrigenincapacidad("EP");
            } else if (estadoOrigenIncapacidad.equals("MA")) {
               duplicarCausasAusentismos.setOrigenincapacidad("MA");
            } else if (estadoOrigenIncapacidad.equals("OT")) {
               duplicarCausasAusentismos.setOrigenincapacidad("OT");
            } else if (estadoOrigenIncapacidad.equals(" ")) {
               duplicarCausasAusentismos.setOrigenincapacidad(null);
            }
         } else {
            duplicarCausasAusentismos.setOrigenincapacidad(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoOrigenIncapacidad");
      }

   }

   //SELECCIONAR FORMA LIQUIDACION
   public void seleccionarFormaLiquidacion(String estadoFormaLiquidacion, int indice, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();

      if (tipoLista == 0) {
         if (estadoFormaLiquidacion != null) {
            if (estadoFormaLiquidacion.equals("BASICO")) {
               causasAusentismoSeleccionado.setFormaliquidacion("BASICO");
            } else if (estadoFormaLiquidacion.equals("IBC MES ANTERIOR")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES ANTERIOR");
            } else if (estadoFormaLiquidacion.equals("IBC MES ENERO")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES ENERO");
            } else if (estadoFormaLiquidacion.equals("IBC MES INCAPACIDAD")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES INCAPACIDAD");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO ACUMULADOS 12 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO ACUMULADOS 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 12 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO IBC 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 6 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO IBC 6 MESES");
            } else if (estadoFormaLiquidacion.equals(" ")) {
               causasAusentismoSeleccionado.setFormaliquidacion(null);
            }
         } else {
            causasAusentismoSeleccionado.setFormaliquidacion(null);
         }
         if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            if (listaCausasAusentismosModificar.isEmpty()) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            }
         }
      } else {
         if (estadoFormaLiquidacion != null) {
            if (estadoFormaLiquidacion.equals("BASICO")) {
               causasAusentismoSeleccionado.setFormaliquidacion("BASICO");
            } else if (estadoFormaLiquidacion.equals("IBC MES ANTERIOR")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES ANTERIOR");
            } else if (estadoFormaLiquidacion.equals("IBC MES ENERO")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES ENERO");
            } else if (estadoFormaLiquidacion.equals("IBC MES INCAPACIDAD")) {
               causasAusentismoSeleccionado.setFormaliquidacion("IBC MES INCAPACIDAD");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO ACUMULADOS 12 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO ACUMULADOS 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 12 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO IBC 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 6 MESES")) {
               causasAusentismoSeleccionado.setFormaliquidacion("PROMEDIO IBC 6 MESES");
            } else if (estadoFormaLiquidacion.equals(" ")) {
               causasAusentismoSeleccionado.setFormaliquidacion(null);
            }
         } else {
            causasAusentismoSeleccionado.setFormaliquidacion(null);
         }
         if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            if (listaCausasAusentismosModificar.isEmpty()) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            }
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

      RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
      System.out.println("Subtipo: " + causasAusentismoSeleccionado.getFormaliquidacion());
   }

   //NUEVO Y DUPLICADO, REGISTRO DE FORMA LIQUIDACION
   public void seleccionarNuevoFormaLiquidacion(String estadoFormaLiquidacion, int tipoNuevo) {
      RequestContext context = RequestContext.getCurrentInstance();

      if (tipoNuevo == 0) {
         if (estadoFormaLiquidacion != null) {
            if (estadoFormaLiquidacion.equals("BASICO")) {
               nuevaCausasAusentismos.setFormaliquidacion("BASICO");
            } else if (estadoFormaLiquidacion.equals("IBC MES ANTERIOR")) {
               nuevaCausasAusentismos.setFormaliquidacion("IBC MES ANTERIOR");
            } else if (estadoFormaLiquidacion.equals("IBC MES ENERO")) {
               nuevaCausasAusentismos.setFormaliquidacion("IBC MES ENERO");
            } else if (estadoFormaLiquidacion.equals("IBC MES INCAPACIDAD")) {
               nuevaCausasAusentismos.setFormaliquidacion("IBC MES INCAPACIDAD");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO ACUMULADOS 12 MESES")) {
               nuevaCausasAusentismos.setFormaliquidacion("PROMEDIO ACUMULADOS 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 12 MESES")) {
               nuevaCausasAusentismos.setFormaliquidacion("PROMEDIO IBC 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 6 MESES")) {
               nuevaCausasAusentismos.setFormaliquidacion("PROMEDIO IBC 6 MESES");
            } else if (estadoFormaLiquidacion.equals(" ")) {
               nuevaCausasAusentismos.setFormaliquidacion(null);
            }
         } else {
            nuevaCausasAusentismos.setFormaliquidacion(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormaLiquidacion");

      } else {
         if (estadoFormaLiquidacion != null) {
            if (estadoFormaLiquidacion.equals("BASICO")) {
               duplicarCausasAusentismos.setFormaliquidacion("BASICO");
            } else if (estadoFormaLiquidacion.equals("IBC MES ANTERIOR")) {
               duplicarCausasAusentismos.setFormaliquidacion("IBC MES ANTERIOR");
            } else if (estadoFormaLiquidacion.equals("IBC MES ENERO")) {
               duplicarCausasAusentismos.setFormaliquidacion("IBC MES ENERO");
            } else if (estadoFormaLiquidacion.equals("IBC MES INCAPACIDAD")) {
               duplicarCausasAusentismos.setFormaliquidacion("IBC MES INCAPACIDAD");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO ACUMULADOS 12 MESES")) {
               duplicarCausasAusentismos.setFormaliquidacion("PROMEDIO ACUMULADOS 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 12 MESES")) {
               duplicarCausasAusentismos.setFormaliquidacion("PROMEDIO IBC 12 MESES");
            } else if (estadoFormaLiquidacion.equals("PROMEDIO IBC 6 MESES")) {
               duplicarCausasAusentismos.setFormaliquidacion("PROMEDIO IBC 6 MESES");
            } else if (estadoFormaLiquidacion.equals(" ")) {
               duplicarCausasAusentismos.setFormaliquidacion(null);
            }
         } else {
            nuevaCausasAusentismos.setFormaliquidacion(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoFormaLiquidacion");
      }
   }

   //AUTOCOMPLETAR
   public void modificarCausasAusentismos(Causasausentismos causa, String confirmarCambio, String valorConfirmar) {
      causasAusentismoSeleccionado = causa;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      int pasa = 0;
      int pasac = 0;
      int pasaf = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("C")) {
         if (tipoLista == 0) {

            if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
               if (causasAusentismoSeleccionado.getCodigo() != null) {
                  for (int i = 0; i < listaCausasAusentismos.size(); i++) {
                     if (causasAusentismoSeleccionado.getCodigo().compareTo(listaCausasAusentismos.get(i).getCodigo()) == 0) {
                        pasa++;
                     }
                  }
               }
               if (causasAusentismoSeleccionado.getCodigo() == null || (causasAusentismoSeleccionado.getCodigo().toString()).equals("")) {
                  pasac++;
                  System.out.println("pasac: " + pasac);
               }
               if (pasa == 1 && pasac == 0) {
                  System.out.println("pasac es: " + pasac);
                  if (listaCausasAusentismosModificar.isEmpty()) {
                     listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
                  } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
                     listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
                  }

                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
               } else if (pasac != 0) {
                  causasAusentismoSeleccionado.setCodigo(codiguin);
                  RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo2");
                  RequestContext.getCurrentInstance().execute("PF('validacionCodigo2').show()");

               } else if (pasa > 1) {
                  causasAusentismoSeleccionado.setCodigo(codiguin);
                  RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
                  RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
               }
            }
         } else if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            for (int i = 0; i < filtrarCausasAusentismos.size(); i++) {
               if (causasAusentismoSeleccionado.getCodigo().compareTo(filtrarCausasAusentismos.get(i).getCodigo()) == 0) {
                  pasaf++;
               }
            }
            if (causasAusentismoSeleccionado.getCodigo() == null || causasAusentismoSeleccionado.getCodigo().equals("")) {
               pasaf++;
            }
            if (pasaf == 1) {
               if (listaCausasAusentismosCrear.isEmpty()) {
                  listaCausasAusentismosCrear.add(causasAusentismoSeleccionado);
               } else if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
                  listaCausasAusentismosCrear.add(causasAusentismoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }

            } else {
               causasAusentismoSeleccionado.setCodigo(codiguin);
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");

      }
      if (confirmarCambio.equalsIgnoreCase("N")) {

         if (tipoLista == 0) {

            if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {

               if (listaCausasAusentismosModificar.isEmpty()) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               }

               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }

            }
         } else if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {

            if (listaCausasAusentismosCrear.isEmpty()) {
               listaCausasAusentismosCrear.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosCrear.add(causasAusentismoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

         }
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");

      } else if (confirmarCambio.equalsIgnoreCase("CLASESAUSENTISMOS")) {
         if (tipoLista == 0) {
            causasAusentismoSeleccionado.getClase().setDescripcion(claseAusentismo);
         } else {
            causasAusentismoSeleccionado.getClase().setDescripcion(claseAusentismo);
         }
         for (int i = 0; i < lovClasesAusentismos.size(); i++) {
            if (lovClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               causasAusentismoSeleccionado.setClase(lovClasesAusentismos.get(indiceUnicoElemento));
            } else {
               causasAusentismoSeleccionado.setClase(lovClasesAusentismos.get(indiceUnicoElemento));
            }
            lovClasesAusentismos.clear();
            getLovClasesAusentismos();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:clasesAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (tipoLista == 0) {
            if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
               if (listaCausasAusentismosModificar.isEmpty()) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");

               }
            }
         } else if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {

            if (listaCausasAusentismosModificar.isEmpty()) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
               listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
   }

   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
   public void asignarIndex(Causasausentismos causa, int dlg, int LND) {
      causasAusentismoSeleccionado = causa;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      if (dlg == 0) {
         getLovClasesAusentismos();
         contarRegistroLovClases();
         RequestContext.getCurrentInstance().update("formularioDialogos:clasesAusentismosDialogo");
         RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
      }
   }

   //MOSTRAR L.O.V CLASES DE AUSENTISMO   
   public void actualizarClases() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            causasAusentismoSeleccionado.setClase(clasesAusentismosSeleccionado);
            if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
               if (listaCausasAusentismosModificar.isEmpty()) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               }
            }
         } else {
            causasAusentismoSeleccionado.setClase(clasesAusentismosSeleccionado);
            if (!listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
               if (listaCausasAusentismosModificar.isEmpty()) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               } else if (!listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
                  listaCausasAusentismosModificar.add(causasAusentismoSeleccionado);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
      } else if (tipoActualizacion == 1) {
         nuevaCausasAusentismos.setClase(clasesAusentismosSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCausasAusentismos");
      } else if (tipoActualizacion == 2) {
         duplicarCausasAusentismos.setClase(clasesAusentismosSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCausasAusentismos");
      }
      lovFiltradoClasesAusentismos = null;
      clasesAusentismosSeleccionado = null;
      aceptar = true;
      causasAusentismoSeleccionado = null;
      causasAusentismoSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVClasesAusentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formularioDialogos:LOVClasesAusentismos");
   }

   public void cancelarCambioClase() {
      lovFiltradoClasesAusentismos = null;
      clasesAusentismosSeleccionado = null;
      aceptar = true;
      causasAusentismoSeleccionado = null;
      causasAusentismoSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVClasesAusentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (causasAusentismoSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 2) {
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:clasesAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
            tipoActualizacion = 0;
         } else {
            deshabilitarBotonLov();
         }
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCausasAusentismosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CausasAusentismosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      causasAusentismoSeleccionado = null;
      causasAusentismoSeleccionado = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCausasAusentismosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CausasAusentismosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      causasAusentismoSeleccionado = null;
      causasAusentismoSeleccionado = null;
   }

   //LIMPIAR NUEVO REGISTRO CAUSA AUSENTISMO
   public void limpiarNuevaCausaAusentismo() {
      nuevaCausasAusentismos = new Causasausentismos();
      nuevaCausasAusentismos.setClase(new Clasesausentismos());
      nuevaCausasAusentismos.getClase().setDescripcion(" ");
   }

   //LIMPIAR DUPLICAR
   public void limpiarDuplicarCausaAusentismo() {
      duplicarCausasAusentismos = new Causasausentismos();
      duplicarCausasAusentismos.setClase(new Clasesausentismos());
      duplicarCausasAusentismos.getClase().setDescripcion(" ");
   }

   //BORRAR CAUSA AUSENTISMO
   public void borrarCausaAusentismo() {

      if (causasAusentismoSeleccionado != null) {
         if (!listaCausasAusentismosModificar.isEmpty() && listaCausasAusentismosModificar.contains(causasAusentismoSeleccionado)) {
            int modIndex = listaCausasAusentismosModificar.indexOf(causasAusentismoSeleccionado);
            listaCausasAusentismosModificar.remove(modIndex);
            listaCausasAusentismosBorrar.add(causasAusentismoSeleccionado);
         } else if (!listaCausasAusentismosCrear.isEmpty() && listaCausasAusentismosCrear.contains(causasAusentismoSeleccionado)) {
            int crearIndex = listaCausasAusentismosCrear.indexOf(causasAusentismoSeleccionado);
            listaCausasAusentismosCrear.remove(crearIndex);
         } else {
            listaCausasAusentismosBorrar.add(causasAusentismoSeleccionado);
         }
         listaCausasAusentismos.remove(causasAusentismoSeleccionado);
         if (tipoLista == 1) {
            filtrarCausasAusentismos.remove(causasAusentismoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         causasAusentismoSeleccionado = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo) {
      if (tipoNuevo == 1) {
         claseAusentismo = nuevaCausasAusentismos.getClase().getDescripcion();
      } else if (tipoNuevo == 2) {
         claseAusentismo = duplicarCausasAusentismos.getClase().getDescripcion();
      }
   }

   //AUTOCOMPLETAR NUEVO Y DUPLICADO CLASE AUSENTISMO
   public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoNuevo == 1) {
         nuevaCausasAusentismos.getClase().setDescripcion(claseAusentismo);
      } else if (tipoNuevo == 2) {
         duplicarCausasAusentismos.getClase().setDescripcion(claseAusentismo);
      }
      for (int i = 0; i < lovClasesAusentismos.size(); i++) {
         if (lovClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
            indiceUnicoElemento = i;
            coincidencias++;
         }
      }
      if (coincidencias == 1) {
         if (tipoNuevo == 1) {
            nuevaCausasAusentismos.setClase(lovClasesAusentismos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoClase");
         } else if (tipoNuevo == 2) {
            duplicarCausasAusentismos.setClase(lovClasesAusentismos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClase");
         }
         lovClasesAusentismos.clear();
         getLovClasesAusentismos();
      } else {
         RequestContext.getCurrentInstance().update("form:clasesAusentismosDialogo");
         RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
         tipoActualizacion = tipoNuevo;
         if (tipoNuevo == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoClase");
         } else if (tipoNuevo == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClase");
         }
      }
   }

   public void llamarLovClasesAusentismos(int tipoN) {
      if (tipoN == 1) {
         tipoActualizacion = 1;
      } else if (tipoN == 2) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:clasesAusentismosDialogo");
      RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
   }

   // Agregar Nueva Causa Ausentismo
   public void agregarNuevaCausaAusentismo() {

      RequestContext context = RequestContext.getCurrentInstance();
      int pasa = 0;
      mensajeValidacion = " ";
      if (nuevaCausasAusentismos.getCodigo() != null) {
         System.out.println("entra1");
         for (int i = 0; i < listaCausasAusentismos.size(); i++) {
            if (nuevaCausasAusentismos.getCodigo() == listaCausasAusentismos.get(i).getCodigo()) {
               pasa++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
      }
      if (nuevaCausasAusentismos.getCodigo() == null) {
         System.out.println("entra2");
         mensajeValidacion = mensajeValidacion + "   * Codigo\n";
         pasa++;
      }
      if (nuevaCausasAusentismos.getClase().getDescripcion() == null || nuevaCausasAusentismos.getClase().getDescripcion().equals("")) {
         System.out.println("entra3");
         mensajeValidacion = mensajeValidacion + "   * Clase Ausentismo\n";
         pasa++;
      }
      if (pasa == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
            Descripcion.setFilterStyle("display: none; visibility: hidden;");
            Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
            Clase.setFilterStyle("display: none; visibility: hidden;");
            OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
            OrigenIncapacidad.setFilterStyle("display: none; visibility: hidden;");
            PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
            PorcentajeLiquidacion.setFilterStyle("display: none; visibility: hidden;");
            RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
            RestaDiasIncapacidad.setFilterStyle("display: none; visibility: hidden;");
            FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
            FormaLiquidacion.setFilterStyle("display: none; visibility: hidden;");
            Remunerada = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:remunerada");
            Remunerada.setFilterStyle("display: none; visibility: hidden;");
            DescuentaTiempoContinuo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoContinuo");
            DescuentaTiempoContinuo.setFilterStyle("display: none; visibility: hidden;");
            DescuentaTiempoSoloPS = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoSoloPS");
            DescuentaTiempoSoloPS.setFilterStyle("display: none; visibility: hidden;");
            GarantizaBaseSalario = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:garantizaBaseSalario");
            GarantizaBaseSalario.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
            bandera = 0;
            filtrarCausasAusentismos = null;
            tipoLista = 0;

         }
         //AGREGAR REGISTRO A LA LISTA CAUSAS AUSENTISMOS
         k++;
         l = BigInteger.valueOf(k);
         nuevaCausasAusentismos.setSecuencia(l);
         if (nuevaCausasAusentismos.isEstadoRemunerada() == true) {
            nuevaCausasAusentismos.setRemunerada("S");

         } else if (nuevaCausasAusentismos.isEstadoRemunerada() == false) {
            nuevaCausasAusentismos.setRemunerada("N");
         }

         if (nuevaCausasAusentismos.isEstadoDescuentaTiempoContinuo() == true) {
            nuevaCausasAusentismos.setDescuentatiempocontinuo("S");
         } else if (nuevaCausasAusentismos.isEstadoDescuentaTiempoContinuo() == false) {
            nuevaCausasAusentismos.setDescuentatiempocontinuo("N");
         }

         if (nuevaCausasAusentismos.isEstadoRestaTiempoSoloPrestaciones() == true) {
            nuevaCausasAusentismos.setRestatiemposoloprestaciones("S");
         } else if (nuevaCausasAusentismos.isEstadoRestaTiempoSoloPrestaciones() == false) {
            nuevaCausasAusentismos.setRestatiemposoloprestaciones("N");
         }

         if (nuevaCausasAusentismos.isEstadoGarantizaBaseml() == true) {
            nuevaCausasAusentismos.setGarantizabasesml("S");
         } else if (nuevaCausasAusentismos.isEstadoGarantizaBaseml() == false) {
            nuevaCausasAusentismos.setGarantizabasesml("N");
         }

         listaCausasAusentismosCrear.add(nuevaCausasAusentismos);
         listaCausasAusentismos.add(nuevaCausasAusentismos);
         causasAusentismoSeleccionado = nuevaCausasAusentismos;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         nuevaCausasAusentismos = new Causasausentismos();
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCausaAusentismo");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCausaAusentismo').hide()");
      } else if (nuevaCausasAusentismos.getClase().getDescripcion() == null || nuevaCausasAusentismos.getClase().getDescripcion().equals("") || nuevaCausasAusentismos.getCodigo() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCausaAusentismo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCausaAusentismo').show()");
      }
   }

   public void duplicarCausaausentismo() {
      if (causasAusentismoSeleccionado != null) {
         duplicarCausasAusentismos = new Causasausentismos();

         if (tipoLista == 0) {
            duplicarCausasAusentismos.setCodigo(causasAusentismoSeleccionado.getCodigo());
            duplicarCausasAusentismos.setDescripcion(causasAusentismoSeleccionado.getDescripcion());
            duplicarCausasAusentismos.setClase(causasAusentismoSeleccionado.getClase());
            duplicarCausasAusentismos.setOrigenincapacidad(causasAusentismoSeleccionado.getOrigenincapacidad());
            duplicarCausasAusentismos.setPorcentajeliquidacion(causasAusentismoSeleccionado.getPorcentajeliquidacion());
            duplicarCausasAusentismos.setRestadiasincapacidad(causasAusentismoSeleccionado.getRestadiasincapacidad());
            duplicarCausasAusentismos.setFormaliquidacion(causasAusentismoSeleccionado.getFormaliquidacion());
            duplicarCausasAusentismos.setRemunerada(causasAusentismoSeleccionado.getRemunerada());
            duplicarCausasAusentismos.setDescuentatiempocontinuo(causasAusentismoSeleccionado.getDescuentatiempocontinuo());
            duplicarCausasAusentismos.setRestatiemposoloprestaciones(causasAusentismoSeleccionado.getRestatiemposoloprestaciones());
            duplicarCausasAusentismos.setGarantizabasesml(causasAusentismoSeleccionado.getGarantizabasesml());
         }
         if (tipoLista == 1) {
            duplicarCausasAusentismos.setCodigo(causasAusentismoSeleccionado.getCodigo());
            duplicarCausasAusentismos.setDescripcion(causasAusentismoSeleccionado.getDescripcion());
            duplicarCausasAusentismos.setClase(causasAusentismoSeleccionado.getClase());
            duplicarCausasAusentismos.setOrigenincapacidad(causasAusentismoSeleccionado.getOrigenincapacidad());
            duplicarCausasAusentismos.setPorcentajeliquidacion(causasAusentismoSeleccionado.getPorcentajeliquidacion());
            duplicarCausasAusentismos.setRestadiasincapacidad(causasAusentismoSeleccionado.getRestadiasincapacidad());
            duplicarCausasAusentismos.setFormaliquidacion(causasAusentismoSeleccionado.getFormaliquidacion());
            duplicarCausasAusentismos.setRemunerada(causasAusentismoSeleccionado.getRemunerada());
            duplicarCausasAusentismos.setDescuentatiempocontinuo(causasAusentismoSeleccionado.getDescuentatiempocontinuo());
            duplicarCausasAusentismos.setRestatiemposoloprestaciones(causasAusentismoSeleccionado.getRestatiemposoloprestaciones());
            duplicarCausasAusentismos.setGarantizabasesml(causasAusentismoSeleccionado.getGarantizabasesml());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCausaAusentismo");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCausaAusentismo').show()");
      }
   }

   public void confirmarDuplicar() {

      //int pasaA = 0;
      int pasa = 0;
      k++;
      l = BigInteger.valueOf(k);
      duplicarCausasAusentismos.setSecuencia(l);
      RequestContext context = RequestContext.getCurrentInstance();

      if (duplicarCausasAusentismos.getCodigo() != null) {
         for (int i = 0; i < listaCausasAusentismos.size(); i++) {
            if (duplicarCausasAusentismos.getCodigo() == listaCausasAusentismos.get(i).getCodigo()) {
               pasa++;
               RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
               RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            }
         }
      }
      if (duplicarCausasAusentismos.getCodigo() == null) {
         mensajeValidacion = mensajeValidacion + "   * Codigo\n";
         pasa++;
      }
      if (duplicarCausasAusentismos.getClase().getDescripcion() == null || duplicarCausasAusentismos.getClase().getDescripcion().equals("")) {
         mensajeValidacion = mensajeValidacion + "   * Clase Ausentismo\n";
         pasa++;
      }

      if (pasa == 0) {
         causasAusentismoSeleccionado = null;
         causasAusentismoSeleccionado = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
            Descripcion.setFilterStyle("display: none; visibility: hidden;");
            Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
            Clase.setFilterStyle("display: none; visibility: hidden;");
            OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
            OrigenIncapacidad.setFilterStyle("display: none; visibility: hidden;");
            PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
            PorcentajeLiquidacion.setFilterStyle("display: none; visibility: hidden;");
            RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
            RestaDiasIncapacidad.setFilterStyle("display: none; visibility: hidden;");
            FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
            FormaLiquidacion.setFilterStyle("display: none; visibility: hidden;");
            Remunerada = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:remunerada");
            Remunerada.setFilterStyle("display: none; visibility: hidden;");
            DescuentaTiempoContinuo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoContinuo");
            DescuentaTiempoContinuo.setFilterStyle("display: none; visibility: hidden;");
            DescuentaTiempoSoloPS = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoSoloPS");
            DescuentaTiempoSoloPS.setFilterStyle("display: none; visibility: hidden;");
            GarantizaBaseSalario = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:garantizaBaseSalario");
            GarantizaBaseSalario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
            altoTabla = "270";
            bandera = 0;
            filtrarCausasAusentismos = null;
            System.out.println("TipoLista= " + tipoLista);
            tipoLista = 0;
         }

         listaCausasAusentismos.add(duplicarCausasAusentismos);
         listaCausasAusentismosCrear.add(duplicarCausasAusentismos);
         causasAusentismoSeleccionado = duplicarCausasAusentismos;
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         duplicarCausasAusentismos = new Causasausentismos();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCausaAusentismo");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCausaAusentismo').hide()");

      } else if (duplicarCausasAusentismos.getClase().getDescripcion() == null || duplicarCausasAusentismos.getClase().getDescripcion().equals("") || duplicarCausasAusentismos.getCodigo() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCausaAusentismo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCausaAusentismo').show()");
      }

   }

   //VERIFICAR RASTRO
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (causasAusentismoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(causasAusentismoSeleccionado.getSecuencia(), "CAUSASAUSENTISMOS");
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("CAUSASAUSENTISMOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      causasAusentismoSeleccionado = null;
   }

   //REFRESCAR LA PAGINA, CANCELAR MODIFICACION SI NO SE A GUARDADO
   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         System.out.println("Desactivar");
         Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
         Codigo.setFilterStyle("display: none; visibility: hidden;");
         Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
         Descripcion.setFilterStyle("display: none; visibility: hidden;");
         Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
         Clase.setFilterStyle("display: none; visibility: hidden;");
         OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
         OrigenIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
         PorcentajeLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
         RestaDiasIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
         FormaLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         Remunerada = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:remunerada");
         Remunerada.setFilterStyle("display: none; visibility: hidden;");
         DescuentaTiempoContinuo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoContinuo");
         DescuentaTiempoContinuo.setFilterStyle("display: none; visibility: hidden;");
         DescuentaTiempoSoloPS = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoSoloPS");
         DescuentaTiempoSoloPS.setFilterStyle("display: none; visibility: hidden;");
         GarantizaBaseSalario = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:garantizaBaseSalario");
         GarantizaBaseSalario.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         altoTabla = "270";
         bandera = 0;
         filtrarCausasAusentismos = null;
         tipoLista = 0;
         System.out.println("TipoLista= " + tipoLista);
      }

      listaCausasAusentismosBorrar.clear();
      listaCausasAusentismosCrear.clear();
      listaCausasAusentismosModificar.clear();
      causasAusentismoSeleccionado = null;
      k = 0;
      listaCausasAusentismos = null;
      getListaCausasAusentismos();
      for (int i = 0; i < listaCausasAusentismos.size(); i++) {
         System.out.println("Posicion: " + i + "Tiene en origen: " + listaCausasAusentismos.get(i).getOrigenincapacidad());
      }
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
      RequestContext.getCurrentInstance().update("form:informacionRegistro");

   }

   //MÉTODO SALIR DE LA PAGINA ACTUAL
   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         System.out.println("Desactivar");
         Codigo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:codigo");
         Codigo.setFilterStyle("display: none; visibility: hidden;");
         Descripcion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descripcion");
         Descripcion.setFilterStyle("display: none; visibility: hidden;");
         Clase = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:clase");
         Clase.setFilterStyle("display: none; visibility: hidden;");
         OrigenIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:origenIncapacidad");
         OrigenIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         PorcentajeLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:porcentajeLiquidacion");
         PorcentajeLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         RestaDiasIncapacidad = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:restaDiasIncapacidad");
         RestaDiasIncapacidad.setFilterStyle("display: none; visibility: hidden;");
         FormaLiquidacion = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:formaLiquidacion");
         FormaLiquidacion.setFilterStyle("display: none; visibility: hidden;");
         Remunerada = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:remunerada");
         Remunerada.setFilterStyle("display: none; visibility: hidden;");
         DescuentaTiempoContinuo = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoContinuo");
         DescuentaTiempoContinuo.setFilterStyle("display: none; visibility: hidden;");
         DescuentaTiempoSoloPS = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:descuentaTiempoSoloPS");
         DescuentaTiempoSoloPS.setFilterStyle("display: none; visibility: hidden;");
         GarantizaBaseSalario = (Column) c.getViewRoot().findComponent("form:datosCausasAusentismos:garantizaBaseSalario");
         GarantizaBaseSalario.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
         altoTabla = "270";
         bandera = 0;
         filtrarCausasAusentismos = null;
         tipoLista = 0;
         System.out.println("TipoLista= " + tipoLista);

      }

      listaCausasAusentismosBorrar.clear();
      listaCausasAusentismosCrear.clear();
      listaCausasAusentismosModificar.clear();
      causasAusentismoSeleccionado = null;
      k = 0;
      listaCausasAusentismos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //GUARDAR
   public void guardarCambiosCausaAusentismo() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            System.out.println("Realizando Operaciones Unidades");
            if (!listaCausasAusentismosBorrar.isEmpty()) {
               administrarCausasAusentismos.borrarCausasAusentismos(listaCausasAusentismosBorrar);
               System.out.println("Entra");
               listaCausasAusentismosBorrar.clear();
            }
            if (!listaCausasAusentismosCrear.isEmpty()) {
               administrarCausasAusentismos.crearCausasAusentismos(listaCausasAusentismosCrear);
               listaCausasAusentismosCrear.clear();
            }
            if (!listaCausasAusentismosModificar.isEmpty()) {
               administrarCausasAusentismos.modificarCausasAusentismos(listaCausasAusentismosModificar);
               listaCausasAusentismosModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaCausasAusentismos = null;
            getListaCausasAusentismos();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosCausasAusentismos");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            causasAusentismoSeleccionado = null;
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //EVENTO FILTRAR
   public void eventofiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
   }

   public void contarRegistroLovClases() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroClasesAusentismos");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarBotonLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarBotonLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //Getter & Setters
   public List<Causasausentismos> getListaCausasAusentismos() {
      if (listaCausasAusentismos == null) {
         listaCausasAusentismos = administrarCausasAusentismos.consultarCausasAusentismos();
      }

      return listaCausasAusentismos;
   }

   public void setListaCausasAusentismos(List<Causasausentismos> listaCausasAusentismos) {
      this.listaCausasAusentismos = listaCausasAusentismos;
   }

   public List<Causasausentismos> getFiltrarCausasAusentismos() {
      return filtrarCausasAusentismos;
   }

   public void setFiltrarCausasAusentismos(List<Causasausentismos> filtrarCausasAusentismos) {
      this.filtrarCausasAusentismos = filtrarCausasAusentismos;
   }

   public List<Causasausentismos> getModificarCausasAusentismos() {
      return listaCausasAusentismosModificar;
   }

   public void setModificarCausasAusentismos(List<Causasausentismos> modificarCausasAusentismos) {
      this.listaCausasAusentismosModificar = modificarCausasAusentismos;
   }

   public List<Causasausentismos> getBorrarCausasAusentismos() {
      return listaCausasAusentismosBorrar;
   }

   public void setBorrarCausasAusentismos(List<Causasausentismos> borrarCausasAusentismos) {
      this.listaCausasAusentismosBorrar = borrarCausasAusentismos;
   }

   public List<Clasesausentismos> getLovClasesAusentismos() {
      if (lovClasesAusentismos == null) {
         lovClasesAusentismos = administrarCausasAusentismos.consultarClasesAusentismos();
      }
      return lovClasesAusentismos;
   }

   public void setLovClasesAusentismos(List<Clasesausentismos> lovClasesAusentismos) {
      this.lovClasesAusentismos = lovClasesAusentismos;
   }

   public List<Clasesausentismos> getLovFiltradoClasesAusentismos() {
      return lovFiltradoClasesAusentismos;
   }

   public void setLovFiltradoClasesAusentismos(List<Clasesausentismos> lovFiltradoClasesAusentismos) {
      this.lovFiltradoClasesAusentismos = lovFiltradoClasesAusentismos;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistroCausasausentismos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVClasesAusentismos");
      infoRegistroClasesausentismos = String.valueOf(tabla.getRowCount());
      return infoRegistroClasesausentismos;
   }

   public void setInfoRegistroCausasausentismos(String infoRegistroCausasausentismos) {
      this.infoRegistroClasesausentismos = infoRegistroCausasausentismos;
   }

   public String getTablaImprimir() {
      return tablaImprimir;
   }

   public void setTablaImprimir(String tablaImprimir) {
      this.tablaImprimir = tablaImprimir;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCausasAusentismos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Causasausentismos getEditarCausasAusentismos() {
      return editarCausasAusentismos;
   }

   public void setEditarCausasAusentismos(Causasausentismos editarCausasAusentismos) {
      this.editarCausasAusentismos = editarCausasAusentismos;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public void setNombreArchivo(String nombreArchivo) {
      this.nombreArchivo = nombreArchivo;
   }

   public Causasausentismos getCausasAusentismoSeleccionado() {
      return causasAusentismoSeleccionado;
   }

   public void setCausasAusentismoSeleccionado(Causasausentismos causasAusentismoSeleccionado) {
      this.causasAusentismoSeleccionado = causasAusentismoSeleccionado;
   }

   public Clasesausentismos getClasesAusentismosSeleccionado() {
      return clasesAusentismosSeleccionado;
   }

   public void setClasesAusentismosSeleccionado(Clasesausentismos clasesAusentismosSeleccionado) {
      this.clasesAusentismosSeleccionado = clasesAusentismosSeleccionado;
   }

   public Causasausentismos getNuevaCausasAusentismos() {
      return nuevaCausasAusentismos;
   }

   public void setNuevaCausasAusentismos(Causasausentismos nuevaCausasAusentismos) {
      this.nuevaCausasAusentismos = nuevaCausasAusentismos;
   }

   public Causasausentismos getDuplicarCausasAusentismos() {
      return duplicarCausasAusentismos;
   }

   public void setDuplicarCausasAusentismos(Causasausentismos duplicarCausasAusentismos) {
      this.duplicarCausasAusentismos = duplicarCausasAusentismos;
   }

   public boolean isActivarBotonLov() {
      return activarBotonLov;
   }

   public void setActivarBotonLov(boolean activarBotonLov) {
      this.activarBotonLov = activarBotonLov;
   }

}
