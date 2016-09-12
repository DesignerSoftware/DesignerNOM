package Controlador;

import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEstructurasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@SessionScoped
public class ControlEstructura implements Serializable {

    @EJB
    AdministrarEstructurasInterface administrarEstructuras;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Organigramas> listaOrganigramas;
    private List<Organigramas> filtradoListaOrganigramas;
    private Organigramas organigramaSeleccionado;
    private TreeNode arbolEstructuras;
    private List<Estructuras> estructurasPadre;
    private List<Estructuras> estructurasHijas1;
    private List<Estructuras> estructurasHijas2;
    private List<Estructuras> estructurasHijas3;
    private List<Estructuras> estructurasHijas4;
    private List<Estructuras> estructurasHijas5;
    private List<Estructuras> estructurasHijas6;
    private List<Estructuras> estructurasHijas7;
    private List<Estructuras> estructurasHijas8;
    private List<Estructuras> estructurasHijas9;
    private List<Estructuras> estructurasHijas10;
    //LOVS
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtradoEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresa;
    //
    private int tipoActualizacion;
    private boolean permitirIndex;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla datosOrganigramas
    private Column organigramaFecha, organigramaCodigo, organigramaEmpresa, organigramaNit, organigramaEstado;
    //Otros
    private boolean aceptar;
    //modificar Organigrama
    private List<Organigramas> listOrganigramasModificar;
    private boolean guardado;
    //crear Organigrama
    public Organigramas nuevoOrganigrama;
    private List<Organigramas> listOrganigramasCrear;
    private BigInteger l;
    private int k;
    //borrar Organigrama
    private List<Organigramas> listOrganigramasBorrar;
    //editar celda
    private Organigramas editarOrganigrama;
    private int cualCelda, tipoLista;
    private boolean aceptarEditar;
    //duplicar
    private Organigramas duplicarOrganigrama;
    //AUTOCOMPLETAR
    private String auxEmpresa;
    private Short auxCodigo;
    private Date auxFecha;
    //Redireccionamiento a paginas
    private String paginaAnterior;
    //CODIGO EMPRESA PARA ESTRUCTURAS HIJAS 
    private Short codigoEmpresa;
    //
    private String altoTabla;
    //
    private String infoRegistro;
    //
    private boolean activarLOV;
    public String permitirCambioBotonLov;

