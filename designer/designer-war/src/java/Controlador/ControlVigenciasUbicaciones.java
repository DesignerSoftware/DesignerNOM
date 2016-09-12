package Controlador;

import Entidades.Empleados;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasUbicaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasUbicacionesInterface;
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

@ManagedBean
@SessionScoped
public class ControlVigenciasUbicaciones implements Serializable {

    @EJB
    AdministrarVigenciasUbicacionesInterface administrarVigenciasUbicaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cargos
    private List<VigenciasUbicaciones> vigenciasUbicaciones;
    private List<VigenciasUbicaciones> filtrarVU;
    private List<UbicacionesGeograficas> listaUbicaciones;
    private VigenciasUbicaciones vigenciaSeleccionada;
    private UbicacionesGeograficas UbicacionSelecionada;
    private List<UbicacionesGeograficas> filtradoUbicaciones;
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column vuFecha, vuDescripcion, vuCiudad;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasUbicaciones> listVUModificar;
    private boolean guardado;
    //crear VC
    public VigenciasUbicaciones nuevaVigencia;
    private List<VigenciasUbicaciones> listVUCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<VigenciasUbicaciones> listVUBorrar;
    //editar celda
    private VigenciasUbicaciones editarVU;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private VigenciasUbicaciones duplicarVU;
    //AUTOCOMPLETAR
    private boolean permitirIndex;
    private String ubicacion;
    //MENSAJE VALIDACION NUEVA VIGENCIA UBICACION
    private String mensajeValidacion;
    private String altoTabla;
    public String infoRegistro;
    //
    private String infoRegistroUbicacion;
    //CONTROL FECHA
    private Date fechaVigenciaBck;
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlVigenciasUbicaciones() {
        vigenciasUbicaciones = null;
        listaUbicaciones = new ArrayList<UbicacionesGeograficas>();
        empleado = new Empleados();
        //UbicacionSelecionada = new UbicacionesGeograficas();
        UbicacionSelecionada = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listVUBorrar = new ArrayList<VigenciasUbicaciones>();
        //crear aficiones
        listVUCrear = new ArrayList<VigenciasUbicaciones>();
        k = 0;
        //modificar aficiones
        listVUModificar = new ArrayList<VigenciasUbicaciones>();
        //editar
        editarVU = new VigenciasUbicaciones();
        cambioEditor = false;
        aceptarEditar = true;
        // cualCelda = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigencia = new VigenciasUbicaciones();
        nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
        //Autocompletar
        permitirIndex = true;
        altoTabla = "292";
        vigenciaSeleccionada = null;
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasUbicaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EMPLEADO DE LA VIGENCIA
    public void recibirEmpleado(BigInteger sec) {
        RequestContext context = RequestContext.getCurrentInstance();
        secuenciaEmpleado = sec;
        empleado = administrarVigenciasUbicaciones.buscarEmpleado(secuenciaEmpleado);
        //vigenciasUbicaciones = null;
        getVigenciasUbicaciones();
        contarRegistrosUbicaciones();
        if (vigenciasUbicaciones != null) {
            vigenciaSeleccionada = vigenciasUbicaciones.get(0);
            modificarInfoRegistro(vigenciasUbicaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void modificarVU(VigenciasUbicaciones vUbicacion, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vUbicacion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            System.out.println("valor Confirmar: " + valorConfirmar);
            if (!valorConfirmar.isEmpty()) {
                int control = 0;
                for (int i = 0; i < vigenciasUbicaciones.size(); i++) {
                    if (vigenciasUbicaciones.get(i) == vUbicacion) {
                        i++;
                        if (i >= vigenciasUbicaciones.size()) {
                            break;
                        }
                    }
                    if (vigenciaSeleccionada.getFechavigencia().compareTo(vigenciaSeleccionada.getFechavigencia()) == 0) {
                        control++;
                        vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
                    }
                }

                if (control == 0) {
                    if (!listVUCrear.contains(vigenciaSeleccionada)) {

                        if (listVUModificar.isEmpty()) {
                            listVUModificar.add(vigenciaSeleccionada);
                        } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                            listVUModificar.add(vigenciaSeleccionada);
                        }
                    }

                    if (guardado) {
                        guardado = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('validacionFechaDuplicada.show();");
                }
            } else {
                vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
                PrimefacesContextUI.ejecutar("PF('validacionFechaVacia.show();");
            }
        } else if (confirmarCambio.equalsIgnoreCase("UBICACION")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            vigenciaSeleccionada.getUbicacion().setDescripcion(ubicacion);
            for (int i = 0; i < listaUbicaciones.size(); i++) {
                if (listaUbicaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setUbicacion(listaUbicaciones.get(indiceUnicoElemento));
                listaUbicaciones.clear();
                getListaUbicaciones();
            } else {
                permitirIndex = false;
                getInfoRegistroUbicacion();
                PrimefacesContextUI.actualizar("form:UbicacionesGeograficasDialogo");
                PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listVUCrear.contains(vigenciaSeleccionada)) {

                if (listVUModificar.isEmpty()) {
                    listVUModificar.add(vigenciaSeleccionada);
                } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                    listVUModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        PrimefacesContextUI.actualizar("form:datosVUEmpleado");
    }

    //Ubicacion Celda.
    public void cambiarIndice(VigenciasUbicaciones vUbicaciones, int celda) {
        if (permitirIndex) {
            vigenciaSeleccionada = vUbicaciones;
            cualCelda = celda;
            if (cualCelda == 0) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                fechaVigenciaBck = vigenciaSeleccionada.getFechavigencia();
            }
            if (cualCelda == 1) {
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                ubicacion = vigenciaSeleccionada.getUbicacion().getDescripcion();
            }
        }
    }

    public void cambioIndiceReadOnly() {
        if (permitirIndex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            RequestContext context = RequestContext.getCurrentInstance();
            Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
            String campo = map.get("INDEX");
            int poss = Integer.parseInt(campo);
            vigenciaSeleccionada = vigenciasUbicaciones.get(poss);
            cualCelda = 2;
        }
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("UBICACION")) {
            if (tipoNuevo == 1) {
                ubicacion = nuevaVigencia.getUbicacion().getDescripcion();
            } else if (tipoNuevo == 2) {
                ubicacion = duplicarVU.getUbicacion().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("UBICACION")) {
            if (tipoNuevo == 1) {
                nuevaVigencia.getUbicacion().setDescripcion(ubicacion);
            } else if (tipoNuevo == 2) {
                duplicarVU.getUbicacion().setDescripcion(ubicacion);
            }
            for (int i = 0; i < listaUbicaciones.size(); i++) {
                if (listaUbicaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.setUbicacion(listaUbicaciones.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaDescripcion");
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    duplicarVU.setUbicacion(listaUbicaciones.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDescripcion");
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudad");
                }
                listaUbicaciones.clear();
                getListaUbicaciones();
            } else {
                PrimefacesContextUI.actualizar("form:UbicacionesGeograficasDialogo");
                PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaDescripcion");
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudad");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarDescripcion");
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudad");
                }
            }
        }
    }
    //GUARDAR

    public void guardarCambiosVU() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Tipos Contratos");
            if (!listVUBorrar.isEmpty()) {
                for (int i = 0; i < listVUBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    administrarVigenciasUbicaciones.borrarVU(listVUBorrar.get(i));
                }
                listVUBorrar.clear();
            }
            if (!listVUCrear.isEmpty()) {
                for (int i = 0; i < listVUCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarVigenciasUbicaciones.crearVU(listVUCrear.get(i));
                }
                listVUCrear.clear();
            }
            if (!listVUModificar.isEmpty()) {
                administrarVigenciasUbicaciones.modificarVU(listVUModificar);
                listVUModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
//            vigenciasUbicaciones = null;
            getVigenciasUbicaciones();
            contarRegistrosUbicaciones();
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVUEmpleado");
            guardado = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }
    //CANCELAR MODIFICACIONES

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            cerrarFiltrado();
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVUBorrar.clear();
        listVUCrear.clear();
        listVUModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        vigenciasUbicaciones = null;
        getVigenciasUbicaciones();
        contarRegistrosUbicaciones();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosVUEmpleado");
        PrimefacesContextUI.actualizar("form:ACEPTAR");

    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        vuFecha = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuFecha");
        vuFecha.setFilterStyle("display: none; visibility: hidden;");
        vuDescripcion = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuDescripcion");
        vuDescripcion.setFilterStyle("display: none; visibility: hidden;");
        vuCiudad = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuCiudad");
        vuCiudad.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "292";
        PrimefacesContextUI.actualizar("form:datosVUEmpleado");
        bandera = 0;
        filtrarVU = null;
        tipoLista = 0;

    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (vigenciaSeleccionada != null) {
            System.out.println("lol 2");
            int result = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASUBICACIONES");
            System.out.println("resultado: " + result);
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASUBICACIONES")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            editarVU = vigenciaSeleccionada;
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFecha");
                PrimefacesContextUI.ejecutar("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDescripcion");
                PrimefacesContextUI.ejecutar("PF('editarDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCiudad");
                PrimefacesContextUI.ejecutar("PF('editarCiudad').show()");
                cualCelda = -1;
            }
        }
    }

    //CREAR VU
    public void agregarNuevaVU() {
        int contador = 0;
        int fechas = 0;
        int pasa = 0;

        mensajeValidacion = " ";
        nuevaVigencia.setEmpleado(empleado);
        RequestContext context = RequestContext.getCurrentInstance();
        boolean banderaConfirmar = false;

        if (nuevaVigencia.getFechavigencia() == null || nuevaVigencia.getFechavigencia().equals("")) {
            mensajeValidacion = " *Fecha\n";
        } else {
            if (vigenciasUbicaciones != null) {
                for (int j = 0; j < vigenciasUbicaciones.size(); j++) {
                    if (nuevaVigencia.getFechavigencia().equals(vigenciasUbicaciones.get(j).getFechavigencia())) {
                        fechas++;
                    }
                }
            }
            if (fechas > 0) {
                PrimefacesContextUI.actualizar("form:fechas");
                PrimefacesContextUI.ejecutar("PF('fechas').show()");
                pasa++;

            } else {
                contador++;
            }
        }
        if (nuevaVigencia.getUbicacion().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + " * Ubicación \n";

        } else {
            contador++;
        }
        if (contador == 2 && pasa == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrado();
            }
            //AGREGAR REGISTRO A LA LISTA 
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);

            listVUCrear.add(nuevaVigencia);
            vigenciasUbicaciones.add(nuevaVigencia);
            vigenciaSeleccionada = vigenciasUbicaciones.get(vigenciasUbicaciones.indexOf(nuevaVigencia));
            modificarInfoRegistro(vigenciasUbicaciones.size());
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            nuevaVigencia = new VigenciasUbicaciones();
            nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            PrimefacesContextUI.actualizar("form:datosVUEmpleado");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVU').hide()");
        } else if (pasa == 0 && contador != 2) {
            PrimefacesContextUI.actualizar("form:validacionNuevo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevo').show()");
            contador = 0;
            pasa = 0;
        }
        PrimefacesContextUI.actualizar("form:NuevoRegistroVU");
    }

//LIMPIAR NUEVO REGISTRO}
    public void limpiarNuevaVU() {
        nuevaVigencia = new VigenciasUbicaciones();
        nuevaVigencia.setUbicacion(new UbicacionesGeograficas());
    }
    //DUPLICAR VC

    public void duplicarVigenciaU() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                duplicarVU = new VigenciasUbicaciones();
                k++;
                l = BigInteger.valueOf(k);
                duplicarVU.setSecuencia(l);
                duplicarVU.setEmpleado(vigenciaSeleccionada.getEmpleado());
                duplicarVU.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
                duplicarVU.setUbicacion(vigenciaSeleccionada.getUbicacion());


                PrimefacesContextUI.actualizar("formularioDialogos:duplicarVU");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVU').show()");
            }
        }
    }

    public void confirmarDuplicar() {

        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;
        mensajeValidacion = " ";

        for (int j = 0; j < vigenciasUbicaciones.size(); j++) {
            if (duplicarVU.getFechavigencia().equals(vigenciasUbicaciones.get(j).getFechavigencia())) {
                contador++;
            }
        }
        if (contador > 0) {
            mensajeValidacion = "Fechas NO Repetidas";

            PrimefacesContextUI.actualizar("form:validacionFechaDuplicada");
            PrimefacesContextUI.ejecutar("PF('validacionFechaDuplicada').show()");

        } else {
            vigenciasUbicaciones.add(duplicarVU);
            listVUCrear.add(duplicarVU);
            vigenciaSeleccionada = vigenciasUbicaciones.get(vigenciasUbicaciones.indexOf(duplicarVU));
            modificarInfoRegistro(vigenciasUbicaciones.size());
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            PrimefacesContextUI.actualizar("form:datosVUEmpleado");
//            vigenciaSeleccionada = null;
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrado();
            }
            duplicarVU = new VigenciasUbicaciones();
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVU').hide()");
        }

    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVU() {
        duplicarVU = new VigenciasUbicaciones();
    }

    //BORRAR VC
    public void borrarVU() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (!listVUModificar.isEmpty() && listVUModificar.contains(vigenciaSeleccionada)) {
                    int modIndex = listVUModificar.indexOf(vigenciaSeleccionada);
                    listVUModificar.remove(modIndex);
                    listVUBorrar.add(vigenciaSeleccionada);
                } else if (!listVUCrear.isEmpty() && listVUCrear.contains(vigenciaSeleccionada)) {
                    int crearIndex = listVUCrear.indexOf(vigenciaSeleccionada);
                    listVUCrear.remove(crearIndex);
                } else {
                    listVUBorrar.add(vigenciaSeleccionada);
                }
                vigenciasUbicaciones.remove(vigenciaSeleccionada);
                modificarInfoRegistro(vigenciasUbicaciones.size());
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:datosVUEmpleado");
                PrimefacesContextUI.actualizar("form:informacionRegistro");

                vigenciaSeleccionada = null;

                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            vuFecha = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuFecha");
            vuFecha.setFilterStyle("width: 85%;");
            vuDescripcion = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuDescripcion");
            vuDescripcion.setFilterStyle("width: 85%;");
            vuCiudad = (Column) c.getViewRoot().findComponent("form:datosVUEmpleado:vuCiudad");
            vuCiudad.setFilterStyle("width: 85%;");
            altoTabla = "272";
            PrimefacesContextUI.actualizar("form:datosVUEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    //SALIR
    public void salir() {
        RequestContext context = RequestContext.getCurrentInstance();
        cerrarFiltrado();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVUBorrar.clear();
        listVUCrear.clear();
        listVUModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        vigenciasUbicaciones = null;
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    public void asignarIndex(VigenciasUbicaciones vu, int LND) {
        vigenciaSeleccionada = vu;
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        UbicacionSelecionada = null;
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        modificarInfoRegistroUbicaciones(listaUbicaciones.size());
        PrimefacesContextUI.actualizar("form:UbicacionesGeograficasDialogo");
        PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').show()");
    }

    //LOVS
    //CIUDAD
    public void actualizarUbicacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setUbicacion(UbicacionSelecionada);
            if (!listVUCrear.contains(vigenciaSeleccionada)) {
                if (listVUModificar.isEmpty()) {
                    listVUModificar.add(vigenciaSeleccionada);
                } else if (!listVUModificar.contains(vigenciaSeleccionada)) {
                    listVUModificar.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.actualizar("form:datosVUEmpleado");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaVigencia.setUbicacion(UbicacionSelecionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaVU");
        } else if (tipoActualizacion == 2) {
            duplicarVU.setUbicacion(UbicacionSelecionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarVU");
        }
        filtradoUbicaciones = null;
        UbicacionSelecionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        context.reset("form:lovUbicaciones:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovUbicaciones').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').hide()");
    }

    public void cancelarCambioUbicacion() {
        filtradoUbicaciones = null;
        UbicacionSelecionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicaciones:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovUbicaciones').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (cualCelda == 1) {
                    UbicacionSelecionada = null;
                    //getInfoRegistroUbicacion();
                    modificarInfoRegistroUbicaciones(listaUbicaciones.size());
                    PrimefacesContextUI.actualizar("form:UbicacionesGeograficasDialogo");
                    PrimefacesContextUI.ejecutar("PF('UbicacionesGeograficasDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public void exportPDF() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosVUEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasUbicacionesGeograficasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosVUEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasUbicacionesGeograficasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        modificarInfoRegistro(filtrarVU.size());
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void eventoFiltrarUbicaciones() {
        modificarInfoRegistroUbicaciones(filtradoUbicaciones.size());
        PrimefacesContextUI.actualizar("form:infoRegistroUbicacion");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    private void modificarInfoRegistroUbicaciones(int valor) {
        infoRegistroUbicacion = String.valueOf(valor);
    }

    public void contarRegistrosUbicaciones() {
        if (vigenciasUbicaciones != null) {
            modificarInfoRegistro(vigenciasUbicaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVUEmpleado");
            tablaC.setSelection(vigenciaSeleccionada);
        } else {
            vigenciaSeleccionada = null;
        }
        System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
    }

    public void anularLOV() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    //GETTERS AND SETTERS
    public List<VigenciasUbicaciones> getVigenciasUbicaciones() {
        try {
            if (vigenciasUbicaciones == null) {
                vigenciasUbicaciones = administrarVigenciasUbicaciones.vigenciasUbicacionesEmpleado(secuenciaEmpleado);
            }
            return vigenciasUbicaciones;

        } catch (Exception e) {
            System.out.println("Error...!! getVigenciasUbicacionsEmpleado ");
            return null;
        }
    }

    public void setVigenciasUbicaciones(List<VigenciasUbicaciones> vigenciasUbicaciones) {
        this.vigenciasUbicaciones = vigenciasUbicaciones;
    }

    public Empleados getEmpleado() {
//        try {
//            empleado = administrarVigenciasUbicaciones.buscarEmpleado(secuenciaEmpleado);
//        } catch (Exception e) {
//            System.out.println("Error getEmpleado (ControlVigenciasUbicaciones)");
//        }
        return empleado;
    }

    public List<VigenciasUbicaciones> getFiltrarVU() {
        return filtrarVU;
    }

    public void setFiltrarVU(List<VigenciasUbicaciones> filtrarVU) {
        this.filtrarVU = filtrarVU;
    }

    public VigenciasUbicaciones getNuevaVigencia() {
        return nuevaVigencia;
    }

    public void setNuevaVigencia(VigenciasUbicaciones nuevaVigencia) {
        this.nuevaVigencia = nuevaVigencia;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<UbicacionesGeograficas> getListaUbicaciones() {
        System.out.println("listaUbicaciones: " + listaUbicaciones);
         if (listaUbicaciones == null || listaUbicaciones.isEmpty()) {
            listaUbicaciones = administrarVigenciasUbicaciones.ubicacionesGeograficas();
        }
        return listaUbicaciones;
    }

    public void setListaUbicaciones(List<UbicacionesGeograficas> listaUbicaciones) {
        this.listaUbicaciones = listaUbicaciones;
    }

    public UbicacionesGeograficas getUbicacionSelecionada() {
        return UbicacionSelecionada;
    }

    public void setUbicacionSelecionada(UbicacionesGeograficas UbicacionSelecionada) {
        this.UbicacionSelecionada = UbicacionSelecionada;
    }

    public List<UbicacionesGeograficas> getFiltradoUbicaciones() {
        return filtradoUbicaciones;
    }

    public void setFiltradoUbicaciones(List<UbicacionesGeograficas> filtradoUbicaciones) {
        this.filtradoUbicaciones = filtradoUbicaciones;
    }

    public VigenciasUbicaciones getEditarVU() {
        return editarVU;
    }

    public void setEditarVU(VigenciasUbicaciones editarVU) {
        this.editarVU = editarVU;
    }

    public VigenciasUbicaciones getDuplicarVU() {
        return duplicarVU;
    }

    public void setDuplicarVU(VigenciasUbicaciones duplicarVU) {
        this.duplicarVU = duplicarVU;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public VigenciasUbicaciones getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasUbicaciones vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public String getInfoRegistroUbicacion() {
//        getListaUbicaciones();
        return infoRegistroUbicacion;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
