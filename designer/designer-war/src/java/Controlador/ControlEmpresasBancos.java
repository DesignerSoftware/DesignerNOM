/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.EmpresasBancos;
import Entidades.Empresas;
import Entidades.Bancos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasBancosInterface;
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
public class ControlEmpresasBancos implements Serializable {

   @EJB
   AdministrarEmpresasBancosInterface administrarEmpresasBancos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<EmpresasBancos> listEmpresasBancos;
   private List<EmpresasBancos> filtrarEmpresasBancos;
   private List<EmpresasBancos> crearEmpresasBancos;
   private List<EmpresasBancos> modificarEmpresasBancos;
   private List<EmpresasBancos> borrarEmpresasBancos;
   private EmpresasBancos nuevoEmpresasBancos;
   private EmpresasBancos duplicarEmpresasBancos;
   private EmpresasBancos editarEmpresasBancos;
   private EmpresasBancos empresasBancoSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column obsoleto, pais, subTituloFirma, personafir, cargo;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String backupPais;
   private String backupNumeroCuenta;
   //-------------------------------
   private String backupEmpresas;
   private List<Empresas> listaEmpresas;
   private List<Empresas> filtradoEmpresas;
   private Empresas empresaSeleccionado;
   private String nuevoYduplicarCompletarEmpresa;
   //---------------------------------
   private String backupCiudad;
   private List<Bancos> listaBancos;
   private List<Bancos> filtradoBancos;
   private Bancos bancoSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private String backupBanco;
   private List<Ciudades> listaCiudades;
   private List<Ciudades> filtradoCiudades;
   private Ciudades cargoSeleccionado;
   private String nuevoYduplicarCompletarCargo;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmpresasBancos() {
      listEmpresasBancos = null;
      crearEmpresasBancos = new ArrayList<EmpresasBancos>();
      modificarEmpresasBancos = new ArrayList<EmpresasBancos>();
      borrarEmpresasBancos = new ArrayList<EmpresasBancos>();
      permitirIndex = true;
      editarEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos.setEmpresa(new Empresas());
      nuevoEmpresasBancos.setBanco(new Bancos());
      nuevoEmpresasBancos.setCiudad(new Ciudades());
      duplicarEmpresasBancos = new EmpresasBancos();
      duplicarEmpresasBancos.setEmpresa(new Empresas());
      duplicarEmpresasBancos.setBanco(new Bancos());
      duplicarEmpresasBancos.setCiudad(new Ciudades());
      listaEmpresas = null;
      filtradoEmpresas = null;
      listaBancos = null;
      filtradoBancos = null;
      listaCiudades = null;
      filtradoCiudades = null;
      guardado = true;
      tamano = 270;
      aceptar = true;
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
         String pagActual = "empresabanco";
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
         administrarEmpresasBancos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlEmpresasBancos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarEmpresasBancos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         System.out.println("ERROR ControlEmpresasBancos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listEmpresasBancos.get(index).getSecuencia();
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backupEmpresas = listEmpresasBancos.get(index).getEmpresa().getNombre();
               System.out.println("EMPRESA : " + backupEmpresas);
            }
            if (cualCelda == 1) {
               backupBanco = listEmpresasBancos.get(index).getBanco().getNombre();
               System.out.println("BANCO : " + backupBanco);

            }
            if (cualCelda == 2) {
               backupCiudad = listEmpresasBancos.get(index).getCiudad().getNombre();
               System.out.println("CIUDAD : " + backupCiudad);

            }
            if (cualCelda == 3) {
               backupNumeroCuenta = listEmpresasBancos.get(index).getNumerocuenta();
            }
         } else if (tipoLista == 1) {
            if (cualCelda == 0) {
               backupEmpresas = filtrarEmpresasBancos.get(index).getEmpresa().getNombre();
               System.out.println("EMPRESA : " + backupEmpresas);
            }
            if (cualCelda == 1) {
               backupBanco = filtrarEmpresasBancos.get(index).getBanco().getNombre();
               System.out.println("BANCO : " + backupBanco);

            }
            if (cualCelda == 2) {
               backupCiudad = filtrarEmpresasBancos.get(index).getCiudad().getNombre();
               System.out.println("CIUDAD : " + backupCiudad);

            }
            if (cualCelda == 3) {
               backupNumeroCuenta = filtrarEmpresasBancos.get(index).getNumerocuenta();
            }

         }

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("\n ENTRE A ControlEmpresasBancos.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 0) {
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
            dig = -1;
         }
         if (dig == 1) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }
         if (dig == 2) {
            RequestContext.getCurrentInstance().update("form:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         System.out.println("ERROR ControlEmpresasBancos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {
         if (cualCelda == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
         }
         if (cualCelda == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
         if (cualCelda == 2) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }

      borrarEmpresasBancos.clear();
      crearEmpresasBancos.clear();
      modificarEmpresasBancos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEmpresasBancos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEmpresasBancos == null || listEmpresasBancos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {  limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }

      borrarEmpresasBancos.clear();
      crearEmpresasBancos.clear();
      modificarEmpresasBancos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEmpresasBancos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEmpresasBancos == null || listEmpresasBancos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("width: 85% !important;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("width: 85% !important;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("width: 85% !important;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("width: 85% !important;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }
   }

   public void actualizarPais() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("pais seleccionado : " + empresaSeleccionado.getNombre());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listEmpresasBancos.get(index).setEmpresa(empresaSeleccionado);

            if (!crearEmpresasBancos.contains(listEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               }
            }
         } else {
            filtrarEmpresasBancos.get(index).setEmpresa(empresaSeleccionado);

            if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + empresaSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + empresaSeleccionado.getNombre());
         nuevoEmpresasBancos.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + empresaSeleccionado.getNombre());
         duplicarEmpresasBancos.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
      }
      filtradoEmpresas = null;
      empresaSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovTiposFamiliares");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void actualizarBancos() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("pais seleccionado : " + bancoSeleccionado.getNombre());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listEmpresasBancos.get(index).setBanco(bancoSeleccionado);

            if (!crearEmpresasBancos.contains(listEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               }
            }
         } else {
            filtrarEmpresasBancos.get(index).setBanco(bancoSeleccionado);

            if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + bancoSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + bancoSeleccionado.getNombre());
         nuevoEmpresasBancos.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + bancoSeleccionado.getNombre());
         duplicarEmpresasBancos.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoBancos = null;
      bancoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovBancos");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void actualizarCiudades() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("pais seleccionado : " + cargoSeleccionado.getNombre());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listEmpresasBancos.get(index).setCiudad(cargoSeleccionado);

            if (!crearEmpresasBancos.contains(listEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(listEmpresasBancos.get(index));
               }
            }
         } else {
            filtrarEmpresasBancos.get(index).setCiudad(cargoSeleccionado);

            if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR PAIS CARGO SELECCIONADO : " + cargoSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + cargoSeleccionado.getNombre());
         nuevoEmpresasBancos.setCiudad(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + cargoSeleccionado.getNombre());
         duplicarEmpresasBancos.setCiudad(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
      filtradoBancos = null;
      bancoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovCiudades");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void cancelarCambioPais() {
      filtradoEmpresas = null;
      empresaSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
   }

   public void cancelarCambioBancos() {
      listEmpresasBancos.get(index).getBanco().setNombre(backupBanco);
      filtradoBancos = null;
      bancoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void cancelarCambioCiudades() {
      filtradoCiudades = null;
      cargoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void mostrarInfo(int indice, int celda) {
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         cualCelda = celda;
         secRegistro = listEmpresasBancos.get(index).getSecuencia();
         if (cualCelda == 5) {
            System.out.println("DIMENSIONES  " + listEmpresasBancos.get(indice).getTipocuenta());
         }
         if (!crearEmpresasBancos.contains(listEmpresasBancos.get(indice))) {

            if (modificarEmpresasBancos.isEmpty()) {
               modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
            } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(indice))) {
               modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);

   }

   public void modificarEmpresasBancos(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearEmpresasBancos.contains(listEmpresasBancos.get(indice))) {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);
               System.out.println("backupSubtituloFirma : " + backupNumeroCuenta);
               System.out.println("backupPais : " + backupPais);

               if (listEmpresasBancos.get(indice).getNumerocuenta() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);

               } else {
                  for (int j = 0; j < listEmpresasBancos.size(); j++) {
                     if (j != indice) {
                        if (listEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);
                  } else {
                     banderita = true;
                  }

               }

               if (banderita == true) {
                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               System.out.println("backupCodigo : " + backupCodigo);
               System.out.println("backupDescripcion : " + backupDescripcion);

               if (listEmpresasBancos.get(indice).getNumerocuenta() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);
               } else {
                  for (int j = 0; j < listEmpresasBancos.size(); j++) {
                     if (j != indice) {
                        if (listEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);
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
               RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {
            if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);
            } else {
               for (int j = 0; j < filtrarEmpresasBancos.size(); j++) {
                  if (j != indice) {
                     if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEmpresasBancos.size(); j++) {
                  if (j != indice) {
                     if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);

               } else {
                  banderita = true;
               }

            }

            if (banderita == true) {

               if (modificarEmpresasBancos.isEmpty()) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
               } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {
                  modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
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
            if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);
            } else {
               for (int j = 0; j < filtrarEmpresasBancos.size(); j++) {
                  if (j != indice) {
                     if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEmpresasBancos.size(); j++) {
                  if (j != indice) {
                     if (filtrarEmpresasBancos.get(indice).getNumerocuenta() == listEmpresasBancos.get(j).getNumerocuenta()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEmpresasBancos.get(indice).setNumerocuenta(backupNumeroCuenta);

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
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("PAISES")) {

         System.out.println("MODIFICANDO NORMA LABORAL : " + listEmpresasBancos.get(indice).getEmpresa().getNombre());
         if (!listEmpresasBancos.get(indice).getEmpresa().getNombre().equals("")) {
            if (tipoLista == 0) {
               listEmpresasBancos.get(indice).getEmpresa().setNombre(backupEmpresas);
            } else {
               listEmpresasBancos.get(indice).getEmpresa().setNombre(backupEmpresas);
            }

            for (int i = 0; i < listaEmpresas.size(); i++) {
               if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(indice).setEmpresa(listaEmpresas.get(indiceUnicoElemento));
               } else {
                  filtrarEmpresasBancos.get(indice).setEmpresa(listaEmpresas.get(indiceUnicoElemento));
               }
               listaEmpresas.clear();
               listaEmpresas = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupEmpresas != null) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(index).getEmpresa().setNombre(backupEmpresas);
               } else {
                  filtrarEmpresasBancos.get(index).getEmpresa().setNombre(backupEmpresas);
               }
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO : " + backupEmpresas);
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearEmpresasBancos.contains(listEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + listEmpresasBancos.get(indice).getBanco().getNombre());
         if (!listEmpresasBancos.get(indice).getBanco().getNombre().equals("")) {
            if (tipoLista == 0) {
               listEmpresasBancos.get(indice).getBanco().setNombre(backupBanco);
            } else {
               listEmpresasBancos.get(indice).getBanco().setNombre(backupBanco);
            }

            for (int i = 0; i < listaBancos.size(); i++) {
               if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(indice).setBanco(listaBancos.get(indiceUnicoElemento));
               } else {
                  filtrarEmpresasBancos.get(indice).setBanco(listaBancos.get(indiceUnicoElemento));
               }
               listaEmpresas.clear();
               listaEmpresas = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupBanco != null) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(index).getBanco().setNombre(backupBanco);
               } else {
                  filtrarEmpresasBancos.get(index).getBanco().setNombre(backupBanco);
               }
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupBanco);
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearEmpresasBancos.contains(listEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
         System.out.println("MODIFICANDO CARGO: " + listEmpresasBancos.get(indice).getCiudad().getNombre());
         if (!listEmpresasBancos.get(indice).getBanco().getNombre().equals("")) {
            if (tipoLista == 0) {
               listEmpresasBancos.get(indice).getCiudad().setNombre(backupCiudad);
            } else {
               listEmpresasBancos.get(indice).getCiudad().setNombre(backupCiudad);
            }

            for (int i = 0; i < listaCiudades.size(); i++) {
               if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
               } else {
                  filtrarEmpresasBancos.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
               }
               listaEmpresas.clear();
               listaEmpresas = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:cargosDialogo");
               RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupCiudad != null) {
               if (tipoLista == 0) {
                  listEmpresasBancos.get(index).getCiudad().setNombre(backupCiudad);
               } else {
                  filtrarEmpresasBancos.get(index).getCiudad().setNombre(backupCiudad);
               }
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO CARGOS : " + backupCiudad);
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearEmpresasBancos.contains(listEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(listEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(listEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {

                  if (modificarEmpresasBancos.isEmpty()) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  } else if (!modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(indice))) {
                     modificarEmpresasBancos.add(filtrarEmpresasBancos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void borrandoEmpresasBancos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            System.out.println("Entro a borrandoEmpresasBancos");
            if (!modificarEmpresasBancos.isEmpty() && modificarEmpresasBancos.contains(listEmpresasBancos.get(index))) {
               int modIndex = modificarEmpresasBancos.indexOf(listEmpresasBancos.get(index));
               modificarEmpresasBancos.remove(modIndex);
               borrarEmpresasBancos.add(listEmpresasBancos.get(index));
            } else if (!crearEmpresasBancos.isEmpty() && crearEmpresasBancos.contains(listEmpresasBancos.get(index))) {
               int crearIndex = crearEmpresasBancos.indexOf(listEmpresasBancos.get(index));
               crearEmpresasBancos.remove(crearIndex);
            } else {
               borrarEmpresasBancos.add(listEmpresasBancos.get(index));
            }
            listEmpresasBancos.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrandoEmpresasBancos ");
            if (!modificarEmpresasBancos.isEmpty() && modificarEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
               int modIndex = modificarEmpresasBancos.indexOf(filtrarEmpresasBancos.get(index));
               modificarEmpresasBancos.remove(modIndex);
               borrarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
            } else if (!crearEmpresasBancos.isEmpty() && crearEmpresasBancos.contains(filtrarEmpresasBancos.get(index))) {
               int crearIndex = crearEmpresasBancos.indexOf(filtrarEmpresasBancos.get(index));
               crearEmpresasBancos.remove(crearIndex);
            } else {
               borrarEmpresasBancos.add(filtrarEmpresasBancos.get(index));
            }
            int VCIndex = listEmpresasBancos.indexOf(filtrarEmpresasBancos.get(index));
            listEmpresasBancos.remove(VCIndex);
            filtrarEmpresasBancos.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      System.out.println("1...");
      if (valorCambio.equals("EMPRESAS")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarEmpresa = nuevoEmpresasBancos.getEmpresa().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarEmpresa = duplicarEmpresasBancos.getEmpresa().getNombre();
         }
         System.out.println("EMPRESA : " + nuevoYduplicarCompletarEmpresa);
      } else if (valorCambio.equals("PERSONA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPersona = nuevoEmpresasBancos.getBanco().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPersona = duplicarEmpresasBancos.getBanco().getNombre();
         }

         System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
      } else if (valorCambio.equals("CARGO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarCargo = nuevoEmpresasBancos.getCiudad().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarCargo = duplicarEmpresasBancos.getCiudad().getNombre();
         }
         System.out.println("CARGO : " + nuevoYduplicarCompletarCargo);
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
         System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
         System.out.println("NOMBRE CENTRO COSTO: " + nuevoEmpresasBancos.getEmpresa().getNombre());

         if (!nuevoEmpresasBancos.getEmpresa().getNombre().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCiudadCompletar: " + nuevoYduplicarCompletarEmpresa);
            nuevoEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
            getListaEmpresas();
            for (int i = 0; i < listaEmpresas.size(); i++) {
               if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoEmpresasBancos.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
               listaEmpresas = null;
               getListaEmpresas();
               System.err.println("NORMA LABORAL GUARDADA " + nuevoEmpresasBancos.getEmpresa().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoEmpresasBancos.setEmpresa(new Empresas());
            System.out.println("empresa" + nuevoEmpresasBancos.getEmpresa().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
      } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
         System.out.println("NUEVO PERSONA :-------> " + nuevoEmpresasBancos.getBanco().getNombre());

         if (!nuevoEmpresasBancos.getBanco().getNombre().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
            nuevoEmpresasBancos.getBanco().setNombre(nuevoYduplicarCompletarPersona);
            getListaEmpresas();
            for (int i = 0; i < listaBancos.size(); i++) {
               if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoEmpresasBancos.setBanco(listaBancos.get(indiceUnicoElemento));
               listaEmpresas = null;
               getListaEmpresas();
               System.err.println("PERSONA GUARDADA :-----> " + nuevoEmpresasBancos.getBanco().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoEmpresasBancos.getBanco().setNombre(nuevoYduplicarCompletarPersona);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoEmpresasBancos.setBanco(new Bancos());
            nuevoEmpresasBancos.getBanco().setNombre(" ");
            System.out.println("NUEVA NORMA LABORAL" + nuevoEmpresasBancos.getBanco().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
         System.out.println("NUEVO PERSONA :-------> " + nuevoEmpresasBancos.getCiudad().getNombre());

         if (!nuevoEmpresasBancos.getCiudad().getNombre().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCargo);
            nuevoEmpresasBancos.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
            getListaEmpresas();
            for (int i = 0; i < listaCiudades.size(); i++) {
               if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoEmpresasBancos.setCiudad(listaCiudades.get(indiceUnicoElemento));
               listaEmpresas = null;
               getListaEmpresas();
               System.err.println("CARGO GUARDADA :-----> " + nuevoEmpresasBancos.getCiudad().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:cargosDialogo");
               RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoEmpresasBancos.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoEmpresasBancos.setCiudad(new Ciudades());
            System.out.println("NUEVO CARGO " + nuevoEmpresasBancos.getCiudad().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      }

   }

   public void asignarVariableEmpresas(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
   }

   public void asignarVariableBancos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
   }

   public void asignarVariableCiudades(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:cargosDialogo");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarEmpresa);

         if (!duplicarEmpresasBancos.getEmpresa().getNombre().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarEmpresa);
            duplicarEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
            for (int i = 0; i < listaEmpresas.size(); i++) {
               if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarEmpresasBancos.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
               listaEmpresas = null;
               getListaEmpresas();
            } else {
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            System.out.println("DUPLICAR INDEX : " + index);
            duplicarEmpresasBancos.setEmpresa(new Empresas());
            duplicarEmpresasBancos.getEmpresa().setNombre(" ");

            System.out.println("DUPLICAR Valor NORMA LABORAL : " + duplicarEmpresasBancos.getEmpresa().getNombre());
            System.out.println("nuevoYduplicarCompletarPais : " + nuevoYduplicarCompletarEmpresa);
            if (tipoLista == 0) {
               listEmpresasBancos.get(index).getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
               System.err.println("tipo lista" + tipoLista);
               System.err.println("Secuencia Parentesco " + listEmpresasBancos.get(index).getEmpresa().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarEmpresasBancos.get(index).getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
      } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

         if (!duplicarEmpresasBancos.getBanco().getNombre().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
            duplicarEmpresasBancos.getBanco().setNombre(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < listaBancos.size(); i++) {
               if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarEmpresasBancos.setBanco(listaBancos.get(indiceUnicoElemento));
               listaBancos = null;
               getListaEmpresas();
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            System.out.println("DUPLICAR INDEX : " + index);
            duplicarEmpresasBancos.setBanco(new Bancos());
            duplicarEmpresasBancos.getBanco().setNombre(" ");

            System.out.println("DUPLICAR PERSONA  : " + duplicarEmpresasBancos.getBanco().getNombre());
            System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
            if (tipoLista == 0) {
               listEmpresasBancos.get(index).getBanco().setNombre(nuevoYduplicarCompletarPersona);
               System.err.println("tipo lista" + tipoLista);
               System.err.println("Secuencia Parentesco " + listEmpresasBancos.get(index).getBanco().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarEmpresasBancos.get(index).getBanco().setNombre(nuevoYduplicarCompletarPersona);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

         if (!duplicarEmpresasBancos.getCiudad().getNombre().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
            duplicarEmpresasBancos.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
            for (int i = 0; i < listaCiudades.size(); i++) {
               if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarEmpresasBancos.setCiudad(listaCiudades.get(indiceUnicoElemento));
               listaCiudades = null;
               getListaCiudades();
            } else {
               RequestContext.getCurrentInstance().update("form:cargosDialogo");
               RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarEmpresasBancos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            System.out.println("DUPLICAR INDEX : " + index);
            duplicarEmpresasBancos.setCiudad(new Ciudades());
            duplicarEmpresasBancos.getCiudad().setNombre(" ");

            System.out.println("DUPLICAR CARGO  : " + duplicarEmpresasBancos.getCiudad().getNombre());
            System.out.println("nuevoYduplicarCompletarCARGO : " + nuevoYduplicarCompletarCargo);
            if (tipoLista == 0) {
               listEmpresasBancos.get(index).getCiudad().setNombre(nuevoYduplicarCompletarCargo);
               System.err.println("tipo lista" + tipoLista);
               System.err.println("Secuencia Parentesco " + listEmpresasBancos.get(index).getCiudad().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarEmpresasBancos.get(index).getCiudad().setNombre(nuevoYduplicarCompletarCargo);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
   }

   /*public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     BigInteger contarBienProgramacionesDepartamento;
     BigInteger contarCapModulosDepartamento;
     BigInteger contarCiudadesDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     System.err.println("Control Secuencia de ControlEmpresasBancos ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarEmpresasBancos.contarBienProgramacionesDepartamento(listEmpresasBancos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarEmpresasBancos.contarCapModulosDepartamento(listEmpresasBancos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarEmpresasBancos.contarCiudadesDepartamento(listEmpresasBancos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarEmpresasBancos.contarSoAccidentesMedicosDepartamento(listEmpresasBancos.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarEmpresasBancos.contarBienProgramacionesDepartamento(filtrarEmpresasBancos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarEmpresasBancos.contarCapModulosDepartamento(filtrarEmpresasBancos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarEmpresasBancos.contarCiudadesDepartamento(filtrarEmpresasBancos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarEmpresasBancos.contarSoAccidentesMedicosDepartamento(filtrarEmpresasBancos.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarCiudadesDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoEmpresasBancos();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
     index = -1;
     contarBienProgramacionesDepartamento = new BigInteger("-1");
     contarCapModulosDepartamento = new BigInteger("-1");
     contarCiudadesDepartamento = new BigInteger("-1");
     contarSoAccidentesMedicosDepartamento = new BigInteger("-1");

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlEmpresasBancos verificarBorrado ERROR " + e);
     }
     }
    */
   public void revisarDialogoGuardar() {

      if (!borrarEmpresasBancos.isEmpty() || !crearEmpresasBancos.isEmpty() || !modificarEmpresasBancos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarEmpresasBancos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarEmpresasBancos");
         if (!borrarEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.borrarEmpresasBancos(borrarEmpresasBancos);
            //mostrarBorrados
            registrosBorrados = borrarEmpresasBancos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEmpresasBancos.clear();
         }
         if (!modificarEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.modificarEmpresasBancos(modificarEmpresasBancos);
            modificarEmpresasBancos.clear();
         }
         if (!crearEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.crearEmpresasBancos(crearEmpresasBancos);
            crearEmpresasBancos.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listEmpresasBancos = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEmpresasBancos = listEmpresasBancos.get(index);
         }
         if (tipoLista == 1) {
            editarEmpresasBancos = filtrarEmpresasBancos.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
            RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editBancos");
            RequestContext.getCurrentInstance().execute("PF('editBancos').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
            RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoEmpresasBancos() {
      System.out.println("agregarNuevoEmpresasBancos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEmpresasBancos.getNumerocuenta() == null) {
         mensajeValidacion = " *Numero Cuenta \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoEmpresasBancos.getNumerocuenta());

         for (int x = 0; x < listEmpresasBancos.size(); x++) {
            if (listEmpresasBancos.get(x).getNumerocuenta() == nuevoEmpresasBancos.getNumerocuenta()) {
               duplicados++;
            }
         }
         System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Cuenta Repetidas \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;//1
         }
      }
      if (nuevoEmpresasBancos.getEmpresa().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Empresa \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;//2

      }
      if (nuevoEmpresasBancos.getBanco().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Banco \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;//3

      }

      if (nuevoEmpresasBancos.getCiudad().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Ciudad \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("bandera");
         contador++;//4

      }

      System.out.println("contador " + contador);

      if (contador == 4) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
            obsoleto.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarEmpresasBancos = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoEmpresasBancos.setSecuencia(l);

         crearEmpresasBancos.add(nuevoEmpresasBancos);

         listEmpresasBancos.add(nuevoEmpresasBancos);
         nuevoEmpresasBancos = new EmpresasBancos();
         nuevoEmpresasBancos.setEmpresa(new Empresas());
         nuevoEmpresasBancos.setCiudad(new Ciudades());
         nuevoEmpresasBancos.setBanco(new Bancos());
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEmpresasBancos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEmpresasBancos() {
      System.out.println("limpiarNuevoEmpresasBancos");
      nuevoEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos.setEmpresa(new Empresas());
      nuevoEmpresasBancos.setBanco(new Bancos());
      nuevoEmpresasBancos.setCiudad(new Ciudades());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoEmpresasBancos() {
      System.out.println("cargarNuevoEmpresasBancos");

      duplicarEmpresasBancos = new EmpresasBancos();
      duplicarEmpresasBancos.setEmpresa(new Empresas());
      duplicarEmpresasBancos.setBanco(new Bancos());
      duplicarEmpresasBancos.setCiudad(new Ciudades());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEmpresasBancos').show()");

   }

   public void duplicandoEmpresasBancos() {
      System.out.println("duplicandoEmpresasBancos");
      if (index >= 0) {
         duplicarEmpresasBancos = new EmpresasBancos();
         duplicarEmpresasBancos.setEmpresa(new Empresas());
         duplicarEmpresasBancos.setBanco(new Bancos());
         duplicarEmpresasBancos.setCiudad(new Ciudades());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarEmpresasBancos.setSecuencia(l);
            duplicarEmpresasBancos.setNumerocuenta(listEmpresasBancos.get(index).getNumerocuenta());
            duplicarEmpresasBancos.setTipocuenta(listEmpresasBancos.get(index).getTipocuenta());
            duplicarEmpresasBancos.setEmpresa(listEmpresasBancos.get(index).getEmpresa());
            duplicarEmpresasBancos.setBanco(listEmpresasBancos.get(index).getBanco());
            duplicarEmpresasBancos.setCiudad(listEmpresasBancos.get(index).getCiudad());
         }
         if (tipoLista == 1) {
            duplicarEmpresasBancos.setSecuencia(l);
            duplicarEmpresasBancos.setNumerocuenta(filtrarEmpresasBancos.get(index).getNumerocuenta());
            duplicarEmpresasBancos.setTipocuenta(filtrarEmpresasBancos.get(index).getTipocuenta());
            duplicarEmpresasBancos.setEmpresa(filtrarEmpresasBancos.get(index).getEmpresa());
            duplicarEmpresasBancos.setBanco(filtrarEmpresasBancos.get(index).getBanco());
            duplicarEmpresasBancos.setCiudad(filtrarEmpresasBancos.get(index).getCiudad());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresasBancos').show()");
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
      System.err.println("ConfirmarDuplicar codigo " + duplicarEmpresasBancos.getNumerocuenta());

      if (duplicarEmpresasBancos.getNumerocuenta() == null) {
         mensajeValidacion = mensajeValidacion + "   *Numero Cuenta \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listEmpresasBancos.size(); x++) {
            if (listEmpresasBancos.get(x).getNumerocuenta() == duplicarEmpresasBancos.getNumerocuenta()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Existan Cuentas repetidas \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;
            duplicados = 0;
         }
      }

      if (duplicarEmpresasBancos.getEmpresa().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   *Empresa \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }
      if (duplicarEmpresasBancos.getBanco().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   *Banco \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }
      if (duplicarEmpresasBancos.getCiudad().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   *Ciudad \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 4) {

         System.out.println("Datos Duplicando: " + duplicarEmpresasBancos.getSecuencia() + "  " + duplicarEmpresasBancos.getNumerocuenta());
         if (crearEmpresasBancos.contains(duplicarEmpresasBancos)) {
            System.out.println("Ya lo contengo.");
         }
         listEmpresasBancos.add(duplicarEmpresasBancos);
         crearEmpresasBancos.add(duplicarEmpresasBancos);
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         index = -1;
         System.out.println("--------------DUPLICAR------------------------");
         System.out.println("CODIGO : " + duplicarEmpresasBancos.getNumerocuenta());
         System.out.println("EMPRESA: " + duplicarEmpresasBancos.getEmpresa().getNombre());
         System.out.println("PERSONA : " + duplicarEmpresasBancos.getBanco().getNombre());
         System.out.println("CARGO : " + duplicarEmpresasBancos.getCiudad().getNombre());
         System.out.println("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
            obsoleto.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
            bandera = 0;
            filtrarEmpresasBancos = null;
            tipoLista = 0;
         }
         duplicarEmpresasBancos = new EmpresasBancos();
         duplicarEmpresasBancos.setEmpresa(new Empresas());
         duplicarEmpresasBancos.setCiudad(new Ciudades());
         duplicarEmpresasBancos.setBanco(new Bancos());
         index = -1;

         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresasBancos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarEmpresasBancos() {
      duplicarEmpresasBancos = new EmpresasBancos();
      duplicarEmpresasBancos.setEmpresa(new Empresas());
      duplicarEmpresasBancos.setBanco(new Bancos());
      duplicarEmpresasBancos.setCiudad(new Ciudades());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EMPRESASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EMPRESASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listEmpresasBancos.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "EMPRESASBANCOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EMPRESASBANCOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   private String infoRegistro;

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<EmpresasBancos> getListEmpresasBancos() {
      if (listEmpresasBancos == null) {
         listEmpresasBancos = administrarEmpresasBancos.consultarEmpresasBancos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEmpresasBancos == null || listEmpresasBancos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEmpresasBancos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listEmpresasBancos;
   }

   public void setListEmpresasBancos(List<EmpresasBancos> listEmpresasBancos) {
      this.listEmpresasBancos = listEmpresasBancos;
   }

   public List<EmpresasBancos> getFiltrarEmpresasBancos() {
      return filtrarEmpresasBancos;
   }

   public void setFiltrarEmpresasBancos(List<EmpresasBancos> filtrarEmpresasBancos) {
      this.filtrarEmpresasBancos = filtrarEmpresasBancos;
   }

   public EmpresasBancos getNuevoEmpresasBancos() {
      return nuevoEmpresasBancos;
   }

   public void setNuevoEmpresasBancos(EmpresasBancos nuevoEmpresasBancos) {
      this.nuevoEmpresasBancos = nuevoEmpresasBancos;
   }

   public EmpresasBancos getDuplicarEmpresasBancos() {
      return duplicarEmpresasBancos;
   }

   public void setDuplicarEmpresasBancos(EmpresasBancos duplicarEmpresasBancos) {
      this.duplicarEmpresasBancos = duplicarEmpresasBancos;
   }

   public EmpresasBancos getEditarEmpresasBancos() {
      return editarEmpresasBancos;
   }

   public void setEditarEmpresasBancos(EmpresasBancos editarEmpresasBancos) {
      this.editarEmpresasBancos = editarEmpresasBancos;
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
   private String infoRegistroEmpresas;

   public List<Empresas> getListaEmpresas() {
      if (listaEmpresas == null) {
         listaEmpresas = administrarEmpresasBancos.consultarLOVEmpresas();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaEmpresas == null || listaEmpresas.isEmpty()) {
         infoRegistroEmpresas = "Cantidad de registros: 0 ";
      } else {
         infoRegistroEmpresas = "Cantidad de registros: " + listaEmpresas.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
      return listaEmpresas;
   }

   public void setListaEmpresas(List<Empresas> listaEmpresas) {
      this.listaEmpresas = listaEmpresas;
   }

   public List<Empresas> getFiltradoEmpresas() {
      return filtradoEmpresas;
   }

   public void setFiltradoEmpresas(List<Empresas> filtradoEmpresas) {
      this.filtradoEmpresas = filtradoEmpresas;
   }

   public Empresas getEmpresaSeleccionado() {
      return empresaSeleccionado;
   }

   public void setEmpresaSeleccionado(Empresas empresaSeleccionado) {
      this.empresaSeleccionado = empresaSeleccionado;
   }
   private String infoRegistrosBancos;

   public List<Bancos> getListaBancos() {
      if (listaBancos == null) {
         listaBancos = administrarEmpresasBancos.consultarLOVBancos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaBancos == null || listaBancos.isEmpty()) {
         infoRegistrosBancos = "Cantidad de registros: 0 ";
      } else {
         infoRegistrosBancos = "Cantidad de registros: " + listaBancos.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistrosBancos");
      return listaBancos;
   }

   public void setListaBancos(List<Bancos> listaBancos) {
      this.listaBancos = listaBancos;
   }

   public List<Bancos> getFiltradoBancos() {
      return filtradoBancos;
   }

   public void setFiltradoBancos(List<Bancos> filtradoBancos) {
      this.filtradoBancos = filtradoBancos;
   }

   public Bancos getBancoSeleccionado() {
      return bancoSeleccionado;
   }

   public void setBancoSeleccionado(Bancos bancoSeleccionado) {
      this.bancoSeleccionado = bancoSeleccionado;
   }

   private String infoRegistroCiudades;

   public List<Ciudades> getListaCiudades() {
      if (listaCiudades == null) {
         listaCiudades = administrarEmpresasBancos.consultarLOVCiudades();
      }

      RequestContext context = RequestContext.getCurrentInstance();
      if (listaCiudades == null || listaCiudades.isEmpty()) {
         infoRegistroCiudades = "Cantidad de registros: 0 ";
      } else {
         infoRegistroCiudades = "Cantidad de registros: " + listaCiudades.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");

      return listaCiudades;
   }

   public void setListaCiudades(List<Ciudades> listaCiudades) {
      this.listaCiudades = listaCiudades;
   }

   public List<Ciudades> getFiltradoCiudades() {
      return filtradoCiudades;
   }

   public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
      this.filtradoCiudades = filtradoCiudades;
   }

   public Ciudades getCiudadSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCiudadSeleccionado(Ciudades cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public EmpresasBancos getEmpresasBancoSeleccionado() {
      return empresasBancoSeleccionado;
   }

   public void setEmpresasBancoSeleccionado(EmpresasBancos empresasBancoSeleccionado) {
      this.empresasBancoSeleccionado = empresasBancoSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroEmpresas() {
      return infoRegistroEmpresas;
   }

   public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
      this.infoRegistroEmpresas = infoRegistroEmpresas;
   }

   public String getInfoRegistrosBancos() {
      return infoRegistrosBancos;
   }

   public void setInfoRegistrosBancos(String infoRegistrosBancos) {
      this.infoRegistrosBancos = infoRegistrosBancos;
   }

   public String getInfoRegistroCiudades() {
      return infoRegistroCiudades;
   }

   public void setInfoRegistroCiudades(String infoRegistroCiudades) {
      this.infoRegistroCiudades = infoRegistroCiudades;
   }

}
