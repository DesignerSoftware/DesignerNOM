/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposConceptos;
import Entidades.Operandos;
import Entidades.Procesos;
import Entidades.OperandosGruposConceptos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarOperandosGruposConceptosInterface;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlOperandoGrupoConcepto implements Serializable {

    @EJB
    AdministrarOperandosGruposConceptosInterface administrarOperandosGruposConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Lista Vigencias OperandosGruposConceptos (Arriba)
    private List<Procesos> listaProcesos;
    private List<Procesos> filtradoListaProcesos;
    private Procesos procesoSeleccionado;
    //Lista OperandosGruposConceptos
    private List<OperandosGruposConceptos> listaOperandosGruposConceptos;
    private List<OperandosGruposConceptos> filtradoListaOperandosGruposConceptos;
    private OperandosGruposConceptos operandoSeleccionado;
    //REGISTRO ACTUAL
    private int registroActual, index, tablaActual;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private String altoScrollProcesos, altoScrollOperandosGruposConceptos;
    //Crear Vigencias OperandosGruposConceptos (Arriba)
    private List<Procesos> listaProcesosCrear;
    public Procesos nuevoProcesos;
    public Procesos duplicarProcesos;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias OperandosGruposConceptos
    private List<Procesos> listaProcesosModificar;
    //Borrar Vigencias OperandosGruposConceptos
    private List<Procesos> listaProcesosBorrar;
    //Crear OperandosGruposConceptos (Abajo)
    private List<OperandosGruposConceptos> listaOperandosGruposConceptosCrear;
    public OperandosGruposConceptos nuevoOperando;
    public OperandosGruposConceptos duplicarOperando;
    //Modificar OperandosGruposConceptos
    private List<OperandosGruposConceptos> listaOperandosGruposConceptosModificar;
    //Borrar OperandosGruposConceptos
    private List<OperandosGruposConceptos> listaOperandosGruposConceptosBorrar;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private Procesos editarProcesos;
    private OperandosGruposConceptos editarOperandosGruposConceptos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //RASTROS
    private BigInteger secRegistro;
    private boolean guardado, guardarOk;
    private boolean cambiosPagina;
    //FILTRADO
    private Column oOperando, oGrupo;
    //Sec Abajo Duplicar
    private int m;
    private BigInteger n;
    //SECUENCIA DE VIGENCIA
    private BigInteger secuenciaProceso;
    private Date fechaParametro;
    private Date fechaVigencia;
    private String Operando, Grupo;
    //L.O.V Operandos
    private List<Operandos> lovListaOperandos;
    private List<Operandos> lovfiltradoslistaOperandos;
    private Operandos operandosSeleccionado;
    //L.O.V GRUPOS
    private List<GruposConceptos> lovListaGruposConceptos;
    private List<GruposConceptos> lovfiltradoslistaGruposConceptos;
    private GruposConceptos gruposSeleccionado;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlOperandoGrupoConcepto() {
        permitirIndex = true;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollProcesos = "90";
        altoScrollOperandosGruposConceptos = "90";
        listaProcesosBorrar = new ArrayList<Procesos>();
        listaProcesosCrear = new ArrayList<Procesos>();
        listaProcesosModificar = new ArrayList<Procesos>();
        listaOperandosGruposConceptosBorrar = new ArrayList<OperandosGruposConceptos>();
        listaOperandosGruposConceptosCrear = new ArrayList<OperandosGruposConceptos>();
        listaOperandosGruposConceptosModificar = new ArrayList<OperandosGruposConceptos>();
        //Crear Vigencia OperandosGruposConceptos
        nuevoProcesos = new Procesos();
        //Crear OperandosGruposConceptos
        nuevoOperando = new OperandosGruposConceptos();
        m = 0;
        cambiosPagina = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOperandosGruposConceptos.obtenerConexion(ses.getId());
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
            String pagActual = "operandogrupoconcepto";
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
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    //BORRAR NOVEDADES
    public void borrar() {

        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();

            if (tipoLista == 0) {
                if (!listaOperandosGruposConceptosModificar.isEmpty() && listaOperandosGruposConceptosModificar.contains(listaOperandosGruposConceptos.get(index))) {
                    int modIndex = listaOperandosGruposConceptosModificar.indexOf(listaOperandosGruposConceptos.get(index));
                    listaOperandosGruposConceptosModificar.remove(modIndex);
                    listaOperandosGruposConceptosBorrar.add(listaOperandosGruposConceptos.get(index));
                } else if (!listaOperandosGruposConceptosCrear.isEmpty() && listaOperandosGruposConceptosCrear.contains(listaOperandosGruposConceptos.get(index))) {
                    int crearIndex = listaOperandosGruposConceptosCrear.indexOf(listaOperandosGruposConceptos.get(index));
                    listaOperandosGruposConceptosCrear.remove(crearIndex);
                } else {
                    listaOperandosGruposConceptosBorrar.add(listaOperandosGruposConceptos.get(index));
                }
                listaOperandosGruposConceptos.remove(index);
            }
            if (tipoLista == 1) {
                if (!listaOperandosGruposConceptosModificar.isEmpty() && listaOperandosGruposConceptosModificar.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                    int modIndex = listaOperandosGruposConceptosModificar.indexOf(filtradoListaOperandosGruposConceptos.get(index));
                    listaOperandosGruposConceptosModificar.remove(modIndex);
                    listaOperandosGruposConceptosBorrar.add(filtradoListaOperandosGruposConceptos.get(index));
                } else if (!listaOperandosGruposConceptosCrear.isEmpty() && listaOperandosGruposConceptosCrear.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                    int crearIndex = listaOperandosGruposConceptosCrear.indexOf(filtradoListaOperandosGruposConceptos.get(index));
                    listaOperandosGruposConceptosCrear.remove(crearIndex);
                } else {
                    listaOperandosGruposConceptosBorrar.add(filtradoListaOperandosGruposConceptos.get(index));
                }
                int CIndex = listaOperandosGruposConceptos.indexOf(filtradoListaOperandosGruposConceptos.get(index));
                listaOperandosGruposConceptos.remove(CIndex);
                filtradoListaOperandosGruposConceptos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {

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
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:gruposDialogo");
            RequestContext.getCurrentInstance().execute("PF('gruposDialogo').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
                RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:gruposDialogo");
                RequestContext.getCurrentInstance().execute("PF('gruposDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void actualizarOperandos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaOperandosGruposConceptos.get(index).setOperando(operandosSeleccionado);
                if (!listaOperandosGruposConceptosCrear.contains(listaOperandosGruposConceptos.get(index))) {
                    if (listaOperandosGruposConceptosModificar.isEmpty()) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosModificar.contains(listaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    }
                }
            } else {
                filtradoListaOperandosGruposConceptos.get(index).setOperando(operandosSeleccionado);
                if (!listaOperandosGruposConceptosCrear.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                    if (listaOperandosGruposConceptosModificar.isEmpty()) {
                        listaOperandosGruposConceptosModificar.add(filtradoListaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosModificar.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosModificar.add(filtradoListaOperandosGruposConceptos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
        } else if (tipoActualizacion == 1) {
            nuevoOperando.setOperando(operandosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoGrupoConcepto");
        } else if (tipoActualizacion == 2) {
            duplicarOperando.setOperando(operandosSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoGrupoConcepto");

        }
        lovfiltradoslistaOperandos = null;
        operandosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVOperandos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVOperandos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVOperandos");
    }

    public void actualizarGrupos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaOperandosGruposConceptos.get(index).setGrupoconcepto(gruposSeleccionado);
                if (!listaOperandosGruposConceptosCrear.contains(listaOperandosGruposConceptos.get(index))) {
                    if (listaOperandosGruposConceptosModificar.isEmpty()) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosModificar.contains(listaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    }
                }
            } else {
                filtradoListaOperandosGruposConceptos.get(index).setGrupoconcepto(gruposSeleccionado);
                if (!listaOperandosGruposConceptosCrear.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                    if (listaOperandosGruposConceptosModificar.isEmpty()) {
                        listaOperandosGruposConceptosModificar.add(filtradoListaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosModificar.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosModificar.add(filtradoListaOperandosGruposConceptos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
        } else if (tipoActualizacion == 1) {
            nuevoOperando.setGrupoconcepto(gruposSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoGrupoConcepto");
        } else if (tipoActualizacion == 2) {
            duplicarOperando.setGrupoconcepto(gruposSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoGrupoConcepto");

        }
        lovfiltradoslistaGruposConceptos = null;
        gruposSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVGrupos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVGrupos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('gruposDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVGrupos");
    }

    public void cancelarCambioOperandos() {
        lovfiltradoslistaOperandos = null;
        operandosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVOperandos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVOperandos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
    }

    public void cancelarCambioGrupos() {
        lovfiltradoslistaGruposConceptos = null;
        gruposSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVGrupos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVGrupos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('gruposDialogo').hide()");
    }

    public void guardarTodo() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones retenciones");
            if (!listaOperandosGruposConceptosBorrar.isEmpty()) {
                for (int i = 0; i < listaOperandosGruposConceptosBorrar.size(); i++) {
                    System.out.println("Borrando...");

                    administrarOperandosGruposConceptos.borrarOperandosGrupos(listaOperandosGruposConceptosBorrar.get(i));
                    System.out.println("Entra");
                    listaOperandosGruposConceptosBorrar.clear();
                }
            }
            if (!listaOperandosGruposConceptosCrear.isEmpty()) {
                for (int i = 0; i < listaOperandosGruposConceptosCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaOperandosGruposConceptosCrear.size());

                    administrarOperandosGruposConceptos.crearOperandosGrupos(listaOperandosGruposConceptosCrear.get(i));
                }

                System.out.println("LimpiaLista");
                listaOperandosGruposConceptosCrear.clear();
            }
            if (!listaOperandosGruposConceptosModificar.isEmpty()) {
                administrarOperandosGruposConceptos.modificarOperandosGrupos(listaOperandosGruposConceptosModificar);
                listaOperandosGruposConceptosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaProcesos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            permitirIndex = true;
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        index = -1;
        secRegistro = null;
    }

    public void limpiarListas() {
        listaOperandosGruposConceptosCrear.clear();
        listaOperandosGruposConceptosBorrar.clear();
        listaOperandosGruposConceptosModificar.clear();
        secuenciaProceso = procesoSeleccionado.getSecuencia();
        listaOperandosGruposConceptos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        if (bandera == 0) {
            System.out.println("Activa 1");
            //Tabla Vigencias OperandosGruposConceptos
            oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
            oOperando.setFilterStyle("width: 85% !important;");
            oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
            oGrupo.setFilterStyle("width: 85% !important;");
            altoScrollOperandosGruposConceptos = "70";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            bandera = 1;

        } else if (bandera == 1) {
            System.out.println("Desactiva 1");
            oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
            oOperando.setFilterStyle("display: none; visibility: hidden;");
            oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
            oGrupo.setFilterStyle("display: none; visibility: hidden;");
            altoScrollOperandosGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            bandera = 0;
            filtradoListaOperandosGruposConceptos = null;
        }
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
            oOperando.setFilterStyle("display: none; visibility: hidden;");
            oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
            oGrupo.setFilterStyle("display: none; visibility: hidden;");
            altoScrollOperandosGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            bandera = 0;
            filtradoListaOperandosGruposConceptos = null;
            tipoLista = 0;
        }
        listaOperandosGruposConceptosBorrar.clear();
        listaOperandosGruposConceptosCrear.clear();
        listaOperandosGruposConceptosModificar.clear();
        index = -1;
        listaOperandosGruposConceptos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
            oOperando.setFilterStyle("display: none; visibility: hidden;");
            oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
            oGrupo.setFilterStyle("display: none; visibility: hidden;");
            altoScrollOperandosGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            bandera = 0;
            filtradoListaOperandosGruposConceptos = null;
            tipoLista = 0;
        }

        listaOperandosGruposConceptosBorrar.clear();
        listaOperandosGruposConceptosCrear.clear();
        listaOperandosGruposConceptosModificar.clear();
        index = -1;
        listaOperandosGruposConceptos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("OPERANDO")) {
            if (tipoNuevo == 1) {
                Operando = nuevoOperando.getOperando().getDescripcion();
            } else if (tipoNuevo == 2) {
                Operando = duplicarOperando.getOperando().getDescripcion();
            }
        } else if (Campo.equals("GRUPO")) {
            if (tipoNuevo == 1) {
                Grupo = nuevoOperando.getGrupoconcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                Grupo = duplicarOperando.getGrupoconcepto().getDescripcion();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoNuevo == 1) {
                nuevoOperando.getOperando().setDescripcion(Operando);
            } else if (tipoNuevo == 2) {
                duplicarOperando.getOperando().setDescripcion(Operando);
            }
            for (int i = 0; i < lovListaOperandos.size(); i++) {
                if (lovListaOperandos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
                } else if (tipoNuevo == 2) {
                    duplicarOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
                }
                lovListaOperandos.clear();
                getLovListaOperandos();
            } else {
                RequestContext.getCurrentInstance().update("form:operandosDialogo");
                RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
            if (tipoNuevo == 1) {
                nuevoOperando.getGrupoconcepto().setDescripcion(Grupo);
            } else if (tipoNuevo == 2) {
                duplicarOperando.getGrupoconcepto().setDescripcion(Grupo);
            }

            for (int i = 0; i < lovListaGruposConceptos.size(); i++) {
                if (lovListaGruposConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoOperando.setGrupoconcepto(lovListaGruposConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoGrupo");
                } else if (tipoNuevo == 2) {
                    duplicarOperando.setGrupoconcepto(lovListaGruposConceptos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
                }
                lovListaGruposConceptos.clear();
                getLovListaGruposConceptos();
            } else {
                RequestContext.getCurrentInstance().update("form:gruposDialogo");
                RequestContext.getCurrentInstance().execute("PF('gruposDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoGrupo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupo");
                }
            }

        }
    }

//CREAR Operando
    public void agregarNuevoOperando() {
        int pasa = 0;

        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoOperando.getOperando().getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                //SOLUCIONES NODOS EMPLEADO
                System.out.println("Desactiva 1");
                oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
                oOperando.setFilterStyle("display: none; visibility: hidden;");
                oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
                oGrupo.setFilterStyle("display: none; visibility: hidden;");
                altoScrollOperandosGruposConceptos = "90";
                RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
                bandera = 0;
                filtradoListaOperandosGruposConceptos = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoOperando.setSecuencia(l);
            nuevoOperando.setProceso(procesoSeleccionado);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaOperandosGruposConceptosCrear.add(nuevoOperando);
            listaOperandosGruposConceptos.add(nuevoOperando);

            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOperandosGruposConceptos').hide()");
            nuevoOperando = new OperandosGruposConceptos();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroOperandosGruposConceptos");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();

        listaOperandosGruposConceptos.add(duplicarOperando);
        listaOperandosGruposConceptosCrear.add(duplicarOperando);

        RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
        index = -1;
        if (guardado == true) {
            guardado = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 1");
            oOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oOperando");
            oOperando.setFilterStyle("display: none; visibility: hidden;");
            oGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperandosGruposConceptos:oGrupo");
            oGrupo.setFilterStyle("display: none; visibility: hidden;");
            altoScrollOperandosGruposConceptos = "90";
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            bandera = 0;
            filtradoListaOperandosGruposConceptos = null;
            tipoLista = 0;
        }

        // OBTENER EL TERMINAL 
        duplicarOperando = new OperandosGruposConceptos();

        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroOperandosGruposConceptos");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOperandosGruposConceptos').hide()");

    }

    //DUPLICAR VIGENCIAS RETENCIONES/RETENCIONES
    public void duplicarE() {
        if (index >= 0) {
            System.out.println("Entra Duplicar Detalle Embargo");

            duplicarOperando = new OperandosGruposConceptos();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoLista == 0) {
                duplicarOperando.setSecuencia(n);
                duplicarOperando.setOperando(listaOperandosGruposConceptos.get(index).getOperando());
                duplicarOperando.setGrupoconcepto(listaOperandosGruposConceptos.get(index).getGrupoconcepto());
            }
            if (tipoLista == 1) {
                duplicarOperando.setSecuencia(n);
                duplicarOperando.setOperando(filtradoListaOperandosGruposConceptos.get(index).getOperando());
                duplicarOperando.setGrupoconcepto(filtradoListaOperandosGruposConceptos.get(index).getGrupoconcepto());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoGrupoConcepto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOperandosGruposConceptos').show()");
            index = -1;
            secRegistro = null;

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarOperandosGruposConceptos = listaOperandosGruposConceptos.get(index);
            }
            if (tipoLista == 1) {
                editarOperandosGruposConceptos = filtradoListaOperandosGruposConceptos.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOperando");
                RequestContext.getCurrentInstance().execute("PF('editarOperando').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoConcepto");
                RequestContext.getCurrentInstance().execute("PF('editarGrupoConcepto').show()");
                cualCelda = -1;
            }
            index = -1;
        }
        secRegistro = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //AUTOCOMPLETAR OperandosGruposConceptos
    public void modificarOperandosGruposConceptos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaOperandosGruposConceptosCrear.contains(listaOperandosGruposConceptos.get(index))) {

                    if (listaOperandosGruposConceptosModificar.isEmpty()) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosModificar.contains(listaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosModificar.add(listaOperandosGruposConceptos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaOperandosGruposConceptosCrear.contains(filtradoListaOperandosGruposConceptos.get(index))) {

                    if (listaOperandosGruposConceptosCrear.isEmpty()) {
                        listaOperandosGruposConceptosCrear.add(filtradoListaOperandosGruposConceptos.get(index));
                    } else if (!listaOperandosGruposConceptosCrear.contains(filtradoListaOperandosGruposConceptos.get(index))) {
                        listaOperandosGruposConceptosCrear.add(filtradoListaOperandosGruposConceptos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
        } else if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
            if (tipoLista == 0) {
                listaOperandosGruposConceptos.get(indice).getOperando().setDescripcion(Operando);
            } else {
                filtradoListaOperandosGruposConceptos.get(indice).getOperando().setDescripcion(Operando);
            }

            for (int i = 0; i < lovListaOperandos.size(); i++) {
                if (lovListaOperandos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaOperandosGruposConceptos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
                } else {
                    filtradoListaOperandosGruposConceptos.get(indice).setOperando(lovListaOperandos.get(indiceUnicoElemento));
                }
                lovListaOperandos.clear();
                getLovListaOperandos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
                RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
            if (tipoLista == 0) {
                listaOperandosGruposConceptos.get(indice).getGrupoconcepto().setDescripcion(Grupo);
            } else {
                filtradoListaOperandosGruposConceptos.get(indice).getGrupoconcepto().setDescripcion(Grupo);
            }

            for (int i = 0; i < lovListaGruposConceptos.size(); i++) {
                if (lovListaGruposConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaOperandosGruposConceptos.get(indice).setGrupoconcepto(lovListaGruposConceptos.get(indiceUnicoElemento));
                } else {
                    filtradoListaOperandosGruposConceptos.get(indice).setGrupoconcepto(lovListaGruposConceptos.get(indiceUnicoElemento));
                }
                lovListaGruposConceptos.clear();
                getLovListaGruposConceptos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:gruposDialogo");
                RequestContext.getCurrentInstance().execute("PF('gruposDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void cambiarProceso() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaOperandosGruposConceptosCrear.isEmpty() && listaOperandosGruposConceptosBorrar.isEmpty() && listaOperandosGruposConceptosModificar.isEmpty()) {
            secuenciaProceso = procesoSeleccionado.getSecuencia();
            listaOperandosGruposConceptos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandosGruposConceptos");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            operandoSeleccionado = listaOperandosGruposConceptos.get(index);
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    Operando = listaOperandosGruposConceptos.get(index).getOperando().getDescripcion();
                } else if (cualCelda == 1) {
                    Grupo = listaOperandosGruposConceptos.get(index).getGrupoconcepto().getDescripcion();
                }
                secRegistro = listaOperandosGruposConceptos.get(index).getSecuencia();
            } else {

                secRegistro = filtradoListaOperandosGruposConceptos.get(index).getSecuencia();
                if (cualCelda == 0) {
                    Operando = filtradoListaOperandosGruposConceptos.get(index).getOperando().getDescripcion();
                } else if (cualCelda == 1) {
                    Grupo = filtradoListaOperandosGruposConceptos.get(index).getGrupoconcepto().getDescripcion();
                }
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "OperandosGruposConceptosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;

    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OperandosGruposConceptosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO Concepto
    public void limpiarNuevoOperando() {
        nuevoOperando = new OperandosGruposConceptos();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR Concepto
    public void limpiarduplicarOperando() {
        duplicarOperando = new OperandosGruposConceptos();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaOperandosGruposConceptos.isEmpty()) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "OPERANDOSGRUPOSCONCEPTOS");
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("OPERANDOSGRUPOSCONCEPTOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //Getter & Setter
    public List<Procesos> getListaProcesos() {
        if (listaProcesos == null) {
            listaProcesos = administrarOperandosGruposConceptos.consultarProcesos();
            if (!listaProcesos.isEmpty()) {
                procesoSeleccionado = listaProcesos.get(0);
                secuenciaProceso = procesoSeleccionado.getSecuencia();
            }
        }
        return listaProcesos;
    }

    public void setListaProcesos(List<Procesos> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public List<Procesos> getFiltradoListaProcesos() {
        return filtradoListaProcesos;
    }

    public void setFiltradoListaProcesos(List<Procesos> filtradoListaProcesos) {
        this.filtradoListaProcesos = filtradoListaProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<OperandosGruposConceptos> getListaOperandosGruposConceptos() {
        if (listaOperandosGruposConceptos == null && procesoSeleccionado != null) {
            listaOperandosGruposConceptos = administrarOperandosGruposConceptos.consultarOperandosGrupos(secuenciaProceso);
        }
        return listaOperandosGruposConceptos;
    }

    public void setListaOperandosGruposConceptos(List<OperandosGruposConceptos> listaOperandosGruposConceptos) {
        this.listaOperandosGruposConceptos = listaOperandosGruposConceptos;
    }

    public List<OperandosGruposConceptos> getFiltradoListaOperandosGruposConceptos() {
        return filtradoListaOperandosGruposConceptos;
    }

    public void setFiltradoListaOperandosGruposConceptos(List<OperandosGruposConceptos> filtradoListaOperandosGruposConceptos) {
        this.filtradoListaOperandosGruposConceptos = filtradoListaOperandosGruposConceptos;
    }

    public OperandosGruposConceptos getOperandoSeleccionado() {
        return operandoSeleccionado;
    }

    public void setOperandoSeleccionado(OperandosGruposConceptos operandoSeleccionado) {
        this.operandoSeleccionado = operandoSeleccionado;
    }

    public String getAltoScrollProcesos() {
        return altoScrollProcesos;
    }

    public void setAltoScrollProcesos(String altoScrollProcesos) {
        this.altoScrollProcesos = altoScrollProcesos;
    }

    public String getAltoScrollOperandosGruposConceptos() {
        return altoScrollOperandosGruposConceptos;
    }

    public void setAltoScrollOperandosGruposConceptos(String altoScrollOperandosGruposConceptos) {
        this.altoScrollOperandosGruposConceptos = altoScrollOperandosGruposConceptos;
    }

    public Procesos getEditarProcesos() {
        return editarProcesos;
    }

    public void setEditarProcesos(Procesos editarProcesos) {
        this.editarProcesos = editarProcesos;
    }

    public OperandosGruposConceptos getEditarOperandosGruposConceptos() {
        return editarOperandosGruposConceptos;
    }

    public void setEditarOperandosGruposConceptos(OperandosGruposConceptos editarOperandosGruposConceptos) {
        this.editarOperandosGruposConceptos = editarOperandosGruposConceptos;
    }

    public Procesos getNuevoProcesos() {
        return nuevoProcesos;
    }

    public void setNuevoProcesos(Procesos nuevoProcesos) {
        this.nuevoProcesos = nuevoProcesos;
    }

    public OperandosGruposConceptos getNuevoOperando() {
        return nuevoOperando;
    }

    public void setNuevoOperando(OperandosGruposConceptos nuevoOperando) {
        this.nuevoOperando = nuevoOperando;
    }

    public OperandosGruposConceptos getDuplicarOperando() {
        return duplicarOperando;
    }

    public void setDuplicarOperando(OperandosGruposConceptos duplicarOperando) {
        this.duplicarOperando = duplicarOperando;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Procesos getDuplicarProcesos() {
        return duplicarProcesos;
    }

    public void setDuplicarProcesos(Procesos duplicarProcesos) {
        this.duplicarProcesos = duplicarProcesos;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public List<Operandos> getLovListaOperandos() {
        if (lovListaOperandos == null) {
            lovListaOperandos = administrarOperandosGruposConceptos.consultarOperandos();
        }
        return lovListaOperandos;
    }

    public void setLovListaOperandos(List<Operandos> lovListaOperandos) {
        this.lovListaOperandos = lovListaOperandos;
    }

    public List<Operandos> getLovfiltradoslistaOperandos() {
        return lovfiltradoslistaOperandos;
    }

    public void setLovfiltradoslistaOperandos(List<Operandos> lovfiltradoslistaOperandos) {
        this.lovfiltradoslistaOperandos = lovfiltradoslistaOperandos;
    }

    public Operandos getOperandosSeleccionado() {
        return operandosSeleccionado;
    }

    public void setOperandosSeleccionado(Operandos operandosSeleccionado) {
        this.operandosSeleccionado = operandosSeleccionado;
    }

    public List<GruposConceptos> getLovListaGruposConceptos() {
        if (lovListaGruposConceptos == null) {
            lovListaGruposConceptos = administrarOperandosGruposConceptos.consultarGrupos();
        }
        return lovListaGruposConceptos;
    }

    public void setLovListaGruposConceptos(List<GruposConceptos> lovListaGruposConceptos) {
        this.lovListaGruposConceptos = lovListaGruposConceptos;
    }

    public List<GruposConceptos> getLovfiltradoslistaGruposConceptos() {
        return lovfiltradoslistaGruposConceptos;
    }

    public void setLovfiltradoslistaGruposConceptos(List<GruposConceptos> lovfiltradoslistaGruposConceptos) {
        this.lovfiltradoslistaGruposConceptos = lovfiltradoslistaGruposConceptos;
    }

    public GruposConceptos getGruposSeleccionado() {
        return gruposSeleccionado;
    }

    public void setGruposSeleccionado(GruposConceptos gruposSeleccionado) {
        this.gruposSeleccionado = gruposSeleccionado;
    }

}
