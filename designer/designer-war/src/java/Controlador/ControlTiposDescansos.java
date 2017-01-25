/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposDescansos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposDescansosInterface;
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

@ManagedBean
@SessionScoped
public class ControlTiposDescansos implements Serializable {

    @EJB
    AdministrarTiposDescansosInterface administrarTiposDescansos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposDescansos> listTiposDescansos;
    private List<TiposDescansos> filtrarTiposDescansos;
    private List<TiposDescansos> crearTiposDescansos;
    private List<TiposDescansos> modificarTiposDescansos;
    private List<TiposDescansos> borrarTiposDescansos;
    private TiposDescansos nuevoTiposDescansos;
    private TiposDescansos duplicarTiposDescansos;
    private TiposDescansos editarTiposDescansos;
    private TiposDescansos tipoDescansoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, diasTrabajados, diasDescansados;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String backUpCodigo;
    private String backUpDescripcion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposDescansos() {
        listTiposDescansos = null;
        crearTiposDescansos = new ArrayList<TiposDescansos>();
        modificarTiposDescansos = new ArrayList<TiposDescansos>();
        borrarTiposDescansos = new ArrayList<TiposDescansos>();
        permitirIndex = true;
        editarTiposDescansos = new TiposDescansos();
        nuevoTiposDescansos = new TiposDescansos();
        duplicarTiposDescansos = new TiposDescansos();
        guardado = true;
        tamano = 330;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposDescansos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
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
            String pagActual = "tipodescanso";
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

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
        tipoDescansoSeleccionado = null;
    }

