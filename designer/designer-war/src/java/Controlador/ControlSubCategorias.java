/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SubCategorias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSubCategoriasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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
public class ControlSubCategorias implements Serializable {

   private static Logger log = Logger.getLogger(ControlSubCategorias.class);

   @EJB
   AdministrarSubCategoriasInterface administrarSubCategorias;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<SubCategorias> listSubCategorias;
   private List<SubCategorias> filtrarSubCategorias;
   private List<SubCategorias> crearSubCategorias;
   private List<SubCategorias> modificarSubCategorias;
   private List<SubCategorias> borrarSubCategorias;
   private SubCategorias nuevoSubCategoria;
   private SubCategorias duplicarSubCategoria;
   private SubCategorias editarSubCategoria;
   private SubCategorias subCategoriaSeleccionada;
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

   public ControlSubCategorias() {
      listSubCategorias = null;
      crearSubCategorias = new ArrayList<SubCategorias>();
      modificarSubCategorias = new ArrayList<SubCategorias>();
      borrarSubCategorias = new ArrayList<SubCategorias>();
      permitirIndex = true;
      editarSubCategoria = new SubCategorias();
      nuevoSubCategoria = new SubCategorias();
      duplicarSubCategoria = new SubCategorias();
      guardado = true;
      tamano = 270;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarSubCategorias.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
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
      String pagActual = "subcategoria";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlSubCategorias.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarSubCategorias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlSubCategorias eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backUpCodigo = listSubCategorias.get(indice).getCodigo();
            } else if (cualCelda == 1) {
               backUpDescripcion = listSubCategorias.get(indice).getDescripcion();
            }
         } else if (cualCelda == 0) {
            backUpCodigo = filtrarSubCategorias.get(indice).getCodigo();
         } else if (cualCelda == 1) {
            backUpDescripcion = filtrarSubCategorias.get(indice).getDescripcion();
         }
         secRegistro = listSubCategorias.get(index).getSecuencia();

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlSubCategorias.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         log.warn("Error ControlSubCategorias.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         bandera = 0;
         filtrarSubCategorias = null;
         tipoLista = 0;
      }

      borrarSubCategorias.clear();
      crearSubCategorias.clear();
      modificarSubCategorias.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      tamano = 270;
      listSubCategorias = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listSubCategorias == null || listSubCategorias.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosSubCategoria");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         bandera = 0;
         filtrarSubCategorias = null;
         tipoLista = 0;
      }

      borrarSubCategorias.clear();
      crearSubCategorias.clear();
      modificarSubCategorias.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      tamano = 270;
      listSubCategorias = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listSubCategorias == null || listSubCategorias.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosSubCategoria");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         bandera = 0;
         filtrarSubCategorias = null;
         tipoLista = 0;
      }
   }

   public void modificarSubCategoria(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;

      int contador = 0, pass = 0;

      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearSubCategorias.contains(listSubCategorias.get(indice))) {
               if (listSubCategorias.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listSubCategorias.size(); j++) {
                     if (j != indice) {
                        if (listSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listSubCategorias.get(indice).setCodigo(backUpCodigo);
                  } else {
                     pass++;
                  }

               }
               if (listSubCategorias.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
               } else if (listSubCategorias.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
               } else {
                  pass++;
               }

               if (pass == 2) {
                  if (modificarSubCategorias.isEmpty()) {
                     modificarSubCategorias.add(listSubCategorias.get(indice));
                  } else if (!modificarSubCategorias.contains(listSubCategorias.get(indice))) {
                     modificarSubCategorias.add(listSubCategorias.get(indice));
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

               if (listSubCategorias.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listSubCategorias.size(); j++) {
                     if (j != indice) {
                        if (listSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listSubCategorias.get(indice).setCodigo(backUpCodigo);
                  } else {
                     pass++;
                  }

               }
               if (listSubCategorias.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
               } else if (listSubCategorias.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSubCategorias.get(indice).setDescripcion(backUpDescripcion);
               } else {
                  pass++;
               }

               if (pass == 2) {
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
         } else if (!crearSubCategorias.contains(filtrarSubCategorias.get(indice))) {
            if (filtrarSubCategorias.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < filtrarSubCategorias.size(); j++) {
                  if (j != indice) {
                     if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listSubCategorias.size(); j++) {
                  if (j != indice) {
                     if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
               } else {
                  pass++;
               }

            }

            if (filtrarSubCategorias.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
            } else if (filtrarSubCategorias.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
            } else {
               pass++;
            }

            if (pass == 2) {
               if (modificarSubCategorias.isEmpty()) {
                  modificarSubCategorias.add(filtrarSubCategorias.get(indice));
               } else if (!modificarSubCategorias.contains(filtrarSubCategorias.get(indice))) {
                  modificarSubCategorias.add(filtrarSubCategorias.get(indice));
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
            if (filtrarSubCategorias.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < filtrarSubCategorias.size(); j++) {
                  if (j != indice) {
                     if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listSubCategorias.size(); j++) {
                  if (j != indice) {
                     if (filtrarSubCategorias.get(indice).getCodigo() == listSubCategorias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarSubCategorias.get(indice).setCodigo(backUpCodigo);
               } else {
                  pass++;
               }

            }

            if (filtrarSubCategorias.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
            } else if (filtrarSubCategorias.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSubCategorias.get(indice).setDescripcion(backUpDescripcion);
            } else {
               pass++;
            }

            if (pass == 2) {
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
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoSubCategorias() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoSubCategorias");
            if (!modificarSubCategorias.isEmpty() && modificarSubCategorias.contains(listSubCategorias.get(index))) {
               int modIndex = modificarSubCategorias.indexOf(listSubCategorias.get(index));
               modificarSubCategorias.remove(modIndex);
               borrarSubCategorias.add(listSubCategorias.get(index));
            } else if (!crearSubCategorias.isEmpty() && crearSubCategorias.contains(listSubCategorias.get(index))) {
               int crearIndex = crearSubCategorias.indexOf(listSubCategorias.get(index));
               crearSubCategorias.remove(crearIndex);
            } else {
               borrarSubCategorias.add(listSubCategorias.get(index));
            }
            listSubCategorias.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoSubCategorias ");
            if (!modificarSubCategorias.isEmpty() && modificarSubCategorias.contains(filtrarSubCategorias.get(index))) {
               int modIndex = modificarSubCategorias.indexOf(filtrarSubCategorias.get(index));
               modificarSubCategorias.remove(modIndex);
               borrarSubCategorias.add(filtrarSubCategorias.get(index));
            } else if (!crearSubCategorias.isEmpty() && crearSubCategorias.contains(filtrarSubCategorias.get(index))) {
               int crearIndex = crearSubCategorias.indexOf(filtrarSubCategorias.get(index));
               crearSubCategorias.remove(crearIndex);
            } else {
               borrarSubCategorias.add(filtrarSubCategorias.get(index));
            }
            int VCIndex = listSubCategorias.indexOf(filtrarSubCategorias.get(index));
            listSubCategorias.remove(VCIndex);
            filtrarSubCategorias.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listSubCategorias == null || listSubCategorias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void verificarBorrado() {
      log.info("Estoy en verificarBorrado");
      BigInteger contaEvalConvocatoriasSubCategoria;

      try {
         log.error("Control Secuencia de ControlSubCategorias ");
         if (tipoLista == 0) {
            contaEvalConvocatoriasSubCategoria = administrarSubCategorias.contarEscalafones(listSubCategorias.get(index).getSecuencia());
         } else {
            contaEvalConvocatoriasSubCategoria = administrarSubCategorias.contarEscalafones(filtrarSubCategorias.get(index).getSecuencia());
         }
         if (contaEvalConvocatoriasSubCategoria.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoSubCategorias();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contaEvalConvocatoriasSubCategoria = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlSubCategorias verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarSubCategorias.isEmpty() || !crearSubCategorias.isEmpty() || !modificarSubCategorias.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarSubCategorias() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarSubCategorias");
         if (!borrarSubCategorias.isEmpty()) {
            administrarSubCategorias.borrarSubCategorias(borrarSubCategorias);

            //mostrarBorrados
            registrosBorrados = borrarSubCategorias.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarSubCategorias.clear();
         }
         if (!crearSubCategorias.isEmpty()) {
            administrarSubCategorias.crearSubCategorias(crearSubCategorias);
            crearSubCategorias.clear();
         }
         if (!modificarSubCategorias.isEmpty()) {
            administrarSubCategorias.modificarSubCategorias(modificarSubCategorias);
            modificarSubCategorias.clear();
         }
         log.info("Se guardaron los datos con exito");
         listSubCategorias = null;
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarSubCategoria = listSubCategorias.get(index);
         }
         if (tipoLista == 1) {
            editarSubCategoria = filtrarSubCategorias.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
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

   public void agregarNuevoSubCategorias() {
      log.info("agregarNuevoSubCategorias");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoSubCategoria.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoSubCategoria.getCodigo());

         for (int x = 0; x < listSubCategorias.size(); x++) {
            if (listSubCategorias.get(x).getCodigo() == nuevoSubCategoria.getCodigo()) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
         }
      }
      if (nuevoSubCategoria.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      log.info("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 270;

            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            bandera = 0;
            filtrarSubCategorias = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoSubCategoria.setSecuencia(l);

         crearSubCategorias.add(nuevoSubCategoria);

         listSubCategorias.add(nuevoSubCategoria);
         nuevoSubCategoria = new SubCategorias();
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");

         infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSubCategorias').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoSubCategorias() {
      log.info("limpiarNuevoSubCategorias");
      nuevoSubCategoria = new SubCategorias();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoSubCategorias() {
      log.info("duplicandoSubCategorias");
      if (index >= 0) {
         duplicarSubCategoria = new SubCategorias();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarSubCategoria.setSecuencia(l);
            duplicarSubCategoria.setCodigo(listSubCategorias.get(index).getCodigo());
            duplicarSubCategoria.setDescripcion(listSubCategorias.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarSubCategoria.setSecuencia(l);
            duplicarSubCategoria.setCodigo(filtrarSubCategorias.get(index).getCodigo());
            duplicarSubCategoria.setDescripcion(filtrarSubCategorias.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSubCategorias').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarSubCategoria.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarSubCategoria.getDescripcion());

      if (duplicarSubCategoria.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listSubCategorias.size(); x++) {
            if (listSubCategorias.get(x).getCodigo() == duplicarSubCategoria.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarSubCategoria.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarSubCategoria.getSecuencia() + "  " + duplicarSubCategoria.getCodigo());
         if (crearSubCategorias.contains(duplicarSubCategoria)) {
            log.info("Ya lo contengo.");
         }
         listSubCategorias.add(duplicarSubCategoria);
         crearSubCategorias.add(duplicarSubCategoria);
         RequestContext.getCurrentInstance().update("form:datosSubCategoria");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 270;

            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSubCategoria:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSubCategoria");
            bandera = 0;
            filtrarSubCategorias = null;
            tipoLista = 0;
         }
         duplicarSubCategoria = new SubCategorias();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSubCategorias').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarSubCategorias() {
      duplicarSubCategoria = new SubCategorias();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSubCategoriaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "SUBCATEGORIAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSubCategoriaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "SUBCATEGORIAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listSubCategorias.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "SUBCATEGORIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            log.info("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("SUBCATEGORIAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<SubCategorias> getListSubCategorias() {
      if (listSubCategorias == null) {
         listSubCategorias = administrarSubCategorias.consultarSubCategorias();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listSubCategorias == null || listSubCategorias.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listSubCategorias.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listSubCategorias;
   }

   public void setListSubCategorias(List<SubCategorias> listSubCategorias) {
      this.listSubCategorias = listSubCategorias;
   }

   public List<SubCategorias> getFiltrarSubCategorias() {
      return filtrarSubCategorias;
   }

   public void setFiltrarSubCategorias(List<SubCategorias> filtrarSubCategorias) {
      this.filtrarSubCategorias = filtrarSubCategorias;
   }

   public SubCategorias getNuevoSubCategoria() {
      return nuevoSubCategoria;
   }

   public void setNuevoSubCategoria(SubCategorias nuevoSubCategoria) {
      this.nuevoSubCategoria = nuevoSubCategoria;
   }

   public SubCategorias getDuplicarSubCategoria() {
      return duplicarSubCategoria;
   }

   public void setDuplicarSubCategoria(SubCategorias duplicarSubCategoria) {
      this.duplicarSubCategoria = duplicarSubCategoria;
   }

   public SubCategorias getEditarSubCategoria() {
      return editarSubCategoria;
   }

   public void setEditarSubCategoria(SubCategorias editarSubCategoria) {
      this.editarSubCategoria = editarSubCategoria;
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

   public SubCategorias getSubCategoriaSeleccionada() {
      return subCategoriaSeleccionada;
   }

   public void setSubCategoriaSeleccionada(SubCategorias subCategoriaSeleccionada) {
      this.subCategoriaSeleccionada = subCategoriaSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
