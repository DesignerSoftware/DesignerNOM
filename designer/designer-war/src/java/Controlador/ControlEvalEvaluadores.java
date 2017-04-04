/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.EvalEvaluadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEvalEvaluadoresInterface;
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
public class ControlEvalEvaluadores implements Serializable {

   @EJB
   AdministrarEvalEvaluadoresInterface administrarEvalEvaluadores;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<EvalEvaluadores> listEvalEvaluadores;
   private List<EvalEvaluadores> filtrarEvalEvaluadores;
   private List<EvalEvaluadores> crearEvalEvaluadores;
   private List<EvalEvaluadores> modificarEvalEvaluadores;
   private List<EvalEvaluadores> borrarEvalEvaluadores;
   private EvalEvaluadores nuevoEvalEvaluador;
   private EvalEvaluadores duplicarEvalEvaluador;
   private EvalEvaluadores editarEvalEvaluador;
   private EvalEvaluadores evalEvaluadorSeleccionado;
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
   private int tamano;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEvalEvaluadores() {

      listEvalEvaluadores = null;
      crearEvalEvaluadores = new ArrayList<EvalEvaluadores>();
      modificarEvalEvaluadores = new ArrayList<EvalEvaluadores>();
      borrarEvalEvaluadores = new ArrayList<EvalEvaluadores>();
      permitirIndex = true;
      editarEvalEvaluador = new EvalEvaluadores();
      nuevoEvalEvaluador = new EvalEvaluadores();
      duplicarEvalEvaluador = new EvalEvaluadores();
      guardado = true;
      tamano = 270;
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
         String pagActual = "evalevaluador";
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
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEvalEvaluadores.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlEvalEvaluadores.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarEvalEvaluadores.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         System.out.println("ERROR ControlEvalEvaluadores eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   private String backupDescripcion;
   private Integer backupCodigo;

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listEvalEvaluadores.get(index).getSecuencia();
         if (tipoLista == 0) {
            backupCodigo = listEvalEvaluadores.get(index).getCodigo();
            backupDescripcion = listEvalEvaluadores.get(index).getDescripcion();
         } else if (tipoLista == 1) {
            backupCodigo = filtrarEvalEvaluadores.get(index).getCodigo();
            backupDescripcion = filtrarEvalEvaluadores.get(index).getDescripcion();
         }
      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A ControlEvalEvaluadores.asignarIndex \n");
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
         System.out.println("ERROR ControlEvalEvaluadores.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         bandera = 0;
         filtrarEvalEvaluadores = null;
         tipoLista = 0;
      }

      borrarEvalEvaluadores.clear();
      crearEvalEvaluadores.clear();
      modificarEvalEvaluadores.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalEvaluadores = null;
      guardado = true;
      permitirIndex = true;
      getListEvalEvaluadores();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalEvaluadores == null || listEvalEvaluadores.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {  limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         bandera = 0;
         filtrarEvalEvaluadores = null;
         tipoLista = 0;
      }

      borrarEvalEvaluadores.clear();
      crearEvalEvaluadores.clear();
      modificarEvalEvaluadores.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalEvaluadores = null;
      guardado = true;
      permitirIndex = true;
      getListEvalEvaluadores();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalEvaluadores == null || listEvalEvaluadores.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         bandera = 0;
         filtrarEvalEvaluadores = null;
         tipoLista = 0;
      }
   }

