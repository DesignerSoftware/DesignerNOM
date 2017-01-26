/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposInfAdicionales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposInfAdicionalesInterface;
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
public class ControlGruposInfAdicionales implements Serializable {

   @EJB
   AdministrarGruposInfAdicionalesInterface administrarGruposInfAdicionales;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<GruposInfAdicionales> listGruposInfAdicionales;
   private List<GruposInfAdicionales> filtrarGruposInfAdicionales;
   private List<GruposInfAdicionales> crearGruposInfAdicionales;
   private List<GruposInfAdicionales> modificarGruposInfAdicionales;
   private List<GruposInfAdicionales> borrarGruposInfAdicionales;
   private GruposInfAdicionales nuevoGruposInfAdicionales;
   private GruposInfAdicionales duplicarGruposInfAdicionales;
   private GruposInfAdicionales editarGruposInfAdicionales;
   private GruposInfAdicionales grupoInfAdSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, descripcion, estado;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String infoRegistro;
   private DataTable tablaC;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlGruposInfAdicionales() {
      listGruposInfAdicionales = null;
      crearGruposInfAdicionales = new ArrayList<GruposInfAdicionales>();
      modificarGruposInfAdicionales = new ArrayList<GruposInfAdicionales>();
      borrarGruposInfAdicionales = new ArrayList<GruposInfAdicionales>();
      permitirIndex = true;
      editarGruposInfAdicionales = new GruposInfAdicionales();
      nuevoGruposInfAdicionales = new GruposInfAdicionales();
      duplicarGruposInfAdicionales = new GruposInfAdicionales();
      guardado = true;
      tamano = 270;
      activarLov = true;
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
         String pagActual = "grupoinfadicional";
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
         administrarGruposInfAdicionales.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPag(String pagina) {
      paginaAnterior = pagina;
      listGruposInfAdicionales = null;
      getListGruposInfAdicionales();
      deshabilitarBotonLov();
      if (!listGruposInfAdicionales.isEmpty()) {
         grupoInfAdSeleccionado = listGruposInfAdicionales.get(0);
      }
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   private String backUpEstado;

   public void cambiarIndice(GruposInfAdicionales grupoInfAdicional, int celda) {
      if (permitirIndex == true) {
         grupoInfAdSeleccionado = grupoInfAdicional;
         cualCelda = celda;
         grupoInfAdSeleccionado.getSecuencia();
         deshabilitarBotonLov();
         if (tipoLista == 0) {
            deshabilitarBotonLov();
            backupCodigo = grupoInfAdSeleccionado.getCodigo();
            backupDescripcion = grupoInfAdSeleccionado.getDescripcion();
            backUpEstado = grupoInfAdSeleccionado.getEstado();
         } else if (tipoLista == 1) {
            deshabilitarBotonLov();
            backupCodigo = grupoInfAdSeleccionado.getCodigo();
            backupDescripcion = grupoInfAdSeleccionado.getDescripcion();
            backUpEstado = grupoInfAdSeleccionado.getEstado();
         }
      }
   }

   public void asignarIndex(GruposInfAdicionales grupoInfAdicional, int LND, int dig) {
      try {
         grupoInfAdSeleccionado = grupoInfAdicional;
         deshabilitarBotonLov();
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         System.out.println("ERROR ControlGruposInfAdicionales.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
         estado.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         bandera = 0;
         filtrarGruposInfAdicionales = null;
         tipoLista = 0;
      }

      borrarGruposInfAdicionales.clear();
      crearGruposInfAdicionales.clear();
      modificarGruposInfAdicionales.clear();
      deshabilitarBotonLov();
      grupoInfAdSeleccionado = null;
      k = 0;
      listGruposInfAdicionales = null;
      guardado = true;
      permitirIndex = true;
      getListGruposInfAdicionales();
      contarRegistros();
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:informacionRegistro");
      context.update("form:datosGruposInfAdicionales");
      context.update("form:ACEPTAR");
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
         estado.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         bandera = 0;
         filtrarGruposInfAdicionales = null;
         tipoLista = 0;
      }

      borrarGruposInfAdicionales.clear();
      crearGruposInfAdicionales.clear();
      modificarGruposInfAdicionales.clear();
      grupoInfAdSeleccionado = null;
      grupoInfAdSeleccionado = null;
      k = 0;
      listGruposInfAdicionales = null;
      guardado = true;
      permitirIndex = true;
      getListGruposInfAdicionales();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      context.update("form:informacionRegistro");
      context.update("form:datosGruposInfAdicionales");
      context.update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
         estado.setFilterStyle("width: 85% !important");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
         estado.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         bandera = 0;
         filtrarGruposInfAdicionales = null;
         tipoLista = 0;
      }
   }

   public void modificarGruposInfAdicionales(GruposInfAdicionales grupoInfAdicional, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      grupoInfAdSeleccionado = grupoInfAdicional;

      int contador = 0, pass = 0;

      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (grupoInfAdSeleccionado.getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listGruposInfAdicionales.size(); j++) {
                     if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                        contador++;
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     grupoInfAdSeleccionado.setCodigo(backupCodigo);
                  } else {
                     pass++;
                  }

               }
               if (grupoInfAdSeleccionado.getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";

                  grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
               } else if (grupoInfAdSeleccionado.getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";

                  grupoInfAdSeleccionado.setDescripcion(backupDescripcion);

               } else {
                  pass++;
               }
               if (grupoInfAdSeleccionado.getEstado() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setEstado(backUpEstado);
               } else if (grupoInfAdSeleccionado.getEstado().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setEstado(backUpEstado);

               } else {
                  pass++;
               }

               if (pass == 3) {
                  if (modificarGruposInfAdicionales.isEmpty()) {
                     modificarGruposInfAdicionales.add(grupoInfAdSeleccionado);
                  } else if (!modificarGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {
                     modificarGruposInfAdicionales.add(grupoInfAdSeleccionado);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               grupoInfAdSeleccionado = null;
               grupoInfAdSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (grupoInfAdSeleccionado.getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listGruposInfAdicionales.size(); j++) {
                     if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                        contador++;
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     grupoInfAdSeleccionado.setCodigo(backupCodigo);
                  } else {
                     pass++;
                  }

               }
               if (grupoInfAdSeleccionado.getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
               } else if (grupoInfAdSeleccionado.getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setDescripcion(backupDescripcion);

               } else {
                  pass++;
               }
               if (grupoInfAdSeleccionado.getEstado().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setEstado(backUpEstado);
               } else if (grupoInfAdSeleccionado.getEstado().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  grupoInfAdSeleccionado.setEstado(backUpEstado);

               } else {
                  pass++;
               }
               if (pass == 3) {
                  if (guardado == true) {
                     guardado = false;
                  }
               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               grupoInfAdSeleccionado = null;
               grupoInfAdSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {
            if (grupoInfAdSeleccionado.getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarGruposInfAdicionales.size(); j++) {
                  if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                     contador++;
                  }
               }
               for (int j = 0; j < listGruposInfAdicionales.size(); j++) {
                  if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                     contador++;
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  grupoInfAdSeleccionado.setCodigo(backupCodigo);

               } else {
                  pass++;
               }

            }

            if (grupoInfAdSeleccionado.getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
            } else if (grupoInfAdSeleccionado.getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
            } else {
               pass++;
            }
            if (grupoInfAdSeleccionado.getEstado().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setEstado(backUpEstado);
            } else if (grupoInfAdSeleccionado.getEstado().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setEstado(backUpEstado);

            } else {
               pass++;
            }
            if (pass == 3) {
               if (modificarGruposInfAdicionales.isEmpty()) {
                  modificarGruposInfAdicionales.add(grupoInfAdSeleccionado);
               } else if (!modificarGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {
                  modificarGruposInfAdicionales.add(grupoInfAdSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            grupoInfAdSeleccionado = null;
            grupoInfAdSeleccionado = null;
         } else {
            if (grupoInfAdSeleccionado.getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarGruposInfAdicionales.size(); j++) {
                  if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                     contador++;
                  }
               }
               for (int j = 0; j < listGruposInfAdicionales.size(); j++) {
                  if (grupoInfAdSeleccionado.getCodigo() == listGruposInfAdicionales.get(j).getCodigo()) {
                     contador++;
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  grupoInfAdSeleccionado.setCodigo(backupCodigo);

               } else {
                  pass++;
               }

            }

            if (grupoInfAdSeleccionado.getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
            } else if (grupoInfAdSeleccionado.getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setDescripcion(backupDescripcion);
            } else {
               pass++;
            }
            if (grupoInfAdSeleccionado.getEstado().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setEstado(backUpEstado);
            } else if (grupoInfAdSeleccionado.getEstado().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               grupoInfAdSeleccionado.setEstado(backUpEstado);

            } else {
               pass++;
            }
            if (pass == 3) {
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            grupoInfAdSeleccionado = null;
            grupoInfAdSeleccionado = null;
         }
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoGruposInfAdicionales() {

      if (grupoInfAdSeleccionado != null) {
         System.out.println("Entro a borrandoGruposInfAdicionales");
         if (!modificarGruposInfAdicionales.isEmpty() && modificarGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {
            int modIndex = modificarGruposInfAdicionales.indexOf(grupoInfAdSeleccionado);
            modificarGruposInfAdicionales.remove(modIndex);
            borrarGruposInfAdicionales.add(grupoInfAdSeleccionado);
         } else if (!crearGruposInfAdicionales.isEmpty() && crearGruposInfAdicionales.contains(grupoInfAdSeleccionado)) {
            int crearIndex = crearGruposInfAdicionales.indexOf(grupoInfAdSeleccionado);
            crearGruposInfAdicionales.remove(crearIndex);
         } else {
            borrarGruposInfAdicionales.add(grupoInfAdSeleccionado);
         }
         listGruposInfAdicionales.remove(grupoInfAdSeleccionado);
         if (tipoLista == 1) {
            filtrarGruposInfAdicionales.remove(grupoInfAdSeleccionado);

         }
         contarRegistros();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         grupoInfAdSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void verificarBorrado() {
      System.out.println("Estoy en verificarBorrado");
      BigInteger verificarInformacionesAdicionales;

      try {
         System.err.println("Control Secuencia de ControlGruposInfAdicionales ");
         if (tipoLista == 0) {
            verificarInformacionesAdicionales = administrarGruposInfAdicionales.verificarInformacionesAdicionales(grupoInfAdSeleccionado.getSecuencia());
         } else {
            verificarInformacionesAdicionales = administrarGruposInfAdicionales.verificarInformacionesAdicionales(grupoInfAdSeleccionado.getSecuencia());
         }
         if (verificarInformacionesAdicionales.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoGruposInfAdicionales();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            grupoInfAdSeleccionado = null;
            verificarInformacionesAdicionales = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlGruposInfAdicionales verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarGruposInfAdicionales.isEmpty() || !crearGruposInfAdicionales.isEmpty() || !modificarGruposInfAdicionales.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarGruposInfAdicionales() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarGruposInfAdicionales");
         if (!borrarGruposInfAdicionales.isEmpty()) {
            administrarGruposInfAdicionales.borrarGruposInfAdicionales(borrarGruposInfAdicionales);
            //mostrarBorrados
            registrosBorrados = borrarGruposInfAdicionales.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarGruposInfAdicionales.clear();
         }
         if (!modificarGruposInfAdicionales.isEmpty()) {
            administrarGruposInfAdicionales.modificarGruposInfAdicionales(modificarGruposInfAdicionales);
            modificarGruposInfAdicionales.clear();
         }
         if (!crearGruposInfAdicionales.isEmpty()) {
            administrarGruposInfAdicionales.crearGruposInfAdicionales(crearGruposInfAdicionales);
            crearGruposInfAdicionales.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listGruposInfAdicionales = null;
         getListGruposInfAdicionales();
         contarRegistros();
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         k = 0;
         guardado = true;
      }
      grupoInfAdSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (grupoInfAdSeleccionado != null) {
         editarGruposInfAdicionales = grupoInfAdSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editEstado");
            RequestContext.getCurrentInstance().execute("PF('editEstado').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void agregarNuevoGruposInfAdicionales() {
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoGruposInfAdicionales.getCodigo() == a) {
         mensajeValidacion = "Campo Código vacío \n";
      } else {

         for (int x = 0; x < listGruposInfAdicionales.size(); x++) {
            if (listGruposInfAdicionales.get(x).getCodigo() == nuevoGruposInfAdicionales.getCodigo()) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = "No puede haber códigos repetidos \n";
         } else {
            contador++;
         }
      }
      if (nuevoGruposInfAdicionales.getDescripcion() == null || nuevoGruposInfAdicionales.getDescripcion().isEmpty()) {
         mensajeValidacion = "Campo Descripción vacío \n";

      } else {
         contador++;
      }
      if (nuevoGruposInfAdicionales.getEstado() == null || nuevoGruposInfAdicionales.getEstado().isEmpty()) {
         mensajeValidacion = "Campo Estado vacío \n";
      } else {
         contador++;
      }

      System.out.println("contador " + contador);

      if (contador == 3) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
            bandera = 0;
            filtrarGruposInfAdicionales = null;
            tamano = 270;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoGruposInfAdicionales.setSecuencia(l);
         crearGruposInfAdicionales.add(nuevoGruposInfAdicionales);
         listGruposInfAdicionales.add(nuevoGruposInfAdicionales);
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         grupoInfAdSeleccionado = nuevoGruposInfAdicionales;
         nuevoGruposInfAdicionales = new GruposInfAdicionales();
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroGruposInfAdicionales').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoGrupoInfAd");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoGrupoInfAd').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoGruposInfAdicionales() {
      nuevoGruposInfAdicionales = new GruposInfAdicionales();
   }

   //------------------------------------------------------------------------------
   public void duplicandoGruposInfAdicionales() {
      System.out.println("duplicandoGruposInfAdicionales");
      if (grupoInfAdSeleccionado != null) {
         duplicarGruposInfAdicionales = new GruposInfAdicionales();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarGruposInfAdicionales.setSecuencia(l);
            duplicarGruposInfAdicionales.setCodigo(grupoInfAdSeleccionado.getCodigo());
            duplicarGruposInfAdicionales.setDescripcion(grupoInfAdSeleccionado.getDescripcion());
            duplicarGruposInfAdicionales.setEstado(grupoInfAdSeleccionado.getEstado());
         }
         if (tipoLista == 1) {
            duplicarGruposInfAdicionales.setSecuencia(l);
            duplicarGruposInfAdicionales.setCodigo(grupoInfAdSeleccionado.getCodigo());
            duplicarGruposInfAdicionales.setDescripcion(grupoInfAdSeleccionado.getDescripcion());
            duplicarGruposInfAdicionales.setEstado(grupoInfAdSeleccionado.getEstado());
            tamano = 270;

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposInfAdicionales').show()");

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarGruposInfAdicionales.getCodigo() == a) {
         mensajeValidacion = "Campo Código vacío \n";
      } else {
         for (int x = 0; x < listGruposInfAdicionales.size(); x++) {
            if (listGruposInfAdicionales.get(x).getCodigo() == duplicarGruposInfAdicionales.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No puede haber códigos repetidos \n";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarGruposInfAdicionales.getDescripcion() == null || duplicarGruposInfAdicionales.getDescripcion().isEmpty()) {
         mensajeValidacion = "Campo Descripción vacío \n";

      } else {
         System.out.println("Bandera : ");
         contador++;
      }
      if (duplicarGruposInfAdicionales.getEstado() == null || duplicarGruposInfAdicionales.getEstado().isEmpty()) {
         mensajeValidacion = "Campo Estado vacío \n";

      } else {
         contador++;
      }

      if (contador == 3) {

         System.out.println("Datos Duplicando: " + duplicarGruposInfAdicionales.getSecuencia() + "  " + duplicarGruposInfAdicionales.getCodigo());
         if (crearGruposInfAdicionales.contains(duplicarGruposInfAdicionales)) {
            System.out.println("Ya lo contengo.");
         }
         listGruposInfAdicionales.add(duplicarGruposInfAdicionales);
         crearGruposInfAdicionales.add(duplicarGruposInfAdicionales);
         RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
         grupoInfAdSeleccionado = duplicarGruposInfAdicionales;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposInfAdicionales:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposInfAdicionales");
            bandera = 0;
            filtrarGruposInfAdicionales = null;
            tipoLista = 0;
         }
         duplicarGruposInfAdicionales = new GruposInfAdicionales();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposInfAdicionales').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarGruposInfAdicionales() {
      duplicarGruposInfAdicionales = new GruposInfAdicionales();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposInfAdicionalesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
      context.responseComplete();
      grupoInfAdSeleccionado = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposInfAdicionalesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
      context.responseComplete();
      grupoInfAdSeleccionado = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (grupoInfAdSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(grupoInfAdSeleccionado.getSecuencia(), "GRUPOSINFADICIONALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("GRUPOSINFADICIONALES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
         contarRegistros();
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void recordarSeleccion() {
      if (grupoInfAdSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosGruposInfAdicionales");
         tablaC.setSelection(grupoInfAdSeleccionado);
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<GruposInfAdicionales> getListGruposInfAdicionales() {
      if (listGruposInfAdicionales == null) {
         listGruposInfAdicionales = administrarGruposInfAdicionales.consultarGruposInfAdicionales();
      }
      return listGruposInfAdicionales;
   }

   public void setListGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
      this.listGruposInfAdicionales = listGruposInfAdicionales;
   }

   public List<GruposInfAdicionales> getFiltrarGruposInfAdicionales() {
      return filtrarGruposInfAdicionales;
   }

   public void setFiltrarGruposInfAdicionales(List<GruposInfAdicionales> filtrarGruposInfAdicionales) {
      this.filtrarGruposInfAdicionales = filtrarGruposInfAdicionales;
   }

   public GruposInfAdicionales getNuevoGruposInfAdicionales() {
      return nuevoGruposInfAdicionales;
   }

   public void setNuevoGruposInfAdicionales(GruposInfAdicionales nuevoGruposInfAdicionales) {
      this.nuevoGruposInfAdicionales = nuevoGruposInfAdicionales;
   }

   public GruposInfAdicionales getDuplicarGruposInfAdicionales() {
      return duplicarGruposInfAdicionales;
   }

   public void setDuplicarGruposInfAdicionales(GruposInfAdicionales duplicarGruposInfAdicionales) {
      this.duplicarGruposInfAdicionales = duplicarGruposInfAdicionales;
   }

   public GruposInfAdicionales getEditarGruposInfAdicionales() {
      return editarGruposInfAdicionales;
   }

   public void setEditarGruposInfAdicionales(GruposInfAdicionales editarGruposInfAdicionales) {
      this.editarGruposInfAdicionales = editarGruposInfAdicionales;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public GruposInfAdicionales getGrupoInfAdSeleccionado() {
      return grupoInfAdSeleccionado;
   }

   public void setGrupoInfAdSeleccionado(GruposInfAdicionales grupoInfAdSeleccionado) {
      this.grupoInfAdSeleccionado = grupoInfAdSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGruposInfAdicionales");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
