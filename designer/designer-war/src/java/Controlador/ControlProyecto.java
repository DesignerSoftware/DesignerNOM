package Controlador;

import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.Proyectos;
import Entidades.PryClientes;
import Entidades.PryPlataformas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProyectosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlProyecto implements Serializable {

    @EJB
    AdministrarProyectosInterface administrarProyectos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Proyectos
    private List<Proyectos> listProyectos;
    private List<Proyectos> listProyectosLOV;
    private List<Proyectos> filtrarListProyectos;
    private List<Proyectos> filtarLovProyectos;
    private Proyectos proyectoLOVSeleccionado;
    private Proyectos proyectoTablaSeleccionado;
    //Empresas
    private List<Empresas> listEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarListEmpresas;
    //PryClientes
    private List<PryClientes> listPryClientes;
    private PryClientes clienteSeleccionado;
    private List<PryClientes> filtrarListPryClientes;
    //PryPlataformas
    private List<PryPlataformas> listPryPlataformas;
    private PryPlataformas plataformaSeleccionada;
    private List<PryPlataformas> filtrarListPryPlataformas;
    //Monedas
    private List<Monedas> listMonedas;
    private Monedas monedaSeleccionada;
    private List<Monedas> filtrarListMonedas;
    //Tipo Actualizacion
    private int tipoActualizacion;
    //Activo/Desactivo VP Crtl + F11
    private int banderaP;
    //Columnas Tabla VP
    private Column pryEmpresa, pryCodigo, pryNombre, pryCliente, pryPlataforma, pryMonto, pryMoneda, pryPersonas, pryFechaInicial, pryFechaFinal, pryDescripcion;
    //Otros
    private boolean aceptar;
    //modificar
    private List<Proyectos> listProyectoModificar;
    private boolean guardado;
    //crear VP
    public Proyectos nuevaProyectos;
    private BigInteger l;
    private int k;
    //borrar VL
    private List<Proyectos> listProyectoBorrar;
    //editar celda
    private Proyectos editarProyecto;
    //duplicar
    //Autocompletar
    private boolean permitirIndexP;
    //Variables Autompletar
    private String empresas, clientes, plataformas, monedas;
    private int cualCeldaP, tipoListaP;
    private Proyectos duplicarProyecto;
    private List<Proyectos> listProyectoCrear;
    private Date fechaInic, fechaFin;
    private String paginaAnterior;
    //
    private String auxCodigo, auxNombre;
    //
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroProyecto, infoRegistroTipoMoneda, infoRegistroPlataforma, infoRegistroCliente, infoRegistroEmpresa;
    private DataTable tablaC;
    private boolean activarLov;

    public ControlProyecto() {
        altoTabla = "300";
        proyectoLOVSeleccionado = new Proyectos();
        proyectoTablaSeleccionado = new Proyectos();
        //Otros
        aceptar = true;
        listProyectoBorrar = new ArrayList<Proyectos>();
        k = 0;
        listProyectoModificar = new ArrayList<Proyectos>();
        editarProyecto = new Proyectos();
        tipoListaP = 0;
        guardado = true;
        listProyectosLOV = null;
        banderaP = 0;
        permitirIndexP = true;
        cualCeldaP = -1;
        listPryPlataformas = null;
        listPryClientes = null;
        listEmpresas = null;
        nuevaProyectos = new Proyectos();
        nuevaProyectos.setTipomoneda(new Monedas());
        nuevaProyectos.setEmpresa(new Empresas());
        nuevaProyectos.setPryCliente(new PryClientes());
        nuevaProyectos.setPryPlataforma(new PryPlataformas());
        listProyectoCrear = new ArrayList<Proyectos>();
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarProyectos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listProyectos = null;
        getListProyectos();
        contarRegistros();
       if(listProyectos == null || listProyectos.isEmpty()){
         proyectoTablaSeleccionado = null;  
       } else {
        proyectoTablaSeleccionado = listProyectos.get(0);
       }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public boolean validarFechasRegistro(int i) {
        boolean retorno = false;
        if (i == 0) {
            if (tipoListaP == 0) {
                if (proyectoLOVSeleccionado.getFechainicial() != null && proyectoLOVSeleccionado.getFechafinal() != null) {
                    if (proyectoLOVSeleccionado.getFechainicial().before(proyectoLOVSeleccionado.getFechafinal())) {
                        retorno = true;
                    } else {
                        retorno = false;
                    }
                }
                if (proyectoLOVSeleccionado.getFechainicial() != null && proyectoLOVSeleccionado.getFechafinal() == null) {
                    retorno = true;
                }
                if (proyectoLOVSeleccionado.getFechainicial() == null && proyectoLOVSeleccionado.getFechafinal() != null) {
                    retorno = false;
                }
            }
            if (tipoListaP == 1) {
                if (proyectoLOVSeleccionado.getFechainicial() != null && proyectoLOVSeleccionado.getFechafinal() != null) {
                    if (proyectoLOVSeleccionado.getFechainicial().before(proyectoLOVSeleccionado.getFechafinal())) {
                        retorno = true;
                    } else {
                        retorno = false;
                    }
                }
                if (proyectoLOVSeleccionado.getFechainicial() != null && proyectoLOVSeleccionado.getFechafinal() == null) {
                    retorno = true;
                }
                if (proyectoLOVSeleccionado.getFechainicial() == null && proyectoLOVSeleccionado.getFechafinal() != null) {
                    retorno = false;
                }
            }

        }
        if (i == 1) {
            if (nuevaProyectos.getFechafinal() != null && nuevaProyectos.getFechainicial() != null) {
                if (nuevaProyectos.getFechainicial().before(nuevaProyectos.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevaProyectos.getFechafinal() != null && nuevaProyectos.getFechainicial() == null) {
                retorno = true;
            }
            if (nuevaProyectos.getFechafinal() == null && nuevaProyectos.getFechainicial() != null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarProyecto.getFechafinal() != null && duplicarProyecto.getFechainicial() != null) {
                if (duplicarProyecto.getFechainicial().before(duplicarProyecto.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarProyecto.getFechafinal() != null && duplicarProyecto.getFechainicial() == null) {
                retorno = true;
            }
            if (duplicarProyecto.getFechafinal() == null && duplicarProyecto.getFechainicial() != null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificacionesFechas(Proyectos proyecto, int c) {
        Proyectos auxiliar = new Proyectos();
        if (tipoListaP == 0) {
            auxiliar = proyectoTablaSeleccionado;
        }
        if (tipoListaP == 1) {
            auxiliar = proyectoTablaSeleccionado;
        }
        boolean retorno = false;
        if ((auxiliar.getFechainicial() == null) && (auxiliar.getFechafinal() == null)) {
            retorno = true;
        } else if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
            proyectoTablaSeleccionado = proyecto;
            retorno = validarFechasRegistro(0);
        } else if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() == null)) {
            retorno = true;
        } else if ((auxiliar.getFechainicial() == null) && (auxiliar.getFechafinal() != null)) {
            retorno = false;
            fechaFin = null;
            fechaInic = null;
        }
        if (retorno == true) {
            cambiarIndiceP(proyectoLOVSeleccionado, c);
            modificarProyecto(proyecto);
        } else {
            if (tipoListaP == 0) {
                proyectoLOVSeleccionado.setFechafinal(fechaFin);
                proyectoLOVSeleccionado.setFechainicial(fechaInic);
            }
            if (tipoListaP == 1) {
                proyectoLOVSeleccionado.setFechafinal(fechaFin);
                proyectoLOVSeleccionado.setFechainicial(fechaInic);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosProyectos");
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public boolean validarDatosNull(int i) {
        boolean retorno = true;
        if (i == 0) {
            Proyectos aux = null;
            if (tipoListaP == 0) {
                aux = proyectoLOVSeleccionado;
            } else {
                aux = proyectoLOVSeleccionado;
            }
            if (aux.getCodigo() == null) {
                retorno = false;
            } else {
                if (aux.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
            if (aux.getNombreproyecto() == null) {
                retorno = false;
            } else {
                if (aux.getNombreproyecto().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevaProyectos.getCodigo() == null) {
                retorno = false;
            } else {
                if (nuevaProyectos.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
            if (nuevaProyectos.getNombreproyecto() == null) {
                retorno = false;
            } else {
                if (nuevaProyectos.getNombreproyecto().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarProyecto.getCodigo() == null) {
                retorno = false;
            } else {
                if (duplicarProyecto.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
            if (duplicarProyecto.getNombreproyecto() == null) {
                retorno = false;
            } else {
                if (duplicarProyecto.getNombreproyecto().isEmpty()) {
                    retorno = false;
                }
            }
        }
        return retorno;

    }

    public void modificarProyecto(Proyectos proyecto) { //(int indice)

        if (validarDatosNull(0) == true) {
            if (tipoListaP == 0) {
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }

            } else {
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }
            }

        }
    }

    public void modificarProyecto(Proyectos proyecto, String confirmarCambio, String valorConfirmar) {
        proyectoLOVSeleccionado = proyecto;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.getEmpresa().setNombre(empresas);
                } else {
                    proyectoLOVSeleccionado.getEmpresa().setNombre(empresas);
                }
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoListaP == 0) {
                        proyectoLOVSeleccionado.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                    } else {
                        proyectoLOVSeleccionado.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                    }
                    listEmpresas = null;
                    getListEmpresas();

                } else {
                    permitirIndexP = false;
                    PrimefacesContextUI.actualizar("form:EmpresasDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                listEmpresas = null;
                getListEmpresas();
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.setEmpresa(new Empresas());
                } else {
                    proyectoLOVSeleccionado.setEmpresa(new Empresas());
                }
            }
            deshabilitarLov();
        } else if (confirmarCambio.equalsIgnoreCase("CLIENTES")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.getPryCliente().setNombre(clientes);
                } else {
                    proyectoLOVSeleccionado.getPryCliente().setNombre(clientes);
                }
                for (int i = 0; i < listPryClientes.size(); i++) {
                    if (listPryClientes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoListaP == 0) {
                        proyectoLOVSeleccionado.setPryCliente(listPryClientes.get(indiceUnicoElemento));
                    } else {
                        proyectoLOVSeleccionado.setPryCliente(listPryClientes.get(indiceUnicoElemento));
                    }
                    listPryClientes = null;
                    getListPryClientes();

                } else {
                    permitirIndexP = false;
                    PrimefacesContextUI.actualizar("form:ClientesDialogo");
                    PrimefacesContextUI.ejecutar("PF('ClientesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                listPryClientes = null;
                getListPryClientes();
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.setPryCliente(new PryClientes());
                } else {
                    proyectoLOVSeleccionado.setPryCliente(new PryClientes());
                }
            }
            deshabilitarLov();
        } else if (confirmarCambio.equalsIgnoreCase("PLATAFORMAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.getPryPlataforma().setDescripcion(plataformas);
                } else {
                    proyectoLOVSeleccionado.getPryPlataforma().setDescripcion(plataformas);
                }
                for (int i = 0; i < listPryPlataformas.size(); i++) {
                    if (listPryPlataformas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoListaP == 0) {
                        proyectoLOVSeleccionado.setPryPlataforma(listPryPlataformas.get(indiceUnicoElemento));
                    } else {
                        proyectoLOVSeleccionado.setPryPlataforma(listPryPlataformas.get(indiceUnicoElemento));
                    }
                    listPryPlataformas = null;
                    getListPryPlataformas();
                } else {
                    permitirIndexP = false;
                    PrimefacesContextUI.actualizar("form:PlataformasDialogo");
                    PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                listPryPlataformas = null;
                getListPryPlataformas();
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.setPryPlataforma(new PryPlataformas());
                } else {
                    proyectoLOVSeleccionado.setPryPlataforma(new PryPlataformas());
                }
            }
            deshabilitarLov();
        } else if (confirmarCambio.equalsIgnoreCase("MONEDAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.getTipomoneda().setNombre(monedas);
                } else {
                    proyectoLOVSeleccionado.getTipomoneda().setNombre(monedas);
                }
                for (int i = 0; i < listMonedas.size(); i++) {
                    if (listMonedas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoListaP == 0) {
                        proyectoLOVSeleccionado.setTipomoneda(listMonedas.get(indiceUnicoElemento));
                    } else {
                        proyectoLOVSeleccionado.setTipomoneda(listMonedas.get(indiceUnicoElemento));
                    }

                    listMonedas = null;
                    getListMonedas();
                } else {
                    permitirIndexP = false;
                    PrimefacesContextUI.actualizar("form:MonedasDialogo");
                    PrimefacesContextUI.ejecutar("PF('MonedasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                listMonedas = null;
                getListMonedas();
                if (tipoListaP == 0) {
                    proyectoLOVSeleccionado.setTipomoneda(new Monedas());
                } else {
                    proyectoLOVSeleccionado.setTipomoneda(new Monedas());
                }
            }
            deshabilitarLov();
        }
        if (coincidencias == 1) {
            if (tipoListaP == 0) {
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {

                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }

            } else {
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {

                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                }

            }
            deshabilitarLov();
        }
        PrimefacesContextUI.actualizar("form:datosProyectos");
    }

    public void valoresBackupAutocompletarProyecto(int tipoNuevo, String Campo) {

        if (Campo.equals("EMPRESAS")) {
            if (tipoNuevo == 1) {
                empresas = nuevaProyectos.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                empresas = duplicarProyecto.getEmpresa().getNombre();
            }
        } else if (Campo.equals("CLIENTES")) {
            if (tipoNuevo == 1) {
                clientes = nuevaProyectos.getPryCliente().getNombre();
            } else if (tipoNuevo == 2) {
                clientes = duplicarProyecto.getPryCliente().getNombre();
            }
        } else if (Campo.equals("PLATAFORMAS")) {
            if (tipoNuevo == 1) {
                plataformas = nuevaProyectos.getPryPlataforma().getDescripcion();
            } else if (tipoNuevo == 2) {
                plataformas = duplicarProyecto.getPryPlataforma().getDescripcion();
            }
        } else if (Campo.equals("MONEDAS")) {
            if (tipoNuevo == 1) {
                monedas = nuevaProyectos.getTipomoneda().getNombre();
            } else if (tipoNuevo == 2) {
                monedas = duplicarProyecto.getTipomoneda().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoProyecto(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaProyectos.getEmpresa().setNombre(empresas);
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.getEmpresa().setNombre(empresas);
                }
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaProyectos.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresaP");
                    } else if (tipoNuevo == 2) {
                        duplicarProyecto.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresaP");
                    }
                    listEmpresas = null;
                    getListEmpresas();
                } else {
                    PrimefacesContextUI.actualizar("form:EmpresasDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresaP");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresaP");
                    }
                }
            } else {
                listEmpresas = null;
                getListEmpresas();
                if (tipoNuevo == 1) {
                    nuevaProyectos.setEmpresa(new Empresas());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresaP");
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.setEmpresa(new Empresas());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresaP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CLIENTES")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaProyectos.getPryCliente().setNombre(clientes);
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.getPryCliente().setNombre(clientes);
                }
                for (int i = 0; i < listPryClientes.size(); i++) {
                    if (listPryClientes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaProyectos.setPryCliente(listPryClientes.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaClienteP");
                    } else if (tipoNuevo == 2) {
                        duplicarProyecto.setPryCliente(listPryClientes.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarClienteP");
                    }
                    listPryClientes = null;
                    getListPryClientes();
                } else {
                    PrimefacesContextUI.actualizar("form:ClientesDialogo");
                    PrimefacesContextUI.ejecutar("PF('ClientesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaClienteP");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarClienteP");
                    }
                }
            } else {
                listPryClientes = null;
                getListPryClientes();
                if (tipoNuevo == 1) {
                    nuevaProyectos.setPryCliente(new PryClientes());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaClienteP");
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.setPryCliente(new PryClientes());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarClienteP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PLATAFORMAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaProyectos.getPryPlataforma().setDescripcion(clientes);
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.getPryPlataforma().setDescripcion(clientes);
                }
                for (int i = 0; i < listPryPlataformas.size(); i++) {
                    if (listPryPlataformas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaProyectos.setPryPlataforma(listPryPlataformas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaPlataformaP");
                    } else {
                        duplicarProyecto.setPryPlataforma(listPryPlataformas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarPlataformaP");
                    }
                    listPryPlataformas = null;
                    getListPryPlataformas();
                } else {
                    PrimefacesContextUI.actualizar("form:PlataformasDialogo");
                    PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaPlataformaP");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarPlataformaP");
                    }
                }
            } else {
                listPryPlataformas = null;
                getListPryPlataformas();
                if (tipoNuevo == 1) {
                    nuevaProyectos.setPryPlataforma(new PryPlataformas());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaPlataformaP");
                } else {
                    duplicarProyecto.setPryPlataforma(new PryPlataformas());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarPlataformaP");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("MONEDAS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaProyectos.getTipomoneda().setNombre(monedas);
                } else if (tipoNuevo == 2) {
                    duplicarProyecto.getTipomoneda().setNombre(monedas);
                }
                for (int i = 0; i < listMonedas.size(); i++) {
                    if (listMonedas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaProyectos.setTipomoneda(listMonedas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaMonedaP");
                    } else if (tipoNuevo == 2) {
                        duplicarProyecto.setTipomoneda(listMonedas.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarMonedaP");
                    }
                    listMonedas = null;
                    getListMonedas();
                } else {
                    PrimefacesContextUI.actualizar("form:PlataformasDialogo");
                    PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaMonedaP");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarMonedaP");
                    }
                }
            }
        } else {
            listMonedas = null;
            getListMonedas();
            if (tipoNuevo == 1) {
                nuevaProyectos.setTipomoneda(new Monedas());
                PrimefacesContextUI.actualizar("formularioDialogos:nuevaMonedaP");
            } else if (tipoNuevo == 2) {
                duplicarProyecto.setTipomoneda(new Monedas());
                PrimefacesContextUI.actualizar("formularioDialogos:duplicarMonedaP");
            }
        }
    }

    public void cambiarIndiceP(Proyectos proyecto, int celda) {
        if (permitirIndexP == true) {
            proyectoLOVSeleccionado = proyecto;
            cualCeldaP = celda;
            if (tipoListaP == 0) {
                auxCodigo = proyectoLOVSeleccionado.getCodigo();
                auxNombre = proyectoLOVSeleccionado.getNombreproyecto();
                fechaFin = proyectoLOVSeleccionado.getFechafinal();
                fechaInic = proyectoLOVSeleccionado.getFechainicial();
                proyectoLOVSeleccionado.getSecuencia();
                empresas = proyectoLOVSeleccionado.getEmpresa().getNombre();
                clientes = proyectoLOVSeleccionado.getPryCliente().getNombre();
                plataformas = proyectoLOVSeleccionado.getPryPlataforma().getDescripcion();
                monedas = proyectoLOVSeleccionado.getTipomoneda().getNombre();
                deshabilitarLov();
                if (cualCeldaP == 0) {
                    contarRegistros();
                    //deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
                    habilitarLov();
                }
                if(cualCeldaP ==3){
                    contarRegistros();
                    habilitarLov();
                }
                if(cualCeldaP ==4){
                    contarRegistros();
                    habilitarLov();
                }
                if(cualCeldaP == 6){
                    contarRegistros();
                    habilitarLov();
                }
            }
            if (tipoListaP == 1) {
                auxCodigo = proyectoLOVSeleccionado.getCodigo();
                auxNombre = proyectoLOVSeleccionado.getNombreproyecto();
                fechaFin = proyectoLOVSeleccionado.getFechafinal();
                fechaInic = proyectoLOVSeleccionado.getFechainicial();
                proyectoLOVSeleccionado.getSecuencia();
                empresas = proyectoLOVSeleccionado.getEmpresa().getNombre();
                clientes = proyectoLOVSeleccionado.getPryCliente().getNombre();
                plataformas = proyectoLOVSeleccionado.getPryPlataforma().getDescripcion();
                monedas = proyectoLOVSeleccionado.getTipomoneda().getNombre();
                deshabilitarLov();
                if (cualCeldaP == 0) {
                    contarRegistros();
                    //deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
                    habilitarLov();
                }
                if(cualCeldaP ==3){
                    contarRegistros();
                    habilitarLov();
                }
                if(cualCeldaP ==4){
                    contarRegistros();
                    habilitarLov();
                }
                if(cualCeldaP == 6){
                    contarRegistros();
                    habilitarLov();
                }
            }

        }
    }

    public void guardadoGeneral() {
        guardarCambiosP();
    }

    public void guardarCambiosP() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listProyectoBorrar.isEmpty()) {
                    administrarProyectos.borrarProyectos(listProyectoBorrar);
                    listProyectoBorrar.clear();
                }
                if (!listProyectoCrear.isEmpty()) {
                    administrarProyectos.crearProyectos(listProyectoCrear);
                    listProyectoCrear.clear();
                }
                if (!listProyectoModificar.isEmpty()) {
                    administrarProyectos.editarProyectos(listProyectoModificar);
                    listProyectoModificar.clear();
                }

                listProyectos = null;
                listProyectosLOV = null;
                getListProyectos();
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                PrimefacesContextUI.actualizar("form:infoRegistro");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
                contarRegistros();
                proyectoTablaSeleccionado = null;
                deshabilitarLov();
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:datosProyectos");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosP : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }

    }

    public void cancelarModificacionP() {
        if (banderaP == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "300";
            pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
            pryEmpresa.setFilterStyle("display: none; visibility: hidden;");
            pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
            pryCodigo.setFilterStyle("display: none; visibility: hidden;");
            pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
            pryNombre.setFilterStyle("display: none; visibility: hidden;");
            pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
            pryCliente.setFilterStyle("display: none; visibility: hidden;");
            pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
            pryPlataforma.setFilterStyle("display: none; visibility: hidden;");
            pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
            pryMonto.setFilterStyle("display: none; visibility: hidden;");
            pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
            pryMoneda.setFilterStyle("display: none; visibility: hidden;");
            pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
            pryPersonas.setFilterStyle("display: none; visibility: hidden;");
            pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
            pryFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
            pryFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
            pryDescripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProyectos");
            banderaP = 0;
            filtrarListProyectos = null;
            tipoListaP = 0;
        }
        listPryPlataformas = null;
        listPryClientes = null;
        listEmpresas = null;
        listProyectoBorrar.clear();
        listProyectoCrear.clear();
        listProyectoModificar.clear();
        k = 0;
        listProyectos = null;
        proyectoTablaSeleccionado = null;
        proyectoLOVSeleccionado = null;
        nuevaProyectos = new Proyectos();
        nuevaProyectos.setTipomoneda(new Monedas());
        nuevaProyectos.setEmpresa(new Empresas());
        nuevaProyectos.setPryCliente(new PryClientes());
        nuevaProyectos.setPryPlataforma(new PryPlataformas());
        RequestContext context = RequestContext.getCurrentInstance();
        getListProyectos();
        contarRegistros();
        deshabilitarLov();
        PrimefacesContextUI.actualizar("form:infoRegistro");
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosProyectos");
    }

    public void editarCelda() {
        if (proyectoLOVSeleccionado != null) {
            if (tipoListaP == 0) {
                editarProyecto = proyectoLOVSeleccionado;
            }
            if (tipoListaP == 1) {
                editarProyecto = proyectoLOVSeleccionado;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaP == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEmpresaPD");
                PrimefacesContextUI.ejecutar("PF('editarEmpresaPD').show()");
                cualCeldaP = -1;
                habilitarLov();
            } else if (cualCeldaP == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCodigoPD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNombrePD");
                PrimefacesContextUI.ejecutar("PF('editarNombrePD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarClientePD");
                PrimefacesContextUI.ejecutar("PF('editarClientePD').show()");
                habilitarLov();
                cualCeldaP = -1;
            } else if (cualCeldaP == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPlataformaPD");
                PrimefacesContextUI.ejecutar("PF('editarPlataformaPD').show()");
                habilitarLov();
                cualCeldaP = -1;
            } else if (cualCeldaP == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarMontoPD");
                PrimefacesContextUI.ejecutar("PF('editarMontoPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 6) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarMonedaPD");
                PrimefacesContextUI.ejecutar("PF('editarMonedaPD').show()");
                cualCeldaP = -1;
                habilitarLov();
            } else if (cualCeldaP == 7) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPersonasPD");
                PrimefacesContextUI.ejecutar("PF('editarPersonasPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 8) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaInicialPD");
                PrimefacesContextUI.ejecutar("PF('editarFechaInicialPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 9) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaFinalPD");
                PrimefacesContextUI.ejecutar("PF('editarFechaFinalPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            } else if (cualCeldaP == 10) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDescripcionPD");
                PrimefacesContextUI.ejecutar("PF('editarDescripcionPD').show()");
                cualCeldaP = -1;
                deshabilitarLov();
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevaP() {
        if (validarFechasRegistro(1) == true) {
            if (validarDatosNull(1) == true) {
                if (banderaP == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTabla = "300";
                    pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
                    pryEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
                    pryCodigo.setFilterStyle("display: none; visibility: hidden;");
                    pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
                    pryNombre.setFilterStyle("display: none; visibility: hidden;");
                    pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
                    pryCliente.setFilterStyle("display: none; visibility: hidden;");
                    pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
                    pryPlataforma.setFilterStyle("display: none; visibility: hidden;");
                    pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
                    pryMonto.setFilterStyle("display: none; visibility: hidden;");
                    pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
                    pryMoneda.setFilterStyle("display: none; visibility: hidden;");
                    pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
                    pryPersonas.setFilterStyle("display: none; visibility: hidden;");
                    pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
                    pryFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
                    pryFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
                    pryDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    PrimefacesContextUI.actualizar("form:datosProyectos");
                    banderaP = 0;
                    filtrarListProyectos = null;
                    tipoListaP = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevaProyectos.setSecuencia(l);
                if (listProyectos == null) {
                    listProyectos = new ArrayList<Proyectos>();
                }
                listProyectos.add(nuevaProyectos);
                listProyectoCrear.add(nuevaProyectos);
                proyectoTablaSeleccionado = nuevaProyectos;
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistro(listProyectos.size());
                deshabilitarLov();
                PrimefacesContextUI.actualizar("form:infoRegistro");
                nuevaProyectos = new Proyectos();
                nuevaProyectos.setTipomoneda(new Monedas());
                nuevaProyectos.setEmpresa(new Empresas());
                nuevaProyectos.setPryCliente(new PryClientes());
                nuevaProyectos.setPryPlataforma(new PryPlataformas());
                limpiarNuevaP();
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                PrimefacesContextUI.actualizar("form:datosProyectos");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroP').hide()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorDatosNull').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public void limpiarNuevaP() {
        nuevaProyectos = new Proyectos();
        nuevaProyectos.setTipomoneda(new Monedas());
        nuevaProyectos.setEmpresa(new Empresas());
        nuevaProyectos.setPryCliente(new PryClientes());
        nuevaProyectos.setPryPlataforma(new PryPlataformas());
        proyectoTablaSeleccionado = null;
        

    }

    public void cancelarNuevaP() {
        nuevaProyectos = new Proyectos();
        nuevaProyectos.setTipomoneda(new Monedas());
        nuevaProyectos.setEmpresa(new Empresas());
        nuevaProyectos.setPryCliente(new PryClientes());
        nuevaProyectos.setPryPlataforma(new PryPlataformas());

    }

    public void verificarDuplicarProyecto() {
        if (proyectoLOVSeleccionado != null) {
            duplicarProyectoM();
        }
    }

    public void duplicarProyectoM() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (proyectoLOVSeleccionado != null) {
            duplicarProyecto = new Proyectos();
            if (tipoListaP == 0) {
                duplicarProyecto.setFechafinal(proyectoLOVSeleccionado.getFechafinal());
                duplicarProyecto.setFechainicial(proyectoLOVSeleccionado.getFechainicial());
                duplicarProyecto.setEmpresa(proyectoLOVSeleccionado.getEmpresa());
                duplicarProyecto.setNombreproyecto(proyectoLOVSeleccionado.getNombreproyecto());
                duplicarProyecto.setCodigo(proyectoLOVSeleccionado.getCodigo());
                duplicarProyecto.setPryCliente(proyectoLOVSeleccionado.getPryCliente());
                duplicarProyecto.setPryPlataforma(proyectoLOVSeleccionado.getPryPlataforma());
                duplicarProyecto.setMonto(proyectoLOVSeleccionado.getMonto());
                duplicarProyecto.setTipomoneda(proyectoLOVSeleccionado.getTipomoneda());
                duplicarProyecto.setDescripcionproyecto(proyectoLOVSeleccionado.getDescripcionproyecto());
                deshabilitarLov();
            }
            if (tipoListaP == 1) {
                duplicarProyecto.setFechafinal(proyectoLOVSeleccionado.getFechafinal());
                duplicarProyecto.setFechainicial(proyectoLOVSeleccionado.getFechainicial());
                duplicarProyecto.setEmpresa(proyectoLOVSeleccionado.getEmpresa());
                duplicarProyecto.setNombreproyecto(proyectoLOVSeleccionado.getNombreproyecto());
                duplicarProyecto.setCodigo(proyectoLOVSeleccionado.getCodigo());
                duplicarProyecto.setPryCliente(proyectoLOVSeleccionado.getPryCliente());
                duplicarProyecto.setPryPlataforma(proyectoLOVSeleccionado.getPryPlataforma());
                duplicarProyecto.setMonto(proyectoLOVSeleccionado.getMonto());
                duplicarProyecto.setTipomoneda(proyectoLOVSeleccionado.getTipomoneda());
                duplicarProyecto.setDescripcionproyecto(proyectoLOVSeleccionado.getDescripcionproyecto());
                deshabilitarLov();
            }
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarP");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroP').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarP() {
        if (validarFechasRegistro(2) == true) {
            if (validarDatosNull(2) == true) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarProyecto.setSecuencia(l);
                if (listProyectos == null) {
                    listProyectos = new ArrayList<Proyectos>();
                }
                listProyectos.add(duplicarProyecto);
                listProyectoCrear.add(duplicarProyecto);
                proyectoTablaSeleccionado = duplicarProyecto;
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                if (banderaP == 1) {
                    altoTabla = "300";
                    FacesContext c = FacesContext.getCurrentInstance();
                    //CERRAR FILTRADO
                    pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
                    pryEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
                    pryCodigo.setFilterStyle("display: none; visibility: hidden;");
                    pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
                    pryNombre.setFilterStyle("display: none; visibility: hidden;");
                    pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
                    pryCliente.setFilterStyle("display: none; visibility: hidden;");
                    pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
                    pryPlataforma.setFilterStyle("display: none; visibility: hidden;");
                    pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
                    pryMonto.setFilterStyle("display: none; visibility: hidden;");
                    pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
                    pryMoneda.setFilterStyle("display: none; visibility: hidden;");
                    pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
                    pryPersonas.setFilterStyle("display: none; visibility: hidden;");
                    pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
                    pryFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
                    pryFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
                    pryDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    PrimefacesContextUI.actualizar("form:datosProyectos");
                    banderaP = 0;
                    filtrarListProyectos = null;
                    tipoListaP = 0;
                }
                duplicarProyecto = new Proyectos();
                limpiarduplicarP();
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistro(listProyectos.size());
                deshabilitarLov();
                PrimefacesContextUI.actualizar("form:infoRegistro");
                PrimefacesContextUI.actualizar("form:datosProyectos");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroP').hide()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorDatosNull').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public void limpiarduplicarP() {
        duplicarProyecto = new Proyectos();
        duplicarProyecto.setTipomoneda(new Monedas());
        duplicarProyecto.setEmpresa(new Empresas());
        duplicarProyecto.setPryCliente(new PryClientes());
        duplicarProyecto.setPryPlataforma(new PryPlataformas());
    }

    public void cancelarDuplicadoP() {
        duplicarProyecto = new Proyectos();
        duplicarProyecto.setTipomoneda(new Monedas());
        duplicarProyecto.setEmpresa(new Empresas());
        duplicarProyecto.setPryCliente(new PryClientes());
        duplicarProyecto.setPryPlataforma(new PryPlataformas());
    }

    public void validarBorradoProyecto() {
        if (proyectoLOVSeleccionado != null) {
            borrarP();
        }
    }

    public void borrarP() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (proyectoTablaSeleccionado != null) {
                    if (!listProyectoModificar.isEmpty() && listProyectoModificar.contains(proyectoTablaSeleccionado)) {
                        listProyectoModificar.remove(listProyectoModificar.indexOf(proyectoTablaSeleccionado));
                        listProyectoBorrar.add(proyectoTablaSeleccionado);
                    } else if (!listProyectoCrear.isEmpty() && listProyectoCrear.contains(proyectoTablaSeleccionado)) {
                        listProyectoCrear.remove(listProyectoCrear.indexOf(proyectoTablaSeleccionado));
                    } else {
                        listProyectoBorrar.add(proyectoTablaSeleccionado);
                    }
                    listProyectos.remove(proyectoTablaSeleccionado);

                if (tipoListaP == 1) {
                    filtrarListProyectos.remove(proyectoTablaSeleccionado);
                }
                modificarInfoRegistro(listProyectos.size());
                deshabilitarLov();
                PrimefacesContextUI.actualizar("form:infoRegistro");
                PrimefacesContextUI.actualizar("form:datosProyectos");
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } catch (Exception e) {
            PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
            System.err.println("ERROR controlProyecto BorrarP " + e);
        }

    }

    public void activarCtrlF11() {
        filtradoProyecto();
    }

    public void filtradoProyecto() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaP == 0) {
            altoTabla = "280";
            pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
            pryEmpresa.setFilterStyle("width: 85%");
            pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
            pryCodigo.setFilterStyle("width: 85%");
            pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
            pryNombre.setFilterStyle("width: 85%");
            pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
            pryCliente.setFilterStyle("width: 85%");
            pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
            pryPlataforma.setFilterStyle("width: 85%");
            pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
            pryMonto.setFilterStyle("width: 85%");
            pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
            pryMoneda.setFilterStyle("width: 85%");
            pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
            pryPersonas.setFilterStyle("width: 85%");
            pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
            pryFechaInicial.setFilterStyle("width: 85%");
            pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
            pryFechaFinal.setFilterStyle("width: 85%");
            pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
            pryDescripcion.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosProyectos");
            tipoListaP = 1;
            banderaP = 1;
        } else if (banderaP == 1) {
            altoTabla = "303";
            pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
            pryEmpresa.setFilterStyle("display: none; visibility: hidden;");
            pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
            pryCodigo.setFilterStyle("display: none; visibility: hidden;");
            pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
            pryNombre.setFilterStyle("display: none; visibility: hidden;");
            pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
            pryCliente.setFilterStyle("display: none; visibility: hidden;");
            pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
            pryPlataforma.setFilterStyle("display: none; visibility: hidden;");
            pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
            pryMonto.setFilterStyle("display: none; visibility: hidden;");
            pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
            pryMoneda.setFilterStyle("display: none; visibility: hidden;");
            pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
            pryPersonas.setFilterStyle("display: none; visibility: hidden;");
            pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
            pryFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
            pryFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
            pryDescripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProyectos");
            banderaP = 0;
            filtrarListProyectos = null;
            tipoListaP = 0;
        }

    }

    public void salir() {
        if (banderaP == 1) {
            altoTabla = "300";
            FacesContext c = FacesContext.getCurrentInstance();
            pryEmpresa = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryEmpresa");
            pryEmpresa.setFilterStyle("display: none; visibility: hidden;");
            pryCodigo = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCodigo");
            pryCodigo.setFilterStyle("display: none; visibility: hidden;");
            pryNombre = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryNombre");
            pryNombre.setFilterStyle("display: none; visibility: hidden;");
            pryCliente = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryCliente");
            pryCliente.setFilterStyle("display: none; visibility: hidden;");
            pryPlataforma = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPlataforma");
            pryPlataforma.setFilterStyle("display: none; visibility: hidden;");
            pryMonto = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMonto");
            pryMonto.setFilterStyle("display: none; visibility: hidden;");
            pryMoneda = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryMoneda");
            pryMoneda.setFilterStyle("display: none; visibility: hidden;");
            pryPersonas = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryPersonas");
            pryPersonas.setFilterStyle("display: none; visibility: hidden;");
            pryFechaInicial = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaInicial");
            pryFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            pryFechaFinal = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryFechaFinal");
            pryFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            pryDescripcion = (Column) c.getViewRoot().findComponent("form:datosProyectos:pryDescripcion");
            pryDescripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosProyectos");
            banderaP = 0;
            filtrarListProyectos = null;
            tipoListaP = 0;
        }
        listProyectoBorrar.clear();
        listProyectoCrear.clear();
        listProyectoModificar.clear();
        deshabilitarLov();
        proyectoTablaSeleccionado = null;
        k = 0;
        listProyectos = null;
        guardado = true;
        contarRegistros();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        tipoActualizacion = -1;
    }

    public void asignarIndex(Proyectos proyecto, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            proyectoLOVSeleccionado = proyecto;
            tipoActualizacion = 0;
            habilitarLov();
        } else if (LND == 1) {
            tipoActualizacion = 1;
            deshabilitarLov();
        } else if (LND == 2) {
            tipoActualizacion = 2;
            deshabilitarLov();
        }
        if (dlg == 0) {
            modificarInfoRegistroEmpresa(listEmpresas.size());
            PrimefacesContextUI.actualizar("form:EmpresasDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
        } else if (dlg == 1) {
            modificarInfoRegistroCliente(listPryClientes.size());
            PrimefacesContextUI.actualizar("form:ClientesDialogo");
            PrimefacesContextUI.ejecutar("PF('ClientesDialogo').show()");
        } else if (dlg == 2) {
            modificarInfoRegistroPlataforma(listPryPlataformas.size());
            PrimefacesContextUI.actualizar("form:PlataformasDialogo");
            PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').show()");
        } else if (dlg == 3) {
            modificarInfoRegistroTipoMoneda(listMonedas.size());
            PrimefacesContextUI.actualizar("form:MonedasDialogo");
            PrimefacesContextUI.ejecutar("PF('MonedasDialogo').show()");
        }

    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaP == 0) {
                proyectoLOVSeleccionado.setEmpresa(empresaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }

                permitirIndexP = true;

            } else {
                proyectoLOVSeleccionado.setEmpresa(empresaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }

                permitirIndexP = true;

            }
            PrimefacesContextUI.actualizar("form:datosProyectos");
        } else if (tipoActualizacion == 1) {
            nuevaProyectos.setEmpresa(empresaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaEmpresaP");
        } else if (tipoActualizacion == 2) {
            duplicarProyecto.setEmpresa(empresaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresaP");
        }
        filtrarListEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        PrimefacesContextUI.actualizar("form:EmpresasDialogo");
        PrimefacesContextUI.actualizar("form:lovEmpresas");
        PrimefacesContextUI.actualizar("form:aceptarE");

        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarListEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
    }

    public void actualizarCliente() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaP == 0) {
                proyectoLOVSeleccionado.setPryCliente(clienteSeleccionado);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            } else {
                proyectoLOVSeleccionado.setPryCliente(clienteSeleccionado);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            permitirIndexP = true;
            PrimefacesContextUI.actualizar("form:datosProyectos");
        } else if (tipoActualizacion == 1) {
            nuevaProyectos.setPryCliente(clienteSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaClienteP");
        } else if (tipoActualizacion == 2) {
            duplicarProyecto.setPryCliente(clienteSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarClienteP");
        }
        filtrarListPryClientes = null;
        clienteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        PrimefacesContextUI.actualizar("form:ClientesDialogo");
        PrimefacesContextUI.actualizar("form:aceptarC");
        PrimefacesContextUI.actualizar("form:datosProyectos");

        context.reset("form:lovClientes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ClientesDialogo').hide()");
    }

    /**
     * Metodo que cancela la seleccion del proyecto (vigencia localizacion)
     */
    public void cancelarCambioCliente() {
        filtrarListPryClientes = null;
        clienteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovClientes:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ClientesDialogo').hide()");
    }

    public void actualizarPlataforma() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaP == 0) {
                proyectoLOVSeleccionado.setPryPlataforma(plataformaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            } else {
                proyectoLOVSeleccionado.setPryPlataforma(plataformaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            permitirIndexP = true;
            PrimefacesContextUI.actualizar("form:datosProyectos");
        } else if (tipoActualizacion == 1) {
            nuevaProyectos.setPryPlataforma(plataformaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaPlataformaP");
        } else if (tipoActualizacion == 2) {
            duplicarProyecto.setPryPlataforma(plataformaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarPlataformaP");
        }
        filtrarListPryPlataformas = null;
        plataformaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        PrimefacesContextUI.actualizar("form:PlataformasDialogo");
        PrimefacesContextUI.actualizar("form:lovPlataforma");
        PrimefacesContextUI.actualizar("form:aceptarP");

        context.reset("form:lovPlataforma:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('form:PlataformasDialogo");
    }

    public void cancelarCambioPlataforma() {
        filtrarListPryPlataformas = null;
        plataformaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPlataforma:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').hide()");
    }

    public void actualizarMoneda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaP == 0) {
                proyectoLOVSeleccionado.setTipomoneda(monedaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            } else {
                proyectoLOVSeleccionado.setTipomoneda(monedaSeleccionada);
                if (!listProyectoCrear.contains(proyectoLOVSeleccionado)) {
                    if (listProyectoModificar.isEmpty()) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    } else if (!listProyectoModificar.contains(proyectoLOVSeleccionado)) {
                        listProyectoModificar.add(proyectoLOVSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            permitirIndexP = true;
            PrimefacesContextUI.actualizar("form:datosProyectos");
        } else if (tipoActualizacion == 1) {
            nuevaProyectos.setTipomoneda(monedaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaMonedaP");
        } else if (tipoActualizacion == 2) {
            duplicarProyecto.setTipomoneda(monedaSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarMonedaP");
        }
        filtrarListMonedas = null;
        monedaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        /*
         PrimefacesContextUI.actualizar("form:MonedasDialogo");
         PrimefacesContextUI.actualizar("form:lovMoneda");
         PrimefacesContextUI.actualizar("form:aceptarM");*/
        context.reset("form:lovMoneda:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('MonedasDialogo').hide()");
    }

    public void cancelarCambioMoneda() {
        filtrarListMonedas = null;
        monedaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMoneda:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovClientes').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('MonedasDialogo').hide()");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (proyectoLOVSeleccionado != null) {
            if (cualCeldaP == 0) {
                modificarInfoRegistroEmpresa(listEmpresas.size());
                habilitarLov();
                PrimefacesContextUI.actualizar("form:EmpresasDialogo");
                PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                tipoActualizacion = 0;
                
            }
            if (cualCeldaP == 3) {
                modificarInfoRegistroCliente(listPryClientes.size());
                habilitarLov();
                PrimefacesContextUI.actualizar("form:ClientesDialogo");
                PrimefacesContextUI.ejecutar("PF('ClientesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaP == 4) {
                modificarInfoRegistroPlataforma(listPryPlataformas.size());
                habilitarLov();
                PrimefacesContextUI.actualizar("form:PlataformasDialogo");
                PrimefacesContextUI.ejecutar("PF('PlataformasDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaP == 6) {
                modificarInfoRegistroTipoMoneda(listMonedas.size());
                habilitarLov();
                PrimefacesContextUI.actualizar("form:MonedasDialogo");
                PrimefacesContextUI.ejecutar("PF('MonedasDialogo').show()");
                tipoActualizacion = 0;
            }
        }

    }

    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        exportPDF_P();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF_P() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProyectoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ProyectosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        proyectoTablaSeleccionado = null;
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        exportXLS_P();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Afiliaciones
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS_P() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProyectoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ProyectosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        proyectoTablaSeleccionado = null;
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoListaP == 0) {
            tipoListaP = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(filtrarListProyectos.size());
        PrimefacesContextUI.actualizar("form:infoRegistro");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInfoRegistroCliente(int valor) {
        infoRegistroCliente = String.valueOf(valor);
    }

    public void modificarInfoRegistroPlataforma(int valor) {
        infoRegistroPlataforma = String.valueOf(valor);
    }

    public void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
    }

    public void modificarInfoRegistroTipoMoneda(int valor) {
        infoRegistroTipoMoneda = String.valueOf(valor);
    }

    public void modificarInfoRegistroProyecto(int valor) {
        infoRegistroProyecto = String.valueOf(valor);
    }

    public void eventoFiltrarClientes() {
        modificarInfoRegistroCliente(filtrarListPryClientes.size());
        PrimefacesContextUI.actualizar("form:infoRegistroCliente");
    }

    public void eventoFiltrarPlataforma() {
        modificarInfoRegistroPlataforma(filtrarListPryPlataformas.size());
        PrimefacesContextUI.actualizar("form:infoRegistroPlataforma");
    }

    public void eventoFiltrarEmpresa() {
        modificarInfoRegistroEmpresa(filtrarListEmpresas.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEmpresa");
    }

    public void eventoFiltrarTipoMoneda() {
        modificarInfoRegistroTipoMoneda(filtrarListMonedas.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTipoMoneda");
    }

    public void eventoFiltrarProyecto() {
        modificarInfoRegistroProyecto(filtarLovProyectos.size());
        PrimefacesContextUI.actualizar("form:infoRegistroProyecto");
    }

    public void habilitarLov() {
        activarLov = false;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void deshabilitarLov() {
        activarLov = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void contarRegistros() {
        if (listProyectos != null) {
            modificarInfoRegistro(listProyectos.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void dialogoProyecto() {
        filtrarListProyectos = null;
        proyectoLOVSeleccionado = null;
        if (listProyectos != null) {
            if (listProyectosLOV == null) {
                listProyectosLOV = new ArrayList<Proyectos>();
                for (int i = 0; i < listProyectos.size(); i++) {
                    listProyectosLOV.add(listProyectos.get(i));
                }
                modificarInfoRegistroProyecto(listProyectosLOV.size());
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:BuscarProyectoDialogo");
        PrimefacesContextUI.ejecutar("PF('BuscarProyectoDialogo').show()");
    }

    public void actualizarProyecto() {
        listProyectos = null;
        filtrarListProyectos = null;
        listProyectos = new ArrayList<Proyectos>();
        listProyectos.add(proyectoLOVSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        proyectoLOVSeleccionado = null;
        modificarInfoRegistro(listProyectos.size());//infoRegistro = "Cantidad de registros : " + listProyectos.size();
        PrimefacesContextUI.actualizar("form:infoRegistro");
        PrimefacesContextUI.actualizar("form:datosProyectos");
        PrimefacesContextUI.actualizar("form:BuscarProyectoDialogo");
        PrimefacesContextUI.actualizar("form:lovProyecto");
        PrimefacesContextUI.actualizar("form:aceptarPro");
        context.reset("form:lovProyecto:globalFilter");
        PrimefacesContextUI.ejecutar("PF('BuscarProyectoDialogo').hide()");
    }

    public void cancelarActualizarProyecto() {
        RequestContext context = RequestContext.getCurrentInstance();
        proyectoLOVSeleccionado = null;
        aceptar = true;
        filtrarListProyectos = null;
        PrimefacesContextUI.actualizar("form:infoRegistro");
        PrimefacesContextUI.actualizar("form:datosProyectos");
        PrimefacesContextUI.actualizar("form:BuscarProyectoDialogo");
        PrimefacesContextUI.actualizar("form:lovProyecto");
        PrimefacesContextUI.actualizar("form:aceptarPro");
        context.reset("form:lovProyecto:globalFilter");
        PrimefacesContextUI.ejecutar("PF('BuscarProyectoDialogo').hide()");
    }

    public void mostrarTodos() {
        listProyectos = new ArrayList<Proyectos>();
        filtrarListProyectos = null;
        if (listProyectosLOV != null) {
            for (int i = 0; i < listProyectosLOV.size(); i++) {
                listProyectos.add(listProyectosLOV.get(i));
            }
        } else {
            listProyectos = administrarProyectos.Proyectos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(listProyectos.size());
        PrimefacesContextUI.actualizar("form:infoRegistro");
        PrimefacesContextUI.actualizar("form:datosProyectos");
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
         if (proyectoLOVSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(proyectoLOVSeleccionado.getSecuencia(), "PROYECTOS");
            if (resultado == 1) {
                PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("PROYECTOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
    }

    public void recordarSeleccionProyecto() {
        if (proyectoTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosProyectos");
            tablaC.setSelection(proyectoTablaSeleccionado);
        }
    }

    //GET - SET 
    public List<Proyectos> getListProyectos() {
        try {
            if (listProyectos == null) {
                listProyectos = administrarProyectos.Proyectos();
            }
            return listProyectos;
        } catch (Exception e) {
            System.out.println("Error en getListProyectos : " + e.toString());
            return null;
        }
    }

    public void setListProyectos(List<Proyectos> setListProyectos) {
        this.listProyectos = setListProyectos;
    }

    public List<Proyectos> getFiltrarListProyectos() {
        return filtrarListProyectos;
    }

    public void setFiltrarListProyectos(List<Proyectos> filtrar) {
        this.filtrarListProyectos = filtrar;
    }

    public List<Empresas> getListEmpresas() {
        try {
            listEmpresas = administrarProyectos.listEmpresas();
            return listEmpresas;
        } catch (Exception e) {
            System.out.println("Error getListEmpresas " + e.toString());
            return null;
        }
    }

    public void setListEmpresas(List<Empresas> setListEmpresas) {
        this.listEmpresas = setListEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas seleccionado) {
        this.empresaSeleccionada = seleccionado;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> setFiltrarListEmpresas) {
        this.filtrarListEmpresas = setFiltrarListEmpresas;
    }

    public List<PryClientes> getListPryClientes() {
        try {
            listPryClientes = administrarProyectos.listPryClientes();
            return listPryClientes;
        } catch (Exception e) {
            System.out.println("Error getListPryClientes " + e.toString());
            return null;
        }
    }

    public void setListPryClientes(List<PryClientes> setListPryClientes) {
        this.listPryClientes = setListPryClientes;
    }

    public PryClientes getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(PryClientes seleccionado) {
        this.clienteSeleccionado = seleccionado;
    }

    public List<PryClientes> getFiltrarListPryClientes() {
        return filtrarListPryClientes;
    }

    public void setFiltrarListPryClientes(List<PryClientes> setFiltrarListPryClientes) {
        this.filtrarListPryClientes = setFiltrarListPryClientes;
    }

    public List<Proyectos> getListProyectoModificar() {
        return listProyectoModificar;
    }

    public void setListProyectoModificar(List<Proyectos> setListProyectoModificar) {
        this.listProyectoModificar = setListProyectoModificar;
    }

    public Proyectos getNuevaProyectos() {
        return nuevaProyectos;
    }

    public void setNuevaProyectos(Proyectos setNuevaProyectos) {
        this.nuevaProyectos = setNuevaProyectos;
    }

    public List<Proyectos> getListProyectoBorrar() {
        return listProyectoBorrar;
    }

    public void setListProyectoBorrar(List<Proyectos> setListProyectoBorrar) {
        this.listProyectoBorrar = setListProyectoBorrar;
    }

    public Proyectos getEditarProyecto() {
        return editarProyecto;
    }

    public void setEditarProyecto(Proyectos setEditarProyecto) {
        this.editarProyecto = setEditarProyecto;
    }

    public Proyectos getDuplicarProyecto() {
        return duplicarProyecto;
    }

    public void setDuplicarProyecto(Proyectos setDuplicarProyecto) {
        this.duplicarProyecto = setDuplicarProyecto;
    }

    public List<Proyectos> getListProyectoCrear() {
        return listProyectoCrear;
    }

    public void setListProyectoCrear(List<Proyectos> setListProyectoCrear) {
        this.listProyectoCrear = setListProyectoCrear;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<PryPlataformas> getListPryPlataformas() {
        try {
            listPryPlataformas = administrarProyectos.listPryPlataformas();
            return listPryPlataformas;
        } catch (Exception e) {
            System.out.println("Error getListPryPlataformas : " + e.toString());
            return null;
        }

    }

    public void setListPryPlataformas(List<PryPlataformas> setListPryPlataformas) {
        this.listPryPlataformas = setListPryPlataformas;
    }

    public PryPlataformas getPlataformaSeleccionada() {
        return plataformaSeleccionada;
    }

    public void setPlataformaSeleccionada(PryPlataformas setPlataformaSeleccionada) {
        this.plataformaSeleccionada = setPlataformaSeleccionada;
    }

    public List<PryPlataformas> getFiltrarListPryPlataformas() {
        return filtrarListPryPlataformas;
    }

    public void setFiltrarListPryPlataformas(List<PryPlataformas> setFiltrarListPryPlataformas) {
        this.filtrarListPryPlataformas = setFiltrarListPryPlataformas;
    }

    public List<Monedas> getListMonedas() {
        listMonedas = administrarProyectos.listMonedas();
        return listMonedas;
    }

    public void setListMonedas(List<Monedas> listMonedas) {
        this.listMonedas = listMonedas;
    }

    public Monedas getMonedaSeleccionada() {
        return monedaSeleccionada;
    }

    public void setMonedaSeleccionada(Monedas monedaSeleccionada) {
        this.monedaSeleccionada = monedaSeleccionada;
    }

    public List<Monedas> getFiltrarListMonedas() {
        return filtrarListMonedas;
    }

    public void setFiltrarListMonedas(List<Monedas> filtrarListMonedas) {
        this.filtrarListMonedas = filtrarListMonedas;
    }

    public List<Proyectos> getFiltarLovProyectos() {
        return filtarLovProyectos;
    }

    public void setFiltarLovProyectos(List<Proyectos> filtarLovProyectos) {
        this.filtarLovProyectos = filtarLovProyectos;
    }
    
    public Proyectos getProyectoLOVSeleccionado() {
        return proyectoLOVSeleccionado;
    }

    public void setProyectoLOVSeleccionado(Proyectos proyectoSeleccionado) {
        this.proyectoLOVSeleccionado = proyectoSeleccionado;
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

    public String getInfoRegistroProyecto() {
        return infoRegistroProyecto;
    }

    public void setInfoRegistroProyecto(String infoRegistroProyecto) {
        this.infoRegistroProyecto = infoRegistroProyecto;
    }

    public String getInfoRegistroTipoMoneda() {
        return infoRegistroTipoMoneda;
    }

    public void setInfoRegistroTipoMoneda(String infoRegistroTipoMoneda) {
        this.infoRegistroTipoMoneda = infoRegistroTipoMoneda;
    }

    public String getInfoRegistroPlataforma() {
        return infoRegistroPlataforma;
    }

    public void setInfoRegistroPlataforma(String infoRegistroPlataforma) {
        this.infoRegistroPlataforma = infoRegistroPlataforma;
    }

    public List<Proyectos> getListProyectosLOV() {
        return listProyectosLOV;
    }

    public void setListProyectosLOV(List<Proyectos> listProyectosLOV) {
        this.listProyectosLOV = listProyectosLOV;
    }

    public String getInfoRegistroCliente() {
        return infoRegistroCliente;
    }

    public void setInfoRegistroCliente(String infoRegistroCliente) {
        this.infoRegistroCliente = infoRegistroCliente;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public Proyectos getProyectoTablaSeleccionado() {
        return proyectoTablaSeleccionado;
    }

    public void setProyectoTablaSeleccionado(Proyectos proyectoTablaSeleccionado) {
        this.proyectoTablaSeleccionado = proyectoTablaSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
