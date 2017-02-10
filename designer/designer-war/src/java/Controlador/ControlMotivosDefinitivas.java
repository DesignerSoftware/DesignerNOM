/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosDefinitivas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosDefinitivasInterface;
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
public class ControlMotivosDefinitivas implements Serializable {

   @EJB
   AdministrarMotivosDefinitivasInterface administrarMotivosDefinitivas;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<MotivosDefinitivas> listMotivosDefinitivas;
   private List<MotivosDefinitivas> filtrarMotivosDefinitivas;
   private List<MotivosDefinitivas> crearMotivosDefinitivas;
   private List<MotivosDefinitivas> modificarMotivosDefinitivas;
   private List<MotivosDefinitivas> borrarMotivosDefinitivas;
   private MotivosDefinitivas nuevoMotivoDefinitiva;
   private MotivosDefinitivas duplicarMotivoDefinitiva;
   private MotivosDefinitivas editarMotivoDefinitiva;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion, retiro, cambioRegimen, catedratico;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlMotivosDefinitivas() {
      listMotivosDefinitivas = null;
      crearMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
      modificarMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
      borrarMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
      permitirIndex = true;
      editarMotivoDefinitiva = new MotivosDefinitivas();
      nuevoMotivoDefinitiva = new MotivosDefinitivas();
      duplicarMotivoDefinitiva = new MotivosDefinitivas();
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
      } else {
         String pagActual = "motivodefinitiva";
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

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarMotivosDefinitivas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A CONTROLMOTIVOSDEFINITIVAS EVENTOFILTRAR \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
      } catch (Exception e) {
         System.err.println("ERROR CONTROLMOTIVOSDEFINITIVAS EVENTOFILTRAR  ERROR =" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listMotivosDefinitivas.get(index).getSecuencia();

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A CONTROLMOTIVOSDEFINITIVAS ASIGNAR INDEX \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("TIPO ACTUALIZACION : " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         System.out.println("ERROR CONTROLMOTIVOSDEFINITIVAS ASIGNAR INDEX ERROR = " + e);
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:retiro");
         retiro.setFilterStyle("display: none; visibility: hidden;");
         cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:cambioRegimen");
         cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
         catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:catedratico");
         catedratico.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         bandera = 0;
         filtrarMotivosDefinitivas = null;
         tipoLista = 0;
      }

      borrarMotivosDefinitivas.clear();
      crearMotivosDefinitivas.clear();
      modificarMotivosDefinitivas.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listMotivosDefinitivas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {

         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:retiro");
         retiro.setFilterStyle("width: 85% !important;");
         cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:cambioRegimen");
         cambioRegimen.setFilterStyle("width: 85% !important;");
         catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:catedratico");
         catedratico.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:retiro");
         retiro.setFilterStyle("display: none; visibility: hidden;");
         cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:cambioRegimen");
         cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
         catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:catedratico");
         catedratico.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         bandera = 0;
         filtrarMotivosDefinitivas = null;
         tipoLista = 0;
      }
   }

   public void mostrarInfo(int indice, int celda) {
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         cualCelda = celda;
         secRegistro = listMotivosDefinitivas.get(index).getSecuencia();
         if (cualCelda == 2) {
            if (listMotivosDefinitivas.get(indice).getVariableRetiro().equals("SI")) {
               listMotivosDefinitivas.get(indice).setRetiro("S");
            } else if (listMotivosDefinitivas.get(indice).getVariableRetiro().equals("NO")) {
               listMotivosDefinitivas.get(indice).setRetiro("N");
            }
         } else if (cualCelda == 3) {
            if (listMotivosDefinitivas.get(indice).getVariableCambioRegimen().equals("SI")) {
               listMotivosDefinitivas.get(indice).setCambioregimen("S");
            } else if (listMotivosDefinitivas.get(indice).getVariableCambioRegimen().equals("NO")) {
               listMotivosDefinitivas.get(indice).setCambioregimen("N");
            } else if (listMotivosDefinitivas.get(indice).getVariableCambioRegimen().equals(" ")) {
               listMotivosDefinitivas.get(indice).setCambioregimen(null);
            }
         } else if (cualCelda == 4) {
            if (listMotivosDefinitivas.get(indice).getVariableCatedratico().equals("SI")) {
               listMotivosDefinitivas.get(indice).setCatedraticosemestral("S");
            } else if (listMotivosDefinitivas.get(indice).getVariableCatedratico().equals("NO")) {
               listMotivosDefinitivas.get(indice).setCatedraticosemestral("N");
            }
         }
         if (!crearMotivosDefinitivas.contains(listMotivosDefinitivas.get(indice))) {

            if (modificarMotivosDefinitivas.isEmpty()) {
               modificarMotivosDefinitivas.add(listMotivosDefinitivas.get(indice));
            } else if (!modificarMotivosDefinitivas.contains(listMotivosDefinitivas.get(indice))) {
               modificarMotivosDefinitivas.add(listMotivosDefinitivas.get(indice));
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);

   }

   public void modificandoMotivoDefinitivas(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR MOTIVOSDEFINITIVAS");
      index = indice;

      int contador = 0;
      int contadorGuardar = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR MOTIVOSDEFINITIVAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearMotivosDefinitivas.contains(listMotivosDefinitivas.get(indice))) {
               if (listMotivosDefinitivas.get(indice).getCodigo() == a || listMotivosDefinitivas.get(indice).getCodigo().equals(null)) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  for (int j = 0; j < listMotivosDefinitivas.size(); j++) {
                     if (j != indice) {
                        if (listMotivosDefinitivas.get(indice).getCodigo().equals(listMotivosDefinitivas.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                  } else {
                     contadorGuardar++;
                  }

               }
               if (listMotivosDefinitivas.get(indice).getNombre().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else if (listMotivosDefinitivas.get(indice).getNombre().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (listMotivosDefinitivas.get(indice).getNombre() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else if (listMotivosDefinitivas.get(indice).getNombre().equals("")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (contadorGuardar == 3) {
                  if (modificarMotivosDefinitivas.isEmpty()) {
                     modificarMotivosDefinitivas.add(listMotivosDefinitivas.get(indice));
                  } else if (!modificarMotivosDefinitivas.contains(listMotivosDefinitivas.get(indice))) {
                     modificarMotivosDefinitivas.add(listMotivosDefinitivas.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                  cancelarModificacion();
               }
               index = -1;
               secRegistro = null;
            }
         } else if (!crearMotivosDefinitivas.contains(filtrarMotivosDefinitivas.get(indice))) {
            if (filtrarMotivosDefinitivas.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               for (int j = 0; j < listMotivosDefinitivas.size(); j++) {
                  if (j != indice) {
                     if (filtrarMotivosDefinitivas.get(indice).getCodigo().equals(listMotivosDefinitivas.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }

               for (int j = 0; j < filtrarMotivosDefinitivas.size(); j++) {
                  if (j != indice) {
                     if (filtrarMotivosDefinitivas.get(indice).getCodigo().equals(filtrarMotivosDefinitivas.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
               } else {
                  contadorGuardar++;
               }

            }

            if (filtrarMotivosDefinitivas.get(indice).getNombre().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (filtrarMotivosDefinitivas.get(indice).getNombre().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               contadorGuardar++;
            }
            if (filtrarMotivosDefinitivas.get(indice).getNombre() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (filtrarMotivosDefinitivas.get(indice).getNombre().equals("")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               contadorGuardar++;
            }
            if (contadorGuardar == 3) {
               if (modificarMotivosDefinitivas.isEmpty()) {
                  modificarMotivosDefinitivas.add(filtrarMotivosDefinitivas.get(indice));
               } else if (!modificarMotivosDefinitivas.contains(filtrarMotivosDefinitivas.get(indice))) {
                  modificarMotivosDefinitivas.add(filtrarMotivosDefinitivas.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();
            }
            index = -1;
            secRegistro = null;
         }
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }
   private BigInteger novedadesSistema;
   private BigInteger verificarParametrosCambiosMasivos;

   public void verificarBorrado() {
      try {
         System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
         System.out.println("secuencia borrado : " + listMotivosDefinitivas.get(index).getSecuencia());
         if (tipoLista == 0) {
            System.out.println("secuencia borrado : " + listMotivosDefinitivas.get(index).getSecuencia());
            novedadesSistema = administrarMotivosDefinitivas.contarNovedadesSistemasMotivoDefinitiva(listMotivosDefinitivas.get(index).getSecuencia());
            verificarParametrosCambiosMasivos = administrarMotivosDefinitivas.contarParametrosCambiosMasivosMotivoDefinitiva(listMotivosDefinitivas.get(index).getSecuencia());
         } else {
            System.out.println("secuencia borrado : " + filtrarMotivosDefinitivas.get(index).getSecuencia());
            novedadesSistema = administrarMotivosDefinitivas.contarNovedadesSistemasMotivoDefinitiva(filtrarMotivosDefinitivas.get(index).getSecuencia());
            verificarParametrosCambiosMasivos = administrarMotivosDefinitivas.contarParametrosCambiosMasivosMotivoDefinitiva(filtrarMotivosDefinitivas.get(index).getSecuencia());
         }
         if (!novedadesSistema.equals(new BigInteger("0")) || !verificarParametrosCambiosMasivos.equals(new BigInteger("0"))) {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            novedadesSistema = new BigInteger("-1");
            verificarParametrosCambiosMasivos = new BigInteger("-1");

         } else {
            System.out.println("Borrado==0");
            borrandoMotivosDefinitivas();
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
      }
   }

   public void borrandoMotivosDefinitivas() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoMotivosDefinitivas");
            if (!modificarMotivosDefinitivas.isEmpty() && modificarMotivosDefinitivas.contains(listMotivosDefinitivas.get(index))) {
               int modIndex = modificarMotivosDefinitivas.indexOf(listMotivosDefinitivas.get(index));
               modificarMotivosDefinitivas.remove(modIndex);
               borrarMotivosDefinitivas.add(listMotivosDefinitivas.get(index));
            } else if (!crearMotivosDefinitivas.isEmpty() && crearMotivosDefinitivas.contains(listMotivosDefinitivas.get(index))) {
               int crearIndex = crearMotivosDefinitivas.indexOf(listMotivosDefinitivas.get(index));
               crearMotivosDefinitivas.remove(crearIndex);
            } else {
               borrarMotivosDefinitivas.add(listMotivosDefinitivas.get(index));
            }
            listMotivosDefinitivas.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoMotivosDefinitivas");
            if (!modificarMotivosDefinitivas.isEmpty() && modificarMotivosDefinitivas.contains(filtrarMotivosDefinitivas.get(index))) {
               int modIndex = modificarMotivosDefinitivas.indexOf(filtrarMotivosDefinitivas.get(index));
               modificarMotivosDefinitivas.remove(modIndex);
               borrarMotivosDefinitivas.add(filtrarMotivosDefinitivas.get(index));
            } else if (!crearMotivosDefinitivas.isEmpty() && crearMotivosDefinitivas.contains(filtrarMotivosDefinitivas.get(index))) {
               int crearIndex = crearMotivosDefinitivas.indexOf(filtrarMotivosDefinitivas.get(index));
               crearMotivosDefinitivas.remove(crearIndex);
            } else {
               borrarMotivosDefinitivas.add(filtrarMotivosDefinitivas.get(index));
            }
            int VCIndex = listMotivosDefinitivas.indexOf(filtrarMotivosDefinitivas.get(index));
            listMotivosDefinitivas.remove(VCIndex);
            filtrarMotivosDefinitivas.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
      }

   }

   public void revisarDialogoGuardar() {

      if (!borrarMotivosDefinitivas.isEmpty() || !crearMotivosDefinitivas.isEmpty() || !modificarMotivosDefinitivas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarMotivoDefinitivas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("REALIZANDO MOTIVOSDEFINITIVAS");
         if (!borrarMotivosDefinitivas.isEmpty()) {
            administrarMotivosDefinitivas.borrarMotivosDefinitivas(borrarMotivosDefinitivas);

            //mostrarBorrados
            registrosBorrados = borrarMotivosDefinitivas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarMotivosDefinitivas.clear();
         }
         if (!crearMotivosDefinitivas.isEmpty()) {
            administrarMotivosDefinitivas.crearMotivosDefinitivas(crearMotivosDefinitivas);

            crearMotivosDefinitivas.clear();
         }
         if (!modificarMotivosDefinitivas.isEmpty()) {
            administrarMotivosDefinitivas.modificarMotivosDefinitivas(modificarMotivosDefinitivas);
            modificarMotivosDefinitivas.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listMotivosDefinitivas = null;
         guardado = true;
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         k = 0;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarMotivoDefinitiva = listMotivosDefinitivas.get(index);
         }
         if (tipoLista == 1) {
            editarMotivoDefinitiva = filtrarMotivosDefinitivas.get(index);
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

   public void agregarNuevoMotivosDefinitivas() {
      System.out.println("agregarNuevoMotivosDefinitivas");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoMotivoDefinitiva.getCodigo() == a) {
         mensajeValidacion = " *Debe Tener Un Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoDefinitiva.getCodigo());

         for (int x = 0; x < listMotivosDefinitivas.size(); x++) {
            if (listMotivosDefinitivas.get(x).getCodigo() == nuevoMotivoDefinitiva.getCodigo()) {
               duplicados++;
            }
         }
         System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO hayan codigos repetidos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;
         }
      }
      if (nuevoMotivoDefinitiva.getNombre() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Debe tener una descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;
      }
      System.err.println("retiro = " + nuevoMotivoDefinitiva.getRetiro());
      System.err.println("cambio regimen = " + nuevoMotivoDefinitiva.getCambioregimen());
      System.err.println("Categratico = " + nuevoMotivoDefinitiva.getCatedraticosemestral());
      if (nuevoMotivoDefinitiva.getRetiro() == null) {
         nuevoMotivoDefinitiva.setRetiro("S");
      }
      if (nuevoMotivoDefinitiva.getCambioregimen() == null || nuevoMotivoDefinitiva.getCambioregimen().equals(" ")) {
         nuevoMotivoDefinitiva.setCambioregimen(null);
      }
      if (nuevoMotivoDefinitiva.getCatedraticosemestral() == null) {
         nuevoMotivoDefinitiva.setCatedraticosemestral("N");
      }
      System.out.println("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:retiro");
            retiro.setFilterStyle("display: none; visibility: hidden;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:cambioRegimen");
            cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:catedratico");
            catedratico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosDefinitivas = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoMotivoDefinitiva.setSecuencia(l);

         crearMotivosDefinitivas.add(nuevoMotivoDefinitiva);

         listMotivosDefinitivas.add(nuevoMotivoDefinitiva);
         nuevoMotivoDefinitiva = new MotivosDefinitivas();
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoMotivosDefinitivas() {
      System.out.println("limpiarNuevoMotivosDefinitivas");
      nuevoMotivoDefinitiva = new MotivosDefinitivas();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoMotivosDefinitivas() {
      System.out.println("duplicandoMotivosDefinitivas");
      if (index >= 0) {
         duplicarMotivoDefinitiva = new MotivosDefinitivas();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarMotivoDefinitiva.setSecuencia(l);
            duplicarMotivoDefinitiva.setCodigo(listMotivosDefinitivas.get(index).getCodigo());
            duplicarMotivoDefinitiva.setNombre(listMotivosDefinitivas.get(index).getNombre());
            duplicarMotivoDefinitiva.setRetiro(listMotivosDefinitivas.get(index).getRetiro());
            duplicarMotivoDefinitiva.setCambioregimen(listMotivosDefinitivas.get(index).getCambioregimen());
            duplicarMotivoDefinitiva.setCatedraticosemestral(listMotivosDefinitivas.get(index).getCatedraticosemestral());
         }
         if (tipoLista == 1) {
            duplicarMotivoDefinitiva.setSecuencia(l);
            duplicarMotivoDefinitiva.setCodigo(filtrarMotivosDefinitivas.get(index).getCodigo());
            duplicarMotivoDefinitiva.setNombre(filtrarMotivosDefinitivas.get(index).getNombre());
            duplicarMotivoDefinitiva.setRetiro(filtrarMotivosDefinitivas.get(index).getRetiro());
            duplicarMotivoDefinitiva.setCambioregimen(filtrarMotivosDefinitivas.get(index).getCambioregimen());
            duplicarMotivoDefinitiva.setCatedraticosemestral(filtrarMotivosDefinitivas.get(index).getCatedraticosemestral());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR MOTIVOSDEFINITIVAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarMotivoDefinitiva.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   * Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listMotivosDefinitivas.size(); x++) {
            if (listMotivosDefinitivas.get(x).getCodigo() == duplicarMotivoDefinitiva.getCodigo()) {
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
      if (duplicarMotivoDefinitiva.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   * Una descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      System.err.println("duplicar retiro = " + duplicarMotivoDefinitiva.getRetiro());
      System.err.println("duplicar cambio regimen = " + duplicarMotivoDefinitiva.getCambioregimen());
      System.err.println("duplicar Categratico = " + duplicarMotivoDefinitiva.getCatedraticosemestral());
      if (duplicarMotivoDefinitiva.getCambioregimen() == null || duplicarMotivoDefinitiva.getCambioregimen().equals("") || duplicarMotivoDefinitiva.getCambioregimen().equals(" ")) {
         duplicarMotivoDefinitiva.setCambioregimen(null);
      }
      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarMotivoDefinitiva.getSecuencia() + "  " + duplicarMotivoDefinitiva.getCodigo());
         if (crearMotivosDefinitivas.contains(duplicarMotivoDefinitiva)) {
            System.out.println("Ya lo contengo.");
         }
         listMotivosDefinitivas.add(duplicarMotivoDefinitiva);
         crearMotivosDefinitivas.add(duplicarMotivoDefinitiva);
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:retiro");
            retiro.setFilterStyle("display: none; visibility: hidden;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:cambioRegimen");
            cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:catedratico");
            catedratico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosDefinitivas = null;
            tipoLista = 0;
         }
         duplicarMotivoDefinitiva = new MotivosDefinitivas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarMotivosDefinitivas() {
      duplicarMotivoDefinitiva = new MotivosDefinitivas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "MOTIVOSDEFINITIVAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "MOTIVOSDEFINITIVAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listMotivosDefinitivas.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSDEFINITIVAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSDEFINITIVAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
   public List<MotivosDefinitivas> getListMotivosDefinitivas() {
      if (listMotivosDefinitivas == null) {
         listMotivosDefinitivas = administrarMotivosDefinitivas.mostrarMotivosDefinitivas();
      }
      return listMotivosDefinitivas;
   }

   public void setListMotivosDefinitivas(List<MotivosDefinitivas> listMotivosDefinitivas) {
      this.listMotivosDefinitivas = listMotivosDefinitivas;
   }

   public List<MotivosDefinitivas> getFiltrarMotivosDefinitivas() {
      return filtrarMotivosDefinitivas;
   }

   public void setFiltrarMotivosDefinitivas(List<MotivosDefinitivas> filtrarMotivosDefinitivas) {
      this.filtrarMotivosDefinitivas = filtrarMotivosDefinitivas;
   }

   public MotivosDefinitivas getNuevoMotivoDefinitiva() {
      return nuevoMotivoDefinitiva;
   }

   public void setNuevoMotivoDefinitiva(MotivosDefinitivas nuevoMotivoDefinitiva) {
      this.nuevoMotivoDefinitiva = nuevoMotivoDefinitiva;
   }

   public MotivosDefinitivas getDuplicarMotivoDefinitiva() {
      return duplicarMotivoDefinitiva;
   }

   public void setDuplicarMotivoDefinitiva(MotivosDefinitivas duplicarMotivoDefinitiva) {
      this.duplicarMotivoDefinitiva = duplicarMotivoDefinitiva;
   }

   public MotivosDefinitivas getEditarMotivoDefinitiva() {
      return editarMotivoDefinitiva;
   }

   public void setEditarMotivoDefinitiva(MotivosDefinitivas editarMotivoDefinitiva) {
      this.editarMotivoDefinitiva = editarMotivoDefinitiva;
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

}
