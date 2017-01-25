package Controlador;

import Entidades.Empleados;
import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import Entidades.VigenciasIndicadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplVigenciaIndicadorInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
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
public class ControlEmplVigenciaIndicador implements Serializable {

   @EJB
   AdministrarEmplVigenciaIndicadorInterface administrarEmplVigenciaIndicador;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //VigenciasIndicadores
   private List<VigenciasIndicadores> listVigenciasIndicadores;
   private List<VigenciasIndicadores> filtrarListVigenciasIndicadores;
   private VigenciasIndicadores vigenciaTablaSeleccionada;
   //TiposIndicadores
   private List<TiposIndicadores> listTiposIndicadores;
   private TiposIndicadores tipoIndicadorSeleccionado;
   private List<TiposIndicadores> filtrarListTiposIndicadores;
   //Indicadores
   private List<Indicadores> listIndicadores;
   private Indicadores indicadorSeleccionado;
   private List<Indicadores> filtrarListIndicadores;
   //Tipo Actualizacion
   private int tipoActualizacion;
   //Activo/Desactivo VP Crtl + F11
   private int banderaV;
   //Columnas Tabla VP
   private Column viTipoIndicador, viIndicador, viFechaInicial, viFechaFinal;
   //Otros
   private boolean aceptar;
   //modificar
   private List<VigenciasIndicadores> listVigenciaIndicadorModificar;
   private boolean guardado, activarLov;
   //crear VP
   public VigenciasIndicadores nuevaVigencia;
   private BigInteger l;
   private int k;
   //borrar VL
   private List<VigenciasIndicadores> listVigenciaIndicadorBorrar;
   //editar celda
   private VigenciasIndicadores editarVigenciaIndicador;
   //duplicar
   //Autocompletar
   private boolean permitirIndexV;
   //Variables Autompletar
   private String tipos, indicador;
   private int cualCelda, tipoLista;
   private VigenciasIndicadores duplicarVigenciaIndicador;
   private List<VigenciasIndicadores> listVigenciaIndicadorCrear;
   private BigInteger backUpSecRegistro;
   private Date fechaInic, fechaFin;
   private Empleados empleado;
   private Date fechaParametro;
   //
   private String infoRegistro;
   private String infoRegistroTipo, infoRegistroIndicador;
   private String altoTabla;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplVigenciaIndicador() {
      altoTabla = "310";
      empleado = new Empleados();
      backUpSecRegistro = null;
      tipoLista = 0;
      //Otros
      aceptar = true;
      listVigenciaIndicadorBorrar = new ArrayList<VigenciasIndicadores>();
      k = 0;
      listVigenciaIndicadorModificar = new ArrayList<VigenciasIndicadores>();
      editarVigenciaIndicador = new VigenciasIndicadores();
      tipoLista = 0;
      guardado = true;
      activarLov = true;
      empleado = new Empleados();
      banderaV = 0;
      permitirIndexV = true;
      vigenciaTablaSeleccionada = null;
      cualCelda = -1;
      listIndicadores = null;
      listTiposIndicadores = null;
      nuevaVigencia = new VigenciasIndicadores();
      nuevaVigencia.setIndicador(new Indicadores());
      nuevaVigencia.setTipoindicador(new TiposIndicadores());
      listVigenciaIndicadorCrear = new ArrayList<VigenciasIndicadores>();
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
         String pagActual = "emplvigenciaindicador";
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
         administrarEmplVigenciaIndicador.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger secuencia) {
      listVigenciasIndicadores = null;
      empleado = administrarEmplVigenciaIndicador.empleadoActual(secuencia);
      getListVigenciasIndicadores();
      deshabilitarBotonLov();
      if (!listVigenciasIndicadores.isEmpty()) {
         vigenciaTablaSeleccionada = listVigenciasIndicadores.get(0);

      }
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         VigenciasIndicadores auxiliar = null;
         if (tipoLista == 0) {
            auxiliar = vigenciaTablaSeleccionada;
         }
         if (tipoLista == 1) {
            auxiliar = vigenciaTablaSeleccionada;
         }
         if (auxiliar.getFechafinal() != null) {
            if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (auxiliar.getFechafinal() == null) {
            if (auxiliar.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 1) {
         if (nuevaVigencia.getFechafinal() != null) {
            if (nuevaVigencia.getFechainicial().after(fechaParametro) && nuevaVigencia.getFechainicial().before(nuevaVigencia.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else if (nuevaVigencia.getFechainicial().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaIndicador.getFechafinal() != null) {
            if (duplicarVigenciaIndicador.getFechainicial().after(fechaParametro) && duplicarVigenciaIndicador.getFechainicial().before(duplicarVigenciaIndicador.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else if (duplicarVigenciaIndicador.getFechainicial().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarFechas(VigenciasIndicadores vigenciaIndicador, int c) {
      VigenciasIndicadores auxiliar = null;
      if (tipoLista == 0) {
         auxiliar = vigenciaTablaSeleccionada;
      }
      if (tipoLista == 1) {
         auxiliar = vigenciaTablaSeleccionada;
      }
      if (auxiliar.getFechainicial() != null) {
         boolean retorno = false;
         if (auxiliar.getFechafinal() == null) {
            retorno = true;
         }
         if (auxiliar.getFechafinal() != null) {
            vigenciaTablaSeleccionada = vigenciaIndicador;
            retorno = validarFechasRegistro(0);
         }
         if (retorno == true) {
            cambiarIndiceV(vigenciaIndicador, c);
            modificarVigencia(vigenciaIndicador);
         } else {
            if (tipoLista == 0) {
               vigenciaTablaSeleccionada.setFechafinal(fechaFin);
               vigenciaTablaSeleccionada.setFechainicial(fechaInic);
            }
            if (tipoLista == 1) {
               vigenciaTablaSeleccionada.setFechafinal(fechaFin);
               vigenciaTablaSeleccionada.setFechainicial(fechaInic);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigencia");
            RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
         }
      } else {
         if (tipoLista == 0) {
            vigenciaTablaSeleccionada.setFechainicial(fechaInic);
         }
         if (tipoLista == 1) {
            vigenciaTablaSeleccionada.setFechainicial(fechaInic);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigencia");
         RequestContext.getCurrentInstance().execute("PF('errorRegInfo').show()");
      }
   }

   public void modificarVigencia(VigenciasIndicadores vigenciaIndicador) {
      vigenciaTablaSeleccionada = vigenciaIndicador;
      if (tipoLista == 0) {
         if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
            if (listVigenciaIndicadorModificar.isEmpty()) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else //            int ind = listVigenciasIndicadores.indexOf(vigenciaTablaSeleccionada);
      //            vigenciaTablaSeleccionada = ind;
       if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
            if (listVigenciaIndicadorModificar.isEmpty()) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosVigencia");

   }

   public void modificarVigencia(VigenciasIndicadores vigenciaIndicador, String confirmarCambio, String valorConfirmar) {
      vigenciaTablaSeleccionada = vigenciaIndicador;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOS")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoLista == 0) {
               vigenciaTablaSeleccionada.getTipoindicador().setDescripcion(tipos);
            } else {
               vigenciaTablaSeleccionada.getTipoindicador().setDescripcion(tipos);
            }
            for (int i = 0; i < listTiposIndicadores.size(); i++) {
               if (listTiposIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  vigenciaTablaSeleccionada.setTipoindicador(listTiposIndicadores.get(indiceUnicoElemento));
               } else {
                  vigenciaTablaSeleccionada.setTipoindicador(listTiposIndicadores.get(indiceUnicoElemento));
               }
               listTiposIndicadores = null;
               getListTiposIndicadores();
            } else {
               permitirIndexV = false;
               RequestContext.getCurrentInstance().update("form:TiposDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            listTiposIndicadores = null;
            getListTiposIndicadores();
            if (tipoLista == 0) {
               vigenciaTablaSeleccionada.setTipoindicador(new TiposIndicadores());
            } else {
               vigenciaTablaSeleccionada.setTipoindicador(new TiposIndicadores());
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("INDICADORES")) {
         if (tipoLista == 0) {
            vigenciaTablaSeleccionada.getIndicador().setDescripcion(indicador);
         } else {
            vigenciaTablaSeleccionada.getIndicador().setDescripcion(indicador);
         }
         for (int i = 0; i < listIndicadores.size(); i++) {
            if (listIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               vigenciaTablaSeleccionada.setIndicador(listIndicadores.get(indiceUnicoElemento));
            } else {
               vigenciaTablaSeleccionada.setIndicador(listIndicadores.get(indiceUnicoElemento));
            }
            listIndicadores = null;
            getListIndicadores();
         } else {
            permitirIndexV = false;
            RequestContext.getCurrentInstance().update("form:IndicadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (tipoLista == 0) {
            if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
               if (listVigenciaIndicadorModificar.isEmpty()) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         } else if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
            if (listVigenciaIndicadorModificar.isEmpty()) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
               listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosVigencia");
   }

   public void valoresBackupAutocompletarVigencia(int tipoNuevo, String Campo) {

      if (Campo.equals("TIPOS")) {
         if (tipoNuevo == 1) {
            tipos = nuevaVigencia.getTipoindicador().getDescripcion();
         } else if (tipoNuevo == 2) {
            tipos = duplicarVigenciaIndicador.getTipoindicador().getDescripcion();
         }
      } else if (Campo.equals("INDICADORES")) {
         if (tipoNuevo == 1) {
            indicador = nuevaVigencia.getIndicador().getDescripcion();
         } else if (tipoNuevo == 2) {
            indicador = duplicarVigenciaIndicador.getIndicador().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoVigencia(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOS")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaVigencia.getTipoindicador().setDescripcion(tipos);
            } else if (tipoNuevo == 2) {
               duplicarVigenciaIndicador.getTipoindicador().setDescripcion(tipos);
            }
            for (int i = 0; i < listTiposIndicadores.size(); i++) {
               if (listTiposIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigencia.setTipoindicador(listTiposIndicadores.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoIndicadorV");
               } else if (tipoNuevo == 2) {
                  duplicarVigenciaIndicador.setTipoindicador(listTiposIndicadores.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoV");
               }
               listTiposIndicadores = null;
               getListTiposIndicadores();
            } else {
               RequestContext.getCurrentInstance().update("form:TiposDialogo");
               RequestContext.getCurrentInstance().execute("PF('TiposDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoIndicadorV");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoV");
               }
            }
         } else {
            listTiposIndicadores = null;
            getListTiposIndicadores();
            if (tipoNuevo == 1) {
               nuevaVigencia.setTipoindicador(new TiposIndicadores());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoIndicadorV");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaIndicador.setTipoindicador(new TiposIndicadores());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoV");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("INDICADORES")) {
         if (tipoNuevo == 1) {
            nuevaVigencia.getIndicador().setDescripcion(indicador);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaIndicador.getIndicador().setDescripcion(indicador);
         }
         for (int i = 0; i < listIndicadores.size(); i++) {
            if (listIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }

         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setIndicador(listIndicadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIndicadorV");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaIndicador.setIndicador(listIndicadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIndicadorV");
            }
            listIndicadores = null;
            getListIndicadores();
         } else {
            RequestContext.getCurrentInstance().update("form:IndicadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIndicadorV");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIndicadorV");
            }
         }
      }
   }

   public void cambiarIndiceV(VigenciasIndicadores vigenciaIndicador, int celda) {
      if (permitirIndexV == true) {
         vigenciaTablaSeleccionada = vigenciaIndicador;
         cualCelda = celda;
         if (tipoLista == 0) {
            fechaFin = vigenciaTablaSeleccionada.getFechafinal();
            fechaInic = vigenciaTablaSeleccionada.getFechainicial();
            vigenciaTablaSeleccionada.getSecuencia();
            //tipos = vigenciaTablaSeleccionada.getTipoindicador().getDescripcion();
            indicador = vigenciaTablaSeleccionada.getIndicador().getDescripcion();
            contarRegistros();
            contarRegistroIndicador();
         }
         if (tipoLista == 1) {
            fechaFin = vigenciaTablaSeleccionada.getFechafinal();
            fechaInic = vigenciaTablaSeleccionada.getFechainicial();
            vigenciaTablaSeleccionada.getSecuencia();
            tipos = vigenciaTablaSeleccionada.getTipoindicador().getDescripcion();
            indicador = vigenciaTablaSeleccionada.getIndicador().getDescripcion();
            contarRegistroTipo();
            contarRegistroIndicador();
         }
      }
   }

   public void guardadoGeneral() {
      guardarCambiosV();
   }

   public void guardarCambiosV() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listVigenciaIndicadorBorrar.isEmpty()) {
               administrarEmplVigenciaIndicador.borrarVigenciasIndicadores(listVigenciaIndicadorBorrar);
               listVigenciaIndicadorBorrar.clear();
            }
            if (!listVigenciaIndicadorCrear.isEmpty()) {
               administrarEmplVigenciaIndicador.crearVigenciasIndicadores(listVigenciaIndicadorCrear);
               listVigenciaIndicadorCrear.clear();
            }
            if (!listVigenciaIndicadorModificar.isEmpty()) {
               administrarEmplVigenciaIndicador.editarVigenciasIndicadores(listVigenciaIndicadorModificar);
               listVigenciaIndicadorModificar.clear();
            }
            listVigenciasIndicadores = null;
            getListVigenciasIndicadores();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigencia");
            k = 0;
            vigenciaTablaSeleccionada = null;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacionV() {
      if (banderaV == 1) {
         altoTabla = "310";
         FacesContext c = FacesContext.getCurrentInstance();
         viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
         viTipoIndicador.setFilterStyle("display: none; visibility: hidden;");

         viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
         viIndicador.setFilterStyle("display: none; visibility: hidden;");

         viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
         viFechaInicial.setFilterStyle("display: none; visibility: hidden;");

         viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
         viFechaFinal.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosVigencia");
         banderaV = 0;
         filtrarListVigenciasIndicadores = null;
         tipoLista = 0;
      }
      listIndicadores = null;
      listTiposIndicadores = null;
      listVigenciaIndicadorBorrar.clear();
      listVigenciaIndicadorCrear.clear();
      listVigenciaIndicadorModificar.clear();
      vigenciaTablaSeleccionada = null;
      k = 0;
      listVigenciasIndicadores = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext context = RequestContext.getCurrentInstance();
      getListVigenciasIndicadores();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosVigencia");
      nuevaVigencia = new VigenciasIndicadores();
      nuevaVigencia.setIndicador(new Indicadores());
      nuevaVigencia.setTipoindicador(new TiposIndicadores());
   }

   public void editarCelda() {
      if (vigenciaTablaSeleccionada != null) {
         if (tipoLista == 0) {
            editarVigenciaIndicador = vigenciaTablaSeleccionada;
         }
         if (tipoLista == 1) {
            editarVigenciaIndicador = vigenciaTablaSeleccionada;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoVD");
            RequestContext.getCurrentInstance().execute("PF('editarTipoVD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarIndicadorVD");
            RequestContext.getCurrentInstance().execute("PF('editarIndicadorVD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
      }
   }

   public void agregarNuevaV() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaVigencia.getFechainicial() != null && nuevaVigencia.getIndicador().getSecuencia() != null) {
         if (validarFechasRegistro(1) == true) {
            //CERRAR FILTRADO
            if (banderaV == 1) {
               altoTabla = "310";
               FacesContext c = FacesContext.getCurrentInstance();
               viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
               viTipoIndicador.setFilterStyle("display: none; visibility: hidden;");

               viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
               viIndicador.setFilterStyle("display: none; visibility: hidden;");

               viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
               viFechaInicial.setFilterStyle("display: none; visibility: hidden;");

               viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
               viFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosVigencia");
               banderaV = 0;
               filtrarListVigenciasIndicadores = null;
               tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);
            listVigenciasIndicadores.add(nuevaVigencia);
            listVigenciaIndicadorCrear.add(nuevaVigencia);
            vigenciaTablaSeleccionada = nuevaVigencia;
            //
            nuevaVigencia = new VigenciasIndicadores();
            nuevaVigencia.setTipoindicador(new TiposIndicadores());
            nuevaVigencia.setIndicador(new Indicadores());
            limpiarNuevaV();
            //
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigencia");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroV').hide()");

         } else {
            RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegInfo').show()");
      }
   }

   public void limpiarNuevaV() {
      nuevaVigencia = new VigenciasIndicadores();
      nuevaVigencia.setTipoindicador(new TiposIndicadores());
      nuevaVigencia.setIndicador(new Indicadores());

   }

   public void cancelarNuevaV() {
      nuevaVigencia = new VigenciasIndicadores();
      nuevaVigencia.setTipoindicador(new TiposIndicadores());
      nuevaVigencia.setIndicador(new Indicadores());
   }

   public void verificarDuplicarVigencia() {
      if (vigenciaTablaSeleccionada != null) {
         duplicarVigenciaM();
      }
   }

   public void duplicarVigenciaM() {
      duplicarVigenciaIndicador = new VigenciasIndicadores();
      if (vigenciaTablaSeleccionada != null) {
         if (tipoLista == 0) {
            duplicarVigenciaIndicador.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
            duplicarVigenciaIndicador.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
            duplicarVigenciaIndicador.setEmpleado(empleado);
            duplicarVigenciaIndicador.setIndicador(vigenciaTablaSeleccionada.getIndicador());
            duplicarVigenciaIndicador.setTipoindicador(vigenciaTablaSeleccionada.getTipoindicador());
         }
         if (tipoLista == 1) {
            duplicarVigenciaIndicador.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
            duplicarVigenciaIndicador.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
            duplicarVigenciaIndicador.setEmpleado(empleado);
            duplicarVigenciaIndicador.setIndicador(vigenciaTablaSeleccionada.getIndicador());
            duplicarVigenciaIndicador.setTipoindicador(vigenciaTablaSeleccionada.getTipoindicador());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarV");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroV').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
      }

   }

   public void confirmarDuplicarV() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarVigenciaIndicador.getFechainicial() != null && duplicarVigenciaIndicador.getIndicador().getSecuencia() != null) {
         if (validarFechasRegistro(2) == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaIndicador.setEmpleado(empleado);
            duplicarVigenciaIndicador.setSecuencia(l);
            listVigenciasIndicadores.add(duplicarVigenciaIndicador);
            listVigenciaIndicadorCrear.add(duplicarVigenciaIndicador);
            vigenciaTablaSeleccionada = duplicarVigenciaIndicador;
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaV == 1) {
               //CERRAR FILTRADO
               altoTabla = "310";
               FacesContext c = FacesContext.getCurrentInstance();
               viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
               viTipoIndicador.setFilterStyle("display: none; visibility: hidden;");

               viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
               viIndicador.setFilterStyle("display: none; visibility: hidden;");

               viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
               viFechaInicial.setFilterStyle("display: none; visibility: hidden;");

               viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
               viFechaFinal.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosVigencia");
               banderaV = 0;
               filtrarListVigenciasIndicadores = null;
               tipoLista = 0;
            }
            duplicarVigenciaIndicador = new VigenciasIndicadores();
            limpiarduplicarV();
            getListVigenciasIndicadores();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigencia");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroV').hide()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegInfo').show()");
      }
   }

   public void limpiarduplicarV() {
      duplicarVigenciaIndicador = new VigenciasIndicadores();
      duplicarVigenciaIndicador.setTipoindicador(new TiposIndicadores());
      duplicarVigenciaIndicador.setIndicador(new Indicadores());
   }

   public void cancelarDuplicadoV() {
      duplicarVigenciaIndicador = new VigenciasIndicadores();
      duplicarVigenciaIndicador.setTipoindicador(new TiposIndicadores());
      duplicarVigenciaIndicador.setIndicador(new Indicadores());
   }

   public void validarBorradoVigencia() {
      if (vigenciaTablaSeleccionada != null) {
         borrarV();
      }
   }

   public void borrarV() {
      if (vigenciaTablaSeleccionada != null) {
         if (!listVigenciaIndicadorModificar.isEmpty() && listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
            int modIndex = listVigenciaIndicadorModificar.indexOf(vigenciaTablaSeleccionada);
            listVigenciaIndicadorModificar.remove(modIndex);
            listVigenciaIndicadorBorrar.add(vigenciaTablaSeleccionada);
         } else if (!listVigenciaIndicadorCrear.isEmpty() && listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
            int crearIndex = listVigenciaIndicadorCrear.indexOf(vigenciaTablaSeleccionada);
            listVigenciaIndicadorCrear.remove(crearIndex);
         } else {
            listVigenciaIndicadorBorrar.add(vigenciaTablaSeleccionada);
         }
         listVigenciasIndicadores.remove(vigenciaTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListVigenciasIndicadores.remove(vigenciaTablaSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosVigencia");
         vigenciaTablaSeleccionada = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
      }

   }

   public void activarCtrlF11() {
      filtradoVigencia();
   }

   public void filtradoVigencia() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaV == 0) {
         altoTabla = "290";
         viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
         viFechaInicial.setFilterStyle("width: 85% !important");

         viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
         viFechaFinal.setFilterStyle("width: 85% !important");

         viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
         viTipoIndicador.setFilterStyle("width: 85% !important");

         viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
         viIndicador.setFilterStyle("width: 85% !important");

         ///
         RequestContext.getCurrentInstance().update("form:datosVigencia");
         tipoLista = 1;
         banderaV = 1;
      } else if (banderaV == 1) {
         altoTabla = "310";
         viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
         viTipoIndicador.setFilterStyle("display: none; visibility: hidden;");

         viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
         viIndicador.setFilterStyle("display: none; visibility: hidden;");

         viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
         viFechaInicial.setFilterStyle("display: none; visibility: hidden;");

         viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
         viFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigencia");
         banderaV = 0;
         filtrarListVigenciasIndicadores = null;
         tipoLista = 0;
      }

   }

   public void salir() {
      if (banderaV == 1) {
         altoTabla = "310";
         FacesContext c = FacesContext.getCurrentInstance();
         viTipoIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viTipoIndicador");
         viTipoIndicador.setFilterStyle("display: none; visibility: hidden;");

         viIndicador = (Column) c.getViewRoot().findComponent("form:datosVigencia:viIndicador");
         viIndicador.setFilterStyle("display: none; visibility: hidden;");

         viFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaInicial");
         viFechaInicial.setFilterStyle("display: none; visibility: hidden;");

         viFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigencia:viFechaFinal");
         viFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigencia");
         banderaV = 0;
         filtrarListVigenciasIndicadores = null;
         tipoLista = 0;
      }
      listVigenciaIndicadorBorrar.clear();
      listVigenciaIndicadorCrear.clear();
      listVigenciaIndicadorModificar.clear();
      vigenciaTablaSeleccionada = null;
      k = 0;
      listVigenciasIndicadores = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      tipoActualizacion = -1;
   }
   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

   public void asignarIndex(VigenciasIndicadores vigenciaIndicador, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      deshabilitarBotonLov();
      if (LND == 0) {
         vigenciaTablaSeleccionada = vigenciaIndicador;
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         contarRegistroTipo();
         habilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:TiposDialogo");
         RequestContext.getCurrentInstance().execute("PF('TiposDialogo').show()");
      } else if (dlg == 1) {
         contarRegistros();
         habilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:IndicadorDialogo");
         RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').show()");
      }

   }

   public void actualizarTipoIndicador() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            vigenciaTablaSeleccionada.setTipoindicador(tipoIndicadorSeleccionado);
            if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
               if (listVigenciaIndicadorModificar.isEmpty()) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexV = true;

         } else {
            vigenciaTablaSeleccionada.setTipoindicador(tipoIndicadorSeleccionado);
            if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
               if (listVigenciaIndicadorModificar.isEmpty()) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexV = true;
         }
         RequestContext.getCurrentInstance().update("form:datosVigencia");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setTipoindicador(tipoIndicadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoIndicadorV");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaIndicador.setTipoindicador(tipoIndicadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoV");
      }
      filtrarListTiposIndicadores = null;
      tipoIndicadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      /*
         RequestContext.getCurrentInstance().update("form:TiposDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipos");
         RequestContext.getCurrentInstance().update("form:aceptarT");*/
      context.reset("form:lovTipos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TiposDialogo').hide()");
   }

   public void cancelarCambioTipoIndicador() {
      filtrarListTiposIndicadores = null;
      tipoIndicadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexV = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TiposDialogo').hide()");
   }

   public void actualizarIndicador() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            vigenciaTablaSeleccionada.setIndicador(indicadorSeleccionado);
            if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
               if (listVigenciaIndicadorModificar.isEmpty()) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               }
            }
         } else {
            vigenciaTablaSeleccionada.setIndicador(indicadorSeleccionado);
            if (!listVigenciaIndicadorCrear.contains(vigenciaTablaSeleccionada)) {
               if (listVigenciaIndicadorModificar.isEmpty()) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               } else if (!listVigenciaIndicadorModificar.contains(vigenciaTablaSeleccionada)) {
                  listVigenciaIndicadorModificar.add(vigenciaTablaSeleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexV = true;
         RequestContext.getCurrentInstance().update("form:datosVigencia");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setIndicador(indicadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIndicadorV");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaIndicador.setIndicador(indicadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIndicadorV");
      }
      filtrarListIndicadores = null;
      indicadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      /*
         RequestContext.getCurrentInstance().update("form:IndicadorDialogo");
         RequestContext.getCurrentInstance().update("form:lovIndicador");
         RequestContext.getCurrentInstance().update("form:aceptarI");*/
      context.reset("form:lovIndicador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIndicador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').hide()");
   }

   public void cancelarCambioIndicador() {
      filtrarListIndicadores = null;
      indicadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexV = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovIndicador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIndicador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').hide()");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaTablaSeleccionada != null) {
         if (cualCelda == 2) {
            contarRegistroTipo();
            contarRegistroIndicador();
            RequestContext.getCurrentInstance().update("form:TiposDialogo");
            RequestContext.getCurrentInstance().execute("PF('TiposDialogo').show()");
            tipoActualizacion = 0;
            habilitarBotonLov();
         }
         if (cualCelda == 3) {
            contarRegistroTipo();
            contarRegistroIndicador();
            RequestContext.getCurrentInstance().update("form:IndicadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('IndicadorDialogo').show()");
            tipoActualizacion = 0;
            habilitarBotonLov();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void validarExportPDF() throws IOException {
      exportPDF_V();
   }

   public void exportPDF_V() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarV:datosVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CensosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      vigenciaTablaSeleccionada = null;
   }

   public void verificarExportXLS() throws IOException {
      exportXLS_V();
   }

   public void exportXLS_V() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarV:datosVigenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CensosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      vigenciaTablaSeleccionada = null;
   }

   public void eventoFiltrar() {
      if (vigenciaTablaSeleccionada != null) {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      }
   }

   public void eventoFiltrarTipo() {
      contarRegistros();
   }

   public void contarRegistroIndicador() {
      RequestContext.getCurrentInstance().update("form:infoRegistroIndicador");
   }

   public void contarRegistroTipo() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipo");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVigenciasIndicadores != null) {
         if (vigenciaTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaTablaSeleccionada.getSecuencia(), "VIGENCIASINDICADORES");
            backUpSecRegistro = vigenciaTablaSeleccionada.getSecuencia();
            vigenciaTablaSeleccionada = null;
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
         } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASINDICADORES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
         }
      }
   }

   public void recordarSeleccion() {
      if (vigenciaTablaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigencia");
         tablaC.setSelection(vigenciaTablaSeleccionada);

      }
   }

   //GET - SET 
   public List<VigenciasIndicadores> getListVigenciasIndicadores() {
      try {
         if (listVigenciasIndicadores == null) {
            if (empleado.getSecuencia() != null) {
               listVigenciasIndicadores = administrarEmplVigenciaIndicador.listVigenciasIndicadoresEmpleado(empleado.getSecuencia());
//                    if (listVigenciasIndicadores != null) {
//                        for (int i = 0; i < listVigenciasIndicadores.size(); i++) {
//                            if (vigenciaTablaSeleccionada.getTipoindicador() == null) {
//                                vigenciaTablaSeleccionada.setTipoindicador(new TiposIndicadores());
//                            }
//                            if (vigenciaTablaSeleccionada.getIndicador() == null) {
//                                vigenciaTablaSeleccionada.setIndicador(new Indicadores());
//                            }
//                        }
//                    }
            }
         }
         return listVigenciasIndicadores;
      } catch (Exception e) {
         System.out.println("Error en getListVigenciasIndicadores : " + e.toString());
         return null;
      }
   }

   public void setListVigenciasIndicadores(List<VigenciasIndicadores> setListVigenciasIndicadores) {
      this.listVigenciasIndicadores = setListVigenciasIndicadores;
   }

   public List<VigenciasIndicadores> getFiltrarListVigenciasIndicadores() {
      return filtrarListVigenciasIndicadores;
   }

   public void setFiltrarListVigenciasIndicadores(List<VigenciasIndicadores> setFiltrarListVigenciasIndicadores) {
      this.filtrarListVigenciasIndicadores = setFiltrarListVigenciasIndicadores;
   }

   public List<TiposIndicadores> getListTiposIndicadores() {
      try {
         listTiposIndicadores = administrarEmplVigenciaIndicador.listTiposIndicadores();
         return listTiposIndicadores;
      } catch (Exception e) {
         System.out.println("Error getListTiposIndicadores " + e.toString());
         return null;
      }
   }

   public void setListTiposIndicadores(List<TiposIndicadores> setListTiposIndicadores) {
      this.listTiposIndicadores = setListTiposIndicadores;
   }

   public TiposIndicadores getTipoIndicadorSeleccionado() {
      return tipoIndicadorSeleccionado;
   }

   public void setTipoIndicadorSeleccionado(TiposIndicadores setTipoIndicadorSeleccionado) {
      this.tipoIndicadorSeleccionado = setTipoIndicadorSeleccionado;
   }

   public List<TiposIndicadores> getFiltrarListTiposIndicadores() {
      return filtrarListTiposIndicadores;
   }

   public void setFiltrarListTiposIndicadores(List<TiposIndicadores> setFiltrarListTiposIndicadores) {
      this.filtrarListTiposIndicadores = setFiltrarListTiposIndicadores;
   }

   public List<Indicadores> getListIndicadores() {
      try {
         listIndicadores = administrarEmplVigenciaIndicador.listIndicadores();
         return listIndicadores;
      } catch (Exception e) {
         System.out.println("Error getListIndicadores " + e.toString());
         return null;
      }
   }

   public void setListIndicadores(List<Indicadores> setListIndicadores) {
      this.listIndicadores = setListIndicadores;
   }

   public Indicadores getIndicadorSeleccionado() {
      return indicadorSeleccionado;
   }

   public void setIndicadorSeleccionado(Indicadores setIndicadorSeleccionado) {
      this.indicadorSeleccionado = setIndicadorSeleccionado;
   }

   public List<Indicadores> getFiltrarListIndicadores() {
      return filtrarListIndicadores;
   }

   public void setFiltrarListIndicadores(List<Indicadores> setFiltrarListIndicadores) {
      this.filtrarListIndicadores = setFiltrarListIndicadores;
   }

   public List<VigenciasIndicadores> getListVigenciaIndicadorModificar() {
      return listVigenciaIndicadorModificar;
   }

   public void setListVigenciaIndicadorModificar(List<VigenciasIndicadores> setListVigenciaIndicadorModificar) {
      this.listVigenciaIndicadorModificar = setListVigenciaIndicadorModificar;
   }

   public VigenciasIndicadores getNuevaVigencia() {
      return nuevaVigencia;
   }

   public void setNuevaVigencia(VigenciasIndicadores setNuevaVigencia) {
      this.nuevaVigencia = setNuevaVigencia;
   }

   public List<VigenciasIndicadores> getListVigenciaIndicadorBorrar() {
      return listVigenciaIndicadorBorrar;
   }

   public void setListVigenciaIndicadorBorrar(List<VigenciasIndicadores> getListVigenciaIndicadorBorrar) {
      this.listVigenciaIndicadorBorrar = getListVigenciaIndicadorBorrar;
   }

   public VigenciasIndicadores getEditarVigenciaIndicador() {
      return editarVigenciaIndicador;
   }

   public void setEditarVigenciaIndicador(VigenciasIndicadores setEditarVigenciaIndicador) {
      this.editarVigenciaIndicador = setEditarVigenciaIndicador;
   }

   public VigenciasIndicadores getDuplicarVigenciaIndicador() {
      return duplicarVigenciaIndicador;
   }

   public void setDuplicarVigenciaIndicador(VigenciasIndicadores setDuplicarVigenciaIndicador) {
      this.duplicarVigenciaIndicador = setDuplicarVigenciaIndicador;
   }

   public List<VigenciasIndicadores> getListVigenciaIndicadorCrear() {
      return listVigenciaIndicadorCrear;
   }

   public void setListVigenciaIndicadorCrear(List<VigenciasIndicadores> setListVigenciaIndicadorCrear) {
      this.listVigenciaIndicadorCrear = setListVigenciaIndicadorCrear;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public BigInteger getBackUpSecRegistro() {
      return backUpSecRegistro;
   }

   public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
      this.backUpSecRegistro = backUpSecRegistro;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public VigenciasIndicadores getVigenciaTablaSeleccionada() {
//        getListVigenciasIndicadores();
//        if (listVigenciasIndicadores != null) {
//            int tam = listVigenciasIndicadores.size();
//            if (tam > 0) {
//                vigenciaTablaSeleccionada = listVigenciasIndicadores.get(0);
//            }
//        }
      return vigenciaTablaSeleccionada;
   }

   public void setVigenciaTablaSeleccionada(VigenciasIndicadores vigenciaTablaSeleccionada) {
      this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigencia");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroTipo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipos");
      infoRegistroTipo = String.valueOf(tabla.getRowCount());
      return infoRegistroTipo;
   }

   public String getInfoRegistroIndicador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovIndicador");
      infoRegistroIndicador = String.valueOf(tabla.getRowCount());
      return infoRegistroIndicador;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public void setInfoRegistroTipo(String infoRegistroTipo) {
      this.infoRegistroTipo = infoRegistroTipo;
   }

   public void setInfoRegistroIndicador(String infoRegistroIndicador) {
      this.infoRegistroIndicador = infoRegistroIndicador;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
