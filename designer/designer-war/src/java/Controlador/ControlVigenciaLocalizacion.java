package Controlador;

import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosLocalizaciones;
import Entidades.Proyectos;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasProrrateos;
import Entidades.VigenciasProrrateosProyectos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciaLocalizacionInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ControlVigenciaLocalizacion implements Serializable {

   @EJB
   AdministrarVigenciaLocalizacionInterface administrarVigenciaLocalizacion;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Vigencias Localizaciones
   private List<VigenciasLocalizaciones> vigenciaLocalizaciones;
   private List<VigenciasLocalizaciones> filtradoVL;
   private VigenciasLocalizaciones vigenciaLocalizacionSeleccionada;
   //Vigencias Prorrateos
   private List<VigenciasProrrateos> vigenciasProrrateosCentroC;
   private List<VigenciasProrrateos> filtradoVP;
   private VigenciasProrrateos vigenciaProrrateoSeleccionada;
   // Vigencia Prorrateos Proyectos
   private List<VigenciasProrrateosProyectos> vigenciasProrrateosProyectos;
   private List<VigenciasProrrateosProyectos> filtradoPP;
   private VigenciasProrrateosProyectos vigenciaProrrateoProyectoSeleccionada;
   //Centros Costos
   private List<CentrosCostos> listCentrosCostos;
   private CentrosCostos centroCostoSeleccionado;
   private List<CentrosCostos> filtradoCentroCostos;
   //Motivos Localizaciones
   private List<MotivosLocalizaciones> listMotivosLocalizaciones;
   private MotivosLocalizaciones motivoLocalizacionSelecionado;
   private List<MotivosLocalizaciones> filtradoMotivosLocalizaciones;
   //Estructuras-Centro Costo 
   private List<Estructuras> listEstructurasCC;
   private Estructuras estructuraSelecionada;
   private List<Estructuras> filtradoEstructuraCC;
   //Proyectos
   private List<Proyectos> listProyectos;
   private Proyectos proyectoSelecionado;
   private List<Proyectos> filtradoProyectos;
   //Empleado
   private Empleados empleado;
   //Tipo Actualizacion
   private int tipoActualizacion;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Activo/Desactivo VP Crtl + F11
   private int banderaVP;
   //Activo/Desactivo VPP Crtl + F11
   private int banderaVPP;
   //Columnas Tabla VL
   private Column vlFechaVigencia, vlCentroCosto, vlLocalizacion, vlMotivo, vlProyecto;
   //Columnas Tabla VP
   private Column vPCentroCosto, vPPorcentaje, vPFechaInicial, vPFechaFinal, vPProyecto, vPSubPorcentaje;
   //Columnas Tabla VPP
   private Column vPPProyecto, vPPPorcentaje, vPPFechaInicial, vPPFechaFinal;
   //Otros
   private boolean aceptar;
   //private int index;
   //modificar
   private List<VigenciasLocalizaciones> listVLModificar;
   private List<VigenciasProrrateos> listVPModificar;
   private List<VigenciasProrrateosProyectos> listVPPModificar;
   private boolean guardado;
   //crear VL
   public VigenciasLocalizaciones nuevaVigencia;
   //crear VP
   public VigenciasProrrateos nuevaVigenciaP;
   //crear VPP
   public VigenciasProrrateosProyectos nuevaVigenciaPP;
   private List<VigenciasLocalizaciones> listVLCrear;
   private BigInteger nuevaVSecuencia;
   private int paraNuevaV;
   //borrar VL
   private List<VigenciasLocalizaciones> listVLBorrar;
   private List<VigenciasProrrateos> listVPBorrar;
   private List<VigenciasProrrateosProyectos> listVPPBorrar;
   //editar celda
   private VigenciasLocalizaciones editarVL;
   private VigenciasProrrateos editarVP;
   private VigenciasProrrateosProyectos editarVPP;
   private int cualCelda, tipoLista;
   //duplicar
   private VigenciasLocalizaciones duplicarVL;
   //Autocompletar
   private boolean permitirIndex, permitirIndexVP, permitirIndexVPP;
   //Variables Autompletar
   private String centroCosto, motivoLocalizacion, proyecto, centroCostosVP, proyectoVP, proyectoVPP;
   //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
   private int cualCeldaVP, cualCeldaVPP, tipoListaVP, tipoListaVPP;
   //Index Auxiliar Para Nuevos Registros
   private int indexAuxVL;
   //Duplicar Vigencia Prorrateo
   private VigenciasProrrateos duplicarVP;
   //Duplicar Vigencia Prorrateo Proyecto
   private VigenciasProrrateosProyectos duplicarVPP;
   //Lista Vigencia Prorrateo Crear
   private List<VigenciasProrrateos> listVPCrear;
   //Lista Vigencia Prorrateo Proyecto Crear
   private List<VigenciasProrrateosProyectos> listVPPCrear;
   private String nombreXML;
   private String nombreTabla;
   //Banderas Boolean de operaciones sobre vigencias prorrateos y vigencias prorrateos proyectos
   private boolean cambioVigenciaP, cambioVigenciaPP, cambiosVigencia;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private Date fechaParametro;
   private Date fechaVigencia, fechaIniVP, fechaFinVP, fechaIniVPP, fechaFinVPP;
   private String altoTabla1, altoTabla2, altoTabla3;
   //
   private String infoRegistroEstLocalizacion;
   private String infoRegistroMotivoLocalizacion;
   private String infoRegistroProyecto;
   private String infoRegistroCentroCosto;
   private String infoRegistroVL;
   private String infoRegistroVP;
   private String infoRegistroVPP;
   //Validaciones
   private String mensajeValidacion;
   private DataTable tabla;
   //para control
   private int cualTabla;
   //
   private boolean activarLOV;

   public ControlVigenciaLocalizacion() {

      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      backUp = null;
      nombreTablaRastro = "";

      listCentrosCostos = null;

      vigenciaLocalizaciones = null;
      listEstructurasCC = null;
      listMotivosLocalizaciones = null;
      empleado = new Empleados();
      listProyectos = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listVLBorrar = new ArrayList<VigenciasLocalizaciones>();
      listVPBorrar = new ArrayList<VigenciasProrrateos>();
      listVPPBorrar = new ArrayList<VigenciasProrrateosProyectos>();
      //crear aficiones
      listVPCrear = new ArrayList<VigenciasProrrateos>();
      listVPPCrear = new ArrayList<VigenciasProrrateosProyectos>();
      listVLCrear = new ArrayList<VigenciasLocalizaciones>();
      //modificar aficiones
      listVLModificar = new ArrayList<VigenciasLocalizaciones>();
      listVPModificar = new ArrayList<VigenciasProrrateos>();
      listVPPModificar = new ArrayList<VigenciasProrrateosProyectos>();
      paraNuevaV = 0;
      //editar
      editarVL = new VigenciasLocalizaciones();
      editarVP = new VigenciasProrrateos();
      editarVPP = new VigenciasProrrateosProyectos();
      cualCelda = -1;
      tipoLista = 0;
      tipoListaVP = 0;
      tipoListaVPP = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevaVigencia = new VigenciasLocalizaciones();
      nuevaVigencia.setLocalizacion(new Estructuras());
      nuevaVigencia.getLocalizacion().setCentrocosto(new CentrosCostos());
      nuevaVigencia.setMotivo(new MotivosLocalizaciones());
      nuevaVigencia.setProyecto(new Proyectos());

      nuevaVigenciaP = new VigenciasProrrateos();
      nuevaVigenciaP.setCentrocosto(new CentrosCostos());
      nuevaVigenciaP.setProyecto(new Proyectos());
      nuevaVigenciaP.setPorcentaje(new BigDecimal(0));

      nuevaVigenciaPP = new VigenciasProrrateosProyectos();
      nuevaVigenciaPP.setProyecto(new Proyectos());
      nuevaVigenciaPP.setPorcentaje(0);

      bandera = 0;
      banderaVP = 0;
      banderaVPP = 0;
      permitirIndex = true;
      permitirIndexVP = true;
      permitirIndexVPP = true;
      cualCeldaVP = -1;
      cualCeldaVPP = -1;

      nombreTabla = ":formExportarVL:datosVLEmpleadoExportar";
      nombreXML = "VigenciasLocalizacionesXML";

      cambioVigenciaP = false;
      cambioVigenciaPP = false;
      cambiosVigencia = false;
      altoTabla1 = "104";
      altoTabla2 = "104";
      altoTabla3 = "104";

      mensajeValidacion = "";
      tipoActualizacion = 0;
      cualTabla = 0;

      activarLOV = true;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarVigenciaLocalizacion.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   //EMPLEADO DE LA VIGENCIA
   /**
    * Metodo que recibe la secuencia empleado desde la pagina anterior y obtiene
    * el empleado referenciado
    *
    * @param sec Secuencia del Empleado
    */
   public void recibirEmpleado(Empleados empl) {
      empleado = empl;
      if (vigenciaLocalizaciones != null) {
         if (!vigenciaLocalizaciones.isEmpty()) {
            vigenciaLocalizacionSeleccionada = vigenciaLocalizaciones.get(0);
         }
      }
      getVigenciaLocalizaciones();
      getVigenciasProrrateosCentroC();
      getVigenciasProrrateosProyectos();
   }

   /**
    * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarVL() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {

         if (listVLModificar.isEmpty()) {
            listVLModificar.add(vigenciaLocalizacionSeleccionada);
         } else if (!listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
            listVLModificar.add(vigenciaLocalizacionSeleccionada);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambiosVigencia = true;
   }

   public boolean validarFechasRegistro(int tipoAct) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (tipoAct == 0) {
         if (vigenciaLocalizacionSeleccionada.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (tipoAct == 1) {
         if (nuevaVigencia.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (tipoAct == 2) {
         if (duplicarVL.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarFechas(VigenciasLocalizaciones vLocalizacion, int c) {
      vigenciaLocalizacionSeleccionada = vLocalizacion;
      if (vigenciaLocalizacionSeleccionada.getFechavigencia() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistro(0);
         if (retorno) {
            cambiarIndice(vigenciaLocalizacionSeleccionada, c);
            modificarVL();
         } else {
            vigenciaLocalizacionSeleccionada.setFechavigencia(fechaVigencia);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaLocalizacionSeleccionada.setFechavigencia(fechaVigencia);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla
    * VigenciasLocalizaciones de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarVL(VigenciasLocalizaciones vLocalizacion, String confirmarCambio) {

      vigenciaLocalizacionSeleccionada = vLocalizacion;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaLocalizacionSeleccionada.getLocalizacion().getCentrocosto().setCodigoNombre(centroCosto);

         for (int i = 0; i < listEstructurasCC.size(); i++) {
            if (listEstructurasCC.get(i).getCentrocosto().getCodigoNombre().startsWith(vigenciaLocalizacionSeleccionada.getLocalizacion().getCentrocosto().getCodigoNombre().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaLocalizacionSeleccionada.setLocalizacion(listEstructurasCC.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:LocalizacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("MOTIVOLOCALIZACION")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaLocalizacionSeleccionada.getMotivo().setDescripcion(motivoLocalizacion);

         for (int i = 0; i < listMotivosLocalizaciones.size(); i++) {
            if (listMotivosLocalizaciones.get(i).getDescripcion().startsWith(vigenciaLocalizacionSeleccionada.getMotivo().getDescripcion().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaLocalizacionSeleccionada.setMotivo(listMotivosLocalizaciones.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:MotivoDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!vigenciaLocalizacionSeleccionada.getProyecto().getNombreproyecto().isEmpty()) {
            vigenciaLocalizacionSeleccionada.getProyecto().setNombreproyecto(proyecto);

            for (int i = 0; i < listProyectos.size(); i++) {
               if (listProyectos.get(i).getNombreproyecto().startsWith(vigenciaLocalizacionSeleccionada.getProyecto().getNombreproyecto().toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigenciaLocalizacionSeleccionada.setProyecto(listProyectos.get(indiceUnicoElemento));
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            vigenciaLocalizacionSeleccionada.setProyecto(new Proyectos());

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigencia = true;
         }
      }
      if (coincidencias == 1) {
         if (!listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {

            if (listVLModificar.isEmpty()) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            } else if (!listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosVigencia = true;

      }
      RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateo que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarVPPorcentaje(String valor, int indice, int nTabla, int subPorcent) {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      tipoActualizacion = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      try {
         if (nTabla == 1) {
            BigDecimal porcentajePrueba = new BigDecimal(valor);
            if (subPorcent == 1) {
               vigenciasProrrateosCentroC.get(indice).setSubporcentaje(porcentajePrueba.toBigInteger());
            } else {
               vigenciasProrrateosCentroC.get(indice).setPorcentaje(porcentajePrueba);
            }
            modificarVP();
            if (porcentajePrueba.intValueExact() < 0 || porcentajePrueba.intValueExact() > 100) {
               RequestContext.getCurrentInstance().update("form:validarPorcentajes");
               RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()");
            }

            vPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPPorcentaje");
            vPPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVPVigencia");
         }
         if (nTabla == 2) {
            int porcentajePrueba = Integer.parseInt(valor);
            vigenciasProrrateosProyectos.get(indice).setPorcentaje(porcentajePrueba);
            modificarVPP();
            if (porcentajePrueba < 0 || porcentajePrueba > 100) {
               RequestContext.getCurrentInstance().update("form:validarPorcentajes");
               RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()");
            }

            vPPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPPorcentaje");
            vPPPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
         }

      } catch (NumberFormatException e) {
         RequestContext.getCurrentInstance().update("form:validarPorcentajes");
         RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()");
      }

   }

   /*
     * public void modificarVPPorcentajes(int tipoAct, int nTabla, String valor,
     * int subPorcent) { activarLOV = true;
     * RequestContext.getCurrentInstance().update("form:listaValores");
     * tipoActualizacion = tipoAct; RequestContext context =
     * RequestContext.getCurrentInstance(); try { System.out.println("entro al
     * try"); if (nTabla == 1) { BigDecimal porcentajePrueba = new
     * BigDecimal(valor); if (tipoActualizacion == 1) { if (subPorcent == 1) {
     * nuevaVigenciaP.setSubporcentaje(porcentajePrueba.toBigInteger()); } else
     * { nuevaVigenciaP.setPorcentaje(porcentajePrueba); } } else if
     * (tipoActualizacion == 2) { if (subPorcent == 1) {
     * duplicarVP.setPorcentaje(porcentajePrueba); } else {
     * duplicarVP.setPorcentaje(porcentajePrueba); } } if
     * (porcentajePrueba.intValueExact() > 100 ||
     * porcentajePrueba.intValueExact() < 0) {
     * RequestContext.getCurrentInstance().update("form:validarPorcentajes");
     * RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()"); } } if (nTabla == 2) { int
     * porcentajePrueba = Integer.parseInt(valor); if (tipoActualizacion == 1) {
     * nuevaVigenciaPP.setPorcentaje(porcentajePrueba); } else if
     * (tipoActualizacion == 2) { } duplicarVPP.setPorcentaje(porcentajePrueba);
     * if (porcentajePrueba > 100 || porcentajePrueba < 0) {
     * RequestContext.getCurrentInstance().update("form:validarPorcentajes");
     * RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()"); } } if (nTabla == 1) { if
     * (tipoActualizacion == 1) {
     * RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCostoVP"); } else if
     * (tipoActualizacion == 2) {
     * RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoCentroCostoVP"); } } else if
     * (nTabla == 2) { if (tipoActualizacion == 1) {
     * RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVP"); } else if
     * (tipoActualizacion == 2) {
     * RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProyectoVP"); } } } catch
     * (NumberFormatException e) { RequestContext.getCurrentInstance().update("form:validarPorcentajes");
     * RequestContext.getCurrentInstance().execute("PF('validarPorcentajes').show()"); }
     *
     * }
    */

   public void modificarVP() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listVPCrear.contains(vigenciaProrrateoSeleccionada)) {

         if (listVPModificar.isEmpty()) {
            listVPModificar.add(vigenciaProrrateoSeleccionada);
         } else if (!listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
            listVPModificar.add(vigenciaProrrateoSeleccionada);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambioVigenciaP = true;
   }

   public boolean validarFechasRegistroVigenciaProrrateo(int tipoAct) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (tipoAct == 0) {

         if (vigenciaProrrateoSeleccionada.getFechafinal() != null) {
            if (vigenciaProrrateoSeleccionada.getFechainicial().after(fechaParametro) && vigenciaProrrateoSeleccionada.getFechainicial().before(vigenciaProrrateoSeleccionada.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (vigenciaProrrateoSeleccionada.getFechafinal() == null) {
            if (vigenciaProrrateoSeleccionada.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (tipoAct == 1) {
         if (nuevaVigenciaP.getFechafinal() != null) {
            if (nuevaVigenciaP.getFechainicial().after(fechaParametro) && nuevaVigenciaP.getFechainicial().before(nuevaVigenciaP.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (nuevaVigenciaP.getFechafinal() == null) {
            if (nuevaVigenciaP.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (tipoAct == 2) {
         if (duplicarVP.getFechafinal() != null) {
            if (duplicarVP.getFechainicial().after(fechaParametro) && duplicarVP.getFechainicial().before(duplicarVP.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (duplicarVP.getFechafinal() == null) {
            if (duplicarVP.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public void modificarFechasVigenciasProrrateos(VigenciasProrrateos vProrrateo, int c) {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaProrrateoSeleccionada = vProrrateo;

      if (vigenciaProrrateoSeleccionada.getFechainicial() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroVigenciaProrrateo(0);
         if (retorno) {
            cambiarIndiceVP(vigenciaProrrateoSeleccionada, c);
            modificarVP();
         } else {
            vigenciaProrrateoSeleccionada.setFechafinal(fechaFinVP);
            vigenciaProrrateoSeleccionada.setFechainicial(fechaIniVP);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVPVigencia");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaProrrateoSeleccionada.setFechainicial(fechaIniVP);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla VigenciaProrrateo
    * de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarVP(VigenciasProrrateos vProrrateos, String confirmarCambio) {
      vigenciaProrrateoSeleccionada = vProrrateos;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaProrrateoSeleccionada.getCentrocosto().setNombre(centroCostosVP);

         for (int i = 0; i < listCentrosCostos.size(); i++) {
            if (listCentrosCostos.get(i).getNombre().startsWith(vigenciaProrrateoSeleccionada.getCentrocosto().getNombre().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            cambioVigenciaP = true;
            vigenciaProrrateoSeleccionada.setCentrocosto(listCentrosCostos.get(indiceUnicoElemento));
         } else {
            permitirIndexVP = false;
            RequestContext.getCurrentInstance().update("form:CentroCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!vigenciaProrrateoSeleccionada.getProyecto().getNombreproyecto().isEmpty()) {
            vigenciaProrrateoSeleccionada.getProyecto().setNombreproyecto(proyectoVP);

            for (int i = 0; i < listProyectos.size(); i++) {
               if (listProyectos.get(i).getNombreproyecto().startsWith(vigenciaProrrateoSeleccionada.getProyecto().getNombreproyecto().toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cambioVigenciaP = true;
               vigenciaProrrateoSeleccionada.setProyecto(listProyectos.get(indiceUnicoElemento));
            } else {
               permitirIndexVP = false;
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            vigenciaProrrateoSeleccionada.setProyecto(new Proyectos());

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambioVigenciaP = true;
         }
      }
      if (coincidencias == 1) {
         if (!listVPCrear.contains(vigenciaProrrateoSeleccionada)) {

            if (listVPModificar.isEmpty()) {
               listVPModificar.add(vigenciaProrrateoSeleccionada);
            } else if (!listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
               listVPModificar.add(vigenciaProrrateoSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambioVigenciaP = true;
      }
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Modifica los elementos de la tabla VigenciaProrrateoProyectos que no usan
    * autocomplete
    *
    * @param indice Fila donde se efectu el cambio
    */
   public void modificarVPP() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listVPPCrear.contains(vigenciaProrrateoProyectoSeleccionada)) {

         if (listVPPModificar.isEmpty()) {
            listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
         } else if (!listVPPModificar.contains(vigenciaProrrateoProyectoSeleccionada)) {
            listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambioVigenciaPP = true;
   }

   public boolean validarFechasRegistroVigenciaProrrateoProyecto(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {

         if (vigenciaProrrateoProyectoSeleccionada.getFechafinal() != null) {
            if (vigenciaProrrateoProyectoSeleccionada.getFechainicial().after(fechaParametro) && vigenciaProrrateoProyectoSeleccionada.getFechainicial().before(vigenciaProrrateoProyectoSeleccionada.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (vigenciaProrrateoProyectoSeleccionada.getFechafinal() == null) {
            if (vigenciaProrrateoProyectoSeleccionada.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 1) {
         if (nuevaVigenciaPP.getFechafinal() != null) {
            if (nuevaVigenciaPP.getFechainicial().after(fechaParametro) && nuevaVigenciaPP.getFechainicial().before(nuevaVigenciaPP.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (nuevaVigenciaPP.getFechafinal() == null) {
            if (nuevaVigenciaPP.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 2) {
         if (duplicarVPP.getFechafinal() != null) {
            if (duplicarVPP.getFechainicial().after(fechaParametro) && duplicarVPP.getFechainicial().before(duplicarVPP.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (duplicarVPP.getFechafinal() == null) {
            if (duplicarVPP.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public void modificarFechasVigenciasProrrateosProyecto(VigenciasProrrateosProyectos vProrrateosProyectos, int c) {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaProrrateoProyectoSeleccionada = vProrrateosProyectos;

      if (vigenciaProrrateoProyectoSeleccionada.getFechainicial() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroVigenciaProrrateoProyecto(0);
         if (retorno) {
            cambiarIndiceVPP(vigenciaProrrateoProyectoSeleccionada, c);
            modificarVPP();
         } else {
            vigenciaProrrateoProyectoSeleccionada.setFechafinal(fechaFinVPP);
            vigenciaProrrateoProyectoSeleccionada.setFechainicial(fechaIniVPP);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaProrrateoProyectoSeleccionada.setFechainicial(fechaIniVPP);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla
    * VigenciaProrrateoProyectos de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarVPP(VigenciasProrrateosProyectos vProrrateosProyectos, String confirmarCambio) {
      vigenciaProrrateoProyectoSeleccionada = vProrrateosProyectos;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         vigenciaProrrateoProyectoSeleccionada.getProyecto().setNombreproyecto(proyectoVPP);

         for (int i = 0; i < listProyectos.size(); i++) {
            if (listProyectos.get(i).getNombreproyecto().startsWith(vigenciaProrrateoProyectoSeleccionada.getProyecto().getNombreproyecto().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            cambioVigenciaPP = true;
            vigenciaProrrateoProyectoSeleccionada.setProyecto(listProyectos.get(indiceUnicoElemento));
         } else {
            permitirIndexVPP = false;
            RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVPPCrear.contains(vigenciaProrrateoProyectoSeleccionada)) {

            if (listVPPModificar.isEmpty()) {
               listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
            } else if (!listVPPModificar.contains(vigenciaProrrateoProyectoSeleccionada)) {
               listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambioVigenciaPP = true;
         //vigenciaProrrateoProyectoSeleccionada = null;
      }
      RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaLocalizacion)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CENTROCOSTO")) {
         if (tipoNuevo == 1) {
            centroCosto = nuevaVigencia.getLocalizacion().getCentrocosto().getCodigoNombre();
         } else if (tipoNuevo == 2) {
            centroCosto = duplicarVL.getLocalizacion().getCentrocosto().getCodigoNombre();
         }
      } else if (Campo.equals("MOTIVOLOCALIZACION")) {
         if (tipoNuevo == 1) {
            motivoLocalizacion = nuevaVigencia.getMotivo().getDescripcion();
         } else if (tipoNuevo == 2) {
            motivoLocalizacion = duplicarVL.getMotivo().getDescripcion();
         }
      } else if (Campo.equals("PROYECTO")) {
         if (tipoNuevo == 1) {
            proyecto = nuevaVigencia.getProyecto().getNombreproyecto();
         } else if (tipoNuevo == 2) {
            proyecto = duplicarVL.getProyecto().getNombreproyecto();
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
   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         if (tipoNuevo == 1) {
            nuevaVigencia.getLocalizacion().getCentrocosto().setCodigoNombre(centroCosto);
         } else if (tipoNuevo == 2) {
            duplicarVL.getLocalizacion().getCentrocosto().setCodigoNombre(centroCosto);
         }
         for (int i = 0; i < listEstructurasCC.size(); i++) {
            if (listEstructurasCC.get(i).getCentrocosto().getCodigoNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setLocalizacion(listEstructurasCC.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCosto");
            } else if (tipoNuevo == 2) {
               duplicarVL.setLocalizacion(listEstructurasCC.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCentroCosto");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:LocalizacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCosto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCentroCosto");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("MOTIVOLOCALIZACION")) {
         if (tipoNuevo == 1) {
            nuevaVigencia.getMotivo().setDescripcion(motivoLocalizacion);
         } else if (tipoNuevo == 2) {
            duplicarVL.getMotivo().setDescripcion(motivoLocalizacion);
         }
         for (int i = 0; i < listMotivosLocalizaciones.size(); i++) {
            if (listMotivosLocalizaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setMotivo(listMotivosLocalizaciones.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivo");
            } else if (tipoNuevo == 2) {
               duplicarVL.setMotivo(listMotivosLocalizaciones.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:MotivoDialogo");
            RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaVigencia.getProyecto().setNombreproyecto(proyecto);
            } else if (tipoNuevo == 2) {
               duplicarVL.getProyecto().setNombreproyecto(proyecto);
            }
            for (int i = 0; i < listProyectos.size(); i++) {
               if (listProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigencia.setProyecto(listProyectos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyecto");
               } else if (tipoNuevo == 2) {
                  duplicarVL.setProyecto(listProyectos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyecto");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyecto");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyecto");
               }
            }
         }
      } else if (tipoNuevo == 1) {
         nuevaVigencia.setProyecto(new Proyectos());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyecto");
      } else if (tipoNuevo == 2) {
         duplicarVL.setProyecto(new Proyectos());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyecto");
      }
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaProrrateo)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletarVP(int tipoNuevo, String Campo) {
      if (Campo.equals("CENTROCOSTO")) {
         if (tipoNuevo == 1) {
            centroCostosVP = nuevaVigenciaP.getCentrocosto().getNombre();
         } else if (tipoNuevo == 2) {
            centroCostosVP = duplicarVP.getCentrocosto().getNombre();
         }
      } else if (Campo.equals("PROYECTO")) {
         if (tipoNuevo == 1) {
            proyectoVP = nuevaVigenciaP.getProyecto().getNombreproyecto();
         } else if (tipoNuevo == 2) {
            proyectoVP = duplicarVP.getProyecto().getNombreproyecto();
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
   public void autocompletarNuevoyDuplicadoVP(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaP.getCentrocosto().setNombre(centroCostosVP);
         } else if (tipoNuevo == 2) {
            duplicarVP.getCentrocosto().setNombre(centroCostosVP);
         }
         for (int i = 0; i < listCentrosCostos.size(); i++) {
            if (listCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaP.setCentrocosto(listCentrosCostos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCostoVP");
            } else if (tipoNuevo == 2) {
               duplicarVP.setCentrocosto(listCentrosCostos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoCentroCostoVP");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:CentroCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCentroCostoVP");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoCentroCostoVP");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaVigenciaP.getProyecto().setNombreproyecto(proyecto);
            } else if (tipoNuevo == 2) {
               duplicarVP.getProyecto().setNombreproyecto(proyecto);
            }
            for (int i = 0; i < listProyectos.size(); i++) {
               if (listProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigenciaP.setProyecto(listProyectos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVP");
               } else if (tipoNuevo == 2) {
                  duplicarVP.setProyecto(listProyectos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProyectoVP");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVP");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProyectoVP");
               }
            }
         }
      } else if (tipoNuevo == 1) {
         nuevaVigenciaP.setProyecto(new Proyectos());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVP");
      } else if (tipoNuevo == 2) {
         duplicarVP.setProyecto(new Proyectos());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProyectoVP");
      }
   }

   /**
    * Metodo que obtiene los valores de los dialogos para realizar los
    * autocomplete de los campos (VigenciaProrrateoProyecto)
    *
    * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
    * @param Campo Campo que toma el cambio de autocomplete
    */
   public void valoresBackupAutocompletarVPP(int tipoNuevo, String Campo) {
      if (Campo.equals("PROYECTO")) {
         if (tipoNuevo == 1) {
            proyectoVPP = nuevaVigenciaPP.getProyecto().getNombreproyecto();
         } else if (tipoNuevo == 2) {
            proyectoVPP = duplicarVPP.getProyecto().getNombreproyecto();
         }
      }
   }

   /**
    *
    * Metodo que genera el auto completar de un proceso nuevoRegistro o
    * duplicarRegistro de VigenciaProrrateoProyecto
    *
    * @param confirmarCambio Tipo de elemento a modificar: PROYECTO
    * @param valorConfirmar Valor ingresado para confirmar
    * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
    */
   public void autocompletarNuevoyDuplicadoVPP(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaPP.getProyecto().setNombreproyecto(proyecto);
         } else if (tipoNuevo == 2) {
            duplicarVPP.getProyecto().setNombreproyecto(proyecto);
         }
         for (int i = 0; i < listProyectos.size(); i++) {
            if (listProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaPP.setProyecto(listProyectos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVPP");
            } else if (tipoNuevo == 2) {
               duplicarVPP.setProyecto(listProyectos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyectoVPP");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProyectoVPP");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyectoVPP");
            }
         }
      }
   }

   public void posicionLocalizacion() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      cambiarIndice(vigenciaLocalizacionSeleccionada, columna);
   }

   public void cambiarIndice(VigenciasLocalizaciones vLocalizacion, int celda) {
      FacesContext c = FacesContext.getCurrentInstance();
      RequestContext context = RequestContext.getCurrentInstance();
      vigenciaLocalizacionSeleccionada = vLocalizacion;

      if (permitirIndex) {
         if ((cambioVigenciaP == false) && (cambioVigenciaPP == false)) {
            cualCelda = celda;

            if (cualCelda == 0) {
               activarLOV = true;
            } else if (cualCelda == 1) {
               activarLOV = false;
               centroCosto = vigenciaLocalizacionSeleccionada.getLocalizacion().getCentrocosto().getCodigoNombre();
            } else if (cualCelda == 3) {
               activarLOV = false;
               motivoLocalizacion = vigenciaLocalizacionSeleccionada.getMotivo().getDescripcion();
            } else if (cualCelda == 4) {
               activarLOV = false;
               proyecto = vigenciaLocalizacionSeleccionada.getProyecto().getNombreproyecto();
            }
            RequestContext.getCurrentInstance().update("form:listaValores");

            vigenciasProrrateosCentroC = null;
            getVigenciasProrrateosCentroC();
            vigenciasProrrateosProyectos = null;
            getVigenciasProrrateosProyectos();
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalir').show()");
         }
      }

      if (banderaVP == 1) {
         restablecerTablaVP();
      }
      if (banderaVPP == 1) {
         restablecerTablaVPP();
      }
      contarRegistrosVP();
      contarRegistrosVPP();
      RequestContext.getCurrentInstance().update("form:informacionRegistroVP");
      RequestContext.getCurrentInstance().update("form:informacionRegistroVPP");
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
   }

   /**
    * Metodo que obtiene la posicion dentro de la tabla VigenciasProrrateos
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndiceVP(VigenciasProrrateos vProrrateos, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVPPCrear.isEmpty() && listVPPBorrar.isEmpty() && listVPPModificar.isEmpty()) {
         vigenciaProrrateoSeleccionada = vProrrateos;
         FacesContext c = FacesContext.getCurrentInstance();
         if (permitirIndexVP) {
            cualCeldaVP = celda;

            if (cambioVigenciaP == false) {
               if (cualCeldaVP == 0) {
                  activarLOV = false;
                  centroCostosVP = vigenciaProrrateoSeleccionada.getCentrocosto().getNombre();
               } else if (cualCelda == 4) {
                  activarLOV = false;
                  proyectoVP = vigenciaProrrateoSeleccionada.getProyecto().getNombreproyecto();
               } else {
                  activarLOV = true;
               }
               RequestContext.getCurrentInstance().update("form:listaValores");
            }
         }
         if (bandera == 1) {
            restablecerTablaVL();
            RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         }
         if (banderaVPP == 1) {
            restablecerTablaVPP();
         }
         vigenciaProrrateoProyectoSeleccionada = null;
         //recordarSeleccionVPP();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalir').show()");
      }
   }

   /**
    * Metodo que obtiene la posicion dentro de la tabla
    * VigenciasProrrateosProyectos
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndiceVPP(VigenciasProrrateosProyectos vProrrateosProyectos, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVPCrear.isEmpty() && listVPBorrar.isEmpty() && listVPModificar.isEmpty()) {
         vigenciaProrrateoProyectoSeleccionada = vProrrateosProyectos;
         FacesContext c = FacesContext.getCurrentInstance();
         if (permitirIndexVPP) {
            cualCeldaVPP = celda;
            if (cualCeldaVPP == 0) {
               activarLOV = false;
               proyectoVPP = vigenciaProrrateoProyectoSeleccionada.getProyecto().getNombreproyecto();
            } else {
               activarLOV = true;
            }
            RequestContext.getCurrentInstance().update("form:listaValores");
         }
         if (bandera == 1) {
            restablecerTablaVL();
            RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         }
         if (banderaVP == 1) {
            restablecerTablaVP();
         }
         vigenciaProrrateoSeleccionada = null;
         //recordarSeleccionVP();
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalir').show()");
      }
   }
   //GUARDAR

   public void guardarYSalir() {
      guardado = true;
      guardadoGeneral();
      salir();
   }

   public void cancelarYSalir() {
      guardado = true;
      cancelarModificacion();
      salir();
   }

   /**
    * Metodo de guardado general para la pagina
    */
   public void guardadoGeneral() {
      if (cambiosVigencia) {
         guardarCambiosVL();
      }
      if (cambioVigenciaP) {
         guardarCambiosVP();
      }
      if (cambioVigenciaPP) {
         guardarCambiosVPP();
      }
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina
    * VigenciasLocalizaciones
    */
   public void guardarCambiosVL() {
      if (cambiosVigencia) {
         cambiosVigencia = false;
         if (!listVLBorrar.isEmpty()) {
            for (int i = 0; i < listVLBorrar.size(); i++) {
               if (listVLBorrar.get(i).getProyecto().getSecuencia() == null) {
                  listVLBorrar.get(i).setProyecto(null);
               }
               if (listVLBorrar.get(i).getLocalizacion().getSecuencia() == null) {
                  listVLBorrar.get(i).setLocalizacion(null);
               }
               if (listVLBorrar.get(i).getMotivo().getSecuencia() == null) {
                  listVLBorrar.get(i).setLocalizacion(null);
               }
               administrarVigenciaLocalizacion.borrarVL(listVLBorrar.get(i));
            }
            listVLBorrar.clear();
         }
         if (!listVLCrear.isEmpty()) {
            for (int i = 0; i < listVLCrear.size(); i++) {
               if (listVLCrear.get(i).getProyecto().getSecuencia() == null) {
                  listVLCrear.get(i).setProyecto(null);
               }
               if (listVLCrear.get(i).getLocalizacion().getSecuencia() == null) {
                  listVLCrear.get(i).setLocalizacion(null);
               }
               if (listVLCrear.get(i).getMotivo().getSecuencia() == null) {
                  listVLCrear.get(i).setLocalizacion(null);
               }
               administrarVigenciaLocalizacion.crearVL(listVLCrear.get(i));
            }
            listVLCrear.clear();
         }
         if (!listVLModificar.isEmpty()) {
            administrarVigenciaLocalizacion.modificarVL(listVLModificar);
            listVLModificar.clear();
         }
         vigenciaLocalizaciones = null;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistrosVL();

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Localizacion con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         paraNuevaV = 0;
      }
      vigenciaLocalizacionSeleccionada = null;
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
    */
   public void guardarCambiosVP() {
      System.out.println("Entro en guardarCambiosVP()");
      if (cambioVigenciaP) {
         if (!listVPBorrar.isEmpty()) {
            for (int i = 0; i < listVPBorrar.size(); i++) {
               System.out.println("borrando elemento: " + listVPBorrar.get(i));

               if (listVPBorrar.get(i).getCentrocosto() == null) {
                  listVPBorrar.get(i).setCentrocosto(null);
               }
               if (listVPBorrar.get(i).getViglocalizacion().getProyecto() == null) {
                  listVPBorrar.get(i).getViglocalizacion().setProyecto(new Proyectos());
               }
               if (listVPBorrar.get(i).getProyecto().getNombreproyecto() == null) {
                  listVPBorrar.get(i).getProyecto().setNombreproyecto(new String());
               }
               if (listVPBorrar.get(i).getViglocalizacion().getProyecto().getNombreproyecto() == null) {
                  listVPBorrar.get(i).getViglocalizacion().getProyecto().setNombreproyecto(new String());
               }
               System.out.println("listVPBorrar.get(i).getViglocalizacion().getProyecto(): " + listVPBorrar.get(i).getViglocalizacion().getProyecto());
               System.out.println("listVPBorrar.get(i).getProyecto(): " + listVPBorrar.get(i).getProyecto());
               administrarVigenciaLocalizacion.borrarVP(listVPBorrar.get(i));
               System.out.println("salio del Delete");
            }
            listVPBorrar.clear();
         }
         if (!listVPCrear.isEmpty()) {
            for (int i = 0; i < listVPCrear.size(); i++) {
               if (listVPCrear.get(i).getProyecto() == null) {
                  listVPCrear.get(i).setProyecto(null);
               }
               if (listVPCrear.get(i).getCentrocosto() == null) {
                  listVPCrear.get(i).setCentrocosto(null);
               }
               if (listVPCrear.get(i).getViglocalizacion().getProyecto() == null) {
                  listVPCrear.get(i).getViglocalizacion().setProyecto(null);
               }
               administrarVigenciaLocalizacion.crearVP(listVPCrear.get(i));
            }
            listVPCrear.clear();
         }
         if (!listVPModificar.isEmpty()) {
            administrarVigenciaLocalizacion.modificarVP(listVPModificar);
            listVPModificar.clear();
         }
         vigenciasProrrateosCentroC = null;
         contarRegistrosVP();
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         paraNuevaV = 0;
         RequestContext context = RequestContext.getCurrentInstance();
         cambioVigenciaP = false;
         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Prorrateo Centro Costo con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   /**
    * Metodo que guarda los cambios efectuados en la pagina
    * VigenciasProrrateosProyectos
    */
   public void guardarCambiosVPP() {
      if (cambioVigenciaPP) {
         if (!listVPPBorrar.isEmpty()) {
            for (int i = 0; i < listVPPBorrar.size(); i++) {
               administrarVigenciaLocalizacion.borrarVPP(listVPPBorrar.get(i));
            }
            listVPPBorrar.clear();
         }
         if (!listVPPCrear.isEmpty()) {
            for (int i = 0; i < listVPPCrear.size(); i++) {
               if (listVPPCrear.get(i).getProyecto().getSecuencia() == null) {
                  listVPPCrear.get(i).setProyecto(null);
               }
               if (listVPPCrear.get(i).getVigencialocalizacion().getProyecto().getSecuencia() == null) {
                  listVPPCrear.get(i).getVigencialocalizacion().setProyecto(null);
               }

               administrarVigenciaLocalizacion.crearVPP(listVPPCrear.get(i));
            }
            listVPPCrear.clear();
         }
         if (!listVPPModificar.isEmpty()) {
            administrarVigenciaLocalizacion.modificarVPP(listVPPModificar);
            listVPPModificar.clear();
         }
         vigenciasProrrateosProyectos = null;
         contarRegistrosVPP();
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
         paraNuevaV = 0;
         cambioVigenciaPP = false;
         FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Prorrateo Proyecto con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restablecerTablaVL();
         //CERRAR FILTRADO
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      }
      if (banderaVP == 1) {
         restablecerTablaVP();
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      }
      if (banderaVPP == 1) {
         restablecerTablaVPP();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      }
      listVLBorrar.clear();
      listVPBorrar.clear();
      listVPPBorrar.clear();
      listVLCrear.clear();
      listVPCrear.clear();
      listVPPCrear.clear();
      listVLModificar.clear();
      listVPModificar.clear();
      listVPPModificar.clear();
      vigenciaLocalizacionSeleccionada = null;
      vigenciaProrrateoProyectoSeleccionada = null;
      vigenciaProrrateoSeleccionada = null;

      paraNuevaV = 0;
      vigenciaLocalizaciones = null;
      vigenciasProrrateosCentroC = null;
      vigenciasProrrateosProyectos = null;
      guardado = true;
      cambiosVigencia = false;
      cambioVigenciaP = false;
      cambioVigenciaPP = false;
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");

      getVigenciaLocalizaciones();
      getVigenciasProrrateosCentroC();
      getVigenciasProrrateosProyectos();
      contarRegistrosVL();
      contarRegistrosVP();
      contarRegistrosVPP();

      RequestContext.getCurrentInstance().update("form:informacionRegistroVL");
      RequestContext.getCurrentInstance().update("form:informacionRegistroVP");
      RequestContext.getCurrentInstance().update("form:informacionRegistroVPP");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
   }

   /**
    * Cancela las modifaciones de la tabla vigencias prorrateos
    */
   public void cancelarModificacionVP() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVP == 1) {
         restablecerTablaVP();
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      }
      listVPBorrar.clear();
      listVPCrear.clear();
      listVPModificar.clear();
      vigenciaProrrateoSeleccionada = null;
      paraNuevaV = 0;
      vigenciasProrrateosCentroC = null;
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      cambioVigenciaP = false;
   }

   /**
    * Cancela las modifaciones de la tabla vigencias prorrateos proyectos
    */
   public void cancelarModificacionVPP() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVPP == 1) {
         restablecerTablaVPP();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      }
      listVPPBorrar.clear();
      listVPPCrear.clear();
      listVPPModificar.clear();
      vigenciaProrrateoProyectoSeleccionada = null;
      paraNuevaV = 0;
      vigenciasProrrateosProyectos = null;
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      cambioVigenciaPP = false;
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vigenciaLocalizacionSeleccionada == null && vigenciaProrrateoSeleccionada == null && vigenciaProrrateoProyectoSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (vigenciaLocalizacionSeleccionada != null) {
            editarVL = vigenciaLocalizacionSeleccionada;

            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigencia");
               RequestContext.getCurrentInstance().execute("PF('editarFechaVigencia').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCosto");
               RequestContext.getCurrentInstance().execute("PF('editarCentroCosto').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarLocalizacion");
               RequestContext.getCurrentInstance().execute("PF('editarLocalizacion').show()");
               cualCelda = -1;
            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoD");
               RequestContext.getCurrentInstance().execute("PF('editarMotivoD').show()");
               cualCelda = -1;
            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarProyecto");
               RequestContext.getCurrentInstance().execute("PF('editarProyecto').show()");
               cualCelda = -1;
            }
         }
         if (vigenciaProrrateoSeleccionada != null) {
            editarVP = vigenciaProrrateoSeleccionada;

            if (cualCeldaVP == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoVP");
               RequestContext.getCurrentInstance().execute("PF('editarCentroCostoVP').show()");
               cualCeldaVP = -1;
            } else if (cualCeldaVP == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:sali");
               RequestContext.getCurrentInstance().execute("PF('editarPorcentajeVP').show()");
               cualCeldaVP = -1;
            } else if (cualCeldaVP == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVP').show()");
               cualCeldaVP = -1;
            } else if (cualCeldaVP == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVP').show()");
               cualCeldaVP = -1;
            } else if (cualCeldaVP == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarProyectoVP");
               RequestContext.getCurrentInstance().execute("PF('editarProyectoVP').show()");
               cualCeldaVP = -1;
            } else if (cualCeldaVP == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSubPorcentajeVP");
               RequestContext.getCurrentInstance().execute("PF('editarSubPorcentajeVP').show()");
               cualCeldaVP = -1;
            }
         }
         if (vigenciaProrrateoProyectoSeleccionada != null) {
            editarVPP = vigenciaProrrateoProyectoSeleccionada;

            if (cualCeldaVPP == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarProyectoVPP");
               RequestContext.getCurrentInstance().execute("PF('editarProyectoVPP').show()");
               cualCeldaVPP = -1;
            } else if (cualCeldaVPP == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeVPP");
               RequestContext.getCurrentInstance().execute("PF('editarPorcentajeVPP').show()");
               cualCeldaVPP = -1;
            } else if (cualCeldaVPP == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVPP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVPP').show()");
               cualCeldaVPP = -1;
            } else if (cualCeldaVPP == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVPP");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVPP').show()");
               cualCeldaVPP = -1;
            }
         }
      }
   }
   //CREAR VL

   /**
    * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
    */
   public void agregarNuevaVL() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaVigencia.getFechavigencia() != null && nuevaVigencia.getLocalizacion().getSecuencia() != null && nuevaVigencia.getMotivo().getSecuencia() != null) {
         int cont = 0;
         mensajeValidacion = "";
         for (int j = 0; j < vigenciaLocalizaciones.size(); j++) {
            if (nuevaVigencia.getFechavigencia().equals(vigenciaLocalizaciones.get(j).getFechavigencia())) {
               cont++;
            }
         }
         if (cont > 0) {
            mensajeValidacion = "FECHAS NO REPETIDAS";
            RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
            RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
         } else {
            FacesContext c = FacesContext.getCurrentInstance();
            if (validarFechasRegistro(1)) {
               cambiosVigencia = true;
               if (bandera == 1) {
                  restablecerTablaVL();
                  //CERRAR FILTRADO
                  RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
               }
               //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
               paraNuevaV++;
               nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);
               nuevaVigencia.setSecuencia(nuevaVSecuencia);
               nuevaVigencia.setEmpleado(empleado);
               listVLCrear.add(nuevaVigencia);
               vigenciaLocalizaciones.add(nuevaVigencia);
               contarRegistrosVL();
               vigenciaLocalizacionSeleccionada = vigenciaLocalizaciones.get(vigenciaLocalizaciones.indexOf(nuevaVigencia));
               activarLOV = true;
               RequestContext.getCurrentInstance().update("form:listaValores");
               nuevaVigencia = new VigenciasLocalizaciones();
               nuevaVigencia.setLocalizacion(new Estructuras());
               nuevaVigencia.getLocalizacion().setCentrocosto(new CentrosCostos());
               nuevaVigencia.setMotivo(new MotivosLocalizaciones());
               nuevaVigencia.setProyecto(new Proyectos());
               RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().update("form:NuevoRegistroVL");
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVL').hide()");
            } else {
               RequestContext.getCurrentInstance().update("errorFechaVL");
               RequestContext.getCurrentInstance().execute("PF('errorFechaVL').show()");
            }
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:negacionNuevaVL");
         RequestContext.getCurrentInstance().execute("PF('negacionNuevaVL').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    * Metodo que limpia las casillas de la nueva vigencia
    */
   public void limpiarNuevaVL() {
      nuevaVigencia = new VigenciasLocalizaciones();
      nuevaVigencia.setLocalizacion(new Estructuras());
      nuevaVigencia.getLocalizacion().setCentrocosto(new CentrosCostos());
      nuevaVigencia.setMotivo(new MotivosLocalizaciones());
      nuevaVigencia.setProyecto(new Proyectos());
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Agrega una nueva Vigencia Prorrateo
    */
   public void agregarNuevaVP() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaVigenciaP.getCentrocosto().getSecuencia() != null && nuevaVigenciaP.getPorcentaje() != null && nuevaVigenciaP.getFechainicial() != null) {
         int cont = 0;
         mensajeValidacion = "";
         for (int j = 0; j < vigenciasProrrateosCentroC.size(); j++) {
            if (nuevaVigenciaP.getFechainicial().equals(vigenciasProrrateosCentroC.get(j).getFechainicial())) {
               cont++;
            }
         }
         if (cont > 0) {
            mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
            RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
            RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
         } else if (validarFechasRegistroVigenciaProrrateo(1)) {
            cambioVigenciaP = true;
            //CERRAR FILTRADO 
            if (banderaVP == 1) {
               restablecerTablaVP();
               RequestContext.getCurrentInstance().update("form:datosVPVigencia");
            } //AGREGAR REGISTRO A LA LISTA VIGENCIAS
            paraNuevaV++;
            nuevaVSecuencia
                    = BigInteger.valueOf(paraNuevaV);
            nuevaVigenciaP.setSecuencia(nuevaVSecuencia);
            if (tipoLista == 0) {// Si NO tiene Filtro
               nuevaVigenciaP.setViglocalizacion(vigenciaLocalizaciones.get(indexAuxVL));
            }
            if (tipoLista == 1) {// Si tiene Filtro
               nuevaVigenciaP.setViglocalizacion(filtradoVL.get(indexAuxVL));
            }
            if (vigenciasProrrateosCentroC == null) {
               vigenciasProrrateosCentroC = new ArrayList<VigenciasProrrateos>();
            }
            listVPCrear.add(nuevaVigenciaP);
            vigenciasProrrateosCentroC.add(nuevaVigenciaP);
            contarRegistrosVP();
            vigenciaProrrateoSeleccionada = vigenciasProrrateosCentroC.get(vigenciasProrrateosCentroC.indexOf(nuevaVigenciaP));
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            nuevaVigenciaP = new VigenciasProrrateos();
            nuevaVigenciaP.setCentrocosto(new CentrosCostos());
            nuevaVigenciaP.setProyecto(new Proyectos());
            RequestContext.getCurrentInstance().update("form:datosVPVigencia");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVP').hide()");
            cualTabla = 0;
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            tipoActualizacion = -1;
         } else {
            RequestContext.getCurrentInstance().update("form:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:negacionNuevaVLP");
         RequestContext.getCurrentInstance().execute("PF('negacionNuevaVLP').show()");
      }
   }

   /**
    * Limpia los elementos de una nueva vigencia prorrateo
    */
   public void limpiarNuevaVP() {
      nuevaVigenciaP = new VigenciasProrrateos();
      nuevaVigenciaP.setCentrocosto(new CentrosCostos());
      nuevaVigenciaP.setProyecto(new Proyectos());
   }

   ///////////////////////////////////////////////////////////////////////////
   /**
    * Agrega una nueva vigencia prorrateo proyecto
    */
   public void agregarNuevaVPP() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaVigenciaPP.getFechainicial() != null && nuevaVigenciaPP.getPorcentaje() >= 0 && nuevaVigenciaPP.getProyecto().getSecuencia() != null) {
         int cont = 0;
         mensajeValidacion = "";
         for (int j = 0; j < vigenciasProrrateosProyectos.size(); j++) {
            if (nuevaVigenciaPP.getFechainicial().equals(vigenciasProrrateosProyectos.get(j).getFechainicial())) {
               cont++;
            }
         }
         if (cont > 0) {
            mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
            RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
            RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
         } else if (validarFechasRegistroVigenciaProrrateoProyecto(1)) {
            cambioVigenciaPP = true;
            //CERRAR FILTRADO
            if (banderaVPP == 1) {
               restablecerTablaVPP();
               RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            paraNuevaV++;
            nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);
            nuevaVigenciaPP.setSecuencia(nuevaVSecuencia);
            if (tipoLista == 0) {// Si NO tiene Filtro
               nuevaVigenciaPP.setVigencialocalizacion(vigenciaLocalizaciones.get(indexAuxVL));
            }
            if (tipoLista == 1) {// Si tiene Filtro
               nuevaVigenciaPP.setVigencialocalizacion(filtradoVL.get(indexAuxVL));
            }
            listVPPCrear.add(nuevaVigenciaPP);
            vigenciasProrrateosProyectos.add(nuevaVigenciaPP);
            contarRegistrosVPP();
            vigenciaProrrateoProyectoSeleccionada = vigenciasProrrateosProyectos.get(vigenciasProrrateosProyectos.indexOf(nuevaVigenciaPP));
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            nuevaVigenciaPP = new VigenciasProrrateosProyectos();
            nuevaVigenciaPP.setProyecto(new Proyectos());
            RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVPP').hide()");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:negacionNuevaVLPP");
         RequestContext.getCurrentInstance().execute("PF('negacionNuevaVLPP').show()");
      }
   }

   /**
    * Elimina los datos de una nueva vigencia prorrateo proyecto
    */
   public void limpiarNuevaVPP() {
      nuevaVigenciaPP = new VigenciasProrrateosProyectos();
      nuevaVigenciaPP.setProyecto(new Proyectos());
   }
   //DUPLICAR VL

   /**
    * Metodo que verifica que proceso de duplicar se genera con respecto a la
    * posicion en la pagina que se tiene
    */
   public void verificarDuplicarVigencia() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vigenciaLocalizacionSeleccionada == null && vigenciaProrrateoSeleccionada == null && vigenciaProrrateoProyectoSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (guardado) {
         tipoActualizacion = 2;
         if (vigenciaProrrateoSeleccionada != null) {
            duplicarVigenciaP();
         } else if (vigenciaProrrateoProyectoSeleccionada != null) {
            duplicarVigenciaPP();
         } else if (vigenciaLocalizacionSeleccionada != null) {
            duplicarVigenciaL();
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalir').show()");
      }
   }

   /**
    * Duplica una nueva vigencia localizacion
    */
   public void duplicarVigenciaL() {
      if (vigenciaLocalizacionSeleccionada != null) {
         duplicarVL = new VigenciasLocalizaciones();
         paraNuevaV++;
         nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);

         duplicarVL.setEmpleado(vigenciaLocalizacionSeleccionada.getEmpleado());
         duplicarVL.setFechavigencia(vigenciaLocalizacionSeleccionada.getFechavigencia());
         duplicarVL.setLocalizacion(vigenciaLocalizacionSeleccionada.getLocalizacion());
         duplicarVL.setMotivo(vigenciaLocalizacionSeleccionada.getMotivo());
         duplicarVL.setProyecto(vigenciaLocalizacionSeleccionada.getProyecto());

         if (duplicarVL.getMotivo() == null) {
            duplicarVL.setMotivo(new MotivosLocalizaciones());
         }
         if (duplicarVL.getProyecto() == null) {
            duplicarVL.setProyecto(new Proyectos());
         }
         if (duplicarVL.getLocalizacion() == null) {
            duplicarVL.setLocalizacion(new Estructuras());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVL");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVL').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasLocalizaciones
    */
   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      mensajeValidacion = "";
      for (int j = 0; j < vigenciaLocalizaciones.size(); j++) {
         if (duplicarVL.getFechavigencia().equals(vigenciaLocalizaciones.get(j).getFechavigencia())) {
            cont++;
         }
      }
      if (cont > 0) {
         mensajeValidacion = "FECHAS NO REPETIDAS";
         RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
         RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
      } else if (duplicarVL.getFechavigencia() != null && duplicarVL.getLocalizacion().getSecuencia() != null && duplicarVL.getMotivo().getSecuencia() != null) {
         if (validarFechasRegistro(2)) {
            paraNuevaV++;
            nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);
            duplicarVL.setSecuencia(nuevaVSecuencia);
            cambiosVigencia = true;
            vigenciaLocalizaciones.add(duplicarVL);
            listVLCrear.add(duplicarVL);
            contarRegistrosVL();
            vigenciaLocalizacionSeleccionada = vigenciaLocalizaciones.get(vigenciaLocalizaciones.indexOf(duplicarVL));
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
               restablecerTablaVL();
               //CERRAR FILTRADO
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVL.hide();");
            duplicarVL = new VigenciasLocalizaciones();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechaVL').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('negacionNuevaVL').show()");
      }
   }
//LIMPIAR DUPLICAR

   /**
    * Metodo que limpia los datos de un duplicar Vigencia Localizacion
    */
   public void limpiarduplicarVL() {
      duplicarVL = new VigenciasLocalizaciones();
      duplicarVL.setLocalizacion(new Estructuras());
      duplicarVL.getLocalizacion().setCentrocosto(new CentrosCostos());
      duplicarVL.setMotivo(new MotivosLocalizaciones());
      duplicarVL.setProyecto(new Proyectos());
   }

   ///////////////////////////////////////////////////////////////
   /**
    * Duplica una registro de VigenciaProrrateos
    */
   public void duplicarVigenciaP() {
      if (vigenciaProrrateoSeleccionada != null) {
         duplicarVP = new VigenciasProrrateos();

         duplicarVP.setCentrocosto(vigenciaProrrateoSeleccionada.getCentrocosto());
         duplicarVP.setViglocalizacion(vigenciaLocalizaciones.get(indexAuxVL));
         duplicarVP.setFechafinal(vigenciaProrrateoSeleccionada.getFechafinal());
         duplicarVP.setFechainicial(vigenciaProrrateoSeleccionada.getFechainicial());
         duplicarVP.setPorcentaje(vigenciaProrrateoSeleccionada.getPorcentaje());
         duplicarVP.setSubporcentaje(vigenciaProrrateoSeleccionada.getSubporcentaje());
         duplicarVP.setProyecto(vigenciaProrrateoSeleccionada.getProyecto());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoVP");
         RequestContext.getCurrentInstance().execute("PF('DuplicadoRegistroVP').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciaProrrateo
    */
   public void confirmarDuplicarVP() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      mensajeValidacion = "";
      for (int j = 0; j < vigenciasProrrateosCentroC.size(); j++) {
         if (duplicarVP.getFechainicial().equals(vigenciasProrrateosCentroC.get(j).getFechainicial())) {
            cont++;
         }
      }
      if (cont > 0) {
         mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
         RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
         RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
      } else if (duplicarVP.getCentrocosto().getSecuencia() != null && duplicarVP.getPorcentaje() == null && duplicarVP.getFechainicial() != null) {
         if (validarFechasRegistroVigenciaProrrateo(2)) {
            cambioVigenciaP = true;
            paraNuevaV++;
            nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);
            duplicarVP.setSecuencia(nuevaVSecuencia);
            vigenciasProrrateosCentroC.add(duplicarVP);
            listVPCrear.add(duplicarVP);
            contarRegistrosVP();
            RequestContext.getCurrentInstance().update("form:datosVPVigencia");
            RequestContext.getCurrentInstance().execute("PF('DuplicadoRegistroVP.hide();");
            vigenciaProrrateoSeleccionada = vigenciasProrrateosCentroC.get(vigenciasProrrateosCentroC.indexOf(duplicarVP));
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaVP == 1) {
               //CERRAR FILTRADO
               restablecerTablaVP();
               RequestContext.getCurrentInstance().update("form:datosVPVigencia");
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            duplicarVP = new VigenciasProrrateos();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show();");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegnew').show();");
      }
   }

   /**
    * Limpia los elementos del duplicar registro Vigencia Prorrateo
    */
   public void limpiarduplicarVP() {
      duplicarVP = new VigenciasProrrateos();
      duplicarVP.setCentrocosto(new CentrosCostos());
      duplicarVP.setProyecto(new Proyectos());
   }

   ///////////////////////////////////////////////////////////////
   /**
    * Duplica un registo Vigencia Prorrateo Proyecto
    */
   public void duplicarVigenciaPP() {
      if (vigenciaProrrateoProyectoSeleccionada != null) {
         duplicarVPP = new VigenciasProrrateosProyectos();

         duplicarVPP.setVigencialocalizacion(vigenciaLocalizaciones.get(indexAuxVL));
         duplicarVPP.setFechafinal(vigenciaProrrateoProyectoSeleccionada.getFechafinal());
         duplicarVPP.setFechainicial(vigenciaProrrateoProyectoSeleccionada.getFechainicial());
         duplicarVPP.setPorcentaje(vigenciaProrrateoProyectoSeleccionada.getPorcentaje());
         duplicarVPP.setProyecto(vigenciaProrrateoProyectoSeleccionada.getProyecto());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVPP");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVPP').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * Vigencia Prorrateo Proyecto
    */
   public void confirmarDuplicarVPP() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      mensajeValidacion = "";
      for (int j = 0; j < vigenciasProrrateosProyectos.size(); j++) {
         if (duplicarVPP.getFechainicial().equals(vigenciasProrrateosProyectos.get(j).getFechainicial())) {
            cont++;
         }
      }
      if (cont > 0) {
         mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
         RequestContext.getCurrentInstance().update("form:validarNuevoFechas");
         RequestContext.getCurrentInstance().execute("PF('validarNuevoFechas').show()");
      } else if (duplicarVPP.getFechainicial() != null && duplicarVPP.getPorcentaje() >= 0 && duplicarVPP.getProyecto().getSecuencia() != null) {
         if (validarFechasRegistroVigenciaProrrateoProyecto(2)) {
            paraNuevaV++;
            nuevaVSecuencia = BigInteger.valueOf(paraNuevaV);
            duplicarVPP.setSecuencia(nuevaVSecuencia);
            vigenciasProrrateosProyectos.add(duplicarVPP);
            listVPPCrear.add(duplicarVPP);
            contarRegistrosVPP();
            RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVPP').show()");
            vigenciaProrrateoProyectoSeleccionada = vigenciasProrrateosProyectos.get(vigenciasProrrateosProyectos.indexOf(duplicarVPP));
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaVPP == 1) {
               restablecerTablaVPP();
               RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            duplicarVPP = new VigenciasProrrateosProyectos();
            cambioVigenciaPP = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show();");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegnew').show();");
      }
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   /**
    * Limpia el registro de duplicar Vigencia Prorrateo Proyecto
    */
   public void limpiarduplicarVPP() {
      duplicarVPP = new VigenciasProrrateosProyectos();
      duplicarVPP.setProyecto(new Proyectos());
   }

   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void validarBorradoVigencia() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vigenciaLocalizacionSeleccionada == null && vigenciaProrrateoSeleccionada == null && vigenciaProrrateoProyectoSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         System.out.println("entro a validarBorrado");
         if (vigenciaProrrateoSeleccionada != null) {
            borrarVP2();
         } else if (vigenciaProrrateoProyectoSeleccionada != null) {
            borrarVPP();
         } else if (vigenciaLocalizacionSeleccionada != null) {
            int tam = 0;
            int tam2 = 0;
            if (vigenciasProrrateosProyectos != null) {
               tam = vigenciasProrrateosProyectos.size();
            }
            if (vigenciasProrrateosCentroC != null) {
               tam2 = vigenciasProrrateosCentroC.size();
            }
            if (tam == 0 && tam2 == 0) {
               borrarVL();
            } else {
               RequestContext.getCurrentInstance().update("form:negacionBorradoVL");
               RequestContext.getCurrentInstance().execute("PF('negacionBorradoVL').show()");
            }
         }
      }
   }
   //BORRAR VL

   /**
    * Metodo que borra una vigencia localizacion
    */
   public void borrarVL() {
      System.out.println("entro en: borrarVL()");
      if (vigenciaLocalizacionSeleccionada != null) {
         cambiosVigencia = true;
         if (!listVLModificar.isEmpty() && listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
            int modIndex = listVLModificar.indexOf(vigenciaLocalizacionSeleccionada);
            listVLModificar.remove(modIndex);
            listVLBorrar.add(vigenciaLocalizacionSeleccionada);
         } else if (!listVLCrear.isEmpty() && listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {
            int crearIndex = listVLCrear.indexOf(vigenciaLocalizacionSeleccionada);
            listVLCrear.remove(crearIndex);
         } else {
            listVLBorrar.add(vigenciaLocalizacionSeleccionada);
         }
         vigenciaLocalizaciones.remove(vigenciaLocalizacionSeleccionada);
         if (tipoLista == 1) {
            filtradoVL.remove(vigenciaLocalizacionSeleccionada);
         }
         contarRegistrosVL();
         vigenciaLocalizacionSeleccionada = null;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:informacionRegistroVL");
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   /**
    * Metodo que borra una vigencia prorrateo
    */
   public void borrarVP() {
      System.out.println("entro en: borrarVP()");
      cambioVigenciaP = true;
      System.out.println("vigenciaProrrateoSeleccionada: " + vigenciaProrrateoSeleccionada);
      if (!listVPModificar.isEmpty() && listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
         listVPModificar.remove(vigenciaProrrateoSeleccionada);
         listVPBorrar.add(vigenciaProrrateoSeleccionada);
      } else if (!listVPCrear.isEmpty() && listVPCrear.contains(vigenciaProrrateoSeleccionada)) {
         listVPCrear.remove(vigenciaProrrateoSeleccionada);
      } else {
         listVPBorrar.add(vigenciaProrrateoSeleccionada);
      }
      vigenciasProrrateosCentroC.remove(vigenciaProrrateoSeleccionada);
      if (tipoLista == 1) {
         filtradoVP.remove(vigenciaProrrateoSeleccionada);
      }
      System.out.println("lista borrado VP poss 0: " + listVPBorrar.get(0));
      contarRegistrosVP();
      activarLOV = true;
      vigenciaProrrateoSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:informacionRegistroVP");
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrarVP2() {
      cambioVigenciaP = true;
      if (!listVPModificar.isEmpty() && listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
         listVPModificar.remove(vigenciaProrrateoSeleccionada);
         listVPBorrar.add(vigenciaProrrateoSeleccionada);
      } else if (!listVPCrear.isEmpty() && listVPCrear.contains(vigenciaProrrateoSeleccionada)) {
         listVPCrear.remove(vigenciaProrrateoSeleccionada);
      } else {
         listVPBorrar.add(vigenciaProrrateoSeleccionada);
      }
      vigenciasProrrateosCentroC.remove(vigenciaProrrateoSeleccionada);
      if (tipoLista == 1) {
         filtradoVP.remove(vigenciaProrrateoSeleccionada);
      }
      contarRegistrosVP();
      activarLOV = true;
      vigenciaProrrateoSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext.getCurrentInstance().update("form:informacionRegistroVP");
      RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   /**
    * Metodo que borra una vigencia prorrateo proyecto
    */
   public void borrarVPP() {
      System.out.println("entro en: borrarVPP()");
      cambioVigenciaPP = true;
      if (!listVPPModificar.isEmpty() && listVPPModificar.contains(vigenciaProrrateoProyectoSeleccionada)) {
         listVPPModificar.remove(vigenciaProrrateoProyectoSeleccionada);
         listVPPBorrar.add(vigenciaProrrateoProyectoSeleccionada);
      } else if (!listVPPCrear.isEmpty() && listVPPCrear.contains(vigenciaProrrateoProyectoSeleccionada)) {
         listVPPCrear.remove(vigenciaProrrateoProyectoSeleccionada);
      } else {
         listVPPBorrar.add(vigenciaProrrateoProyectoSeleccionada);
      }
      vigenciasProrrateosProyectos.remove(vigenciaProrrateoProyectoSeleccionada);
      if (tipoLista == 1) {
         filtradoPP.remove(vigenciaProrrateoProyectoSeleccionada);
      }
      System.out.println("lista borrado VPP poss 0: " + listVPPBorrar.get(0));
      contarRegistrosVPP();
      activarLOV = true;
      vigenciaProrrateoProyectoSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:informacionRegistroVPP");
      RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (vigenciaProrrateoSeleccionada != null) {
         filtradoVigenciaProrrateo();
         vigenciaProrrateoSeleccionada = null;
      } else if (vigenciaProrrateoProyectoSeleccionada != null) {
         filtradoVigenciaProrrateoProyecto();
         vigenciaProrrateoProyectoSeleccionada = null;
      } else if (vigenciaLocalizacionSeleccionada != null) {
         filtradoVigenciaLocalizacion();
         vigenciaLocalizacionSeleccionada = null;
      }
   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia localizacion
    */
   public void filtradoVigenciaLocalizacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         vlFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlFechaVigencia");
         vlFechaVigencia.setFilterStyle("width: 80%");
         vlCentroCosto = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlCentroCosto");
         vlCentroCosto.setFilterStyle("width: 80%");
         vlLocalizacion = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlLocalizacion");
         vlLocalizacion.setFilterStyle("width: 80%");
         vlMotivo = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlMotivo");
         vlMotivo.setFilterStyle("width: 80%");
         vlProyecto = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlProyecto");
         vlProyecto.setFilterStyle("width: 80%");
         altoTabla1 = "84";
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         bandera = 1;
      } else if (bandera == 1) {
         restablecerTablaVL();
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      }
   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia prorrateo
    */
   public void filtradoVigenciaProrrateo() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVP == 0) {
         //Columnas Tabla VPP
         vPCentroCosto = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPCentroCosto");
         vPCentroCosto.setFilterStyle("width: 80%");
         vPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPPorcentaje");
         vPPorcentaje.setFilterStyle("width: 80%");
         vPFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPFechaInicial");
         vPFechaInicial.setFilterStyle("width: 80%");
         vPFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPFechaFinal");
         vPFechaFinal.setFilterStyle("width: 80%");
         vPProyecto = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPProyecto");
         vPProyecto.setFilterStyle("width: 80%");
         vPSubPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPSubPorcentaje");
         vPSubPorcentaje.setFilterStyle("width: 80%");
         altoTabla2 = "84";
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
         banderaVP = 1;
      } else if (banderaVP == 1) {
         restablecerTablaVP();
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
      }
   }

   /**
    * Metodo que acciona el filtrado de la tabla vigencia prorrateo proyecto
    */
   public void filtradoVigenciaProrrateoProyecto() {
      FacesContext c = FacesContext.getCurrentInstance();
      //Columnas Tabla VPP
      if (banderaVPP == 0) {
         vPPProyecto = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPProyecto");
         vPPProyecto.setFilterStyle("width: 80%");
         vPPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPPorcentaje");
         vPPPorcentaje.setFilterStyle("width: 80%");
         vPPFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPFechaInicial");
         vPPFechaInicial.setFilterStyle("width: 80%");
         vPPFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPFechaFinal");
         vPPFechaFinal.setFilterStyle("width: 80%");
         altoTabla3 = "84";
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
         banderaVPP = 1;
      } else if (banderaVPP == 1) {
         restablecerTablaVPP();
         RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
      }

   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (bandera == 1) {
         restablecerTablaVL();
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      }

      listVLBorrar.clear();
      listVLCrear.clear();
      listVLModificar.clear();
      vigenciaLocalizacionSeleccionada = null;
      vigenciaProrrateoSeleccionada = null;
      vigenciaProrrateoProyectoSeleccionada = null;
      paraNuevaV = 0;
      vigenciaLocalizaciones = null;
      listCentrosCostos = null;
      listProyectos = null;
      listMotivosLocalizaciones = null;
      listEstructurasCC = null;
      guardado = true;
      vigenciasProrrateosCentroC = null;
      vigenciasProrrateosProyectos = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   /**
    * Metodo que ejecuta los dialogos de estructuras, motivos localizaciones,
    * proyectos
    *
    * @param indice Fila de la tabla
    * @param dlg Dialogo
    */
   public void asignarIndex(VigenciasLocalizaciones vLocalizacion, int dlg, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      if (tipoAct == 0) {
         vigenciaLocalizacionSeleccionada = vLocalizacion;
      }
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (dlg == 0) {
         //Estructuras
         contarRegistrosEstrLoca();
         RequestContext.getCurrentInstance().update("form:LocalizacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').show()");
      } else if (dlg == 1) {
         //MotivosLocalizaciones
         contarRegistrosMotiLoc();
         RequestContext.getCurrentInstance().update("form:MotivoDialogo");
         RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
      } else if (dlg == 2) {
         //Proyectos
         contarRegistrosProyecto();
         RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
      }
   }

   public void asignarIndexVP(VigenciasProrrateos vProrrateo, int dlg, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      if (tipoAct == 0) {
         vigenciaProrrateoSeleccionada = vProrrateo;
      }

      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (dlg == 0) {
         //Centro Costos
         contarRegistrosCentroC();
         RequestContext.getCurrentInstance().update("form:CentroCostosDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').show()");
      } else if (dlg == 1) {
         //Proyectos
         contarRegistrosProyecto();
         RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
      }
   }

   public void asignarIndexVPP(VigenciasProrrateosProyectos vProrrateosProyectos, int dlg, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      if (tipoAct == 0) {
         vigenciaProrrateoProyectoSeleccionada = vProrrateosProyectos;
      }
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
      if (dlg == 0) {
         //Proyectos
         contarRegistrosProyecto();
         RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
      }
   }

   //LOVS
   //Estructuras
   /**
    * Metodo que actualiza la estructura seleccionada (vigencia localizacion)
    */
   public void actualizarEstructura() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {//// Si se trabaja sobre la tabla y no sobre un dialogo
         vigenciaLocalizacionSeleccionada.setLocalizacion(estructuraSelecionada);
         if (!listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {
            if (listVLModificar.isEmpty()) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            } else if (!listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosVigencia = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      } else if (tipoActualizacion == 1) {// Para crear registro
         nuevaVigencia.setLocalizacion(estructuraSelecionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVL");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarVL.setLocalizacion(estructuraSelecionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVL");
      }
      filtradoEstructuraCC = null;
      estructuraSelecionada = null;
      aceptar = true;
      tipoActualizacion = -1;

      context.reset("form:lovLocalizacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovLocalizacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovLocalizacion");
   }

   /**
    * Metodo que cancela los cambios sobre estructura (vigencia localizacion)
    */
   public void cancelarCambioEstructura() {
      filtradoEstructuraCC = null;
      estructuraSelecionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovLocalizacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovLocalizacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovLocalizacion");
   }

   //Motivo Localizacion
   /**
    * Metodo que actualiza el motivo localizacion seleccionado (vigencia
    * localizacion)
    */
   public void actualizarMotivoLocalizacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
         vigenciaLocalizacionSeleccionada.setMotivo(motivoLocalizacionSelecionado);
         if (!listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {
            if (listVLModificar.isEmpty()) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            } else if (!listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
               listVLModificar.add(vigenciaLocalizacionSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosVigencia = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
      } else if (tipoActualizacion == 1) {// Para cerar registro
         nuevaVigencia.setMotivo(motivoLocalizacionSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVL");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarVL.setMotivo(motivoLocalizacionSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVL");
      }
      filtradoMotivosLocalizaciones = null;
      motivoLocalizacionSelecionado = new MotivosLocalizaciones();
      aceptar = true;
      tipoActualizacion = -1;

      context.reset("form:lovMotivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovMotivo");
   }

   /**
    * Metodo que cancela la seleccion del motivo localizacion (vigencia
    * localizacion)
    */
   public void cancelarCambioMotivoLocalizacion() {
      filtradoMotivosLocalizaciones = null;
      motivoLocalizacionSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovMotivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovMotivo");
   }

   //Motivo Localizacion
   /**
    * Metodo que actualiza el proyecto seleccionado (vigencia localizacion)
    */
   public void actualizarProyecto() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (tipoActualizacion == 1) {// Para crear registro

         if (cualTabla == 1) {
            nuevaVigencia.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVL");
         } else if (cualTabla == 2) {
            nuevaVigenciaP.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVP");
         } else if (cualTabla == 3) {
            nuevaVigenciaPP.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVPP");
         }

      } else if (vigenciaProrrateoSeleccionada != null) {
         if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaProrrateoSeleccionada.setProyecto(proyectoSelecionado);
            if (!listVPCrear.contains(vigenciaProrrateoSeleccionada)) {
               if (listVPModificar.isEmpty()) {
                  listVPModificar.add(vigenciaProrrateoSeleccionada);
               } else if (!listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
                  listVPModificar.add(vigenciaProrrateoSeleccionada);
               }
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambioVigenciaP = true;
            permitirIndexVP = true;
            RequestContext.getCurrentInstance().update("form:datosVPVigencia");
         } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVP.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVP");
         }
         filtradoProyectos = null;
         proyectoSelecionado = new Proyectos();
         aceptar = true;
         tipoActualizacion = -1;

         context.reset("form:lovProyectos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
         RequestContext.getCurrentInstance().update("form:lovProyectos");

         //////////////////////////////////////////////
      } else if (vigenciaProrrateoProyectoSeleccionada != null) {
         if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaProrrateoProyectoSeleccionada.setProyecto(proyectoSelecionado);
            if (!listVPPCrear.contains(vigenciaProrrateoProyectoSeleccionada)) {
               if (listVPPModificar.isEmpty()) {
                  listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
               } else if (!listVPPModificar.contains(vigenciaProrrateoProyectoSeleccionada)) {
                  listVPPModificar.add(vigenciaProrrateoProyectoSeleccionada);
               }
            }

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexVPP = true;
            cambioVigenciaPP = true;
            RequestContext.getCurrentInstance().update("form:datosVPPVigencia");
         } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVPP.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVPP");
         }
         filtradoProyectos = null;
         proyectoSelecionado = new Proyectos();
         aceptar = true;
         tipoActualizacion = -1;

         context.reset("form:lovProyectos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
         RequestContext.getCurrentInstance().update("form:lovProyectos");

         ///////////////////////////////////////////////////////////////    
      } else if (vigenciaLocalizacionSeleccionada != null) {
         if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaLocalizacionSeleccionada.setProyecto(proyectoSelecionado);
            if (!listVLCrear.contains(vigenciaLocalizacionSeleccionada)) {
               if (listVLModificar.isEmpty()) {
                  listVLModificar.add(vigenciaLocalizacionSeleccionada);
               } else if (!listVLModificar.contains(vigenciaLocalizacionSeleccionada)) {
                  listVLModificar.add(vigenciaLocalizacionSeleccionada);
               }
            }

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigencia = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVLEmpleado");
         } else if (tipoActualizacion == 2) {// Para duplicar registro
            duplicarVL.setProyecto(proyectoSelecionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVL");
         }
         filtradoProyectos = null;
         proyectoSelecionado = new Proyectos();
         aceptar = true;
         tipoActualizacion = -1;
         context.reset("form:lovProyectos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
         RequestContext.getCurrentInstance().update("form:lovProyectos");
      }
   }

   /**
    * Metodo que cancela la seleccion del proyecto (vigencia localizacion)
    */
   public void cancelarCambioProyecto() {
      filtradoProyectos = null;
      proyectoSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProyectos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovProyectos");
   }

   ///////////////////////////////////////////////////////////////////////
   /**
    * Metodo que actualiza el centro costo seleccionado (vigencia prorrateo)
    */
   public void actualizarCentroCostoVP() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
         vigenciaProrrateoSeleccionada.setCentrocosto(centroCostoSeleccionado);
         if (!listVPCrear.contains(vigenciaProrrateoSeleccionada)) {
            if (listVPModificar.isEmpty()) {
               listVPModificar.add(vigenciaProrrateoSeleccionada);
            } else if (!listVPModificar.contains(vigenciaProrrateoSeleccionada)) {
               listVPModificar.add(vigenciaProrrateoSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambioVigenciaP = true;
         RequestContext.getCurrentInstance().update("form:datosVPVigencia");
         permitirIndexVP = true;
      } else if (tipoActualizacion == 1) {// Para crear registro
         nuevaVigenciaP.setCentrocosto(centroCostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVP");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarVP.setCentrocosto(centroCostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVP");
      }
      filtradoCentroCostos = null;
      centroCostoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      context.reset("form:lovCentroCostos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').hide()");
   }

   /**
    * Cancela la seleccion del centro costo (vigencia prorrateo)
    */
   public void cancelarCambioCentroCostoVP() {
      filtradoCentroCostos = null;
      centroCostoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVP = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCentroCostos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').hide()");
   }

   ///////////////////////////////////////////////////////////////////////
   /**
    * Actualiza el proyeecto seleccionado (vigencia prorrateo)
    */
//    public void actualizarProyectoVP() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        
//    }
   /**
    * Cancela el proyecto seleccionado (vigencia prorrateo)
    */
   public void cancelarCambioProyectoVP() {
      filtradoProyectos = null;
      proyectoSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVP = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProyectos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
   }

   ///////////////////////////////////////////////////////////////////////
   /**
    * Actualiza el proyecto seleccionado (vigencia prorrateo proyecto)
    */
//    public void actualizarProyectoVPP() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        
//    }
   /**
    * Cancela la seleccion del proyecto (vigencioa prorrateo proyecto)
    */
   public void cancelarCambioProyectoVPP() {
      filtradoProyectos = null;
      proyectoSelecionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVPP = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProyectos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProyectos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').hide()");
   }
   //LISTA DE VALORES DINAMICA

   /**
    * Metodo que activa la lista de valores de todas las tablas con respecto al
    * index activo y la columna activa
    */
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro selecciionado
      if (vigenciaLocalizacionSeleccionada == null && vigenciaProrrateoSeleccionada == null && vigenciaProrrateoProyectoSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         tipoActualizacion = 0;

         if (vigenciaProrrateoSeleccionada != null) {
            if (cualCeldaVP == 0) {
               contarRegistrosCentroC();
               RequestContext.getCurrentInstance().update("form:CentroCostosDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostosDialogo').show()");
            }
            if (cualCeldaVP == 4) {
               contarRegistrosProyecto();
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
            }
         } else if (vigenciaProrrateoProyectoSeleccionada != null) {
            if (cualCeldaVPP == 0) {
               contarRegistrosProyecto();
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
            }
         } else if (vigenciaLocalizacionSeleccionada != null) {
            if (cualCelda == 1) {
               //Estructura
               contarRegistrosEstrLoca();
               RequestContext.getCurrentInstance().update("form:LocalizacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('LocalizacionDialogo').show()");
            }
            if (cualCelda == 3) {
               //Motivos
               contarRegistrosMotiLoc();
               RequestContext.getCurrentInstance().update("form:MotivoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoDialogo').show()");
            }
            if (cualCelda == 4) {
               //Proyecto
               contarRegistrosProyecto();
               RequestContext.getCurrentInstance().update("form:ProyectosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectosDialogo').show()");
            }
         }
      }
   }

   /**
    * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
    * posicion en la pagina
    */
   public void validarNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalir').show()");
      } else {
         //Dialogo de nuevo registro multiple
         RequestContext.getCurrentInstance().update("form:NuevoRegistroPagina");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').show()");
      }
   }

   public void validarCualTabla(int tabla) {
      cualTabla = tabla;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tabla == 1) {
         //Dialogo de nuevo registro vigencias localizaciones
         RequestContext.getCurrentInstance().update("form:NuevoRegistroVL");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVL').show()");
      }
      if (tabla == 2) {
         if (vigenciaLocalizacionSeleccionada != null) {
            nuevaVigenciaP = new VigenciasProrrateos();
            nuevaVigenciaP.setCentrocosto(new CentrosCostos());
            nuevaVigenciaP.setProyecto(new Proyectos());
            //Dialogo de nuevo registro vigencia prorrateo
            RequestContext.getCurrentInstance().update("form:NuevoRegistroVP");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVP').show()");
            tipoActualizacion = 1;
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroLE').show()");
         }
      }
      if (tabla == 3) {
         if (vigenciaLocalizacionSeleccionada != null) {
            nuevaVigenciaPP = new VigenciasProrrateosProyectos();
            nuevaVigenciaPP.setProyecto(new Proyectos());
            //Dialogo de nuevo registro vigencia prorrateo proyecto
            RequestContext.getCurrentInstance().update("form:NuevoRegistroVPP");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVPP').show()");
            tipoActualizacion = 1;
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroLE').show()");
         }
      }
   }

   /**
    * Valida un registro VigenciaProrrateoProyecto con respecto a la existencia
    * de VigenciaProrrateo en el registro
    */
   public void validarRegistroVPP() {
      int tam = 0;
      if (vigenciasProrrateosCentroC != null) {
         tam = vigenciasProrrateosCentroC.size();
      }
      if (tam > 0) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:NuevoRegistroVPP");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVPP').show()");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:negacionNuevoRegistroVPP");
         RequestContext.getCurrentInstance().execute("PF('negacionNuevoRegistroVPP').show()");
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
      if (vigenciaLocalizacionSeleccionada != null) {
         nombreTabla = ":formExportarVL:datosVLEmpleadoExportar";
         nombreXML = "VigenciasLocalizacionesXML";
      }
      if (vigenciaProrrateoSeleccionada != null) {
         nombreTabla = ":formExportarVP:datosVPVigenciaExportar";
         nombreXML = "VigenciasProrrateoXML";
      }
      if (vigenciaProrrateoProyectoSeleccionada != null) {
         nombreTabla = ":formExportarVPP:datosVPPVigenciaExportar";
         nombreXML = "VigenciasProrrateosProyectosXML";
      }
      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (vigenciaLocalizacionSeleccionada != null) {
         exportPDFVL();
      }
      if (vigenciaProrrateoSeleccionada != null) {
         exportPDFVP();
      }
      if (vigenciaProrrateoProyectoSeleccionada != null) {
         exportPDFVPP();
      }
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDFVL() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVL:datosVLEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tablaEx, "VigenciasLocalizacionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Prorrateo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDFVP() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVP:datosVPVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tablaEx, "VigenciasProrrateosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Prorrateo Proyecto
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDFVPP() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVPP:datosVPPVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tablaEx, "VigenciasProrrateosProyectosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      if (vigenciaLocalizacionSeleccionada != null) {
         exportXLSVL();
      }
      if (vigenciaProrrateoSeleccionada != null) {
         exportXLSVP();
      }
      if (vigenciaProrrateoProyectoSeleccionada != null) {
         exportXLSVPP();
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLSVL() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVL:datosVLEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tablaEx, "VigenciasLocalizacionesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Prorrateo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLSVP() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVP:datosVPVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tablaEx, "VigenciasProrrateosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Prorrateo Proyecto
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLSVPP() throws IOException {
      DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVPP:datosVPPVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tablaEx, "VigenciasProrrateosProyectosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void verificarRastroTabla() {
      //Si no hay registros eleccionado
      if (vigenciaLocalizacionSeleccionada == null && vigenciaProrrateoSeleccionada == null && vigenciaProrrateoProyectoSeleccionada == null) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      } else if (vigenciaProrrateoSeleccionada != null) {
         //Metodo Rastro Vigencias Afiliaciones
         verificarRastroVigenciaProrrateoCC();
      } else if (vigenciaProrrateoProyectoSeleccionada != null) {
         //Metodo Rastro Vigencias Afiliaciones
         verificarRastroVigenciaProrrateoProyecto();
      } else if (vigenciaLocalizacionSeleccionada != null) {
         verificarRastroVigenciaLocalizacion();
      }
   }

   public void verificarRastroVigenciaLocalizacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaLocalizacionSeleccionada.getSecuencia(), "VIGENCIASLOCALIZACIONES");
      backUp = vigenciaLocalizacionSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasLocalizaciones";
         msnConfirmarRastro = "La tabla VIGENCIASLOCALIZACIONES tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroVigenciaLocalizacionHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASLOCALIZACIONES")) {
         nombreTablaRastro = "VigenciasLocalizaciones";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASLOCALIZACIONES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaProrrateoCC() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaProrrateoSeleccionada.getSecuencia(), "VIGENCIASPRORRATEOS");
      backUp = vigenciaProrrateoSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasProrrateos";
         msnConfirmarRastro = "La tabla VIGENCIASPRORRATEOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroVigenciaProrrateoCCHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASPRORRATEOS")) {
         nombreTablaRastro = "VigenciasProrrateos";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASPRORRATEOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaProrrateoProyecto() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaProrrateoProyectoSeleccionada.getSecuencia(), "VIGENCIASPRORRATEOSPROYECTOS");
      backUp = vigenciaProrrateoProyectoSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasProrrateosProyectos";
         msnConfirmarRastro = "La tabla VIGENCIASPRORRATEOSPROYECTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroVigenciaProrrateoProyectoHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASPRORRATEOSPROYECTOS")) {
         nombreTablaRastro = "VigenciasProrrateosProyectos";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASPRORRATEOSPROYECTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrarVL() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaLocalizacionSeleccionada = null;
      contarRegistrosVL();
   }

   public void eventoFiltrarVP() {
      if (tipoListaVP == 0) {
         tipoListaVP = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaProrrateoSeleccionada = null;
      contarRegistrosVP();
   }

   public void eventoFiltrarVPP() {
      if (tipoListaVPP == 0) {
         tipoListaVPP = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaProrrateoProyectoSeleccionada = null;
      contarRegistrosVPP();
   }

   public void contarRegistrosVP() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroVP");
   }

   public void contarRegistrosVPP() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroVPP");
   }

   public void contarRegistrosVL() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroVL");
   }

   public void contarRegistrosCentroC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCentroCosto");
   }

   public void contarRegistrosEstrLoca() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLocalizacion");
   }

   public void contarRegistrosMotiLoc() {
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivoLocalizacion");
   }

   public void contarRegistrosProyecto() {
      RequestContext.getCurrentInstance().update("form:infoRegistroProyecto");
   }
//
//    private void modificarInfoRegistroVL(int valor) {
//        infoRegistroVL = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroVP(int valor) {
//        infoRegistroVP = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroVPP(int valor) {
//        infoRegistroVPP = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroCentroC(int valor) {
//        infoRegistroCentroCosto = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroEstrLoca(int valor) {
//        infoRegistroEstLocalizacion = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroMotiLoc(int valor) {
//        infoRegistroMotivoLocalizacion = String.valueOf(valor);
//    }
//
//    private void modificarInfoRegistroProyecto(int valor) {
//        infoRegistroProyecto = String.valueOf(valor);
//    }

   public void recordarSeleccionVL() {
      if (vigenciaLocalizacionSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tabla = (DataTable) c.getViewRoot().findComponent("form:datosVLEmpleado");
         tabla.setSelection(vigenciaLocalizacionSeleccionada);
      }
   }

   public void recordarSeleccionVP() {
      if (vigenciaProrrateoSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tabla = (DataTable) c.getViewRoot().findComponent("form:datosVPVigencia");
         tabla.setSelection(vigenciaProrrateoSeleccionada);
      }
   }

   public void recordarSeleccionVPP() {
      if (vigenciaProrrateoProyectoSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tabla = (DataTable) c.getViewRoot().findComponent("form:datosVPPVigencia");
         tabla.setSelection(vigenciaProrrateoProyectoSeleccionada);
      }
   }

   public void restablecerTablaVL() {
      FacesContext c = FacesContext.getCurrentInstance();
      vlFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlFechaVigencia");
      vlFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
      vlCentroCosto = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlCentroCosto");
      vlCentroCosto.setFilterStyle("display: none; visibility: hidden;");
      vlLocalizacion = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlLocalizacion");
      vlLocalizacion.setFilterStyle("display: none; visibility: hidden;");
      vlMotivo = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlMotivo");
      vlMotivo.setFilterStyle("display: none; visibility: hidden;");
      vlProyecto = (Column) c.getViewRoot().findComponent("form:datosVLEmpleado:vlProyecto");
      vlProyecto.setFilterStyle("display: none; visibility: hidden;");
      altoTabla1 = "104";
      bandera = 0;
      filtradoVL = null;
      tipoLista = 0;
   }

   public void restablecerTablaVP() {
      FacesContext c = FacesContext.getCurrentInstance();
      vPCentroCosto = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPCentroCosto");
      vPCentroCosto.setFilterStyle("display: none; visibility: hidden;");
      vPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPPorcentaje");
      vPPorcentaje.setFilterStyle("display: none; visibility: hidden;");
      vPFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPFechaInicial");
      vPFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vPFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPFechaFinal");
      vPFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vPProyecto = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPProyecto");
      vPProyecto.setFilterStyle("display: none; visibility: hidden;");
      vPSubPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPVigencia:vPSubPorcentaje");
      vPSubPorcentaje.setFilterStyle("display: none; visibility: hidden;");
      altoTabla2 = "104";
      banderaVP = 0;
      filtradoVP = null;
      tipoListaVP = 0;
   }

   public void restablecerTablaVPP() {
      FacesContext c = FacesContext.getCurrentInstance();
      vPPProyecto = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPProyecto");
      vPPProyecto.setFilterStyle("display: none; visibility: hidden;");
      vPPPorcentaje = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPPorcentaje");
      vPPPorcentaje.setFilterStyle("display: none; visibility: hidden;");
      vPPFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPFechaInicial");
      vPPFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vPPFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVPPVigencia:vPPFechaFinal");
      vPPFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      altoTabla3 = "104";
      banderaVPP = 0;
      filtradoPP = null;
      tipoListaVPP = 0;
   }

   public List<VigenciasLocalizaciones> getVigenciaLocalizaciones() {
      if (vigenciaLocalizaciones == null) {
         if (empleado != null) {
            vigenciaLocalizaciones = new ArrayList<VigenciasLocalizaciones>();
            vigenciaLocalizaciones = administrarVigenciaLocalizacion.VigenciasLocalizacionesEmpleado(empleado.getSecuencia());
            if (vigenciaLocalizaciones != null) {
               if (!vigenciaLocalizaciones.isEmpty()) {
                  for (int i = 0; i < vigenciaLocalizaciones.size(); i++) {
                     if (vigenciaLocalizaciones.get(i).getProyecto() == null) {
                        vigenciaLocalizaciones.get(i).setProyecto(new Proyectos());
                     }
                  }
                  /*
                         * if (vigenciaLocalizacionSeleccionada == null) {
                         * vigenciaLocalizacionSeleccionada =
                         * vigenciaLocalizaciones.get(0); }
                   */
               }
            }
         }
      }
      return vigenciaLocalizaciones;
   }

   public void setVigenciaLocalizaciones(List<VigenciasLocalizaciones> vigenciaLocalizaciones) {
      this.vigenciaLocalizaciones = vigenciaLocalizaciones;
   }

   public List<VigenciasLocalizaciones> getFiltradoVL() {
      try {
         return filtradoVL;
      } catch (Exception e) {
         System.out.println("Error getFiltrarVL : " + e.toString());
         return null;
      }
   }

   public void setFiltradoVL(List<VigenciasLocalizaciones> filtrarVL) {
      try {
         this.filtradoVL = filtrarVL;
      } catch (Exception e) {
         System.out.println("Se estallo: " + e);
      }
   }

   public List<Estructuras> getListEstructurasCC() {
      if (listEstructurasCC == null) {
         listEstructurasCC = administrarVigenciaLocalizacion.estructuras();
      }
      return listEstructurasCC;
   }

   public void setListEstructurasCC(List<Estructuras> estructuras) {
      this.listEstructurasCC = estructuras;
   }

   public Estructuras getEstructuraSelecionada() {
      return estructuraSelecionada;
   }

   public void setEstructuraSelecionada(Estructuras estructuraSelecionada) {
      this.estructuraSelecionada = estructuraSelecionada;
   }

   public List<Estructuras> getFiltradoEstructura() {
      return filtradoEstructuraCC;
   }

   public void setFiltradoEstructura(List<Estructuras> filtradoEstructura) {
      this.filtradoEstructuraCC = filtradoEstructura;
   }

   public List<MotivosLocalizaciones> getListMotivosLocalizaciones() {
      if (listMotivosLocalizaciones == null) {
         listMotivosLocalizaciones = administrarVigenciaLocalizacion.motivosLocalizaciones();
      }
      return listMotivosLocalizaciones;
   }

   public void setListMotivosLocalizaciones(List<MotivosLocalizaciones> motivosLocalizaciones) {
      this.listMotivosLocalizaciones = motivosLocalizaciones;
   }

   public MotivosLocalizaciones getMotivoLocalizacionSelecionado() {
      return motivoLocalizacionSelecionado;
   }

   public void setMotivoLocalizacionSelecionado(MotivosLocalizaciones motivoLocalizacionSelecionado) {
      this.motivoLocalizacionSelecionado = motivoLocalizacionSelecionado;
   }

   public List<MotivosLocalizaciones> getFiltradoMotivosLocalizaciones() {
      return filtradoMotivosLocalizaciones;
   }

   public void setFiltradoMotivosLocalizaciones(List<MotivosLocalizaciones> filtradoMotivosLocalizaciones) {
      this.filtradoMotivosLocalizaciones = filtradoMotivosLocalizaciones;
   }

   public List<Proyectos> getListProyectos() {
      if (listProyectos == null) {
         listProyectos = administrarVigenciaLocalizacion.proyectos();
      }
      return listProyectos;
   }

   public void setListProyectos(List<Proyectos> listProyectos) {
      this.listProyectos = listProyectos;
   }

   public Proyectos getProyectoSelecionado() {
      return proyectoSelecionado;
   }

   public void setProyectoSelecionado(Proyectos proyectoSelecionado) {
      this.proyectoSelecionado = proyectoSelecionado;
   }

   public List<Proyectos> getFiltradoProyectos() {
      return filtradoProyectos;
   }

   public void setFiltradoProyectos(List<Proyectos> filtradoProyectos) {
      this.filtradoProyectos = filtradoProyectos;
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

   public List<VigenciasLocalizaciones> getListVLModificar() {
      return listVLModificar;
   }

   public void setListVLModificar(List<VigenciasLocalizaciones> listVLModificar) {
      this.listVLModificar = listVLModificar;
   }

   public VigenciasLocalizaciones getNuevaVigencia() {
      return nuevaVigencia;
   }

   public void setNuevaVigencia(VigenciasLocalizaciones nuevaVigencia) {
      this.nuevaVigencia = nuevaVigencia;
   }

   public List<VigenciasLocalizaciones> getListVLCrear() {
      return listVLCrear;
   }

   public void setListVLCrear(List<VigenciasLocalizaciones> listVLCrear) {
      this.listVLCrear = listVLCrear;
   }

   public List<VigenciasLocalizaciones> getListVLBorrar() {
      return listVLBorrar;
   }

   public void setListVLBorrar(List<VigenciasLocalizaciones> listVLBorrar) {
      this.listVLBorrar = listVLBorrar;
   }

   public VigenciasLocalizaciones getEditarVL() {
      return editarVL;
   }

   public void setEditarVL(VigenciasLocalizaciones editarVL) {
      this.editarVL = editarVL;
   }

   public VigenciasLocalizaciones getDuplicarVL() {
      return duplicarVL;
   }

   public void setDuplicarVL(VigenciasLocalizaciones duplicarVL) {
      this.duplicarVL = duplicarVL;
   }

   public List<VigenciasProrrateos> getVigenciasProrrateosCentroC() {
      try {
         if (vigenciaLocalizacionSeleccionada != null) {
            if (vigenciasProrrateosCentroC == null) {
               vigenciasProrrateosCentroC = administrarVigenciaLocalizacion.VigenciasProrrateosVigencia(vigenciaLocalizacionSeleccionada.getSecuencia());
               if (vigenciasProrrateosCentroC != null) {
                  for (int i = 0; i < vigenciasProrrateosCentroC.size(); i++) {
                     if (vigenciasProrrateosCentroC.get(i).getProyecto() == null) {
                        vigenciasProrrateosCentroC.get(i).setProyecto(new Proyectos());
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         System.out.println("Error cargarVigenciasProrrateos : " + e.toString());
         return null;
      }
      return vigenciasProrrateosCentroC;
   }

   public void setVigenciasProrrateosCentroC(List<VigenciasProrrateos> vigenciasProrrateosVigencia) {
      this.vigenciasProrrateosCentroC = vigenciasProrrateosVigencia;
   }

   public List<VigenciasProrrateos> getFiltradoVP() {
      return filtradoVP;
   }

   public void setFiltradoVP(List<VigenciasProrrateos> filtradoVigenciasProrrateosVigencia) {
      this.filtradoVP = filtradoVigenciasProrrateosVigencia;
   }

   public List<VigenciasProrrateosProyectos> getVigenciasProrrateosProyectos() {
      try {
         if (vigenciaLocalizacionSeleccionada != null) {
            if (vigenciasProrrateosProyectos == null) {
               vigenciasProrrateosProyectos = administrarVigenciaLocalizacion.VigenciasProrrateosProyectosVigencia(vigenciaLocalizacionSeleccionada.getSecuencia());
            }
         }
      } catch (Exception e) {
         System.out.println("Error vigenciasProrrateosProyectosVigencia");
         return null;
      }
      return vigenciasProrrateosProyectos;
   }

   public void setVigenciasProrrateosProyectos(List<VigenciasProrrateosProyectos> vigenciasProrrateosProyectosVigencia) {
      this.vigenciasProrrateosProyectos = vigenciasProrrateosProyectosVigencia;
   }

   public List<VigenciasProrrateosProyectos> getFiltradoPP() {
      return filtradoPP;
   }

   public void setFiltradoPP(List<VigenciasProrrateosProyectos> filtradoVigenciasProrrateosProyectosVigencia) {
      this.filtradoPP = filtradoVigenciasProrrateosProyectosVigencia;
   }

   public VigenciasProrrateos getEditarVP() {
      return editarVP;
   }

   public void setEditarVP(VigenciasProrrateos editarVP) {
      this.editarVP = editarVP;
   }

   public VigenciasProrrateosProyectos getEditarVPP() {
      return editarVPP;
   }

   public void setEditarVPP(VigenciasProrrateosProyectos editarVPP) {
      this.editarVPP = editarVPP;
   }

   public List<CentrosCostos> getListCentrosCostos() {
      if (listCentrosCostos == null) {
         listCentrosCostos = administrarVigenciaLocalizacion.centrosCostos();
      }
      return listCentrosCostos;
   }

   public void setListCentrosCostos(List<CentrosCostos> centrosCostos) {
      this.listCentrosCostos = centrosCostos;
   }

   public CentrosCostos getCentroCostoSeleccionado() {
      return centroCostoSeleccionado;
   }

   public void setCentroCostoSeleccionado(CentrosCostos centroCostoSeleccionado) {
      this.centroCostoSeleccionado = centroCostoSeleccionado;
   }

   public List<CentrosCostos> getFiltradoCentroCostos() {
      return filtradoCentroCostos;
   }

   public void setFiltradoCentroCostos(List<CentrosCostos> filtradoCentroCostos) {
      this.filtradoCentroCostos = filtradoCentroCostos;
   }

   public VigenciasProrrateos getNuevaVigenciaP() {
      return nuevaVigenciaP;
   }

   public void setNuevaVigenciaP(VigenciasProrrateos nuevaVigenciaP) {
      this.nuevaVigenciaP = nuevaVigenciaP;
   }

   public VigenciasProrrateosProyectos getNuevaVigenciaPP() {
      return nuevaVigenciaPP;
   }

   public void setNuevaVigenciaPP(VigenciasProrrateosProyectos nuevaVigenciaPP) {
      this.nuevaVigenciaPP = nuevaVigenciaPP;
   }

   public VigenciasProrrateos getDuplicarVP() {
      return duplicarVP;
   }

   public void setDuplicarVP(VigenciasProrrateos duplicarVP) {
      this.duplicarVP = duplicarVP;
   }

   public VigenciasProrrateosProyectos getDuplicarVPP() {
      return duplicarVPP;
   }

   public void setDuplicarVPP(VigenciasProrrateosProyectos duplicarVPP) {
      this.duplicarVPP = duplicarVPP;
   }

   public List<VigenciasProrrateos> getListVPCrear() {
      return listVPCrear;
   }

   public void setListVPCrear(List<VigenciasProrrateos> listVPCrear) {
      this.listVPCrear = listVPCrear;
   }

   public List<VigenciasProrrateosProyectos> getListVPPCrear() {
      return listVPPCrear;
   }

   public void setListVPPCrear(List<VigenciasProrrateosProyectos> listVPPCrear) {
      this.listVPPCrear = listVPPCrear;
   }

   public List<VigenciasProrrateos> getListVPModificar() {
      return listVPModificar;
   }

   public void setListVPModificar(List<VigenciasProrrateos> listVPModificar) {
      this.listVPModificar = listVPModificar;
   }

   public List<VigenciasProrrateosProyectos> getListVPPModificar() {
      return listVPPModificar;
   }

   public void setListVPPModificar(List<VigenciasProrrateosProyectos> listVPPModificar) {
      this.listVPPModificar = listVPPModificar;
   }

   public List<VigenciasProrrateos> getListVPBorrar() {
      return listVPBorrar;
   }

   public void setListVPBorrar(List<VigenciasProrrateos> listVPBorrar) {
      this.listVPBorrar = listVPBorrar;
   }

   public List<VigenciasProrrateosProyectos> getListVPPBorrar() {
      return listVPPBorrar;
   }

   public void setListVPPBorrar(List<VigenciasProrrateosProyectos> listVPPBorrar) {
      this.listVPPBorrar = listVPPBorrar;
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

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getAltoTabla1() {
      return altoTabla1;
   }

   public String getAltoTabla2() {
      return altoTabla2;
   }

   public String getAltoTabla3() {
      return altoTabla3;
   }

   public VigenciasLocalizaciones getVigenciaLocalizacionSeleccionada() {
      return vigenciaLocalizacionSeleccionada;
   }

   public void setVigenciaLocalizacionSeleccionada(VigenciasLocalizaciones vigenciaLocalizacionSeleccionada) {
      this.vigenciaLocalizacionSeleccionada = vigenciaLocalizacionSeleccionada;
   }

   public VigenciasProrrateos getVigenciaProrrateoSeleccionada() {
      return vigenciaProrrateoSeleccionada;
   }

   public void setVigenciaProrrateoSeleccionada(VigenciasProrrateos vigenciaProrrateoSeleccionada) {
      this.vigenciaProrrateoSeleccionada = vigenciaProrrateoSeleccionada;
   }

   public VigenciasProrrateosProyectos getVigenciaProrrateoProyectoSeleccionada() {
      return vigenciaProrrateoProyectoSeleccionada;
   }

   public void setVigenciaProrrateoProyectoSeleccionada(VigenciasProrrateosProyectos vigenciaProrrateoProyectoSeleccionada) {
      this.vigenciaProrrateoProyectoSeleccionada = vigenciaProrrateoProyectoSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public String getInfoRegistroEstLocalizacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovLocalizacion");
      infoRegistroEstLocalizacion = String.valueOf(tabla.getRowCount());
      return infoRegistroEstLocalizacion;
   }

   public String getInfoRegistroMotivoLocalizacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMotivo");
      infoRegistroMotivoLocalizacion = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoLocalizacion;
   }

   public String getInfoRegistroProyecto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProyectos");
      infoRegistroProyecto = String.valueOf(tabla.getRowCount());
      return infoRegistroProyecto;
   }

   public String getInfoRegistroCentroCosto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCentroCostos");
      infoRegistroCentroCosto = String.valueOf(tabla.getRowCount());
      return infoRegistroCentroCosto;
   }

   public String getInfoRegistroVP() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVPVigencia");
      infoRegistroVP = String.valueOf(tabla.getRowCount());
      return infoRegistroVP;
   }

   public String getInfoRegistroVPP() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVPPVigencia");
      infoRegistroVPP = String.valueOf(tabla.getRowCount());
      return infoRegistroVPP;
   }

   public String getInfoRegistroVL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVLEmpleado");
      infoRegistroVL = String.valueOf(tabla.getRowCount());
      return infoRegistroVL;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
