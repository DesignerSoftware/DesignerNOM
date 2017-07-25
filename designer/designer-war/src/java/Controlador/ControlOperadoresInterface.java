/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Operadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarOperadoresInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlOperadoresInterface")
@SessionScoped
public class ControlOperadoresInterface implements Serializable {

   private static Logger log = Logger.getLogger(ControlOperadoresInterface.class);

    @EJB
    AdministrarOperadoresInterface administrarOperadores;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Operadores> listOperadores;
    private List<Operadores> listaOperadoresFiltrar;
//   private List<Operadores> listaOperadoresCrear;
//   private List<Operadores> listaOperadoresModificar;
//   private List<Operadores> listaOperadoresBorrar;
//   private Operadores nuevoOperador;
//   private Operadores duplicarOperador;
    private Operadores editarOperador;
    private Operadores operadorSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
//   private BigInteger l;
    private boolean aceptar, guardado;
    private Column signo, descripcion;
//   private int registrosBorrados;
//    private String mensajeValidacion;
    private int tamano;
    private String infoRegistro;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlOperadoresInterface() {
        listOperadores = null;
//      crearTiposCursos = new ArrayList<TiposCursos>();
//      modificarTiposCursos = new ArrayList<TiposCursos>();
//      borrarTiposCursos = new ArrayList<TiposCursos>();
        editarOperador = new Operadores();
//      nuevoOperador = new Operadores();
//      duplicarOperador = new Operadores();
        guardado = true;
        tamano = 270;
        cualCelda = -1;
        operadorSeleccionado = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOperadores.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listOperadores = null;
        getListOperadores();
        if (listOperadores != null) {
            operadorSeleccionado = listOperadores.get(0);
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listOperadores = null;
        getListOperadores();
        if (listOperadores != null) {
            operadorSeleccionado = listOperadores.get(0);
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "operador";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(Operadores operador, int celda) {
        operadorSeleccionado = operador;
        cualCelda = celda;
        operadorSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            operadorSeleccionado.getSigno();
        } else if (cualCelda == 1) {
            operadorSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            signo = (Column) c.getViewRoot().findComponent("form:datosOperadores:signo");
            signo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaOperadoresFiltrar = null;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosOperadores");
            tipoLista = 0;
        }

//      borrarTiposCursos.clear();
//      crearTiposCursos.clear();
//      modificarTiposCursos.clear();
        operadorSeleccionado = null;
        contarRegistros();
        k = 0;
        listOperadores = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosOperadores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            signo = (Column) c.getViewRoot().findComponent("form:datosOperadores:signo");
            signo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperadores");
            bandera = 0;
            listaOperadoresFiltrar = null;
            tipoLista = 0;
            tamano = 270;
        }
//      borrarTiposCursos.clear();
//      crearTiposCursos.clear();
//      modificarTiposCursos.clear();
        operadorSeleccionado = null;
        k = 0;
        listOperadores = null;
        guardado = true;
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            signo = (Column) c.getViewRoot().findComponent("form:datosOperadores:signo");
            signo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosOperadores");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 270;
            signo = (Column) c.getViewRoot().findComponent("form:datosOperadores:signo");
            signo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperadores");
            bandera = 0;
            listaOperadoresFiltrar = null;
            tipoLista = 0;
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (operadorSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(operadorSeleccionado.getSecuencia(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (operadorSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosOperadores");
            tablaC.setSelection(operadorSeleccionado);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

//    public void revisarDialogoGuardar() {
//        if (!list.isEmpty() || !crearTiposCursos.isEmpty() || !modificarTiposCursos.isEmpty()) {
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
//            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
//        }
//    }

    public void guardarTiposCursos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
//                if (!borrarTiposCursos.isEmpty()) {
//                    administrarTiposCursos.borrarTiposCursos(borrarTiposCursos);
//                    registrosBorrados = borrarTiposCursos.size();
//                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
//                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
//                    borrarTiposCursos.clear();
//                }
//                if (!modificarTiposCursos.isEmpty()) {
//                    administrarTiposCursos.modificarTiposCursos(modificarTiposCursos);
//                    modificarTiposCursos.clear();
//                }
//                if (!crearTiposCursos.isEmpty()) {
//                    administrarTiposCursos.crearTiposCursos(crearTiposCursos);
//                    crearTiposCursos.clear();
//                }
                listOperadores = null;
                getListOperadores();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                operadorSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosOperadores");
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (operadorSeleccionado != null) {
            editarOperador = operadorSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSigno");
                RequestContext.getCurrentInstance().execute("PF('editSigno').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    /*
    public void modificarTiposCursos(TiposCursos tipo) {
      operadorSeleccionado = tipo;
      if (!crearTiposCursos.contains(operadorSeleccionado)) {
         if (modificarTiposCursos.isEmpty()) {
            modificarTiposCursos.add(operadorSeleccionado);
         } else if (!modificarTiposCursos.contains(operadorSeleccionado)) {
            modificarTiposCursos.add(operadorSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosOperadores");
   }

   public void borrandoTiposCursos() {
      if (operadorSeleccionado != null) {
         if (!modificarTiposCursos.isEmpty() && modificarTiposCursos.contains(operadorSeleccionado)) {
            modificarTiposCursos.remove(modificarTiposCursos.indexOf(operadorSeleccionado));
            borrarTiposCursos.add(operadorSeleccionado);
         } else if (!crearTiposCursos.isEmpty() && crearTiposCursos.contains(operadorSeleccionado)) {
            crearTiposCursos.remove(crearTiposCursos.indexOf(operadorSeleccionado));
         } else {
            borrarTiposCursos.add(operadorSeleccionado);
         }
         listTiposCursos.remove(operadorSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposCursos.remove(operadorSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosOperadores");
         contarRegistros();
         operadorSeleccionado = null;
         guardado = true;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

  
   public void agregarNuevoTiposCursos() {
      log.info("agregarNuevoTiposCursos");
      int contador = 0;
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";

      if (nuevoTiposCursos.getDescripcion().equals(" ") || nuevoTiposCursos.getDescripcion().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (nuevoTiposCursos.getCodigo() == 0) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }

      for (int i = 0; i < listTiposCursos.size(); i++) {
         if (listTiposCursos.get(i).getCodigo() == nuevoTiposCursos.getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            duplicados++;
         }
      }

      if (contador != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoSector");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");
      }

      if (contador == 0 && duplicados == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosOperadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarTiposCursos = null;
            tipoLista = 0;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosOperadores");
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposCursos.setSecuencia(l);
         crearTiposCursos.add(nuevoTiposCursos);
         listTiposCursos.add(nuevoTiposCursos);
         contarRegistros();
         operadorSeleccionado = nuevoTiposCursos;
         nuevoTiposCursos = new TiposCursos();
         RequestContext.getCurrentInstance().update("form:datosOperadores");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposCursos').hide()");
      }
   }

   public void limpiarNuevoTiposCursos() {
      nuevoTiposCursos = new TiposCursos();
   }

   public void duplicandoTiposCursos() {
      if (operadorSeleccionado != null) {
         duplicarTiposCursos = new TiposCursos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTiposCursos.setSecuencia(l);
            duplicarTiposCursos.setCodigo(operadorSeleccionado.getCodigo());
            duplicarTiposCursos.setDescripcion(operadorSeleccionado.getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarTiposCursos.setSecuencia(l);
            duplicarTiposCursos.setCodigo(operadorSeleccionado.getCodigo());
            duplicarTiposCursos.setDescripcion(operadorSeleccionado.getDescripcion());
            tamano = 270;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int contador = 0;

      for (int i = 0; i < listTiposCursos.size(); i++) {
         if (duplicarTiposCursos.getCodigo() == listTiposCursos.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            contador++;
         }
      }

      if (contador == 0) {
         listTiposCursos.add(duplicarTiposCursos);
         crearTiposCursos.add(duplicarTiposCursos);
         operadorSeleccionado = duplicarTiposCursos;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosOperadores");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosOperadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOperadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarTiposCursos = null;
            RequestContext.getCurrentInstance().update("form:datosOperadores");
            tipoLista = 0;
         }
         duplicarTiposCursos = new TiposCursos();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTiposCursos");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').hide()");
   }

   public void limpiarDuplicarTiposCursos() {
      duplicarTiposCursos = new TiposCursos();
   }

     */
    //////////set y get/////////////
    public List<Operadores> getListOperadores() {
        return listOperadores;
    }

    public void setListOperadores(List<Operadores> listOperadores) {
        this.listOperadores = listOperadores;
    }

    public Operadores getEditarOperador() {
        return editarOperador;
    }

    public void setEditarOperador(Operadores editarOperador) {
        this.editarOperador = editarOperador;
    }

    public Operadores getOperadorSeleccionado() {
        return operadorSeleccionado;
    }

    public void setOperadorSeleccionado(Operadores operadorSeleccionado) {
        this.operadorSeleccionado = operadorSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
