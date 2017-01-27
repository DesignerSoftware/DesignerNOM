/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIdiomaPersonaInterface;
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
public class ControlIdiomaPersona implements Serializable {

    @EJB
    AdministrarIdiomaPersonaInterface administrarIdiomaPersona;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cargos
    private List<IdiomasPersonas> listIdiomasPersonas;
    private List<IdiomasPersonas> filtrarListIdiomasPersonas;
    private IdiomasPersonas idiomaTablaSeleccionado;

    private List<Idiomas> listIdiomas;
    private Idiomas idiomaSeleccionado;
    private List<Idiomas> filtrarListIdiomas;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column idIdioma, idConversacion, idLectura, idEscritura;
    //Otros
    private boolean aceptar;
    //modificar
    private List<IdiomasPersonas> listIdiomaPersonaModificar;
    private boolean guardado;
    //crear VC
    public IdiomasPersonas nuevaIdiomaPersona;
    private List<IdiomasPersonas> listIdiomaPersonaCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<IdiomasPersonas> listIdiomaPersonaBorrar;
    //editar celda
    private IdiomasPersonas editarIdiomaPersona;
    private int cualCelda, tipoLista;
    //duplicar
    private IdiomasPersonas duplicarIdiomaPersona;
    private String idioma;
    private boolean permitirIndex, activarLov;
    private BigInteger backUpSecRegistro;
    private Empleados empleadoActual;
    //
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroIdioma;
    private DataTable tablaC;
    private IdiomasPersonas secuencia;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlIdiomaPersona() {
        altoTabla = "304";
        listIdiomasPersonas = null;
        listIdiomas = null;
        aceptar = true;
        listIdiomaPersonaBorrar = new ArrayList<IdiomasPersonas>();
        listIdiomaPersonaCrear = new ArrayList<IdiomasPersonas>();
        secuencia = new IdiomasPersonas();
        k = 0;
        listIdiomaPersonaModificar = new ArrayList<IdiomasPersonas>();
        editarIdiomaPersona = new IdiomasPersonas();
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevaIdiomaPersona = new IdiomasPersonas();
        empleadoActual = new Empleados();
        nuevaIdiomaPersona.setIdioma(new Idiomas());
        idiomaTablaSeleccionado = null;
        permitirIndex = true;
        backUpSecRegistro = null;
        activarLov = true;
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
        } else {
            String pagActual = "idiomapersona";
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
            administrarIdiomaPersona.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secuencia) {
        listIdiomasPersonas = null;
        listIdiomas = null;
        empleadoActual = administrarIdiomaPersona.empleadoActual(secuencia);
        getListIdiomasPersonas();
        deshabilitarBotonLov();
        if (!listIdiomasPersonas.isEmpty()) {
            idiomaTablaSeleccionado = listIdiomasPersonas.get(0);
        }
    }

