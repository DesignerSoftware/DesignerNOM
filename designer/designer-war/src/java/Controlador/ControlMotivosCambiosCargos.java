/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosCambiosCargos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosCambiosCargosInterface;
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
public class ControlMotivosCambiosCargos implements Serializable {

    @EJB
    AdministrarMotivosCambiosCargosInterface administrarMotivosCambiosCargos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosCambiosCargos> listMotivosCambiosCargos;
    private List<MotivosCambiosCargos> filtrarMotivosCambiosCargos;
    private List<MotivosCambiosCargos> crearMotivoCambioCargo;
    private List<MotivosCambiosCargos> modificarMotivoCambioCargo;
    private List<MotivosCambiosCargos> borrarMotivoCambioCargo;
    private MotivosCambiosCargos nuevoMotivoCambioCargo;
    private MotivosCambiosCargos duplicarMotivoCambioCargo;
    private MotivosCambiosCargos editarMotivoCambioCargo;
    private MotivosCambiosCargos motivoCambioCargoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private Short backupCodigo;
    private String backupNombre;
    private int tamano;
    private String infoRegistro, paginaanterior;
    private boolean activarLOV;
//
    private DataTable tablaC;

    /**
     * Creates a new instance of ControlMotivosCambiosCargos
     */
    public ControlMotivosCambiosCargos() {

        listMotivosCambiosCargos = null;
        crearMotivoCambioCargo = new ArrayList<MotivosCambiosCargos>();
        modificarMotivoCambioCargo = new ArrayList<MotivosCambiosCargos>();
        borrarMotivoCambioCargo = new ArrayList<MotivosCambiosCargos>();
        permitirIndex = true;
        editarMotivoCambioCargo = new MotivosCambiosCargos();
        nuevoMotivoCambioCargo = new MotivosCambiosCargos();
        duplicarMotivoCambioCargo = new MotivosCambiosCargos();
        guardado = true;
        tamano = 315;
        paginaanterior = "";
        activarLOV = true;
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   mapParametros.put ("paginaAnterior", paginaAnterior);
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
            administrarMotivosCambiosCargos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaanterior = pagina;
        getListMotivosCambiosCargos();
        if (listMotivosCambiosCargos != null) {
            if (!listMotivosCambiosCargos.isEmpty()) {
                motivoCambioCargoSeleccionado = listMotivosCambiosCargos.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(MotivosCambiosCargos motivoCambioCargo, int celda) {
        motivoCambioCargoSeleccionado = motivoCambioCargo;

        if (permitirIndex == true) {
            cualCelda = celda;
            motivoCambioCargoSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                backupCodigo = motivoCambioCargoSeleccionado.getCodigo();
            }
            if (cualCelda == 1) {
                backupNombre = motivoCambioCargoSeleccionado.getNombre();
            }
        }

    }

    public void asignarIndex(MotivosCambiosCargos motivoCambioCargo, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMotiviosCambiosCargos.asignarIndex \n");
            motivoCambioCargoSeleccionado = motivoCambioCargo;
            RequestContext context = RequestContext.getCurrentInstance();
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            bandera = 0;
            filtrarMotivosCambiosCargos = null;
            tipoLista = 0;
        }
        tamano = 315;
        borrarMotivoCambioCargo.clear();
        crearMotivoCambioCargo.clear();
        modificarMotivoCambioCargo.clear();
        k = 0;
        listMotivosCambiosCargos = null;
        motivoCambioCargoSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        getListMotivosCambiosCargos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            bandera = 0;
            filtrarMotivosCambiosCargos = null;
            tipoLista = 0;
        }
        tamano = 315;
        borrarMotivoCambioCargo.clear();
        crearMotivoCambioCargo.clear();
        modificarMotivoCambioCargo.clear();
        motivoCambioCargoSeleccionado = null;
        k = 0;
        listMotivosCambiosCargos = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        getListMotivosCambiosCargos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 295;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tamano = 315;
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            bandera = 0;
            filtrarMotivosCambiosCargos = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosCambiosCargos(MotivosCambiosCargos motivoCambioCargo, String column, String valorConfirmar) {
        motivoCambioCargoSeleccionado = motivoCambioCargo;
        int contador = 0;
        Short codigoVacio = new Short("0");
        boolean coincidencias = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (column.equalsIgnoreCase("N")) {

            if (motivoCambioCargoSeleccionado.getCodigo() == null || motivoCambioCargoSeleccionado.getCodigo().equals(codigoVacio)) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                motivoCambioCargoSeleccionado.setCodigo(backupCodigo);
            } else {

                for (int j = 0; j < listMotivosCambiosCargos.size(); j++) {
                    if (motivoCambioCargoSeleccionado.getSecuencia() != listMotivosCambiosCargos.get(j).getSecuencia()) {
                        if (motivoCambioCargoSeleccionado.getCodigo().equals(listMotivosCambiosCargos.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    coincidencias = false;
                    motivoCambioCargoSeleccionado.setCodigo(backupCodigo);
                } else {
                    coincidencias = true;
                }
            }
        }

        if (column.equalsIgnoreCase("M")) {

            if (motivoCambioCargoSeleccionado.getNombre() == null || motivoCambioCargoSeleccionado.getNombre().isEmpty()) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                motivoCambioCargoSeleccionado.setNombre(backupNombre);
                coincidencias = false;
            }
            for (int j = 0; j < listMotivosCambiosCargos.size(); j++) {
                if (motivoCambioCargoSeleccionado.getSecuencia() != listMotivosCambiosCargos.get(j).getSecuencia()) {
                    if (motivoCambioCargoSeleccionado.getNombre().equals(listMotivosCambiosCargos.get(j).getNombre())) {
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "MOTIVO REPETIDOS";
                coincidencias = false;
                motivoCambioCargoSeleccionado.setCodigo(backupCodigo);
            } else {
                coincidencias = true;
            }
        }

        if (coincidencias == true) {
            if (!crearMotivoCambioCargo.contains(motivoCambioCargoSeleccionado)) {
                if (!modificarMotivoCambioCargo.contains(motivoCambioCargoSeleccionado)) {
                    modificarMotivoCambioCargo.add(motivoCambioCargoSeleccionado);
                }
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().update("form:validacionModificar");
            RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
        }

        RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
    }

    public void borrarMotivosCambiosCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoCambioCargoSeleccionado != null) {
            if (!modificarMotivoCambioCargo.isEmpty() && modificarMotivoCambioCargo.contains(motivoCambioCargoSeleccionado)) {
                modificarMotivoCambioCargo.remove(modificarMotivoCambioCargo.indexOf(motivoCambioCargoSeleccionado));
                borrarMotivoCambioCargo.add(motivoCambioCargoSeleccionado);
            } else if (!crearMotivoCambioCargo.isEmpty() && crearMotivoCambioCargo.contains(motivoCambioCargoSeleccionado)) {
                crearMotivoCambioCargo.remove(crearMotivoCambioCargo.indexOf(motivoCambioCargoSeleccionado));
            } else {
                borrarMotivoCambioCargo.add(motivoCambioCargoSeleccionado);
            }
            listMotivosCambiosCargos.remove(motivoCambioCargoSeleccionado);

            if (tipoLista == 1) {
                filtrarMotivosCambiosCargos.remove(motivoCambioCargoSeleccionado);
                listMotivosCambiosCargos.remove(motivoCambioCargoSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger borradoVC;

        try {
            if (tipoLista == 0) {
                borradoVC = administrarMotivosCambiosCargos.contarVigenciasCargosMotivoCambioCargo(motivoCambioCargoSeleccionado.getSecuencia());
            } else {
                borradoVC = administrarMotivosCambiosCargos.contarVigenciasCargosMotivoCambioCargo(motivoCambioCargoSeleccionado.getSecuencia());
            }
            if (borradoVC.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrarMotivosCambiosCargos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                motivoCambioCargoSeleccionado = null;
                borradoVC = new BigInteger("-1");
            }

        } catch (Exception e) {
            System.err.println("ERROR ControlTiposEntidades verificarBorrado ERROR " + e);
        }
    }

    public void guardarMotivosCambiosCargos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarMotivoCambioCargo.isEmpty()) {
                administrarMotivosCambiosCargos.borrarMotivosCambiosCargos(borrarMotivoCambioCargo);

                //mostrarBorrados
                registrosBorrados = borrarMotivoCambioCargo.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivoCambioCargo.clear();
            }
            if (!crearMotivoCambioCargo.isEmpty()) {

                administrarMotivosCambiosCargos.crearMotivosCambiosCargos(crearMotivoCambioCargo);
                crearMotivoCambioCargo.clear();
            }
            if (!modificarMotivoCambioCargo.isEmpty()) {
                administrarMotivosCambiosCargos.modificarMotivosCambiosCargos(modificarMotivoCambioCargo);
                modificarMotivoCambioCargo.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosCambiosCargos = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (motivoCambioCargoSeleccionado != null) {
            editarMotivoCambioCargo = motivoCambioCargoSeleccionado;

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
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivoCambioCargo() {
        int contador = 0;
        int duplicados = 0;
        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoCambioCargo.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoCambioCargo.getCodigo());

            for (int x = 0; x < listMotivosCambiosCargos.size(); x++) {
                if (listMotivosCambiosCargos.get(x).getCodigo() == nuevoMotivoCambioCargo.getCodigo()) {
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
        if (nuevoMotivoCambioCargo.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 315;
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
                bandera = 0;
                filtrarMotivosCambiosCargos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoCambioCargo.setSecuencia(l);
            crearMotivoCambioCargo.add(nuevoMotivoCambioCargo);
            listMotivosCambiosCargos.add(nuevoMotivoCambioCargo);
            nuevoMotivoCambioCargo = new MotivosCambiosCargos();
            motivoCambioCargoSeleccionado = nuevoMotivoCambioCargo;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoCambiosCargos').hide()");
            System.out.println("Despues de nuevoRegistroMotivoCambiosCargos");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoMotivoCambioCargo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoMotivoCambioCargo').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoCambioCargo() {
        System.out.println("limpiarNuevoMotivoCambioCargo");
        nuevoMotivoCambioCargo = new MotivosCambiosCargos();
        motivoCambioCargoSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosCambiosCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoCambioCargoSeleccionado != null) {
            duplicarMotivoCambioCargo = new MotivosCambiosCargos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoCambioCargo.setSecuencia(l);
            duplicarMotivoCambioCargo.setCodigo(motivoCambioCargoSeleccionado.getCodigo());
            duplicarMotivoCambioCargo.setNombre(motivoCambioCargoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivosCambiosCargos");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosCambiosCargos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        if (duplicarMotivoCambioCargo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosCambiosCargos.size(); x++) {
                if (listMotivosCambiosCargos.get(x).getCodigo() == duplicarMotivoCambioCargo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "No puede haber códigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoCambioCargo.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivoCambioCargo.getSecuencia() + "  " + duplicarMotivoCambioCargo.getCodigo());
            if (crearMotivoCambioCargo.contains(duplicarMotivoCambioCargo)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosCambiosCargos.add(duplicarMotivoCambioCargo);
            crearMotivoCambioCargo.add(duplicarMotivoCambioCargo);
            motivoCambioCargoSeleccionado = duplicarMotivoCambioCargo;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 315;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoCambioCargo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoCambioCargo");
                bandera = 0;
                filtrarMotivosCambiosCargos = null;
                tipoLista = 0;
            }
            infoRegistro = "Cantidad de registros: " + listMotivosCambiosCargos.size();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            duplicarMotivoCambioCargo = new MotivosCambiosCargos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosCambiosCargos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarMotivoCambioCargo");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarMotivoCambioCargo').show()");
        }
    }

    public void limpiarduplicarMotivosCambiosCargos() {
        duplicarMotivoCambioCargo = new MotivosCambiosCargos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoCambioCargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosCambiosCargosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoCambioCargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosCambiosCargosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoCambioCargoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(motivoCambioCargoSeleccionado.getSecuencia(), "MOTIVOSCAMBIOSCARGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSCAMBIOSCARGOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void recordarSeleccionMotivoCambioCargo() {
        if (motivoCambioCargoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosMotivoCambioCargo");
            tablaC.setSelection(motivoCambioCargoSeleccionado);
        }
    }

    //--------------------------------------------------------------------------
    public List<MotivosCambiosCargos> getListMotivosCambiosCargos() {
        if (listMotivosCambiosCargos == null) {
            listMotivosCambiosCargos = administrarMotivosCambiosCargos.consultarMotivosCambiosCargos();
        }
        return listMotivosCambiosCargos;
    }

    public void setListMotivosCambiosCargos(List<MotivosCambiosCargos> listMotivosCambiosCargos) {
        this.listMotivosCambiosCargos = listMotivosCambiosCargos;
    }

    public List<MotivosCambiosCargos> getFiltrarMotivosCambiosCargos() {
        return filtrarMotivosCambiosCargos;
    }

    public void setFiltrarMotivosCambiosCargos(List<MotivosCambiosCargos> filtrarMotivosCambiosCargos) {
        this.filtrarMotivosCambiosCargos = filtrarMotivosCambiosCargos;
    }

    public MotivosCambiosCargos getNuevoMotivoCambioCargo() {
        return nuevoMotivoCambioCargo;
    }

    public void setNuevoMotivoCambioCargo(MotivosCambiosCargos nuevoMotivoCambioCargo) {
        this.nuevoMotivoCambioCargo = nuevoMotivoCambioCargo;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getPaginaanterior() {
        return paginaanterior;
    }

    public void setPaginaanterior(String paginaanterior) {
        this.paginaanterior = paginaanterior;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public MotivosCambiosCargos getEditarMotivoCambioCargo() {
        return editarMotivoCambioCargo;
    }

    public void setEditarMotivoCambioCargo(MotivosCambiosCargos editarMotivoCambioCargo) {
        this.editarMotivoCambioCargo = editarMotivoCambioCargo;
    }

    public MotivosCambiosCargos getDuplicarMotivoCambioCargo() {
        return duplicarMotivoCambioCargo;
    }

    public void setDuplicarMotivoCambioCargo(MotivosCambiosCargos duplicarMotivoCambioCargo) {
        this.duplicarMotivoCambioCargo = duplicarMotivoCambioCargo;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public MotivosCambiosCargos getMotivoCambioCargoSeleccionado() {
        return motivoCambioCargoSeleccionado;
    }

    public void setMotivoCambioCargoSeleccionado(MotivosCambiosCargos motivoCambioGargoSeleccionado) {
        this.motivoCambioCargoSeleccionado = motivoCambioGargoSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivoCambioCargo");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

}
