/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Paises;
import Entidades.Festivos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarFestivosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlFestivos implements Serializable {

   private static Logger log = Logger.getLogger(ControlFestivos.class);

   @EJB
   AdministrarFestivosInterface administrarFestivos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Festivos> listFestivosPorPais;
   private List<Festivos> filtrarFestivosPorPais;
   private List<Festivos> crearFestivosPorPais;
   private List<Festivos> modificarFestivosPorPais;
   private List<Festivos> borrarFestivosPorPais;
   private Festivos nuevoFestivos;
   private Festivos duplicarFestivos;
   private Festivos editarFestivos;
   private Festivos festivoSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column fecha;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
//eado
   private List<Paises> listPaises;
   private Paises paisSeleccionado;
   private List<Paises> listPaisesFiltrar;
   private int tamano;
   private String infoRegistro;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFestivos() {
      listFestivosPorPais = null;
      crearFestivosPorPais = new ArrayList<Festivos>();
      modificarFestivosPorPais = new ArrayList<Festivos>();
      borrarFestivosPorPais = new ArrayList<Festivos>();
      permitirIndex = true;
      editarFestivos = new Festivos();
      nuevoFestivos = new Festivos();
      nuevoFestivos.setPais(new Paises());
      duplicarFestivos = new Festivos();
      guardado = true;
      tamano = 180;
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
      String pagActual = "festivo";

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
         administrarFestivos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPais() {
      log.info("PAIS ESCOGIDO : " + paisSeleccionado.getNombre());
      log.info("PAIS ESCOGIDO : " + crearFestivosPorPais.size());
      log.info("PAIS ESCOGIDO : " + modificarFestivosPorPais.size());
      log.info("PAIS ESCOGIDO : " + borrarFestivosPorPais.size());

      if (crearFestivosPorPais.size() == 0 && modificarFestivosPorPais.size() == 0 && borrarFestivosPorPais.size() == 0) {
         listFestivosPorPais = null;
         getListFestivosPorPais();
      } else {
         RequestContext.getCurrentInstance().update("form:confirmarPais");
         RequestContext.getCurrentInstance().execute("PF('confirmarPais').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void anularCambios() {
      modificarFestivosPorPais = null;
      crearFestivosPorPais = null;
      borrarFestivosPorPais = null;
      listFestivosPorPais = null;
      recibirPais();
   }

   public void mostrarNuevo() {
      log.error("NUEVA FECHA : " + nuevoFestivos.getDia());
   }

   public void mostrarInfo(int indice, int celda) {
      int contador = 0;
      int fechas = 0;
      mensajeValidacion = " ";
      index = indice;
      cualCelda = celda;
      RequestContext context = RequestContext.getCurrentInstance();
      if (permitirIndex == true) {
         if (tipoLista == 0) {
            secRegistro = listFestivosPorPais.get(index).getSecuencia();
            log.error("MODIFICAR FECHA \n Indice" + indice + "Fecha " + listFestivosPorPais.get(indice).getDia());
            if (listFestivosPorPais.get(indice).getDia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               contador++;
            } else {
               for (int j = 0; j < listFestivosPorPais.size(); j++) {
                  if (j != indice) {
                     if (listFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                        fechas++;
                     }
                  }
               }
            }
            if (fechas > 0) {
               mensajeValidacion = "FECHAS REPETIDAS";
               contador++;
            }
            if (contador == 0) {
               if (!crearFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
                  if (modificarFestivosPorPais.isEmpty()) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
                  } else if (!modificarFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosHvEntrevista");

               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();
            }
         } else {

            secRegistro = filtrarFestivosPorPais.get(index).getSecuencia();
            log.error("MODIFICAR FECHA \n Indice" + indice + "Fecha " + filtrarFestivosPorPais.get(indice).getDia());
            if (filtrarFestivosPorPais.get(indice).getDia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               contador++;
            } else {
               for (int j = 0; j < filtrarFestivosPorPais.size(); j++) {
                  if (j != indice) {
                     if (filtrarFestivosPorPais.get(indice).getDia().equals(filtrarFestivosPorPais.get(j).getDia())) {
                        fechas++;
                     }
                  }
               }
               for (int j = 0; j < listFestivosPorPais.size(); j++) {
                  if (j != indice) {
                     if (filtrarFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                        fechas++;
                     }
                  }
               }
            }
            if (fechas > 0) {
               mensajeValidacion = "FECHAS REPETIDAS";
               contador++;
            }
            if (contador == 0) {
               if (!crearFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
                  if (modificarFestivosPorPais.isEmpty()) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
                  } else if (!modificarFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();

            }
            index = -1;
            secRegistro = null;
         }
         log.info("Indice: " + index + " Celda: " + cualCelda);

      }

   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlFestivos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de Registros: " + filtrarFestivosPorPais.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlFestivos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   private Date backUpFecha;

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listFestivosPorPais.get(index).getSecuencia();
         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpFecha = listFestivosPorPais.get(index).getDia();
            } else {
               backUpFecha = filtrarFestivosPorPais.get(index).getDia();
            }
         }
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlFestivos.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 1) {
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error ControlFestivos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {
         if (cualCelda == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         bandera = 0;
         filtrarFestivosPorPais = null;
         tipoLista = 0;
      }

      borrarFestivosPorPais.clear();
      crearFestivosPorPais.clear();
      modificarFestivosPorPais.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listFestivosPorPais = null;
      guardado = true;
      permitirIndex = true;
      getListFestivosPorPais();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listFestivosPorPais != null && !listFestivosPorPais.isEmpty()) {
         festivoSeleccionado = listFestivosPorPais.get(0);
         infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
      } else {
         infoRegistro = "Cantidad de registros: 0";
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      cancelarModificacion();
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 160;
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 180;
         log.info("Desactivar");
         fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         bandera = 0;
         filtrarFestivosPorPais = null;
         tipoLista = 0;
      }
   }

   public void modificandoFestivos(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR EMPL VIGENCIA NORMA LABORAL");
      index = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      int contador = 0;
      boolean banderita = false;
      Short a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPLVIGENCIANORMALABORAL, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
               if (listFestivosPorPais.get(indice).getDia() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listFestivosPorPais.get(indice).setDia(backUpFecha);
               } else {
                  for (int j = 0; j < listFestivosPorPais.size(); j++) {
                     if (j != indice) {
                        if (listFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "FECHAS REPETIDAS";
                     listFestivosPorPais.get(indice).setDia(backUpFecha);
                     banderita = false;
                  } else {
                     banderita = true;
                  }
               }

               if (banderita == true) {
                  if (modificarFestivosPorPais.isEmpty()) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
                  } else if (!modificarFestivosPorPais.contains(listFestivosPorPais.get(indice))) {
                     modificarFestivosPorPais.add(listFestivosPorPais.get(indice));
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
               if (listFestivosPorPais.get(indice).getDia() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listFestivosPorPais.get(indice).setDia(backUpFecha);
               } else {
                  for (int j = 0; j < listFestivosPorPais.size(); j++) {
                     if (j != indice) {
                        if (listFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "FECHAS REPETIDAS";
                     listFestivosPorPais.get(indice).setDia(backUpFecha);
                     banderita = false;
                  } else {
                     banderita = true;
                  }
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
         } else if (!crearFestivosPorPais.contains(filtrarFestivosPorPais.get(indice))) {
            if (filtrarFestivosPorPais.get(indice).getDia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarFestivosPorPais.get(indice).setDia(backUpFecha);
            } else {
               for (int j = 0; j < filtrarFestivosPorPais.size(); j++) {
                  if (j != indice) {
                     if (filtrarFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "FECHAS REPETIDAS";
                  filtrarFestivosPorPais.get(indice).setDia(backUpFecha);
                  banderita = false;
               } else {
                  banderita = true;
               }
            }

            if (banderita == true) {
               if (modificarFestivosPorPais.isEmpty()) {
                  modificarFestivosPorPais.add(filtrarFestivosPorPais.get(indice));
               } else if (!modificarFestivosPorPais.contains(filtrarFestivosPorPais.get(indice))) {
                  modificarFestivosPorPais.add(filtrarFestivosPorPais.get(indice));
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
            if (filtrarFestivosPorPais.get(indice).getDia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarFestivosPorPais.get(indice).setDia(backUpFecha);
            } else {
               for (int j = 0; j < filtrarFestivosPorPais.size(); j++) {
                  if (j != indice) {
                     if (filtrarFestivosPorPais.get(indice).getDia().equals(listFestivosPorPais.get(j).getDia())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "FECHAS REPETIDAS";
                  filtrarFestivosPorPais.get(indice).setDia(backUpFecha);
                  banderita = false;
               } else {
                  banderita = true;
               }
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
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void limpiarNuevaNormaLaboral() {
      try {
         log.info("\n ENTRE A LIMPIAR NUEVO NORMA LABORAL \n");
         nuevoFestivos = new Festivos();
         nuevoFestivos.setPais(new Paises());
         index = -1;
      } catch (Exception e) {
         log.warn("Error ControlFestivos LIMPIAR NUEVO NORMA LABORAL ERROR :" + e.getMessage());
      }
   }

   public void borrandoFestivos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoEvalCompetencias");
            if (!modificarFestivosPorPais.isEmpty() && modificarFestivosPorPais.contains(listFestivosPorPais.get(index))) {
               int modIndex = modificarFestivosPorPais.indexOf(listFestivosPorPais.get(index));
               modificarFestivosPorPais.remove(modIndex);
               borrarFestivosPorPais.add(listFestivosPorPais.get(index));
            } else if (!crearFestivosPorPais.isEmpty() && crearFestivosPorPais.contains(listFestivosPorPais.get(index))) {
               int crearIndex = crearFestivosPorPais.indexOf(listFestivosPorPais.get(index));
               crearFestivosPorPais.remove(crearIndex);
            } else {
               borrarFestivosPorPais.add(listFestivosPorPais.get(index));
            }
            listFestivosPorPais.remove(index);
            infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();

         }
         if (tipoLista == 1) {
            log.info("borrandoEvalCompetencias ");
            if (!modificarFestivosPorPais.isEmpty() && modificarFestivosPorPais.contains(filtrarFestivosPorPais.get(index))) {
               int modIndex = modificarFestivosPorPais.indexOf(filtrarFestivosPorPais.get(index));
               modificarFestivosPorPais.remove(modIndex);
               borrarFestivosPorPais.add(filtrarFestivosPorPais.get(index));
            } else if (!crearFestivosPorPais.isEmpty() && crearFestivosPorPais.contains(filtrarFestivosPorPais.get(index))) {
               int crearIndex = crearFestivosPorPais.indexOf(filtrarFestivosPorPais.get(index));
               crearFestivosPorPais.remove(crearIndex);
            } else {
               borrarFestivosPorPais.add(filtrarFestivosPorPais.get(index));
            }
            int VCIndex = listFestivosPorPais.indexOf(filtrarFestivosPorPais.get(index));
            listFestivosPorPais.remove(VCIndex);
            filtrarFestivosPorPais.remove(index);
            infoRegistro = "Cantidad de registros: " + filtrarFestivosPorPais.size();

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         index = -1;
         secRegistro = null;

      }

   }

   /* public void verificarBorrado() {
     log.info("Estoy en verificarBorrado");
     try {
     log.error("Control Secuencia de ControlHvEntrevistas ");
     competenciasCargos = administrarHvEntrevistas.verificarBorradoCompetenciasCargos(listHvEntrevistas.get(index).getSecuencia());

     if (competenciasCargos.intValueExact() == 0) {
     log.info("Borrado==0");
     borrandoHvEntrevistas();
     } else {
     log.info("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
     index = -1;

     competenciasCargos = new BigDecimal(-1);

     }
     } catch (Exception e) {
     log.error("ERROR ControlHvEntrevistas verificarBorrado ERROR " + e);
     }
     }*/
   public void revisarDialogoGuardar() {

      if (!borrarFestivosPorPais.isEmpty() || !crearFestivosPorPais.isEmpty() || !modificarFestivosPorPais.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarFestivos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarEvalCompetencias");
         if (!borrarFestivosPorPais.isEmpty()) {
            for (int i = 0; i < borrarFestivosPorPais.size(); i++) {
               log.info("Borrando...");
               administrarFestivos.borrarFestivos(borrarFestivosPorPais);
            }
            //mostrarBorrados
            registrosBorrados = borrarFestivosPorPais.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarFestivosPorPais.clear();
         }
         if (!crearFestivosPorPais.isEmpty()) {
            for (int i = 0; i < crearFestivosPorPais.size(); i++) {
               log.info("Creando...");
               log.info("-----------------------------------------------");
               log.info("Fecha :" + crearFestivosPorPais.get(i).getDia());
               log.info("Pais : " + crearFestivosPorPais.get(i).getPais().getNombre());
               log.info("-----------------------------------------------");
               administrarFestivos.crearFestivos(crearFestivosPorPais);

            }
            crearFestivosPorPais.clear();
         }
         if (!modificarFestivosPorPais.isEmpty()) {
            log.info("Modificando...");
            administrarFestivos.modificarFestivos(modificarFestivosPorPais);
            modificarFestivosPorPais.clear();
         }
         log.info("Se guardaron los datos con exito");
         listFestivosPorPais = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         k = 0;
         guardado = true;
         getListFestivosPorPais();
         if (listFestivosPorPais != null && !listFestivosPorPais.isEmpty()) {
            festivoSeleccionado = listFestivosPorPais.get(0);
            infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
         } else {
            infoRegistro = "Cantidad de registros: 0";
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarFestivos = listFestivosPorPais.get(index);
         }
         if (tipoLista == 1) {
            editarFestivos = filtrarFestivosPorPais.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPuntaje");
            RequestContext.getCurrentInstance().execute("PF('editPuntaje').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoFestivos() {
      log.info("agregarNuevoFestivos");
      int contador = 0;
      //nuevoFestivos.setPais(new Paises());
      Short a = 0;
      a = null;
      int fechas = 0;
      mensajeValidacion = " ";
      nuevoFestivos.setPais(paisSeleccionado);
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("Nueva Fecha : " + nuevoFestivos.getDia());
      if (nuevoFestivos.getDia() == null || nuevoFestivos.getDia().equals("")) {
         mensajeValidacion = " *Debe tener una fecha \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int i = 0; i < listFestivosPorPais.size(); i++) {
            if (nuevoFestivos.getDia().equals(listFestivosPorPais.get(i).getDia())) {
               fechas++;
            }
         }
         if (fechas > 0) {
            mensajeValidacion = "Fechas repetidas ";
         } else {
            contador++;
         }
      }

      /*if (nuevoHvEntrevista.getTipo() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Debe tener un tipo entrevista \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
         log.info("bandera");
         contador++;
         }*/
      log.info("contador " + contador);

      if (contador == 1) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
            bandera = 0;
            filtrarFestivosPorPais = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoFestivos.setSecuencia(l);
         log.error("---------------AGREGAR REGISTRO----------------");
         log.error("fecha " + nuevoFestivos.getDia());
         log.error("Pais " + nuevoFestivos.getPais().getNombre());
         log.error("-----------------------------------------------");

         crearFestivosPorPais.add(nuevoFestivos);
         listFestivosPorPais.add(nuevoFestivos);
         nuevoFestivos = new Festivos();
         nuevoFestivos.setPais(new Paises());
         infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEmpresas').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoFestivos() {
      log.info("limpiarNuevoFestivos");
      nuevoFestivos = new Festivos();
      nuevoFestivos.setPais(new Paises());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoFestivos() {
      if (index >= 0) {
         log.info("duplicandoFestivos");
         duplicarFestivos = new Festivos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarFestivos.setSecuencia(l);
            duplicarFestivos.setPais(listFestivosPorPais.get(index).getPais());
            duplicarFestivos.setDia(listFestivosPorPais.get(index).getDia());
         }
         if (tipoLista == 1) {
            duplicarFestivos.setSecuencia(l);
            duplicarFestivos.setPais(filtrarFestivosPorPais.get(index).getPais());
            duplicarFestivos.setDia(filtrarFestivosPorPais.get(index).getDia());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').show()");
         //index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR HVENTREVISTAS");
      int contador = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      Short a = 0;
      int fechas = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarFestivos.getDia());

      if (duplicarFestivos.getDia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Fecha \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {

         for (int j = 0; j < listFestivosPorPais.size(); j++) {
            if (duplicarFestivos.getDia().equals(listFestivosPorPais.get(j).getDia())) {
               log.info("Se repiten");
               fechas++;
            }
         }
         if (fechas > 0) {
            mensajeValidacion = "FECHAS REPETIDAS";
         } else {
            log.info("bandera");
            contador++;
         }

      }

      if (contador == 1) {

         log.info("Datos Duplicando: " + duplicarFestivos.getSecuencia() + "  " + duplicarFestivos.getDia());
         if (crearFestivosPorPais.contains(duplicarFestivos)) {
            log.info("Ya lo contengo.");
         }
         listFestivosPorPais.add(duplicarFestivos);
         crearFestivosPorPais.add(duplicarFestivos);
         RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
         index = -1;
         secRegistro = null;

         log.error("---------------DUPLICAR REGISTRO----------------");
         log.error("fecha " + duplicarFestivos.getDia());
         log.error("Pais " + duplicarFestivos.getPais().getNombre());
         log.error("-----------------------------------------------");
         infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosHvEntrevista:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
            bandera = 0;
            filtrarFestivosPorPais = null;
            tipoLista = 0;
         }
         duplicarFestivos = new Festivos();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').hide()");

      } else {
         contador = 0;
         fechas = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void limpiarDuplicarFestivos() {
      duplicarFestivos = new Festivos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvEntrevistaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FESTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvEntrevistaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FESTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listFestivosPorPais.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "FESTIVOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("FESTIVOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Festivos> getListFestivosPorPais() {
      if (listFestivosPorPais == null) {
         listFestivosPorPais = administrarFestivos.consultarFestivosPais(paisSeleccionado.getSecuencia());
         RequestContext context = RequestContext.getCurrentInstance();
         if (listFestivosPorPais == null || listFestivosPorPais.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      }
      return listFestivosPorPais;
   }

   public void setListFestivosPorPais(List<Festivos> listFestivosPorPais) {
      this.listFestivosPorPais = listFestivosPorPais;
   }

   public List<Festivos> getFiltrarFestivosPorPais() {
      return filtrarFestivosPorPais;
   }

   public void setFiltrarFestivosPorPais(List<Festivos> filtrarFestivosPorPais) {
      this.filtrarFestivosPorPais = filtrarFestivosPorPais;
   }

   public Festivos getNuevoFestivos() {
      return nuevoFestivos;
   }

   public void setNuevoFestivos(Festivos nuevoFestivos) {
      this.nuevoFestivos = nuevoFestivos;
   }

   public Festivos getDuplicarFestivos() {
      return duplicarFestivos;
   }

   public void setDuplicarFestivos(Festivos duplicarFestivos) {
      this.duplicarFestivos = duplicarFestivos;
   }

   public Festivos getEditarFestivos() {
      return editarFestivos;
   }

   public void setEditarFestivos(Festivos editarFestivos) {
      this.editarFestivos = editarFestivos;
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

   public List<Paises> getListPaises() {
      if (listPaises == null) {
         listPaises = administrarFestivos.consultarLOVPaises();
         paisSeleccionado = listPaises.get(0);
         RequestContext context = RequestContext.getCurrentInstance();
         if (listFestivosPorPais == null || listFestivosPorPais.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listFestivosPorPais.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      }
      return listPaises;
   }

   public void setListPaises(List<Paises> listPaises) {
      this.listPaises = listPaises;
   }

   public Paises getPaisSeleccionado() {
      return paisSeleccionado;
   }

   public void setPaisSeleccionado(Paises paisSeleccionado) {
      this.paisSeleccionado = paisSeleccionado;
   }

   public List<Paises> getListPaisesFiltrar() {
      return listPaisesFiltrar;
   }

   public void setListPaisesFiltrar(List<Paises> listPaisesFiltrar) {
      this.listPaisesFiltrar = listPaisesFiltrar;
   }

   public Festivos getFestivoSeleccionado() {
      return festivoSeleccionado;
   }

   public void setFestivoSeleccionado(Festivos festivoSeleccionado) {
      this.festivoSeleccionado = festivoSeleccionado;
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
