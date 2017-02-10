/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.TiposBloques;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposBloquesInterface;
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
public class ControlTipoBloque implements Serializable {

    @EJB
    AdministrarTiposBloquesInterface administrarTiposBloques;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Parametros que llegan
    private BigInteger secOperando;
    private Operandos operando;
    private String tOperando;
    //Parametros que envia
    private BigInteger secOperando2;
    private Operandos operando2;
    //LISTA INFOREPORTES
    private List<TiposBloques> listaTiposBloques;
    private List<TiposBloques> filtradosListaTiposBloques;
    private TiposBloques tipoBloqueSeleccionado;
    //L.O.V INFOREPORTES
    private List<TiposBloques> lovlistaTiposBloques;
    private List<TiposBloques> lovfiltradoslistaTiposBloques;
    private TiposBloques operandosSeleccionado;
    //editar celda
    private TiposBloques editarTiposBloques;
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
    private List<TiposBloques> listaTiposBloquesCrear;
    public TiposBloques nuevoTipoBloque;
    public TiposBloques duplicarTipoBloque;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<TiposBloques> listaTiposBloquesModificar;
    //Borrar Novedades
    private List<TiposBloques> listaTiposBloquesBorrar;
    //AUTOCOMPLETAR
    private String Formula;
    //Columnas Tabla Ciudades
    private Column tiposBloquesIniciales, tiposBloquesFinales, tiposBloquesTipos, tiposBloquesSQL;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private boolean cambiosPagina;
    //Booleanos para activar casillas en la Tabla
    private boolean numericoB;
    private boolean fechaB;
    private boolean cadenaB;
    //Booleanos para activar casillas en nuevo registro
    private boolean numericoBN;
    private boolean fechaBN;
    private boolean cadenaBN;
    //Booleanos para activar casillas en duplicar registro
    private boolean numericoBD;
    private boolean fechaBD;
    private boolean cadenaBD;
    //String que guarda el editar
    private String editorE;
    public BigInteger secuenciaOperando;
    public String tipoOperando;
    public Operandos operandoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTipoBloque() {
        listaTiposBloques = null;
        cambiosPagina = true;
        nuevoTipoBloque = new TiposBloques();
        nuevoTipoBloque.setFechainicial(new Date());
        permitirIndex = true;
        permitirIndex = true;
        aceptar = true;
        secRegistro = null;
        guardado = true;
        tipoLista = 0;
        listaTiposBloquesBorrar = new ArrayList<TiposBloques>();
        listaTiposBloquesCrear = new ArrayList<TiposBloques>();
        listaTiposBloquesModificar = new ArrayList<TiposBloques>();
        altoTabla = "270";
        duplicarTipoBloque = new TiposBloques();
        nuevoTipoBloque.setFechainicial(new Date());
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRastros.obtenerConexion(ses.getId());
            administrarTiposBloques.obtenerConexion(ses.getId());
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
            String pagActual = "tipobloque";
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

    public void recibirDatosOperando(BigInteger secuenciaOperando, String tipoOperando, Operandos operandoRegistro) {
        secOperando = secuenciaOperando;
        tOperando = tipoOperando;
        operando = operandoRegistro;
        listaTiposBloques = null;
        getListaTiposBloques();
    }

    public void guardarVariables(int indice, BigInteger secuencia) {
        if (listaTiposBloquesBorrar.isEmpty() && listaTiposBloquesCrear.isEmpty() && listaTiposBloquesModificar.isEmpty()) {
            secOperando2 = secOperando;
            operando2 = operando;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('dirigirDependencia()");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

        }

    }

    public void cambiarEditor() {
        System.out.println("Bloque Tipos Bloques : " + editarTiposBloques.getBloqueplsql());
        System.out.println("Editar Tipo Bloque" + editarTiposBloques.getSecuencia());
        for (int i = 0; i < listaTiposBloques.size(); i++) {
            if (editarTiposBloques.getSecuencia().equals(listaTiposBloques.get(i).getSecuencia())) {
                System.out.println("ENTRO");
                listaTiposBloques.get(i).setBloqueplsql(editarTiposBloques.getBloqueplsql());
                listaTiposBloquesModificar.add(listaTiposBloques.get(i));
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposBloques");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposBloques = listaTiposBloques.get(index);
            }
            if (tipoLista == 1) {
                editarTiposBloques = filtradosListaTiposBloques.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSQL");
                RequestContext.getCurrentInstance().execute("PF('editarSQL').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

//UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                secRegistro = listaTiposBloques.get(index).getSecuencia();
                editorE = listaTiposBloques.get(index).getBloqueplsql();
            } else {
                secRegistro = filtradosListaTiposBloques.get(index).getSecuencia();
                editorE = filtradosListaTiposBloques.get(index).getBloqueplsql();
            }
        }
    }

//BORRAR CIUDADES
    public void borrarTipoBloque() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaTiposBloquesModificar.isEmpty() && listaTiposBloquesModificar.contains(listaTiposBloques.get(index))) {
                    int modIndex = listaTiposBloquesModificar.indexOf(listaTiposBloques.get(index));
                    listaTiposBloquesModificar.remove(modIndex);
                    listaTiposBloquesBorrar.add(listaTiposBloques.get(index));
                } else if (!listaTiposBloquesCrear.isEmpty() && listaTiposBloquesCrear.contains(listaTiposBloques.get(index))) {
                    int crearIndex = listaTiposBloquesCrear.indexOf(listaTiposBloques.get(index));
                    listaTiposBloquesCrear.remove(crearIndex);
                } else {
                    listaTiposBloquesBorrar.add(listaTiposBloques.get(index));
                }
                listaTiposBloques.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaTiposBloquesModificar.isEmpty() && listaTiposBloquesModificar.contains(filtradosListaTiposBloques.get(index))) {
                    int modIndex = listaTiposBloquesModificar.indexOf(filtradosListaTiposBloques.get(index));
                    listaTiposBloquesModificar.remove(modIndex);
                    listaTiposBloquesBorrar.add(filtradosListaTiposBloques.get(index));
                } else if (!listaTiposBloquesCrear.isEmpty() && listaTiposBloquesCrear.contains(filtradosListaTiposBloques.get(index))) {
                    int crearIndex = listaTiposBloquesCrear.indexOf(filtradosListaTiposBloques.get(index));
                    listaTiposBloquesCrear.remove(crearIndex);
                } else {
                    listaTiposBloquesBorrar.add(filtradosListaTiposBloques.get(index));
                }
                int CIndex = listaTiposBloques.indexOf(filtradosListaTiposBloques.get(index));
                listaTiposBloques.remove(CIndex);
                filtradosListaTiposBloques.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "250";
            tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
            tiposBloquesIniciales.setFilterStyle("width: 85% !important;");
            tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
            tiposBloquesFinales.setFilterStyle("width: 85% !important;");
            tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
            tiposBloquesTipos.setFilterStyle("width: 85% !important;");
            tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
            tiposBloquesSQL.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
            tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
            tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
            tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
            tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            bandera = 0;
            filtradosListaTiposBloques = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
            tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
            tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
            tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
            tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            bandera = 0;
            filtradosListaTiposBloques = null;
            tipoLista = 0;
        }
        listaTiposBloquesBorrar.clear();
        listaTiposBloquesCrear.clear();
        listaTiposBloquesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposBloques = null;
        guardado = true;
        permitirIndex = true;

    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            altoTabla = "270";
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
            tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
            tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
            tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
            tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
            tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            bandera = 0;
            filtradosListaTiposBloques = null;
            tipoLista = 0;
        }

        listaTiposBloquesBorrar.clear();
        listaTiposBloquesCrear.clear();
        listaTiposBloquesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaTiposBloques = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposBloques");
    }

    //DUPLICAR Operando
    public void duplicarTC() {
        if (index >= 0) {
            duplicarTipoBloque = new TiposBloques();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoBloque.setSecuencia(l);
                duplicarTipoBloque.setFechainicial(listaTiposBloques.get(index).getFechainicial());
                duplicarTipoBloque.setFechafinal(listaTiposBloques.get(index).getFechafinal());
                duplicarTipoBloque.setOperando(listaTiposBloques.get(index).getOperando());
                duplicarTipoBloque.setTipo(listaTiposBloques.get(index).getTipo());
                duplicarTipoBloque.setBloqueplsql(listaTiposBloques.get(index).getBloqueplsql());
            }
            if (tipoLista == 1) {
                duplicarTipoBloque.setSecuencia(l);
                duplicarTipoBloque.setFechainicial(filtradosListaTiposBloques.get(index).getFechainicial());
                duplicarTipoBloque.setFechafinal(filtradosListaTiposBloques.get(index).getFechafinal());
                duplicarTipoBloque.setOperando(filtradosListaTiposBloques.get(index).getOperando());
                duplicarTipoBloque.setTipo(filtradosListaTiposBloques.get(index).getTipo());
                duplicarTipoBloque.setBloqueplsql(filtradosListaTiposBloques.get(index).getBloqueplsql());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoBloque");
            RequestContext.getCurrentInstance().execute("PF('DuplicarTipoBloque').show()");
            index = -1;
            secRegistro = null;
        }
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaTiposBloques.isEmpty()) {
            if (secRegistro != null) {
                int result = administrarRastros.obtenerTabla(secRegistro, "TIPOSCONSTANTES");
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSCONSTANTES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //GUARDAR
    public void guardarCambiosTiposBloques() {

        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaTiposBloquesBorrar.isEmpty()) {
                for (int i = 0; i < listaTiposBloquesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaTiposBloquesBorrar.size());
                    administrarTiposBloques.borrarTiposBloques(listaTiposBloquesBorrar.get(i));
                }
                System.out.println("Entra");
                listaTiposBloquesBorrar.clear();
            }

            if (!listaTiposBloquesCrear.isEmpty()) {
                for (int i = 0; i < listaTiposBloquesCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarTiposBloques.crearTiposBloques(listaTiposBloquesCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaTiposBloquesCrear.clear();
            }
            if (!listaTiposBloquesModificar.isEmpty()) {
                for (int i = 0; i < listaTiposBloquesModificar.size(); i++) {
                    administrarTiposBloques.modificarTiposBloques(listaTiposBloquesModificar.get(i));
                }
                listaTiposBloquesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaTiposBloques = null;

            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

            //  k = 0;
        }
        index = -1;
        secRegistro = null;
    }

    public void seleccionarTipo(String estadoTipo, int indice, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoLista == 0) {
            if (estadoTipo.equals("NUMERICO")) {
                listaTiposBloques.get(indice).setTipo("NUMBER");
            } else if (estadoTipo.equals("CARACTER")) {
                listaTiposBloques.get(indice).setTipo("VARCHAR");
            } else if (estadoTipo.equals("FECHA")) {
                listaTiposBloques.get(indice).setTipo("DATE");
            }

            if (!listaTiposBloquesCrear.contains(listaTiposBloques.get(indice))) {
                if (listaTiposBloquesModificar.isEmpty()) {
                    listaTiposBloquesModificar.add(listaTiposBloques.get(indice));
                } else if (!listaTiposBloquesModificar.contains(listaTiposBloques.get(indice))) {
                    listaTiposBloquesModificar.add(listaTiposBloques.get(indice));
                }
            }
        } else {
            if (estadoTipo.equals("NUMERICO")) {
                filtradosListaTiposBloques.get(indice).setTipo("NUMBER");
            } else if (estadoTipo.equals("CARACTER")) {
                filtradosListaTiposBloques.get(indice).setTipo("VARCHAR");
            } else if (estadoTipo.equals("FECHA")) {
                filtradosListaTiposBloques.get(indice).setTipo("DATE");

            }
            if (!listaTiposBloquesCrear.contains(filtradosListaTiposBloques.get(indice))) {
                if (listaTiposBloquesModificar.isEmpty()) {
                    listaTiposBloquesModificar.add(filtradosListaTiposBloques.get(indice));
                } else if (!listaTiposBloquesModificar.contains(filtradosListaTiposBloques.get(indice))) {
                    listaTiposBloquesModificar.add(filtradosListaTiposBloques.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosTiposBloques");
    }

    public void seleccionarTipoNuevoTipoBloque(String estadoTipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipo.equals("NUMERICO")) {
                nuevoTipoBloque.setTipo("NUMBER");
            } else if (estadoTipo.equals("CARACTER")) {
                nuevoTipoBloque.setTipo("VARCHAR");
            } else if (estadoTipo.equals("FECHA")) {
                nuevoTipoBloque.setTipo("DATE");
            }
        } else if (estadoTipo.equals("NUMERICO")) {
            duplicarTipoBloque.setTipo("NUMBER");
        } else if (estadoTipo.equals("CARACTER")) {
            duplicarTipoBloque.setTipo("VARCHAR");
        } else if (estadoTipo.equals("FECHA")) {
            duplicarTipoBloque.setTipo("DATE");
        }

    }

//AUTOCOMPLETAR
    public void modificarTiposBloques(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposBloquesCrear.contains(listaTiposBloques.get(index))) {

                    if (listaTiposBloquesModificar.isEmpty()) {
                        listaTiposBloquesModificar.add(listaTiposBloques.get(index));
                    } else if (!listaTiposBloquesModificar.contains(listaTiposBloques.get(index))) {
                        listaTiposBloquesModificar.add(listaTiposBloques.get(index));
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
                if (!listaTiposBloquesCrear.contains(filtradosListaTiposBloques.get(index))) {

                    if (listaTiposBloquesCrear.isEmpty()) {
                        listaTiposBloquesCrear.add(filtradosListaTiposBloques.get(index));
                    } else if (!listaTiposBloquesCrear.contains(filtradosListaTiposBloques.get(index))) {
                        listaTiposBloquesCrear.add(filtradosListaTiposBloques.get(index));
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
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void agregarNuevoTipoBloque() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoTipoBloque.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (nuevoTipoBloque.getFechafinal() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
            pasa++;
        }

        if (nuevoTipoBloque.getFechainicial() != null && nuevoTipoBloque.getFechafinal() != null) {
            if (nuevoTipoBloque.getFechafinal().before(nuevoTipoBloque.getFechainicial())) {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                pasa2++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoBloque");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoBloque').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "270";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
                tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
                tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
                tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
                tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposBloques");
                bandera = 0;
                filtradosListaTiposBloques = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoBloque.setSecuencia(l);
            System.out.println("Operando: " + operando);
            nuevoTipoBloque.setOperando(operando);

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaTiposBloquesCrear.add(nuevoTipoBloque);
            listaTiposBloques.add(nuevoTipoBloque);
            nuevoTipoBloque = new TiposBloques();
            RequestContext.getCurrentInstance().update("form:datosTiposBloques");
            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoTipoBloque').hide()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarTipoBloque.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoBloque");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoBloque').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                altoTabla = "270";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
                tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
                tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
                tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
                tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
                tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposBloques");
                bandera = 0;
                filtradosListaTiposBloques = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            //Falta Ponerle el Operando al cual se agregará
            duplicarTipoBloque.setOperando(operando);
            listaTiposBloques.add(duplicarTipoBloque);
            listaTiposBloquesCrear.add(duplicarTipoBloque);

            index = -1;
            if (guardado == true) {
                guardado = false;
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
            duplicarTipoBloque = new TiposBloques();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoBloque");
            RequestContext.getCurrentInstance().execute("PF('DuplicarTipoBloque').hide()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposBloquesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TiposBloquesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposBloquesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TiposBloquesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO CIUDAD
    public void limpiarNuevoTiposBloques() {
        nuevoTipoBloque = new TiposBloques();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR Duplicar REGISTRO CIUDAD
    public void limpiarDuplicarTiposBloques() {
        duplicarTipoBloque = new TiposBloques();
        index = -1;
        secRegistro = null;
    }

    //Getter & Setter
    public List<TiposBloques> getListaTiposBloques() {
        if (listaTiposBloques == null) {
            System.out.println("secOperando" + secOperando);
            System.out.println("tOperando" + tOperando);
            System.out.println("operando seleccionado" + operando);
            listaTiposBloques = administrarTiposBloques.buscarTiposBloques(secOperando, tOperando);
        }
        return listaTiposBloques;
    }

    public void setListaTiposBloques(List<TiposBloques> listaTiposBloques) {
        this.listaTiposBloques = listaTiposBloques;
    }

    public List<TiposBloques> getFiltradosListaTiposBloques() {
        return filtradosListaTiposBloques;
    }

    public void setFiltradosListaTiposBloques(List<TiposBloques> filtradosListaTiposBloques) {
        this.filtradosListaTiposBloques = filtradosListaTiposBloques;
    }

    public TiposBloques getEditarTiposBloques() {
        return editarTiposBloques;
    }

    public void setEditarTiposBloques(TiposBloques editarTiposBloques) {
        this.editarTiposBloques = editarTiposBloques;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    public void setAceptarEditar(boolean aceptarEditar) {
        this.aceptarEditar = aceptarEditar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public TiposBloques getNuevoTipoBloque() {
        return nuevoTipoBloque;
    }

    public void setNuevoTipoBloque(TiposBloques nuevoTipoBloque) {
        this.nuevoTipoBloque = nuevoTipoBloque;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public BigInteger getSecOperando() {
        return secOperando;
    }

    public void setSecOperando(BigInteger secOperando) {
        this.secOperando = secOperando;
    }

    public String gettOperando() {
        return tOperando;
    }

    public void settOperando(String tOperando) {
        this.tOperando = tOperando;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public TiposBloques getDuplicarTipoBloque() {
        return duplicarTipoBloque;
    }

    public void setDuplicarTipoBloque(TiposBloques duplicarTipoBloque) {
        this.duplicarTipoBloque = duplicarTipoBloque;
    }

    public boolean isNumericoB() {
        return numericoB;
    }

    public void setNumericoB(boolean numericoB) {
        this.numericoB = numericoB;
    }

    public boolean isFechaB() {
        return fechaB;
    }

    public void setFechaB(boolean fechaB) {
        this.fechaB = fechaB;
    }

    public boolean isCadenaB() {
        return cadenaB;
    }

    public void setCadenaB(boolean cadenaB) {
        this.cadenaB = cadenaB;
    }

    public boolean isNumericoBN() {
        return numericoBN;
    }

    public void setNumericoBN(boolean numericoBN) {
        this.numericoBN = numericoBN;
    }

    public boolean isFechaBN() {
        return fechaBN;
    }

    public void setFechaBN(boolean fechaBN) {
        this.fechaBN = fechaBN;
    }

    public boolean isCadenaBN() {
        return cadenaBN;
    }

    public void setCadenaBN(boolean cadenaBN) {
        this.cadenaBN = cadenaBN;
    }

    public boolean isNumericoBD() {
        return numericoBD;
    }

    public void setNumericoBD(boolean numericoBD) {
        this.numericoBD = numericoBD;
    }

    public boolean isFechaBD() {
        return fechaBD;
    }

    public void setFechaBD(boolean fechaBD) {
        this.fechaBD = fechaBD;
    }

    public boolean isCadenaBD() {
        return cadenaBD;
    }

    public void setCadenaBD(boolean cadenaBD) {
        this.cadenaBD = cadenaBD;
    }

    public String getEditorE() {
        return editorE;
    }

    public void setEditorE(String editorE) {
        this.editorE = editorE;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BigInteger getSecuenciaOperando() {
        return secuenciaOperando;
    }

    public void setSecuenciaOperando(BigInteger secuenciaOperando) {
        this.secuenciaOperando = secuenciaOperando;
    }

    public Operandos getOperandoSeleccionado() {
        return operandoRegistro;
    }

    public void setOperandoSeleccionado(Operandos operandoRegistro) {
        this.operandoRegistro = operandoRegistro;
    }

    public BigInteger getSecOperando2() {
        return secOperando2;
    }

    public void setSecOperando2(BigInteger secOperando2) {
        this.secOperando2 = secOperando2;
    }

    public Operandos getOperando2() {
        return operando2;
    }

    public void setOperando2(Operandos operando2) {
        this.operando2 = operando2;
    }

    public TiposBloques getTipoBloqueSeleccionado() {
        return tipoBloqueSeleccionado;
    }

    public void setTipoBloqueSeleccionado(TiposBloques tipoBloqueSeleccionado) {
        this.tipoBloqueSeleccionado = tipoBloqueSeleccionado;
    }

}
