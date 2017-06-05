/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Cargos;
import Entidades.Estructuras;
import Entidades.VigenciasArps;
import Entidades.VigenciasCargos;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasArpsInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlVigenciasArps")
@SessionScoped
public class ControlVigenciasArps implements Serializable {

   @EJB
   AdministrarVigenciasArpsInterface administrarVigenciasArps;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private VigenciasArps vigenciaSeleccionada;
   private List<VigenciasArps> listaVigenciasArps;
   private List<VigenciasArps> filtrarVigenciasArps;

   private Estructuras estructuraLovSeleccionada;
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> filtrarEstructuras;

   private Cargos cargoLovSeleccionado;
   private List<Cargos> lovCargos;
   private List<Cargos> filtrarCargos;

   private String altoTabla, infoRegistro, infoRegistroLovCargos, infoRegistroLovEstructuras;

   private int tipoActualizacion;//Activo/Desactivo Crtl + F11
   private int bandera;
   private int cualCelda, tipoLista;
   private boolean aceptar, guardado;
   private BigInteger l;
   private int k;
   String estructuraBkp, cargoBkp;
   Date fechaIniBkp, fechaFinBkp;
   BigDecimal porcentajeBkp;

   private List<VigenciasArps> listaBorrar;
   private List<VigenciasArps> listaCrear;
   private List<VigenciasArps> listaModificar;
   private VigenciasArps nuevaVig, duplicarVig;

