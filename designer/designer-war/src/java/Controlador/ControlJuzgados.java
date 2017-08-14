/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Juzgados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarJuzgadosInterface;
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
public class ControlJuzgados implements Serializable {

   private static Logger log = Logger.getLogger(ControlJuzgados.class);

   @EJB
   AdministrarJuzgadosInterface administrarJuzgados;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
//EMPRESA

   private int banderaModificacionEmpresa;
   private int indiceEmpresaMostrada;
   private List<Juzgados> listJuzgadosPorCiudad;
   private List<Juzgados> filtrarJuzgados;
   private List<Juzgados> crearJuzgados;
   private List<Juzgados> modificarJuzgados;
   private List<Juzgados> borrarJuzgados;
   private Juzgados nuevoJuzgado;
   private Juzgados duplicarJuzgado;
   private Juzgados editarJuzgado;

   private Column codigo, nombre,
           ciudad, oficina,
           observaciones;

   //AUTOCOMPLETAR
   private List<Ciudades> lovCiudades;
   private String ciudadesAutocompletar;
   private Ciudades ciudadSeleccionada;
   private List<Ciudades> lovCiudadesFiltrar;
   private List<Juzgados> listaJuzgadosPorCiudadBoton;
   private boolean banderaSeleccionCentrosCostosPorEmpresa;
   private boolean mostrarTodos;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlJuzgados() {
      listJuzgadosPorCiudad = null;
      guardado = true;
      permitirIndex = true;
      lovCiudades = null;
      ciudadSeleccionada = null;
      indiceEmpresaMostrada = 0;
//        listCentrosCostosPorEmpresaBoton = null;
      crearJuzgados = new ArrayList<Juzgados>();
      modificarJuzgados = new ArrayList<Juzgados>();
      borrarJuzgados = new ArrayList<Juzgados>();
      editarJuzgado = new Juzgados();
      nuevoJuzgado = new Juzgados();
      nuevoJuzgado.setCiudad(new Ciudades());
      duplicarJuzgado = new Juzgados();
      aceptar = true;
      lovCiudadesFiltrar = null;
      guardado = true;
      banderaSeleccionCentrosCostosPorEmpresa = false;
      listaJuzgadosPorCiudadBoton = null;
      mostrarTodos = true;
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
         
      } else {
       */
      String pagActual = "juzgado";

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
         administrarJuzgados.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("ENTRE A CONTROLJUZGADOS EVENTOFILTRAR");
         if (tipoLista == 0) {
            tipoLista = 1;
         }

      } catch (Exception e) {
         log.warn("Error CONTROLJUZGADOS EVENTOFILTRAR ERROR :" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
      log.error("PERMITIR INDEX = " + permitirIndex);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         log.error("CAMBIAR INDICE CUALCELDA = " + cualCelda);
         secRegistro = listJuzgadosPorCiudad.get(index).getSecuencia();
         log.error("Sec Registro = " + secRegistro);
         if (cualCelda == 2) {
            if (tipoLista == 0) {
               ciudadesAutocompletar = listJuzgadosPorCiudad.get(index).getCiudad().getNombre();
               log.error("CONTROLJUZGADOS CIUDADESAUTOCOMPLETAR = " + ciudadesAutocompletar);

            } else {
               ciudadesAutocompletar = filtrarJuzgados.get(index).getCiudad().getNombre();
            }
         }
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void modificandoJuzgados(int indice, String confirmarCambio, String valorConfirmar) {

      log.error("ENTRE A MODIFICAR HV ENTREVISTA");
      index = indice;
      banderaModificacionEmpresa = 1;
      int coincidencias = 0;
      int contadorGuardar = 0;
      int indiceUnicoElemento = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      int contador = 0;
      Short a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.info("ENTRE A MODIFICANDOJUZGADOS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearJuzgados.contains(listJuzgadosPorCiudad.get(indice))) {
               if (listJuzgadosPorCiudad.get(indice).getCodigo().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else if (listJuzgadosPorCiudad.get(indice).getCodigo().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  for (int j = 0; j < listJuzgadosPorCiudad.size(); j++) {
                     if (j != indice) {
                        if (listJuzgadosPorCiudad.get(indice).getCodigo().equals(listJuzgadosPorCiudad.get(j).getCodigo())) {
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

               if (listJuzgadosPorCiudad.get(indice).getNombre().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
               } else if (listJuzgadosPorCiudad.get(indice).getNombre().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
               } else {
                  contadorGuardar++;
               }
               if (listJuzgadosPorCiudad.get(indice).getOficina().equals("") || listJuzgadosPorCiudad.get(indice).getOficina().isEmpty() || listJuzgadosPorCiudad.get(indice).getOficina() == null) {
                  contadorGuardar++;
               } else if (administrarJuzgados.isNumeric(listJuzgadosPorCiudad.get(indice).getOficina()) == true) {
                  contadorGuardar++;
               } else {
                  mensajeValidacion = "EL CAMPO 'Oficina' SOLO ACEPTA NUMEROS";
               }
               if (contadorGuardar == 3) {
                  if (modificarJuzgados.isEmpty()) {
                     modificarJuzgados.add(listJuzgadosPorCiudad.get(indice));
                  } else if (!modificarJuzgados.contains(listJuzgadosPorCiudad.get(indice))) {
                     modificarJuzgados.add(listJuzgadosPorCiudad.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;

                  }
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                  cancelarModificacion();
               }
               index = -1;
               secRegistro = null;
            }
         } else if (!crearJuzgados.contains(filtrarJuzgados.get(indice))) {
            if (filtrarJuzgados.get(indice).getCodigo().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (filtrarJuzgados.get(indice).getCodigo().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               for (int j = 0; j < listJuzgadosPorCiudad.size(); j++) {
                  if (filtrarJuzgados.get(indice).getCodigo().equals(listJuzgadosPorCiudad.get(j).getCodigo())) {
                     contador++;
                  }
               }

               for (int j = 0; j < filtrarJuzgados.size(); j++) {
                  if (j != indice) {
                     if (filtrarJuzgados.get(indice).getCodigo().equals(filtrarJuzgados.get(j).getCodigo())) {
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

            if (filtrarJuzgados.get(indice).getNombre().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
            }
            if (filtrarJuzgados.get(indice).getNombre().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
            } else {
               contadorGuardar++;
            }
            log.error("oficina es igual = " + filtrarJuzgados.get(indice).getOficina());
            if (filtrarJuzgados.get(indice).getOficina().equals("") || filtrarJuzgados.get(indice).getOficina().isEmpty() || filtrarJuzgados.get(indice).getOficina() == null) {
               contadorGuardar++;

            } else if (administrarJuzgados.isNumeric(filtrarJuzgados.get(indice).getOficina()) == true) {
               contadorGuardar++;
            } else {
               mensajeValidacion = "EL CAMPO 'Oficina' SOLO ACEPTA NUMEROS";
            }
            if (contadorGuardar == 3) {
               if (modificarJuzgados.isEmpty()) {
                  modificarJuzgados.add(filtrarJuzgados.get(indice));
               } else if (!modificarJuzgados.contains(filtrarJuzgados.get(indice))) {
                  modificarJuzgados.add(filtrarJuzgados.get(indice));
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
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
      } else if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
         log.error("ENTRE A MODIFICAR CIUDADES , CONFIRMAR CAMBIO ES CIUDADES");
         log.error("CIUDADES AUTOCOMPLETAR" + ciudadesAutocompletar);
         log.error("LISTJUZGADOSPORCIUDAD CIUDAD = " + listJuzgadosPorCiudad.get(indice).getCiudad().getNombre());
         if (tipoLista == 0) {
            log.error("COMPLETAR   ciudadAutocompletar " + ciudadesAutocompletar);
            listJuzgadosPorCiudad.get(indice).getCiudad().setNombre(ciudadesAutocompletar);
         } else {

            filtrarJuzgados.get(indice).getCiudad().setNombre(ciudadesAutocompletar);
         }
         //getListaTiposCentrosCostos();
         for (int i = 0; i < lovCiudades.size(); i++) {
            if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listJuzgadosPorCiudad.get(indice).setCiudad(lovCiudades.get(indiceUnicoElemento));
            } else {
               filtrarJuzgados.get(indice).setCiudad(lovCiudades.get(indiceUnicoElemento));
            }
            lovCiudades.clear();
            lovCiudades = null;
            // getListaTiposCentrosCostos();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      RequestContext.getCurrentInstance().update("form:datosJuzgados");

   }

   public void cancelarModificacion() {
      try {
         log.info("entre a CONTROLJUZGADOS CANCELARMODIFICACION");
         if (bandera == 1) {
            //CERRAR FILTRADO
            //0
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            //1
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            //2
            ciudad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            //3 
            oficina = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:oficina");
            oficina.setFilterStyle("display: none; visibility: hidden;");
            //4
            observaciones = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:observaciones");
            observaciones.setFilterStyle("display: none; visibility: hidden;");

            bandera = 0;
            filtrarJuzgados = null;
            tipoLista = 0;
         }

         borrarJuzgados.clear();
         crearJuzgados.clear();
         modificarJuzgados.clear();
         index = -1;
         k = 0;
         listJuzgadosPorCiudad = null;
         guardado = true;
         permitirIndex = true;
         mostrarTodos = true;
         RequestContext context = RequestContext.getCurrentInstance();
         banderaModificacionEmpresa = 0;
         //   if (banderaModificacionEmpresa == 0) {
         //     cambiarEmpresa();
         // }
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      } catch (Exception E) {
         log.warn("Error CONTROLJUZGADOS CANCELARMODIFICACION ERROR " + E.getMessage());
      }
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("ENTRE A CONTROLJUZGADOS ASIGNARINDEX");
         index = indice;
         RequestContext context = RequestContext.getCurrentInstance();

         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 4) {
            RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
            dig = -1;
         }

      } catch (Exception e) {
         log.warn("Error CONTROLJUZGADOS ASIGNARINDEX ERROR " + e.getMessage());
      }
   }

   public void actualizarCiudades() {
      log.info("\n ENTRE A ControlCentroCosto.actualizarCentroCosto \n");
      try {
         banderaModificacionEmpresa = 1;
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlCentroCosto.actualizarCentroCosto TIPOACTUALIZACION====" + tipoActualizacion);
         if (tipoActualizacion == 0) {
            listJuzgadosPorCiudad.get(index).setCiudad(ciudadSeleccionada);
            if (!crearJuzgados.contains(listJuzgadosPorCiudad.get(index))) {
               if (modificarJuzgados.isEmpty()) {
                  modificarJuzgados.add(listJuzgadosPorCiudad.get(index));
               } else if (!modificarJuzgados.contains(listJuzgadosPorCiudad.get(index))) {
                  modificarJuzgados.add(listJuzgadosPorCiudad.get(index));
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }

            }
            RequestContext.getCurrentInstance().update("form:datosJuzgados");
            RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");
         } else if (tipoActualizacion == 1) {
            // context.reset("formularioDialogos:nuevaTipoCentroCostos");
            log.info("Entro actualizar centro costo nuevo rgistro");
            nuevoJuzgado.setCiudad(ciudadSeleccionada);
            log.info("CIUDAD SELECCIONADA: " + nuevoJuzgado.getCiudad().getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoCentroCostos");
         } else if (tipoActualizacion == 2) {
            duplicarJuzgado.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
         }
         lovCiudadesFiltrar = null;
         ciudadSeleccionada = null;
         aceptar = true;
         index = -1;
         tipoActualizacion = -1;
         permitirIndex = true;

         context.reset("form:lovTipoCentrosCostos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTipoCentrosCostos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");
      } catch (Exception e) {
         log.warn("Error BETA .CONTROLJUZGADOS ERROR " + e);
      }
   }

   public void cancelarCambioCiudades() {
      try {
         lovCiudadesFiltrar = null;
         ciudadSeleccionada = null;
         aceptar = true;
         index = -1;
         tipoActualizacion = -1;
         permitirIndex = true;
         RequestContext context = RequestContext.getCurrentInstance();
         context.reset("form:lovTipoCentrosCostos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTipoCentrosCostos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");
      } catch (Exception e) {
         log.warn("Error CONTROLJUZGADOS CANCELARCAMBIOCIUDADES ERROR=====" + e.getMessage());
      }
   }

   public void activarCtrlF11() {
      log.info("\n ENTRE A CONTROLJUZGADOS ACTIVARCTRLF11 \n");
      try {
         if (bandera == 0) {
            log.info("Activar");
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            ciudad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:ciudad");
            ciudad.setFilterStyle("width: 85% !important;");
            oficina = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:oficina");
            oficina.setFilterStyle("width: 85% !important;");
            observaciones = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:observaciones");
            observaciones.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosJuzgados");
            bandera = 1;
         } else if (bandera == 1) {
            log.info("Desactivar");
            //0
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ciudad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            oficina = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:oficina");
            oficina.setFilterStyle("display: none; visibility: hidden;");
            observaciones = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:observaciones");
            observaciones.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosJuzgados");
            bandera = 0;
            filtrarJuzgados = null;
            tipoLista = 0;
         }
      } catch (Exception e) {

         log.warn("Error CONTROLJUZGADOS ACTIVARCTRLF11 ERROR " + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void lovEmpresas() {
      index = -1;
      secRegistro = null;
      cualCelda = -1;
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
   }

   public void cambiarCiudad() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("Cambiar empresa  GUARDADO = " + guardado);
      log.error("Cambiar empresa  GUARDADO = " + ciudadSeleccionada.getNombre());
      if (guardado == true) {
         listJuzgadosPorCiudad = null;
         listaJuzgadosPorCiudadBoton = null;
         getListaJuzgadosPorCiudadBoton();
         lovCiudadesFiltrar = null;
         aceptar = true;
         context.reset("formularioDialogos:lovEmpresas:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
         //RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
         banderaModificacionEmpresa = 1;
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
         mostrarTodos = false;
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");

      } else {
         banderaModificacionEmpresa = 0;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cancelarCambioCiudad() {
      RequestContext context = RequestContext.getCurrentInstance();

      lovCiudadesFiltrar = null;
      banderaModificacionEmpresa = 0;
      index = -1;
      mostrarTodos = true;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      context.reset("formularioDialogos:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void guardarCambiosJuzgados() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando Operaciones Vigencias Localizacion");
         if (!borrarJuzgados.isEmpty()) {
            administrarJuzgados.borrarJuzgados(borrarJuzgados);

            //mostrarBorrados
            registrosBorrados = borrarJuzgados.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarJuzgados.clear();
         }
         if (!crearJuzgados.isEmpty()) {
            administrarJuzgados.crearJuzgados(crearJuzgados);
            crearJuzgados.clear();
         }
         if (!modificarJuzgados.isEmpty()) {
            administrarJuzgados.modificarJuzgados(modificarJuzgados);

            modificarJuzgados.clear();
         }
         log.info("Se guardaron los datos con exito");
         listJuzgadosPorCiudad = null;
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
         k = 0;
         guardado = true;

         if (banderaModificacionEmpresa == 0) {
            cambiarCiudad();
            banderaModificacionEmpresa = 1;

         }
         /*if (banderaSeleccionCentrosCostosPorEmpresa == true) {
             listCentrosCostosPorEmpresaBoton = null;
             getListCentrosCostosPorEmpresaBoton();
             index = -1;
             RequestContext.getCurrentInstance().update("formularioDialogos:lovCentrosCostos");
             RequestContext.getCurrentInstance().execute("PF('buscarCentrosCostosDialogo').show()");
             banderaSeleccionCentrosCostosPorEmpresa = false;
             }*/
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      banderaModificacionEmpresa = 0;
   }
   private BigInteger verificarEerPrestamos;

   public void verificarBorrado() {
      log.info("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
      try {
         log.info("secuencia borrado : " + listJuzgadosPorCiudad.get(index).getSecuencia());
         if (tipoLista == 0) {
            log.info("secuencia borrado : " + listJuzgadosPorCiudad.get(index).getSecuencia());
            verificarEerPrestamos = administrarJuzgados.verificarEerPrestamos(listJuzgadosPorCiudad.get(index).getSecuencia());
         } else {
            log.info("secuencia borrado : " + filtrarJuzgados.get(index).getSecuencia());
            verificarEerPrestamos = administrarJuzgados.verificarEerPrestamos(filtrarJuzgados.get(index).getSecuencia());
         }
         if (!verificarEerPrestamos.equals(new BigInteger("0"))) {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            verificarEerPrestamos = new BigInteger("-1");

         } else {
            log.info("Borrado==0");
            borrandoJuzgados();
         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
      }
   }

   public void borrandoJuzgados() {

      if (index >= 0) {

         if (tipoLista == 0) {
            log.info("Entro a BORRANDOJUZGADOS");
            if (!modificarJuzgados.isEmpty() && modificarJuzgados.contains(listJuzgadosPorCiudad.get(index))) {
               int modIndex = modificarJuzgados.indexOf(listJuzgadosPorCiudad.get(index));
               modificarJuzgados.remove(modIndex);
               borrarJuzgados.add(listJuzgadosPorCiudad.get(index));
            } else if (!crearJuzgados.isEmpty() && crearJuzgados.contains(listJuzgadosPorCiudad.get(index))) {
               int crearIndex = crearJuzgados.indexOf(listJuzgadosPorCiudad.get(index));
               crearJuzgados.remove(crearIndex);
            } else {
               borrarJuzgados.add(listJuzgadosPorCiudad.get(index));
            }
            listJuzgadosPorCiudad.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoTiposEmbargos");
            if (!modificarJuzgados.isEmpty() && modificarJuzgados.contains(filtrarJuzgados.get(index))) {
               int modIndex = modificarJuzgados.indexOf(filtrarJuzgados.get(index));
               modificarJuzgados.remove(modIndex);
               borrarJuzgados.add(filtrarJuzgados.get(index));
            } else if (!crearJuzgados.isEmpty() && crearJuzgados.contains(filtrarJuzgados.get(index))) {
               int crearIndex = crearJuzgados.indexOf(filtrarJuzgados.get(index));
               crearJuzgados.remove(crearIndex);
            } else {
               borrarJuzgados.add(filtrarJuzgados.get(index));
            }
            int VCIndex = listJuzgadosPorCiudad.indexOf(filtrarJuzgados.get(index));
            listJuzgadosPorCiudad.remove(VCIndex);
            filtrarJuzgados.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void editarCelda() {
      try {
         log.info("\n ENTRE A editarCelda INDEX  " + index);
         if (index >= 0) {
            log.info("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
            if (tipoLista == 0) {
               editarJuzgado = listJuzgadosPorCiudad.get(index);
            }
            if (tipoLista == 1) {
               editarJuzgado = filtrarJuzgados.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCCC");
               RequestContext.getCurrentInstance().execute("PF('editarCCC').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNCC");
               RequestContext.getCurrentInstance().execute("PF('editarNCC').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarTCC");
               RequestContext.getCurrentInstance().execute("PF('editarTCC').show()");
               cualCelda = -1;
            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarMO");
               RequestContext.getCurrentInstance().execute("PF('editarMO').show()");
               cualCelda = -1;
            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCAT");
               RequestContext.getCurrentInstance().execute("PF('editarCAT').show()");
               cualCelda = -1;
            }
         }
         index = -1;
      } catch (Exception E) {
         log.warn("Error ControlCentroCosto.editarCelDa ERROR=====================" + E.getMessage());
      }
   }

   public void listaValoresBoton() {

      try {
         if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 4) {
               log.info("\n ListaValoresBoton \n");
               RequestContext.getCurrentInstance().update("formularioDialogos:tiposCentrosCostosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      } catch (Exception e) {
         log.info("\n ERROR ControlCentroCosto.listaValoresBoton ERROR====================" + e.getMessage());

      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJuzgadosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "JUZGADOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
   }

   /**
    *
    * @throws IOException
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJuzgadosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "JUZGADOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listJuzgadosPorCiudad.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "JUZGADOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("JUZGADOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }
   private String nuevoYduplicarCiudadCompletar;

   public void valoresBackupAutocompletar(int tipoNuevo) {
      log.info("1...");
      if (tipoNuevo == 1) {
         nuevoYduplicarCiudadCompletar = nuevoJuzgado.getCiudad().getNombre();
      } else if (tipoNuevo == 2) {
         nuevoYduplicarCiudadCompletar = duplicarJuzgado.getCiudad().getNombre();
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
         log.info(" nueva Ciudad    Entro al if 'Centro costo'");
         log.info("NOMBRE CENTRO COSTO: " + nuevoJuzgado.getCiudad().getNombre());

         if (!nuevoJuzgado.getCiudad().getNombre().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCiudadCompletar: " + nuevoYduplicarCiudadCompletar);
            nuevoJuzgado.getCiudad().setNombre(nuevoYduplicarCiudadCompletar);
            getLovCiudades();
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoJuzgado.setCiudad(lovCiudades.get(indiceUnicoElemento));
               lovCiudades = null;
               getLovCiudades();
               log.error("CIUDAD GUARDADA EN NUEVO JUZGADO " + nuevoJuzgado.getCiudad().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoJuzgado.getCiudad().setNombre(nuevoYduplicarCiudadCompletar);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoJuzgado.setCiudad(new Ciudades());
            nuevoJuzgado.getCiudad().setNombre(" ");
            log.info("NUEVO CIUDAD" + nuevoJuzgado.getCiudad().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoGrupoTipoCC");
      }

   }

   public void asignarVariableCiudad(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
      RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
   }

   public void limpiarNuevoJuzgado() {
      try {
         log.info("\n ENTRE A LIMPIAR NUEVO JUZGADO \n");
         nuevoJuzgado = new Juzgados();
         nuevoJuzgado.setCiudad(new Ciudades());
         index = -1;
      } catch (Exception e) {
         log.warn("Error CONTROLJUZGADOS LIMPIAR NUEVO JUZGADO ERROR :" + e.getMessage());
      }
   }

   public void cargarCiudadesNuevoYDuplicar(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
      } else if (tipoNuevo == 1) {
         tipoActualizacion = 2;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
      }
   }

   public void mostrarDialogoNuevoJuzgado() {
      RequestContext context = RequestContext.getCurrentInstance();
      index = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCentroCostos");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCentroCostos').show()");
   }

   public void agregarNuevoJuzgado() {
      log.info("\n ENTRE AGREGARNUEVOJUZGADO \n");
      // try {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      banderaModificacionEmpresa = 1;
      if (nuevoJuzgado.getCodigo() == null) {
         mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoJuzgado.getCodigo().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoJuzgado.getCodigo().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   * Un codigo \n";

      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoJuzgado.getCodigo());

         for (int x = 0; x < listJuzgadosPorCiudad.size(); x++) {
            if (listJuzgadosPorCiudad.get(x).getCodigo().equals(nuevoJuzgado.getCodigo())) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO hayan codigos repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
         }
      }
      if (nuevoJuzgado.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   * Un nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoJuzgado.getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   * Un nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoJuzgado.getNombre().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   * Un nombre \n";

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (nuevoJuzgado.getCiudad().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + "   *Una ciudad\n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (nuevoJuzgado.getOficina() == null) {
         contador++;

      } else if (administrarJuzgados.isNumeric(nuevoJuzgado.getOficina()) == true) {
         contador++;
      } else {
         mensajeValidacion = "EL CAMPO 'Oficina' SOLO ACEPTA NUMEROS";
      }

      if (contador == 4) {
         if (crearJuzgados.contains(nuevoJuzgado)) {
            log.info("Ya lo contengo.");
         } else {
            crearJuzgados.add(nuevoJuzgado);

         }
         listJuzgadosPorCiudad.add(nuevoJuzgado);
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
         nuevoJuzgado = new Juzgados();
         // index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            ciudad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            oficina = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:oficina");
            oficina.setFilterStyle("display: none; visibility: hidden;");
            observaciones = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:observaciones");
            observaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosJuzgados");

            bandera = 0;
            filtrarJuzgados = null;
            tipoLista = 0;
         }
         mensajeValidacion = " ";
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCentroCostos').hide()");
         nuevoJuzgado = new Juzgados();
         nuevoJuzgado.setCiudad(new Ciudades());
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }

      //  } catch (Exception e) {
      //     log.error("CONTROLJUZGADOS AGREGARNUEVOJUZGADO ERROR :" + e);
      //  }
   }

   public void duplicandoJuzgados() {
      try {
         banderaModificacionEmpresa = 1;
         log.info("\n ENTRE A CONTROLJUZGADOS DUPLICANDO INDEX  = " + index);

         if (index >= 0) {
            log.info("\n ENTRE A CONTROLJUZGADOS DUPLICANDO TIPOLISTA  = " + tipoLista);

            duplicarJuzgado = new Juzgados();
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
               duplicarJuzgado.setSecuencia(l);
               duplicarJuzgado.setCodigo(listJuzgadosPorCiudad.get(index).getCodigo());
               duplicarJuzgado.setNombre(listJuzgadosPorCiudad.get(index).getNombre());
               duplicarJuzgado.setOficina(listJuzgadosPorCiudad.get(index).getOficina());
               duplicarJuzgado.setObservaciones(listJuzgadosPorCiudad.get(index).getObservaciones());
               duplicarJuzgado.setCiudad(listJuzgadosPorCiudad.get(index).getCiudad());

            }
            if (tipoLista == 1) {

               duplicarJuzgado.setSecuencia(l);
               duplicarJuzgado.setSecuencia(l);
               duplicarJuzgado.setCodigo(filtrarJuzgados.get(index).getCodigo());
               duplicarJuzgado.setNombre(filtrarJuzgados.get(index).getNombre());
               duplicarJuzgado.setOficina(filtrarJuzgados.get(index).getOficina());
               duplicarJuzgado.setObservaciones(filtrarJuzgados.get(index).getObservaciones());
               duplicarJuzgado.setCiudad(filtrarJuzgados.get(index).getCiudad());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCentroCostos");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').show()");
            index = -1;
         }
      } catch (Exception e) {
         log.warn("Error CONTROLJUZGADOS DUPLICANDOJUZGADOS ERROR :" + e.getMessage());
      }
   }

   public void limpiarDuplicarJuzgados() {
      try {
         log.info("\n ENTRE A CONTROLJUZGADOS LIMPIARJUZGADOS \n");
         duplicarJuzgado = new Juzgados();
      } catch (Exception e) {
         log.warn("Error ControlCentroCosto.limpiarDuplicarCentroCostos ERROR========" + e.getMessage());
      }

   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (duplicarJuzgado.getCodigo().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarJuzgado.getCodigo().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   * Un codigo \n";

      } else {
         log.info("codigo en Motivo Cambio Cargo: " + duplicarJuzgado.getCodigo());

         for (int x = 0; x < listJuzgadosPorCiudad.size(); x++) {
            if (listJuzgadosPorCiudad.get(x).getCodigo().equals(duplicarJuzgado.getCodigo())) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO hayan codigos repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
         }

      }
      if (duplicarJuzgado.getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   * Un nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarJuzgado.getNombre().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   * Un nombre \n";

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarJuzgado.getCiudad().getSecuencia() == null || duplicarJuzgado.getCiudad().getNombre().isEmpty() || duplicarJuzgado.getCiudad().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   *Una Ciudad \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarJuzgado.getOficina().equals("") || duplicarJuzgado.getOficina().isEmpty() || duplicarJuzgado.getOficina().equals(" ") || duplicarJuzgado.getOficina() == null) {
         contador++;

      } else if (administrarJuzgados.isNumeric(duplicarJuzgado.getOficina()) == true) {
         contador++;
      } else {
         mensajeValidacion = "EL CAMPO 'Oficina' SOLO ACEPTA NUMEROS";
      }

      if (contador == 4) {

         if (crearJuzgados.contains(duplicarJuzgado)) {
            log.info("Ya lo contengo.");
         } else {
            listJuzgadosPorCiudad.add(duplicarJuzgado);
         }
         crearJuzgados.add(duplicarJuzgado);
         RequestContext.getCurrentInstance().update("form:datosJuzgados");

         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            //1
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            //2
            ciudad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:ciudad");
            ciudad.setFilterStyle("display: none; visibility: hidden;");
            //3 COMBO BOX
            //4
            oficina = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:oficina");
            oficina.setFilterStyle("display: none; visibility: hidden;");
            //5 COMBO BOX
            //6
            observaciones = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosJuzgados:observaciones");
            observaciones.setFilterStyle("display: none; visibility: hidden;");
            //7 COMBO BOX

            RequestContext.getCurrentInstance().update("form:datosJuzgados");
            bandera = 0;
            filtrarJuzgados = null;
            tipoLista = 0;
         }
         duplicarJuzgado = new Juzgados();
         duplicarJuzgado.setCiudad(new Ciudades());
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').hide()");
         mensajeValidacion = " ";
         banderaModificacionEmpresa = 1;

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
         log.info("valorConfirmar : " + valorConfirmar);
         log.info("CIUDAD bkp : " + nuevoYduplicarCiudadCompletar);

         if (!duplicarJuzgado.getCiudad().getNombre().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoTipoCCAutoCompletar: " + nuevoYduplicarCiudadCompletar);
            duplicarJuzgado.getCiudad().setNombre(nuevoYduplicarCiudadCompletar);
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarJuzgado.setCiudad(lovCiudades.get(indiceUnicoElemento));
               lovCiudades = null;
               getLovCiudades();
            } else {
               RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            duplicarJuzgado.getCiudad().setNombre(nuevoYduplicarCiudadCompletar);

            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            duplicarJuzgado.setCiudad(new Ciudades());
            duplicarJuzgado.getCiudad().setNombre(" ");
            log.info("Valor CIUDAD : " + duplicarJuzgado.getCiudad().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
      }
   }

   /*-----------------------------------------------------------------------------
     * GETTER AND SETTER
     *------------------------------------------------------------------------------*/
   public List<Juzgados> getListJuzgadosPorCiudad() {
      if (listJuzgadosPorCiudad == null) {
         listJuzgadosPorCiudad = administrarJuzgados.LOVJuzgadosPorCiudadGeneral();
      }
      return listJuzgadosPorCiudad;
   }

   public void setListJuzgadosPorCiudad(List<Juzgados> listJuzgadosPorCiudad) {
      this.listJuzgadosPorCiudad = listJuzgadosPorCiudad;
   }

   public List<Juzgados> getFiltrarJuzgados() {
      return filtrarJuzgados;
   }

   public void setFiltrarJuzgados(List<Juzgados> filtrarJuzgados) {
      this.filtrarJuzgados = filtrarJuzgados;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
      this.ciudadSeleccionada = ciudadSeleccionada;
   }

   public List<Ciudades> getLovCiudadesFiltrar() {
      return lovCiudadesFiltrar;
   }

   public void setLovCiudadesFiltrar(List<Ciudades> lovCiudadesFiltrar) {
      this.lovCiudadesFiltrar = lovCiudadesFiltrar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarJuzgados.consultarLOVCiudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Juzgados> getListaJuzgadosPorCiudadBoton() {
      if (listaJuzgadosPorCiudadBoton == null) {
         listaJuzgadosPorCiudadBoton = administrarJuzgados.consultarJuzgadosPorCiudad(ciudadSeleccionada.getSecuencia());
         listJuzgadosPorCiudad = listaJuzgadosPorCiudadBoton;
         RequestContext.getCurrentInstance().update("form:datosJuzgados");
      }
      return listaJuzgadosPorCiudadBoton;
   }

   public void setListaJuzgadosPorCiudadBoton(List<Juzgados> listaJuzgadosPorCiudadBoton) {
      this.listaJuzgadosPorCiudadBoton = listaJuzgadosPorCiudadBoton;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public Juzgados getEditarJuzgado() {
      return editarJuzgado;
   }

   public void setEditarJuzgado(Juzgados editarJuzgado) {
      this.editarJuzgado = editarJuzgado;
   }

   public Juzgados getNuevoJuzgado() {
      return nuevoJuzgado;
   }

   public void setNuevoJuzgado(Juzgados nuevoJuzgado) {
      this.nuevoJuzgado = nuevoJuzgado;
   }

   public Juzgados getDuplicarJuzgado() {
      return duplicarJuzgado;
   }

   public void setDuplicarJuzgado(Juzgados duplicarJuzgado) {
      this.duplicarJuzgado = duplicarJuzgado;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public void setMostrarTodos(boolean mostrarTodos) {
      this.mostrarTodos = mostrarTodos;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
   }

}
