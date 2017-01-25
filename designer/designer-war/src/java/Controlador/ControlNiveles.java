/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Niveles;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNivelesInterface;
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
public class ControlNiveles implements Serializable {

   @EJB
   AdministrarNivelesInterface administrarNiveles;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Niveles> listNiveles;
   private List<Niveles> filtrarNiveles;
   private List<Niveles> crearNiveles;
   private List<Niveles> modificarNiveles;
   private List<Niveles> borrarNiveles;
   private Niveles nuevoNiveles;
   private Niveles duplicarNiveles;
   private Niveles editarNiveles;
   private Niveles nivelSeleccionado;
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
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNiveles() {
      listNiveles = null;
      crearNiveles = new ArrayList<Niveles>();
      modificarNiveles = new ArrayList<Niveles>();
      borrarNiveles = new ArrayList<Niveles>();
      permitirIndex = true;
      editarNiveles = new Niveles();
      nuevoNiveles = new Niveles();
      duplicarNiveles = new Niveles();
      guardado = true;
      tamano = 270;
      System.out.println("controlNiveles Constructor");
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
         String pagActual = "nivel";
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
         System.out.println("ControlNiveles PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNiveles.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlNiveles.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarNiveles.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         System.out.println("ERROR ControlNiveles eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backUpCodigo = listNiveles.get(index).getCodigo();
               System.out.println(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
               backUpDescripcion = listNiveles.get(index).getDescripcion();
               System.out.println(" backUpDescripcion : " + backUpDescripcion);
            }
            secRegistro = listNiveles.get(index).getSecuencia();
         } else {
            if (cualCelda == 0) {
               backUpCodigo = filtrarNiveles.get(index).getCodigo();
               System.out.println(" backUpCodigo : " + backUpCodigo);

            } else if (cualCelda == 1) {
               backUpDescripcion = filtrarNiveles.get(index).getDescripcion();
               System.out.println(" backUpDescripcion : " + backUpDescripcion);

            }
            secRegistro = filtrarNiveles.get(index).getSecuencia();
         }

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A ControlNiveles.asignarIndex \n");
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
         System.out.println("ERROR ControlNiveles.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         bandera = 0;
         filtrarNiveles = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarNiveles.clear();
      crearNiveles.clear();
      modificarNiveles.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listNiveles = null;
      guardado = true;
      permitirIndex = true;
      getListNiveles();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listNiveles == null || listNiveles.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listNiveles.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosNiveles");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         bandera = 0;
         filtrarNiveles = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarNiveles.clear();
      crearNiveles.clear();
      modificarNiveles.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listNiveles = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosNiveles");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         bandera = 0;
         filtrarNiveles = null;
         tipoLista = 0;
      }
   }

