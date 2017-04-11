/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Motivosmvrs;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosMvrsInterface;
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
public class ControlMotivosMvrs implements Serializable {

    @EJB
    AdministrarMotivosMvrsInterface administrarMotivosMvrs;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Motivosmvrs> listMotivosMvrs;
    private List<Motivosmvrs> filtrarMotivosMvrs;
    private List<Motivosmvrs> crearMotivoMvrs;
    private List<Motivosmvrs> modificarMotivoMvrs;
    private List<Motivosmvrs> borrarMotivoMvrs;
    private Motivosmvrs nuevoMotivoMvr;
    private Motivosmvrs duplicarMotivosMvrs;
    private Motivosmvrs editarMotivosMvrs;
    private Motivosmvrs motivoMvrSeleccionada;
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
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private int tamano;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMotivosMvrs() {
        listMotivosMvrs = null;
        crearMotivoMvrs = new ArrayList<Motivosmvrs>();
        modificarMotivoMvrs = new ArrayList<Motivosmvrs>();
        borrarMotivoMvrs = new ArrayList<Motivosmvrs>();
        permitirIndex = true;
        editarMotivosMvrs = new Motivosmvrs();
        nuevoMotivoMvr = new Motivosmvrs();
        duplicarMotivosMvrs = new Motivosmvrs();
        tamano = 330;
        guardado = true;
        motivoMvrSeleccionada = null;
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
         System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "motivomvr";
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
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
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
            administrarMotivosMvrs.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaAnterior(String pagina) {
        paginaAnterior = pagina;
    }

    public String redireccionarPaginaAnterior() {
        return paginaAnterior;
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

    public void cambiarIndice(Motivosmvrs mmvr, int celda) {
        motivoMvrSeleccionada = mmvr;
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = motivoMvrSeleccionada.getCodigo();
                System.out.println("BACKUPCODIGO " + backUpCodigo);
            }
            if (cualCelda == 1) {
                backUpDescripcion = motivoMvrSeleccionada.getNombre();
                System.out.println("BACKUPDESCRIPCION " + backUpDescripcion);
            }
        }
    }

    public void asignarIndex(Motivosmvrs mmvr, int LND, int dig) {
        motivoMvrSeleccionada = mmvr;
        tipoActualizacion = LND;
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }

