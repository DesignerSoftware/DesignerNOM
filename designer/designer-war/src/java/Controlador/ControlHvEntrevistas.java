/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.HvEntrevistas;
import Entidades.HVHojasDeVida;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarHvEntrevistasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlHvEntrevistas implements Serializable {

   @EJB
   AdministrarHvEntrevistasInterface administrarHvEntrevistas;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<HvEntrevistas> listHvEntrevistas;
   private List<HvEntrevistas> filtrarHvEntrevistas;
   private List<HvEntrevistas> crearHvEntrevistas;
   private List<HvEntrevistas> modificarHvEntrevistas;
   private List<HvEntrevistas> borrarHvEntrevistas;
   private HvEntrevistas nuevoHvEntrevista;
   private HvEntrevistas duplicarHvEntrevista;
   private HvEntrevistas editarHvEntrevista;
   private HvEntrevistas hvEntrevistaSeleccionada;
   //otros
   private HVHojasDeVida hojavida;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado, activarLov;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column fecha, nombre, tipoPuntaje, puntaje;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private BigInteger secuenciaEmpleado;
//Empleado
   private Empleados empleadoSeleccionado;
//otros
   private HVHojasDeVida hvHojasDeVida;
   private List<HVHojasDeVida> listHVHojasDeVida;
   private int tamano;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlHvEntrevistas() {
      listHvEntrevistas = null;
      crearHvEntrevistas = new ArrayList<HvEntrevistas>();
      modificarHvEntrevistas = new ArrayList<HvEntrevistas>();
      borrarHvEntrevistas = new ArrayList<HvEntrevistas>();
      permitirIndex = true;
      editarHvEntrevista = new HvEntrevistas();
      nuevoHvEntrevista = new HvEntrevistas();
      nuevoHvEntrevista.setHojadevida(new HVHojasDeVida());
      nuevoHvEntrevista.setFecha(new Date());
      duplicarHvEntrevista = new HvEntrevistas();
      listHVHojasDeVida = new ArrayList<HVHojasDeVida>();
      guardado = true;
      tamano = 270;
      activarLov = true;
      hojavida = new HVHojasDeVida();
      empleadoSeleccionado = new Empleados();
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
         String pagActual = "hventrevista";
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
         administrarHvEntrevistas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      secuenciaEmpleado = sec;
      empleadoSeleccionado = administrarHvEntrevistas.consultarEmpleado(secuenciaEmpleado);

//        if (empleadoSeleccionado.getPersona().getSecuencia() != null) {
//            hojavida = administrarHvEntrevistas.obtenerHojaVidaPersona(empleadoSeleccionado.getPersona().getSecuencia());
//        }
//        if (hojavida != null) {
      getListHvEntrevistas();
//            if (listHvEntrevistas != null) {
      if (!listHvEntrevistas.isEmpty()) {
         hvEntrevistaSeleccionada = listHvEntrevistas.get(0);
      }
//            }
//        }

   }

   public void mostrarNuevo() {
      System.out.println("nuevo Tipo Entrevista " + nuevoHvEntrevista.getTipo());
   }

   public void mostrarInfo(HvEntrevistas entrevista, int celda) {
      int contador = 0;
      int fechas = 0;
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         mensajeValidacion = " ";
         hvEntrevistaSeleccionada = entrevista;
         cualCelda = celda;
         hvEntrevistaSeleccionada.getSecuencia();
         if (hvEntrevistaSeleccionada.getFecha() == null) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
            contador++;
            hvEntrevistaSeleccionada.setFecha(hvEntrevistaSeleccionada.getFecha());
         }
//            else {
//                for (int j = 0; j < listHvEntrevistas.size(); j++) {
//                        if (hvEntrevistaSeleccionada.getFecha().equals(listHvEntrevistas.get(j).getFecha())) {
//                            fechas++;
//                        }
//                }
//            }
//            if (fechas > 0) {
//                mensajeValidacion = "FECHAS REPETIDAS";
//                contador++;
//                hvEntrevistaSeleccionada.setFecha(backUpfecha);
//            }
         if (contador == 0) {
            if (!crearHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
               if (modificarHvEntrevistas.isEmpty()) {
                  modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
               } else if (!modificarHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
                  modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().update("form:datosHvEntrevista");

            } else {
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
            }
         }
      }
   }

   public void cambiarIndice(HvEntrevistas entrevista, int celda) {

      if (permitirIndex == true) {
         hvEntrevistaSeleccionada = entrevista;
         cualCelda = celda;
         hvEntrevistaSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            hvEntrevistaSeleccionada.getFecha();
         }
         if (cualCelda == 1) {
            hvEntrevistaSeleccionada.getNombre();
         }

         if (cualCelda == 3) {
            hvEntrevistaSeleccionada.getPuntaje();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
         tipoPuntaje.setFilterStyle("display: none; visibility: hidden;");
         puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
         puntaje.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         bandera = 0;
         filtrarHvEntrevistas = null;
         tipoLista = 0;
      }

      borrarHvEntrevistas.clear();
      crearHvEntrevistas.clear();
      modificarHvEntrevistas.clear();
      hvEntrevistaSeleccionada = null;
      k = 0;
      listHvEntrevistas = null;
      guardado = true;
      permitirIndex = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
         tipoPuntaje.setFilterStyle("display: none; visibility: hidden;");
         puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
         puntaje.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         bandera = 0;
         filtrarHvEntrevistas = null;
         tipoLista = 0;
      }

      borrarHvEntrevistas.clear();
      crearHvEntrevistas.clear();
      modificarHvEntrevistas.clear();
      hvEntrevistaSeleccionada = null;
      k = 0;
      listHvEntrevistas = null;
      guardado = true;
      permitirIndex = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
         nombre.setFilterStyle("width: 85% !important");
         tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
         tipoPuntaje.setFilterStyle("width: 85% !important");
         puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
         puntaje.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 270;
         System.out.println("Desactivar");
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
         tipoPuntaje.setFilterStyle("display: none; visibility: hidden;");
         puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
         puntaje.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         bandera = 0;
         filtrarHvEntrevistas = null;
         tipoLista = 0;
      }
   }

   public void modificarHvEntrevista(HvEntrevistas entrevista, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR HV ENTREVISTA");
      hvEntrevistaSeleccionada = entrevista;
      int contador = 0, pass = 0;
      Short a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EVALCOMPETENCIAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearHvEntrevistas.contains(hvEntrevistaSeleccionada)) {

               if (hvEntrevistaSeleccionada.getNombre() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
               } else if (hvEntrevistaSeleccionada.getNombre().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
               } else {
                  pass++;
               }

               if (pass == 1) {
                  if (modificarHvEntrevistas.isEmpty()) {
                     modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
                  } else if (!modificarHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
                     modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               }
            } else {
               if (hvEntrevistaSeleccionada.getNombre() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
               } else if (hvEntrevistaSeleccionada.getNombre().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
               } else {
                  pass++;
               }

               if (pass == 1) {
                  if (guardado == true) {
                     guardado = false;
                  }

               }
            }
         } else if (!crearHvEntrevistas.contains(hvEntrevistaSeleccionada)) {

            if (hvEntrevistaSeleccionada.getNombre() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";

               hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
            } else if (hvEntrevistaSeleccionada.getNombre().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
            } else {
               pass++;
            }

            if (pass == 1) {
               if (modificarHvEntrevistas.isEmpty()) {
                  modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
               } else if (!modificarHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
                  modificarHvEntrevistas.add(hvEntrevistaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }

            }
         } else {
            if (hvEntrevistaSeleccionada.getNombre() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";

               hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
            } else if (hvEntrevistaSeleccionada.getNombre().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               hvEntrevistaSeleccionada.setNombre(hvEntrevistaSeleccionada.getNombre());
            } else {
               pass++;
            }

            if (pass == 1) {
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrandoHvEntrevistas() {
      if (hvEntrevistaSeleccionada != null) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoEvalCompetencias");
            if (!modificarHvEntrevistas.isEmpty() && modificarHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
               int modIndex = modificarHvEntrevistas.indexOf(hvEntrevistaSeleccionada);
               modificarHvEntrevistas.remove(modIndex);
               borrarHvEntrevistas.add(hvEntrevistaSeleccionada);
            } else if (!crearHvEntrevistas.isEmpty() && crearHvEntrevistas.contains(hvEntrevistaSeleccionada)) {
               int crearIndex = crearHvEntrevistas.indexOf(hvEntrevistaSeleccionada);
               crearHvEntrevistas.remove(crearIndex);
            } else {
               borrarHvEntrevistas.add(hvEntrevistaSeleccionada);
            }
            listHvEntrevistas.remove(hvEntrevistaSeleccionada);
         }
         if (tipoLista == 1) {
            filtrarHvEntrevistas.remove(hvEntrevistaSeleccionada);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         hvEntrevistaSeleccionada = null;
         hvEntrevistaSeleccionada = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarHvEntrevistas.isEmpty() || !crearHvEntrevistas.isEmpty() || !modificarHvEntrevistas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarSalir() {
      guardarHvEntrevistas();;
      salir();
   }

   public void cancelarSalir() {
      cancelarModificacion();
      salir();
   }

   public void guardarHvEntrevistas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         if (!borrarHvEntrevistas.isEmpty()) {
            administrarHvEntrevistas.borrarHvEntrevistas(borrarHvEntrevistas);
            registrosBorrados = borrarHvEntrevistas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarHvEntrevistas.clear();
         }
         if (!crearHvEntrevistas.isEmpty()) {
            administrarHvEntrevistas.crearHvEntrevistas(crearHvEntrevistas);
            crearHvEntrevistas.clear();
         }
         if (!modificarHvEntrevistas.isEmpty()) {
            administrarHvEntrevistas.modificarHvEntrevistas(modificarHvEntrevistas);
            modificarHvEntrevistas.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listHvEntrevistas = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         k = 0;
      }
      guardado = true;
      hvEntrevistaSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (hvEntrevistaSeleccionada != null) {
         if (tipoLista == 0) {
            editarHvEntrevista = hvEntrevistaSeleccionada;
         }
         if (tipoLista == 1) {
            editarHvEntrevista = hvEntrevistaSeleccionada;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
            RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
            cualCelda = -1;

         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcionCompetencia");
            RequestContext.getCurrentInstance().execute("PF('editDescripcionCompetencia').show()");
            cualCelda = -1;

         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPuntaje");
            RequestContext.getCurrentInstance().execute("PF('editPuntaje').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoHvEntrevistas() {
      int contador = 0;
      nuevoHvEntrevista.setHojadevida(new HVHojasDeVida());
      Short a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoHvEntrevista.getFecha() == null) {
         mensajeValidacion = " *Fecha \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("bandera");
         contador++;
      }
      if (nuevoHvEntrevista.getNombre() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("bandera");
         contador++;
      }

      System.out.println("secuencia persona" + empleadoSeleccionado.getPersona().getSecuencia());
      listHVHojasDeVida = administrarHvEntrevistas.buscarHVHojasDeVida(empleadoSeleccionado.getPersona().getSecuencia());
      if (listHVHojasDeVida == null) {
         System.err.println("ERROR NULO HVHOJASDEVIDA PARA LA SECUENCIA DE PERSONA :" + empleadoSeleccionado.getPersona().getSecuencia());
      } else {
         System.out.println("tamaño de la lista hojas de vida : " + listHVHojasDeVida.size());
         hvHojasDeVida = listHVHojasDeVida.get(0);
         nuevoHvEntrevista.setHojadevida(hvHojasDeVida);
         System.err.println("Agregar nuevo HVHojasDeVida " + hvHojasDeVida.getSecuencia());
         nuevoHvEntrevista.setHojadevida(new HVHojasDeVida());
      }
      if (nuevoHvEntrevista.getTipo() == null) {
         nuevoHvEntrevista.setTipo("INDIVIDUAL");
      } else {
         nuevoHvEntrevista.setTipo("GRUPAL");
      }

      if (contador == 2) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
            tipoPuntaje.setFilterStyle("display: none; visibility: hidden;");
            puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
            puntaje.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
            bandera = 0;
            filtrarHvEntrevistas = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");
         System.out.println("nueva entrevista : " + nuevoHvEntrevista);

         k++;
         l = BigInteger.valueOf(k);
         nuevoHvEntrevista.setSecuencia(l);
         nuevoHvEntrevista.setHojadevida(hojavida);
         crearHvEntrevistas.add(nuevoHvEntrevista);
         if (listHvEntrevistas == null) {
            listHvEntrevistas = new ArrayList<HvEntrevistas>();
         }
         listHvEntrevistas.add(nuevoHvEntrevista);
         hvEntrevistaSeleccionada = nuevoHvEntrevista;
         System.out.println("nueva entrevista fecha : " + nuevoHvEntrevista.getFecha());
         System.out.println("nueva entrevista tipo : " + nuevoHvEntrevista.getTipo());
         System.out.println("nueva entrevista nombre : " + nuevoHvEntrevista.getNombre());
         System.out.println("nueva entrevista puntaje : " + nuevoHvEntrevista.getPuntaje());
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         nuevoHvEntrevista = new HvEntrevistas();
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEmpresas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaEntrevista");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaEntrevista').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoHvEntrevistas() {
      nuevoHvEntrevista = new HvEntrevistas();
      nuevoHvEntrevista.setHojadevida(new HVHojasDeVida());
      nuevoHvEntrevista.setFecha(new Date());
   }

   //------------------------------------------------------------------------------
   public void duplicandoHvEntrevistas() {
      if (hvEntrevistaSeleccionada != null) {
         duplicarHvEntrevista = new HvEntrevistas();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarHvEntrevista.setSecuencia(l);
            duplicarHvEntrevista.setFecha(hvEntrevistaSeleccionada.getFecha());
            duplicarHvEntrevista.setNombre(hvEntrevistaSeleccionada.getNombre());
            duplicarHvEntrevista.setTipo(hvEntrevistaSeleccionada.getTipo());
            duplicarHvEntrevista.setPuntaje(hvEntrevistaSeleccionada.getPuntaje());
            duplicarHvEntrevista.setHojadevida(hvEntrevistaSeleccionada.getHojadevida());
         }
         if (tipoLista == 1) {
            duplicarHvEntrevista.setSecuencia(l);
            duplicarHvEntrevista.setFecha(hvEntrevistaSeleccionada.getFecha());
            duplicarHvEntrevista.setNombre(hvEntrevistaSeleccionada.getNombre());
            duplicarHvEntrevista.setTipo(hvEntrevistaSeleccionada.getTipo());
            duplicarHvEntrevista.setPuntaje(hvEntrevistaSeleccionada.getPuntaje());
            duplicarHvEntrevista.setHojadevida(hvEntrevistaSeleccionada.getHojadevida());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR HVENTREVISTAS");
      int contador = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      Short a = 0;
      a = null;

      if (duplicarHvEntrevista.getFecha() == null) {
         mensajeValidacion = mensajeValidacion + "   *Fecha \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("bandera");
         contador++;

      }
      if (duplicarHvEntrevista.getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   *Nombre \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarHvEntrevista.getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Nombre \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tipoPuntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:tipoPuntaje");
            tipoPuntaje.setFilterStyle("display: none; visibility: hidden;");
            puntaje = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:puntaje");
            puntaje.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
            bandera = 0;
            filtrarHvEntrevistas = null;
            tipoLista = 0;
         }
         System.out.println("Datos Duplicando: " + duplicarHvEntrevista.getSecuencia() + "  " + duplicarHvEntrevista.getFecha());
         if (crearHvEntrevistas.contains(duplicarHvEntrevista)) {
            System.out.println("Ya lo contengo.");
         }
         listHvEntrevistas.add(duplicarHvEntrevista);
         crearHvEntrevistas.add(duplicarHvEntrevista);
         hvEntrevistaSeleccionada = duplicarHvEntrevista;
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         duplicarHvEntrevista = new HvEntrevistas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarHvEntrevistas() {
      duplicarHvEntrevista = new HvEntrevistas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvEntrevistaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "HVENTREVISTAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvEntrevistaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "HVENTREVISTAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (hvEntrevistaSeleccionada != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(hvEntrevistaSeleccionada.getSecuencia(), "HVENTREVISTAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("HVENTREVISTAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      hvEntrevistaSeleccionada = null;
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlHvEntrevistas.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         System.out.println("ERROR ControlHvEntrevistas eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<HvEntrevistas> getListHvEntrevistas() {
      if (listHvEntrevistas == null) {
         listHvEntrevistas = administrarHvEntrevistas.consultarHvEntrevistasPorEmpleado(secuenciaEmpleado);
      }
      return listHvEntrevistas;
   }

   public void setListHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
      this.listHvEntrevistas = listHvEntrevistas;
   }

   public List<HvEntrevistas> getFiltrarHvEntrevistas() {
      return filtrarHvEntrevistas;
   }

   public void setFiltrarHvEntrevistas(List<HvEntrevistas> filtrarHvEntrevistas) {
      this.filtrarHvEntrevistas = filtrarHvEntrevistas;
   }

   public HvEntrevistas getNuevoHvEntrevista() {
      return nuevoHvEntrevista;
   }

   public void setNuevoHvEntrevista(HvEntrevistas nuevoHvEntrevista) {
      this.nuevoHvEntrevista = nuevoHvEntrevista;
   }

   public HvEntrevistas getDuplicarHvEntrevista() {
      return duplicarHvEntrevista;
   }

   public void setDuplicarHvEntrevista(HvEntrevistas duplicarHvEntrevista) {
      this.duplicarHvEntrevista = duplicarHvEntrevista;
   }

   public HvEntrevistas getEditarHvEntrevista() {
      return editarHvEntrevista;
   }

   public void setEditarHvEntrevista(HvEntrevistas editarHvEntrevista) {
      this.editarHvEntrevista = editarHvEntrevista;
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

   public HvEntrevistas getHvEntrevistaSeleccionada() {
      return hvEntrevistaSeleccionada;
   }

   public void setHvEntrevistaSeleccionada(HvEntrevistas hvEntrevistaSeleccionada) {
      this.hvEntrevistaSeleccionada = hvEntrevistaSeleccionada;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosHvEntrevista");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

//    public List<HVHojasDeVida> getListHVHojasDeVida() {
//        return listHVHojasDeVida;
//    }
//
//    public void setListHVHojasDeVida(List<HVHojasDeVida> listHVHojasDeVida) {
//        this.listHVHojasDeVida = listHVHojasDeVida;
//    }
}
