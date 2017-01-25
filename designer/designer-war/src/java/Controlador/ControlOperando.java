/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Operandos;
import Entidades.Modulos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarOperandosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
public class ControlOperando implements Serializable {

    @EJB
    AdministrarOperandosInterface administrarOperandos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private Operandos operandoActual;
    private Operandos operandoRegistro;
    //LISTA INFOREPORTES
    private List<Operandos> listaOperandos;
    private List<Operandos> filtradosListaOperandos;
    //L.O.V INFOREPORTES
    private List<Operandos> lovlistaOperandos;
    private List<Operandos> lovfiltradoslistaOperandos;
    private Operandos operandosSeleccionado;
    //editar celda
    private Operandos editarOperandos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private BigInteger secRegistro;
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<Operandos> listaOperandosCrear;
    public Operandos nuevoOperando;
    public Operandos duplicarOperando;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<Operandos> listaOperandosModificar;
    //Borrar Novedades
    private List<Operandos> listaOperandosBorrar;
    //L.O.V MODULOS
    private List<Modulos> lovListaModulos;
    private List<Modulos> lovFiltradoslistaModulos;
    private Modulos moduloSeleccionado;
    //AUTOCOMPLETAR
    private String Modulo;
    //Columnas Tabla Ciudades
    private Column operandosNombres, operandosTipos, operandosValores, operandosDescripciones, operandosCodigos;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //Cambios de PAgina
    public String action;
    public BigInteger secuenciaOperando;
    public String tipoOperando;

