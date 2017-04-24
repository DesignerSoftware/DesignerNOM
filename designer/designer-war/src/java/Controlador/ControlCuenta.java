/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cuentas;
import Entidades.Empresas;
import Entidades.GruposTiposCC;
import Entidades.Rubrospresupuestales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCuentasInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlCuenta implements Serializable {

   @EJB
   AdministrarCuentasInterface administrarCuentas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //
   private List<Cuentas> listCuentas;
   private List<Cuentas> filtrarListCuentas;
   private Cuentas cuentaTablaSeleccionada;
   //
   private List<Empresas> listaEmpresas;
   private List<Empresas> filtrarListEmpresas;
   private Empresas empresaSeleccionada;
   private Empresas empresaActual;
   //
   private List<Rubrospresupuestales> listRubros;
   private List<Rubrospresupuestales> filtrarListRubros;
   private Rubrospresupuestales rubroSeleccionado;
   //
   private List<Cuentas> listCuentasTesoreria;
   private List<Cuentas> filtrarListCuentasTesoreria;
   private Cuentas cuentaSeleccionada;
   //
   private int tipoActualizacion;
   private int bandera;
   private Empresas backUpEmpresaActual;
   //Columnas Tabla VL
   private Column cuentaCodigo, cuentasDescripcion, cuentaContracuenta, cuentaRubro, cuentaManejaNit, cuentaManejaNitEmpleado,
           cuentaProrrateo, cuentaCodigoA, cuentaConsolidaNit, cuentaIncluyeShort, cuentaAsociadaSAP, cuentaSubCuenta, cuentaNaturaleza, cuentaTipo, cuentaCC, cuentaICC;
   //Otros
   private boolean aceptar;
   private List<Cuentas> listCuentasModificar;
   private boolean guardado;
   public Cuentas nuevoCuentas;
   private List<Cuentas> listCuentasCrear;
   private BigInteger l;
   private int k;
   private List<Cuentas> listCuentasBorrar;
   private Cuentas editarCuentas;
   private int cualCelda, tipoLista;
   private Cuentas duplicarCuentas;
   private boolean permitirIndex;
   private String nombreXML;
   private String nombreTabla;
   private boolean cambiosCuentas;
   private BigInteger backUpSecRegistroCuentas;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String cuenta, rubroP;
   private Cuentas cuentaActual;
   private String altoTabla;
   private String infoRegistro;
   private boolean activoDetalle;
   private String infoRegistroBuscarCuenta, infoRegistroRubro, infoRegistroContraCuenta, infoRegistroEmpresa;
   private String auxCodigoCuenta, auxDescripcionCuenta;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlCuenta() {
      empresaSeleccionada = new Empresas();
      activoDetalle = true;
      altoTabla = "265";
      cuentaActual = null;
      listRubros = null;
      listCuentasTesoreria = null;
      backUpEmpresaActual = new Empresas();
      listaEmpresas = null;
      nombreTablaRastro = "";
      backUp = null;
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      cuentaTablaSeleccionada = null;
      backUpSecRegistroCuentas = null;
      aceptar = true;
      listCuentasBorrar = new ArrayList<Cuentas>();
      listCuentasCrear = new ArrayList<Cuentas>();
      k = 0;
      listCuentasModificar = new ArrayList<Cuentas>();
      editarCuentas = new Cuentas();

      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevoCuentas = new Cuentas();
      nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
      nuevoCuentas.setContracuentatesoreria(new Cuentas());
      cuentaTablaSeleccionada = null;
      bandera = 0;
      permitirIndex = true;
      nombreTabla = ":formExportarCuentas:datosCuentasExportar";
      nombreXML = "CuentasXML";
      duplicarCuentas = new Cuentas();
      cambiosCuentas = false;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      activoDetalle = true;
      listCuentas = null;
      getListEmpresas();
      if (listaEmpresas != null) {
         if (listaEmpresas.size() > 0) {
            empresaActual = listaEmpresas.get(0);
            backUpEmpresaActual = empresaActual;
         }
      }
      empresaSeleccionada = empresaActual;
      getListCuentas();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      activoDetalle = true;
      listCuentas = null;
      getListEmpresas();
      if (listaEmpresas != null) {
         if (listaEmpresas.size() > 0) {
            empresaActual = listaEmpresas.get(0);
            backUpEmpresaActual = empresaActual;
         }
      }
      empresaSeleccionada = empresaActual;
      getListCuentas();
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
String pagActual = "cuenta";
         
         
         


         
         
         
         
         
         
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
         administrarCuentas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListEmpresas();
         if (listaEmpresas != null) {
            if (listaEmpresas.size() > 0) {
               empresaActual = listaEmpresas.get(0);
               backUpEmpresaActual = empresaActual;
            }
         }
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void obtenerEmpresa() {
      activoDetalle = true;
      listCuentas = null;
      getListEmpresas();
      if (listaEmpresas != null) {
         if (listaEmpresas.size() > 0) {
            empresaActual = listaEmpresas.get(0);
            backUpEmpresaActual = empresaActual;
         }
      }
      empresaSeleccionada = empresaActual;
      getListCuentas();
      contarRegistro();
   }

   public void seleccionarTipo(String tipo, Cuentas cuenta) {
      if (tipo.equals(" ")) {
         cuentaTablaSeleccionada.setTipo("NULL");
      } else if (tipo.equals("CtaXPagar")) {
         cuentaTablaSeleccionada.setTipo("CXP");
      } else if (tipo.equals("CtaXCobrar")) {
         cuentaTablaSeleccionada.setTipo("CXC");
      } else if (tipo.equals("NetoDeNomina")) {
         cuentaTablaSeleccionada.setTipo("NDN");
      } else if (tipo.equals("Puente")) {
         cuentaTablaSeleccionada.setTipo("PNT");
      }
      if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
         if (listCuentasModificar.isEmpty()) {
            listCuentasModificar.add(cuentaTablaSeleccionada);
         } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
            listCuentasModificar.add(cuentaTablaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambiosCuentas = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:datosCuenta");
   }

   public void seleccionarNaturaleza(String naturaleza, Cuentas cuenta) {
      if (naturaleza.equals(" ")) {
         cuentaTablaSeleccionada.setNaturaleza("NULL");
      } else if (naturaleza.equals("DEBITO")) {
         cuentaTablaSeleccionada.setNaturaleza("DEBITO");
      } else if (naturaleza.equals("CREDITO")) {
         cuentaTablaSeleccionada.setNaturaleza("CREDITO");
      }
      if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
         if (listCuentasModificar.isEmpty()) {
            listCuentasModificar.add(cuentaTablaSeleccionada);
         } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
            listCuentasModificar.add(cuentaTablaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambiosCuentas = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:datosCuenta");
   }

   public void modificarCuenta(Cuentas cuenta) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (validarDatosNull(0) == true) {
         if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
            if (listCuentasModificar.isEmpty()) {
               listCuentasModificar.add(cuentaTablaSeleccionada);
            } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
               listCuentasModificar.add(cuentaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;

               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosCuentas = true;
         activoDetalle = true;
      } else {
         cuentaTablaSeleccionada.setCodigo(auxCodigoCuenta);
         cuentaTablaSeleccionada.setDescripcion(auxDescripcionCuenta);
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCuenta').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosCuenta");
   }

   public void modificarCuentaAutocompletar(Cuentas cuenta, String confirmarCambio, String valorConfirmar) {
      cuentaTablaSeleccionada = cuenta;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONTRACUENTA")) {
         if (!valorConfirmar.isEmpty()) {
            cuentaTablaSeleccionada.getContracuentatesoreria().setCodigo(cuentaTablaSeleccionada.getContracuentatesoreria().getCodigo());
            for (int i = 0; i < listCuentasTesoreria.size(); i++) {
               if (listCuentasTesoreria.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cuentaTablaSeleccionada.setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
               listCuentasTesoreria.clear();
               getListCuentasTesoreria();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
               RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
//                cuentaTablaSeleccionada.setContracuentatesoreria(new Cuentas());
            cuentaTablaSeleccionada.getContracuentatesoreria().setCodigo("");
            cuentaTablaSeleccionada.getContracuentatesoreria().setDescripcion("");
            cuentaTablaSeleccionada.getContracuentatesoreria().setEmpresa(empresaActual);
            listCuentasTesoreria.clear();
            getListCuentasTesoreria();
         }
      }
      if (confirmarCambio.equalsIgnoreCase("RUBRO")) {
         if (!valorConfirmar.isEmpty()) {
            cuentaTablaSeleccionada.getRubropresupuestal().setDescripcion(cuentaTablaSeleccionada.getRubropresupuestal().getDescripcion());
            for (int i = 0; i < listRubros.size(); i++) {
               if (listRubros.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cuentaTablaSeleccionada.setRubropresupuestal(listRubros.get(indiceUnicoElemento));
               listRubros.clear();
               getListRubros();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:RubrosDialogo");
               RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
//                cuentaTablaSeleccionada.setRubropresupuestal(new Rubrospresupuestales());
            cuentaTablaSeleccionada.getRubropresupuestal().setDescripcion("");
            cuentaTablaSeleccionada.getRubropresupuestal().setPlaTipres("");
            cuentaTablaSeleccionada.getRubropresupuestal().setPlaGrupos("");
            cuentaTablaSeleccionada.getRubropresupuestal().setPlaConcep("");
            cuentaTablaSeleccionada.getRubropresupuestal().setPlaTipres("");
            cuentaTablaSeleccionada.getRubropresupuestal().setAreCodigo(Short.valueOf("1"));
            cuentaTablaSeleccionada.getRubropresupuestal().setGrupotipocc(new GruposTiposCC());
            listRubros.clear();
            getListRubros();
         }
      }
      if (coincidencias == 1) {
         if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
            if (listCuentasModificar.isEmpty()) {
               listCuentasModificar.add(cuentaTablaSeleccionada);
            } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
               listCuentasModificar.add(cuentaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         activoDetalle = true;
         cambiosCuentas = true;
      }
      RequestContext.getCurrentInstance().update("form:datosCuenta");
   }

   public void cambiarIndice(Cuentas cuenta, int celda) {
      if (permitirIndex == true) {
         cualCelda = celda;
         cuentaTablaSeleccionada = cuenta;
         cuentaActual = cuentaTablaSeleccionada;
         activoDetalle = false;

         cuentaTablaSeleccionada.getSecuencia();
         auxCodigoCuenta = cuentaTablaSeleccionada.getCodigo();
         auxDescripcionCuenta = cuentaTablaSeleccionada.getDescripcion();

         if (cualCelda == 0) {
            cuentaTablaSeleccionada.getSecuencia();
         } else if (cualCelda == 1) {
            cuentaTablaSeleccionada.getCodigo();
         } else if (cualCelda == 2) {
            cuentaTablaSeleccionada.getDescripcion();
         } else if (cualCelda == 5) {
            cuentaTablaSeleccionada.getContracuentatesoreria().getCodigo();
         } else if (cualCelda == 6) {
            cuentaTablaSeleccionada.getRubropresupuestal().getDescripcion();
         } else if (cualCelda == 12) {
            cuentaTablaSeleccionada.getCodigoalternativo();
         }
      }
   }

   public void guardadoGeneral() {
      if (cambiosCuentas == true) {
         guardarCambiosCuenta();
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosCuenta() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listCuentasBorrar.isEmpty()) {

               System.out.println("entró al administrar de borrar");
               administrarCuentas.borrarCuentas(listCuentasBorrar);
               listCuentasBorrar.clear();
            }
            if (!listCuentasCrear.isEmpty()) {
               administrarCuentas.crearCuentas(listCuentasCrear);
               listCuentasCrear.clear();
            }
            if (!listCuentasModificar.isEmpty()) {
               System.out.println("entró a guardarCambiosCuenta.listCuentaModificar : " + listCuentasModificar.size());
               for (int i = 0; i < listCuentasModificar.size(); i++) {
                  if (listCuentasModificar.get(i).getRubropresupuestal().getDescripcion().equals("") || listCuentasModificar.get(i).getRubropresupuestal().getDescripcion().isEmpty()) {
                     listCuentasModificar.get(i).setRubropresupuestal(null);
                  }

                  if (listCuentasModificar.get(i).getContracuentatesoreria().getCodigo().equals("") || listCuentasModificar.get(i).getContracuentatesoreria().getCodigo().isEmpty()) {
                     listCuentasModificar.get(i).setContracuentatesoreria(null);
                  }
               }
               administrarCuentas.modificarCuentas(listCuentasModificar);
               listCuentasModificar.clear();
            }
            listCuentas = null;
            getListCuentas();
            contarRegistro();
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            k = 0;
            cuentaTablaSeleccionada = null;
            cambiosCuentas = false;
            activoDetalle = true;
            guardado = true;
            cambiosCuentas = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCuenta Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         altoTabla = "265";
         FacesContext c = FacesContext.getCurrentInstance();
         cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
         cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
         cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
         cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
         cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
         cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
         cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
         cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
         cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
         cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
         cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
         cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
         cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
         cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
         cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
         cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
         cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
         cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
         cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
         cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
         cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
         cuentaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
         cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
         cuentaTipo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
         cuentaCC.setFilterStyle("display: none; visibility: hidden;");
         cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
         cuentaICC.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         bandera = 0;
         filtrarListCuentas = null;
         tipoLista = 0;
      }
      listCuentasBorrar.clear();
      listCuentasCrear.clear();
      listCuentasModificar.clear();
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      cuentaTablaSeleccionada = null;
      k = 0;
      listCuentas = null;
      listCuentasTesoreria = null;
      guardado = true;
      cambiosCuentas = false;
      cuentaActual = null;
      getListCuentas();
      contarRegistro();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      getListCuentasTesoreria();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosCuenta");
   }

   public void editarCelda() {
      if (cuentaTablaSeleccionada != null) {
         System.out.println("entró a editar celda");
         editarCuentas = cuentaTablaSeleccionada;
         System.out.println("editarCuentas : " + editarCuentas.getDescripcion());
         System.out.println("cual celda en editarCelda : " + cualCelda);
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCuentaD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoCuentaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCuentaD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionCuentaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCuentaTCuentaD");
            RequestContext.getCurrentInstance().execute("PF('editarCuentaTCuentaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarContraCuentaTesoreria");
            RequestContext.getCurrentInstance().execute("PF('editarContraCuentaTesoreria').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarRubroCuentaD");
            RequestContext.getCurrentInstance().execute("PF('editarRubroCuentaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoACuentaD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoACuentaD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

//        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void validarIngresoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCuenta");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCuenta').show()");

   }

   public void validarDuplicadoRegistro() {
      if (cuentaTablaSeleccionada != null) {
         duplicarCuenta();
      }
   }

   public void validarBorradoRegistro() {
      if (cuentaTablaSeleccionada != null) {
         borrarCuenta();
      }
   }

   public boolean validarDatosNull(int i) {
      boolean retorno = true;
      if (i == 0) {
         Cuentas aux = null;
         if (tipoLista == 0) {
            aux = cuentaTablaSeleccionada;
         } else {
            aux = cuentaTablaSeleccionada;
         }
         if (aux.getDescripcion() == null) {
            retorno = false;
         } else if (aux.getDescripcion().isEmpty()) {
            retorno = false;
         }
         if (aux.getCodigo() == null) {
            retorno = false;
         } else if (aux.getCodigo().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoCuentas.getDescripcion() == null) {
            retorno = false;
         } else if (nuevoCuentas.getDescripcion().isEmpty()) {
            retorno = false;
         }
         if (nuevoCuentas.getCodigo() == null) {
            retorno = false;
         } else if (nuevoCuentas.getCodigo().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarCuentas.getDescripcion() == null) {
            retorno = false;
         } else if (duplicarCuentas.getDescripcion().isEmpty()) {
            retorno = false;
         }
         if (duplicarCuentas.getCodigo() == null) {
            retorno = false;
         } else if (duplicarCuentas.getCodigo().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void agregarNuevoCuenta() {
      if (validarDatosNull(1) == true) {
         if (bandera == 1) {
            altoTabla = "265";
            FacesContext c = FacesContext.getCurrentInstance();
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
            cuentaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
            cuentaTipo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
            cuentaCC.setFilterStyle("display: none; visibility: hidden;");
            cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
            cuentaICC.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 0;
            filtrarListCuentas = null;
            tipoLista = 0;
         }
         //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
         k++;
         BigInteger var = BigInteger.valueOf(k);
         nuevoCuentas.setSecuencia(var);
         nuevoCuentas.setEmpresa(empresaActual);
         listCuentasCrear.add(nuevoCuentas);
         listCuentas.add(nuevoCuentas);
         cuentaTablaSeleccionada = nuevoCuentas;
         contarRegistro();
         nuevoCuentas = new Cuentas();
         nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
         nuevoCuentas.setContracuentatesoreria(new Cuentas());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCuenta').hide()");
         cambiosCuentas = true;
         activoDetalle = true;
//            RequestContext.getCurrentInstance().update("form:DETALLES");
      } else {
         RequestContext.getCurrentInstance().execute("errorDatosNullCuenta').show()");
      }
   }

   public void limpiarNuevoCuenta() {
      nuevoCuentas = new Cuentas();
      nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
      nuevoCuentas.setContracuentatesoreria(new Cuentas());
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void duplicarCuenta() {
      duplicarCuentas = new Cuentas();
      duplicarCuentas.setCodigo(cuentaTablaSeleccionada.getCodigo());
      duplicarCuentas.setDescripcion(cuentaTablaSeleccionada.getDescripcion());
      duplicarCuentas.setNaturaleza(cuentaTablaSeleccionada.getNaturaleza());
      duplicarCuentas.setContracuentatesoreria(cuentaTablaSeleccionada.getContracuentatesoreria());
      duplicarCuentas.setTipo(cuentaTablaSeleccionada.getTipo());
      duplicarCuentas.setRubropresupuestal(cuentaTablaSeleccionada.getRubropresupuestal());
      duplicarCuentas.setManejanit(cuentaTablaSeleccionada.getManejanit());
      duplicarCuentas.setManejanitempleado(cuentaTablaSeleccionada.getManejanitempleado());
      duplicarCuentas.setProrrateo(cuentaTablaSeleccionada.getProrrateo());
      duplicarCuentas.setManejacentrocosto(cuentaTablaSeleccionada.getManejacentrocosto());
      duplicarCuentas.setIncluyecentrocostocodigocuenta(cuentaTablaSeleccionada.getIncluyecentrocostocodigocuenta());
      duplicarCuentas.setCodigoalternativo(cuentaTablaSeleccionada.getCodigoalternativo());
      duplicarCuentas.setConsolidanitempresa(cuentaTablaSeleccionada.getConsolidanitempresa());
      duplicarCuentas.setCuentaasociadasap(cuentaTablaSeleccionada.getCuentaasociadasap());
      duplicarCuentas.setManejasubcuenta(cuentaTablaSeleccionada.getManejasubcuenta());
      duplicarCuentas.setCheckAsociadaSAP(cuentaTablaSeleccionada.isCheckAsociadaSAP());
      duplicarCuentas.setCheckCCEmpleado(cuentaTablaSeleccionada.isCheckCCEmpleado());
      duplicarCuentas.setCheckConsolidaNITEmpresa(cuentaTablaSeleccionada.isCheckConsolidaNITEmpresa());
      duplicarCuentas.setCheckManejaNit(cuentaTablaSeleccionada.isCheckManejaNit());
      duplicarCuentas.setCheckProrrateo(cuentaTablaSeleccionada.isCheckProrrateo());
      duplicarCuentas.setCheckShortName(cuentaTablaSeleccionada.isCheckShortName());
      duplicarCuentas.setCheckSubCuenta(cuentaTablaSeleccionada.isCheckSubCuenta());

//        if (duplicarCuentas.getRubropresupuestal().getSecuencia() == null) {
//            duplicarCuentas.setRubropresupuestal(null);
//        }
//        if (duplicarCuentas.getContracuentatesoreria().getSecuencia() == null) {
//            duplicarCuentas.setContracuentatesoreria(null);
//        }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroCuenta");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCuenta').show()");
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void confirmarDuplicar() {
      int aux = 0;
      if (validarDatosNull(2) == true) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "265";
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
            cuentaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
            cuentaTipo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
            cuentaCC.setFilterStyle("display: none; visibility: hidden;");
            cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
            cuentaICC.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 0;
            filtrarListCuentas = null;
            tipoLista = 0;
         }

         for (int i = 0; i < listCuentas.size(); i++) {
            if (listCuentas.get(i).getCodigo().equals(duplicarCuentas.getCodigo())) {
               RequestContext.getCurrentInstance().update("formularioDialogos:existeCuenta");
               RequestContext.getCurrentInstance().execute("PF('existeCuenta').show()");
               aux++;
            }
         }

         if (aux == 0) {
            k++;
            BigInteger var = BigInteger.valueOf(k);
            duplicarCuentas.setSecuencia(var);
            duplicarCuentas.setEmpresa(empresaActual);
            listCuentasCrear.add(duplicarCuentas);
            listCuentas.add(duplicarCuentas);
            cuentaTablaSeleccionada = duplicarCuentas;
            duplicarCuentas = new Cuentas();
            contarRegistro();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCuenta').hide()");
            cambiosCuentas = true;
            activoDetalle = true;
         }

      } else {
         RequestContext.getCurrentInstance().execute("errorDatosNullCuenta').show()");
      }
   }

   public void limpiarDuplicarCuenta() {
      duplicarCuentas = new Cuentas();
      nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
      nuevoCuentas.setContracuentatesoreria(new Cuentas());
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void borrarCuenta() {
      if (cuentaTablaSeleccionada != null) {
         if (!listCuentasModificar.isEmpty() && listCuentasModificar.contains(cuentaTablaSeleccionada)) {
            int modIndex = listCuentasModificar.indexOf(cuentaTablaSeleccionada);
            listCuentasModificar.remove(modIndex);
            listCuentasBorrar.add(cuentaTablaSeleccionada);
         } else if (!listCuentasCrear.isEmpty() && listCuentasCrear.contains(cuentaTablaSeleccionada)) {
            int crearIndex = listCuentasCrear.indexOf(cuentaTablaSeleccionada);
            listCuentasCrear.remove(crearIndex);
         } else {
            listCuentasBorrar.add(cuentaTablaSeleccionada);
         }
         listCuentas.remove(cuentaTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListCuentas.remove(cuentaTablaSeleccionada);
         }
         contarRegistro();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCuenta");

         cuentaTablaSeleccionada = null;
         activoDetalle = true;
         cambiosCuentas = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void activarCtrlF11() {
      filtradoCuenta();
   }

   public void filtradoCuenta() {

      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "240";
         cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
         cuentaCodigo.setFilterStyle("width: 85% !important");
         cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
         cuentasDescripcion.setFilterStyle("width: 85% !important");
         cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
         cuentaNaturaleza.setFilterStyle("width: 85% !important");
         cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
         cuentaTipo.setFilterStyle("width: 85% !important");
         cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
         cuentaCC.setFilterStyle("width: 85% !important");
         cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
         cuentaICC.setFilterStyle("width: 85% !important");
         cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
         cuentaContracuenta.setFilterStyle("width: 85% !important");
         cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
         cuentaRubro.setFilterStyle("width: 85% !important");
         cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
         cuentaManejaNit.setFilterStyle("width: 85% !important");
         cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
         cuentaManejaNitEmpleado.setFilterStyle("width: 85% !important");
         cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
         cuentaProrrateo.setFilterStyle("width: 85% !important");
         cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
         cuentaCodigoA.setFilterStyle("width: 85% !important");
         cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
         cuentaConsolidaNit.setFilterStyle("width: 85% !important");
         cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
         cuentaIncluyeShort.setFilterStyle("width: 85% !important");
         cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
         cuentaAsociadaSAP.setFilterStyle("width: 85% !important");
         cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
         cuentaSubCuenta.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         bandera = 1;
      } else if (bandera == 1) {
         altoTabla = "265";
         cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
         cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
         cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
         cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
         cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
         cuentaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
         cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
         cuentaTipo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
         cuentaCC.setFilterStyle("display: none; visibility: hidden;");
         cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
         cuentaICC.setFilterStyle("display: none; visibility: hidden;");
         cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
         cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
         cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
         cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
         cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
         cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
         cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
         cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
         cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
         cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
         cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
         cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
         cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
         cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
         cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
         cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         bandera = 0;
         filtrarListCuentas = null;
         tipoLista = 0;
      }

   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         altoTabla = "265";
         FacesContext c = FacesContext.getCurrentInstance();
         cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
         cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
         cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
         cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
         cuentaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaNaturaleza");
         cuentaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
         cuentaTipo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaTipo");
         cuentaTipo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCC");
         cuentaCC.setFilterStyle("display: none; visibility: hidden;");
         cuentaICC = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaICC");
         cuentaICC.setFilterStyle("display: none; visibility: hidden;");
         cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
         cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
         cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
         cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
         cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
         cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
         cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
         cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
         cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
         cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
         cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
         cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
         cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
         cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
         cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
         cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
         cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
         cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         bandera = 0;
         filtrarListCuentas = null;
         tipoLista = 0;
      }
      listCuentasBorrar.clear();
      listCuentasCrear.clear();
      listCuentasModificar.clear();
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      cuentaTablaSeleccionada = null;
      listCuentas = null;
      listaEmpresas = null;
      empresaActual = null;
      k = 0;
      listCuentas = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void asignarIndex(Cuentas cuenta, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      cuentaTablaSeleccionada = cuenta;
      if (LND == 0) {
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
         RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:RubrosDialogo");
         RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
      }
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cuentaTablaSeleccionada != null) {
         System.out.println("cual celda en lista valores Boton : " + cualCelda);
         if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("form:RubrosDialogo");
            RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CONTRACUENTA")) {
         if (tipoNuevo == 1) {
            cuenta = nuevoCuentas.getContracuentatesoreria().getDescripcion();
         } else if (tipoNuevo == 2) {
            cuenta = duplicarCuentas.getContracuentatesoreria().getDescripcion();
         }
      } else if (Campo.equals("RUBRO")) {
         if (tipoNuevo == 1) {
            rubroP = nuevoCuentas.getRubropresupuestal().getDescripcion();
         } else if (tipoNuevo == 2) {
            rubroP = duplicarCuentas.getRubropresupuestal().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoCuenta(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONTRACUENTA")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoCuentas.getContracuentatesoreria().setDescripcion(cuenta);
            } else if (tipoNuevo == 2) {
               duplicarCuentas.getContracuentatesoreria().setDescripcion(cuenta);
            }
            for (int i = 0; i < listCuentasTesoreria.size(); i++) {
               if (listCuentasTesoreria.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoCuentas.setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
               } else if (tipoNuevo == 2) {
                  duplicarCuentas.setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
               }
               listCuentasTesoreria.clear();
               getListCuentasTesoreria();
            } else {
               RequestContext.getCurrentInstance().update("form:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
               }
            }
         } else {
            if (tipoNuevo == 1) {
               nuevoCuentas.setContracuentatesoreria(new Cuentas());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
            } else if (tipoNuevo == 2) {
               duplicarCuentas.setContracuentatesoreria(new Cuentas());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
            }
            listCuentasTesoreria.clear();
            getListCuentasTesoreria();
         }
      } else if (confirmarCambio.equalsIgnoreCase("RUBROS")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoCuentas.getRubropresupuestal().setDescripcion(rubroP);
            } else if (tipoNuevo == 2) {
               duplicarCuentas.getRubropresupuestal().setDescripcion(rubroP);
            }
            for (int i = 0; i < listRubros.size(); i++) {
               if (listRubros.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoCuentas.setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
               } else if (tipoNuevo == 2) {
                  duplicarCuentas.setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
               }
               listRubros.clear();
               getListRubros();
            } else {
               RequestContext.getCurrentInstance().update("form:CiudadDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
               }
            }
         } else {
            if (tipoNuevo == 1) {
               nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
            } else if (tipoNuevo == 2) {
               duplicarCuentas.setRubropresupuestal(new Rubrospresupuestales());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
            }
            listRubros.clear();
            getListRubros();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarCuenta() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            cuentaTablaSeleccionada.setContracuentatesoreria(cuentaSeleccionada);
            if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
               if (listCuentasModificar.isEmpty()) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               }
            }
         } else {
            cuentaTablaSeleccionada.setContracuentatesoreria(cuentaSeleccionada);
            if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
               if (listCuentasModificar.isEmpty()) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosCuentas = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosCuenta");
      } else if (tipoActualizacion == 1) {
         nuevoCuentas.setContracuentatesoreria(cuentaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCtaTesoreriaC");
      } else if (tipoActualizacion == 2) {
         duplicarCuentas.setContracuentatesoreria(cuentaSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaCtaTesoreriaC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionada = null;
      aceptar = true;
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
      RequestContext.getCurrentInstance().update("form:lovContracuenta");
      RequestContext.getCurrentInstance().update("form:aceptarCC");
      context.reset("form:lovContracuenta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContracuenta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').hide()");

   }

   public void cancelarCambioCuenta() {
      filtrarListCuentas = null;
      cuentaSeleccionada = null;
      aceptar = true;
      activoDetalle = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
      RequestContext.getCurrentInstance().update("form:lovContracuenta");
      RequestContext.getCurrentInstance().update("form:aceptarCC");
      context.reset("form:lovContracuenta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContracuenta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').hide()");
   }

   public void actualizarRubro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            cuentaTablaSeleccionada.setRubropresupuestal(rubroSeleccionado);
            if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
               if (listCuentasModificar.isEmpty()) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               }
            }
         } else {
            cuentaTablaSeleccionada.setRubropresupuestal(rubroSeleccionado);
            if (!listCuentasCrear.contains(cuentaTablaSeleccionada)) {
               if (listCuentasModificar.isEmpty()) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               } else if (!listCuentasModificar.contains(cuentaTablaSeleccionada)) {
                  listCuentasModificar.add(cuentaTablaSeleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosCuentas = true;
         permitirIndex = true;

         RequestContext.getCurrentInstance().update("form:datosCuenta");
      } else if (tipoActualizacion == 1) {
         nuevoCuentas.setRubropresupuestal(rubroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaRubroC");
      } else if (tipoActualizacion == 2) {
         duplicarCuentas.setRubropresupuestal(rubroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaRubroC");
      }
      filtrarListRubros = null;
      rubroSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:RubrosDialogo");
      RequestContext.getCurrentInstance().update("form:lovRubros");
      RequestContext.getCurrentInstance().update("form:aceptarRP");
      context.reset("form:lovRubros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovRubros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').hide()");
   }

   public void cancelarCambioRubro() {
      filtrarListRubros = null;
      rubroSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:RubrosDialogo");
      RequestContext.getCurrentInstance().update("form:lovRubros");
      RequestContext.getCurrentInstance().update("form:aceptarRP");
      context.reset("form:lovRubros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovRubros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').hide()");
   }

   public String exportXML() {
      if (cuentaTablaSeleccionada != null) {
         nombreTabla = ":formExportarCuentas:datosCuentasExportar";
         nombreXML = "CuentasXML";
      }
      return nombreTabla;
   }

   public void validarExportPDF() throws IOException {
      if (cuentaTablaSeleccionada != null) {
         exportPDFT();
      }
   }

   public void exportPDFT() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCuentas:datosCuentasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CuentasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void verificarExportXLS() throws IOException {
      if (cuentaTablaSeleccionada != null) {
         exportXLST();
      }

   }

   public void exportXLST() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCuentas:datosCuentasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CuentasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistro();
   }

   public void dialogoSeleccionarCuenta() {
      getListCuentasTesoreria();
      RequestContext.getCurrentInstance().update("form:BuscarCuentasDialogo");
      RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').show()");
   }

   public void validarSeleccionCuenta() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosCuentas == false) {
         listCuentas = null;
         listCuentas = new ArrayList<Cuentas>();
         listCuentas.add(cuentaSeleccionada);
         cuentaSeleccionada = new Cuentas();
         filtrarListCuentasTesoreria = null;
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         /*
             RequestContext.getCurrentInstance().update("form:BuscarCuentasDialogo");
             RequestContext.getCurrentInstance().update("form:lovCuentas");
             RequestContext.getCurrentInstance().update("form:aceptarBC");*/
         context.reset("form:lovCuentas:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').hide()");
      } else {
         cuentaSeleccionada = new Cuentas();
         filtrarListCuentasTesoreria = null;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cancelarSeleccionCuenta() {
      RequestContext context = RequestContext.getCurrentInstance();
      cuentaSeleccionada = new Cuentas();
      filtrarListCuentasTesoreria = null;
      context.reset("form:lovCuentas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').hide()");
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosCuentas == false) {
         listCuentas = null;
         getListCuentas();
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         cuentaActual = null;
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
   public void verificarRastroTabla() {
      verificarRastroTerceros();
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   //Verificar Rastro Vigencia Sueldos
   public void verificarRastroTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cuentaTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(cuentaTablaSeleccionada.getSecuencia(), "CUENTAS");
         backUpSecRegistroCuentas = cuentaTablaSeleccionada.getSecuencia();
         backUp = cuentaTablaSeleccionada.getSecuencia();
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
      } else if (administrarRastros.verificarHistoricosTabla("CUENTAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      cuentaTablaSeleccionada = null;
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
   }

   public void lovEmpresas() {
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      cualCelda = -1;
      RequestContext.getCurrentInstance().execute("EmpresasDialogo').show()");
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosCuentas == false) {
         aceptar = true;
         empresaActual = empresaSeleccionada;
         backUpEmpresaActual = empresaActual;
         listCuentasTesoreria = null;
         cuentaActual = null;
         listCuentas = null;
         getListCuentas();
         contarRegistro();
         getListCuentasTesoreria();
         RequestContext.getCurrentInstance().update("form:nombreEmpresa");
         RequestContext.getCurrentInstance().update("form:nitEmpresa");
         RequestContext.getCurrentInstance().update("form:datosCuenta");
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         //empresaSeleccionada = new Empresas();
         filtrarListEmpresas = null;

         RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
         RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
         RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
         context.reset("formularioDialogos:lovEmpresas:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cancelarCambioEmpresa() {
      cuentaTablaSeleccionada = null;
      activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
      filtrarListEmpresas = null;
      RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      RequestContext.getCurrentInstance().reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void contarRegistro() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistroLov() {
      RequestContext.getCurrentInstance().update("form:infoRegistroBuscarCuenta");
   }

   public void contarRegistroContraCuenta() {
      RequestContext.getCurrentInstance().update("form:infoRegistroContraCuenta");
   }

   public void contarRegistroRubro() {
      RequestContext.getCurrentInstance().update("form:infoRegistroRubro");
   }

   public void contarRegistroEmpresa() {
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
   }

   public void verDetalle(Cuentas cuentaS) {
      cuentaSeleccionada = cuentaS;
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "detalleCuenta");
      cambiarIndice(cuentaS, 0);
   }

   //GET - SET 
   public List<Cuentas> getListCuentas() {
      try {
         if (listCuentas == null) {
            if (empresaActual.getSecuencia() != null) {
               listCuentas = administrarCuentas.consultarCuentasEmpresa(empresaActual.getSecuencia());
               if (listCuentas != null) {
                  for (int i = 0; i < listCuentas.size(); i++) {
                     if (listCuentas.get(i).getRubropresupuestal() == null) {
                        listCuentas.get(i).setRubropresupuestal(new Rubrospresupuestales());
                     }
                     if (listCuentas.get(i).getContracuentatesoreria() == null) {
                        listCuentas.get(i).setContracuentatesoreria(new Cuentas());
                     }
                  }
               }
            }
         }
         return listCuentas;
      } catch (Exception e) {
         System.out.println("Error getListCuentas " + e.toString());
         return null;
      }
   }

   public void setListCuentas(List<Cuentas> t) {
      this.listCuentas = t;
   }

   public List<Cuentas> getFiltrarListCuentas() {
      return filtrarListCuentas;
   }

   public void setFiltrarListCuentas(List<Cuentas> t) {
      this.filtrarListCuentas = t;
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

   public BigInteger getBackUpSecRegistroCuentas() {
      return backUpSecRegistroCuentas;
   }

   public void setBackUpSecRegistroCuentas(BigInteger b) {
      this.backUpSecRegistroCuentas = b;
   }

   public List<Cuentas> getListCuentasModificar() {
      return listCuentasModificar;
   }

   public void setListCuentasModificar(List<Cuentas> listTerceroCuentas) {
      this.listCuentasModificar = listTerceroCuentas;
   }

   public Cuentas getNuevoCuentas() {
      return nuevoCuentas;
   }

   public void setNuevoCuentas(Cuentas nuevoCuentas) {
      this.nuevoCuentas = nuevoCuentas;
   }

   public List<Cuentas> getListCuentasCrear() {
      return listCuentasCrear;
   }

   public void setListCuentasCrear(List<Cuentas> listCuentasCrear) {
      this.listCuentasCrear = listCuentasCrear;
   }

   public List<Cuentas> getListCuentasBorrar() {
      return listCuentasBorrar;
   }

   public void setListCuentasBorrar(List<Cuentas> listCuentasBorrar) {
      this.listCuentasBorrar = listCuentasBorrar;
   }

   public Cuentas getEditarCuentas() {
      return editarCuentas;
   }

   public void setEditarCuentas(Cuentas editarCuentas) {
      this.editarCuentas = editarCuentas;
   }

   public Cuentas getDuplicarCuentas() {
      return duplicarCuentas;
   }

   public void setDuplicarCuentas(Cuentas duplicarCuentas) {
      this.duplicarCuentas = duplicarCuentas;
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

   public List<Empresas> getListEmpresas() {
      listaEmpresas = administrarCuentas.consultarEmpresas();
      return listaEmpresas;
   }

   public void setListEmpresas(List<Empresas> listEmpresas) {
      this.listaEmpresas = listEmpresas;
   }

   public List<Empresas> getFiltrarListEmpresas() {
      return filtrarListEmpresas;
   }

   public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
      this.filtrarListEmpresas = filtrarListEmpresas;
   }

   public Empresas getEmpresaActual() {
      return empresaActual;
   }

   public void setEmpresaActual(Empresas empresaActual) {
      this.empresaActual = empresaActual;
   }

   public Empresas getBackUpEmpresaActual() {
      return backUpEmpresaActual;
   }

   public void setBackUpEmpresaActual(Empresas backUpEmpresaActual) {
      this.backUpEmpresaActual = backUpEmpresaActual;
   }

   public List<Rubrospresupuestales> getListRubros() {
      listRubros = administrarCuentas.consultarLOVRubros();

      return listRubros;
   }

   public void setListRubros(List<Rubrospresupuestales> listCiudades) {
      this.listRubros = listCiudades;
   }

   public List<Rubrospresupuestales> getFiltrarListRubros() {
      return filtrarListRubros;
   }

   public void setFiltrarListRubros(List<Rubrospresupuestales> filtrar) {
      this.filtrarListRubros = filtrar;
   }

   public Rubrospresupuestales getRubroSeleccionado() {
      return rubroSeleccionado;
   }

   public void setRubroSeleccionado(Rubrospresupuestales seleccionado) {
      this.rubroSeleccionado = seleccionado;
   }

   public List<Cuentas> getListCuentasTesoreria() {
      if (empresaActual.getSecuencia() != null) {
         listCuentasTesoreria = administrarCuentas.consultarCuentasEmpresa(empresaActual.getSecuencia());
      }
      return listCuentasTesoreria;
   }

   public void setListCuentasTesoreria(List<Cuentas> CuentasTesoreria) {
      this.listCuentasTesoreria = CuentasTesoreria;
   }

   public List<Cuentas> getFiltrarListCuentasTesoreria() {
      return filtrarListCuentasTesoreria;
   }

   public void setFiltrarListCuentasTesoreria(List<Cuentas> filtrarList) {
      this.filtrarListCuentasTesoreria = filtrarList;
   }

   public Cuentas getCuentaSeleccionada() {
      return cuentaSeleccionada;
   }

   public void setCuentaSeleccionada(Cuentas seleccionado) {
      this.cuentaSeleccionada = seleccionado;
   }

   public Cuentas getCuentaActual() {
      return cuentaActual;
   }

   public void setCuentaActual(Cuentas cuentaActual) {
      this.cuentaActual = cuentaActual;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCuenta");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isActivoDetalle() {
      return activoDetalle;
   }

   public void setActivoDetalle(boolean activoDetalle) {
      this.activoDetalle = activoDetalle;
   }

   public String getInfoRegistroBuscarCuenta() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCuentas");
      infoRegistroBuscarCuenta = String.valueOf(tabla.getRowCount());
      return infoRegistroBuscarCuenta;
   }

   public void setInfoRegistroBuscarCuenta(String infoRegistroBuscarCuenta) {
      this.infoRegistroBuscarCuenta = infoRegistroBuscarCuenta;
   }

   public String getInfoRegistroRubro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovRubros");
      infoRegistroRubro = String.valueOf(tabla.getRowCount());
      return infoRegistroRubro;
   }

   public void setInfoRegistroRubro(String infoRegistroRubro) {
      this.infoRegistroRubro = infoRegistroRubro;
   }

   public String getInfoRegistroContraCuenta() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovContracuenta");
      infoRegistroContraCuenta = String.valueOf(tabla.getRowCount());
      return infoRegistroContraCuenta;
   }

   public void setInfoRegistroContraCuenta(String infoRegistroContraCuenta) {
      this.infoRegistroContraCuenta = infoRegistroContraCuenta;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public Cuentas getCuentaTablaSeleccionada() {
      getListCuentas();
      if (listCuentas != null) {
         int tam = listCuentas.size();
         if (tam > 0) {
            cuentaTablaSeleccionada = listCuentas.get(0);
         }
      }
      return cuentaTablaSeleccionada;
   }

   public void setCuentaTablaSeleccionada(Cuentas cuentaTablaSeleccionada) {
      this.cuentaTablaSeleccionada = cuentaTablaSeleccionada;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

}
