/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.SoPoblacionObjetivos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSoPoblacionObjetivosInterface;
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
public class ControlSoPoblacionObjetivos implements Serializable {

    @EJB
    AdministrarSoPoblacionObjetivosInterface administrarSoPoblacionObjetivos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SoPoblacionObjetivos> listSoPoblacionObjetivos;
    private List<SoPoblacionObjetivos> filtrarSoPoblacionObjetivos;
    private List<SoPoblacionObjetivos> crearSoPoblacionObjetivos;
    private List<SoPoblacionObjetivos> modificarSoPoblacionObjetivos;
    private List<SoPoblacionObjetivos> borrarSoPoblacionObjetivos;
    private SoPoblacionObjetivos nuevoSoPoblacionObjetivos;
    private SoPoblacionObjetivos duplicarSoPoblacionObjetivos;
    private SoPoblacionObjetivos editarSoPoblacionObjetivos;
    private SoPoblacionObjetivos soPoblacionObjetivoSeleccionado;
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
    private Integer backupCodigo;
    private String backupDescripcion;
    private String backupPais;
    private String infoRegistro;

    public ControlSoPoblacionObjetivos() {
        listSoPoblacionObjetivos = null;
        crearSoPoblacionObjetivos = new ArrayList<SoPoblacionObjetivos>();
        modificarSoPoblacionObjetivos = new ArrayList<SoPoblacionObjetivos>();
        borrarSoPoblacionObjetivos = new ArrayList<SoPoblacionObjetivos>();
        permitirIndex = true;
        editarSoPoblacionObjetivos = new SoPoblacionObjetivos();
        nuevoSoPoblacionObjetivos = new SoPoblacionObjetivos();
        duplicarSoPoblacionObjetivos = new SoPoblacionObjetivos();
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
            administrarSoPoblacionObjetivos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSoPoblacionObjetivos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSoPoblacionObjetivos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlSoPoblacionObjetivos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listSoPoblacionObjetivos.get(index).getSecuencia();
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backupCodigo = listSoPoblacionObjetivos.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = listSoPoblacionObjetivos.get(index).getDescripcion();
                    System.out.println("DESCRIPCION : " + backupDescripcion);
                }

            } else if (tipoLista == 1) {
                if (cualCelda == 0) {
                    backupCodigo = filtrarSoPoblacionObjetivos.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = filtrarSoPoblacionObjetivos.get(index).getDescripcion();
                    System.out.println("DESCRIPCION : " + backupDescripcion);
                }

            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A ControlSoPoblacionObjetivos.asignarIndex \n");
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
            System.out.println("ERROR ControlSoPoblacionObjetivos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {

            if (cualCelda == 2) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            }
            if (cualCelda == 3) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            bandera = 0;
            filtrarSoPoblacionObjetivos = null;
            tipoLista = 0;
        }

        borrarSoPoblacionObjetivos.clear();
        crearSoPoblacionObjetivos.clear();
        modificarSoPoblacionObjetivos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoPoblacionObjetivos = null;
        guardado = true;
        permitirIndex = true;
        getListSoPoblacionObjetivos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoPoblacionObjetivos == null || listSoPoblacionObjetivos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            bandera = 0;
            filtrarSoPoblacionObjetivos = null;
            tipoLista = 0;
        }

        borrarSoPoblacionObjetivos.clear();
        crearSoPoblacionObjetivos.clear();
        modificarSoPoblacionObjetivos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoPoblacionObjetivos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoPoblacionObjetivos == null || listSoPoblacionObjetivos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            bandera = 0;
            filtrarSoPoblacionObjetivos = null;
            tipoLista = 0;
        }
    }

    public void modificarSoPoblacionObjetivos(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearSoPoblacionObjetivos.contains(listSoPoblacionObjetivos.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSoPoblacionObjetivos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);

                    } else {
                        for (int j = 0; j < listSoPoblacionObjetivos.size(); j++) {
                            if (j != indice) {
                                if (listSoPoblacionObjetivos.get(indice).getCodigo() == listSoPoblacionObjetivos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoPoblacionObjetivos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listSoPoblacionObjetivos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSoPoblacionObjetivos.isEmpty()) {
                            modificarSoPoblacionObjetivos.add(listSoPoblacionObjetivos.get(indice));
                        } else if (!modificarSoPoblacionObjetivos.contains(listSoPoblacionObjetivos.get(indice))) {
                            modificarSoPoblacionObjetivos.add(listSoPoblacionObjetivos.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSoPoblacionObjetivos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listSoPoblacionObjetivos.size(); j++) {
                            if (j != indice) {
                                if (listSoPoblacionObjetivos.get(indice).getCodigo() == listSoPoblacionObjetivos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoPoblacionObjetivos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listSoPoblacionObjetivos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearSoPoblacionObjetivos.contains(filtrarSoPoblacionObjetivos.get(indice))) {
                    if (filtrarSoPoblacionObjetivos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSoPoblacionObjetivos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoPoblacionObjetivos.get(indice).getCodigo() == filtrarSoPoblacionObjetivos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoPoblacionObjetivos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarSoPoblacionObjetivos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSoPoblacionObjetivos.isEmpty()) {
                            modificarSoPoblacionObjetivos.add(filtrarSoPoblacionObjetivos.get(indice));
                        } else if (!modificarSoPoblacionObjetivos.contains(filtrarSoPoblacionObjetivos.get(indice))) {
                            modificarSoPoblacionObjetivos.add(filtrarSoPoblacionObjetivos.get(indice));
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
                    if (filtrarSoPoblacionObjetivos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSoPoblacionObjetivos.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoPoblacionObjetivos.get(indice).getCodigo() == filtrarSoPoblacionObjetivos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSoPoblacionObjetivos.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoPoblacionObjetivos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarSoPoblacionObjetivos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSoPoblacionObjetivos.get(indice).setDescripcion(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
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
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSoPoblacionObjetivos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoSoPoblacionObjetivos");
                if (!modificarSoPoblacionObjetivos.isEmpty() && modificarSoPoblacionObjetivos.contains(listSoPoblacionObjetivos.get(index))) {
                    int modIndex = modificarSoPoblacionObjetivos.indexOf(listSoPoblacionObjetivos.get(index));
                    modificarSoPoblacionObjetivos.remove(modIndex);
                    borrarSoPoblacionObjetivos.add(listSoPoblacionObjetivos.get(index));
                } else if (!crearSoPoblacionObjetivos.isEmpty() && crearSoPoblacionObjetivos.contains(listSoPoblacionObjetivos.get(index))) {
                    int crearIndex = crearSoPoblacionObjetivos.indexOf(listSoPoblacionObjetivos.get(index));
                    crearSoPoblacionObjetivos.remove(crearIndex);
                } else {
                    borrarSoPoblacionObjetivos.add(listSoPoblacionObjetivos.get(index));
                }
                listSoPoblacionObjetivos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSoPoblacionObjetivos ");
                if (!modificarSoPoblacionObjetivos.isEmpty() && modificarSoPoblacionObjetivos.contains(filtrarSoPoblacionObjetivos.get(index))) {
                    int modIndex = modificarSoPoblacionObjetivos.indexOf(filtrarSoPoblacionObjetivos.get(index));
                    modificarSoPoblacionObjetivos.remove(modIndex);
                    borrarSoPoblacionObjetivos.add(filtrarSoPoblacionObjetivos.get(index));
                } else if (!crearSoPoblacionObjetivos.isEmpty() && crearSoPoblacionObjetivos.contains(filtrarSoPoblacionObjetivos.get(index))) {
                    int crearIndex = crearSoPoblacionObjetivos.indexOf(filtrarSoPoblacionObjetivos.get(index));
                    crearSoPoblacionObjetivos.remove(crearIndex);
                } else {
                    borrarSoPoblacionObjetivos.add(filtrarSoPoblacionObjetivos.get(index));
                }
                int VCIndex = listSoPoblacionObjetivos.indexOf(filtrarSoPoblacionObjetivos.get(index));
                listSoPoblacionObjetivos.remove(VCIndex);
                filtrarSoPoblacionObjetivos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSoPoblacionObjetivos == null || listSoPoblacionObjetivos.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    /*public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     BigInteger contarBienProgramacionesDepartamento;
     BigInteger contarCapModulosDepartamento;
     BigInteger contarCiudadesDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     System.err.println("Control Secuencia de ControlSoPoblacionObjetivos ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarSoPoblacionObjetivos.contarBienProgramacionesDepartamento(listSoPoblacionObjetivos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarSoPoblacionObjetivos.contarCapModulosDepartamento(listSoPoblacionObjetivos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarSoPoblacionObjetivos.contarCiudadesDepartamento(listSoPoblacionObjetivos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarSoPoblacionObjetivos.contarSoAccidentesMedicosDepartamento(listSoPoblacionObjetivos.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarSoPoblacionObjetivos.contarBienProgramacionesDepartamento(filtrarSoPoblacionObjetivos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarSoPoblacionObjetivos.contarCapModulosDepartamento(filtrarSoPoblacionObjetivos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarSoPoblacionObjetivos.contarCiudadesDepartamento(filtrarSoPoblacionObjetivos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarSoPoblacionObjetivos.contarSoAccidentesMedicosDepartamento(filtrarSoPoblacionObjetivos.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarCiudadesDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoSoPoblacionObjetivos();
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
     System.err.println("ERROR ControlSoPoblacionObjetivos verificarBorrado ERROR " + e);
     }
     }
     */
    public void revisarDialogoGuardar() {

        if (!borrarSoPoblacionObjetivos.isEmpty() || !crearSoPoblacionObjetivos.isEmpty() || !modificarSoPoblacionObjetivos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarSoPoblacionObjetivos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarSoPoblacionObjetivos");
            if (!borrarSoPoblacionObjetivos.isEmpty()) {
                administrarSoPoblacionObjetivos.borrarSoPoblacionObjetivos(borrarSoPoblacionObjetivos);
                //mostrarBorrados
                registrosBorrados = borrarSoPoblacionObjetivos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarSoPoblacionObjetivos.clear();
            }
            if (!modificarSoPoblacionObjetivos.isEmpty()) {
                administrarSoPoblacionObjetivos.modificarSoPoblacionObjetivos(modificarSoPoblacionObjetivos);
                modificarSoPoblacionObjetivos.clear();
            }
            if (!crearSoPoblacionObjetivos.isEmpty()) {
                administrarSoPoblacionObjetivos.crearSoPoblacionObjetivos(crearSoPoblacionObjetivos);
                crearSoPoblacionObjetivos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSoPoblacionObjetivos = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSoPoblacionObjetivos = listSoPoblacionObjetivos.get(index);
            }
            if (tipoLista == 1) {
                editarSoPoblacionObjetivos = filtrarSoPoblacionObjetivos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                RequestContext.getCurrentInstance().execute("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
                RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editBancos");
                RequestContext.getCurrentInstance().execute("PF('editBancos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
                RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoSoPoblacionObjetivos() {
        System.out.println("agregarNuevoSoPoblacionObjetivos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSoPoblacionObjetivos.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoSoPoblacionObjetivos.getCodigo());

            for (int x = 0; x < listSoPoblacionObjetivos.size(); x++) {
                if (listSoPoblacionObjetivos.get(x).getCodigo().equals(nuevoSoPoblacionObjetivos.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;//1
            }
        }
        if (nuevoSoPoblacionObjetivos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoSoPoblacionObjetivos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//2

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarSoPoblacionObjetivos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoSoPoblacionObjetivos.setSecuencia(l);

            crearSoPoblacionObjetivos.add(nuevoSoPoblacionObjetivos);

            listSoPoblacionObjetivos.add(nuevoSoPoblacionObjetivos);
            nuevoSoPoblacionObjetivos = new SoPoblacionObjetivos();

            infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSoPoblacionObjetivos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSoPoblacionObjetivos() {
        System.out.println("limpiarNuevoSoPoblacionObjetivos");
        nuevoSoPoblacionObjetivos = new SoPoblacionObjetivos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void cargarNuevoSoPoblacionObjetivos() {
        System.out.println("cargarNuevoSoPoblacionObjetivos");

        duplicarSoPoblacionObjetivos = new SoPoblacionObjetivos();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSoPoblacionObjetivos').show()");

    }

    public void duplicandoSoPoblacionObjetivos() {
        System.out.println("duplicandoSoPoblacionObjetivos");
        if (index >= 0) {
            duplicarSoPoblacionObjetivos = new SoPoblacionObjetivos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSoPoblacionObjetivos.setSecuencia(l);
                duplicarSoPoblacionObjetivos.setCodigo(listSoPoblacionObjetivos.get(index).getCodigo());
                duplicarSoPoblacionObjetivos.setDescripcion(listSoPoblacionObjetivos.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSoPoblacionObjetivos.setSecuencia(l);
                duplicarSoPoblacionObjetivos.setCodigo(filtrarSoPoblacionObjetivos.get(index).getCodigo());
                duplicarSoPoblacionObjetivos.setDescripcion(filtrarSoPoblacionObjetivos.get(index).getDescripcion());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoPoblacionObjetivos').show()");
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
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSoPoblacionObjetivos.getCodigo());

        if (duplicarSoPoblacionObjetivos.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listSoPoblacionObjetivos.size(); x++) {
                if (listSoPoblacionObjetivos.get(x).getCodigo().equals(duplicarSoPoblacionObjetivos.getCodigo())) {
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

        if (duplicarSoPoblacionObjetivos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarSoPoblacionObjetivos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarSoPoblacionObjetivos.setSecuencia(l);
            System.out.println("Datos Duplicando: " + duplicarSoPoblacionObjetivos.getSecuencia() + "  " + duplicarSoPoblacionObjetivos.getCodigo());
            if (crearSoPoblacionObjetivos.contains(duplicarSoPoblacionObjetivos)) {
                System.out.println("Ya lo contengo.");
            }
            listSoPoblacionObjetivos.add(duplicarSoPoblacionObjetivos);
            crearSoPoblacionObjetivos.add(duplicarSoPoblacionObjetivos);
            RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
            index = -1;
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("CODIGO : " + duplicarSoPoblacionObjetivos.getCodigo());
            System.out.println("EMPRESA: " + duplicarSoPoblacionObjetivos.getDescripcion());
            System.out.println("--------------DUPLICAR------------------------");

            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }

            infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();

            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoPoblacionObjetivos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoPoblacionObjetivos");
                bandera = 0;
                filtrarSoPoblacionObjetivos = null;
                tipoLista = 0;
            }
            duplicarSoPoblacionObjetivos = new SoPoblacionObjetivos();

            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoPoblacionObjetivos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSoPoblacionObjetivos() {
        duplicarSoPoblacionObjetivos = new SoPoblacionObjetivos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoPoblacionObjetivosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SOPOBLACIONOBJETIVOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoPoblacionObjetivosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SOPOBLACIONOBJETIVOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSoPoblacionObjetivos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOPOBLACIONOBJETIVOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SOPOBLACIONOBJETIVOS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<SoPoblacionObjetivos> getListSoPoblacionObjetivos() {
        if (listSoPoblacionObjetivos == null) {
            listSoPoblacionObjetivos = administrarSoPoblacionObjetivos.consultarSoPoblacionObjetivos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoPoblacionObjetivos == null || listSoPoblacionObjetivos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoPoblacionObjetivos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSoPoblacionObjetivos;
    }

    public void setListSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
        this.listSoPoblacionObjetivos = listSoPoblacionObjetivos;
    }

    public List<SoPoblacionObjetivos> getFiltrarSoPoblacionObjetivos() {
        return filtrarSoPoblacionObjetivos;
    }

    public void setFiltrarSoPoblacionObjetivos(List<SoPoblacionObjetivos> filtrarSoPoblacionObjetivos) {
        this.filtrarSoPoblacionObjetivos = filtrarSoPoblacionObjetivos;
    }

    public SoPoblacionObjetivos getNuevoSoPoblacionObjetivos() {
        return nuevoSoPoblacionObjetivos;
    }

    public void setNuevoSoPoblacionObjetivos(SoPoblacionObjetivos nuevoSoPoblacionObjetivos) {
        this.nuevoSoPoblacionObjetivos = nuevoSoPoblacionObjetivos;
    }

    public SoPoblacionObjetivos getDuplicarSoPoblacionObjetivos() {
        return duplicarSoPoblacionObjetivos;
    }

    public void setDuplicarSoPoblacionObjetivos(SoPoblacionObjetivos duplicarSoPoblacionObjetivos) {
        this.duplicarSoPoblacionObjetivos = duplicarSoPoblacionObjetivos;
    }

    public SoPoblacionObjetivos getEditarSoPoblacionObjetivos() {
        return editarSoPoblacionObjetivos;
    }

    public void setEditarSoPoblacionObjetivos(SoPoblacionObjetivos editarSoPoblacionObjetivos) {
        this.editarSoPoblacionObjetivos = editarSoPoblacionObjetivos;
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public SoPoblacionObjetivos getSoPoblacionObjetivoSeleccionado() {
        return soPoblacionObjetivoSeleccionado;
    }

    public void setSoPoblacionObjetivoSeleccionado(SoPoblacionObjetivos soPoblacionObjetivoSeleccionado) {
        this.soPoblacionObjetivoSeleccionado = soPoblacionObjetivoSeleccionado;
    }

}
