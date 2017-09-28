package Controlador;

import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetalleLegislacionInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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
public class ControlDetalleLegislacion implements Serializable {

   private static Logger log = Logger.getLogger(ControlDetalleLegislacion.class);

   @EJB
   AdministrarDetalleLegislacionInterface administrarDetalleLegislacion;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<Formulascontratos> listFormulasContratosDetalle;
   private List<Formulascontratos> filtrarListFormulasContratosDetalle;
   private List<Formulascontratos> lovFormulasContratos;
   private List<Formulascontratos> filtrarListFormulasContratos;
   private Contratos contratoActual;
   private Formulascontratos formulaContratoActual;
   private Formulascontratos formulaContratosSeleccionado;
   //
   private List<Terceros> lovTerceros;
   private List<Terceros> filtrarListTerceros;
   private Terceros terceroLovSeleccionado;
   //
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtrarListPeriodicidades;
   private Periodicidades periodicidadSeleccionada;
   //
   private List<Formulas> lovFormulas;
   private List<Formulas> filtrarListFormulas;
   private Formulas formulaSeleccionada;
   //
   private int tipoActualizacion;
   private int bandera;
   private Contratos backUpContratoActual;
   //Columnas Tabla VL
   private Column formulaFechaInicial, formulaFechaFinal, formulaNombreLargo, formulaNombreCorto, formulaEstado, formulaPeriodicidad, formulaNit, formulaTercero, formulaObservacion, formulaConcepto;
   //Otros
   private boolean aceptar;
   private int index;
   private List<Formulascontratos> listFormulasContratosModificar;
   private boolean guardado;
   public Formulascontratos nuevoFormulaContrato;
   private List<Formulascontratos> listFormulasContratosCrear;
   private BigInteger l;
   private int k;
   private List<Formulascontratos> listFormulasContratosBorrar;
   private Formulascontratos editarFormulaContrato;
   private int cualCelda, tipoLista;
   private boolean cambioEditor, aceptarEditar;
   private Formulascontratos duplicarFormulaContrato;
   private boolean permitirIndex;
   //Variables Autompletar
   //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
   //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
   private String nombreXML;
   private String nombreTabla;
   private boolean cambiosFormulaContrato;
   private BigInteger secRegistroFormulaContrato;
   private BigInteger backUpSecRegistroFormulaCuenta;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private int indexAux;
   private String nombreLargo, nombreCorto, estado, periodicidad, nit, tercero, concepto, observacionFormula;
   private Date fechaIni;
   private Date fechaParametro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private ListasRecurrentes listasRecurrentes;

   public ControlDetalleLegislacion() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      lovFormulasContratos = null;
      fechaParametro = new Date(1, 1, 0);
      formulaContratoActual = new Formulascontratos();
      indexAux = 0;
      lovTerceros = null;
      lovPeriodicidades = null;
      backUpContratoActual = new Contratos();
      nombreTablaRastro = "";
      backUp = null;
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      secRegistroFormulaContrato = null;
      backUpSecRegistroFormulaCuenta = null;
      listFormulasContratosDetalle = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listFormulasContratosBorrar = new ArrayList<Formulascontratos>();
      //crear aficiones
      listFormulasContratosCrear = new ArrayList<Formulascontratos>();
      k = 0;
      //modificar aficiones
      listFormulasContratosModificar = new ArrayList<Formulascontratos>();
      //editar
      editarFormulaContrato = new Formulascontratos();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoFormulaContrato = new Formulascontratos();
      nuevoFormulaContrato.setTercero(new Terceros());
      nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
      nuevoFormulaContrato.setFormula(new Formulas());
      index = -1;
      bandera = 0;
      permitirIndex = true;
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreXML = "CuentasXML";
      duplicarFormulaContrato = new Formulascontratos();
      cambiosFormulaContrato = false;
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
      String pagActual = "detallelegislacion";
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

   public void limpiarListasValor() {
      lovFormulas = null;
      lovFormulasContratos = null;
      lovPeriodicidades = null;
      lovTerceros = null;
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
         administrarDetalleLegislacion.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void obtenerContrato(BigInteger secuencia) {
      index = -1;
      contratoActual = new Contratos();
      listFormulasContratosDetalle = null;
      contratoActual = administrarDetalleLegislacion.consultarContrato(secuencia);
      getListFormulasContratosDetalle();
      lovFormulasContratos = null;
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "detalleLegislacion");
   }

   public boolean validarRegistrosNuevos(int i) {
      boolean valor = false;
      if (i == 1) {
         if (nuevoFormulaContrato.getFechainicial() != null
                 && nuevoFormulaContrato.getFormula().getSecuencia() != null
                 && nuevoFormulaContrato.getPeriodicidad().getSecuencia() != null) {
            valor = true;
         }
      }
      if (i == 2) {
         if (duplicarFormulaContrato.getFechainicial() != null
                 && duplicarFormulaContrato.getFormula().getSecuencia() != null
                 && duplicarFormulaContrato.getPeriodicidad().getSecuencia() != null) {
            valor = true;
         }
      }
      return valor;
   }

