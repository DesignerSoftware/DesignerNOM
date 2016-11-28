package Controlador;

import Entidades.Cargos;
import Entidades.Competenciascargos;
import Entidades.DetallesCargos;
import Entidades.Empresas;
import Entidades.Enfoques;
import Entidades.EvalCompetencias;
import Entidades.GruposSalariales;
import Entidades.GruposViaticos;
import Entidades.ProcesosProductivos;
import Entidades.SueldosMercados;
import Entidades.TiposDetalles;
import Entidades.TiposEmpresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCargosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlCargo implements Serializable {

   @EJB
   AdministrarCargosInterface administrarCargos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<Cargos> listaCargos;
   private List<Cargos> filtrarListaCargos;
   //
   private List<SueldosMercados> listaSueldosMercados;
   private List<SueldosMercados> filtrarListaSueldosMercados;
   //
   private List<Competenciascargos> listaCompetenciasCargos;
   private List<Competenciascargos> filtrarListaCompetenciasCargos;
   //
   private List<TiposDetalles> listaTiposDetalles;
   private List<TiposDetalles> filtrarListaTiposDetalles;
   //Activo/Desactivo Crtl + F11
   private int bandera, banderaSueldoMercado, banderaCompetencia, banderaTipoDetalle;
   //Columnas Tabla 
   private Column cargoCodigo, cargoNombre, cargoGrupoSalarial, cargoSalario, cargoSueldoMinimo, cargoSueldoMaximo, cargoGrupoViatico, cargoCargoRotativo, cargoJefe, cargoProcesoProductivo, cargoCodigoAlternativo;
   private Column sueldoMercadoTipoEmpresa, sueldoMercadoSueldoMinimo, sueldoMercadoSueldoMaximo;
   private Column competenciaCargoDescripcion;
   private Column tipoDetalleCodigo, tipoDetalleDescripcion, tipoDetalleEnfoque;
   //Otros
   private boolean aceptar;
   private int banderaDetalleCargo;
   //modificar
   private List<Cargos> listCargosModificar;
   private List<SueldosMercados> listSueldosMercadosModificar;
   private List<Competenciascargos> listCompetenciasCargosModificar;
   private List<TiposDetalles> listTiposDetallesModificar;
   private boolean guardado, guardadoSueldoMercado, guardadoCompetencia, guardadoTipoDetalle, guardadoDetalleCargo, borrarDetalleCargo;
   //crear 
   private Cargos nuevoCargo;
   private SueldosMercados nuevoSueldoMercado;
   private Competenciascargos nuevoCompetenciaCargo;
   private TiposDetalles nuevoTipoDetalle;
   private List<Cargos> listCargosCrear;
   private List<SueldosMercados> listSueldosMercadosCrear;
   private List<Competenciascargos> listCompetenciasCargosCrear;
   private List<TiposDetalles> listTiposDetallesCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<Cargos> listCargosBorrar;
   private List<SueldosMercados> listSueldosMercadosBorrar;
   private List<Competenciascargos> listCompetenciasCargosBorrar;
   private List<TiposDetalles> listTiposDetallesBorrar;
   //editar celda
   private Cargos editarCargo;
   private SueldosMercados editarSueldoMercado;
   private Competenciascargos editarCompetenciaCargo;
   private TiposDetalles editarTipoDetalle;
   private int cualCelda, tipoLista, cualCeldaSueldoMercado, tipoListaSueldoMercado, cualCeldaCompetencia, tipoListaCompetencia, cualCeldaTipoDetalle, tipoListaTipoDetalle;
   //duplicar
   private Cargos duplicarCargo;
   private SueldosMercados duplicarSueldoMercado;
   private Competenciascargos duplicarCompetenciaCargo;
   private TiposDetalles duplicarTipoDetalle;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   //private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String grupoSalarial, grupoViatico, procesoProductivo, tipoEmpresa, evalCompetencia, enfoque;
   ///////////LOV///////////
   private List<GruposSalariales> lovGruposSalariales;
   private List<GruposSalariales> filtrarLovGruposSalariales;
   private GruposSalariales grupoSalarialSeleccionado;
   private List<GruposViaticos> lovGruposViaticos;
   private List<GruposViaticos> filtrarLovGruposViaticos;
   private GruposViaticos grupoViaticoSeleccionado;
   private List<ProcesosProductivos> lovProcesosProductivos;
   private List<ProcesosProductivos> filtrarLovProcesosProductivos;
   private ProcesosProductivos procesoProductivoSeleccionado;
   private List<TiposEmpresas> lovTiposEmpresas;
   private List<TiposEmpresas> filtrarLovTiposEmpresas;
   private TiposEmpresas tipoEmpresaSeleccionado;
   private List<EvalCompetencias> lovEvalCompetencias;
   private List<EvalCompetencias> filtrarLovEvalCompetencias;
   private EvalCompetencias evalCompetenciaSeleccionado;
   private List<Enfoques> lovEnfoques;
   private List<Enfoques> filtrarLovEnfoques;
   private Enfoques enfoqueSeleccionado;
   private boolean permitirIndexSueldoMercado, permitirCompetencia, permitirIndexTipoDetalle, permitirIndexCargo;
   private int tipoActualizacion;
   private Short auxCodigoCargo;
   private String auxNombreCargo;
   private BigInteger auxCodigoTipoDetalle;
   private String auxDescriptionTipoDetalle;
   //
   private boolean cambiosPagina;
   private String altoTablaCargo, altoTablaSueldoMercado, altoTablaCompetencia, altoTablaTipoDetalle;
   //
   private String paginaAnterior;
   //
   private boolean activoSueldoMercado, activoCompetencia, activoTipoDetalle;
   //
   private Empresas empresaActual;
   private Empresas empresaSeleccionada;
   private List<Empresas> lovEmpresas;
   private List<Empresas> filtrarLovEmpresas;
   //
   private String legendDetalleCargo;
   //
   private DetallesCargos detalleCargo;
   //
   private boolean activoDetalleCargo;
   //
   private List<Cargos> lovCargos;
   private List<Cargos> filtrarLovCargos;
   private Cargos cargoSeleccionado;
   //
   private BigInteger backUp;
   private Cargos cargoTablaSeleccionado;
   private SueldosMercados sueldoMercadoSeleccionado;
   private Competenciascargos competenciaCargoSeleccionado;
   private TiposDetalles tipoDetalleSeleccionado;
   //
   private String tablaActiva;
   //
   private boolean activarLOV;
   private String infoRegistroC, infoRegistroSM, infoRegistroCC, infoRegistroTD;
   private String infoRegistroEnfoque, infoRegistroEvalCom, infoRegistroCargos, infoRegistroGrupoS, infoRegistroGrupoV, infoRegistroProcesoP, infoRegistroTipoEmpresa, infoRegistroEmpresa;

   public ControlCargo() {
      cargoTablaSeleccionado = new Cargos();
      sueldoMercadoSeleccionado = new SueldosMercados();
      competenciaCargoSeleccionado = new Competenciascargos();
      tipoDetalleSeleccionado = new TiposDetalles();
      lovCargos = null;
      cargoSeleccionado = null;
      activoDetalleCargo = true;
      detalleCargo = null;
      legendDetalleCargo = "";
      empresaSeleccionada = null;
      empresaActual = null;
      lovEmpresas = null;
      activoSueldoMercado = true;
      activoCompetencia = true;
      activoTipoDetalle = true;
      paginaAnterior = "";
      //altos tablas
      altoTablaCargo = "95";
      altoTablaSueldoMercado = "68";
      altoTablaCompetencia = "68";
      altoTablaTipoDetalle = "68";
      //Permitir index
      permitirIndexSueldoMercado = true;
      permitirIndexTipoDetalle = true;
      permitirCompetencia = true;
      permitirIndexCargo = true;
      //lovs
      lovGruposSalariales = null;
      grupoSalarialSeleccionado = null;
      lovGruposViaticos = null;
      grupoViaticoSeleccionado = null;
      lovProcesosProductivos = null;
      procesoProductivoSeleccionado = null;
      lovTiposEmpresas = null;
      tipoEmpresaSeleccionado = null;
      lovEnfoques = null;
      enfoqueSeleccionado = null;
      lovEvalCompetencias = null;
      evalCompetenciaSeleccionado = null;
      //listas de tablas
      listaCargos = null;
      listaSueldosMercados = null;
      listaCompetenciasCargos = null;
      listaTiposDetalles = null;
      //Otros
      aceptar = true;
      cambiosPagina = true;
      tipoActualizacion = -1;
      k = 0;
      //borrar 
      listCargosBorrar = new ArrayList<Cargos>();
      listSueldosMercadosBorrar = new ArrayList<SueldosMercados>();
      listCompetenciasCargosBorrar = new ArrayList<Competenciascargos>();
      listTiposDetallesBorrar = new ArrayList<TiposDetalles>();
      //crear 
      listCargosCrear = new ArrayList<Cargos>();
      listSueldosMercadosCrear = new ArrayList<SueldosMercados>();
      listCompetenciasCargosCrear = new ArrayList<Competenciascargos>();
      listTiposDetallesCrear = new ArrayList<TiposDetalles>();
      //modificar 
      listSueldosMercadosModificar = new ArrayList<SueldosMercados>();
      listCargosModificar = new ArrayList<Cargos>();
      listCompetenciasCargosModificar = new ArrayList<Competenciascargos>();
      listTiposDetallesModificar = new ArrayList<TiposDetalles>();
      //editar
      editarCargo = new Cargos();
      editarSueldoMercado = new SueldosMercados();
      editarCompetenciaCargo = new Competenciascargos();
      editarTipoDetalle = new TiposDetalles();
      //Cual Celda
      cualCelda = -1;
      cualCeldaSueldoMercado = -1;
      cualCeldaTipoDetalle = -1;
      cualCeldaCompetencia = -1;
      //Tipo Lista
      tipoListaSueldoMercado = 0;
      tipoListaTipoDetalle = 0;
      tipoLista = 0;
      tipoListaCompetencia = 0;
      //guardar
      guardado = true;
      guardadoSueldoMercado = true;
      guardadoTipoDetalle = true;
      guardadoCompetencia = true;
      guardadoDetalleCargo = true;
      borrarDetalleCargo = false;
      //Crear 
      nuevoCargo = new Cargos();
      nuevoCargo.setGruposalarial(new GruposSalariales());
      nuevoCargo.setGrupoviatico(new GruposViaticos());
      nuevoCargo.setProcesoproductivo(new ProcesosProductivos());
      nuevoSueldoMercado = new SueldosMercados();
      nuevoSueldoMercado.setTipoempresa(new TiposEmpresas());
      nuevoCompetenciaCargo = new Competenciascargos();
      nuevoCompetenciaCargo.setEvalcompetencia(new EvalCompetencias());
      nuevoTipoDetalle = new TiposDetalles();
      nuevoTipoDetalle.setEnfoque(new Enfoques());
      //Duplicar
      duplicarCargo = new Cargos();
      duplicarSueldoMercado = new SueldosMercados();
      duplicarCompetenciaCargo = new Competenciascargos();
      duplicarTipoDetalle = new TiposDetalles();
      //Banderas
      bandera = 0;
      banderaSueldoMercado = 0;
      banderaTipoDetalle = 0;
      banderaCompetencia = 0;
      //
      tablaActiva = "";
      activarLOV = true;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarCargos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      lovEmpresas = administrarCargos.listaEmpresas();

      if (lovEmpresas.size() > 0) {
         System.out.println("listaEmpresas : " + lovEmpresas);
         empresaActual = lovEmpresas.get(0);
         empresaSeleccionada = empresaActual;
         listaCargos = null;
         getListaCargos();
         System.out.println("listaCargos : " + listaCargos);
         if (listaCargos.size() > 0) {
            cargoTablaSeleccionado = listaCargos.get(0);
            listaSueldosMercados = null;
            getListaSueldosMercados();
            System.out.println("listaSueldosMercados : " + listaSueldosMercados);
            if (listaSueldosMercados != null) {
               if (listaSueldosMercados.size() > 0) {
                  sueldoMercadoSeleccionado = listaSueldosMercados.get(0);
               }
            }
            listaCompetenciasCargos = null;
            getListaCompetenciasCargos();
            if (listaCompetenciasCargos != null) {
               if (listaCompetenciasCargos.size() > 0) {
                  competenciaCargoSeleccionado = listaCompetenciasCargos.get(0);
               }
            }
         }
         listaTiposDetalles = null;
         getListaTiposDetalles();
         System.out.println("listaTiposDetalles : " + listaTiposDetalles);
         /*
             * if (listaTiposDetalles != null) { if (listaTiposDetalles.size() >
             * 0) { tipoDetalleSeleccionado = listaTiposDetalles.get(0);
             * System.out.println("tipoDetalleSeleccionado : " +
             * tipoDetalleSeleccionado); legendDetalleCargo = "[" +
             * listaTiposDetalles.get(0).getDescripcion() + "]";
             * cambiarIndiceTipoDetalle(tipoDetalleSeleccionado, 0); } }
          */
         activoDetalleCargo = true;

      }
   }

   /**
    *
    * @param i
    * @return
    */
   public boolean validarCamposNulosCargos(int i) {
      boolean retorno = true;
      if (i == 0) {

         if (cargoTablaSeleccionado.getCodigo() == null) {
            retorno = false;
         } else if (cargoTablaSeleccionado.getCodigo() <= 0) {
            retorno = false;
         }
         if (cargoTablaSeleccionado.getNombre() == null) {
            retorno = false;
         } else if (cargoTablaSeleccionado.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoCargo.getCodigo() == null) {
            retorno = false;
         } else if (nuevoCargo.getCodigo() <= 0) {
            retorno = false;
         }
         if (nuevoCargo.getNombre() == null) {
            retorno = false;
         } else if (nuevoCargo.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarCargo.getCodigo() == null) {
            retorno = false;
         } else if (duplicarCargo.getCodigo() <= 0) {
            retorno = false;
         }
         if (duplicarCargo.getNombre() == null) {
            retorno = false;
         } else if (duplicarCargo.getNombre().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   /**
    *
    * @param i
    * @return
    */
   public boolean validarCamposNulosSueldosMercados(int i) {
      boolean retorno = true;
      if (i == 1) {
         if (nuevoSueldoMercado.getTipoempresa().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarSueldoMercado.getTipoempresa().getSecuencia() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   /**
    *
    * @param i
    * @return
    */
   public boolean validarCamposNulosTiposDetalles(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (tipoDetalleSeleccionado.getEnfoque().getSecuencia() == null) {
            retorno = false;
         }
         if (tipoDetalleSeleccionado.getCodigo() == null) {
            retorno = false;
         } else if (tipoDetalleSeleccionado.getCodigo().intValue() <= 0) {
            retorno = false;
         }
         if (tipoDetalleSeleccionado.getDescripcion() == null) {
            retorno = false;
         } else if (tipoDetalleSeleccionado.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoTipoDetalle.getEnfoque().getSecuencia() == null) {
            retorno = false;
         }
         if (nuevoTipoDetalle.getCodigo() == null) {
            retorno = false;
         } else if (nuevoTipoDetalle.getCodigo().intValue() <= 0) {
            retorno = false;
         }
         if (nuevoTipoDetalle.getDescripcion() == null) {
            retorno = false;
         } else if (nuevoTipoDetalle.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {

         if (duplicarTipoDetalle.getEnfoque().getSecuencia() == null) {
            retorno = false;
         }
         if (duplicarTipoDetalle.getCodigo() == null) {
            retorno = false;
         } else if (duplicarTipoDetalle.getCodigo().intValue() <= 0) {
            retorno = false;
         }
         if (duplicarTipoDetalle.getDescripcion() == null) {
            retorno = false;
         } else if (duplicarTipoDetalle.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   /**
    *
    * @param i
    */
   public void procesoModificacionCargo(Cargos cargo) {
      cargoTablaSeleccionado = cargo;
      boolean respuesta = validarCamposNulosCargos(0);
      if (respuesta == true) {
         modificarCargo(cargoTablaSeleccionado);
      } else {
         cargoTablaSeleccionado.setCodigo(auxCodigoCargo);
         cargoTablaSeleccionado.setNombre(auxNombreCargo);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCargo");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCargo').show()");
      }
   }

   /**
    *
    * @param indice
    */
   public void modificarCargo(Cargos cargo) {
      cargoTablaSeleccionado = cargo;
      int tamDes = cargoTablaSeleccionado.getNombre().length();

      if (tamDes >= 1 && tamDes <= 50) {
         cargoTablaSeleccionado.setNombre(cargoTablaSeleccionado.getNombre().toUpperCase());
         if (cargoTablaSeleccionado.getCodigoalternativo() != null) {
            cargoTablaSeleccionado.setCodigoalternativo(cargoTablaSeleccionado.getCodigoalternativo().toUpperCase());
         }
         if (!listCargosCrear.contains(cargoTablaSeleccionado)) {
            if (listCargosModificar.isEmpty()) {
               listCargosModificar.add(cargoTablaSeleccionado);
            } else if (!listCargosModificar.contains(cargoTablaSeleccionado)) {
               listCargosModificar.add(cargoTablaSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCargo");
      } else {
         cargoTablaSeleccionado.setNombre(auxNombreCargo);
         RequestContext.getCurrentInstance().update("form:datosCargo");
         RequestContext.getCurrentInstance().execute("PF('errorDescripcionCargo').show()");
      }
   }

   /**
    *
    * @param indice
    * @param confirmarCambio
    * @param valorConfirmar
    */
   public void modificarCargo(Cargos cargo, String confirmarCambio, String valorConfirmar) {
      cargoTablaSeleccionado = cargo;
      //indexSueldoMercado = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("GRUPOSALARIAL")) {
         if (!valorConfirmar.isEmpty()) {
            cargoTablaSeleccionado.getGruposalarial().setDescripcion(grupoSalarial);

            for (int i = 0; i < lovGruposSalariales.size(); i++) {
               if (lovGruposSalariales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cargoTablaSeleccionado.setGruposalarial(lovGruposSalariales.get(indiceUnicoElemento));

               lovGruposSalariales = null;
               getLovGruposSalariales();
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndexCargo = false;
               RequestContext.getCurrentInstance().update("form:GrupoSalarialDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            lovGruposSalariales = null;
            getLovGruposSalariales();
            cargoTablaSeleccionado.setGruposalarial(new GruposSalariales());

            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }

      if (confirmarCambio.equalsIgnoreCase("GRUPOVIATICO")) {
         if (!valorConfirmar.isEmpty()) {
            cargoTablaSeleccionado.getGrupoviatico().setDescripcion(grupoViatico);

            for (int i = 0; i < lovGruposViaticos.size(); i++) {
               if (lovGruposViaticos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cargoTablaSeleccionado.setGrupoviatico(lovGruposViaticos.get(indiceUnicoElemento));

               lovGruposViaticos = null;
               getLovGruposViaticos();
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndexCargo = false;
               RequestContext.getCurrentInstance().update("form:GrupoViaticoDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            cargoTablaSeleccionado.setGrupoviatico(new GruposViaticos());

            lovGruposViaticos = null;
            getLovGruposViaticos();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (confirmarCambio.equalsIgnoreCase("PROCESOPRODUCTIVO")) {
         if (!valorConfirmar.isEmpty()) {
            cargoTablaSeleccionado.getProcesoproductivo().setDescripcion(procesoProductivo);

            for (int i = 0; i < lovProcesosProductivos.size(); i++) {
               if (lovProcesosProductivos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               cargoTablaSeleccionado.setProcesoproductivo(lovProcesosProductivos.get(indiceUnicoElemento));

               lovProcesosProductivos = null;
               getLovProcesosProductivos();
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndexCargo = false;
               RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            cargoTablaSeleccionado.setProcesoproductivo(new ProcesosProductivos());

            lovProcesosProductivos = null;
            getLovProcesosProductivos();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }

      if (coincidencias == 1) {
         if (!listCargosCrear.contains(cargoTablaSeleccionado)) {
            if (listCargosModificar.isEmpty()) {
               listCargosModificar.add(cargoTablaSeleccionado);
            } else if (!listCargosModificar.contains(cargoTablaSeleccionado)) {
               listCargosModificar.add(cargoTablaSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }

      }
      RequestContext.getCurrentInstance().update("form:datosCargo");
   }

   public void modificarSueldoMercado(int indice) {
      if (!listSueldosMercadosCrear.contains(sueldoMercadoSeleccionado)) {
         if (listSueldosMercadosModificar.isEmpty()) {
            listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
         } else if (!listSueldosMercadosModificar.contains(sueldoMercadoSeleccionado)) {
            listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
         }
         if (guardadoSueldoMercado == true) {
            guardadoSueldoMercado = false;
         }
      }

      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
   }

   public void modificarSueldoMercado(SueldosMercados sueldoMercado, String confirmarCambio, String valorConfirmar) {
      sueldoMercadoSeleccionado = sueldoMercado;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOEMPRESA")) {
         sueldoMercadoSeleccionado.getTipoempresa().setDescripcion(tipoEmpresa);

         for (int i = 0; i < lovTiposEmpresas.size(); i++) {
            if (lovTiposEmpresas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            sueldoMercadoSeleccionado.setTipoempresa(lovTiposEmpresas.get(indiceUnicoElemento));

            lovTiposEmpresas = null;
            getLovTiposEmpresas();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexSueldoMercado = false;
            RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listSueldosMercadosCrear.contains(sueldoMercadoSeleccionado)) {
            if (listSueldosMercadosModificar.isEmpty()) {
               listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
            } else if (!listSueldosMercadosModificar.contains(sueldoMercadoSeleccionado)) {
               listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
            }
            if (guardadoSueldoMercado == true) {
               guardadoSueldoMercado = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
   }

   /**
    *
    * @param indice
    */
   public void modificarCompetenciaCargo(int indice) {
      if (!listCompetenciasCargosCrear.contains(competenciaCargoSeleccionado)) {
         if (listCompetenciasCargosModificar.isEmpty()) {
            listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
         } else if (!listCompetenciasCargosModificar.contains(competenciaCargoSeleccionado)) {
            listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
         }
         if (guardadoCompetencia == true) {
            guardadoCompetencia = false;
         }
      }

      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
   }

   /**
    *
    * @param indice
    * @param confirmarCambio
    * @param valorConfirmar
    */
   public void modificarCompetenciaCargo(Competenciascargos competencia, String confirmarCambio, String valorConfirmar) {
      competenciaCargoSeleccionado = competencia;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("EVALCOMPETENCIA")) {
         competenciaCargoSeleccionado.getEvalcompetencia().setDescripcion(evalCompetencia);

         for (int i = 0; i < lovEvalCompetencias.size(); i++) {
            if (lovEvalCompetencias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            competenciaCargoSeleccionado.setEvalcompetencia(lovEvalCompetencias.get(indiceUnicoElemento));

            lovEvalCompetencias = null;
            getLovEvalCompetencias();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirCompetencia = false;
            RequestContext.getCurrentInstance().update("form:EvalCompetenciaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listCompetenciasCargosCrear.contains(competenciaCargoSeleccionado)) {
            if (listCompetenciasCargosModificar.isEmpty()) {
               listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
            } else if (!listCompetenciasCargosModificar.contains(competenciaCargoSeleccionado)) {
               listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
            }
            if (guardadoCompetencia == true) {
               guardadoCompetencia = false;
            }
         }

      }
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
   }

   /**
    *
    * @param i
    */
   public void procesoModificacionTipoDetalle(TiposDetalles tipoDetalle) {
      tipoDetalleSeleccionado = tipoDetalle;
      boolean respuesta = validarCamposNulosTiposDetalles(0);
      if (respuesta == true) {
         modificarTipoDetalle(tipoDetalleSeleccionado);
      } else {
         tipoDetalleSeleccionado.setCodigo(auxCodigoTipoDetalle);
         tipoDetalleSeleccionado.setDescripcion(auxDescriptionTipoDetalle);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoDetalle').show()");
      }
   }

   /**
    *
    * @param indice
    */
   public void modificarTipoDetalle(TiposDetalles tipoDetalle) {
      tipoDetalleSeleccionado = tipoDetalle;
      String descrip = tipoDetalleSeleccionado.getDescripcion();

      if (descrip.length() >= 1 && descrip.length() <= 40) {
         String aux = tipoDetalleSeleccionado.getDescripcion().toUpperCase();
         tipoDetalleSeleccionado.setDescripcion(aux);
         if (!listTiposDetallesCrear.contains(tipoDetalleSeleccionado)) {
            if (listTiposDetallesModificar.isEmpty()) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            } else if (!listTiposDetallesModificar.contains(tipoDetalleSeleccionado)) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            }
            if (guardadoTipoDetalle == true) {
               guardadoTipoDetalle = false;
            }
         }

         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
      } else {
         tipoDetalleSeleccionado.setDescripcion(auxDescriptionTipoDetalle);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
         RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoDetalle').show()");

      }
   }

   /**
    *
    * @param indice
    * @param confirmarCambio
    * @param valorConfirmar
    */
   public void modificarTipoDetalle(TiposDetalles tipoDetalle, String confirmarCambio, String valorConfirmar) {
      tipoDetalleSeleccionado = tipoDetalle;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("ENFOQUE")) {
         tipoDetalleSeleccionado.getEnfoque().setDescripcion(grupoSalarial);

         for (int i = 0; i < lovEnfoques.size(); i++) {
            if (lovEnfoques.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoDetalleSeleccionado.setEnfoque(lovEnfoques.get(indiceUnicoElemento));

            lovEnfoques = null;
            getLovEnfoques();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTipoDetalle = false;
            RequestContext.getCurrentInstance().update("form:EnfoqueDialogo");
            RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listTiposDetallesCrear.contains(tipoDetalleSeleccionado)) {
            if (listTiposDetallesModificar.isEmpty()) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            } else if (!listTiposDetallesModificar.contains(tipoDetalleSeleccionado)) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            }
            if (guardadoTipoDetalle == true) {
               guardadoTipoDetalle = false;
            }
         }

      }
      RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
   }

   public void posicionCargo() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      RequestContext.getCurrentInstance().execute("PF('datosCargo').unselectAllRows();PF('datosCargo').selectRow(" + indice + ");");
      cargoTablaSeleccionado = listaCargos.get(indice);
      cambiarIndice(cargoTablaSeleccionado, columna);
   }

   /**
    *
    * @param indice
    * @param celda
    */
   public void cambiarIndice(Cargos cargo, int celda) {
      tablaActiva = "C";
      if (guardadoSueldoMercado == true && guardadoCompetencia == true && guardadoTipoDetalle == true && guardadoDetalleCargo == true) {
         if (permitirIndexCargo == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            cualCelda = celda;
            cargoTablaSeleccionado = cargo;
            sueldoMercadoSeleccionado = null;
            competenciaCargoSeleccionado = null;
            tipoDetalleSeleccionado = listaTiposDetalles.get(0);
            banderaDetalleCargo = -1;
            auxCodigoCargo = cargoTablaSeleccionado.getCodigo();
            auxNombreCargo = cargoTablaSeleccionado.getNombre();
            grupoSalarial = cargoTablaSeleccionado.getGruposalarial().getDescripcion();
            grupoViatico = cargoTablaSeleccionado.getGrupoviatico().getDescripcion();
            procesoProductivo = cargoTablaSeleccionado.getProcesoproductivo().getDescripcion();

            listaSueldosMercados = null;
            getListaSueldosMercados();
            RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
            listaCompetenciasCargos = null;
            getListaCompetenciasCargos();
            RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
            if (banderaSueldoMercado == 1) {
               restaurarTablaSueldoM();
            }
            if (banderaCompetencia == 1) {
               restaurarTablaCompetencia();
            }
            activoDetalleCargo = true;
            legendDetalleCargo = "";
            detalleCargo = new DetallesCargos();
            RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
            RequestContext.getCurrentInstance().update("form:detalleCargo");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cambiarIndiceSueldoMercado(SueldosMercados sueldoM, int celda) {
      tablaActiva = "SM";
      if (guardadoDetalleCargo == true) {
         if (permitirIndexSueldoMercado == true) {
            sueldoMercadoSeleccionado = sueldoM;
            competenciaCargoSeleccionado = null;
            tipoDetalleSeleccionado = listaTiposDetalles.get(0);
            banderaDetalleCargo = -1;
            cualCeldaSueldoMercado = celda;
            tipoEmpresa = sueldoMercadoSeleccionado.getTipoempresa().getDescripcion();

            activoDetalleCargo = true;
            legendDetalleCargo = "";
            detalleCargo = new DetallesCargos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
            RequestContext.getCurrentInstance().update("form:detalleCargo");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   /**
    *
    * @param indice
    * @param celda
    */
   public void cambiarIndiceCompetenciaCargo(Competenciascargos competencia, int celda) {
      tablaActiva = "CC";
      if (guardadoDetalleCargo == true) {
         if (permitirCompetencia == true) {
            competenciaCargoSeleccionado = competencia;
            tipoDetalleSeleccionado = listaTiposDetalles.get(0);
            sueldoMercadoSeleccionado = null;
            cualCeldaCompetencia = celda;
            banderaDetalleCargo = -1;
            evalCompetencia = competenciaCargoSeleccionado.getEvalcompetencia().getDescripcion();

            activoDetalleCargo = true;
            legendDetalleCargo = "";
            detalleCargo = new DetallesCargos();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
            RequestContext.getCurrentInstance().update("form:detalleCargo");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cambiarIndiceTipoDetalle(TiposDetalles tipoDetalle, int celda) {
      tablaActiva = "TD";
      if (guardadoDetalleCargo == true) {
         if (permitirIndexTipoDetalle == true) {
            tipoDetalleSeleccionado = tipoDetalle;
            cualCeldaTipoDetalle = celda;
            banderaDetalleCargo = -1;
            enfoque = tipoDetalleSeleccionado.getEnfoque().getDescripcion();
            auxCodigoTipoDetalle = tipoDetalleSeleccionado.getCodigo();
            auxDescriptionTipoDetalle = tipoDetalleSeleccionado.getDescripcion();
            legendDetalleCargo = "[" + tipoDetalleSeleccionado.getDescripcion() + "]";

            detalleCargo = null;
            getDetalleCargo();
            activoDetalleCargo = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
            RequestContext.getCurrentInstance().update("form:detalleCargo");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

      }
   }
   //GUARDAR

   /**
    *
    */
   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   /**
    *
    */
   public void cancelarYSalir() {
      cancelarModificacionGeneral();
      salir();
   }

   /**
    *
    */
   public void guardarGeneral() {
      if (guardado == false || guardadoSueldoMercado == false || guardadoCompetencia == false || guardadoTipoDetalle == false || guardadoDetalleCargo == false) {
         tablaActiva = "";
         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado == false) {
            guardarCambiosCargos();
         }
         if (guardadoSueldoMercado == false) {
            guardarCambiosSueldosMercados();
         }
         if (guardadoCompetencia == false) {
            guardarCambiosCompetenciasCargos();
         }
         if (guardadoTipoDetalle == false) {
            guardarCambiosTiposDetalles();
         }
         if (guardadoDetalleCargo == false) {
            if (borrarDetalleCargo == false) {
               administrarCargos.editarDetalleCargo(detalleCargo);
            } else {
               administrarCargos.borrarDetalleCargo(detalleCargo);
            }
            guardadoDetalleCargo = true;
            activoDetalleCargo = true;
            detalleCargo = new DetallesCargos();
            legendDetalleCargo = "";
            contarRegistrosC();
            contarRegistrosCC();
            contarRegistrosSM();
            contarRegistrosTD();
            RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
            RequestContext.getCurrentInstance().update("form:detalleCargo");
         }
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   /**
    *
    */
   public void guardarCambiosCargos() {
      try {
         if (!listCargosBorrar.isEmpty()) {
            for (int i = 0; i < listCargosBorrar.size(); i++) {
               administrarCargos.borrarCargos(listCargosBorrar);
            }
            listCargosBorrar.clear();
         }
         if (!listCargosCrear.isEmpty()) {
            for (int i = 0; i < listCargosCrear.size(); i++) {
               administrarCargos.crearCargos(listCargosCrear);
            }
            listCargosCrear.clear();
         }
         if (!listCargosModificar.isEmpty()) {
            administrarCargos.editarCargos(listCargosModificar);
            listCargosModificar.clear();
         }
         listaCargos = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCargo");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:aceptar");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Cargos se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCargos : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Cargos, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void guardarCambiosSueldosMercados() {
      try {
         if (!listSueldosMercadosBorrar.isEmpty()) {
            administrarCargos.borrarSueldosMercados(listSueldosMercadosBorrar);
            listSueldosMercadosBorrar.clear();
         }
         if (!listSueldosMercadosCrear.isEmpty()) {
            administrarCargos.crearSueldosMercados(listSueldosMercadosCrear);
            listSueldosMercadosCrear.clear();
         }
         if (!listSueldosMercadosModificar.isEmpty()) {
            administrarCargos.editarSueldosMercados(listSueldosMercadosModificar);
            listSueldosMercadosModificar.clear();
         }
         listaSueldosMercados = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
         guardadoSueldoMercado = true;
         RequestContext.getCurrentInstance().update("form:aceptar");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Sueldos Mercados se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCargos : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Sueldos Mercados, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   /**
    *
    */
   public void guardarCambiosCompetenciasCargos() {
      try {
         if (!listCompetenciasCargosBorrar.isEmpty()) {
            administrarCargos.borrarCompetenciasCargos(listCompetenciasCargosBorrar);
            listCompetenciasCargosBorrar.clear();
         }
         if (!listCompetenciasCargosCrear.isEmpty()) {
            administrarCargos.crearCompetenciasCargos(listCompetenciasCargosCrear);
            listCompetenciasCargosCrear.clear();
         }
         if (!listCompetenciasCargosModificar.isEmpty()) {
            administrarCargos.editarCompetenciasCargos(listCompetenciasCargosModificar);
            listCompetenciasCargosModificar.clear();
         }
         listaCompetenciasCargos = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
         guardadoCompetencia = true;
         RequestContext.getCurrentInstance().update("form:aceptar");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Competencias se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCargos : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Competencias, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosTiposDetalles() {
      try {
         if (!listTiposDetallesBorrar.isEmpty()) {
            administrarCargos.borrarTiposDetalles(listTiposDetallesBorrar);
            listTiposDetallesBorrar.clear();
         }
         if (!listTiposDetallesCrear.isEmpty()) {
            administrarCargos.crearTiposDetalles(listTiposDetallesCrear);
            listTiposDetallesCrear.clear();
         }
         if (!listTiposDetallesModificar.isEmpty()) {
            administrarCargos.editarTiposDetalles(listTiposDetallesModificar);
            listTiposDetallesModificar.clear();
         }
         listaTiposDetalles = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
         guardadoTipoDetalle = true;
         RequestContext.getCurrentInstance().update("form:aceptar");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Propiedades Cargo se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCargos : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Propiedades Cargo, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacionGeneral() {
      tablaActiva = "";
      sueldoMercadoSeleccionado = null;
      competenciaCargoSeleccionado = null;
      tipoDetalleSeleccionado = null;
      cargoTablaSeleccionado = null;
      cambiosPagina = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        if (guardado == false) {
      cancelarModificacionCargos();
      RequestContext.getCurrentInstance().update("form:datosCargo");
//        }
//        if (guardadoSueldoMercado == false) {
      cancelarModificacionSueldosMercados();
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
//        }
//        if (guardadoCompetencia == false) {
      cancelarModificacionCompetenciasCargos();
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
//        }
//        if (guardadoTipoDetalle == false) {
      cancelarModificacionTiposDetalles();
      RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
//        }
      activoDetalleCargo = true;
      detalleCargo = new DetallesCargos();
      legendDetalleCargo = "";
      contarRegistrosC();
      contarRegistrosCC();
      contarRegistrosSM();
      contarRegistrosTD();
      RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
      RequestContext.getCurrentInstance().update("form:detalleCargo");
      guardadoDetalleCargo = true;
   }

   /**
    *
    * @return
    */
   public String redirigir() {
      return paginaAnterior;
   }

   /**
    *
    *
    */
   public void cancelarModificacionCargos() {
      cargoTablaSeleccionado = null;
      if (bandera == 1) {
         restaurarTablaCargos();
      }
      listCargosBorrar.clear();
      listCargosCrear.clear();
      listCargosModificar.clear();

      k = 0;
      listaCargos = null;
      getListaCargos();
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosCargo");
   }

   /**
    *
    */
   public void cancelarModificacionSueldosMercados() {
      sueldoMercadoSeleccionado = null;
      if (banderaSueldoMercado == 1) {
         restaurarTablaSueldoM();
      }
      listSueldosMercadosBorrar.clear();
      listSueldosMercadosCrear.clear();
      listSueldosMercadosModificar.clear();
      k = 0;
      listaSueldosMercados = null;
      getListaSueldosMercados();
      guardadoSueldoMercado = true;
      permitirIndexSueldoMercado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
   }

   /**
    *
    */
   public void cancelarModificacionCompetenciasCargos() {
      competenciaCargoSeleccionado = null;
      if (banderaCompetencia == 1) {
         restaurarTablaCompetencia();
      }
      listCompetenciasCargosBorrar.clear();
      listCompetenciasCargosCrear.clear();
      listCompetenciasCargosModificar.clear();
      k = 0;
      listaCompetenciasCargos = null;
      getListaCompetenciasCargos();
      guardadoCompetencia = true;
      permitirCompetencia = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
   }

   /**
    *
    */
   public void cancelarModificacionTiposDetalles() {
      tipoDetalleSeleccionado = null;
      if (banderaTipoDetalle == 1) {
         restaurarTablaTipoD();
      }
      listTiposDetallesBorrar.clear();
      listTiposDetallesCrear.clear();
      listTiposDetallesModificar.clear();
      k = 0;
      listaTiposDetalles = null;
      getListaTiposDetalles();
      guardadoTipoDetalle = true;
      permitirIndexTipoDetalle = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
   }

   /**
    *
    */
   public void editarCelda() {

      RequestContext context = RequestContext.getCurrentInstance();
      if ("SM".equals(tablaActiva)) {
         if (sueldoMercadoSeleccionado != null) {
            editarSueldoMercado = sueldoMercadoSeleccionado;

            if (cualCeldaSueldoMercado == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEmpresaSueldoMercadoD");
               RequestContext.getCurrentInstance().execute("PF('editarTipoEmpresaSueldoMercadoD').show()");
               cualCeldaSueldoMercado = -1;
            } else if (cualCeldaSueldoMercado == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMinimoSueldoMercadoD");
               RequestContext.getCurrentInstance().execute("PF('editarSueldoMinimoSueldoMercadoD').show()");
               cualCeldaSueldoMercado = -1;
            } else if (cualCeldaSueldoMercado == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMaximoSueldoMercadoD");
               RequestContext.getCurrentInstance().execute("PF('editarSueldoMaximoSueldoMercadoD').show()");
               cualCeldaSueldoMercado = -1;
            }
         }

      } else if ("CC".equals(tablaActiva)) {
         if (competenciaCargoSeleccionado != null) {
            editarCompetenciaCargo = competenciaCargoSeleccionado;

            if (cualCeldaCompetencia == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCompetenciaCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarDescripcionCompetenciaCargoD').show()");
               cualCeldaCompetencia = -1;
            }
         }
      } else if ("TD".equals(tablaActiva)) {
         if (tipoDetalleSeleccionado != null) {
            editarTipoDetalle = tipoDetalleSeleccionado;

            if (cualCeldaTipoDetalle == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoTipoDetalleD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoTipoDetalleD').show()");
               cualCeldaTipoDetalle = -1;
            } else if (cualCeldaTipoDetalle == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionTipoDetalleD");
               RequestContext.getCurrentInstance().execute("PF('editarDescripcionTipoDetalleD').show()");
               cualCeldaTipoDetalle = -1;
            } else if (cualCeldaTipoDetalle == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarEnfoqueTipoDetalleD");
               RequestContext.getCurrentInstance().execute("PF('editarEnfoqueTipoDetalleD').show()");
               cualCeldaTipoDetalle = -1;
            }
         }
      } else if ("C".equals(tablaActiva)) {
         if (cargoTablaSeleccionado != null) {
            editarCargo = cargoTablaSeleccionado;
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoCargoD').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarNombreCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoSalarialCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarGrupoSalarialCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSalarioCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarSalarioCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMinimoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarSueldoMinimoCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMaximoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarSueldoMaximoCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 6) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoViaticoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarGrupoViaticoCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 9) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoProductivoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarProcesoProductivoCargoD').show()");
               cualCelda = -1;

            } else if (cualCelda == 10) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoAlternativoCargoD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoAlternativoCargoD').show()");
               cualCelda = -1;
            }
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    *
    */
   public void dialogoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false || guardadoSueldoMercado == false || guardadoCompetencia == false || guardadoTipoDetalle == false) {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else {/*
             * if (listaCargos.isEmpty()) { activoSueldoMercado = true;
             * activoCompetencia = true; activoTipoDetalle = false;
             * RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
             * RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()"); } else { if
             * (listaSueldosMercados.isEmpty() ||
             * listaCompetenciasCargos.isEmpty() ||
             * listaTiposDetalles.isEmpty()) { activoSueldoMercado = false;
             * activoCompetencia = false; activoTipoDetalle = false;
             * RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
             * RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()"); } else {
             * activoSueldoMercado = false; activoCompetencia = false;
             * activoTipoDetalle = false; if (cargoTablaSeleccionado != null) {
             * codigoNuevoCargo();
             * RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCargo");
             * RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCargo').show()"); } if
             * (sueldoMercadoSeleccionado != null) {
             * RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroSueldoMercado");
             * RequestContext.getCurrentInstance().execute("PF('NuevoRegistroSueldoMercado').show()"); } if
             * (competenciaCargoSeleccionado != null) {
             * RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCompetenciaCargo");
             * RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCompetenciaCargo').show()"); } if
             * (tipoDetalleSeleccionado != null) { codigoNuevoTipoDetalle();
             * RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTipoDetalle");
             * RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoDetalle').show()"); } } }
          */

      }
   }

   /**
    *
    */
   public void dispararDialogoNuevoCargo() {
      RequestContext context = RequestContext.getCurrentInstance();
      codigoNuevoCargo();
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCargo");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCargo').show()");
   }

   public void dispararDialogoNuevoTipoDetalle() {
      codigoNuevoTipoDetalle();
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTipoDetalle");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoDetalle').show()");
   }

   /**
    *
    */
   public void codigoNuevoCargo() {
      String code = "";
      if (listaCargos.size() > 0) {
         int newCode = listaCargos.get(listaCargos.size() - 1).getCodigo().intValue() + 1;
         code = String.valueOf(newCode);
      } else {
         code = "1";
      }
      nuevoCargo.setCodigo(new Short(code));
   }

   public void codigoNuevoTipoDetalle() {
      String code = "";
      if (listaTiposDetalles.size() > 0) {
         int newCode = listaTiposDetalles.get(listaTiposDetalles.size() - 1).getCodigo().intValue() + 1;
         code = String.valueOf(newCode);
      } else {
         code = "1";
      }
      nuevoTipoDetalle.setCodigo(new BigInteger(code));
   }

//CREAR 
   /**
    *
    */
   public void agregarNuevoCargo() {
      boolean respueta = validarCamposNulosCargos(1);
      if (respueta == true) {
         int tamDes = nuevoCargo.getNombre().length();
         if (tamDes >= 1 && tamDes <= 50) {
            if (bandera == 1) {
               restaurarTablaCargos();
            }

            k++;
            l = BigInteger.valueOf(k);
            nuevoCargo.setEmpresa(empresaActual);
            String text = nuevoCargo.getNombre().toUpperCase();
            nuevoCargo.setNombre(text);
            if (nuevoCargo.getCodigoalternativo() != null) {
               String text2 = nuevoCargo.getCodigoalternativo().toUpperCase();
               nuevoCargo.setCodigoalternativo(text2);
            }
            nuevoCargo.setSecuencia(l);
            listCargosCrear.add(nuevoCargo);
            listaCargos.add(nuevoCargo);
            contarRegistrosC();
            cargoTablaSeleccionado = listaCargos.get(listaCargos.indexOf(nuevoCargo));
            nuevoCargo = new Cargos();
            nuevoCargo.setGruposalarial(new GruposSalariales());
            nuevoCargo.setGrupoviatico(new GruposViaticos());
            nuevoCargo.setProcesoproductivo(new ProcesosProductivos());
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCargo");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCargo').hide()");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:aceptar");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionCargo').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCargo').show()");
      }
   }

   public void agregarNuevoSueldoMercado() {
      boolean respueta = validarCamposNulosSueldosMercados(1);
      if (respueta == true) {
         if (banderaSueldoMercado == 1) {
            restaurarTablaSueldoM();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoSueldoMercado.setSecuencia(l);
         nuevoSueldoMercado.setCargo(cargoTablaSeleccionado);

         if (listaSueldosMercados == null) {
            listaSueldosMercados = new ArrayList<SueldosMercados>();
         }
         listSueldosMercadosCrear.add(nuevoSueldoMercado);
         listaSueldosMercados.add(nuevoSueldoMercado);
         contarRegistrosSM();
         sueldoMercadoSeleccionado = listaSueldosMercados.get(listaSueldosMercados.indexOf(nuevoSueldoMercado));
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroSueldoMercado').hide()");
         nuevoSueldoMercado = new SueldosMercados();
         nuevoSueldoMercado.setTipoempresa(new TiposEmpresas());
         if (guardadoSueldoMercado == true) {
            guardadoSueldoMercado = false;
            RequestContext.getCurrentInstance().update("form:aceptar");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullSueldoMercado').show()");
      }
   }

   /**
    *
    */
   public void agregarNuevoCompetenciaCargo() {
      if (nuevoCompetenciaCargo.getEvalcompetencia().getSecuencia() != null) {
         if (banderaCompetencia == 1) {
            restaurarTablaCompetencia();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoCompetenciaCargo.setSecuencia(l);
         nuevoCompetenciaCargo.setCargo(cargoTablaSeleccionado);

         if (listaCompetenciasCargos == null) {
            listaCompetenciasCargos = new ArrayList<Competenciascargos>();
         }
         listCompetenciasCargosCrear.add(nuevoCompetenciaCargo);
         listaCompetenciasCargos.add(nuevoCompetenciaCargo);
         contarRegistrosCC();
         competenciaCargoSeleccionado = listaCompetenciasCargos.get(listaCompetenciasCargos.indexOf(nuevoCompetenciaCargo));
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCompetenciaCargo').hide()");
         nuevoCompetenciaCargo = new Competenciascargos();
         nuevoCompetenciaCargo.setEvalcompetencia(new EvalCompetencias());
         if (guardadoCompetencia == true) {
            guardadoCompetencia = false;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCompetencia').show()");
      }
   }

   /**
    *
    */
   public void agregarNuevoTipoDetalle() {
      boolean respueta = validarCamposNulosTiposDetalles(1);
      if (respueta == true) {
         if (nuevoTipoDetalle.getDescripcion().length() >= 1 && nuevoTipoDetalle.getDescripcion().length() <= 40) {
            if (banderaTipoDetalle == 1) {
               restaurarTablaTipoD();

            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoDetalle.setSecuencia(l);
            if (listaTiposDetalles.isEmpty()) {
               listaTiposDetalles = new ArrayList<TiposDetalles>();
            }
            String var = nuevoTipoDetalle.getDescripcion().toUpperCase();
            nuevoTipoDetalle.setDescripcion(var);
            listTiposDetallesCrear.add(nuevoTipoDetalle);
            listaTiposDetalles.add(nuevoTipoDetalle);
            contarRegistrosTD();
            tipoDetalleSeleccionado = listaTiposDetalles.get(listaTiposDetalles.indexOf(nuevoTipoDetalle));
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoDetalle').hide()");
            nuevoTipoDetalle = new TiposDetalles();
            nuevoTipoDetalle.setEnfoque(new Enfoques());
            if (guardadoTipoDetalle == true) {
               guardadoTipoDetalle = false;
               RequestContext.getCurrentInstance().update("form:aceptar");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoDetalle').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoDetalle').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    */
   public void limpiarNuevaCargo() {
      nuevoCargo = new Cargos();
      nuevoCargo.setGruposalarial(new GruposSalariales());
      nuevoCargo.setGrupoviatico(new GruposViaticos());
      nuevoCargo.setProcesoproductivo(new ProcesosProductivos());
   }

   /**
    *
    */
   public void limpiarNuevaSueldoMercado() {
      nuevoSueldoMercado = new SueldosMercados();
      nuevoSueldoMercado.setTipoempresa(new TiposEmpresas());
   }

   /**
    *
    */
   public void limpiarNuevaCompetenciaCargo() {
      nuevoCompetenciaCargo = new Competenciascargos();
      nuevoCompetenciaCargo.setEvalcompetencia(new EvalCompetencias());
   }

   /**
    *
    */
   public void limpiarNuevaTipoDetalle() {
      nuevoTipoDetalle = new TiposDetalles();
      nuevoTipoDetalle.setEnfoque(new Enfoques());
   }

   //DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      if (tablaActiva.equals("C")) {
         if (cargoTablaSeleccionado != null) {
            duplicarCargoM();
         }
      } else if (tablaActiva.equals("SM")) {
         if (sueldoMercadoSeleccionado != null) {
            duplicarSueldoMercadoM();
         }
      } else if (tablaActiva.equals("CC")) {
         if (competenciaCargoSeleccionado != null) {
            duplicarCompetenciaCargoM();
         }
      } else if (tablaActiva.equals("TD")) {
         if (tipoDetalleSeleccionado != null) {
            duplicarTipoDetalleM();
         }
      }
   }

   /**
    *
    */
   public void duplicarCargoM() {
      duplicarCargo = new Cargos();
      k++;
      l = BigInteger.valueOf(k);
      duplicarCargo.setSecuencia(l);
      duplicarCargo.setCodigo(cargoTablaSeleccionado.getCodigo());
      duplicarCargo.setNombre(cargoTablaSeleccionado.getNombre());
      duplicarCargo.setGruposalarial(cargoTablaSeleccionado.getGruposalarial());
      duplicarCargo.setSueldominimo(cargoTablaSeleccionado.getSueldomaximo());
      duplicarCargo.setSueldomaximo(cargoTablaSeleccionado.getSueldomaximo());
      duplicarCargo.setGrupoviatico(cargoTablaSeleccionado.getGrupoviatico());
      duplicarCargo.setTurnorotativo(cargoTablaSeleccionado.getTurnorotativo());
      duplicarCargo.setJefe(cargoTablaSeleccionado.getJefe());
      duplicarCargo.setProcesoproductivo(cargoTablaSeleccionado.getProcesoproductivo());
      duplicarCargo.setCodigoalternativo(cargoTablaSeleccionado.getCodigoalternativo());

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroCargo");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCargo').show()");
   }

   /**
    *
    */
   public void duplicarSueldoMercadoM() {
      duplicarSueldoMercado = new SueldosMercados();
      k++;
      l = BigInteger.valueOf(k);
      duplicarSueldoMercado.setSecuencia(l);
      duplicarSueldoMercado.setTipoempresa(sueldoMercadoSeleccionado.getTipoempresa());
      duplicarSueldoMercado.setSueldomax(sueldoMercadoSeleccionado.getSueldomax());
      duplicarSueldoMercado.setSueldomin(sueldoMercadoSeleccionado.getSueldomin());

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroSueldoMercado");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSueldoMercado').show()");

   }

   public void duplicarCompetenciaCargoM() {
      duplicarCompetenciaCargo = new Competenciascargos();
      k++;
      l = BigInteger.valueOf(k);
      duplicarCompetenciaCargo.setSecuencia(l);
      duplicarCompetenciaCargo.setEvalcompetencia(competenciaCargoSeleccionado.getEvalcompetencia());

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroCompetenciaCargo");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCompetenciaCargo').show()");
   }

   /**
    *
    */
   public void duplicarTipoDetalleM() {
      duplicarTipoDetalle = new TiposDetalles();

      duplicarTipoDetalle.setCodigo(tipoDetalleSeleccionado.getCodigo());
      duplicarTipoDetalle.setDescripcion(tipoDetalleSeleccionado.getDescripcion());
      duplicarTipoDetalle.setEnfoque(tipoDetalleSeleccionado.getEnfoque());

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoDetalle");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoDetalle').show()");
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarCargo() {
      boolean respueta = validarCamposNulosCargos(2);
      if (respueta == true) {
         int tamDes = 0;
         tamDes = nuevoCargo.getNombre().length();
         if (tamDes >= 1 && tamDes <= 50) {
            if (bandera == 1) {
               restaurarTablaCargos();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoDetalle.setSecuencia(l);
            duplicarCargo.setEmpresa(empresaActual);
            String text = duplicarCargo.getNombre().toUpperCase();
            duplicarCargo.setNombre(text);
            if (duplicarCargo.getCodigoalternativo() != null) {
               String text2 = duplicarCargo.getCodigoalternativo().toUpperCase();
               duplicarCargo.setCodigoalternativo(text2);
            }
            listaCargos.add(duplicarCargo);
            listCargosCrear.add(duplicarCargo);
            contarRegistrosC();
            cargoTablaSeleccionado = listaCargos.get(listaCargos.indexOf(duplicarCargo));
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCargo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCargo').hide()");
            if (guardado == true) {
               guardado = false;
               //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            duplicarCargo = new Cargos();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionCargo').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCargo').show()");
      }
   }

   public void confirmarDuplicarSueldoMercado() {
      boolean respueta = validarCamposNulosSueldosMercados(2);
      if (respueta == true) {
         if (banderaSueldoMercado == 1) {
            restaurarTablaSueldoM();
         }
         duplicarSueldoMercado.setCargo(cargoTablaSeleccionado);

         listaSueldosMercados.add(duplicarSueldoMercado);
         listSueldosMercadosCrear.add(duplicarSueldoMercado);
         contarRegistrosSM();
         sueldoMercadoSeleccionado = listaSueldosMercados.get(listaSueldosMercados.indexOf(duplicarSueldoMercado));

         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSueldoMercado').hide()");
         if (guardadoSueldoMercado == true) {
            guardadoSueldoMercado = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
         }

         duplicarSueldoMercado = new SueldosMercados();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullSueldoMercado').show()");
      }
   }

   public void confirmarDuplicarCompetenciaCargo() {
      if (duplicarCompetenciaCargo.getEvalcompetencia().getSecuencia() != null) {
         if (banderaCompetencia == 1) {
            restaurarTablaCompetencia();
         }
         duplicarCompetenciaCargo.setCargo(cargoTablaSeleccionado);

         listaCompetenciasCargos.add(duplicarCompetenciaCargo);
         listCompetenciasCargosCrear.add(duplicarCompetenciaCargo);
         contarRegistrosCC();
         competenciaCargoSeleccionado = listaCompetenciasCargos.get(listaCompetenciasCargos.indexOf(duplicarCompetenciaCargo));

         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCompetenciaCargo').hide()");
         if (guardadoCompetencia == true) {
            guardadoCompetencia = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
         }

         duplicarCompetenciaCargo = new Competenciascargos();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCompetencia').show()");
      }
   }

   public void confirmarDuplicarTipoDetalle() {
      boolean respueta = validarCamposNulosTiposDetalles(2);
      if (respueta == true) {
         if (duplicarTipoDetalle.getDescripcion().length() >= 1 && duplicarTipoDetalle.getDescripcion().length() <= 40) {
            if (banderaTipoDetalle == 1) {
               restaurarTablaTipoD();

            }
            String var = duplicarTipoDetalle.getDescripcion().toUpperCase();
            duplicarTipoDetalle.setDescripcion(var);
            listaTiposDetalles.add(duplicarTipoDetalle);
            listTiposDetallesCrear.add(duplicarTipoDetalle);
            contarRegistrosTD();
            tipoDetalleSeleccionado = listaTiposDetalles.get(listaTiposDetalles.indexOf(duplicarTipoDetalle));

            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoDetalle').hide()");
            if (guardadoTipoDetalle == true) {
               guardadoTipoDetalle = false;
               //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            duplicarTipoDetalle = new TiposDetalles();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoDetalle').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoDetalle').show()");
      }
   }

   //LIMPIAR DUPLICAR
   public void limpiarDuplicarCargo() {
      duplicarCargo = new Cargos();
      duplicarCargo.setGruposalarial(new GruposSalariales());
      duplicarCargo.setGrupoviatico(new GruposViaticos());
      duplicarCargo.setProcesoproductivo(new ProcesosProductivos());
   }

   public void limpiarDuplicarSueldoMercado() {
      duplicarSueldoMercado = new SueldosMercados();
      duplicarSueldoMercado.setTipoempresa(new TiposEmpresas());
   }

   public void limpiarDuplicarCompetenciaCargo() {
      duplicarCompetenciaCargo = new Competenciascargos();
      duplicarCompetenciaCargo.setEvalcompetencia(new EvalCompetencias());
   }

   public void limpiarDuplicarTipoDetalle() {
      duplicarTipoDetalle = new TiposDetalles();
      duplicarTipoDetalle.setEnfoque(new Enfoques());
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   public boolean validarExistenciaCargoDetalleCargo() {
      boolean retorno = true;
      int regAsociados = 0;
      regAsociados = administrarCargos.validarExistenciaCargoDetalleCargos(cargoTablaSeleccionado.getSecuencia());

      if (regAsociados == 0) {
         retorno = true;
      }
      if (regAsociados > 0) {
         retorno = false;
      }
      return retorno;
   }

   public void verificarRegistroBorrar() {
      if (tablaActiva.equals("C")) {
         if (cargoTablaSeleccionado != null) {
            if (listaSueldosMercados.isEmpty() && listaCompetenciasCargos.isEmpty()) {
               if (validarExistenciaCargoDetalleCargo() == true) {
                  borrarCargo();
               } else {
                  RequestContext context = RequestContext.getCurrentInstance();
                  RequestContext.getCurrentInstance().execute("PF('errorBorradoCargo').show()");
               }
            } else {
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
         }
      } else if (tablaActiva.equals("SM")) {
         if (sueldoMercadoSeleccionado != null) {
            borrarSueldoMercado();
         }
      } else if (tablaActiva.equals("CC")) {
         if (competenciaCargoSeleccionado != null) {
            borrarCompetenciaCargo();
         }
      } else if (tablaActiva.equals("TD")) {
         if (tipoDetalleSeleccionado != null) {
            borrarTipoDetalle();
         }
      } else if (tablaActiva.equals("DC")) {
         if (banderaDetalleCargo >= 0) {
            borrarDetalleCargoM();
         }
      }
   }

   /**
    *
    */
   public void borrarDetalleCargoM() {
      borrarDetalleCargo = true;
      guardadoDetalleCargo = false;
      BigInteger auxS = detalleCargo.getSecuencia();
      Cargos auxC = detalleCargo.getCargo();
      TiposDetalles auxTD = detalleCargo.getTipodetalle();
      detalleCargo = new DetallesCargos();
      detalleCargo.setCargo(auxC);
      detalleCargo.setSecuencia(auxS);
      detalleCargo.setTipodetalle(auxTD);
      cambiosPagina = false;
      tablaActiva = "";
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:detalleCargo");
   }

   public void borrarCargo() {
      if (cargoTablaSeleccionado != null) {
         if (!listCargosModificar.isEmpty() && listCargosModificar.contains(cargoTablaSeleccionado)) {
            int modIndex = listCargosModificar.indexOf(cargoTablaSeleccionado);
            listCargosModificar.remove(modIndex);
            listCargosBorrar.add(cargoTablaSeleccionado);
         } else if (!listCargosCrear.isEmpty() && listCargosCrear.contains(cargoTablaSeleccionado)) {
            int crearIndex = listCargosCrear.indexOf(cargoTablaSeleccionado);
            listCargosCrear.remove(crearIndex);
         } else {
            listCargosBorrar.add(cargoTablaSeleccionado);
         }
         listaCargos.remove(cargoTablaSeleccionado);
         if (tipoLista == 1) {
            filtrarListaCargos.remove(cargoTablaSeleccionado);
         }
         contarRegistrosC();
         tablaActiva = "";
         cambiosPagina = false;
         cargoTablaSeleccionado = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCargo");

         if (guardado == true) {
            guardado = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
         }
      }
   }

   /**
    *
    */
   public void borrarSueldoMercado() {
      if (sueldoMercadoSeleccionado != null) {
         if (!listSueldosMercadosModificar.isEmpty() && listSueldosMercadosModificar.contains(sueldoMercadoSeleccionado)) {
            int modIndex = listSueldosMercadosModificar.indexOf(sueldoMercadoSeleccionado);
            listSueldosMercadosModificar.remove(modIndex);
            listSueldosMercadosBorrar.add(sueldoMercadoSeleccionado);
         } else if (!listSueldosMercadosCrear.isEmpty() && listSueldosMercadosCrear.contains(sueldoMercadoSeleccionado)) {
            int crearIndex = listSueldosMercadosCrear.indexOf(sueldoMercadoSeleccionado);
            listSueldosMercadosCrear.remove(crearIndex);
         } else {
            listSueldosMercadosBorrar.add(sueldoMercadoSeleccionado);
         }
         listaSueldosMercados.remove(sueldoMercadoSeleccionado);
         if (tipoListaSueldoMercado == 1) {
            filtrarListaSueldosMercados.remove(sueldoMercadoSeleccionado);
         }
         contarRegistrosSM();
         sueldoMercadoSeleccionado = null;
         tablaActiva = "";
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");

         if (guardadoSueldoMercado == true) {
            guardadoSueldoMercado = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
         }
      }
   }

   /**
    *
    */
   public void borrarCompetenciaCargo() {
      if (competenciaCargoSeleccionado != null) {
         if (!listCompetenciasCargosModificar.isEmpty() && listCompetenciasCargosModificar.contains(competenciaCargoSeleccionado)) {
            int modIndex = listCompetenciasCargosModificar.indexOf(competenciaCargoSeleccionado);
            listCompetenciasCargosModificar.remove(modIndex);
            listCompetenciasCargosBorrar.add(competenciaCargoSeleccionado);
         } else if (!listCompetenciasCargosCrear.isEmpty() && listCompetenciasCargosCrear.contains(competenciaCargoSeleccionado)) {
            int crearIndex = listCompetenciasCargosCrear.indexOf(competenciaCargoSeleccionado);
            listCompetenciasCargosCrear.remove(crearIndex);
         } else {
            listCompetenciasCargosBorrar.add(competenciaCargoSeleccionado);
         }
         listaCompetenciasCargos.remove(competenciaCargoSeleccionado);
         if (tipoListaCompetencia == 1) {
            filtrarListaCompetenciasCargos.remove(competenciaCargoSeleccionado);
         }
         contarRegistrosCC();
         tablaActiva = "";
         cambiosPagina = false;
         competenciaCargoSeleccionado = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
         if (guardadoCompetencia == true) {
            guardadoCompetencia = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
         }
      }
   }

   public void borrarTipoDetalle() {
      if (tipoDetalleSeleccionado != null) {
         if (!listTiposDetallesModificar.isEmpty() && listTiposDetallesModificar.contains(tipoDetalleSeleccionado)) {
            int modIndex = listTiposDetallesModificar.indexOf(tipoDetalleSeleccionado);
            listTiposDetallesModificar.remove(modIndex);
            listTiposDetallesBorrar.add(tipoDetalleSeleccionado);
         } else if (!listTiposDetallesCrear.isEmpty() && listTiposDetallesCrear.contains(tipoDetalleSeleccionado)) {
            int crearIndex = listTiposDetallesCrear.indexOf(tipoDetalleSeleccionado);
            listTiposDetallesCrear.remove(crearIndex);
         } else {
            listTiposDetallesBorrar.add(tipoDetalleSeleccionado);
         }
         listaTiposDetalles.remove(tipoDetalleSeleccionado);
         if (tipoListaTipoDetalle == 1) {
            filtrarListaTiposDetalles.remove(tipoDetalleSeleccionado);
         }
         contarRegistrosTD();
         tablaActiva = "";
         tipoDetalleSeleccionado = null;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");

         if (guardadoTipoDetalle == true) {
            guardadoTipoDetalle = false;
         }
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if ("C".equals(tablaActiva)) {
         if (cargoTablaSeleccionado != null) {
            if (bandera == 0) {
               altoTablaCargo = "77";
               cargoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCodigo");
               cargoCodigo.setFilterStyle("width: 85% !important");
               cargoNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoNombre");
               cargoNombre.setFilterStyle("width: 85% !important");
               cargoGrupoSalarial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoGrupoSalarial");
               cargoGrupoSalarial.setFilterStyle("width: 85% !important");
               cargoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSalario");
               cargoSalario.setFilterStyle("width: 85% !important");
               cargoSueldoMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSueldoMinimo");
               cargoSueldoMinimo.setFilterStyle("width: 85% !important");
               cargoSueldoMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSueldoMaximo");
               cargoSueldoMaximo.setFilterStyle("width: 85% !important");
               cargoGrupoViatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoGrupoViatico");
               cargoGrupoViatico.setFilterStyle("width: 85% !important");
               cargoCargoRotativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCargoRotativo");
               cargoCargoRotativo.setFilterStyle("width: 85% !important");
               cargoJefe = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoJefe");
               cargoJefe.setFilterStyle("width: 85% !important");
               cargoProcesoProductivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoProcesoProductivo");
               cargoProcesoProductivo.setFilterStyle("width: 85% !important");
               cargoCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCodigoAlternativo");
               cargoCodigoAlternativo.setFilterStyle("width: 85% !important");
               RequestContext.getCurrentInstance().update("form:datosCargo");
               bandera = 1;
            } else if (bandera == 1) {
               restaurarTablaCargos();
            }
         }
      } else if ("SM".equals(tablaActiva)) {
         if (sueldoMercadoSeleccionado != null) {
            if (banderaSueldoMercado == 0) {
               altoTablaSueldoMercado = "50";
               sueldoMercadoTipoEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoTipoEmpresa");
               sueldoMercadoTipoEmpresa.setFilterStyle("width: 85% !important");
               sueldoMercadoSueldoMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoSueldoMinimo");
               sueldoMercadoSueldoMinimo.setFilterStyle("width: 85% !important");
               sueldoMercadoSueldoMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoSueldoMaximo");
               sueldoMercadoSueldoMaximo.setFilterStyle("width: 85% !important");
               RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
               banderaSueldoMercado = 1;
            } else if (banderaSueldoMercado == 1) {
               restaurarTablaSueldoM();
            }
         }
      } else if ("CC".equals(tablaActiva)) {
         if (competenciaCargoSeleccionado != null) {
            if (banderaCompetencia == 0) {
               altoTablaCompetencia = "50";
               competenciaCargoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCompetenciaCargo:competenciaCargoDescripcion");
               competenciaCargoDescripcion.setFilterStyle("width: 85% !important");
               RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
               banderaCompetencia = 1;
            } else if (banderaCompetencia == 1) {
               restaurarTablaCompetencia();
            }
         }
      } else if ("TD".equals(tablaActiva)) {
         if (tipoDetalleSeleccionado != null) {
            if (banderaTipoDetalle == 0) {
               altoTablaTipoDetalle = "50";

               tipoDetalleDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleDescripcion");
               tipoDetalleDescripcion.setFilterStyle("width: 85% !important");
               tipoDetalleCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleCodigo");
               tipoDetalleCodigo.setFilterStyle("width: 85% !important");
               tipoDetalleEnfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleEnfoque");
               tipoDetalleEnfoque.setFilterStyle("width: 85% !important");
               RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
               banderaTipoDetalle = 1;
            } else if (banderaTipoDetalle == 1) {
               restaurarTablaTipoD();
            }
         }
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      if (bandera == 1) {
         restaurarTablaCargos();
      }
      if (banderaSueldoMercado == 1) {
         restaurarTablaSueldoM();
      }
      if (banderaCompetencia == 1) {
         restaurarTablaCompetencia();
      }
      if (banderaTipoDetalle == 1) {
         restaurarTablaTipoD();

      }
      listCargosBorrar.clear();
      listCargosCrear.clear();
      listCargosModificar.clear();
      listSueldosMercadosBorrar.clear();
      listSueldosMercadosCrear.clear();
      listSueldosMercadosModificar.clear();
      listCompetenciasCargosBorrar.clear();
      listCompetenciasCargosCrear.clear();
      listCompetenciasCargosModificar.clear();
      listTiposDetallesBorrar.clear();
      listTiposDetallesCrear.clear();
      listTiposDetallesModificar.clear();
      cargoTablaSeleccionado = null;
      sueldoMercadoSeleccionado = null;
      tipoDetalleSeleccionado = null;
      competenciaCargoSeleccionado = null;
      tablaActiva = "";
      k = 0;
      listaCargos = null;
      listaSueldosMercados = null;
      listaTiposDetalles = null;
      listaCompetenciasCargos = null;
      guardado = true;
      guardadoSueldoMercado = true;
      guardadoTipoDetalle = true;
      guardadoCompetencia = true;
      cambiosPagina = true;
      lovGruposViaticos = null;
      lovGruposSalariales = null;
      lovProcesosProductivos = null;
      lovTiposEmpresas = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void restaurarTablaCargos() {
      altoTablaCargo = "95";
      cargoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCodigo");
      cargoCodigo.setFilterStyle("display: none; visibility: hidden;");
      cargoNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoNombre");
      cargoNombre.setFilterStyle("display: none; visibility: hidden;");
      cargoGrupoSalarial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoGrupoSalarial");
      cargoGrupoSalarial.setFilterStyle("display: none; visibility: hidden;");
      cargoSalario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSalario");
      cargoSalario.setFilterStyle("display: none; visibility: hidden;");
      cargoSueldoMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSueldoMinimo");
      cargoSueldoMinimo.setFilterStyle("display: none; visibility: hidden;");
      cargoSueldoMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoSueldoMaximo");
      cargoSueldoMaximo.setFilterStyle("display: none; visibility: hidden;");
      cargoGrupoViatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoGrupoViatico");
      cargoGrupoViatico.setFilterStyle("display: none; visibility: hidden;");
      cargoCargoRotativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCargoRotativo");
      cargoCargoRotativo.setFilterStyle("display: none; visibility: hidden;");
      cargoJefe = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoJefe");
      cargoJefe.setFilterStyle("display: none; visibility: hidden;");
      cargoProcesoProductivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoProcesoProductivo");
      cargoProcesoProductivo.setFilterStyle("display: none; visibility: hidden;");
      cargoCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCargo:cargoCodigoAlternativo");
      cargoCodigoAlternativo.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      filtrarListaCargos = null;
      tipoLista = 0;
      RequestContext.getCurrentInstance().update("form:datosCargo");
   }

   public void restaurarTablaCompetencia() {
      altoTablaCompetencia = "68";
      competenciaCargoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCompetenciaCargo:competenciaCargoDescripcion");
      competenciaCargoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      banderaCompetencia = 0;
      filtrarListaCompetenciasCargos = null;
      tipoListaCompetencia = 0;
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
   }

   public void restaurarTablaSueldoM() {
      altoTablaSueldoMercado = "68";
      sueldoMercadoSueldoMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoSueldoMinimo");
      sueldoMercadoSueldoMinimo.setFilterStyle("display: none; visibility: hidden;");
      sueldoMercadoTipoEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoTipoEmpresa");
      sueldoMercadoTipoEmpresa.setFilterStyle("display: none; visibility: hidden;");
      sueldoMercadoSueldoMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSueldoMercado:sueldoMercadoSueldoMaximo");
      sueldoMercadoSueldoMaximo.setFilterStyle("display: none; visibility: hidden;");
      banderaSueldoMercado = 0;
      filtrarListaSueldosMercados = null;
      tipoListaSueldoMercado = 0;
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
   }

   public void restaurarTablaTipoD() {
      altoTablaTipoDetalle = "68";
      tipoDetalleDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleDescripcion");
      tipoDetalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
      tipoDetalleCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleCodigo");
      tipoDetalleCodigo.setFilterStyle("display: none; visibility: hidden;");
      tipoDetalleEnfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoDetalle:tipoDetalleEnfoque");
      tipoDetalleEnfoque.setFilterStyle("display: none; visibility: hidden;");
      banderaTipoDetalle = 0;
      filtrarListaTiposDetalles = null;
      tipoListaTipoDetalle = 0;
      RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if ("C".equals(tablaActiva)) {
         if (cargoTablaSeleccionado != null) {
            if (cualCelda == 2) {
               contarRegistrosGrupoSal();
               RequestContext.getCurrentInstance().update("form:GrupoSalarialDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').show()");
               tipoActualizacion = 0;
            }
            if (cualCelda == 6) {
               contarRegistrosGrupoVia();
               RequestContext.getCurrentInstance().update("form:GrupoViaticoDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').show()");
               tipoActualizacion = 0;
            }
            if (cualCelda == 9) {
               contarRegistrosProceProdu();
               RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      } else if ("SM".equals(tablaActiva)) {
         if (sueldoMercadoSeleccionado != null) {
            if (cualCeldaSueldoMercado == 0) {
               contarRegistrosEmpresas();
               RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      } else if ("CC".equals(tablaActiva)) {
         if (competenciaCargoSeleccionado != null) {
            if (cualCeldaCompetencia == 0) {
               contarRegistrosEvalComp();
               RequestContext.getCurrentInstance().update("form:EvalCompetenciaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      } else if ("DC".equals(tablaActiva)) {
         if (tipoDetalleSeleccionado != null) {
            if (cualCeldaTipoDetalle == 2) {
               contarRegistrosEnfo();
               RequestContext.getCurrentInstance().update("form:EnfoqueDialogo");
               RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      }
   }

   public void asignarIndexCargos(Cargos cargo, int celda, int tipoAct) {
      tablaActiva = "C";
      RequestContext context = RequestContext.getCurrentInstance();
      cargoTablaSeleccionado = cargo;
      tipoActualizacion = tipoAct;
      if (celda == 0) {
         contarRegistrosGrupoSal();
         RequestContext.getCurrentInstance().update("form:GrupoSalarialDialogo");
         RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').show()");
      }
      if (celda == 1) {
         contarRegistrosGrupoVia();
         RequestContext.getCurrentInstance().update("form:GrupoViaticoDialogo");
         RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').show()");
      }
      if (celda == 2) {
         contarRegistrosProceProdu();
         RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').show()");
      }
   }

   public void asignarIndexSueldoM(SueldosMercados sueldoM, int celda, int tipoAct) {
      tablaActiva = "SM";
      RequestContext context = RequestContext.getCurrentInstance();
      sueldoMercadoSeleccionado = sueldoM;
      tipoActualizacion = tipoAct;
      if (celda == 0) {
         contarRegistrosEmpresas();
         RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').show()");
      }
   }

   public void asignarIndexCompetenciaC(Competenciascargos competenciaC, int celda, int tipoAct) {
      tablaActiva = "CC";
      RequestContext context = RequestContext.getCurrentInstance();
      competenciaCargoSeleccionado = competenciaC;
      tipoActualizacion = tipoAct;
      if (celda == 0) {
         contarRegistrosEvalComp();
         RequestContext.getCurrentInstance().update("form:EvalCompetenciaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').show()");
      }
   }

   public void asignarIndexTipoDetalle(TiposDetalles tipoDetalle, int celda, int tipoAct) {
      tablaActiva = "TD";
      RequestContext context = RequestContext.getCurrentInstance();
      tipoDetalleSeleccionado = tipoDetalle;
      tipoActualizacion = tipoAct;
      if (celda == 0) {
         contarRegistrosEnfo();
         RequestContext.getCurrentInstance().update("form:EnfoqueDialogo");
         RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').show()");
      }
   }

   public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
      if (Campo.equals("GRUPOSALARIAL")) {
         if (tipoNuevo == 1) {
            grupoSalarial = nuevoCargo.getGruposalarial().getDescripcion();
         } else if (tipoNuevo == 2) {
            grupoSalarial = duplicarCargo.getGruposalarial().getDescripcion();
         }
      }
      if (Campo.equals("GRUPOVIATICO")) {
         if (tipoNuevo == 1) {
            grupoViatico = nuevoCargo.getGrupoviatico().getDescripcion();
         } else if (tipoNuevo == 2) {
            grupoViatico = duplicarCargo.getGrupoviatico().getDescripcion();
         }
      }
      if (Campo.equals("PROCESOPRODUCTIVO")) {
         if (tipoNuevo == 1) {
            procesoProductivo = nuevoCargo.getProcesoproductivo().getDescripcion();
         } else if (tipoNuevo == 2) {
            procesoProductivo = duplicarCargo.getProcesoproductivo().getDescripcion();
         }
      }
      if (Campo.equals("TIPOEMPRESA")) {
         if (tipoNuevo == 1) {
            tipoEmpresa = nuevoSueldoMercado.getTipoempresa().getDescripcion();
         } else if (tipoNuevo == 2) {
            tipoEmpresa = duplicarSueldoMercado.getTipoempresa().getDescripcion();
         }
      }
      if (Campo.equals("EVALCOMPETENCIA")) {
         if (tipoNuevo == 1) {
            evalCompetencia = nuevoCompetenciaCargo.getEvalcompetencia().getDescripcion();
         } else if (tipoNuevo == 2) {
            evalCompetencia = duplicarCompetenciaCargo.getEvalcompetencia().getDescripcion();
         }
      }
      if (Campo.equals("ENFOQUE")) {
         if (tipoNuevo == 1) {
            enfoque = nuevoTipoDetalle.getEnfoque().getDescripcion();
         } else if (tipoNuevo == 2) {
            enfoque = duplicarTipoDetalle.getEnfoque().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoCargo(String column, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("GRUPOSALARIAL")) {
         if (!valor.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoCargo.getGruposalarial().setDescripcion(grupoSalarial);
            } else if (tipoNuevo == 2) {
               duplicarCargo.getGruposalarial().setDescripcion(grupoSalarial);
            }
            for (int i = 0; i < lovGruposSalariales.size(); i++) {
               if (lovGruposSalariales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoCargo.setGruposalarial(lovGruposSalariales.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoSalarial");
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoSalario");
               } else if (tipoNuevo == 2) {
                  duplicarCargo.setGruposalarial(lovGruposSalariales.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoSalarial");
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoSalario");
               }
               lovGruposSalariales = null;
               getLovGruposSalariales();
            } else {
               RequestContext.getCurrentInstance().update("form:GrupoSalarialDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoSalarial");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoSalarial");
               }
            }
         } else {
            lovGruposSalariales = null;
            getLovGruposSalariales();
            if (tipoNuevo == 1) {
               nuevoCargo.setGruposalarial(new GruposSalariales());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoSalarial");
            } else if (tipoNuevo == 2) {
               duplicarCargo.setGruposalarial(new GruposSalariales());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoSalarial");
            }
         }
      }
      if (column.equalsIgnoreCase("GRUPOVIATICO")) {
         if (!valor.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoCargo.getGrupoviatico().setDescripcion(grupoViatico);
            } else if (tipoNuevo == 2) {
               duplicarCargo.getGrupoviatico().setDescripcion(grupoViatico);
            }
            for (int i = 0; i < lovGruposViaticos.size(); i++) {
               if (lovGruposViaticos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoCargo.setGrupoviatico(lovGruposViaticos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoViatico");
               } else if (tipoNuevo == 2) {
                  duplicarCargo.setGrupoviatico(lovGruposViaticos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoViatico");
               }
               lovGruposViaticos = null;
               getLovGruposViaticos();
            } else {
               RequestContext.getCurrentInstance().update("form:GrupoViaticoDialogo");
               RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoViatico");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoViatico");
               }
            }
         } else {
            lovGruposViaticos.clear();
            getLovGruposViaticos();
            if (tipoNuevo == 1) {
               nuevoCargo.setGrupoviatico(new GruposViaticos());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoViatico");
            } else if (tipoNuevo == 2) {
               duplicarCargo.setGrupoviatico(new GruposViaticos());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoViatico");
            }
         }
      }
      if (column.equalsIgnoreCase("PROCESOPRODUCTIVO")) {
         if (!valor.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoCargo.getProcesoproductivo().setDescripcion(procesoProductivo);
            } else if (tipoNuevo == 2) {
               duplicarCargo.getProcesoproductivo().setDescripcion(procesoProductivo);
            }
            for (int i = 0; i < lovProcesosProductivos.size(); i++) {
               if (lovProcesosProductivos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoCargo.setProcesoproductivo(lovProcesosProductivos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoProcesoProductivo");
               } else if (tipoNuevo == 2) {
                  duplicarCargo.setProcesoproductivo(lovProcesosProductivos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoProcesoProductivo");
               }
               lovProcesosProductivos = null;
               getLovProcesosProductivos();
            } else {
               RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoProcesoProductivo");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoProcesoProductivo");
               }
            }
         } else {
            lovProcesosProductivos.clear();
            getLovProcesosProductivos();
            if (tipoNuevo == 1) {
               nuevoCargo.setProcesoproductivo(new ProcesosProductivos());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoProcesoProductivo");
            } else if (tipoNuevo == 2) {
               duplicarCargo.setProcesoproductivo(new ProcesosProductivos());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoProcesoProductivo");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoSueldoMercado(String column, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("TIPOEMPRESA")) {
         if (tipoNuevo == 1) {
            nuevoSueldoMercado.getTipoempresa().setDescripcion(tipoEmpresa);
         } else if (tipoNuevo == 2) {
            duplicarSueldoMercado.getTipoempresa().setDescripcion(tipoEmpresa);
         }
         for (int i = 0; i < lovTiposEmpresas.size(); i++) {
            if (lovTiposEmpresas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoSueldoMercado.setTipoempresa(lovTiposEmpresas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSueldoMercadoTipoEmpresa");
            } else if (tipoNuevo == 2) {
               duplicarSueldoMercado.setTipoempresa(lovTiposEmpresas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSueldoMercadoTipoEmpresa");
            }
            lovTiposEmpresas = null;
            getLovTiposEmpresas();
         } else {
            RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSueldoMercadoTipoEmpresa");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSueldoMercadoTipoEmpresa");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoCompetenciaCargo(String column, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("EVALCOMPETENCIA")) {
         if (tipoNuevo == 1) {
            nuevoCompetenciaCargo.getEvalcompetencia().setDescripcion(evalCompetencia);
         } else if (tipoNuevo == 2) {
            duplicarCompetenciaCargo.getEvalcompetencia().setDescripcion(evalCompetencia);
         }
         for (int i = 0; i < lovEvalCompetencias.size(); i++) {
            if (lovEvalCompetencias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoCompetenciaCargo.setEvalcompetencia(lovEvalCompetencias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCompetenciaCargoEval");
            } else if (tipoNuevo == 2) {
               duplicarCompetenciaCargo.setEvalcompetencia(lovEvalCompetencias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCompetenciaCargoEval");
            }
            lovEvalCompetencias = null;
            getLovEvalCompetencias();
         } else {
            RequestContext.getCurrentInstance().update("form:EvalCompetenciaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCompetenciaCargoEval");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCompetenciaCargoEval");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoTipoDetalle(String column, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("ENFOQUE")) {
         if (tipoNuevo == 1) {
            nuevoTipoDetalle.getEnfoque().setDescripcion(enfoque);
         } else if (tipoNuevo == 2) {
            duplicarTipoDetalle.getEnfoque().setDescripcion(enfoque);
         }
         for (int i = 0; i < lovEnfoques.size(); i++) {
            if (lovEnfoques.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTipoDetalle.setEnfoque(lovEnfoques.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDetalleEnfoque");
            } else if (tipoNuevo == 2) {
               duplicarTipoDetalle.setEnfoque(lovEnfoques.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDetalleEnfoque");
            }
            lovEnfoques = null;
            getLovEnfoques();
         } else {
            RequestContext.getCurrentInstance().update("form:EnfoqueDialogo");
            RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDetalleEnfoque");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDetalleEnfoque");
            }
         }
      }
   }

   /**
    *
    */
   public void actualizarGrupoSalarial() {
      if (tipoActualizacion == 0) {
         cargoTablaSeleccionado.setGruposalarial(grupoSalarialSeleccionado);
         if (!listCargosCrear.contains(cargoTablaSeleccionado)) {
            if (listCargosModificar.isEmpty()) {
               listCargosModificar.add(cargoTablaSeleccionado);
            } else if (!listCargosModificar.contains(cargoTablaSeleccionado)) {
               listCargosModificar.add(cargoTablaSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
         }
         permitirIndexCargo = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCargo");
      } else if (tipoActualizacion == 1) {
         nuevoCargo.setGruposalarial(grupoSalarialSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoSalarial");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoSalario");
      } else if (tipoActualizacion == 2) {
         duplicarCargo.setGruposalarial(grupoSalarialSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoSalarial");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoSalario");
      }
      filtrarLovGruposSalariales = null;
      grupoSalarialSeleccionado = new GruposSalariales();
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:GrupoSalarialDialogo");
      RequestContext.getCurrentInstance().update("form:lovGrupoSalarial");
      RequestContext.getCurrentInstance().update("form:aceptarGS");

      context.reset("form:lovGrupoSalarial:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoSalarial').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').hide()");
   }

   /**
    *
    */
   public void cancelarCambioGrupoSalarial() {
      filtrarLovGruposSalariales = null;
      grupoSalarialSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexCargo = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovGrupoSalarial:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoSalarial').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoSalarialDialogo').hide()");
   }

   /**
    *
    */
   public void actualizarGrupoViatico() {
      if (tipoActualizacion == 0) {
         cargoTablaSeleccionado.setGrupoviatico(grupoViaticoSeleccionado);
         if (!listCargosCrear.contains(cargoTablaSeleccionado)) {
            if (listCargosModificar.isEmpty()) {
               listCargosModificar.add(cargoTablaSeleccionado);
            } else if (!listCargosModificar.contains(cargoTablaSeleccionado)) {
               listCargosModificar.add(cargoTablaSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
         }
         permitirIndexCargo = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCargo");
      } else if (tipoActualizacion == 1) {
         nuevoCargo.setGrupoviatico(grupoViaticoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoGrupoViatico");
      } else if (tipoActualizacion == 2) {
         duplicarCargo.setGrupoviatico(grupoViaticoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoViatico");
      }
      filtrarLovGruposViaticos = null;
      grupoViaticoSeleccionado = new GruposViaticos();
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:GrupoViaticoDialogo");
      RequestContext.getCurrentInstance().update("form:lovGrupoViatico");
      RequestContext.getCurrentInstance().update("form:aceptarGV");

      context.reset("form:lovGrupoViatico:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoViatico').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').hide()");
   }

   /**
    *
    */
   public void cancelarCambioGrupoViatico() {
      filtrarLovGruposViaticos = null;
      grupoViaticoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexCargo = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovGrupoViatico:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoViatico').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoViaticoDialogo').hide()");
   }

   /**
    *
    */
   public void actualizarProcesoProductivo() {
      if (tipoActualizacion == 0) {
         cargoTablaSeleccionado.setProcesoproductivo(procesoProductivoSeleccionado);
         if (!listCargosCrear.contains(cargoTablaSeleccionado)) {
            if (listCargosModificar.isEmpty()) {
               listCargosModificar.add(cargoTablaSeleccionado);
            } else if (!listCargosModificar.contains(cargoTablaSeleccionado)) {
               listCargosModificar.add(cargoTablaSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
         }
         permitirIndexCargo = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCargo");
      } else if (tipoActualizacion == 1) {
         nuevoCargo.setProcesoproductivo(procesoProductivoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargoProcesoProductivo");
      } else if (tipoActualizacion == 2) {
         duplicarCargo.setProcesoproductivo(procesoProductivoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoProcesoProductivo");
      }
      filtrarLovProcesosProductivos = null;
      procesoProductivoSeleccionado = new ProcesosProductivos();
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
      RequestContext.getCurrentInstance().update("form:lovProcesoProductivo");
      RequestContext.getCurrentInstance().update("form:aceptarPP");

      context.reset("form:lovProcesoProductivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesoProductivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').hide()");
   }

   /**
    *
    */
   public void cancelarCambioProcesoProductivo() {
      filtrarLovProcesosProductivos = null;
      procesoProductivoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexCargo = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProcesoProductivo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesoProductivo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoProductivoDialogo').hide()");
   }

   /**
    *
    */
   public void actualizarTipoEmpresa() {
      if (tipoActualizacion == 0) {
         sueldoMercadoSeleccionado.setTipoempresa(tipoEmpresaSeleccionado);
         if (!listSueldosMercadosCrear.contains(sueldoMercadoSeleccionado)) {
            if (listSueldosMercadosModificar.isEmpty()) {
               listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
            } else if (!listSueldosMercadosModificar.contains(sueldoMercadoSeleccionado)) {
               listSueldosMercadosModificar.add(sueldoMercadoSeleccionado);
            }
         }

         if (guardadoSueldoMercado == true) {
            guardadoSueldoMercado = false;
         }
         permitirIndexSueldoMercado = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
      } else if (tipoActualizacion == 1) {
         nuevoSueldoMercado.setTipoempresa(tipoEmpresaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSueldoMercadoTipoEmpresa");
      } else if (tipoActualizacion == 2) {
         duplicarSueldoMercado.setTipoempresa(tipoEmpresaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSueldoMercadoTipoEmpresa");
      }
      filtrarLovTiposEmpresas = null;
      tipoEmpresaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoEmpresa");
      RequestContext.getCurrentInstance().update("form:aceptarTT");

      context.reset("form:lovTipoEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').hide()");
   }

   public void cancelarCambioTipoEmpresa() {
      filtrarLovTiposEmpresas = null;
      tipoEmpresaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirCompetencia = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoEmpresaDialogo').hide()");
   }

   public void actualizarEvalCompetencia() {
      if (tipoActualizacion == 0) {
         competenciaCargoSeleccionado.setEvalcompetencia(evalCompetenciaSeleccionado);
         if (!listCompetenciasCargosCrear.contains(competenciaCargoSeleccionado)) {
            if (listCompetenciasCargosModificar.isEmpty()) {
               listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
            } else if (!listCompetenciasCargosModificar.contains(competenciaCargoSeleccionado)) {
               listCompetenciasCargosModificar.add(competenciaCargoSeleccionado);
            }
         }

         if (guardadoCompetencia == true) {
            guardadoCompetencia = false;
         }
         permitirCompetencia = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
      } else if (tipoActualizacion == 1) {
         nuevoCompetenciaCargo.setEvalcompetencia(evalCompetenciaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCompetenciaCargoEval");
      } else if (tipoActualizacion == 2) {
         duplicarCompetenciaCargo.setEvalcompetencia(evalCompetenciaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCompetenciaCargoEval");
      }
      filtrarLovProcesosProductivos = null;
      procesoProductivoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:EvalCompetenciaDialogo");
      RequestContext.getCurrentInstance().update("form:lovEvalCompetencia");
      RequestContext.getCurrentInstance().update("form:aceptarEC");

      context.reset("form:lovEvalCompetencia:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEvalCompetencia').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').hide()");
   }

   /**
    *
    */
   public void cancelarCambioEvalCompetencia() {
      filtrarLovProcesosProductivos = null;
      procesoProductivoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirCompetencia = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEvalCompetencia:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEvalCompetencia').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EvalCompetenciaDialogo').hide()");
   }

   public void actualizarEnfoque() {
      if (tipoActualizacion == 0) {
         tipoDetalleSeleccionado.setEnfoque(enfoqueSeleccionado);
         if (!listTiposDetallesCrear.contains(tipoDetalleSeleccionado)) {
            if (listTiposDetallesModificar.isEmpty()) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            } else if (!listTiposDetallesModificar.contains(tipoDetalleSeleccionado)) {
               listTiposDetallesModificar.add(tipoDetalleSeleccionado);
            }
         }

         if (guardadoTipoDetalle == true) {
            guardadoTipoDetalle = false;
         }
         permitirIndexTipoDetalle = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
      } else if (tipoActualizacion == 1) {
         nuevoTipoDetalle.setEnfoque(enfoqueSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoDetalleEnfoque");
      } else if (tipoActualizacion == 2) {
         duplicarTipoDetalle.setEnfoque(enfoqueSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoDetalleEnfoque");
      }
      lovEnfoques = null;
      enfoqueSeleccionado = new Enfoques();
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:EnfoqueDialogo");
      RequestContext.getCurrentInstance().update("form:lovEnfoque");
      RequestContext.getCurrentInstance().update("form:aceptarE");

      context.reset("form:lovEnfoque:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEnfoque').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').hide()");
   }

   /**
    *
    */
   public void cancelarCambioEnfoque() {
      lovTiposEmpresas = null;
      tipoEmpresaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTipoDetalle = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEnfoque:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEnfoque').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EnfoqueDialogo').hide()");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   /**
    *
    * @return
    */
   public String exportXML() {
      if (tablaActiva.equals("C")) {
         nombreTabla = ":formExportarC:datosCargoExportar";
         nombreXML = "Cargos_XML";
      } else if (tablaActiva.equals("SM")) {
         nombreTabla = ":formExportarSM:datosSueldoMercadoExportar";
         nombreXML = "SueldosMercados_XML";
      } else if (tablaActiva.equals("CC")) {
         nombreTabla = ":formExportarCC:datosCompetenciaCargoExportar";
         nombreXML = "CompetenciasCargos_XML";
      } else if (tablaActiva.equals("TD")) {
         nombreTabla = ":formExportarTD:datosTipoDetalleExportar";
         nombreXML = "PropiedadesCargo_XML";
      }
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (tablaActiva.equals("C")) {
         exportPDF_C();
      } else if (tablaActiva.equals("SM")) {
         exportPDF_SM();
      } else if (tablaActiva.equals("CC")) {
         exportPDF_CC();
      } else if (tablaActiva.equals("TD")) {
         exportPDF_TD();
      }
   }

   public void exportPDF_C() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarC:datosCargoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Cargos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_SM() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarSM:datosSueldoMercadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "SueldosMercados_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    *
    * @throws IOException
    */
   public void exportPDF_CC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCC:datosCompetenciaCargoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CompetenciasCargos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    *
    * @throws IOException
    */
   public void exportPDF_TD() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTD:datosTipoDetalleExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "PropiedadesCargo_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      if ("C".equals(tablaActiva)) {
         exportXLS_C();
      } else if ("SM".equals(tablaActiva)) {
         exportXLS_SM();
      } else if ("CC".equals(tablaActiva)) {
         exportXLS_CC();
      } else if ("TD".equals(tablaActiva)) {
         exportXLS_TD();
      }
   }

   /**
    *
    * @throws IOException
    */
   public void exportXLS_C() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarC:datosCargoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Cargos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    *
    * @throws IOException
    */
   public void exportXLS_SM() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarSM:datosSueldoMercadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "SueldosMercados_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_CC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCC:datosCompetenciaCargoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CompetenciasCargos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    *
    * @throws IOException
    */
   public void exportXLS_TD() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTD:datosTipoDetalleExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "PropiedadesCargo_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      if (cargoTablaSeleccionado == null && sueldoMercadoSeleccionado == null && competenciaCargoSeleccionado == null && tipoDetalleSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("verificarRastrosTablasHistoricos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("verificarRastrosTablas').show()");
      }
   }

   /**
    *
    */
   public void verificarRastroCargoH() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("CARGOS")) {
         nombreTablaRastro = "Cargos";
         msnConfirmarRastroHistorico = "La tabla CARGOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroCargo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaCargos != null) {
         if (cargoTablaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(cargoTablaSeleccionado.getSecuencia(), "CARGOS");
            backUp = cargoTablaSeleccionado.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "Cargos";
               msnConfirmarRastro = "La tabla CARGOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
      }
   }

   /**
    *
    */
   public void verificarRastroSueldoMercadoH() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("SUELDOSMERCADOS")) {
         nombreTablaRastro = "SueldosMercados";
         msnConfirmarRastroHistorico = "La tabla SUELDOSMERCADOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroSueldoMercado() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaSueldosMercados != null) {
         if (sueldoMercadoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(sueldoMercadoSeleccionado.getSecuencia(), "SUELDOSMERCADOS");
            backUp = sueldoMercadoSeleccionado.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "SueldosMercados";
               msnConfirmarRastro = "La tabla SUELDOSMERCADOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
      }
   }

   /**
    *
    */
   public void verificarRastroCompetenciaH() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("COMPETENCIASCARGOS")) {
         nombreTablaRastro = "Competenciascargos";
         msnConfirmarRastroHistorico = "La tabla COMPETENCIASCARGOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroCompetencia() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaCompetenciasCargos != null) {
         if (competenciaCargoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(competenciaCargoSeleccionado.getSecuencia(), "COMPETENCIASCARGOS");
            backUp = competenciaCargoSeleccionado.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "Competenciascargos";
               msnConfirmarRastro = "La tabla COMPETENCIASCARGOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
      }
   }

   /**
    *
    */
   public void verificarRastroTipoDetalleH() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("TIPOSDETALLES")) {
         nombreTablaRastro = "TiposDetalles";
         msnConfirmarRastroHistorico = "La tabla TIPOSDETALLES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTipoDetalle() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaTiposDetalles != null) {
         if (tipoDetalleSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipoDetalleSeleccionado.getSecuencia(), "TIPOSDETALLES");
            backUp = tipoDetalleSeleccionado.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "TiposDetalles";
               msnConfirmarRastro = "La tabla TIPOSDETALLES tiene rastros para el registro seleccionado, ¿desea continuar?";
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
      }
   }

   /**
    *
    */
   public void lovEmpresas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambiosPagina == true) {
         competenciaCargoSeleccionado = null;
         sueldoMercadoSeleccionado = null;
         tipoDetalleSeleccionado = null;
         cualCelda = -1;
         contarRegistrosEmpresas();
         RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   /**
    *
    */
   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:nombreEmpresa");
      RequestContext.getCurrentInstance().update("form:nitEmpresa");
      filtrarLovEmpresas = null;
      aceptar = true;

      context.reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarEM");

      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
      empresaActual = empresaSeleccionada;
      listaCargos = null;
      getListaCargos();
      if (listaCargos.size() > 0) {
         cargoTablaSeleccionado = listaCargos.get(0);
         listaSueldosMercados = null;
         getListaSueldosMercados();
         if (listaSueldosMercados != null) {
            if (listaSueldosMercados.size() > 0) {
               sueldoMercadoSeleccionado = listaSueldosMercados.get(0);
            }
         }
         listaCompetenciasCargos = null;
         getListaCompetenciasCargos();
         if (listaCompetenciasCargos != null) {
            if (listaCompetenciasCargos.size() > 0) {
               competenciaCargoSeleccionado = listaCompetenciasCargos.get(0);
            }
         }
      }
      listaTiposDetalles = null;
      getListaTiposDetalles();
      /*
         * if (listaTiposDetalles != null) { int tamTipo =
         * listaTiposDetalles.size(); if (tamTipo > 0) { tipoDetalleSeleccionado
         * = listaTiposDetalles.get(0);
         * cambiarIndiceTipoDetalle(tipoDetalleSeleccionado, 0); } }
       */
      activoDetalleCargo = true;
      detalleCargo = new DetallesCargos();
      legendDetalleCargo = "";
      RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
      RequestContext.getCurrentInstance().update("form:detalleCargo");
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
      RequestContext.getCurrentInstance().update("form:datosCargo");
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
      RequestContext.getCurrentInstance().update("form:datosTipoDetalle");
   }

   /**
    *
    */
   public void cancelarCambioEmpresa() {
      filtrarLovEmpresas = null;
      empresaSeleccionada = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void validarExistenciaInformacionDetalleCargo() {
      tablaActiva = "DC";
      if (guardado == true && guardadoTipoDetalle == true) {
         if (detalleCargo.getSecuencia() == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('ingresoNuevoDetalleCargo').show()");
         } else {
            banderaDetalleCargo = 1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   /**
    *
    */
   public void crearDetalleCargo() {
      try {
         k++;
         l = BigInteger.valueOf(k);
         detalleCargo = new DetallesCargos();
         detalleCargo.setSecuencia(l);
         detalleCargo.setDescripcion(" ");
         detalleCargo.setCargo(cargoTablaSeleccionado);
         detalleCargo.setTipodetalle(tipoDetalleSeleccionado);

         administrarCargos.crearDetalleCargo(detalleCargo);
         FacesMessage msg = new FacesMessage("Información", "El registro de Detalle fue creado con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         detalleCargo = null;
         getDetalleCargo();
      } catch (Exception e) {
         System.out.println("Error crearDetalleCargo : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado de Detalle, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   /**
    *
    */
   public void dispararDialogoBuscarCargo() {
      if (guardado == true && guardadoCompetencia == true && guardadoDetalleCargo == true && guardadoSueldoMercado == true && guardadoTipoDetalle == true) {
         lovCargos = null;
         lovCargos = administrarCargos.listaCargosParaEmpresa(empresaActual.getSecuencia());
         cargoSeleccionado = new Cargos();
         filtrarLovCargos = null;
         aceptar = true;
         contarRegistrosCargos();
         RequestContext.getCurrentInstance().update("form:BuscarCargoDialogo");
         RequestContext.getCurrentInstance().update("form:lovBuscarCargo");
         RequestContext.getCurrentInstance().update("form:aceptarBC");
         RequestContext.getCurrentInstance().execute("PF('BuscarCargoDialogo').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void actualizarCargo() {
      listaCargos.clear();
      listaCargos.add(cargoSeleccionado);
      cargoSeleccionado = new Cargos();
      filtrarLovCargos = null;
      for (int i = 0; i < listaCargos.size(); i++) {
         if (listaCargos.get(i).getGruposalarial() == null) {
            listaCargos.get(i).setGruposalarial(new GruposSalariales());
         }
         if (listaCargos.get(i).getGrupoviatico() == null) {
            listaCargos.get(i).setGrupoviatico(new GruposViaticos());
         }
         if (listaCargos.get(i).getProcesoproductivo() == null) {
            listaCargos.get(i).setProcesoproductivo(new ProcesosProductivos());
         }
      }
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:aceptarBC");
      context.reset("form:lovBuscarCargo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBuscarCargo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('BuscarCargoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:BuscarCargoDialogo");
      RequestContext.getCurrentInstance().update("form:lovBuscarCargo");

      sueldoMercadoSeleccionado = null;
      competenciaCargoSeleccionado = null;
      tipoDetalleSeleccionado = null;
      banderaDetalleCargo = -1;
      if (listaCargos.size() > 0) {
         cargoTablaSeleccionado = listaCargos.get(0);
         listaSueldosMercados = null;
         getListaSueldosMercados();
         if (listaSueldosMercados != null) {
            if (listaSueldosMercados.size() > 0) {
               sueldoMercadoSeleccionado = listaSueldosMercados.get(0);
            }
         }
         listaCompetenciasCargos = null;
         getListaCompetenciasCargos();
         if (listaCompetenciasCargos != null) {
            if (listaCompetenciasCargos.size() > 0) {
               competenciaCargoSeleccionado = listaCompetenciasCargos.get(0);
            }
         }
      }
      listaTiposDetalles = null;
      getListaTiposDetalles();
      /*
         * if (listaTiposDetalles != null) { if (listaTiposDetalles.size() > 0)
         * { tipoDetalleSeleccionado = listaTiposDetalles.get(0);
         * cambiarIndiceTipoDetalle(tipoDetalleSeleccionado, 0); } }
       */
      if (banderaSueldoMercado == 1) {
         restaurarTablaSueldoM();
      }
      if (banderaCompetencia == 1) {
         restaurarTablaCompetencia();
      }
      if (banderaTipoDetalle == 1) {
         restaurarTablaTipoD();
      }
      activoDetalleCargo = true;
      legendDetalleCargo = "";
      detalleCargo = new DetallesCargos();
      RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
      RequestContext.getCurrentInstance().update("form:detalleCargo");
      RequestContext.getCurrentInstance().update("form:datosCargo");
      RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
      RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");
   }

   /**
    *
    */
   public void cancelarSeleccionCargo() {
      cargoSeleccionado = new Cargos();
      filtrarLovCargos = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovBuscarCargo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBuscarCargo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('BuscarCargoDialogo').hide()");
   }

   public void anularLOV() {
      activarLOV = true;
      tablaActiva = "";
   }

   /**
    *
    */
   public void mostrarTodos() {
      if (guardado == true && guardadoCompetencia == true && guardadoDetalleCargo == true && guardadoSueldoMercado == true && guardadoTipoDetalle == true) {
         listaCargos = null;
         getListaCargos();
         RequestContext context = RequestContext.getCurrentInstance();
         sueldoMercadoSeleccionado = null;
         competenciaCargoSeleccionado = null;
         tipoDetalleSeleccionado = null;
         banderaDetalleCargo = -1;
         if (listaCargos.size() > 0) {
            cargoTablaSeleccionado = listaCargos.get(0);
            listaSueldosMercados = null;
            getListaSueldosMercados();
            if (listaSueldosMercados != null) {
               if (listaSueldosMercados.size() > 0) {
                  sueldoMercadoSeleccionado = listaSueldosMercados.get(0);
               }
            }
            listaCompetenciasCargos = null;
            getListaCompetenciasCargos();
            if (listaCompetenciasCargos != null) {
               if (listaCompetenciasCargos.size() > 0) {
                  competenciaCargoSeleccionado = listaCompetenciasCargos.get(0);
               }
            }
         }
         listaTiposDetalles = null;
         getListaTiposDetalles();
         /*
             * if (listaTiposDetalles != null) { if (listaTiposDetalles.size() >
             * 0) { tipoDetalleSeleccionado = listaTiposDetalles.get(0);
             * cambiarIndiceTipoDetalle(tipoDetalleSeleccionado, 0); } }
          */
         if (banderaSueldoMercado == 1) {
            restaurarTablaSueldoM();
         }
         if (banderaCompetencia == 1) {
            restaurarTablaCompetencia();
         }
         if (banderaTipoDetalle == 1) {
            restaurarTablaTipoD();
         }
         activoDetalleCargo = true;
         legendDetalleCargo = "";
         detalleCargo = new DetallesCargos();
         RequestContext.getCurrentInstance().update("form:legendDetalleCargo");
         RequestContext.getCurrentInstance().update("form:detalleCargo");
         RequestContext.getCurrentInstance().update("form:datosCargo");
         RequestContext.getCurrentInstance().update("form:datosSueldoMercado");
         RequestContext.getCurrentInstance().update("form:datosCompetenciaCargo");

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   /**
    *
    */
   public void modificacionesDetalleCargo() {
      if (guardadoDetalleCargo == true) {
         guardadoDetalleCargo = false;
      }
      if (borrarDetalleCargo == true) {
         borrarDetalleCargo = false;
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tablaActiva.equals("C")) {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         cargoTablaSeleccionado = null;
         contarRegistrosC();

      } else if (tablaActiva.equals("SM")) {
         if (tipoListaSueldoMercado == 0) {
            tipoListaSueldoMercado = 1;
         }
         sueldoMercadoSeleccionado = null;
         contarRegistrosSM();

      } else if (tablaActiva.equals("CC")) {
         if (tipoListaCompetencia == 0) {
            tipoListaCompetencia = 1;
         }
         contarRegistrosCC();

      } else if (tablaActiva.equals("TD")) {
         if (tipoListaTipoDetalle == 0) {
            tipoListaTipoDetalle = 1;
         }
         tipoDetalleSeleccionado = null;
         contarRegistrosTD();

      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void contarRegistrosEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCargo");
   }

   public void contarRegistrosEnfo() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEnfoque");
   }

   public void contarRegistrosEvalComp() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEval");
   }

   public void contarRegistrosGrupoSal() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupoSalarial");
   }

   public void contarRegistrosGrupoVia() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupoViatico");
   }

   public void contarRegistrosProceProdu() {
      RequestContext.getCurrentInstance().update("form:infoRegistroProcesoProductivo");
   }

   public void contarRegistrosTiposEmp() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoEmpresa");
   }

   ////////////////////CONTAR REGISTROS/////////////////////
   public void contarRegistrosC() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroCargo");
   }

   public void contarRegistrosSM() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroSM");
   }

   public void contarRegistrosCC() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroCC");
   }

   public void contarRegistrosTD() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroTD");
   }

   //GETTERS AND SETTERS
   public List<Cargos> getListaCargos() {
      //try {
      System.out.println("getListaCargos . empresaActual : " + empresaActual);
      if (empresaActual != null) {
         System.out.println("empresaActual.getNombre : " + empresaActual.getNombre());
      }
      if (listaCargos == null && empresaActual != null) {
         listaCargos = administrarCargos.listaCargosParaEmpresa(empresaActual.getSecuencia());
         if (listaCargos != null) {
            for (int i = 0; i < listaCargos.size(); i++) {
               if (listaCargos.get(i).getGruposalarial() == null) {
                  listaCargos.get(i).setGruposalarial(new GruposSalariales());
               }
               if (listaCargos.get(i).getGrupoviatico() == null) {
                  listaCargos.get(i).setGrupoviatico(new GruposViaticos());
               }
               if (listaCargos.get(i).getProcesoproductivo() == null) {
                  listaCargos.get(i).setProcesoproductivo(new ProcesosProductivos());
               }
            }
         }
      }

      return listaCargos;

//        } catch (Exception e) {
//            System.out.println("Error...!! getListaCargos " + e.toString());
//            return null;
//        }
   }

   /**
    *
    * @param setListaTiposSueldos
    */
   public void setListaCargos(List<Cargos> setListaTiposSueldos) {
      this.listaCargos = setListaTiposSueldos;
   }

   /**
    *
    * @return
    */
   public List<Cargos> getFiltrarListaCargos() {
      return filtrarListaCargos;
   }

   /**
    *
    * @param setFiltrarListaTiposSueldos
    */
   public void setFiltrarListaCargos(List<Cargos> setFiltrarListaTiposSueldos) {
      this.filtrarListaCargos = setFiltrarListaTiposSueldos;
   }

   public Cargos getNuevoCargo() {
      return nuevoCargo;
   }

   /**
    *
    * @param setNuevoTipoSueldo
    */
   public void setNuevoCargo(Cargos setNuevoTipoSueldo) {
      this.nuevoCargo = setNuevoTipoSueldo;
   }

   /**
    *
    * @return
    */
   public boolean isAceptar() {
      return aceptar;
   }

   public Cargos getEditarCargo() {
      return editarCargo;
   }

   public void setEditarCargo(Cargos setEditarTipoSueldo) {
      this.editarCargo = setEditarTipoSueldo;
   }

   /**
    *
    * @return
    */
   public Cargos getDuplicarCargo() {
      return duplicarCargo;
   }

   /**
    *
    * @param setDuplicarTipoSueldo
    */
   public void setDuplicarCargo(Cargos setDuplicarTipoSueldo) {
      this.duplicarCargo = setDuplicarTipoSueldo;
   }

   /**
    *
    * @return
    */
   public List<SueldosMercados> getListaSueldosMercados() {
      if (listaSueldosMercados == null) {
         if (cargoTablaSeleccionado != null) {
            if (listaCargos != null) {
               if (listaCargos.size() > 0) {
                  listaSueldosMercados = administrarCargos.listaSueldosMercadosParaCargo(cargoTablaSeleccionado.getSecuencia());
               }
            }

            if (listaSueldosMercados != null) {
               for (int i = 0; i < listaSueldosMercados.size(); i++) {
                  if (listaSueldosMercados.get(i).getTipoempresa() == null) {
                     listaSueldosMercados.get(i).setTipoempresa(new TiposEmpresas());
                  }
               }
            }
         }
      }
      return listaSueldosMercados;
   }

   public void setListaSueldosMercados(List<SueldosMercados> setListaTSFormulasConceptos) {
      this.listaSueldosMercados = setListaTSFormulasConceptos;
   }

   public List<SueldosMercados> getFiltrarListaSueldosMercados() {
      return filtrarListaSueldosMercados;
   }

   public void setFiltrarListaSueldosMercados(List<SueldosMercados> setFiltrarListaTSFormulasConceptos) {
      this.filtrarListaSueldosMercados = setFiltrarListaTSFormulasConceptos;
   }

   public List<Cargos> getListCargosModificar() {
      return listCargosModificar;
   }

   public void setListCargosModificar(List<Cargos> setListTiposSueldosModificar) {
      this.listCargosModificar = setListTiposSueldosModificar;
   }

   public List<Cargos> getListCargosCrear() {
      return listCargosCrear;
   }

   public void setListCargosCrear(List<Cargos> setListTiposSueldosCrear) {
      this.listCargosCrear = setListTiposSueldosCrear;
   }

   public List<Cargos> getListCargosBorrar() {
      return listCargosBorrar;
   }

   public void setListCargosBorrar(List<Cargos> setListTiposSueldosBorrar) {
      this.listCargosBorrar = setListTiposSueldosBorrar;
   }

   public List<SueldosMercados> getListSueldosMercadosModificar() {
      return listSueldosMercadosModificar;
   }

   public void setListSueldosMercadosModificar(List<SueldosMercados> setListTSFormulasConceptosModificar) {
      this.listSueldosMercadosModificar = setListTSFormulasConceptosModificar;
   }

   public SueldosMercados getNuevoSueldoMercado() {
      return nuevoSueldoMercado;
   }

   public void setNuevoSueldoMercado(SueldosMercados setNuevoTSFormulaConcepto) {
      this.nuevoSueldoMercado = setNuevoTSFormulaConcepto;
   }

   public List<SueldosMercados> getListSueldosMercadosCrear() {
      return listSueldosMercadosCrear;
   }

   public void setListSueldosMercadosCrear(List<SueldosMercados> setListTSFormulasConceptosCrear) {
      this.listSueldosMercadosCrear = setListTSFormulasConceptosCrear;
   }

   public List<SueldosMercados> getListSueldosMercadosBorrar() {
      return listSueldosMercadosBorrar;
   }

   public void setListSueldosMercadosBorrar(List<SueldosMercados> setListTSFormulasConceptosBorrar) {
      this.listSueldosMercadosBorrar = setListTSFormulasConceptosBorrar;
   }

   public SueldosMercados getEditarSueldoMercado() {
      return editarSueldoMercado;
   }

   public void setEditarSueldoMercado(SueldosMercados setEditarTSFormulaConcepto) {
      this.editarSueldoMercado = setEditarTSFormulaConcepto;
   }

   public SueldosMercados getDuplicarSueldoMercado() {
      return duplicarSueldoMercado;
   }

   public void setDuplicarSueldoMercado(SueldosMercados setDuplicarTSFormulaConcepto) {
      this.duplicarSueldoMercado = setDuplicarTSFormulaConcepto;
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

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public BigInteger getBackUp() {
      return backUp;
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

   public void setNombreTabla(String setNombreTabla) {
      this.nombreTabla = setNombreTabla;
   }

   public List<GruposSalariales> getLovGruposSalariales() {
      lovGruposSalariales = administrarCargos.lovGruposSalariales();
      return lovGruposSalariales;
   }

   public void setLovGruposSalariales(List<GruposSalariales> setLovFormulas) {
      this.lovGruposSalariales = setLovFormulas;
   }

   public List<GruposSalariales> getFiltrarLovGruposSalariales() {
      return filtrarLovGruposSalariales;
   }

   public void setFiltrarLovGruposSalariales(List<GruposSalariales> setFiltrarLovFormulas) {
      this.filtrarLovGruposSalariales = setFiltrarLovFormulas;
   }

   public GruposSalariales getGrupoSalarialSeleccionado() {
      return grupoSalarialSeleccionado;
   }

   public void setGrupoSalarialSeleccionado(GruposSalariales setFormulaSeleccionado) {
      this.grupoSalarialSeleccionado = setFormulaSeleccionado;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTablaCargo() {
      return altoTablaCargo;
   }

   public void setAltoTablaCargo(String setAltoTablaTiposSueldos) {
      this.altoTablaCargo = setAltoTablaTiposSueldos;
   }

   public String getAltoTablaSueldoMercado() {
      return altoTablaSueldoMercado;
   }

   public void setAltoTablaSueldoMercado(String setAltoTablaTSFormulas) {
      this.altoTablaSueldoMercado = setAltoTablaTSFormulas;
   }

   public List<GruposViaticos> getLovGruposViaticos() {
      lovGruposViaticos = administrarCargos.lovGruposViaticos();

      return lovGruposViaticos;
   }

   public void setLovGruposViaticos(List<GruposViaticos> lovConceptos) {
      this.lovGruposViaticos = lovConceptos;
   }

   public List<GruposViaticos> getFiltrarLovGruposViaticos() {
      return filtrarLovGruposViaticos;
   }

   public void setFiltrarLovGruposViaticos(List<GruposViaticos> filtrarLovConceptos) {
      this.filtrarLovGruposViaticos = filtrarLovConceptos;
   }

   public GruposViaticos getGrupoViaticoSeleccionado() {
      return grupoViaticoSeleccionado;
   }

   public void setGrupoViaticoSeleccionado(GruposViaticos conceptoSeleccionado) {
      this.grupoViaticoSeleccionado = conceptoSeleccionado;
   }

   public List<Competenciascargos> getListaCompetenciasCargos() {
      if (listaCompetenciasCargos == null) {
         listaCompetenciasCargos = new ArrayList<Competenciascargos>();
         if (cargoTablaSeleccionado != null) {
            if (listaCargos != null) {
               if (listaCargos.size() > 0) {
                  listaCompetenciasCargos = administrarCargos.listaCompetenciasCargosParaCargo(cargoTablaSeleccionado.getSecuencia());
               }
            }

            if (listaCompetenciasCargos != null) {
               for (int i = 0; i < listaCompetenciasCargos.size(); i++) {
                  if (listaCompetenciasCargos.get(i).getEvalcompetencia() == null) {
                     listaCompetenciasCargos.get(i).setEvalcompetencia(new EvalCompetencias());
                  }
               }
            }
         }
      }
      return listaCompetenciasCargos;
   }

   public void setListaCompetenciasCargos(List<Competenciascargos> listaTSGruposTiposEntidades) {
      this.listaCompetenciasCargos = listaTSGruposTiposEntidades;
   }

   public List<Competenciascargos> getFiltrarListaCompetenciasCargos() {
      return filtrarListaCompetenciasCargos;
   }

   public void setFiltrarListaCompetenciasCargos(List<Competenciascargos> filtrarListaTSGruposTiposEntidades) {
      this.filtrarListaCompetenciasCargos = filtrarListaTSGruposTiposEntidades;
   }

   public List<Competenciascargos> getListCompetenciasCargosModificar() {
      return listCompetenciasCargosModificar;
   }

   public void setListCompetenciasCargosModificar(List<Competenciascargos> listTSGruposTiposEntidadesModificar) {
      this.listCompetenciasCargosModificar = listTSGruposTiposEntidadesModificar;
   }

   public Competenciascargos getNuevoCompetenciaCargo() {
      return nuevoCompetenciaCargo;
   }

   public void setNuevoCompetenciaCargo(Competenciascargos nuevoTSGrupoTipoEntidad) {
      this.nuevoCompetenciaCargo = nuevoTSGrupoTipoEntidad;
   }

   public List<Competenciascargos> getListCompetenciasCargosCrear() {
      return listCompetenciasCargosCrear;
   }

   public void setListCompetenciasCargosCrear(List<Competenciascargos> listTSGruposTiposEntidadessCrear) {
      this.listCompetenciasCargosCrear = listTSGruposTiposEntidadessCrear;
   }

   public List<Competenciascargos> getListCompetenciasCargosBorrar() {
      return listCompetenciasCargosBorrar;
   }

   public void setListCompetenciasCargosBorrar(List<Competenciascargos> listTSGruposTiposEntidadesBorrar) {
      this.listCompetenciasCargosBorrar = listTSGruposTiposEntidadesBorrar;
   }

   public Competenciascargos getEditarCompetenciaCargo() {
      return editarCompetenciaCargo;
   }

   public void setEditarCompetenciaCargo(Competenciascargos editarTSGrupoTipoEntidad) {
      this.editarCompetenciaCargo = editarTSGrupoTipoEntidad;
   }

   public Competenciascargos getDuplicarCompetenciaCargo() {
      return duplicarCompetenciaCargo;
   }

   public void setDuplicarCompetenciaCargo(Competenciascargos duplicarTSGrupoTipoEntidad) {
      this.duplicarCompetenciaCargo = duplicarTSGrupoTipoEntidad;
   }

   public List<ProcesosProductivos> getLovProcesosProductivos() {
      lovProcesosProductivos = administrarCargos.lovProcesosProductivos();

      return lovProcesosProductivos;
   }

   public void setLovProcesosProductivos(List<ProcesosProductivos> lovGruposTiposEntidades) {
      this.lovProcesosProductivos = lovGruposTiposEntidades;
   }

   public List<ProcesosProductivos> getFiltrarLovProcesosProductivos() {
      return filtrarLovProcesosProductivos;
   }

   public void setFiltrarLovProcesosProductivos(List<ProcesosProductivos> filtrarLovGruposTiposEntidades) {
      this.filtrarLovProcesosProductivos = filtrarLovGruposTiposEntidades;
   }

   public ProcesosProductivos getProcesoProductivoSeleccionado() {
      return procesoProductivoSeleccionado;
   }

   public void setProcesoProductivoSeleccionado(ProcesosProductivos setProcesoProductivoSeleccionado) {
      this.procesoProductivoSeleccionado = setProcesoProductivoSeleccionado;
   }

   public String getAltoTablaCompetencia() {
      return altoTablaCompetencia;
   }

   public void setAltoTablaCompetencia(String altoTablaTSGrupos) {
      this.altoTablaCompetencia = altoTablaTSGrupos;
   }

   public List<TiposDetalles> getListaTiposDetalles() {
      if (listaTiposDetalles == null) {
         listaTiposDetalles = new ArrayList<TiposDetalles>();
         listaTiposDetalles = administrarCargos.listaTiposDetalles();
         if (listaTiposDetalles != null) {
            for (int i = 0; i < listaTiposDetalles.size(); i++) {
               if (listaTiposDetalles.get(i).getEnfoque() == null) {
                  listaTiposDetalles.get(i).setEnfoque(new Enfoques());
               }
            }
         }
      }
      return listaTiposDetalles;
   }

   public void setListaTiposDetalles(List<TiposDetalles> listaTEFormulasConceptos) {
      this.listaTiposDetalles = listaTEFormulasConceptos;
   }

   public List<TiposDetalles> getFiltrarListaTiposDetalles() {
      return filtrarListaTiposDetalles;
   }

   public void setFiltrarListaTiposDetalles(List<TiposDetalles> filtrarListaTEFormulasConceptos) {
      this.filtrarListaTiposDetalles = filtrarListaTEFormulasConceptos;
   }

   public List<TiposDetalles> getListTiposDetallesModificar() {
      return listTiposDetallesModificar;
   }

   public void setListTiposDetallesModificar(List<TiposDetalles> listTEFormulasConceptosModificar) {
      this.listTiposDetallesModificar = listTEFormulasConceptosModificar;
   }

   public TiposDetalles getNuevoTipoDetalle() {
      return nuevoTipoDetalle;
   }

   public void setNuevoTipoDetalle(TiposDetalles nuevoTEFormulaConcepto) {
      this.nuevoTipoDetalle = nuevoTEFormulaConcepto;
   }

   public List<TiposDetalles> getListTiposDetallesCrear() {
      return listTiposDetallesCrear;
   }

   public void setListTiposDetallesCrear(List<TiposDetalles> listTEFormulasConceptosCrear) {
      this.listTiposDetallesCrear = listTEFormulasConceptosCrear;
   }

   public List<TiposDetalles> getListTiposDetallesBorrar() {
      return listTiposDetallesBorrar;
   }

   public void setListTiposDetallesBorrar(List<TiposDetalles> listTEFormulasConceptosBorrar) {
      this.listTiposDetallesBorrar = listTEFormulasConceptosBorrar;
   }

   public TiposDetalles getEditarTipoDetalle() {
      return editarTipoDetalle;
   }

   public void setEditarTipoDetalle(TiposDetalles editarTEFormulaConcepto) {
      this.editarTipoDetalle = editarTEFormulaConcepto;
   }

   public TiposDetalles getDuplicarTipoDetalle() {
      return duplicarTipoDetalle;
   }

   public void setDuplicarTipoDetalle(TiposDetalles duplicarTEFormulaConcepto) {
      this.duplicarTipoDetalle = duplicarTEFormulaConcepto;
   }

   public List<TiposEmpresas> getLovTiposEmpresas() {
      lovTiposEmpresas = administrarCargos.lovTiposEmpresas();

      return lovTiposEmpresas;
   }

   public void setLoviposEmpresas(List<TiposEmpresas> lovTiposEntidades) {
      this.lovTiposEmpresas = lovTiposEntidades;
   }

   public List<TiposEmpresas> getFiltrarLovTiposEmpresas() {
      return filtrarLovTiposEmpresas;
   }

   public void setFiltrarLovTiposEmpresas(List<TiposEmpresas> filtrarLovTiposEntidades) {
      this.filtrarLovTiposEmpresas = filtrarLovTiposEntidades;
   }

   public TiposEmpresas getTipoEmpresaSeleccionado() {
      return tipoEmpresaSeleccionado;
   }

   public void setTipoEmpresaSeleccionado(TiposEmpresas tipoEntidadSeleccionado) {
      this.tipoEmpresaSeleccionado = tipoEntidadSeleccionado;
   }

   public String getAltoTablaTipoDetalle() {
      return altoTablaTipoDetalle;
   }

   public void setAltoTablaTipoDetalle(String altoTablaTEFormulas) {
      this.altoTablaTipoDetalle = altoTablaTEFormulas;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public boolean isActivoSueldoMercado() {
      return activoSueldoMercado;
   }

   public void setActivoSueldoMercado(boolean activoFormulaConcepto) {
      this.activoSueldoMercado = activoFormulaConcepto;
   }

   public boolean isActivoCompetencia() {
      return activoCompetencia;
   }

   public void setActivoCompetencia(boolean activoGrupoDistribucion) {
      this.activoCompetencia = activoGrupoDistribucion;
   }

   public boolean isActivoTipoDetalle() {
      return activoTipoDetalle;
   }

   public void setActivoTipoDetalle(boolean activoTipoEntidad) {
      this.activoTipoDetalle = activoTipoEntidad;
   }

   public List<EvalCompetencias> getLovEvalCompetencias() {
      lovEvalCompetencias = administrarCargos.lovEvalCompetencias();

      return lovEvalCompetencias;
   }

   public void setLovEvalCompetencias(List<EvalCompetencias> lovEvalCompetencias) {
      this.lovEvalCompetencias = lovEvalCompetencias;
   }

   public List<EvalCompetencias> getFiltrarLovEvalCompetencias() {
      return filtrarLovEvalCompetencias;
   }

   public void setFiltrarLovEvalCompetencias(List<EvalCompetencias> filtrarLovEvalCompetencias) {
      this.filtrarLovEvalCompetencias = filtrarLovEvalCompetencias;
   }

   public EvalCompetencias getEvalCompetenciaSeleccionado() {
      return evalCompetenciaSeleccionado;
   }

   public void setEvalCompetenciaSeleccionado(EvalCompetencias evalCompetenciaSeleccionado) {
      this.evalCompetenciaSeleccionado = evalCompetenciaSeleccionado;
   }

   public List<Enfoques> getLovEnfoques() {
      lovEnfoques = administrarCargos.lovEnfoques();

      return lovEnfoques;
   }

   public void setLovEnfoques(List<Enfoques> lovEnfoques) {
      this.lovEnfoques = lovEnfoques;
   }

   public List<Enfoques> getFiltrarLovEnfoques() {
      return filtrarLovEnfoques;
   }

   public void setFiltrarLovEnfoques(List<Enfoques> filtrarLovEnfoques) {
      this.filtrarLovEnfoques = filtrarLovEnfoques;
   }

   public Enfoques getEnfoqueSeleccionado() {
      return enfoqueSeleccionado;
   }

   public void setEnfoqueSeleccionado(Enfoques enfoqueSeleccionado) {
      this.enfoqueSeleccionado = enfoqueSeleccionado;
   }

   public Empresas getEmpresaActual() {
      return empresaActual;
   }

   public void setEmpresaActual(Empresas empresaActual) {
      this.empresaActual = empresaActual;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarCargos.listaEmpresas();
      }

      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> listaEmpresas) {
      this.lovEmpresas = listaEmpresas;
   }

   public List<Empresas> getFiltrarLovEmpresas() {
      return filtrarLovEmpresas;
   }

   public void setFiltrarLovEmpresas(List<Empresas> filtrarListaEmpresas) {
      this.filtrarLovEmpresas = filtrarListaEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public String getLegendDetalleCargo() {
      return legendDetalleCargo;
   }

   public void setLegendDetalleCargo(String legendDetalleCargo) {
      this.legendDetalleCargo = legendDetalleCargo;
   }

   public DetallesCargos getDetalleCargo() {
      if (detalleCargo == null) {
         if (cargoTablaSeleccionado != null && tipoDetalleSeleccionado != null) {
            activoDetalleCargo = false;
            Cargos actualC = cargoTablaSeleccionado;
            TiposDetalles actualTD = tipoDetalleSeleccionado;

            detalleCargo = administrarCargos.detalleDelCargo(actualTD.getSecuencia(), actualC.getSecuencia());
         }
         if (detalleCargo == null) {
            detalleCargo = new DetallesCargos();
         }
      }
      return detalleCargo;
   }

   public void setDetalleCargo(DetallesCargos detalleCargo) {
      this.detalleCargo = detalleCargo;
   }

   public boolean isActivoDetalleCargo() {
      return activoDetalleCargo;
   }

   public void setActivoDetalleCargo(boolean activoDetalleCargo) {
      this.activoDetalleCargo = activoDetalleCargo;
   }

   public List<Cargos> getLovCargos() {
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getFiltrarLovCargos() {
      return filtrarLovCargos;
   }

   public void setFiltrarLovCargos(List<Cargos> filtrarLovCargos) {
      this.filtrarLovCargos = filtrarLovCargos;
   }

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public Cargos getCargoTablaSeleccionado() {
      return cargoTablaSeleccionado;
   }

   public void setCargoTablaSeleccionado(Cargos cargoTablaSeleccionado) {
      this.cargoTablaSeleccionado = cargoTablaSeleccionado;
   }

   public SueldosMercados getSueldoMercadoSeleccionado() {
      return sueldoMercadoSeleccionado;
   }

   public void setSueldoMercadoSeleccionado(SueldosMercados sueldoMercadoTablaSeleccionado) {
      this.sueldoMercadoSeleccionado = sueldoMercadoTablaSeleccionado;
   }

   public Competenciascargos getCompetenciaCargoSeleccionado() {
      return competenciaCargoSeleccionado;
   }

   public void setCompetenciaCargoSeleccionado(Competenciascargos competenciaCargoTablaSeleccionado) {
      this.competenciaCargoSeleccionado = competenciaCargoTablaSeleccionado;
   }

   public TiposDetalles getTipoDetalleSeleccionado() {
      return tipoDetalleSeleccionado;
   }

   public void setTipoDetalleSeleccionado(TiposDetalles tipoDetalleTablaSeleccionado) {
      this.tipoDetalleSeleccionado = tipoDetalleTablaSeleccionado;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public String getInfoRegistroC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCargo");
      infoRegistroC = String.valueOf(tabla.getRowCount());
      return infoRegistroC;
   }

   public String getInfoRegistroCC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCompetenciaCargo");
      infoRegistroCC = String.valueOf(tabla.getRowCount());
      return infoRegistroCC;
   }

   public String getInfoRegistroSM() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSueldoMercado");
      infoRegistroSM = String.valueOf(tabla.getRowCount());
      return infoRegistroSM;
   }

   public String getInfoRegistroTD() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoDetalle");
      infoRegistroTD = String.valueOf(tabla.getRowCount());
      return infoRegistroTD;
   }

   public String getInfoRegistroEnfoque() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEnfoque");
      infoRegistroEnfoque = String.valueOf(tabla.getRowCount());
      return infoRegistroEnfoque;
   }

   public String getInfoRegistroEvalCom() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEvalCompetencia");
      infoRegistroEvalCom = String.valueOf(tabla.getRowCount());
      return infoRegistroEvalCom;
   }

   public String getInfoRegistroCargos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovBuscarCargo");
      infoRegistroCargos = String.valueOf(tabla.getRowCount());
      return infoRegistroCargos;
   }

   public String getInfoRegistroGrupoV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGrupoViatico");
      infoRegistroGrupoV = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupoV;
   }

   public String getInfoRegistroProcesoP() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProcesoProductivo");
      infoRegistroProcesoP = String.valueOf(tabla.getRowCount());
      return infoRegistroProcesoP;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public String getInfoRegistroTipoEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoEmpresa");
      infoRegistroTipoEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoEmpresa;
   }

   public String getInfoRegistroGrupoS() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGrupoSalarial");
      infoRegistroGrupoS = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupoS;
   }

   public void setInfoRegistroEnfoque(String infoRegistroEnfoque) {
      this.infoRegistroEnfoque = infoRegistroEnfoque;
   }

   public void setInfoRegistroEvalCom(String infoRegistroEvalCom) {
      this.infoRegistroEvalCom = infoRegistroEvalCom;
   }

   public void setInfoRegistroCargos(String infoRegistroCargos) {
      this.infoRegistroCargos = infoRegistroCargos;
   }

   public void setInfoRegistroGrupoS(String infoRegistroGrupoS) {
      this.infoRegistroGrupoS = infoRegistroGrupoS;
   }

   public void setInfoRegistroGrupoV(String infoRegistroGrupoV) {
      this.infoRegistroGrupoV = infoRegistroGrupoV;
   }

   public void setInfoRegistroProcesoP(String infoRegistroProcesoP) {
      this.infoRegistroProcesoP = infoRegistroProcesoP;
   }

   public void setInfoRegistroTipoEmpresa(String infoRegistroTipoEmpresa) {
      this.infoRegistroTipoEmpresa = infoRegistroTipoEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }
}
