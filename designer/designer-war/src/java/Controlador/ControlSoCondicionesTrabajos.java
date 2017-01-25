/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.SoCondicionesTrabajos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSoCondicionesTrabajosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
public class ControlSoCondicionesTrabajos implements Serializable {

    @EJB
    AdministrarSoCondicionesTrabajosInterface administrarSoCondicionesTrabajos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SoCondicionesTrabajos> listSoCondicionesTrabajos;
    private List<SoCondicionesTrabajos> filtrarSoCondicionesTrabajos;
    private List<SoCondicionesTrabajos> crearSoCondicionesTrabajos;
    private List<SoCondicionesTrabajos> modificarSoCondicionesTrabajos;
    private List<SoCondicionesTrabajos> borrarSoCondicionesTrabajos;
    private SoCondicionesTrabajos nuevoSoCondicionesTrabajos;
    private SoCondicionesTrabajos duplicarSoCondicionesTrabajos;
    private SoCondicionesTrabajos editarSoCondicionesTrabajos;
    private SoCondicionesTrabajos condicionTrabajoSeleccionada;
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
    private String backUpDescripcion;
    private String infoRegistro;
    private Integer backUpCodigo;

    public ControlSoCondicionesTrabajos() {
        listSoCondicionesTrabajos = null;
        crearSoCondicionesTrabajos = new ArrayList<SoCondicionesTrabajos>();
        modificarSoCondicionesTrabajos = new ArrayList<SoCondicionesTrabajos>();
        borrarSoCondicionesTrabajos = new ArrayList<SoCondicionesTrabajos>();
        permitirIndex = true;
        editarSoCondicionesTrabajos = new SoCondicionesTrabajos();
        nuevoSoCondicionesTrabajos = new SoCondicionesTrabajos();
        duplicarSoCondicionesTrabajos = new SoCondicionesTrabajos();
        guardado = true;
        tamano = 270;
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
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
         String pagActual = "cargo"XXX;
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
            administrarSoCondicionesTrabajos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSoCondicionesTrabajos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSoCondicionesTrabajos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlSoCondicionesTrabajos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listSoCondicionesTrabajos.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listSoCondicionesTrabajos.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarSoCondicionesTrabajos.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listSoCondicionesTrabajos.get(index).getFactorriesgo();
                } else {
                    backUpDescripcion = filtrarSoCondicionesTrabajos.get(index).getFactorriesgo();
                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlSoCondicionesTrabajos.asignarIndex \n");
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
            System.out.println("ERROR ControlSoCondicionesTrabajos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            bandera = 0;
            filtrarSoCondicionesTrabajos = null;
            tipoLista = 0;
        }

        borrarSoCondicionesTrabajos.clear();
        crearSoCondicionesTrabajos.clear();
        modificarSoCondicionesTrabajos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesTrabajos = null;
        guardado = true;
        permitirIndex = true;
        getListSoCondicionesTrabajos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesTrabajos == null || listSoCondicionesTrabajos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            bandera = 0;
            filtrarSoCondicionesTrabajos = null;
            tipoLista = 0;
        }

