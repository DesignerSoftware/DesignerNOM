package Controlador;


import Entidades.Categorias;
import Entidades.ClasesCategorias;
import Entidades.Conceptos;
import Entidades.TiposSueldos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCategoriasInterface;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlCategoriaEsca implements Serializable {

    @EJB
    AdministrarCategoriasInterface administrarCategorias;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //
    private List<Categorias> listaCategorias;
    private List<Categorias> filtrarListaCategorias;
    //
    private List<ClasesCategorias> lovClasesCategorias;
    private ClasesCategorias claseCategoriaSelecionada;
    private List<ClasesCategorias> filtrarLovClasesCategorias;
    //
    private List<TiposSueldos> lovTiposSueldos;
    private TiposSueldos tipoSueldoSelecionada;
    private List<TiposSueldos> filtrarLovTiposSueldos;
    //
    private List<Conceptos> lovConceptos;
    private Conceptos conceptoSelecionada;
    private List<Conceptos> filtrarLovConceptos;
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    private Column categoriaCodigo, categoriaDescripcion, categoriaClase, categoriaTipo, categoriaConcepto;
    //Otros
    private boolean aceptar;
    private int index;
    //modificar
    private List<Categorias> listaCategoriasModificar;
    private boolean guardado, guardarOk;
    //crear VC
    public Categorias nuevaCategoria;
    private List<Categorias> listaCategoriasCrear;
    private BigInteger l;
    private int k;
    //borrar VC
    private List<Categorias> listaCategoriasBorrar;
    //editar celda
    private Categorias editarCategoria;
    private int cualCelda, tipoLista;
    private boolean cambioEditor, aceptarEditar;
    //duplicar
    private Categorias duplicarCategoria;
    private String claseCategoria, tipoSueldo, concepto;
    private boolean permitirIndex;
    private BigInteger secRegistro;
    private BigInteger backUpSecRegistro;
    private String auxDescripcion;
    private BigInteger auxCodigo;
    //
    private boolean cambiosPagina;
    //
    private String algoTabla;

    public ControlCategoriaEsca() {
        algoTabla = "200";
        cambiosPagina = true;
        listaCategorias = null;
        lovClasesCategorias = null;
        lovTiposSueldos = null;
        lovConceptos = null;
        aceptar = true;
        listaCategoriasBorrar = new ArrayList<Categorias>();
        listaCategoriasCrear = new ArrayList<Categorias>();
        k = 0;
        listaCategoriasModificar = new ArrayList<Categorias>();
        editarCategoria = new Categorias();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevaCategoria = new Categorias();
        nuevaCategoria.setClasecategoria(new ClasesCategorias());
        nuevaCategoria.setConcepto(new Conceptos());
        nuevaCategoria.setTiposueldo(new TiposSueldos());
        secRegistro = null;
        permitirIndex = true;
        backUpSecRegistro = null;
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
            administrarCategorias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void modificarCategoria(int indice) {
        if (tipoLista == 0) {
            if (listaCategorias.get(indice).getCodigo() == null || listaCategorias.get(indice).getDescripcion().isEmpty()) {
                listaCategorias.get(indice).setCodigo(auxCodigo);
                listaCategorias.get(indice).setDescripcion(auxDescripcion);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosCategoria");
                RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
            } else {
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (!listaCategoriasCrear.contains(listaCategorias.get(indice))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(listaCategorias.get(indice));
                    } else if (!listaCategoriasModificar.contains(listaCategorias.get(indice))) {
                        listaCategoriasModificar.add(listaCategorias.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            index = -1;
            secRegistro = null;
        } else {

            if (filtrarListaCategorias.get(indice).getCodigo() == null || filtrarListaCategorias.get(indice).getDescripcion().isEmpty()) {
                filtrarListaCategorias.get(indice).setCodigo(auxCodigo);
                filtrarListaCategorias.get(indice).setDescripcion(auxDescripcion);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosCategoria");
                RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
            } else {
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (!listaCategoriasCrear.contains(filtrarListaCategorias.get(indice))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(indice));
                    } else if (!listaCategoriasModificar.contains(filtrarListaCategorias.get(indice))) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }
            index = -1;
            secRegistro = null;
        }
    }

    public void modificarCategoria(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            if (valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listaCategorias.get(indice).getClasecategoria().setDescripcion(claseCategoria);
                } else {
                    filtrarListaCategorias.get(indice).getClasecategoria().setDescripcion(claseCategoria);
                }
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
                RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
                tipoActualizacion = 0;
            } else {
                if (tipoLista == 0) {
                    listaCategorias.get(indice).getClasecategoria().setDescripcion(claseCategoria);
                } else {
                    filtrarListaCategorias.get(indice).getClasecategoria().setDescripcion(claseCategoria);
                }
                for (int i = 0; i < lovClasesCategorias.size(); i++) {
                    if (lovClasesCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listaCategorias.get(indice).setClasecategoria(lovClasesCategorias.get(indiceUnicoElemento));
                    } else {
                        filtrarListaCategorias.get(indice).setClasecategoria(lovClasesCategorias.get(indiceUnicoElemento));
                    }
                    lovClasesCategorias.clear();
                    getLovClasesCategorias();
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listaCategorias.get(indice).getTiposueldo().setDescripcion(tipoSueldo);
                } else {
                    filtrarListaCategorias.get(indice).getTiposueldo().setDescripcion(tipoSueldo);
                }
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
                tipoActualizacion = 0;
            }
            if (tipoLista == 0) {
                listaCategorias.get(indice).getTiposueldo().setDescripcion(tipoSueldo);
            } else {
                filtrarListaCategorias.get(indice).getTiposueldo().setDescripcion(tipoSueldo);
            }
            for (int i = 0; i < lovTiposSueldos.size(); i++) {
                if (lovTiposSueldos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaCategorias.get(indice).setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                } else {
                    filtrarListaCategorias.get(indice).setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                }
                lovTiposSueldos.clear();
                getLovTiposSueldos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (valorConfirmar.isEmpty()) {
                listaCategorias.get(indice).setConcepto(new Conceptos());
                coincidencias = 1;
            } else {
                if (tipoLista == 0) {
                    listaCategorias.get(indice).getConcepto().setDescripcion(concepto);
                } else {
                    filtrarListaCategorias.get(indice).getConcepto().setDescripcion(concepto);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listaCategorias.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarListaCategorias.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                    }
                    lovConceptos.clear();
                    getLovConceptos();
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaCategoriasCrear.contains(listaCategorias.get(indice))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(listaCategorias.get(indice));
                    } else if (!listaCategoriasModificar.contains(listaCategorias.get(indice))) {
                        listaCategoriasModificar.add(listaCategorias.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaCategoriasCrear.contains(filtrarListaCategorias.get(indice))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(indice));
                    } else if (!listaCategoriasModificar.contains(filtrarListaCategorias.get(indice))) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistro = null;
            }
        }
        RequestContext.getCurrentInstance().update("form:datosCategoria");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CLASE")) {
            if (tipoNuevo == 1) {
                claseCategoria = nuevaCategoria.getClasecategoria().getDescripcion();
            } else if (tipoNuevo == 2) {
                claseCategoria = duplicarCategoria.getClasecategoria().getDescripcion();
            }
        }
        if (Campo.equals("TIPO")) {
            if (tipoNuevo == 1) {
                tipoSueldo = nuevaCategoria.getTiposueldo().getDescripcion();
            } else if (tipoNuevo == 2) {
                tipoSueldo = duplicarCategoria.getTiposueldo().getDescripcion();
            }
        }
        if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                tipoSueldo = nuevaCategoria.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                tipoSueldo = duplicarCategoria.getConcepto().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            if (valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaCategoria.getClasecategoria().setDescripcion(claseCategoria);
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaClaseCategoria");
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.getClasecategoria().setDescripcion(claseCategoria);
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClaseCategoria");
                }
                tipoActualizacion = tipoNuevo;
                RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
                RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
            } else {
                if (tipoNuevo == 1) {
                    nuevaCategoria.getClasecategoria().setDescripcion(claseCategoria);
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.getClasecategoria().setDescripcion(claseCategoria);
                }
                for (int i = 0; i < lovClasesCategorias.size(); i++) {
                    if (lovClasesCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaCategoria.setClasecategoria(lovClasesCategorias.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaClaseCategoria");
                    } else if (tipoNuevo == 2) {
                        duplicarCategoria.setClasecategoria(lovClasesCategorias.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClaseCategoria");
                    }
                    lovClasesCategorias.clear();
                    getLovClasesCategorias();
                } else {
                    RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaClaseCategoria");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClaseCategoria");
                    }
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaCategoria.getTiposueldo().setDescripcion(tipoSueldo);
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoSueldo");
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.getTiposueldo().setDescripcion(tipoSueldo);
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoSueldo");
                }
                RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
                tipoActualizacion = tipoNuevo;
            } else {
                if (tipoNuevo == 1) {
                    nuevaCategoria.getTiposueldo().setDescripcion(tipoSueldo);
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.getTiposueldo().setDescripcion(tipoSueldo);
                }
                for (int i = 0; i < lovTiposSueldos.size(); i++) {
                    if (lovTiposSueldos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaCategoria.setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoSueldo");
                    } else if (tipoNuevo == 2) {
                        duplicarCategoria.setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoSueldo");
                    }
                    lovTiposSueldos.clear();
                    getLovTiposSueldos();
                } else {
                    RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoSueldo");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoSueldo");
                    }
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaCategoria.setConcepto(new Conceptos());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.setConcepto(new Conceptos());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
                }
                lovConceptos = null;
                getLovConceptos();
            } else {
                if (tipoNuevo == 1) {
                    nuevaCategoria.getConcepto().setDescripcion(tipoSueldo);
                } else if (tipoNuevo == 2) {
                    duplicarCategoria.getConcepto().setDescripcion(tipoSueldo);
                }
                for (int i = 0; i < lovConceptos.size(); i++) {
                    if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaCategoria.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
                    } else if (tipoNuevo == 2) {
                        duplicarCategoria.setConcepto(lovConceptos.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
                    }
                    lovConceptos.clear();
                    getLovConceptos();
                } else {
                    RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
                    }
                }
            }
        }
    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla
     * VigenciasReformasLaborales
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                auxCodigo = listaCategorias.get(index).getCodigo();
                auxDescripcion = listaCategorias.get(index).getDescripcion();
                secRegistro = listaCategorias.get(index).getSecuencia();
                if (cualCelda == 2) {
                    claseCategoria = listaCategorias.get(index).getClasecategoria().getDescripcion();
                }
                if (cualCelda == 3) {
                    tipoSueldo = listaCategorias.get(index).getTiposueldo().getDescripcion();
                }
                if (cualCelda == 4) {
                    concepto = listaCategorias.get(index).getConcepto().getDescripcion();
                }
            }
            if (tipoLista == 1) {
                auxCodigo = filtrarListaCategorias.get(index).getCodigo();
                auxDescripcion = filtrarListaCategorias.get(index).getDescripcion();
                secRegistro = filtrarListaCategorias.get(index).getSecuencia();
                if (cualCelda == 2) {
                    claseCategoria = filtrarListaCategorias.get(index).getClasecategoria().getDescripcion();
                }
                if (cualCelda == 3) {
                    tipoSueldo = filtrarListaCategorias.get(index).getTiposueldo().getDescripcion();
                }
                if (cualCelda == 4) {
                    concepto = filtrarListaCategorias.get(index).getConcepto().getDescripcion();
                }
            }
        }
    }
    //GUARDAR

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacion();
        salir();
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina
     */
    public void guardarCambios() {
        if (guardado == false) {
            if (!listaCategoriasBorrar.isEmpty()) {
                administrarCategorias.borrarCategorias(listaCategoriasBorrar);
                listaCategoriasBorrar.clear();
            }
            if (!listaCategoriasCrear.isEmpty()) {
                administrarCategorias.crearCategorias(listaCategoriasCrear);
                listaCategoriasCrear.clear();
            }
            if (!listaCategoriasModificar.isEmpty()) {
                administrarCategorias.editarCategorias(listaCategoriasModificar);
                listaCategoriasModificar.clear();
            }
            cambiosPagina = true;
            listaCategorias = null;
            getListaCategorias();
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
        }
        index = -1;
        secRegistro = null;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            algoTabla = "200";
            categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaCodigo");
            categoriaCodigo.setFilterStyle("display: none; visibility: hidden;");
            categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaClase");
            categoriaClase.setFilterStyle("display: none; visibility: hidden;");
            categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaDescripcion");
            categoriaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            categoriaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaTipo");
            categoriaTipo.setFilterStyle("display: none; visibility: hidden;");
            categoriaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaConcepto");
            categoriaConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            bandera = 0;
            filtrarListaCategorias = null;
            tipoLista = 0;
        }
        listaCategoriasBorrar.clear();
        listaCategoriasCrear.clear();
        listaCategoriasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaCategorias = null;
        guardado = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosCategoria");
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarCategoria = listaCategorias.get(index);
            }
            if (tipoLista == 1) {
                editarCategoria = filtrarListaCategorias.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarClaseD");
                RequestContext.getCurrentInstance().execute("PF('editarClaseD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoD");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoD').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    //CREAR VU
    /**
     * Metodo que se encarga de agregar un nueva VigenciaReformaLaboral
     */
    public void agregarNuevaCategoria() {
        if (nuevaCategoria.getCodigo() != null && nuevaCategoria.getClasecategoria().getSecuencia() != null && nuevaCategoria.getTiposueldo().getSecuencia() != null) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                algoTabla = "200";
                categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaCodigo");
                categoriaCodigo.setFilterStyle("display: none; visibility: hidden;");
                categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaClase");
                categoriaClase.setFilterStyle("display: none; visibility: hidden;");
                categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaDescripcion");
                categoriaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                categoriaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaTipo");
                categoriaTipo.setFilterStyle("display: none; visibility: hidden;");
                categoriaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaConcepto");
                categoriaConcepto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosCategoria");
                bandera = 0;
                filtrarListaCategorias = null;
                tipoLista = 0;
            }
            cambiosPagina = false;
            k++;
            l = BigInteger.valueOf(k);
            nuevaCategoria.setSecuencia(l);
            listaCategoriasCrear.add(nuevaCategoria);
            listaCategorias.add(nuevaCategoria);
            nuevaCategoria = new Categorias();
            nuevaCategoria.setClasecategoria(new ClasesCategorias());
            nuevaCategoria.setConcepto(new Conceptos());
            nuevaCategoria.setTiposueldo(new TiposSueldos());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCategoria').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            index = -1;
            secRegistro = null;

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }
//LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevaCategoria() {
        nuevaCategoria = new Categorias();
        nuevaCategoria.setClasecategoria(new ClasesCategorias());
        nuevaCategoria.setConcepto(new Conceptos());
        nuevaCategoria.setTiposueldo(new TiposSueldos());
        index = -1;
        secRegistro = null;
    }
    //DUPLICAR VC

    /**
     * Metodo que duplica una vigencia especifica dado por la posicion de la
     * fila
     */
    public void duplicarCategoriaM() {
        if (index >= 0) {
            duplicarCategoria = new Categorias();
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
                duplicarCategoria.setSecuencia(l);
                duplicarCategoria.setClasecategoria(listaCategorias.get(index).getClasecategoria());
                duplicarCategoria.setCodigo(listaCategorias.get(index).getCodigo());
                duplicarCategoria.setConcepto(listaCategorias.get(index).getConcepto());
                duplicarCategoria.setTiposueldo(listaCategorias.get(index).getTiposueldo());
                duplicarCategoria.setDescripcion(listaCategorias.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarCategoria.setSecuencia(l);
                duplicarCategoria.setClasecategoria(filtrarListaCategorias.get(index).getClasecategoria());
                duplicarCategoria.setCodigo(filtrarListaCategorias.get(index).getCodigo());
                duplicarCategoria.setConcepto(filtrarListaCategorias.get(index).getConcepto());
                duplicarCategoria.setTiposueldo(filtrarListaCategorias.get(index).getTiposueldo());
                duplicarCategoria.setDescripcion(filtrarListaCategorias.get(index).getDescripcion());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCate");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCategoria').show()");
            index = -1;
            secRegistro = null;
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasReformasLaborales
     */
    public void confirmarDuplicar() {
        if (duplicarCategoria.getCodigo() != null && duplicarCategoria.getClasecategoria().getSecuencia() != null && duplicarCategoria.getTiposueldo().getSecuencia() != null) {
            cambiosPagina = false;
            listaCategorias.add(duplicarCategoria);
            listaCategoriasCrear.add(duplicarCategoria);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCategoria').hide()");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                algoTabla = "200";
                categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaCodigo");
                categoriaCodigo.setFilterStyle("display: none; visibility: hidden;");
                categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaClase");
                categoriaClase.setFilterStyle("display: none; visibility: hidden;");
                categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaDescripcion");
                categoriaDescripcion.setFilterStyle("display: none; visibility: hidden;");
                categoriaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaTipo");
                categoriaTipo.setFilterStyle("display: none; visibility: hidden;");
                categoriaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaConcepto");
                categoriaConcepto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosCategoria");
                bandera = 0;
                filtrarListaCategorias = null;
                tipoLista = 0;
            }
            duplicarCategoria = new Categorias();
            duplicarCategoria.setClasecategoria(new ClasesCategorias());
            duplicarCategoria.setConcepto(new Conceptos());
            duplicarCategoria.setTiposueldo(new TiposSueldos());
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia
     */
    public void limpiarduplicarCategoria() {
        index = -1;
        secRegistro = null;
        duplicarCategoria = new Categorias();
        duplicarCategoria.setClasecategoria(new ClasesCategorias());
        duplicarCategoria.setConcepto(new Conceptos());
        duplicarCategoria.setTiposueldo(new TiposSueldos());
    }

    //BORRAR VC
    /**
     * Metodo que borra las vigencias seleccionadas
     */
    public void borrarCategoria() {
        if (index >= 0) {
            cambiosPagina = false;
            if (tipoLista == 0) {
                if (!listaCategoriasModificar.isEmpty() && listaCategoriasModificar.contains(listaCategorias.get(index))) {
                    int modIndex = listaCategoriasModificar.indexOf(listaCategorias.get(index));
                    listaCategoriasModificar.remove(modIndex);
                    listaCategoriasBorrar.add(listaCategorias.get(index));
                } else if (!listaCategoriasCrear.isEmpty() && listaCategoriasCrear.contains(listaCategorias.get(index))) {
                    int crearIndex = listaCategoriasCrear.indexOf(listaCategorias.get(index));
                    listaCategoriasCrear.remove(crearIndex);
                } else {
                    listaCategoriasBorrar.add(listaCategorias.get(index));
                }
                listaCategorias.remove(index);
            }
            if (tipoLista == 1) {
                if (!listaCategoriasModificar.isEmpty() && listaCategoriasModificar.contains(filtrarListaCategorias.get(index))) {
                    int modIndex = listaCategoriasModificar.indexOf(filtrarListaCategorias.get(index));
                    listaCategoriasModificar.remove(modIndex);
                    listaCategoriasBorrar.add(filtrarListaCategorias.get(index));
                } else if (!listaCategoriasCrear.isEmpty() && listaCategoriasCrear.contains(filtrarListaCategorias.get(index))) {
                    int crearIndex = listaCategoriasCrear.indexOf(filtrarListaCategorias.get(index));
                    listaCategoriasCrear.remove(crearIndex);
                } else {
                    listaCategoriasBorrar.add(filtrarListaCategorias.get(index));
                }
                int catIndex = listaCategorias.indexOf(filtrarListaCategorias.get(index));
                listaCategorias.remove(catIndex);
                filtrarListaCategorias.remove(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }
    //CTRL + F11 ACTIVAR/DESACTIVAR

    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {

        if (bandera == 0) {
            algoTabla = "180";
            categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaCodigo");
            categoriaCodigo.setFilterStyle("width: 85% !important");
            categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaClase");
            categoriaClase.setFilterStyle("width: 85% !important");
            categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaDescripcion");
            categoriaDescripcion.setFilterStyle("width: 85% !important");
            categoriaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaTipo");
            categoriaTipo.setFilterStyle("width: 85% !important");
            categoriaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaConcepto");
            categoriaConcepto.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            bandera = 1;
        } else if (bandera == 1) {
            //CERRAR FILTRADO
            algoTabla = "200";
            categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaCodigo");
            categoriaCodigo.setFilterStyle("display: none; visibility: hidden;");
            categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaClase");
            categoriaClase.setFilterStyle("display: none; visibility: hidden;");
            categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaDescripcion");
            categoriaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            categoriaTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaTipo");
            categoriaTipo.setFilterStyle("display: none; visibility: hidden;");
            categoriaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:categoriaConcepto");
            categoriaConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            bandera = 0;
            filtrarListaCategorias = null;
            tipoLista = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            algoTabla = "200";
            categoriaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:escalafonCodigo");
            categoriaCodigo.setFilterStyle("display: none; visibility: hidden;");
            categoriaClase = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:escalafonSubCategoria");
            categoriaClase.setFilterStyle("display: none; visibility: hidden;");
            categoriaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCategoria:escalafonCategoria");
            categoriaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            bandera = 0;
            filtrarListaCategorias = null;
            tipoLista = 0;
        }
        listaCategoriasBorrar.clear();
        listaCategoriasCrear.clear();
        listaCategoriasModificar.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listaCategorias = null;
        guardado = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)

    public void asignarIndex(Integer indice, int LND, int tipo) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (tipo == 0) {
            RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
        }
        if (tipo == 1) {
            RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
        }
        if (tipo == 2) {
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
        }
    }

    public void actualizarClaseCategoria() {
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaCategorias.get(index).setClasecategoria(claseCategoriaSelecionada);
                if (!listaCategoriasCrear.contains(listaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(listaCategorias.get(index))) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    }
                }
            } else {
                filtrarListaCategorias.get(index).setClasecategoria(claseCategoriaSelecionada);
                if (!listaCategoriasCrear.contains(filtrarListaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(filtrarListaCategorias.get(index))) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaCategoria.setClasecategoria(claseCategoriaSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCategoria");
        } else if (tipoActualizacion == 2) {
            duplicarCategoria.setClasecategoria(claseCategoriaSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCate");
        }
        filtrarLovClasesCategorias = null;
        claseCategoriaSelecionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
         RequestContext.getCurrentInstance().update("form:lovClaseCategoria");
         RequestContext.getCurrentInstance().update("form:aceptarCCat");*/
        context.reset("form:lovClaseCategoria:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovClaseCategoria').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').hide()");
    }

    public void cancelarCambioClaseCategoria() {
        filtrarLovClasesCategorias = null;
        claseCategoriaSelecionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovClaseCategoria:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovClaseCategoria').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').hide()");
    }

    public void actualizarTipoSueldo() {
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaCategorias.get(index).setTiposueldo(tipoSueldoSelecionada);
                if (!listaCategoriasCrear.contains(listaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(listaCategorias.get(index))) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    }
                }
            } else {
                filtrarListaCategorias.get(index).setTiposueldo(tipoSueldoSelecionada);
                if (!listaCategoriasCrear.contains(filtrarListaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(filtrarListaCategorias.get(index))) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaCategoria.setTiposueldo(tipoSueldoSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCategoria");
        } else if (tipoActualizacion == 2) {
            duplicarCategoria.setTiposueldo(tipoSueldoSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCate");
        }
        filtrarLovTiposSueldos = null;
        tipoSueldoSelecionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoSueldo");
         RequestContext.getCurrentInstance().update("form:aceptarT");*/
        context.reset("form:lovTipoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').hide()");
    }

    public void cancelarCambioTipoSueldo() {
        filtrarLovTiposSueldos = null;
        tipoSueldoSelecionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').hide()");
    }

    public void actualizarConcepto() {
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaCategorias.get(index).setConcepto(conceptoSelecionada);
                if (!listaCategoriasCrear.contains(listaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(listaCategorias.get(index))) {
                        listaCategoriasModificar.add(listaCategorias.get(index));
                    }
                }
            } else {
                filtrarListaCategorias.get(index).setConcepto(conceptoSelecionada);
                if (!listaCategoriasCrear.contains(filtrarListaCategorias.get(index))) {
                    if (listaCategoriasModificar.isEmpty()) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    } else if (!listaCategoriasModificar.contains(filtrarListaCategorias.get(index))) {
                        listaCategoriasModificar.add(filtrarListaCategorias.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCategoria");
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevaCategoria.setConcepto(conceptoSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCategoria");
        } else if (tipoActualizacion == 2) {
            duplicarCategoria.setConcepto(conceptoSelecionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCate");
        }
        filtrarLovConceptos = null;
        conceptoSelecionada = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
         RequestContext.getCurrentInstance().update("form:lovConcepto");
         RequestContext.getCurrentInstance().update("form:aceptarC");*/
        context.reset("form:lovConcepto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
    }

    public void cancelarCambioConcepto() {
        filtrarLovConceptos = null;
        conceptoSelecionada = null;
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

    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:ClaseCategoriaDialogo");
                RequestContext.getCurrentInstance().execute("PF('ClaseCategoriaDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("form:TipoSueldoDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoSueldoDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCategoriaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Categorias_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCategoriaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Categorias_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaCategorias != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CLASESCATEGORIAS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("CLASESCATEGORIAS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }
    //GETTERS AND SETTERS

    public List<Categorias> getListaCategorias() {
        try {
            if (listaCategorias == null) {
                return listaCategorias = administrarCategorias.listaCategorias();
            }
            if (!listaCategorias.isEmpty()) {
                for (int i = 0; i < listaCategorias.size(); i++) {
                    if (listaCategorias.get(i).getConcepto() == null) {
                        listaCategorias.get(i).setConcepto(new Conceptos());
                    }
                }
            }
            return listaCategorias;
        } catch (Exception e) {
            System.out.println("Error...!! getListaCategorias ");
            return null;
        }
    }

    public void setListaCategorias(List<Categorias> setListaCategorias) {
        this.listaCategorias = setListaCategorias;
    }

    public List<Categorias> getFiltrarListaCategorias() {
        return filtrarListaCategorias;
    }

    public void setFiltrarListaCategorias(List<Categorias> setFiltrarListaCategorias) {
        this.filtrarListaCategorias = setFiltrarListaCategorias;
    }

    public Categorias getNuevaCategoria() {
        return nuevaCategoria;
    }

    public void setNuevaCategoria(Categorias setNuevaCategoria) {
        this.nuevaCategoria = setNuevaCategoria;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public List<TiposSueldos> getLovTiposSueldos() {
        if (lovTiposSueldos == null) {
            lovTiposSueldos = new ArrayList<TiposSueldos>();
            lovTiposSueldos = administrarCategorias.lovTiposSueldos();
        }
        return lovTiposSueldos;
    }

    public void setLovTiposSueldos(List<TiposSueldos> setLovTiposSueldos) {
        this.lovTiposSueldos = setLovTiposSueldos;
    }

    public TiposSueldos getTipoSueldoSelecionada() {
        return tipoSueldoSelecionada;
    }

    public void setTipoSueldoSelecionada(TiposSueldos setTipoSueldoSelecionada) {
        this.tipoSueldoSelecionada = setTipoSueldoSelecionada;
    }

    public List<TiposSueldos> getFiltrarLovTiposSueldos() {
        return filtrarLovTiposSueldos;
    }

    public void setFiltrarLovTiposSueldos(List<TiposSueldos> setFiltrarLovTiposSueldos) {
        this.filtrarLovTiposSueldos = setFiltrarLovTiposSueldos;
    }

    public List<Conceptos> getLovConceptos() {
        if (lovConceptos == null) {
            lovConceptos = new ArrayList<Conceptos>();
            lovConceptos = administrarCategorias.lovConceptos();
        }
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> lovConceptos) {
        this.lovConceptos = lovConceptos;
    }

    public Conceptos getConceptoSelecionada() {
        return conceptoSelecionada;
    }

    public void setConceptoSelecionada(Conceptos conceptoSelecionada) {
        this.conceptoSelecionada = conceptoSelecionada;
    }

    public List<Conceptos> getFiltrarLovConceptos() {
        return filtrarLovConceptos;
    }

    public void setFiltrarLovConceptos(List<Conceptos> filtrarLovConceptos) {
        this.filtrarLovConceptos = filtrarLovConceptos;
    }

    public List<Categorias> getListaCategoriasModificar() {
        return listaCategoriasModificar;
    }

    public void setListaCategoriasModificar(List<Categorias> setListaCategoriasModificar) {
        this.listaCategoriasModificar = setListaCategoriasModificar;
    }

    public List<Categorias> getListaCategoriasCrear() {
        return listaCategoriasCrear;
    }

    public void setListaCategoriasCrear(List<Categorias> setListaCategoriasCrear) {
        this.listaCategoriasCrear = setListaCategoriasCrear;
    }

    public List<Categorias> getListaCategoriasBorrar() {
        return listaCategoriasBorrar;
    }

    public void setListaCategoriasBorrar(List<Categorias> setListaCategoriasBorrar) {
        this.listaCategoriasBorrar = setListaCategoriasBorrar;
    }

    public List<ClasesCategorias> getLovClasesCategorias() {
        if (lovClasesCategorias == null) {
            lovClasesCategorias = new ArrayList<ClasesCategorias>();
            lovClasesCategorias = administrarCategorias.lovClasesCategorias();
        }
        return lovClasesCategorias;
    }

    public void setLovClasesCategorias(List<ClasesCategorias> setLovClasesCategorias) {
        this.lovClasesCategorias = setLovClasesCategorias;
    }

    public List<ClasesCategorias> getFiltrarLovClasesCategorias() {
        return filtrarLovClasesCategorias;
    }

    public void setFiltrarLovClasesCategorias(List<ClasesCategorias> setFiltrarLovClasesCategorias) {
        this.filtrarLovClasesCategorias = setFiltrarLovClasesCategorias;
    }

    public Categorias getEditarCategoria() {
        return editarCategoria;
    }

    public void setEditarCategoria(Categorias setEditarCategoria) {
        this.editarCategoria = setEditarCategoria;
    }

    public Categorias getDuplicarCategoria() {
        return duplicarCategoria;
    }

    public void setDuplicarCategoria(Categorias setDuplicarCategoria) {
        this.duplicarCategoria = setDuplicarCategoria;
    }

    public ClasesCategorias getClaseCategoriaSelecionada() {
        return claseCategoriaSelecionada;
    }

    public void setClaseCategoriaSelecionada(ClasesCategorias setClaseCategoriaSelecionada) {
        this.claseCategoriaSelecionada = setClaseCategoriaSelecionada;
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

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public String getAlgoTabla() {
        return algoTabla;
    }

    public void setAlgoTabla(String algoTabla) {
        this.algoTabla = algoTabla;
    }
}
