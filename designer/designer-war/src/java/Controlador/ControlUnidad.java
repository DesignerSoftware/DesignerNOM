/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.TiposUnidades;
import Entidades.Unidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUnidadesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlUnidad implements Serializable {

    @EJB
    AdministrarUnidadesInterface administrarUnidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Unidades> listaUnidades;
    private List<Unidades> filtradoListaUnidades;
    private Unidades unidadSeleccionada;
    //LISTA DE VALORES DE TIPOS UNIDADES FALTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<TiposUnidades> lovTiposUnidades;
    private List<TiposUnidades> lovFiltradoTiposUnidades;
    private TiposUnidades tiposUnidadSeleccionado;
    //Otros
    private boolean aceptar;
    private int index;
    private int tipoActualizacion;
    private boolean permitirIndex;
    private int tipoLista;
    private int cualCelda;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Modificar Unidades
    private List<Unidades> listaUnidadesModificar;
    private boolean guardado, guardarOk;
    //Crear Unidades
    public Unidades nuevaUnidad;
    private List<Unidades> listaUnidadesCrear;
    private BigInteger l;
    private int k;
    private String mensajeValidacion;
    //Borrar Unidad
    private List<Unidades> listaUnidadesBorrar;
    //AUTOCOMPLETAR
    private String tipoUnidad;
    //editar celda
    private Unidades editarUnidad;
    private boolean cambioEditor, aceptarEditar, activarLov;
    //DUPLICAR
    private Unidades duplicarUnidad;
    //RASTRO
    private BigInteger secRegistro;
    public String altoTabla;
    public String infoRegistroTiposUnidades;
    //
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    private Column unidadesCodigos, unidadesNombres, unidadesTipos;
    public String infoRegistro;
    ///////////////////////////////////////////////
    //////////////////PRUEBAS UNITARIAS COMPONENTES
    ///////////////////////////////////////////////
    public String buscarNombre;
    public boolean buscador;
    private BigInteger secuenciaPruebaConceptoEmpresa;
    private BigInteger secuenciaEmpleado;
    public String codiguin, descrecuperado;
    public String paginaAnterior;
    private Map<String, Object> mapParametros;

    public ControlUnidad() {
        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listaUnidadesBorrar = new ArrayList<Unidades>();
        listaUnidadesCrear = new ArrayList<Unidades>();
        listaUnidadesModificar = new ArrayList<Unidades>();
        lovTiposUnidades = new ArrayList<TiposUnidades>();
        //editar
        editarUnidad = new Unidades();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        nuevaUnidad = new Unidades();
        nuevaUnidad.setTipounidad(new TiposUnidades());
        secRegistro = null;
        k = 0;
        altoTabla = "270";
        guardado = true;
        buscador = false;
        tablaImprimir = ":formExportar:datosUnidadesExportar";
        nombreArchivo = "UnidadesXML";
        //     secuenciaPruebaConceptoEmpresa = new BigInteger("11197246");
        //secuenciaEmpleado = new BigInteger("11280578");
        secuenciaEmpleado = null;
        secuenciaPruebaConceptoEmpresa = null;
        activarLov = true;
        paginaAnterior = "nominaf";
        mapParametros = new LinkedHashMap<String, Object>();
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUnidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaUnidades = null;
        getListaUnidades();
        contarRegistros();
        deshabbilitarBotonLov();
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaUnidades = null;
        getListaUnidades();
        contarRegistros();
        deshabbilitarBotonLov();
    }

    public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "cargo";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
        // mapParametros.put("paginaAnterior", pagActual);
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

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("width: 85% !important;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("width: 85% !important;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("width: 85% !important;");
            altoTabla = "250";
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            bandera = 1;
            tipoLista = 1;
            System.out.println("TipoLista= " + tipoLista);
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            altoTabla = "270";
            bandera = 0;
            filtradoListaUnidades = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);

        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            tablaImprimir = ":formExportar:datosUnidadesExportar";
            nombreArchivo = "UnidadesXML";
            if (tipoLista == 0) {
                deshabbilitarBotonLov();
                secRegistro = listaUnidades.get(index).getSecuencia();
                descrecuperado = listaUnidades.get(index).getNombre();
                codiguin = listaUnidades.get(index).getCodigo();
                if (cualCelda == 2) {
                    habilitarBotonLov();
                    tipoUnidad = listaUnidades.get(index).getTipounidad().getNombre();
                }
            } else {
                secRegistro = filtradoListaUnidades.get(index).getSecuencia();
                descrecuperado = filtradoListaUnidades.get(index).getNombre();
                codiguin = filtradoListaUnidades.get(index).getCodigo();
                deshabbilitarBotonLov();
                if (cualCelda == 2) {
                    habilitarBotonLov();
                    tipoUnidad = filtradoListaUnidades.get(index).getTipounidad().getNombre();
                }
            }
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarUnidad = listaUnidades.get(index);
            }
            if (tipoLista == 1) {
                editarUnidad = filtradoListaUnidades.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                deshabbilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
                RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                deshabbilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombres");
                RequestContext.getCurrentInstance().execute("PF('editarNombres').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
                RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //AUTOCOMPLETAR
    public void modificarUnidades(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        int pasa = 0;
        int pasas = 0;
        int coincidencia = 0;

        if (confirmarCambio.equalsIgnoreCase("C")) {
            if (tipoLista == 0) {
                if (!listaUnidadesCrear.contains(listaUnidades.get(indice))) {
                    for (int i = 0; i < listaUnidades.size(); i++) {
                        if (listaUnidades.get(indice).getCodigo() != null) {
                            System.out.println("No es nulo el código");
                            System.out.println("codiguin 1 " + codiguin);
                            if (listaUnidades.get(indice).getCodigo().equals(listaUnidades.get(i).getCodigo())) {
                                System.out.println("coincidencia 2 " + coincidencia);
                                System.out.println("codiguin 3 " + codiguin);
                                pasas++;
                            }
                        }
                    }
                    if (pasas == 1) {
                        if (listaUnidadesModificar.isEmpty()) {
                            listaUnidadesModificar.add(listaUnidades.get(indice));
                        } else if (!listaUnidadesModificar.contains(listaUnidades.get(indice))) {
                            listaUnidadesModificar.add(listaUnidades.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    } else {
                        listaUnidades.get(indice).setCodigo(codiguin);
                        RequestContext.getCurrentInstance().update("formularioDialogos:existe");
                        RequestContext.getCurrentInstance().execute("PF('existe').show()");
                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaUnidadesCrear.contains(filtradoListaUnidades.get(indice))) {
                    for (int i = 0; i < listaUnidades.size(); i++) {
                        if (listaUnidades.get(indice).getCodigo() != null) {
                            System.out.println("No es nulo el código");
                            if (filtradoListaUnidades.get(indice).getCodigo().equals(filtradoListaUnidades.get(i).getCodigo())) {
                                System.out.println("Entro al IF");
                                pasas++;
                            }
                        }
                    }
                    if (pasas == 1) {
                        if (listaUnidadesModificar.isEmpty()) {
                            listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                        } else if (!listaUnidadesModificar.contains(filtradoListaUnidades.get(indice))) {
                            listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    } else {
                        filtradoListaUnidades.get(indice).setCodigo(codiguin);
                        RequestContext.getCurrentInstance().update("formularioDialogos:existe");
                        RequestContext.getCurrentInstance().execute("PF('existe').show()");
                    }
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosUnidades");
        } else if (confirmarCambio.equalsIgnoreCase("D")) {
            if (tipoLista == 0) {
                if (!listaUnidadesCrear.contains(listaUnidades.get(indice))) {
                    if (listaUnidades.get(indice).getNombre() == null || listaUnidades.get(indice).getNombre().equals("")) {
                        System.out.println("Entra 3");
                        pasa++;
                    }
                    if (pasa != 0) {
                        listaUnidades.get(indice).setNombre(descrecuperado);
                        RequestContext.getCurrentInstance().update("formularioDialogos:validacionNombre");
                        RequestContext.getCurrentInstance().execute("PF('validacionNombre').show()");
                    }
                    if (pasa == 0) {
                        if (listaUnidadesModificar.isEmpty()) {
                            listaUnidadesModificar.add(listaUnidades.get(indice));
                        } else if (!listaUnidadesModificar.contains(listaUnidades.get(indice))) {
                            listaUnidadesModificar.add(listaUnidades.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaUnidadesCrear.contains(filtradoListaUnidades.get(indice))) {
                    if (filtradoListaUnidades.get(indice).getNombre() == null || filtradoListaUnidades.get(indice).getNombre().equals("")) {
                        System.out.println("Entra 1");
                        pasa++;
                    }
                    if (pasa != 0) {
                        filtradoListaUnidades.get(indice).setNombre(descrecuperado);
                        RequestContext.getCurrentInstance().update("formularioDialogos:validacionNombre");
                        RequestContext.getCurrentInstance().execute("PF('validacionNombre').show()");
                    }
                    if (pasa == 0) {
                        if (listaUnidadesModificar.isEmpty()) {
                            listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                        } else if (!listaUnidadesModificar.contains(filtradoListaUnidades.get(indice))) {
                            listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                    }
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosUnidades");
        } else if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaUnidadesCrear.contains(listaUnidades.get(indice))) {

                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(listaUnidades.get(indice));
                    } else if (!listaUnidadesModificar.contains(listaUnidades.get(indice))) {
                        listaUnidadesModificar.add(listaUnidades.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;

            } else {
                if (!listaUnidadesCrear.contains(filtradoListaUnidades.get(indice))) {

                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                    } else if (!listaUnidadesModificar.contains(filtradoListaUnidades.get(indice))) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosUnidades");
        } else if (confirmarCambio.equalsIgnoreCase(
                "TIPOSUNIDADES")) {
            if (tipoLista == 0) {
                listaUnidades.get(indice).getTipounidad().setNombre(tipoUnidad);
            } else {
                filtradoListaUnidades.get(indice).getTipounidad().setNombre(tipoUnidad);
            }

            for (int i = 0; i < lovTiposUnidades.size(); i++) {
                if (lovTiposUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaUnidades.get(indice).setTipounidad(lovTiposUnidades.get(indiceUnicoElemento));
                } else {
                    filtradoListaUnidades.get(indice).setTipounidad(lovTiposUnidades.get(indiceUnicoElemento));
                }
                lovTiposUnidades.clear();
                getLovTiposUnidades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias
                == 1) {
            if (tipoLista == 0) {
                if (!listaUnidadesCrear.contains(listaUnidades.get(indice))) {
                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(listaUnidades.get(indice));
                    } else if (!listaUnidadesModificar.contains(listaUnidades.get(indice))) {
                        listaUnidadesModificar.add(listaUnidades.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaUnidadesCrear.contains(filtradoListaUnidades.get(indice))) {

                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
                    } else if (!listaUnidadesModificar.contains(filtradoListaUnidades.get(indice))) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(indice));
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

        RequestContext.getCurrentInstance().update("form:datosUnidades");
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {
        index = indice;
        deshabbilitarBotonLov();
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
            modificarInfoRegistroTUnidades(lovTiposUnidades.size());
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
        }
    }

    //Actualizar desde una lista de valores
    //METODOS L.O.V Tipos Unidades
    public void actualizarTiposUnidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaUnidades.get(index).setTipounidad(tiposUnidadSeleccionado);
                if (!listaUnidadesCrear.contains(listaUnidades.get(index))) {
                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(listaUnidades.get(index));
                    } else if (!listaUnidadesModificar.contains(listaUnidades.get(index))) {
                        listaUnidadesModificar.add(listaUnidades.get(index));
                    }
                }
            } else {
                filtradoListaUnidades.get(index).setTipounidad(tiposUnidadSeleccionado);
                if (!listaUnidadesCrear.contains(filtradoListaUnidades.get(index))) {
                    if (listaUnidadesModificar.isEmpty()) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(index));
                    } else if (!listaUnidadesModificar.contains(filtradoListaUnidades.get(index))) {
                        listaUnidadesModificar.add(filtradoListaUnidades.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosCiudades");
        } else if (tipoActualizacion == 1) {
            nuevaUnidad.setTipounidad(tiposUnidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoUnidad");
        } else if (tipoActualizacion == 2) {
            duplicarUnidad.setTipounidad(tiposUnidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoUnidad");
        }
        lovFiltradoTiposUnidades = null;
        tiposUnidadSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposUnidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposUnidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposUnidades");
    }

    public void cancelarCambioTiposUnidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        lovFiltradoTiposUnidades = null;
        tiposUnidadSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formularioDialogos:LOVTiposUnidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposUnidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').hide()");
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                habilitarBotonLov();
                modificarInfoRegistroTUnidades(lovTiposUnidades.size());
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUnidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UnidadesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUnidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UnidadesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO REGISTRO UNIDAD
    public void limpiarNuevaUnidad() {
        nuevaUnidad = new Unidades();
        nuevaUnidad.setTipounidad(new TiposUnidades());
        nuevaUnidad.getTipounidad().setNombre(" ");
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarDuplicarUnidad() {
        duplicarUnidad = new Unidades();
    }

    //VERIFICAR RASTRO
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaUnidades.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "UNIDADES");
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
        } else if (administrarRastros.verificarHistoricosTabla("UNIDADES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //REFRESCAR LA PAGINA, CANCELAR MODIFICACION SI NO SE A GUARDADO
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            altoTabla = "270";
            bandera = 0;
            filtradoListaUnidades = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);

        }

        listaUnidadesBorrar.clear();
        listaUnidadesCrear.clear();
        listaUnidadesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUnidades = null;
        getListaUnidades();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosUnidades");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            altoTabla = "270";
            bandera = 0;
            filtradoListaUnidades = null;
            tipoLista = 0;
            System.out.println("TipoLista= " + tipoLista);

        }

        listaUnidadesBorrar.clear();
        listaUnidadesCrear.clear();
        listaUnidadesModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaUnidades = null;
        getListaUnidades();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosUnidades");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        navegar("atras");
    }

    public void valoresBackupAutocompletar(int tipoNuevo) {
        if (tipoNuevo == 1) {
            tipoUnidad = nuevaUnidad.getTipounidad().getNombre();
        } else if (tipoNuevo == 2) {
            tipoUnidad = duplicarUnidad.getTipounidad().getNombre();
        }
    }

    public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevaUnidad.getTipounidad().setNombre(tipoUnidad);
        } else if (tipoNuevo == 2) {
            duplicarUnidad.getTipounidad().setNombre(tipoUnidad);
        }
        for (int i = 0; i < lovTiposUnidades.size(); i++) {
            if (lovTiposUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevaUnidad.setTipounidad(lovTiposUnidades.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoUnidad");
            } else if (tipoNuevo == 2) {
                duplicarUnidad.setTipounidad(lovTiposUnidades.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoUnidad");
            }
            lovTiposUnidades.clear();
            getLovTiposUnidades();
        } else {
            RequestContext.getCurrentInstance().update("form:tiposUnidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoUnidad");
            } else if (tipoNuevo == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoUnidad");
            }
        }
    }

    public void llamarLovTiposUnidades(int tipoN) {
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else if (tipoN == 2) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
        RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
    }

    //BORRAR UNIDADES
    public void borrarUnidades() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listaUnidadesModificar.isEmpty() && listaUnidadesModificar.contains(listaUnidades.get(index))) {
                    int modIndex = listaUnidadesModificar.indexOf(listaUnidades.get(index));
                    listaUnidadesModificar.remove(modIndex);
                    listaUnidadesBorrar.add(listaUnidades.get(index));
                } else if (!listaUnidadesCrear.isEmpty() && listaUnidadesCrear.contains(listaUnidades.get(index))) {
                    int crearIndex = listaUnidadesCrear.indexOf(listaUnidades.get(index));
                    listaUnidadesCrear.remove(crearIndex);
                } else {
                    listaUnidadesBorrar.add(listaUnidades.get(index));
                }
                listaUnidades.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaUnidadesModificar.isEmpty() && listaUnidadesModificar.contains(filtradoListaUnidades.get(index))) {
                    int modIndex = listaUnidadesModificar.indexOf(filtradoListaUnidades.get(index));
                    listaUnidadesModificar.remove(modIndex);
                    listaUnidadesBorrar.add(filtradoListaUnidades.get(index));
                } else if (!listaUnidadesCrear.isEmpty() && listaUnidadesCrear.contains(filtradoListaUnidades.get(index))) {
                    int crearIndex = listaUnidadesCrear.indexOf(filtradoListaUnidades.get(index));
                    listaUnidadesCrear.remove(crearIndex);
                } else {
                    listaUnidadesBorrar.add(filtradoListaUnidades.get(index));
                }
                int CIndex = listaUnidades.indexOf(filtradoListaUnidades.get(index));
                listaUnidades.remove(CIndex);
                filtradoListaUnidades.remove(index);
                modificarInfoRegistro(filtradoListaUnidades.size());
            }

            modificarInfoRegistro(listaUnidades.size());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //CREAR UNIDAD
    public void agregarNuevaUnidad() {
        int pasaA = 0;
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaUnidad.getNombre() == null || nuevaUnidad.getNombre().equals("")) {
            System.out.println("Entra 1");
            mensajeValidacion = mensajeValidacion + "   * Nombre \n";
            pasa++;
        }

        if (nuevaUnidad.getTipounidad().getNombre() == null) {
            System.out.println("Entra 2");
            mensajeValidacion = mensajeValidacion + "   * Tipo Unidad\n";
            pasa++;
        }

        for (int i = 0; i < listaUnidades.size(); i++) {
            if (nuevaUnidad.getCodigo() != null) {
                System.out.println("No es nulo el código");
                if (nuevaUnidad.getCodigo().equals(listaUnidades.get(i).getCodigo())) {
                    System.out.println("Entro al IF");
                    RequestContext.getCurrentInstance().update("formularioDialogos:existe");
                    RequestContext.getCurrentInstance().execute("PF('existe').show()");
                    pasaA++;
                }
            }
        }

        if (pasa == 0 && pasaA == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
                unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
                unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
                unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
                unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
                unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUnidades");
                altoTabla = "270";
                bandera = 0;
                filtradoListaUnidades = null;
                System.out.println("TipoLista= " + tipoLista);
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA CIUDADES.
            k++;
            l = BigInteger.valueOf(k);
            nuevaUnidad.setSecuencia(l);
            listaUnidadesCrear.add(nuevaUnidad);
            listaUnidades.add(nuevaUnidad);
            modificarInfoRegistro(listaUnidades.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevaUnidad = new Unidades();
            //  nuevaCiudad.setNombre(Departamento);
            nuevaUnidad.setTipounidad(new TiposUnidades());
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUnidad').hide()");
            index = -1;
            secRegistro = null;
        } else if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUnidad");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUnidad').show()");
        }

    }

    //DUPLICAR CIUDAD
    public void duplicarU() {
        if (index >= 0) {
            duplicarUnidad = new Unidades();

            if (tipoLista == 0) {
                duplicarUnidad.setCodigo(listaUnidades.get(index).getCodigo());
                duplicarUnidad.setNombre(listaUnidades.get(index).getNombre());
                duplicarUnidad.setTipounidad(listaUnidades.get(index).getTipounidad());
            }
            if (tipoLista == 1) {
                duplicarUnidad.setCodigo(filtradoListaUnidades.get(index).getCodigo());
                duplicarUnidad.setNombre(filtradoListaUnidades.get(index).getNombre());
                duplicarUnidad.setTipounidad(filtradoListaUnidades.get(index).getTipounidad());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUnidad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUnidad').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasaA = 0;
        int pasa = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUnidad.setSecuencia(l);
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarUnidad.getNombre() == null || duplicarUnidad.getNombre().equals("")) {
            System.out.println("Entra 1");
            mensajeValidacion = mensajeValidacion + "   * Nombre \n";
            pasa++;
        }

        if (duplicarUnidad.getTipounidad().getNombre() == null) {
            System.out.println("Entra 2");
            mensajeValidacion = mensajeValidacion + "   * Tipo Unidad\n";
            pasa++;
        }

        for (int i = 0; i < listaUnidades.size(); i++) {
            if (duplicarUnidad.getCodigo() != null) {
                System.out.println("No es nulo el código");
                if (duplicarUnidad.getCodigo().equals(listaUnidades.get(i).getCodigo())) {
                    System.out.println("Entro al IF");
                    RequestContext.getCurrentInstance().update("formularioDialogos:existe");
                    RequestContext.getCurrentInstance().execute("PF('existe').show()");
                    pasaA++;
                }
            }
        }

        if (pasa == 0 && pasaA == 0) {
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
                unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
                unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
                unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
                unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
                unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUnidades");
                altoTabla = "270";
                bandera = 0;
                filtradoListaUnidades = null;
                System.out.println("TipoLista= " + tipoLista);
                tipoLista = 0;
            }
            listaUnidades.add(duplicarUnidad);
            listaUnidadesCrear.add(duplicarUnidad);
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            duplicarUnidad = new Unidades();
            modificarInfoRegistro(listaUnidades.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroUnidad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUnidad').hide()");

        } else if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUnidad");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUnidad').show()");
        }

    }

    public void activarBuscador() {
        buscador = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:busquedaNombre");
        RequestContext.getCurrentInstance().execute("PF('busquedaNombre').show()");
    }

    //GUARDAR
    public void guardarCambiosUnidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                System.out.println("Realizando Operaciones Unidades");
                if (!listaUnidadesBorrar.isEmpty()) {
                    administrarUnidades.borrarUnidades(listaUnidadesBorrar);
                    System.out.println("Entra");
                    listaUnidadesBorrar.clear();
                }
                if (!listaUnidadesCrear.isEmpty()) {
                    administrarUnidades.crearUnidades(listaUnidadesCrear);
                    listaUnidadesCrear.clear();
                }
                if (!listaUnidadesModificar.isEmpty()) {
                    administrarUnidades.modificarUnidades(listaUnidadesModificar);
                    listaUnidadesModificar.clear();
                }
                System.out.println("Se guardaron los datos con exito");
                listaUnidades = null;
                getListaUnidades();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosUnidades");
                guardado = true;
                permitirIndex = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                index = -1;
                secRegistro = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabbilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //EVENTO FILTRAR
    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(filtradoListaUnidades.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listaUnidades != null) {
            modificarInfoRegistro(listaUnidades.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void modificarInfoRegistroTUnidades(int valor) {
        infoRegistroTiposUnidades = String.valueOf(valor);
    }

    public void eventoFiltrarTUnidades() {
        modificarInfoRegistroTUnidades(lovFiltradoTiposUnidades.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTiposUnidades");
    }

    //Getter & Setters
    public List<Unidades> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = administrarUnidades.consultarUnidades();
        }
        return listaUnidades;
    }

    public void setListaUnidades(List<Unidades> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public List<Unidades> getFiltradoListaUnidades() {
        return filtradoListaUnidades;
    }

    public void setFiltradoListaUnidades(List<Unidades> filtradoListaUnidades) {
        this.filtradoListaUnidades = filtradoListaUnidades;
    }

    public Unidades getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public List<TiposUnidades> getLovTiposUnidades() {
        lovTiposUnidades = administrarUnidades.consultarTiposUnidades();
        return lovTiposUnidades;
    }

    public void setLovTiposUnidades(List<TiposUnidades> lovTiposUnidades) {
        this.lovTiposUnidades = lovTiposUnidades;
    }

    public List<TiposUnidades> getLovFiltradoTiposUnidades() {
        return lovFiltradoTiposUnidades;
    }

    public void setLovFiltradoTiposUnidades(List<TiposUnidades> lovFiltradoTiposUnidades) {
        this.lovFiltradoTiposUnidades = lovFiltradoTiposUnidades;
    }

    public TiposUnidades getTiposUnidadSeleccionado() {
        return tiposUnidadSeleccionado;
    }

    public void setTiposUnidadSeleccionado(TiposUnidades tiposUnidadSeleccionado) {
        this.tiposUnidadSeleccionado = tiposUnidadSeleccionado;
    }

    public String getInfoRegistroTiposUnidades() {
        return infoRegistroTiposUnidades;
    }

    public void setInfoRegistroTiposUnidades(String infoRegistroTiposUnidades) {
        this.infoRegistroTiposUnidades = infoRegistroTiposUnidades;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Unidades getEditarUnidad() {
        return editarUnidad;
    }

    public void setEditarUnidad(Unidades editarUnidad) {
        this.editarUnidad = editarUnidad;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
/////////////////////////////////
////PRUEBAS UNITARIAS COMPONENTES
/////////////////////////////////    

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    public boolean isBuscador() {
        return buscador;
    }

    public void setBuscador(boolean buscador) {
        this.buscador = buscador;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Unidades getNuevaUnidad() {
        return nuevaUnidad;
    }

    public void setNuevaUnidad(Unidades nuevaUnidad) {
        this.nuevaUnidad = nuevaUnidad;
    }

    public Unidades getDuplicarUnidad() {
        return duplicarUnidad;
    }

    public void setDuplicarUnidad(Unidades duplicarUnidad) {
        this.duplicarUnidad = duplicarUnidad;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getSecuenciaPruebaConceptoEmpresa() {
        return secuenciaPruebaConceptoEmpresa;
    }

    public void setSecuenciaPruebaConceptoEmpresa(BigInteger secuenciaPruebaConceptoEmpresa) {
        this.secuenciaPruebaConceptoEmpresa = secuenciaPruebaConceptoEmpresa;
    }

    public BigInteger getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
        this.secuenciaEmpleado = secuenciaEmpleado;
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