    public ControlEstructura() {
        altoTabla = "65";
        arbolEstructuras = null;
        estructurasPadre = new ArrayList<Estructuras>();
        estructurasHijas1 = new ArrayList<Estructuras>();
        estructurasHijas2 = new ArrayList<Estructuras>();
        estructurasHijas3 = new ArrayList<Estructuras>();
        estructurasHijas4 = new ArrayList<Estructuras>();
        estructurasHijas5 = new ArrayList<Estructuras>();
        estructurasHijas6 = new ArrayList<Estructuras>();
        estructurasHijas7 = new ArrayList<Estructuras>();
        estructurasHijas8 = new ArrayList<Estructuras>();
        estructurasHijas9 = new ArrayList<Estructuras>();
        estructurasHijas10 = new ArrayList<Estructuras>();
        //LOVS
        lovEmpresas = null;
        permitirIndex = true;
        listaOrganigramas = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listOrganigramasBorrar = new ArrayList<Organigramas>();
        //crear aficiones
        listOrganigramasCrear = new ArrayList<Organigramas>();
        k = 0;
        //modificar aficiones
        listOrganigramasModificar = new ArrayList<Organigramas>();
        //editar
        editarOrganigrama = new Organigramas();
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear
        nuevoOrganigrama = new Organigramas();
        nuevoOrganigrama.setEmpresa(new Empresas());
        nuevoOrganigrama.setEstado("A");
        organigramaSeleccionado = null;
        activarLOV = true;
        permitirCambioBotonLov = "SIapagarCelda";
        auxCodigo = new Short("0");
        auxFecha = new Date();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEstructuras.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaOrganigramas = null;
        getListaOrganigramas();
        contarRegistrosOrg();
        if (listaOrganigramas != null) {
            if (!listaOrganigramas.isEmpty()) {
                organigramaSeleccionado = listaOrganigramas.get(0);
            }
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    //Ubicacion Celda.
    public void cambiarIndice(Organigramas organigrama, int celda) {
        if (permitirIndex == true) {
            organigramaSeleccionado = organigrama;
            cualCelda = celda;
            if (cualCelda == 2) {
                permitirCambioBotonLov = "NOapagarCelda";
            } else {
                permitirCambioBotonLov = "SoloHacerNull";
            }
            if (cualCelda == 0) {
                auxFecha = organigramaSeleccionado.getFecha();
            }
            auxEmpresa = organigramaSeleccionado.getEmpresa().getNombre();
            auxCodigo = organigramaSeleccionado.getCodigo();
            arbolEstructuras = null;
            codigoEmpresa = organigramaSeleccionado.getEmpresa().getCodigo();
            getArbolEstructuras();
            PrimefacesContextUI.actualizar("form:arbolEstructuras");
        }
    }

    public void cambiarIndiceDefault() {
        System.out.println("cambiarIndiceDefault()");
        if (permitirCambioBotonLov.equals("SoloHacerNull")) {
            anularBotonLOV();
        } else if (permitirCambioBotonLov.equals("SIapagarCelda")) {
            anularBotonLOV();
            cualCelda = -1;
        } else if (permitirCambioBotonLov.equals("NOapagarCelda")) {
            activarBotonLOV();
        }
        permitirCambioBotonLov = "SIapagarCelda";
    }

    //AUTOCOMPLETAR
    public void modificarOrganigrama(Organigramas organigrama, String column, Object valor) {
        organigramaSeleccionado = organigrama;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (column.equalsIgnoreCase("N")) {
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
            PrimefacesContextUI.actualizar("form:datosOrganigramas");
        } else if (column.equalsIgnoreCase("EMPRESA")) {
            organigramaSeleccionado.getEmpresa().setNombre(auxEmpresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
                String empresa = (String) valor;
                if (lovEmpresas.get(i).getNombre().startsWith(empresa.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                organigramaSeleccionado.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                permitirIndex = false;
                contarRegistrosLovEmp(0);
                PrimefacesContextUI.actualizar("form:EmpresasDialogo");
                PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (column.equalsIgnoreCase("COD")) {
            organigramaSeleccionado.setCodigo(auxCodigo);
            Short cod = (Short) valor;
            System.out.println(" modificar cod = " + cod);
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(cod)) {
                    System.out.println(" modificar codigo 1 igual");
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias > 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:validacionCodigoRepetido");
                PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                tipoActualizacion = 0;
            } else {
                organigramaSeleccionado.setCodigo(cod);
                coincidencias = 1;
            }
        } else if (column.equalsIgnoreCase("F")) {
            organigramaSeleccionado.setFecha(auxFecha);
            Date fecha = (Date) valor;
            System.out.println(" modificar fecha = " + fecha);
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getFecha().equals(fecha)
                        && listaOrganigramas.get(i).getEmpresa().equals(organigramaSeleccionado.getEmpresa())) {
                    System.out.println(" modificar fecha 1 igual para Empresa");
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias > 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:validacionFechaRepetida");
                PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                tipoActualizacion = 0;
            } else {
                organigramaSeleccionado.setFecha(fecha);
                coincidencias = 1;
            }
        }
        if (coincidencias == 1) {
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }
        PrimefacesContextUI.actualizar("form:datosOrganigramas");
    }

    //ASIGNAR INDEX PARA LA TABLA PRINCIPAL
    public void asignarIndexTabla(Organigramas organigrama) {
        organigramaSeleccionado = organigrama;
        tipoActualizacion = 0;
        activarBotonLOV();
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:EmpresasDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (tipoAct = NUEVO - DUPLICADO)
    public void asignarIndexDialogos(int tipoAct) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = tipoAct;
        PrimefacesContextUI.actualizar("form:EmpresasDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO
    public void valoresBackupAutocompletar(int tipoNuevo) {
        if (tipoNuevo == 1) {
            auxEmpresa = nuevoOrganigrama.getEmpresa().getNombre();
        } else if (tipoNuevo == 2) {
            auxEmpresa = duplicarOrganigrama.getEmpresa().getNombre();
        }
    }

    public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevoOrganigrama.getEmpresa().setNombre(auxEmpresa);
        } else if (tipoNuevo == 2) {
            duplicarOrganigrama.getEmpresa().setNombre(auxEmpresa);
        }
        for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevoOrganigrama.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresa");
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoNit");
            } else if (tipoNuevo == 2) {
                duplicarOrganigrama.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresa");
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarNit");
            }
            lovEmpresas.clear();
            getLovEmpresas();
        } else {
            PrimefacesContextUI.actualizar("form:EmpresasDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresa");
                PrimefacesContextUI.actualizar("formularioDialogos:nuevoNit");
            } else if (tipoNuevo == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresa");
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarNit");
            }
        }
    }

    //CREAR ORGANIGRAMA
    public void agregarNuevoOrganigrama() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoOrganigrama.getEmpresa().getSecuencia() == null || nuevoOrganigrama.getCodigo() == null) {
            PrimefacesContextUI.actualizar("form:validacioNuevoOrganigrama");
            PrimefacesContextUI.ejecutar("PF('validacioNuevoOrganigrama').show()");
        } else {
            boolean continuar = true;
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(nuevoOrganigrama.getCodigo())) {
                    System.out.println(" nuevo codigo 1 igual");
                    PrimefacesContextUI.actualizar("formularioDialogos:validacionCodigoRepetido");
                    PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                    continuar = false;
                    break;
                } else if (listaOrganigramas.get(i).getFecha().equals(nuevoOrganigrama.getFecha())
                        && listaOrganigramas.get(i).getEmpresa().equals(nuevoOrganigrama.getEmpresa())) {
                    System.out.println(" nuevo fecha 1 igual para Empresa");
                    PrimefacesContextUI.actualizar("formularioDialogos:validacionFechaRepetida");
                    PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                    continuar = false;
                    break;
                }
            }
            if (continuar) {
                //AGREGAR REGISTRO A LA LISTA ORGANIGRAMAS.
                k++;
                l = BigInteger.valueOf(k);
                nuevoOrganigrama.setSecuencia(l);
                listOrganigramasCrear.add(nuevoOrganigrama);
                listaOrganigramas.add(nuevoOrganigrama);
                organigramaSeleccionado = listaOrganigramas.get(listaOrganigramas.indexOf(nuevoOrganigrama));
                contarRegistrosOrg();
                anularBotonLOV();
                nuevoOrganigrama = new Organigramas();
                nuevoOrganigrama.setEmpresa(new Empresas());
                nuevoOrganigrama.setEstado("A");
                if (bandera == 1) {
                    restaurarTabla();
                }
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                PrimefacesContextUI.actualizar("form:datosOrganigramas");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroOrganigramas').hide()");
                arbolEstructuras = null;
                getArbolEstructuras();
                PrimefacesContextUI.actualizar("form:arbolEstructuras");
            }
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoOrganigrama() {
        nuevoOrganigrama = new Organigramas();
        nuevoOrganigrama.setEmpresa(new Empresas());
        nuevoOrganigrama.setEstado("A");
    }

    //DUPLICAR
    public void duplicarOrg() {
        if (organigramaSeleccionado != null) {
            duplicarOrganigrama = new Organigramas();
            duplicarOrganigrama.setFecha(organigramaSeleccionado.getFecha());
            duplicarOrganigrama.setCodigo(organigramaSeleccionado.getCodigo());
            duplicarOrganigrama.setEmpresa(organigramaSeleccionado.getEmpresa());
            duplicarOrganigrama.setEstado(organigramaSeleccionado.getEstado());
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarOrganigrama");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroOrganigramas').show()");
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarOrganigrama.getEmpresa().getSecuencia() == null || duplicarOrganigrama.getCodigo() == null) {
            PrimefacesContextUI.actualizar("form:validacioNuevoOrganigrama");
            PrimefacesContextUI.ejecutar("PF('validacioNuevoOrganigrama').show()");
        } else {
            boolean continuar = true;
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(duplicarOrganigrama.getCodigo())) {
                    System.out.println(" nuevo codigo 1 igual");
                    PrimefacesContextUI.actualizar("formularioDialogos:validacionCodigoRepetido");
                    PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                    continuar = false;
                    break;
                } else if (listaOrganigramas.get(i).getFecha().equals(duplicarOrganigrama.getFecha())
                        && listaOrganigramas.get(i).getEmpresa().equals(duplicarOrganigrama.getEmpresa())) {
                    System.out.println(" nuevo fecha 1 igual para Empresa");
                    PrimefacesContextUI.actualizar("formularioDialogos:validacionFechaRepetida");
                    PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                    continuar = false;
                    break;
                }
            }
            if (continuar) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarOrganigrama.setSecuencia(l);
                listaOrganigramas.add(duplicarOrganigrama);
                listOrganigramasCrear.add(duplicarOrganigrama);
                organigramaSeleccionado = listaOrganigramas.get(listaOrganigramas.indexOf(duplicarOrganigrama));
                contarRegistrosOrg();
                anularBotonLOV();
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                if (bandera == 1) {
                    restaurarTabla();
                }
                PrimefacesContextUI.actualizar("form:datosOrganigramas");
                duplicarOrganigrama = new Organigramas();
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroOrganigramas').hide()");
                contarRegistrosOrg();
                arbolEstructuras = null;
                getArbolEstructuras();
                PrimefacesContextUI.actualizar("form:arbolEstructuras");
            }
        }
    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVTC() {
        duplicarOrganigrama = new Organigramas();
        duplicarOrganigrama.setEmpresa(new Empresas());
    }

    //LOV EMPRESAS
    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            organigramaSeleccionado.setEmpresa(empresaSeleccionada);
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosOrganigramas");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevoOrganigrama.setEmpresa(empresaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoOrganigrama");
        } else if (tipoActualizacion == 2) {
            duplicarOrganigrama.setEmpresa(empresaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarOrganigrama");
        }
        filtradoEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
        PrimefacesContextUI.actualizar("form:EmpresasDialogo");
        PrimefacesContextUI.actualizar("form:lovEmpresas");
        PrimefacesContextUI.actualizar("form:aceptarE");
    }

    public void cancelarEmpresa() {
        filtradoEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
        PrimefacesContextUI.actualizar("form:EmpresasDialogo");
        PrimefacesContextUI.actualizar("form:lovEmpresas");
        PrimefacesContextUI.actualizar("form:aceptarE");
    }

    //GUARDAR
    public void guardarCambiosOrganigramas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listOrganigramasBorrar.isEmpty()) {
                    for (int i = 0; i < listOrganigramasBorrar.size(); i++) {
                        administrarEstructuras.borrarOrganigrama(listOrganigramasBorrar.get(i));
                    }
                    listOrganigramasBorrar.clear();
                }
                if (!listOrganigramasCrear.isEmpty()) {
                    for (int i = 0; i < listOrganigramasCrear.size(); i++) {
                        administrarEstructuras.crearOrganigrama(listOrganigramasCrear.get(i));
                    }
                    listOrganigramasCrear.clear();
                }
                if (!listOrganigramasModificar.isEmpty()) {
                    administrarEstructuras.modificarOrganigrama(listOrganigramasModificar);
                    listOrganigramasModificar.clear();
                }
                listaOrganigramas = null;
                getListaOrganigramas();
                organigramaSeleccionado = null;
                PrimefacesContextUI.actualizar("form:datosOrganigramas");
                contarRegistrosOrg();
                guardado = true;
                permitirIndex = true;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosOrganigramas Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    public void cancelarModificacion() {
        if (bandera == 1) {
            restaurarTabla();
        }
        listOrganigramasBorrar.clear();
        listOrganigramasCrear.clear();
        listOrganigramasModificar.clear();
        organigramaSeleccionado = null;
        k = 0;
        listaOrganigramas = null;
        getListaOrganigramas();
        contarRegistrosOrg();
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        permitirIndex = true;
        PrimefacesContextUI.actualizar("form:datosOrganigramas");
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "45";
            organigramaFecha = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaFecha");
            organigramaFecha.setFilterStyle("width: 85%;");
            organigramaCodigo = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaCodigo");
            organigramaCodigo.setFilterStyle("width: 85%;");
            organigramaEmpresa = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaEmpresa");
            organigramaEmpresa.setFilterStyle("width: 85%;");
            organigramaNit = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaNit");
            organigramaNit.setFilterStyle("width: 85%");
            organigramaEstado = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaEstado");
            organigramaEstado.setFilterStyle("");
            PrimefacesContextUI.actualizar("form:datosOrganigramas");
            bandera = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
    }

    //SALIR
    public void salir() {
        if (bandera == 1) {
            restaurarTabla();
        }
        listOrganigramasBorrar.clear();
        listOrganigramasCrear.clear();
        listOrganigramasModificar.clear();
        organigramaSeleccionado = null;
        k = 0;
        listaOrganigramas = null;
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        listaOrganigramas = null;
        PrimefacesContextUI.actualizar("form:datosOrganigramas");
        arbolEstructuras = null;
        PrimefacesContextUI.actualizar("form:arbolEstructuras");
        permitirIndex = true;
    }