    public void modificarIdiomaPersona(IdiomasPersonas idiomaPersona) {
        idiomaTablaSeleccionado = idiomaPersona;
        if (!listIdiomaPersonaCrear.contains(idiomaTablaSeleccionado)) {
            if (listIdiomaPersonaModificar.isEmpty()) {
                listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
            } else if (!listIdiomaPersonaModificar.contains(idiomaTablaSeleccionado)) {
                listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("IDIOMAS")) {
            if (tipoNuevo == 1) {
                idioma = nuevaIdiomaPersona.getIdioma().getNombre();
            } else if (tipoNuevo == 2) {
                idioma = duplicarIdiomaPersona.getIdioma().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("IDIOMAS")) {
            if (tipoNuevo == 1) {
                nuevaIdiomaPersona.getIdioma().setNombre(idioma);
            } else if (tipoNuevo == 2) {
                duplicarIdiomaPersona.getIdioma().setNombre(idioma);
            }
            for (int i = 0; i < listIdiomas.size(); i++) {
                if (listIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaIdiomaPersona.setIdioma(listIdiomas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIdioma");
                } else if (tipoNuevo == 2) {
                    duplicarIdiomaPersona.setIdioma(listIdiomas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIdioma");
                }
                listIdiomas.clear();
                getListIdiomas();
            } else {
                //  eliminarRegistrosIdiomaLov();
                RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
                RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIdioma");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIdioma");
                }
            }
        }
    }

    public void cambiarIndice(IdiomasPersonas idiomaPersona, int celda) {
        if (permitirIndex == true) {
            idiomaTablaSeleccionado = idiomaPersona;
            cualCelda = celda;
            if (tipoLista == 0) {
                deshabilitarBotonLov();
                idiomaTablaSeleccionado.getSecuencia();
                if (cualCelda == 0) {
                    habilitarBotonLov();
                    idioma = idiomaTablaSeleccionado.getIdioma().getNombre();
                }
            } else {
                idiomaTablaSeleccionado.getSecuencia();
                deshabilitarBotonLov();
                if (cualCelda == 0) {
                    habilitarBotonLov();
                    idioma = idiomaTablaSeleccionado.getIdioma().getNombre();
                }
            }
        }
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listIdiomaPersonaBorrar.isEmpty()) {
                    administrarIdiomaPersona.borrarIdiomasPersonas(listIdiomaPersonaBorrar);
                    listIdiomaPersonaBorrar.clear();
                }
                if (!listIdiomaPersonaCrear.isEmpty()) {
                    administrarIdiomaPersona.crearIdiomasPersonas(listIdiomaPersonaCrear);
                    listIdiomaPersonaCrear.clear();
                }
                if (!listIdiomaPersonaModificar.isEmpty()) {
                    administrarIdiomaPersona.editarIdiomasPersonas(listIdiomaPersonaModificar);
                    listIdiomaPersonaModificar.clear();
                }
                listIdiomasPersonas = null;
                getListIdiomasPersonas();
                contarRegistros();
                //RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                idiomaTablaSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            deshabilitarBotonLov();
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "304";
            //CERRAR FILTRADO
            idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
            idIdioma.setFilterStyle("display: none; visibility: hidden;");
            idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
            idConversacion.setFilterStyle("display: none; visibility: hidden;");
            idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
            idLectura.setFilterStyle("display: none; visibility: hidden;");
            idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
            idEscritura.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 0;
            filtrarListIdiomasPersonas = null;
            tipoLista = 0;
        }

        listIdiomaPersonaBorrar.clear();
        listIdiomaPersonaCrear.clear();
        listIdiomaPersonaModificar.clear();
        listIdiomasPersonas = null;
        k = 0;
        idiomaTablaSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListIdiomasPersonas();
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosIdiomas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (idiomaTablaSeleccionado != null) {
            if (tipoLista == 0) {
                editarIdiomaPersona = idiomaTablaSeleccionado;
            }
            if (tipoLista == 1) {
                editarIdiomaPersona = idiomaTablaSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                habilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIdiomaD");
                RequestContext.getCurrentInstance().execute("PF('editarIdiomaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                deshabilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConversacionD");
                RequestContext.getCurrentInstance().execute("PF('editarConversacionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                deshabilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarLecturaD");
                RequestContext.getCurrentInstance().execute("PF('editarLecturaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEscrituraD");
                RequestContext.getCurrentInstance().execute("PF('editarEscrituraD').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
        }
    }

    public void agregarNuevaIdiomaPersona() {
        if (nuevaIdiomaPersona.getIdioma().getSecuencia() != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                altoTabla = "304";
                //CERRAR FILTRADO
                idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
                idIdioma.setFilterStyle("display: none; visibility: hidden;");
                idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
                idConversacion.setFilterStyle("display: none; visibility: hidden;");
                idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
                idLectura.setFilterStyle("display: none; visibility: hidden;");
                idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
                idEscritura.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIdiomas");
                bandera = 0;
                filtrarListIdiomasPersonas = null;
                tipoLista = 0;
            }

            boolean continuar = validarIdioma();
            if (continuar) {
                k++;
                l = BigInteger.valueOf(k);
                nuevaIdiomaPersona.setSecuencia(l);
                nuevaIdiomaPersona.setPersona(empleadoActual.getPersona());
                listIdiomaPersonaCrear.add(nuevaIdiomaPersona);
                if (listIdiomasPersonas == null) {
                    listIdiomasPersonas = new ArrayList<IdiomasPersonas>();
                }
                listIdiomasPersonas.add(nuevaIdiomaPersona);
                idiomaTablaSeleccionado = nuevaIdiomaPersona;
                RequestContext context = RequestContext.getCurrentInstance();
                getListIdiomasPersonas();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosIdiomas");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroIdiomas').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                nuevaIdiomaPersona = new IdiomasPersonas();
                nuevaIdiomaPersona.setIdioma(new Idiomas());
            } else {
                RequestContext.getCurrentInstance().update("form:existeIdioma");
                RequestContext.getCurrentInstance().execute("PF('existeIdioma').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNull').show()");
        }
    }

    public void limpiarNuevaIdiomaPersona() {
        nuevaIdiomaPersona = new IdiomasPersonas();
        nuevaIdiomaPersona.setIdioma(new Idiomas());

    }

    public void duplicarIdiomaPersonaM() {
        if (idiomaTablaSeleccionado != null) {
            duplicarIdiomaPersona = new IdiomasPersonas();
            if (tipoLista == 0) {
                duplicarIdiomaPersona.setEscritura(idiomaTablaSeleccionado.getEscritura());
                duplicarIdiomaPersona.setHabla(idiomaTablaSeleccionado.getHabla());
                duplicarIdiomaPersona.setIdioma(idiomaTablaSeleccionado.getIdioma());
                duplicarIdiomaPersona.setLectura(idiomaTablaSeleccionado.getLectura());
                duplicarIdiomaPersona.setPersona(idiomaTablaSeleccionado.getPersona());
            }
            if (tipoLista == 1) {
                duplicarIdiomaPersona.setEscritura(idiomaTablaSeleccionado.getEscritura());
                duplicarIdiomaPersona.setHabla(idiomaTablaSeleccionado.getHabla());
                duplicarIdiomaPersona.setIdioma(idiomaTablaSeleccionado.getIdioma());
                duplicarIdiomaPersona.setLectura(idiomaTablaSeleccionado.getLectura());
                duplicarIdiomaPersona.setPersona(idiomaTablaSeleccionado.getPersona());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIdiomas");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroIdiomas').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        if (duplicarIdiomaPersona.getIdioma().getSecuencia() != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                altoTabla = "304";
                //CERRAR FILTRADO
                idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
                idIdioma.setFilterStyle("display: none; visibility: hidden;");
                idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
                idConversacion.setFilterStyle("display: none; visibility: hidden;");
                idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
                idLectura.setFilterStyle("display: none; visibility: hidden;");
                idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
                idEscritura.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIdiomas");
                bandera = 0;
                filtrarListIdiomasPersonas = null;
                tipoLista = 0;
            }

            boolean continuar = validarIdioma();
            if (continuar) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarIdiomaPersona.setSecuencia(l);
                duplicarIdiomaPersona.setPersona(empleadoActual.getPersona());
                listIdiomasPersonas.add(duplicarIdiomaPersona);
                listIdiomaPersonaCrear.add(duplicarIdiomaPersona);
                if (listIdiomasPersonas == null) {
                    listIdiomasPersonas = new ArrayList<IdiomasPersonas>();
                }
                idiomaTablaSeleccionado = duplicarIdiomaPersona;
                RequestContext context = RequestContext.getCurrentInstance();
                getListIdiomasPersonas();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosIdiomas");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroIdiomas').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                duplicarIdiomaPersona = new IdiomasPersonas();
            } else {
                RequestContext.getCurrentInstance().update("form:existeIdioma");
                RequestContext.getCurrentInstance().execute("PF('existeIdioma').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNull').show()");
        }
    }

    public void limpiarDuplicar() {
        duplicarIdiomaPersona = new IdiomasPersonas();
        duplicarIdiomaPersona.setIdioma(new Idiomas());
    }

    public void borrarIdiomaPersona() {
        if (idiomaTablaSeleccionado != null) {
            if (!listIdiomaPersonaModificar.isEmpty() && listIdiomaPersonaModificar.contains(idiomaTablaSeleccionado)) {
                int modIndex = listIdiomaPersonaModificar.indexOf(idiomaTablaSeleccionado);
                listIdiomaPersonaModificar.remove(modIndex);
                listIdiomaPersonaBorrar.add(idiomaTablaSeleccionado);
            } else if (!listIdiomaPersonaCrear.isEmpty() && listIdiomaPersonaCrear.contains(idiomaTablaSeleccionado)) {
                int crearIndex = listIdiomaPersonaCrear.indexOf(idiomaTablaSeleccionado);
                listIdiomaPersonaCrear.remove(crearIndex);
            } else {
                listIdiomaPersonaBorrar.add(idiomaTablaSeleccionado);
            }
            listIdiomasPersonas.remove(idiomaTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarListIdiomasPersonas.remove(idiomaTablaSeleccionado);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            getListIdiomasPersonas();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            idiomaTablaSeleccionado = null;
            idiomaTablaSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('form:seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "284";
            idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
            idIdioma.setFilterStyle("width: 85% !important");
            idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
            idConversacion.setFilterStyle("width: 85% !important");
            idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
            idLectura.setFilterStyle("width: 85% !important");
            idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
            idEscritura.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "304";
            idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
            idIdioma.setFilterStyle("display: none; visibility: hidden;");
            idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
            idConversacion.setFilterStyle("display: none; visibility: hidden;");
            idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
            idLectura.setFilterStyle("display: none; visibility: hidden;");
            idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
            idEscritura.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 0;
            filtrarListIdiomasPersonas = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "304";
            idIdioma = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idIdioma");
            idIdioma.setFilterStyle("display: none; visibility: hidden;");
            idConversacion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idConversacion");
            idConversacion.setFilterStyle("display: none; visibility: hidden;");
            idLectura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idLectura");
            idLectura.setFilterStyle("display: none; visibility: hidden;");
            idEscritura = (Column) c.getViewRoot().findComponent("form:datosIdiomas:idEscritura");
            idEscritura.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 0;
            filtrarListIdiomasPersonas = null;
            tipoLista = 0;
        }

        listIdiomaPersonaBorrar.clear();
        listIdiomaPersonaCrear.clear();
        listIdiomaPersonaModificar.clear();
        idiomaTablaSeleccionado = null;
        idiomaTablaSeleccionado = null;
        k = 0;
        listIdiomasPersonas = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void asignarIndex(IdiomasPersonas idiomaPersona, int LND) {
        idiomaTablaSeleccionado = idiomaPersona;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        // eliminarRegistrosIdiomaLov();
        habilitarBotonLov();
        contarRegistroIdioma();
        RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
        RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
    }

    public void actualizarIdioma() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                idiomaTablaSeleccionado.setIdioma(idiomaSeleccionado);
                if (!listIdiomaPersonaCrear.contains(idiomaTablaSeleccionado)) {
                    if (listIdiomaPersonaModificar.isEmpty()) {
                        listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
                    } else if (!listIdiomaPersonaModificar.contains(idiomaTablaSeleccionado)) {
                        listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
                    }
                }
            } else {
                idiomaTablaSeleccionado.setIdioma(idiomaSeleccionado);
                if (!listIdiomaPersonaCrear.contains(idiomaTablaSeleccionado)) {
                    if (listIdiomaPersonaModificar.isEmpty()) {
                        listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
                    } else if (!listIdiomaPersonaModificar.contains(idiomaTablaSeleccionado)) {
                        listIdiomaPersonaModificar.add(idiomaTablaSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
        } else if (tipoActualizacion == 1) {
            nuevaIdiomaPersona.setIdioma(idiomaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaIdioma");
        } else if (tipoActualizacion == 2) {
            duplicarIdiomaPersona.setIdioma(idiomaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIdioma");
        }
        filtrarListIdiomas = null;
        aceptar = true;
        idiomaTablaSeleccionado = null;
        tipoActualizacion = -1;

        context.reset("form:lovIdiomas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
        RequestContext.getCurrentInstance().update("form:lovIdiomas");
        RequestContext.getCurrentInstance().update("form:aceptarI");
    }

    public void cancelarCambioIdioma() {
        filtrarListIdiomas = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();

        context.reset("form:lovIdiomas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
        RequestContext.getCurrentInstance().update("form:lovIdiomas");
        RequestContext.getCurrentInstance().update("form:atrasI");
    }

    public boolean validarIdioma() {
        boolean retorno = true;
        if (listIdiomasPersonas != null) {
            if (!listIdiomasPersonas.isEmpty()) {
                for (int i = 0; i < listIdiomasPersonas.size(); i++) {
                    if (nuevaIdiomaPersona.getIdioma().getSecuencia().equals(listIdiomasPersonas.get(i).getIdioma().getSecuencia())) {
                        System.out.println("idioma repetido");
                        retorno = false;
                        break;
                    }
                }
            }
        }
        return retorno;
    }

    public void listaValoresBoton() {
        if (idiomaTablaSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            deshabilitarBotonLov();
            if (cualCelda == 0) {
                //  eliminarRegistrosIdiomaLov();
                habilitarBotonLov();
                contarRegistroIdioma();
                RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
                RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIdiomaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "IdiomasPersonasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        idiomaTablaSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIdiomaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "IdiomasPersonasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        idiomaTablaSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (idiomaTablaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(idiomaTablaSeleccionado.getSecuencia(), "IDIOMASPERSONAS");
            backUpSecRegistro = idiomaTablaSeleccionado.getSecuencia();
            idiomaTablaSeleccionado = null;
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
        } else if (administrarRastros.verificarHistoricosTabla("IDIOMASPERSONAS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        idiomaTablaSeleccionado = null;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistroIdioma() {
        RequestContext.getCurrentInstance().update("form:infoRegistroIdioma");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void recordarSeleccion() {
        if (idiomaTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosIdiomas");
            tablaC.setSelection(idiomaTablaSeleccionado);
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

    //////////////SETS Y GETS///////////////
    public List<IdiomasPersonas> getListIdiomasPersonas() {
        try {
            if (listIdiomasPersonas == null) {
                if (empleadoActual.getSecuencia() != null) {
                    listIdiomasPersonas = administrarIdiomaPersona.listIdiomasPersonas(empleadoActual.getPersona().getSecuencia());
                }
            }
            return listIdiomasPersonas;
        } catch (Exception e) {
            System.out.println("Error...!! getListIdiomasPersonas : " + e.toString());

            return null;
        }
    }

    public void setListIdiomasPersonas(List<IdiomasPersonas> setListIdiomasPersonas) {
        this.listIdiomasPersonas = setListIdiomasPersonas;
    }

    public List<IdiomasPersonas> getFiltrarListIdiomasPersonas() {
        return filtrarListIdiomasPersonas;
    }

    public void setFiltrarListIdiomasPersonas(List<IdiomasPersonas> setFiltrarListIdiomasPersonas) {
        this.filtrarListIdiomasPersonas = setFiltrarListIdiomasPersonas;
    }

    public IdiomasPersonas getNuevaIdiomaPersona() {
        return nuevaIdiomaPersona;
    }

    public void setNuevaIdiomaPersona(IdiomasPersonas setNuevaIdiomaPersona) {
        this.nuevaIdiomaPersona = setNuevaIdiomaPersona;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<Idiomas> getListIdiomas() {
        listIdiomas = administrarIdiomaPersona.listIdiomas();
        return listIdiomas;
    }

    public void setListIdiomas(List<Idiomas> setListIdiomas) {
        this.listIdiomas = setListIdiomas;
    }

    public List<Idiomas> getFiltrarListIdiomas() {
        return filtrarListIdiomas;
    }

    public void setFiltrarListIdiomas(List<Idiomas> setFiltrarListIdiomas) {
        this.filtrarListIdiomas = setFiltrarListIdiomas;
    }

    public IdiomasPersonas getEditarIdiomaPersona() {
        return editarIdiomaPersona;
    }

    public void setEditarIdiomaPersona(IdiomasPersonas setEditarIdiomaPersona) {
        this.editarIdiomaPersona = setEditarIdiomaPersona;
    }

    public IdiomasPersonas getDuplicarIdiomaPersona() {
        return duplicarIdiomaPersona;
    }

    public void setDuplicarIdiomaPersona(IdiomasPersonas setDuplicarIdiomaPersona) {
        this.duplicarIdiomaPersona = setDuplicarIdiomaPersona;
    }

    public Idiomas getIdiomaSeleccionado() {
        return idiomaSeleccionado;
    }

    public void setIdiomaSeleccionado(Idiomas setIdiomaSeleccionado) {
        this.idiomaSeleccionado = setIdiomaSeleccionado;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public Empleados getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleados empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public IdiomasPersonas getIdiomaTablaSeleccionado() {
        return idiomaTablaSeleccionado;
    }

    public void setIdiomaTablaSeleccionado(IdiomasPersonas idiomaTablaSeleccionado) {
        this.idiomaTablaSeleccionado = idiomaTablaSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIdiomas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroIdioma() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovIdiomas");
        infoRegistroIdioma = String.valueOf(tabla.getRowCount());
        return infoRegistroIdioma;
    }

    public void setInfoRegistroIdioma(String infoRegistroIdioma) {
        this.infoRegistroIdioma = infoRegistroIdioma;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