   public void modificarEvalEvaluador(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR EVALDIMENSIONES");
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;

      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearEvalEvaluadores.contains(listEvalEvaluadores.get(indice))) {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (listEvalEvaluadores.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalEvaluadores.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalEvaluadores.size(); j++) {
                     if (j != indice) {
                        if (listEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     banderita = false;
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalEvaluadores.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalEvaluadores.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalEvaluadores.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarEvalEvaluadores.isEmpty()) {
                     modificarEvalEvaluadores.add(listEvalEvaluadores.get(indice));
                  } else if (!modificarEvalEvaluadores.contains(listEvalEvaluadores.get(indice))) {
                     modificarEvalEvaluadores.add(listEvalEvaluadores.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (listEvalEvaluadores.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalEvaluadores.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalEvaluadores.size(); j++) {
                     if (j != indice) {
                        if (listEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalEvaluadores.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalEvaluadores.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalEvaluadores.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);

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
               RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearEvalEvaluadores.contains(filtrarEvalEvaluadores.get(indice))) {
            if (filtrarEvalEvaluadores.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalEvaluadores.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalEvaluadores.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalEvaluadores.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalEvaluadores.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalEvaluadores.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalEvaluadores.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarEvalEvaluadores.isEmpty()) {
                  modificarEvalEvaluadores.add(filtrarEvalEvaluadores.get(indice));
               } else if (!modificarEvalEvaluadores.contains(filtrarEvalEvaluadores.get(indice))) {
                  modificarEvalEvaluadores.add(filtrarEvalEvaluadores.get(indice));
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
            if (filtrarEvalEvaluadores.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalEvaluadores.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalEvaluadores.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalEvaluadores.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalEvaluadores.get(indice).getCodigo() == listEvalEvaluadores.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalEvaluadores.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalEvaluadores.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalEvaluadores.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalEvaluadores.get(indice).setDescripcion(backupDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrarEvalEvaluadores() {

      if (index >= 0) {

         if (tipoLista == 0) {
            System.out.println("Entro a borrarMotivosContratos");
            if (!modificarEvalEvaluadores.isEmpty() && modificarEvalEvaluadores.contains(listEvalEvaluadores.get(index))) {
               int modIndex = modificarEvalEvaluadores.indexOf(listEvalEvaluadores.get(index));
               modificarEvalEvaluadores.remove(modIndex);
               borrarEvalEvaluadores.add(listEvalEvaluadores.get(index));
            } else if (!crearEvalEvaluadores.isEmpty() && crearEvalEvaluadores.contains(listEvalEvaluadores.get(index))) {
               int crearIndex = crearEvalEvaluadores.indexOf(listEvalEvaluadores.get(index));
               crearEvalEvaluadores.remove(crearIndex);
            } else {
               borrarEvalEvaluadores.add(listEvalEvaluadores.get(index));
            }
            listEvalEvaluadores.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrarMotivosContratos ");
            if (!modificarEvalEvaluadores.isEmpty() && modificarEvalEvaluadores.contains(filtrarEvalEvaluadores.get(index))) {
               int modIndex = modificarEvalEvaluadores.indexOf(filtrarEvalEvaluadores.get(index));
               modificarEvalEvaluadores.remove(modIndex);
               borrarEvalEvaluadores.add(filtrarEvalEvaluadores.get(index));
            } else if (!crearEvalEvaluadores.isEmpty() && crearEvalEvaluadores.contains(filtrarEvalEvaluadores.get(index))) {
               int crearIndex = crearEvalEvaluadores.indexOf(filtrarEvalEvaluadores.get(index));
               crearEvalEvaluadores.remove(crearIndex);
            } else {
               borrarEvalEvaluadores.add(filtrarEvalEvaluadores.get(index));
            }
            int VCIndex = listEvalEvaluadores.indexOf(filtrarEvalEvaluadores.get(index));
            listEvalEvaluadores.remove(VCIndex);
            filtrarEvalEvaluadores.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         index = -1;
         secRegistro = null;

      }

   }

   public void verificarBorrado() {
      System.out.println("Estoy en verificarBorrado");
      BigInteger borradoVC = new BigInteger("-1");
      try {
         if (tipoLista == 0) {
            borradoVC = administrarEvalEvaluadores.verificarEvalPruebas(listEvalEvaluadores.get(index).getSecuencia());
         } else {
            borradoVC = administrarEvalEvaluadores.verificarEvalPruebas(filtrarEvalEvaluadores.get(index).getSecuencia());
         }
         if (borradoVC.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrarEvalEvaluadores();
         } else {
            System.out.println("Borrado>0");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            borradoVC = new BigInteger("-1");
         }

      } catch (Exception e) {
         System.err.println("ERROR ControlMotivosContratos verificarBorrado ERROR " + e);
      }
   }

   public void guardarEvalEvaluadores() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando Motivos Contratos");
         if (!borrarEvalEvaluadores.isEmpty()) {
            administrarEvalEvaluadores.borrarEvalEvaluadores(borrarEvalEvaluadores);
            //mostrarBorrados
            registrosBorrados = borrarEvalEvaluadores.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEvalEvaluadores.clear();
         }
         if (!crearEvalEvaluadores.isEmpty()) {
            administrarEvalEvaluadores.crearEvalEvaluadores(crearEvalEvaluadores);
            crearEvalEvaluadores.clear();
         }
         if (!modificarEvalEvaluadores.isEmpty()) {
            administrarEvalEvaluadores.modificarEvalEvaluadores(modificarEvalEvaluadores);
            modificarEvalEvaluadores.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listEvalEvaluadores = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEvalEvaluador = listEvalEvaluadores.get(index);
         }
         if (tipoLista == 1) {
            editarEvalEvaluador = filtrarEvalEvaluadores.get(index);
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

   public void agregarNuevoEvalEvaluadores() {
      System.out.println("Agregar Motivo Contrato");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEvalEvaluador.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoEvalEvaluador.getCodigo());

         for (int x = 0; x < listEvalEvaluadores.size(); x++) {
            if (listEvalEvaluadores.get(x).getCodigo() == nuevoEvalEvaluador.getCodigo()) {
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
      if (nuevoEvalEvaluador.getDescripcion() == (null)) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
            bandera = 0;
            filtrarEvalEvaluadores = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
         k++;
         l = BigInteger.valueOf(k);
         nuevoEvalEvaluador.setSecuencia(l);

         crearEvalEvaluadores.add(nuevoEvalEvaluador);

         listEvalEvaluadores.add(nuevoEvalEvaluador);
         nuevoEvalEvaluador = new EvalEvaluadores();

         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         System.out.println("Despues de la bandera guardado");

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEvaluadores').hide()");
         index = -1;
         secRegistro = null;
         System.out.println("Despues de nuevoRegistroEvalEvaluadores");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEvalEvaluadores() {
      System.out.println("limpiarnuevoEvalEvaluadores");
      nuevoEvalEvaluador = new EvalEvaluadores();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicarEvalEvaluadores() {
      System.out.println("duplicarEvalsEvaluadores");
      if (index >= 0) {
         duplicarEvalEvaluador = new EvalEvaluadores();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarEvalEvaluador.setSecuencia(l);
            duplicarEvalEvaluador.setCodigo(listEvalEvaluadores.get(index).getCodigo());
            duplicarEvalEvaluador.setDescripcion(listEvalEvaluadores.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarEvalEvaluador.setSecuencia(l);
            duplicarEvalEvaluador.setCodigo(filtrarEvalEvaluadores.get(index).getCodigo());
            duplicarEvalEvaluador.setDescripcion(filtrarEvalEvaluadores.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvalsEvaluadores");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalEvaluadores').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      System.err.println("ConfirmarDuplicar codigo " + duplicarEvalEvaluador.getCodigo());
      System.err.println("ConfirmarDuplicar nombre " + duplicarEvalEvaluador.getDescripcion());

      if (duplicarEvalEvaluador.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listEvalEvaluadores.size(); x++) {
            if (listEvalEvaluadores.get(x).getCodigo() == duplicarEvalEvaluador.getCodigo()) {
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
      if (duplicarEvalEvaluador.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarEvalEvaluador.getSecuencia() + "  " + duplicarEvalEvaluador.getCodigo());
         if (crearEvalEvaluadores.contains(duplicarEvalEvaluador)) {
            System.out.println("Ya lo contengo.");
         }
         listEvalEvaluadores.add(duplicarEvalEvaluador);
         crearEvalEvaluadores.add(duplicarEvalEvaluador);
         RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalEvaluadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalEvaluadores");
            bandera = 0;
            filtrarEvalEvaluadores = null;
            tipoLista = 0;
         }
         duplicarEvalEvaluador = new EvalEvaluadores();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalEvaluadores').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarduplicarEvalEvaluadores() {
      duplicarEvalEvaluador = new EvalEvaluadores();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalEvaluadoresExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Evaluadores", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalEvaluadoresExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Evaluadores", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listEvalEvaluadores.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "EVALEVALUADORES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EVALEVALUADORES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //***/*/*/*/*-*-*--*/-*/-*/*-/*/*/*/-*/-*/-*/-*/*/-*/**/****----*///**-----
   public List<EvalEvaluadores> getListEvalEvaluadores() {
      if (listEvalEvaluadores == null) {
         listEvalEvaluadores = administrarEvalEvaluadores.consultarEvalEvaluadores();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalEvaluadores == null || listEvalEvaluadores.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalEvaluadores.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listEvalEvaluadores;
   }

   public void setListEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
      this.listEvalEvaluadores = listEvalEvaluadores;
   }

   public List<EvalEvaluadores> getFiltrarEvalEvaluadores() {
      return filtrarEvalEvaluadores;
   }

   public void setFiltrarEvalEvaluadores(List<EvalEvaluadores> filtrarEvalEvaluadores) {
      this.filtrarEvalEvaluadores = filtrarEvalEvaluadores;
   }

   public EvalEvaluadores getNuevoEvalEvaluador() {
      return nuevoEvalEvaluador;
   }

   public void setNuevoEvalEvaluador(EvalEvaluadores nuevoEvalEvaluador) {
      this.nuevoEvalEvaluador = nuevoEvalEvaluador;
   }

   public EvalEvaluadores getDuplicarEvalEvaluador() {
      return duplicarEvalEvaluador;
   }

   public void setDuplicarEvalEvaluador(EvalEvaluadores duplicarEvalEvaluador) {
      this.duplicarEvalEvaluador = duplicarEvalEvaluador;
   }

   public EvalEvaluadores getEditarEvalEvaluador() {
      return editarEvalEvaluador;
   }

   public void setEditarEvalEvaluador(EvalEvaluadores editarEvalEvaluador) {
      this.editarEvalEvaluador = editarEvalEvaluador;
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

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public EvalEvaluadores getEvalEvaluadorSeleccionado() {
      return evalEvaluadorSeleccionado;
   }

   public void setEvalEvaluadorSeleccionado(EvalEvaluadores evalEvaluadorSeleccionado) {
      this.evalEvaluadorSeleccionado = evalEvaluadorSeleccionado;
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

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