//BORRAR
    public void borrarOrganigrama() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (organigramaSeleccionado != null) {
            if (!listOrganigramasModificar.isEmpty() && listOrganigramasModificar.contains(organigramaSeleccionado)) {
                listOrganigramasModificar.remove(organigramaSeleccionado);
                listOrganigramasBorrar.add(organigramaSeleccionado);
            } else if (!listOrganigramasCrear.isEmpty() && listOrganigramasCrear.contains(organigramaSeleccionado)) {
                listOrganigramasCrear.remove(organigramaSeleccionado);
            } else {
                listOrganigramasBorrar.add(organigramaSeleccionado);
            }
            listaOrganigramas.remove(organigramaSeleccionado);
            if (tipoLista == 1) {
                filtradoListaOrganigramas.remove(organigramaSeleccionado);
            }
            contarRegistrosOrg();
            anularBotonLOV();
            PrimefacesContextUI.actualizar("form:datosOrganigramas");
            organigramaSeleccionado = null;
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            arbolEstructuras = null;
//            getArbolEstructuras();
//            PrimefacesContextUI.actualizar("form:arbolEstructuras");
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (organigramaSeleccionado != null) {
            editarOrganigrama = organigramaSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFecha");
                PrimefacesContextUI.ejecutar("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCod");
                PrimefacesContextUI.ejecutar("PF('editarCod').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEmp");
                PrimefacesContextUI.ejecutar("PF('editarEmp').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarN");
                PrimefacesContextUI.ejecutar("PF('editarN').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEs");
                PrimefacesContextUI.ejecutar("PF('editarEs').show()");
                cualCelda = -1;
            }
            anularBotonLOV();
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (organigramaSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                contarRegistrosLovEmp(0);
                PrimefacesContextUI.actualizar("form:EmpresasDialogo");
                PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOrganigramasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "OrganigramasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOrganigramasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OrganigramasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        organigramaSeleccionado = null;
        anularBotonLOV();
        contarRegistrosOrg();
    }

    public void eventoFiltrarLovEmp() {
        contarRegistrosLovEmp(1);
    }

    public void anularBotonLOV() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void activarBotonLOV() {
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void contarRegistrosOrg() {
        if (tipoLista == 1) {
            infoRegistro = String.valueOf(filtradoListaOrganigramas.size());
        } else if (listaOrganigramas != null) {
            infoRegistro = String.valueOf(listaOrganigramas.size());
        } else {
            infoRegistro = String.valueOf(0);
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void contarRegistrosLovEmp(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroEmpresa = String.valueOf(filtradoEmpresas.size());
        } else if (lovEmpresas != null) {
            infoRegistroEmpresa = String.valueOf(lovEmpresas.size());
        } else {
            infoRegistroEmpresa = String.valueOf(0);
        }
        PrimefacesContextUI.actualizar("form:infoRegistroEmpresa");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void seleccionarEstado(String estadoOrganigrama, int indice, int celda) {
        if (estadoOrganigrama.equals("ACTIVO")) {
            organigramaSeleccionado.setEstado("A");
        } else {
            organigramaSeleccionado.setEstado("I");
        }

        if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
            if (listOrganigramasModificar.isEmpty()) {
                listOrganigramasModificar.add(organigramaSeleccionado);
            } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                listOrganigramasModificar.add(organigramaSeleccionado);
            }
        }
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        PrimefacesContextUI.actualizar("form:datosOrganigramas");
    }

    public void seleccionarEstadoNuevoOrganigrama(String estadoOrganigrama, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoOrganigrama.equals("ACTIVO")) {
                nuevoOrganigrama.setEstado("A");
            } else {
                nuevoOrganigrama.setEstado("I");
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoEstadoOrganigrama");
        } else {
            if (estadoOrganigrama.equals("ACTIVO")) {
                duplicarOrganigrama.setEstado("A");
            } else {
                duplicarOrganigrama.setEstado("I");
            }
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarOrganigrama");
        }

    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (organigramaSeleccionado != null) {
            int result = administrarRastros.obtenerTabla(organigramaSeleccionado.getSecuencia(), "ORGANIGRAMAS");
            if (result == 1) {
                PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
            } else if (result == 3) {
                PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("ORGANIGRAMAS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
    }

    //GETTER AND SETTER
    public TreeNode getArbolEstructuras() {
        if (arbolEstructuras == null) {
            if (listaOrganigramas != null) {
                arbolEstructuras = new DefaultTreeNode("arbolEstructuras", null);
                if (organigramaSeleccionado == null) {
                    estructurasPadre = administrarEstructuras.estructuraPadre(listaOrganigramas.get(0).getCodigo());
                } else {
                    estructurasPadre = administrarEstructuras.estructuraPadre(organigramaSeleccionado.getCodigo());
                }
                if (estructurasPadre != null) {
                    for (int i = 0; i < estructurasPadre.size(); i++) {
                        TreeNode padre = new DefaultTreeNode(estructurasPadre.get(i), arbolEstructuras);
                        estructurasHijas1 = administrarEstructuras.estructurasHijas(estructurasPadre.get(i).getSecuencia(), codigoEmpresa);
                        if (estructurasHijas1 != null) {
                            for (int j = 0; j < estructurasHijas1.size(); j++) {
                                TreeNode hija1 = new DefaultTreeNode(estructurasHijas1.get(j), padre);
                                estructurasHijas2 = administrarEstructuras.estructurasHijas(estructurasHijas1.get(j).getSecuencia(), codigoEmpresa);
                                if (estructurasHijas2 != null) {
                                    for (int k = 0; k < estructurasHijas2.size(); k++) {
                                        TreeNode hija2 = new DefaultTreeNode(estructurasHijas2.get(k), hija1);
                                        estructurasHijas3 = administrarEstructuras.estructurasHijas(estructurasHijas2.get(k).getSecuencia(), codigoEmpresa);
                                        if (estructurasHijas3 != null) {
                                            for (int l = 0; l < estructurasHijas3.size(); l++) {
                                                TreeNode hija3 = new DefaultTreeNode(estructurasHijas3.get(l), hija2);
                                                estructurasHijas4 = administrarEstructuras.estructurasHijas(estructurasHijas3.get(l).getSecuencia(), codigoEmpresa);
                                                if (estructurasHijas4 != null) {
                                                    for (int m = 0; m < estructurasHijas4.size(); m++) {
                                                        TreeNode hija4 = new DefaultTreeNode(estructurasHijas4.get(m), hija3);
                                                        estructurasHijas5 = administrarEstructuras.estructurasHijas(estructurasHijas4.get(m).getSecuencia(), codigoEmpresa);
                                                        if (estructurasHijas5 != null) {
                                                            for (int f = 0; f < estructurasHijas5.size(); f++) {
                                                                TreeNode hija5 = new DefaultTreeNode(estructurasHijas5.get(f), hija4);
                                                                estructurasHijas6 = administrarEstructuras.estructurasHijas(estructurasHijas5.get(f).getSecuencia(), codigoEmpresa);
                                                                if (estructurasHijas6 != null) {
                                                                    for (int e = 0; e < estructurasHijas6.size(); e++) {
                                                                        TreeNode hija6 = new DefaultTreeNode(estructurasHijas6.get(e), hija5);
                                                                        estructurasHijas7 = administrarEstructuras.estructurasHijas(estructurasHijas6.get(e).getSecuencia(), codigoEmpresa);
                                                                        if (estructurasHijas7 != null) {
                                                                            for (int p = 0; p < estructurasHijas7.size(); p++) {
                                                                                TreeNode hija7 = new DefaultTreeNode(estructurasHijas7.get(p), hija6);
                                                                                estructurasHijas8 = administrarEstructuras.estructurasHijas(estructurasHijas7.get(p).getSecuencia(), codigoEmpresa);
                                                                                if (estructurasHijas8 != null) {
                                                                                    for (int a = 0; a < estructurasHijas8.size(); a++) {
                                                                                        TreeNode hija8 = new DefaultTreeNode(estructurasHijas8.get(m), hija7);
                                                                                        estructurasHijas9 = administrarEstructuras.estructurasHijas(estructurasHijas8.get(m).getSecuencia(), codigoEmpresa);
                                                                                        if (estructurasHijas9 != null) {
                                                                                            for (int t = 0; t < estructurasHijas9.size(); t++) {
                                                                                                TreeNode hija9 = new DefaultTreeNode(estructurasHijas9.get(m), hija8);
                                                                                                estructurasHijas10 = administrarEstructuras.estructurasHijas(estructurasHijas9.get(m).getSecuencia(), codigoEmpresa);
                                                                                                if (estructurasHijas10 != null) {
                                                                                                    for (int r = 0; r < estructurasHijas10.size(); r++) {
                                                                                                        TreeNode hija10 = new DefaultTreeNode(estructurasHijas10.get(m), hija9);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return arbolEstructuras;
    }

    public void restaurarTabla() {
        altoTabla = "65";
        FacesContext c = FacesContext.getCurrentInstance();
        organigramaFecha = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaFecha");
        organigramaFecha.setFilterStyle("display: none; visibility: hidden;");
        organigramaCodigo = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaCodigo");
        organigramaCodigo.setFilterStyle("display: none; visibility: hidden;");
        organigramaEmpresa = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaEmpresa");
        organigramaEmpresa.setFilterStyle("display: none; visibility: hidden;");
        organigramaNit = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaNit");
        organigramaNit.setFilterStyle("display: none; visibility: hidden;");
        organigramaEstado = (Column) c.getViewRoot().findComponent("form:datosOrganigramas:organigramaEstado");
        organigramaEstado.setFilterStyle("display: none; visibility: hidden;");
        PrimefacesContextUI.actualizar("form:datosOrganigramas");
        bandera = 0;
        filtradoListaOrganigramas = null;
        tipoLista = 0;
    }

    //
//    public void posicionCredito() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
//        String name = map.get("n"); // name attribute of node
//        String type = map.get("t"); // type attribute of node
//        int indice = Integer.parseInt(type);
//        int columna = Integer.parseInt(name);
//        cambiarIndice(indice, columna);
//    }
    public List<Estructuras> getEstructurasPadre() {
        return estructurasPadre;
    }

    public void setEstructurasPadre(List<Estructuras> estructurasPadre) {
        this.estructurasPadre = estructurasPadre;
    }

    public List<Organigramas> getListaOrganigramas() {
        if (listaOrganigramas == null) {
            listaOrganigramas = administrarEstructuras.obtenerOrganigramas();
        }
        return listaOrganigramas;
    }

    public void setListaOrganigramas(List<Organigramas> listaOrganigramas) {
        this.listaOrganigramas = listaOrganigramas;
    }

    public List<Organigramas> getFiltradoListaOrganigramas() {
        return filtradoListaOrganigramas;
    }

    public void setFiltradoListaOrganigramas(List<Organigramas> filtradoListaOrganigramas) {
        this.filtradoListaOrganigramas = filtradoListaOrganigramas;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarEstructuras.obtenerEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> listaEmpresas) {
        this.lovEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltradoEmpresas() {
        return filtradoEmpresas;
    }

    public void setFiltradoEmpresas(List<Empresas> filtradoEmpresas) {
        this.filtradoEmpresas = filtradoEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas selecionEmpresa) {
        this.empresaSeleccionada = selecionEmpresa;
    }

    public Organigramas getNuevoOrganigrama() {
        return nuevoOrganigrama;
    }

    public void setNuevoOrganigrama(Organigramas nuevoOrganigrama) {
        this.nuevoOrganigrama = nuevoOrganigrama;
    }

    public Organigramas getEditarOrganigrama() {
        return editarOrganigrama;
    }

    public void setEditarOrganigrama(Organigramas editarOrganigrama) {
        this.editarOrganigrama = editarOrganigrama;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    public void setAceptarEditar(boolean aceptarEditar) {
        this.aceptarEditar = aceptarEditar;
    }

    public Organigramas getDuplicarOrganigrama() {
        return duplicarOrganigrama;
    }

    public void setDuplicarOrganigrama(Organigramas duplicarOrganigrama) {
        this.duplicarOrganigrama = duplicarOrganigrama;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Organigramas getOrganigramaSeleccionado() {
        return organigramaSeleccionado;
    }

    public void setOrganigramaSeleccionado(Organigramas organigramaTablaSeleccionado) {
        this.organigramaSeleccionado = organigramaTablaSeleccionado;
    }

    public Empresas getSeleccionEmpresa() {
        return empresaSeleccionada;
    }

    public void setSeleccionEmpresa(Empresas seleccionEmpresa) {
        this.empresaSeleccionada = seleccionEmpresa;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
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
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
