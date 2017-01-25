/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.ClasesPensiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarClasesPensionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.Map;
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
public class ControlClasesPensiones implements Serializable {

   @EJB
   AdministrarClasesPensionesInterface administrarClasesPensiones;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<ClasesPensiones> listClasesPensiones;
   private List<ClasesPensiones> filtrarClasesPensiones;
   private List<ClasesPensiones> crearClasesPensiones;
   private List<ClasesPensiones> modificarClasesPensiones;
   private List<ClasesPensiones> borrarClasesPensiones;
   private ClasesPensiones nuevoClasesPensiones;
   private ClasesPensiones duplicarClasesPensiones;
   private ClasesPensiones editarClasesPensiones;
   private ClasesPensiones clasesPensionesSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backUpCodigo;
   private String backUpDescripcion;

   public ControlClasesPensiones() {
      listClasesPensiones = null;
      crearClasesPensiones = new ArrayList<ClasesPensiones>();
      modificarClasesPensiones = new ArrayList<ClasesPensiones>();
      borrarClasesPensiones = new ArrayList<ClasesPensiones>();
      permitirIndex = true;
      editarClasesPensiones = new ClasesPensiones();
      nuevoClasesPensiones = new ClasesPensiones();
      duplicarClasesPensiones = new ClasesPensiones();
      guardado = true;
      tamano = 270;
      System.out.println("controlClasesPensiones Constructor");
   }

   private String paginaAnterior;
   private Map<String, Object> mapParametros;

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         System.out.println("ControlClasesPensiones PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarClasesPensiones.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "clasepension";
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

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlClasesPensiones.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarClasesPensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         System.out.println("ERROR ControlClasesPensiones eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backUpCodigo = listClasesPensiones.get(index).getCodigo();
               System.out.println(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
               backUpDescripcion = listClasesPensiones.get(index).getDescripcion();
               System.out.println(" backUpDescripcion : " + backUpDescripcion);
            }
            secRegistro = listClasesPensiones.get(index).getSecuencia();
         } else {
            if (cualCelda == 0) {
               backUpCodigo = filtrarClasesPensiones.get(index).getCodigo();
               System.out.println(" backUpCodigo : " + backUpCodigo);

            } else if (cualCelda == 1) {
               backUpDescripcion = filtrarClasesPensiones.get(index).getDescripcion();
               System.out.println(" backUpDescripcion : " + backUpDescripcion);

            }
            secRegistro = filtrarClasesPensiones.get(index).getSecuencia();
         }

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A ControlClasesPensiones.asignarIndex \n");
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
         System.out.println("ERROR ControlClasesPensiones.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         bandera = 0;
         filtrarClasesPensiones = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarClasesPensiones.clear();
      crearClasesPensiones.clear();
      modificarClasesPensiones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listClasesPensiones = null;
      guardado = true;
      permitirIndex = true;
      getListClasesPensiones();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listClasesPensiones == null || listClasesPensiones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         bandera = 0;
         filtrarClasesPensiones = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarClasesPensiones.clear();
      crearClasesPensiones.clear();
      modificarClasesPensiones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listClasesPensiones = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         bandera = 0;
         filtrarClasesPensiones = null;
         tipoLista = 0;
      }
   }