        borrarSoCondicionesTrabajos.clear();
        crearSoCondicionesTrabajos.clear();
        modificarSoCondicionesTrabajos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesTrabajos = null;
        guardado = true;
        permitirIndex = true;
        getListSoCondicionesTrabajos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesTrabajos == null || listSoCondicionesTrabajos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            bandera = 0;
            filtrarSoCondicionesTrabajos = null;
            tipoLista = 0;
        }
    }

    public void modificarSoCondicionesTrabajos(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearSoCondicionesTrabajos.contains(listSoCondicionesTrabajos.get(indice))) {
                    if (listSoCondicionesTrabajos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesTrabajos.get(indice).getCodigo() == listSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSoCondicionesTrabajos.get(indice).getFactorriesgo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
                    } else if (listSoCondicionesTrabajos.get(indice).getFactorriesgo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarSoCondicionesTrabajos.isEmpty()) {
                            modificarSoCondicionesTrabajos.add(listSoCondicionesTrabajos.get(indice));
                        } else if (!modificarSoCondicionesTrabajos.contains(listSoCondicionesTrabajos.get(indice))) {
                            modificarSoCondicionesTrabajos.add(listSoCondicionesTrabajos.get(indice));
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
                    if (listSoCondicionesTrabajos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesTrabajos.get(indice).getCodigo() == listSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listSoCondicionesTrabajos.get(indice).getFactorriesgo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
                    } else if (listSoCondicionesTrabajos.get(indice).getFactorriesgo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);

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
            } else {

                if (!crearSoCondicionesTrabajos.contains(filtrarSoCondicionesTrabajos.get(indice))) {
                    if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == filtrarSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == listSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSoCondicionesTrabajos.get(indice).getFactorriesgo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
                    } else if (filtrarSoCondicionesTrabajos.get(indice).getFactorriesgo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarSoCondicionesTrabajos.isEmpty()) {
                            modificarSoCondicionesTrabajos.add(filtrarSoCondicionesTrabajos.get(indice));
                        } else if (!modificarSoCondicionesTrabajos.contains(filtrarSoCondicionesTrabajos.get(indice))) {
                            modificarSoCondicionesTrabajos.add(filtrarSoCondicionesTrabajos.get(indice));
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

                    if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == filtrarSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSoCondicionesTrabajos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoCondicionesTrabajos.get(indice).getCodigo() == listSoCondicionesTrabajos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarSoCondicionesTrabajos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarSoCondicionesTrabajos.get(indice).getFactorriesgo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
                    } else if (filtrarSoCondicionesTrabajos.get(indice).getFactorriesgo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesTrabajos.get(indice).setFactorriesgo(backUpDescripcion);
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

            }
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSoCondicionesTrabajos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoSoCondicionesTrabajos");
                if (!modificarSoCondicionesTrabajos.isEmpty() && modificarSoCondicionesTrabajos.contains(listSoCondicionesTrabajos.get(index))) {
                    int modIndex = modificarSoCondicionesTrabajos.indexOf(listSoCondicionesTrabajos.get(index));
                    modificarSoCondicionesTrabajos.remove(modIndex);
                    borrarSoCondicionesTrabajos.add(listSoCondicionesTrabajos.get(index));
                } else if (!crearSoCondicionesTrabajos.isEmpty() && crearSoCondicionesTrabajos.contains(listSoCondicionesTrabajos.get(index))) {
                    int crearIndex = crearSoCondicionesTrabajos.indexOf(listSoCondicionesTrabajos.get(index));
                    crearSoCondicionesTrabajos.remove(crearIndex);
                } else {
                    borrarSoCondicionesTrabajos.add(listSoCondicionesTrabajos.get(index));
                }
                listSoCondicionesTrabajos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSoCondicionesTrabajos ");
                if (!modificarSoCondicionesTrabajos.isEmpty() && modificarSoCondicionesTrabajos.contains(filtrarSoCondicionesTrabajos.get(index))) {
                    int modIndex = modificarSoCondicionesTrabajos.indexOf(filtrarSoCondicionesTrabajos.get(index));
                    modificarSoCondicionesTrabajos.remove(modIndex);
                    borrarSoCondicionesTrabajos.add(filtrarSoCondicionesTrabajos.get(index));
                } else if (!crearSoCondicionesTrabajos.isEmpty() && crearSoCondicionesTrabajos.contains(filtrarSoCondicionesTrabajos.get(index))) {
                    int crearIndex = crearSoCondicionesTrabajos.indexOf(filtrarSoCondicionesTrabajos.get(index));
                    crearSoCondicionesTrabajos.remove(crearIndex);
                } else {
                    borrarSoCondicionesTrabajos.add(filtrarSoCondicionesTrabajos.get(index));
                }
                int VCIndex = listSoCondicionesTrabajos.indexOf(filtrarSoCondicionesTrabajos.get(index));
                listSoCondicionesTrabajos.remove(VCIndex);
                filtrarSoCondicionesTrabajos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSoCondicionesTrabajos == null || listSoCondicionesTrabajos.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarSoCondicionesTrabajos.isEmpty() || !crearSoCondicionesTrabajos.isEmpty() || !modificarSoCondicionesTrabajos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("verificarBorrado");
        BigInteger contarInspeccionesSoCondicionTrabajo;
        BigInteger contarSoAccidentesMedicosSoCondicionTrabajo;
        BigInteger contarSoDetallesPanoramasSoCondicionTrabajo;
        BigInteger contarSoExposicionesFrSoCondicionTrabajo;

        try {
            if (tipoLista == 0) {
                contarInspeccionesSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarInspeccionesSoCondicionTrabajo(listSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoAccidentesMedicosSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoAccidentesMedicosSoCondicionTrabajo(listSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoDetallesPanoramasSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoDetallesPanoramasSoCondicionTrabajo(listSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoExposicionesFrSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoExposicionesFrSoCondicionTrabajo(listSoCondicionesTrabajos.get(index).getSecuencia());
            } else {
                contarInspeccionesSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarInspeccionesSoCondicionTrabajo(filtrarSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoAccidentesMedicosSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoAccidentesMedicosSoCondicionTrabajo(filtrarSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoDetallesPanoramasSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoDetallesPanoramasSoCondicionTrabajo(filtrarSoCondicionesTrabajos.get(index).getSecuencia());
                contarSoExposicionesFrSoCondicionTrabajo = administrarSoCondicionesTrabajos.contarSoExposicionesFrSoCondicionTrabajo(filtrarSoCondicionesTrabajos.get(index).getSecuencia());
            }
            if (contarInspeccionesSoCondicionTrabajo.equals(new BigInteger("0"))
                    && contarSoAccidentesMedicosSoCondicionTrabajo.equals(new BigInteger("0"))
                    && contarSoDetallesPanoramasSoCondicionTrabajo.equals(new BigInteger("0"))
                    && contarSoExposicionesFrSoCondicionTrabajo.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoSoCondicionesTrabajos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void guardarSoCondicionesTrabajos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarSoCondicionesTrabajos");
            if (!borrarSoCondicionesTrabajos.isEmpty()) {
                administrarSoCondicionesTrabajos.borrarSoCondicionesTrabajos(borrarSoCondicionesTrabajos);
                //mostrarBorrados
                registrosBorrados = borrarSoCondicionesTrabajos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarSoCondicionesTrabajos.clear();
            }
            if (!modificarSoCondicionesTrabajos.isEmpty()) {
                administrarSoCondicionesTrabajos.modificarSoCondicionesTrabajos(modificarSoCondicionesTrabajos);
                modificarSoCondicionesTrabajos.clear();
            }
            if (!crearSoCondicionesTrabajos.isEmpty()) {
                administrarSoCondicionesTrabajos.crearSoCondicionesTrabajos(crearSoCondicionesTrabajos);
                crearSoCondicionesTrabajos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSoCondicionesTrabajos = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSoCondicionesTrabajos = listSoCondicionesTrabajos.get(index);
            }
            if (tipoLista == 1) {
                editarSoCondicionesTrabajos = filtrarSoCondicionesTrabajos.get(index);
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

    public void agregarNuevoSoCondicionesTrabajos() {
        System.out.println("agregarNuevoSoCondicionesTrabajos");
        int contador = 0;
        int duplicados = 0;
        Integer a;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSoCondicionesTrabajos.getCodigo() == a) {
            mensajeValidacion = " *Código \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoSoCondicionesTrabajos.getCodigo());

            for (int x = 0; x < listSoCondicionesTrabajos.size(); x++) {
                if (listSoCondicionesTrabajos.get(x).getCodigo() == nuevoSoCondicionesTrabajos.getCodigo()) {
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
        if (nuevoSoCondicionesTrabajos.getFactorriesgo() == null) {
            mensajeValidacion = mensajeValidacion + " *Factor Riesgo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            nuevoSoCondicionesTrabajos.setFuente(" ");
            nuevoSoCondicionesTrabajos.setEfectoagudo(" ");
            nuevoSoCondicionesTrabajos.setEfectocronico(" ");
            nuevoSoCondicionesTrabajos.setObservacion(" ");
            nuevoSoCondicionesTrabajos.setRecomendacion(" ");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
                bandera = 0;
                filtrarSoCondicionesTrabajos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoSoCondicionesTrabajos.setSecuencia(l);

            crearSoCondicionesTrabajos.add(nuevoSoCondicionesTrabajos);

            listSoCondicionesTrabajos.add(nuevoSoCondicionesTrabajos);
            nuevoSoCondicionesTrabajos = new SoCondicionesTrabajos();
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");

            infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSoCondicionesTrabajos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
        }
    }

    public void limpiarNuevoSoCondicionesTrabajos() {
        System.out.println("limpiarNuevoSoCondicionesTrabajos");
        nuevoSoCondicionesTrabajos = new SoCondicionesTrabajos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSoCondicionesTrabajos() {
        System.out.println("duplicandoSoCondicionesTrabajos");
        if (index >= 0) {
            duplicarSoCondicionesTrabajos = new SoCondicionesTrabajos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSoCondicionesTrabajos.setSecuencia(l);
                duplicarSoCondicionesTrabajos.setCodigo(listSoCondicionesTrabajos.get(index).getCodigo());
                duplicarSoCondicionesTrabajos.setFactorriesgo(listSoCondicionesTrabajos.get(index).getFactorriesgo());
            }
            if (tipoLista == 1) {
                duplicarSoCondicionesTrabajos.setSecuencia(l);
                duplicarSoCondicionesTrabajos.setCodigo(filtrarSoCondicionesTrabajos.get(index).getCodigo());
                duplicarSoCondicionesTrabajos.setFactorriesgo(filtrarSoCondicionesTrabajos.get(index).getFactorriesgo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesTrabajos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSoCondicionesTrabajos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarSoCondicionesTrabajos.getFactorriesgo());

        if (duplicarSoCondicionesTrabajos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listSoCondicionesTrabajos.size(); x++) {
                if (listSoCondicionesTrabajos.get(x).getCodigo() == duplicarSoCondicionesTrabajos.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (duplicarSoCondicionesTrabajos.getFactorriesgo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Factor Riesgo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            duplicarSoCondicionesTrabajos.setFuente(" ");
            duplicarSoCondicionesTrabajos.setEfectoagudo(" ");
            duplicarSoCondicionesTrabajos.setEfectocronico(" ");
            duplicarSoCondicionesTrabajos.setObservacion(" ");
            duplicarSoCondicionesTrabajos.setRecomendacion(" ");
            System.out.println("Datos Duplicando: " + duplicarSoCondicionesTrabajos.getSecuencia() + "  " + duplicarSoCondicionesTrabajos.getCodigo());
            if (crearSoCondicionesTrabajos.contains(duplicarSoCondicionesTrabajos)) {
                System.out.println("Ya lo contengo.");
            }
            listSoCondicionesTrabajos.add(duplicarSoCondicionesTrabajos);
            crearSoCondicionesTrabajos.add(duplicarSoCondicionesTrabajos);
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesTrabajos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesTrabajos");
                bandera = 0;
                filtrarSoCondicionesTrabajos = null;
                tipoLista = 0;
            }
            duplicarSoCondicionesTrabajos = new SoCondicionesTrabajos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesTrabajos').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSoCondicionesTrabajos() {
        duplicarSoCondicionesTrabajos = new SoCondicionesTrabajos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesTrabajosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SOCONDICIONESTRABAJOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesTrabajosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SOCONDICIONESTRABAJOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSoCondicionesTrabajos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOCONDICIONESTRABAJOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("SOCONDICIONESTRABAJOS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<SoCondicionesTrabajos> getListSoCondicionesTrabajos() {
        if (listSoCondicionesTrabajos == null) {
            listSoCondicionesTrabajos = administrarSoCondicionesTrabajos.consultarSoCondicionesTrabajos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesTrabajos == null || listSoCondicionesTrabajos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesTrabajos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSoCondicionesTrabajos;
    }

    public void setListSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
        this.listSoCondicionesTrabajos = listSoCondicionesTrabajos;
    }

    public List<SoCondicionesTrabajos> getFiltrarSoCondicionesTrabajos() {
        return filtrarSoCondicionesTrabajos;
    }

    public void setFiltrarSoCondicionesTrabajos(List<SoCondicionesTrabajos> filtrarSoCondicionesTrabajos) {
        this.filtrarSoCondicionesTrabajos = filtrarSoCondicionesTrabajos;
    }

    public SoCondicionesTrabajos getNuevoSoCondicionesTrabajos() {
        return nuevoSoCondicionesTrabajos;
    }

    public void setNuevoSoCondicionesTrabajos(SoCondicionesTrabajos nuevoSoCondicionesTrabajos) {
        this.nuevoSoCondicionesTrabajos = nuevoSoCondicionesTrabajos;
    }

    public SoCondicionesTrabajos getDuplicarSoCondicionesTrabajos() {
        return duplicarSoCondicionesTrabajos;
    }

    public void setDuplicarSoCondicionesTrabajos(SoCondicionesTrabajos duplicarSoCondicionesTrabajos) {
        this.duplicarSoCondicionesTrabajos = duplicarSoCondicionesTrabajos;
    }

    public SoCondicionesTrabajos getEditarSoCondicionesTrabajos() {
        return editarSoCondicionesTrabajos;
    }

    public void setEditarSoCondicionesTrabajos(SoCondicionesTrabajos editarSoCondicionesTrabajos) {
        this.editarSoCondicionesTrabajos = editarSoCondicionesTrabajos;
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

    public SoCondicionesTrabajos getCondicionTrabajoSeleccionada() {
        return condicionTrabajoSeleccionada;
    }

    public void setCondicionTrabajoSeleccionada(SoCondicionesTrabajos condicionTrabajoSeleccionada) {
        this.condicionTrabajoSeleccionada = condicionTrabajoSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
