package Controlador;

import Administrar.AdministrarCarpetaDesigner;
import Entidades.Aficiones;
import Entidades.Empleados;
import Entidades.VigenciasCargos;
import InterfaceAdministrar.AdministrarCarpetaDesignerInterface;
import InterfaceAdministrar.AdministrarCarpetaPersonalInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author -Felipphe-
 */
@ManagedBean
@SessionScoped
public class CtrlVigenciasCargos implements Serializable {

    @EJB
    AdministrarCarpetaPersonalInterface administrarCarpetaPersonal;
    @EJB
    AdministrarCarpetaDesignerInterface administrarCarpetaDesigner;
    private List<VigenciasCargos> vigenciasCargosEmpleados;
    private List<VigenciasCargos> filtradoVigenciasCargosEmpleados;
    private VigenciasCargos vigenciaSeleccionada;
    private BigInteger secuencia;
    private Empleados empleado;
    private int bandera;
    //Id's Tabla
    private Column vcFecha, vcEstructura, vcMotivo, vcNombreCargo, vcCentrosC, vcNombreJefe;
    //Panel
    Panel panelNuevo;
    //Pruebas para modificar
    private List<Aficiones> listAficiones;
    private List<Aficiones> listAficionesModificar;
    private Aficiones seleccionAficion, afi;
    private String a;
    private boolean guardado, guardarOk;
    //crear aficion
    private Integer max;
    private String sugerencia;
    private List<Aficiones> listAficionesCrear;
    private BigInteger l;
    private int k;
    //borrar aficion 
    private int index;
    private List<Aficiones> listAficionesBorrar;
    //editar celda
    private Aficiones editarAficion;
    private int cualCelda;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private Aficiones duplicarAficion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public CtrlVigenciasCargos() {
        System.out.println("Se creo un nuevo BakingBean de Vigencias Cargos");
        bandera = 0;
        guardado = true;
        guardarOk = false;
        afi = new Aficiones();
        administrarCarpetaDesigner = new AdministrarCarpetaDesigner();
        listAficiones = null;
        //borrar aficiones
        listAficionesBorrar = new ArrayList();
        //crear aficiones
        listAficionesCrear = new ArrayList();
        k = 0;
        //modificar aficiones
        listAficionesModificar = new ArrayList();
        //editar
        editarAficion = new Aficiones();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarCarpetaPersonal.obtenerConexion(ses.getId());
            administrarCarpetaDesigner.obtenerConexion(ses.getId());
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
         System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "vigenciascargos";
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
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
        }
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void paila(BigInteger sec) {
        secuencia = sec;
        System.out.println(sec);
    }

    public void activar() {
        if (bandera == 0) {
            System.out.println("Activar");
            vcFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcFecha");
            vcFecha.setFilterStyle("");
            vcEstructura = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcEstructura");
            vcEstructura.setFilterStyle("");
            vcMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcMotivo");
            vcMotivo.setFilterStyle("");
            vcNombreCargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcNombreCargo");
            vcNombreCargo.setFilterStyle("");
            vcNombreJefe = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcNombreJefe");
            vcNombreJefe.setFilterStyle("");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            vcFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcFecha");
            vcFecha.setFilterStyle("display: none; visibility: hidden;");
            vcEstructura = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcEstructura");
            vcEstructura.setFilterStyle("display: none; visibility: hidden;");
            vcMotivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcMotivo");
            vcMotivo.setFilterStyle("display: none; visibility: hidden;");
            vcNombreCargo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcNombreCargo");
            vcNombreCargo.setFilterStyle("display: none; visibility: hidden;");
            vcNombreJefe = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVCEmpleado:vcNombreJefe");
            vcNombreJefe.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            bandera = 0;
        }
    }

    public Empleados getEmpleado() {
        try {
            empleado = administrarCarpetaPersonal.consultarEmpleado(secuencia);
        } catch (Exception e) {
            System.out.println("Upsss");
        }
        return empleado;
    }

