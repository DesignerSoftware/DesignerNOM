/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosDemandasInterface;
import Entidades.MotivosDemandas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
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
public class ControlMotivosDemandas implements Serializable {

    @EJB
    AdministrarMotivosDemandasInterface administrarMotivosDemandas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosDemandas> listMotivosDemandas;
    private List<MotivosDemandas> filtrarMotivosDemandas;
    private List<MotivosDemandas> crearMotivoContratos;
    private List<MotivosDemandas> modificarMotivoContrato;
    private List<MotivosDemandas> borrarMotivoContrato;
    private MotivosDemandas nuevoMotivoContrato;
    private MotivosDemandas duplicarMotivoContrato;
    private MotivosDemandas editarMotivoContrato;
    private MotivosDemandas motivoDemandaSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex, activarLov;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion, paginaanterior;
    private BigInteger contarDemandasMotivoDemanda;
    private int tamano;

    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;

    public ControlMotivosDemandas() {

        listMotivosDemandas = null;
        crearMotivoContratos = new ArrayList<MotivosDemandas>();
        modificarMotivoContrato = new ArrayList<MotivosDemandas>();
        borrarMotivoContrato = new ArrayList<MotivosDemandas>();
        permitirIndex = true;
        editarMotivoContrato = new MotivosDemandas();
        nuevoMotivoContrato = new MotivosDemandas();
        duplicarMotivoContrato = new MotivosDemandas();
        guardado = true;
        tamano = 270;
        activarLov = true;
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
            administrarMotivosDemandas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaanterior = pagina;
        listMotivosDemandas = null;
        getListMotivosDemandas();
        if (listMotivosDemandas != null) {
            if (!listMotivosDemandas.isEmpty()) {
                motivoDemandaSeleccionado = listMotivosDemandas.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(MotivosDemandas motivo, int celda) {

        if (permitirIndex == true) {
            motivoDemandaSeleccionado = motivo;
            cualCelda = celda;
            if (cualCelda == 0) {
                motivoDemandaSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
                motivoDemandaSeleccionado.getDescripcion();
            }
            motivoDemandaSeleccionado.getSecuencia();

        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        motivoDemandaSeleccionado = null;
        k = 0;
        listMotivosDemandas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        motivoDemandaSeleccionado = null;
        k = 0;
        listMotivosDemandas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            bandera = 0;
            filtrarMotivosDemandas = null;
            tipoLista = 0;
        }
        RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
    }

    public void modificarMotivosContrato(MotivosDemandas motivo, String confirmarCambio, String valorConfirmar) {
        motivoDemandaSeleccionado = motivo;
        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR MOTIVOS CONTRATOS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMotivoContratos.contains(motivoDemandaSeleccionado)) {
                    if (motivoDemandaSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (motivoDemandaSeleccionado.getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            motivoDemandaSeleccionado.setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (motivoDemandaSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);

                    }
                    if (motivoDemandaSeleccionado.getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);

                    }

                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(motivoDemandaSeleccionado);
                        } else if (!modificarMotivoContrato.contains(motivoDemandaSeleccionado)) {
                            modificarMotivoContrato.add(motivoDemandaSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                } else {
                    if (motivoDemandaSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosDemandas.size(); j++) {
                            if (motivoDemandaSeleccionado.getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            motivoDemandaSeleccionado.setCodigo(backUpCodigo);

                        } else {
                            banderita = true;
                        }

                    }
                    if (motivoDemandaSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);

                    }
                    if (motivoDemandaSeleccionado.getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);

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
            } else if (!crearMotivoContratos.contains(motivoDemandaSeleccionado)) {
                if (motivoDemandaSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMotivosDemandas.size(); j++) {
                        if (motivoDemandaSeleccionado.getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        banderita = true;
                    }

                }

                if (motivoDemandaSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (motivoDemandaSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarMotivoContrato.isEmpty()) {
                        modificarMotivoContrato.add(motivoDemandaSeleccionado);
                    } else if (!modificarMotivoContrato.contains(motivoDemandaSeleccionado)) {
                        modificarMotivoContrato.add(motivoDemandaSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
            } else {
                if (motivoDemandaSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMotivosDemandas.size(); j++) {
                        if (motivoDemandaSeleccionado.getCodigo() == listMotivosDemandas.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        motivoDemandaSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        banderita = true;
                    }

                }

                if (motivoDemandaSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (motivoDemandaSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    motivoDemandaSeleccionado.setDescripcion(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrarMotivosDemandas() {

        if (motivoDemandaSeleccionado != null) {

            if (tipoLista == 0) {
                System.out.println("Entro a borrarMotivosDemandas");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(motivoDemandaSeleccionado)) {
                    int modIndex = modificarMotivoContrato.indexOf(motivoDemandaSeleccionado);
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(motivoDemandaSeleccionado);
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(motivoDemandaSeleccionado)) {
                    int crearIndex = crearMotivoContratos.indexOf(motivoDemandaSeleccionado);
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(motivoDemandaSeleccionado);
                }
                listMotivosDemandas.remove(motivoDemandaSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarMotivosDemandas.remove(motivoDemandaSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            contarRegistros();
            motivoDemandaSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void guardarMotivosDemandas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Motivos Contratos");
            if (!borrarMotivoContrato.isEmpty()) {
                System.out.println("Borrando...");
                administrarMotivosDemandas.borrarMotivosDemandas(borrarMotivoContrato);
                registrosBorrados = borrarMotivoContrato.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivoContrato.clear();
            }
            if (!crearMotivoContratos.isEmpty()) {
                administrarMotivosDemandas.crearMotivosDemandas(crearMotivoContratos);
            }
            crearMotivoContratos.clear();

            if (!modificarMotivoContrato.isEmpty()) {
                administrarMotivosDemandas.modificarMotivosDemandas(modificarMotivoContrato);
                modificarMotivoContrato.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosDemandas = null;
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;
        }
        motivoDemandaSeleccionado = null;

        RequestContext.getCurrentInstance()
                .update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (motivoDemandaSeleccionado != null) {
            if (tipoLista == 0) {
                editarMotivoContrato = motivoDemandaSeleccionado;
            }
            if (tipoLista == 1) {
                editarMotivoContrato = motivoDemandaSeleccionado;
            }

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
            }

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivoContrato() {
        System.out.println("Agregar Motivo Contrato");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoContrato.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoContrato.getCodigo());

            for (int x = 0; x < listMotivosDemandas.size(); x++) {
                if (listMotivosDemandas.get(x).getCodigo() == nuevoMotivoContrato.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoMotivoContrato.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivoContrato.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
                bandera = 0;
                filtrarMotivosDemandas = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoContrato.setSecuencia(l);

            crearMotivoContratos.add(nuevoMotivoContrato);

            listMotivosDemandas.add(nuevoMotivoContrato);
            motivoDemandaSeleccionado = nuevoMotivoContrato;
            nuevoMotivoContrato = new MotivosDemandas();

            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            System.out.println("Despues de la bandera guardado");

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoContratos').hide()");
            System.out.println("Despues de nuevoRegistroMotivoContratos");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoContratos() {
        System.out.println("limpiarnuevoMotivoContrato");
        nuevoMotivoContrato = new MotivosDemandas();
    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosDemandas() {
        System.out.println("duplicarMotivosCambiosCargos");
        if (motivoDemandaSeleccionado != null) {
            duplicarMotivoContrato = new MotivosDemandas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(motivoDemandaSeleccionado.getCodigo());
                duplicarMotivoContrato.setDescripcion(motivoDemandaSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(motivoDemandaSeleccionado.getCodigo());
                duplicarMotivoContrato.setDescripcion(motivoDemandaSeleccionado.getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivosCambiosCargos");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDemandas').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivoContrato.getCodigo());
        System.err.println("ConfirmarDuplicar nombre " + duplicarMotivoContrato.getDescripcion());

        if (duplicarMotivoContrato.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosDemandas.size(); x++) {
                if (listMotivosDemandas.get(x).getCodigo() == duplicarMotivoContrato.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoContrato.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivoContrato.getSecuencia() + "  " + duplicarMotivoContrato.getCodigo());
            if (crearMotivoContratos.contains(duplicarMotivoContrato)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosDemandas.add(duplicarMotivoContrato);
            crearMotivoContratos.add(duplicarMotivoContrato);
            RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
            contarRegistros();
            motivoDemandaSeleccionado = duplicarMotivoContrato;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoDemanda:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoDemanda");
                bandera = 0;
                filtrarMotivosDemandas = null;
                tipoLista = 0;
            }
            duplicarMotivoContrato = new MotivosDemandas();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDemandas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarduplicarMotivosDemandas() {
        duplicarMotivoContrato = new MotivosDemandas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoDemandaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDemandas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoDemandaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDemandas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (motivoDemandaSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(motivoDemandaSeleccionado.getSecuencia(), "MOTIVOSDEMANDAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSDEMANDAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

//-----------------------//---------------//----------------------//------------
    public List<MotivosDemandas> getListMotivosDemandas() {
        if (listMotivosDemandas == null) {
            listMotivosDemandas = administrarMotivosDemandas.consultarMotivosDemandas();
        }
        return listMotivosDemandas;
    }

    public void setListMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
        this.listMotivosDemandas = listMotivosDemandas;
    }

    public List<MotivosDemandas> getFiltrarMotivosDemandas() {
        return filtrarMotivosDemandas;
    }

    public void setFiltrarMotivosDemandas(List<MotivosDemandas> filtrarMotivosDemandas) {
        this.filtrarMotivosDemandas = filtrarMotivosDemandas;
    }

    public MotivosDemandas getNuevoMotivoContrato() {
        return nuevoMotivoContrato;
    }

    public void setNuevoMotivoContrato(MotivosDemandas nuevoMotivoContrato) {
        this.nuevoMotivoContrato = nuevoMotivoContrato;
    }

    public MotivosDemandas getEditarMotivoContrato() {
        return editarMotivoContrato;
    }

    public void setEditarMotivoContrato(MotivosDemandas editarMotivoContrato) {
        this.editarMotivoContrato = editarMotivoContrato;
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

    public MotivosDemandas getDuplicarMotivoContrato() {
        return duplicarMotivoContrato;
    }

    public void setDuplicarMotivoContrato(MotivosDemandas duplicarMotivoContrato) {
        this.duplicarMotivoContrato = duplicarMotivoContrato;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public MotivosDemandas getMotivoDemandaSeleccionado() {
        return motivoDemandaSeleccionado;
    }

    public void setMotivoDemandaSeleccionado(MotivosDemandas motivoDemandaSeleccionado) {
        this.motivoDemandaSeleccionado = motivoDemandaSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivoDemanda");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