   public boolean validarFechasRegistroHistoriaFormula(int i) {
      boolean retorno = false;
      if (i == 0) {
         Formulascontratos auxiliar = listFormulasContratosDetalle.get(i);
         if (auxiliar.getFechafinal() != null) {
            if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
               retorno = true;
            }
         }
         if (auxiliar.getFechafinal() == null) {
            if (auxiliar.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      if (i == 1) {
         if (nuevoFormulaContrato.getFechafinal() != null) {
            if (nuevoFormulaContrato.getFechainicial().after(fechaParametro) && (nuevoFormulaContrato.getFechainicial().before(nuevoFormulaContrato.getFechafinal()))) {
               retorno = true;
            }
         }
         if (nuevoFormulaContrato.getFechafinal() == null) {
            if (nuevoFormulaContrato.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      if (i == 2) {
         if (duplicarFormulaContrato.getFechafinal() != null) {
            if (duplicarFormulaContrato.getFechainicial().after(fechaParametro) && (duplicarFormulaContrato.getFechainicial().before(duplicarFormulaContrato.getFechafinal()))) {
               retorno = true;
            }
         }
         if (duplicarFormulaContrato.getFechafinal() == null) {
            if (duplicarFormulaContrato.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      return retorno;
   }

   public void modificacionesFechaHistoriaFormula(int i, int c) {
      Formulascontratos auxiliar = null;
      if (tipoLista == 0) {
         auxiliar = listFormulasContratosDetalle.get(i);
      }
      if (tipoLista == 1) {
         auxiliar = filtrarListFormulasContratosDetalle.get(i);
      }
      if ((auxiliar.getFechainicial() != null)) {
         boolean validacion = validarFechasRegistroHistoriaFormula(0);
         if (validacion == true) {
            cambiarIndice(i, c);
            modificarFormulaContrato(i);
            indexAux = i;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
            }
            if (tipoLista == 1) {
               filtrarListFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
            }
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
            index = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
         }
         if (tipoLista == 1) {
            filtrarListFormulasContratosDetalle.get(index).setFechainicial(fechaIni);
         }
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         index = -1;
      }
   }

   /**
    * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarFormulaContrato(int indice) {
      if (tipoLista == 0) {
         listFormulasContratosDetalle.get(indice).getFormula().setObservaciones(observacionFormula);
         listFormulasContratosDetalle.get(indice).setStrConcepto(concepto);
         if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(indice))) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
            } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(indice))) {
               listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         index = -1;
         secRegistroFormulaContrato = null;
      } else {
         filtrarListFormulasContratosDetalle.get(indice).getFormula().setObservaciones(observacionFormula);
         filtrarListFormulasContratosDetalle.get(indice).setStrConcepto(concepto);
         if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(indice))) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
            } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(indice))) {
               listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         index = -1;
         secRegistroFormulaContrato = null;
      }
      cambiosFormulaContrato = true;
   }

   public void modificarFormulaContratoAutocompletar(int indice, String confirmarCambio, String valorConfirmar) {
      index = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("NOMBRELARGO")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getFormula().setNombrelargo(nombreLargo);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getFormula().setNombrelargo(nombreLargo);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("NOMBRECORTO")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getFormula().setNombrecorto(nombreCorto);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getFormula().setNombrecorto(nombreCorto);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("ESTADO")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getFormula().setEstado(estado);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getFormula().setEstado(estado);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getEstado().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
            }
            lovTerceros.clear();
            getLovFormulas();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getPeriodicidad().setNombre(periodicidad);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getPeriodicidad().setNombre(periodicidad);
         }
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            }
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("NIT")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getTercero().setStrNit(nit);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getTercero().setStrNit(nit);
         }
         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setTercero(lovTerceros.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setTercero(lovTerceros.get(indiceUnicoElemento));
            }
            lovTerceros.clear();
            getLovTerceros();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(indice).getTercero().setNombre(tercero);
         } else {
            filtrarListFormulasContratosDetalle.get(indice).getTercero().setNombre(tercero);
         }
         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listFormulasContratosDetalle.get(indice).setTercero(lovTerceros.get(indiceUnicoElemento));
            } else {
               filtrarListFormulasContratosDetalle.get(indice).setTercero(lovTerceros.get(indiceUnicoElemento));
            }
            lovTerceros.clear();
            getLovTerceros();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (tipoLista == 0) {
            if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(indice))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
               } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(indice))) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistroFormulaContrato = null;
         } else {
            if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(indice))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
               } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(indice))) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistroFormulaContrato = null;
         }
         cambiosFormulaContrato = true;
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateo que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   //Ubicacion Celda.
   /**
    * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndice(int indice, int celda) {

      cualCelda = celda;
      index = indice;
      indexAux = indice;
      if (tipoLista == 0) {
         secRegistroFormulaContrato = listFormulasContratosDetalle.get(index).getSecuencia();
         formulaContratoActual = listFormulasContratosDetalle.get(index);
         fechaIni = listFormulasContratosDetalle.get(index).getFechainicial();
         if (cualCelda == 2) {
            nombreLargo = listFormulasContratosDetalle.get(index).getFormula().getNombrelargo();
         }
         if (cualCelda == 3) {
            nombreCorto = listFormulasContratosDetalle.get(index).getFormula().getNombrecorto();
         }
         if (cualCelda == 4) {
            estado = listFormulasContratosDetalle.get(index).getFormula().getEstado();
         }
         if (cualCelda == 5) {
            periodicidad = listFormulasContratosDetalle.get(index).getPeriodicidad().getNombre();
         }
         if (cualCelda == 6) {
            nit = listFormulasContratosDetalle.get(index).getTercero().getStrNit();
         }
         if (cualCelda == 7) {
            tercero = listFormulasContratosDetalle.get(index).getTercero().getNombre();
         }
         if (cualCelda == 8) {
            observacionFormula = listFormulasContratosDetalle.get(index).getFormula().getObservaciones();
         }
         if (cualCelda == 9) {
            concepto = listFormulasContratosDetalle.get(index).getStrConcepto();
         }
      }
      if (tipoLista == 1) {
         secRegistroFormulaContrato = filtrarListFormulasContratosDetalle.get(index).getSecuencia();
         formulaContratoActual = filtrarListFormulasContratosDetalle.get(index);
         fechaIni = filtrarListFormulasContratosDetalle.get(index).getFechainicial();
         if (cualCelda == 2) {
            nombreLargo = filtrarListFormulasContratosDetalle.get(index).getFormula().getNombrelargo();
         }
         if (cualCelda == 3) {
            nombreCorto = filtrarListFormulasContratosDetalle.get(index).getFormula().getNombrecorto();
         }
         if (cualCelda == 4) {
            estado = filtrarListFormulasContratosDetalle.get(index).getFormula().getEstado();
         }
         if (cualCelda == 5) {
            periodicidad = filtrarListFormulasContratosDetalle.get(index).getPeriodicidad().getNombre();
         }
         if (cualCelda == 6) {
            nit = filtrarListFormulasContratosDetalle.get(index).getTercero().getStrNit();
         }
         if (cualCelda == 7) {
            tercero = filtrarListFormulasContratosDetalle.get(index).getTercero().getNombre();
         }
         if (cualCelda == 8) {
            observacionFormula = filtrarListFormulasContratosDetalle.get(index).getFormula().getObservaciones();
         }
         if (cualCelda == 9) {
            concepto = filtrarListFormulasContratosDetalle.get(index).getStrConcepto();
         }
      }
   }

   //GUARDAR
   /**
    * Metodo de guardado general para la pagina
    */
   public void guardayYSalir() {
      guardadoGeneral();
      salir();
   }

   public void guardadoGeneral() {
      if (cambiosFormulaContrato == true) {
         guardarCambiosFormulaContrato();
      }
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina
    * VigenciasLocalizaciones
    */
   public void guardarCambiosFormulaContrato() {
      if (guardado == false) {
         if (!listFormulasContratosBorrar.isEmpty()) {
            administrarDetalleLegislacion.borrarFormulasContratos(listFormulasContratosBorrar);
            listFormulasContratosBorrar.clear();
         }
         if (!listFormulasContratosCrear.isEmpty()) {
            administrarDetalleLegislacion.crearFormulasContratos(listFormulasContratosCrear);
            listFormulasContratosCrear.clear();
         }
         if (!listFormulasContratosModificar.isEmpty()) {
            administrarDetalleLegislacion.modificarFormulasContratos(listFormulasContratosModificar);
            listFormulasContratosModificar.clear();
         }
         listFormulasContratosDetalle = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
         RequestContext.getCurrentInstance().update("form:growl");
         k = 0;
      }
      index = -1;
      secRegistroFormulaContrato = null;
      cambiosFormulaContrato = false;
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      if (bandera == 1) {
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
         formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
         formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
         formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
         formulaEstado.setFilterStyle("display: none; visibility: hidden;");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
         formulaNit.setFilterStyle("display: none; visibility: hidden;");
         formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("display: none; visibility: hidden;");
         formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
         formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
         formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
         formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         bandera = 0;
         filtrarListFormulasContratosDetalle = null;
         tipoLista = 0;
      }
      listFormulasContratosBorrar.clear();
      listFormulasContratosCrear.clear();
      listFormulasContratosModificar.clear();
      index = -1;
      indexAux = -1;
      secRegistroFormulaContrato = null;
      k = 0;
      listFormulasContratosDetalle = null;
      lovPeriodicidades = null;
      lovTerceros = null;
      lovFormulas = null;
      guardado = true;
      aceptar = true;
      cambiosFormulaContrato = false;
      lovFormulasContratos = null;
      getLovFormulasContratos();
      getLovFormulas();
      getListFormulasContratosModificar();
      getLovPeriodicidades();
      getLovTerceros();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarFormulaContrato = listFormulasContratosDetalle.get(index);
         }
         if (tipoLista == 1) {
            editarFormulaContrato = filtrarListFormulasContratosDetalle.get(index);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaIniFormula");
            RequestContext.getCurrentInstance().execute("PF('editarFechaIniFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinFormula");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreLargoFormula");
            RequestContext.getCurrentInstance().execute("PF('editarNombreLargoFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreCortoFormula");
            RequestContext.getCurrentInstance().execute("PF('editarNombreCortoFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoFormula");
            RequestContext.getCurrentInstance().execute("PF('editarEstadoFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadFormula");
            RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNitFormula");
            RequestContext.getCurrentInstance().execute("PF('editarNitFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroFormula");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionFormula");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptosFormula");
            RequestContext.getCurrentInstance().execute("PF('editarConceptosFormula').show()");
            cualCelda = -1;
         }
      }
      index = -1;
      secRegistroFormulaContrato = null;
   }

   public void validarIngresoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:NuevoRegistroFormula");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').show()");

   }

   public void validarDuplicadoRegistro() {
      if (index >= 0) {
         duplicarFormula();
      }
   }

   public void validarBorradoRegistro() {
      if (index >= 0) {
         borrarFormula();
      }
   }

   //CREAR VL
   /**
    * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
    */
   public void agregarNuevoFormula() {
      if (validarRegistrosNuevos(1) == true) {
         boolean validacion = validarFechasRegistroHistoriaFormula(1);
         if (validacion == true) {
            if (bandera == 1) {
               formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
               formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
               formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
               formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
               formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
               formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
               formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
               formulaEstado.setFilterStyle("display: none; visibility: hidden;");
               formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
               formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
               formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
               formulaNit.setFilterStyle("display: none; visibility: hidden;");
               formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
               formulaTercero.setFilterStyle("display: none; visibility: hidden;");
               formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
               formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
               formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
               formulaConcepto.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
               bandera = 0;
               filtrarListFormulasContratosDetalle = null;
               tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            k++;
            if (!nuevoFormulaContrato.getFormula().getObservaciones().isEmpty()) {
               String aux = nuevoFormulaContrato.getFormula().getObservaciones().toUpperCase();
               nuevoFormulaContrato.getFormula().setObservaciones(aux);
            }
            BigInteger var = BigInteger.valueOf(k);
            nuevoFormulaContrato.setSecuencia(var);
            nuevoFormulaContrato.setContrato(contratoActual);
            nuevoFormulaContrato.setStrConcepto(" ");
            listFormulasContratosCrear.add(nuevoFormulaContrato);
            listFormulasContratosDetalle.add(nuevoFormulaContrato);
            ////------////
            nuevoFormulaContrato = new Formulascontratos();
            nuevoFormulaContrato.setTercero(new Terceros());
            nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
            nuevoFormulaContrato.setFormula(new Formulas());
            ////-----////
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').hide()");
            cambiosFormulaContrato = true;
            index = -1;
            secRegistroFormulaContrato = null;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         log.warn("Error");
      }
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    * Metodo que limpia las casillas de la nueva vigencia
    */
   public void limpiarNuevoFormula() {
      nuevoFormulaContrato = new Formulascontratos();
      nuevoFormulaContrato.setTercero(new Terceros());
      nuevoFormulaContrato.setPeriodicidad(new Periodicidades());
      nuevoFormulaContrato.setFormula(new Formulas());
      index = -1;
      secRegistroFormulaContrato = null;
   }

   ////////--- //// ---////
   //DUPLICAR VL
   /**
    * Metodo que verifica que proceso de duplicar se genera con respecto a la
    * posicion en la pagina que se tiene
    */
   /**
    * Duplica una nueva vigencia localizacion
    */
   public void duplicarFormula() {
      if (index >= 0) {
         duplicarFormulaContrato = new Formulascontratos();
         if (tipoLista == 0) {
            duplicarFormulaContrato.setFechafinal(listFormulasContratosDetalle.get(index).getFechafinal());
            duplicarFormulaContrato.setFechainicial(listFormulasContratosDetalle.get(index).getFechainicial());
            duplicarFormulaContrato.setFormula(listFormulasContratosDetalle.get(index).getFormula());
            duplicarFormulaContrato.setPeriodicidad(listFormulasContratosDetalle.get(index).getPeriodicidad());
            duplicarFormulaContrato.setStrConcepto(listFormulasContratosDetalle.get(index).getStrConcepto());
            duplicarFormulaContrato.setTercero(listFormulasContratosDetalle.get(index).getTercero());
         }
         if (tipoLista == 1) {

            duplicarFormulaContrato.setFechafinal(filtrarListFormulasContratosDetalle.get(index).getFechafinal());
            duplicarFormulaContrato.setFechainicial(filtrarListFormulasContratosDetalle.get(index).getFechainicial());
            duplicarFormulaContrato.setFormula(filtrarListFormulasContratosDetalle.get(index).getFormula());
            duplicarFormulaContrato.setPeriodicidad(filtrarListFormulasContratosDetalle.get(index).getPeriodicidad());
            duplicarFormulaContrato.setStrConcepto(filtrarListFormulasContratosDetalle.get(index).getStrConcepto());
            duplicarFormulaContrato.setTercero(filtrarListFormulasContratosDetalle.get(index).getTercero());
         }
         if (duplicarFormulaContrato.getFormula().getSecuencia() == null) {
            duplicarFormulaContrato.setFormula(new Formulas());
         }
         if (duplicarFormulaContrato.getPeriodicidad().getSecuencia() == null) {
            duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
         }
         if (duplicarFormulaContrato.getTercero().getSecuencia() == null) {
            duplicarFormulaContrato.setTercero(new Terceros());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').show()");
         index = -1;
         secRegistroFormulaContrato = null;
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasLocalizaciones
    */
   public void confirmarDuplicar() {
      if (validarRegistrosNuevos(2) == true) {
         boolean validacion = validarFechasRegistroHistoriaFormula(2);
         if (validacion == true) {
            if (bandera == 1) {
               formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
               formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
               formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
               formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
               formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
               formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
               formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
               formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
               formulaEstado.setFilterStyle("display: none; visibility: hidden;");
               formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
               formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
               formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
               formulaNit.setFilterStyle("display: none; visibility: hidden;");
               formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
               formulaTercero.setFilterStyle("display: none; visibility: hidden;");
               formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
               formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
               formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
               formulaConcepto.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
               bandera = 0;
               filtrarListFormulasContratosDetalle = null;
               tipoLista = 0;
            }
            k++;
            BigInteger var = BigInteger.valueOf(k);
            if (!duplicarFormulaContrato.getFormula().getObservaciones().isEmpty()) {
               String aux = duplicarFormulaContrato.getFormula().getObservaciones().toUpperCase();
               duplicarFormulaContrato.getFormula().setObservaciones(aux);
            }
            duplicarFormulaContrato.setSecuencia(var);
            duplicarFormulaContrato.setContrato(contratoActual);
            duplicarFormulaContrato.setStrConcepto(" ");
            listFormulasContratosCrear.add(duplicarFormulaContrato);
            listFormulasContratosDetalle.add(duplicarFormulaContrato);
            duplicarFormulaContrato = new Formulascontratos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').hide()");
            cambiosFormulaContrato = true;
            index = -1;
            secRegistroFormulaContrato = null;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         log.warn("Error");
      }
   }

   public void limpiarDuplicarFormula() {
      duplicarFormulaContrato = new Formulascontratos();
      duplicarFormulaContrato.setTercero(new Terceros());
      duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
      duplicarFormulaContrato.setFormula(new Formulas());
      index = -1;
      secRegistroFormulaContrato = null;
   }

   ///////////////////////////////////////////////////////////////
   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void borrarFormula() {
      if (index >= 0) {
         if (tipoLista == 0) {
            if (!listFormulasContratosModificar.isEmpty() && listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
               int modIndex = listFormulasContratosModificar.indexOf(listFormulasContratosDetalle.get(index));
               listFormulasContratosModificar.remove(modIndex);
               listFormulasContratosBorrar.add(listFormulasContratosDetalle.get(index));
            } else if (!listFormulasContratosCrear.isEmpty() && listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
               int crearIndex = listFormulasContratosCrear.indexOf(listFormulasContratosDetalle.get(index));
               listFormulasContratosCrear.remove(crearIndex);
            } else {
               listFormulasContratosBorrar.add(listFormulasContratosDetalle.get(index));
            }
            listFormulasContratosDetalle.remove(index);
         }
         if (tipoLista == 1) {
            if (!listFormulasContratosModificar.isEmpty() && listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
               int modIndex = listFormulasContratosModificar.indexOf(filtrarListFormulasContratosDetalle.get(index));
               listFormulasContratosModificar.remove(modIndex);
               listFormulasContratosBorrar.add(filtrarListFormulasContratosDetalle.get(index));
            } else if (!listFormulasContratosCrear.isEmpty() && listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
               int crearIndex = listFormulasContratosCrear.indexOf(filtrarListFormulasContratosDetalle.get(index));
               listFormulasContratosCrear.remove(crearIndex);
            } else {
               listFormulasContratosBorrar.add(filtrarListFormulasContratosDetalle.get(index));
            }
            int VLIndex = listFormulasContratosDetalle.indexOf(filtrarListFormulasContratosDetalle.get(index));
            listFormulasContratosDetalle.remove(VLIndex);
            filtrarListFormulasContratosDetalle.remove(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");

         index = -1;
         secRegistroFormulaContrato = null;
         cambiosFormulaContrato = true;
         if (guardado == true) {
            guardado = false;
         }
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      filtradoFormula();

   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia localizacion
    */
   public void filtradoFormula() {
      if (bandera == 0) {
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("width: 85% !important");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("width: 85% !important");
         formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
         formulaNombreLargo.setFilterStyle("width: 85% !important");
         formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
         formulaNombreCorto.setFilterStyle("width: 85% !important");
         formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
         formulaEstado.setFilterStyle("width: 85% !important");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("width: 85% !important");
         formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
         formulaNit.setFilterStyle("width: 85% !important");
         formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("width: 85% !important");
         formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
         formulaObservacion.setFilterStyle("width: 85% !important");
         formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
         formulaConcepto.setFilterStyle("width: 85% !important");

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         bandera = 1;
      } else if (bandera == 1) {
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
         formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
         formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
         formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
         formulaEstado.setFilterStyle("display: none; visibility: hidden;");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
         formulaNit.setFilterStyle("display: none; visibility: hidden;");
         formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("display: none; visibility: hidden;");
         formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
         formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
         formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
         formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         bandera = 0;
         filtrarListFormulasContratosDetalle = null;
         tipoLista = 0;
      }

   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreLargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreLargo");
         formulaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
         formulaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNombreCorto");
         formulaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
         formulaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaEstado");
         formulaEstado.setFilterStyle("display: none; visibility: hidden;");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         formulaNit = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaNit");
         formulaNit.setFilterStyle("display: none; visibility: hidden;");
         formulaTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("display: none; visibility: hidden;");
         formulaObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaObservacion");
         formulaObservacion.setFilterStyle("display: none; visibility: hidden;");
         formulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaContrato:formulaConcepto");
         formulaConcepto.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         bandera = 0;
         filtrarListFormulasContratosDetalle = null;
         tipoLista = 0;
      }
      listFormulasContratosBorrar.clear();
      listFormulasContratosCrear.clear();
      listFormulasContratosModificar.clear();
      index = -1;
      secRegistroFormulaContrato = null;
      listFormulasContratosDetalle = null;
      k = 0;
      listFormulasContratosDetalle = null;
      lovFormulasContratos = null;
      guardado = true;
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   /**
    * Metodo que ejecuta los dialogos de estructuras, motivos localizaciones,
    * proyectos
    *
    * @param indice Fila de la tabla
    * @param dlg Dialogo
    * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
    *
    */
   public void asignarIndex(Integer indice, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (LND == 0) {
         index = indice;
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
      } else if (dlg == 2) {
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   /**
    * Metodo que activa la lista de valores de todas las tablas con respecto al
    * index activo y la columna activa
    */
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (index >= 0) {
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   /**
    * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
    * posicion en la pagina
    */
   /**
    * Metodo que activa el boton aceptar de la pagina y los dialogos
    *
    * @param tipoNuevo
    * @param Campo
    */
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("NOMBRELARGO")) {
         if (tipoNuevo == 1) {
            nombreLargo = nuevoFormulaContrato.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            nombreLargo = duplicarFormulaContrato.getFormula().getNombrelargo();
         }
      } else if (Campo.equals("NOMBRECORTO")) {
         if (tipoNuevo == 1) {
            nombreCorto = nuevoFormulaContrato.getFormula().getNombrecorto();
         } else if (tipoNuevo == 2) {
            nombreCorto = duplicarFormulaContrato.getFormula().getNombrecorto();
         }
      } else if (Campo.equals("ESTADO")) {
         if (tipoNuevo == 1) {
            nombreCorto = nuevoFormulaContrato.getFormula().getEstado();
         } else if (tipoNuevo == 2) {
            nombreCorto = duplicarFormulaContrato.getFormula().getEstado();
         }
      } else if (Campo.equals("PERIODICIDAD")) {
         if (tipoNuevo == 1) {
            nombreCorto = nuevoFormulaContrato.getPeriodicidad().getNombre();
         } else if (tipoNuevo == 2) {
            nombreCorto = duplicarFormulaContrato.getPeriodicidad().getNombre();
         }
      } else if (Campo.equals("NIT")) {
         if (tipoNuevo == 1) {
            nombreCorto = nuevoFormulaContrato.getTercero().getStrNit();
         } else if (tipoNuevo == 2) {
            nombreCorto = duplicarFormulaContrato.getTercero().getStrNit();
         }
      } else if (Campo.equals("TERCERO")) {
         if (tipoNuevo == 1) {
            nombreCorto = nuevoFormulaContrato.getTercero().getNombre();
         } else if (tipoNuevo == 2) {
            nombreCorto = duplicarFormulaContrato.getTercero().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("NOMBRELARGO")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getFormula().setNombrelargo(nombreLargo);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getFormula().setNombrelargo(nombreLargo);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaObservacion");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaObservacion");
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreLargo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreLargo");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NOMBRECORTO")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getFormula().setNombrecorto(nombreCorto);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getFormula().setNombrecorto(nombreCorto);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaObservacion");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaObservacion");
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreCorto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreCorto");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("ESTADO")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getFormula().setEstado(estado);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getFormula().setEstado(estado);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getEstado().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaObservacion");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreLargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreCorto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaEstado");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaObservacion");
            }
            lovFormulas.clear();
            getLovFormulas();
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaEstado");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaEstado");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDADES")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getPeriodicidad().setNombre(periodicidad);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getPeriodicidad().setNombre(periodicidad);
         }
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaPeriodicidad");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaPeriodicidad");
            }
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaPeriodicidad");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaPeriodicidad");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getTercero().setStrNit(nit);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getTercero().setStrNit(nit);
         }
         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaTercero");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaTercero");
            }
            lovTerceros.clear();
            getLovTerceros();
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNit");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNit");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
         if (tipoNuevo == 1) {
            nuevoFormulaContrato.getTercero().setNombre(tercero);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getTercero().setNombre(tercero);
         }
         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaTercero");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNit");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaTercero");
            }
            lovTerceros.clear();
            getLovTerceros();
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaTercero");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaTercero");
            }
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (!formulaSeleccionada.getObservaciones().isEmpty()) {
            String aux = formulaSeleccionada.getObservaciones().toUpperCase();
            formulaSeleccionada.setObservaciones(aux);
         } else {
            formulaSeleccionada.setObservaciones(" ");
         }
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(index).setFormula(formulaSeleccionada);
            if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               }
            }
         } else {
            filtrarListFormulasContratosDetalle.get(index).setFormula(formulaSeleccionada);
            if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosFormulaContrato = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevoFormulaContrato.setFormula(formulaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreLargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNombreCorto");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaEstado");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaObservacion");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setFormula(formulaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreLargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNombreCorto");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaEstado");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaObservacion");
      }
      filtrarListFormulas = null;
      formulaSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;

      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");

   }

   public void cancelarCambioFormula() {
      filtrarListFormulas = null;
      formulaSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
   }

   public void actualizarTercero() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(index).setTercero(terceroLovSeleccionado);
            if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               }
            }
         } else {
            filtrarListFormulasContratosDetalle.get(index).setTercero(terceroLovSeleccionado);
            if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosFormulaContrato = true;
         permitirIndex = true;

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevoFormulaContrato.setTercero(terceroLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaNit");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaTercero");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setTercero(terceroLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaNit");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaTercero");
      }
      filtrarListTerceros = null;
      terceroLovSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;

      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void cancelarCambioTercero() {
      filtrarListTerceros = null;
      terceroLovSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void actualizarPeriodicidad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listFormulasContratosDetalle.get(index).setPeriodicidad(periodicidadSeleccionada);
            if (!listFormulasContratosCrear.contains(listFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(listFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(listFormulasContratosDetalle.get(index));
               }
            }
         } else {
            filtrarListFormulasContratosDetalle.get(index).setPeriodicidad(periodicidadSeleccionada);
            if (!listFormulasContratosCrear.contains(filtrarListFormulasContratosDetalle.get(index))) {
               if (listFormulasContratosModificar.isEmpty()) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               } else if (!listFormulasContratosModificar.contains(filtrarListFormulasContratosDetalle.get(index))) {
                  listFormulasContratosModificar.add(filtrarListFormulasContratosDetalle.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosFormulaContrato = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevoFormulaContrato.setPeriodicidad(periodicidadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaPeriodicidad");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setPeriodicidad(periodicidadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaFormulaPeriodicidad");
      }
      filtrarListPeriodicidades = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;

      context.reset("form:lovPeriodicidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').hide()");
   }

   public void cancelarCambioPeriodicidad() {
      filtrarListPeriodicidades = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistroFormulaContrato = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovPeriodicidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').hide()");
   }

   /**
    * Selecciona la tabla a exportar XML con respecto al index activo
    *
    * @return Nombre del dialogo a exportar en XML
    */
   public String exportXML() {
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreXML = "FormulaContrato_XML";

      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      exportPDF_FC();

   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_FC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formExportarFormula:datosFormulaContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FormulaContrato_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistroFormulaContrato = null;
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      exportXLS_FC();

   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_FC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formExportarFormula:datosFormulaContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FormulaContrato_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistroFormulaContrato = null;
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrar() {
      if (index >= 0) {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
      }
   }

   public void dialogoSeleccionarFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:BuscarFormulaDialogo");
      RequestContext.getCurrentInstance().execute("PF('BuscarFormulaDialogo').show()");
   }

   public void cancelarSeleccionFormula() {
      formulaContratosSeleccionado = new Formulascontratos();
      filtrarListFormulasContratosDetalle = null;
   }

   public void validarSeleccionFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosFormulaContrato == false) {
         listFormulasContratosDetalle = null;
         listFormulasContratosDetalle = new ArrayList<Formulascontratos>();
         listFormulasContratosDetalle.add(formulaContratosSeleccionado);
         formulaContratosSeleccionado = new Formulascontratos();
         filtrarListPeriodicidades = null;
         aceptar = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else {
         formulaContratosSeleccionado = new Formulascontratos();
         filtrarListFormulasContratosDetalle = null;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosFormulaContrato == false) {
         listFormulasContratosDetalle = null;
         getListFormulasContratosDetalle();
         aceptar = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
   public void verificarRastroTabla() {
      verificarRastroFormulaContrato();
      index = -1;
   }

   //Verificar Rastro Vigencia Sueldos
   public void verificarRastroFormulaContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listFormulasContratosDetalle != null) {
         if (secRegistroFormulaContrato != null) {
            int resultado = administrarRastros.obtenerTabla(secRegistroFormulaContrato, "FORMULASCONTRATOS");
            backUpSecRegistroFormulaCuenta = secRegistroFormulaContrato;
            backUp = secRegistroFormulaContrato;
            secRegistroFormulaContrato = null;
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "Formulascontratos";
               msnConfirmarRastro = "La tabla FORMULASCONTRATOS tiene rastros para el registro seleccionado, ¿desea continuar?";
               RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
               RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
               RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASCONTRATOS")) {
         nombreTablaRastro = "Formulascontratos";
         msnConfirmarRastroHistorico = "La tabla FORMULASCONTRATOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //GET - SET 
   public List<Formulascontratos> getListFormulasContratosDetalle() {
      try {
         if (listFormulasContratosDetalle == null) {
            listFormulasContratosDetalle = new ArrayList<Formulascontratos>();
            listFormulasContratosDetalle = administrarDetalleLegislacion.consultarListaFormulasContratosContrato(contratoActual.getSecuencia());
            if (listFormulasContratosDetalle != null) {
               for (int i = 0; i < listFormulasContratosDetalle.size(); i++) {
                  if (listFormulasContratosDetalle.get(i).getFormula() == null) {
                     listFormulasContratosDetalle.get(i).setFormula(new Formulas());
                  }
                  if (listFormulasContratosDetalle.get(i).getTercero() == null) {
                     listFormulasContratosDetalle.get(i).setTercero(new Terceros());
                  }
                  if (listFormulasContratosDetalle.get(i).getPeriodicidad() == null) {
                     listFormulasContratosDetalle.get(i).setPeriodicidad(new Periodicidades());
                  }
                  if (!listFormulasContratosDetalle.get(i).getFormula().getObservaciones().isEmpty()) {
                     String aux = listFormulasContratosDetalle.get(i).getFormula().getObservaciones();
                     listFormulasContratosDetalle.get(i).getFormula().setObservaciones(aux);
                  }
               }
            }
         }
         return listFormulasContratosDetalle;
      } catch (Exception e) {
         log.warn("Error getListFormulasContratosDetalle : " + e.toString());
         return null;
      }
   }

   public void setListFormulasContratosDetalle(List<Formulascontratos> t) {
      this.listFormulasContratosDetalle = t;
   }

   public List<Formulascontratos> getFiltrarListFormulasContratosDetalle() {
      return filtrarListFormulasContratosDetalle;
   }

   public void setFiltrarListFormulasContratosDetalle(List<Formulascontratos> t) {
      this.filtrarListFormulasContratosDetalle = t;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public BigInteger getSecRegistroFormulaContrato() {
      return secRegistroFormulaContrato;
   }

   public void setSecRegistroFormulaContrato(BigInteger setSecRegistroFormulaContrato) {
      this.secRegistroFormulaContrato = setSecRegistroFormulaContrato;
   }

   public BigInteger getBackUpSecRegistroFormulaCuenta() {
      return backUpSecRegistroFormulaCuenta;
   }

   public void setBackUpSecRegistroFormulaCuenta(BigInteger b) {
      this.backUpSecRegistroFormulaCuenta = b;
   }

   public List<Formulascontratos> getListFormulasContratosModificar() {
      return listFormulasContratosModificar;
   }

   public void setListFormulasContratosModificar(List<Formulascontratos> setListFormulasContratosModificar) {
      this.listFormulasContratosModificar = setListFormulasContratosModificar;
   }

   public Formulascontratos getNuevoFormulaContrato() {
      return nuevoFormulaContrato;
   }

   public void setNuevoFormulaContrato(Formulascontratos setNuevoFormulaContrato) {
      this.nuevoFormulaContrato = setNuevoFormulaContrato;
   }

   public List<Formulascontratos> getListFormulasContratosCrear() {
      return listFormulasContratosCrear;
   }

   public void setListFormulasContratosCrear(List<Formulascontratos> setListFormulasContratosCrear) {
      this.listFormulasContratosCrear = setListFormulasContratosCrear;
   }

   public List<Formulascontratos> getListFormulasContratosBorrar() {
      return listFormulasContratosBorrar;
   }

   public void setListFormulasContratosBorrar(List<Formulascontratos> setListFormulasContratosBorrar) {
      this.listFormulasContratosBorrar = setListFormulasContratosBorrar;
   }

   public Formulascontratos getEditarFormulaContrato() {
      return editarFormulaContrato;
   }

   public void setEditarFormulaContrato(Formulascontratos setEditarFormulaContrato) {
      this.editarFormulaContrato = setEditarFormulaContrato;
   }

   public Formulascontratos getDuplicarFormulaContrato() {
      return duplicarFormulaContrato;
   }

   public void setDuplicarFormulaContrato(Formulascontratos setDuplicarFormulaContrato) {
      this.duplicarFormulaContrato = setDuplicarFormulaContrato;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public Contratos getBackUpContratoActual() {
      return backUpContratoActual;
   }

   public void setBackUpContratoActual(Contratos setBackUpContratoActual) {
      this.backUpContratoActual = setBackUpContratoActual;
   }

   public List<Terceros> getLovTerceros() {
      if (lovTerceros == null) {
         lovTerceros = administrarDetalleLegislacion.consultarLOVTerceros();
      }
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> setListTerceros) {
      this.lovTerceros = setListTerceros;
   }

   public List<Terceros> getFiltrarListTerceros() {
      return filtrarListTerceros;
   }

   public void setFiltrarListTerceros(List<Terceros> filtrar) {
      this.filtrarListTerceros = filtrar;
   }

   public Terceros getTerceroLovSeleccionado() {
      return terceroLovSeleccionado;
   }

   public void setTerceroLovSeleccionado(Terceros seleccionado) {
      this.terceroLovSeleccionado = seleccionado;
   }

   public Formulascontratos getFormulaContratoActual() {
      return formulaContratoActual;
   }

   public void setFormulaContratoActual(Formulascontratos formulaContratoActual) {
      this.formulaContratoActual = formulaContratoActual;
   }

   public Contratos getContratoActual() {
      return contratoActual;
   }

   public void setContratoActual(Contratos contratoActual) {
      this.contratoActual = contratoActual;
   }

   public Formulascontratos getFormulaContratosSeleccionado() {
      return formulaContratosSeleccionado;
   }

   public void setFormulaContratosSeleccionado(Formulascontratos formulaContratosSeleccionado) {
      this.formulaContratosSeleccionado = formulaContratosSeleccionado;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarDetalleLegislacion.consultarLOVPeriodicidades();
      }
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
      this.lovPeriodicidades = lovPeriodicidades;
   }

   public List<Periodicidades> getFiltrarListPeriodicidades() {
      return filtrarListPeriodicidades;
   }

   public void setFiltrarListPeriodicidades(List<Periodicidades> filtrarListPeriodicidades) {
      this.filtrarListPeriodicidades = filtrarListPeriodicidades;
   }

   public Periodicidades getPeriodicidadSeleccionada() {
      return periodicidadSeleccionada;
   }

   public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
      this.periodicidadSeleccionada = periodicidadSeleccionada;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         if (listasRecurrentes.getLovFormulas().isEmpty()) {
            lovFormulas = administrarDetalleLegislacion.consultarLOVFormulas();
            if (lovFormulas != null) {
               listasRecurrentes.setLovFormulas(lovFormulas);
            }
         } else {
            lovFormulas = new ArrayList<Formulas>(listasRecurrentes.getLovFormulas());
         }
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<Formulas> getFiltrarListFormulas() {
      return filtrarListFormulas;
   }

   public void setFiltrarListFormulas(List<Formulas> filtrarListFormulas) {
      this.filtrarListFormulas = filtrarListFormulas;
   }

   public Formulas getFormulaSeleccionada() {
      return formulaSeleccionada;
   }

   public void setFormulaSeleccionada(Formulas formulaSeleccionada) {
      this.formulaSeleccionada = formulaSeleccionada;
   }

   public List<Formulascontratos> getLovFormulasContratos() {
      if (lovFormulasContratos == null) {
         lovFormulasContratos = administrarDetalleLegislacion.consultarListaFormulasContratosContrato(contratoActual.getSecuencia());
      }
      return lovFormulasContratos;
   }

   public void setLovFormulasContratos(List<Formulascontratos> lovFormulasContratos) {
      this.lovFormulasContratos = lovFormulasContratos;
   }

   public List<Formulascontratos> getFiltrarListFormulasContratos() {
      return filtrarListFormulasContratos;
   }

   public void setFiltrarListFormulasContratos(List<Formulascontratos> filtrarListFormulasContratos) {
      this.filtrarListFormulasContratos = filtrarListFormulasContratos;
   }

}
