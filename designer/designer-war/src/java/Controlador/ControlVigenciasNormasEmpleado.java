/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Administrar.AdministrarVigenciaNormaLaboral;
import Entidades.Empleados;
import Entidades.NormasLaborales;
import Entidades.VigenciasNormasEmpleados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVigenciaNormaLaboralInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlVigenciasNormasEmpleado implements Serializable {

    /**
     * Creacion de los ejb
     */
    @EJB
    AdministrarVigenciaNormaLaboralInterface administrarVigenciaNormaLaboral;
    /**
     * CREACION ATRIBUTOS
     */
    private List<VigenciasNormasEmpleados> vigenciasNormasEmpleado;
    private List<VigenciasNormasEmpleados> filtrarVNE;
    private List<VigenciasNormasEmpleados> listVNECrear;
    private List<VigenciasNormasEmpleados> listVNEBorrar;
    private List<VigenciasNormasEmpleados> listVNEModificar;
    public VigenciasNormasEmpleados nuevaVigenciaNormasEmpleados;
    private VigenciasNormasEmpleados editarVNE;
    private VigenciasNormasEmpleados duplicarVNE;
    private List<NormasLaborales> listaNormasLaborales;
    private NormasLaborales normaLaboralSelecionada;
    private List<NormasLaborales> filtradoNormasLaborales;
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    private int tipoActualizacion;
    private int bandera;
    private Column vneFecha, vneNombreNormaLaboral;
    private boolean aceptar;
    private int index;
    private boolean guardado, guardarOk;
    private BigInteger l;
    private int k;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //PRUEBA
    private NormasLaborales normasLaboralesPrueba;
    private String banderaPruebas;
    private int div;
       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); 

    public ControlVigenciasNormasEmpleado() {
        vigenciasNormasEmpleado = null;
        listaNormasLaborales = new ArrayList<NormasLaborales>();
        empleado = new Empleados();
        normaLaboralSelecionada = new NormasLaborales();
        //Otros
        aceptar = true;
        //borrar aficiones
        listVNEBorrar = new ArrayList<VigenciasNormasEmpleados>();
        //crear aficiones
        listVNECrear = new ArrayList<VigenciasNormasEmpleados>();
        k = 0;
        //modificar aficiones
        listVNEModificar = new ArrayList<VigenciasNormasEmpleados>();
        //editar
        editarVNE = new VigenciasNormasEmpleados();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigenciaNormasEmpleados = new VigenciasNormasEmpleados();
        administrarVigenciaNormaLaboral = new AdministrarVigenciaNormaLaboral();
        secuenciaEmpleado = BigInteger.valueOf(10664356);
        normasLaboralesPrueba = new NormasLaborales();
        banderaPruebas = "visible";
           mapParametros.put ("paginaAnterior", paginaAnterior);
    }
    
   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciaNormaLaboral.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
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
         String pagActual = "vigencianormasempleado";
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

    
    public void recibirEmpleado(BigInteger sec) {
        vigenciasNormasEmpleado = null;
        secuenciaEmpleado = sec;
    }

    public void modificarVNE(int indice) {
////////////////////////////PRIMER DUDA/////////////////////////////////////////////////////////////////////////
        if (!listVNECrear.contains(vigenciasNormasEmpleado.get(indice))) {

            if (listVNEModificar.isEmpty()) {
                listVNEModificar.add(vigenciasNormasEmpleado.get(indice));
            } else if (!listVNEModificar.contains(vigenciasNormasEmpleado.get(indice))) {
                listVNEModificar.add(vigenciasNormasEmpleado.get(indice));
            }
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //Ubicacion Celda.
    public void cambiarIndice(int indice, int celda) {
        index = indice;
        normasLaboralesPrueba = vigenciasNormasEmpleado.get(index).getNormalaboral();

        cualCelda = celda;
        System.out.println(" ControlVigenciasNormasEmpleado CambiarIndice.......... Indice: " + index + " Celda: " + cualCelda);

        if (index % 2 == 0) {//par no muestra
            banderaPruebas = "hidden";
            RequestContext context1 = RequestContext.getCurrentInstance();
            context1.update("formExportar:botonPrueba2");

        } else {//impar si muestra
            banderaPruebas = "visible";
            RequestContext context1 = RequestContext.getCurrentInstance();
            context1.update("formExportar:botonPrueba2");
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formExportar:botonPrueba");
    }

    ///////////////////////////////////////////////////////////////////////////////
    public String getBanderaPruebas() {
        return banderaPruebas;
    }

    public void setBanderaPruebas(String banderaPruebas) {
        this.banderaPruebas = banderaPruebas;
    }

/////////////////////////////////////////////////////////////////////////////////    
    /**
     * GUARDAR
     */
    public void guardarCambiosVNE() {
        if (guardado == false) {
            System.out.println("ControlVigenciasNormasEmpleado(GuardarCambiosVNE)+");
            System.out.println("Realizando Operaciones Vigencias Tipos Contratos");
            if (!listVNEBorrar.isEmpty()) {
                for (int i = 0; i < listVNEBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    //  administrarVigenciaNormaLaboral.borrarVNE(listVNEBorrar.get(i));
                }
                listVNEBorrar.clear();
            }
            if (!listVNECrear.isEmpty()) {
                for (int i = 0; i < listVNECrear.size(); i++) {
                    System.out.println("Creando...");
                    //administrarVigenciaNormaLaboral.crearVNE(listVNECrear.get(i));
                }
                listVNECrear.clear();
            }
            if (!listVNEModificar.isEmpty()) {
                //    administrarVigenciaNormaLaboral.modificarVNE(listVNEModificar);
                listVNEModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            vigenciasNormasEmpleado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVNEEmpleado");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
        }
        index = -1;
    }

    /**
     * CANCELAR MODIFICACIONES
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("display: none; visibility: hidden;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 0;
            filtrarVNE = null;
            tipoLista = 0;
        }
        listVNEBorrar.clear();
        listVNECrear.clear();
        listVNEModificar.clear();
        index = -1;
        k = 0;
        vigenciasNormasEmpleado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarVNE = vigenciasNormasEmpleado.get(index);
            }
            if (tipoLista == 1) {
                editarVNE = filtrarVNE.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNormaLaboral");
                RequestContext.getCurrentInstance().execute("PF('editarNormaLaboral').show()");
                cualCelda = -1;
            }
        }
        index = -1;
    }

    //CREAR VU
    public void agregarNuevaVNE() {
        if (bandera == 1) {
            System.err.println("ControlVigenciasNormas AgregarNuevaVNE");
            //CERRAR FILTRADO
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("display: none; visibility: hidden;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 0;
            filtrarVNE = null;
            tipoLista = 0;
        }
        //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
        k++;
        l = BigInteger.valueOf(k);
        nuevaVigenciaNormasEmpleados.setSecuencia(l);
        nuevaVigenciaNormasEmpleados.setEmpleado(empleado);
        listVNECrear.add(nuevaVigenciaNormasEmpleados);
        vigenciasNormasEmpleado.add(nuevaVigenciaNormasEmpleados);
        nuevaVigenciaNormasEmpleados = new VigenciasNormasEmpleados();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        index = -1;
    }
    //LIMPIAR NUEVO REGISTRO

    public void limpiarNuevaVNE() {
        nuevaVigenciaNormasEmpleados = new VigenciasNormasEmpleados();
        index = -1;
    }

    //DUPLICAR VC
    public void duplicarVigenciaVNE() {
        if (index >= 0) {
            duplicarVNE = new VigenciasNormasEmpleados();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVNE.setSecuencia(l);
                duplicarVNE.setEmpleado(vigenciasNormasEmpleado.get(index).getEmpleado());
                duplicarVNE.setFechavigencia(vigenciasNormasEmpleado.get(index).getFechavigencia());
                duplicarVNE.setNormalaboral(vigenciasNormasEmpleado.get(index).getNormalaboral());

            }
            if (tipoLista == 1) {
                duplicarVNE.setSecuencia(l);
                duplicarVNE.setEmpleado(filtrarVNE.get(index).getEmpleado());
                duplicarVNE.setFechavigencia(filtrarVNE.get(index).getFechavigencia());
                duplicarVNE.setNormalaboral(filtrarVNE.get(index).getNormalaboral());
            }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVNE");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVNE').show()");
            index = -1;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////            
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void confirmarDuplicar() {

        vigenciasNormasEmpleado.add(duplicarVNE);
        listVNECrear.add(duplicarVNE);
        RequestContext context = RequestContext.getCurrentInstance();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
        index = -1;
        if (guardado == true) {
            guardado = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            //CERRAR FILTRADO/////////////////////////////////////////////////////////////////////////////////////////////////////////
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("display: none; visibility: hidden;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 0;
            filtrarVNE = null;
            tipoLista = 0;
        }
        duplicarVNE = new VigenciasNormasEmpleados();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVNE() {
        duplicarVNE = new VigenciasNormasEmpleados();
    }
    /////////////////////////////////////////////////////////////////

    //BORRAR VC
    public void borrarVNE() {

        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listVNEModificar.isEmpty() && listVNEModificar.contains(vigenciasNormasEmpleado.get(index))) {
                    int modIndex = listVNEModificar.indexOf(vigenciasNormasEmpleado.get(index));
                    listVNEModificar.remove(modIndex);
                    listVNEBorrar.add(vigenciasNormasEmpleado.get(index));
                } else if (!listVNECrear.isEmpty() && listVNECrear.contains(vigenciasNormasEmpleado.get(index))) {
                    System.out.println("Entro xD");
                    int crearIndex = listVNECrear.indexOf(vigenciasNormasEmpleado.get(index));
                    listVNECrear.remove(crearIndex);
                } else {
                    listVNEBorrar.add(vigenciasNormasEmpleado.get(index));
                }
                vigenciasNormasEmpleado.remove(index);
            }
            if (tipoLista == 1) {
                if (!listVNEModificar.isEmpty() && listVNEModificar.contains(filtrarVNE.get(index))) {
                    int modIndex = listVNEModificar.indexOf(filtrarVNE.get(index));
                    listVNEModificar.remove(modIndex);
                    listVNEBorrar.add(filtrarVNE.get(index));
                } else if (!listVNECrear.isEmpty() && listVNECrear.contains(filtrarVNE.get(index))) {
                    int crearIndex = listVNECrear.indexOf(filtrarVNE.get(index));
                    listVNECrear.remove(crearIndex);
                } else {
                    listVNEBorrar.add(filtrarVNE.get(index));
                }
                int VNEIndex = vigenciasNormasEmpleado.indexOf(filtrarVNE.get(index));
                vigenciasNormasEmpleado.remove(VNEIndex);
                filtrarVNE.remove(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            index = -1;

            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void activarCtrlF11() {
        if (bandera == 0) {
            System.out.println("Activar");
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("width: 85% !important;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("display: none; visibility: hidden;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 0;
            filtrarVNE = null;
            tipoLista = 0;
        }
    }

    //SALIR
    public void salir() {
        if (bandera == 1) {
            vneFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneFecha");
            vneFecha.setFilterStyle("display: none; visibility: hidden;");
            vneNombreNormaLaboral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVNEmpleados:vneNombreNormaLaboral");
            vneNombreNormaLaboral.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVNEmpleados");
            bandera = 0;
            filtrarVNE = null;
            tipoLista = 0;
        }

        listVNEBorrar.clear();
        listVNECrear.clear();
        listVNEModificar.clear();
        index = -1;
        k = 0;
        vigenciasNormasEmpleado = null;
        guardado = true;

    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    public void asignarIndex(Integer indice, int LND) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
        RequestContext.getCurrentInstance().update("form:NormasLaboralesDialogo");
        RequestContext.getCurrentInstance().execute("PF('NormasLaboralesDialogo').show()");
    }

    //LOVS
    //CIUDAD
    public void actualizarNormaLaboral() {
        if (tipoActualizacion == 0) {
            vigenciasNormasEmpleado.get(index).setNormalaboral(normaLaboralSelecionada);

            if (!listVNECrear.contains(vigenciasNormasEmpleado.get(index))) {
                if (listVNEModificar.isEmpty()) {
                    listVNEModificar.add(vigenciasNormasEmpleado.get(index));
                } else if (!listVNEModificar.contains(vigenciasNormasEmpleado.get(index))) {
                    listVNEModificar.add(vigenciasNormasEmpleado.get(index));
                }
                if (guardado == true) {
                    guardado = false;
                    //RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNormasEmpleados.setNormalaboral(normaLaboralSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVNE");
        } else if (tipoActualizacion == 2) {
            duplicarVNE.setNormalaboral(normaLaboralSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVNE");
        }
        filtradoNormasLaborales = null;
        normaLaboralSelecionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
    }

    public void cancelarCambioNormaLaboral() {
        filtradoNormasLaborales = null;
        normaLaboralSelecionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
    }

    //LISTA DE VALORES DINAMICA
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("form:NombreTerceroDialogo");
                RequestContext.getCurrentInstance().execute("PF('NombreTerceroDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("form:EstadosAfilacionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('EstadosAfilacionesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVNEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasNormasEmpleadosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void exportXLS() throws IOException {
        try {
            System.out.println("En treControlVigenciasNormasEmpleado.exportXLS");
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVNEmpleadoExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasNormasEmpleadosfXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    //EVENTO FILTRAR

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    /**
     * GETTERS AND SETTERS
     */
    public List<VigenciasNormasEmpleados> getVigenciasNormasEmpleados() {
        try {
            if (vigenciasNormasEmpleado == null) {
                //    return vigenciasNormasEmpleado = administrarVigenciaNormaLaboral.vigenciasNormasEmpleadosEmpl(secuenciaEmpleado);
                return null;
            } else {
                return vigenciasNormasEmpleado;
            }

        } catch (Exception e) {
            System.out.println("Error...!! getVigenciasUbicacionsEmpleado ");
            return null;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setVigenciasNormasEmpleados(List<VigenciasNormasEmpleados> vigenciasNormasEmpleados) {
        this.vigenciasNormasEmpleado = vigenciasNormasEmpleados;
    }

    public Empleados getEmpleado() {
        empleado = administrarVigenciaNormaLaboral.consultarEmpleado(secuenciaEmpleado);
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<NormasLaborales> getListaNormasLaborales() {
//        listaNormasLaborales = administrarVigenciaNormaLaboral.normasLaborales();
        return listaNormasLaborales;
    }

    public void setListaNormasLaborales(List<NormasLaborales> listaNormasLaborales) {
        this.listaNormasLaborales = listaNormasLaborales;
    }

    public NormasLaborales getNormaLaboralSelecionada() {
        return normaLaboralSelecionada;
    }

    public void setNormaLaboralSelecionada(NormasLaborales NormaLaboralSelecionada) {
        this.normaLaboralSelecionada = NormaLaboralSelecionada;
    }

    public List<NormasLaborales> getFiltradoNormasLaborales() {
        return filtradoNormasLaborales;
    }

    public void setFiltradoNormasLaborales(List<NormasLaborales> filtradoNormasLaborales) {
        this.filtradoNormasLaborales = filtradoNormasLaborales;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public VigenciasNormasEmpleados getNuevaVigenciaNormasEmpleados() {
        return nuevaVigenciaNormasEmpleados;
    }

    public void setNuevaVigenciaNormasEmpleados(VigenciasNormasEmpleados nuevaVigenciaNormasEmpleados) {
        this.nuevaVigenciaNormasEmpleados = nuevaVigenciaNormasEmpleados;
    }

    public VigenciasNormasEmpleados getDuplicarVNE() {
        return duplicarVNE;
    }

    public void setDuplicarVNE(VigenciasNormasEmpleados duplicarVNE) {
        this.duplicarVNE = duplicarVNE;
    }

    public VigenciasNormasEmpleados getEditarVNE() {
        return editarVNE;
    }

    public void setEditarVNE(VigenciasNormasEmpleados editarVNE) {
        this.editarVNE = editarVNE;
    }

    public List<VigenciasNormasEmpleados> getFiltrarVNE() {
        return filtrarVNE;
    }

    public void setFiltrarVNE(List<VigenciasNormasEmpleados> filtrarVNE) {
        this.filtrarVNE = filtrarVNE;
    }

    public NormasLaborales getNormasLaboralesPrueba() {
        return normasLaboralesPrueba;
    }

    public void setNormasLaboralesPrueba(NormasLaborales normasLaboralesPrueba) {
        this.normasLaboralesPrueba = normasLaboralesPrueba;
    }
}
