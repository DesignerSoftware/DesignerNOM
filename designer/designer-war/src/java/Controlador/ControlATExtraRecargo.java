package Controlador;


import Entidades.Conceptos;
import Entidades.Contratos;
import Entidades.DetallesExtrasRecargos;
import Entidades.Empresas;
import Entidades.ExtrasRecargos;
import Entidades.TiposDias;
import Entidades.TiposJornadas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarATExtraRecargoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlATExtraRecargo implements Serializable {

    @EJB
    AdministrarATExtraRecargoInterface administrarATExtraRecargo;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<ExtrasRecargos> listExtrasRecargos;
    private List<ExtrasRecargos> filtrarListExtrasRecargos;
    private ExtrasRecargos extraRecargoTablaSeleccionado;
    ///////
    private List<DetallesExtrasRecargos> listDetallesExtrasRecargos;
    private List<DetallesExtrasRecargos> filtrarListDetallesExtrasRecargos;
    private DetallesExtrasRecargos detalleTablaSeleccionado;
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaDER;

    private Column extraCodigo, extraDescripcion, extraTipoDia, extraTipoJornada, extraTipoLegislacion, extraTurno, extraAprobacion;
    private Column detalleHI, detalleHF, detalleConcepto, detalleEmpresa, detalleDia, detalleAdicion, detalleMinutos, detallePagoAdicion, detalleIniFin, detalleGarantiza;
    //Otros
    private boolean aceptar;
    private int index, indexDER, indexAux;
    //modificar
    private List<ExtrasRecargos> listExtraRecargoModificar;
    private List<DetallesExtrasRecargos> listDetalleExtraRecargoModificar;
    private boolean guardado, guardadoDER;
    //crear 
    private ExtrasRecargos nuevoExtraRecargo;
    private DetallesExtrasRecargos nuevoDetalleExtraRecargo;
    private List<ExtrasRecargos> listExtraRecargoCrear;
    private List<DetallesExtrasRecargos> listDetalleExtraRecargoCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<ExtrasRecargos> listExtraRecargoBorrar;
    private List<DetallesExtrasRecargos> listDetalleExtraRecargoBorrar;
    //editar celda
    private ExtrasRecargos editarExtraRecargo;
    private DetallesExtrasRecargos editarDetalleExtraRecargo;
    private int cualCelda, tipoLista, cualCeldaDER, tipoListaDER;
    //duplicar
    private ExtrasRecargos duplicarExtraRecargo;
    private DetallesExtrasRecargos duplicarDetalleExtraRecargo;
    private BigInteger secRegistro, secRegistroDER;
    private BigInteger backUpSecRegistro, backUpSecRegistroDER;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String tipoDia, tipoJornada, legislacion, concepto, empresa;

    //////////////////////
    private List<TiposDias> lovTiposDias;
    private List<TiposDias> filtrarLovTiposDias;
    private TiposDias tipoDiaSeleccionado;

    private List<TiposJornadas> lovTiposJornadas;
    private List<TiposJornadas> filtrarLovTiposJornadaS;
    private TiposJornadas tipoJornadaSeleccionada;

    private List<Contratos> lovContratos;
    private List<Contratos> filtrarLovContratos;
    private Contratos contratoSeleccionado;

    private List<Conceptos> lovConceptos;
    private List<Conceptos> filtrarLovConceptos;
    private Conceptos conceptoSeleccionado;

    private boolean permitirIndex, permitirIndexDER;
    private int tipoActualizacion;
    private String auxExtraDescripcion;
    private BigDecimal auxDetalleIni, auxDetalleFin;
    //
    private String altoTablaRecargo, altoTablaDetalle;
    private String infoRegistroTipoDia, infoRegistroTipoJornada, infoRegistroLegislacion, infoRegistroConcepto;
    //
    private boolean cambiosPagina;
    //
    private String paginaAnterior;

    public ControlATExtraRecargo() {
        altoTablaDetalle = "135";
        altoTablaRecargo = "135";
        cambiosPagina = true;
        auxExtraDescripcion = "";
        auxDetalleIni = null;
        permitirIndex = true;
        auxDetalleFin = null;
        permitirIndexDER = true;
        tipoActualizacion = -1;
        lovTiposDias = null;
        lovTiposJornadas = null;
        lovContratos = null;
        lovConceptos = null;
        tipoDiaSeleccionado = new TiposDias();
        tipoJornadaSeleccionada = new TiposJornadas();
        contratoSeleccionado = new Contratos();
        conceptoSeleccionado = new Conceptos();
        indexDER = -1;
        backUpSecRegistro = null;
        listExtrasRecargos = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listExtraRecargoBorrar = new ArrayList<ExtrasRecargos>();
        listDetalleExtraRecargoModificar = new ArrayList<DetallesExtrasRecargos>();
        listDetalleExtraRecargoBorrar = new ArrayList<DetallesExtrasRecargos>();
        //crear aficiones
        listExtraRecargoCrear = new ArrayList<ExtrasRecargos>();
        listDetalleExtraRecargoCrear = new ArrayList<DetallesExtrasRecargos>();
        k = 0;
        //modificar aficiones
        listExtraRecargoModificar = new ArrayList<ExtrasRecargos>();
        //editar
        editarExtraRecargo = new ExtrasRecargos();
        editarDetalleExtraRecargo = new DetallesExtrasRecargos();
        cualCelda = -1;
        cualCeldaDER = -1;
        tipoListaDER = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        guardadoDER = true;
        //Crear VC
        nuevoExtraRecargo = new ExtrasRecargos();
        nuevoExtraRecargo.setTipodia(new TiposDias());
        nuevoExtraRecargo.setTipojornada(new TiposJornadas());
        nuevoExtraRecargo.setTipolegislacion(new Contratos());
        nuevoDetalleExtraRecargo = new DetallesExtrasRecargos();
        nuevoDetalleExtraRecargo.setConcepto(new Conceptos());
        nuevoDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
        duplicarExtraRecargo = new ExtrasRecargos();
        duplicarDetalleExtraRecargo = new DetallesExtrasRecargos();
        secRegistro = null;
        secRegistroDER = null;
        backUpSecRegistroDER = null;
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
            administrarATExtraRecargo.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String page) {
        paginaAnterior = page;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public boolean validarCamposNulosExtraRecargo(int i) {
        boolean retorno = true;
        if (i == 0) {
            ExtrasRecargos aux = new ExtrasRecargos();
            if (tipoLista == 0) {
                aux = listExtrasRecargos.get(index);
            }
            if (tipoLista == 1) {
                aux = filtrarListExtrasRecargos.get(index);
            }
            if (aux.getDescripcion() == null) {
                retorno = false;
            } else {
                if (aux.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (aux.getTipodia().getSecuencia() == null) {
                retorno = false;
            }
            if (aux.getTipojornada().getSecuencia() == null) {
                retorno = false;
            }
            if (aux.getTipolegislacion().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoExtraRecargo.getDescripcion() == null) {
                retorno = false;
            } else {
                if (nuevoExtraRecargo.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (nuevoExtraRecargo.getTipodia().getSecuencia() == null) {
                retorno = false;
            }
            if (nuevoExtraRecargo.getTipojornada().getSecuencia() == null) {
                retorno = false;
            }
            if (nuevoExtraRecargo.getTipolegislacion().getSecuencia() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarExtraRecargo.getDescripcion() == null) {
                retorno = false;
            } else {
                if (duplicarExtraRecargo.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (duplicarExtraRecargo.getTipodia().getSecuencia() == null) {
                retorno = false;
            }
            if (duplicarExtraRecargo.getTipojornada().getSecuencia() == null) {
                retorno = false;
            }
            if (duplicarExtraRecargo.getTipolegislacion().getSecuencia() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosDetalleExtraRecargo(int i) {
        boolean retorno = true;
        if (i == 0) {
            DetallesExtrasRecargos aux = new DetallesExtrasRecargos();
            if (tipoListaDER == 0) {
                aux = listDetallesExtrasRecargos.get(indexDER);
            }
            if (tipoListaDER == 1) {
                aux = filtrarListDetallesExtrasRecargos.get(indexDER);
            }
            if (aux.getHorasinicial() == null || aux.getHorasfinal() == null) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoDetalleExtraRecargo.getHorasinicial() == null || nuevoDetalleExtraRecargo.getHorasfinal() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarDetalleExtraRecargo.getHorasinicial() == null || duplicarDetalleExtraRecargo.getHorasfinal() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void procesoModificacionExtraRecargo(int i) {
        index = i;
        boolean respuesta = validarCamposNulosExtraRecargo(0);
        if (respuesta == true) {
            modificarExtraRecargo(i);
        } else {
            if (tipoLista == 0) {
                listExtrasRecargos.get(index).setDescripcion(auxExtraDescripcion);
            }
            if (tipoLista == 1) {
                filtrarListExtrasRecargos.get(index).setDescripcion(auxExtraDescripcion);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullExtra').show()");
        }
    }

    public void procesoModificacionDetalleExtraRecargo(int i) {
        index = i;
        boolean respuesa = validarCamposNulosDetalleExtraRecargo(0);
        if (respuesa == true) {
            modificarDetalleExtraRecargo(i);
        } else {
            if (tipoListaDER == 0) {
                listDetallesExtrasRecargos.get(indexDER).setHorasfinal(auxDetalleFin);
                listDetallesExtrasRecargos.get(indexDER).setHorasinicial(auxDetalleIni);
            }
            if (tipoListaDER == 1) {
                filtrarListDetallesExtrasRecargos.get(indexDER).setHorasfinal(auxDetalleFin);
                filtrarListDetallesExtrasRecargos.get(indexDER).setHorasinicial(auxDetalleIni);
            }
            indexDER = -1;
            secRegistroDER = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalle').show()");
        }
    }

    public void modificarExtraRecargo(int indice) {
        int tamDes = 0;
        if (tipoLista == 0) {
            tamDes = listExtrasRecargos.get(indice).getDescripcion().length();
        }
        if (tipoLista == 1) {
            tamDes = filtrarListExtrasRecargos.get(indice).getDescripcion().length();
        }
        if (tamDes >= 1 && tamDes <= 40) {
            if (tipoLista == 0) {
                String textM = listExtrasRecargos.get(indice).getDescripcion().toUpperCase();
                listExtrasRecargos.get(indice).setDescripcion(textM);
                if (!listExtraRecargoCrear.contains(listExtrasRecargos.get(indice))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(indice));
                    } else if (!listExtraRecargoModificar.contains(listExtrasRecargos.get(indice))) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            if (tipoLista == 1) {
                String textM = filtrarListExtrasRecargos.get(indice).getDescripcion().toUpperCase();
                filtrarListExtrasRecargos.get(indice).setDescripcion(textM);
                if (!listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(indice))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(indice));
                    } else if (!listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(indice))) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
        } else {
            if (tipoLista == 0) {
                listExtrasRecargos.get(index).setDescripcion(auxExtraDescripcion);
            }
            if (tipoLista == 1) {
                filtrarListExtrasRecargos.get(index).setDescripcion(auxExtraDescripcion);
            }
            index = -1;
            secRegistro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionExtra').show()");
        }

    }

    public void modificarExtraRecargo(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("DIAS")) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(indice).getTipodia().setDescripcion(tipoDia);
            } else {
                filtrarListExtrasRecargos.get(indice).getTipodia().setDescripcion(tipoDia);
            }
            for (int i = 0; i < lovTiposDias.size(); i++) {
                if (lovTiposDias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listExtrasRecargos.get(indice).setTipodia(lovTiposDias.get(indiceUnicoElemento));
                } else {
                    filtrarListExtrasRecargos.get(indice).setTipodia(lovTiposDias.get(indiceUnicoElemento));
                }
                lovTiposDias.clear();
                getLovTiposDias();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("JORNADA")) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(indice).getTipojornada().setDescripcion(tipoJornada);
            } else {
                filtrarListExtrasRecargos.get(indice).getTipojornada().setDescripcion(tipoJornada);
            }
            for (int i = 0; i < lovTiposJornadas.size(); i++) {
                if (lovTiposJornadas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listExtrasRecargos.get(indice).setTipojornada(lovTiposJornadas.get(indiceUnicoElemento));
                } else {
                    filtrarListExtrasRecargos.get(indice).setTipojornada(lovTiposJornadas.get(indiceUnicoElemento));
                }
                lovTiposJornadas.clear();
                getLovTiposJornadas();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:TipoJornadaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (confirmarCambio.equalsIgnoreCase("LEGISLACION")) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(indice).getTipolegislacion().setDescripcion(legislacion);
            } else {
                filtrarListExtrasRecargos.get(indice).getTipolegislacion().setDescripcion(legislacion);
            }
            for (int i = 0; i < lovContratos.size(); i++) {
                if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listExtrasRecargos.get(indice).setTipolegislacion(lovContratos.get(indiceUnicoElemento));
                } else {
                    filtrarListExtrasRecargos.get(indice).setTipolegislacion(lovContratos.get(indiceUnicoElemento));
                }
                lovContratos.clear();
                getLovContratos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:LegislacionDialogo");
                RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listExtraRecargoCrear.contains(listExtrasRecargos.get(indice))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(indice));
                    } else if (!listExtraRecargoModificar.contains(listExtrasRecargos.get(indice))) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            if (tipoLista == 1) {
                if (!listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(indice))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(indice));
                    } else if (!listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(indice))) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
    }

    public void modificarDetalleExtraRecargo(int indice) {
        if (tipoListaDER == 0) {
            if (!listDetalleExtraRecargoCrear.contains(listDetallesExtrasRecargos.get(indice))) {
                if (listDetalleExtraRecargoModificar.isEmpty()) {
                    listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indice));
                } else if (!listDetalleExtraRecargoModificar.contains(listDetallesExtrasRecargos.get(indice))) {
                    listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indice));
                }
                if (guardadoDER == true) {
                    guardadoDER = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (tipoListaDER == 1) {
            if (!listDetalleExtraRecargoCrear.contains(filtrarListDetallesExtrasRecargos.get(indice))) {
                if (listDetalleExtraRecargoModificar.isEmpty()) {
                    listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indice));
                } else if (!listDetalleExtraRecargoModificar.contains(filtrarListDetallesExtrasRecargos.get(indice))) {
                    listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indice));
                }
                if (guardadoDER == true) {
                    guardadoDER = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        indexDER = -1;
        secRegistroDER = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
    }

    public void modificarDetalleExtraRecargo(int indice, String confirmarCambio, String valorConfirmar) {
        indexDER = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listDetallesExtrasRecargos.get(indice).getConcepto().setDescripcion(concepto);
                } else {
                    filtrarListDetallesExtrasRecargos.get(indice).getConcepto().setDescripcion(concepto);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listDetallesExtrasRecargos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarListDetallesExtrasRecargos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    }
                    lovConceptos.clear();
                    getLovConceptos();
                } else {
                    permitirIndexDER = false;
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                lovConceptos.clear();
                getLovConceptos();
                coincidencias = 1;
                if (tipoLista == 0) {
                    listDetallesExtrasRecargos.get(indice).setConcepto(new Conceptos());
                    listDetallesExtrasRecargos.get(indice).getConcepto().setEmpresa(new Empresas());
                } else {
                    filtrarListDetallesExtrasRecargos.get(indice).setConcepto(new Conceptos());
                    filtrarListDetallesExtrasRecargos.get(indice).getConcepto().setEmpresa(new Empresas());
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listDetallesExtrasRecargos.get(indice).getConcepto().getEmpresa().setNombre(empresa);
                } else {
                    filtrarListDetallesExtrasRecargos.get(indice).getConcepto().getEmpresa().setNombre(empresa);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listDetallesExtrasRecargos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarListDetallesExtrasRecargos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    }
                    lovConceptos.clear();
                    getLovConceptos();
                } else {
                    permitirIndexDER = false;
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                lovConceptos.clear();
                getLovConceptos();
                if (tipoLista == 0) {
                    listDetallesExtrasRecargos.get(indice).setConcepto(new Conceptos());
                    listDetallesExtrasRecargos.get(indice).getConcepto().setEmpresa(new Empresas());
                } else {
                    filtrarListDetallesExtrasRecargos.get(indice).setConcepto(new Conceptos());
                    filtrarListDetallesExtrasRecargos.get(indice).getConcepto().setEmpresa(new Empresas());
                }
            }
        }
        if (coincidencias == 1) {
            if (tipoListaDER == 0) {
                if (!listDetalleExtraRecargoCrear.contains(listDetallesExtrasRecargos.get(indice))) {
                    if (listDetalleExtraRecargoModificar.isEmpty()) {
                        listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indice));
                    } else if (!listDetalleExtraRecargoModificar.contains(listDetallesExtrasRecargos.get(indice))) {
                        listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indice));
                    }
                    if (guardadoDER == true) {
                        guardadoDER = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            if (tipoListaDER == 1) {
                if (!listDetalleExtraRecargoCrear.contains(filtrarListDetallesExtrasRecargos.get(indice))) {
                    if (listDetalleExtraRecargoModificar.isEmpty()) {
                        listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indice));
                    } else if (!listDetalleExtraRecargoModificar.contains(filtrarListDetallesExtrasRecargos.get(indice))) {
                        listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indice));
                    }
                    if (guardadoDER == true) {
                        guardadoDER = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
    }

    public void cambiarIndice(int indice, int celda) {
        if (guardadoDER == true) {
            if (permitirIndex == true) {
                index = indice;
                cualCelda = celda;
                indexAux = indice;
                indexDER = -1;

                if (tipoLista == 0) {
                    auxExtraDescripcion = listExtrasRecargos.get(index).getDescripcion();
                    secRegistro = listExtrasRecargos.get(index).getSecuencia();
                    tipoDia = listExtrasRecargos.get(index).getTipodia().getDescripcion();
                    tipoJornada = listExtrasRecargos.get(index).getTipojornada().getDescripcion();
                    legislacion = listExtrasRecargos.get(index).getTipolegislacion().getDescripcion();
                }
                if (tipoLista == 1) {
                    auxExtraDescripcion = filtrarListExtrasRecargos.get(index).getDescripcion();
                    secRegistro = filtrarListExtrasRecargos.get(index).getSecuencia();
                    tipoDia = filtrarListExtrasRecargos.get(index).getTipodia().getDescripcion();
                    tipoJornada = filtrarListExtrasRecargos.get(index).getTipojornada().getDescripcion();
                    legislacion = filtrarListExtrasRecargos.get(index).getTipolegislacion().getDescripcion();
                }
                listDetallesExtrasRecargos = null;
                getListDetallesExtrasRecargos();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                if (banderaDER == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTablaDetalle = "135";
                    detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
                    detalleHF.setFilterStyle("display: none; visibility: hidden;");
                    detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
                    detalleHI.setFilterStyle("display: none; visibility: hidden;");
                    detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
                    detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                    detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
                    detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
                    detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
                    detalleDia.setFilterStyle("display: none; visibility: hidden;");
                    detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
                    detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
                    detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
                    detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                    detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
                    detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
                    detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
                    detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
                    detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
                    detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                    banderaDER = 0;
                    filtrarListDetallesExtrasRecargos = null;
                    tipoListaDER = 0;
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceDER(int indice, int celda) {
        if (permitirIndexDER == true) {
            indexDER = indice;
            index = -1;
            cualCeldaDER = celda;
            if (tipoListaDER == 0) {
                auxDetalleFin = listDetallesExtrasRecargos.get(indexDER).getHorasfinal();
                auxDetalleIni = listDetallesExtrasRecargos.get(indexDER).getHorasinicial();
                secRegistroDER = listDetallesExtrasRecargos.get(indexDER).getSecuencia();
                concepto = listDetallesExtrasRecargos.get(indexDER).getConcepto().getDescripcion();
                empresa = listDetallesExtrasRecargos.get(indexDER).getConcepto().getEmpresa().getNombre();
            }
            if (tipoListaDER == 1) {
                auxDetalleFin = filtrarListDetallesExtrasRecargos.get(indexDER).getHorasfinal();
                auxDetalleIni = filtrarListDetallesExtrasRecargos.get(indexDER).getHorasinicial();
                secRegistroDER = filtrarListDetallesExtrasRecargos.get(indexDER).getSecuencia();
                concepto = filtrarListDetallesExtrasRecargos.get(indexDER).getConcepto().getDescripcion();
                empresa = filtrarListDetallesExtrasRecargos.get(indexDER).getConcepto().getEmpresa().getNombre();
            }
            if (bandera == 0) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaRecargo = "135";
                extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
                extraCodigo.setFilterStyle("display: none; visibility: hidden;");
                extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
                extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
                extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
                extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
                extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
                extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
                extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
                extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
                extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
                extraTurno.setFilterStyle("display: none; visibility: hidden;");
                extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
                extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                bandera = 0;
                filtrarListExtrasRecargos = null;
                tipoLista = 0;
            }
        }
    }
    //GUARDAR

    public void guardarGeneral() {
        guardarCambiosExtraRecargo();
        guardarCambiosDetalleExtraRecargo();
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void guardarCambiosExtraRecargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listExtraRecargoBorrar.isEmpty()) {
                    for (int i = 0; i < listExtraRecargoBorrar.size(); i++) {
                        administrarATExtraRecargo.borrarExtrasRecargos(listExtraRecargoBorrar);
                    }
                    listExtraRecargoBorrar.clear();
                }
                if (!listExtraRecargoCrear.isEmpty()) {
                    for (int i = 0; i < listExtraRecargoCrear.size(); i++) {
                        administrarATExtraRecargo.crearExtrasRecargos(listExtraRecargoCrear);
                    }
                    listExtraRecargoCrear.clear();
                }
                if (!listExtraRecargoModificar.isEmpty()) {
                    administrarATExtraRecargo.editarExtrasRecargos(listExtraRecargoModificar);
                    listExtraRecargoModificar.clear();
                }
                listExtrasRecargos = null;
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                guardado = true;
                k = 0;
                index = -1;
                secRegistro = null;
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Recargo Extra con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosExtraRecargo : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Recargo Extra, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosDetalleExtraRecargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardadoDER == false) {
                if (!listDetalleExtraRecargoBorrar.isEmpty()) {
                    for (int i = 0; i < listDetalleExtraRecargoBorrar.size(); i++) {
                        administrarATExtraRecargo.borrarDetallesExtrasRecargos(listDetalleExtraRecargoBorrar);
                    }
                    listDetalleExtraRecargoBorrar.clear();
                }
                if (!listDetalleExtraRecargoCrear.isEmpty()) {
                    for (int i = 0; i < listDetalleExtraRecargoCrear.size(); i++) {
                        administrarATExtraRecargo.crearDetallesExtrasRecargos(listDetalleExtraRecargoCrear);
                    }
                    listDetalleExtraRecargoCrear.clear();
                }
                if (!listDetalleExtraRecargoModificar.isEmpty()) {
                    administrarATExtraRecargo.editarDetallesExtrasRecargos(listDetalleExtraRecargoModificar);
                    listDetalleExtraRecargoModificar.clear();
                }
                listDetallesExtrasRecargos = null;
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                guardadoDER = true;
                k = 0;
                indexDER = -1;
                secRegistroDER = null;
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Detalle con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosExtraRecargo : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Detalle, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacionGeneral() {
        if (guardado == false) {
            cancelarModificacionExtraRecargo();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
        }
        if (guardadoDER == false) {
            cancelarModificacionDetalleExtraRecargo();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
        }
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificacionExtraRecargo() {
        if (bandera == 1) {
            altoTablaRecargo = "135";
            FacesContext c = FacesContext.getCurrentInstance();
            extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
            extraCodigo.setFilterStyle("display: none; visibility: hidden;");
            extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
            extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
            extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
            extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
            extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
            extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
            extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
            extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
            extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
            extraTurno.setFilterStyle("display: none; visibility: hidden;");
            extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
            extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
            bandera = 0;
            filtrarListExtrasRecargos = null;
            tipoLista = 0;
        }
        listExtraRecargoBorrar.clear();
        listExtraRecargoCrear.clear();
        listExtraRecargoModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listExtrasRecargos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
    }

    public void cancelarModificacionDetalleExtraRecargo() {
        if (banderaDER == 1) {
            altoTablaDetalle = "135";
            FacesContext c = FacesContext.getCurrentInstance();
            detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
            detalleHF.setFilterStyle("display: none; visibility: hidden;");
            detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
            detalleHI.setFilterStyle("display: none; visibility: hidden;");
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
            detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
            detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
            detalleDia.setFilterStyle("display: none; visibility: hidden;");
            detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
            detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
            detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
            detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
            detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
            detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
            detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            banderaDER = 0;
            filtrarListDetallesExtrasRecargos = null;
            tipoListaDER = 0;
        }
        listDetalleExtraRecargoBorrar.clear();
        listDetalleExtraRecargoCrear.clear();
        listDetalleExtraRecargoModificar.clear();
        indexDER = -1;
        secRegistroDER = null;
        k = 0;
        listDetallesExtrasRecargos = null;
        guardadoDER = true;

        permitirIndexDER = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarExtraRecargo = listExtrasRecargos.get(index);
            }
            if (tipoLista == 1) {
                editarExtraRecargo = filtrarListExtrasRecargos.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarExtraCodigoD");
                RequestContext.getCurrentInstance().execute("PF('editarExtraCodigoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarExtraDescripcionD");
                RequestContext.getCurrentInstance().execute("PF('editarExtraDescripcionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarExtraTipoDiaD");
                RequestContext.getCurrentInstance().execute("PF('editarExtraTipoDiaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarExtraTipoJornadaD");
                RequestContext.getCurrentInstance().execute("PF('editarExtraTipoJornadaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarExtraLegislacionD");
                RequestContext.getCurrentInstance().execute("PF('editarExtraLegislacionD').show()");
                cualCelda = -1;
            }
            index = -1;
            secRegistro = null;
        }
        if (indexDER >= 0) {
            if (tipoListaDER == 0) {
                editarDetalleExtraRecargo = listDetallesExtrasRecargos.get(indexDER);
            }
            if (tipoListaDER == 1) {
                editarDetalleExtraRecargo = listDetallesExtrasRecargos.get(indexDER);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaDER == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleHID");
                RequestContext.getCurrentInstance().execute("PF('editarDetalleHID').show()");
                cualCeldaDER = -1;
            } else if (cualCeldaDER == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleHFD");
                RequestContext.getCurrentInstance().execute("PF('editarDetalleHFD').show()");
                cualCeldaDER = -1;
            } else if (cualCeldaDER == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleConceptoD");
                RequestContext.getCurrentInstance().execute("PF('editarDetalleConceptoD').show()");
                cualCeldaDER = -1;
            } else if (cualCeldaDER == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleEmpresaD");
                RequestContext.getCurrentInstance().execute("PF('editarDetalleEmpresaD').show()");
                cualCeldaDER = -1;
            } else if (cualCeldaDER == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalleMinutoD");
                RequestContext.getCurrentInstance().execute("PF('editarDetalleMinutoD').show()");
                cualCeldaDER = -1;
            }
            indexDER = -1;
            secRegistroDER = null;
        }

    }

    public void dialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        int tamER = listExtrasRecargos.size();
        int tamDER = listDetallesExtrasRecargos.size();
        if (tamER == 0 || tamDER == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
        } else {
            if (index >= 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroExtraRecargo");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroExtraRecargo').show()");
            }
            if (indexDER >= 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetalleExtraRecargo");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleExtraRecargo').show()");
            }
        }
    }

    //CREAR 
    public void agregarNuevoExtraRecargo() {
        boolean respueta = validarCamposNulosExtraRecargo(1);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = nuevoExtraRecargo.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 40) {
                if (bandera == 1) {
                    altoTablaRecargo = "135";
                    FacesContext c = FacesContext.getCurrentInstance();
                    extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
                    extraCodigo.setFilterStyle("display: none; visibility: hidden;");
                    extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
                    extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
                    extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
                    extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
                    extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
                    extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
                    extraTurno.setFilterStyle("display: none; visibility: hidden;");
                    extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
                    extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                    bandera = 0;
                    filtrarListExtrasRecargos = null;
                    tipoLista = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                String text = nuevoExtraRecargo.getDescripcion().toUpperCase();
                nuevoExtraRecargo.setDescripcion(text);
                nuevoExtraRecargo.setSecuencia(l);
                listExtraRecargoCrear.add(nuevoExtraRecargo);
                listExtrasRecargos.add(nuevoExtraRecargo);
                nuevoExtraRecargo = new ExtrasRecargos();
                nuevoExtraRecargo.setTipodia(new TiposDias());
                nuevoExtraRecargo.setTipojornada(new TiposJornadas());
                nuevoExtraRecargo.setTipolegislacion(new Contratos());
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroExtraRecargo').hide()");
                if (guardado == true) {
                    guardado = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                index = -1;
                secRegistro = null;
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionExtra').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullExtra').show()");
        }
    }

    public void agregarNuevoDetalleExtraRecargo() {
        boolean respueta = validarCamposNulosDetalleExtraRecargo(1);
        if (respueta == true) {
            if (banderaDER == 1) {
                altoTablaDetalle = "135";
                FacesContext c = FacesContext.getCurrentInstance();
                detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
                detalleHF.setFilterStyle("display: none; visibility: hidden;");
                detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
                detalleHI.setFilterStyle("display: none; visibility: hidden;");
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
                detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
                detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
                detalleDia.setFilterStyle("display: none; visibility: hidden;");
                detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
                detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
                detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
                detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
                detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
                detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                banderaDER = 0;
                filtrarListDetallesExtrasRecargos = null;
                tipoListaDER = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoDetalleExtraRecargo.setSecuencia(l);
            if (tipoLista == 0) {
                nuevoDetalleExtraRecargo.setExtrarecargo(listExtrasRecargos.get(indexAux));
            }
            if (tipoLista == 1) {
                nuevoDetalleExtraRecargo.setExtrarecargo(filtrarListExtrasRecargos.get(indexAux));
            }
            listDetalleExtraRecargoCrear.add(nuevoDetalleExtraRecargo);
            listDetallesExtrasRecargos.add(nuevoDetalleExtraRecargo);
            nuevoDetalleExtraRecargo = new DetallesExtrasRecargos();
            nuevoDetalleExtraRecargo.setConcepto(new Conceptos());
            nuevoDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleExtraRecargo').hide()");
            if (guardadoDER == true) {
                guardadoDER = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            indexDER = -1;
            secRegistroDER = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalle').show()");
        }
    }

    public void limpiarNuevaExtraRecargo() {
        nuevoExtraRecargo = new ExtrasRecargos();
        nuevoExtraRecargo.setTipodia(new TiposDias());
        nuevoExtraRecargo.setTipojornada(new TiposJornadas());
        nuevoExtraRecargo.setTipolegislacion(new Contratos());
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaDetalleExtraRecargo() {
        nuevoDetalleExtraRecargo = new DetallesExtrasRecargos();
        nuevoDetalleExtraRecargo.setConcepto(new Conceptos());
        nuevoDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
        indexDER = -1;
        secRegistroDER = null;
    }

    public void verificarRegistroDuplicar() {
        if (index >= 0) {
            duplicarExtraRecargoM();
        }
        if (indexDER >= 0) {
            duplicarDetalleExtraRecuerdoM();
        }
    }

    public void duplicarExtraRecargoM() {
        if (index >= 0) {
            duplicarExtraRecargo = new ExtrasRecargos();
            if (tipoLista == 0) {
                duplicarExtraRecargo.setAprobacionautomatica(listExtrasRecargos.get(index).getAprobacionautomatica());
                duplicarExtraRecargo.setDescripcion(listExtrasRecargos.get(index).getDescripcion());
                duplicarExtraRecargo.setTipodia(listExtrasRecargos.get(index).getTipodia());
                duplicarExtraRecargo.setTipojornada(listExtrasRecargos.get(index).getTipojornada());
                duplicarExtraRecargo.setTipolegislacion(listExtrasRecargos.get(index).getTipolegislacion());
                duplicarExtraRecargo.setTurno(listExtrasRecargos.get(index).getTurno());
                duplicarExtraRecargo.setCodigo(listExtrasRecargos.get(index).getCodigo());
            }
            if (tipoLista == 1) {
                duplicarExtraRecargo.setAprobacionautomatica(filtrarListExtrasRecargos.get(index).getAprobacionautomatica());
                duplicarExtraRecargo.setDescripcion(filtrarListExtrasRecargos.get(index).getDescripcion());
                duplicarExtraRecargo.setTipodia(filtrarListExtrasRecargos.get(index).getTipodia());
                duplicarExtraRecargo.setTipojornada(filtrarListExtrasRecargos.get(index).getTipojornada());
                duplicarExtraRecargo.setTipolegislacion(filtrarListExtrasRecargos.get(index).getTipolegislacion());
                duplicarExtraRecargo.setTurno(filtrarListExtrasRecargos.get(index).getTurno());
                duplicarExtraRecargo.setCodigo(filtrarListExtrasRecargos.get(index).getCodigo());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroExtraRecargo').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void duplicarDetalleExtraRecuerdoM() {
        if (indexDER >= 0) {
            duplicarDetalleExtraRecargo = new DetallesExtrasRecargos();
            if (tipoListaDER == 0) {
                duplicarDetalleExtraRecargo.setAdicional(listDetallesExtrasRecargos.get(indexDER).getAdicional());
                duplicarDetalleExtraRecargo.setAdicionaliniciofinjornada(listDetallesExtrasRecargos.get(indexDER).getAdicionaliniciofinjornada());
                duplicarDetalleExtraRecargo.setConcepto(listDetallesExtrasRecargos.get(indexDER).getConcepto());
                duplicarDetalleExtraRecargo.setDia(listDetallesExtrasRecargos.get(indexDER).getDia());
                duplicarDetalleExtraRecargo.setHorasinicial(listDetallesExtrasRecargos.get(indexDER).getHorasinicial());
                duplicarDetalleExtraRecargo.setHorasfinal(listDetallesExtrasRecargos.get(indexDER).getHorasfinal());
                duplicarDetalleExtraRecargo.setMinimominutospagar(listDetallesExtrasRecargos.get(indexDER).getMinimominutospagar());
                duplicarDetalleExtraRecargo.setGarantizaadicional(listDetallesExtrasRecargos.get(indexDER).getGarantizaadicional());
                duplicarDetalleExtraRecargo.setPagotiempocompleto(listDetallesExtrasRecargos.get(indexDER).getPagotiempocompleto());
            }
            if (tipoListaDER == 1) {
                duplicarDetalleExtraRecargo.setAdicional(filtrarListDetallesExtrasRecargos.get(indexDER).getAdicional());
                duplicarDetalleExtraRecargo.setAdicionaliniciofinjornada(filtrarListDetallesExtrasRecargos.get(indexDER).getAdicionaliniciofinjornada());
                duplicarDetalleExtraRecargo.setConcepto(filtrarListDetallesExtrasRecargos.get(indexDER).getConcepto());
                duplicarDetalleExtraRecargo.setDia(filtrarListDetallesExtrasRecargos.get(indexDER).getDia());
                duplicarDetalleExtraRecargo.setHorasinicial(filtrarListDetallesExtrasRecargos.get(indexDER).getHorasinicial());
                duplicarDetalleExtraRecargo.setHorasfinal(filtrarListDetallesExtrasRecargos.get(indexDER).getHorasfinal());
                duplicarDetalleExtraRecargo.setMinimominutospagar(filtrarListDetallesExtrasRecargos.get(indexDER).getMinimominutospagar());
                duplicarDetalleExtraRecargo.setGarantizaadicional(filtrarListDetallesExtrasRecargos.get(indexDER).getGarantizaadicional());
                duplicarDetalleExtraRecargo.setPagotiempocompleto(filtrarListDetallesExtrasRecargos.get(indexDER).getPagotiempocompleto());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleExtraRecargo').show()");
            indexDER = -1;
            secRegistroDER = null;
        }
    }

    public void confirmarDuplicarExtraRecargo() {
        boolean respueta = validarCamposNulosExtraRecargo(2);
        if (respueta == true) {
            int tamDes = 0;
            tamDes = duplicarExtraRecargo.getDescripcion().length();
            if (tamDes >= 1 && tamDes <= 40) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarExtraRecargo.setSecuencia(l);
                String text = duplicarExtraRecargo.getDescripcion().toUpperCase();
                duplicarExtraRecargo.setDescripcion(text);
                listExtrasRecargos.add(duplicarExtraRecargo);
                listExtraRecargoCrear.add(duplicarExtraRecargo);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroExtraRecargo').hide()");
                index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                }
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (bandera == 1) {
                    altoTablaRecargo = "135";
                    FacesContext c = FacesContext.getCurrentInstance();
                    extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
                    extraCodigo.setFilterStyle("display: none; visibility: hidden;");
                    extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
                    extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
                    extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
                    extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
                    extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
                    extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
                    extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
                    extraTurno.setFilterStyle("display: none; visibility: hidden;");
                    extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
                    extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                    bandera = 0;
                    filtrarListExtrasRecargos = null;
                    tipoLista = 0;
                }
                duplicarExtraRecargo = new ExtrasRecargos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorDescripcionExtra').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullExtra').show()");
        }
    }

    public void confirmarDuplicarDetalleExtraCargo() {
        boolean respueta = validarCamposNulosDetalleExtraRecargo(2);
        if (respueta == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarDetalleExtraRecargo.setSecuencia(l);
            if (tipoLista == 0) {
                duplicarDetalleExtraRecargo.setExtrarecargo(listExtrasRecargos.get(indexAux));
            }
            if (tipoLista == 1) {
                duplicarDetalleExtraRecargo.setExtrarecargo(filtrarListExtrasRecargos.get(indexAux));
            }
            listDetallesExtrasRecargos.add(duplicarDetalleExtraRecargo);
            listDetalleExtraRecargoCrear.add(duplicarDetalleExtraRecargo);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleExtraRecargo').hide()");
            indexDER = -1;
            secRegistroDER = null;
            if (guardadoDER == true) {
                guardadoDER = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (banderaDER == 1) {
                altoTablaDetalle = "135";
                FacesContext c = FacesContext.getCurrentInstance();
                detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
                detalleHF.setFilterStyle("display: none; visibility: hidden;");
                detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
                detalleHI.setFilterStyle("display: none; visibility: hidden;");
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
                detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
                detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
                detalleDia.setFilterStyle("display: none; visibility: hidden;");
                detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
                detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
                detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
                detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
                detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
                detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                banderaDER = 0;
                filtrarListDetallesExtrasRecargos = null;
                tipoListaDER = 0;
            }
            duplicarDetalleExtraRecargo = new DetallesExtrasRecargos();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalle').show()");
        }
    }

    public void limpiarDuplicarExtraRecargo() {
        duplicarExtraRecargo = new ExtrasRecargos();
        duplicarExtraRecargo.setTipodia(new TiposDias());
        duplicarExtraRecargo.setTipojornada(new TiposJornadas());
        duplicarExtraRecargo.setTipolegislacion(new Contratos());
    }

    public void limpiarDuplicarDetalleExtraRecargo() {
        duplicarDetalleExtraRecargo = new DetallesExtrasRecargos();
        duplicarDetalleExtraRecargo = new DetallesExtrasRecargos();
        duplicarDetalleExtraRecargo.setConcepto(new Conceptos());
        duplicarDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    public void verificarRegistroBorrar() {
        if (index >= 0) {
            if (listDetallesExtrasRecargos.isEmpty()) {
                borrarExtraRecargo();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
        }
        if (indexDER >= 0) {
            borrarDetalleExtraRecargo();
        }
    }

    public void borrarExtraRecargo() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listExtraRecargoModificar.isEmpty() && listExtraRecargoModificar.contains(listExtrasRecargos.get(index))) {
                    int modIndex = listExtraRecargoModificar.indexOf(listExtrasRecargos.get(index));
                    listExtraRecargoModificar.remove(modIndex);
                    listExtraRecargoBorrar.add(listExtrasRecargos.get(index));
                } else if (!listExtraRecargoCrear.isEmpty() && listExtraRecargoCrear.contains(listExtrasRecargos.get(index))) {
                    int crearIndex = listExtraRecargoCrear.indexOf(listExtrasRecargos.get(index));
                    listExtraRecargoCrear.remove(crearIndex);
                } else {
                    listExtraRecargoBorrar.add(listExtrasRecargos.get(index));
                }
                listExtrasRecargos.remove(index);
            }
            if (tipoLista == 1) {
                if (!listExtraRecargoModificar.isEmpty() && listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(index))) {
                    int modIndex = listExtraRecargoModificar.indexOf(filtrarListExtrasRecargos.get(index));
                    listExtraRecargoModificar.remove(modIndex);
                    listExtraRecargoBorrar.add(filtrarListExtrasRecargos.get(index));
                } else if (!listExtraRecargoCrear.isEmpty() && listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(index))) {
                    int crearIndex = listExtraRecargoCrear.indexOf(filtrarListExtrasRecargos.get(index));
                    listExtraRecargoCrear.remove(crearIndex);
                } else {
                    listExtraRecargoBorrar.add(filtrarListExtrasRecargos.get(index));
                }
                int VCIndex = listExtrasRecargos.indexOf(filtrarListExtrasRecargos.get(index));
                listExtrasRecargos.remove(VCIndex);
                filtrarListExtrasRecargos.remove(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarDetalleExtraRecargo() {
        if (indexDER >= 0) {
            if (tipoListaDER == 0) {
                if (!listDetalleExtraRecargoModificar.isEmpty() && listDetalleExtraRecargoModificar.contains(listDetallesExtrasRecargos.get(indexDER))) {
                    int modIndex = listDetalleExtraRecargoModificar.indexOf(listDetallesExtrasRecargos.get(indexDER));
                    listDetalleExtraRecargoModificar.remove(modIndex);
                    listDetalleExtraRecargoBorrar.add(listDetallesExtrasRecargos.get(indexDER));
                } else if (!listDetalleExtraRecargoCrear.isEmpty() && listDetalleExtraRecargoCrear.contains(listDetallesExtrasRecargos.get(indexDER))) {
                    int crearIndex = listDetalleExtraRecargoCrear.indexOf(listDetallesExtrasRecargos.get(indexDER));
                    listDetalleExtraRecargoCrear.remove(crearIndex);
                } else {
                    listDetalleExtraRecargoBorrar.add(listDetallesExtrasRecargos.get(indexDER));
                }
                listDetallesExtrasRecargos.remove(indexDER);
            }
            if (tipoListaDER == 1) {
                if (!listDetalleExtraRecargoModificar.isEmpty() && listDetalleExtraRecargoModificar.contains(filtrarListDetallesExtrasRecargos.get(indexDER))) {
                    int modIndex = listDetalleExtraRecargoModificar.indexOf(filtrarListDetallesExtrasRecargos.get(indexDER));
                    listDetalleExtraRecargoModificar.remove(modIndex);
                    listDetalleExtraRecargoBorrar.add(filtrarListDetallesExtrasRecargos.get(indexDER));
                } else if (!listDetalleExtraRecargoCrear.isEmpty() && listDetalleExtraRecargoCrear.contains(filtrarListDetallesExtrasRecargos.get(indexDER))) {
                    int crearIndex = listDetalleExtraRecargoCrear.indexOf(filtrarListDetallesExtrasRecargos.get(indexDER));
                    listDetalleExtraRecargoCrear.remove(crearIndex);
                } else {
                    listDetalleExtraRecargoBorrar.add(filtrarListDetallesExtrasRecargos.get(indexDER));
                }
                int VCIndex = listDetallesExtrasRecargos.indexOf(filtrarListDetallesExtrasRecargos.get(indexDER));
                listDetallesExtrasRecargos.remove(VCIndex);
                filtrarListDetallesExtrasRecargos.remove(indexDER);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            indexDER = -1;
            secRegistroDER = null;

            if (guardadoDER == true) {
                guardadoDER = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void activarCtrlF11() {
        if (index >= 0) {
            if (bandera == 0) {
                altoTablaRecargo = "117";
                FacesContext c = FacesContext.getCurrentInstance();
                extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
                extraCodigo.setFilterStyle("width: 85% !important;");
                extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
                extraDescripcion.setFilterStyle("width: 85% !important;");
                extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
                extraTipoDia.setFilterStyle("width: 65px");
                extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
                extraTipoJornada.setFilterStyle("width: 85% !important;");
                extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
                extraTipoLegislacion.setFilterStyle("width: 85% !important;");
                extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
                extraTurno.setFilterStyle("width: 10px");
                extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
                extraAprobacion.setFilterStyle("width: 10px");
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                bandera = 1;
            } else {
                altoTablaRecargo = "135";
                FacesContext c = FacesContext.getCurrentInstance();
                extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
                extraCodigo.setFilterStyle("display: none; visibility: hidden;");
                extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
                extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
                extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
                extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
                extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
                extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
                extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
                extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
                extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
                extraTurno.setFilterStyle("display: none; visibility: hidden;");
                extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
                extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
                bandera = 0;
                filtrarListExtrasRecargos = null;
                tipoLista = 0;
            }
        }
        if (indexDER >= 0) {
            if (banderaDER == 0) {
                altoTablaDetalle = "113";
                FacesContext c = FacesContext.getCurrentInstance();
                detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
                detalleHF.setFilterStyle("width: 85% !important;");
                detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
                detalleHI.setFilterStyle("width: 85% !important;");
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
                detalleConcepto.setFilterStyle("width: 85% !important;");
                detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
                detalleEmpresa.setFilterStyle("width: 85% !important;");
                detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
                detalleDia.setFilterStyle("width: 85% !important;");
                detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
                detalleAdicion.setFilterStyle("width: 85% !important;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
                detalleMinutos.setFilterStyle("width: 85% !important;");
                detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
                detallePagoAdicion.setFilterStyle("width: 85% !important;");
                detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
                detalleIniFin.setFilterStyle("width: 85% !important;");
                detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
                detalleGarantiza.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                banderaDER = 1;
            } else {
                altoTablaDetalle = "135";
                FacesContext c = FacesContext.getCurrentInstance();
                detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
                detalleHF.setFilterStyle("display: none; visibility: hidden;");
                detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
                detalleHI.setFilterStyle("display: none; visibility: hidden;");
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
                detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
                detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
                detalleDia.setFilterStyle("display: none; visibility: hidden;");
                detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
                detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
                detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
                detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
                detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
                detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
                detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
                banderaDER = 0;
                filtrarListDetallesExtrasRecargos = null;
                tipoListaDER = 0;
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            altoTablaRecargo = "135";
            FacesContext c = FacesContext.getCurrentInstance();
            extraCodigo = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraCodigo");
            extraCodigo.setFilterStyle("display: none; visibility: hidden;");
            extraDescripcion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraDescripcion");
            extraDescripcion.setFilterStyle("display: none; visibility: hidden;");
            extraTipoDia = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoDia");
            extraTipoDia.setFilterStyle("display: none; visibility: hidden;");
            extraTipoJornada = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoJornada");
            extraTipoJornada.setFilterStyle("display: none; visibility: hidden;");
            extraTipoLegislacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTipoLegislacion");
            extraTipoLegislacion.setFilterStyle("display: none; visibility: hidden;");
            extraTurno = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraTurno");
            extraTurno.setFilterStyle("display: none; visibility: hidden;");
            extraAprobacion = (Column) c.getViewRoot().findComponent("form:datosExtraRecargo:extraAprobacion");
            extraAprobacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
            bandera = 0;
            filtrarListExtrasRecargos = null;
            tipoLista = 0;
        }

        if (banderaDER == 1) {
            altoTablaDetalle = "135";
            FacesContext c = FacesContext.getCurrentInstance();
            detalleHF = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHF");
            detalleHF.setFilterStyle("display: none; visibility: hidden;");
            detalleHI = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleHI");
            detalleHI.setFilterStyle("display: none; visibility: hidden;");
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleEmpresa");
            detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
            detalleDia = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleDia");
            detalleDia.setFilterStyle("display: none; visibility: hidden;");
            detalleAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleAdicion");
            detalleAdicion.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detallePagoAdicion = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detallePagoAdicion");
            detallePagoAdicion.setFilterStyle("display: none; visibility: hidden;");
            detalleIniFin = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleIniFin");
            detalleIniFin.setFilterStyle("display: none; visibility: hidden;");
            detalleGarantiza = (Column) c.getViewRoot().findComponent("form:datosDetalleExtraRecargo:detalleGarantiza");
            detalleGarantiza.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
            banderaDER = 0;
            filtrarListDetallesExtrasRecargos = null;
            tipoListaDER = 0;
        }

        listExtraRecargoBorrar.clear();
        listExtraRecargoCrear.clear();
        listExtraRecargoModificar.clear();
        listDetalleExtraRecargoBorrar.clear();
        listDetalleExtraRecargoCrear.clear();
        listDetalleExtraRecargoModificar.clear();
        index = -1;
        indexAux = -1;
        indexDER = -1;
        secRegistroDER = null;
        secRegistro = null;
        k = 0;
        listExtrasRecargos = null;
        listDetallesExtrasRecargos = null;
        guardado = true;
        guardadoDER = true;
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("form:TipoJornadaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("form:LegislacionDialogo");
                RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').show()");
                tipoActualizacion = 0;
            }
        }

        if (indexDER >= 0) {
            if (cualCeldaDER == 2) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaDER == 3) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void asignarIndex(Integer indice, int dlg, int LND, int tabla) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tabla == 0) {
            if (LND == 0) {
                index = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
            } else if (dlg == 1) {
                RequestContext.getCurrentInstance().update("form:TipoJornadaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').show()");
            } else if (dlg == 2) {
                RequestContext.getCurrentInstance().update("form:LegislacionDialogo");
                RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').show()");
            }
        }
        if (tabla == 1) {
            if (LND == 0) {
                indexDER = indice;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            } else if (dlg == 1) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            }
        }
    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo, int tabla) {
        if (tabla == 0) {
            if (Campo.equals("DIAS")) {
                if (tipoNuevo == 1) {
                    tipoDia = nuevoExtraRecargo.getTipodia().getDescripcion();
                } else if (tipoNuevo == 2) {
                    tipoDia = duplicarExtraRecargo.getTipodia().getDescripcion();
                }
            } else if (Campo.equals("JORNADA")) {
                if (tipoNuevo == 1) {
                    tipoJornada = nuevoExtraRecargo.getTipojornada().getDescripcion();
                } else if (tipoNuevo == 2) {
                    tipoJornada = duplicarExtraRecargo.getTipojornada().getDescripcion();
                }
            } else if (Campo.equals("LEGISLACION")) {
                if (tipoNuevo == 1) {
                    legislacion = nuevoExtraRecargo.getTipolegislacion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    legislacion = duplicarExtraRecargo.getTipolegislacion().getDescripcion();
                }
            }
        }
        if (tabla == 1) {
            if (Campo.equals("CONCEPTO")) {
                if (tipoNuevo == 1) {
                    concepto = nuevoDetalleExtraRecargo.getConcepto().getDescripcion();
                } else if (tipoNuevo == 2) {
                    concepto = duplicarDetalleExtraRecargo.getConcepto().getDescripcion();
                }
            } else if (Campo.equals("EMPRESA")) {
                if (tipoNuevo == 1) {
                    empresa = nuevoDetalleExtraRecargo.getConcepto().getEmpresa().getNombre();
                } else if (tipoNuevo == 2) {
                    empresa = duplicarDetalleExtraRecargo.getConcepto().getEmpresa().getNombre();
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoExtraRecargo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("DIAS")) {
            if (tipoNuevo == 1) {
                nuevoExtraRecargo.getTipodia().setDescripcion(tipoDia);
            } else if (tipoNuevo == 2) {
                duplicarExtraRecargo.getTipodia().setDescripcion(tipoDia);
            }
            for (int i = 0; i < lovTiposDias.size(); i++) {
                if (lovTiposDias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoExtraRecargo.setTipodia(lovTiposDias.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
                } else if (tipoNuevo == 2) {
                    duplicarExtraRecargo.setTipodia(lovTiposDias.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
                }
                lovTiposDias.clear();
                getLovTiposDias();
            } else {
                RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("JORNADA")) {
            if (tipoNuevo == 1) {
                nuevoExtraRecargo.getTipojornada().setDescripcion(tipoJornada);
            } else if (tipoNuevo == 2) {
                duplicarExtraRecargo.getTipojornada().setDescripcion(tipoJornada);
            }
            for (int i = 0; i < lovTiposJornadas.size(); i++) {
                if (lovTiposJornadas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoExtraRecargo.setTipojornada(lovTiposJornadas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    duplicarExtraRecargo.setTipojornada(lovTiposJornadas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                }
                lovTiposJornadas.clear();
                getLovTiposJornadas();
            } else {
                RequestContext.getCurrentInstance().update("form:TipoJornadaDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("LEGISLACION")) {
            if (tipoNuevo == 1) {
                nuevoExtraRecargo.getTipolegislacion().setDescripcion(tipoJornada);
            } else if (tipoNuevo == 2) {
                duplicarExtraRecargo.getTipolegislacion().setDescripcion(tipoJornada);
            }
            for (int i = 0; i < lovContratos.size(); i++) {
                if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoExtraRecargo.setTipolegislacion(lovContratos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    duplicarExtraRecargo.setTipolegislacion(lovContratos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                }
                lovContratos.clear();
                getLovContratos();
            } else {
                RequestContext.getCurrentInstance().update("form:LegislacionDialogo");
                RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicadoDetalleExtraRecargo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoDetalleExtraRecargo.getConcepto().setDescripcion(concepto);
                } else if (tipoNuevo == 2) {
                    duplicarDetalleExtraRecargo.getConcepto().setDescripcion(concepto);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoDetalleExtraRecargo.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                    } else if (tipoNuevo == 2) {
                        duplicarDetalleExtraRecargo.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                    }
                    lovConceptos.clear();;
                    getLovConceptos();
                } else {
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                    }
                }
            } else {
                lovConceptos.clear();
                getLovConceptos();
                if (tipoNuevo == 1) {
                    nuevoDetalleExtraRecargo.setConcepto(new Conceptos());
                    nuevoDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarDetalleExtraRecargo.setConcepto(new Conceptos());
                    duplicarDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoDetalleExtraRecargo.getConcepto().getEmpresa().setNombre(empresa);
                } else if (tipoNuevo == 2) {
                    duplicarDetalleExtraRecargo.getConcepto().getEmpresa().setNombre(empresa);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoDetalleExtraRecargo.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                    } else if (tipoNuevo == 2) {
                        duplicarDetalleExtraRecargo.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                    }
                    lovConceptos.clear();
                    getLovConceptos();
                } else {
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                    }
                }
            } else {
                lovConceptos.clear();
                getLovConceptos();
                if (tipoNuevo == 1) {
                    nuevoDetalleExtraRecargo.setConcepto(new Conceptos());
                    nuevoDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarDetalleExtraRecargo.setConcepto(new Conceptos());
                    duplicarDetalleExtraRecargo.getConcepto().setEmpresa(new Empresas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
                }
            }
        }
    }

    public void actualizarTipoDia() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(index).setTipodia(tipoDiaSeleccionado);
                if (!listExtraRecargoCrear.contains(listExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(listExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    }
                }
            } else {
                filtrarListExtrasRecargos.get(index).setTipodia(tipoDiaSeleccionado);
                if (!listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
        } else if (tipoActualizacion == 1) {
            nuevoExtraRecargo.setTipodia(tipoDiaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraTipoDia");
        } else if (tipoActualizacion == 2) {
            duplicarExtraRecargo.setTipodia(tipoDiaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraTipoDia");
        }
        filtrarLovTiposDias = null;
        tipoDiaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:TipoDiaDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoDia");
         RequestContext.getCurrentInstance().update("form:aceptarTD");*/
        context.reset("form:lovTipoDia:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDia').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').hide()");
    }

    public void cancelarCambioTipoDia() {
        filtrarLovTiposDias = null;
        tipoDiaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoDia:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDia').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoDiaDialogo').hide()");
    }

    public void actualizarTipoJornada() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(index).setTipojornada(tipoJornadaSeleccionada);
                if (!listExtraRecargoCrear.contains(listExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(listExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    }
                }
            } else {
                filtrarListExtrasRecargos.get(index).setTipojornada(tipoJornadaSeleccionada);
                if (!listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
        } else if (tipoActualizacion == 1) {
            nuevoExtraRecargo.setTipojornada(tipoJornadaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraTipoJornada");
        } else if (tipoActualizacion == 2) {
            duplicarExtraRecargo.setTipojornada(tipoJornadaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraTipoJornada");
        }
        filtrarLovTiposJornadaS = null;
        tipoJornadaSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:TipoJornadaDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoJornada");
         RequestContext.getCurrentInstance().update("form:aceptarTJ");*/
        context.reset("form:lovTipoJornada:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoJornada').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').hide()");
    }

    public void cancelarCambioTipoJornada() {
        filtrarLovTiposJornadaS = null;
        tipoJornadaSeleccionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoJornada:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoJornada').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoJornadaDialogo').hide()");
    }

    public void actualizarLegislacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listExtrasRecargos.get(index).setTipolegislacion(contratoSeleccionado);
                if (!listExtraRecargoCrear.contains(listExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(listExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(listExtrasRecargos.get(index));
                    }
                }
            } else {
                filtrarListExtrasRecargos.get(index).setTipolegislacion(contratoSeleccionado);
                if (!listExtraRecargoCrear.contains(filtrarListExtrasRecargos.get(index))) {
                    if (listExtraRecargoModificar.isEmpty()) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    } else if (!listExtraRecargoModificar.contains(filtrarListExtrasRecargos.get(index))) {
                        listExtraRecargoModificar.add(filtrarListExtrasRecargos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosExtraRecargo");
        } else if (tipoActualizacion == 1) {
            nuevoExtraRecargo.setTipolegislacion(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraLegislacion");
        } else if (tipoActualizacion == 2) {
            duplicarExtraRecargo.setTipolegislacion(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraLegislacion");
        }
        filtrarLovContratos = null;
        contratoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:LegislacionDialogo");
         RequestContext.getCurrentInstance().update("form:lovLegislacion");
         RequestContext.getCurrentInstance().update("form:aceptarLeg");*/
        context.reset("form:lovLegislacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLegislacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').hide()");
    }

    public void cancelarCambioLegislacion() {
        filtrarLovContratos = null;
        contratoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovLegislacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLegislacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LegislacionDialogo').hide()");
    }

    public void actualizarConcepto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaDER == 0) {
                listDetallesExtrasRecargos.get(indexDER).setConcepto(conceptoSeleccionado);
                if (!listDetalleExtraRecargoCrear.contains(listDetallesExtrasRecargos.get(indexDER))) {
                    if (listDetalleExtraRecargoModificar.isEmpty()) {
                        listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indexDER));
                    } else if (!listDetalleExtraRecargoModificar.contains(listDetallesExtrasRecargos.get(indexDER))) {
                        listDetalleExtraRecargoModificar.add(listDetallesExtrasRecargos.get(indexDER));
                    }
                }
            } else {
                filtrarListDetallesExtrasRecargos.get(indexDER).setConcepto(conceptoSeleccionado);
                if (!listDetalleExtraRecargoCrear.contains(filtrarListDetallesExtrasRecargos.get(indexDER))) {
                    if (listDetalleExtraRecargoModificar.isEmpty()) {
                        listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indexDER));
                    } else if (!listDetalleExtraRecargoModificar.contains(filtrarListDetallesExtrasRecargos.get(indexDER))) {
                        listDetalleExtraRecargoModificar.add(filtrarListDetallesExtrasRecargos.get(indexDER));
                    }
                }
            }
            if (guardadoDER == true) {
                guardadoDER = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndexDER = true;
            RequestContext.getCurrentInstance().update("form:datosDetalleExtraRecargo");
        } else if (tipoActualizacion == 1) {
            nuevoDetalleExtraRecargo.setConcepto(conceptoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalleConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoExtraEmpresa");
        } else if (tipoActualizacion == 2) {
            duplicarDetalleExtraRecargo.setConcepto(conceptoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarExtraEmpresa");
        }
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
        RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
        RequestContext.getCurrentInstance().update("form:lovConcepto");
        RequestContext.getCurrentInstance().update("form:aceptarCon");*/
        context.reset("form:lovConcepto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
    }

    public void cancelarCambioConcepto() {
        filtrarLovConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovConcepto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
    }

    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        if (index >= 0) {
            nombreTabla = ":formExportarExtra:datosExtraRecargoExportar";
            nombreXML = "ExtraRecargo_XML";
        }
        if (indexDER >= 0) {
            nombreTabla = ":formExportarDER:datosDetalleExtraRecargoExportar";
            nombreXML = "DetallesExtraRecargo_XML";
        }
        return nombreTabla;
    }

    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDF_ER();
        }
        if (indexDER >= 0) {
            exportPDF_DER();
        }
    }

    public void exportPDF_ER() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarExtra:datosExtraRecargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ExtraRecargo_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportPDF_DER() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDER:datosDetalleExtraRecargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "DetallesExtraRecargo_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void validarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLS_ER();
        }
        if (indexDER >= 0) {
            exportXLS_DER();
        }
    }

    public void exportXLS_ER() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarExtra:datosExtraRecargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ExtraRecargo_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS_DER() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDER:datosDetalleExtraRecargoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "DetallesExtraRecargo_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void eventoFiltrar() {
        if (index >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
        if (indexDER >= 0) {
            if (tipoListaDER == 0) {
                tipoListaDER = 1;
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        if (listDetallesExtrasRecargos == null || listExtrasRecargos == null) {
        } else {
            if (index >= 0) {
                verificarRastroExtraRecargo();
                index = -1;
            }
            if (indexDER >= 0) {
                verificarRastroDetalleExtraRecargo();
                indexDER = -1;
            }
        }
    }

    public void verificarRastroExtraRecargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listExtrasRecargos != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "EXTRASRECARGOS");
                backUpSecRegistro = secRegistro;
                backUp = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "ExtrasRecargos";
                    msnConfirmarRastro = "La tabla EXTRASRECARGOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("EXTRASRECARGOS")) {
                nombreTablaRastro = "ExtrasRecargos";
                msnConfirmarRastroHistorico = "La tabla EXTRASRECARGOS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    public void verificarRastroDetalleExtraRecargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listDetallesExtrasRecargos != null) {
            if (secRegistroDER != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroDER, "DETALLESEXTRASRECARGOS");
                backUpSecRegistroDER = secRegistroDER;
                backUp = secRegistroDER;
                secRegistroDER = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "DetallesExtrasRecargos";
                    msnConfirmarRastro = "La tabla DETALLESEXTRASRECARGOS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("DETALLESEXTRASRECARGOS")) {
                nombreTablaRastro = "DetallesExtrasRecargos";
                msnConfirmarRastroHistorico = "La tabla DETALLESEXTRASRECARGOS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
    }

    //GETTERS AND SETTERS
    public List<ExtrasRecargos> getListExtrasRecargos() {
        try {
            if (listExtrasRecargos == null) {
                listExtrasRecargos = new ArrayList<ExtrasRecargos>();
                listExtrasRecargos = administrarATExtraRecargo.consultarExtrasRecargos();
            }
            return listExtrasRecargos;

        } catch (Exception e) {
            System.out.println("Error...!! getListExtrasRecargos " + e.toString());
            return null;
        }
    }

    public void setListExtrasRecargos(List<ExtrasRecargos> setListExtrasRecargos) {
        this.listExtrasRecargos = setListExtrasRecargos;
    }

    public List<ExtrasRecargos> getFiltrarListExtrasRecargos() {
        return filtrarListExtrasRecargos;
    }

    public void setFiltrarListExtrasRecargos(List<ExtrasRecargos> setFiltrarListExtrasRecargos) {
        this.filtrarListExtrasRecargos = setFiltrarListExtrasRecargos;
    }

    public ExtrasRecargos getNuevoExtraRecargo() {
        return nuevoExtraRecargo;
    }

    public void setNuevoExtraRecargo(ExtrasRecargos setNuevoExtraRecargo) {
        this.nuevoExtraRecargo = setNuevoExtraRecargo;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public ExtrasRecargos getEditarExtraRecargo() {
        return editarExtraRecargo;
    }

    public void setEditarExtraRecargo(ExtrasRecargos setEditarExtraRecargo) {
        this.editarExtraRecargo = setEditarExtraRecargo;
    }

    public ExtrasRecargos getDuplicarExtraRecargo() {
        return duplicarExtraRecargo;
    }

    public void setDuplicarExtraRecargo(ExtrasRecargos setDuplicarExtraRecargo) {
        this.duplicarExtraRecargo = setDuplicarExtraRecargo;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
        this.backUpSecRegistro = BackUpSecRegistro;
    }

    public List<DetallesExtrasRecargos> getListDetallesExtrasRecargos() {
        try {
            if (listDetallesExtrasRecargos == null) {
                listDetallesExtrasRecargos = new ArrayList<DetallesExtrasRecargos>();
                if (index >= 0) {
                    if (tipoLista == 0) {
                        listDetallesExtrasRecargos = administrarATExtraRecargo.consultarDetallesExtrasRecargos(listExtrasRecargos.get(index).getSecuencia());
                    }
                    if (tipoLista == 1) {
                        listDetallesExtrasRecargos = administrarATExtraRecargo.consultarDetallesExtrasRecargos(filtrarListExtrasRecargos.get(index).getSecuencia());
                    }
                }
            }
            return listDetallesExtrasRecargos;
        } catch (Exception e) {
            System.out.println("Error getListDetallesExtrasRecargos : " + e.toString());
            return null;
        }
    }

    public void setListDetallesExtrasRecargos(List<DetallesExtrasRecargos> setListDetallesExtrasRecargos) {
        this.listDetallesExtrasRecargos = setListDetallesExtrasRecargos;
    }

    public List<DetallesExtrasRecargos> getFiltrarListDetallesExtrasRecargos() {
        return filtrarListDetallesExtrasRecargos;
    }

    public void setFiltrarListDetallesExtrasRecargos(List<DetallesExtrasRecargos> setFiltrarListDetallesExtrasRecargos) {
        this.filtrarListDetallesExtrasRecargos = setFiltrarListDetallesExtrasRecargos;
    }

    public List<ExtrasRecargos> getListExtraRecargoModificar() {
        return listExtraRecargoModificar;
    }

    public void setListExtraRecargoModificar(List<ExtrasRecargos> setListExtraRecargoModificar) {
        this.listExtraRecargoModificar = setListExtraRecargoModificar;
    }

    public List<ExtrasRecargos> getListExtraRecargoCrear() {
        return listExtraRecargoCrear;
    }

    public void setListExtraRecargoCrear(List<ExtrasRecargos> setListExtraRecargoCrear) {
        this.listExtraRecargoCrear = setListExtraRecargoCrear;
    }

    public List<ExtrasRecargos> getListExtraRecargoBorrar() {
        return listExtraRecargoBorrar;
    }

    public void setListExtraRecargoBorrar(List<ExtrasRecargos> setListExtraRecargoBorrar) {
        this.listExtraRecargoBorrar = setListExtraRecargoBorrar;
    }

    public List<DetallesExtrasRecargos> getListDetalleExtraRecargoModificar() {
        return listDetalleExtraRecargoModificar;
    }

    public void setListDetalleExtraRecargoModificar(List<DetallesExtrasRecargos> setListDetalleExtraRecargoModificar) {
        this.listDetalleExtraRecargoModificar = setListDetalleExtraRecargoModificar;
    }

    public DetallesExtrasRecargos getNuevoDetalleExtraRecargo() {
        return nuevoDetalleExtraRecargo;
    }

    public void setNuevoDetalleExtraRecargo(DetallesExtrasRecargos setNuevoDetalleExtraRecargo) {
        this.nuevoDetalleExtraRecargo = setNuevoDetalleExtraRecargo;
    }

    public List<DetallesExtrasRecargos> getListDetalleExtraRecargoCrear() {
        return listDetalleExtraRecargoCrear;
    }

    public void setListDetalleExtraRecargoCrear(List<DetallesExtrasRecargos> setListDetalleExtraRecargoCrear) {
        this.listDetalleExtraRecargoCrear = setListDetalleExtraRecargoCrear;
    }

    public List<DetallesExtrasRecargos> getListDetalleExtraRecargoBorrar() {
        return listDetalleExtraRecargoBorrar;
    }

    public void setListDetalleExtraRecargoBorrar(List<DetallesExtrasRecargos> setListDetalleExtraRecargoBorrar) {
        this.listDetalleExtraRecargoBorrar = setListDetalleExtraRecargoBorrar;
    }

    public DetallesExtrasRecargos getEditarDetalleExtraRecargo() {
        return editarDetalleExtraRecargo;
    }

    public void setEditarDetalleExtraRecargo(DetallesExtrasRecargos setEditarDetalleExtraRecargo) {
        this.editarDetalleExtraRecargo = setEditarDetalleExtraRecargo;
    }

    public DetallesExtrasRecargos getDuplicarDetalleExtraRecargo() {
        return duplicarDetalleExtraRecargo;
    }

    public void setDuplicarDetalleExtraRecargo(DetallesExtrasRecargos setDuplicarDetalleExtraRecargo) {
        this.duplicarDetalleExtraRecargo = setDuplicarDetalleExtraRecargo;
    }

    public BigInteger getSecRegistroDER() {
        return secRegistroDER;
    }

    public void setSecRegistroDER(BigInteger setSecRegistroDER) {
        this.secRegistroDER = setSecRegistroDER;
    }

    public BigInteger getBackUpSecRegistroDER() {
        return backUpSecRegistroDER;
    }

    public void setBackUpSecRegistroDER(BigInteger setBackUpSecRegistroDER) {
        this.backUpSecRegistroDER = setBackUpSecRegistroDER;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public List<TiposDias> getLovTiposDias() {
        lovTiposDias = administrarATExtraRecargo.consultarLOVListaTiposDias();
        return lovTiposDias;
    }

    public void setLovTiposDias(List<TiposDias> lovTiposDias) {
        this.lovTiposDias = lovTiposDias;
    }

    public List<TiposDias> getFiltrarLovTiposDias() {
        return filtrarLovTiposDias;
    }

    public void setFiltrarLovTiposDias(List<TiposDias> filtrarLovTiposDias) {
        this.filtrarLovTiposDias = filtrarLovTiposDias;
    }

    public TiposDias getTipoDiaSeleccionado() {
        return tipoDiaSeleccionado;
    }

    public void setTipoDiaSeleccionado(TiposDias tipoDiaSeleccionado) {
        this.tipoDiaSeleccionado = tipoDiaSeleccionado;
    }

    public List<TiposJornadas> getLovTiposJornadas() {
        lovTiposJornadas = administrarATExtraRecargo.consultarLOVTiposJornadas();
        return lovTiposJornadas;
    }

    public void setLovTiposJornadas(List<TiposJornadas> lovTiposJornadas) {
        this.lovTiposJornadas = lovTiposJornadas;
    }

    public List<TiposJornadas> getFiltrarLovTiposJornadaS() {
        return filtrarLovTiposJornadaS;
    }

    public void setFiltrarLovTiposJornadaS(List<TiposJornadas> filtrarLovTiposJornadaS) {
        this.filtrarLovTiposJornadaS = filtrarLovTiposJornadaS;
    }

    public TiposJornadas getTipoJornadaSeleccionada() {
        return tipoJornadaSeleccionada;
    }

    public void setTipoJornadaSeleccionada(TiposJornadas tipoJornadaSeleccionada) {
        this.tipoJornadaSeleccionada = tipoJornadaSeleccionada;
    }

    public List<Contratos> getLovContratos() {
        lovContratos = administrarATExtraRecargo.consultarLOVContratos();
        return lovContratos;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public List<Contratos> getFiltrarLovContratos() {
        return filtrarLovContratos;
    }

    public void setFiltrarLovContratos(List<Contratos> filtrarLovContratos) {
        this.filtrarLovContratos = filtrarLovContratos;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }

    public List<Conceptos> getLovConceptos() {
        lovConceptos = administrarATExtraRecargo.consultarLOVConceptos();
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> lovConceptos) {
        this.lovConceptos = lovConceptos;
    }

    public List<Conceptos> getFiltrarLovConceptos() {
        return filtrarLovConceptos;
    }

    public void setFiltrarLovConceptos(List<Conceptos> filtrarLovConceptos) {
        this.filtrarLovConceptos = filtrarLovConceptos;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public ExtrasRecargos getExtraRecargoTablaSeleccionado() {
        getListExtrasRecargos();
        if (listExtrasRecargos != null) {
            int tam = listExtrasRecargos.size();
            if (tam > 0) {
                extraRecargoTablaSeleccionado = listExtrasRecargos.get(0);
            }
        }
        return extraRecargoTablaSeleccionado;
    }

    public void setExtraRecargoTablaSeleccionado(ExtrasRecargos extraRecargoTablaSeleccionado) {
        this.extraRecargoTablaSeleccionado = extraRecargoTablaSeleccionado;
    }

    public DetallesExtrasRecargos getDetalleTablaSeleccionado() {
        getListDetallesExtrasRecargos();
        if (listDetallesExtrasRecargos != null) {
            int tam = listDetallesExtrasRecargos.size();
            if (tam > 0) {
                detalleTablaSeleccionado = listDetallesExtrasRecargos.get(0);
            }
        }
        return detalleTablaSeleccionado;
    }

    public void setDetalleTablaSeleccionado(DetallesExtrasRecargos detalleTablaSeleccionado) {
        this.detalleTablaSeleccionado = detalleTablaSeleccionado;
    }

    public String getAltoTablaRecargo() {
        return altoTablaRecargo;
    }

    public void setAltoTablaRecargo(String altoTablaRecargo) {
        this.altoTablaRecargo = altoTablaRecargo;
    }

    public String getAltoTablaDetalle() {
        return altoTablaDetalle;
    }

    public void setAltoTablaDetalle(String altoTablaDetalle) {
        this.altoTablaDetalle = altoTablaDetalle;
    }

    public String getInfoRegistroTipoDia() {
        getLovTiposDias();
        if (lovTiposDias != null) {
            infoRegistroTipoDia = "Cantidad de registros : " + lovTiposDias.size();
        } else {
            infoRegistroTipoDia = "Cantidad de registros : 0";
        }
        return infoRegistroTipoDia;
    }

    public void setInfoRegistroTipoDia(String infoRegistroTipoDia) {
        this.infoRegistroTipoDia = infoRegistroTipoDia;
    }

    public String getInfoRegistroTipoJornada() {
        getLovTiposJornadas();
        if (lovTiposJornadas != null) {
            infoRegistroTipoJornada = "Cantidad de registros : " + lovTiposJornadas.size();
        } else {
            infoRegistroTipoJornada = "Cantidad de registros : 0";
        }
        return infoRegistroTipoJornada;
    }

    public void setInfoRegistroTipoJornada(String infoRegistroTipoJornada) {
        this.infoRegistroTipoJornada = infoRegistroTipoJornada;
    }

    public String getInfoRegistroLegislacion() {
        getLovContratos();
        if (lovContratos != null) {
            infoRegistroLegislacion = "Cantidad de registros : " + lovContratos.size();
        } else {
            infoRegistroLegislacion = "Cantidad de registros : 0";
        }
        return infoRegistroLegislacion;
    }

    public void setInfoRegistroLegislacion(String infoRegistroLegislacion) {
        this.infoRegistroLegislacion = infoRegistroLegislacion;
    }

    public String getInfoRegistroConcepto() {
        getLovConceptos();
        if (lovConceptos != null) {
            infoRegistroConcepto = "Cantidad de registros : " + lovConceptos.size();
        } else {
            infoRegistroConcepto = "Cantidad de registros : 0";
        }
        return infoRegistroConcepto;
    }

    public void setInfoRegistroConcepto(String infoRegistroConcepto) {
        this.infoRegistroConcepto = infoRegistroConcepto;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

}
