/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Eventos;
import Entidades.VigenciasDeportes;
import Entidades.VigenciasEventos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplVigenciaEventoInterface;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplVigenciaEvento implements Serializable {

    @EJB
    AdministrarEmplVigenciaEventoInterface administrarEmplVigenciaEvento;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    ////
    private List<VigenciasEventos> listVigenciasEventos;
    private List<VigenciasEventos> filtrarListVigenciasEventos;
    private VigenciasEventos vigenciaTablaSeleccionada;

    private List<Eventos> listEventos;
    private Eventos eventoSeleccionado;
    private List<Eventos> filtrarListEventos;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column veFechaInicial, veFechaFinal, veDescripcion, veIndividual, veCIndividual, veGrupal, veCGrupal;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasEventos> listVigenciaEventoModificar;
    private boolean guardado;
    //crear VC
    public VigenciasEventos nuevaVigenciaEvento;
    private List<VigenciasEventos> listVigenciaEventoCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<VigenciasEventos> listVigenciaEventoBorrar;
    //editar celda
    private VigenciasEventos editarVigenciaEvento;
    private int cualCelda, tipoLista;
    //duplicar
    private VigenciasEventos duplicarVigenciaEvento;
    private String evento;
    private boolean permitirIndex, activarLov;
    private BigInteger backUpSecRegistro;
    private Empleados empleado;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    //
    private DataTable tablaC;
    private String altoTabla;
    private String infoRegistro, infoRegistroEvento;

    public ControlEmplVigenciaEvento() {
        altoTabla = "272";
        listVigenciasEventos = null;
        listEventos = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listVigenciaEventoBorrar = new ArrayList<VigenciasEventos>();
        //crear aficiones
        listVigenciaEventoCrear = new ArrayList<VigenciasEventos>();
        k = 0;
        //modificar aficiones
        listVigenciaEventoModificar = new ArrayList<VigenciasEventos>();
        //editar
        editarVigenciaEvento = new VigenciasEventos();
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigenciaEvento = new VigenciasEventos();
        nuevaVigenciaEvento.setEvento(new Eventos());
        vigenciaTablaSeleccionada = null;
        permitirIndex = true;
        backUpSecRegistro = null;
        empleado = new Empleados();
        activarLov = true;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmplVigenciaEvento.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {
        listVigenciasEventos = null;
        listEventos = null;
        empleado = administrarEmplVigenciaEvento.empleadoActual(secuencia);
        getListVigenciasEventos();
        contarRegistros();
        deshabilitarBotonLov();
        if (!listVigenciasEventos.isEmpty()) {
            vigenciaTablaSeleccionada = listVigenciasEventos.get(0);
        }
    }

    public void modificarVigenciaEvento(VigenciasEventos vigenciaEvento) {
        vigenciaTablaSeleccionada = vigenciaEvento;
        if (tipoLista == 0) {
            if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {

                if (listVigenciaEventoModificar.isEmpty()) {
                    listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                    listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else {
            if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {

                if (listVigenciaEventoModificar.isEmpty()) {
                    listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                    listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
           
        }
    }

    public void modificarVigenciaEvento(VigenciasEventos vigenciaEvento, String confirmarCambio, String valorConfirmar) {
        vigenciaTablaSeleccionada = vigenciaEvento;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        deshabilitarBotonLov();
        if (confirmarCambio.equalsIgnoreCase("EVENTOS")) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.getEvento().setDescripcion(evento);
            } else {
                vigenciaTablaSeleccionada.getEvento().setDescripcion(evento);
            }
            for (int i = 0; i < listEventos.size(); i++) {
                if (listEventos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setEvento(listEventos.get(indiceUnicoElemento));
                } else {
                    vigenciaTablaSeleccionada.setEvento(listEventos.get(indiceUnicoElemento));
                }
                listEventos.clear();
                getListEventos();
            } else {
                permitirIndex = false;
                context.update("form:EventosDialogo");
                context.execute("EventosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {

                    if (listVigenciaEventoModificar.isEmpty()) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else {
                if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {

                    if (listVigenciaEventoModificar.isEmpty()) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            }
        }
        context.update("form:datosVigenciaEventos");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("EVENTOS")) {
            if (tipoNuevo == 1) {
                evento = nuevaVigenciaEvento.getEvento().getDescripcion();
            } else if (tipoNuevo == 2) {
                evento = duplicarVigenciaEvento.getEvento().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EVENTOS")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaEvento.getEvento().setDescripcion(evento);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaEvento.getEvento().setDescripcion(evento);
            }
            for (int i = 0; i < listEventos.size(); i++) {
                if (listEventos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaEvento.setEvento(listEventos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaEvento.setEvento(listEventos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarVigencias");
                }
                listEventos.clear();
                getListEventos();
            } else {
                context.update("form:EventosDialogo");
                context.execute("EventosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarVigencias");
                }
            }
        }
    }

    public void cambiarIndice(VigenciasEventos vigenciaEvento, int celda) {
        if (permitirIndex == true) {
            vigenciaTablaSeleccionada = vigenciaEvento;
            cualCelda = celda;
            if (tipoLista == 0) {
                deshabilitarBotonLov();
                fechaFin = vigenciaTablaSeleccionada.getFechafinal();
                fechaIni = vigenciaTablaSeleccionada.getFechainicial();
                vigenciaTablaSeleccionada.getSecuencia();
                if (cualCelda == 2) {
                    habilitarBotonLov();
                    evento = vigenciaTablaSeleccionada.getEvento().getDescripcion();
                }
            }
            if (tipoLista == 1) {
                deshabilitarBotonLov();
                fechaFin = vigenciaTablaSeleccionada.getFechafinal();
                fechaIni = vigenciaTablaSeleccionada.getFechainicial();
                vigenciaTablaSeleccionada.getSecuencia();
                if (cualCelda == 2) {
                    habilitarBotonLov();
                    evento = vigenciaTablaSeleccionada.getEvento().getDescripcion();
                }
            }

        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasEventos auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = vigenciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                auxiliar = vigenciaTablaSeleccionada;
            }
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
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
            if (nuevaVigenciaEvento.getFechafinal() != null) {
                if (nuevaVigenciaEvento.getFechainicial().after(fechaParametro) && nuevaVigenciaEvento.getFechainicial().before(nuevaVigenciaEvento.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                if (nuevaVigenciaEvento.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarVigenciaEvento.getFechafinal() != null) {
                if (duplicarVigenciaEvento.getFechainicial().after(fechaParametro) && duplicarVigenciaEvento.getFechainicial().before(duplicarVigenciaEvento.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                if (duplicarVigenciaEvento.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechas(VigenciasEventos vigenciaEvento, int c) {
        VigenciasEventos auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (tipoLista == 1) {
            auxiliar = vigenciaTablaSeleccionada;
        }
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            if (auxiliar.getFechafinal() == null) {
                retorno = true;
            }
            if (auxiliar.getFechafinal() != null) {
                vigenciaTablaSeleccionada = vigenciaEvento;
                retorno = validarFechasRegistro(0);
            }
            if (retorno == true) {
                cambiarIndice(vigenciaEvento, c);
                modificarVigenciaEvento(vigenciaEvento);
            } else {
                if (tipoLista == 0) {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);
                } else {
                    vigenciaTablaSeleccionada.setFechafinal(fechaFin);
                    vigenciaTablaSeleccionada.setFechainicial(fechaIni);

                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosVigenciaEventos");
                context.execute("form:errorFechas.show()");
            }
        } else {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);
            } else {
                vigenciaTablaSeleccionada.setFechainicial(fechaIni);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciaEventos");
            context.execute("errorRegNew.show()");
        }
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listVigenciaEventoBorrar.isEmpty()) {
                    administrarEmplVigenciaEvento.borrarVigenciasEventos(listVigenciaEventoBorrar);
                    listVigenciaEventoBorrar.clear();
                }
                if (!listVigenciaEventoCrear.isEmpty()) {
                    administrarEmplVigenciaEvento.crearVigenciasEventos(listVigenciaEventoCrear);
                    listVigenciaEventoCrear.clear();
                }
                if (!listVigenciaEventoModificar.isEmpty()) {
                    administrarEmplVigenciaEvento.editarVigenciasEventos(listVigenciaEventoModificar);
                    listVigenciaEventoModificar.clear();
                }
                listVigenciasEventos = null;
                getListVigenciasEventos();
                contarRegistros();
                deshabilitarBotonLov();
                context.update("form:informacionRegistro");
                context.update("form:datosVigenciaEventos");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                vigenciaTablaSeleccionada = null;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "272";
            veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
            bandera = 0;
            filtrarListVigenciasEventos = null;
            tipoLista = 0;
        }

        listVigenciaEventoBorrar.clear();
        listVigenciaEventoCrear.clear();
        listVigenciaEventoModificar.clear();
        vigenciaTablaSeleccionada = null;
        k = 0;
        listVigenciasEventos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext context = RequestContext.getCurrentInstance();
        getListVigenciasEventos();
        contarRegistros();
        deshabilitarBotonLov();
        context.update("form:informacionRegistro");
        context.update("form:datosVigenciaEventos");
    }

    public void editarCelda() {
        if (vigenciaTablaSeleccionada != null) {
            if (tipoLista == 0) {
                editarVigenciaEvento = vigenciaTablaSeleccionada;
            }
            if (tipoLista == 1) {
                editarVigenciaEvento = vigenciaTablaSeleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarFechaInicialD");
                context.execute("editarFechaInicialD.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarFechaFinalD");
                context.execute("editarFechaFinalD.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                modificarInfoRegistroEventos(listEventos.size());
                context.update("formularioDialogos:editarDescripcionD");
                context.execute("editarDescripcionD.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarIndividualD");
                context.execute("editarIndividualD.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarCIndividualD");
                context.execute("editarCIndividualD.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarGrupalD");
                context.execute("editarGrupalD.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                deshabilitarBotonLov();
                context.update("formularioDialogos:editarCGrupalD");
                context.execute("editarCGrupalD.show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("form:seleccionarRegistro.show()");
        }
    }

    public void agregarNuevaVigenciaEvento() {
        if (nuevaVigenciaEvento.getFechainicial() != null && nuevaVigenciaEvento.getEvento().getSecuencia() != null) {
            if (validarFechasRegistro(1) == true) {
                if (bandera == 1) {
                    //CERRAR FILTRADO
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTabla = "272";
                    veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
                    veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
                    veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
                    veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
                    veIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
                    veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
                    veGrupal.setFilterStyle("display: none; visibility: hidden;");
                    veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
                    veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
                    bandera = 0;
                    filtrarListVigenciasEventos = null;
                    tipoLista = 0;
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
                k++;
                l = BigInteger.valueOf(k);
                nuevaVigenciaEvento.setSecuencia(l);
                nuevaVigenciaEvento.setEmpleado(empleado);
                listVigenciaEventoCrear.add(nuevaVigenciaEvento);
                listVigenciasEventos.add(nuevaVigenciaEvento);
                vigenciaTablaSeleccionada = nuevaVigenciaEvento;
                nuevaVigenciaEvento = new VigenciasEventos();
                nuevaVigenciaEvento.setEvento(new Eventos());
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistro(listVigenciasEventos.size());
                context.update("form:informacionRegistro");
                context.update("form:datosVigenciaEventos");
                context.execute("NuevoRegistroVigencias.hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechas.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNew.show()");
        }
    }

    public void limpiarNuevaVigenciaEvento() {
        nuevaVigenciaEvento = new VigenciasEventos();
        nuevaVigenciaEvento.setEvento(new Eventos());
    }

    public void duplicarVigenciaEventoM() {
        if (vigenciaTablaSeleccionada != null) {
            duplicarVigenciaEvento = new VigenciasEventos();
            if (tipoLista == 0) {
                duplicarVigenciaEvento.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaEvento.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaEvento.setEmpleado(vigenciaTablaSeleccionada.getEmpleado());
                duplicarVigenciaEvento.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaEvento.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaEvento.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaEvento.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());
                duplicarVigenciaEvento.setEvento(vigenciaTablaSeleccionada.getEvento());
            }
            if (tipoLista == 1) {
                duplicarVigenciaEvento.setFechafinal(vigenciaTablaSeleccionada.getFechafinal());
                duplicarVigenciaEvento.setFechainicial(vigenciaTablaSeleccionada.getFechainicial());
                duplicarVigenciaEvento.setEmpleado(vigenciaTablaSeleccionada.getEmpleado());
                duplicarVigenciaEvento.setValorcualitativo(vigenciaTablaSeleccionada.getValorcualitativo());
                duplicarVigenciaEvento.setValorcualitativogrupo(vigenciaTablaSeleccionada.getValorcualitativogrupo());
                duplicarVigenciaEvento.setValorcuantitativo(vigenciaTablaSeleccionada.getValorcuantitativo());
                duplicarVigenciaEvento.setValorcuantitativogrupo(vigenciaTablaSeleccionada.getValorcuantitativogrupo());
                duplicarVigenciaEvento.setEvento(vigenciaTablaSeleccionada.getEvento());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarVigencias");
            context.execute("DuplicarRegistroVigencias.show()");
        } else {
            RequestContext.getCurrentInstance().execute("form:seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar() {
        if (duplicarVigenciaEvento.getFechainicial() != null && duplicarVigenciaEvento.getEvento().getSecuencia() != null) {
            if (validarFechasRegistro(2) == true) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarVigenciaEvento.setSecuencia(l);
                duplicarVigenciaEvento.setEmpleado(empleado);
                listVigenciasEventos.add(duplicarVigenciaEvento);
                listVigenciaEventoCrear.add(duplicarVigenciaEvento);
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistro(listVigenciasEventos.size());
                context.update("form:informacionRegistro");
                context.update("form:datosVigenciaEventos");
                context.execute("DuplicarRegistroVigencias.hide()");
                vigenciaTablaSeleccionada = duplicarVigenciaEvento;

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    //CERRAR FILTRADO
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTabla = "272";
                    veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
                    veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
                    veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
                    veDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
                    veIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
                    veCIndividual.setFilterStyle("display: none; visibility: hidden;");
                    veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
                    veGrupal.setFilterStyle("display: none; visibility: hidden;");
                    veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
                    veCGrupal.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
                    bandera = 0;
                    filtrarListVigenciasEventos = null;
                    tipoLista = 0;
                }
                duplicarVigenciaEvento = new VigenciasEventos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechas.show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNew.show()");
        }
    }

    public void limpiarDuplicar() {
        duplicarVigenciaEvento = new VigenciasEventos();
        duplicarVigenciaEvento.setEvento(new Eventos());
    }

    public void borrarVigenciaEvento() {

        if (vigenciaTablaSeleccionada != null) {
            if (!listVigenciaEventoModificar.isEmpty() && listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                int modIndex = listVigenciaEventoModificar.indexOf(vigenciaTablaSeleccionada);
                listVigenciaEventoModificar.remove(modIndex);
                listVigenciaEventoBorrar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaEventoCrear.isEmpty() && listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {
                int crearIndex = listVigenciaEventoCrear.indexOf(vigenciaTablaSeleccionada);
                listVigenciaEventoCrear.remove(crearIndex);
            } else {
                listVigenciaEventoBorrar.add(vigenciaTablaSeleccionada);
            }
            listVigenciasEventos.remove(vigenciaTablaSeleccionada);
            if (tipoLista == 1) {
                filtrarListVigenciasEventos.remove(vigenciaTablaSeleccionada);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(listVigenciasEventos.size());
            context.update("form:informacionRegistro");
            context.update("form:datosVigenciaEventos");
            vigenciaTablaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("form:seleccionarRegistro.show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "252";
            veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
            veFechaInicial.setFilterStyle("width: 85%");
            veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
            veFechaFinal.setFilterStyle("width: 85%");
            veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
            veDescripcion.setFilterStyle("width: 85%");
            veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
            veIndividual.setFilterStyle("width: 85%");
            veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
            veCIndividual.setFilterStyle("width: 85%");
            veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
            veGrupal.setFilterStyle("width: 85%");
            veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
            veCGrupal.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "272";
            veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
            bandera = 0;
            filtrarListVigenciasEventos = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "272";
            veFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaInicial");
            veFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            veFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veFechaFinal");
            veFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            veDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veDescripcion");
            veDescripcion.setFilterStyle("display: none; visibility: hidden;");
            veIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veIndividual");
            veIndividual.setFilterStyle("display: none; visibility: hidden;");
            veCIndividual = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCIndividual");
            veCIndividual.setFilterStyle("display: none; visibility: hidden;");
            veGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veGrupal");
            veGrupal.setFilterStyle("display: none; visibility: hidden;");
            veCGrupal = (Column) c.getViewRoot().findComponent("form:datosVigenciaEventos:veCGrupal");
            veCGrupal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaEventos");
            bandera = 0;
            filtrarListVigenciasEventos = null;
            tipoLista = 0;
        }

        listVigenciaEventoBorrar.clear();
        listVigenciaEventoCrear.clear();
        listVigenciaEventoModificar.clear();
        vigenciaTablaSeleccionada = null;
        k = 0;
        listVigenciasEventos = null;
        getListVigenciasEventos();
        contarRegistros();
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void asignarIndex(VigenciasEventos vigenciaEvento, int LND) {
        vigenciaTablaSeleccionada = vigenciaEvento;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        habilitarBotonLov();
        modificarInfoRegistroEventos(listEventos.size());
        context.update("form:EventosDialogo");
        context.execute("EventosDialogo.show()");
    }

    public void actualizarEvento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaTablaSeleccionada.setEvento(eventoSeleccionado);
                if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaEventoModificar.isEmpty()) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            } else {
                vigenciaTablaSeleccionada.setEvento(eventoSeleccionado);
                if (!listVigenciaEventoCrear.contains(vigenciaTablaSeleccionada)) {
                    if (listVigenciaEventoModificar.isEmpty()) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    } else if (!listVigenciaEventoModificar.contains(vigenciaTablaSeleccionada)) {
                        listVigenciaEventoModificar.add(vigenciaTablaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;

            context.update("form:datosVigenciaEventos");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaEvento.setEvento(eventoSeleccionado);
            context.update("formularioDialogos:nuevaVigencias");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaEvento.setEvento(eventoSeleccionado);
            context.update("formularioDialogos:duplicarVigencias");
        }
        filtrarListEventos = null;
        eventoSeleccionado = null;
        aceptar = true;
        //vigenciaTablaSeleccionada = null;
        tipoActualizacion = -1;

        context.reset("form:lovEventos:globalFilter");
        context.execute("lovEventos.clearFilters()");
        context.execute("EventosDialogo.hide()");
        context.update("form:EventosDialogo");
        context.update("form:lovEventos");
        context.update("form:aceptarE");

    }

    public void cancelarCambioEvento() {
        filtrarListEventos = null;
        eventoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();

        context.reset("form:lovEventos:globalFilter");
        context.execute("lovEventos.clearFilters()");
        context.execute("EventosDialogo.hide()");
        context.update("form:EventosDialogo");
        context.update("form:lovEventos");
        context.update("form:aceptarE");
    }

    public void listaValoresBoton() {
        if (vigenciaTablaSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                habilitarBotonLov();
                modificarInfoRegistroEventos(listEventos.size());
                context.update("form:EventosDialogo");
                context.execute("EventosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasEventosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        vigenciaTablaSeleccionada = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasEventosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        vigenciaTablaSeleccionada = null;
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (vigenciaTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaTablaSeleccionada.getSecuencia(), "VIGENCIASEVENTOS");
            backUpSecRegistro = vigenciaTablaSeleccionada.getSecuencia();
            vigenciaTablaSeleccionada = null;
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASEVENTOS")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(filtrarListVigenciasEventos.size());
        context.update("form:informacionRegistro");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInfoRegistroEventos(int valor) {
        infoRegistroEvento = String.valueOf(valor);
    }

    public void eventoFiltrarEventos() {  /// evento filtrar LOV deportes
        modificarInfoRegistroEventos(filtrarListEventos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroEvento");
    }

    public void contarRegistros() {
        if (listVigenciasEventos != null) {
            modificarInfoRegistro(listVigenciasEventos.size());
        } else {
            modificarInfoRegistro(0);
        }

    }

    public void recordarSeleccion() {
        if (vigenciaTablaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaEventos");
            tablaC.setSelection(vigenciaTablaSeleccionada);
        }
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

//////////////////////////////SETS Y GETS////////////////////////////
    public List<VigenciasEventos> getListVigenciasEventos() {
        try {
            if (listVigenciasEventos == null) {
                if (empleado.getSecuencia() != null) {
                    listVigenciasEventos = administrarEmplVigenciaEvento.listVigenciasEventosEmpleado(empleado.getSecuencia());
                }
                return listVigenciasEventos;
            } else {
                return listVigenciasEventos;
            }
        } catch (Exception e) {
            System.out.println("Error...!! getListVigenciasEventos : " + e.toString());
            return null;
        }
    }

    public void setListVigenciasEventos(List<VigenciasEventos> setListVigenciasEventos) {
        this.listVigenciasEventos = setListVigenciasEventos;
    }

    public List<VigenciasEventos> getFiltrarListVigenciasEventos() {
        return filtrarListVigenciasEventos;
    }

    public void setFiltrarListVigenciasEventos(List<VigenciasEventos> setFiltrarListVigenciasEventos) {
        this.filtrarListVigenciasEventos = setFiltrarListVigenciasEventos;
    }

    public VigenciasEventos getNuevaVigenciaEvento() {
        return nuevaVigenciaEvento;
    }

    public void setNuevaVigenciaEvento(VigenciasEventos setNuevaVigenciaEvento) {
        this.nuevaVigenciaEvento = setNuevaVigenciaEvento;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<Eventos> getListEventos() {
        listEventos = administrarEmplVigenciaEvento.listEventos();
        return listEventos;
    }

    public void setListEventos(List<Eventos> setListEventos) {
        this.listEventos = setListEventos;
    }

    public List<Eventos> getFiltrarListEventos() {
        return filtrarListEventos;
    }

    public void setFiltrarListEventos(List<Eventos> setFiltrarListEventos) {
        this.filtrarListEventos = setFiltrarListEventos;
    }

    public VigenciasEventos getEditarVigenciaEvento() {
        return editarVigenciaEvento;
    }

    public void setEditarVigenciaEvento(VigenciasEventos setEditarVigenciaEvento) {
        this.editarVigenciaEvento = setEditarVigenciaEvento;
    }

    public VigenciasEventos getDuplicarVigenciaEvento() {
        return duplicarVigenciaEvento;
    }

    public void setDuplicarVigenciaEvento(VigenciasEventos setDuplicarVigenciaEvento) {
        this.duplicarVigenciaEvento = setDuplicarVigenciaEvento;
    }

    public Eventos getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    public void setEventoSeleccionado(Eventos setEventoSeleccionado) {
        this.eventoSeleccionado = setEventoSeleccionado;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public VigenciasEventos getVigenciaTablaSeleccionada() {
        return vigenciaTablaSeleccionada;
    }

    public void setVigenciaTablaSeleccionada(VigenciasEventos vigenciaTablaSeleccionada) {
        this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
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

    public String getInfoRegistroEvento() {
        return infoRegistroEvento;
    }

    public void setInfoRegistroEvento(String infoRegistroEvento) {
        this.infoRegistroEvento = infoRegistroEvento;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