    public List<VigenciasCargos> getVigenciasCargosEmpleados() {
        try {
            //BigInteger a = BigInteger.valueOf(10661039);
            vigenciasCargosEmpleados = administrarCarpetaPersonal.consultarVigenciasCargosEmpleado(secuencia);
        } catch (Exception e) {
            System.out.println("Tambien Upsss");
        }
        return vigenciasCargosEmpleados;
    }

    public void modificarVC() {
        //System.out.println(vigenciasCargosEmpleados.get(0).getEstructura().getNombre());
        System.out.println(vigenciaSeleccionada.getEstructura().getNombre());
        //administrarCarpetaPersonal.editarVigenciasCargos(vigenciasCargosEmpleados.get(0));
    }

    public VigenciasCargos getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasCargos vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public List<VigenciasCargos> getFiltradoVigenciasCargosEmpleados() {
        return filtradoVigenciasCargosEmpleados;
    }

    public void setFiltradoVigenciasCargosEmpleados(List<VigenciasCargos> filtradoVigenciasCargosEmpleados) {
        this.filtradoVigenciasCargosEmpleados = filtradoVigenciasCargosEmpleados;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public void setVigenciasCargosEmpleados(List<VigenciasCargos> vigenciasCargosEmpleados) {
        this.vigenciasCargosEmpleados = vigenciasCargosEmpleados;
    }

    public Aficiones getEditarAficion() {
        return editarAficion;
    }

    public void setEditarAficion(Aficiones editarAficion) {
        this.editarAficion = editarAficion;
    }

    public List<Aficiones> getListAficiones() {
        if (listAficiones == null) {
//            listAficiones = administrarCarpetaDesigner.buscarAficiones();
            listAficiones = administrarCarpetaDesigner.consultarAficiones();
            return listAficiones;
        } else {
            return listAficiones;
        }
    }

    public void setListAficiones(List<Aficiones> listAficiones) {
        this.listAficiones = listAficiones;
    }

    public Aficiones getSeleccionAficion() {
        return seleccionAficion;
    }

    public void setSeleccionAficion(Aficiones seleccionAficion) {
        this.seleccionAficion = seleccionAficion;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Aficiones getAfi() {
        //afi = administrarCarpetaDesigner.buscarAfi(BigDecimal.valueOf(10));
        return afi;
    }

    public void setAfi(Aficiones afi) {
        this.afi = afi;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public boolean isAceptarEditar() {
        return aceptarEditar;
    }

    /*public void ver24(CellEditEvent event) {
     String oldValue = event.getOldValue().toString();
     String newValue = event.getNewValue().toString();

     if (newValue != null && !newValue.equals(oldValue)) {
     System.out.println("Old:" + oldValue);
     System.out.println("New:" + newValue);
     guardado = false;
     RequestContext.getCurrentInstance().update("form:ACEPTAR");
     }
     if (guardarOk == true) {
     modificarAficiones();
     guardarOk = false;
     }
     }*/
    public void modificarAficiones() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones");
            if (!listAficionesBorrar.isEmpty()) {
                for (int i = 0; i < listAficionesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    administrarCarpetaDesigner.borrarAficion(listAficionesBorrar.get(i));
                }
                listAficionesBorrar.clear();
            }
            if (!listAficionesCrear.isEmpty()) {
                for (int i = 0; i < listAficionesCrear.size(); i++) {
                    System.out.println("Creando...");
                    administrarCarpetaDesigner.crearAficion(listAficionesCrear.get(i));
                }
                listAficionesCrear.clear();
            }
            if (!listAficionesModificar.isEmpty()) {
                administrarCarpetaDesigner.modificarAficiones(listAficionesModificar);
                listAficionesModificar.clear();
            }
            listAficiones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aficiones");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
        }
    }

    public void borrarAficion() {

        if (index >= 0) {
            if (!listAficionesModificar.isEmpty() && listAficionesModificar.contains(listAficiones.get(index))) {
                int modIndex = listAficionesModificar.indexOf(listAficiones.get(index));
                listAficionesModificar.remove(modIndex);
                listAficionesBorrar.add(listAficiones.get(index));
            } else if (!listAficionesCrear.isEmpty() && listAficionesCrear.contains(listAficiones.get(index))) {
                int crearIndex = listAficionesCrear.indexOf(listAficiones.get(index));
                System.out.println("Indice de crear que va a ser eliminado: " + crearIndex);
                System.out.println("Codigo de quien se eliminara:  " + listAficiones.get(index).getCodigo() + " Descripcion:  " + listAficiones.get(index).getDescripcion());
                listAficionesCrear.remove(crearIndex);
            } else {
                listAficionesBorrar.add(listAficiones.get(index));
            }
            listAficiones.remove(index);
            for (int i = 0; i < listAficionesBorrar.size(); i++) {
                System.out.println("Descripcion del que se va a matar: " + listAficionesBorrar.get(i).getDescripcion());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aficiones");
            index = -1;
            //guardado = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }

    }

    public void cambiarIndice(int indice, int celda) {
        index = indice;
        cualCelda = celda;
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void cancelarModAficion() {
        listAficionesBorrar.clear();
        listAficionesCrear.clear();
        listAficionesModificar.clear();
        listAficiones = null;
        index = -1;
        k = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:aficiones");
        panelNuevo = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNuevo");
        panelNuevo.setStyle("border: none; display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:panelNuevo");
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void habilitarAgregar() {
        //max = administrarCarpetaDesigner.sugerenciaCodigoAficiones();
        short newTwo = 0;
        for (int i = 0; i < listAficiones.size(); i++) {
            if (listAficiones.get(i).getCodigo() > newTwo) {
                newTwo = listAficiones.get(i).getCodigo();
            }
        }
        //sugerencia = max.toString();

        afi.setCodigo((short) (newTwo + 1));

        panelNuevo = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNuevo");
        panelNuevo.setStyle("border: none;");
        RequestContext.getCurrentInstance().update("form:panelNuevo");
    }

    public void desabilitarAgregar() {
        panelNuevo = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNuevo");
        panelNuevo.setStyle("border: none; display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:panelNuevo");
    }

    public void activarGuardado(int indice) {
        if (!listAficionesCrear.contains(listAficiones.get(indice))) {

            if (listAficionesModificar.isEmpty()) {
                listAficionesModificar.add(listAficiones.get(indice));
            } else if (!listAficionesModificar.contains(listAficiones.get(indice))) {
                listAficionesModificar.add(listAficiones.get(indice));
            }
            for (int i = 0; i < listAficionesModificar.size(); i++) {
                System.out.println("Las que se modificaron: " + listAficionesModificar.get(i).getDescripcion());
            }
        }
        for (int i = 0; i < listAficionesCrear.size(); i++) {
            System.out.println("Las que se Crearon: " + listAficionesCrear.get(i).getDescripcion());
        }

        //guardado = false;
        //RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    /*   public void validate(FacesContext arg0, InputText arg1, Object arg2) throws ValidatorException {
     System.out.println("Mas de 1!!");
     if (((Integer) arg2).toString().length() < 1) {
     System.out.println("Mas de 1!!");
     }
     }*/
    public void validarCodigo(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        InputText in = (InputText) component;

        try {
            if (((Short) value).toString().length() > 4) {
                System.out.println("Alerta!!");
                in.setStyle("width: 89.68%; border-radius: 0px; border-color: blue;");

            } else if (((Short) value).toString().length() <= 4 && ((Short) value).toString().length() > 0) {
                in.setStyle("width: 89.68%; border-radius: 0px;");
            } else if (((Short) value) == 0) {
                in.setStyle("border-color: blue;");
            }
        } catch (Exception e) {
            System.out.println("jaajaj");
        }

    }

    public void squirtle() {
        desabilitarAgregar();
        k++;
        l = BigInteger.valueOf(k);
        afi.setSecuencia(l);
        listAficionesCrear.add(afi);
        for (int i = 0; i < listAficionesCrear.size(); i++) {
            System.out.println("Las que se Crearon: " + listAficionesCrear.get(i).getDescripcion());
        }
        listAficiones.add(afi);
        afi = new Aficiones();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:aficiones");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void duplicarAficion() {
        if (index >= 0) {
            duplicarAficion = new Aficiones();
            k++;
            l = BigInteger.valueOf(k);
            duplicarAficion.setSecuencia(l);
            duplicarAficion.setCodigo(listAficiones.get(index).getCodigo());
            duplicarAficion.setDescripcion(listAficiones.get(index).getDescripcion());

            System.out.println("Datos del duplicado: " + duplicarAficion.getSecuencia() + "  " + duplicarAficion.getCodigo() + "  " + duplicarAficion.getDescripcion());
            if (listAficionesCrear.contains(duplicarAficion)) {
                System.out.println("Ya lo contengo.");
            }

            listAficiones.add(duplicarAficion);
            listAficionesCrear.add(duplicarAficion);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aficiones");
            index = -1;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void editarCelda() {
        if (index >= 0) {
            editarAficion = listAficiones.get(index);
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("form:editarCodigo");
                System.out.println("Dialogo de Codigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("form:editarDescripcion");
                System.out.println("Dialogo de Descripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            }
        }
    }

    public void cambioEditable() {
        cambioEditor = true;
        System.out.println("Estado del cambio : " + cambioEditor);
    }

    public void aplicarCambioCelda() {
        if (index >= 0) {
            if (cambioEditor == true) {
                if (!listAficionesCrear.contains(editarAficion)) {

                    if (listAficionesModificar.isEmpty()) {
                        listAficionesModificar.add(editarAficion);
                        listAficiones.set(index, editarAficion);
                    } else if (listAficionesModificar.contains(editarAficion)) {
                        int editarIndex = listAficionesModificar.indexOf(editarAficion);
                        listAficionesModificar.set(editarIndex, editarAficion);
                        listAficiones.set(index, editarAficion);

                    } else if (!listAficionesModificar.contains(editarAficion)) {
                        listAficionesModificar.add(editarAficion);
                        listAficiones.set(index, editarAficion);
                    }
                    for (int i = 0; i < listAficionesModificar.size(); i++) {
                        System.out.println("Las que se modificaron: " + listAficionesModificar.get(i).getDescripcion());
                    }
                } else if (listAficionesCrear.contains(editarAficion)) {
                    int editarIndex = listAficionesCrear.indexOf(editarAficion);
                    listAficionesCrear.set(editarIndex, editarAficion);
                    listAficiones.set(index, editarAficion);
                }
            }

            for (int i = 0; i < listAficionesCrear.size(); i++) {
                System.out.println("Las que se Crearon: " + listAficionesCrear.get(i).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aficiones");
            editarAficion = new Aficiones();
            cambioEditor = false;
            index = -1;
            aceptarEditar = true;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void reiniciarEditar() {
        editarAficion = new Aficiones();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
    }

    public void verProceso() {
        for (int i = 0; i < listAficionesModificar.size(); i++) {
            System.out.println("Las que se Modificaron:         " + " Codigo:   " + listAficionesModificar.get(i).getCodigo() + "   Descripcion:  " + listAficionesModificar.get(i).getDescripcion());
        }
        System.out.println(".................................................");
        for (int i = 0; i < listAficionesCrear.size(); i++) {
            System.out.println("Las que se van a Crear:         " + " Codigo:   " + listAficionesCrear.get(i).getCodigo() + "   Descripcion:  " + listAficionesCrear.get(i).getDescripcion() + "   Secuencia: " + listAficionesCrear.get(i).getSecuencia());
        }
        System.out.println(".................................................");
        for (int i = 0; i < listAficionesBorrar.size(); i++) {
            System.out.println("Las que van a ser Eliminadas:   " + " Codigo:   " + listAficionesBorrar.get(i).getCodigo() + " Descripcion:  " + listAficionesBorrar.get(i).getDescripcion());
        }
        System.out.println(".................................................");
        for (int i = 0; i < listAficiones.size(); i++) {
            System.out.println("lista total:         " + " Codigo:   " + listAficiones.get(i).getCodigo() + "   Descripcion:  " + listAficiones.get(i).getDescripcion() + "   Secuencia: " + listAficiones.get(i).getSecuencia());
        }
        System.out.println("................................................. FIN.");
    }

    public void activarAceptar(int dialog) {
        aceptarEditar = false;
        if (dialog == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aceptarCod");
        } else if (dialog == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:aceptarDesc");
        }

    }

    public void activarAceptarGlobal() {
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

}
