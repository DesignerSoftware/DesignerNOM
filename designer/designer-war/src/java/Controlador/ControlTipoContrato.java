package Controlador;

import Entidades.DiasLaborables;
import Entidades.TiposContratos;
import Entidades.TiposDias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposContratosInterface;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlTipoContrato implements Serializable {

   @EJB
   AdministrarTiposContratosInterface administrarTipoContrato;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<TiposContratos> listaTiposContratos;
   private List<TiposContratos> filtrarListaTiposContratos;
   private TiposContratos tipoContratoSeleccionado;
   ///////
   private List<DiasLaborables> listaDiasLaborables;
   private List<DiasLaborables> filtrarListaDiasLaborables;
   private DiasLaborables diaLaborableSeleccionado;
   //Activo/Desactivo Crtl + F11
   private int bandera, banderaDiasLab;
   //Columnas Tabla VC
   private Column tipoCCodigo, tipoCNombre, tipoCPeriodo, tipoCVE, tipoCForza;
   private Column diasLabTipoDia, diasLabDia, diasLabHL;
   //Otros
   private boolean aceptar;
   //modificar
   private List<TiposContratos> listTiposContratosModificar;
   private List<DiasLaborables> listDiasLaborablesModificar;
   private boolean guardado, guardadoDias;
   //crear 
   private TiposContratos nuevoTipoContrato;
   private DiasLaborables nuevoDiaLaborable;
   private List<TiposContratos> listTiposContratosCrear;
   private List<DiasLaborables> listDiasLaborablesCrear;
   private BigInteger newSecuencia;
   private int k;
   //borrar 
   private List<TiposContratos> listTiposContratosBorrar;
   private List<DiasLaborables> listDiasLaborablesBorrar;
   //editar celda
   private TiposContratos editarTipoContrato;
   private DiasLaborables editarDiaLaborable;
   private int cualCelda, tipoLista, cualCeldaDias, tipoListaDias;
   //duplicar
   private TiposContratos duplicarTipoContrato;
   private DiasLaborables duplicarDiaLaborable;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String tipoDia;
   //////////////////////
   private List<TiposDias> lovTiposDias;
   private List<TiposDias> filtrarLovTiposDias;
   private TiposDias tipoDiaLOVSeleccionado;
   private boolean permitirIndexDias;
   private int tipoActualizacion;
   private short auxCodigoTipoContrato;
   private String auxNombreTipoContrato;
   //
   private boolean cambiosPagina;
   //
   private String altoTablaTiposC, altoTablaDiasLab;
   //
   private String nombreTipoCClonar;
   private short codigoTipoCClonar;
   //
   private List<TiposContratos> lovTiposContratos;
   private List<TiposContratos> filtrarLovTiposContratos;
   private TiposContratos tipoContratoLOVSeleccionado;
   //
   private TiposContratos tipoContratoAClonar;
   //
   private String auxNombreClonar;
   private short auxCodigoClonar;
   //
   private String infoRegistroTipoDia, infoRegistroTipoContrato, infoRegistroTipoDiaLOV, infoRegistroTipoContratoLOV;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTipoContrato() {
      infoRegistroTipoContrato = "";
      infoRegistroTipoContratoLOV = "";
      infoRegistroTipoDia = "";
      infoRegistroTipoDiaLOV = "";

      tipoContratoAClonar = new TiposContratos();
      tipoContratoLOVSeleccionado = null;
      lovTiposContratos = null;
      altoTablaTiposC = "190";
      altoTablaDiasLab = "75";
      cambiosPagina = true;
      permitirIndexDias = true;
      tipoActualizacion = -1;
      lovTiposDias = null;
      lovTiposDias = null;
      tipoDiaLOVSeleccionado = null;
      listaTiposContratos = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listTiposContratosBorrar = new ArrayList<TiposContratos>();
      listDiasLaborablesBorrar = new ArrayList<DiasLaborables>();
      //crear aficiones
      listTiposContratosCrear = new ArrayList<TiposContratos>();
      listDiasLaborablesCrear = new ArrayList<DiasLaborables>();
      k = 0;
      //modificar aficiones
      listDiasLaborablesModificar = new ArrayList<DiasLaborables>();
      listTiposContratosModificar = new ArrayList<TiposContratos>();
      //editar
      editarTipoContrato = new TiposContratos();
      editarDiaLaborable = new DiasLaborables();
      cualCelda = -1;
      cualCeldaDias = -1;
      tipoListaDias = 0;
      tipoLista = 0;
      //guardar
      guardado = true;
      guardadoDias = true;
      //Crear VC
      nuevoTipoContrato = new TiposContratos();
      nuevoDiaLaborable = new DiasLaborables();
      nuevoDiaLaborable.setTipodia(new TiposDias());
      nuevoDiaLaborable.setHoraslaborables(BigInteger.valueOf(8));
      duplicarDiaLaborable = new DiasLaborables();
      duplicarTipoContrato = new TiposContratos();
      bandera = 0;
      listaDiasLaborables = null;
      banderaDiasLab = 0;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         */
String pagActual = "tipocontrato";
         
         
         


         
         
         
         
         
         
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

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTipoContrato.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaTiposContratos = null;
      getListaTiposContratos();
      if (listaTiposContratos.size() >= 1) {
         tipoContratoSeleccionado = listaTiposContratos.get(0);
         getListaDiasLaborables();
      }
   }

   public boolean validarCamposNulosTipoContrato(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (tipoContratoSeleccionado.getCodigo() < 0) {
            retorno = false;
         }
         if (tipoContratoSeleccionado.getNombre() == null) {
            retorno = false;
         } else if (tipoContratoSeleccionado.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoTipoContrato.getCodigo() < 0) {
            retorno = false;
         }
         if (nuevoTipoContrato.getNombre() == null) {
            retorno = false;
         } else if (nuevoTipoContrato.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarTipoContrato.getCodigo() < 0) {
            retorno = false;
         }
         if (duplicarTipoContrato.getNombre() == null) {
            retorno = false;
         } else if (duplicarTipoContrato.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosDiaLaborable(int i) {
      boolean retorno = true;
      if (i == 1) {
         if (nuevoDiaLaborable.getDia() == null || nuevoDiaLaborable.getTipodia().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarDiaLaborable.getDia() == null || duplicarDiaLaborable.getTipodia().getSecuencia() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void procesoModificacionTipoContrato(TiposContratos tiposContratos) {
      tipoContratoSeleccionado = tiposContratos;
      boolean respuesta = validarCamposNulosTipoContrato(0);
      if (respuesta == true) {
         modificarTipoContrato(tipoContratoSeleccionado);
      } else {
         tipoContratoSeleccionado.setCodigo(auxCodigoTipoContrato);
         tipoContratoSeleccionado.setNombre(auxNombreTipoContrato);

         RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoContrato').show()");
      }
   }

   public void modificarTipoContrato(TiposContratos tiposContratos) {
      int tamDes = 0;
      tipoContratoSeleccionado = tiposContratos;
      if (tipoLista == 0) {
         tamDes = tipoContratoSeleccionado.getNombre().length();

         if (tamDes >= 1 && tamDes <= 30) {
            String textM = tipoContratoSeleccionado.getNombre().toUpperCase();
            tipoContratoSeleccionado.setNombre(textM);
            if (!listTiposContratosCrear.contains(tipoContratoSeleccionado)) {
               if (listTiposContratosModificar.isEmpty()) {
                  listTiposContratosModificar.add(tipoContratoSeleccionado);
               } else if (!listTiposContratosModificar.contains(tipoContratoSeleccionado)) {
                  listTiposContratosModificar.add(tipoContratoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            cambiosPagina = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         } else {
            tipoContratoSeleccionado.setNombre(auxNombreTipoContrato);

            RequestContext.getCurrentInstance().update("form:datosTipoContrato");
            RequestContext.getCurrentInstance().execute("PF('errorNombreTipoContrato').show()");
         }
      }
   }

   public void modificarDiaLaborable(DiasLaborables diasLaborables) {
      diaLaborableSeleccionado = diasLaborables;
      if (!listDiasLaborablesCrear.contains(diaLaborableSeleccionado)) {
         if (listDiasLaborablesModificar.isEmpty()) {
            listDiasLaborablesModificar.add(diaLaborableSeleccionado);
         } else if (!listDiasLaborablesModificar.contains(diaLaborableSeleccionado)) {
            listDiasLaborablesModificar.add(diaLaborableSeleccionado);
         }
         if (guardadoDias == true) {
            guardadoDias = false;
         }
      }

      cambiosPagina = false;

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
   }

   public void modificarDiaLaborable(DiasLaborables diasLaborables, String column, String valorConfirmar) {
      diaLaborableSeleccionado = diasLaborables;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (column.equalsIgnoreCase("DIAS")) {
         diaLaborableSeleccionado.getTipodia().setDescripcion(tipoDia);

         for (int i = 0; i < lovTiposDias.size(); i++) {
            if (lovTiposDias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            diaLaborableSeleccionado.setTipodia(lovTiposDias.get(indiceUnicoElemento));

            lovTiposDias.clear();
            getLovTiposDias();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexDias = false;
            RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listDiasLaborablesCrear.contains(diaLaborableSeleccionado)) {
            if (listDiasLaborablesModificar.isEmpty()) {
               listDiasLaborablesModificar.add(diaLaborableSeleccionado);
            } else if (!listDiasLaborablesModificar.contains(diaLaborableSeleccionado)) {
               listDiasLaborablesModificar.add(diaLaborableSeleccionado);
            }
            if (guardadoDias == true) {
               guardadoDias = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
   }

   public void cambiarIndice(TiposContratos tiposContratos, int celda) {
      System.out.println("tipoContratoTablaSeleccionado antes : " + tipoContratoSeleccionado);
      tipoContratoSeleccionado = tiposContratos;
      diaLaborableSeleccionado = null;
      cualCelda = celda;
      deshabilitarBotonLov();
      if (cualCelda == 0) {
         auxCodigoTipoContrato = tipoContratoSeleccionado.getCodigo();
      } else if (cualCelda == 1) {
         auxNombreTipoContrato = tipoContratoSeleccionado.getNombre();
      } else if (cualCelda == 2) {
         tipoContratoSeleccionado.getDuracionperiodoprueba();
      }
      System.out.println("Secuencia : " + tipoContratoSeleccionado.getSecuencia());
      listaDiasLaborables = administrarTipoContrato.listaDiasLaborablesParaTipoContrato(tipoContratoSeleccionado.getSecuencia());
      System.out.println("cambiarIndice. listaDiasLaborables : " + listaDiasLaborables);

      contarRegistrosTipoD();
      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
   }

   public void cambiarIndiceDia(DiasLaborables diasLaborables, int celda) {
      diaLaborableSeleccionado = diasLaborables;
      cualCeldaDias = celda;
      if (cualCeldaDias == 0) {
         tipoDia = diaLaborableSeleccionado.getTipodia().getDescripcion();
         habilitarBotonLov();
      } else if (cualCeldaDias == 2) {
         diaLaborableSeleccionado.getHoraslaborables();
         deshabilitarBotonLov();
      }
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GUARDAR
   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   public void cancelarYSalir() {
      cancelarModificacionGeneral();
      salir();
   }

   public void guardarGeneral() {
      if (guardado == false || guardadoDias == false) {
         if (guardado == false) {
            guardarCambiosTipoContrato();
         }
         if (guardadoDias == false) {
            guardarCambiosDiasLaborables();
         }
         cambiosPagina = true;
         contarRegistrosTipoC();
         contarRegistrosTipoD();

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosTipoContrato() {
      try {
         if (!listTiposContratosBorrar.isEmpty()) {
            administrarTipoContrato.borrarTiposContratos(listTiposContratosBorrar);
            listTiposContratosBorrar.clear();
         }
         if (!listTiposContratosCrear.isEmpty()) {
            administrarTipoContrato.crearTiposContratos(listTiposContratosCrear);
            listTiposContratosCrear.clear();
         }
         if (!listTiposContratosModificar.isEmpty()) {
            administrarTipoContrato.editarTiposContratos(listTiposContratosModificar);
            listTiposContratosModificar.clear();
         }
         listaTiposContratos = null;
         getListaTiposContratos();

         RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;

         FacesMessage msg = new FacesMessage("Información", "Los datos de Tipo Contrato se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosTipoContrato : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Tipo Contrato, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosDiasLaborables() {
      try {
         if (!listDiasLaborablesBorrar.isEmpty()) {
            administrarTipoContrato.borrarDiasLaborables(listDiasLaborablesBorrar);

            listDiasLaborablesBorrar.clear();
         }
         if (!listDiasLaborablesCrear.isEmpty()) {
            administrarTipoContrato.crearDiasLaborables(listDiasLaborablesCrear);

            listDiasLaborablesCrear.clear();
         }
         if (!listDiasLaborablesModificar.isEmpty()) {
            administrarTipoContrato.editarDiasLaborables(listDiasLaborablesModificar);
            listDiasLaborablesModificar.clear();
         }
         listaDiasLaborables = null;

         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
         guardadoDias = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;

         FacesMessage msg = new FacesMessage("Información", "Los datos de Dias Laborables se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosDiasLaborables : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Dias Laborables, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacionGeneral() {

      tipoContratoSeleccionado = null;
      diaLaborableSeleccionado = null;
      if (guardado == false) {
         cancelarModificacionTipoContrato();
      }
      if (guardadoDias == false) {
         cancelarModificacionDiaLaborable();
      }
      cancelarProcesoClonado();
      contarRegistrosTipoC();
      contarRegistrosTipoD();
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:datosTipoContrato");
      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarProcesoClonado() {
      codigoTipoCClonar = 0;
      nombreTipoCClonar = "";
      tipoContratoAClonar = new TiposContratos();

      RequestContext.getCurrentInstance().update("form:codigoTipoCClonar");
      RequestContext.getCurrentInstance().update("form:nombreTipoCClonar");
      RequestContext.getCurrentInstance().update("form:codigoTipoCClonarBase");
      RequestContext.getCurrentInstance().update("form:nombreTipoCClonarBase");
   }

   public void cancelarModificacionTipoContrato() {
      if (bandera == 1) {
         altoTablaTiposC = "190";
         tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
         tipoCCodigo.setFilterStyle("display: none; visibility: hidden;");
         tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
         tipoCNombre.setFilterStyle("display: none; visibility: hidden;");
         tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
         tipoCPeriodo.setFilterStyle("display: none; visibility: hidden;");
         tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
         tipoCVE.setFilterStyle("display: none; visibility: hidden;");
         tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
         tipoCForza.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         bandera = 0;
         filtrarListaTiposContratos = null;
         tipoLista = 0;
      }
      listTiposContratosBorrar.clear();
      listTiposContratosCrear.clear();
      listTiposContratosModificar.clear();
      tipoContratoSeleccionado = null;
      k = 0;
      listaTiposContratos = null;
      guardado = true;

      RequestContext.getCurrentInstance().update("form:datosTipoContrato");
   }

   public void cancelarModificacionDiaLaborable() {
      if (banderaDiasLab == 1) {
         altoTablaDiasLab = "75";
         diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
         diasLabDia.setFilterStyle("display: none; visibility: hidden;");
         diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
         diasLabTipoDia.setFilterStyle("display: none; visibility: hidden;");
         diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
         diasLabHL.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
         banderaDiasLab = 0;
         filtrarListaDiasLaborables = null;
         tipoListaDias = 0;
      }
      listDiasLaborablesBorrar.clear();
      listDiasLaborablesCrear.clear();
      listDiasLaborablesModificar.clear();
      diaLaborableSeleccionado = null;
      k = 0;
      listaDiasLaborables = null;
      guardadoDias = true;
      permitirIndexDias = true;

      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
   }

   public void editarCelda() {

      if (diaLaborableSeleccionado != null) {
         editarDiaLaborable = diaLaborableSeleccionado;

         if (cualCeldaDias == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoDiaDiaLabD");
            RequestContext.getCurrentInstance().execute("PF('editarTipoDiaDiaLabD').show()");
            cualCeldaDias = -1;
         } else if (cualCeldaDias == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDiaDiaLabD");
            RequestContext.getCurrentInstance().execute("PF('editarDiaDiaLabD').show()");
            cualCeldaDias = -1;
         } else if (cualCeldaDias == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasDiaLabD");
            RequestContext.getCurrentInstance().execute("PF('editarHorasDiaLabD').show()");
            cualCeldaDias = -1;
         }
      } else if (tipoContratoSeleccionado != null) {
         editarTipoContrato = tipoContratoSeleccionado;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoTipoContratoD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoTipoContratoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreTipoContratoD");
            RequestContext.getCurrentInstance().execute("PF('editarNombreTipoContratoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDuracionPTipoContratoD");
            RequestContext.getCurrentInstance().execute("PF('editarDuracionPTipoContratoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarVinculacionTipoContratoD");
            RequestContext.getCurrentInstance().execute("PF('editarVinculacionTipoContratoD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void validarNuevoRegistroDias() {

      if (tipoContratoSeleccionado != null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDiaLaboral");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDiaLaboral').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
//        }NuevoRegistroDiaLaboral.show()
   }

   //CREAR 
   public void agregarNuevoTipoContrato() {

      boolean respueta = validarCamposNulosTipoContrato(1);
      if (respueta == true) {
         if (validarRepetidosNuevoyDuplicado(1)) {
            int tamDes = nuevoTipoContrato.getNombre().length();
            if (tamDes >= 1 && tamDes <= 30) {
               if (bandera == 1) {
                  altoTablaTiposC = "190";
                  tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
                  tipoCCodigo.setFilterStyle("display: none; visibility: hidden;");
                  tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
                  tipoCNombre.setFilterStyle("display: none; visibility: hidden;");
                  tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
                  tipoCPeriodo.setFilterStyle("display: none; visibility: hidden;");
                  tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
                  tipoCVE.setFilterStyle("display: none; visibility: hidden;");
                  tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
                  tipoCForza.setFilterStyle("display: none; visibility: hidden;");
                  RequestContext.getCurrentInstance().update("form:datosTipoContrato");
                  bandera = 0;
                  filtrarListaTiposContratos = null;
                  tipoLista = 0;
               }

               k++;
               newSecuencia = BigInteger.valueOf(k);
               String text = nuevoTipoContrato.getNombre().toUpperCase();
               nuevoTipoContrato.setNombre(text);
               nuevoTipoContrato.setSecuencia(newSecuencia);
               listTiposContratosCrear.add(nuevoTipoContrato);
               listaTiposContratos.add(nuevoTipoContrato);
               tipoContratoSeleccionado = listaTiposContratos.get(listaTiposContratos.indexOf(nuevoTipoContrato));
               contarRegistrosTipoC();

               nuevoTipoContrato = new TiposContratos();
               cambiosPagina = false;

               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:datosTipoContrato");
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoContrato').hide()");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            } else {

               RequestContext.getCurrentInstance().execute("PF('errorNombreTipoContrato').show()");
            }
         } else {

            RequestContext.getCurrentInstance().execute("PF('errorRepetidosTipoContrato').show()");
         }
      } else {

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoContrato').show()");
      }
   }

   public void agregarNuevoDiaLaborable() {
      boolean respueta = validarCamposNulosDiaLaborable(1);
      if (respueta == true) {
         if (banderaDiasLab == 1) {
            altoTablaDiasLab = "75";
            diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
            diasLabDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
            diasLabTipoDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
            diasLabHL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
            banderaDiasLab = 0;
            filtrarListaDiasLaborables = null;
            tipoListaDias = 0;
         }
         k++;
         newSecuencia = BigInteger.valueOf(k);
         nuevoDiaLaborable.setSecuencia(newSecuencia);
         nuevoDiaLaborable.setTipocontrato(tipoContratoSeleccionado);

         if (listaDiasLaborables.size() == 0) {
            listaDiasLaborables = new ArrayList<DiasLaborables>();
         }
         listDiasLaborablesCrear.add(nuevoDiaLaborable);
         listaDiasLaborables.add(nuevoDiaLaborable);
         diaLaborableSeleccionado = listaDiasLaborables.get(listaDiasLaborables.indexOf(nuevoDiaLaborable));
         contarRegistrosTipoD();

         cambiosPagina = false;

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDiaLaboral').hide()");
         nuevoDiaLaborable = new DiasLaborables();
         nuevoDiaLaborable.setTipodia(new TiposDias());
         if (guardadoDias == true) {
            guardadoDias = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullDiasLab').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    */
   public void limpiarNuevaTipoContrato() {
      nuevoTipoContrato = new TiposContratos();
   }

   public void limpiarNuevaDiaLaborable() {
      nuevoDiaLaborable = new DiasLaborables();
      nuevoDiaLaborable.setTipodia(new TiposDias());
      nuevoDiaLaborable.setHoraslaborables(BigInteger.valueOf(8));
   }

   //DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      if (diaLaborableSeleccionado != null) {
         duplicarDiaLaborableM();
      } else if (tipoContratoSeleccionado != null) {
         duplicarTipoContratoM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarTipoContratoM() {
      duplicarTipoContrato = new TiposContratos();
      k++;
      newSecuencia = BigInteger.valueOf(k);
      duplicarTipoContrato.setSecuencia(newSecuencia);
      duplicarTipoContrato.setCodigo(tipoContratoSeleccionado.getCodigo());
      duplicarTipoContrato.setNombre(tipoContratoSeleccionado.getNombre());
      duplicarTipoContrato.setDuracionperiodoprueba(tipoContratoSeleccionado.getDuracionperiodoprueba());
      duplicarTipoContrato.setVinculacionempresa(tipoContratoSeleccionado.getVinculacionempresa());
      duplicarTipoContrato.setForzacotizacionpila30dias(tipoContratoSeleccionado.getForzacotizacionpila30dias());

      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoContrato");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoContrato').show()");
   }

   public void duplicarDiaLaborableM() {
      duplicarDiaLaborable = new DiasLaborables();

      duplicarDiaLaborable.setDia(diaLaborableSeleccionado.getDia());
      duplicarDiaLaborable.setHoraslaborables(diaLaborableSeleccionado.getHoraslaborables());
      duplicarDiaLaborable.setTipodia(diaLaborableSeleccionado.getTipodia());

      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDiaLaborable");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDiaLaborable').show()");
   }

   public boolean validarRepetidosNuevoyDuplicado(int tipoNuevo) {
      boolean retorno = true;
      switch (tipoNuevo) {
         case 1: {
            if (listaTiposContratos != null) {
               for (int i = 0; i < listaTiposContratos.size(); i++) {
                  if (nuevoTipoContrato.getCodigo() == listaTiposContratos.get(i).getCodigo()) {
                     retorno = false;
                  }
                  if (nuevoTipoContrato.getNombre().equals(listaTiposContratos.get(i).getNombre())) {
                     retorno = false;
                  }
               }
            }
         }
         break;
         case 2: {
            for (int i = 0; i < listaTiposContratos.size(); i++) {
               if (duplicarTipoContrato.getCodigo() == listaTiposContratos.get(i).getCodigo()) {
                  retorno = false;
               }
               if (duplicarTipoContrato.getNombre().equals(listaTiposContratos.get(i).getNombre())) {
                  retorno = false;
               }
            }
         }
         break;
      }
      return retorno;
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarTipoContrato() {
      boolean respueta = validarCamposNulosTipoContrato(2);
      if (respueta == true) {
         if (validarRepetidosNuevoyDuplicado(2)) {
            int tamDes = duplicarTipoContrato.getNombre().length();
            if (tamDes >= 1 && tamDes <= 30) {
               k++;
               newSecuencia = BigInteger.valueOf(k);
               duplicarDiaLaborable.setSecuencia(newSecuencia);
               String text = duplicarTipoContrato.getNombre().toUpperCase();
               duplicarTipoContrato.setNombre(text);
               listaTiposContratos.add(duplicarTipoContrato);
               listTiposContratosCrear.add(duplicarTipoContrato);
               tipoContratoSeleccionado = listaTiposContratos.get(listaTiposContratos.indexOf(duplicarTipoContrato));
               contarRegistrosTipoC();
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:datosTipoContrato");
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoContrato').hide()");
               if (guardado == true) {
                  guardado = false;
                  //RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               if (bandera == 1) {
                  altoTablaTiposC = "190";
                  tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
                  tipoCCodigo.setFilterStyle("display: none; visibility: hidden;");
                  tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
                  tipoCNombre.setFilterStyle("display: none; visibility: hidden;");
                  tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
                  tipoCPeriodo.setFilterStyle("display: none; visibility: hidden;");
                  tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
                  tipoCVE.setFilterStyle("display: none; visibility: hidden;");
                  tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
                  tipoCForza.setFilterStyle("display: none; visibility: hidden;");
                  RequestContext.getCurrentInstance().update("form:datosTipoContrato");
                  bandera = 0;
                  filtrarListaTiposContratos = null;
                  tipoLista = 0;
               }
               duplicarTipoContrato = new TiposContratos();
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorNombreTipoContrato').show()");
            }
         } else {

            RequestContext.getCurrentInstance().execute("PF('errorRepetidosTipoContrato').show()");
         }
      } else {

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoContrato').show()");
      }
   }

   public void confirmarDuplicarDiaLaborable() {
      boolean respueta = validarCamposNulosDiaLaborable(2);
      if (respueta == true) {
         duplicarDiaLaborable.setTipocontrato(tipoContratoSeleccionado);

         listaDiasLaborables.add(duplicarDiaLaborable);
         listDiasLaborablesCrear.add(duplicarDiaLaborable);
         diaLaborableSeleccionado = listaDiasLaborables.get(listaDiasLaborables.indexOf(duplicarDiaLaborable));
         contarRegistrosTipoD();

         cambiosPagina = false;

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDiaLaborable').hide()");

         if (guardadoDias == true) {
            guardadoDias = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (banderaDiasLab == 1) {
            altoTablaDiasLab = "75";
            diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
            diasLabDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
            diasLabTipoDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
            diasLabHL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
            banderaDiasLab = 0;
            filtrarListaDiasLaborables = null;
            tipoListaDias = 0;
         }
         duplicarDiaLaborable = new DiasLaborables();
         duplicarDiaLaborable.setTipodia(new TiposDias());
      } else {

         RequestContext.getCurrentInstance().execute("PF('errorDatosNullDiasLab').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
   public void limpiarDuplicarTipoContrato() {
      duplicarTipoContrato = new TiposContratos();
   }

   public void limpiarDuplicarDiaLaborable() {
      duplicarDiaLaborable = new DiasLaborables();
      duplicarDiaLaborable.setTipodia(new TiposDias());
      duplicarDiaLaborable.setHoraslaborables(BigInteger.valueOf(8));
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   /**
    */
   public void verificarRegistroBorrar() {
      if (diaLaborableSeleccionado != null) {
         borrarDiasLaborables();
      } else if (tipoContratoSeleccionado != null) {
         if (listaDiasLaborables.isEmpty()) {
            borrarTipoContrato();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarTipoContrato() {
      if (!listTiposContratosModificar.isEmpty() && listTiposContratosModificar.contains(tipoContratoSeleccionado)) {
         listTiposContratosModificar.remove(tipoContratoSeleccionado);
         listTiposContratosBorrar.add(tipoContratoSeleccionado);
      } else if (!listTiposContratosCrear.isEmpty() && listTiposContratosCrear.contains(tipoContratoSeleccionado)) {
         listTiposContratosCrear.remove(tipoContratoSeleccionado);
      } else {
         listTiposContratosBorrar.add(tipoContratoSeleccionado);
      }
      listaTiposContratos.remove(tipoContratoSeleccionado);
      if (tipoLista == 1) {
         filtrarListaTiposContratos.remove(tipoContratoSeleccionado);
      }
      contarRegistrosTipoC();

      tipoContratoSeleccionado = null;
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTipoContrato");

      if (guardado == true) {
         guardado = false;
         //RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrarDiasLaborables() {
      if (!listDiasLaborablesModificar.isEmpty() && listDiasLaborablesModificar.contains(diaLaborableSeleccionado)) {
         listDiasLaborablesModificar.remove(diaLaborableSeleccionado);
         listDiasLaborablesBorrar.add(diaLaborableSeleccionado);
      } else if (!listDiasLaborablesCrear.isEmpty() && listDiasLaborablesCrear.contains(diaLaborableSeleccionado)) {
         listDiasLaborablesCrear.remove(diaLaborableSeleccionado);
      } else {
         listDiasLaborablesBorrar.add(diaLaborableSeleccionado);
      }
      listaDiasLaborables.remove(diaLaborableSeleccionado);
      if (tipoListaDias == 1) {
         filtrarListaDiasLaborables.remove(diaLaborableSeleccionado);
      }
      contarRegistrosTipoD();

      diaLaborableSeleccionado = null;
      cambiosPagina = false;

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDiasLaborables");

      if (guardadoDias == true) {
         guardadoDias = false;
         //RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (diaLaborableSeleccionado != null) {
         if (banderaDiasLab == 0) {
            altoTablaDiasLab = "55";
            diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
            diasLabDia.setFilterStyle("width: 85% !important");
            diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
            diasLabTipoDia.setFilterStyle("width: 85% !important");
            diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
            diasLabHL.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
            banderaDiasLab = 1;
         } else if (banderaDiasLab == 1) {
            altoTablaDiasLab = "75";
            diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
            diasLabDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
            diasLabTipoDia.setFilterStyle("display: none; visibility: hidden;");
            diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
            diasLabHL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
            banderaDiasLab = 0;
            filtrarListaDiasLaborables = null;
            tipoListaDias = 0;
         }
      } else if (tipoContratoSeleccionado != null) {
         if (bandera == 0) {
            altoTablaTiposC = "166";
            tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
            tipoCCodigo.setFilterStyle("width: 85% !important");
            tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
            tipoCNombre.setFilterStyle("width: 85% !important");
            tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
            tipoCPeriodo.setFilterStyle("width: 85% !important");
            tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
            tipoCVE.setFilterStyle("width: 85% !important");
//                tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
//                tipoCForza.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosTipoContrato");
            bandera = 1;
         } else if (bandera == 1) {
            altoTablaTiposC = "190";
            tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
            tipoCCodigo.setFilterStyle("display: none; visibility: hidden;");
            tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
            tipoCNombre.setFilterStyle("display: none; visibility: hidden;");
            tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
            tipoCPeriodo.setFilterStyle("display: none; visibility: hidden;");
            tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
            tipoCVE.setFilterStyle("display: none; visibility: hidden;");
//                tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
//                tipoCForza.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoContrato");
            bandera = 0;
            filtrarListaTiposContratos = null;
            tipoLista = 0;
         }
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         altoTablaTiposC = "190";
         tipoCCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCCodigo");
         tipoCCodigo.setFilterStyle("display: none; visibility: hidden;");
         tipoCNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCNombre");
         tipoCNombre.setFilterStyle("display: none; visibility: hidden;");
         tipoCPeriodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCPeriodo");
         tipoCPeriodo.setFilterStyle("display: none; visibility: hidden;");
         tipoCVE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCVE");
         tipoCVE.setFilterStyle("display: none; visibility: hidden;");
         tipoCForza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoContrato:tipoCForza");
         tipoCForza.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         bandera = 0;
         filtrarListaTiposContratos = null;
         tipoLista = 0;
      }
      if (banderaDiasLab == 1) {
         altoTablaDiasLab = "75";
         diasLabDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabDia");
         diasLabDia.setFilterStyle("display: none; visibility: hidden;");
         diasLabTipoDia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabTipoDia");
         diasLabTipoDia.setFilterStyle("display: none; visibility: hidden;");
         diasLabHL = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDiasLaborables:diasLabHL");
         diasLabHL.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
         banderaDiasLab = 0;
         filtrarListaDiasLaborables = null;
         tipoListaDias = 0;
      }
      listTiposContratosBorrar.clear();
      listTiposContratosCrear.clear();
      listTiposContratosModificar.clear();
      listDiasLaborablesBorrar.clear();
      listDiasLaborablesCrear.clear();
      listDiasLaborablesModificar.clear();
      diaLaborableSeleccionado = null;
      tipoContratoSeleccionado = null;
      k = 0;
      listaTiposContratos = null;
      listaDiasLaborables = null;
      guardado = true;
      guardadoDias = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void listaValoresBoton() {
      if (diaLaborableSeleccionado != null) {
         if (cualCeldaDias == 0) {
            contarRegistrosTipoDLOV();
            RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(DiasLaborables diasLaborables, int column, int LND) {

      diaLaborableSeleccionado = diasLaborables;
      tipoActualizacion = LND;

      if (column == 0) {
         contarRegistrosTipoDLOV();
         RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
      }
   }

   public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
      if (Campo.equals("DIAS")) {
         if (tipoNuevo == 1) {
            tipoDia = nuevoDiaLaborable.getTipodia().getDescripcion();
         } else if (tipoNuevo == 2) {
            tipoDia = duplicarDiaLaborable.getTipodia().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoDiaLaborable(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (confirmarCambio.equalsIgnoreCase("DIAS")) {
         if (tipoNuevo == 1) {
            nuevoDiaLaborable.getTipodia().setDescripcion(tipoDia);
         } else if (tipoNuevo == 2) {
            duplicarDiaLaborable.getTipodia().setDescripcion(tipoDia);
         }
         for (int i = 0; i < lovTiposDias.size(); i++) {
            if (lovTiposDias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoDiaLaborable.setTipodia(lovTiposDias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDiaLabTipoDia");
            } else if (tipoNuevo == 2) {
               duplicarDiaLaborable.setTipodia(lovTiposDias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiaLabTipoDia");
            }
            lovTiposDias.clear();
            getLovTiposDias();
         } else {
            RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDiaLabTipoDia");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiaLabTipoDia");
            }
         }
      }
   }

   public void actualizarTipoDia() {

      if (tipoActualizacion == 0) {
         diaLaborableSeleccionado.setTipodia(tipoDiaLOVSeleccionado);
         if (!listDiasLaborablesCrear.contains(diaLaborableSeleccionado)) {
            if (listDiasLaborablesModificar.isEmpty()) {
               listDiasLaborablesModificar.add(diaLaborableSeleccionado);
            } else if (!listDiasLaborablesModificar.contains(diaLaborableSeleccionado)) {
               listDiasLaborablesModificar.add(diaLaborableSeleccionado);
            }
         }

         if (guardadoDias == true) {
            guardadoDias = false;
         }
         permitirIndexDias = true;
         cambiosPagina = false;

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
      } else if (tipoActualizacion == 1) {
         nuevoDiaLaborable.setTipodia(tipoDiaLOVSeleccionado);

         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDiaLabTipoDia");
      } else if (tipoActualizacion == 2) {
         duplicarDiaLaborable.setTipodia(tipoDiaLOVSeleccionado);

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiaLabTipoDia");
      }
      filtrarLovTiposDias = null;
      aceptar = true;
      tipoActualizacion = -1;
      tipoDiaLOVSeleccionado = null;

      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoDia:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoDia').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoDia");
      RequestContext.getCurrentInstance().update("form:aceptarTD");
   }

   public void cancelarCambioTipoDia() {
      filtrarLovTiposDias = null;
      aceptar = true;
      tipoActualizacion = -1;
      tipoDiaLOVSeleccionado = null;
      permitirIndexDias = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoDia:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoDia').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoDia");
      RequestContext.getCurrentInstance().update("form:aceptarTD");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      if (diaLaborableSeleccionado != null) {
         nombreTabla = ":formExportarDL:datosDiasLaborablesExportar";
         nombreXML = "DiasLaborables_XML";
      } else if (tipoContratoSeleccionado != null) {
         nombreTabla = ":formExportarTC:datosTipoContratoExportar";
         nombreXML = "TiposContratos_XML";
      }
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (diaLaborableSeleccionado != null) {
         exportPDF_DL();
      } else if (tipoContratoSeleccionado != null) {
         exportPDF_TC();
      }
   }

   public void exportPDF_TC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTC:datosTipoContratoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposContratos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_DL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDL:datosDiasLaborablesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "DiasLaborables_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      if (tipoContratoSeleccionado != null) {
         exportXLS_TC();
      }
      if (diaLaborableSeleccionado != null) {
         exportXLS_DL();
      }
   }

   public void exportXLS_TC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTC:datosTipoContratoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposContratos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_DL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDL:datosDiasLaborablesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DiasLaborables_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      if (diaLaborableSeleccionado == null && tipoContratoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablasH').show()");
      } else if (diaLaborableSeleccionado != null) {
         verificarRastroDiaLaborable();
      } else if (tipoContratoSeleccionado != null) {
         verificarRastroTipoContrato();
      }
   }

   public void verificarRastroTipoContrato() {

      int resultado = administrarRastros.obtenerTabla(tipoContratoSeleccionado.getSecuencia(), "TIPOSCONTRATOS");
      backUp = tipoContratoSeleccionado.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "TiposContratos";
         msnConfirmarRastro = "La tabla TIPOSCONTRATOS tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroTipoContratoH() {

      if (administrarRastros.verificarHistoricosTabla("TIPOSCONTRATOS")) {
         nombreTablaRastro = "TiposContratos";
         msnConfirmarRastroHistorico = "La tabla TIPOSCONTRATOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroDiaLaborable() {

      int resultado = administrarRastros.obtenerTabla(diaLaborableSeleccionado.getSecuencia(), "DIASLABORABLES");
      backUp = diaLaborableSeleccionado.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "DiasLaborables";
         msnConfirmarRastro = "La tabla DIASLABORABLES tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroDiaLaborableH() {

      if (administrarRastros.verificarHistoricosTabla("DIASLABORABLES")) {
         nombreTablaRastro = "DiasLaborables";
         msnConfirmarRastroHistorico = "La tabla DIASLABORABLES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public boolean validarCodigoNuevoClonado() {
      boolean retorno = true;
      int conteo = 0;
      for (int i = 0; i < lovTiposContratos.size(); i++) {
         if (lovTiposContratos.get(i).getCodigo() == codigoTipoCClonar) {
            conteo++;
         }
      }
      if (conteo > 0) {
         retorno = false;
      }
      return retorno;
   }

   public void verificarclonarTipoContrato() {
      if ((nombreTipoCClonar.isEmpty()) || (codigoTipoCClonar <= 0) || (tipoContratoAClonar.getSecuencia() == null)) {

         RequestContext.getCurrentInstance().update("form:errorClonadoTipoC");
         RequestContext.getCurrentInstance().execute("PF('errorClonadoTipoC').show()");
      } else if (validarCodigoNuevoClonado() == true) {

         RequestContext.getCurrentInstance().update("form:continuarOperacionClonado");
         RequestContext.getCurrentInstance().execute("PF('continuarOperacionClonado').show()");
      } else {

         RequestContext.getCurrentInstance().update("form:errorCodigoClonado");
         RequestContext.getCurrentInstance().execute("PF('errorCodigoClonado').show()");
      }
   }

   public void dispararDialogoClonarTipoContrato() {
      contarRegistrosTipoCLOV();
      RequestContext.getCurrentInstance().update("form:TipoContratoDialogo");
      RequestContext.getCurrentInstance().execute("PF('TipoContratoDialogo').show()");
   }

   public void posicionTipoContratoClonar() {
      if (guardado == true) {
         habilitarBotonLov();
         auxCodigoClonar = tipoContratoAClonar.getCodigo();
         auxNombreClonar = tipoContratoAClonar.getNombre();
      } else {

         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void clonarTipoContrato() {
      if (nombreTipoCClonar.length() >= 1 && nombreTipoCClonar.length() <= 30) {
         String newNombreTC = nombreTipoCClonar.toUpperCase();

         administrarTipoContrato.clonarTC(tipoContratoAClonar.getSecuencia(), newNombreTC, codigoTipoCClonar);
         listaTiposContratos = null;
         listaDiasLaborables = null;
         getListaTiposContratos();
         contarRegistrosTipoC();
         contarRegistrosTipoD();
         tipoContratoSeleccionado = null;
         tipoContratoAClonar = new TiposContratos();
         nombreTipoCClonar = "";
         codigoTipoCClonar = 0;

         RequestContext.getCurrentInstance().update("form:codigoTipoCClonar");
         RequestContext.getCurrentInstance().update("form:nombreTipoCClonar");
         RequestContext.getCurrentInstance().update("form:codigoTipoCClonarBase");
         RequestContext.getCurrentInstance().update("form:nombreTipoCClonarBase");

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoContrato");
         RequestContext.getCurrentInstance().update("form:datosDiasLaborables");
      } else {

         RequestContext.getCurrentInstance().execute("PF('errorNombreTipoContrato').show()");
      }
   }

   public void seleccionarTipoContratoClonar() {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoContratoAClonar = tipoContratoLOVSeleccionado;
      lovTiposContratos.clear();
      getLovTiposContratos();
      RequestContext.getCurrentInstance().update("form:codigoTipoCClonarBase");
      RequestContext.getCurrentInstance().update("form:nombreTipoCClonarBase");
      tipoContratoLOVSeleccionado = null;
      filtrarLovTiposContratos = null;

      context.reset("form:lovTipoContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoContratoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:TipoContratoDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoContrato");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }

   public void cancelarTipoContratoClonar() {
      tipoContratoLOVSeleccionado = null;
      filtrarLovTiposContratos = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoContratoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:TipoContratoDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoContrato");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }

   public void autoCompletarSeleccionarTipoContratoClonar(String valorConfirmar, int tipoAutoCompletar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoAutoCompletar == 0) {
         short num = Short.parseShort(valorConfirmar);
         for (int i = 0; i < lovTiposContratos.size(); i++) {
            if (lovTiposContratos.get(i).getCodigo() == num) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoContratoAClonar = lovTiposContratos.get(indiceUnicoElemento);
            lovTiposContratos.clear();
            getLovTiposContratos();
         } else {
            tipoContratoAClonar.setCodigo(auxCodigoClonar);
            tipoContratoAClonar.setNombre(auxNombreClonar);
            contarRegistrosTipoCLOV();
            RequestContext.getCurrentInstance().update("form:codigoTipoCClonarBase");
            RequestContext.getCurrentInstance().update("form:nombreTipoCClonarBase");
            RequestContext.getCurrentInstance().update("form:TipoContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoContratoDialogo').show()");
         }
      }
      if (tipoAutoCompletar == 1) {
         for (int i = 0; i < lovTiposContratos.size(); i++) {
            if (lovTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoContratoAClonar = lovTiposContratos.get(indiceUnicoElemento);
            lovTiposContratos.clear();
            getLovTiposContratos();
         } else {
            tipoContratoAClonar.setCodigo(auxCodigoClonar);
            tipoContratoAClonar.setNombre(auxNombreClonar);
            contarRegistrosTipoCLOV();
            RequestContext.getCurrentInstance().update("form:nombreTipoCClonarBase");
            RequestContext.getCurrentInstance().update("form:codigoTipoCClonarBase");
            RequestContext.getCurrentInstance().update("form:TipoContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoContratoDialogo').show()");
         }
      }
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrarTC() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      tipoContratoSeleccionado = null;
      contarRegistrosTipoC();
      listaDiasLaborables = null;
   }

   public void eventoFiltrarTD() {
      if (tipoListaDias == 0) {
         tipoListaDias = 1;
      }
      diaLaborableSeleccionado = null;
      contarRegistrosTipoD();
   }

   public void contarRegistrosTipoC() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroTC");
   }

   public void contarRegistrosTipoD() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroDias");
   }

   public void contarRegistrosTipoCLOV() {
      RequestContext.getCurrentInstance().update("form:infoRegistoTipoContratoLov");
   }

   public void contarRegistrosTipoDLOV() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoDiaLov");
   }

   //GETTERS AND SETTERS
   public List<TiposContratos> getListaTiposContratos() {
      try {
         if (listaTiposContratos == null) {
            listaTiposContratos = administrarTipoContrato.listaTiposContratos();
            return listaTiposContratos;
         } else {
            return listaTiposContratos;
         }
      } catch (Exception e) {
         System.out.println("Error...!! getListaTiposContratos " + e.toString());
         return null;
      }
   }

   public void setListaTiposContratos(List<TiposContratos> setListaTiposContratos) {
      this.listaTiposContratos = setListaTiposContratos;
   }

   public List<TiposContratos> getFiltrarListaTiposContratos() {
      return filtrarListaTiposContratos;
   }

   public void setFiltrarListaTiposContratos(List<TiposContratos> setFiltrarListaTiposContratos) {
      this.filtrarListaTiposContratos = setFiltrarListaTiposContratos;
   }

   public TiposContratos getNuevoTipoContrato() {
      return nuevoTipoContrato;
   }

   public void setNuevoTipoContrato(TiposContratos setNuevoTipoContrato) {
      this.nuevoTipoContrato = setNuevoTipoContrato;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public TiposContratos getEditarTipoContrato() {
      return editarTipoContrato;
   }

   public void setEditarTipoContrato(TiposContratos setEditarTipoContrato) {
      this.editarTipoContrato = setEditarTipoContrato;
   }

   public TiposContratos getDuplicarTipoContrato() {
      return duplicarTipoContrato;
   }

   public void setDuplicarTipoContrato(TiposContratos setDuplicarTipoContrato) {
      this.duplicarTipoContrato = setDuplicarTipoContrato;
   }

   public List<DiasLaborables> getListaDiasLaborables() {
      if (listaDiasLaborables == null) {
         if (tipoContratoSeleccionado != null) {
            listaDiasLaborables = administrarTipoContrato.listaDiasLaborablesParaTipoContrato(tipoContratoSeleccionado.getSecuencia());
         }
      }
      return listaDiasLaborables;
   }

   public void setListaDiasLaborables(List<DiasLaborables> setListaDiasLaborables) {
      this.listaDiasLaborables = setListaDiasLaborables;
   }

   public List<DiasLaborables> getFiltrarListaDiasLaborables() {
      return filtrarListaDiasLaborables;
   }

   public void setFiltrarListaDiasLaborables(List<DiasLaborables> setFiltrarListaDiasLaborables) {
      this.filtrarListaDiasLaborables = setFiltrarListaDiasLaborables;
   }

   public List<TiposContratos> getListTiposContratosModificar() {
      return listTiposContratosModificar;
   }

   public void setListTiposContratosModificar(List<TiposContratos> setListTiposContratosModificar) {
      this.listTiposContratosModificar = setListTiposContratosModificar;
   }

   public List<TiposContratos> getListTiposContratosCrear() {
      return listTiposContratosCrear;
   }

   public void setListTiposContratosCrear(List<TiposContratos> setListExtraRecargoCrear) {
      this.listTiposContratosCrear = setListExtraRecargoCrear;
   }

   public List<TiposContratos> getListTiposContratosBorrar() {
      return listTiposContratosBorrar;
   }

   public void setListTiposContratosBorrar(List<TiposContratos> setListTiposContratosBorrar) {
      this.listTiposContratosBorrar = setListTiposContratosBorrar;
   }

   public List<DiasLaborables> getListDiasLaborablesModificar() {
      return listDiasLaborablesModificar;
   }

   public void setListDiasLaborablesModificar(List<DiasLaborables> setListDiasLaborablesModificar) {
      this.listDiasLaborablesModificar = setListDiasLaborablesModificar;
   }

   public DiasLaborables getNuevoDiaLaborable() {
      return nuevoDiaLaborable;
   }

   public void setNuevoDiaLaborable(DiasLaborables setNuevoDiaLaborable) {
      this.nuevoDiaLaborable = setNuevoDiaLaborable;
   }

   public List<DiasLaborables> getListDiasLaborablesCrear() {
      return listDiasLaborablesCrear;
   }

   public void setListDiasLaborablesCrear(List<DiasLaborables> setListDiasLaborablesCrear) {
      this.listDiasLaborablesCrear = setListDiasLaborablesCrear;
   }

   public List<DiasLaborables> getListDiasLaborablesBorrar() {
      return listDiasLaborablesBorrar;
   }

   public void setListDiasLaborablesBorrar(List<DiasLaborables> setListDiasLaborablesBorrar) {
      this.listDiasLaborablesBorrar = setListDiasLaborablesBorrar;
   }

   public DiasLaborables getEditarDiaLaborable() {
      return editarDiaLaborable;
   }

   public void setEditarDiaLaborable(DiasLaborables setEditarDiaLaborable) {
      this.editarDiaLaborable = setEditarDiaLaborable;
   }

   public DiasLaborables getDuplicarDiaLaborable() {
      return duplicarDiaLaborable;
   }

   public void setDuplicarDiaLaborable(DiasLaborables setDuplicarDiaLaborable) {
      this.duplicarDiaLaborable = setDuplicarDiaLaborable;
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

   public List<TiposDias> getLovTiposDias() {
      lovTiposDias = administrarTipoContrato.lovTiposDias();

      return lovTiposDias;
   }

   public void setLovTiposDias(List<TiposDias> setLovTiposDias) {
      this.lovTiposDias = setLovTiposDias;
   }

   public List<TiposDias> getFiltrarLovTiposDias() {
      return filtrarLovTiposDias;
   }

   public void setFiltrarLovTiposDias(List<TiposDias> setFiltrarLovTiposDias) {
      this.filtrarLovTiposDias = setFiltrarLovTiposDias;
   }

   public TiposDias getTipoDiaLOVSeleccionado() {
      return tipoDiaLOVSeleccionado;
   }

   public void setTipoDiaLOVSeleccionado(TiposDias setTipoDiaSeleccionado) {
      this.tipoDiaLOVSeleccionado = setTipoDiaSeleccionado;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTablaTiposC() {
      return altoTablaTiposC;
   }

   public void setAltoTablaTiposC(String altoTablaTiposC) {
      this.altoTablaTiposC = altoTablaTiposC;
   }

   public String getAltoTablaDiasLab() {
      return altoTablaDiasLab;
   }

   public void setAltoTablaDiasLab(String altoTablaDiasLab) {
      this.altoTablaDiasLab = altoTablaDiasLab;
   }

   public short getCodigoTipoCClonar() {
      return codigoTipoCClonar;
   }

   public void setCodigoTipoCClonar(short codigoTipoCClonar) {
      this.codigoTipoCClonar = codigoTipoCClonar;
   }

   public String getNombreTipoCClonar() {
      return nombreTipoCClonar;
   }

   public void setNombreTipoCClonar(String nombreTipoCColnar) {
      this.nombreTipoCClonar = nombreTipoCColnar.toUpperCase();
   }

   public List<TiposContratos> getLovTiposContratos() {
      lovTiposContratos = administrarTipoContrato.listaTiposContratos();
      return lovTiposContratos;
   }

   public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
      this.lovTiposContratos = lovTiposContratos;
   }

   public List<TiposContratos> getFiltrarLovTiposContratos() {
      return filtrarLovTiposContratos;
   }

   public void setFiltrarLovTiposContratos(List<TiposContratos> filtrarLovTiposContratos) {
      this.filtrarLovTiposContratos = filtrarLovTiposContratos;
   }

   public TiposContratos getTipoContratoLOVSeleccionado() {
      return tipoContratoLOVSeleccionado;
   }

   public void setTipoContratoLOVSeleccionado(TiposContratos tipoContratoSeleccionado) {
      this.tipoContratoLOVSeleccionado = tipoContratoSeleccionado;
   }

   public TiposContratos getTipoContratoAClonar() {
      return tipoContratoAClonar;
   }

   public void setTipoContratoAClonar(TiposContratos tipoContratoAClonar) {
      this.tipoContratoAClonar = tipoContratoAClonar;
   }

   public TiposContratos getTipoContratoSeleccionado() {
      return tipoContratoSeleccionado;
   }

   public void setTipoContratoSeleccionado(TiposContratos tipoContratoTablaSeleccionado) {
      this.tipoContratoSeleccionado = tipoContratoTablaSeleccionado;
   }

   public DiasLaborables getDiaLaborableSeleccionado() {
      return diaLaborableSeleccionado;
   }

   public void setDiaLaborableSeleccionado(DiasLaborables diaLaborableTablaSeleccionado) {
      this.diaLaborableSeleccionado = diaLaborableTablaSeleccionado;
   }

   public String getInfoRegistroTipoContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoContrato");
      infoRegistroTipoContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoContrato;
   }

   public String getInfoRegistroTipoContratoLOV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoContrato");
      infoRegistroTipoContratoLOV = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoContratoLOV;
   }

   public String getInfoRegistroTipoDia() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDiasLaborables");
      infoRegistroTipoDia = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoDia;
   }

   public String getInfoRegistroTipoDiaLOV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoDia");
      infoRegistroTipoDiaLOV = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoDiaLOV;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
