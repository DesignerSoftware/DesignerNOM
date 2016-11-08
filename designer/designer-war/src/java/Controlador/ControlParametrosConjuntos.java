/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.ParametrosConjuntos;
import Entidades.VWDSolucionesNodosN;
import Entidades.VWDSolucionesNodosNDetalle;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarParametrosConjuntosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.security.auth.message.callback.PrivateKeyCallback;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlParametrosConjuntos implements Serializable {

   @EJB
   AdministrarParametrosConjuntosInterface administrarParametrosConjuntos;

   //Parametros Actuales segun el Usuario
   private ParametrosConjuntos parametrosActuales;
//    private int vistaActual;
   //Tablas Principales:
   private List<VWDSolucionesNodosN> listaEstadisticas;
   private List<VWDSolucionesNodosN> filtradoEstadisticas;
   private VWDSolucionesNodosN estadisticaSeleccionada;
   private List<VWDSolucionesNodosN> listaEstadisticasLB;
   private List<VWDSolucionesNodosN> filtradoEstadisticasLB;
   private VWDSolucionesNodosN estadisticaLBSeleccionada;
//   private List<VWDSolucionesNodosN> totalesEstadisticas;
//   private List<VWDSolucionesNodosN> totalesEstadisticasLB;
   private List<VWDSolucionesNodosN> porcentajesExportar;
   private Map<String, BigDecimal> porcentajesVariacion;
   private String altoTabla;
   private String altoTablaDetalles;
   private VWDSolucionesNodosN obVacio;
   //Listas de detalles
   private List<VWDSolucionesNodosNDetalle> listaDetalles;
   private List<VWDSolucionesNodosNDetalle> filtradoDetalles;
   private BigInteger valorTotalDetalles;
   private BigDecimal totalUnidadesDetalles;
   private String descripcionDetalle;
   //Lista de conceptos
   private List<Conceptos> listaConceptos;
   private List<Conceptos> listaConceptosEspecificos;
   private List<Conceptos> filtradoConceptos;
   private Conceptos conceptoSeleccionado;
   private Map<String, String> conjuntoC;
   //Navegabilidad
   private String paginaAnterior;
   //Conteo de Registros
   private int bandera;
   private int tipoLista;
   private int tipoListaLB;
   private String infoRegistro;
   private String infoRegistroLB;
   private String infoRegistroDetalles;
   private String infoRegistroConceptos;
   private int cualCelda;
   //Parametros
   private String dimension;
   private String periodicidad;
   //columnas para el filtrado
   private Column eDimension;
   private Column eC1;
   private Column eC2;
   private Column eC3;
   private Column eC4;
   private Column eC5;
   private Column eC6;
   private Column eC7;
   private Column eC8;
   private Column eC9;
   private Column eC10;
   private Column eC11;
   private Column eC12;
   private Column eC13;
   private Column eC14;
   private Column eC15;
   private Column eC16;
   private Column eC17;
   private Column eC18;
   private Column eC19;
   private Column eC20;
   private Column eC21;
   private Column eC22;
   private Column eC23;
   private Column eC24;
   private Column eC25;
   private Column eC26;
   private Column eC27;
   private Column eC28;
   private Column eC29;
   private Column eC30;
   private Column eC31;
   private Column eC32;
   private Column eC33;
   private Column eC34;
   private Column eC35;
   private Column eC36;
   private Column eC37;
   private Column eC38;
   private Column eC39;
   private Column eC40;
   private Column eC41;
   private Column eC42;
   private Column eC43;
   private Column eC44;
   private Column eC45;
   private Column eTP;
   private Column eCTD;
   private Column eCN;
   private Column eCTG;
   private Column eCTPvos;
   private Column eCGT;
   //LB
   private Column eLBDimension;
   private Column eLBC1;
   private Column eLBC2;
   private Column eLBC3;
   private Column eLBC4;
   private Column eLBC5;
   private Column eLBC6;
   private Column eLBC7;
   private Column eLBC8;
   private Column eLBC9;
   private Column eLBC10;
   private Column eLBC11;
   private Column eLBC12;
   private Column eLBC13;
   private Column eLBC14;
   private Column eLBC15;
   private Column eLBC16;
   private Column eLBC17;
   private Column eLBC18;
   private Column eLBC19;
   private Column eLBC20;
   private Column eLBC21;
   private Column eLBC22;
   private Column eLBC23;
   private Column eLBC24;
   private Column eLBC25;
   private Column eLBC26;
   private Column eLBC27;
   private Column eLBC28;
   private Column eLBC29;
   private Column eLBC30;
   private Column eLBC31;
   private Column eLBC32;
   private Column eLBC33;
   private Column eLBC34;
   private Column eLBC35;
   private Column eLBC36;
   private Column eLBC37;
   private Column eLBC38;
   private Column eLBC39;
   private Column eLBC40;
   private Column eLBC41;
   private Column eLBC42;
   private Column eLBC43;
   private Column eLBC44;
   private Column eLBC45;
   private Column eLBTP;
   private Column eLBCTD;
   private Column eLBCN;
   private Column eLBCTG;
   private Column eLBCTPvos;
   private Column eLBCGT;

   private InputText input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15,
           input16, input17, input18, input19, input20, input21, input22, input23, input24, input25, input26, input27, input28, input29, input30,
           input31, input32, input33, input34, input35, input36, input37, input38, input39, input40, input41, input42, input43, input44, input45;

   // Editar : 
   private String tituloEditar;
   private String textoEditar;
   //Exportar
   private String nombreXML;
   private String tablaXML;
   //Totales
   VWDSolucionesNodosN diferencia;
   VWDSolucionesNodosN totalesLB;
   VWDSolucionesNodosN totales;

   private boolean seleccionPorcentajes;

   /**
    * Creates a new instance of ControlParametrosConjuntos
    */
   public ControlParametrosConjuntos() {
      parametrosActuales = null;
      //Tablas Principales:
      listaEstadisticas = new ArrayList<VWDSolucionesNodosN>();
      filtradoEstadisticas = null;
      estadisticaSeleccionada = null;
      listaEstadisticasLB = new ArrayList<VWDSolucionesNodosN>();
      filtradoEstadisticasLB = null;
      estadisticaLBSeleccionada = null;
      obVacio = new VWDSolucionesNodosN();
      altoTabla = "75";
      //Listas de detalles
      listaDetalles = new ArrayList<VWDSolucionesNodosNDetalle>();
      valorTotalDetalles = new BigInteger("0");
      totalUnidadesDetalles = new BigDecimal(0);
      //Lista de conceptos
      listaConceptos = new ArrayList<Conceptos>();
      listaConceptosEspecificos = new ArrayList<Conceptos>();
      filtradoConceptos = new ArrayList<Conceptos>();
      conceptoSeleccionado = null;
      //Navegabilidad
      paginaAnterior = "";
      tipoLista = 0;
      tipoListaLB = 0;
      infoRegistro = "0";
      infoRegistroLB = "0";
      infoRegistroDetalles = "0";
      infoRegistroConceptos = "0";
      bandera = 0;
      //Parametros
      dimension = "";
      periodicidad = "";
      cualCelda = -1;
      descripcionDetalle = " ";
      conjuntoC = new LinkedHashMap<String, String>();
      int i = 0;
      conjuntoC.put("45", "45");
      while (i <= 43) {
         i++;
         conjuntoC.put("" + i + "", "" + i + "");
      }
      porcentajesVariacion = new LinkedHashMap<String, BigDecimal>();

      tituloEditar = "";
      textoEditar = "";
      nombreXML = "EstadisticasXML";
      tablaXML = ":formExportar:tablaEstadisticasExportar";
      seleccionPorcentajes = false;
      porcentajesExportar = new ArrayList<VWDSolucionesNodosN>();

      totales = new VWDSolucionesNodosN();
      totalesLB = new VWDSolucionesNodosN();
      diferencia = new VWDSolucionesNodosN();
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarParametrosConjuntos.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaAnterior(String pagina) {
      cargarParametros();
      paginaAnterior = pagina;
   }

   public String retornarPagina() {
      parametrosActuales = null;
      //Tablas Principales:
      listaEstadisticas = new ArrayList<VWDSolucionesNodosN>();
      filtradoEstadisticas = null;
      estadisticaSeleccionada = null;
      listaEstadisticasLB = new ArrayList<VWDSolucionesNodosN>();
      filtradoEstadisticasLB = null;
      estadisticaLBSeleccionada = null;
      obVacio = new VWDSolucionesNodosN();
      porcentajesVariacion.clear();
      //Listas de detalles
      listaDetalles = new ArrayList<VWDSolucionesNodosNDetalle>();
      valorTotalDetalles = new BigInteger("0");
      totalUnidadesDetalles = new BigDecimal(0);
      //Lista de conceptos
      listaConceptos = new ArrayList<Conceptos>();
      filtradoConceptos = new ArrayList<Conceptos>();
      conceptoSeleccionado = null;
      //Navegabilidad
      tipoLista = 0;
      tipoListaLB = 0;
      infoRegistro = "0";
      infoRegistroLB = "0";
      infoRegistroDetalles = "0";
      infoRegistroConceptos = "0";
      bandera = 0;
      //Parametros
      dimension = "";
      periodicidad = "";
      textoEditar = "";
      tituloEditar = "";

      seleccionPorcentajes = false;
      return paginaAnterior;
   }

   public void cargarParametros() {
      parametrosActuales = null;
      parametrosActuales = administrarParametrosConjuntos.consultarParametros();
   }

   public void modificarParametrosActuales() {
      administrarParametrosConjuntos.editarParametros(parametrosActuales);

      listaEstadisticas.clear();
      listaEstadisticasLB.clear();
      totales = new VWDSolucionesNodosN();
      totalesLB = new VWDSolucionesNodosN();
      diferencia = new VWDSolucionesNodosN();
      porcentajesVariacion.clear();

      tipoLista = 0;
      tipoListaLB = 0;
      bandera = 0;
      restablecerTablas();
      despintarPorcentajes();
      FacesMessage msg = new FacesMessage("Información", "Se guardaron los parametros con éxito");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
      contarRegistrosLB();
      contarRegistros();
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotales");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotalesLB");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
   }

   //Se guardan las modificaciones de los porcentajes de Desalarización
   public void modificarParametrosActualesDes() {
      administrarParametrosConjuntos.editarParametros(parametrosActuales);

      tipoLista = 0;
      tipoListaLB = 0;
      bandera = 0;
      restablecerTablas();
      contarRegistros();
      contarRegistrosLB();
      pintarPorcentajes();

      FacesMessage msg = new FacesMessage("Información", "Se guardaron los porcentajes de desviacion con éxito");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotales");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotalesLB");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
   }

   public void cargarEstadisticas() {
      listaEstadisticas = administrarParametrosConjuntos.consultarDSolucionesNodosN(parametrosActuales.getDimension(), parametrosActuales.getFechaHasta());
      if (listaEstadisticas != null) {
         totales = new VWDSolucionesNodosN();
         for (int i = 0; i < listaEstadisticas.size(); i++) {
            totales.setConjunto1(totales.getConjunto1().add(listaEstadisticas.get(i).getConjunto1()));
            totales.setConjunto2(totales.getConjunto2().add(listaEstadisticas.get(i).getConjunto2()));
            totales.setConjunto3(totales.getConjunto3().add(listaEstadisticas.get(i).getConjunto3()));
            totales.setConjunto4(totales.getConjunto4().add(listaEstadisticas.get(i).getConjunto4()));
            totales.setConjunto5(totales.getConjunto5().add(listaEstadisticas.get(i).getConjunto5()));
            totales.setConjunto6(totales.getConjunto6().add(listaEstadisticas.get(i).getConjunto6()));
            totales.setConjunto7(totales.getConjunto7().add(listaEstadisticas.get(i).getConjunto7()));
            totales.setConjunto8(totales.getConjunto8().add(listaEstadisticas.get(i).getConjunto8()));
            totales.setConjunto9(totales.getConjunto9().add(listaEstadisticas.get(i).getConjunto9()));
            totales.setConjunto10(totales.getConjunto10().add(listaEstadisticas.get(i).getConjunto10()));
            totales.setConjunto11(totales.getConjunto11().add(listaEstadisticas.get(i).getConjunto11()));
            totales.setConjunto12(totales.getConjunto12().add(listaEstadisticas.get(i).getConjunto12()));
            totales.setConjunto13(totales.getConjunto13().add(listaEstadisticas.get(i).getConjunto13()));
            totales.setConjunto14(totales.getConjunto14().add(listaEstadisticas.get(i).getConjunto14()));
            totales.setConjunto15(totales.getConjunto15().add(listaEstadisticas.get(i).getConjunto15()));
            totales.setConjunto16(totales.getConjunto16().add(listaEstadisticas.get(i).getConjunto16()));
            totales.setConjunto17(totales.getConjunto17().add(listaEstadisticas.get(i).getConjunto17()));
            totales.setConjunto18(totales.getConjunto18().add(listaEstadisticas.get(i).getConjunto18()));
            totales.setConjunto19(totales.getConjunto19().add(listaEstadisticas.get(i).getConjunto19()));
            totales.setConjunto20(totales.getConjunto20().add(listaEstadisticas.get(i).getConjunto20()));
            totales.setConjunto21(totales.getConjunto21().add(listaEstadisticas.get(i).getConjunto21()));
            totales.setConjunto22(totales.getConjunto22().add(listaEstadisticas.get(i).getConjunto22()));
            totales.setConjunto23(totales.getConjunto23().add(listaEstadisticas.get(i).getConjunto23()));
            totales.setConjunto24(totales.getConjunto24().add(listaEstadisticas.get(i).getConjunto24()));
            totales.setConjunto25(totales.getConjunto25().add(listaEstadisticas.get(i).getConjunto25()));
            totales.setConjunto26(totales.getConjunto26().add(listaEstadisticas.get(i).getConjunto26()));
            totales.setConjunto27(totales.getConjunto27().add(listaEstadisticas.get(i).getConjunto27()));
            totales.setConjunto28(totales.getConjunto28().add(listaEstadisticas.get(i).getConjunto28()));
            totales.setConjunto29(totales.getConjunto29().add(listaEstadisticas.get(i).getConjunto29()));
            totales.setConjunto30(totales.getConjunto30().add(listaEstadisticas.get(i).getConjunto30()));
            totales.setConjunto31(totales.getConjunto31().add(listaEstadisticas.get(i).getConjunto31()));
            totales.setConjunto32(totales.getConjunto32().add(listaEstadisticas.get(i).getConjunto32()));
            totales.setConjunto33(totales.getConjunto33().add(listaEstadisticas.get(i).getConjunto33()));
            totales.setConjunto34(totales.getConjunto34().add(listaEstadisticas.get(i).getConjunto34()));
            totales.setConjunto35(totales.getConjunto35().add(listaEstadisticas.get(i).getConjunto35()));
            totales.setConjunto36(totales.getConjunto36().add(listaEstadisticas.get(i).getConjunto36()));
            totales.setConjunto37(totales.getConjunto37().add(listaEstadisticas.get(i).getConjunto37()));
            totales.setConjunto38(totales.getConjunto38().add(listaEstadisticas.get(i).getConjunto38()));
            totales.setConjunto39(totales.getConjunto39().add(listaEstadisticas.get(i).getConjunto39()));
            totales.setConjunto40(totales.getConjunto40().add(listaEstadisticas.get(i).getConjunto40()));
            totales.setConjunto41(totales.getConjunto41().add(listaEstadisticas.get(i).getConjunto41()));
            totales.setConjunto42(totales.getConjunto42().add(listaEstadisticas.get(i).getConjunto42()));
            totales.setConjunto43(totales.getConjunto43().add(listaEstadisticas.get(i).getConjunto43()));
            totales.setConjunto44(totales.getConjunto44().add(listaEstadisticas.get(i).getConjunto44()));
            totales.setConjunto45(totales.getConjunto45().add(listaEstadisticas.get(i).getConjunto45()));
         }
      }

      if (!listaEstadisticas.isEmpty() && !listaEstadisticasLB.isEmpty()) {
         VWDSolucionesNodosN diferencia = new VWDSolucionesNodosN();
         diferencia.setConjunto1(totales.getConjunto1().subtract(totalesLB.getConjunto1()));
         diferencia.setConjunto2(totales.getConjunto2().subtract(totalesLB.getConjunto2()));
         diferencia.setConjunto3(totales.getConjunto3().subtract(totalesLB.getConjunto3()));
         diferencia.setConjunto4(totales.getConjunto4().subtract(totalesLB.getConjunto4()));
         diferencia.setConjunto5(totales.getConjunto5().subtract(totalesLB.getConjunto5()));
         diferencia.setConjunto6(totales.getConjunto6().subtract(totalesLB.getConjunto6()));
         diferencia.setConjunto7(totales.getConjunto7().subtract(totalesLB.getConjunto7()));
         diferencia.setConjunto8(totales.getConjunto8().subtract(totalesLB.getConjunto8()));
         diferencia.setConjunto9(totales.getConjunto9().subtract(totalesLB.getConjunto9()));
         diferencia.setConjunto10(totales.getConjunto10().subtract(totalesLB.getConjunto10()));
         diferencia.setConjunto11(totales.getConjunto11().subtract(totalesLB.getConjunto11()));
         diferencia.setConjunto12(totales.getConjunto12().subtract(totalesLB.getConjunto12()));
         diferencia.setConjunto13(totales.getConjunto13().subtract(totalesLB.getConjunto13()));
         diferencia.setConjunto14(totales.getConjunto14().subtract(totalesLB.getConjunto14()));
         diferencia.setConjunto15(totales.getConjunto15().subtract(totalesLB.getConjunto15()));
         diferencia.setConjunto16(totales.getConjunto16().subtract(totalesLB.getConjunto16()));
         diferencia.setConjunto17(totales.getConjunto17().subtract(totalesLB.getConjunto17()));
         diferencia.setConjunto18(totales.getConjunto18().subtract(totalesLB.getConjunto18()));
         diferencia.setConjunto19(totales.getConjunto19().subtract(totalesLB.getConjunto19()));
         diferencia.setConjunto20(totales.getConjunto20().subtract(totalesLB.getConjunto20()));
         diferencia.setConjunto21(totales.getConjunto21().subtract(totalesLB.getConjunto21()));
         diferencia.setConjunto22(totales.getConjunto22().subtract(totalesLB.getConjunto22()));
         diferencia.setConjunto23(totales.getConjunto23().subtract(totalesLB.getConjunto23()));
         diferencia.setConjunto24(totales.getConjunto24().subtract(totalesLB.getConjunto24()));
         diferencia.setConjunto25(totales.getConjunto25().subtract(totalesLB.getConjunto25()));
         diferencia.setConjunto26(totales.getConjunto26().subtract(totalesLB.getConjunto26()));
         diferencia.setConjunto27(totales.getConjunto27().subtract(totalesLB.getConjunto27()));
         diferencia.setConjunto28(totales.getConjunto28().subtract(totalesLB.getConjunto28()));
         diferencia.setConjunto29(totales.getConjunto29().subtract(totalesLB.getConjunto29()));
         diferencia.setConjunto30(totales.getConjunto30().subtract(totalesLB.getConjunto30()));
         diferencia.setConjunto31(totales.getConjunto31().subtract(totalesLB.getConjunto31()));
         diferencia.setConjunto32(totales.getConjunto32().subtract(totalesLB.getConjunto32()));
         diferencia.setConjunto33(totales.getConjunto33().subtract(totalesLB.getConjunto33()));
         diferencia.setConjunto34(totales.getConjunto34().subtract(totalesLB.getConjunto34()));
         diferencia.setConjunto35(totales.getConjunto35().subtract(totalesLB.getConjunto35()));
         diferencia.setConjunto36(totales.getConjunto36().subtract(totalesLB.getConjunto36()));
         diferencia.setConjunto37(totales.getConjunto37().subtract(totalesLB.getConjunto37()));
         diferencia.setConjunto38(totales.getConjunto38().subtract(totalesLB.getConjunto38()));
         diferencia.setConjunto39(totales.getConjunto39().subtract(totalesLB.getConjunto39()));
         diferencia.setConjunto40(totales.getConjunto40().subtract(totalesLB.getConjunto40()));
         diferencia.setConjunto41(totales.getConjunto41().subtract(totalesLB.getConjunto41()));
         diferencia.setConjunto42(totales.getConjunto42().subtract(totalesLB.getConjunto42()));
         diferencia.setConjunto43(totales.getConjunto43().subtract(totalesLB.getConjunto43()));
         diferencia.setConjunto44(totales.getConjunto44().subtract(totalesLB.getConjunto44()));
         diferencia.setConjunto45(totales.getConjunto45().subtract(totalesLB.getConjunto45()));
         cargaPorcentajes();
      }
      RequestContext.getCurrentInstance().update("form:tablaEstadisticas");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotales");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasTotalesLB");