   public void modificarClasesPensiones(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;

      int contador = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearClasesPensiones.contains(listClasesPensiones.get(indice))) {
               if (listClasesPensiones.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listClasesPensiones.size(); j++) {
                     if (j != indice) {
                        if (listClasesPensiones.get(indice).getCodigo().equals(listClasesPensiones.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listClasesPensiones.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listClasesPensiones.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listClasesPensiones.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
               }

               if (banderita == true) {
                  if (modificarClasesPensiones.isEmpty()) {
                     modificarClasesPensiones.add(listClasesPensiones.get(indice));
                  } else if (!modificarClasesPensiones.contains(listClasesPensiones.get(indice))) {
                     modificarClasesPensiones.add(listClasesPensiones.get(indice));
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
               if (listClasesPensiones.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listClasesPensiones.size(); j++) {
                     if (j != indice) {
                        if (listClasesPensiones.get(indice).getCodigo().equals(listClasesPensiones.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listClasesPensiones.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listClasesPensiones.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listClasesPensiones.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
               }

               if (banderita == true) {

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
         } else if (!crearClasesPensiones.contains(filtrarClasesPensiones.get(indice))) {
            if (filtrarClasesPensiones.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarClasesPensiones.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarClasesPensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarClasesPensiones.get(indice).getCodigo().equals(filtrarClasesPensiones.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarClasesPensiones.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarClasesPensiones.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarClasesPensiones.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
            }

            if (banderita == true) {
               if (modificarClasesPensiones.isEmpty()) {
                  modificarClasesPensiones.add(filtrarClasesPensiones.get(indice));
               } else if (!modificarClasesPensiones.contains(filtrarClasesPensiones.get(indice))) {
                  modificarClasesPensiones.add(filtrarClasesPensiones.get(indice));
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
            if (filtrarClasesPensiones.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarClasesPensiones.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarClasesPensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarClasesPensiones.get(indice).getCodigo().equals(filtrarClasesPensiones.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarClasesPensiones.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarClasesPensiones.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarClasesPensiones.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarClasesPensiones.get(indice).setDescripcion(backUpDescripcion);
            }

            if (banderita == true) {

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
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoClasesPensiones() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoClasesPensiones");
            if (!modificarClasesPensiones.isEmpty() && modificarClasesPensiones.contains(listClasesPensiones.get(index))) {
               int modIndex = modificarClasesPensiones.indexOf(listClasesPensiones.get(index));
               modificarClasesPensiones.remove(modIndex);
               borrarClasesPensiones.add(listClasesPensiones.get(index));
            } else if (!crearClasesPensiones.isEmpty() && crearClasesPensiones.contains(listClasesPensiones.get(index))) {
               int crearIndex = crearClasesPensiones.indexOf(listClasesPensiones.get(index));
               crearClasesPensiones.remove(crearIndex);
            } else {
               borrarClasesPensiones.add(listClasesPensiones.get(index));
            }
            listClasesPensiones.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoClasesPensiones ");
            if (!modificarClasesPensiones.isEmpty() && modificarClasesPensiones.contains(filtrarClasesPensiones.get(index))) {
               int modIndex = modificarClasesPensiones.indexOf(filtrarClasesPensiones.get(index));
               modificarClasesPensiones.remove(modIndex);
               borrarClasesPensiones.add(filtrarClasesPensiones.get(index));
            } else if (!crearClasesPensiones.isEmpty() && crearClasesPensiones.contains(filtrarClasesPensiones.get(index))) {
               int crearIndex = crearClasesPensiones.indexOf(filtrarClasesPensiones.get(index));
               crearClasesPensiones.remove(crearIndex);
            } else {
               borrarClasesPensiones.add(filtrarClasesPensiones.get(index));
            }
            int VCIndex = listClasesPensiones.indexOf(filtrarClasesPensiones.get(index));
            listClasesPensiones.remove(VCIndex);
            filtrarClasesPensiones.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
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
      BigInteger contarRetiradosClasePension;

      try {
         System.err.println("Control Secuencia de ControlClasesPensiones ");
         if (tipoLista == 0) {
            contarRetiradosClasePension = administrarClasesPensiones.contarRetiradosClasePension(listClasesPensiones.get(index).getSecuencia());
         } else {
            contarRetiradosClasePension = administrarClasesPensiones.contarRetiradosClasePension(filtrarClasesPensiones.get(index).getSecuencia());
         }
         if (contarRetiradosClasePension.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoClasesPensiones();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarRetiradosClasePension = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlClasesPensiones verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarClasesPensiones.isEmpty() || !crearClasesPensiones.isEmpty() || !modificarClasesPensiones.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarClasesPensiones() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarClasesPensiones");
         if (!borrarClasesPensiones.isEmpty()) {
            administrarClasesPensiones.borrarClasesPensiones(borrarClasesPensiones);
            //mostrarBorrados
            registrosBorrados = borrarClasesPensiones.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarClasesPensiones.clear();
         }
         if (!modificarClasesPensiones.isEmpty()) {
            administrarClasesPensiones.modificarClasesPensiones(modificarClasesPensiones);
            modificarClasesPensiones.clear();
         }
         if (!crearClasesPensiones.isEmpty()) {
            administrarClasesPensiones.crearClasesPensiones(crearClasesPensiones);
            crearClasesPensiones.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listClasesPensiones = null;
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarClasesPensiones = listClasesPensiones.get(index);
         }
         if (tipoLista == 1) {
            editarClasesPensiones = filtrarClasesPensiones.get(index);
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

   public void agregarNuevoClasesPensiones() {
      System.out.println("agregarNuevoClasesPensiones");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoClasesPensiones.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoClasesPensiones.getCodigo());

         for (int x = 0; x < listClasesPensiones.size(); x++) {
            if (listClasesPensiones.get(x).getCodigo().equals(nuevoClasesPensiones.getCodigo())) {
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
      if (nuevoClasesPensiones.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoClasesPensiones.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

      }

      System.out.println("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 0;
            filtrarClasesPensiones = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoClasesPensiones.setSecuencia(l);

         crearClasesPensiones.add(nuevoClasesPensiones);

         listClasesPensiones.add(nuevoClasesPensiones);
         nuevoClasesPensiones = new ClasesPensiones();
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClasesPensiones').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoClasesPensiones() {
      System.out.println("limpiarNuevoClasesPensiones");
      nuevoClasesPensiones = new ClasesPensiones();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoClasesPensiones() {
      System.out.println("duplicandoClasesPensiones");
      if (index >= 0) {
         duplicarClasesPensiones = new ClasesPensiones();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarClasesPensiones.setSecuencia(l);
            duplicarClasesPensiones.setCodigo(listClasesPensiones.get(index).getCodigo());
            duplicarClasesPensiones.setDescripcion(listClasesPensiones.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarClasesPensiones.setSecuencia(l);
            duplicarClasesPensiones.setCodigo(filtrarClasesPensiones.get(index).getCodigo());
            duplicarClasesPensiones.setDescripcion(filtrarClasesPensiones.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClasesPensiones').show()");
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
      System.err.println("ConfirmarDuplicar codigo " + duplicarClasesPensiones.getCodigo());
      System.err.println("ConfirmarDuplicar Descripcion " + duplicarClasesPensiones.getDescripcion());

      if (duplicarClasesPensiones.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listClasesPensiones.size(); x++) {
            if (listClasesPensiones.get(x).getCodigo().equals(duplicarClasesPensiones.getCodigo())) {
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
      if (duplicarClasesPensiones.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarClasesPensiones.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarClasesPensiones.getSecuencia() + "  " + duplicarClasesPensiones.getCodigo());
         if (crearClasesPensiones.contains(duplicarClasesPensiones)) {
            System.out.println("Ya lo contengo.");
         }
         listClasesPensiones.add(duplicarClasesPensiones);
         crearClasesPensiones.add(duplicarClasesPensiones);
         RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 0;
            filtrarClasesPensiones = null;
            tipoLista = 0;
         }
         duplicarClasesPensiones = new ClasesPensiones();
         RequestContext.getCurrentInstance().execute("duplicarRegistroClasesPensiones').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarClasesPensiones() {
      duplicarClasesPensiones = new ClasesPensiones();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesPensionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CLASESPENSIONES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesPensionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CLASESPENSIONES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listClasesPensiones.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "CLASESPENSIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("CLASESPENSIONES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ClasesPensiones> getListClasesPensiones() {
      if (listClasesPensiones == null) {
         System.out.println("ControlClasesPensiones getListClasesPensiones");
         listClasesPensiones = administrarClasesPensiones.consultarClasesPensiones();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listClasesPensiones == null || listClasesPensiones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
      }
      return listClasesPensiones;
   }

   public void setListClasesPensiones(List<ClasesPensiones> listClasesPensiones) {
      this.listClasesPensiones = listClasesPensiones;
   }

   public List<ClasesPensiones> getFiltrarClasesPensiones() {
      return filtrarClasesPensiones;
   }

   public void setFiltrarClasesPensiones(List<ClasesPensiones> filtrarClasesPensiones) {
      this.filtrarClasesPensiones = filtrarClasesPensiones;
   }

   public ClasesPensiones getNuevoClasesPensiones() {
      return nuevoClasesPensiones;
   }

   public void setNuevoClasesPensiones(ClasesPensiones nuevoClasesPensiones) {
      this.nuevoClasesPensiones = nuevoClasesPensiones;
   }

   public ClasesPensiones getDuplicarClasesPensiones() {
      return duplicarClasesPensiones;
   }

   public void setDuplicarClasesPensiones(ClasesPensiones duplicarClasesPensiones) {
      this.duplicarClasesPensiones = duplicarClasesPensiones;
   }

   public ClasesPensiones getEditarClasesPensiones() {
      return editarClasesPensiones;
   }

   public void setEditarClasesPensiones(ClasesPensiones editarClasesPensiones) {
      this.editarClasesPensiones = editarClasesPensiones;
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

   public ClasesPensiones getClasesPensionesSeleccionado() {
      return clasesPensionesSeleccionado;
   }

   public void setClasesPensionesSeleccionado(ClasesPensiones clasesPensionesSeleccionado) {
      this.clasesPensionesSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