   private Column columnEstructura, columnCargo, columnInicial, columnFinal, columnPorcentaje;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlVigenciasArps
    */
   public ControlVigenciasArps() {
      bandera = 0;
      aceptar = true;
      k = 0;
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaVig = new VigenciasArps();
      duplicarVig = new VigenciasArps();
      listaBorrar = new ArrayList<VigenciasArps>();
      listaCrear = new ArrayList<VigenciasArps>();
      listaModificar = new ArrayList<VigenciasArps>();
      altoTabla = "310";
      vigenciaSeleccionada = null;
      listaVigenciasArps = null;
      lovCargos = null;
      lovEstructuras = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarVigenciasArps.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct ControlVigenciasArps: " + e);
         System.out.println("Causa: " + e.getCause());
      }
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
      String pagActual = "vigenciasarps";
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
      lovCargos = null;
      lovEstructuras = null;
   }

   public void modificarV(VigenciasArps vArp, String campo, String valor) {
      vigenciaSeleccionada = vArp;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("ESTRUCTURA")) {
         vigenciaSeleccionada.setNombreEstructura(estructuraBkp);
         for (int i = 0; i < lovEstructuras.size(); i++) {
            if (lovEstructuras.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setEstructura(lovEstructuras.get(indiceUnicoElemento).getSecuencia());
            vigenciaSeleccionada.setNombreEstructura(lovEstructuras.get(indiceUnicoElemento).getNombre());
            lovEstructuras.clear();
            getLovEstructuras();
         } else {
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
            RequestContext.getCurrentInstance().update("form:DatosVigencias");
            tipoActualizacion = 0;
         }
      } else if (campo.equalsIgnoreCase("CARGO")) {
         vigenciaSeleccionada.setNombreCargo(cargoBkp);
         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setCargo(lovCargos.get(indiceUnicoElemento).getSecuencia());
            vigenciaSeleccionada.setNombreCargo(lovCargos.get(indiceUnicoElemento).getNombre());
         } else {
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
            RequestContext.getCurrentInstance().update("form:DatosVigencias");
            tipoActualizacion = 0;
         }
      } else if (campo.equalsIgnoreCase("INI")) {
         int control = 0;
         for (int i = 0; i < listaVigenciasArps.size(); i++) {
            if ((listaVigenciasArps.get(i).getFechainicial().compareTo(vigenciaSeleccionada.getFechainicial()) == 0)
                    && (!listaVigenciasArps.get(i).getSecuencia().equals(vigenciaSeleccionada.getSecuencia()))) {
               control++;
               vigenciaSeleccionada.setFechainicial(fechaIniBkp);
            }
         }
         if (control == 0) {
            if (!listaCrear.contains(vigenciaSeleccionada)) {
               if (listaModificar.isEmpty()) {
                  listaModificar.add(vigenciaSeleccionada);
               } else if (!listaModificar.contains(vigenciaSeleccionada)) {
                  listaModificar.add(vigenciaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show();");
            RequestContext.getCurrentInstance().execute("PF('form:DatosVigencias");
         }
      }
      if (coincidencias == 1) {
         if (!listaCrear.contains(vigenciaSeleccionada)) {
            if (listaModificar.isEmpty()) {
               listaModificar.add(vigenciaSeleccionada);
            } else if (!listaModificar.contains(vigenciaSeleccionada)) {
               listaModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:DatosVigencias");
      }
   }

   public void cambiarIndice(VigenciasArps vARP, int celda) {
      vigenciaSeleccionada = vARP;
      cualCelda = celda;
      if (cualCelda == 0) {
         estructuraBkp = vigenciaSeleccionada.getNombreEstructura();
      } else if (cualCelda == 1) {
         cargoBkp = vigenciaSeleccionada.getNombreCargo();
      } else if (cualCelda == 2) {
         fechaIniBkp = vigenciaSeleccionada.getFechainicial();
      } else if (cualCelda == 3) {
         fechaFinBkp = vigenciaSeleccionada.getFechafinal();
      } else if (cualCelda == 4) {
         porcentajeBkp = vigenciaSeleccionada.getPorcentaje();
      }
   }

   public void modificarVC(VigenciasArps vigenciaA) {
      vigenciaSeleccionada = vigenciaA;
      if (!listaCrear.contains(vigenciaSeleccionada)) {
         if (listaModificar.isEmpty()) {
            listaModificar.add(vigenciaSeleccionada);
         } else if (!listaModificar.contains(vigenciaSeleccionada)) {
            listaModificar.add(vigenciaSeleccionada);
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:DatosVigencias");
   }

   public void autocompletarNuevoyDuplicado(String campo, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("ESTRUCTURA")) {
         if (tipoNuevo == 1) {
            nuevaVig.setNombreEstructura(estructuraBkp);
         } else if (tipoNuevo == 2) {
            duplicarVig.setNombreEstructura(estructuraBkp);
         }
         for (int i = 0; i < lovEstructuras.size(); i++) {
            if (lovEstructuras.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVig.setNombreEstructura(lovEstructuras.get(indiceUnicoElemento).getNombre());
               nuevaVig.setEstructura(lovEstructuras.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEst");
            } else if (tipoNuevo == 2) {
               duplicarVig.setNombreEstructura(lovEstructuras.get(indiceUnicoElemento).getNombre());
               duplicarVig.setEstructura(lovEstructuras.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEst");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEst");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEst");
            }
         }
      } else if (campo.equalsIgnoreCase("CARGO")) {
         if (tipoNuevo == 1) {
            nuevaVig.setNombreCargo(cargoBkp);
         } else if (tipoNuevo == 2) {
            duplicarVig.setNombreCargo(cargoBkp);
         }
         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVig.setCargo(lovCargos.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            } else if (tipoNuevo == 2) {
               duplicarVig.setCargo(lovCargos.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            }
            lovCargos.clear();
            getLovCargos();
         } else {
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            }
         }
      }
   }

   //DUPLICAR VC
   public void duplicarVigenciaC() {
      if (vigenciaSeleccionada != null) {
         duplicarVig = new VigenciasArps();
         k++;
         l = BigInteger.valueOf(k);
         duplicarVig.setSecuencia(l);
         duplicarVig.setFechainicial(vigenciaSeleccionada.getFechainicial());
         duplicarVig.setFechafinal(vigenciaSeleccionada.getFechafinal());
         duplicarVig.setEstructura(vigenciaSeleccionada.getEstructura());
         duplicarVig.setNombreEstructura(vigenciaSeleccionada.getNombreEstructura());
         duplicarVig.setCargo(vigenciaSeleccionada.getCargo());
         duplicarVig.setNombreCargo(vigenciaSeleccionada.getNombreCargo());
         duplicarVig.setPorcentaje(vigenciaSeleccionada.getPorcentaje());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarV");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroV').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cancelarModificacion() {
      restaurarTabla();
      bandera = 0;
      aceptar = true;
      k = 0;
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaVig = new VigenciasArps();
      duplicarVig = new VigenciasArps();
      altoTabla = "310";
      vigenciaSeleccionada = null;
      listaVigenciasArps = null;
      lovCargos = null;
      lovEstructuras = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
      vigenciaSeleccionada = null;
      guardado = true;
      getListaVigenciasArps();
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:DatosVigencias");
      contarRegistros();
      context.update("form:ACEPTAR");
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      if (vigenciaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASARPS");
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASARPS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void actualizarEstructura() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setEstructura(estructuraLovSeleccionada.getSecuencia());
         if (!listaCrear.contains(vigenciaSeleccionada)) {
            if (listaModificar.isEmpty()) {
               listaModificar.add(vigenciaSeleccionada);
            } else if (!listaModificar.contains(vigenciaSeleccionada)) {
               listaModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:DatosVigencias");
      } else if (tipoActualizacion == 1) {
         nuevaVig.setEstructura(estructuraLovSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaV");
      } else if (tipoActualizacion == 2) {
         duplicarVig.setEstructura(estructuraLovSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarV");
      }
      filtrarEstructuras = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      estructuraLovSeleccionada = null;
   }

   public void actualizarCargo() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setCargo(cargoLovSeleccionado.getSecuencia());
         if (!listaCrear.contains(vigenciaSeleccionada)) {
            if (listaModificar.isEmpty()) {
               listaModificar.add(vigenciaSeleccionada);
            } else if (!listaModificar.contains(vigenciaSeleccionada)) {
               listaModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:DatosVigencias");
      } else if (tipoActualizacion == 1) {
         nuevaVig.setCargo(cargoLovSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaV");
      } else if (tipoActualizacion == 2) {
         duplicarVig.setCargo(cargoLovSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarV");
      }
      filtrarCargos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         columnEstructura = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnEstructura");
         columnEstructura.setFilterStyle("width: 85% !important;");
         columnCargo = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnCargo");
         columnCargo.setFilterStyle("width: 85% !important;");
         columnInicial = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnInicial");
         columnInicial.setFilterStyle("width: 80% !important;");
         columnFinal = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnFinal");
         columnFinal.setFilterStyle("width: 80% !important;");
         columnPorcentaje = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnPorcentaje");
         columnPorcentaje.setFilterStyle("width: 75% !important;");
         altoTabla = "290";
         RequestContext.getCurrentInstance().update("form:DatosVigencias");
         bandera = 1;
      } else {
         restaurarTabla();
      }
      cualCelda = -1;
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      columnEstructura = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnEstructura");
      columnEstructura.setFilterStyle("display: none; visibility: hidden;");
      columnCargo = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnCargo");
      columnCargo.setFilterStyle("display: none; visibility: hidden;");
      columnInicial = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnInicial");
      columnInicial.setFilterStyle("display: none; visibility: hidden;");
      columnFinal = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnFinal");
      columnFinal.setFilterStyle("display: none; visibility: hidden;");
      columnPorcentaje = (Column) c.getViewRoot().findComponent("form:DatosVigencias:columnPorcentaje");
      columnPorcentaje.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "310";
      bandera = 0;
      filtrarCargos = null;
      tipoLista = 0;
      RequestContext.getCurrentInstance().update("form:DatosVigencias");
   }

   public void guardarYSalir() {
      guardarCambiosVC();
      salir();
   }

   public void salir() {
      limpiarListasValor();
      limpiarListasValor();
      restaurarTabla();
      listaBorrar.clear();
      listaCrear.clear();
      listaModificar.clear();
      vigenciaSeleccionada = null;
      k = 0;
      listaVigenciasArps = null;
      guardado = true;
      navegar("atras");
   }

   public void guardarCambiosVC() {
      if (guardado == false) {
         if (!listaBorrar.isEmpty()) {
            for (int i = 0; i < listaBorrar.size(); i++) {
               administrarVigenciasArps.borrarVArp(listaBorrar.get(i));
            }
            listaBorrar.clear();
         }
         if (!listaCrear.isEmpty()) {
            for (int i = 0; i < listaCrear.size(); i++) {
               administrarVigenciasArps.crearVArp(listaCrear.get(i));
            }
            listaCrear.clear();
         }
         if (!listaModificar.isEmpty()) {
            for (int i = 0; i < listaModificar.size(); i++) {
               administrarVigenciasArps.modificarVArp(listaModificar.get(i));
            }
            listaModificar.clear();
         }
         //System.out.println("Se guardaron los datos con exito");
         listaVigenciasArps = null;
         getListaVigenciasArps();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:DatosVigencias");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLovCargos");
   }

   public void contarRegistrosEstructuras() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLovEstructuras");
   }

   public void listaEstructuras(int tipoNuevo) {
      tipoActualizacion = tipoNuevo;
//      if (tipoActualizacion == 1) {
//         if (nuevaVig.getFechainicial() == null) {
//            RequestContext.getCurrentInstance().execute("PF('necesitaFecha').show()");
//         } else {
//            String forFecha = formatoFecha.format(nuevaVig.getFechainicial());
//            lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
//         }
//      } else if (tipoActualizacion == 2) {
//         if (duplicarVig.getFechainicial() == null) {
//            RequestContext.getCurrentInstance().execute("PF('necesitaFecha').show()");
//         } else {
//            String forFecha = formatoFecha.format(duplicarVig.getFechainicial());
//            lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
//         }
//      }
      RequestContext.getCurrentInstance().update("form:dlgEstructuras");
      contarRegistrosEstructuras();
      RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
   }

   public void listaCargos(int tipoNuevo) {
      tipoActualizacion = tipoNuevo;
      RequestContext.getCurrentInstance().update("form:dlgCargos");
      contarRegistrosEstructuras();
      RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
   }

   public void listaValoresBoton() {
      if (vigenciaSeleccionada != null) {
         if (cualCelda == 0) {
            estructuraLovSeleccionada = null;
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            contarRegistrosEstructuras();
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 1) {
            tipoActualizacion = 0;
            cargoLovSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            contarRegistrosCargos();
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO
   public void limpiarNuevaVC() {
      nuevaVig = new VigenciasArps();
   }

   public void limpiarduplicarVC() {
      duplicarVig = new VigenciasArps();
   }

   public void editarCelda() {
      if (vigenciaSeleccionada != null) {
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructura");
            RequestContext.getCurrentInstance().execute("PF('editarEstructura').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreCargo");
            RequestContext.getCurrentInstance().execute("PF('editarNombreCargo').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaI");
            RequestContext.getCurrentInstance().execute("PF('editarFechaI').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaF");
            RequestContext.getCurrentInstance().execute("PF('editarFechaF').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentaje");
            RequestContext.getCurrentInstance().execute("PF('editarPorcentaje').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //GETs y SETs
   public VigenciasArps getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasArps vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }

   public List<VigenciasArps> getListaVigenciasArps() {
      if (listaVigenciasArps == null) {
         listaVigenciasArps = administrarVigenciasArps.consultarVigenciasArps();
      }
      return listaVigenciasArps;
   }

   public void setListaVigenciasArps(List<VigenciasArps> listaVigenciasArps) {
      this.listaVigenciasArps = listaVigenciasArps;
   }

   public List<VigenciasArps> getFiltrarVigenciasArps() {
      return filtrarVigenciasArps;
   }

   public void setFiltrarVigenciasArps(List<VigenciasArps> filtrarVigenciasArps) {
      this.filtrarVigenciasArps = filtrarVigenciasArps;
   }

   public Estructuras getEstructuraLovSeleccionada() {
      return estructuraLovSeleccionada;
   }

   public void setEstructuraLovSeleccionada(Estructuras estructuraLovSeleccionada) {
      this.estructuraLovSeleccionada = estructuraLovSeleccionada;
   }

   public List<Estructuras> getLovEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarVigenciasArps.consultarTodoEstructuras();
      }
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public List<Estructuras> getFiltrarEstructuras() {
      return filtrarEstructuras;
   }

   public void setFiltrarEstructuras(List<Estructuras> filtrarEstructuras) {
      this.filtrarEstructuras = filtrarEstructuras;
   }

   public Cargos getCargoLovSeleccionado() {
      return cargoLovSeleccionado;
   }

   public void setCargoLovSeleccionado(Cargos cargoLovSeleccionado) {
      this.cargoLovSeleccionado = cargoLovSeleccionado;
   }

   public List<Cargos> getLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarVigenciasArps.consultarTodoCargos();
      }
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getFiltrarCargos() {
      return filtrarCargos;
   }

   public void setFiltrarCargos(List<Cargos> filtrarCargos) {
      this.filtrarCargos = filtrarCargos;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:DatosVigencias");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroLovCargos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCargos");
      infoRegistroLovCargos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCargos;
   }

   public void setInfoRegistroLovCargos(String infoRegistroLovCargos) {
      this.infoRegistroLovCargos = infoRegistroLovCargos;
   }

   public String getInfoRegistroLovEstructuras() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstructuras");
      infoRegistroLovEstructuras = String.valueOf(tabla.getRowCount());
      return infoRegistroLovEstructuras;
   }

   public void setInfoRegistroLovEstructuras(String infoRegistroLovEstructuras) {
      this.infoRegistroLovEstructuras = infoRegistroLovEstructuras;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public VigenciasArps getNuevaVig() {
      return nuevaVig;
   }

   public void setNuevaVig(VigenciasArps nuevaVig) {
      this.nuevaVig = nuevaVig;
   }

   public VigenciasArps getDuplicarVig() {
      return duplicarVig;
   }

   public void setDuplicarVig(VigenciasArps duplicarVig) {
      this.duplicarVig = duplicarVig;
   }

}
