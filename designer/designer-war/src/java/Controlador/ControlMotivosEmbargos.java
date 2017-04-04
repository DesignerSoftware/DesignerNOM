/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosEmbargos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosEmbargosInterface;
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
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlMotivosEmbargos implements Serializable {

   @EJB
   AdministrarMotivosEmbargosInterface administrarMotivosEmbargos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<MotivosEmbargos> listMotivosEmbargos;
   private List<MotivosEmbargos> filtrarMotivosEmbargos;
   private List<MotivosEmbargos> crearMotivosEmbargos;
   private List<MotivosEmbargos> modificarMotivosEmbargos;
   private List<MotivosEmbargos> borrarMotivosEmbargos;
   private MotivosEmbargos nuevoMotivoEmbargo;
   private MotivosEmbargos duplicarMotivoEmbargo;
   private MotivosEmbargos editarMotivoEmbargo;
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
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlMotivosEmbargos() {
      listMotivosEmbargos = null;
      crearMotivosEmbargos = new ArrayList<MotivosEmbargos>();
      modificarMotivosEmbargos = new ArrayList<MotivosEmbargos>();
      borrarMotivosEmbargos = new ArrayList<MotivosEmbargos>();
      permitirIndex = true;
      editarMotivoEmbargo = new MotivosEmbargos();
      nuevoMotivoEmbargo = new MotivosEmbargos();
      duplicarMotivoEmbargo = new MotivosEmbargos();
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
         String pagActual = "motivoembargo";
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
         administrarMotivosEmbargos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A CONTROLMOTIVOSEMBARGOS EVENTOFILTRAR \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
      } catch (Exception e) {
         System.err.println("ERROR CONTROLMOTIVOSEMBARGOS EVENTOFILTRAR  ERROR =" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listMotivosEmbargos.get(index).getSecuencia();

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A CONTROLMOTIVOSEMBARGOS ASIGNAR INDEX \n");
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
         System.out.println("ERROR CONTROLMOTIVOSEMBARGOS ASIGNAR INDEX ERROR = " + e);
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
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         bandera = 0;
         filtrarMotivosEmbargos = null;
         tipoLista = 0;
      }

      borrarMotivosEmbargos.clear();
      crearMotivosEmbargos.clear();
      modificarMotivosEmbargos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listMotivosEmbargos = null;
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
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
         bandera = 0;
         filtrarMotivosEmbargos = null;
         tipoLista = 0;
      }
   }

   public void modificandoMotivoEmbargo(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR MOTIVOSEMBARGOS");
      index = indice;

      int contador = 0;
      int contadorGuardar = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR MOTIVOEMBARGOS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearMotivosEmbargos.contains(listMotivosEmbargos.get(indice))) {
               if (listMotivosEmbargos.get(indice).getCodigo() == a || listMotivosEmbargos.get(indice).getCodigo().equals(null)) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  for (int j = 0; j < listMotivosEmbargos.size(); j++) {
                     if (j != indice) {
                        if (listMotivosEmbargos.get(indice).getCodigo().equals(listMotivosEmbargos.get(j).getCodigo())) {
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
               if (listMotivosEmbargos.get(indice).getNombre().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else if (listMotivosEmbargos.get(indice).getNombre().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (listMotivosEmbargos.get(indice).getNombre() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else if (listMotivosEmbargos.get(indice).getNombre().equals("")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (contadorGuardar == 3) {
                  if (modificarMotivosEmbargos.isEmpty()) {
                     modificarMotivosEmbargos.add(listMotivosEmbargos.get(indice));
                  } else if (!modificarMotivosEmbargos.contains(listMotivosEmbargos.get(indice))) {
                     modificarMotivosEmbargos.add(listMotivosEmbargos.get(indice));
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
         } else if (!crearMotivosEmbargos.contains(filtrarMotivosEmbargos.get(indice))) {
            if (filtrarMotivosEmbargos.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               for (int j = 0; j < listMotivosEmbargos.size(); j++) {
                  if (j == indice) {
                     if (filtrarMotivosEmbargos.get(indice).getCodigo().equals(listMotivosEmbargos.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }

               for (int j = 0; j < filtrarMotivosEmbargos.size(); j++) {
                  if (j == indice) {
                     if (filtrarMotivosEmbargos.get(indice).getCodigo().equals(filtrarMotivosEmbargos.get(j).getCodigo())) {
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

            if (filtrarMotivosEmbargos.get(indice).getNombre().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (filtrarMotivosEmbargos.get(indice).getNombre().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               contadorGuardar++;
            }
            if (filtrarMotivosEmbargos.get(indice).getNombre() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (filtrarMotivosEmbargos.get(indice).getNombre().equals("")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               contadorGuardar++;
            }
            if (contadorGuardar == 3) {
               if (modificarMotivosEmbargos.isEmpty()) {
                  modificarMotivosEmbargos.add(filtrarMotivosEmbargos.get(indice));
               } else if (!modificarMotivosEmbargos.contains(filtrarMotivosEmbargos.get(indice))) {
                  modificarMotivosEmbargos.add(filtrarMotivosEmbargos.get(indice));
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
   private BigInteger verificarEerPrestamos;
   private BigInteger verificarEmbargos;

   public void verificarBorrado() {
      try {
         System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
         System.out.println("secuencia borrado : " + listMotivosEmbargos.get(index).getSecuencia());
         if (tipoLista == 0) {
            System.out.println("secuencia borrado : " + listMotivosEmbargos.get(index).getSecuencia());
            verificarEerPrestamos = administrarMotivosEmbargos.contarEersPrestamosMotivoEmbargo(listMotivosEmbargos.get(index).getSecuencia());
            verificarEmbargos = administrarMotivosEmbargos.contarEmbargosMotivoEmbargo(listMotivosEmbargos.get(index).getSecuencia());
         } else {
            System.out.println("secuencia borrado : " + filtrarMotivosEmbargos.get(index).getSecuencia());
            verificarEerPrestamos = administrarMotivosEmbargos.contarEersPrestamosMotivoEmbargo(filtrarMotivosEmbargos.get(index).getSecuencia());
            verificarEmbargos = administrarMotivosEmbargos.contarEmbargosMotivoEmbargo(filtrarMotivosEmbargos.get(index).getSecuencia());
         }
         if (!verificarEerPrestamos.equals(new BigInteger("0")) || !verificarEmbargos.equals(new BigInteger("0"))) {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            verificarEerPrestamos = new BigInteger("-1");
            verificarEmbargos = new BigInteger("-1");

         } else {
            System.out.println("Borrado==0");
            borrandoMotivosEmbargos();
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
      }
   }

   public void borrandoMotivosEmbargos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoMotivosEmbargos");
            if (!modificarMotivosEmbargos.isEmpty() && modificarMotivosEmbargos.contains(listMotivosEmbargos.get(index))) {
               int modIndex = modificarMotivosEmbargos.indexOf(listMotivosEmbargos.get(index));
               modificarMotivosEmbargos.remove(modIndex);
               borrarMotivosEmbargos.add(listMotivosEmbargos.get(index));
            } else if (!crearMotivosEmbargos.isEmpty() && crearMotivosEmbargos.contains(listMotivosEmbargos.get(index))) {
               int crearIndex = crearMotivosEmbargos.indexOf(listMotivosEmbargos.get(index));
               crearMotivosEmbargos.remove(crearIndex);
            } else {
               borrarMotivosEmbargos.add(listMotivosEmbargos.get(index));
            }
            listMotivosEmbargos.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoMotivosEmbargos");
            if (!modificarMotivosEmbargos.isEmpty() && modificarMotivosEmbargos.contains(filtrarMotivosEmbargos.get(index))) {
               int modIndex = modificarMotivosEmbargos.indexOf(filtrarMotivosEmbargos.get(index));
               modificarMotivosEmbargos.remove(modIndex);
               borrarMotivosEmbargos.add(filtrarMotivosEmbargos.get(index));
            } else if (!crearMotivosEmbargos.isEmpty() && crearMotivosEmbargos.contains(filtrarMotivosEmbargos.get(index))) {
               int crearIndex = crearMotivosEmbargos.indexOf(filtrarMotivosEmbargos.get(index));
               crearMotivosEmbargos.remove(crearIndex);
            } else {
               borrarMotivosEmbargos.add(filtrarMotivosEmbargos.get(index));
            }
            int VCIndex = listMotivosEmbargos.indexOf(filtrarMotivosEmbargos.get(index));
            listMotivosEmbargos.remove(VCIndex);
            filtrarMotivosEmbargos.remove(index);

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

      if (!borrarMotivosEmbargos.isEmpty() || !crearMotivosEmbargos.isEmpty() || !modificarMotivosEmbargos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarMotivoEmbargo() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("REALIZANDO MOTIVOEMBARGO");
         if (!borrarMotivosEmbargos.isEmpty()) {

            administrarMotivosEmbargos.borrarMotivosEmbargos(borrarMotivosEmbargos);

            //mostrarBorrados
            registrosBorrados = borrarMotivosEmbargos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarMotivosEmbargos.clear();
         }
         if (!crearMotivosEmbargos.isEmpty()) {
            administrarMotivosEmbargos.crearMotivosEmbargos(crearMotivosEmbargos);

            crearMotivosEmbargos.clear();
         }
         if (!modificarMotivosEmbargos.isEmpty()) {
            administrarMotivosEmbargos.modificarMotivosEmbargos(modificarMotivosEmbargos);
            modificarMotivosEmbargos.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listMotivosEmbargos = null;
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
            editarMotivoEmbargo = listMotivosEmbargos.get(index);
         }
         if (tipoLista == 1) {
            editarMotivoEmbargo = filtrarMotivosEmbargos.get(index);
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

         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorRiesgo");
            RequestContext.getCurrentInstance().execute("PF('editarFactorRiesgo').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoMotivosEmbargos() {
      System.out.println("agregarNuevoMotivosEmbargos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoMotivoEmbargo.getCodigo() == a) {
         mensajeValidacion = " *Debe Tener Un Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoEmbargo.getCodigo());

         for (int x = 0; x < listMotivosEmbargos.size(); x++) {
            if (listMotivosEmbargos.get(x).getCodigo() == nuevoMotivoEmbargo.getCodigo()) {
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
      if (nuevoMotivoEmbargo.getNombre() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Debe tener una descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;

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
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosEmbargos = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoMotivoEmbargo.setSecuencia(l);

         crearMotivosEmbargos.add(nuevoMotivoEmbargo);

         listMotivosEmbargos.add(nuevoMotivoEmbargo);
         nuevoMotivoEmbargo = new MotivosEmbargos();
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

   public void limpiarNuevoMotivosEmbargos() {
      System.out.println("limpiarNuevoMotivosEmbargos");
      nuevoMotivoEmbargo = new MotivosEmbargos();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoMotivosEmbargos() {
      System.out.println("duplicandoMotivosEmbargos");
      if (index >= 0) {
         duplicarMotivoEmbargo = new MotivosEmbargos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarMotivoEmbargo.setSecuencia(l);
            duplicarMotivoEmbargo.setCodigo(listMotivosEmbargos.get(index).getCodigo());
            duplicarMotivoEmbargo.setNombre(listMotivosEmbargos.get(index).getNombre());
         }
         if (tipoLista == 1) {
            duplicarMotivoEmbargo.setSecuencia(l);
            duplicarMotivoEmbargo.setCodigo(filtrarMotivosEmbargos.get(index).getCodigo());
            duplicarMotivoEmbargo.setNombre(filtrarMotivosEmbargos.get(index).getNombre());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR MOTIVOSEMBARGOS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarMotivoEmbargo.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   * Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listMotivosEmbargos.size(); x++) {
            if (listMotivosEmbargos.get(x).getCodigo() == duplicarMotivoEmbargo.getCodigo()) {
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
      if (duplicarMotivoEmbargo.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   * Una descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarMotivoEmbargo.getSecuencia() + "  " + duplicarMotivoEmbargo.getCodigo());
         if (crearMotivosEmbargos.contains(duplicarMotivoEmbargo)) {
            System.out.println("Ya lo contengo.");
         }
         listMotivosEmbargos.add(duplicarMotivoEmbargo);
         crearMotivosEmbargos.add(duplicarMotivoEmbargo);
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
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosEmbargos = null;
            tipoLista = 0;
         }
         duplicarMotivoEmbargo = new MotivosEmbargos();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarMotivosEmbargos() {
      duplicarMotivoEmbargo = new MotivosEmbargos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "MOTIVOSEMBARGOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "MOTIVOSEMBARGOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listMotivosEmbargos.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSEMBARGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSEMBARGOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
   public List<MotivosEmbargos> getListMotivosEmbargos() {
      if (listMotivosEmbargos == null) {
         listMotivosEmbargos = administrarMotivosEmbargos.mostrarMotivosEmbargos();
      }
      return listMotivosEmbargos;
   }

   public void setListMotivosEmbargos(List<MotivosEmbargos> listMotivosEmbargos) {
      this.listMotivosEmbargos = listMotivosEmbargos;
   }

   public List<MotivosEmbargos> getFiltrarMotivosEmbargos() {
      return filtrarMotivosEmbargos;
   }

   public void setFiltrarMotivosEmbargos(List<MotivosEmbargos> filtrarMotivosEmbargos) {
      this.filtrarMotivosEmbargos = filtrarMotivosEmbargos;
   }

   public MotivosEmbargos getNuevoMotivoEmbargo() {
      return nuevoMotivoEmbargo;
   }

   public void setNuevoMotivoEmbargo(MotivosEmbargos nuevoMotivoEmbargo) {
      this.nuevoMotivoEmbargo = nuevoMotivoEmbargo;
   }

   public MotivosEmbargos getDuplicarMotivoEmbargo() {
      return duplicarMotivoEmbargo;
   }

   public void setDuplicarMotivoEmbargo(MotivosEmbargos duplicarMotivoEmbargo) {
      this.duplicarMotivoEmbargo = duplicarMotivoEmbargo;
   }

   public MotivosEmbargos getEditarMotivoEmbargo() {
      return editarMotivoEmbargo;
   }

   public void setEditarMotivoEmbargo(MotivosEmbargos editarMotivoEmbargo) {
      this.editarMotivoEmbargo = editarMotivoEmbargo;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
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

}