    public void cambiarIndice(TiposDescansos td, int celda) {
        tipoDescansoSeleccionado = td;
        if (permitirIndex == true) {
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = tipoDescansoSeleccionado.getCodigo();
            }
            if (cualCelda == 1) {
                backUpDescripcion = tipoDescansoSeleccionado.getDescripcion();
            }
        }
    }

    public void asignarIndex(TiposDescansos td, int LND, int dig) {
        tipoDescansoSeleccionado = td;
        try {
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposDescansos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
            diasTrabajados.setFilterStyle("display: none; visibility: hidden;");
            diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
            diasDescansados.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            bandera = 0;
            filtrarTiposDescansos = null;
            tipoLista = 0;
        }

        borrarTiposDescansos.clear();
        crearTiposDescansos.clear();
        modificarTiposDescansos.clear();
        tipoDescansoSeleccionado = null;
        k = 0;
        listTiposDescansos = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
            diasTrabajados.setFilterStyle("display: none; visibility: hidden;");
            diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
            diasDescansados.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            bandera = 0;
            filtrarTiposDescansos = null;
            tipoLista = 0;
        }
        borrarTiposDescansos.clear();
        crearTiposDescansos.clear();
        modificarTiposDescansos.clear();
        tipoDescansoSeleccionado = null;
        k = 0;
        listTiposDescansos = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
            diasTrabajados.setFilterStyle("width: 85% !important;");
            diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
            diasDescansados.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 330;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
            diasTrabajados.setFilterStyle("display: none; visibility: hidden;");
            diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
            diasDescansados.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            bandera = 0;
            filtrarTiposDescansos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposDescansos(TiposDescansos td, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR TIPODESCANSO");
        tipoDescansoSeleccionado = td;
        int contador = 0;
        boolean banderita = false;

        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (!crearTiposDescansos.contains(tipoDescansoSeleccionado)) {
                if (tipoDescansoSeleccionado.getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                } else if (tipoDescansoSeleccionado.getCodigo().length() > 3) {
                    mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                    tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < listTiposDescansos.size(); j++) {
                        if (!tipoDescansoSeleccionado.getCodigo().equals(listTiposDescansos.get(j).getCodigo())) {
                            if (tipoDescansoSeleccionado.getCodigo().equals(listTiposDescansos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }
                if (tipoDescansoSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tipoDescansoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (tipoDescansoSeleccionado.getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tipoDescansoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (banderita == true) {
                    if (modificarTiposDescansos.isEmpty()) {
                        modificarTiposDescansos.add(tipoDescansoSeleccionado);
                    } else if (!modificarTiposDescansos.contains(tipoDescansoSeleccionado)) {
                        modificarTiposDescansos.add(tipoDescansoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else {
                if (tipoDescansoSeleccionado.getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                } else if (tipoDescansoSeleccionado.getCodigo().length() > 3) {
                    mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                    tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < listTiposDescansos.size(); j++) {
                        if (!tipoDescansoSeleccionado.getCodigo().equals(listTiposDescansos.get(j).getCodigo())) {
                            if (tipoDescansoSeleccionado.getCodigo().equals(listTiposDescansos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        tipoDescansoSeleccionado.setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }
                if (tipoDescansoSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tipoDescansoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (tipoDescansoSeleccionado.getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    tipoDescansoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }

                if (banderita == true) {
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposDescansos() {

        System.out.println("Entro a borrandoTiposDescansos");
        if (!modificarTiposDescansos.isEmpty() && modificarTiposDescansos.contains(tipoDescansoSeleccionado)) {
            modificarTiposDescansos.remove(tipoDescansoSeleccionado);
            borrarTiposDescansos.add(tipoDescansoSeleccionado);
        } else if (!crearTiposDescansos.isEmpty() && crearTiposDescansos.contains(tipoDescansoSeleccionado)) {
            crearTiposDescansos.remove(tipoDescansoSeleccionado);
        } else {
            borrarTiposDescansos.add(tipoDescansoSeleccionado);
        }
        listTiposDescansos.remove(tipoDescansoSeleccionado);
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
        tipoDescansoSeleccionado = null;

        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void verificarBorrado() {
        if (tipoDescansoSeleccionado != null) {
            System.out.println("Estoy en verificarBorrado");
            BigInteger contarCodeudoresTipoDocumento;

            try {
                System.err.println("Control Secuencia de ControlTiposDescansos " + tipoDescansoSeleccionado.getSecuencia());
                contarCodeudoresTipoDocumento = administrarTiposDescansos.contarVigenciasJornadasTipoDescanso(tipoDescansoSeleccionado.getSecuencia());
                if (contarCodeudoresTipoDocumento.equals(new BigInteger("0"))) {
                    System.out.println("Borrado==0");
                    borrandoTiposDescansos();
                } else {
                    System.out.println("Borrado>0");

                    RequestContext.getCurrentInstance().update("form:validacionBorrar");
                    RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                    tipoDescansoSeleccionado = null;
                    contarCodeudoresTipoDocumento = new BigInteger("-1");
                }
            } catch (Exception e) {
                System.err.println("ERROR ControlTiposDescansos verificarBorrado ERROR " + e);
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposDescansos.isEmpty() || !crearTiposDescansos.isEmpty() || !modificarTiposDescansos.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposDescansos() {
        if (guardado == false) {
            System.out.println("Realizando guardarTiposDescansos");
            if (!borrarTiposDescansos.isEmpty()) {
                administrarTiposDescansos.borrarTiposDescansos(borrarTiposDescansos);
                //mostrarBorrados
                registrosBorrados = borrarTiposDescansos.size();
                borrarTiposDescansos.clear();
            }
            if (!modificarTiposDescansos.isEmpty()) {
                administrarTiposDescansos.modificarTiposDescansos(modificarTiposDescansos);
                modificarTiposDescansos.clear();
            }
            if (!crearTiposDescansos.isEmpty()) {
                administrarTiposDescansos.crearTiposDescansos(crearTiposDescansos);
                crearTiposDescansos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposDescansos = null;
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (tipoDescansoSeleccionado != null) {
            editarTiposDescansos = tipoDescansoSeleccionado;

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
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDiasTrabajados");
                RequestContext.getCurrentInstance().execute("PF('editDiasTrabajados').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDiasDescansados");
                RequestContext.getCurrentInstance().execute("PF('editDiasDescansados').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposDescansos() {
        System.out.println("agregarNuevoTiposDescansos");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        if (nuevoTiposDescansos.getCodigo() == null) {
            mensajeValidacion = " *Codigo\n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int i = 0; i < listTiposDescansos.size(); i++) {
                if (nuevoTiposDescansos.getCodigo().equals(listTiposDescansos.get(i).getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Codigo Repetido";
            }
        }
        if (nuevoTiposDescansos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
                diasTrabajados.setFilterStyle("display: none; visibility: hidden;");
                diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
                diasDescansados.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
                bandera = 0;
                filtrarTiposDescansos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposDescansos.setSecuencia(l);

            crearTiposDescansos.add(nuevoTiposDescansos);
            listTiposDescansos.add(nuevoTiposDescansos);
            tipoDescansoSeleccionado = listTiposDescansos.get(listTiposDescansos.indexOf(nuevoTiposDescansos));
            nuevoTiposDescansos = new TiposDescansos();
            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposDescansos').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposDescansos() {
        System.out.println("limpiarNuevoTiposDescansos");
        nuevoTiposDescansos = new TiposDescansos();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposDescansos() {
        System.out.println("duplicandoTiposDescansos");
        if (tipoDescansoSeleccionado != null) {
            duplicarTiposDescansos = new TiposDescansos();
            k++;
            l = BigInteger.valueOf(k);

            duplicarTiposDescansos.setSecuencia(l);
            duplicarTiposDescansos.setCodigo(tipoDescansoSeleccionado.getCodigo());
            duplicarTiposDescansos.setDescripcion(tipoDescansoSeleccionado.getDescripcion());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDescansos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;

        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposDescansos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposDescansos.getDescripcion());

        if (duplicarTiposDescansos.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int i = 0; i < listTiposDescansos.size(); i++) {
                if (duplicarTiposDescansos.getCodigo().equals(listTiposDescansos.get(i).getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "codigo repetido";
            }

        }
        if (duplicarTiposDescansos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion  \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            if (crearTiposDescansos.contains(duplicarTiposDescansos)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposDescansos.add(duplicarTiposDescansos);
            crearTiposDescansos.add(duplicarTiposDescansos);
            tipoDescansoSeleccionado = listTiposDescansos.get(listTiposDescansos.indexOf(duplicarTiposDescansos));

            RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                diasTrabajados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasTrabajados");
                diasTrabajados.setFilterStyle("display: none; visibility: hidden;");
                diasDescansados = (Column) c.getViewRoot().findComponent("form:datosTiposDescansos:diasDescansados");
                diasDescansados.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDescansos");
                bandera = 0;
                filtrarTiposDescansos = null;
                tipoLista = 0;
            }
            duplicarTiposDescansos = new TiposDescansos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDescansos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposDescansos() {
        duplicarTiposDescansos = new TiposDescansos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDescansosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSDESCANSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDescansosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSDESCANSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        System.out.println("lol");
        if (tipoDescansoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(tipoDescansoSeleccionado.getSecuencia(), "TIPOSDESCANSOS"); //En ENCARGATURAS lo cambia por el Descripcion de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSDESCANSOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposDescansos> getListTiposDescansos() {
        if (listTiposDescansos == null) {
            listTiposDescansos = administrarTiposDescansos.consultarTiposDescansos();
        }
        return listTiposDescansos;
    }

    public void setListTiposDescansos(List<TiposDescansos> listTiposDescansos) {
        this.listTiposDescansos = listTiposDescansos;
    }

    public List<TiposDescansos> getFiltrarTiposDescansos() {
        return filtrarTiposDescansos;
    }

    public void setFiltrarTiposDescansos(List<TiposDescansos> filtrarTiposDescansos) {
        this.filtrarTiposDescansos = filtrarTiposDescansos;
    }

    public TiposDescansos getNuevoTiposDescansos() {
        return nuevoTiposDescansos;
    }

    public void setNuevoTiposDescansos(TiposDescansos nuevoTiposDescansos) {
        this.nuevoTiposDescansos = nuevoTiposDescansos;
    }

    public TiposDescansos getDuplicarTiposDescansos() {
        return duplicarTiposDescansos;
    }

    public void setDuplicarTiposDescansos(TiposDescansos duplicarTiposDescansos) {
        this.duplicarTiposDescansos = duplicarTiposDescansos;
    }

    public TiposDescansos getEditarTiposDescansos() {
        return editarTiposDescansos;
    }

    public void setEditarTiposDescansos(TiposDescansos editarTiposDescansos) {
        this.editarTiposDescansos = editarTiposDescansos;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public TiposDescansos getTipoDescansoSeleccionado() {
        return tipoDescansoSeleccionado;
    }

    public void setTipoDescansoSeleccionado(TiposDescansos tipoDescansoSeleccionado) {
        this.tipoDescansoSeleccionado = tipoDescansoSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposDescansos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
