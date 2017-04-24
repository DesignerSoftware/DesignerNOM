/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.OtrosCertificados;
import Entidades.TiposCertificados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplMvrsInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.acl.Group;
import java.util.*;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplMvr implements Serializable {

   @EJB
   AdministrarEmplMvrsInterface administrarEmplMvrs;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<Mvrs> listMvrsEmpleado;
   private List<Mvrs> filtrarListMvrsEmpleado;
   private Mvrs mvrSeleccionado;
   private List<OtrosCertificados> listOCertificados;
   private List<OtrosCertificados> filtrarListOtrosCertificados;
   private OtrosCertificados otroCertificadoSeleccionado;
   private List<Motivosmvrs> listMotivosMvrs;
   private Motivosmvrs motivoMvrSeleccionado;
   private List<Motivosmvrs> filtrarListMotivosMvrs;
   //
   private List<TiposCertificados> listTiposCertificados;
   private TiposCertificados tipoCertificadoSeleccionado;
   private List<TiposCertificados> filtrarListTiposCertificados;
   //Empleado
   private Empleados empleado;
   //Tipo Actualizacion
   private int tipoActualizacion;
   //Activo/Desactivo Crtl + F11
   private int banderaMvrs;
   //Activo/Desactivo VP Crtl + F11
   private int banderaOC;
   //Columnas Tabla VL
   private Column mvrValorAnual, mvrMotivo, mvrValor, mvrFechaFinal, mvrFechaInicial;
   //Columnas Tabla VP
   private Column ocDias, ocCertificado, ocEstado, ocValor, ocFechaFinal, ocFechaInicial;
   //Otros
   private boolean aceptar;
   //private int indexMvrs;
   //modificar
   private List<Mvrs> listMvrsModificar;
   private List<OtrosCertificados> listOtrosCertificadosModificar;
   private boolean guardado;
   //private boolean guardarOk;
   //crear VL
   public Mvrs nuevaMvrs;
   //crear VP
   public OtrosCertificados nuevaOtroCertificado;
   private List<Mvrs> listMvrsCrear;
   private BigInteger nuevaMVRSecuencia;
   private int paraNuevaMenValRet;
   //borrar VL
   private List<Mvrs> listMvrsBorrar;
   private List<OtrosCertificados> listOtrosCertificadosBorrar;
   //editar celda
   private Mvrs editarMvrs;
   private OtrosCertificados editarOtrosCertificados;
   private int cualCeldaMvrs, tipoListaMvrs;
   //duplicar
   private Mvrs duplicarMvrs;
   //Autocompletar
   private boolean permitirIndexMvrs, permitirIndexOtrosCertificados;
   //Variables Autompletar
   private String motivoMvrs, tipoCertificado;
   //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
   private int cualCeldaOtrosCertificados, tipoListaOtrosCertificados;
   //Index Auxiliar Para Nuevos Registros
   //Duplicar Vigencia Prorrateo
   private OtrosCertificados duplicarOtrosCertificados;
   private List<OtrosCertificados> listOtrosCertificadosCrear;
   private String nombreXML;
   private String nombreTabla;
   private BigInteger backUp;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private String nombreTablaRastro;
   private Date fechaParametro;
   private Date fechaIni, fechaFin, fechaIniOC, fechaFinOC;
   //ALTO TABLA
   private String altoTabla1, altoTabla2;
   private boolean cambiosMvr, cambiosOtros;
   //
   private String infoRegistroMVR, infoRegistroOtroC;
   private String infoRegistroMotivoMVR, infoRegistroCertificado;
   //
   private DataTable tablaC;
   private boolean activarLOV;
   private String estadoMientras;
   private HashMap<Integer, String> hMapEstados;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplMvr() {
      listMvrsEmpleado = null;
      listOCertificados = null;
      cambiosMvr = false;
      cambiosOtros = false;
      nombreTablaRastro = "";
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      listMotivosMvrs = null;
      empleado = new Empleados();
      listTiposCertificados = null;
      backUp = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listMvrsBorrar = new ArrayList<Mvrs>();
      listOtrosCertificadosBorrar = new ArrayList<OtrosCertificados>();
      //crear aficiones
      listMvrsCrear = new ArrayList<Mvrs>();
      paraNuevaMenValRet = 0;
      //modificar aficiones
      listMvrsModificar = new ArrayList<Mvrs>();
      listOtrosCertificadosModificar = new ArrayList<OtrosCertificados>();
      //editar
      editarMvrs = new Mvrs();
      editarOtrosCertificados = new OtrosCertificados();
      cualCeldaMvrs = -1;
      tipoListaMvrs = 0;
      tipoListaOtrosCertificados = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevaMvrs = new Mvrs();
      nuevaMvrs.setMotivo(new Motivosmvrs());
      banderaMvrs = 0;
      banderaOC = 0;
      permitirIndexMvrs = true;
      permitirIndexOtrosCertificados = true;
      cualCeldaOtrosCertificados = -1;

      nuevaOtroCertificado = new OtrosCertificados();
      nuevaOtroCertificado.setTipocertificado(new TiposCertificados());
      listOtrosCertificadosCrear = new ArrayList<OtrosCertificados>();

      nombreTabla = ":formExportarMVR:datosMvrEmpleadoExportar";
      nombreXML = "M.V.R.XML";

      duplicarMvrs = new Mvrs();
      duplicarOtrosCertificados = new OtrosCertificados();

      altoTabla1 = "115";
      altoTabla2 = "115";

      activarLOV = true;

      estadoMientras = "";
      hMapEstados = new HashMap<Integer, String>();
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         */
String pagActual = "emplmvrs";
         
         
         


         
         
         
         
         
         
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
      listMotivosMvrs = null;
      listTiposCertificados = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmplMvrs.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger empl) {
      empleado = administrarEmplMvrs.empleadoActual(empl);
      getListMvrsEmpleado();
      getListOCertificados();
      int vacio = 0;
      if (listMvrsEmpleado != null) {
         if (!listMvrsEmpleado.isEmpty()) {
            mvrSeleccionado = listMvrsEmpleado.get(0);
         } else {
            vacio++;
         }
      } else {
         vacio++;
      }

      if (vacio > 0) {
         if (listOCertificados != null) {
            if (!listOCertificados.isEmpty()) {
               otroCertificadoSeleccionado = listOCertificados.get(0);
            }
         }
      }
   }

   /**
    * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarMvrs(Mvrs mvrs) {
      mvrSeleccionado = mvrs;
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listMvrsCrear.contains(mvrSeleccionado)) {
         if (listMvrsModificar.isEmpty()) {
            listMvrsModificar.add(mvrSeleccionado);
         } else if (!listMvrsModificar.contains(mvrSeleccionado)) {
            listMvrsModificar.add(mvrSeleccionado);
         }
         cambiosMvr = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
   }

   public boolean validarFechasRegistroMvrs(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {

         if (mvrSeleccionado.getFechafinal() != null) {
            if (mvrSeleccionado.getFechainicial().after(fechaParametro) && mvrSeleccionado.getFechainicial().before(mvrSeleccionado.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (mvrSeleccionado.getFechafinal() == null) {
            if (mvrSeleccionado.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 1) {
         if (nuevaMvrs.getFechafinal() != null) {
            if (nuevaMvrs.getFechainicial().after(fechaParametro) && nuevaMvrs.getFechainicial().before(nuevaMvrs.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (nuevaMvrs.getFechafinal() == null) {
            if (nuevaMvrs.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 2) {
         if (duplicarMvrs.getFechafinal() != null) {
            if (duplicarMvrs.getFechainicial().after(fechaParametro) && duplicarMvrs.getFechainicial().before(duplicarMvrs.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (duplicarMvrs.getFechafinal() == null) {
            if (duplicarMvrs.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroOtroC(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         OtrosCertificados auxiliar = null;
         auxiliar = otroCertificadoSeleccionado;

         if (auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevaOtroCertificado.getFechainicial().before(nuevaOtroCertificado.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }

      }
      if (i == 2) {
         if (duplicarOtrosCertificados.getFechainicial().before(duplicarOtrosCertificados.getFechafinal())) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarFechasMvrs(Mvrs mvrs, int c) {
      mvrSeleccionado = mvrs;

      RequestContext context = RequestContext.getCurrentInstance();
      if (mvrSeleccionado.getFechainicial() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroMvrs(0);
         if (retorno) {
            cambiarIndiceMvr(mvrSeleccionado, c);
            modificarMvrs(mvrSeleccionado);
         } else {
            mvrSeleccionado.setFechafinal(fechaFin);
            mvrSeleccionado.setFechainicial(fechaIni);

            RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
            RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
         }
      } else {
         mvrSeleccionado.setFechainicial(fechaIni);
         RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNewMvr').show()");
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void modificarFechasOtros(OtrosCertificados otroC, int c) {
      otroCertificadoSeleccionado = otroC;

      if (otroCertificadoSeleccionado.getFechainicial() != null && otroCertificadoSeleccionado.getFechafinal() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroOtroC(0);
         if (retorno) {
            cambiarIndiceOtrosCertificados(otroCertificadoSeleccionado, c);
            modificarOtrosCertificados(otroCertificadoSeleccionado);
         } else {
            otroCertificadoSeleccionado.setFechafinal(fechaFinOC);
            otroCertificadoSeleccionado.setFechainicial(fechaIniOC);

            RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
            RequestContext.getCurrentInstance().execute("PF('form:errorFechasOC').show()");
         }
      } else {
         otroCertificadoSeleccionado.setFechafinal(fechaFinOC);
         otroCertificadoSeleccionado.setFechainicial(fechaIniOC);

         RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNewOtro').show()");
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla
    * VigenciasLocalizaciones de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarMvrs(Mvrs mvrs, String confirmarCambio, String valorConfirmar) {
      mvrSeleccionado = mvrs;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
         mvrSeleccionado.getMotivo().setNombre(motivoMvrs);

         for (int i = 0; i < listMotivosMvrs.size(); i++) {
            if (listMotivosMvrs.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            mvrSeleccionado.setMotivo(listMotivosMvrs.get(indiceUnicoElemento));
         } else {
            permitirIndexMvrs = false;
            contarRegistrosMot();
            RequestContext.getCurrentInstance().update("form:MotivoDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
            tipoActualizacion = 0;
         }
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
      }
      if (coincidencias == 1) {
         if (!listMvrsCrear.contains(mvrSeleccionado)) {

            if (listMvrsModificar.isEmpty()) {
               listMvrsModificar.add(mvrSeleccionado);
            } else if (!listMvrsModificar.contains(mvrSeleccionado)) {
               listMvrsModificar.add(mvrSeleccionado);
            }
            cambiosMvr = true;
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateo que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarOtrosCertificados(OtrosCertificados otroC) {
      otroCertificadoSeleccionado = otroC;
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listOtrosCertificadosCrear.contains(otroCertificadoSeleccionado)) {
         if (listOtrosCertificadosModificar.isEmpty()) {
            listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
         } else if (!listOtrosCertificadosModificar.contains(otroCertificadoSeleccionado)) {
            listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
         }
         cambiosOtros = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarEstado() {
      otroCertificadoSeleccionado.setEstado(hMapEstados.get(otroCertificadoSeleccionado.getSecuencia().intValue()));
      System.out.println("otroCertificadoSeleccionado.getEstado(): " + otroCertificadoSeleccionado.getEstado());
      System.out.println("hMapEstados.get(otro.getSecuencia().intValue()): " + hMapEstados.get(otroCertificadoSeleccionado.getSecuencia().intValue()));

      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
   }

   /*
    public String llamarEstado(BigInteger sec) {
        estadoMientras = hMapEstados.get(sec.intValue());
        return estadoMientras;
    }*/
   /**
    * Metodo que modifica los cambios efectuados en la tabla VigenciaProrrateo
    * de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarOtrosCertificados(OtrosCertificados otrosC, String confirmarCambio, String valorConfirmar) {
      otroCertificadoSeleccionado = otrosC;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CERTIFICADO")) {
         otroCertificadoSeleccionado.getTipocertificado().setDescripcion(tipoCertificado);

         for (int i = 0; i < listTiposCertificados.size(); i++) {
            if (listTiposCertificados.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            otroCertificadoSeleccionado.setTipocertificado(listTiposCertificados.get(indiceUnicoElemento));
         } else {
            permitirIndexOtrosCertificados = false;
            contarRegistrosCer();
            RequestContext.getCurrentInstance().update("form:CertificadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('CertificadosDialogo').show()");
            tipoActualizacion = 0;
         }
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
      }
      if (coincidencias == 1) {
         if (!listOtrosCertificadosCrear.contains(otroCertificadoSeleccionado)) {

            if (listOtrosCertificadosModificar.isEmpty()) {
               listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
            } else if (!listOtrosCertificadosModificar.contains(otroCertificadoSeleccionado)) {
               listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
            }
            cambiosOtros = true;
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaLocalizacion)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletarMvr(int tipoNuevo, String Campo) {
      if (Campo.equals("MOTIVO")) {
         if (tipoNuevo == 1) {
            motivoMvrs = nuevaMvrs.getMotivo().getNombre();
         } else if (tipoNuevo == 2) {
            motivoMvrs = duplicarMvrs.getMotivo().getNombre();
         }
      }
   }

   /**
    * Metodo que genera el auto completar de un proceso nuevoRegistro o
    * duplicarRegistro de VigenciasLocalizaciones
    *
    * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
    * MOTIVOLOCALIZACION - PROYECTO
    * @param valorConfirmar Valor ingresado para confirmar
    * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
    */
   public void autocompletarNuevoyDuplicadoMvr(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
         if (tipoNuevo == 1) {
            nuevaMvrs.getMotivo().setNombre(motivoMvrs);
         } else if (tipoNuevo == 2) {
            duplicarMvrs.getMotivo().setNombre(motivoMvrs);
         }
         for (int i = 0; i < listMotivosMvrs.size(); i++) {
            if (listMotivosMvrs.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaMvrs.setMotivo(listMotivosMvrs.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoMVRS");
            } else if (tipoNuevo == 2) {
               duplicarMvrs.setMotivo(listMotivosMvrs.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoMVRS");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:MotivoDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoMVRS");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoMVRS");
            }
         }
      }
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaProrrateo)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletarOtrosCertificados(int tipoNuevo, String Campo) {

      if (Campo.equals("CERTIFICADOS")) {
         if (tipoNuevo == 1) {
            tipoCertificado = nuevaOtroCertificado.getTipocertificado().getDescripcion();
         } else if (tipoNuevo == 2) {
            tipoCertificado = duplicarOtrosCertificados.getTipocertificado().getDescripcion();
         }
      }
   }

   /**
    *
    * Metodo que genera el auto completar de un proceso nuevoRegistro o
    * duplicarRegistro de VigenciasProrrateos
    *
    * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
    * PROYECTO
    * @param valorConfirmar Valor ingresado para confirmar
    * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
    */
   public void autocompletarNuevoyDuplicadoOtrosCertificados(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CERTIFICADO")) {
         if (tipoNuevo == 1) {
            nuevaOtroCertificado.getTipocertificado().setDescripcion(tipoCertificado);
         } else if (tipoNuevo == 2) {
            duplicarOtrosCertificados.getTipocertificado().setDescripcion(tipoCertificado);
         }
         for (int i = 0; i < listTiposCertificados.size(); i++) {
            if (listTiposCertificados.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaOtroCertificado.setTipocertificado(listTiposCertificados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCertificadoOC");
            } else if (tipoNuevo == 2) {
               duplicarOtrosCertificados.setTipocertificado(listTiposCertificados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCertificadoOC");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:CertificadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('CertificadosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCertificadoOC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCertificadoOC");
            }
         }
      }
   }

   //Ubicacion Celda.
   /**
    * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndiceMvr(Mvrs mvrs, int celda) {
      mvrSeleccionado = mvrs;
      if (permitirIndexMvrs) {
         cualCeldaMvrs = celda;
         fechaFin = mvrSeleccionado.getFechafinal();
         fechaIni = mvrSeleccionado.getFechainicial();
         if (cualCeldaMvrs == 3) {
            motivoMvrs = mvrSeleccionado.getMotivo().getNombre();
            activarLOV = false;
         } else {
            activarLOV = true;
         }
         RequestContext.getCurrentInstance().update("form:listaValores");
      }
      otroCertificadoSeleccionado = null;
      RequestContext.getCurrentInstance().execute("PF('datosOCEmpleado').unselectAllRows()");
      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node 
      String type = map.get("t"); // type attribute of node 
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      otroCertificadoSeleccionado = listOCertificados.get(indice);
      cambiarIndiceOtrosCertificados(otroCertificadoSeleccionado, columna);
   }

   public void deseleccionarRow() {
      System.out.println("entro en deseleccionarRow()");
      mvrSeleccionado = null;
      otroCertificadoSeleccionado = null;
      mvrSeleccionado = null;
      listOCertificados = null;
      listMvrsEmpleado = null;
      tipoActualizacion = -1;
      permitirIndexMvrs = true;
      filtrarListMotivosMvrs = null;
      motivoMvrSeleccionado = null;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
   }

   public void cambiarIndiceOtrosCertificados(OtrosCertificados otroC, int celda) {
      otroCertificadoSeleccionado = otroC;
      if (permitirIndexOtrosCertificados) {
         cualCeldaOtrosCertificados = celda;
         fechaFinOC = otroCertificadoSeleccionado.getFechafinal();
         fechaIniOC = otroCertificadoSeleccionado.getFechainicial();
         if (cualCeldaOtrosCertificados == 4) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            tipoCertificado = otroCertificadoSeleccionado.getTipocertificado().getDescripcion();
         } else {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
         }
      }
      mvrSeleccionado = null;
      RequestContext.getCurrentInstance().execute("PF('datosMvrEmpleado').unselectAllRows()");
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
   }

   public void guardarYSalir() {
      guardadoGeneral();
      salir();
   }

   //GUARDAR
   /**
    * Metodo de guardado general para la pagina
    */
   public void guardadoGeneral() {
      guardarCambiosMvrs();
      guardarCambiosOtrosCertificados();
      guardado = true;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina
    * VigenciasLocalizaciones
    */
   public void guardarCambiosMvrs() {
      if (cambiosMvr) {
         if (!listMvrsBorrar.isEmpty()) {
            administrarEmplMvrs.borrarMvrs(listMvrsBorrar);
            listMvrsBorrar.clear();
         }
         if (!listMvrsCrear.isEmpty()) {
            administrarEmplMvrs.crearMvrs(listMvrsCrear);
            listMvrsCrear.clear();
         }
         if (!listMvrsModificar.isEmpty()) {
            administrarEmplMvrs.modificarMvrs(listMvrsModificar);
            listMvrsModificar.clear();
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
         paraNuevaMenValRet = 0;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Mvrs con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         cambiosMvr = false;

         contarRegistrosMVR();
      }
      mvrSeleccionado = null;
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
    */
   public void guardarCambiosOtrosCertificados() {
      if (cambiosOtros) {
         if (!listOtrosCertificadosBorrar.isEmpty()) {
            administrarEmplMvrs.borrarOtrosCertificados(listOtrosCertificadosBorrar);
            listOtrosCertificadosBorrar.clear();
         }
         if (!listOtrosCertificadosCrear.isEmpty()) {
            administrarEmplMvrs.crearOtrosCertificados(listOtrosCertificadosCrear);
            listOtrosCertificadosCrear.clear();
         }
         if (!listOtrosCertificadosModificar.isEmpty()) {
            administrarEmplMvrs.modificarOtrosCertificados(listOtrosCertificadosModificar);
            listOtrosCertificadosModificar.clear();
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
         paraNuevaMenValRet = 0;
         cambiosOtros = false;

         contarRegistrosOC();

         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Otros Certificados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      otroCertificadoSeleccionado = null;
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaMvrs == 1) {
         restablecerTablaMVR();
      }
      if (banderaOC == 1) {
         restablecerTablaOC();
      }
      listMvrsBorrar.clear();
      listOtrosCertificadosBorrar.clear();
      listMvrsCrear.clear();
      listOtrosCertificadosCrear.clear();
      listMvrsModificar.clear();
      listOtrosCertificadosModificar.clear();
      paraNuevaMenValRet = 0;
      listMvrsEmpleado = null;
      listOCertificados = null;
      guardado = true;
      cambiosOtros = false;
      cambiosMvr = false;
      otroCertificadoSeleccionado = null;
      mvrSeleccionado = null;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");

      getListMvrsEmpleado();
      getListOCertificados();
      contarRegistrosMVR();
      contarRegistrosOC();

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (otroCertificadoSeleccionado == null && mvrSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (mvrSeleccionado != null) {
            editarMvrs = mvrSeleccionado;

            if (cualCeldaMvrs == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialMVRD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialMVRD').show()");
               cualCeldaMvrs = -1;
            } else if (cualCeldaMvrs == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalMVRD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalMVRD').show()");
               cualCeldaMvrs = -1;
            } else if (cualCeldaMvrs == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMVRD");
               RequestContext.getCurrentInstance().execute("PF('editarValorMVRD').show()");
               cualCeldaMvrs = -1;
            } else if (cualCeldaMvrs == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoMVRD");
               RequestContext.getCurrentInstance().execute("PF('editarMotivoMVRD').show()");
               cualCeldaMvrs = -1;
            } else if (cualCeldaMvrs == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarValorAMVRD");
               RequestContext.getCurrentInstance().execute("PF('editarValorAMVRD').show()");
               cualCeldaMvrs = -1;
            }
         }
         if (otroCertificadoSeleccionado != null) {
            editarOtrosCertificados = otroCertificadoSeleccionado;

            if (cualCeldaOtrosCertificados == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialOCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialOCD').show()");
               cualCeldaOtrosCertificados = -1;
            } else if (cualCeldaOtrosCertificados == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalOCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalOCD').show()");
               cualCeldaOtrosCertificados = -1;
            } else if (cualCeldaOtrosCertificados == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarValorOCD");
               RequestContext.getCurrentInstance().execute("PF('editarValorOCD').show()");
               cualCeldaOtrosCertificados = -1;
            } else if (cualCeldaOtrosCertificados == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoOCD");
               RequestContext.getCurrentInstance().execute("PF('editarEstadoOCD').show()");
               cualCeldaOtrosCertificados = -1;
            } else if (cualCeldaOtrosCertificados == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCertificadoOCD");
               RequestContext.getCurrentInstance().execute("PF('editarCertificadoOCD').show()");
               cualCeldaOtrosCertificados = -1;
            } else if (cualCeldaOtrosCertificados == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasOCD");
               RequestContext.getCurrentInstance().execute("PF('editarDiasOCD').show()");
               cualCeldaOtrosCertificados = -1;
            }
         }
      }
   }
   //CREAR VL

   /**
    * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
    */
   public void agregarNuevaMvr() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (nuevaMvrs.getFechainicial() != null && nuevaMvrs.getMotivo() != null) {
         int error = 0;
         for (int i = 0; i < listMvrsEmpleado.size(); i++) {
            if (listMvrsEmpleado.get(i).getFechainicial().equals(nuevaMvrs.getFechainicial())) {
               error++;
            }
         }
         if (error == 0) {
            if (validarFechasRegistroMvrs(1)) {
               cambiosMvr = true;
               if (banderaMvrs == 1) {
                  restablecerTablaMVR();
               }
               //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
               paraNuevaMenValRet++;
               BigInteger var = BigInteger.valueOf(paraNuevaMenValRet);
               nuevaMvrs.setSecuencia(var);
               nuevaMvrs.setEmpleado(empleado);
               listMvrsCrear.add(nuevaMvrs);
               listMvrsEmpleado.add(nuevaMvrs);
               contarRegistrosMVR();
               mvrSeleccionado = listMvrsEmpleado.get(listMvrsEmpleado.indexOf(nuevaMvrs));
               activarLOV = true;
               RequestContext.getCurrentInstance().update("form:listaValores");
               nuevaMvrs = new Mvrs();
               nuevaMvrs.setMotivo(new Motivosmvrs());
               RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroMVRS').hide()");
            } else {
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasDuplicadas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNewMvr').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    * Metodo que limpia las casillas de la nueva vigencia
    */
   public void limpiarNuevaMvr() {
      nuevaMvrs = new Mvrs();
      nuevaMvrs.setMotivo(new Motivosmvrs());
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Agrega una nueva Vigencia Prorrateo
    */
   public void agregarNuevaOtroC() {
      if (nuevaOtroCertificado.getFechainicial() != null && nuevaOtroCertificado.getFechafinal() != null
              && nuevaOtroCertificado.getTipocertificado() != null) {
         nuevaOtroCertificado.setEstado("MANUAL");
         if (validarFechasRegistroOtroC(1)) {
            cambiosOtros = true;
            if (banderaOC == 1) {
               restablecerTablaOC();
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS
            paraNuevaMenValRet++;
            nuevaMVRSecuencia = BigInteger.valueOf(paraNuevaMenValRet);
            BigDecimal var = BigDecimal.valueOf(paraNuevaMenValRet);
            nuevaOtroCertificado.setSecuencia(nuevaMVRSecuencia);
            nuevaOtroCertificado.setEmpleado(empleado);
            String aux = nuevaOtroCertificado.getEstado().toUpperCase();
            nuevaOtroCertificado.setEstado(aux);
            listOtrosCertificadosCrear.add(nuevaOtroCertificado);
            listOCertificados.add(nuevaOtroCertificado);
            contarRegistrosOC();
            otroCertificadoSeleccionado = listOCertificados.get(listOCertificados.indexOf(nuevaOtroCertificado));
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");

            nuevaOtroCertificado = new OtrosCertificados();
            nuevaOtroCertificado.setTipocertificado(new TiposCertificados());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOC').hide()");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasOC').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNewOtro').show()");
      }
   }

   /**
    * Limpia los elementos de una nueva vigencia prorrateo
    */
   public void limpiarNuevaOtroC() {
      nuevaOtroCertificado = new OtrosCertificados();
      nuevaOtroCertificado.setTipocertificado(new TiposCertificados());
      //nuevaOtroCertificado.setEstado("MANUAL");
   }

   //DUPLICAR VL
   /**
    * Metodo que verifica que proceso de duplicar se genera con respecto a la
    * posicion en la pagina que se tiene
    */
   public void verificarDuplicarRegistro() {
      if (otroCertificadoSeleccionado == null && mvrSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (mvrSeleccionado != null || otroCertificadoSeleccionado != null) {
         if (mvrSeleccionado != null) {
            limpiarduplicarMvr();
            duplicarMvr();
         }
         if (otroCertificadoSeleccionado != null) {
            limpiarduplicarOtroC();
            duplicarOtroC();
         }
      }
   }

   /**
    * Duplica una nueva vigencia localizacion
    */
   public void duplicarMvr() {
      if (mvrSeleccionado != null) {
         duplicarMvrs = new Mvrs();

         duplicarMvrs.setEmpleado(mvrSeleccionado.getEmpleado());
         duplicarMvrs.setValor(mvrSeleccionado.getValor());
         duplicarMvrs.setFechafinal(mvrSeleccionado.getFechafinal());
         duplicarMvrs.setFechainicial(mvrSeleccionado.getFechainicial());
         duplicarMvrs.setValoranualoriginal(mvrSeleccionado.getValoranualoriginal());
         duplicarMvrs.setMotivo(mvrSeleccionado.getMotivo());

         if (duplicarMvrs.getMotivo() == null) {
            duplicarMvrs.setMotivo(new Motivosmvrs());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMVRS");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroMVRS').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasLocalizaciones
    */
   public void confirmarDuplicar() {
      if (duplicarMvrs.getFechainicial() != null && duplicarMvrs.getMotivo() != null) {
         int error = 0;
         for (int i = 0; i < listMvrsEmpleado.size(); i++) {
            if (listMvrsEmpleado.get(i).getFechainicial().equals(duplicarMvrs.getFechainicial())) {
               error++;
            }
         }
         if (error == 0) {
            if (validarFechasRegistroMvrs(2)) {
               paraNuevaMenValRet++;
               BigInteger var = BigInteger.valueOf(paraNuevaMenValRet);
               duplicarMvrs.setSecuencia(var);
               cambiosMvr = true;
               listMvrsEmpleado.add(duplicarMvrs);
               listMvrsCrear.add(duplicarMvrs);
               mvrSeleccionado = listMvrsEmpleado.get(listMvrsEmpleado.indexOf(duplicarMvrs));
               activarLOV = true;
               RequestContext.getCurrentInstance().update("form:listaValores");
               contarRegistrosMVR();
               RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               if (banderaMvrs == 1) {
                  restablecerTablaMVR();
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroMVRS').hide()");
               duplicarMvrs = new Mvrs();
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasDuplicadas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNewMvr').show()");
      }
   }
   //LIMPIAR DUPLICAR

   /**
    * Metodo que limpia los datos de un duplicar Vigencia Localizacion
    */
   public void limpiarduplicarMvr() {
      duplicarMvrs = new Mvrs();
      duplicarMvrs.setMotivo(new Motivosmvrs());
   }

   ///////////////////////////////////////////////////////////////
   /**
    * Duplica una registro de VigenciaProrrateos
    */
   public void duplicarOtroC() {
      if (otroCertificadoSeleccionado != null) {
         duplicarOtrosCertificados = new OtrosCertificados();
         paraNuevaMenValRet++;
         nuevaMVRSecuencia = BigInteger.valueOf(paraNuevaMenValRet);
         duplicarOtrosCertificados.setSecuencia(nuevaMVRSecuencia);
         duplicarOtrosCertificados.setFechafinal(otroCertificadoSeleccionado.getFechafinal());
         duplicarOtrosCertificados.setFechainicial(otroCertificadoSeleccionado.getFechainicial());
         duplicarOtrosCertificados.setValor(otroCertificadoSeleccionado.getValor());
         duplicarOtrosCertificados.setEstado(otroCertificadoSeleccionado.getEstado());
         duplicarOtrosCertificados.setTipocertificado(otroCertificadoSeleccionado.getTipocertificado());
         duplicarOtrosCertificados.setDiascontratados(otroCertificadoSeleccionado.getDiascontratados());

         if (duplicarOtrosCertificados.getTipocertificado().getSecuencia() == null) {
            duplicarOtrosCertificados.setTipocertificado(new TiposCertificados());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOC");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOC').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciaProrrateo
    */
   public void confirmarDuplicarOtroC() {
      if (duplicarOtrosCertificados.getFechainicial() != null && duplicarOtrosCertificados.getFechafinal() != null
              && duplicarOtrosCertificados.getEstado() != null && duplicarOtrosCertificados.getTipocertificado() != null) {
         if (validarFechasRegistroOtroC(2)) {
            cambiosOtros = true;
            String aux = duplicarOtrosCertificados.getEstado().toUpperCase();
            duplicarOtrosCertificados.setEstado(aux);
            listOCertificados.add(duplicarOtrosCertificados);
            listOtrosCertificadosCrear.add(duplicarOtrosCertificados);
            otroCertificadoSeleccionado = listOCertificados.get(listOCertificados.indexOf(duplicarOtrosCertificados));
            contarRegistrosOC();
            RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOC.hide();");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaOC == 1) {
               restablecerTablaOC();
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            duplicarOtrosCertificados = new OtrosCertificados();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasOC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNewOtro').show()");
      }
   }

   /**
    * Limpia los elementos del duplicar registro Vigencia Prorrateo
    */
   public void limpiarduplicarOtroC() {
      duplicarOtrosCertificados = new OtrosCertificados();
      duplicarOtrosCertificados.setTipocertificado(new TiposCertificados());

   }

   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void validarBorradoRegistro() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (otroCertificadoSeleccionado == null && mvrSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (mvrSeleccionado != null) {
            borrarMvr();
         }
         if (otroCertificadoSeleccionado != null) {
            borrarOtroC();
         }
      }
   }

   //BORRAR VL
   /**
    * Metodo que borra una vigencia localizacion
    */
   public void borrarMvr() {
      if (mvrSeleccionado != null) {
         if (!listMvrsModificar.isEmpty() && listMvrsModificar.contains(mvrSeleccionado)) {
            int modIndex = listMvrsModificar.indexOf(mvrSeleccionado);
            listMvrsModificar.remove(modIndex);
            listMvrsBorrar.add(mvrSeleccionado);
         } else if (!listMvrsCrear.isEmpty() && listMvrsCrear.contains(mvrSeleccionado)) {
            int crearIndex = listMvrsCrear.indexOf(mvrSeleccionado);
            listMvrsCrear.remove(crearIndex);
         } else {
            listMvrsBorrar.add(mvrSeleccionado);
         }
         listMvrsEmpleado.remove(mvrSeleccionado);
         if (tipoListaMvrs == 1) {
            filtrarListMvrsEmpleado.remove(mvrSeleccionado);
         }
         contarRegistrosMVR();
         RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
         mvrSeleccionado = null;
         cambiosMvr = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   /**
    * Metodo que borra una vigencia prorrateo
    */
   public void borrarOtroC() {
      if (otroCertificadoSeleccionado != null) {
         if (!listOtrosCertificadosModificar.isEmpty() && listOtrosCertificadosModificar.contains(otroCertificadoSeleccionado)) {
            int modIndex = listOtrosCertificadosModificar.indexOf(otroCertificadoSeleccionado);
            listOtrosCertificadosModificar.remove(modIndex);
            listOtrosCertificadosBorrar.add(otroCertificadoSeleccionado);
         } else if (!listOtrosCertificadosCrear.isEmpty() && listOtrosCertificadosCrear.contains(otroCertificadoSeleccionado)) {
            listOtrosCertificadosCrear.remove(otroCertificadoSeleccionado);
         } else {
            listOtrosCertificadosBorrar.add(otroCertificadoSeleccionado);
         }
         if (tipoListaOtrosCertificados == 1) {
            filtrarListOtrosCertificados.remove(filtrarListOtrosCertificados.indexOf(otroCertificadoSeleccionado));
         }
         listOCertificados.remove(otroCertificadoSeleccionado);
         contarRegistrosOC();
         RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
         otroCertificadoSeleccionado = null;
         cambiosOtros = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      // if (mvrSeleccionado != null) {
      filtradoMvr();
      // }
      //  if (indexOtrosCertificados >= 0) {
      filtradoOtroC();
      //  }
   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia localizacion
    */
   public void filtradoMvr() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (mvrSeleccionado != null) {
         if (banderaMvrs == 0) {
            mvrValorAnual = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrValorAnual");
            mvrValorAnual.setFilterStyle("width: 85% !important");
            mvrMotivo = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrMotivo");
            mvrMotivo.setFilterStyle("width: 85% !important");
            mvrValor = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrValor");
            mvrValor.setFilterStyle("width: 85% !important");
            mvrFechaFinal = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrFechaFinal");
            mvrFechaFinal.setFilterStyle("width: 85% !important");
            mvrFechaInicial = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrFechaInicial");
            mvrFechaInicial.setFilterStyle("width: 85% !important");
            altoTabla1 = "95";
            RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
            banderaMvrs = 1;
         } else if (banderaMvrs == 1) {
            restablecerTablaMVR();
         }
      }
   }

   public void restablecerTablaMVR() {
      FacesContext c = FacesContext.getCurrentInstance();
      mvrValorAnual = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrValorAnual");
      mvrValorAnual.setFilterStyle("display: none; visibility: hidden;");
      mvrMotivo = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrMotivo");
      mvrMotivo.setFilterStyle("display: none; visibility: hidden;");
      mvrValor = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrValor");
      mvrValor.setFilterStyle("display: none; visibility: hidden;");
      mvrFechaFinal = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrFechaFinal");
      mvrFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      mvrFechaInicial = (Column) c.getViewRoot().findComponent("form:datosMvrEmpleado:mvrFechaInicial");
      mvrFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      altoTabla1 = "115";
      banderaMvrs = 0;
      filtrarListMvrsEmpleado = null;
      tipoListaMvrs = 0;
      RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
   }

   public void restablecerTablaOC() {
      FacesContext c = FacesContext.getCurrentInstance();
      ocDias = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocDias");
      ocDias.setFilterStyle("display: none; visibility: hidden;");
      ocCertificado = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocCertificado");
      ocCertificado.setFilterStyle("display: none; visibility: hidden;");
      ocEstado = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocEstado");
      ocEstado.setFilterStyle("display: none; visibility: hidden;");
      ocValor = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocValor");
      ocValor.setFilterStyle("display: none; visibility: hidden;");
      ocFechaFinal = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocFechaFinal");
      ocFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      ocFechaInicial = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocFechaInicial");
      ocFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      altoTabla2 = "115";
      banderaOC = 0;
      filtrarListOtrosCertificados = null;
      tipoListaOtrosCertificados = 0;
      RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia prorrateo
    */
   public void filtradoOtroC() {
      if (otroCertificadoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (banderaOC == 0) {
            //Columnas Tabla VPP
            ocDias = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocDias");
            ocDias.setFilterStyle("width: 85% !important");
            ocCertificado = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocCertificado");
            ocCertificado.setFilterStyle("width: 85% !important");
            ocEstado = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocEstado");
            ocEstado.setFilterStyle("width: 85% !important");
            ocValor = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocValor");
            ocValor.setFilterStyle("width: 85% !important");
            ocFechaFinal = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocFechaFinal");
            ocFechaFinal.setFilterStyle("width: 85% !important");
            ocFechaInicial = (Column) c.getViewRoot().findComponent("form:datosOCEmpleado:ocFechaInicial");
            ocFechaInicial.setFilterStyle("width: 85% !important");
            altoTabla2 = "95";
            RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
            banderaOC = 1;
         } else if (banderaOC == 1) {
            restablecerTablaOC();
         }
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaMvrs == 1) {
         restablecerTablaMVR();
      }
      if (banderaOC == 1) {
         restablecerTablaOC();
      }
      limpiarListasValor();
      listMvrsBorrar.clear();
      listMvrsCrear.clear();
      listMvrsModificar.clear();
      listOtrosCertificadosBorrar.clear();
      listOtrosCertificadosCrear.clear();
      listOtrosCertificadosModificar.clear();
      mvrSeleccionado = null;
      otroCertificadoSeleccionado = null;
      paraNuevaMenValRet = 0;
      listMvrsEmpleado = null;
      listOCertificados = null;
      guardado = true;
      cambiosMvr = false;
      cambiosOtros = false;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   public void asignarIndexMVR(Mvrs mvr, int column, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      mvrSeleccionado = mvr;
      tipoActualizacion = tipoAct;
      System.out.println("entro en asignarIndex column : " + column + " , tipoAct : " + tipoAct);
      if (column == 0) {
         activarLOV = false;
         context.update("form:listaValores");
         contarRegistrosMVR();
         context.update("form:MotivoDialogo");
         System.out.println("Va a hacer el .show() de MotivoDialogo");
         RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
      }
   }

   public void asignarIndexOC(OtrosCertificados oc, int column, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      otroCertificadoSeleccionado = oc;
      tipoActualizacion = tipoAct;
      if (column == 0) {
         activarLOV = false;
         context.update("form:listaValores");
         contarRegistrosCer();
         context.update("form:CertificadosDialogo");
         context.execute("PF('CertificadosDialogo').show()");
      }

   }

   public void asignarIndex(int column, int tipoAct, int tipoTab) {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("entro en asignarIndex2");
      if (tipoTab == 0) {
         tipoActualizacion = tipoAct;
         if (column == 0) {
            activarLOV = false;
            context.update("form:listaValores");
            contarRegistrosMVR();
            context.update("form:MotivoDialogo");
            context.execute("PF('MotivoDialogo').show()");
         }
      }
      if (tipoTab == 1) {
         System.out.println("entro tt: " + tipoTab);
         tipoActualizacion = tipoAct;
         if (column == 0) {
            activarLOV = false;
            context.update("form:listaValores");
            contarRegistrosCer();
            context.update("form:CertificadosDialogo");
            context.execute("PF('CertificadosDialogo').show()");
         }
      }
   }

   //LOVS
   //Estructuras
   /**
    * Metodo que actualiza la estructura seleccionada (vigencia localizacion)
    */
   public void actualizarMotivo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
         mvrSeleccionado.setMotivo(motivoMvrSeleccionado);
         if (!listMvrsCrear.contains(mvrSeleccionado)) {
            if (listMvrsModificar.isEmpty()) {
               listMvrsModificar.add(mvrSeleccionado);
            } else if (!listMvrsModificar.contains(mvrSeleccionado)) {
               listMvrsModificar.add(mvrSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexMvrs = true;
         RequestContext.getCurrentInstance().update("form:datosMvrEmpleado");
      } else if (tipoActualizacion == 1) {// Para crear registro
         nuevaMvrs.setMotivo(motivoMvrSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMVRS");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarMvrs.setMotivo(motivoMvrSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMVRS");
      }
      filtrarListMotivosMvrs = null;
      motivoMvrSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCeldaMvrs = -1;
      context.reset("form:lovMotivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').hide()");
   }

   /**
    * Metodo que cancela los cambios sobre estructura (vigencia localizacion)
    */
   public void cancelarCambioMotivo() {
      filtrarListMotivosMvrs = null;
      motivoMvrSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexMvrs = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovMotivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').hide()");
   }

   //Motivo Localizacion
   /**
    * Metodo que actualiza el proyecto seleccionado (vigencia localizacion)
    */
   public void actualizarTipo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
         otroCertificadoSeleccionado.setTipocertificado(tipoCertificadoSeleccionado);
         if (!listOtrosCertificadosCrear.contains(otroCertificadoSeleccionado)) {
            if (listOtrosCertificadosModificar.isEmpty()) {
               listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
            } else if (!listOtrosCertificadosModificar.contains(otroCertificadoSeleccionado)) {
               listOtrosCertificadosModificar.add(otroCertificadoSeleccionado);
            }
         }

         cambiosOtros = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexOtrosCertificados = true;
         RequestContext.getCurrentInstance().update("form:datosOCEmpleado");
      } else if (tipoActualizacion == 1) {// Para crear registro
         nuevaOtroCertificado.setTipocertificado(tipoCertificadoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCertificadoOC");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarOtrosCertificados.setTipocertificado(tipoCertificadoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCertificadoOC");
      }
      filtrarListTiposCertificados = null;
      tipoCertificadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      context.reset("form:lovCertificado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCertificado').clearFilters()");
   }

   /**
    * Metodo que cancela la seleccion del proyecto (vigencia localizacion)
    */
   public void cancelarCambioTipo() {
      filtrarListTiposCertificados = null;
      tipoCertificadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexOtrosCertificados = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCertificado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCertificado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CertificadosDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   /**
    * Metodo que activa la lista de valores de todas las tablas con respecto al
    * index activo y la columna activa
    */
   public void listaValoresBoton() {
      if (otroCertificadoSeleccionado == null && mvrSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (mvrSeleccionado != null) {
            if (cualCeldaMvrs == 3) {
               contarRegistrosMVR();
               RequestContext.getCurrentInstance().update("form:MotivoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (otroCertificadoSeleccionado != null) {
            if (cualCeldaOtrosCertificados == 4) {
               contarRegistrosCer();
               RequestContext.getCurrentInstance().update("form:CertificadosDialogo");
               RequestContext.getCurrentInstance().execute("PF('CertificadosDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      }
   }

   /**
    * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
    * posicion en la pagina
    */
   public void validarNuevoRegistro() {
      int tam1 = 0;
      if (listMvrsEmpleado != null) {
         tam1 = listMvrsEmpleado.size();
      }
      int tam2 = 0;
      if (listOCertificados != null) {
         tam2 = listOCertificados.size();
      }
      if (tam1 == 0 || tam2 == 0) {
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').show()");
      } else {
         if (mvrSeleccionado != null) {
            limpiarNuevaMvr();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroMVRS");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroMVRS').show()");
         }
         if (otroCertificadoSeleccionado != null) {
            limpiarNuevaOtroC();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroOC");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOC').show()");
         }
      }
   }

   /**
    * Metodo que activa el boton aceptar de la pagina y los dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   /**
    * Selecciona la tabla a exportar XML con respecto al index activo
    *
    * @return Nombre del dialogo a exportar en XML
    */
   public String exportXML() {
      if (mvrSeleccionado != null) {
         nombreTabla = ":formExportarMVR:datosMvrEmpleadoExportar";
         nombreXML = "M.V.R.XML";
      }
      if (otroCertificadoSeleccionado != null) {
         nombreTabla = ":formExportarOC:datosOCEmpleadoExportar";
         nombreXML = "OtrosCertificadosXML";
      }
      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (mvrSeleccionado != null) {
         exportPDF_MVR();
      }
      if (otroCertificadoSeleccionado != null) {
         exportPDF_OC();
      }
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_MVR() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarMVR:datosMvrEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "M.V.R.PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Prorrateo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_OC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOC:datosOCEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "OtrosCertificadosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      if (mvrSeleccionado != null) {
         exportXLS_MVR();
      }
      if (otroCertificadoSeleccionado != null) {
         exportXLS_OC();
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_MVR() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarMVR:datosMvrEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "M.V.R.XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Afiliaciones
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_OC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOC:datosOCEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "OtrosCertificadosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrarMVRS() {
      if (tipoListaMvrs == 0) {
         tipoListaMvrs = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      mvrSeleccionado = null;
      contarRegistrosMVR();
   }

   public void eventoFiltrarOC() {
      if (tipoListaMvrs == 0) {
         tipoListaMvrs = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      otroCertificadoSeleccionado = null;
      contarRegistrosOC();
   }

   public void contarRegistrosCer() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCertificado");
   }

   public void contarRegistrosMot() {
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivoMVR");
   }

   public void contarRegistrosMVR() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroMVR");
   }

   public void contarRegistrosOC() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroOtroC");
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
   public void verificarRastroTabla() {
      //Cuando alguna tabla no tiene datos
      if (listOCertificados == null || listMvrsEmpleado == null) {
         //Cuando no se ha seleccionado ningun registro
         if (mvrSeleccionado == null && otroCertificadoSeleccionado == null) {
            //Dialogo para seleccionar el rastro de la tabla deseada
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
         }
         //Cuando una tabla tiene datos y se selecciono registro:
         if (mvrSeleccionado != null) {
            verificarRastroMvr();
         }
         if (otroCertificadoSeleccionado != null) {
            verificarRastroOC();
         }
      }
      //Cuando las dos tablas tienen datos
      if ((listOCertificados != null) && (listMvrsEmpleado != null)) {
         //Cuando no se ha seleccionado ningun registro:
         if (mvrSeleccionado == null && otroCertificadoSeleccionado == null) {
            //Dialogo para seleccionar el rastro de la tabla deseada
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
         }
         //Cuando se selecciono registro:
         if (mvrSeleccionado != null) {
            verificarRastroMvr();
         }
         if (otroCertificadoSeleccionado != null) {
            verificarRastroOC();
         }
      }
   }

   public void verificarRastroMvr() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (mvrSeleccionado.getSecuencia() != null) {
         int resultado = administrarRastros.obtenerTabla(mvrSeleccionado.getSecuencia(), "MVRS");
         backUp = mvrSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Mvrs";
            msnConfirmarRastro = "La tabla MVRS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
   }

   public void verificarRastroMvrHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("MVRS")) {
         nombreTablaRastro = "Mvrs";
         msnConfirmarRastroHistorico = "La tabla MVRS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroOC() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (otroCertificadoSeleccionado.getSecuencia() != null) {
         int resultado = administrarRastros.obtenerTabla(otroCertificadoSeleccionado.getSecuencia(), "OTROSCERTIFICADOS");
         backUp = otroCertificadoSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "OtrosCertificados";
            msnConfirmarRastro = "La tabla OTROSCERTIFICADOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
   }

   public void verificarRastroOCHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("OTROSCERTIFICADOS")) {
         nombreTablaRastro = "OtrosCertificados";
         msnConfirmarRastroHistorico = "La tabla OTROSCERTIFICADOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void recordarSeleccionMVR() {
      if (mvrSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosMvrEmpleado");
         tablaC.setSelection(mvrSeleccionado);
      }
   }

   public void recordarSeleccionOS() {
      if (otroCertificadoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosOCEmpleado");
         tablaC.setSelection(otroCertificadoSeleccionado);
      }
   }

   //  GET'S y SET'S
   public List<Mvrs> getListMvrsEmpleado() {
      try {
         if (listMvrsEmpleado == null) {
            if (empleado != null) {
               listMvrsEmpleado = administrarEmplMvrs.listMvrsEmpleado(empleado.getSecuencia());
            }
         }
         return listMvrsEmpleado;
      } catch (Exception e) {
         System.out.println("Error getListMvrsEmpleado : " + e.toString());
         return null;
      }
   }

   public void setListMvrsEmpleado(List<Mvrs> listMvrsEmpleado) {
      this.listMvrsEmpleado = listMvrsEmpleado;
   }

   public List<Mvrs> getFiltrarListMvrsEmpleado() {
      return filtrarListMvrsEmpleado;
   }

   public void setFiltrarListMvrsEmpleado(List<Mvrs> filtrarListMvrsEmpleado) {
      this.filtrarListMvrsEmpleado = filtrarListMvrsEmpleado;
   }

   public List<OtrosCertificados> getListOCertificados() {
      try {
         if (listOCertificados == null) {
            if (empleado != null) {
               listOCertificados = administrarEmplMvrs.listOtrosCertificadosEmpleado(empleado.getSecuencia());
            }
            if (listOCertificados != null) {
               if (!listOCertificados.isEmpty()) {
                  for (int i = 0; i < listOCertificados.size(); i++) {
                     hMapEstados.put(listOCertificados.get(i).getSecuencia().intValue(), listOCertificados.get(i).getEstado());
                  }
               }
            }
         }
         return listOCertificados;
      } catch (Exception e) {
         System.out.println("Error getListOtrosCertificadosEmpleado : " + e.toString());
         return null;
      }
   }

   public void setListOCertificados(List<OtrosCertificados> listOtrosCertificadosEmpleado) {
      this.listOCertificados = listOtrosCertificadosEmpleado;
   }

   public List<OtrosCertificados> getFiltrarListOtrosCertificados() {
      return filtrarListOtrosCertificados;
   }

   public void setFiltrarListOtrosCertificados(List<OtrosCertificados> filtrarListOtrosCertificados) {
      this.filtrarListOtrosCertificados = filtrarListOtrosCertificados;
   }

   public List<Motivosmvrs> getListMotivosMvrs() {
      if (listMotivosMvrs == null) {
         listMotivosMvrs = administrarEmplMvrs.listMotivos();
      }
      return listMotivosMvrs;
   }

   public void setListMotivosMvrs(List<Motivosmvrs> listMotivosMvrs) {
      this.listMotivosMvrs = listMotivosMvrs;
   }

   public Motivosmvrs getMotivoMvrSeleccionado() {
      return motivoMvrSeleccionado;
   }

   public void setMotivoMvrSeleccionado(Motivosmvrs motivoMvrSeleccionado) {
      this.motivoMvrSeleccionado = motivoMvrSeleccionado;
   }

   public List<Motivosmvrs> getFiltrarListMotivosMvrs() {
      return filtrarListMotivosMvrs;
   }

   public void setFiltrarListMotivosMvrs(List<Motivosmvrs> filtrarListMotivosMvrs) {
      this.filtrarListMotivosMvrs = filtrarListMotivosMvrs;
   }

   public List<TiposCertificados> getListTiposCertificados() {
      if (listTiposCertificados == null) {
         listTiposCertificados = administrarEmplMvrs.listTiposCertificados();
      }
      return listTiposCertificados;
   }

   public void setListTiposCertificados(List<TiposCertificados> listTiposCertificados) {
      this.listTiposCertificados = listTiposCertificados;
   }

   public TiposCertificados getTipoCertificadoSeleccionado() {
      return tipoCertificadoSeleccionado;
   }

   public void setTipoCertificadoSeleccionado(TiposCertificados tipoCertificadoSeleccionado) {
      this.tipoCertificadoSeleccionado = tipoCertificadoSeleccionado;
   }

   public List<TiposCertificados> getFiltrarListTiposCertificados() {
      return filtrarListTiposCertificados;
   }

   public void setFiltrarListTiposCertificados(List<TiposCertificados> filtrarListTiposCertificados) {
      this.filtrarListTiposCertificados = filtrarListTiposCertificados;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<Mvrs> getListMvrsModificar() {
      return listMvrsModificar;
   }

   public void setListMvrsModificar(List<Mvrs> listMvrsModificar) {
      this.listMvrsModificar = listMvrsModificar;
   }

   public List<OtrosCertificados> getListOtrosCertificadosModificar() {
      return listOtrosCertificadosModificar;
   }

   public void setListOtrosCertificadosModificar(List<OtrosCertificados> listOtrosCertificadosModificar) {
      this.listOtrosCertificadosModificar = listOtrosCertificadosModificar;
   }

   public Mvrs getNuevaMvrs() {
      return nuevaMvrs;
   }

   public void setNuevaMvrs(Mvrs nuevaMvrs) {
      this.nuevaMvrs = nuevaMvrs;
   }

   public OtrosCertificados getNuevaOtroCertificado() {
      return nuevaOtroCertificado;
   }

   public void setNuevaOtroCertificado(OtrosCertificados nuevaOtroCertificado) {
      this.nuevaOtroCertificado = nuevaOtroCertificado;
   }

   public List<Mvrs> getListMvrsCrear() {
      return listMvrsCrear;
   }

   public void setListMvrsCrear(List<Mvrs> listMvrsCrear) {
      this.listMvrsCrear = listMvrsCrear;
   }

   public List<Mvrs> getListMvrsBorrar() {
      return listMvrsBorrar;
   }

   public void setListMvrsBorrar(List<Mvrs> listMvrsBorrar) {
      this.listMvrsBorrar = listMvrsBorrar;
   }

   public List<OtrosCertificados> getListOtrosCertificadosBorrar() {
      return listOtrosCertificadosBorrar;
   }

   public void setListOtrosCertificadosBorrar(List<OtrosCertificados> listOtrosCertificadosBorrar) {
      this.listOtrosCertificadosBorrar = listOtrosCertificadosBorrar;
   }

   public Mvrs getEditarMvrs() {
      return editarMvrs;
   }

   public void setEditarMvrs(Mvrs editarMvrs) {
      this.editarMvrs = editarMvrs;
   }

   public OtrosCertificados getEditarOtrosCertificados() {
      return editarOtrosCertificados;
   }

   public void setEditarOtrosCertificados(OtrosCertificados editarOtrosCertificados) {
      this.editarOtrosCertificados = editarOtrosCertificados;
   }

   public Mvrs getDuplicarMvrs() {
      return duplicarMvrs;
   }

   public void setDuplicarMvrs(Mvrs duplicarMvrs) {
      this.duplicarMvrs = duplicarMvrs;
   }

   public OtrosCertificados getDuplicarOtrosCertificados() {
      return duplicarOtrosCertificados;
   }

   public void setDuplicarOtrosCertificados(OtrosCertificados duplicarOtrosCertificados) {
      this.duplicarOtrosCertificados = duplicarOtrosCertificados;
   }

   public List<OtrosCertificados> getListOtrosCertificadosCrear() {
      return listOtrosCertificadosCrear;
   }

   public void setListOtrosCertificadosCrear(List<OtrosCertificados> listOtrosCertificadosCrear) {
      this.listOtrosCertificadosCrear = listOtrosCertificadosCrear;
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

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public Mvrs getMvrSeleccionado() {
      return mvrSeleccionado;
   }

   public void setMvrSeleccionado(Mvrs mvrSeleccionado) {
      this.mvrSeleccionado = mvrSeleccionado;
   }

   public OtrosCertificados getOtroCertificadoSeleccionado() {
      return otroCertificadoSeleccionado;
   }

   public void setOtroCertificadoSeleccionado(OtrosCertificados otroCertificadoSeleccionado) {
      this.otroCertificadoSeleccionado = otroCertificadoSeleccionado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public String getAltoTabla1() {
      return altoTabla1;
   }

   public String getAltoTabla2() {
      return altoTabla2;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getInfoRegistroMotivoMVR() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivo");
      infoRegistroMotivoMVR = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoMVR;
   }

   public String getInfoRegistroCertificado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCertificado");
      infoRegistroCertificado = String.valueOf(tabla.getRowCount());
      return infoRegistroCertificado;
   }

   public String getInfoRegistroMVR() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMvrEmpleado");
      infoRegistroMVR = String.valueOf(tabla.getRowCount());
      return infoRegistroMVR;
   }

   public String getInfoRegistroOtroC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosOCEmpleado");
      infoRegistroOtroC = String.valueOf(tabla.getRowCount());
      return infoRegistroOtroC;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public String getEstadoMientras() {
      return estadoMientras;
   }

   public void setEstadoMientras(String estadoMientras) {
      this.estadoMientras = estadoMientras;
   }
}