   public void modificarNiveles(int indice, String confirmarCambio, String valorConfirmar) {
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
            if (!crearNiveles.contains(listNiveles.get(indice))) {
               if (listNiveles.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listNiveles.size(); j++) {
                     if (j != indice) {
                        if (listNiveles.get(indice).getCodigo() == listNiveles.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listNiveles.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listNiveles.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listNiveles.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setDescripcion(backUpDescripcion);
               }

               if (banderita == true) {
                  if (modificarNiveles.isEmpty()) {
                     modificarNiveles.add(listNiveles.get(indice));
                  } else if (!modificarNiveles.contains(listNiveles.get(indice))) {
                     modificarNiveles.add(listNiveles.get(indice));
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
               if (listNiveles.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listNiveles.size(); j++) {
                     if (j != indice) {
                        if (listNiveles.get(indice).getCodigo() == listNiveles.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listNiveles.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listNiveles.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listNiveles.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listNiveles.get(indice).setDescripcion(backUpDescripcion);
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
         } else if (!crearNiveles.contains(filtrarNiveles.get(indice))) {
            if (filtrarNiveles.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarNiveles.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < listNiveles.size(); j++) {
                  if (j != indice) {
                     if (filtrarNiveles.get(indice).getCodigo() == listNiveles.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarNiveles.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarNiveles.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarNiveles.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarNiveles.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarNiveles.get(indice).setDescripcion(backUpDescripcion);
            }

            if (banderita == true) {
               if (modificarNiveles.isEmpty()) {
                  modificarNiveles.add(filtrarNiveles.get(indice));
               } else if (!modificarNiveles.contains(filtrarNiveles.get(indice))) {
                  modificarNiveles.add(filtrarNiveles.get(indice));
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
            if (filtrarNiveles.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarNiveles.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < listNiveles.size(); j++) {
                  if (j != indice) {
                     if (filtrarNiveles.get(indice).getCodigo() == listNiveles.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarNiveles.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarNiveles.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarNiveles.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarNiveles.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarNiveles.get(indice).setDescripcion(backUpDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoNiveles() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoNiveles");
            if (!modificarNiveles.isEmpty() && modificarNiveles.contains(listNiveles.get(index))) {
               int modIndex = modificarNiveles.indexOf(listNiveles.get(index));
               modificarNiveles.remove(modIndex);
               borrarNiveles.add(listNiveles.get(index));
            } else if (!crearNiveles.isEmpty() && crearNiveles.contains(listNiveles.get(index))) {
               int crearIndex = crearNiveles.indexOf(listNiveles.get(index));
               crearNiveles.remove(crearIndex);
            } else {
               borrarNiveles.add(listNiveles.get(index));
            }
            listNiveles.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoNiveles ");
            if (!modificarNiveles.isEmpty() && modificarNiveles.contains(filtrarNiveles.get(index))) {
               int modIndex = modificarNiveles.indexOf(filtrarNiveles.get(index));
               modificarNiveles.remove(modIndex);
               borrarNiveles.add(filtrarNiveles.get(index));
            } else if (!crearNiveles.isEmpty() && crearNiveles.contains(filtrarNiveles.get(index))) {
               int crearIndex = crearNiveles.indexOf(filtrarNiveles.get(index));
               crearNiveles.remove(crearIndex);
            } else {
               borrarNiveles.add(filtrarNiveles.get(index));
            }
            int VCIndex = listNiveles.indexOf(filtrarNiveles.get(index));
            listNiveles.remove(VCIndex);
            filtrarNiveles.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         infoRegistro = "Cantidad de registros: " + listNiveles.size();
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
      BigInteger contarEvalConvocatoriasNivel;
      BigInteger contarPlantasNivel;
      BigInteger contarPlantasPersonalesNivel;

      try {
         System.err.println("Control Secuencia de ControlNiveles ");
         if (tipoLista == 0) {
            contarEvalConvocatoriasNivel = administrarNiveles.contarEvalConvocatoriasNivel(listNiveles.get(index).getSecuencia());
            contarPlantasNivel = administrarNiveles.contarPlantasNivel(listNiveles.get(index).getSecuencia());
            contarPlantasPersonalesNivel = administrarNiveles.contarPlantasPersonalesNivel(listNiveles.get(index).getSecuencia());
         } else {
            contarEvalConvocatoriasNivel = administrarNiveles.contarEvalConvocatoriasNivel(filtrarNiveles.get(index).getSecuencia());
            contarPlantasNivel = administrarNiveles.contarPlantasNivel(filtrarNiveles.get(index).getSecuencia());
            contarPlantasPersonalesNivel = administrarNiveles.contarPlantasPersonalesNivel(filtrarNiveles.get(index).getSecuencia());
         }
         if (contarEvalConvocatoriasNivel.equals(new BigInteger("0"))
                 && contarPlantasNivel.equals(new BigInteger("0"))
                 && contarPlantasPersonalesNivel.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoNiveles();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarEvalConvocatoriasNivel = new BigInteger("-1");
            contarPlantasNivel = new BigInteger("-1");
            contarPlantasPersonalesNivel = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlNiveles verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarNiveles.isEmpty() || !crearNiveles.isEmpty() || !modificarNiveles.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarNiveles() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarNiveles");
         if (!borrarNiveles.isEmpty()) {
            administrarNiveles.borrarNiveles(borrarNiveles);
            //mostrarBorrados
            registrosBorrados = borrarNiveles.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarNiveles.clear();
         }
         if (!modificarNiveles.isEmpty()) {
            administrarNiveles.modificarNiveles(modificarNiveles);
            modificarNiveles.clear();
         }
         if (!crearNiveles.isEmpty()) {
            administrarNiveles.crearNiveles(crearNiveles);
            crearNiveles.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listNiveles = null;
         RequestContext.getCurrentInstance().update("form:datosNiveles");
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
            editarNiveles = listNiveles.get(index);
         }
         if (tipoLista == 1) {
            editarNiveles = filtrarNiveles.get(index);
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

   public void agregarNuevoNiveles() {
      System.out.println("agregarNuevoNiveles");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoNiveles.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoNiveles.getCodigo());

         for (int x = 0; x < listNiveles.size(); x++) {
            if (listNiveles.get(x).getCodigo() == nuevoNiveles.getCodigo()) {
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
      if (nuevoNiveles.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoNiveles.getDescripcion().isEmpty()) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosNiveles");
            bandera = 0;
            filtrarNiveles = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoNiveles.setSecuencia(l);

         crearNiveles.add(nuevoNiveles);

         listNiveles.add(nuevoNiveles);
         nuevoNiveles = new Niveles();
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         infoRegistro = "Cantidad de registros: " + listNiveles.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroNiveles').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoNiveles() {
      System.out.println("limpiarNuevoNiveles");
      nuevoNiveles = new Niveles();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoNiveles() {
      System.out.println("duplicandoNiveles");
      if (index >= 0) {
         duplicarNiveles = new Niveles();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarNiveles.setSecuencia(l);
            duplicarNiveles.setCodigo(listNiveles.get(index).getCodigo());
            duplicarNiveles.setDescripcion(listNiveles.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarNiveles.setSecuencia(l);
            duplicarNiveles.setCodigo(filtrarNiveles.get(index).getCodigo());
            duplicarNiveles.setDescripcion(filtrarNiveles.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroNiveles').show()");
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
      System.err.println("ConfirmarDuplicar codigo " + duplicarNiveles.getCodigo());
      System.err.println("ConfirmarDuplicar Descripcion " + duplicarNiveles.getDescripcion());

      if (duplicarNiveles.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listNiveles.size(); x++) {
            if (listNiveles.get(x).getCodigo() == duplicarNiveles.getCodigo()) {
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
      if (duplicarNiveles.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarNiveles.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarNiveles.getSecuencia() + "  " + duplicarNiveles.getCodigo());
         if (crearNiveles.contains(duplicarNiveles)) {
            System.out.println("Ya lo contengo.");
         }
         listNiveles.add(duplicarNiveles);
         crearNiveles.add(duplicarNiveles);
         RequestContext.getCurrentInstance().update("form:datosNiveles");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listNiveles.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosNiveles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosNiveles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosNiveles");
            bandera = 0;
            filtrarNiveles = null;
            tipoLista = 0;
         }
         duplicarNiveles = new Niveles();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroNiveles').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarNiveles() {
      duplicarNiveles = new Niveles();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNivelesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "NIVELES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNivelesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NIVELES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listNiveles.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "NIVELES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("NIVELES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Niveles> getListNiveles() {
      if (listNiveles == null) {
         System.out.println("ControlNiveles getListNiveles");
         listNiveles = administrarNiveles.consultarNiveles();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listNiveles == null || listNiveles.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listNiveles.size();
      }
      return listNiveles;
   }

   public void setListNiveles(List<Niveles> listNiveles) {
      this.listNiveles = listNiveles;
   }

   public List<Niveles> getFiltrarNiveles() {
      return filtrarNiveles;
   }

   public void setFiltrarNiveles(List<Niveles> filtrarNiveles) {
      this.filtrarNiveles = filtrarNiveles;
   }

   public Niveles getNuevoNiveles() {
      return nuevoNiveles;
   }

   public void setNuevoNiveles(Niveles nuevoNiveles) {
      this.nuevoNiveles = nuevoNiveles;
   }

   public Niveles getDuplicarNiveles() {
      return duplicarNiveles;
   }

   public void setDuplicarNiveles(Niveles duplicarNiveles) {
      this.duplicarNiveles = duplicarNiveles;
   }

   public Niveles getEditarNiveles() {
      return editarNiveles;
   }

   public void setEditarNiveles(Niveles editarNiveles) {
      this.editarNiveles = editarNiveles;
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

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public Niveles getNivelSeleccionado() {
      return nivelSeleccionado;
   }

   public void setNivelSeleccionado(Niveles nivelSeleccionado) {
      this.nivelSeleccionado = nivelSeleccionado;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