        borrarMotivoMvrs.clear();
        crearMotivoMvrs.clear();
        modificarMotivoMvrs.clear();
        motivoMvrSeleccionada = null;
        k = 0;
        listMotivosMvrs = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }

        borrarMotivoMvrs.clear();
        crearMotivoMvrs.clear();
        modificarMotivoMvrs.clear();
        motivoMvrSeleccionada = null;
        k = 0;
        listMotivosMvrs = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 330;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            bandera = 0;
            filtrarMotivosMvrs = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosMvrs(Motivosmvrs mmvr) {
        motivoMvrSeleccionada = mmvr;
        if (!crearMotivoMvrs.contains(motivoMvrSeleccionada)) {
            if (modificarMotivoMvrs.isEmpty()) {
                modificarMotivoMvrs.add(motivoMvrSeleccionada);
            } else if (!modificarMotivoMvrs.contains(motivoMvrSeleccionada)) {
                modificarMotivoMvrs.add(motivoMvrSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
    }

    public void borrarMotivosMvrs() {
        if (motivoMvrSeleccionada != null) {
            System.out.println("Entro a borrarNormasLaborales");
            if (!modificarMotivoMvrs.isEmpty() && modificarMotivoMvrs.contains(motivoMvrSeleccionada)) {
                modificarMotivoMvrs.remove(motivoMvrSeleccionada);
                borrarMotivoMvrs.add(motivoMvrSeleccionada);
            } else if (!crearMotivoMvrs.isEmpty() && crearMotivoMvrs.contains(motivoMvrSeleccionada)) {
                crearMotivoMvrs.remove(motivoMvrSeleccionada);
            } else {
                borrarMotivoMvrs.add(motivoMvrSeleccionada);
            }
            listMotivosMvrs.remove(motivoMvrSeleccionada);
            if (tipoLista == 1) {
                filtrarMotivosMvrs.remove(motivoMvrSeleccionada);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            motivoMvrSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    /* public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     try {
     borradoVC = administrarNormasLaborales.verificarBorradoVNE(listNormasLaborales.get(index).getSecuencia());
     if (borradoVC.intValue() == 0) {
     System.out.println("Borrado==0");
     borrarNormasLaborales();
     }
     if (borradoVC.intValue() != 0) {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
     motivoMvrSeleccionada = null;
     borradoVC = new Long(-1);
     }

     } catch (Exception e) {
     System.err.println("ERROR ControlNormasLaborales verificarBorrado ERROR " + e);
     }
     }
     */
    public void guardarMotivosMvrs() {
        if (guardado == false) {
            System.out.println("Realizando Motivos Mvrs");
            if (!borrarMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.borrarMotivosMvrs(borrarMotivoMvrs);
                registrosBorrados = borrarMotivoMvrs.size();
                borrarMotivoMvrs.clear();
            }
            if (!crearMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.crearMotivosMvrs(crearMotivoMvrs);
                crearMotivoMvrs.clear();
            }
            if (!modificarMotivoMvrs.isEmpty()) {
                administrarMotivosMvrs.modificarMotivosMvrs(modificarMotivoMvrs);
                modificarMotivoMvrs.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosMvrs = null;
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        if (motivoMvrSeleccionada != null) {
            editarMotivosMvrs = motivoMvrSeleccionada;

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
    }

    public void agregarNuevoMotivoMvrs() {
        System.out.println("Agregar Norma Laboral");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoMvr.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoMvr.getCodigo());

            for (int x = 0; x < listMotivosMvrs.size(); x++) {
                if (listMotivosMvrs.get(x).getCodigo() == nuevoMotivoMvr.getCodigo()) {
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
        if (nuevoMotivoMvr.getNombre() == (null) || nuevoMotivoMvr.getNombre().equals(" ") || nuevoMotivoMvr.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
                bandera = 0;
                filtrarMotivosMvrs = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoMvr.setSecuencia(l);
            crearMotivoMvrs.add(nuevoMotivoMvr);
            listMotivosMvrs.add(nuevoMotivoMvr);
            motivoMvrSeleccionada = listMotivosMvrs.get(listMotivosMvrs.indexOf(nuevoMotivoMvr));

            nuevoMotivoMvr = new Motivosmvrs();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoMvrs').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoMvrs() {
        System.out.println("limpiarNuevoMotivoMvrs");
        nuevoMotivoMvr = new Motivosmvrs();

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivoMvrs() {
        System.out.println("duplicarMotivoMvr");
        if (motivoMvrSeleccionada != null) {
            duplicarMotivosMvrs = new Motivosmvrs();
            k++;
            l = BigInteger.valueOf(k);

            duplicarMotivosMvrs.setSecuencia(l);
            duplicarMotivosMvrs.setCodigo(motivoMvrSeleccionada.getCodigo());
            duplicarMotivosMvrs.setNombre(motivoMvrSeleccionada.getNombre());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoMvr");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosMvr').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLMOTIVOMVRS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivosMvrs.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarMotivosMvrs.getNombre());

        if (duplicarMotivosMvrs.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosMvrs.size(); x++) {
                if (listMotivosMvrs.get(x).getCodigo() == duplicarMotivosMvrs.getCodigo()) {
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
        if (duplicarMotivosMvrs.getNombre() == (null) || duplicarMotivosMvrs.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 2) {
            System.out.println("Datos Duplicando: " + duplicarMotivosMvrs.getSecuencia() + "  " + duplicarMotivosMvrs.getCodigo());
            if (crearMotivoMvrs.contains(duplicarMotivosMvrs)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosMvrs.add(duplicarMotivosMvrs);
            crearMotivoMvrs.add(duplicarMotivosMvrs);
            motivoMvrSeleccionada = listMotivosMvrs.get(listMotivosMvrs.indexOf(duplicarMotivosMvrs));
            RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoMvr:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoMvr");
                bandera = 0;
                filtrarMotivosMvrs = null;
                tipoLista = 0;
            }
            duplicarMotivosMvrs = new Motivosmvrs();
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosMvr').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosMvrs() {
        duplicarMotivosMvrs = new Motivosmvrs();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoMvrExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivoMvr", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoMvrExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivoMvr", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (motivoMvrSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(motivoMvrSeleccionada.getSecuencia(), "MOTIVOSMVRS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla(
                "MOTIVOSMVRS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

//-----------------------------------------------------------------------------   
    public List<Motivosmvrs> getListMotivosMvrs() {
        if (listMotivosMvrs == null) {
            listMotivosMvrs = administrarMotivosMvrs.consultarMotivosMvrs();
        }
        return listMotivosMvrs;
    }

    public void setListMotivosMvrs(List<Motivosmvrs> listMotivosMvrs) {
        this.listMotivosMvrs = listMotivosMvrs;
    }

    public List<Motivosmvrs> getFiltrarMotivosMvrs() {
        return filtrarMotivosMvrs;
    }

    public void setFiltrarMotivosMvrs(List<Motivosmvrs> filtrarMotivosMvrs) {
        this.filtrarMotivosMvrs = filtrarMotivosMvrs;
    }

    public Motivosmvrs getDuplicarMotivosMvrs() {
        return duplicarMotivosMvrs;
    }

    public void setDuplicarMotivosMvrs(Motivosmvrs duplicarMotivosMvrs) {
        this.duplicarMotivosMvrs = duplicarMotivosMvrs;
    }

    public Motivosmvrs getEditarMotivosMvrs() {
        return editarMotivosMvrs;
    }

    public void setEditarMotivosMvrs(Motivosmvrs editarMotivosMvrs) {
        this.editarMotivosMvrs = editarMotivosMvrs;
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

    public Motivosmvrs getNuevoMotivoMvr() {
        return nuevoMotivoMvr;
    }

    public void setNuevoMotivoMvr(Motivosmvrs nuevoMotivoMvr) {
        this.nuevoMotivoMvr = nuevoMotivoMvr;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Motivosmvrs getMotivoMvrSeleccionada() {
        return motivoMvrSeleccionada;
    }

    public void setMotivoMvrSeleccionada(Motivosmvrs motivoMvrSeleccionada) {
        this.motivoMvrSeleccionada = motivoMvrSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivoMvr");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