//      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
      contarRegistros();
   }

   public void cargarEstadisticasLB() {
      listaEstadisticasLB = administrarParametrosConjuntos.consultarDSolucionesNodosNLB(parametrosActuales.getDimension(), parametrosActuales.getFechaHasta());
      totalesLB = new VWDSolucionesNodosN();
      diferencia = new VWDSolucionesNodosN();
      if (listaEstadisticasLB != null) {
         for (int i = 0; i < listaEstadisticasLB.size(); i++) {
            totalesLB.setConjunto1(totalesLB.getConjunto1().add(listaEstadisticasLB.get(i).getConjunto1()));
            totalesLB.setConjunto2(totalesLB.getConjunto2().add(listaEstadisticasLB.get(i).getConjunto2()));
            totalesLB.setConjunto3(totalesLB.getConjunto3().add(listaEstadisticasLB.get(i).getConjunto3()));
            totalesLB.setConjunto4(totalesLB.getConjunto4().add(listaEstadisticasLB.get(i).getConjunto4()));
            totalesLB.setConjunto5(totalesLB.getConjunto5().add(listaEstadisticasLB.get(i).getConjunto5()));
            totalesLB.setConjunto6(totalesLB.getConjunto6().add(listaEstadisticasLB.get(i).getConjunto6()));
            totalesLB.setConjunto7(totalesLB.getConjunto7().add(listaEstadisticasLB.get(i).getConjunto7()));
            totalesLB.setConjunto8(totalesLB.getConjunto8().add(listaEstadisticasLB.get(i).getConjunto8()));
            totalesLB.setConjunto9(totalesLB.getConjunto9().add(listaEstadisticasLB.get(i).getConjunto9()));
            totalesLB.setConjunto10(totalesLB.getConjunto10().add(listaEstadisticasLB.get(i).getConjunto10()));
            totalesLB.setConjunto11(totalesLB.getConjunto11().add(listaEstadisticasLB.get(i).getConjunto11()));
            totalesLB.setConjunto12(totalesLB.getConjunto12().add(listaEstadisticasLB.get(i).getConjunto12()));
            totalesLB.setConjunto13(totalesLB.getConjunto13().add(listaEstadisticasLB.get(i).getConjunto13()));
            totalesLB.setConjunto14(totalesLB.getConjunto14().add(listaEstadisticasLB.get(i).getConjunto14()));
            totalesLB.setConjunto15(totalesLB.getConjunto15().add(listaEstadisticasLB.get(i).getConjunto15()));
            totalesLB.setConjunto16(totalesLB.getConjunto16().add(listaEstadisticasLB.get(i).getConjunto16()));
            totalesLB.setConjunto17(totalesLB.getConjunto17().add(listaEstadisticasLB.get(i).getConjunto17()));
            totalesLB.setConjunto18(totalesLB.getConjunto18().add(listaEstadisticasLB.get(i).getConjunto18()));
            totalesLB.setConjunto19(totalesLB.getConjunto19().add(listaEstadisticasLB.get(i).getConjunto19()));
            totalesLB.setConjunto20(totalesLB.getConjunto20().add(listaEstadisticasLB.get(i).getConjunto20()));
            totalesLB.setConjunto21(totalesLB.getConjunto21().add(listaEstadisticasLB.get(i).getConjunto21()));
            totalesLB.setConjunto22(totalesLB.getConjunto22().add(listaEstadisticasLB.get(i).getConjunto22()));
            totalesLB.setConjunto23(totalesLB.getConjunto23().add(listaEstadisticasLB.get(i).getConjunto23()));
            totalesLB.setConjunto24(totalesLB.getConjunto24().add(listaEstadisticasLB.get(i).getConjunto24()));
            totalesLB.setConjunto25(totalesLB.getConjunto25().add(listaEstadisticasLB.get(i).getConjunto25()));
            totalesLB.setConjunto26(totalesLB.getConjunto26().add(listaEstadisticasLB.get(i).getConjunto26()));
            totalesLB.setConjunto27(totalesLB.getConjunto27().add(listaEstadisticasLB.get(i).getConjunto27()));
            totalesLB.setConjunto28(totalesLB.getConjunto28().add(listaEstadisticasLB.get(i).getConjunto28()));
            totalesLB.setConjunto29(totalesLB.getConjunto29().add(listaEstadisticasLB.get(i).getConjunto29()));
            totalesLB.setConjunto30(totalesLB.getConjunto30().add(listaEstadisticasLB.get(i).getConjunto30()));
            totalesLB.setConjunto31(totalesLB.getConjunto31().add(listaEstadisticasLB.get(i).getConjunto31()));
            totalesLB.setConjunto32(totalesLB.getConjunto32().add(listaEstadisticasLB.get(i).getConjunto32()));
            totalesLB.setConjunto33(totalesLB.getConjunto33().add(listaEstadisticasLB.get(i).getConjunto33()));
            totalesLB.setConjunto34(totalesLB.getConjunto34().add(listaEstadisticasLB.get(i).getConjunto34()));
            totalesLB.setConjunto35(totalesLB.getConjunto35().add(listaEstadisticasLB.get(i).getConjunto35()));
            totalesLB.setConjunto36(totalesLB.getConjunto36().add(listaEstadisticasLB.get(i).getConjunto36()));
            totalesLB.setConjunto37(totalesLB.getConjunto37().add(listaEstadisticasLB.get(i).getConjunto37()));
            totalesLB.setConjunto38(totalesLB.getConjunto38().add(listaEstadisticasLB.get(i).getConjunto38()));
            totalesLB.setConjunto39(totalesLB.getConjunto39().add(listaEstadisticasLB.get(i).getConjunto39()));
            totalesLB.setConjunto40(totalesLB.getConjunto40().add(listaEstadisticasLB.get(i).getConjunto40()));
            totalesLB.setConjunto41(totalesLB.getConjunto41().add(listaEstadisticasLB.get(i).getConjunto41()));
            totalesLB.setConjunto42(totalesLB.getConjunto42().add(listaEstadisticasLB.get(i).getConjunto42()));
            totalesLB.setConjunto43(totalesLB.getConjunto43().add(listaEstadisticasLB.get(i).getConjunto43()));
            totalesLB.setConjunto44(totalesLB.getConjunto44().add(listaEstadisticasLB.get(i).getConjunto44()));
            totalesLB.setConjunto45(totalesLB.getConjunto45().add(listaEstadisticasLB.get(i).getConjunto45()));
         }
      }

      if (!listaEstadisticas.isEmpty() && !listaEstadisticasLB.isEmpty()) {
         diferencia.setConjunto1(totales.getConjunto1().subtract(totalesLB.getConjunto1()));
         diferencia.setConjunto2(totales.getConjunto2().subtract(totalesLB.getConjunto2()));
         diferencia.setConjunto3(totales.getConjunto3().subtract(totalesLB.getConjunto3()));
         diferencia.setConjunto4(totales.getConjunto4().subtract(totalesLB.getConjunto4()));
         diferencia.setConjunto5(totales.getConjunto5().subtract(totalesLB.getConjunto5()));
         diferencia.setConjunto6(totales.getConjunto6().subtract(totalesLB.getConjunto6()));
         diferencia.setConjunto7(totales.getConjunto7().subtract(totalesLB.getConjunto7()));
         diferencia.setConjunto8(totales.getConjunto8().subtract(totalesLB.getConjunto8()));
         diferencia.setConjunto9(totales.getConjunto9().subtract(totalesLB.getConjunto9()));
         diferencia.setConjunto10(totales.getConjunto10().subtract(totalesLB.getConjunto10()));
         diferencia.setConjunto11(totales.getConjunto11().subtract(totalesLB.getConjunto11()));
         diferencia.setConjunto12(totales.getConjunto12().subtract(totalesLB.getConjunto12()));
         diferencia.setConjunto13(totales.getConjunto13().subtract(totalesLB.getConjunto13()));
         diferencia.setConjunto14(totales.getConjunto14().subtract(totalesLB.getConjunto14()));
         diferencia.setConjunto15(totales.getConjunto15().subtract(totalesLB.getConjunto15()));
         diferencia.setConjunto16(totales.getConjunto16().subtract(totalesLB.getConjunto16()));
         diferencia.setConjunto17(totales.getConjunto17().subtract(totalesLB.getConjunto17()));
         diferencia.setConjunto18(totales.getConjunto18().subtract(totalesLB.getConjunto18()));
         diferencia.setConjunto19(totales.getConjunto19().subtract(totalesLB.getConjunto19()));
         diferencia.setConjunto20(totales.getConjunto20().subtract(totalesLB.getConjunto20()));
         diferencia.setConjunto21(totales.getConjunto21().subtract(totalesLB.getConjunto21()));
         diferencia.setConjunto22(totales.getConjunto22().subtract(totalesLB.getConjunto22()));
         diferencia.setConjunto23(totales.getConjunto23().subtract(totalesLB.getConjunto23()));
         diferencia.setConjunto24(totales.getConjunto24().subtract(totalesLB.getConjunto24()));
         diferencia.setConjunto25(totales.getConjunto25().subtract(totalesLB.getConjunto25()));
         diferencia.setConjunto26(totales.getConjunto26().subtract(totalesLB.getConjunto26()));
         diferencia.setConjunto27(totales.getConjunto27().subtract(totalesLB.getConjunto27()));
         diferencia.setConjunto28(totales.getConjunto28().subtract(totalesLB.getConjunto28()));
         diferencia.setConjunto29(totales.getConjunto29().subtract(totalesLB.getConjunto29()));
         diferencia.setConjunto30(totales.getConjunto30().subtract(totalesLB.getConjunto30()));
         diferencia.setConjunto31(totales.getConjunto31().subtract(totalesLB.getConjunto31()));
         diferencia.setConjunto32(totales.getConjunto32().subtract(totalesLB.getConjunto32()));
         diferencia.setConjunto33(totales.getConjunto33().subtract(totalesLB.getConjunto33()));
         diferencia.setConjunto34(totales.getConjunto34().subtract(totalesLB.getConjunto34()));
         diferencia.setConjunto35(totales.getConjunto35().subtract(totalesLB.getConjunto35()));
         diferencia.setConjunto36(totales.getConjunto36().subtract(totalesLB.getConjunto36()));
         diferencia.setConjunto37(totales.getConjunto37().subtract(totalesLB.getConjunto37()));
         diferencia.setConjunto38(totales.getConjunto38().subtract(totalesLB.getConjunto38()));
         diferencia.setConjunto39(totales.getConjunto39().subtract(totalesLB.getConjunto39()));
         diferencia.setConjunto40(totales.getConjunto40().subtract(totalesLB.getConjunto40()));
         diferencia.setConjunto41(totales.getConjunto41().subtract(totalesLB.getConjunto41()));
         diferencia.setConjunto42(totales.getConjunto42().subtract(totalesLB.getConjunto42()));
         diferencia.setConjunto43(totales.getConjunto43().subtract(totalesLB.getConjunto43()));
         diferencia.setConjunto44(totales.getConjunto44().subtract(totalesLB.getConjunto44()));
         diferencia.setConjunto45(totales.getConjunto45().subtract(totalesLB.getConjunto45()));

         cargaPorcentajes();
      }
      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
      contarRegistrosLB();
   }

   public void cargaPorcentajes() {
      BigDecimal cien = new BigDecimal(100);
      BigDecimal cero = new BigDecimal("0.00");
      porcentajesVariacion.clear();

      if (totalesLB.getConjunto1().doubleValue() != 0) {
         porcentajesVariacion.put("1", ((diferencia.getConjunto1()).divide(totalesLB.getConjunto1().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("1", cero);
      }
      if (totalesLB.getConjunto2().doubleValue() != 0) {
         porcentajesVariacion.put("2", ((diferencia.getConjunto2()).divide(totalesLB.getConjunto2().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("2", cero);
      }
      if (totalesLB.getConjunto3().doubleValue() != 0) {
         porcentajesVariacion.put("3", ((diferencia.getConjunto3()).divide(totalesLB.getConjunto3().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("3", cero);
      }
      if (totalesLB.getConjunto4().doubleValue() != 0) {
         porcentajesVariacion.put("4", ((diferencia.getConjunto4()).divide(totalesLB.getConjunto4().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("4", cero);
      }
      if (totalesLB.getConjunto5().doubleValue() != 0) {
         porcentajesVariacion.put("5", ((diferencia.getConjunto5()).divide(totalesLB.getConjunto5().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("5", cero);
      }
      if (totalesLB.getConjunto6().doubleValue() != 0) {
         porcentajesVariacion.put("6", ((diferencia.getConjunto6()).divide(totalesLB.getConjunto6().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("6", cero);
      }
      if (totalesLB.getConjunto7().doubleValue() != 0) {
         porcentajesVariacion.put("7", ((diferencia.getConjunto7()).divide(totalesLB.getConjunto7().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("7", cero);
      }
      if (totalesLB.getConjunto8().doubleValue() != 0) {
         porcentajesVariacion.put("8", ((diferencia.getConjunto8()).divide(totalesLB.getConjunto8().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("8", cero);
      }
      if (totalesLB.getConjunto9().doubleValue() != 0) {
         porcentajesVariacion.put("9", ((diferencia.getConjunto9()).divide(totalesLB.getConjunto9().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("9", cero);
      }
      if (totalesLB.getConjunto10().doubleValue() != 0) {
         porcentajesVariacion.put("10", ((diferencia.getConjunto10()).divide(totalesLB.getConjunto10().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("10", cero);
      }
      if (totalesLB.getConjunto11().doubleValue() != 0) {
         porcentajesVariacion.put("11", ((diferencia.getConjunto11()).divide(totalesLB.getConjunto11().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("11", cero);
      }
      if (totalesLB.getConjunto12().doubleValue() != 0) {
         porcentajesVariacion.put("12", ((diferencia.getConjunto12()).divide(totalesLB.getConjunto12().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("12", cero);
      }
      if (totalesLB.getConjunto13().doubleValue() != 0) {
         porcentajesVariacion.put("13", ((diferencia.getConjunto13()).divide(totalesLB.getConjunto13().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("13", cero);
      }
      if (totalesLB.getConjunto14().doubleValue() != 0) {
         porcentajesVariacion.put("14", ((diferencia.getConjunto14()).divide(totalesLB.getConjunto14().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("14", cero);
      }
      if (totalesLB.getConjunto15().doubleValue() != 0) {
         porcentajesVariacion.put("15", ((diferencia.getConjunto15()).divide(totalesLB.getConjunto15().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("15", cero);
      }
      if (totalesLB.getConjunto16().doubleValue() != 0) {
         porcentajesVariacion.put("16", ((diferencia.getConjunto16()).divide(totalesLB.getConjunto16().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("16", cero);
      }
      if (totalesLB.getConjunto17().doubleValue() != 0) {
         porcentajesVariacion.put("17", ((diferencia.getConjunto17()).divide(totalesLB.getConjunto17().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("17", cero);
      }
      if (totalesLB.getConjunto18().doubleValue() != 0) {
         porcentajesVariacion.put("18", ((diferencia.getConjunto18()).divide(totalesLB.getConjunto18().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("18", cero);
      }
      if (totalesLB.getConjunto19().doubleValue() != 0) {
         porcentajesVariacion.put("19", ((diferencia.getConjunto19()).divide(totalesLB.getConjunto19().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("19", cero);
      }
      if (totalesLB.getConjunto20().doubleValue() != 0) {
         porcentajesVariacion.put("20", ((diferencia.getConjunto20()).divide(totalesLB.getConjunto20().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("20", cero);
      }
      if (totalesLB.getConjunto21().doubleValue() != 0) {
         porcentajesVariacion.put("21", ((diferencia.getConjunto21()).divide(totalesLB.getConjunto21().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("21", cero);
      }
      if (totalesLB.getConjunto22().doubleValue() != 0) {
         porcentajesVariacion.put("22", ((diferencia.getConjunto22()).divide(totalesLB.getConjunto22().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("22", cero);
      }
      if (totalesLB.getConjunto23().doubleValue() != 0) {
         porcentajesVariacion.put("23", ((diferencia.getConjunto23()).divide(totalesLB.getConjunto23().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("23", cero);
      }
      if (totalesLB.getConjunto24().doubleValue() != 0) {
         porcentajesVariacion.put("24", ((diferencia.getConjunto24()).divide(totalesLB.getConjunto24().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("24", cero);
      }
      if (totalesLB.getConjunto25().doubleValue() != 0) {
         porcentajesVariacion.put("25", ((diferencia.getConjunto25()).divide(totalesLB.getConjunto25().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("25", cero);
      }
      if (totalesLB.getConjunto26().doubleValue() != 0) {
         porcentajesVariacion.put("26", ((diferencia.getConjunto26()).divide(totalesLB.getConjunto26().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("26", cero);
      }
      if (totalesLB.getConjunto27().doubleValue() != 0) {
         porcentajesVariacion.put("27", ((diferencia.getConjunto27()).divide(totalesLB.getConjunto27().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("27", cero);
      }
      if (totalesLB.getConjunto28().doubleValue() != 0) {
         porcentajesVariacion.put("28", ((diferencia.getConjunto28()).divide(totalesLB.getConjunto28().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("28", cero);
      }
      if (totalesLB.getConjunto29().doubleValue() != 0) {
         porcentajesVariacion.put("29", ((diferencia.getConjunto29()).divide(totalesLB.getConjunto29().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("29", cero);
      }
      if (totalesLB.getConjunto30().doubleValue() != 0) {
         porcentajesVariacion.put("30", ((diferencia.getConjunto30()).divide(totalesLB.getConjunto30().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("30", cero);
      }
      if (totalesLB.getConjunto31().doubleValue() != 0) {
         porcentajesVariacion.put("31", ((diferencia.getConjunto31()).divide(totalesLB.getConjunto31().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("31", cero);
      }
      if (totalesLB.getConjunto32().doubleValue() != 0) {
         porcentajesVariacion.put("32", ((diferencia.getConjunto32()).divide(totalesLB.getConjunto32().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("32", cero);
      }
      if (totalesLB.getConjunto33().doubleValue() != 0) {
         porcentajesVariacion.put("33", ((diferencia.getConjunto33()).divide(totalesLB.getConjunto33().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("33", cero);
      }
      if (totalesLB.getConjunto34().doubleValue() != 0) {
         porcentajesVariacion.put("34", ((diferencia.getConjunto34()).divide(totalesLB.getConjunto34().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("34", cero);
      }
      if (totalesLB.getConjunto35().doubleValue() != 0) {
         porcentajesVariacion.put("35", ((diferencia.getConjunto35()).divide(totalesLB.getConjunto35().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("35", cero);
      }
      if (totalesLB.getConjunto36().doubleValue() != 0) {
         porcentajesVariacion.put("36", ((diferencia.getConjunto36()).divide(totalesLB.getConjunto36().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("36", cero);
      }
      if (totalesLB.getConjunto37().doubleValue() != 0) {
         porcentajesVariacion.put("37", ((diferencia.getConjunto37()).divide(totalesLB.getConjunto37().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("37", cero);
      }
      if (totalesLB.getConjunto38().doubleValue() != 0) {
         porcentajesVariacion.put("38", ((diferencia.getConjunto38()).divide(totalesLB.getConjunto38().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("38", cero);
      }
      if (totalesLB.getConjunto39().doubleValue() != 0) {
         porcentajesVariacion.put("39", ((diferencia.getConjunto39()).divide(totalesLB.getConjunto39().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("39", cero);
      }
      if (totalesLB.getConjunto40().doubleValue() != 0) {
         porcentajesVariacion.put("40", ((diferencia.getConjunto40()).divide(totalesLB.getConjunto40().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("40", cero);
      }
      if (totalesLB.getConjunto41().doubleValue() != 0) {
         porcentajesVariacion.put("41", ((diferencia.getConjunto41()).divide(totalesLB.getConjunto41().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("41", cero);
      }
      if (totalesLB.getConjunto42().doubleValue() != 0) {
         porcentajesVariacion.put("42", ((diferencia.getConjunto42()).divide(totalesLB.getConjunto42().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("42", cero);
      }
      if (totalesLB.getConjunto43().doubleValue() != 0) {
         porcentajesVariacion.put("43", ((diferencia.getConjunto43()).divide(totalesLB.getConjunto43().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("43", cero);
      }
      if (totalesLB.getConjunto44().doubleValue() != 0) {
         porcentajesVariacion.put("44", ((diferencia.getConjunto44()).divide(totalesLB.getConjunto44().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("44", cero);
      }
      if (totalesLB.getConjunto45().doubleValue() != 0) {
         porcentajesVariacion.put("45", ((diferencia.getConjunto45()).divide(totalesLB.getConjunto45().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("45", cero);
      }

      if (totalesLB.getTotalPagos().doubleValue() != 0) {
         porcentajesVariacion.put("TP", ((diferencia.getTotalPagos()).divide(totalesLB.getTotalPagos().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("TP", cero);
      }
      if (totalesLB.getTotalDescuentos().doubleValue() != 0) {
         porcentajesVariacion.put("TD", ((diferencia.getTotalDescuentos()).divide(totalesLB.getTotalDescuentos().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("TD", cero);
      }
      if (totalesLB.getTotalGastos().doubleValue() != 0) {
         porcentajesVariacion.put("TG", ((diferencia.getTotalGastos()).divide(totalesLB.getTotalGastos().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("TG", cero);
      }
      if (totalesLB.getTotalPasivos().doubleValue() != 0) {
         porcentajesVariacion.put("TPvos", ((diferencia.getTotalPasivos()).divide(totalesLB.getTotalPasivos().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("TPvos", cero);
      }
      if (totalesLB.getNetos().doubleValue() != 0) {
         porcentajesVariacion.put("N", ((diferencia.getNetos()).divide(totalesLB.getNetos().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("N", cero);
      }
      if (totalesLB.getGranTotal().doubleValue() != 0) {
         porcentajesVariacion.put("GT", ((diferencia.getGranTotal()).divide(totalesLB.getGranTotal().divide(cien), 2, RoundingMode.CEILING)));
      } else {
         porcentajesVariacion.put("GT", cero);
      }
      pintarPorcentajes();
   }

   public void pintarPorcentajes() {

      FacesContext c = FacesContext.getCurrentInstance();
      input1 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input1");
      input2 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input2");
      input3 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input3");
      input4 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input4");
      input5 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input5");
      input6 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input6");
      input7 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input7");
      input8 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input8");
      input9 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input9");
      input10 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input10");
      input11 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input11");
      input12 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input12");
      input13 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input13");
      input14 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input14");
      input15 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input15");
      input16 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input16");
      input17 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input17");
      input18 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input18");
      input19 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input19");
      input20 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input20");
      input21 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input21");
      input22 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input22");
      input23 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input23");
      input24 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input24");
      input25 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input25");
      input26 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input26");
      input27 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input27");
      input28 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input28");
      input29 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input29");
      input30 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input30");
      input31 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input31");
      input32 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input32");
      input33 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input33");
      input34 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input34");
      input35 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input35");
      input36 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input36");
      input37 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input37");
      input38 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input38");
      input39 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input39");
      input40 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input40");
      input41 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input41");
      input42 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input42");
      input43 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input43");
      input44 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input44");
      input45 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input45");

      String estilo = "width: 45% !important; display: inline-block !important; padding: 0px !important; height: 13px !important;";
      String estiloNaranja = " background-color: #FDAB1C !important;";
      String estiloVerde = " background-color: #89fd89 !important;";
      String estiloRojo = " background-color: red !important; color: white !important;";

      if ((parametrosActuales.getDesviacion1().doubleValue() == 0) || (porcentajesVariacion.get("1").doubleValue() == 0)) {
         input1.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion1().compareTo(porcentajesVariacion.get("1")) == -1) {
         input1.setStyle(estilo + estiloRojo);
      } else {
         input1.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion2().doubleValue() == 0) || (porcentajesVariacion.get("2").doubleValue() == 0)) {
         input2.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion2().compareTo(porcentajesVariacion.get("2")) == -1) {
         input2.setStyle(estilo + estiloRojo);
      } else {
         input2.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion3().doubleValue() == 0) || (porcentajesVariacion.get("3").doubleValue() == 0)) {
         input3.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion3().compareTo(porcentajesVariacion.get("3")) == -1) {
         input3.setStyle(estilo + estiloRojo);
      } else {
         input3.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion4().doubleValue() == 0) || (porcentajesVariacion.get("4").doubleValue() == 0)) {
         input4.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion4().compareTo(porcentajesVariacion.get("4")) == -1) {
         input4.setStyle(estilo + estiloRojo);
      } else {
         input4.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion5().doubleValue() == 0) || (porcentajesVariacion.get("5").doubleValue() == 0)) {
         input5.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion5().compareTo(porcentajesVariacion.get("5")) == -1) {
         input5.setStyle(estilo + estiloRojo);
      } else {
         input5.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion6().doubleValue() == 0) || (porcentajesVariacion.get("6").doubleValue() == 0)) {
         input6.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion6().compareTo(porcentajesVariacion.get("6")) == -1) {
         input6.setStyle(estilo + estiloRojo);
      } else {
         input6.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion7().doubleValue() == 0) || (porcentajesVariacion.get("7").doubleValue() == 0)) {
         input7.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion7().compareTo(porcentajesVariacion.get("7")) == -1) {
         input7.setStyle(estilo + estiloRojo);
      } else {
         input7.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion8().doubleValue() == 0) || (porcentajesVariacion.get("8").doubleValue() == 0)) {
         input8.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion8().compareTo(porcentajesVariacion.get("8")) == -1) {
         input8.setStyle(estilo + estiloRojo);
      } else {
         input8.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion9().doubleValue() == 0) || (porcentajesVariacion.get("9").doubleValue() == 0)) {
         input9.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion9().compareTo(porcentajesVariacion.get("9")) == -1) {
         input9.setStyle(estilo + estiloRojo);
      } else {
         input9.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion10().doubleValue() == 0) || (porcentajesVariacion.get("10").doubleValue() == 0)) {
         input10.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion10().compareTo(porcentajesVariacion.get("10")) == -1) {
         input10.setStyle(estilo + estiloRojo);
      } else {
         input10.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion11().doubleValue() == 0) || (porcentajesVariacion.get("11").doubleValue() == 0)) {
         input11.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion11().compareTo(porcentajesVariacion.get("11")) == -1) {
         input11.setStyle(estilo + estiloRojo);
      } else {
         input11.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion12().doubleValue() == 0) || (porcentajesVariacion.get("12").doubleValue() == 0)) {
         input12.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion12().compareTo(porcentajesVariacion.get("12")) == -1) {
         input12.setStyle(estilo + estiloRojo);
      } else {
         input12.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion13().doubleValue() == 0) || (porcentajesVariacion.get("13").doubleValue() == 0)) {
         input13.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion13().compareTo(porcentajesVariacion.get("13")) == -1) {
         input13.setStyle(estilo + estiloRojo);
      } else {
         input13.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion14().doubleValue() == 0) || (porcentajesVariacion.get("14").doubleValue() == 0)) {
         input14.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion14().compareTo(porcentajesVariacion.get("14")) == -1) {
         input14.setStyle(estilo + estiloRojo);
      } else {
         input14.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion15().doubleValue() == 0) || (porcentajesVariacion.get("15").doubleValue() == 0)) {
         input15.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion15().compareTo(porcentajesVariacion.get("15")) == -1) {
         input15.setStyle(estilo + estiloRojo);
      } else {
         input15.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion16().doubleValue() == 0) || (porcentajesVariacion.get("16").doubleValue() == 0)) {
         input16.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion16().compareTo(porcentajesVariacion.get("16")) == -1) {
         input16.setStyle(estilo + estiloRojo);
      } else {
         input16.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion17().doubleValue() == 0) || (porcentajesVariacion.get("17").doubleValue() == 0)) {
         input17.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion17().compareTo(porcentajesVariacion.get("17")) == -1) {
         input17.setStyle(estilo + estiloRojo);
      } else {
         input17.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion18().doubleValue() == 0) || (porcentajesVariacion.get("18").doubleValue() == 0)) {
         input18.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion18().compareTo(porcentajesVariacion.get("18")) == -1) {
         input18.setStyle(estilo + estiloRojo);
      } else {
         input18.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion19().doubleValue() == 0) || (porcentajesVariacion.get("19").doubleValue() == 0)) {
         input19.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion19().compareTo(porcentajesVariacion.get("19")) == -1) {
         input19.setStyle(estilo + estiloRojo);
      } else {
         input19.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion20().doubleValue() == 0) || (porcentajesVariacion.get("20").doubleValue() == 0)) {
         input20.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion20().compareTo(porcentajesVariacion.get("20")) == -1) {
         input20.setStyle(estilo + estiloRojo);
      } else {
         input20.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion21().doubleValue() == 0) || (porcentajesVariacion.get("21").doubleValue() == 0)) {
         input21.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion21().compareTo(porcentajesVariacion.get("21")) == -1) {
         input21.setStyle(estilo + estiloRojo);
      } else {
         input21.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion22().doubleValue() == 0) || (porcentajesVariacion.get("22").doubleValue() == 0)) {
         input22.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion22().compareTo(porcentajesVariacion.get("22")) == -1) {
         input22.setStyle(estilo + estiloRojo);
      } else {
         input22.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion23().doubleValue() == 0) || (porcentajesVariacion.get("23").doubleValue() == 0)) {
         input23.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion23().compareTo(porcentajesVariacion.get("23")) == -1) {
         input23.setStyle(estilo + estiloRojo);
      } else {
         input23.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion24().doubleValue() == 0) || (porcentajesVariacion.get("24").doubleValue() == 0)) {
         input24.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion24().compareTo(porcentajesVariacion.get("24")) == -1) {
         input24.setStyle(estilo + estiloRojo);
      } else {
         input24.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion25().doubleValue() == 0) || (porcentajesVariacion.get("25").doubleValue() == 0)) {
         input25.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion25().compareTo(porcentajesVariacion.get("25")) == -1) {
         input25.setStyle(estilo + estiloRojo);
      } else {
         input25.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion26().doubleValue() == 0) || (porcentajesVariacion.get("26").doubleValue() == 0)) {
         input26.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion26().compareTo(porcentajesVariacion.get("26")) == -1) {
         input26.setStyle(estilo + estiloRojo);
      } else {
         input26.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion27().doubleValue() == 0) || (porcentajesVariacion.get("27").doubleValue() == 0)) {
         input27.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion27().compareTo(porcentajesVariacion.get("27")) == -1) {
         input27.setStyle(estilo + estiloRojo);
      } else {
         input27.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion28().doubleValue() == 0) || (porcentajesVariacion.get("28").doubleValue() == 0)) {
         input28.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion28().compareTo(porcentajesVariacion.get("28")) == -1) {
         input28.setStyle(estilo + estiloRojo);
      } else {
         input28.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion29().doubleValue() == 0) || (porcentajesVariacion.get("29").doubleValue() == 0)) {
         input29.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion29().compareTo(porcentajesVariacion.get("29")) == -1) {
         input29.setStyle(estilo + estiloRojo);
      } else {
         input29.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion30().doubleValue() == 0) || (porcentajesVariacion.get("30").doubleValue() == 0)) {
         input30.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion30().compareTo(porcentajesVariacion.get("30")) == -1) {
         input30.setStyle(estilo + estiloRojo);
      } else {
         input30.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion31().doubleValue() == 0) || (porcentajesVariacion.get("31").doubleValue() == 0)) {
         input31.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion31().compareTo(porcentajesVariacion.get("31")) == -1) {
         input31.setStyle(estilo + estiloRojo);
      } else {
         input31.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion32().doubleValue() == 0) || (porcentajesVariacion.get("32").doubleValue() == 0)) {
         input32.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion32().compareTo(porcentajesVariacion.get("32")) == -1) {
         input32.setStyle(estilo + estiloRojo);
      } else {
         input32.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion33().doubleValue() == 0) || (porcentajesVariacion.get("33").doubleValue() == 0)) {
         input33.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion33().compareTo(porcentajesVariacion.get("33")) == -1) {
         input33.setStyle(estilo + estiloRojo);
      } else {
         input33.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion34().doubleValue() == 0) || (porcentajesVariacion.get("34").doubleValue() == 0)) {
         input34.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion34().compareTo(porcentajesVariacion.get("34")) == -1) {
         input34.setStyle(estilo + estiloRojo);
      } else {
         input34.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion35().doubleValue() == 0) || (porcentajesVariacion.get("35").doubleValue() == 0)) {
         input35.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion35().compareTo(porcentajesVariacion.get("35")) == -1) {
         input35.setStyle(estilo + estiloRojo);
      } else {
         input35.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion36().doubleValue() == 0) || (porcentajesVariacion.get("36").doubleValue() == 0)) {
         input36.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion36().compareTo(porcentajesVariacion.get("36")) == -1) {
         input36.setStyle(estilo + estiloRojo);
      } else {
         input36.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion37().doubleValue() == 0) || (porcentajesVariacion.get("37").doubleValue() == 0)) {
         input37.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion37().compareTo(porcentajesVariacion.get("37")) == -1) {
         input37.setStyle(estilo + estiloRojo);
      } else {
         input37.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion38().doubleValue() == 0) || (porcentajesVariacion.get("38").doubleValue() == 0)) {
         input38.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion38().compareTo(porcentajesVariacion.get("38")) == -1) {
         input38.setStyle(estilo + estiloRojo);
      } else {
         input38.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion39().doubleValue() == 0) || (porcentajesVariacion.get("39").doubleValue() == 0)) {
         input39.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion39().compareTo(porcentajesVariacion.get("39")) == -1) {
         input39.setStyle(estilo + estiloRojo);
      } else {
         input39.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion40().doubleValue() == 0) || (porcentajesVariacion.get("40").doubleValue() == 0)) {
         input40.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion40().compareTo(porcentajesVariacion.get("40")) == -1) {
         input40.setStyle(estilo + estiloRojo);
      } else {
         input40.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion41().doubleValue() == 0) || (porcentajesVariacion.get("41").doubleValue() == 0)) {
         input41.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion41().compareTo(porcentajesVariacion.get("41")) == -1) {
         input41.setStyle(estilo + estiloRojo);
      } else {
         input41.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion42().doubleValue() == 0) || (porcentajesVariacion.get("42").doubleValue() == 0)) {
         input42.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion42().compareTo(porcentajesVariacion.get("42")) == -1) {
         input42.setStyle(estilo + estiloRojo);
      } else {
         input42.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion43().doubleValue() == 0) || (porcentajesVariacion.get("43").doubleValue() == 0)) {
         input43.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion43().compareTo(porcentajesVariacion.get("43")) == -1) {
         input43.setStyle(estilo + estiloRojo);
      } else {
         input43.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion44().doubleValue() == 0) || (porcentajesVariacion.get("44").doubleValue() == 0)) {
         input44.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion44().compareTo(porcentajesVariacion.get("44")) == -1) {
         input44.setStyle(estilo + estiloRojo);
      } else {
         input44.setStyle(estilo + estiloVerde);
      }
      if ((parametrosActuales.getDesviacion45().doubleValue() == 0) || (porcentajesVariacion.get("45").doubleValue() == 0)) {
         input45.setStyle(estilo + estiloNaranja);
      } else if (parametrosActuales.getDesviacion45().compareTo(porcentajesVariacion.get("45")) == -1) {
         input45.setStyle(estilo + estiloRojo);
      } else {
         input45.setStyle(estilo + estiloVerde);
      }
   }

   public void despintarPorcentajes() {

      FacesContext c = FacesContext.getCurrentInstance();
      input1 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input1");
      input2 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input2");
      input3 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input3");
      input4 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input4");
      input5 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input5");
      input6 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input6");
      input7 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input7");
      input8 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input8");
      input9 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input9");
      input10 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input10");
      input11 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input11");
      input12 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input12");
      input13 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input13");
      input14 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input14");
      input15 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input15");
      input16 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input16");
      input17 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input17");
      input18 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input18");
      input19 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input19");
      input20 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input20");
      input21 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input21");
      input22 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input22");
      input23 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input23");
      input24 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input24");
      input25 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input25");
      input26 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input26");
      input27 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input27");
      input28 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input28");
      input29 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input29");
      input30 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input30");
      input31 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input31");
      input32 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input32");
      input33 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input33");
      input34 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input34");
      input35 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input35");
      input36 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input36");
      input37 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input37");
      input38 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input38");
      input39 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input39");
      input40 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input40");
      input41 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input41");
      input42 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input42");
      input43 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input43");
      input44 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input44");
      input45 = (InputText) c.getViewRoot().findComponent("form:tablaEstadisticasLB:input45");

      String estilo = "width: 45% !important; display: inline-block !important; padding: 0px !important; height: 13px !important; background-color: white !important; color: black !important;";

      input1.setStyle(estilo);
      input2.setStyle(estilo);
      input3.setStyle(estilo);
      input4.setStyle(estilo);
      input5.setStyle(estilo);
      input6.setStyle(estilo);
      input7.setStyle(estilo);
      input8.setStyle(estilo);
      input9.setStyle(estilo);
      input10.setStyle(estilo);
      input11.setStyle(estilo);
      input12.setStyle(estilo);
      input13.setStyle(estilo);
      input14.setStyle(estilo);
      input15.setStyle(estilo);
      input16.setStyle(estilo);
      input17.setStyle(estilo);
      input18.setStyle(estilo);
      input19.setStyle(estilo);
      input20.setStyle(estilo);
      input21.setStyle(estilo);
      input22.setStyle(estilo);
      input23.setStyle(estilo);
      input24.setStyle(estilo);
      input25.setStyle(estilo);
      input26.setStyle(estilo);
      input27.setStyle(estilo);
      input28.setStyle(estilo);
      input29.setStyle(estilo);
      input30.setStyle(estilo);
      input31.setStyle(estilo);
      input32.setStyle(estilo);
      input33.setStyle(estilo);
      input34.setStyle(estilo);
      input35.setStyle(estilo);
      input36.setStyle(estilo);
      input37.setStyle(estilo);
      input38.setStyle(estilo);
      input39.setStyle(estilo);
      input40.setStyle(estilo);
      input41.setStyle(estilo);
      input42.setStyle(estilo);
      input43.setStyle(estilo);
      input44.setStyle(estilo);
      input45.setStyle(estilo);
   }

   public BigDecimal retornarPorcentaje(String id) {
      if (porcentajesVariacion.containsKey(id)) {
         return porcentajesVariacion.get(id);
      } else {
         return new BigDecimal("0.00");
      }
   }

   public void cambiarSeleccionDefault() {
      seleccionPorcentajes = false;
      estadisticaLBSeleccionada = null;
//        RequestContext.getCurrentInstance().execute("PF('tablaEstadisticasLB').unselectAllRows()");
//        RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
      System.out.println("Termino cambiarSeleccionDefault() :");
      System.out.println("estadisticaSeleccionada : " + estadisticaSeleccionada);
      System.out.println("estadisticaLBSeleccionada : " + estadisticaLBSeleccionada);
   }

   public void cambiarSeleccionLBDefault() {
      seleccionPorcentajes = false;
      estadisticaSeleccionada = null;
//        RequestContext.getCurrentInstance().execute("PF('tablaEstadisticas').unselectAllRows()");
//        RequestContext.getCurrentInstance().update("form:tablaEstadisticas");
      System.out.println("Termino cambiarSeleccionLBDefault() :");
      System.out.println("estadisticaSeleccionada : " + estadisticaSeleccionada);
      System.out.println("estadisticaLBSeleccionada : " + estadisticaLBSeleccionada);
   }

   public void posicionEstadisticas() {
      seleccionPorcentajes = false;
      FacesContext contextF = FacesContext.getCurrentInstance();
      Map<String, String> map = contextF.getExternalContext().getRequestParameterMap();
//        String type = map.get("t"); // type attribute of node 
//        int indice = Integer.parseInt(type);
//        String name = map.get("n"); // name attribute of node 
      cualCelda = Integer.parseInt(map.get("n"));
      System.out.println("Termino posicionEstadisticas() cualCelda : " + cualCelda);
   }

   public void estadisticasDetalle() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').show()");
      seleccionPorcentajes = false;
      FacesContext contextF = FacesContext.getCurrentInstance();
      Map<String, String> map = contextF.getExternalContext().getRequestParameterMap();
      cualCelda = Integer.parseInt(map.get("n"));

      if (estadisticaSeleccionada != null) {
         listaDetalles = administrarParametrosConjuntos.consultarDetalleN(parametrosActuales.getDimension(), cualCelda, estadisticaSeleccionada.getSecuenciaFiltro().toBigInteger());
         descripcionDetalle = estadisticaSeleccionada.getDimension();
         System.out.println("Termino estadisticasDetalle() listaDetalles : " + listaDetalles);
      } else {
         System.out.println("ERROR : estadisticasDetalle() estadisticaSeleccionada es NULL");
      }
      System.out.println("Termino estadisticasDetalle() cualCelda : " + cualCelda);
      if (listaDetalles != null) {
         for (int i = 0; i < listaDetalles.size(); i++) {
            if (cualCelda > 0 && cualCelda < 46) {
               if (cualCelda == 1) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto1());
               } else if (cualCelda == 2) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto2());
               } else if (cualCelda == 3) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto3());
               } else if (cualCelda == 4) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto4());
               } else if (cualCelda == 5) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto5());
               } else if (cualCelda == 6) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto6());
               } else if (cualCelda == 7) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto7());
               } else if (cualCelda == 8) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto8());
               } else if (cualCelda == 9) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto9());
               } else if (cualCelda == 10) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto10());
               } else if (cualCelda == 11) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto11());
               } else if (cualCelda == 12) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto12());
               } else if (cualCelda == 13) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto13());
               } else if (cualCelda == 14) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto14());
               } else if (cualCelda == 15) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto15());
               } else if (cualCelda == 16) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto16());
               } else if (cualCelda == 17) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto17());
               } else if (cualCelda == 18) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto18());
               } else if (cualCelda == 19) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto19());
               } else if (cualCelda == 20) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto20());
               } else if (cualCelda == 21) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto21());
               } else if (cualCelda == 22) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto22());
               } else if (cualCelda == 23) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto23());
               } else if (cualCelda == 24) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto24());
               } else if (cualCelda == 25) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto25());
               } else if (cualCelda == 26) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto26());
               } else if (cualCelda == 27) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto27());
               } else if (cualCelda == 28) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto28());
               } else if (cualCelda == 29) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto29());
               } else if (cualCelda == 30) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto30());
               } else if (cualCelda == 31) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto31());
               } else if (cualCelda == 32) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto32());
               } else if (cualCelda == 33) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto33());
               } else if (cualCelda == 34) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto34());
               } else if (cualCelda == 35) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto35());
               } else if (cualCelda == 36) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto36());
               } else if (cualCelda == 37) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto37());
               } else if (cualCelda == 38) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto38());
               } else if (cualCelda == 39) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto39());
               } else if (cualCelda == 40) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto40());
               } else if (cualCelda == 41) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto41());
               } else if (cualCelda == 42) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto42());
               } else if (cualCelda == 43) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto43());
               } else if (cualCelda == 44) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto44());
               } else if (cualCelda == 45) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto45());
               }
            }
         }
         System.out.println("Ya Asigno las Unidades de Detalles");
      }
      actualizarCosasDetalles();
   }

   public void actualizarCosasDetalles() {
      RequestContext context = RequestContext.getCurrentInstance();
      calcularTotalesDet();
      context.reset("form:lOVDetalles:globalFilter");
      System.out.println("1");
      RequestContext.getCurrentInstance().update("form:detallesDialogo");
      System.out.println("2");
      RequestContext.getCurrentInstance().update("form:lOVDetalles");
      System.out.println("3");
      contarRegistrosDetalles();
      RequestContext.getCurrentInstance().execute("PF('lOVDetalles').clearFilters()");
      System.out.println("4");
      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
      System.out.println("5");
      RequestContext.getCurrentInstance().execute("PF('detallesDialogo').show()");
      System.out.println("6");
   }

   public void estadisticasDetalleLB() {
      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').show()");
      seleccionPorcentajes = false;
      FacesContext contextF = FacesContext.getCurrentInstance();
      Map<String, String> map = contextF.getExternalContext().getRequestParameterMap();
      cualCelda = Integer.parseInt(map.get("n"));

      if (estadisticaLBSeleccionada != null) {
         listaDetalles = administrarParametrosConjuntos.consultarDetalleNLB(parametrosActuales.getDimension(), cualCelda, estadisticaLBSeleccionada.getSecuenciaFiltro().toBigInteger());
         descripcionDetalle = estadisticaLBSeleccionada.getDimension();
         System.out.println("Termino estadisticasDetalleLB() listaDetalles : " + listaDetalles);
      } else {
         System.out.println("ERROR : estadisticasDetalleLB() estadisticaSeleccionada es NULL");
      }
      System.out.println("Termino estadisticasDetalleLB() cualCelda : " + cualCelda);
      if (listaDetalles != null) {
         for (int i = 0; i < listaDetalles.size(); i++) {
            if (cualCelda > 0 && cualCelda < 46) {
               if (cualCelda == 1) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto1());
               } else if (cualCelda == 2) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto2());
               } else if (cualCelda == 3) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto3());
               } else if (cualCelda == 4) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto4());
               } else if (cualCelda == 5) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto5());
               } else if (cualCelda == 6) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto6());
               } else if (cualCelda == 7) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto7());
               } else if (cualCelda == 8) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto8());
               } else if (cualCelda == 9) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto9());
               } else if (cualCelda == 10) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto10());
               } else if (cualCelda == 11) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto11());
               } else if (cualCelda == 12) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto12());
               } else if (cualCelda == 13) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto13());
               } else if (cualCelda == 14) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto14());
               } else if (cualCelda == 15) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto15());
               } else if (cualCelda == 16) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto16());
               } else if (cualCelda == 17) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto17());
               } else if (cualCelda == 18) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto18());
               } else if (cualCelda == 19) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto19());
               } else if (cualCelda == 20) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto20());
               } else if (cualCelda == 21) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto21());
               } else if (cualCelda == 22) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto22());
               } else if (cualCelda == 23) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto23());
               } else if (cualCelda == 24) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto24());
               } else if (cualCelda == 25) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto25());
               } else if (cualCelda == 26) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto26());
               } else if (cualCelda == 27) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto27());
               } else if (cualCelda == 28) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto28());
               } else if (cualCelda == 29) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto29());
               } else if (cualCelda == 30) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto30());
               } else if (cualCelda == 31) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto31());
               } else if (cualCelda == 32) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto32());
               } else if (cualCelda == 33) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto33());
               } else if (cualCelda == 34) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto34());
               } else if (cualCelda == 35) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto35());
               } else if (cualCelda == 36) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto36());
               } else if (cualCelda == 37) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto37());
               } else if (cualCelda == 38) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto38());
               } else if (cualCelda == 39) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto39());
               } else if (cualCelda == 40) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto40());
               } else if (cualCelda == 41) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto41());
               } else if (cualCelda == 42) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto42());
               } else if (cualCelda == 43) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto43());
               } else if (cualCelda == 44) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto44());
               } else if (cualCelda == 45) {
                  listaDetalles.get(i).setUnidadesActivo(listaDetalles.get(i).getUnidadConjunto45());
               }
            }
         }
         System.out.println("Ya Asigno las Unidades de Detalles");
      }
