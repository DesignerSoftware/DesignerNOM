/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.ClasesAccidentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarClasesAccidentesInterface;
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
public class ControlClasesAccidentes implements Serializable {

    @EJB
    AdministrarClasesAccidentesInterface administrarClasesAccidentes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<ClasesAccidentes> listClasesAccidentes;
    private List<ClasesAccidentes> filtrarClasesAccidentes;
    private List<ClasesAccidentes> crearClasesAccidentes;
    private List<ClasesAccidentes> modificarClasesAccidentes;
    private List<ClasesAccidentes> borrarClasesAccidentes;
    private ClasesAccidentes nuevaClaseAccidente;
    private ClasesAccidentes duplicarClaseAccidente;
    private ClasesAccidentes editarClaseAccidente;
    private ClasesAccidentes claseAccidenteSeleccionado;
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
    private int tamano;
    private String mensajeValidacion;
    private BigInteger verificarBorradoAccidentes;
    private String backUpCodigo;
    private String backUpDescripcion;

    public ControlClasesAccidentes() {
        listClasesAccidentes = null;
        crearClasesAccidentes = new ArrayList<ClasesAccidentes>();
        modificarClasesAccidentes = new ArrayList<ClasesAccidentes>();
        borrarClasesAccidentes = new ArrayList<ClasesAccidentes>();
        permitirIndex = true;
        editarClaseAccidente = new ClasesAccidentes();
        nuevaClaseAccidente = new ClasesAccidentes();
        duplicarClaseAccidente = new ClasesAccidentes();
        guardado = true;
        tamano = 270;
   mapParametros.put ("paginaAnterior", paginaAnterior);
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
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
         String pagActual = "claseaccidente";
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
            administrarClasesAccidentes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n EVENTO FILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
             RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarClasesAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR EVENTO FILTRAR ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listClasesAccidentes.get(indice).getCodigo();
                } else {
                    backUpCodigo = filtrarClasesAccidentes.get(indice).getCodigo();
                }
            } else if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listClasesAccidentes.get(indice).getNombre();
                } else {
                    backUpDescripcion = filtrarClasesAccidentes.get(indice).getNombre();
                }
            }
            secRegistro = listClasesAccidentes.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE CONTROCLASESACCIDENTES  AsignarIndex \n");
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
            System.out.println("ERROR CONTROCLASESACCIDENTES asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            bandera = 0;
            filtrarClasesAccidentes = null;
            tipoLista = 0;
        }

        borrarClasesAccidentes.clear();
        crearClasesAccidentes.clear();
        modificarClasesAccidentes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listClasesAccidentes = null;
        guardado = true;
        permitirIndex = true;
        getListClasesAccidentes();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
        if (listClasesAccidentes == null || listClasesAccidentes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listClasesAccidentes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    private String infoRegistro;

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            bandera = 0;
            filtrarClasesAccidentes = null;
            tipoLista = 0;
        }

        borrarClasesAccidentes.clear();
        crearClasesAccidentes.clear();
        modificarClasesAccidentes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listClasesAccidentes = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            bandera = 0;
            filtrarClasesAccidentes = null;
            tipoLista = 0;
        }
    }

    public void modificandoClaseAccidente(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("MODIFICAR CLASES ACCIDENTES");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("MODIFICANDO CLASE ACCIDENTE CONFIRMAR CAMBIO = N");
            if (tipoLista == 0) {
                if (!crearClasesAccidentes.contains(listClasesAccidentes.get(indice))) {
                    if (listClasesAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listClasesAccidentes.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listClasesAccidentes.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listClasesAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listClasesAccidentes.get(indice).getCodigo().equals(listClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listClasesAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (listClasesAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarClasesAccidentes.isEmpty()) {
                            modificarClasesAccidentes.add(listClasesAccidentes.get(indice));
                        } else if (!modificarClasesAccidentes.contains(listClasesAccidentes.get(indice))) {
                            modificarClasesAccidentes.add(listClasesAccidentes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {

                    if (listClasesAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listClasesAccidentes.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listClasesAccidentes.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listClasesAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listClasesAccidentes.get(indice).getCodigo().equals(listClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listClasesAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (listClasesAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearClasesAccidentes.contains(filtrarClasesAccidentes.get(indice))) {
                    if (filtrarClasesAccidentes.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarClasesAccidentes.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listClasesAccidentes.size(); j++) {
                            System.err.println("indice lista  indice : " + listClasesAccidentes.get(j).getCodigo());
                            if (indice != j) {
                                if (filtrarClasesAccidentes.get(indice).getCodigo().equals(listClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        for (int j = 0; j < filtrarClasesAccidentes.size(); j++) {
                            System.err.println("indice filtrar indice : " + filtrarClasesAccidentes.get(j).getCodigo());
                            if (j != indice) {
                                if (filtrarClasesAccidentes.get(indice).getCodigo().equals(filtrarClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarClasesAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarClasesAccidentes.get(indice).getNombre().equals(" ")) {
                        filtrarClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarClasesAccidentes.isEmpty()) {
                            modificarClasesAccidentes.add(filtrarClasesAccidentes.get(indice));
                        } else if (!modificarClasesAccidentes.contains(filtrarClasesAccidentes.get(indice))) {
                            modificarClasesAccidentes.add(filtrarClasesAccidentes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarClasesAccidentes.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarClasesAccidentes.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listClasesAccidentes.size(); j++) {
                            System.err.println("indice lista  indice : " + listClasesAccidentes.get(j).getCodigo());
                            if (indice != j) {
                                if (filtrarClasesAccidentes.get(indice).getCodigo().equals(listClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        for (int j = 0; j < filtrarClasesAccidentes.size(); j++) {
                            System.err.println("indice filtrar indice : " + filtrarClasesAccidentes.get(j).getCodigo());
                            if (j != indice) {
                                if (filtrarClasesAccidentes.get(indice).getCodigo().equals(filtrarClasesAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarClasesAccidentes.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarClasesAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarClasesAccidentes.get(indice).getNombre().equals(" ")) {
                        filtrarClasesAccidentes.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
        }

    }

    public void borrandoClaseAccidente() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("borrandoClaseAccidente");
                if (!modificarClasesAccidentes.isEmpty() && modificarClasesAccidentes.contains(listClasesAccidentes.get(index))) {
                    int modIndex = modificarClasesAccidentes.indexOf(listClasesAccidentes.get(index));
                    modificarClasesAccidentes.remove(modIndex);
                    borrarClasesAccidentes.add(listClasesAccidentes.get(index));
                } else if (!crearClasesAccidentes.isEmpty() && crearClasesAccidentes.contains(listClasesAccidentes.get(index))) {
                    int crearIndex = crearClasesAccidentes.indexOf(listClasesAccidentes.get(index));
                    crearClasesAccidentes.remove(crearIndex);
                } else {
                    borrarClasesAccidentes.add(listClasesAccidentes.get(index));
                }
                listClasesAccidentes.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoClaseAccidente");
                if (!modificarClasesAccidentes.isEmpty() && modificarClasesAccidentes.contains(filtrarClasesAccidentes.get(index))) {
                    int modIndex = modificarClasesAccidentes.indexOf(filtrarClasesAccidentes.get(index));
                    modificarClasesAccidentes.remove(modIndex);
                    borrarClasesAccidentes.add(filtrarClasesAccidentes.get(index));
                } else if (!crearClasesAccidentes.isEmpty() && crearClasesAccidentes.contains(filtrarClasesAccidentes.get(index))) {
                    int crearIndex = crearClasesAccidentes.indexOf(filtrarClasesAccidentes.get(index));
                    crearClasesAccidentes.remove(crearIndex);
                } else {
                    borrarClasesAccidentes.add(filtrarClasesAccidentes.get(index));
                }
                int VCIndex = listClasesAccidentes.indexOf(filtrarClasesAccidentes.get(index));
                listClasesAccidentes.remove(VCIndex);
                filtrarClasesAccidentes.remove(index);

            }
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            index = -1;
            secRegistro = null;
            infoRegistro = "Cantidad de registros: " + listClasesAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("verificarBorrado");
        try {
            if (tipoLista == 0) {
                verificarBorradoAccidentes = administrarClasesAccidentes.verificarSoAccidentesMedicosClaseAccidente(listClasesAccidentes.get(index).getSecuencia());
            } else {
                verificarBorradoAccidentes = administrarClasesAccidentes.verificarSoAccidentesMedicosClaseAccidente(filtrarClasesAccidentes.get(index).getSecuencia());
            }
            if (verificarBorradoAccidentes.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoClaseAccidente();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                verificarBorradoAccidentes = new BigInteger("-1");
            }
            verificarBorradoAccidentes = new BigInteger("-1");
        } catch (Exception e) {
            System.err.println("ERROR CLASES ACCIDENTES verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarClasesAccidentes.isEmpty() || !crearClasesAccidentes.isEmpty() || !modificarClasesAccidentes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardandoClasesAccidentes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("REALIZANDO CLASES ACCIDENTES");
            if (!borrarClasesAccidentes.isEmpty()) {
                administrarClasesAccidentes.borrarClasesAccidentes(borrarClasesAccidentes);
                //mostrarBorrados
                registrosBorrados = borrarClasesAccidentes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarClasesAccidentes.clear();
            }
            if (!crearClasesAccidentes.isEmpty()) {
                administrarClasesAccidentes.crearClasesAccidentes(crearClasesAccidentes);
                crearClasesAccidentes.clear();
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
                bandera = 0;
                filtrarClasesAccidentes = null;
                tipoLista = 0;
            }
            if (!modificarClasesAccidentes.isEmpty()) {
                administrarClasesAccidentes.modificarClasesAccidentes(modificarClasesAccidentes);
                modificarClasesAccidentes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listClasesAccidentes = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            k = 0;
            if (guardado == false) {
                guardado = true;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarClaseAccidente = listClasesAccidentes.get(index);
            }
            if (tipoLista == 1) {
                editarClaseAccidente = filtrarClasesAccidentes.get(index);
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

    public void agregarNuevaClaseAccidente() {
        System.out.println("agregarNuevaClaseAccidente");
        int contador = 0;
        int duplicados = 0;

        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaClaseAccidente.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevaClaseAccidente.getCodigo());

            for (int x = 0; x < listClasesAccidentes.size(); x++) {
                if (listClasesAccidentes.get(x).getCodigo().equals(nuevaClaseAccidente.getCodigo())) {
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
        if (nuevaClaseAccidente.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
                bandera = 0;
                filtrarClasesAccidentes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevaClaseAccidente.setSecuencia(l);

            crearClasesAccidentes.add(nuevaClaseAccidente);

            listClasesAccidentes.add(nuevaClaseAccidente);
            nuevaClaseAccidente = new ClasesAccidentes();
            infoRegistro = "Cantidad de registros: " + listClasesAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClasesAccidentes').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevaClaseAccidente() {
        System.out.println("limpiarNuevaClaseAccidente");
        nuevaClaseAccidente = new ClasesAccidentes();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoClaseAccidente() {
        System.out.println("duplicandoClaseAccidente");
        if (index >= 0) {
            duplicarClaseAccidente = new ClasesAccidentes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarClaseAccidente.setSecuencia(l);
                duplicarClaseAccidente.setCodigo(listClasesAccidentes.get(index).getCodigo());
                duplicarClaseAccidente.setNombre(listClasesAccidentes.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarClaseAccidente.setSecuencia(l);
                duplicarClaseAccidente.setCodigo(filtrarClasesAccidentes.get(index).getCodigo());
                duplicarClaseAccidente.setNombre(filtrarClasesAccidentes.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCA");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClasesAccidentes').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("CONFIRMAR DUPLICAR CLASE ACCIDENTE");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarClaseAccidente.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarClaseAccidente.getNombre());

        if (duplicarClaseAccidente.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int x = 0; x < listClasesAccidentes.size(); x++) {
                if (listClasesAccidentes.get(x).getCodigo().equals(duplicarClaseAccidente.getCodigo())) {
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
        if (duplicarClaseAccidente.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarClaseAccidente.getSecuencia() + "  " + duplicarClaseAccidente.getCodigo());
            if (crearClasesAccidentes.contains(duplicarClaseAccidente)) {
                System.out.println("Ya lo contengo.");
            }
            listClasesAccidentes.add(duplicarClaseAccidente);
            crearClasesAccidentes.add(duplicarClaseAccidente);
            RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listClasesAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosClasesAccidentes");
                bandera = 0;
                filtrarClasesAccidentes = null;
                tipoLista = 0;
            }
            duplicarClaseAccidente = new ClasesAccidentes();
            RequestContext.getCurrentInstance().execute("duplicarRegistroClasesAccidentes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarClaseAccidente() {
        duplicarClaseAccidente = new ClasesAccidentes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CLASESACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CLASESACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listClasesAccidentes.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CLASESACCIDENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("CLASESACCIDENTES")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<ClasesAccidentes> getListClasesAccidentes() {
        if (listClasesAccidentes == null) {
            listClasesAccidentes = administrarClasesAccidentes.consultarClasesAccidentes();

        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listClasesAccidentes == null || listClasesAccidentes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listClasesAccidentes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listClasesAccidentes;
    }

    public void setListClasesAccidentes(List<ClasesAccidentes> listClasesAccidentes) {
        this.listClasesAccidentes = listClasesAccidentes;
    }

    public List<ClasesAccidentes> getFiltrarClasesAccidentes() {
        return filtrarClasesAccidentes;
    }

    public void setFiltrarClasesAccidentes(List<ClasesAccidentes> filtrarClasesAccidentes) {
        this.filtrarClasesAccidentes = filtrarClasesAccidentes;
    }

    public ClasesAccidentes getNuevaClaseAccidente() {
        return nuevaClaseAccidente;
    }

    public void setNuevaClaseAccidente(ClasesAccidentes nuevaClaseAccidente) {
        this.nuevaClaseAccidente = nuevaClaseAccidente;
    }

    public ClasesAccidentes getDuplicarClaseAccidente() {
        return duplicarClaseAccidente;
    }

    public void setDuplicarClaseAccidente(ClasesAccidentes duplicarClaseAccidente) {
        this.duplicarClaseAccidente = duplicarClaseAccidente;
    }

    public ClasesAccidentes getEditarClaseAccidente() {
        return editarClaseAccidente;
    }

    public void setEditarClaseAccidente(ClasesAccidentes editarClaseAccidente) {
        this.editarClaseAccidente = editarClaseAccidente;
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

    public ClasesAccidentes getClaseAccidenteSeleccionado() {
        return claseAccidenteSeleccionado;
    }

    public void setClaseAccidenteSeleccionado(ClasesAccidentes claseAccidenteSeleccionado) {
        this.claseAccidenteSeleccionado = claseAccidenteSeleccionado;
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
