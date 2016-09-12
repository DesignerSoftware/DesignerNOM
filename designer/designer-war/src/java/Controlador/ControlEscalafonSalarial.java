package Controlador;

import Entidades.EscalafonesSalariales;
import Entidades.GruposSalariales;
import Entidades.TiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEscalafonesSalarialesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlEscalafonSalarial implements Serializable {

    @EJB
    AdministrarEscalafonesSalarialesInterface administrarEscalafonesSalariales;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //
    private List<EscalafonesSalariales> listaEscalafonesSalariales;
    private List<EscalafonesSalariales> filtrarListaEscalafonesSalariales;
    private EscalafonesSalariales escalafonTablaSeleccionado;
    ///////
    private List<GruposSalariales> listaGruposSalariales;
    private List<GruposSalariales> filtrarListaGruposSalariales;
    private GruposSalariales grupoTablaSeleccionado;
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaGS;
    //Columnas Tabla VC
    private Column escalafonCodigo, escalafonDescripcion, escalafonTipoTrabajador;
    private Column grupoDescripcion, grupoSalario;
    //Otros
    private boolean aceptar;
    private int index, indexGS, indexAux;
    //modificar
    private List<EscalafonesSalariales> listEscalafonesSalarialesModificar;
    private List<GruposSalariales> listGruposSalarialesModificar;
    private boolean guardado, guardadoGS;
    //crear 
    private EscalafonesSalariales nuevoEscalafonSalarial;
    private GruposSalariales nuevoGrupoSalarial;
    private List<EscalafonesSalariales> listEscalafonesSalarialesCrear;
    private List<GruposSalariales> listGruposSalarialesCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<EscalafonesSalariales> listEscalafonesSalarialesBorrar;
    private List<GruposSalariales> listGruposSalarialesBorrar;
    //editar celda
    private EscalafonesSalariales editarEscalafonSalarial;
    private GruposSalariales editarGrupoSalarial;
    private int cualCelda, tipoLista, cualCeldaGS, tipoListaGS;
    //duplicar
    private EscalafonesSalariales duplicarEscalafonSalarial;
    private GruposSalariales duplicarGrupoSalarial;
    private BigInteger secRegistro, secRegistroGS;
    private BigInteger backUpSecRegistro, backUpSecRegistroGS;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String tipoTrabajador;

    //////////////////////
    private List<TiposTrabajadores> lovTiposTrabajadores;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private TiposTrabajadores tipoTrabajadorSeleccionado;

    private boolean permitirIndex;
    private int tipoActualizacion;
    private String auxEscalafonDescripcion, auxGrupoDescripcion;
    private Short auxEscalafonCodigo;
    private BigDecimal auxGrupoSalario;
    ///
    private String altoTablaEscalafon, altoTablaGrupo;
    //
    private boolean cambiosPagina;
    //
    private String infoRegistroTipoTrabajador;
    //
    private boolean disabledNuevoGrupoSalarial;

    public ControlEscalafonSalarial() {
        disabledNuevoGrupoSalarial = true;
        cambiosPagina = true;
        altoTablaEscalafon = "170";
        altoTablaGrupo = "120";
        auxGrupoDescripcion = "";
        auxEscalafonDescripcion = "";
        auxGrupoSalario = null;
        permitirIndex = true;
        tipoActualizacion = -1;
        lovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        indexGS = -1;
        backUpSecRegistro = null;
        listaEscalafonesSalariales = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listEscalafonesSalarialesBorrar = new ArrayList<EscalafonesSalariales>();
        listGruposSalarialesModificar = new ArrayList<GruposSalariales>();
        listGruposSalarialesBorrar = new ArrayList<GruposSalariales>();
        //crear aficiones
        listEscalafonesSalarialesCrear = new ArrayList<EscalafonesSalariales>();
        listGruposSalarialesCrear = new ArrayList<GruposSalariales>();
        k = 0;
        //modificar aficiones
        listEscalafonesSalarialesModificar = new ArrayList<EscalafonesSalariales>();
        //editar
        editarEscalafonSalarial = new EscalafonesSalariales();
        editarGrupoSalarial = new GruposSalariales();
        cualCelda = -1;
        index = -1;
        cualCeldaGS = -1;
        tipoListaGS = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        guardadoGS = true;
        //Crear VC
        nuevoEscalafonSalarial = new EscalafonesSalariales();
        nuevoEscalafonSalarial.setTipotrabajador(new TiposTrabajadores());
        nuevoGrupoSalarial = new GruposSalariales();
        duplicarEscalafonSalarial = new EscalafonesSalariales();
        duplicarGrupoSalarial = new GruposSalariales();
        secRegistro = null;
        secRegistroGS = null;
        backUpSecRegistroGS = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEscalafonesSalariales.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public boolean validarCamposNulosEscalafonSalarial(int i) {
        boolean retorno = true;
        if (i == 0) {
            EscalafonesSalariales aux = new EscalafonesSalariales();
            if (tipoLista == 0) {
                aux = listaEscalafonesSalariales.get(index);
            }
            if (tipoLista == 1) {
                aux = filtrarListaEscalafonesSalariales.get(index);
            }
            if (aux.getCodigo() == null) {
                retorno = false;
            }
            if (aux.getDescripcion() == null) {
                retorno = false;
            } else {
                if (aux.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (aux.getTipotrabajador().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoEscalafonSalarial.getCodigo() == null) {
                retorno = false;
            }
            if (nuevoEscalafonSalarial.getDescripcion() == null) {
                retorno = false;
            } else {
                if (nuevoEscalafonSalarial.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (nuevoEscalafonSalarial.getTipotrabajador().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarEscalafonSalarial.getCodigo() == null) {
                retorno = false;
            }
            if (duplicarEscalafonSalarial.getDescripcion() == null) {
                retorno = false;
            } else {
                if (duplicarEscalafonSalarial.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (duplicarEscalafonSalarial.getTipotrabajador().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosGrupoSalarial(int i) {
        boolean retorno = true;
        if (i == 0) {
            GruposSalariales aux = new GruposSalariales();
            if (tipoListaGS == 0) {
                aux = listaGruposSalariales.get(indexGS);
            }
            if (tipoListaGS == 1) {
                aux = filtrarListaGruposSalariales.get(indexGS);
            }
            if (aux.getDescripcion() == null) {
                retorno = false;
            } else {
                if (aux.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (aux.getSalario() == null) {
                retorno = false;
            } else {
                if (aux.getSalario().doubleValue() <= 0) {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoGrupoSalarial.getDescripcion() == null) {
                retorno = false;
            } else {
                if (nuevoGrupoSalarial.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (nuevoGrupoSalarial.getSalario() == null) {
                retorno = false;
            } else {
                if (nuevoGrupoSalarial.getSalario().doubleValue() <= 0) {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarGrupoSalarial.getDescripcion() == null) {
                retorno = false;
            } else {
                if (duplicarGrupoSalarial.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (duplicarGrupoSalarial.getSalario() == null) {
                retorno = false;
            } else {
                if (duplicarGrupoSalarial.getSalario().doubleValue() <= 0) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void procesoModificacionEscalafonSalarial(int i) {
        index = i;
        boolean respuesta = validarCamposNulosEscalafonSalarial(0);
        if (respuesta == true) {
            modificarEscalafonSalarial(i);
        } else {
            if (tipoLista == 0) {
                listaEscalafonesSalariales.get(index).setDescripcion(auxEscalafonDescripcion);
                listaEscalafonesSalariales.get(index).setCodigo(auxEscalafonCodigo);
            }
            if (tipoLista == 1) {
                filtrarListaEscalafonesSalariales.get(index).setDescripcion(auxEscalafonDescripcion);
                filtrarListaEscalafonesSalariales.get(index).setCodigo(auxEscalafonCodigo);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEscalafon').show()");
        }
    }

    public void procesoModificacionGrupoSalarial(int i) {
        index = i;
        boolean respuesa = validarCamposNulosGrupoSalarial(0);
        if (respuesa == true) {
            modificarGrupoSalarial(i);
        } else {
            if (tipoListaGS == 0) {
                listaGruposSalariales.get(indexGS).setDescripcion(auxGrupoDescripcion);
                listaGruposSalariales.get(indexGS).setSalario(auxGrupoSalario);

                if (tipoListaGS == 1) {
                    filtrarListaGruposSalariales.get(indexGS).setDescripcion(auxGrupoDescripcion);
                    filtrarListaGruposSalariales.get(indexGS).setSalario(auxGrupoSalario);
                }
                indexGS = -1;
                secRegistroGS = null;
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                PrimefacesContextUI.ejecutar("PF('errorDatosNullGrupo').show()");
            }
        }
    }

    public void modificarEscalafonSalarial(int indice) {
        int tamDes = 0;
        if (tipoLista == 0) {
            tamDes = listaEscalafonesSalariales.get(indice).getDescripcion().length();
        }
        if (tipoLista == 1) {
            tamDes = filtrarListaEscalafonesSalariales.get(indice).getDescripcion().length();
        }
        if (tamDes >= 1 && tamDes <= 30) {
            if (tipoLista == 0) {
                String textM = listaEscalafonesSalariales.get(indice).getDescripcion().toUpperCase();
                listaEscalafonesSalariales.get(indice).setDescripcion(textM);
                if (!listEscalafonesSalarialesCrear.contains(listaEscalafonesSalariales.get(indice))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(indice));
                    } else if (!listEscalafonesSalarialesModificar.contains(listaEscalafonesSalariales.get(indice))) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListaEscalafonesSalariales.get(indice).getDescripcion().toUpperCase();
                filtrarListaEscalafonesSalariales.get(indice).setDescripcion(textM);
                if (!listEscalafonesSalarialesCrear.contains(filtrarListaEscalafonesSalariales.get(indice))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(indice));
                    } else if (!listEscalafonesSalarialesModificar.contains(filtrarListaEscalafonesSalariales.get(indice))) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        //PrimefacesContextUI.actualizar("form:aceptar");
                    }
                }
            }
            index = -1;
            secRegistro = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
        } else {
            if (tipoLista == 0) {
                listaEscalafonesSalariales.get(index).setDescripcion(auxEscalafonDescripcion);
            }
            if (tipoLista == 1) {
                filtrarListaEscalafonesSalariales.get(index).setDescripcion(auxEscalafonDescripcion);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            PrimefacesContextUI.ejecutar("PF('errorDescripcionEscalafon').show()");
        }

    }

    public void modificarEscalafonSalarial(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (tipoLista == 0) {
                listaEscalafonesSalariales.get(indice).getTipotrabajador().setNombre(tipoTrabajador);
            } else {
                filtrarListaEscalafonesSalariales.get(indice).getTipotrabajador().setNombre(tipoTrabajador);
            }
            for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaEscalafonesSalariales.get(indice).setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                } else {
                    filtrarListaEscalafonesSalariales.get(indice).setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                }
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                lovTiposTrabajadores.clear();
                getLovTiposTrabajadores();
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listEscalafonesSalarialesCrear.contains(listaEscalafonesSalariales.get(indice))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(indice));
                    } else if (!listEscalafonesSalarialesModificar.contains(listaEscalafonesSalariales.get(indice))) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            if (tipoLista == 1) {
                if (!listEscalafonesSalarialesCrear.contains(filtrarListaEscalafonesSalariales.get(indice))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(indice));
                    } else if (!listEscalafonesSalarialesModificar.contains(filtrarListaEscalafonesSalariales.get(indice))) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        //PrimefacesContextUI.actualizar("form:aceptar");
                    }
                }
            }
            guardado = true;
        }
        PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
    }

    public void modificarGrupoSalarial(int indice) {
        if (tipoListaGS == 0) {
            if (!listGruposSalarialesCrear.contains(listaGruposSalariales.get(indice))) {
                if (listGruposSalarialesModificar.isEmpty()) {
                    listGruposSalarialesModificar.add(listaGruposSalariales.get(indice));
                } else if (!listGruposSalarialesModificar.contains(listaGruposSalariales.get(indice))) {
                    listGruposSalarialesModificar.add(listaGruposSalariales.get(indice));
                }
                if (guardadoGS == true) {
                    guardadoGS = false;
                }
            }
        }
        if (tipoListaGS == 1) {
            if (!listGruposSalarialesCrear.contains(filtrarListaGruposSalariales.get(indice))) {
                if (listGruposSalarialesModificar.isEmpty()) {
                    listGruposSalarialesModificar.add(filtrarListaGruposSalariales.get(indice));
                } else if (!listGruposSalarialesModificar.contains(filtrarListaGruposSalariales.get(indice))) {
                    listGruposSalarialesModificar.add(filtrarListaGruposSalariales.get(indice));
                }
                if (guardadoGS == true) {
                    guardadoGS = false;
                    //PrimefacesContextUI.actualizar("form:aceptar");
                }
            }
        }
        indexGS = -1;
        secRegistroGS = null;
        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoGS == true) {
            if (permitirIndex == true) {
                index = indice;
                cualCelda = celda;
                indexAux = indice;
                indexGS = -1;

                if (tipoLista == 0) {
                    auxEscalafonDescripcion = listaEscalafonesSalariales.get(index).getDescripcion();
                    secRegistro = listaEscalafonesSalariales.get(index).getSecuencia();
                    tipoTrabajador = listaEscalafonesSalariales.get(index).getTipotrabajador().getNombre();
                    auxEscalafonCodigo = listaEscalafonesSalariales.get(index).getCodigo();
                    auxEscalafonDescripcion = listaEscalafonesSalariales.get(index).getDescripcion();
                }
                if (tipoLista == 1) {
                    auxEscalafonDescripcion = filtrarListaEscalafonesSalariales.get(index).getDescripcion();
                    secRegistro = filtrarListaEscalafonesSalariales.get(index).getSecuencia();
                    tipoTrabajador = filtrarListaEscalafonesSalariales.get(index).getTipotrabajador().getNombre();
                    auxEscalafonCodigo = filtrarListaEscalafonesSalariales.get(index).getCodigo();
                    auxEscalafonDescripcion = filtrarListaEscalafonesSalariales.get(index).getDescripcion();
                }
                listaGruposSalariales = null;
                getListaGruposSalariales();
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                if (banderaGS == 1) {
                    altoTablaGrupo = "120";
                    grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
                    grupoSalario.setFilterStyle("display: none; visibility: hidden;");
                    grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
                    grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                    banderaGS = 0;
                    filtrarListaGruposSalariales = null;
                    tipoListaGS = 0;
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceGrupoSalarial(int indice, int celda) {
        indexGS = indice;
        index = -1;
        cualCeldaGS = celda;
        if (tipoListaGS == 0) {
            auxGrupoSalario = listaGruposSalariales.get(indexGS).getSalario();
            secRegistroGS = listaGruposSalariales.get(indexGS).getSecuencia();
            auxGrupoDescripcion = listaGruposSalariales.get(indexGS).getDescripcion();
        }
        if (tipoListaGS == 1) {
            auxGrupoSalario = filtrarListaGruposSalariales.get(indexGS).getSalario();
            secRegistroGS = filtrarListaGruposSalariales.get(indexGS).getSecuencia();
            auxGrupoDescripcion = filtrarListaGruposSalariales.get(indexGS).getDescripcion();
        }
    }
    //GUARDAR

    public void guardarYSalir() {
        guardarGeneral();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacionGeneral();
        salir();
    }

    public void guardarGeneral() {
        if (guardado == false || guardadoGS == false) {
            if (guardado == false) {
                guardarCambiosEscalafonSalarial();
            }
            if (guardadoGS == false) {
                guardarCambiosGrupoSalarial();
            }
            cambiosPagina = true;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void guardarCambiosEscalafonSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listEscalafonesSalarialesBorrar.isEmpty()) {
                for (int i = 0; i < listEscalafonesSalarialesBorrar.size(); i++) {
                    administrarEscalafonesSalariales.borrarEscalafonesSalariales(listEscalafonesSalarialesBorrar);
                }
                listEscalafonesSalarialesBorrar.clear();
            }
            if (!listEscalafonesSalarialesCrear.isEmpty()) {
                for (int i = 0; i < listEscalafonesSalarialesCrear.size(); i++) {
                    administrarEscalafonesSalariales.crearEscalafonesSalariales(listEscalafonesSalarialesCrear);
                }
                listEscalafonesSalarialesCrear.clear();
            }
            if (!listEscalafonesSalarialesModificar.isEmpty()) {
                administrarEscalafonesSalariales.editarEscalafonesSalariales(listEscalafonesSalarialesModificar);
                listEscalafonesSalarialesModificar.clear();
            }
            listaEscalafonesSalariales = null;
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            guardado = true;
            k = 0;
            index = -1;
            secRegistro = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Escalafon Salarial se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosEscalafonSalarial : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Escalafon Salarial, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }

    public void guardarCambiosGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listGruposSalarialesBorrar.isEmpty()) {
                for (int i = 0; i < listGruposSalarialesBorrar.size(); i++) {
                    administrarEscalafonesSalariales.borrarGruposSalariales(listGruposSalarialesBorrar);
                }
                listGruposSalarialesBorrar.clear();
            }
            if (!listGruposSalarialesCrear.isEmpty()) {
                for (int i = 0; i < listGruposSalarialesCrear.size(); i++) {
                    administrarEscalafonesSalariales.crearGruposSalariales(listGruposSalarialesCrear);
                }
                listGruposSalarialesCrear.clear();
            }
            if (!listGruposSalarialesModificar.isEmpty()) {
                administrarEscalafonesSalariales.editarGruposSalariales(listGruposSalarialesModificar);
                listGruposSalarialesModificar.clear();
            }
            listaGruposSalariales = null;
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            guardadoGS = true;
            k = 0;
            indexGS = -1;
            secRegistroGS = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Grupo Salarial se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosEscalafonSalarial : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Grupo Salarial, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }

    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacionGeneral() {
        if (guardado == false) {
            cancelarModificacionEscalafonSalarial();
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
        }
        if (guardadoGS == false) {
            cancelarModificacionGrupoSalarial();
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
        }
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void cancelarModificacionEscalafonSalarial() {
        if (bandera == 1) {
            altoTablaEscalafon = "170";
            escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonCodigo");
            escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
            escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonDescripcion");
            escalafonDescripcion.setFilterStyle("display: none; visibility: hidden;");
            escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonTipoTrabajador");
            escalafonTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            bandera = 0;
            filtrarListaEscalafonesSalariales = null;
            tipoLista = 0;
        }
        listEscalafonesSalarialesBorrar.clear();
        listEscalafonesSalarialesCrear.clear();
        listEscalafonesSalarialesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaEscalafonesSalariales = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
    }

    public void cancelarModificacionGrupoSalarial() {
        if (banderaGS == 1) {
            altoTablaGrupo = "120";
            grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
            grupoSalario.setFilterStyle("display: none; visibility: hidden;");
            grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
            grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            banderaGS = 0;
            filtrarListaGruposSalariales = null;
            tipoListaGS = 0;
        }
        listGruposSalarialesBorrar.clear();
        listGruposSalarialesCrear.clear();
        listGruposSalarialesModificar.clear();
        indexGS = -1;
        secRegistroGS = null;
        k = 0;
        listaGruposSalariales = null;
        guardadoGS = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarEscalafonSalarial = listaEscalafonesSalariales.get(index);
            }
            if (tipoLista == 1) {
                editarEscalafonSalarial = filtrarListaEscalafonesSalariales.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEscalafonCodigoD");
                PrimefacesContextUI.ejecutar("PF('editarEscalafonCodigoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEscalafonDescripcionD");
                PrimefacesContextUI.ejecutar("PF('editarEscalafonDescripcionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEscalafonTipoTrabajadorD");
                PrimefacesContextUI.ejecutar("PF('editarEscalafonTipoTrabajadorD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexGS >= 0) {
            if (tipoListaGS == 0) {
                editarGrupoSalarial = listaGruposSalariales.get(indexGS);
            }
            if (tipoListaGS == 1) {
                editarGrupoSalarial = listaGruposSalariales.get(indexGS);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaGS == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarGrupoDescripcionD");
                PrimefacesContextUI.ejecutar("PF('editarGrupoDescripcionD').show()");
                cualCeldaGS = -1;
            } else if (cualCeldaGS == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarGrupoSalarioD");
                PrimefacesContextUI.ejecutar("PF('editarGrupoSalarioD').show()");
                cualCeldaGS = -1;
            }

            indexGS = -1;
            secRegistroGS = null;
        }

    }

    public void dialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        int tam = listaEscalafonesSalariales.size();
        int tam2 = listaGruposSalariales.size();
        if (tam == 0 || tam2 == 0) {
            if (cambiosPagina == true) {
                if(tam > 0){
                    disabledNuevoGrupoSalarial = false;
                } else{
                    disabledNuevoGrupoSalarial = true;
                }
                PrimefacesContextUI.actualizar("form:verificarNuevoRegistro");
                PrimefacesContextUI.ejecutar("PF('verificarNuevoRegistro').show()");
            } else {
                PrimefacesContextUI.actualizar("form:confirmarGuardar");
                PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
            }
        } else {
            if (index >= 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroEscalafonSalarial");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroEscalafonSalarial').show()");
            }
            if (indexGS >= 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroGrupoSalarial");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroGrupoSalarial').show()");
            }
        }
    }

    //CREAR 
    public void agregarNuevoEscalafonSalarial() {
        boolean respueta = validarCamposNulosEscalafonSalarial(1);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoEscalafonSalarial.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                if (bandera == 1) {
                    altoTablaEscalafon = "170";
                    escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:escalafonCodigo:extraCodigo");
                    escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
                    escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:escalafonDescripcion:extraDescripcion");
                    escalafonDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:escalafonTipoTrabajador:extraTipoDia");
                    escalafonTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                    PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                    bandera = 0;
                    filtrarListaEscalafonesSalariales = null;
                    tipoLista = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                String text = nuevoEscalafonSalarial.getDescripcion().toUpperCase();
                nuevoEscalafonSalarial.setDescripcion(text);
                nuevoEscalafonSalarial.setSecuencia(l);
                listEscalafonesSalarialesCrear.add(nuevoEscalafonSalarial);
                listaEscalafonesSalariales.add(nuevoEscalafonSalarial);
                nuevoEscalafonSalarial = new EscalafonesSalariales();
                nuevoEscalafonSalarial.setTipotrabajador(new TiposTrabajadores());
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroEscalafonSalarial').hide()");
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:aceptar");
                }
                index = -1;
                secRegistro = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorDescripcionEscalafon').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEscalafon').show()");
        }
    }

    public void agregarNuevoGrupoSalarial() {
        boolean respueta = validarCamposNulosGrupoSalarial(1);
        if (respueta == true) {
            if (banderaGS == 1) {
                altoTablaGrupo = "120";
                grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
                grupoSalario.setFilterStyle("display: none; visibility: hidden;");
                grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
                grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                banderaGS = 0;
                filtrarListaGruposSalariales = null;
                tipoListaGS = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoGrupoSalarial.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoGrupoSalarial.setEscalafonsalarial(listaEscalafonesSalariales.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoGrupoSalarial.setEscalafonsalarial(filtrarListaEscalafonesSalariales.get(indexAux));
            }
            listGruposSalarialesCrear.add(nuevoGrupoSalarial);
            listaGruposSalariales.add(nuevoGrupoSalarial);
            nuevoGrupoSalarial = new GruposSalariales();
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroGrupoSalarial').hide()");
            if (guardadoGS == true) {
                guardadoGS = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
            indexGS = -1;
            secRegistroGS = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullGrupo').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     */
    public void limpiarNuevaEscalafonSalarial() {
        nuevoEscalafonSalarial = new EscalafonesSalariales();
        nuevoEscalafonSalarial.setTipotrabajador(new TiposTrabajadores());
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaGrupoSalarial() {
        nuevoGrupoSalarial = new GruposSalariales();
        indexGS = -1;
        secRegistroGS = null;
    }

    //DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarEscalafonSalarialM();
        }
        if (indexGS >= 0) {
            duplicarGrupoSalarialM();
        }
    }

    public void duplicarEscalafonSalarialM() {
        if (index >= 0) {
            duplicarEscalafonSalarial = new EscalafonesSalariales();
            if (tipoLista == 0) {
                duplicarEscalafonSalarial.setCodigo(listaEscalafonesSalariales.get(index).getCodigo());
                duplicarEscalafonSalarial.setDescripcion(listaEscalafonesSalariales.get(index).getDescripcion());
                duplicarEscalafonSalarial.setTipotrabajador(listaEscalafonesSalariales.get(index).getTipotrabajador());
            }
            if (tipoLista == 1) {
                duplicarEscalafonSalarial.setCodigo(filtrarListaEscalafonesSalariales.get(index).getCodigo());
                duplicarEscalafonSalarial.setDescripcion(filtrarListaEscalafonesSalariales.get(index).getDescripcion());
                duplicarEscalafonSalarial.setTipotrabajador(filtrarListaEscalafonesSalariales.get(index).getTipotrabajador());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroEscalafonSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroEscalafonSalarial').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void duplicarGrupoSalarialM() {
        if (indexGS >= 0) {
            duplicarGrupoSalarial = new GruposSalariales();
            if (tipoListaGS == 0) {
                duplicarGrupoSalarial.setDescripcion(listaGruposSalariales.get(indexGS).getDescripcion());
                duplicarGrupoSalarial.setSalario(listaGruposSalariales.get(indexGS).getSalario());
            }
            if (tipoListaGS == 1) {
                duplicarGrupoSalarial.setDescripcion(filtrarListaGruposSalariales.get(indexGS).getDescripcion());
                duplicarGrupoSalarial.setSalario(filtrarListaGruposSalariales.get(indexGS).getSalario());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroGrupoSalarial').show()");
            indexGS = -1;
            secRegistroGS = null;
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
     */
    public void confirmarDuplicarEscalafonSalarial() {
        boolean respueta = validarCamposNulosEscalafonSalarial(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = duplicarEscalafonSalarial.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 30) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarEscalafonSalarial.setSecuencia(l);
                String text = duplicarEscalafonSalarial.getDescripcion().toUpperCase();
                duplicarEscalafonSalarial.setDescripcion(text);
                listaEscalafonesSalariales.add(duplicarEscalafonSalarial);
                listEscalafonesSalarialesCrear.add(duplicarEscalafonSalarial);
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroEscalafonSalarial').hide()");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    //PrimefacesContextUI.actualizar("form:aceptar");
                }
                if (bandera == 1) {
                    altoTablaEscalafon = "170";
                    escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonCodigo");
                    escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
                    escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonDescripcion");
                    escalafonDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonTipoTrabajador");
                    escalafonTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                    PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                    bandera = 0;
                    filtrarListaEscalafonesSalariales = null;
                    tipoLista = 0;
                }
                duplicarEscalafonSalarial = new EscalafonesSalariales();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorDescripcionEscalafon').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEscalafon').show()");
        }
    }

    public void confirmarDuplicarGrupoSalarial() {
        boolean respueta = validarCamposNulosGrupoSalarial(2);
        if (respueta == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarGrupoSalarial.setSecuencia(l);
            if (tipoLista == 0) {
                duplicarGrupoSalarial.setEscalafonsalarial(listaEscalafonesSalariales.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarGrupoSalarial.setEscalafonsalarial(filtrarListaEscalafonesSalariales.get(indexAux));
            }
            listaGruposSalariales.add(duplicarGrupoSalarial);
            listGruposSalarialesCrear.add(duplicarGrupoSalarial);
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroGrupoSalarial').hide()");
            indexGS = -1;
            secRegistroGS = null;
            if (guardadoGS == true) {
                guardadoGS = false;
                //PrimefacesContextUI.actualizar("form:aceptar");
            }
            if (banderaGS == 1) {
                altoTablaGrupo = "120";
                grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
                grupoSalario.setFilterStyle("display: none; visibility: hidden;");
                grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
                grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                banderaGS = 0;
                filtrarListaGruposSalariales = null;
                tipoListaGS = 0;
            }
            duplicarGrupoSalarial = new GruposSalariales();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullGrupo').show()");
        }
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Set
     */
    public void limpiarDuplicarEscalafonSalarial() {
        duplicarEscalafonSalarial = new EscalafonesSalariales();
        duplicarEscalafonSalarial.setTipotrabajador(new TiposTrabajadores());
    }

    public void limpiarDuplicarGrupoSalarial() {
        duplicarGrupoSalarial = new GruposSalariales();
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //BORRAR VC
    /**
     */
    public void verificarRegistroBorrar() {
        if (index >= 0) {
            if (listaGruposSalariales.isEmpty()) {
                borrarEscalafonSalarial();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexGS >= 0) {
            borrarGrupoSalarial();
        }
    }

    public void borrarEscalafonSalarial() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listEscalafonesSalarialesModificar.isEmpty() && listEscalafonesSalarialesModificar.contains(listaEscalafonesSalariales.get(index))) {
                    int modIndex = listEscalafonesSalarialesModificar.indexOf(listaEscalafonesSalariales.get(index));
                    listEscalafonesSalarialesModificar.remove(modIndex);
                    listEscalafonesSalarialesBorrar.add(listaEscalafonesSalariales.get(index));
                } else if (!listEscalafonesSalarialesCrear.isEmpty() && listEscalafonesSalarialesCrear.contains(listaEscalafonesSalariales.get(index))) {
                    int crearIndex = listEscalafonesSalarialesCrear.indexOf(listaEscalafonesSalariales.get(index));
                    listEscalafonesSalarialesCrear.remove(crearIndex);
                } else {
                    listEscalafonesSalarialesBorrar.add(listaEscalafonesSalariales.get(index));
                }
                listaEscalafonesSalariales.remove(index);
            }
            if (tipoLista == 1) {
                if (!listEscalafonesSalarialesModificar.isEmpty() && listEscalafonesSalarialesModificar.contains(filtrarListaEscalafonesSalariales.get(index))) {
                    int modIndex = listEscalafonesSalarialesModificar.indexOf(filtrarListaEscalafonesSalariales.get(index));
                    listEscalafonesSalarialesModificar.remove(modIndex);
                    listEscalafonesSalarialesBorrar.add(filtrarListaEscalafonesSalariales.get(index));
                } else if (!listEscalafonesSalarialesCrear.isEmpty() && listEscalafonesSalarialesCrear.contains(filtrarListaEscalafonesSalariales.get(index))) {
                    int crearIndex = listEscalafonesSalarialesCrear.indexOf(filtrarListaEscalafonesSalariales.get(index));
                    listEscalafonesSalarialesCrear.remove(crearIndex);
                } else {
                    listEscalafonesSalarialesBorrar.add(filtrarListaEscalafonesSalariales.get(index));
                }
                int VCIndex = listaEscalafonesSalariales.indexOf(filtrarListaEscalafonesSalariales.get(index));
                listaEscalafonesSalariales.remove(VCIndex);
                filtrarListaEscalafonesSalariales.remove(index);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                //PrimefacesContextUI.actualizar("form:aceptar");
            }
        }
    }

    public void borrarGrupoSalarial() {
        if (indexGS >= 0) {
            if (tipoListaGS == 0) {
                if (!listGruposSalarialesModificar.isEmpty() && listGruposSalarialesModificar.contains(listaGruposSalariales.get(indexGS))) {
                    int modIndex = listGruposSalarialesModificar.indexOf(listaGruposSalariales.get(indexGS));
                    listGruposSalarialesModificar.remove(modIndex);
                    listGruposSalarialesBorrar.add(listaGruposSalariales.get(indexGS));
                } else if (!listGruposSalarialesCrear.isEmpty() && listGruposSalarialesCrear.contains(listaGruposSalariales.get(indexGS))) {
                    int crearIndex = listGruposSalarialesCrear.indexOf(listaGruposSalariales.get(indexGS));
                    listGruposSalarialesCrear.remove(crearIndex);
                } else {
                    listGruposSalarialesBorrar.add(listaGruposSalariales.get(indexGS));
                }
                listaGruposSalariales.remove(indexGS);
            }
            if (tipoListaGS == 1) {
                if (!listGruposSalarialesModificar.isEmpty() && listGruposSalarialesModificar.contains(filtrarListaGruposSalariales.get(indexGS))) {
                    int modIndex = listGruposSalarialesModificar.indexOf(filtrarListaGruposSalariales.get(indexGS));
                    listGruposSalarialesModificar.remove(modIndex);
                    listGruposSalarialesBorrar.add(filtrarListaGruposSalariales.get(indexGS));
                } else if (!listGruposSalarialesCrear.isEmpty() && listGruposSalarialesCrear.contains(filtrarListaGruposSalariales.get(indexGS))) {
                    int crearIndex = listGruposSalarialesCrear.indexOf(filtrarListaGruposSalariales.get(indexGS));
                    listGruposSalarialesCrear.remove(crearIndex);
                } else {
                    listGruposSalarialesBorrar.add(filtrarListaGruposSalariales.get(indexGS));
                }
                int VCIndex = listaGruposSalariales.indexOf(filtrarListaGruposSalariales.get(indexGS));
                listaGruposSalariales.remove(VCIndex);
                filtrarListaGruposSalariales.remove(indexGS);
            }

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            indexGS = -1;
            secRegistroGS = null;

            if (guardadoGS == true) {
                guardadoGS = false;
                //PrimefacesContextUI.actualizar("form:aceptar");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (index >= 0) {
            if (bandera == 0) {
                altoTablaEscalafon = "150";
                escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonCodigo");
                escalafonCodigo.setFilterStyle("width: 85%");
                escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonDescripcion");
                escalafonDescripcion.setFilterStyle("width: 85%;");
                escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonTipoTrabajador");
                escalafonTipoTrabajador.setFilterStyle("width: 85%");
                PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                bandera = 1;
            } else if (bandera == 1) {
                altoTablaEscalafon = "170";
                escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonCodigo");
                escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
                escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonDescripcion");
                escalafonDescripcion.setFilterStyle("display: none; visibility: hidden;");
                escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonTipoTrabajador");
                escalafonTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
                bandera = 0;
                filtrarListaEscalafonesSalariales = null;
                tipoLista = 0;
            }
        }
        if (indexGS >= 0) {
            if (banderaGS == 0) {
                altoTablaGrupo = "98";
                grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
                grupoSalario.setFilterStyle("width: 85%;");
                grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
                grupoDescripcion.setFilterStyle("width: 85%;");
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                banderaGS = 1;
            } else if (banderaGS == 1) {
                altoTablaGrupo = "120";
                grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
                grupoSalario.setFilterStyle("display: none; visibility: hidden;");
                grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
                grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
                banderaGS = 0;
                filtrarListaGruposSalariales = null;
                tipoListaGS = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            altoTablaEscalafon = "170";
            escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonCodigo");
            escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
            escalafonDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonDescripcion");
            escalafonDescripcion.setFilterStyle("display: none; visibility: hidden;");
            escalafonTipoTrabajador = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafonSalarial:escalafonTipoTrabajador");
            escalafonTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
            bandera = 0;
            filtrarListaEscalafonesSalariales = null;
            tipoLista = 0;
        }
        if (banderaGS == 1) {
            altoTablaGrupo = "120";
            grupoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoSalario");
            grupoSalario.setFilterStyle("display: none; visibility: hidden;");
            grupoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGrupoSalarial:grupoDescripcion");
            grupoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosGrupoSalarial");
            banderaGS = 0;
            filtrarListaGruposSalariales = null;
            tipoListaGS = 0;
        }

        listEscalafonesSalarialesBorrar.clear();
        listEscalafonesSalarialesCrear.clear();
        listEscalafonesSalarialesModificar.clear();
        listGruposSalarialesBorrar.clear();
        listGruposSalarialesCrear.clear();
        listGruposSalarialesModificar.clear();
        index = -1;
        indexAux = -1;
        indexGS = -1;
        secRegistroGS = null;
        secRegistro = null;
        k = 0;
        listaEscalafonesSalariales = null;
        listaGruposSalariales = null;
        guardado = true;
        guardadoGS = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            index = indice;
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
        }
    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOTRABAJADOR")) {
            if (tipoNuevo == 1) {
                tipoTrabajador = nuevoEscalafonSalarial.getTipotrabajador().getNombre();
            } else if (tipoNuevo == 2) {
                tipoTrabajador = duplicarEscalafonSalarial.getTipotrabajador().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoEscalafonSalarial(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (tipoNuevo == 1) {
                nuevoEscalafonSalarial.getTipotrabajador().setNombre(tipoTrabajador);
            } else if (tipoNuevo == 2) {
                duplicarEscalafonSalarial.getTipotrabajador().setNombre(tipoTrabajador);
            }
            for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEscalafonSalarial.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoEscalafonSalarialTipoTrabajador");
                } else if (tipoNuevo == 2) {
                    duplicarEscalafonSalarial.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarEscalafonSalarialTipoTrabajador");
                }
                lovTiposTrabajadores.clear();
                getLovTiposTrabajadores();
            } else {
                PrimefacesContextUI.actualizar("form:TipoDiaDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoDiaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoEscalafonSalarialTipoTrabajador");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarEscalafonSalarialTipoTrabajador");
                }
            }
        }
    }

    public void actualizarTipoTrabajador() {
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaEscalafonesSalariales.get(index).setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listEscalafonesSalarialesCrear.contains(listaEscalafonesSalariales.get(index))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(index));
                    } else if (!listEscalafonesSalarialesModificar.contains(listaEscalafonesSalariales.get(index))) {
                        listEscalafonesSalarialesModificar.add(listaEscalafonesSalariales.get(index));
                    }
                }
            } else {
                filtrarListaEscalafonesSalariales.get(index).setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listEscalafonesSalarialesCrear.contains(filtrarListaEscalafonesSalariales.get(index))) {
                    if (listEscalafonesSalarialesModificar.isEmpty()) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(index));
                    } else if (!listEscalafonesSalarialesModificar.contains(filtrarListaEscalafonesSalariales.get(index))) {
                        listEscalafonesSalarialesModificar.add(filtrarListaEscalafonesSalariales.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosEscalafonSalarial");
        } else if (tipoActualizacion == 1) {
            nuevoEscalafonSalarial.setTipotrabajador(tipoTrabajadorSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoEscalafonSalarialTipoTrabajador");
        } else if (tipoActualizacion == 2) {
            duplicarEscalafonSalarial.setTipotrabajador(tipoTrabajadorSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarEscalafonSalarialTipoTrabajador");
        }
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
        PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
        PrimefacesContextUI.actualizar("form:lovTipoTrabajador");
        PrimefacesContextUI.actualizar("form:aceptarTT");*/
        context.reset("form:lovTipoTrabajador:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajador').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        if (index >= 0) {
            nombreTabla = ":formExportarEscalafon:datosEscalafonSalarialExportar";
            nombreXML = "EscalafonSalarial_XML";
        }
        if (indexGS >= 0) {
            nombreTabla = ":formExportarGrupo:datosGrupoSalarialExportar";
            nombreXML = "GrupoSalarial_XML";
        }
        return nombreTabla;
    }

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDF_ES();
        }
        if (indexGS >= 0) {
            exportPDF_GS();
        }
    }

    public void exportPDF_ES() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarEscalafon:datosEscalafonSalarialExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EscalafonSalarial_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_GS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarGrupo:datosGrupoSalarialExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GrupoSalarial_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_ES();
        }
        if (indexGS >= 0) {
            exportXLS_GS();
        }
    }

    public void exportXLS_ES() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarEscalafon:datosEscalafonSalarialExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EscalafonSalarial_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_GS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarGrupo:datosGrupoSalarialExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GrupoSalarial_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
        if (indexGS >= 0) {
            if (tipoListaGS == 0) {
                tipoListaGS = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        if (listaGruposSalariales == null || listaEscalafonesSalariales == null) {
        } else {
            if (index >= 0) {
                verificarRastroEscalafonSalarial();
                index = -1;
            }
            if (indexGS >= 0) {
                verificarRastroGrupoSalarial();
                indexGS = -1;
            }
        }
    }

    public void verificarRastroEscalafonSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaEscalafonesSalariales != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "ESCALAFONESSALARIALES");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "EscalafonesSalariales";
                    msnConfirmarRastro = "La tabla ESCALAFONESSALARIALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    PrimefacesContextUI.actualizar("form:msnConfirmarRastro");
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("ESCALAFONESSALARIALES")) {
                nombreTablaRastro = "EscalafonesSalariales";
                msnConfirmarRastroHistorico = "La tabla ESCALAFONESSALARIALES tiene rastros historicos, ¿Desea continuar?";
                PrimefacesContextUI.actualizar("form:confirmarRastroHistorico");
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void verificarRastroGrupoSalarial() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaGruposSalariales != null) {
            if (secRegistroGS != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroGS, "GRUPOSSALARIALES");
                backUpSecRegistroGS = secRegistroGS;
                backUp = secRegistroGS;
                secRegistroGS = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "GruposSalariales";
                    msnConfirmarRastro = "La tabla GRUPOSSALARIALES tiene rastros para el registro seleccionado, ¿desea continuar?";
                    PrimefacesContextUI.actualizar("form:msnConfirmarRastro");
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("GRUPOSSALARIALES")) {
                nombreTablaRastro = "GruposSalariales";
                msnConfirmarRastroHistorico = "La tabla GRUPOSSALARIALES tiene rastros historicos, ¿Desea continuar?";
                PrimefacesContextUI.actualizar("form:confirmarRastroHistorico");
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    //GETTERS AND SETTERS
    public List<EscalafonesSalariales> getListaEscalafonesSalariales() {
        try {
            if (listaEscalafonesSalariales == null) {
                listaEscalafonesSalariales = new ArrayList<EscalafonesSalariales>();
                listaEscalafonesSalariales = administrarEscalafonesSalariales.listaEscalafonesSalariales();
            }
            return listaEscalafonesSalariales;
        } catch (Exception e) {
            System.out.println("Error...!! getListaEscalafonesSalariales " + e.toString());
            return null;
        }
    }

    public void setListaEscalafonesSalariales(List<EscalafonesSalariales> setListaEscalafonesSalariales) {
        this.listaEscalafonesSalariales = setListaEscalafonesSalariales;
    }

    public List<EscalafonesSalariales> getFiltrarListaEscalafonesSalariales() {
        return filtrarListaEscalafonesSalariales;
    }

    public void setFiltrarListaEscalafonesSalariales(List<EscalafonesSalariales> setFiltrarListaEscalafonesSalariales) {
        this.filtrarListaEscalafonesSalariales = setFiltrarListaEscalafonesSalariales;
    }

    public EscalafonesSalariales getNuevoEscalafonSalarial() {
        return nuevoEscalafonSalarial;
    }

    public void setNuevoEscalafonSalarial(EscalafonesSalariales setNuevoEscalafonSalarial) {
        this.nuevoEscalafonSalarial = setNuevoEscalafonSalarial;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public EscalafonesSalariales getEditarEscalafonSalarial() {
        return editarEscalafonSalarial;
    }

    public void setEditarEscalafonSalarial(EscalafonesSalariales setEditarEscalafonSalarial) {
        this.editarEscalafonSalarial = setEditarEscalafonSalarial;
    }

    public EscalafonesSalariales getDuplicarEscalafonSalarial() {
        return duplicarEscalafonSalarial;
    }

    public void setDuplicarEscalafonSalarial(EscalafonesSalariales setDuplicarEscalafonSalarial) {
        this.duplicarEscalafonSalarial = setDuplicarEscalafonSalarial;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
        this.backUpSecRegistro = BackUpSecRegistro;
    }

    public List<GruposSalariales> getListaGruposSalariales() {
        if (listaGruposSalariales == null) {
            listaGruposSalariales = new ArrayList<GruposSalariales>();
            if (index >= 0) {
                if (tipoLista == 0) {
                    listaGruposSalariales = administrarEscalafonesSalariales.listaGruposSalarialesParaEscalafonSalarial(listaEscalafonesSalariales.get(index).getSecuencia());
                }
                if (tipoLista == 1) {
                    listaGruposSalariales = administrarEscalafonesSalariales.listaGruposSalarialesParaEscalafonSalarial(filtrarListaEscalafonesSalariales.get(index).getSecuencia());
                }
            }
        }
        return listaGruposSalariales;
    }

    public void setListaGruposSalariales(List<GruposSalariales> setListaGruposSalariales) {
        this.listaGruposSalariales = setListaGruposSalariales;
    }

    public List<GruposSalariales> getFiltrarListaGruposSalariales() {
        return filtrarListaGruposSalariales;
    }

    public void setFiltrarListaGruposSalariales(List<GruposSalariales> setFiltrarListaGruposSalariales) {
        this.filtrarListaGruposSalariales = setFiltrarListaGruposSalariales;
    }

    public List<EscalafonesSalariales> getListEscalafonesSalarialesModificar() {
        return listEscalafonesSalarialesModificar;
    }

    public void setListEscalafonesSalarialesModificar(List<EscalafonesSalariales> setListEscalafonesSalarialesModificar) {
        this.listEscalafonesSalarialesModificar = setListEscalafonesSalarialesModificar;
    }

    public List<EscalafonesSalariales> getListEscalafonesSalarialesCrear() {
        return listEscalafonesSalarialesCrear;
    }

    public void setListEscalafonesSalarialesCrear(List<EscalafonesSalariales> setListEscalafonesSalarialesCrear) {
        this.listEscalafonesSalarialesCrear = setListEscalafonesSalarialesCrear;
    }

    public List<EscalafonesSalariales> getListEscalafonesSalarialesBorrar() {
        return listEscalafonesSalarialesBorrar;
    }

    public void setListEscalafonesSalarialesBorrar(List<EscalafonesSalariales> setListEscalafonesSalarialesBorrar) {
        this.listEscalafonesSalarialesBorrar = setListEscalafonesSalarialesBorrar;
    }

    public List<GruposSalariales> getListGruposSalarialesModificar() {
        return listGruposSalarialesModificar;
    }

    public void setListGruposSalarialesModificar(List<GruposSalariales> setListGruposSalarialesModificar) {
        this.listGruposSalarialesModificar = setListGruposSalarialesModificar;
    }

    public GruposSalariales getNuevoGrupoSalarial() {
        return nuevoGrupoSalarial;
    }

    public void setNuevoGrupoSalarial(GruposSalariales setNuevoGrupoSalarial) {
        this.nuevoGrupoSalarial = setNuevoGrupoSalarial;
    }

    public List<GruposSalariales> getListGruposSalarialesCrear() {
        return listGruposSalarialesCrear;
    }

    public void setListGruposSalarialesCrear(List<GruposSalariales> setListGruposSalarialesCrear) {
        this.listGruposSalarialesCrear = setListGruposSalarialesCrear;
    }

    public List<GruposSalariales> getListGruposSalarialesBorrar() {
        return listGruposSalarialesBorrar;
    }

    public void setListGruposSalarialesBorrar(List<GruposSalariales> setListGruposSalarialesBorrar) {
        this.listGruposSalarialesBorrar = setListGruposSalarialesBorrar;
    }

    public GruposSalariales getEditarGrupoSalarial() {
        return editarGrupoSalarial;
    }

    public void setEditarGrupoSalarial(GruposSalariales setEditarGrupoSalarial) {
        this.editarGrupoSalarial = setEditarGrupoSalarial;
    }

    public GruposSalariales getDuplicarGrupoSalarial() {
        return duplicarGrupoSalarial;
    }

    public void setDuplicarGrupoSalarial(GruposSalariales setDuplicarGrupoSalarial) {
        this.duplicarGrupoSalarial = setDuplicarGrupoSalarial;
    }

    public BigInteger getSecRegistroGS() {
        return secRegistroGS;
    }

    public void setSecRegistroGS(BigInteger setSecRegistroGS) {
        this.secRegistroGS = setSecRegistroGS;
    }

    public BigInteger getBackUpSecRegistroGS() {
        return backUpSecRegistroGS;
    }

    public void setBackUpSecRegistroGS(BigInteger setBackUpSecRegistroGS) {
        this.backUpSecRegistroGS = setBackUpSecRegistroGS;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String setMsnConfirmarRastro) {
        this.msnConfirmarRastro = setMsnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        lovTiposTrabajadores = administrarEscalafonesSalariales.lovTiposTrabajadores();
        return lovTiposTrabajadores;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> setLovTiposTrabajadores) {
        this.lovTiposTrabajadores = setLovTiposTrabajadores;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> setFiltrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = setFiltrarLovTiposTrabajadores;
    }

    public TiposTrabajadores getTipoTrabajadorSeleccionado() {
        return tipoTrabajadorSeleccionado;
    }

    public void setTipoTrabajadorSeleccionado(TiposTrabajadores setTipoTrabajadorSeleccionado) {
        this.tipoTrabajadorSeleccionado = setTipoTrabajadorSeleccionado;
    }

    public String getAltoTablaEscalafon() {
        return altoTablaEscalafon;
    }

    public void setAltoTablaEscalafon(String altoTablaEscalafon) {
        this.altoTablaEscalafon = altoTablaEscalafon;
    }

    public String getAltoTablaGrupo() {
        return altoTablaGrupo;
    }

    public void setAltoTablaGrupo(String altoTablaGrupo) {
        this.altoTablaGrupo = altoTablaGrupo;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public EscalafonesSalariales getEscalafonTablaSeleccionado() {
        getListaEscalafonesSalariales();
        if (listaEscalafonesSalariales != null) {
            int tam = listaEscalafonesSalariales.size();
            if (tam > 0) {
                escalafonTablaSeleccionado = listaEscalafonesSalariales.get(0);
            }
        }
        return escalafonTablaSeleccionado;
    }

    public void setEscalafonTablaSeleccionado(EscalafonesSalariales escalafonTablaSeleccionado) {
        this.escalafonTablaSeleccionado = escalafonTablaSeleccionado;
    }

    public GruposSalariales getGrupoTablaSeleccionado() {
        getListaGruposSalariales();
        if (listaGruposSalariales != null) {
            int tam = listaGruposSalariales.size();
            if (tam > 0) {
                grupoTablaSeleccionado = listaGruposSalariales.get(0);
            }
        }
        return grupoTablaSeleccionado;
    }

    public void setGrupoTablaSeleccionado(GruposSalariales grupoTablaSeleccionado) {
        this.grupoTablaSeleccionado = grupoTablaSeleccionado;
    }

    public String getInfoRegistroTipoTrabajador() {
        getLovTiposTrabajadores();
        if (lovTiposTrabajadores != null) {
            infoRegistroTipoTrabajador = "Cantidad de registros : " + lovTiposTrabajadores.size();
        } else {
            infoRegistroTipoTrabajador = "Cantidad de registros : 0";
        }
        return infoRegistroTipoTrabajador;
    }

    public void setInfoRegistroTipoTrabajador(String infoRegistroTipoTrabajador) {
        this.infoRegistroTipoTrabajador = infoRegistroTipoTrabajador;
    }

    public boolean isDisabledNuevoGrupoSalarial() {
        return disabledNuevoGrupoSalarial;
    }

    public void setDisabledNuevoGrupoSalarial(boolean disabledNuevoGrupoSalarial) {
        this.disabledNuevoGrupoSalarial = disabledNuevoGrupoSalarial;
    }

}
