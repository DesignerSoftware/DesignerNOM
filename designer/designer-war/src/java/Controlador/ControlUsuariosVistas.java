package Controlador;

import Administrar.AdministrarUsuariosVistas;
import Entidades.ObjetosDB;
import Entidades.UsuariosVistas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosVistasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlUsuariosVistas implements Serializable {

    @EJB
    AdministrarUsuariosVistasInterface administrarUsuarioVistas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<UsuariosVistas> listaUsuariosVistas;
    private List<UsuariosVistas> filtrarUsuariosVistas;
    private String mensajeValidacion;
    private List<UsuariosVistas> listaUsuariosVistasCrear;
    private List<UsuariosVistas> listaUsuariosVistasModificar;
    private List<UsuariosVistas> listaUsuariosVistasBorrar;
    //LISTA DE VALORES DE OBJETODB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<ObjetosDB> lovObjetosDB;
    private List<ObjetosDB> lovFiltradoObjetosDB;
    private ObjetosDB objetosDBSeleccionado;
    //NUEVO, DUPLICADO EDITAR Y SELECCIONADA
    private UsuariosVistas nuevaUsuariosVistas;
    private UsuariosVistas duplicarUsuariosVistas;
    private UsuariosVistas eliminarUsuariosVistas;
    private UsuariosVistas editarUsuariosVistas;
    private UsuariosVistas usuariosVistasSeleccionado;
    private BigInteger secRegistro;
    public String altoTabla;
    public String infoRegistroObjetosDB;
    //AutoCompletar
    private boolean permitirIndex;
    private String objeto;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    private Column usuariovistaDescripcion, usuariovistaNombreVista, usuariovistaObjetoDB, usuariovistaAlias,
            usuariovistaBaseEstructura, usuariovistaEstructuraJOIN, usuariovistaCondicion, usuariovistaHINTPrincipal;
    public String infoRegistro;
    ///////////////////////////////////////////////
    //////////PRUEBAS UNITARIAS COMPONENTES////////
    ///////////////////////////////////////////////
    public boolean buscador;
    public String paginaAnterior;
    public String alisin;
    //otros
    private int cualCelda, tipoLista, index, indiceAux, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;

    public ControlUsuariosVistas() {

        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listaUsuariosVistas = null;

        listaUsuariosVistasCrear = new ArrayList<UsuariosVistas>();
        listaUsuariosVistasModificar = new ArrayList<UsuariosVistas>();
        listaUsuariosVistasBorrar = new ArrayList<UsuariosVistas>();
        lovObjetosDB = null;

        cualCelda = -1;
        tipoLista = 0;
        nuevaUsuariosVistas = new UsuariosVistas();
        nuevaUsuariosVistas.setObjetodb(new ObjetosDB());
        duplicarUsuariosVistas = new UsuariosVistas();
        eliminarUsuariosVistas = new UsuariosVistas();
        duplicarUsuariosVistas.setObjetodb(new ObjetosDB());
        secRegistro = null;
        k = 0;
        altoTabla = "270";
        guardado = true;
        buscador = false;
        tablaImprimir = ":formExportar:datosUsuariosVistasExportar";
        nombreArchivo = "UsuariosVistasXML";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuarioVistas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            getListaUsuariosVistas();
            if (listaUsuariosVistas != null) {
                infoRegistro = "Cantidad de registros : " + listaUsuariosVistas.size();
            } else {
                infoRegistro = "Cantidad de registros : 0";
            }
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            usuariosVistasSeleccionado = listaUsuariosVistas.get(index);
            cualCelda = celda;
            tablaImprimir = ":formExportar:datosUsuariosVistasExportar";
            nombreArchivo = "UsuariosVistasXML";
            if (tipoLista == 0) {
                alisin = listaUsuariosVistas.get(index).getAlias();
                secRegistro = listaUsuariosVistas.get(index).getSecuencia();
                if (cualCelda == 2) {
                    objeto = listaUsuariosVistas.get(index).getObjetodb().getNombre();
                }

            } else {
                alisin = filtrarUsuariosVistas.get(index).getAlias();
                secRegistro = filtrarUsuariosVistas.get(index).getSecuencia();
                if (cualCelda == 2) {
                    objeto = filtrarUsuariosVistas.get(index).getObjetodb().getNombre();
                }
            }
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarUsuariosVistas = listaUsuariosVistas.get(index);
            }
            if (tipoLista == 1) {
                editarUsuariosVistas = filtrarUsuariosVistas.get(index);
            }

            indiceAux = index;

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarDescripcion");
                context.execute("editarDescripcion.show()");
                cualCelda = -1;
            }
            if (cualCelda == 1) {
                context.update("formularioDialogos:editarNombreVista");
                context.execute("editarNombreVista.show()");
                cualCelda = -1;
            }
            if (cualCelda == 2) {
                context.update("formularioDialogos:editarObjetoDB");
                context.execute("editarObjetoDB.show()");
                cualCelda = -1;
            }
            if (cualCelda == 3) {
                context.update("formularioDialogos:editarAlias");
                context.execute("editarAlias.show()");
                cualCelda = -1;
            }
            if (cualCelda == 5) {
                context.update("formularioDialogos:editarEstructuraJOIN");
                context.execute("editarEstructuraJOIN.show()");
                cualCelda = -1;
            }
            if (cualCelda == 6) {
                context.update("formularioDialogos:editarCondicion");
                context.execute("editarCondicion.show()");
                cualCelda = -1;
            }
            if (cualCelda == 7) {
                context.update("formularioDialogos:editarHINTPrincipal");
                context.execute("editarHINTPrincipal.show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //MOSTRAR L.O.V OBJETOSDB
    public void actualizarObjetosDB() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaUsuariosVistas.get(index).setObjetodb(objetosDBSeleccionado);
                if (!listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(index))) {
                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(index));
                    } else if (!listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(index))) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(index));
                    }
                }
            } else {
                filtrarUsuariosVistas.get(index).setObjetodb(objetosDBSeleccionado);
                if (!listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(index))) {
                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(index));
                    } else if (!listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(index))) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosUsuariosVistas");
        } else if (tipoActualizacion == 1) {
            nuevaUsuariosVistas.setObjetodb(objetosDBSeleccionado);
            context.update("formularioDialogos:nuevaUsuarioVista");
        } else if (tipoActualizacion == 2) {
            duplicarUsuariosVistas.setObjetodb(objetosDBSeleccionado);
            context.update("formularioDialogos:duplicarUsuarioVista");
        }
        lovFiltradoObjetosDB = null;
        objetosDBSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVObjetosDB:globalFilter");
        context.execute("LOVObjetosDB.clearFilters()");
        context.execute("objetosDBDialogo.hide()");
        infoRegistroObjetosDB = "Cantidad de registros: " + lovObjetosDB.size();
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LND = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexObjeto(Integer indice, int dlg, int LND) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            context.update("formularioDialogos:objetosDBDialogo");
            context.execute("objetosDBDialogo.show()");
            infoRegistroObjetosDB = "Cantidad de registros: " + lovObjetosDB.size();
            context.update("formularioDialogos:infoRegistroObjetosDB");
        }
    }

    public void cancelarCambioObjetoDB() {
        lovFiltradoObjetosDB = null;
        objetosDBSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVObjetosDB:globalFilter");
        context.execute("LOVObjetosDB.clearFilters()");
        context.execute("objetosDBDialogo.hide()");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                context.update("formularioDialogos:objetosDBDialogo");
                context.execute("objetosDBDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    //SELECCIONAR BASE ESTRUCTURA
    public void seleccionarBaseEstructura(String estadoBaseEstructura, int indice, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoLista == 0) {
            if (estadoBaseEstructura != null) {
                if (estadoBaseEstructura.equals("SI")) {
                    listaUsuariosVistas.get(indice).setBaseestructura("S");
                } else if (estadoBaseEstructura.equals("NO")) {
                    listaUsuariosVistas.get(indice).setBaseestructura("N");
                }
            } else {
                listaUsuariosVistas.get(indice).setBaseestructura(null);
            }
            if (!listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(indice))) {
                if (listaUsuariosVistasModificar.isEmpty()) {
                    listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                } else if (!listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(indice))) {
                    listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                }
            }
        } else {
            if (estadoBaseEstructura != null) {
                if (estadoBaseEstructura.equals("SI")) {
                    filtrarUsuariosVistas.get(indice).setBaseestructura("S");
                } else if (estadoBaseEstructura.equals("NO")) {
                    filtrarUsuariosVistas.get(indice).setBaseestructura("N");
                }
            } else {
                filtrarUsuariosVistas.get(indice).setBaseestructura(null);
            }
            if (!listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(indice))) {
                if (listaUsuariosVistasModificar.isEmpty()) {
                    listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                } else if (!listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(indice))) {
                    listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            context.update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
        System.out.println("Subtipo: " + listaUsuariosVistas.get(indice).getBaseestructura());
    }

    //NUEVO Y DUPLICADO, REGISTRO DE BASE ESTRUCTURA
    public void seleccionarNuevoBaseEstructura(String estadoBaseEstructura, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (estadoBaseEstructura != null) {
                if (estadoBaseEstructura.equals("SI")) {
                    nuevaUsuariosVistas.setBaseestructura("S");
                } else if (estadoBaseEstructura.equals("NO")) {
                    nuevaUsuariosVistas.setBaseestructura("N");
                }
            } else {
                nuevaUsuariosVistas.setBaseestructura(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoBaseEstructura");

        } else {
            if (estadoBaseEstructura != null) {
                if (estadoBaseEstructura.equals("SI")) {
                    duplicarUsuariosVistas.setBaseestructura("S");
                } else if (estadoBaseEstructura.equals("NO")) {
                    duplicarUsuariosVistas.setBaseestructura("N");
                }
            } else {
                duplicarUsuariosVistas.setBaseestructura(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoBaseEstructura");
        }

    }

    //ACTIVAR F11
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("width: 85%");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("width: 85%");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("width: 85%");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("width: 85%");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("width: 85%;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("width: 85%;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("width: 85%;");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            bandera = 1;
            tipoLista = 1;
            System.out.println("TipoLista= " + tipoLista);
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuariosVistas = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);

        }
    }

    //EVENTO FILTRAR
    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        infoRegistro = "Cantidad de registros: " + filtrarUsuariosVistas.size();
        context.update("form:informacionRegistro");
    }

    public void entreAqui(String valor) {
        modificarUsuariosVistas(indiceAux, "N", valor);
        indiceAux = -1;
    }

    //AUTOCOMPLETAR
    public void modificarUsuariosVistas(int indice, String confirmarCambio, String valorConfirmar) {

        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(indice))) {

                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                    } else if (!listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(indice))) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(indice))) {

                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                    } else if (!listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(indice))) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
            }

            context.update("form:datosUsuariosVistas");
        }
        if (confirmarCambio.equalsIgnoreCase("A")) {
            System.out.println("Está entrando al modificar para Alias");
            int pasa = 0;
            if (tipoLista == 0) {
                if (!listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(indice))) {
                    if (listaUsuariosVistas.get(indice).getAlias() == null || listaUsuariosVistas.get(indice).getAlias().equals("")) {
                        mensajeValidacion = mensajeValidacion + "   * Alias\n";
                        pasa++;
                    }
                    if (pasa == 0) {
                        if (listaUsuariosVistasModificar.isEmpty()) {
                            listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                        } else if (!listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(indice))) {
                            listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    } else if (pasa > 0) {
                        listaUsuariosVistas.get(indice).setAlias(alisin);
                        context.update("formularioDialogos:validacionVacio");
                        context.execute("validacionVacio.show()");
                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(indice))) {
                    if (filtrarUsuariosVistas.get(indice).getAlias() == null || filtrarUsuariosVistas.get(indice).getAlias().equals("")) {
                        mensajeValidacion = mensajeValidacion + "   * Alias\n";
                        pasa++;
                    }
                    if (pasa == 0) {
                        if (listaUsuariosVistasModificar.isEmpty()) {
                            listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                        } else if (!listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(indice))) {
                            listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    } else if (pasa > 0) {
                        context.update("formularioDialogos:validacionVacio");
                        context.execute("validacionVacio.show()");
                    }
                }
                index = -1;
            }

            context.update("form:datosUsuariosVistas");
        } else if (confirmarCambio.equalsIgnoreCase("OBJETOSDB")) {
            System.out.println("Está entrando al modificar para lista de valores");
            if (tipoLista == 0) {
                listaUsuariosVistas.get(indice).getObjetodb().setNombre(objeto);
            } else {
                filtrarUsuariosVistas.get(indice).getObjetodb().setNombre(objeto);
            }

            for (int i = 0; i < lovObjetosDB.size(); i++) {
                if (lovObjetosDB.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaUsuariosVistas.get(indice).setObjetodb(lovObjetosDB.get(indiceUnicoElemento));
                } else {
                    filtrarUsuariosVistas.get(indice).setObjetodb(lovObjetosDB.get(indiceUnicoElemento));
                }
                lovObjetosDB.clear();
                getLovObjetosDB();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:objetosDBDialogo");
                context.execute("objetosDBDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(indice))) {
                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                    } else if (!listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(indice))) {
                        listaUsuariosVistasModificar.add(listaUsuariosVistas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(indice))) {

                    if (listaUsuariosVistasModificar.isEmpty()) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                    } else if (!listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(indice))) {
                        listaUsuariosVistasModificar.add(filtrarUsuariosVistas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;
            }
        }
        context.update("form:datosUsuariosVistas");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UsuariosVistasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosVistasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO USUARIO
    public void limpiarNuevaUsuarioVista() {
        nuevaUsuariosVistas = new UsuariosVistas();
        nuevaUsuariosVistas.setObjetodb(new ObjetosDB());
        nuevaUsuariosVistas.getObjetodb().setNombre(" ");
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarDuplicarUsuarioVista() {
        duplicarUsuariosVistas = new UsuariosVistas();
    }

    //AUTOCOMPLETAR LISTAS DE VALORES OBJETOSDB
    public void valoresBackupAutocompletarObjetosDB(int tipoNuevo) {
        if (tipoNuevo == 1) {
            objeto = nuevaUsuariosVistas.getObjetodb().getNombre();
        } else if (tipoNuevo == 2) {
            objeto = duplicarUsuariosVistas.getObjetodb().getNombre();
        }
    }

    public void llamarLovObjetosDB(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:objetosDBDialogo");
        context.execute("objetosDBDialogo.show()");
    }

    //AUTOCOMPLETAR NUEVO Y DUPLICADO OBJETOSdb
    public void autocompletarNuevoyDuplicadoObjetoDB(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaUsuariosVistas.getObjetodb().setNombre(objeto);
        } else if (tipoNuevo == 2) {
            duplicarUsuariosVistas.getObjetodb().setNombre(objeto);
        }
        for (int i = 0; i < lovObjetosDB.size(); i++) {
            if (lovObjetosDB.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaUsuariosVistas.setObjetodb(lovObjetosDB.get(indiceUnicoElemento));
                context.update("formularioDialogos:nuevoObjetoDB");
            } else if (tipoNuevo == 2) {
                duplicarUsuariosVistas.setObjetodb(lovObjetosDB.get(indiceUnicoElemento));
                context.update("formularioDialogos:duplicarObjetoDB");
            }
            lovObjetosDB.clear();
            getLovObjetosDB();
        } else {
            context.update("form:objetosDBDialogo");
            context.execute("objetosDBDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                context.update("formularioDialogos:nuevoObjetoDB");
            } else if (tipoNuevo == 2) {
                context.update("formularioDialogos:duplicarObjetoDB");
            }
        }
    }

    // Agregar Nueva Usuario Vista
    public void agregarNuevaUsuarioVista() {

        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;
        mensajeValidacion = " ";

        if (nuevaUsuariosVistas.getAlias() == null || nuevaUsuariosVistas.getAlias().equals("")) {
            System.out.println("entra2");
            mensajeValidacion = mensajeValidacion + "   * Alias\n";
            pasa++;
        }
        if (nuevaUsuariosVistas.getObjetodb().getNombre() == null || nuevaUsuariosVistas.getObjetodb().getNombre().equals("")) {
            System.out.println("entra3");
            mensajeValidacion = mensajeValidacion + "   * Objeto DB\n";
            pasa++;
        }
        /*if (nuevaUsuariosVistas.getBaseestructura() == null || nuevaUsuariosVistas.getBaseestructura().equals("")) {
         System.out.println("entra4");
         mensajeValidacion = mensajeValidacion + "   * perfil\n";
         pasa++;
         }*/

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
                usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
                usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
                usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
                usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
                usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
                usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
                usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                altoTabla = "270";
                bandera = 0;
                filtrarUsuariosVistas = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA USUARIOS VISTAS
            k++;
            l = BigInteger.valueOf(k);
            nuevaUsuariosVistas.setSecuencia(l);

            listaUsuariosVistasCrear.add(nuevaUsuariosVistas);
            listaUsuariosVistas.add(nuevaUsuariosVistas);
            infoRegistro = "Cantidad de registros: " + listaUsuariosVistas.size();
            context.update("form:infoRegistro");
            nuevaUsuariosVistas = new UsuariosVistas();
            context.update("form:datosUsuariosVistas");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("SE ESTÁ CERRANDO? YA VEREMOS");
            context.update("formularioDialogos:NuevoRegistroUsuarioVista");
            context.execute("NuevoRegistroUsuarioVista.hide()");
            index = -1;
            secRegistro = null;
        } else if (pasa > 0) {
            context.update("formularioDialogos:validacionNuevaUsuarioVista");
            context.execute("validacionNuevaUsuarioVista.show()");
        }
    }

    //BORRAR USUARIO VISTA
    public void borrarUsuarioVista() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaUsuariosVistasModificar.isEmpty() && listaUsuariosVistasModificar.contains(listaUsuariosVistas.get(index))) {
                    int modIndex = listaUsuariosVistasModificar.indexOf(listaUsuariosVistas.get(index));
                    listaUsuariosVistasModificar.remove(modIndex);
                    listaUsuariosVistasBorrar.add(listaUsuariosVistas.get(index));
                } else if (!listaUsuariosVistasCrear.isEmpty() && listaUsuariosVistasCrear.contains(listaUsuariosVistas.get(index))) {
                    int crearIndex = listaUsuariosVistasCrear.indexOf(listaUsuariosVistas.get(index));
                    listaUsuariosVistasCrear.remove(crearIndex);
                } else {
                    listaUsuariosVistasBorrar.add(listaUsuariosVistas.get(index));
                }
                listaUsuariosVistas.remove(index);
                infoRegistro = "Cantidad de registros: " + listaUsuariosVistas.size();
            }

            if (tipoLista == 1) {
                if (!listaUsuariosVistasModificar.isEmpty() && listaUsuariosVistasModificar.contains(filtrarUsuariosVistas.get(index))) {
                    int modIndex = listaUsuariosVistasModificar.indexOf(filtrarUsuariosVistas.get(index));
                    listaUsuariosVistasModificar.remove(modIndex);
                    listaUsuariosVistasBorrar.add(filtrarUsuariosVistas.get(index));
                } else if (!listaUsuariosVistasCrear.isEmpty() && listaUsuariosVistasCrear.contains(filtrarUsuariosVistas.get(index))) {
                    int crearIndex = listaUsuariosVistasCrear.indexOf(filtrarUsuariosVistas.get(index));
                    listaUsuariosVistasCrear.remove(crearIndex);
                } else {
                    listaUsuariosVistasBorrar.add(filtrarUsuariosVistas.get(index));
                }
                int CIndex = listaUsuariosVistas.indexOf(filtrarUsuariosVistas.get(index));
                listaUsuariosVistas.remove(CIndex);
                filtrarUsuariosVistas.remove(index);
                infoRegistro = "Cantidad de registros: " + filtrarUsuariosVistas.size();
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosUsuariosVistas");
            context.update("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    //DUPLICAR USUARIO VISTA
    public void duplicarUsuarioVista() {
        if (index >= 0) {
            duplicarUsuariosVistas = new UsuariosVistas();

            if (tipoLista == 0) {
                duplicarUsuariosVistas.setDescripcion(listaUsuariosVistas.get(index).getDescripcion());
                duplicarUsuariosVistas.setNombrevista(listaUsuariosVistas.get(index).getNombrevista());
                duplicarUsuariosVistas.setObjetodb(listaUsuariosVistas.get(index).getObjetodb());
                duplicarUsuariosVistas.setAlias(listaUsuariosVistas.get(index).getAlias());
                duplicarUsuariosVistas.setBaseestructura(listaUsuariosVistas.get(index).getBaseestructura());
                duplicarUsuariosVistas.setEstructurajoin(listaUsuariosVistas.get(index).getEstructurajoin());
                duplicarUsuariosVistas.setCondicion(listaUsuariosVistas.get(index).getEstructurajoin());
                duplicarUsuariosVistas.setHintprincipal(listaUsuariosVistas.get(index).getHintprincipal());
            }
            if (tipoLista == 1) {
                duplicarUsuariosVistas.setDescripcion(filtrarUsuariosVistas.get(index).getDescripcion());
                duplicarUsuariosVistas.setNombrevista(filtrarUsuariosVistas.get(index).getNombrevista());
                duplicarUsuariosVistas.setObjetodb(filtrarUsuariosVistas.get(index).getObjetodb());
                duplicarUsuariosVistas.setAlias(filtrarUsuariosVistas.get(index).getAlias());
                duplicarUsuariosVistas.setBaseestructura(filtrarUsuariosVistas.get(index).getBaseestructura());
                duplicarUsuariosVistas.setEstructurajoin(filtrarUsuariosVistas.get(index).getEstructurajoin());
                duplicarUsuariosVistas.setCondicion(filtrarUsuariosVistas.get(index).getEstructurajoin());
                duplicarUsuariosVistas.setHintprincipal(filtrarUsuariosVistas.get(index).getHintprincipal());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarUsuarioVista");
            context.execute("DuplicarRegistroUsuarioVistas.show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUsuariosVistas.setSecuencia(l);
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarUsuariosVistas.getAlias() == null || duplicarUsuariosVistas.getAlias().equals("")) {
            System.out.println("entra2");
            mensajeValidacion = mensajeValidacion + "   * Alias\n";
            pasa++;
        }
        if (duplicarUsuariosVistas.getObjetodb().getNombre() == null || duplicarUsuariosVistas.getObjetodb().getNombre().equals("")) {
            System.out.println("entra3");
            mensajeValidacion = mensajeValidacion + "   * Objeto DB\n";
            pasa++;
        }

        if (pasa == 0) {
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
                usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
                usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
                usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
                usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
                usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
                usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
                usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
                usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                altoTabla = "270";
                bandera = 0;
                filtrarUsuariosVistas = null;
                System.out.println("TipoLista= " + tipoLista);
                tipoLista = 0;
            }

            listaUsuariosVistas.add(duplicarUsuariosVistas);
            listaUsuariosVistasCrear.add(duplicarUsuariosVistas);
            context.update("form:datosUsuariosVistas");
            duplicarUsuariosVistas = new UsuariosVistas();
            infoRegistro = "Cantidad de registros: " + listaUsuariosVistas.size();
            context.update("form:informacionRegistro");

            context.update("formularioDialogos:duplicarUsuarioVista");
            context.execute("DuplicarRegistroUsuarioVistas.hide()");

        } else if (pasa > 0) {
            context.update("formularioDialogos:validacionNuevaUsuarioVista");
            context.execute("validacionNuevaUsuarioVista.show()");
        }

    }

    //REFRESCAR LA PAGINA, CANCELAR MODIFICACION SI NO SE A GUARDADO
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuariosVistas = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);
        }
        listaUsuariosVistasBorrar.clear();
        listaUsuariosVistasCrear.clear();
        listaUsuariosVistasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUsuariosVistas = null;

        getListaUsuariosVistas();
        if (listaUsuariosVistas != null && !listaUsuariosVistas.isEmpty()) {
            usuariosVistasSeleccionado = listaUsuariosVistas.get(0);
            infoRegistro = "Cantidad de registros: " + listaUsuariosVistas.size();
        } else {
            infoRegistro = "Cantidad de registros: 0";
        }
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosUsuariosVistas");
        context.update("form:informacionRegistro");
    }

    //MÉTODO SALIR DE LA PAGINA ACTUAL
    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "270";
            bandera = 0;
            filtrarUsuariosVistas = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);
        }
        listaUsuariosVistasBorrar.clear();
        listaUsuariosVistasCrear.clear();
        listaUsuariosVistasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUsuariosVistas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosUsuariosVistas");
        context.update("form:informacionRegistro");
    }

    //VERIFICAR RASTRO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaUsuariosVistas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "USUARIOSVISTAS");
                System.out.println("resultado: " + resultado);
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
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("USUARIOSVISTAS")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
        index = -1;
    }

    //GUARDAR
    public void guardarCambiosUsuarioVista() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                System.out.println("Realizando Operaciones UsuariosVistas");
                if (!listaUsuariosVistasBorrar.isEmpty()) {
                    administrarUsuarioVistas.borrarUsuariosVistas(listaUsuariosVistasBorrar);
                    System.out.println("Entra");
                    listaUsuariosVistasBorrar.clear();
                }
                if (!listaUsuariosVistasCrear.isEmpty()) {
                    administrarUsuarioVistas.crearUsuariosVistas(listaUsuariosVistasCrear);
                    listaUsuariosVistasCrear.clear();
                }
                if (!listaUsuariosVistasModificar.isEmpty()) {
                    administrarUsuarioVistas.modificarUsuariosVistas(listaUsuariosVistasModificar);
                    listaUsuariosVistasModificar.clear();
                }
                System.out.println("Se guardaron los datos con exito");
                listaUsuariosVistas = null;
                getListaUsuariosVistas();
                if (listaUsuariosVistas != null && !listaUsuariosVistas.isEmpty()) {
                    usuariosVistasSeleccionado = listaUsuariosVistas.get(0);
                    infoRegistro = "Cantidad de registros: " + listaUsuariosVistas.size();
                } else {
                    infoRegistro = "Cantidad de registros: 0";
                }
                context.update("form:informacionRegistro");
                context.update("form:datosUsuariosVistas");
                guardado = true;
                permitirIndex = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                index = -1;
                secRegistro = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    //  BOTON CREAR DERECHA
    public void crearUsuarioVista() {
        RequestContext context = RequestContext.getCurrentInstance();
        String mensaje = "";
        Integer exe = null;
        //try {
        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("objeto: " + usuariosVistasSeleccionado.getObjetodb().getSecuencia());
                exe = administrarUsuarioVistas.crearUsuarioVistaDB(usuariosVistasSeleccionado.getObjetodb().getSecuencia());
                System.out.println("Esto trae: " + exe);
                if (exe != null) {
                    mensaje = "Creando la nueva Vista Usuario...";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                    exe = null;
                } else {
                    mensaje = "Excepción no tratada";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                }
            }
            if (tipoLista == 1) {

                System.out.println("objeto: " + filtrarUsuariosVistas.get(index).getObjetodb().getNombre());
                exe = administrarUsuarioVistas.crearUsuarioVistaDB(filtrarUsuariosVistas.get(index).getObjetodb().getSecuencia());
                if (exe != null) {
                    mensaje = "Creando la nueva Vista Usuario...";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                } else {
                    mensaje = "Excepción no tratada";
                    FacesMessage msg = new FacesMessage("Información", mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                }
            }
            index = -1;
            secRegistro = null;
        }        
    }

    //GETTER AND SETTER
    public List<UsuariosVistas> getListaUsuariosVistasCrear() {
        return listaUsuariosVistasCrear;
    }

    public void setListaUsuariosVistasCrear(List<UsuariosVistas> listaUsuariosVistasCrear) {
        this.listaUsuariosVistasCrear = listaUsuariosVistasCrear;
    }

    public List<UsuariosVistas> getListaUsuariosVistasModificar() {
        return listaUsuariosVistasModificar;
    }

    public void setListaUsuariosVistasModificar(List<UsuariosVistas> listaUsuariosVistasModificar) {
        this.listaUsuariosVistasModificar = listaUsuariosVistasModificar;
    }

    public List<UsuariosVistas> getListaUsuariosVistasBorrar() {
        return listaUsuariosVistasBorrar;
    }

    public void setListaUsuariosVistasBorrar(List<UsuariosVistas> listaUsuariosVistasBorrar) {
        this.listaUsuariosVistasBorrar = listaUsuariosVistasBorrar;
    }

    public List<UsuariosVistas> getListaUsuariosVistas() {
        if (listaUsuariosVistas == null) {
            listaUsuariosVistas = administrarUsuarioVistas.consultarUsuariosVistas();
        }
        return listaUsuariosVistas;
    }

    public void setListaUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
        this.listaUsuariosVistas = listaUsuariosVistas;
    }

    public List<UsuariosVistas> getFiltrarUsuariosVistas() {
        return filtrarUsuariosVistas;
    }

    public void setFiltrarUsuariosVistas(List<UsuariosVistas> filtrarUsuariosVistas) {
        this.filtrarUsuariosVistas = filtrarUsuariosVistas;
    }

    public ObjetosDB getObjetosDBSeleccionado() {
        return objetosDBSeleccionado;
    }

    public void setObjetosDBSeleccionado(ObjetosDB objetosDBSeleccionado) {
        this.objetosDBSeleccionado = objetosDBSeleccionado;
    }

    public List<ObjetosDB> getLovObjetosDB() {
        lovObjetosDB = administrarUsuarioVistas.consultarObjetosDB();
        RequestContext context = RequestContext.getCurrentInstance();

        if (lovObjetosDB == null || lovObjetosDB.isEmpty()) {
            infoRegistroObjetosDB = "Cantidad de registros: 0 ";
        } else {
            infoRegistroObjetosDB = "Cantidad de registros: " + lovObjetosDB.size();
        }
        context.update("formularioDialogos:infoRegistroObjetosDB");
        return lovObjetosDB;
    }

    public String getInfoRegistroObjetosDB() {
        return infoRegistroObjetosDB;
    }

    public void setInfoRegistroObjetosDB(String infoRegistroObjetosDB) {
        this.infoRegistroObjetosDB = infoRegistroObjetosDB;
    }

    public void setLovObjetosDB(List<ObjetosDB> lovObjetosDB) {
        this.lovObjetosDB = lovObjetosDB;
    }

    public List<ObjetosDB> getLovFiltradoObjetosDB() {
        return lovFiltradoObjetosDB;
    }

    public void setLovFiltradoObjetosDB(List<ObjetosDB> lovFiltradoObjetosDB) {
        this.lovFiltradoObjetosDB = lovFiltradoObjetosDB;
    }

    public UsuariosVistas getNuevaUsuariosVistas() {
        return nuevaUsuariosVistas;
    }

    public void setNuevaUsuariosVistas(UsuariosVistas nuevaUsuariosVistas) {
        this.nuevaUsuariosVistas = nuevaUsuariosVistas;
    }

    public UsuariosVistas getDuplicarUsuariosVistas() {
        return duplicarUsuariosVistas;
    }

    public void setDuplicarUsuariosVistas(UsuariosVistas duplicarUsuariosVistas) {
        this.duplicarUsuariosVistas = duplicarUsuariosVistas;
    }

    public UsuariosVistas getEditarUsuariosVistas() {
        return editarUsuariosVistas;
    }

    public void setEditarUsuariosVistas(UsuariosVistas editarUsuariosVistas) {
        this.editarUsuariosVistas = editarUsuariosVistas;
    }

    public UsuariosVistas getUsuariosVistasSeleccionado() {
        return usuariosVistasSeleccionado;
    }

    public void setUsuariosVistasSeleccionado(UsuariosVistas usuariosVistasSeleccionado) {
        this.usuariosVistasSeleccionado = usuariosVistasSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public UsuariosVistas getEliminarUsuariosVistas() {
        return eliminarUsuariosVistas;
    }

    public void setEliminarUsuariosVistas(UsuariosVistas eliminarUsuariosVistas) {
        this.eliminarUsuariosVistas = eliminarUsuariosVistas;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

}
