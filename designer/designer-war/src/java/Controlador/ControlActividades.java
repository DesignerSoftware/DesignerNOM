/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Actividades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarActividadesInterface;
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
public class ControlActividades implements Serializable {

   @EJB
   AdministrarActividadesInterface administrarActividades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Actividades> listActividades;
   private List<Actividades> filtrarActividades;
   private List<Actividades> crearActividades;
   private List<Actividades> modificarActividades;
   private List<Actividades> borrarActividades;
   private Actividades nuevoActividades;
   private Actividades duplicarActividades;
   private Actividades editarActividades;
   private Actividades actividadSeleccionada;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion, dimensiones;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlActividades() {
      listActividades = null;
      crearActividades = new ArrayList<Actividades>();
      modificarActividades = new ArrayList<Actividades>();
      borrarActividades = new ArrayList<Actividades>();
      permitirIndex = true;
      editarActividades = new Actividades();
      nuevoActividades = new Actividades();
      duplicarActividades = new Actividades();
      guardado = true;
      tamano = 270;
      guardado = true;
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
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "actividadbienestar";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
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
         administrarActividades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());

      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlActividades.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         System.out.println("ERROR ControlActividades eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);
      System.err.println("permitirIndex  " + permitirIndex);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listActividades.get(index).getSecuencia();
         if (tipoLista == 0) {
            backupCodigo = listActividades.get(index).getCodigo();
            backupDescripcion = listActividades.get(index).getDescripcion();
         } else if (tipoLista == 1) {
            backupCodigo = filtrarActividades.get(index).getCodigo();
            backupDescripcion = filtrarActividades.get(index).getDescripcion();
         }
      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A ControlActividades.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         System.out.println("ERROR ControlActividades.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();

         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
         dimensiones.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         bandera = 0;
         filtrarActividades = null;
         tipoLista = 0;
      }

      borrarActividades.clear();
      crearActividades.clear();
      modificarActividades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listActividades = null;
      guardado = true;
      permitirIndex = true;
      getListActividades();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listActividades == null || listActividades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listActividades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosActividades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();

         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
         dimensiones.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         bandera = 0;
         filtrarActividades = null;
         tipoLista = 0;
      }

