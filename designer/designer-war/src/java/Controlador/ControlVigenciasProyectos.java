/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.VigenciasProyectos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProyectosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasProyectosInterface;
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

@ManagedBean
@SessionScoped
public class ControlVigenciasProyectos implements Serializable {

    @EJB
    AdministrarVigenciasProyectosInterface administrarVigenciasProyectos;
    @EJB
    AdministrarProyectosInterface administrarProyectos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    //LISTA VIGENCIAS FORMALES
    private List<VigenciasProyectos> listaVigenciasProyectos;
    private List<VigenciasProyectos> filtradosListaVigenciasProyectos;
    private VigenciasProyectos vigenciaProyectoSeleccionado;
    //Columnas Tabla Vigencias Proyectos
    private Column vPFechasIniciales, vPFechasFinales, vPProyectos, vPPryRoles, vPCargos, vPCantidadPersonas;
    //L.O.V Proyectos
    private List<Proyectos> listaProyectos;
    private List<Proyectos> filtradoslistaProyectos;
    private Proyectos seleccionProyectos;
    //L.O.V PRYROLES
    private List<PryRoles> listaPryRoles;
    private List<PryRoles> filtradoslistaPryRoles;
    private PryRoles seleccionPryRoles;
    //L.O.V CARGOS
    private List<Cargos> listaCargos;
    private List<Cargos> filtradoslistaCargos;
    private Cargos seleccionCargos;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private VigenciasProyectos editarVigenciasProyectos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //Crear Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosCrear;
    public VigenciasProyectos nuevaVigenciaProyectos;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosModificar;
    private boolean guardado, guardarOk;
    //Borrar Vigencias Proyectos
    private List<VigenciasProyectos> listaVigenciasProyectosBorrar;
    //AUTOCOMPLETAR
    private String FechaInicial, Proyecto, PryRol, Cargo;
    //Duplicar
    private VigenciasProyectos duplicarVigenciaProyectos;
    //Consultas sobre detalles
    private Proyectos proyectoParametro;
    private String clienteParametroProyecto;
    private String plataformaParametroProyecto;
    private String altoTabla;
    private String infoRegistro, infoRegistroRol, infoRegistroCargo, infoRegistroProyecto;
    private Date fechaParametro;
    private String paginaAnterior;
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciasProyectos() {
        permitirIndex = true;
        aceptar = true;
        listaVigenciasProyectosBorrar = new ArrayList<VigenciasProyectos>();
        listaVigenciasProyectosCrear = new ArrayList<VigenciasProyectos>();
        listaVigenciasProyectosModificar = new ArrayList<VigenciasProyectos>();
        guardado = true;
        listaProyectos = new ArrayList<Proyectos>();
        listaPryRoles = new ArrayList<PryRoles>();
        listaCargos = new ArrayList<Cargos>();
        vigenciaProyectoSeleccionado = null;
        //Crear VC
        nuevaVigenciaProyectos = new VigenciasProyectos();
        nuevaVigenciaProyectos.setProyecto(new Proyectos());
        nuevaVigenciaProyectos.setPryRol(new PryRoles());
        nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
        duplicarVigenciaProyectos = new VigenciasProyectos();
        duplicarVigenciaProyectos.setProyecto(new Proyectos());
        duplicarVigenciaProyectos.setPryRol(new PryRoles());
        duplicarVigenciaProyectos.setPryCargoproyecto(new Cargos());
        proyectoParametro = new Proyectos();
        altoTabla = "115";
        plataformaParametroProyecto = null;
        listaProyectos = null;
        listaPryRoles = null;
        listaCargos = null;
        paginaAnterior = "nominaf";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasProyectos.obtenerConexion(ses.getId());
            administrarProyectos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
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
            String pagActual = "vigenciasproyectos";
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

    public void recibirEmpleado(BigInteger secEmpleado) {
        secuenciaEmpleado = secEmpleado;
        empleado = null;
        getEmpleado();
        listaVigenciasProyectos = null;
        getListaVigenciasProyectos();
        aceptar = true;
        if (listaVigenciasProyectos != null) {
            if (!listaVigenciasProyectos.isEmpty()) {
                vigenciaProyectoSeleccionado = listaVigenciasProyectos.get(0);
            }
        }

    }

    //AUTOCOMPLETAR
    public void modificarVigenciasProyectos(VigenciasProyectos vigencia, String confirmarCambio, String valorConfirmar) {
        vigenciaProyectoSeleccionado = vigencia;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {

                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {

                if (listaVigenciasProyectosModificar.isEmpty()) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        } else if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.getProyecto().setNombreproyecto(Proyecto);
            } else {
                vigenciaProyectoSeleccionado.getProyecto().setNombreproyecto(Proyecto);
            }

            for (int i = 0; i < listaProyectos.size(); i++) {
                if (listaProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaProyectoSeleccionado.setProyecto(listaProyectos.get(indiceUnicoElemento));
                } else {
                    vigenciaProyectoSeleccionado.setProyecto(listaProyectos.get(indiceUnicoElemento));
                }
                listaProyectos.clear();
                getListaProyectos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
                RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRYROL")) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.getPryRol().setDescripcion(PryRol);
            } else {
                vigenciaProyectoSeleccionado.getPryRol().setDescripcion(PryRol);
            }
            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaProyectoSeleccionado.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                } else {
                    vigenciaProyectoSeleccionado.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                }
                listaPryRoles.clear();
                getListaPryRoles();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
                RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.getPryCargoproyecto().setNombre(Cargo);
            } else {
                vigenciaProyectoSeleccionado.getPryCargoproyecto().setNombre(Cargo);
            }
            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaProyectoSeleccionado.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                } else {
                    vigenciaProyectoSeleccionado.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {

                if (listaVigenciasProyectosModificar.isEmpty()) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        ////actualizar campos de detalles proyecto
        RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
        RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
        RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
        RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
        RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
        RequestContext.getCurrentInstance().update("formularioDetalles:monto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
        RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");
    }

//Ubicacion Celda.
    public void cambiarIndice(VigenciasProyectos vigencia, int celda) {
        if (permitirIndex == true) {
            vigenciaProyectoSeleccionado = vigencia;
            cualCelda = celda;
            getProyectoParametro();
            vigenciaProyectoSeleccionado.getSecuencia();
            if (cualCelda == 2) {
                Proyecto = vigenciaProyectoSeleccionado.getProyecto().getNombreproyecto();
            } else if (cualCelda == 3) {
                PryRol = vigenciaProyectoSeleccionado.getPryRol().getDescripcion();
            } else if (cualCelda == 4) {
                Cargo = vigenciaProyectoSeleccionado.getPryCargoproyecto().getNombre();
            } else if (cualCelda == 5) {
                vigenciaProyectoSeleccionado.getCantidadpersonaacargo();
            }

        }
    }

    public void asignarIndex(VigenciasProyectos vigencia, int dlg, int LND) {
        vigenciaProyectoSeleccionado = vigencia;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            listaProyectos = null;
            getListaProyectos();
            contarRegistroProyecto();
            RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
            RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').show()");
        } else if (dlg == 1) {
            listaPryRoles = null;
            getListaPryRoles();
            contarRegistroRol();
            RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
            RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').show()");
        } else if (dlg == 2) {
            listaCargos = null;
            getListaCargos();
            contarRegistroCargo();
            RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
        }
    }

    public void activarCtrlF11() {

        FacesContext c = FacesContext.getCurrentInstance();

        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("width: 85% !important;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("width: 85% !important;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("width: 85% !important;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("width: 85% !important;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("width: 85% !important;");
            altoTabla = "95";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";

            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasProyectos auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = vigenciaProyectoSeleccionado;
            }
            if (tipoLista == 1) {
                auxiliar = vigenciaProyectoSeleccionado;
            }
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevaVigenciaProyectos.getFechafinal() != null) {
                if (nuevaVigenciaProyectos.getFechainicial().after(fechaParametro) && nuevaVigenciaProyectos.getFechainicial().before(nuevaVigenciaProyectos.getFechafinal())) {
                    retorno = true;
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                    retorno = false;
                }
            } else if (nuevaVigenciaProyectos.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarVigenciaProyectos.getFechafinal() != null) {
                if (duplicarVigenciaProyectos.getFechainicial().after(fechaParametro) && duplicarVigenciaProyectos.getFechainicial().before(duplicarVigenciaProyectos.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            } else if (duplicarVigenciaProyectos.getFechainicial().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarVigenciaDeporte(VigenciasProyectos vigenciaproyecto) {
        vigenciaProyectoSeleccionado = vigenciaproyecto;
        if (tipoLista == 0) {
            if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                if (listaVigenciasProyectosModificar.isEmpty()) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                    listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
            if (listaVigenciasProyectosModificar.isEmpty()) {
                listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
            } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void modificarFechas(VigenciasProyectos vigenciaproyecto, int c) {
        VigenciasProyectos auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = vigenciaProyectoSeleccionado;
        }
        if (tipoLista == 1) {
            auxiliar = vigenciaProyectoSeleccionado;
        }
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            if (auxiliar.getFechafinal() == null) {
                retorno = true;
            }
            if (auxiliar.getFechafinal() != null) {
                vigenciaProyectoSeleccionado = vigenciaproyecto;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndice(vigenciaproyecto, c);
                modificarVigenciaDeporte(vigenciaproyecto);
            } else {
                if (tipoLista == 0) {
                    vigenciaProyectoSeleccionado.setFechafinal(vigenciaProyectoSeleccionado.getFechafinal());
                    vigenciaProyectoSeleccionado.setFechainicial(vigenciaProyectoSeleccionado.getFechainicial());
                }
                if (tipoLista == 1) {
                    vigenciaProyectoSeleccionado.setFechafinal(vigenciaProyectoSeleccionado.getFechafinal());
                    vigenciaProyectoSeleccionado.setFechainicial(vigenciaProyectoSeleccionado.getFechainicial());

                }
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
                RequestContext.getCurrentInstance().execute("PF('form:errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.setFechainicial(vigenciaProyectoSeleccionado.getFechafinal());
            }
            if (tipoLista == 1) {
                vigenciaProyectoSeleccionado.setFechainicial(vigenciaProyectoSeleccionado.getFechafinal());

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (vigenciaProyectoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                listaProyectos = null;
                getListaProyectos();
                RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
                RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                listaPryRoles = null;
                getListaPryRoles();
                RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
                RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 4) {
                listaCargos = null;
                getListaCargos();
                RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasProyectosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasProyectosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasProyectosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasProyectosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //LIMPIAR NUEVO REGISTRO VIGENCIA PROYECTO
    public void limpiarNuevaVigenciaProyecto() {
        nuevaVigenciaProyectos = new VigenciasProyectos();
        nuevaVigenciaProyectos.setProyecto(new Proyectos());
        nuevaVigenciaProyectos.setPryRol(new PryRoles());
        nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("PROYECTO")) {
            if (tipoNuevo == 1) {
                Proyecto = nuevaVigenciaProyectos.getProyecto().getNombreproyecto();
            } else if (tipoNuevo == 2) {
                Proyecto = duplicarVigenciaProyectos.getProyecto().getNombreproyecto();
            }
        } else if (Campo.equals("PRYROL")) {
            if (tipoNuevo == 1) {
                PryRol = nuevaVigenciaProyectos.getPryRol().getDescripcion();
            } else if (tipoNuevo == 2) {
                PryRol = duplicarVigenciaProyectos.getPryRol().getDescripcion();
            }
        } else if (Campo.equals("CARGO")) {
            if (tipoNuevo == 1) {
                Cargo = nuevaVigenciaProyectos.getPryCargoproyecto().getNombre();
            } else if (tipoNuevo == 2) {
                Cargo = duplicarVigenciaProyectos.getPryCargoproyecto().getNombre();
            }
        }
    }

    //CREAR VIGENCIA PROYECTO
    public void agregarNuevaVigenciaProyecto() {
        int pasa = 0;
        mensajeValidacion = " ";

        if (nuevaVigenciaProyectos.getFechainicial() == null) {
            System.out.println("Entro a Fecha");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getProyecto().getNombreproyecto().equals(" ") || nuevaVigenciaProyectos.getProyecto().getNombreproyecto().equals("")) {
            System.out.println("Entro a Proyecto");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getPryRol().getDescripcion().equals(" ") || nuevaVigenciaProyectos.getPryRol().getDescripcion().equals("")) {
            System.out.println("Entro a Rol");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaVigenciaProyectos.getPryCargoproyecto().getNombre().equals(" ") || nuevaVigenciaProyectos.getPryCargoproyecto().getNombre().equals("")) {
            System.out.println("Entro a Cargo");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (pasa == 0) {
            if (bandera == 1) {
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                FacesContext c = FacesContext.getCurrentInstance();

                vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
                vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
                vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
                vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
                vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
                vPProyectos.setFilterStyle("display: none; visibility: hidden;");
                vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
                vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
                vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
                vPCargos.setFilterStyle("display: none; visibility: hidden;");
                vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
                vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "115";
                RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
                bandera = 0;
                filtradosListaVigenciasProyectos = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS PROYECTOS.
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaProyectos.setSecuencia(l);
            nuevaVigenciaProyectos.setEmpleado(empleado);
            if (nuevaVigenciaProyectos.getProyecto().getSecuencia() == null) {
                nuevaVigenciaProyectos.setProyecto(null);
            }
            listaVigenciasProyectosCrear.add(nuevaVigenciaProyectos);
            listaVigenciasProyectos.add(nuevaVigenciaProyectos);
            vigenciaProyectoSeleccionado = nuevaVigenciaProyectos;
            nuevaVigenciaProyectos = new VigenciasProyectos();
            nuevaVigenciaProyectos.setPryRol(new PryRoles());
            nuevaVigenciaProyectos.setProyecto(new Proyectos());
            nuevaVigenciaProyectos.setPryCargoproyecto(new Cargos());
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");

            ////actualizar campos de detalles proyecto
            RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
            RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
            RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
            RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
            RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
            RequestContext.getCurrentInstance().update("formularioDetalles:monto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
            RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaProyecto').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaVigenciaProyecto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaVigenciaProyecto').show()");
        }
    }

    //DUPLICAR VIGENCIA PROYECTO
    public void duplicarVP() {
        if (vigenciaProyectoSeleccionado != null) {
            duplicarVigenciaProyectos = new VigenciasProyectos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciaProyectos.setSecuencia(l);
                duplicarVigenciaProyectos.setEmpleado(vigenciaProyectoSeleccionado.getEmpleado());
                duplicarVigenciaProyectos.setFechainicial(vigenciaProyectoSeleccionado.getFechainicial());
                duplicarVigenciaProyectos.setFechafinal(vigenciaProyectoSeleccionado.getFechafinal());
                duplicarVigenciaProyectos.setProyecto(vigenciaProyectoSeleccionado.getProyecto());
                duplicarVigenciaProyectos.setPryRol(vigenciaProyectoSeleccionado.getPryRol());
                duplicarVigenciaProyectos.setPryCargoproyecto(vigenciaProyectoSeleccionado.getPryCargoproyecto());
                duplicarVigenciaProyectos.setCantidadpersonaacargo(vigenciaProyectoSeleccionado.getCantidadpersonaacargo());
            }
            if (tipoLista == 1) {
                duplicarVigenciaProyectos.setSecuencia(l);
                duplicarVigenciaProyectos.setEmpleado(vigenciaProyectoSeleccionado.getEmpleado());
                duplicarVigenciaProyectos.setFechainicial(vigenciaProyectoSeleccionado.getFechainicial());
                duplicarVigenciaProyectos.setFechafinal(vigenciaProyectoSeleccionado.getFechafinal());
                duplicarVigenciaProyectos.setProyecto(vigenciaProyectoSeleccionado.getProyecto());
                duplicarVigenciaProyectos.setPryRol(vigenciaProyectoSeleccionado.getPryRol());
                duplicarVigenciaProyectos.setPryCargoproyecto(vigenciaProyectoSeleccionado.getPryCargoproyecto());
                duplicarVigenciaProyectos.setCantidadpersonaacargo(vigenciaProyectoSeleccionado.getCantidadpersonaacargo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaProyecto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaProyecto').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        listaVigenciasProyectos.add(duplicarVigenciaProyectos);
        listaVigenciasProyectosCrear.add(duplicarVigenciaProyectos);
        vigenciaProyectoSeleccionado = duplicarVigenciaProyectos;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
        RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
        RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
        RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
        RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
        RequestContext.getCurrentInstance().update("formularioDetalles:monto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
        RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
            RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
            RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
            RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
            RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
            RequestContext.getCurrentInstance().update("formularioDetalles:monto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
            RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }
        duplicarVigenciaProyectos = new VigenciasProyectos();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaProyecto");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaProyecto').hide()");
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia Proyecto
     */
    public void limpiarduplicarVigenciaProyectos() {
        duplicarVigenciaProyectos = new VigenciasProyectos();
        duplicarVigenciaProyectos.setProyecto(new Proyectos());
        duplicarVigenciaProyectos.setPryCargoproyecto(new Cargos());
        duplicarVigenciaProyectos.setPryRol(new PryRoles());
    }

    //BORRAR VIGENCIA PROYECTO
    public void borrarVigenciasProyectos() {

        if (vigenciaProyectoSeleccionado != null) {
            if (!listaVigenciasProyectosModificar.isEmpty() && listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                listaVigenciasProyectosModificar.remove(listaVigenciasProyectosModificar.indexOf(vigenciaProyectoSeleccionado));
                listaVigenciasProyectosBorrar.add(vigenciaProyectoSeleccionado);
            } else if (!listaVigenciasProyectosCrear.isEmpty() && listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                listaVigenciasProyectosCrear.remove(listaVigenciasProyectosCrear.indexOf(vigenciaProyectoSeleccionado));
            } else {
                listaVigenciasProyectosBorrar.add(vigenciaProyectoSeleccionado);
            }
            listaVigenciasProyectos.remove(vigenciaProyectoSeleccionado);

            if (tipoLista == 1) {
                filtradosListaVigenciasProyectos.remove(vigenciaProyectoSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");

            RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
            RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
            RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
            RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
            RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
            RequestContext.getCurrentInstance().update("formularioDetalles:monto");
            RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
            RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            vigenciaProyectoSeleccionado = null;
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();

            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }

        listaVigenciasProyectosBorrar.clear();
        listaVigenciasProyectosCrear.clear();
        listaVigenciasProyectosModificar.clear();
        vigenciaProyectoSeleccionado = null;
//        k = 0;
        listaVigenciasProyectos = null;
        listaProyectos = null;
        listaCargos = null;
        listaPryRoles = null;
        guardado = true;
        permitirIndex = true;
        proyectoParametro.setNombreproyecto("");
        proyectoParametro.setCodigo("");
        proyectoParametro.setMonto(null);
        proyectoParametro.setFechainicial(null);
        proyectoParametro.setFechafinal(null);
        proyectoParametro.getTipomoneda().setNombre("");
        proyectoParametro.setCantidadpersonas(null);
        proyectoParametro.getPryCliente().setNombre("");
        proyectoParametro.getPryCliente().setDireccion("");
        proyectoParametro.getPryCliente().setTelefono("");
        proyectoParametro.getPryCliente().setContacto("");
        proyectoParametro.getPryPlataforma().setDescripcion("");
        proyectoParametro.getPryPlataforma().setObservacion("");
        proyectoParametro.setDescripcionproyecto(" ");
        RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
        RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
        RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
        RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
        RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
        RequestContext.getCurrentInstance().update("formularioDetalles:monto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
        RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
    }

    public void salir() {

        if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            FacesContext c = FacesContext.getCurrentInstance();

            vPFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasIniciales");
            vPFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            vPFechasFinales = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPFechasFinales");
            vPFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            vPProyectos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPProyectos");
            vPProyectos.setFilterStyle("display: none; visibility: hidden;");
            vPPryRoles = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPPryRoles");
            vPPryRoles.setFilterStyle("display: none; visibility: hidden;");
            vPCargos = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCargos");
            vPCargos.setFilterStyle("display: none; visibility: hidden;");
            vPCantidadPersonas = (Column) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona:vPCantidadPersonas");
            vPCantidadPersonas.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            bandera = 0;
            filtradosListaVigenciasProyectos = null;
            tipoLista = 0;
        }
        proyectoParametro.setNombreproyecto("");
        proyectoParametro.setCodigo("");
        proyectoParametro.setMonto(null);
        proyectoParametro.setFechainicial(null);
        proyectoParametro.setFechafinal(null);
        proyectoParametro.getTipomoneda().setNombre("");
        proyectoParametro.setCantidadpersonas(null);
        proyectoParametro.getPryCliente().setNombre("");
        proyectoParametro.getPryCliente().setDireccion("");
        proyectoParametro.getPryCliente().setContacto("");
        proyectoParametro.getPryCliente().setTelefono("");
        proyectoParametro.getPryPlataforma().setDescripcion("");
        proyectoParametro.getPryPlataforma().setObservacion("");
        proyectoParametro.setDescripcionproyecto(" ");
        RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
        RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
        RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
        RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
        RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
        RequestContext.getCurrentInstance().update("formularioDetalles:monto");
        RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
        RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");

        listaVigenciasProyectosBorrar.clear();
        listaVigenciasProyectosCrear.clear();
        listaVigenciasProyectosModificar.clear();
        vigenciaProyectoSeleccionado = null;
        listaProyectos = null;
        listaCargos = null;
        listaPryRoles = null;
        //  k = 0;
        listaVigenciasProyectos = null;
        guardado = true;
        permitirIndex = true;

    }

    //GUARDAR
    public void guardarCambiosVigenciasProyectos() {
        if (guardado == false) {
            if (!listaVigenciasProyectosBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasProyectosBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaVigenciasProyectosBorrar.get(i).getProyecto().getSecuencia() == null) {
                        listaVigenciasProyectosBorrar.get(i).setProyecto(null);
                        administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                    } else if (listaVigenciasProyectosBorrar.get(i).getPryRol().getSecuencia() == null) {
                        listaVigenciasProyectosBorrar.get(i).setPryRol(null);
                        administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                    } else {
                        administrarVigenciasProyectos.borrarVigenciaProyecto(listaVigenciasProyectosBorrar.get(i));
                    }
                }
                listaVigenciasProyectosBorrar.clear();
            }

            if (!listaVigenciasProyectosCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasProyectosCrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listaVigenciasProyectosCrear.get(i).getProyecto().getSecuencia() == null) {
                        listaVigenciasProyectosCrear.get(i).setProyecto(null);
                        administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));
                    } else if (listaVigenciasProyectosCrear.get(i).getPryRol().getSecuencia() == null) {
                        listaVigenciasProyectosCrear.get(i).setPryRol(null);
                        administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));
                    } else {
                        administrarVigenciasProyectos.crearVigenciaProyecto(listaVigenciasProyectosCrear.get(i));

                    }
                }
                listaVigenciasProyectosCrear.clear();
            }
            if (!listaVigenciasProyectosModificar.isEmpty()) {
                administrarVigenciasProyectos.modificarVigenciaProyecto(listaVigenciasProyectosModificar);
                listaVigenciasProyectosModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaVigenciasProyectos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PROYECTO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getProyecto().setNombreproyecto(Proyecto);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getProyecto().setNombreproyecto(Proyecto);
            }
            for (int i = 0; i < listaProyectos.size(); i++) {
                if (listaProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setProyecto(listaProyectos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProyecto");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setProyecto(listaProyectos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyecto");
                }
                listaProyectos.clear();
                getListaProyectos();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
                RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProyecto");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProyecto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRYROL")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getPryRol().setDescripcion(PryRol);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getPryRol().setDescripcion(PryRol);
            }

            for (int i = 0; i < listaPryRoles.size(); i++) {
                if (listaPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPryRol");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setPryRol(listaPryRoles.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPryRol");
                }
                listaPryRoles.clear();
                getListaPryRoles();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
                RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPryRol");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPryRol");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaProyectos.getPryCargoproyecto().setNombre(Cargo);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaProyectos.getPryCargoproyecto().setNombre(Cargo);
            }

            for (int i = 0; i < listaCargos.size(); i++) {
                if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaProyectos.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaProyectos.setPryCargoproyecto(listaCargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                }
            }

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (vigenciaProyectoSeleccionado != null) {
            editarVigenciasProyectos = vigenciaProyectoSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProyectos");
                RequestContext.getCurrentInstance().execute("PF('editarProyectos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPryRoles");
                RequestContext.getCurrentInstance().execute("PF('editarPryRoles').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCargos");
                RequestContext.getCurrentInstance().execute("PF('editarCargos').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCantidadPersonas");
                RequestContext.getCurrentInstance().execute("PF('editarCantidadPersonas').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void actualizarProyectos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.setProyecto(seleccionProyectos);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            } else {
                vigenciaProyectoSeleccionado.setProyecto(seleccionProyectos);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setProyecto(seleccionProyectos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setProyecto(seleccionProyectos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaProyecto");
        }
        filtradoslistaProyectos = null;
        seleccionProyectos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVProyectos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProyectos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProyectos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPr");
    }

    public void actualizarPryRoles() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.setPryRol(seleccionPryRoles);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            } else {
                vigenciaProyectoSeleccionado.setPryRol(seleccionPryRoles);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setPryRol(seleccionPryRoles);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setPryRol(seleccionPryRoles);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaProyecto");

        }
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPryRoles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPryRoles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPryRoles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarR");
    }

    public void actualizarCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaProyectoSeleccionado.setPryCargoproyecto(seleccionCargos);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            } else {
                vigenciaProyectoSeleccionado.setPryCargoproyecto(seleccionCargos);
                if (!listaVigenciasProyectosCrear.contains(vigenciaProyectoSeleccionado)) {
                    if (listaVigenciasProyectosModificar.isEmpty()) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    } else if (!listaVigenciasProyectosModificar.contains(vigenciaProyectoSeleccionado)) {
                        listaVigenciasProyectosModificar.add(vigenciaProyectoSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasProyectosPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaProyectos.setPryCargoproyecto(seleccionCargos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaProyecto");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaProyectos.setPryCargoproyecto(seleccionCargos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaProyecto");

        }
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
    }

    public void cancelarCambioProyectos() {
        filtradoslistaProyectos = null;
        seleccionProyectos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVProyectos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProyectos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('proyectosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:proyectosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProyectos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPr");
    }

    public void cancelarCambioPryRoles() {
        filtradoslistaPryRoles = null;
        seleccionPryRoles = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPryRoles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPryRoles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('pryRolesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:pryRolesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPryRoles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarR");
    }

    public void cancelarCambioCargo() {
        filtradoslistaCargos = null;
        seleccionCargos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (vigenciaProyectoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(vigenciaProyectoSeleccionado.getSecuencia(), "VIGENCIASPROYECTOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASPROYECTOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistroProyecto() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroProyecto");
    }

    public void contarRegistroRol() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroRol");
    }

    public void contarRegistroCargo() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCargo");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    ///////////////////Getter And Setter/////////////////////////////
    public BigInteger getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
        this.secuenciaEmpleado = secuenciaEmpleado;
    }

    public Empleados getEmpleado() {
        if (empleado == null) {
            empleado = administrarVigenciasProyectos.encontrarEmpleado(secuenciaEmpleado);
        }

        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<VigenciasProyectos> getListaVigenciasProyectos() {
        if (listaVigenciasProyectos == null) {
            listaVigenciasProyectos = administrarVigenciasProyectos.vigenciasProyectosEmpleado(secuenciaEmpleado);
        }
        return listaVigenciasProyectos;
    }

    public void setListaVigenciasProyectos(List<VigenciasProyectos> listaVigenciasProyectos) {
        this.listaVigenciasProyectos = listaVigenciasProyectos;
    }

    public List<VigenciasProyectos> getFiltradosListaVigenciasProyectos() {
        return filtradosListaVigenciasProyectos;
    }

    public void setFiltradosListaVigenciasProyectos(List<VigenciasProyectos> filtradosListaVigenciasProyectos) {
        this.filtradosListaVigenciasProyectos = filtradosListaVigenciasProyectos;
    }

    public List<Proyectos> getListaProyectos() {
        if (listaProyectos == null) {
            listaProyectos = administrarVigenciasProyectos.lovProyectos();
        }
        return listaProyectos;
    }

    public void setListaProyectos(List<Proyectos> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }

    public List<PryRoles> getListaPryRoles() {
        if (listaPryRoles == null) {
            listaPryRoles = administrarVigenciasProyectos.lovPryRoles();
        }

        return listaPryRoles;
    }

    public void setListaPryRoles(List<PryRoles> listaPryRoles) {
        this.listaPryRoles = listaPryRoles;
    }

    public List<Cargos> getListaCargos() {
        if (listaCargos == null) {
            listaCargos = administrarVigenciasProyectos.lovCargos();
        }
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List<Proyectos> getFiltradoslistaProyectos() {
        return filtradoslistaProyectos;
    }

    public void setFiltradoslistaProyectos(List<Proyectos> filtradoslistaProyectos) {
        this.filtradoslistaProyectos = filtradoslistaProyectos;
    }

    public List<PryRoles> getFiltradoslistaPryRoles() {
        return filtradoslistaPryRoles;
    }

    public void setFiltradoslistaPryRoles(List<PryRoles> filtradoslistaPryRoles) {
        this.filtradoslistaPryRoles = filtradoslistaPryRoles;
    }

    public List<Cargos> getFiltradoslistaCargos() {
        return filtradoslistaCargos;
    }

    public void setFiltradoslistaCargos(List<Cargos> filtradoslistaCargos) {
        this.filtradoslistaCargos = filtradoslistaCargos;
    }

    public Proyectos getSeleccionProyectos() {
        return seleccionProyectos;
    }

    public void setSeleccionProyectos(Proyectos seleccionProyectos) {
        this.seleccionProyectos = seleccionProyectos;
    }

    public PryRoles getSeleccionPryRoles() {
        return seleccionPryRoles;
    }

    public void setSeleccionPryRoles(PryRoles seleccionPryRoles) {
        this.seleccionPryRoles = seleccionPryRoles;
    }

    public Cargos getSeleccionCargos() {
        return seleccionCargos;
    }

    public void setSeleccionCargos(Cargos seleccionCargos) {
        this.seleccionCargos = seleccionCargos;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public VigenciasProyectos getNuevaVigenciaProyectos() {
        return nuevaVigenciaProyectos;
    }

    public void setNuevaVigenciaProyectos(VigenciasProyectos nuevaVigenciaProyectos) {
        this.nuevaVigenciaProyectos = nuevaVigenciaProyectos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasProyectos getDuplicarVigenciaProyectos() {
        return duplicarVigenciaProyectos;
    }

    public void setDuplicarVigenciaProyectos(VigenciasProyectos duplicarVigenciaProyectos) {
        this.duplicarVigenciaProyectos = duplicarVigenciaProyectos;
    }

    public VigenciasProyectos getEditarVigenciasProyectos() {
        return editarVigenciasProyectos;
    }

    public void setEditarVigenciasProyectos(VigenciasProyectos editarVigenciasProyectos) {
        this.editarVigenciasProyectos = editarVigenciasProyectos;
    }

    public Proyectos getProyectoParametro() {
        if (vigenciaProyectoSeleccionado != null) {
            if (!listaVigenciasProyectos.isEmpty()) {
                proyectoParametro = administrarVigenciasProyectos.buscarProyectoPorNombreVigencia(vigenciaProyectoSeleccionado.getProyecto().getNombreproyecto());
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDetalles:nomProyecto");
                RequestContext.getCurrentInstance().update("formularioDetalles:tipoMoneda");
                RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
                RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
                RequestContext.getCurrentInstance().update("formularioDetalles:totalPersonas");
                RequestContext.getCurrentInstance().update("formularioDetalles:detalleProyecto");
                RequestContext.getCurrentInstance().update("formularioDetalles:fechaInicial");
                RequestContext.getCurrentInstance().update("formularioDetalles:monto");
                RequestContext.getCurrentInstance().update("formularioDetalles:fechaFinal");
                RequestContext.getCurrentInstance().update("formularioDetalles:codigoProyecto");
            }
        }
        return proyectoParametro;
    }

    public void setProyectoParametro(Proyectos proyectoParametro) {
        this.proyectoParametro = proyectoParametro;
    }

    public String getClienteParametroProyecto() {
        if (proyectoParametro.getSecuencia() != null) {
            clienteParametroProyecto = proyectoParametro.getPryCliente().getNombre() + "/" + proyectoParametro.getPryCliente().getDireccion() + "/" + proyectoParametro.getPryCliente().getTelefono() + " - " + proyectoParametro.getPryCliente().getContacto();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDetalles:cliente");
        }
        return clienteParametroProyecto;
    }

    public void setClienteParametroProyecto(String clienteParametroProyecto) {
        this.clienteParametroProyecto = clienteParametroProyecto;
    }

    public String getPlataformaParametroProyecto() {
        if (proyectoParametro.getSecuencia() != null) {
            plataformaParametroProyecto = proyectoParametro.getPryPlataforma().getDescripcion() + "-" + proyectoParametro.getPryPlataforma().getObservacion();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDetalles:plataforma");
        }
        return plataformaParametroProyecto;
    }

    public void setPlataformaParametroProyecto(String plataformaParametroProyecto) {
        this.plataformaParametroProyecto = plataformaParametroProyecto;
    }

    public VigenciasProyectos getVigenciaProyectoSeleccionado() {
        return vigenciaProyectoSeleccionado;
    }

    public void setVigenciaProyectoSeleccionado(VigenciasProyectos vigenciaProyectoSeleccionado) {
        this.vigenciaProyectoSeleccionado = vigenciaProyectoSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasProyectosPersona");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroRol() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPryRoles");
        infoRegistroRol = String.valueOf(tabla.getRowCount());
        return infoRegistroRol;
    }

    public void setInfoRegistroRol(String infoRegistroRol) {
        this.infoRegistroRol = infoRegistroRol;
    }

    public String getInfoRegistroCargo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCargos");
        infoRegistroCargo = String.valueOf(tabla.getRowCount());
        return infoRegistroCargo;
    }

    public void setInfoRegistroCargo(String infoRegistroCargo) {
        this.infoRegistroCargo = infoRegistroCargo;
    }

    public String getInfoRegistroProyecto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVProyectos");
        infoRegistroProyecto = String.valueOf(tabla.getRowCount());
        return infoRegistroProyecto;
    }

    public void setInfoRegistroProyecto(String infoRegistroProyecto) {
        this.infoRegistroProyecto = infoRegistroProyecto;
    }

}