    public ControlOperando() {
        cambiosPagina = true;
        nuevoOperando = new Operandos();
        nuevoOperando.setCodigo(0);
        nuevoOperando.setDescripcion(" ");
        nuevoOperando.setNombre(" ");
        nuevoOperando.setTipo("CONSTANTE");
        lovListaModulos = null;
        permitirIndex = true;
        listaOperandos = null;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaOperandosBorrar = new ArrayList<Operandos>();
        listaOperandosCrear = new ArrayList<Operandos>();
        listaOperandosModificar = new ArrayList<Operandos>();
        altoTabla = "270";
        duplicarOperando = new Operandos();
    }
    
       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
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
         String pagActual = "cargo"XXX;
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
            administrarOperandos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            operandoRegistro = listaOperandos.get(index);
            if (tipoLista == 0) {
                secRegistro = listaOperandos.get(index).getSecuencia();
            } else {
                secRegistro = filtradosListaOperandos.get(index).getSecuencia();
            }
        }
    }

    //AUTOCOMPLETAR
    public void modificarOperandos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaOperandosCrear.contains(listaOperandos.get(indice))) {

                    if (listaOperandosModificar.isEmpty()) {
                        listaOperandosModificar.add(listaOperandos.get(indice));
                    } else if (!listaOperandosModificar.contains(listaOperandos.get(indice))) {
                        listaOperandosModificar.add(listaOperandos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaOperandosCrear.contains(filtradosListaOperandos.get(indice))) {

                    if (listaOperandosCrear.isEmpty()) {
                        listaOperandosCrear.add(filtradosListaOperandos.get(indice));
                    } else if (!listaOperandosCrear.contains(filtradosListaOperandos.get(indice))) {
                        listaOperandosCrear.add(filtradosListaOperandos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosOperandos");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void seleccionarTipo(String estadoTipo, int indice, int celda) {
        if (tipoLista == 0) {
            if (estadoTipo.equals("FORMULA")) {
                listaOperandos.get(indice).setTipo("FORMULA");
            } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
                listaOperandos.get(indice).setTipo("BLOQUE PL/SQL");
            } else if (estadoTipo.equals("CONSTANTE")) {
                listaOperandos.get(indice).setTipo("CONSTANTE");
            } else if (estadoTipo.equals("FUNCION")) {
                listaOperandos.get(indice).setTipo("FUNCION");
            } else if (estadoTipo.equals("RELACIONAL")) {
                listaOperandos.get(indice).setTipo("RELACIONAL");
            }

            if (!listaOperandosCrear.contains(listaOperandos.get(indice))) {
                if (listaOperandosModificar.isEmpty()) {
                    listaOperandosModificar.add(listaOperandos.get(indice));
                } else if (!listaOperandosModificar.contains(listaOperandos.get(indice))) {
                    listaOperandosModificar.add(listaOperandos.get(indice));
                }
            }
        } else {
            if (estadoTipo.equals("FORMULA")) {
                filtradosListaOperandos.get(indice).setTipo("FORMULA");
            } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
                filtradosListaOperandos.get(indice).setTipo("BLOQUE PL/SQL");
            } else if (estadoTipo.equals("CONSTANTE")) {
                filtradosListaOperandos.get(indice).setTipo("CONSTANTE");
            } else if (estadoTipo.equals("FUNCION")) {
                filtradosListaOperandos.get(indice).setTipo("FUNCION");
            } else if (estadoTipo.equals("RELACIONAL")) {
                filtradosListaOperandos.get(indice).setTipo("RELACIONAL");
            }
            if (!listaOperandosCrear.contains(filtradosListaOperandos.get(indice))) {
                if (listaOperandosModificar.isEmpty()) {
                    listaOperandosModificar.add(filtradosListaOperandos.get(indice));
                } else if (!listaOperandosModificar.contains(filtradosListaOperandos.get(indice))) {
                    listaOperandosModificar.add(filtradosListaOperandos.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosOperandos");
    }

    public void guardarVariables(BigInteger secuencia) {
        if (operandoRegistro == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
        if (listaOperandosCrear.isEmpty() && listaOperandosBorrar.isEmpty() && listaOperandosModificar.isEmpty()) {
            if (operandoRegistro != null) {
                secuenciaOperando = operandoRegistro.getSecuencia();

                System.out.println("secuenciaOperando" + secuenciaOperando + "operandoRegistro" + operandoRegistro);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('dirigirDetalle()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
        operandoRegistro = null;
    }

    public void verificarTipo(BigInteger secuencia) {

        if (listaOperandosCrear.isEmpty() && listaOperandosBorrar.isEmpty() && listaOperandosModificar.isEmpty()) {
            if (operandoRegistro != null) {
                if (operandoRegistro.getTipo().equals("FUNCION")) {
                    action = "funcion";
                    secuenciaOperando = operandoRegistro.getSecuencia();
                    tipoOperando = operandoRegistro.getTipo();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().execute("PF('dirigirTipoFuncion()");
                }
                if (operandoRegistro.getTipo().equals("FORMULA")) {
                    action = "formula";
                    secuenciaOperando = operandoRegistro.getSecuencia();
                    tipoOperando = operandoRegistro.getTipo();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().execute("PF('dirigirTipoFormula()");
                }
                if (operandoRegistro.getTipo().equals("CONSTANTE")) {
                    action = "constante";
                    secuenciaOperando = operandoRegistro.getSecuencia();
                    tipoOperando = operandoRegistro.getTipo();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().execute("PF('dirigirTipoConstante()");
                }
                if (operandoRegistro.getTipo().equals("BLOQUE PL/SQL")) {
                    action = "bloque";
                    secuenciaOperando = operandoRegistro.getSecuencia();
                    tipoOperando = operandoRegistro.getTipo();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().execute("PF('dirigirTipoBloque()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

        }
        operandoRegistro = null;
    }

    public void seleccionarTipoNuevoOperando(String estadoTipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipo.equals("FORMULA")) {
                nuevoOperando.setTipo("FORMULA");
            } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
                nuevoOperando.setTipo("BLOQUE PL/SQL");
            } else if (estadoTipo.equals("CONSTANTE")) {
                nuevoOperando.setTipo("CONSTANTE");
            } else if (estadoTipo.equals("FUNCION")) {
                nuevoOperando.setTipo("FUNCION");
            } else if (estadoTipo.equals("RELACIONAL")) {
                nuevoOperando.setTipo("RELACIONAL");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoOperando");
        } else {
            if (estadoTipo.equals("FORMULA")) {
                duplicarOperando.setTipo("FORMULA");
            } else if (estadoTipo.equals("BLOQUE PL/SQL")) {
                duplicarOperando.setTipo("BLOQUE PL/SQL");
            } else if (estadoTipo.equals("CONSTANTE")) {
                duplicarOperando.setTipo("CONSTANTE");
            } else if (estadoTipo.equals("FUNCION")) {
                duplicarOperando.setTipo("FUNCION");
            } else if (estadoTipo.equals("RELACIONAL")) {
                duplicarOperando.setTipo("RELACIONAL");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoOperando");
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

        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaOperandos.isEmpty()) {
            listaOperandos.clear();
            listaOperandos = administrarOperandos.buscarOperandos();
        } else {
            listaOperandos = administrarOperandos.buscarOperandos();
        }

        RequestContext.getCurrentInstance().update("form:datosOperandos");
        filtradosListaOperandos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarOperando.getDescripcion() == null || duplicarOperando.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Descripcion\n";
            pasa++;
        }

        if (duplicarOperando.getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }

        if (duplicarOperando.getNombre().equals(" ") || duplicarOperando.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }
        if (duplicarOperando.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoOperando");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoOperando').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
                operandosNombres.setFilterStyle("display: none; visibility: hidden;");
                operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
                operandosTipos.setFilterStyle("display: none; visibility: hidden;");
                operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
                operandosValores.setFilterStyle("display: none; visibility: hidden;");
                operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
                operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
                operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
                operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosOperandos");
                bandera = 0;
                filtradosListaOperandos = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaOperandos.add(duplicarOperando);
            listaOperandosCrear.add(duplicarOperando);

            index = -1;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            duplicarOperando = new Operandos();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarOperando");
            RequestContext.getCurrentInstance().execute("PF('DuplicarOperando').hide()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarOperandos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaOperandos.isEmpty()) {
            listaOperandos.clear();
            listaOperandos.add(operandosSeleccionado);

        } else {
            listaOperandos.add(operandosSeleccionado);
        }
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        context.reset("formularioDialogos:LOVOperandos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVOperandos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVOperandos");
        RequestContext.getCurrentInstance().update("form:datosOperandos");
        filtradosListaOperandos = null;
        operandosSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "250";
            operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
            operandosNombres.setFilterStyle("width: 85% !important;");
            operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
            operandosTipos.setFilterStyle("width: 85% !important;");
            operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
            operandosValores.setFilterStyle("width: 85% !important;");
            operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
            operandosDescripciones.setFilterStyle("width: 85% !important;");
            operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
            operandosCodigos.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
            operandosNombres.setFilterStyle("display: none; visibility: hidden;");
            operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
            operandosTipos.setFilterStyle("display: none; visibility: hidden;");
            operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
            operandosValores.setFilterStyle("display: none; visibility: hidden;");
            operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
            operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
            operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
            operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            bandera = 0;
            filtradosListaOperandos = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
            operandosNombres.setFilterStyle("display: none; visibility: hidden;");
            operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
            operandosTipos.setFilterStyle("display: none; visibility: hidden;");
            operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
            operandosValores.setFilterStyle("display: none; visibility: hidden;");
            operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
            operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
            operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
            operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            bandera = 0;
            filtradosListaOperandos = null;
            tipoLista = 0;
        }

        listaOperandosBorrar.clear();
        listaOperandosCrear.clear();
        listaOperandosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaOperandos = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = false;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosOperandos");
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

    public void cancelarCambioModulos() {
        lovFiltradoslistaModulos = null;
        moduloSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarOperandos = listaOperandos.get(index);
            }
            if (tipoLista == 1) {
                editarOperandos = filtradosListaOperandos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombres");
                RequestContext.getCurrentInstance().execute("PF('editarNombres').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValores");
                RequestContext.getCurrentInstance().execute("PF('editarValores').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripciones");
                RequestContext.getCurrentInstance().execute("PF('editarDescripciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
                RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "OperandosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOperandosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OperandosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //LIMPIAR NUEVO REGISTRO CIUDAD

    public void limpiarNuevoOperando() {
        nuevoOperando = new Operandos();
        nuevoOperando.setCodigo(0);
        nuevoOperando.setDescripcion(" ");
        nuevoOperando.setNombre(" ");
        nuevoOperando.setTipo("CONSTANTE");
        index = -1;
        secRegistro = null;
    }

    public void limpiarduplicarOperandos() {
        duplicarOperando = new Operandos();
        duplicarOperando.setCodigo(0);
        duplicarOperando.setDescripcion(" ");
        duplicarOperando.setNombre(" ");
        duplicarOperando.setTipo("CONSTANTE");
        index = -1;
        secRegistro = null;
    }

    //CREAR Operando
    public void agregarNuevoOperando() {
        int pasa = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoOperando.getDescripcion() == null || nuevoOperando.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Descripcion\n";
            pasa++;
        }

        if (nuevoOperando.getNombre().equals(" ") || nuevoOperando.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " * Nombre\n";
            pasa++;
        }
        if (nuevoOperando.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoOperando");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoOperando').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altoTabla = "270";
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
                operandosNombres.setFilterStyle("display: none; visibility: hidden;");
                operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
                operandosTipos.setFilterStyle("display: none; visibility: hidden;");
                operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
                operandosValores.setFilterStyle("display: none; visibility: hidden;");
                operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
                operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
                operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
                operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosOperandos");
                bandera = 0;
                filtradosListaOperandos = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoOperando.setSecuencia(l);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaOperandosCrear.add(nuevoOperando);
            listaOperandos.add(nuevoOperando);
            nuevoOperando = new Operandos();
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoOperando').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    //BORRAR CIUDADES
    public void borrarOperandos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaOperandosModificar.isEmpty() && listaOperandosModificar.contains(listaOperandos.get(index))) {
                    int modIndex = listaOperandosModificar.indexOf(listaOperandos.get(index));
                    listaOperandosModificar.remove(modIndex);
                    listaOperandosBorrar.add(listaOperandos.get(index));
                } else if (!listaOperandosCrear.isEmpty() && listaOperandosCrear.contains(listaOperandos.get(index))) {
                    int crearIndex = listaOperandosCrear.indexOf(listaOperandos.get(index));
                    listaOperandosCrear.remove(crearIndex);
                } else {
                    listaOperandosBorrar.add(listaOperandos.get(index));
                }
                listaOperandos.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaOperandosModificar.isEmpty() && listaOperandosModificar.contains(filtradosListaOperandos.get(index))) {
                    int modIndex = listaOperandosModificar.indexOf(filtradosListaOperandos.get(index));
                    listaOperandosModificar.remove(modIndex);
                    listaOperandosBorrar.add(filtradosListaOperandos.get(index));
                } else if (!listaOperandosCrear.isEmpty() && listaOperandosCrear.contains(filtradosListaOperandos.get(index))) {
                    int crearIndex = listaOperandosCrear.indexOf(filtradosListaOperandos.get(index));
                    listaOperandosCrear.remove(crearIndex);
                } else {
                    listaOperandosBorrar.add(filtradosListaOperandos.get(index));
                }
                int CIndex = listaOperandos.indexOf(filtradosListaOperandos.get(index));
                listaOperandos.remove(CIndex);
                filtradosListaOperandos.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //GUARDAR
    public void guardarCambiosOperandos() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaOperandosBorrar.isEmpty()) {
                for (int i = 0; i < listaOperandosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaOperandosBorrar.size());

                    administrarOperandos.borrarOperando(listaOperandosBorrar.get(i));
                }
                System.out.println("Entra");
                listaOperandosBorrar.clear();
            }

            if (!listaOperandosCrear.isEmpty()) {
                for (int i = 0; i < listaOperandosCrear.size(); i++) {
                    System.out.println("Creando...");

                    administrarOperandos.crearOperando(listaOperandosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaOperandosCrear.clear();
            }
            if (!listaOperandosModificar.isEmpty()) {
                administrarOperandos.modificarOperando(listaOperandosModificar);
                listaOperandosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaOperandos = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        secRegistro = null;
    }

    //DUPLICAR Operando
    public void duplicarO() {
        if (index >= 0) {
            duplicarOperando = new Operandos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarOperando.setSecuencia(l);
                duplicarOperando.setNombre(listaOperandos.get(index).getNombre());
                duplicarOperando.setTipo(listaOperandos.get(index).getTipo());
                duplicarOperando.setDescripcion(listaOperandos.get(index).getDescripcion());
                duplicarOperando.setCambioanual(listaOperandos.get(index).getCambioanual());
                duplicarOperando.setActualizable(listaOperandos.get(index).getActualizable());
                duplicarOperando.setCodigo(listaOperandos.get(index).getCodigo());
            }
            if (tipoLista == 1) {
                duplicarOperando.setSecuencia(l);
                duplicarOperando.setNombre(filtradosListaOperandos.get(index).getNombre());
                duplicarOperando.setTipo(filtradosListaOperandos.get(index).getTipo());
                duplicarOperando.setDescripcion(filtradosListaOperandos.get(index).getDescripcion());
                duplicarOperando.setCambioanual(filtradosListaOperandos.get(index).getCambioanual());
                duplicarOperando.setActualizable(filtradosListaOperandos.get(index).getActualizable());
                duplicarOperando.setCodigo(filtradosListaOperandos.get(index).getCodigo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
            RequestContext.getCurrentInstance().execute("PF('DuplicarOperando').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaOperandos.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "OPERANDOS");
                System.out.println("resultado: " + result);
                if (result == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (result == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (result == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (result == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (result == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("OPERANDOS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "270";
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            operandosNombres = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosNombres");
            operandosNombres.setFilterStyle("display: none; visibility: hidden;");
            operandosTipos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosTipos");
            operandosTipos.setFilterStyle("display: none; visibility: hidden;");
            operandosValores = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosValores");
            operandosValores.setFilterStyle("display: none; visibility: hidden;");
            operandosDescripciones = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosDescripciones");
            operandosDescripciones.setFilterStyle("display: none; visibility: hidden;");
            operandosCodigos = (Column) c.getViewRoot().findComponent("form:datosOperandos:operandosCodigos");
            operandosCodigos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOperandos");
            bandera = 0;
            filtradosListaOperandos = null;
            tipoLista = 0;
        }
        listaOperandosBorrar.clear();
        listaOperandosCrear.clear();
        listaOperandosModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaOperandos = null;
        guardado = true;
        permitirIndex = true;

    }

    //Getter & Setter
    public List<Operandos> getListaOperandos() {
        if (listaOperandos == null) {
            listaOperandos = administrarOperandos.buscarOperandos();
            if (!listaOperandos.isEmpty()) {
                for (int i = 0; i < listaOperandos.size(); i++) {
                    String valor;
                    valor = administrarOperandos.buscarValores(listaOperandos.get(i).getSecuencia());
                    listaOperandos.get(i).setValor(valor);
                }
            }
        }
        return listaOperandos;
    }

    public void setListaOperandos(List<Operandos> listaOperandos) {
        this.listaOperandos = listaOperandos;
    }

    public List<Operandos> getFiltradosListaOperandos() {
        return filtradosListaOperandos;
    }

    public void setFiltradosListaOperandos(List<Operandos> filtradosListaOperandos) {
        this.filtradosListaOperandos = filtradosListaOperandos;
    }

    public List<Operandos> getLovlistaOperandos() {
        if (lovlistaOperandos == null) {
            lovlistaOperandos = administrarOperandos.buscarOperandos();
        }
        return lovlistaOperandos;
    }

    public void setLovlistaOperandos(List<Operandos> lovlistaOperandos) {
        this.lovlistaOperandos = lovlistaOperandos;
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

    public boolean isAceptar() {
        return aceptar;
    }

    public Operandos getEditarOperandos() {
        return editarOperandos;
    }

    public void setEditarOperandos(Operandos editarOperandos) {
        this.editarOperandos = editarOperandos;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public Operandos getNuevoOperando() {
        return nuevoOperando;
    }

    public void setNuevoOperando(Operandos nuevoOperando) {
        this.nuevoOperando = nuevoOperando;
    }

    public Operandos getDuplicarOperando() {
        return duplicarOperando;
    }

    public void setDuplicarOperando(Operandos duplicarOperando) {
        this.duplicarOperando = duplicarOperando;
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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigInteger getSecuenciaOperando() {
        return secuenciaOperando;
    }

    public void setSecuenciaOperando(BigInteger secuenciaOperando) {
        this.secuenciaOperando = secuenciaOperando;
    }

    public String getTipoOperando() {
        return tipoOperando;
    }

    public void setTipoOperando(String tipoOperando) {
        this.tipoOperando = tipoOperando;
    }

    public Operandos getOperandoSeleccionado() {
        return operandoRegistro;
    }

    public void setOperandoSeleccionado(Operandos operandoRegistro) {
        this.operandoRegistro = operandoRegistro;
    }

    public Operandos getOperandoActual() {
        return operandoActual;
    }

    public void setOperandoActual(Operandos operandoActual) {
        this.operandoActual = operandoActual;
    }

    public Operandos getOperandoRegistro() {
        return operandoRegistro;
    }

    public void setOperandoRegistro(Operandos operandoRegistro) {
        this.operandoRegistro = operandoRegistro;
    }

}