      borrarActividades.clear();
      crearActividades.clear();
      modificarActividades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listActividades = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosActividades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
         dimensiones.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
         dimensiones.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         bandera = 0;
         filtrarActividades = null;
         tipoLista = 0;
      }
   }

   public void mostrarInfo(int indice, int celda) {
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         cualCelda = celda;
         secRegistro = listActividades.get(index).getSecuencia();
         if (cualCelda == 2) {
            System.out.println("CLASE ACTIVIDAD : " + listActividades.get(indice).getClaseactividad());
         }
         if (tipoLista == 0) {
            if (modificarActividades.isEmpty()) {
               modificarActividades.add(listActividades.get(indice));
            } else if (!modificarActividades.contains(listActividades.get(indice))) {
               modificarActividades.add(listActividades.get(indice));
            }
         } else if (modificarActividades.isEmpty()) {
            modificarActividades.add(filtrarActividades.get(indice));
         } else if (!modificarActividades.contains(filtrarActividades.get(indice))) {
            modificarActividades.add(filtrarActividades.get(indice));
         }
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosActividades");

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);

   }

   public void modificarActividades(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;

      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;

      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearActividades.contains(listActividades.get(indice))) {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (listActividades.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listActividades.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listActividades.size(); j++) {
                     if (j != indice) {
                        if (listActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listActividades.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listActividades.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listActividades.get(indice).setDescripcion(backupDescripcion);
               } else if (listActividades.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listActividades.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarActividades.isEmpty()) {
                     modificarActividades.add(listActividades.get(indice));
                  } else if (!modificarActividades.contains(listActividades.get(indice))) {
                     modificarActividades.add(listActividades.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosActividades");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (listActividades.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listActividades.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listActividades.size(); j++) {
                     if (j != indice) {
                        if (listActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listActividades.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listActividades.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listActividades.get(indice).setDescripcion(backupDescripcion);
               } else if (listActividades.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listActividades.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (guardado == true) {
                     guardado = false;
                  }
               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosActividades");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearActividades.contains(filtrarActividades.get(indice))) {
            if (filtrarActividades.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarActividades.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarActividades.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarActividades.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarActividades.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarActividades.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarActividades.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarActividades.isEmpty()) {
                  modificarActividades.add(filtrarActividades.get(indice));
               } else if (!modificarActividades.contains(filtrarActividades.get(indice))) {
                  modificarActividades.add(filtrarActividades.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            index = -1;
            secRegistro = null;
         } else {
            if (filtrarActividades.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarActividades.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarActividades.get(indice).getCodigo() == listActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarActividades.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarActividades.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarActividades.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarActividades.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarActividades.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            index = -1;
            secRegistro = null;
         }
         RequestContext.getCurrentInstance().update("form:datosActividades");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoActividades() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoActividades");
            if (!modificarActividades.isEmpty() && modificarActividades.contains(listActividades.get(index))) {
               int modIndex = modificarActividades.indexOf(listActividades.get(index));
               modificarActividades.remove(modIndex);
               borrarActividades.add(listActividades.get(index));
            } else if (!crearActividades.isEmpty() && crearActividades.contains(listActividades.get(index))) {
               int crearIndex = crearActividades.indexOf(listActividades.get(index));
               crearActividades.remove(crearIndex);
            } else {
               borrarActividades.add(listActividades.get(index));
            }
            listActividades.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoActividades ");
            if (!modificarActividades.isEmpty() && modificarActividades.contains(filtrarActividades.get(index))) {
               int modIndex = modificarActividades.indexOf(filtrarActividades.get(index));
               modificarActividades.remove(modIndex);
               borrarActividades.add(filtrarActividades.get(index));
            } else if (!crearActividades.isEmpty() && crearActividades.contains(filtrarActividades.get(index))) {
               int crearIndex = crearActividades.indexOf(filtrarActividades.get(index));
               crearActividades.remove(crearIndex);
            } else {
               borrarActividades.add(filtrarActividades.get(index));
            }
            int VCIndex = listActividades.indexOf(filtrarActividades.get(index));
            listActividades.remove(VCIndex);
            filtrarActividades.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosActividades");
         infoRegistro = "Cantidad de registros: " + listActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void verificarBorrado() {
      System.out.println("Estoy en verificarBorrado");
      BigInteger contarBienNecesidadesActividad;
      BigInteger contarParametrosInformesActividad;

      try {
         System.err.println("Control Secuencia de ControlActividades ");
         if (tipoLista == 0) {
            contarBienNecesidadesActividad = administrarActividades.contarBienNecesidadesActividad(listActividades.get(index).getSecuencia());
            contarParametrosInformesActividad = administrarActividades.contarParametrosInformesActividad(listActividades.get(index).getSecuencia());
         } else {
            contarBienNecesidadesActividad = administrarActividades.contarBienNecesidadesActividad(filtrarActividades.get(index).getSecuencia());
            contarParametrosInformesActividad = administrarActividades.contarParametrosInformesActividad(filtrarActividades.get(index).getSecuencia());
         }
         if (contarBienNecesidadesActividad.equals(new BigInteger("0")) && contarParametrosInformesActividad.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoActividades();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarBienNecesidadesActividad = new BigInteger("-1");
            contarParametrosInformesActividad = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlActividades verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarActividades.isEmpty() || !crearActividades.isEmpty() || !modificarActividades.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarActividades() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarActividades");
         if (!borrarActividades.isEmpty()) {
            administrarActividades.borrarActividades(borrarActividades);
            //mostrarBorrados
            registrosBorrados = borrarActividades.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarActividades.clear();
         }
         if (!modificarActividades.isEmpty()) {
            administrarActividades.modificarActividades(modificarActividades);
            modificarActividades.clear();
         }
         if (!crearActividades.isEmpty()) {
            administrarActividades.crearActividades(crearActividades);
            crearActividades.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listActividades = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarActividades = listActividades.get(index);
         }
         if (tipoLista == 1) {
            editarActividades = filtrarActividades.get(index);
         }

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
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoActividades() {
      System.out.println("agregarNuevoActividades");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoActividades.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoActividades.getCodigo());

         for (int x = 0; x < listActividades.size(); x++) {
            if (listActividades.get(x).getCodigo() == nuevoActividades.getCodigo()) {
               duplicados++;
            }
         }
         System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;
         }
      }
      if (nuevoActividades.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoActividades.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

      }
      if (nuevoActividades.getClaseactividad() == null) {
         nuevoActividades.setClaseactividad(null);
      } else if (nuevoActividades.getClaseactividad().isEmpty()) {
         nuevoActividades.setClaseactividad(null);
      }

      System.out.println("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
            dimensiones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosActividades");
            bandera = 0;
            filtrarActividades = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoActividades.setSecuencia(l);

         crearActividades.add(nuevoActividades);

         listActividades.add(nuevoActividades);
         nuevoActividades = new Actividades();
         RequestContext.getCurrentInstance().update("form:datosActividades");
         infoRegistro = "Cantidad de registros: " + listActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroActividades').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoActividades() {
      System.out.println("limpiarNuevoActividades");
      nuevoActividades = new Actividades();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoActividades() {
      System.out.println("duplicandoActividades");
      if (index >= 0) {
         duplicarActividades = new Actividades();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarActividades.setSecuencia(l);
            duplicarActividades.setCodigo(listActividades.get(index).getCodigo());
            duplicarActividades.setDescripcion(listActividades.get(index).getDescripcion());
            duplicarActividades.setClaseactividad(listActividades.get(index).getClaseactividad());
         }
         if (tipoLista == 1) {
            duplicarActividades.setSecuencia(l);
            duplicarActividades.setCodigo(filtrarActividades.get(index).getCodigo());
            duplicarActividades.setDescripcion(filtrarActividades.get(index).getDescripcion());
            duplicarActividades.setClaseactividad(filtrarActividades.get(index).getClaseactividad());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroActividades').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      System.err.println("ConfirmarDuplicar codigo " + duplicarActividades.getCodigo());
      System.err.println("ConfirmarDuplicar Descripcion " + duplicarActividades.getDescripcion());

      if (duplicarActividades.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listActividades.size(); x++) {
            if (listActividades.get(x).getCodigo() == duplicarActividades.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarActividades.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarActividades.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

      }
      if (duplicarActividades.getClaseactividad() == null) {
         duplicarActividades.setClaseactividad(null);
      } else if (duplicarActividades.getClaseactividad().isEmpty()) {
         duplicarActividades.setClaseactividad(null);
      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarActividades.getSecuencia() + "  " + duplicarActividades.getCodigo());
         if (crearActividades.contains(duplicarActividades)) {
            System.out.println("Ya lo contengo.");
         }
         listActividades.add(duplicarActividades);
         crearActividades.add(duplicarActividades);
         RequestContext.getCurrentInstance().update("form:datosActividades");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosActividades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosActividades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            dimensiones = (Column) c.getViewRoot().findComponent("form:datosActividades:dimensiones");
            dimensiones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosActividades");
            bandera = 0;
            filtrarActividades = null;
            tipoLista = 0;
         }
         duplicarActividades = new Actividades();
         RequestContext.getCurrentInstance().execute("duplicarRegistroActividades').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarActividades() {
      duplicarActividades = new Actividades();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosActividadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ACTIVIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosActividadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ACTIVIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listActividades.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "ACTIVIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("ACTIVIDADES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Actividades> getListActividades() {
      if (listActividades == null) {
         listActividades = administrarActividades.consultarActividades();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listActividades == null || listActividades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listActividades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listActividades;
   }

   public void setListActividades(List<Actividades> listActividades) {
      this.listActividades = listActividades;
   }

   public List<Actividades> getFiltrarActividades() {
      return filtrarActividades;
   }

   public void setFiltrarActividades(List<Actividades> filtrarActividades) {
      this.filtrarActividades = filtrarActividades;
   }

   public Actividades getNuevoActividades() {
      return nuevoActividades;
   }

   public void setNuevoActividades(Actividades nuevoActividades) {
      this.nuevoActividades = nuevoActividades;
   }

   public Actividades getDuplicarActividades() {
      return duplicarActividades;
   }

   public void setDuplicarActividades(Actividades duplicarActividades) {
      this.duplicarActividades = duplicarActividades;
   }

   public Actividades getEditarActividades() {
      return editarActividades;
   }

   public void setEditarActividades(Actividades editarActividades) {
      this.editarActividades = editarActividades;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
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

   public Actividades getActividadSeleccionada() {
      return actividadSeleccionada;
   }

   public void setActividadSeleccionada(Actividades actividadSeleccionada) {
      this.actividadSeleccionada = actividadSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
