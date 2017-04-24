/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Enfermedades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEnfermedadesInterface;
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
public class ControlEnfermedades implements Serializable {

   @EJB
   AdministrarEnfermedadesInterface administrarEnfermedades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Enfermedades> listEnfermedades;
   private List<Enfermedades> filtrarEnfermedades;
   private List<Enfermedades> crearEnfermedades;
   private List<Enfermedades> modificarEnfermedades;
   private List<Enfermedades> borrarEnfermedades;
   private Enfermedades nuevaEnfermedad;
   private Enfermedades duplicarEnfermedad;
   private Enfermedades editarEnfermedad;
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
   private Integer a;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEnfermedades() {
      listEnfermedades = null;
      crearEnfermedades = new ArrayList<Enfermedades>();
      modificarEnfermedades = new ArrayList<Enfermedades>();
      borrarEnfermedades = new ArrayList<Enfermedades>();
      permitirIndex = true;
      editarEnfermedad = new Enfermedades();
      nuevaEnfermedad = new Enfermedades();
      duplicarEnfermedad = new Enfermedades();
      a = null;
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
         
      } else {
         */
String pagActual = "enfermedad";
         
         
         


         
         
         
         
         
         
         if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEnfermedades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlEnfermedades.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
      } catch (Exception e) {
         System.out.println("ERROR ControlEnfermedades eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listEnfermedades.get(index).getSecuencia();

      }
      System.out.println("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         System.out.println("\n ENTRE A ControlEnfermedades.asignarIndex \n");
         System.out.println("Tipo Actualizacion: " + tipoActualizacion);
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         System.out.println("ERROR ControlEnfermedades.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         bandera = 0;
         filtrarEnfermedades = null;
         tipoLista = 0;
      }

      borrarEnfermedades.clear();
      crearEnfermedades.clear();
      modificarEnfermedades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEnfermedades = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEnfermedades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {

         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         bandera = 0;
         filtrarEnfermedades = null;
         tipoLista = 0;
      }
   }

   public void modificarEnfermedad(int indice, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR Enfermedades");
      index = indice;

      int contador = 0;
      boolean banderita = false;

      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         System.err.println("ENTRE A MODIFICAR Enfermedades, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearEnfermedades.contains(listEnfermedades.get(indice))) {
               if (listEnfermedades.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               } else {
                  for (int j = 0; j < listEnfermedades.size(); j++) {
                     if (j != indice) {
                        if (listEnfermedades.get(indice).getCodigo() == listEnfermedades.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listEnfermedades.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               }
               if (listEnfermedades.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               }

               if (banderita == true) {
                  if (modificarEnfermedades.isEmpty()) {
                     modificarEnfermedades.add(listEnfermedades.get(indice));
                  } else if (!modificarEnfermedades.contains(listEnfermedades.get(indice))) {
                     modificarEnfermedades.add(listEnfermedades.get(indice));
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
         } else if (!crearEnfermedades.contains(filtrarEnfermedades.get(indice))) {
            if (filtrarEnfermedades.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               for (int j = 0; j < listEnfermedades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEnfermedades.get(indice).getCodigo() == listEnfermedades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < filtrarEnfermedades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEnfermedades.get(indice).getCodigo() == filtrarEnfermedades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarEnfermedades.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            }
            if (filtrarEnfermedades.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            }

            if (banderita == true) {
               if (modificarEnfermedades.isEmpty()) {
                  modificarEnfermedades.add(filtrarEnfermedades.get(indice));
               } else if (!modificarEnfermedades.contains(filtrarEnfermedades.get(indice))) {
                  modificarEnfermedades.add(filtrarEnfermedades.get(indice));
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
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrarEnfermedad() {

      if (index >= 0) {

         if (tipoLista == 0) {
            System.out.println("Entro a borrarEnfermedades");
            if (!modificarEnfermedades.isEmpty() && modificarEnfermedades.contains(listEnfermedades.get(index))) {
               int modIndex = modificarEnfermedades.indexOf(listEnfermedades.get(index));
               modificarEnfermedades.remove(modIndex);
               borrarEnfermedades.add(listEnfermedades.get(index));
            } else if (!crearEnfermedades.isEmpty() && crearEnfermedades.contains(listEnfermedades.get(index))) {
               int crearIndex = crearEnfermedades.indexOf(listEnfermedades.get(index));
               crearEnfermedades.remove(crearIndex);
            } else {
               borrarEnfermedades.add(listEnfermedades.get(index));
            }
            listEnfermedades.remove(index);
         }
         if (tipoLista == 1) {
            System.out.println("borrarEnfermedades ");
            if (!modificarEnfermedades.isEmpty() && modificarEnfermedades.contains(filtrarEnfermedades.get(index))) {
               int modIndex = modificarEnfermedades.indexOf(filtrarEnfermedades.get(index));
               modificarEnfermedades.remove(modIndex);
               borrarEnfermedades.add(filtrarEnfermedades.get(index));
            } else if (!crearEnfermedades.isEmpty() && crearEnfermedades.contains(filtrarEnfermedades.get(index))) {
               int crearIndex = crearEnfermedades.indexOf(filtrarEnfermedades.get(index));
               crearEnfermedades.remove(crearIndex);
            } else {
               borrarEnfermedades.add(filtrarEnfermedades.get(index));
            }
            int VCIndex = listEnfermedades.indexOf(filtrarEnfermedades.get(index));
            listEnfermedades.remove(VCIndex);
            filtrarEnfermedades.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }
   private BigInteger contadorAusentimos;
   private BigInteger contadorDetallesLicencias;
   private BigInteger contadorEnfermedadesPadecidas;
   private BigInteger contadorSoausentismos;
   private BigInteger contadorSorevisionessSistemas;

   public void verificarBorrado() {
      try {
         System.out.println("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
         System.out.println("secuencia borrado : " + listEnfermedades.get(index).getSecuencia());
         if (tipoLista == 0) {
            System.out.println("secuencia borrado : " + listEnfermedades.get(index).getSecuencia());
            contadorAusentimos = administrarEnfermedades.verificarAusentimos(listEnfermedades.get(index).getSecuencia());
            contadorDetallesLicencias = administrarEnfermedades.verificarDetallesLicencias(listEnfermedades.get(index).getSecuencia());
            contadorEnfermedadesPadecidas = administrarEnfermedades.verificarEnfermedadesPadecidas(listEnfermedades.get(index).getSecuencia());
            contadorSoausentismos = administrarEnfermedades.verificarSoAusentismos(listEnfermedades.get(index).getSecuencia());
            contadorSorevisionessSistemas = administrarEnfermedades.verificarSoRevisionesSistemas(listEnfermedades.get(index).getSecuencia());
         } else {
            System.out.println("secuencia borrado : " + filtrarEnfermedades.get(index).getSecuencia());
            contadorAusentimos = administrarEnfermedades.verificarAusentimos(filtrarEnfermedades.get(index).getSecuencia());
            contadorDetallesLicencias = administrarEnfermedades.verificarDetallesLicencias(filtrarEnfermedades.get(index).getSecuencia());
            contadorEnfermedadesPadecidas = administrarEnfermedades.verificarEnfermedadesPadecidas(filtrarEnfermedades.get(index).getSecuencia());
            contadorSoausentismos = administrarEnfermedades.verificarSoAusentismos(filtrarEnfermedades.get(index).getSecuencia());
            contadorSorevisionessSistemas = administrarEnfermedades.verificarSoRevisionesSistemas(filtrarEnfermedades.get(index).getSecuencia());
         }
         System.out.println("contadorSoAccidentes " + contadorAusentimos.toString());
         System.out.println("contadorDetallesLicencias " + contadorDetallesLicencias.toString());
         System.out.println("contadorEnfermedadesPadecidas " + contadorEnfermedadesPadecidas.toString());
         if (!contadorAusentimos.equals(new BigInteger("0")) || !contadorDetallesLicencias.equals(new BigInteger("0")) || !contadorEnfermedadesPadecidas.equals(new BigInteger("0")) || !contadorSoausentismos.equals(new BigInteger("0")) || !contadorSorevisionessSistemas.equals(new BigInteger("0"))) {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            contadorAusentimos = new BigInteger("-1");
            contadorDetallesLicencias = new BigInteger("-1");
            contadorEnfermedadesPadecidas = new BigInteger("-1");
            contadorSoausentismos = new BigInteger("-1");
            contadorSorevisionessSistemas = new BigInteger("-1");

         } else {
            System.out.println("Borrado==0");
            borrarEnfermedad();
         }
      } catch (Exception e) {
         System.err.println("ERROR CONTROLENFERMEDADES verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarEnfermedades.isEmpty() || !crearEnfermedades.isEmpty() || !modificarEnfermedades.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarEnfermedad() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando Enfermedades");
         if (!borrarEnfermedades.isEmpty()) {
            administrarEnfermedades.borrarEnfermedades(borrarEnfermedades);

            //mostrarBorrados
            registrosBorrados = borrarEnfermedades.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEnfermedades.clear();
         }
         if (!crearEnfermedades.isEmpty()) {
            administrarEnfermedades.crearEnfermedades(crearEnfermedades);

            crearEnfermedades.clear();
         }
         if (!modificarEnfermedades.isEmpty()) {
            administrarEnfermedades.modificarEnfermedades(modificarEnfermedades);
            modificarEnfermedades.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listEnfermedades = null;
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEnfermedad = listEnfermedades.get(index);
         }
         if (tipoLista == 1) {
            editarEnfermedad = filtrarEnfermedades.get(index);
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

   public void agregarNuevoEnfermedades() {
      System.out.println("Agregar Enfermedades");
      int contador = 0;
      int duplicados = 0;

      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaEnfermedad.getCodigo() == a) {
         mensajeValidacion = " *Debe Tener Un Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevaEnfermedad.getCodigo());

         for (int x = 0; x < listEnfermedades.size(); x++) {
            if (listEnfermedades.get(x).getCodigo() == nuevaEnfermedad.getCodigo()) {
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
      if (nuevaEnfermedad.getDescripcion() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener un Descripcion \n";
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
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 0;
            filtrarEnfermedades = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevaEnfermedad.setSecuencia(l);

         crearEnfermedades.add(nuevaEnfermedad);

         listEnfermedades.add(nuevaEnfermedad);
         nuevaEnfermedad = new Enfermedades();

         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEnfermedades').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEnfermedades() {
      System.out.println("limpiarNuevoEnfermedades");
      nuevaEnfermedad = new Enfermedades();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicarEnfermedades() {
      System.out.println("duplicarEnfermedades");
      if (index >= 0) {
         duplicarEnfermedad = new Enfermedades();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarEnfermedad.setSecuencia(l);
            duplicarEnfermedad.setCodigo(listEnfermedades.get(index).getCodigo());
            duplicarEnfermedad.setDescripcion(listEnfermedades.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarEnfermedad.setSecuencia(l);
            duplicarEnfermedad.setCodigo(filtrarEnfermedades.get(index).getCodigo());
            duplicarEnfermedad.setDescripcion(filtrarEnfermedades.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfer");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfermedad').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR Enfermedades");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      System.err.println("ConfirmarDuplicar codigo " + duplicarEnfermedad.getCodigo());
      System.err.println("ConfirmarDuplicar Descripcion " + duplicarEnfermedad.getDescripcion());

      if (duplicarEnfermedad.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   * Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listEnfermedades.size(); x++) {
            if (listEnfermedades.get(x).getCodigo() == duplicarEnfermedad.getCodigo()) {
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
      if (duplicarEnfermedad.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   * Un Nombre \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         System.out.println("Datos Duplicando: " + duplicarEnfermedad.getSecuencia() + "  " + duplicarEnfermedad.getCodigo());
         if (crearEnfermedades.contains(duplicarEnfermedad)) {
            System.out.println("Ya lo contengo.");
         }
         listEnfermedades.add(duplicarEnfermedad);
         crearEnfermedades.add(duplicarEnfermedad);
         RequestContext.getCurrentInstance().update("form:datosEnfermedades");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 0;
            filtrarEnfermedades = null;
            tipoLista = 0;
         }
         duplicarEnfermedad = new Enfermedades();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfermedad').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarEnfermedades() {
      duplicarEnfermedad = new Enfermedades();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfermedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ENFERMEDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfermedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ENFERMEDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (!listEnfermedades.isEmpty()) {
         if (secRegistro != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "ENFERMEDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("ENFERMEDADES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   public List<Enfermedades> getListEnfermedades() {
      if (listEnfermedades == null) {
         listEnfermedades = administrarEnfermedades.consultarEnfermedades();
      }
      return listEnfermedades;
   }

   public void setListEnfermedades(List<Enfermedades> listEnfermedades) {
      this.listEnfermedades = listEnfermedades;
   }

   public List<Enfermedades> getFiltrarEnfermedades() {
      return filtrarEnfermedades;
   }

   public void setFiltrarEnfermedades(List<Enfermedades> filtrarEnfermedades) {
      this.filtrarEnfermedades = filtrarEnfermedades;
   }

   public Enfermedades getNuevaEnfermedad() {
      return nuevaEnfermedad;
   }

   public void setNuevaEnfermedad(Enfermedades nuevaEnfermedad) {
      this.nuevaEnfermedad = nuevaEnfermedad;
   }

   public Enfermedades getDuplicarEnfermedad() {
      return duplicarEnfermedad;
   }

   public void setDuplicarEnfermedad(Enfermedades duplicarEnfermedad) {
      this.duplicarEnfermedad = duplicarEnfermedad;
   }

   public Enfermedades getEditarEnfermedad() {
      return editarEnfermedad;
   }

   public void setEditarEnfermedad(Enfermedades editarEnfermedad) {
      this.editarEnfermedad = editarEnfermedad;
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