//      calcularTotalesDet();
//      context.reset("form:lOVDetalles:globalFilter");
//      System.out.println("1");
//      RequestContext.getCurrentInstance().update("form:detallesDialogo");
//      System.out.println("2");
//      RequestContext.getCurrentInstance().update("form:lOVDetalles");
//      System.out.println("3");
//      contarRegistrosDetalles();
//      RequestContext.getCurrentInstance().execute("PF('lOVDetalles').clearFilters()");
//      System.out.println("4");
//      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
//      System.out.println("5");
//      RequestContext.getCurrentInstance().execute("PF('detallesDialogo').show()");
//      System.out.println("6");
      actualizarCosasDetalles();

   }

   public void salirDetalles() {
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lOVDetalles:globalFilter");
      RequestContext.getCurrentInstance().update("form:detallesDialogo");
      RequestContext.getCurrentInstance().update("form:lOVDetalles");
      RequestContext.getCurrentInstance().execute("PF('lOVDetalles').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('detallesDialogo').hide()");
   }

   public void calcularTotalesDet() {
      System.out.println("Entro en calcularTotalesDet()");
      valorTotalDetalles = new BigInteger("0");
      if (!listaDetalles.isEmpty()) {
         for (int i = 0; i < listaDetalles.size(); i++) {
            valorTotalDetalles = listaDetalles.get(i).getValor().add(valorTotalDetalles);
         }
      }
      totalUnidadesDetalles = new BigDecimal(0);
      if (!listaDetalles.isEmpty()) {
         for (int i = 0; i < listaDetalles.size(); i++) {
            totalUnidadesDetalles = listaDetalles.get(i).getUnidadesActivo().add(totalUnidadesDetalles);
         }
      }
      System.out.println("Termino calcularTotalesDet()");
   }

   public void activarFiltros() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         eDimension = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eDimension");
         eC1 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC1");
         eC2 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC2");
         eC3 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC3");
         eC4 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC4");
         eC5 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC5");
         eC6 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC6");
         eC7 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC7");
         eC8 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC8");
         eC9 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC9");
         eC10 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC10");
         eC11 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC11");
         eC12 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC12");
         eC13 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC13");
         eC14 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC14");
         eC15 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC15");
         eC16 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC16");
         eC17 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC17");
         eC18 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC18");
         eC19 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC19");
         eC20 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC20");
         eC21 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC21");
         eC22 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC22");
         eC23 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC23");
         eC24 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC24");
         eC25 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC25");
         eC26 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC26");
         eC27 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC27");
         eC28 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC28");
         eC29 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC29");
         eC30 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC30");
         eC31 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC31");
         eC32 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC32");
         eC33 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC33");
         eC34 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC34");
         eC35 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC35");
         eC36 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC36");
         eC37 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC37");
         eC38 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC38");
         eC39 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC39");
         eC40 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC40");
         eC41 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC41");
         eC42 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC42");
         eC43 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC43");
         eC44 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC44");
         eC45 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC45");
         eTP = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eTP");
         eCTD = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTD");
         eCN = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCN");
         eCTG = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTG");
         eCTPvos = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTPvos");
         eCGT = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCGT");

         eLBDimension = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBDimension");
         eLBC1 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC1");
         eLBC2 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC2");
         eLBC3 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC3");
         eLBC4 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC4");
         eLBC5 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC5");
         eLBC6 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC6");
         eLBC7 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC7");
         eLBC8 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC8");
         eLBC9 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC9");
         eLBC10 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC10");
         eLBC11 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC11");
         eLBC12 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC12");
         eLBC13 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC13");
         eLBC14 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC14");
         eLBC15 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC15");
         eLBC16 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC16");
         eLBC17 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC17");
         eLBC18 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC18");
         eLBC19 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC19");
         eLBC20 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC20");
         eLBC21 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC21");
         eLBC22 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC22");
         eLBC23 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC23");
         eLBC24 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC24");
         eLBC25 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC25");
         eLBC26 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC26");
         eLBC27 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC27");
         eLBC28 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC28");
         eLBC29 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC29");
         eLBC30 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC30");
         eLBC31 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC31");
         eLBC32 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC32");
         eLBC33 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC33");
         eLBC34 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC34");
         eLBC35 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC35");
         eLBC36 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC36");
         eLBC37 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC37");
         eLBC38 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC38");
         eLBC39 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC39");
         eLBC40 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC40");
         eLBC41 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC41");
         eLBC42 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC42");
         eLBC43 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC43");
         eLBC44 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC44");
         eLBC45 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC45");
         eLBTP = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBTP");
         eLBCTD = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTD");
         eLBCN = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCN");
         eLBCTG = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTG");
         eLBCTPvos = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTPvos");
         eLBCGT = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCGT");

         eDimension.setFilterStyle("width: 85% !important;");
         eC1.setFilterStyle("width: 85% !important;");
         eC2.setFilterStyle("width: 85% !important;");
         eC3.setFilterStyle("width: 85% !important;");
         eC4.setFilterStyle("width: 85% !important;");
         eC5.setFilterStyle("width: 85% !important;");
         eC6.setFilterStyle("width: 85% !important;");
         eC7.setFilterStyle("width: 85% !important;");
         eC8.setFilterStyle("width: 85% !important;");
         eC9.setFilterStyle("width: 85% !important;");
         eC10.setFilterStyle("width: 85% !important;");
         eC11.setFilterStyle("width: 85% !important;");
         eC12.setFilterStyle("width: 85% !important;");
         eC13.setFilterStyle("width: 85% !important;");
         eC14.setFilterStyle("width: 85% !important;");
         eC15.setFilterStyle("width: 85% !important;");
         eC16.setFilterStyle("width: 85% !important;");
         eC17.setFilterStyle("width: 85% !important;");
         eC18.setFilterStyle("width: 85% !important;");
         eC19.setFilterStyle("width: 85% !important;");
         eC20.setFilterStyle("width: 85% !important;");
         eC21.setFilterStyle("width: 85% !important;");
         eC22.setFilterStyle("width: 85% !important;");
         eC23.setFilterStyle("width: 85% !important;");
         eC24.setFilterStyle("width: 85% !important;");
         eC25.setFilterStyle("width: 85% !important;");
         eC26.setFilterStyle("width: 85% !important;");
         eC27.setFilterStyle("width: 85% !important;");
         eC28.setFilterStyle("width: 85% !important;");
         eC29.setFilterStyle("width: 85% !important;");
         eC30.setFilterStyle("width: 85% !important;");
         eC31.setFilterStyle("width: 85% !important;");
         eC32.setFilterStyle("width: 85% !important;");
         eC33.setFilterStyle("width: 85% !important;");
         eC34.setFilterStyle("width: 85% !important;");
         eC35.setFilterStyle("width: 85% !important;");
         eC36.setFilterStyle("width: 85% !important;");
         eC37.setFilterStyle("width: 85% !important;");
         eC38.setFilterStyle("width: 85% !important;");
         eC39.setFilterStyle("width: 85% !important;");
         eC40.setFilterStyle("width: 85% !important;");
         eC41.setFilterStyle("width: 85% !important;");
         eC42.setFilterStyle("width: 85% !important;");
         eC43.setFilterStyle("width: 85% !important;");
         eC44.setFilterStyle("width: 85% !important;");
         eC45.setFilterStyle("width: 85% !important;");
         eTP.setFilterStyle("width: 85% !important;");
         eCTD.setFilterStyle("width: 85% !important;");
         eCN.setFilterStyle("width: 85% !important;");
         eCTG.setFilterStyle("width: 85% !important;");
         eCTPvos.setFilterStyle("width: 85% !important;");
         eCGT.setFilterStyle("width: 85% !important;");

         eLBDimension.setFilterStyle("width: 85% !important;");
         eLBC1.setFilterStyle("width: 85% !important;");
         eLBC2.setFilterStyle("width: 85% !important;");
         eLBC3.setFilterStyle("width: 85% !important;");
         eLBC4.setFilterStyle("width: 85% !important;");
         eLBC5.setFilterStyle("width: 85% !important;");
         eLBC6.setFilterStyle("width: 85% !important;");
         eLBC7.setFilterStyle("width: 85% !important;");
         eLBC8.setFilterStyle("width: 85% !important;");
         eLBC9.setFilterStyle("width: 85% !important;");
         eLBC10.setFilterStyle("width: 85% !important;");
         eLBC11.setFilterStyle("width: 85% !important;");
         eLBC12.setFilterStyle("width: 85% !important;");
         eLBC13.setFilterStyle("width: 85% !important;");
         eLBC14.setFilterStyle("width: 85% !important;");
         eLBC15.setFilterStyle("width: 85% !important;");
         eLBC16.setFilterStyle("width: 85% !important;");
         eLBC17.setFilterStyle("width: 85% !important;");
         eLBC18.setFilterStyle("width: 85% !important;");
         eLBC19.setFilterStyle("width: 85% !important;");
         eLBC20.setFilterStyle("width: 85% !important;");
         eLBC21.setFilterStyle("width: 85% !important;");
         eLBC22.setFilterStyle("width: 85% !important;");
         eLBC23.setFilterStyle("width: 85% !important;");
         eLBC24.setFilterStyle("width: 85% !important;");
         eLBC25.setFilterStyle("width: 85% !important;");
         eLBC26.setFilterStyle("width: 85% !important;");
         eLBC27.setFilterStyle("width: 85% !important;");
         eLBC28.setFilterStyle("width: 85% !important;");
         eLBC29.setFilterStyle("width: 85% !important;");
         eLBC30.setFilterStyle("width: 85% !important;");
         eLBC31.setFilterStyle("width: 85% !important;");
         eLBC32.setFilterStyle("width: 85% !important;");
         eLBC33.setFilterStyle("width: 85% !important;");
         eLBC34.setFilterStyle("width: 85% !important;");
         eLBC35.setFilterStyle("width: 85% !important;");
         eLBC36.setFilterStyle("width: 85% !important;");
         eLBC37.setFilterStyle("width: 85% !important;");
         eLBC38.setFilterStyle("width: 85% !important;");
         eLBC39.setFilterStyle("width: 85% !important;");
         eLBC40.setFilterStyle("width: 85% !important;");
         eLBC41.setFilterStyle("width: 85% !important;");
         eLBC42.setFilterStyle("width: 85% !important;");
         eLBC43.setFilterStyle("width: 85% !important;");
         eLBC44.setFilterStyle("width: 85% !important;");
         eLBC45.setFilterStyle("width: 85% !important;");
         eLBTP.setFilterStyle("width: 85% !important;");
         eLBCTD.setFilterStyle("width: 85% !important;");
         eLBCN.setFilterStyle("width: 85% !important;");
         eLBCTG.setFilterStyle("width: 85% !important;");
         eLBCTPvos.setFilterStyle("width: 85% !important;");
         eLBCGT.setFilterStyle("width: 85% !important;");

         altoTabla = "51";
         bandera = 1;
      } else if (bandera == 1) {
         restablecerTablas();
      }
      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
      RequestContext.getCurrentInstance().update("form:tablaEstadisticas");
   }

   public void restablecerTablas() {
      FacesContext c = FacesContext.getCurrentInstance();
      eDimension = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eDimension");
      eC1 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC1");
      eC2 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC2");
      eC3 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC3");
      eC4 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC4");
      eC5 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC5");
      eC6 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC6");
      eC7 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC7");
      eC8 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC8");
      eC9 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC9");
      eC10 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC10");
      eC11 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC11");
      eC12 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC12");
      eC13 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC13");
      eC14 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC14");
      eC15 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC15");
      eC16 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC16");
      eC17 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC17");
      eC18 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC18");
      eC19 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC19");
      eC20 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC20");
      eC21 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC21");
      eC22 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC22");
      eC23 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC23");
      eC24 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC24");
      eC25 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC25");
      eC26 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC26");
      eC27 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC27");
      eC28 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC28");
      eC29 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC29");
      eC30 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC30");
      eC31 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC31");
      eC32 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC32");
      eC33 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC33");
      eC34 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC34");
      eC35 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC35");
      eC36 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC36");
      eC37 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC37");
      eC38 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC38");
      eC39 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC39");
      eC40 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC40");
      eC41 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC41");
      eC42 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC42");
      eC43 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC43");
      eC44 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC44");
      eC45 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eC45");
      eTP = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eTP");
      eCTD = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTD");
      eCN = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCN");
      eCTG = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTG");
      eCTPvos = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCTPvos");
      eCGT = (Column) c.getViewRoot().findComponent("form:tablaEstadisticas:eCGT");

      eLBDimension = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBDimension");
      eLBC1 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC1");
      eLBC2 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC2");
      eLBC3 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC3");
      eLBC4 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC4");
      eLBC5 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC5");
      eLBC6 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC6");
      eLBC7 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC7");
      eLBC8 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC8");
      eLBC9 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC9");
      eLBC10 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC10");
      eLBC11 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC11");
      eLBC12 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC12");
      eLBC13 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC13");
      eLBC14 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC14");
      eLBC15 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC15");
      eLBC16 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC16");
      eLBC17 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC17");
      eLBC18 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC18");
      eLBC19 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC19");
      eLBC20 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC20");
      eLBC21 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC21");
      eLBC22 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC22");
      eLBC23 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC23");
      eLBC24 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC24");
      eLBC25 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC25");
      eLBC26 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC26");
      eLBC27 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC27");
      eLBC28 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC28");
      eLBC29 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC29");
      eLBC30 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC30");
      eLBC31 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC31");
      eLBC32 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC32");
      eLBC33 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC33");
      eLBC34 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC34");
      eLBC35 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC35");
      eLBC36 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC36");
      eLBC37 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC37");
      eLBC38 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC38");
      eLBC39 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC39");
      eLBC40 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC40");
      eLBC41 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC41");
      eLBC42 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC42");
      eLBC43 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC43");
      eLBC44 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC44");
      eLBC45 = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBC45");
      eLBTP = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBTP");
      eLBCTD = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTD");
      eLBCN = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCN");
      eLBCTG = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTG");
      eLBCTPvos = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCTPvos");
      eLBCGT = (Column) c.getViewRoot().findComponent("form:tablaEstadisticasLB:eLBCGT");

      eDimension.setFilterStyle("display: none; visibility: hidden;");
      eC1.setFilterStyle("display: none; visibility: hidden;");
      eC2.setFilterStyle("display: none; visibility: hidden;");
      eC3.setFilterStyle("display: none; visibility: hidden;");
      eC4.setFilterStyle("display: none; visibility: hidden;");
      eC5.setFilterStyle("display: none; visibility: hidden;");
      eC6.setFilterStyle("display: none; visibility: hidden;");
      eC7.setFilterStyle("display: none; visibility: hidden;");
      eC8.setFilterStyle("display: none; visibility: hidden;");
      eC9.setFilterStyle("display: none; visibility: hidden;");
      eC10.setFilterStyle("display: none; visibility: hidden;");
      eC11.setFilterStyle("display: none; visibility: hidden;");
      eC12.setFilterStyle("display: none; visibility: hidden;");
      eC13.setFilterStyle("display: none; visibility: hidden;");
      eC14.setFilterStyle("display: none; visibility: hidden;");
      eC15.setFilterStyle("display: none; visibility: hidden;");
      eC16.setFilterStyle("display: none; visibility: hidden;");
      eC17.setFilterStyle("display: none; visibility: hidden;");
      eC18.setFilterStyle("display: none; visibility: hidden;");
      eC19.setFilterStyle("display: none; visibility: hidden;");
      eC20.setFilterStyle("display: none; visibility: hidden;");
      eC21.setFilterStyle("display: none; visibility: hidden;");
      eC22.setFilterStyle("display: none; visibility: hidden;");
      eC23.setFilterStyle("display: none; visibility: hidden;");
      eC24.setFilterStyle("display: none; visibility: hidden;");
      eC25.setFilterStyle("display: none; visibility: hidden;");
      eC26.setFilterStyle("display: none; visibility: hidden;");
      eC27.setFilterStyle("display: none; visibility: hidden;");
      eC28.setFilterStyle("display: none; visibility: hidden;");
      eC29.setFilterStyle("display: none; visibility: hidden;");
      eC30.setFilterStyle("display: none; visibility: hidden;");
      eC31.setFilterStyle("display: none; visibility: hidden;");
      eC32.setFilterStyle("display: none; visibility: hidden;");
      eC33.setFilterStyle("display: none; visibility: hidden;");
      eC34.setFilterStyle("display: none; visibility: hidden;");
      eC35.setFilterStyle("display: none; visibility: hidden;");
      eC36.setFilterStyle("display: none; visibility: hidden;");
      eC37.setFilterStyle("display: none; visibility: hidden;");
      eC38.setFilterStyle("display: none; visibility: hidden;");
      eC39.setFilterStyle("display: none; visibility: hidden;");
      eC40.setFilterStyle("display: none; visibility: hidden;");
      eC41.setFilterStyle("display: none; visibility: hidden;");
      eC42.setFilterStyle("display: none; visibility: hidden;");
      eC43.setFilterStyle("display: none; visibility: hidden;");
      eC44.setFilterStyle("display: none; visibility: hidden;");
      eC45.setFilterStyle("display: none; visibility: hidden;");
      eTP.setFilterStyle("display: none; visibility: hidden;");
      eCTD.setFilterStyle("display: none; visibility: hidden;");
      eCN.setFilterStyle("display: none; visibility: hidden;");
      eCTG.setFilterStyle("display: none; visibility: hidden;");
      eCTPvos.setFilterStyle("display: none; visibility: hidden;");
      eCGT.setFilterStyle("display: none; visibility: hidden;");

      eLBDimension.setFilterStyle("display: none; visibility: hidden;");
      eLBC1.setFilterStyle("display: none; visibility: hidden;");
      eLBC2.setFilterStyle("display: none; visibility: hidden;");
      eLBC3.setFilterStyle("display: none; visibility: hidden;");
      eLBC4.setFilterStyle("display: none; visibility: hidden;");
      eLBC5.setFilterStyle("display: none; visibility: hidden;");
      eLBC6.setFilterStyle("display: none; visibility: hidden;");
      eLBC7.setFilterStyle("display: none; visibility: hidden;");
      eLBC8.setFilterStyle("display: none; visibility: hidden;");
      eLBC9.setFilterStyle("display: none; visibility: hidden;");
      eLBC10.setFilterStyle("display: none; visibility: hidden;");
      eLBC11.setFilterStyle("display: none; visibility: hidden;");
      eLBC12.setFilterStyle("display: none; visibility: hidden;");
      eLBC13.setFilterStyle("display: none; visibility: hidden;");
      eLBC14.setFilterStyle("display: none; visibility: hidden;");
      eLBC15.setFilterStyle("display: none; visibility: hidden;");
      eLBC16.setFilterStyle("display: none; visibility: hidden;");
      eLBC17.setFilterStyle("display: none; visibility: hidden;");
      eLBC18.setFilterStyle("display: none; visibility: hidden;");
      eLBC19.setFilterStyle("display: none; visibility: hidden;");
      eLBC20.setFilterStyle("display: none; visibility: hidden;");
      eLBC21.setFilterStyle("display: none; visibility: hidden;");
      eLBC22.setFilterStyle("display: none; visibility: hidden;");
      eLBC23.setFilterStyle("display: none; visibility: hidden;");
      eLBC24.setFilterStyle("display: none; visibility: hidden;");
      eLBC25.setFilterStyle("display: none; visibility: hidden;");
      eLBC26.setFilterStyle("display: none; visibility: hidden;");
      eLBC27.setFilterStyle("display: none; visibility: hidden;");
      eLBC28.setFilterStyle("display: none; visibility: hidden;");
      eLBC29.setFilterStyle("display: none; visibility: hidden;");
      eLBC30.setFilterStyle("display: none; visibility: hidden;");
      eLBC31.setFilterStyle("display: none; visibility: hidden;");
      eLBC32.setFilterStyle("display: none; visibility: hidden;");
      eLBC33.setFilterStyle("display: none; visibility: hidden;");
      eLBC34.setFilterStyle("display: none; visibility: hidden;");
      eLBC35.setFilterStyle("display: none; visibility: hidden;");
      eLBC36.setFilterStyle("display: none; visibility: hidden;");
      eLBC37.setFilterStyle("display: none; visibility: hidden;");
      eLBC38.setFilterStyle("display: none; visibility: hidden;");
      eLBC39.setFilterStyle("display: none; visibility: hidden;");
      eLBC40.setFilterStyle("display: none; visibility: hidden;");
      eLBC41.setFilterStyle("display: none; visibility: hidden;");
      eLBC42.setFilterStyle("display: none; visibility: hidden;");
      eLBC43.setFilterStyle("display: none; visibility: hidden;");
      eLBC44.setFilterStyle("display: none; visibility: hidden;");
      eLBC45.setFilterStyle("display: none; visibility: hidden;");
      eLBTP.setFilterStyle("display: none; visibility: hidden;");
      eLBCTD.setFilterStyle("display: none; visibility: hidden;");
      eLBCN.setFilterStyle("display: none; visibility: hidden;");
      eLBCTG.setFilterStyle("display: none; visibility: hidden;");
      eLBCTPvos.setFilterStyle("display: none; visibility: hidden;");
      eLBCGT.setFilterStyle("display: none; visibility: hidden;");

      altoTabla = "75";
      bandera = 0;
      filtradoEstadisticas = null;
      filtradoEstadisticasLB = null;
      tipoLista = 0;
      tipoListaLB = 0;

      RequestContext.getCurrentInstance().update("form:tablaEstadisticasLB");
      RequestContext.getCurrentInstance().update("form:tablaEstadisticas");
      contarRegistros();
      contarRegistrosLB();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      estadisticaSeleccionada = null;
      contarRegistros();
   }

   public void eventoFiltrarLB() {
      if (tipoListaLB == 0) {
         tipoListaLB = 1;
      }
      estadisticaLBSeleccionada = null;
      contarRegistrosLB();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLB() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroLB");
   }

   public void contarRegistrosDetalles() {
      System.out.println("Entro en contarRegistrosDetalles()");
      RequestContext.getCurrentInstance().update("form:infoRegistroDetalles");
   }

   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
   }

   public void cargarConceptosEspecificos(Short nConjunto) {
      System.out.println("Entro en cargarConceptosEspecificos() con parametro Short : " + nConjunto);
      cargarListaConceptos();
      System.out.println("listaConceptos.size() : " + listaConceptos.size());
      listaConceptosEspecificos.clear();
      for (int i = 0; i < listaConceptos.size(); i++) {
         if (listaConceptos.get(i).getConjunto() != null) {
            if (listaConceptos.get(i).getConjunto().equals(nConjunto)) {
               listaConceptosEspecificos.add(listaConceptos.get(i));
            }
         }
      }
      System.out.println("listaConceptosEspecificos.size() : " + listaConceptosEspecificos.size());
      RequestContext context = RequestContext.getCurrentInstance();
      conceptoSeleccionado = null;
      context.reset("form:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().update("form:conceptosDialogo");
      RequestContext.getCurrentInstance().update("form:LOVConceptos");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows()");
      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      System.out.println("Va a contar Registros");
      contarRegistrosConceptos();
      System.out.println("Ya hizo todo cargarConceptosEspecificos()");
   }

   public void cargarListaConceptos() {
      if (listaConceptos.isEmpty()) {
         listaConceptos = administrarParametrosConjuntos.consultarConceptos();
      }
   }

   public void modificarConjuntoConcepto(Conceptos concepto) {
      conceptoSeleccionado = concepto;
      System.out.println("modificarConjuntoConcepto() conceptoSeleccionado.getDescripcion() : " + conceptoSeleccionado.getDescripcion());
      System.out.println("modificarConjuntoConcepto() conceptoSeleccionado.getConjunto() : " + conceptoSeleccionado.getConjunto());
      administrarParametrosConjuntos.modificarConcepto(conceptoSeleccionado);
      listaConceptos.clear();
      cargarListaConceptos();
      conceptoSeleccionado = null;
      FacesMessage msg = new FacesMessage("Información", "Se guardaron con éxito los cambios en Conceptos");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:growl");

      conceptoSeleccionado = null;
      context.reset("form:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().update("form:conceptosDialogo");
      RequestContext.getCurrentInstance().update("form:LOVConceptos");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows()");
      RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void editar() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (estadisticaSeleccionada != null || estadisticaLBSeleccionada != null) {
         VWDSolucionesNodosN estadistica;

         if (estadisticaSeleccionada != null) {
            estadistica = estadisticaSeleccionada;
         } else if (estadisticaLBSeleccionada != null) {
            estadistica = estadisticaLBSeleccionada;
         } else {
            estadistica = new VWDSolucionesNodosN();
         }

         if (cualCelda == 1) {
            tituloEditar = parametrosActuales.getTituloConjunto1();
            textoEditar = (String) estadistica.getConjunto1().toString();
         } else if (cualCelda == 2) {
            tituloEditar = parametrosActuales.getTituloConjunto2();
            textoEditar = (String) estadistica.getConjunto2().toString();
         } else if (cualCelda == 3) {
            tituloEditar = parametrosActuales.getTituloConjunto3();
            textoEditar = (String) estadistica.getConjunto3().toString();
         } else if (cualCelda == 4) {
            tituloEditar = parametrosActuales.getTituloConjunto4();
            textoEditar = (String) estadistica.getConjunto4().toString();
         } else if (cualCelda == 5) {
            tituloEditar = parametrosActuales.getTituloConjunto5();
            textoEditar = (String) estadistica.getConjunto5().toString();
         } else if (cualCelda == 6) {
            tituloEditar = parametrosActuales.getTituloConjunto6();
            textoEditar = (String) estadistica.getConjunto6().toString();
         } else if (cualCelda == 7) {
            tituloEditar = parametrosActuales.getTituloConjunto7();
            textoEditar = (String) estadistica.getConjunto7().toString();
         } else if (cualCelda == 8) {
            tituloEditar = parametrosActuales.getTituloConjunto8();
            textoEditar = (String) estadistica.getConjunto8().toString();
         } else if (cualCelda == 9) {
            tituloEditar = parametrosActuales.getTituloConjunto9();
            textoEditar = (String) estadistica.getConjunto9().toString();
         } else if (cualCelda == 10) {
            tituloEditar = parametrosActuales.getTituloConjunto10();
            textoEditar = (String) estadistica.getConjunto10().toString();
         } else if (cualCelda == 11) {
            tituloEditar = parametrosActuales.getTituloConjunto11();
            textoEditar = (String) estadistica.getConjunto11().toString();
         } else if (cualCelda == 12) {
            tituloEditar = parametrosActuales.getTituloConjunto12();
            textoEditar = (String) estadistica.getConjunto12().toString();
         } else if (cualCelda == 13) {
            tituloEditar = parametrosActuales.getTituloConjunto13();
            textoEditar = (String) estadistica.getConjunto13().toString();
         } else if (cualCelda == 14) {
            tituloEditar = parametrosActuales.getTituloConjunto14();
            textoEditar = (String) estadistica.getConjunto14().toString();
         } else if (cualCelda == 15) {
            tituloEditar = parametrosActuales.getTituloConjunto15();
            textoEditar = (String) estadistica.getConjunto15().toString();
         } else if (cualCelda == 16) {
            tituloEditar = parametrosActuales.getTituloConjunto16();
            textoEditar = (String) estadistica.getConjunto16().toString();
         } else if (cualCelda == 17) {
            tituloEditar = parametrosActuales.getTituloConjunto17();
            textoEditar = (String) estadistica.getConjunto17().toString();
         } else if (cualCelda == 18) {
            tituloEditar = parametrosActuales.getTituloConjunto18();
            textoEditar = (String) estadistica.getConjunto18().toString();
         } else if (cualCelda == 19) {
            tituloEditar = parametrosActuales.getTituloConjunto19();
            textoEditar = (String) estadistica.getConjunto19().toString();
         } else if (cualCelda == 20) {
            tituloEditar = parametrosActuales.getTituloConjunto20();
            textoEditar = (String) estadistica.getConjunto20().toString();
         } else if (cualCelda == 21) {
            tituloEditar = parametrosActuales.getTituloConjunto21();
            textoEditar = (String) estadistica.getConjunto21().toString();
         } else if (cualCelda == 22) {
            tituloEditar = parametrosActuales.getTituloConjunto22();
            textoEditar = (String) estadistica.getConjunto22().toString();
         } else if (cualCelda == 23) {
            tituloEditar = parametrosActuales.getTituloConjunto23();
            textoEditar = (String) estadistica.getConjunto23().toString();
         } else if (cualCelda == 24) {
            tituloEditar = parametrosActuales.getTituloConjunto24();
            textoEditar = (String) estadistica.getConjunto24().toString();
         } else if (cualCelda == 25) {
            tituloEditar = parametrosActuales.getTituloConjunto25();
            textoEditar = (String) estadistica.getConjunto25().toString();
         } else if (cualCelda == 26) {
            tituloEditar = parametrosActuales.getTituloConjunto26();
            textoEditar = (String) estadistica.getConjunto26().toString();
         } else if (cualCelda == 27) {
            tituloEditar = parametrosActuales.getTituloConjunto27();
            textoEditar = (String) estadistica.getConjunto27().toString();
         } else if (cualCelda == 28) {
            tituloEditar = parametrosActuales.getTituloConjunto28();
            textoEditar = (String) estadistica.getConjunto28().toString();
         } else if (cualCelda == 29) {
            tituloEditar = parametrosActuales.getTituloConjunto29();
            textoEditar = (String) estadistica.getConjunto29().toString();
         } else if (cualCelda == 30) {
            tituloEditar = parametrosActuales.getTituloConjunto30();
            textoEditar = (String) estadistica.getConjunto30().toString();
         } else if (cualCelda == 31) {
            tituloEditar = parametrosActuales.getTituloConjunto31();
            textoEditar = (String) estadistica.getConjunto31().toString();
         } else if (cualCelda == 32) {
            tituloEditar = parametrosActuales.getTituloConjunto32();
            textoEditar = (String) estadistica.getConjunto32().toString();
         } else if (cualCelda == 33) {
            tituloEditar = parametrosActuales.getTituloConjunto33();
            textoEditar = (String) estadistica.getConjunto33().toString();
         } else if (cualCelda == 34) {
            tituloEditar = parametrosActuales.getTituloConjunto34();
            textoEditar = (String) estadistica.getConjunto34().toString();
         } else if (cualCelda == 35) {
            tituloEditar = parametrosActuales.getTituloConjunto35();
            textoEditar = (String) estadistica.getConjunto35().toString();
         } else if (cualCelda == 36) {
            tituloEditar = parametrosActuales.getTituloConjunto36();
            textoEditar = (String) estadistica.getConjunto36().toString();
         } else if (cualCelda == 37) {
            tituloEditar = parametrosActuales.getTituloConjunto37();
            textoEditar = (String) estadistica.getConjunto37().toString();
         } else if (cualCelda == 38) {
            tituloEditar = parametrosActuales.getTituloConjunto38();
            textoEditar = (String) estadistica.getConjunto38().toString();
         } else if (cualCelda == 39) {
            tituloEditar = parametrosActuales.getTituloConjunto39();
            textoEditar = (String) estadistica.getConjunto39().toString();
         } else if (cualCelda == 40) {
            tituloEditar = parametrosActuales.getTituloConjunto40();
            textoEditar = (String) estadistica.getConjunto40().toString();
         } else if (cualCelda == 41) {
            tituloEditar = parametrosActuales.getTituloConjunto41();
            textoEditar = (String) estadistica.getConjunto41().toString();
         } else if (cualCelda == 42) {
            tituloEditar = parametrosActuales.getTituloConjunto42();
            textoEditar = (String) estadistica.getConjunto42().toString();
         } else if (cualCelda == 43) {
            tituloEditar = parametrosActuales.getTituloConjunto43();
            textoEditar = (String) estadistica.getConjunto43().toString();
         } else if (cualCelda == 44) {
            tituloEditar = parametrosActuales.getTituloConjunto44();
            textoEditar = (String) estadistica.getConjunto44().toString();
         } else if (cualCelda == 45) {
            tituloEditar = parametrosActuales.getTituloConjunto45();
            textoEditar = (String) estadistica.getConjunto45().toString();

         } else if (cualCelda == 101) {
            tituloEditar = "Total Pagos";
            textoEditar = (String) estadistica.getTotalPagos().toString();
         } else if (cualCelda == 102) {
            tituloEditar = "Total Descuentos";
            textoEditar = (String) estadistica.getTotalDescuentos().toString();
         } else if (cualCelda == 103) {
            tituloEditar = "Total Netos";
            textoEditar = (String) estadistica.getNetos().toString();
         } else if (cualCelda == 104) {
            tituloEditar = "Total Gastos";
            textoEditar = (String) estadistica.getTotalGastos().toString();
         } else if (cualCelda == 105) {
            tituloEditar = "Total Pasivos";
            textoEditar = (String) estadistica.getTotalPasivos().toString();
         } else if (cualCelda == 106) {
            tituloEditar = "Gran Total";
            textoEditar = (String) estadistica.getGranTotal().toString();
         } else if (cualCelda == 107) {
            tituloEditar = "Descripción";
            textoEditar = (String) estadistica.getDimension().toString();
         } else if (cualCelda == 108) {
            tituloEditar = "Fecha";
            textoEditar = (String) estadistica.getZ().toString();
         } else if (cualCelda == 109) {
            tituloEditar = "Estado";
            textoEditar = (String) estadistica.getEstado().toString();
         } else if (cualCelda == 110) {
            tituloEditar = "Codigo Empleado";
            textoEditar = (String) estadistica.getCodigoEmpleado().toString();
         } else if (cualCelda == 111) {
            tituloEditar = "Estructura";
            textoEditar = (String) estadistica.getEstructura().toString();
         } else if (cualCelda == 112) {
            tituloEditar = "Cargo";
            textoEditar = (String) estadistica.getCargo().toString();
         } else {
            tituloEditar = "";
            textoEditar = "";
         }
         if (cualCelda == 107 || cualCelda > 108) {
            RequestContext.getCurrentInstance().update("form:editarDialogo");
            RequestContext.getCurrentInstance().execute("PF('editarDialogo').show()");
         } else if (cualCelda == 108) {
            RequestContext.getCurrentInstance().update("form:editarDialogoFecha");
            RequestContext.getCurrentInstance().execute("PF('editarDialogoFecha').show()");
         } else {
            RequestContext.getCurrentInstance().update("form:editarDialogoNum");
            RequestContext.getCurrentInstance().execute("PF('editarDialogoNum').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void seleccionPorcentajes() {
//        System.out.println("seleccionPorcentajes() : " + seleccionPorcentajes);
      seleccionPorcentajes = true;
      if (!listaEstadisticas.isEmpty() && !listaEstadisticasLB.isEmpty()) {
         VWDSolucionesNodosN obj = new VWDSolucionesNodosN();
         obj.setConjunto1(retornarPorcentaje("1"));
         obj.setConjunto2(retornarPorcentaje("2"));
         obj.setConjunto3(retornarPorcentaje("3"));
         obj.setConjunto4(retornarPorcentaje("4"));
         obj.setConjunto5(retornarPorcentaje("5"));
         obj.setConjunto6(retornarPorcentaje("6"));
         obj.setConjunto7(retornarPorcentaje("7"));
         obj.setConjunto8(retornarPorcentaje("8"));
         obj.setConjunto9(retornarPorcentaje("9"));
         obj.setConjunto10(retornarPorcentaje("10"));
         obj.setConjunto11(retornarPorcentaje("11"));
         obj.setConjunto12(retornarPorcentaje("12"));
         obj.setConjunto13(retornarPorcentaje("13"));
         obj.setConjunto14(retornarPorcentaje("14"));
         obj.setConjunto15(retornarPorcentaje("15"));
         obj.setConjunto16(retornarPorcentaje("16"));
         obj.setConjunto17(retornarPorcentaje("17"));
         obj.setConjunto18(retornarPorcentaje("18"));
         obj.setConjunto19(retornarPorcentaje("19"));
         obj.setConjunto20(retornarPorcentaje("20"));
         obj.setConjunto21(retornarPorcentaje("21"));
         obj.setConjunto22(retornarPorcentaje("22"));
         obj.setConjunto23(retornarPorcentaje("23"));
         obj.setConjunto24(retornarPorcentaje("24"));
         obj.setConjunto25(retornarPorcentaje("25"));
         obj.setConjunto26(retornarPorcentaje("26"));
         obj.setConjunto27(retornarPorcentaje("27"));
         obj.setConjunto28(retornarPorcentaje("28"));
         obj.setConjunto29(retornarPorcentaje("29"));
         obj.setConjunto30(retornarPorcentaje("30"));
         obj.setConjunto31(retornarPorcentaje("31"));
         obj.setConjunto32(retornarPorcentaje("32"));
         obj.setConjunto33(retornarPorcentaje("33"));
         obj.setConjunto34(retornarPorcentaje("34"));
         obj.setConjunto35(retornarPorcentaje("35"));
         obj.setConjunto36(retornarPorcentaje("36"));
         obj.setConjunto37(retornarPorcentaje("37"));
         obj.setConjunto38(retornarPorcentaje("38"));
         obj.setConjunto39(retornarPorcentaje("39"));
         obj.setConjunto40(retornarPorcentaje("40"));
         obj.setConjunto41(retornarPorcentaje("41"));
         obj.setConjunto42(retornarPorcentaje("42"));
         obj.setConjunto43(retornarPorcentaje("43"));
         obj.setConjunto44(retornarPorcentaje("44"));
         obj.setConjunto45(retornarPorcentaje("45"));
         obj.setTotalPagos(retornarPorcentaje("TP"));
         obj.setTotalDescuentos(retornarPorcentaje("TD"));
         obj.setNetos(retornarPorcentaje("N"));
         obj.setTotalGastos(retornarPorcentaje("TG"));
         obj.setTotalPasivos(retornarPorcentaje("TPvos"));
         obj.setGranTotal(retornarPorcentaje("GT"));
         porcentajesExportar.clear();
         porcentajesExportar.add(obj);
      }
   }

   // Exportar
   public String nombreArchivoXML() {
      System.out.println("porcentajesExportar : " + porcentajesExportar);
      if (seleccionPorcentajes) {
         nombreXML = "PorcentajesXML";
      } else if (estadisticaLBSeleccionada != null) {
         nombreXML = "EstadisticasPeriodoAnteriorXML";
      } else {
         nombreXML = "EstadisticasXML";
      }
      return nombreXML;
   }

   public String tablaXML() {
      if (seleccionPorcentajes) {
         tablaXML = ":formExportarPor:tablaPorcentajesExportar";
      } else if (estadisticaLBSeleccionada != null) {
         tablaXML = ":formExportarLB:tablaEstadisticasLBExportar";
      } else {
         tablaXML = ":formExportar:tablaEstadisticasExportar";
      }
      return tablaXML;
   }

   public void exportPDF() throws IOException {
      System.out.println("porcentajesExportar : " + porcentajesExportar);
      if (seleccionPorcentajes) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarPor:tablaPorcentajesExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarPDFTablasAnchas();
         exporter.export(context, tabla, "PorcentajesPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (estadisticaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:tablaEstadisticasExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarPDFTablasAnchas();
         exporter.export(context, tabla, "EstadisticasPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (estadisticaLBSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarLB:tablaEstadisticasLBExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarPDFTablasAnchas();
         exporter.export(context, tabla, "EstadisticasPeriodoAnteriorPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportXLS() throws IOException {
      System.out.println("porcentajesExportar : " + porcentajesExportar);
      if (seleccionPorcentajes) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarPor:tablaPorcentajesExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "PorcentajesXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (estadisticaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:tablaEstadisticasExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "EstadisticasXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (estadisticaLBSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarLB:tablaEstadisticasLBExportar");
         FacesContext context = c;
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "EstadisticasPeriodoAnteriorXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @return
    */
   //GET'S  y  SET'S:
   public ParametrosConjuntos getParametrosActuales() {
      return parametrosActuales;
   }

   public void setParametrosActuales(ParametrosConjuntos parametrosActuales) {
      this.parametrosActuales = parametrosActuales;
   }

   public List<VWDSolucionesNodosN> getListaEstadisticas() {
      return listaEstadisticas;
   }

   public void setListaEstadisticas(List<VWDSolucionesNodosN> listaEstadisticas) {
      this.listaEstadisticas = listaEstadisticas;
   }

   public List<VWDSolucionesNodosN> getFiltradoEstadisticas() {
      return filtradoEstadisticas;
   }

   public void setFiltradoEstadisticas(List<VWDSolucionesNodosN> filtradoEstadisticas) {
      this.filtradoEstadisticas = filtradoEstadisticas;
   }

   public VWDSolucionesNodosN getEstadisticaSeleccionada() {
      return estadisticaSeleccionada;
   }

   public void setEstadisticaSeleccionada(VWDSolucionesNodosN estadisticaSeleccionada) {
      this.estadisticaSeleccionada = estadisticaSeleccionada;
   }

   public List<VWDSolucionesNodosN> getListaEstadisticasLB() {
      return listaEstadisticasLB;
   }

   public void setListaEstadisticasLB(List<VWDSolucionesNodosN> listaEstadisticasLB) {
      this.listaEstadisticasLB = listaEstadisticasLB;
   }

   public List<VWDSolucionesNodosN> getFiltradoEstadisticasLB() {
      return filtradoEstadisticasLB;
   }

   public void setFiltradoEstadisticasLB(List<VWDSolucionesNodosN> filtradoEstadisticasLB) {
      this.filtradoEstadisticasLB = filtradoEstadisticasLB;
   }

   public VWDSolucionesNodosN getEstadisticaLBSeleccionada() {
      return estadisticaLBSeleccionada;
   }

   public void setEstadisticaLBSeleccionada(VWDSolucionesNodosN estadisticaLBSeleccionada) {
      this.estadisticaLBSeleccionada = estadisticaLBSeleccionada;
   }

   public List<VWDSolucionesNodosNDetalle> getListaDetalles() {
      return listaDetalles;
   }

   public void setListaDetalles(List<VWDSolucionesNodosNDetalle> listaDetalles) {
      this.listaDetalles = listaDetalles;
   }

   public BigInteger getValorTotalDetalles() {
      return valorTotalDetalles;
   }

   public void setValorTotalDetalles(BigInteger valorTotalDetalles) {
      this.valorTotalDetalles = valorTotalDetalles;
   }

   public BigDecimal getTotalUnidadesDetalles() {
      return totalUnidadesDetalles;
   }

   public void setTotalUnidadesDetalles(BigDecimal totalUnidadesDetalles) {
      this.totalUnidadesDetalles = totalUnidadesDetalles;
   }

   public List<Conceptos> getListaConceptos() {
      return listaConceptos;
   }

   public void setListaConceptos(List<Conceptos> listaConceptos) {
      this.listaConceptos = listaConceptos;
   }

   public List<Conceptos> getFiltradoConceptos() {
      return filtradoConceptos;
   }

   public void setFiltradoConceptos(List<Conceptos> filtradoConceptos) {
      this.filtradoConceptos = filtradoConceptos;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
      this.conceptoSeleccionado = conceptoSeleccionado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public String getDimension() {
      return dimension;
   }

   public void setDimension(String dimension) {
      this.dimension = dimension;
   }

   public String getPeriodicidad() {
      return periodicidad;
   }

   public void setPeriodicidad(String periodicidad) {
      this.periodicidad = periodicidad;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaDetalles() {
      return altoTablaDetalles;
   }

   public void setAltoTablaDetalles(String altoTablaDetalles) {
      this.altoTablaDetalles = altoTablaDetalles;
   }

   public List<VWDSolucionesNodosNDetalle> getFiltradoDetalles() {
      return filtradoDetalles;
   }

   public void setFiltradoDetalles(List<VWDSolucionesNodosNDetalle> filtradoDetalles) {
      this.filtradoDetalles = filtradoDetalles;
   }

   public String getDescripcionDetalle() {
      return descripcionDetalle;
   }

   public void setDescripcionDetalle(String descripcionDetalle) {
      this.descripcionDetalle = descripcionDetalle;
   }

   public List<Conceptos> getListaConceptosEspecificos() {
      return listaConceptosEspecificos;
   }

   public void setListaConceptosEspecificos(List<Conceptos> listaConceptosEspecificos) {
      this.listaConceptosEspecificos = listaConceptosEspecificos;
   }

   public Map<String, String> getConjuntoC() {
      return conjuntoC;
   }

   public void setConjuntoC(Map<String, String> conjuntoC) {
      this.conjuntoC = conjuntoC;
   }

   public Map<String, BigDecimal> getPorcentajesVariacion() {
      return porcentajesVariacion;
   }

   public void setPorcentajesVariacion(Map<String, BigDecimal> porcentajesVariacion) {
      this.porcentajesVariacion = porcentajesVariacion;
   }

   public String getTituloEditar() {
      return tituloEditar;
   }

   public void setTituloEditar(String tituloEditar) {
      this.tituloEditar = tituloEditar;
   }

   public String getTextoEditar() {
      return textoEditar;
   }

   public void setTextoEditar(String textoEditar) {
      this.textoEditar = textoEditar;
   }

   public List<VWDSolucionesNodosN> getPorcentajesExportar() {
      if (porcentajesExportar.size() > 1) {
         while (porcentajesExportar.size() > 1) {
            porcentajesExportar.remove((porcentajesExportar.size() - 1));
         }
      }
      return porcentajesExportar;
   }

   public void setPorcentajesExportar(List<VWDSolucionesNodosN> porcentajesExportar) {
      this.porcentajesExportar = porcentajesExportar;
   }

   public VWDSolucionesNodosN getDiferencia() {
      return diferencia;
   }

   public void setDiferencia(VWDSolucionesNodosN diferencia) {
      this.diferencia = diferencia;
   }

   public VWDSolucionesNodosN getTotalesLB() {
      return totalesLB;
   }

   public void setTotalesLB(VWDSolucionesNodosN totalesLB) {
      this.totalesLB = totalesLB;
   }

   public VWDSolucionesNodosN getTotales() {
      return totales;
   }

   public void setTotales(VWDSolucionesNodosN totales) {
      this.totales = totales;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaEstadisticas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroLB() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaEstadisticasLB");
      infoRegistroLB = String.valueOf(tabla.getRowCount());
      return infoRegistroLB;
   }

   public void setInfoRegistroLB(String infoRegistroLB) {
      this.infoRegistroLB = infoRegistroLB;
   }

   public String getInfoRegistroDetalles() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lOVDetalles");
      infoRegistroDetalles = String.valueOf(tabla.getRowCount());
      return infoRegistroDetalles;
   }

   public void setInfoRegistroDetalles(String infoRegistroDetalles) {
      this.infoRegistroDetalles = infoRegistroDetalles;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:LOVConceptos");
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

}
