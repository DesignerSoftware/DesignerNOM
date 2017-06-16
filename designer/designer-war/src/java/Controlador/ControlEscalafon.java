package Controlador;

import Entidades.Categorias;
import Entidades.Escalafones;
import Entidades.SubCategorias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEscalafonesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlEscalafon implements Serializable {

   @EJB
   AdministrarEscalafonesInterface administrarEscalafones;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //
   private List<Escalafones> listaEscalafones;
   private List<Escalafones> filtrarListaEscalafones;
   private Escalafones escalafonTablaSeleccionado;
   //
   private List<Categorias> lovCategorias;
   private Categorias categoriaSelecionada;
   private List<Categorias> filtrarLovCategorias;
   private String infoRegistroCategoria;
   //
   private List<SubCategorias> lovSubCategorias;
   private SubCategorias subCategoriaSelecionada;
   private List<SubCategorias> filtrarLovSubCategorias;
   private String infoRegistroSubCategoria;

   private int tipoActualizacion;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   private Column escalafonCodigo, escalafonCategoria, escalafonSubCategoria;
   //Otros
   private boolean aceptar;
   private int index;
   //modificar
   private List<Escalafones> listaEscalafonesModificar;
   private boolean guardado;
   //crear VC
   public Escalafones nuevaEscalafon;
   private List<Escalafones> listaEscalafonesCrear;
   private BigInteger l;
   private int k;
   //borrar VC
   private List<Escalafones> listaEscalafonesBorrar;
   //editar celda
   private Escalafones editarEscalafon;
   private int cualCelda, tipoLista;
   //duplicar
   private Escalafones duplicarEscalafon;
   private String categoria, subCategoria;
   private boolean permitirIndex;
   private BigInteger secRegistro;
   private BigInteger backUpSecRegistro;
   private String auxCodigo;
   //
   private boolean cambiosPagina;
   //
   private String algoTabla;
   //
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEscalafon() {
      algoTabla = "300";
      cambiosPagina = true;
      lovCategorias = null;
      lovSubCategorias = null;
      aceptar = true;
      listaEscalafonesBorrar = new ArrayList<Escalafones>();
      listaEscalafonesCrear = new ArrayList<Escalafones>();
      k = 0;
      listaEscalafonesModificar = new ArrayList<Escalafones>();
      editarEscalafon = new Escalafones();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaEscalafon = new Escalafones();
      nuevaEscalafon.setSubcategoria(new SubCategorias());
      nuevaEscalafon.setCategoria(new Categorias());
      secRegistro = null;
      permitirIndex = true;
      backUpSecRegistro = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
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
      String pagActual = "escalafon";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
         //         if (pag.equals("rastrotabla")) {
         //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public void limpiarListasValor() {
      lovCategorias = null;
      lovSubCategorias = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEscalafones.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         listaEscalafones = null;
         getListaEscalafones();
         if (listaEscalafones != null) {
            infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
         } else {
            infoRegistro = "Cantidad de registros : 0";
         }
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void modificarEscalafon(int indice) {
      boolean validarCodigo = true;
      String auxCod;
      if (tipoLista == 0) {
         auxCod = listaEscalafones.get(indice).getCodigo();
      } else {
         auxCod = filtrarListaEscalafones.get(indice).getCodigo();
      }
      if (auxCod == null) {
         validarCodigo = false;
      } else if (auxCod.isEmpty()) {
         validarCodigo = false;
      }
      if (validarCodigo == true) {
         if (tipoLista == 0) {
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (!listaEscalafonesCrear.contains(listaEscalafones.get(indice))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(listaEscalafones.get(indice));
               } else if (!listaEscalafonesModificar.contains(listaEscalafones.get(indice))) {
                  listaEscalafonesModificar.add(listaEscalafones.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistro = null;
         } else {
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (!listaEscalafonesCrear.contains(filtrarListaEscalafones.get(indice))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(indice));
               } else if (!listaEscalafonesModificar.contains(filtrarListaEscalafones.get(indice))) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistro = null;
         }
      } else {
         if (tipoLista == 0) {
            listaEscalafones.get(indice).setCodigo(auxCodigo);
         } else {
            filtrarListaEscalafones.get(indice).setCodigo(auxCodigo);
         }
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosEscalafon");
   }

   public void modificarEscalafon(int indice, String confirmarCambio, String valorConfirmar) {
      index = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CATEGORIA")) {
         if (tipoLista == 0) {
            listaEscalafones.get(indice).getCategoria().setDescripcion(categoria);
         } else {
            filtrarListaEscalafones.get(indice).getCategoria().setDescripcion(categoria);
         }
         for (int i = 0; i < lovCategorias.size(); i++) {
            if (lovCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listaEscalafones.get(indice).setCategoria(lovCategorias.get(indiceUnicoElemento));
            } else {
               filtrarListaEscalafones.get(indice).setCategoria(lovCategorias.get(indiceUnicoElemento));
            }
            lovCategorias.clear();
            getLovCategorias();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:CategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("SUBCATEGORIA")) {
         if (tipoLista == 0) {
            listaEscalafones.get(indice).getSubcategoria().setDescripcion(subCategoria);
         } else {
            filtrarListaEscalafones.get(indice).getSubcategoria().setDescripcion(subCategoria);
         }
         for (int i = 0; i < lovSubCategorias.size(); i++) {
            if (lovSubCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listaEscalafones.get(indice).setSubcategoria(lovSubCategorias.get(indiceUnicoElemento));
            } else {
               filtrarListaEscalafones.get(indice).setSubcategoria(lovSubCategorias.get(indiceUnicoElemento));
            }
            lovSubCategorias.clear();
            getLovSubCategorias();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:SubCategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (tipoLista == 0) {
            if (!listaEscalafonesCrear.contains(listaEscalafones.get(indice))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(listaEscalafones.get(indice));
               } else if (!listaEscalafonesModificar.contains(listaEscalafones.get(indice))) {
                  listaEscalafonesModificar.add(listaEscalafones.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistro = null;
         } else {
            if (!listaEscalafonesCrear.contains(filtrarListaEscalafones.get(indice))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(indice));
               } else if (!listaEscalafonesModificar.contains(filtrarListaEscalafones.get(indice))) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            index = -1;
            secRegistro = null;
         }
      }
      RequestContext.getCurrentInstance().update("form:datosEscalafon");
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CATEGORIA")) {
         if (tipoNuevo == 1) {
            categoria = nuevaEscalafon.getCategoria().getDescripcion();
         } else if (tipoNuevo == 2) {
            categoria = duplicarEscalafon.getCategoria().getDescripcion();
         }
      }
      if (Campo.equals("SUBCATEGORIA")) {
         if (tipoNuevo == 1) {
            subCategoria = nuevaEscalafon.getSubcategoria().getDescripcion();
         } else if (tipoNuevo == 2) {
            subCategoria = duplicarEscalafon.getSubcategoria().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CATEGORIA")) {
         if (tipoNuevo == 1) {
            nuevaEscalafon.getCategoria().setDescripcion(categoria);
         } else if (tipoNuevo == 2) {
            duplicarEscalafon.getCategoria().setDescripcion(categoria);
         }
         for (int i = 0; i < lovCategorias.size(); i++) {
            if (lovCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEscalafon.setCategoria(lovCategorias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCategoria");
            } else if (tipoNuevo == 2) {
               duplicarEscalafon.setCategoria(lovCategorias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCategoria");
            }
            lovCategorias.clear();
            getLovCategorias();
         } else {
            RequestContext.getCurrentInstance().update("form:CategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCategoria");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCategoria");
            }
         }
      }
      if (confirmarCambio.equalsIgnoreCase("SUBCATEGORIA")) {
         if (tipoNuevo == 1) {
            nuevaEscalafon.getSubcategoria().setDescripcion(subCategoria);
         } else if (tipoNuevo == 2) {
            duplicarEscalafon.getSubcategoria().setDescripcion(subCategoria);
         }
         for (int i = 0; i < lovSubCategorias.size(); i++) {
            if (lovSubCategorias.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEscalafon.setSubcategoria(lovSubCategorias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSubCategoria");
            } else if (tipoNuevo == 2) {
               duplicarEscalafon.setSubcategoria(lovSubCategorias.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSubCategoria");
            }
            lovSubCategorias.clear();
            getLovSubCategorias();
         } else {
            RequestContext.getCurrentInstance().update("form:SubCategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSubCategoria");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSubCategoria");
            }
         }
      }
   }

   public void cambiarIndice(int indice, int celda) {
      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            auxCodigo = listaEscalafones.get(index).getCodigo();
            secRegistro = listaEscalafones.get(index).getSecuencia();
            if (cualCelda == 1) {
               categoria = listaEscalafones.get(index).getCategoria().getDescripcion();
            }
            if (cualCelda == 2) {
               subCategoria = listaEscalafones.get(index).getSubcategoria().getDescripcion();
            }
         } else {
            auxCodigo = filtrarListaEscalafones.get(index).getCodigo();
            secRegistro = filtrarListaEscalafones.get(index).getSecuencia();
            if (cualCelda == 1) {
               categoria = filtrarListaEscalafones.get(index).getCategoria().getDescripcion();
            }
            if (cualCelda == 2) {
               subCategoria = filtrarListaEscalafones.get(index).getSubcategoria().getDescripcion();
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

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listaEscalafonesBorrar.isEmpty()) {
               administrarEscalafones.borrarEscalafones(listaEscalafonesBorrar);
               listaEscalafonesBorrar.clear();
            }
            if (!listaEscalafonesCrear.isEmpty()) {
               administrarEscalafones.crearEscalafones(listaEscalafonesCrear);
               listaEscalafonesCrear.clear();
            }
            if (!listaEscalafonesModificar.isEmpty()) {
               administrarEscalafones.editarEscalafones(listaEscalafonesModificar);
               listaEscalafonesModificar.clear();
            }
            cambiosPagina = true;
            listaEscalafones = null;
            getListaEscalafones();
            if (listaEscalafones != null) {
               infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
            } else {
               infoRegistro = "Cantidad de registros : 0";
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEscalafon");
            guardado = true;
            k = 0;
            index = -1;
            secRegistro = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         algoTabla = "300";
         escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
         escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
         escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
         escalafonSubCategoria.setFilterStyle("display: none; visibility: hidden;");
         escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
         escalafonCategoria.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         bandera = 0;
         filtrarListaEscalafones = null;
         tipoLista = 0;
      }
      listaEscalafonesBorrar.clear();
      listaEscalafonesCrear.clear();
      listaEscalafonesModificar.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listaEscalafones = null;
      getListaEscalafones();
      if (listaEscalafones != null) {
         infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
      } else {
         infoRegistro = "Cantidad de registros : 0";
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosEscalafon");
   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEscalafon = listaEscalafones.get(index);
         }
         if (tipoLista == 1) {
            editarEscalafon = filtrarListaEscalafones.get(index);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCategoriaD");
            RequestContext.getCurrentInstance().execute("PF('editarCategoriaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSubCategoriaD");
            RequestContext.getCurrentInstance().execute("PF('editarSubCategoriaD').show()");
            cualCelda = -1;
         }
      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevaEscalafon() {
      boolean validarDatos = true;
      if (nuevaEscalafon.getCategoria().getSecuencia() == null) {
         validarDatos = false;
      }
      if (nuevaEscalafon.getSubcategoria().getSecuencia() == null) {
         validarDatos = false;
      }
      if (nuevaEscalafon.getCodigo() == null) {
         validarDatos = false;
      } else if (nuevaEscalafon.getCodigo().isEmpty()) {
         validarDatos = false;
      }
      if (validarDatos == true) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            algoTabla = "300";
            escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
            escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
            escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
            escalafonSubCategoria.setFilterStyle("display: none; visibility: hidden;");
            escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
            escalafonCategoria.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEscalafon");
            bandera = 0;
            filtrarListaEscalafones = null;
            tipoLista = 0;
         }
         cambiosPagina = false;
         k++;
         l = BigInteger.valueOf(k);
         nuevaEscalafon.setSecuencia(l);
         listaEscalafonesCrear.add(nuevaEscalafon);
         listaEscalafones.add(nuevaEscalafon);
         nuevaEscalafon = new Escalafones();
         nuevaEscalafon.setSubcategoria(new SubCategorias());
         nuevaEscalafon.setCategoria(new Categorias());
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroEscalafon').hide()");
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

   public void limpiarNuevaEscalafon() {
      nuevaEscalafon = new Escalafones();
      nuevaEscalafon.setSubcategoria(new SubCategorias());
      nuevaEscalafon.setCategoria(new Categorias());
      index = -1;
      secRegistro = null;
   }

   public void duplicarEscalafonM() {
      if (index >= 0) {
         duplicarEscalafon = new Escalafones();

         if (tipoLista == 0) {
            duplicarEscalafon.setCategoria(listaEscalafones.get(index).getCategoria());
            duplicarEscalafon.setCodigo(listaEscalafones.get(index).getCodigo());
            duplicarEscalafon.setSubcategoria(listaEscalafones.get(index).getSubcategoria());
         }
         if (tipoLista == 1) {
            duplicarEscalafon.setCategoria(filtrarListaEscalafones.get(index).getCategoria());
            duplicarEscalafon.setCodigo(filtrarListaEscalafones.get(index).getCodigo());
            duplicarEscalafon.setSubcategoria(filtrarListaEscalafones.get(index).getSubcategoria());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEsc");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEscalafon').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      boolean validarDatos = true;
      if (duplicarEscalafon.getCategoria().getSecuencia() == null) {
         validarDatos = false;
      }
      if (duplicarEscalafon.getSubcategoria().getSecuencia() == null) {
         validarDatos = false;
      }
      if (duplicarEscalafon.getCodigo() == null) {
         validarDatos = false;
      } else if (duplicarEscalafon.getCodigo().isEmpty()) {
         validarDatos = false;
      }
      if (validarDatos == true) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarEscalafon.setSecuencia(l);
         cambiosPagina = false;
         listaEscalafones.add(duplicarEscalafon);
         listaEscalafonesCrear.add(duplicarEscalafon);
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEscalafon').hide()");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            //CERRAR FILTRADO
            algoTabla = "300";
            escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
            escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
            escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
            escalafonSubCategoria.setFilterStyle("display: none; visibility: hidden;");
            escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
            escalafonCategoria.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEscalafon");
            bandera = 0;
            filtrarListaEscalafones = null;
            tipoLista = 0;
         }
         duplicarEscalafon = new Escalafones();
         duplicarEscalafon.setSubcategoria(new SubCategorias());
         duplicarEscalafon.setCategoria(new Categorias());

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarduplicarEscalafon() {
      index = -1;
      secRegistro = null;
      duplicarEscalafon = new Escalafones();
      duplicarEscalafon.setSubcategoria(new SubCategorias());
      duplicarEscalafon.setCategoria(new Categorias());
   }

   public void borrarEscalafon() {
      if (index >= 0) {
         cambiosPagina = false;
         if (tipoLista == 0) {
            if (!listaEscalafonesModificar.isEmpty() && listaEscalafonesModificar.contains(listaEscalafones.get(index))) {
               int modIndex = listaEscalafonesModificar.indexOf(listaEscalafones.get(index));
               listaEscalafonesModificar.remove(modIndex);
               listaEscalafonesBorrar.add(listaEscalafones.get(index));
            } else if (!listaEscalafonesCrear.isEmpty() && listaEscalafonesCrear.contains(listaEscalafones.get(index))) {
               int crearIndex = listaEscalafonesCrear.indexOf(listaEscalafones.get(index));
               listaEscalafonesCrear.remove(crearIndex);
            } else {
               listaEscalafonesBorrar.add(listaEscalafones.get(index));
            }
            listaEscalafones.remove(index);
         }
         if (tipoLista == 1) {
            if (!listaEscalafonesModificar.isEmpty() && listaEscalafonesModificar.contains(filtrarListaEscalafones.get(index))) {
               int modIndex = listaEscalafonesModificar.indexOf(filtrarListaEscalafones.get(index));
               listaEscalafonesModificar.remove(modIndex);
               listaEscalafonesBorrar.add(filtrarListaEscalafones.get(index));
            } else if (!listaEscalafonesCrear.isEmpty() && listaEscalafonesCrear.contains(filtrarListaEscalafones.get(index))) {
               int crearIndex = listaEscalafonesCrear.indexOf(filtrarListaEscalafones.get(index));
               listaEscalafonesCrear.remove(crearIndex);
            } else {
               listaEscalafonesBorrar.add(filtrarListaEscalafones.get(index));
            }
            int VCIndex = listaEscalafones.indexOf(filtrarListaEscalafones.get(index));
            listaEscalafones.remove(VCIndex);
            filtrarListaEscalafones.remove(index);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros : " + listaEscalafones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         algoTabla = "280";
         escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
         escalafonCodigo.setFilterStyle("width: 85% !important;");
         escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
         escalafonSubCategoria.setFilterStyle("width: 85% !important;");
         escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
         escalafonCategoria.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         bandera = 1;
      } else if (bandera == 1) {
         //CERRAR FILTRADO
         algoTabla = "300";
         escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
         escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
         escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
         escalafonSubCategoria.setFilterStyle("display: none; visibility: hidden;");
         escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
         escalafonCategoria.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         bandera = 0;
         filtrarListaEscalafones = null;
         tipoLista = 0;
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         algoTabla = "300";
         escalafonCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCodigo");
         escalafonCodigo.setFilterStyle("display: none; visibility: hidden;");
         escalafonSubCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonSubCategoria");
         escalafonSubCategoria.setFilterStyle("display: none; visibility: hidden;");
         escalafonCategoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEscalafon:escalafonCategoria");
         escalafonCategoria.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         bandera = 0;
         filtrarListaEscalafones = null;
         tipoLista = 0;
      }
      listaEscalafonesBorrar.clear();
      listaEscalafonesCrear.clear();
      listaEscalafonesModificar.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listaEscalafones = null;
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
         RequestContext.getCurrentInstance().update("form:CategoriaDialogo");
         RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').show()");
      }
      if (tipo == 1) {
         RequestContext.getCurrentInstance().update("form:SubCategoriaDialogo");
         RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').show()");
      }
   }

   public void actualizarCategoria() {
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listaEscalafones.get(index).setCategoria(categoriaSelecionada);
            if (!listaEscalafonesCrear.contains(listaEscalafones.get(index))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(listaEscalafones.get(index));
               } else if (!listaEscalafonesModificar.contains(listaEscalafones.get(index))) {
                  listaEscalafonesModificar.add(listaEscalafones.get(index));
               }
            }
         } else {
            filtrarListaEscalafones.get(index).setCategoria(categoriaSelecionada);
            if (!listaEscalafonesCrear.contains(filtrarListaEscalafones.get(index))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(index));
               } else if (!listaEscalafonesModificar.contains(filtrarListaEscalafones.get(index))) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(index));
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
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         permitirIndex = true;
      } else if (tipoActualizacion == 1) {
         nuevaEscalafon.setCategoria(categoriaSelecionada);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEsc");
      } else if (tipoActualizacion == 2) {
         duplicarEscalafon.setCategoria(categoriaSelecionada);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEsc");
      }
      filtrarLovCategorias = null;
      categoriaSelecionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      /*
         RequestContext.getCurrentInstance().update("form:CategoriaDialogo");
         RequestContext.getCurrentInstance().update("form:lovCategoria");
         RequestContext.getCurrentInstance().update("form:aceptarCat");*/
      context.reset("form:lovCategoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCategoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').hide()");
   }

   public void cancelarCambioCategoria() {
      filtrarLovCategorias = null;
      categoriaSelecionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCategoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCategoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').hide()");
   }

   public void actualizarSubCategoria() {
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listaEscalafones.get(index).setSubcategoria(subCategoriaSelecionada);
            if (!listaEscalafonesCrear.contains(listaEscalafones.get(index))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(listaEscalafones.get(index));
               } else if (!listaEscalafonesModificar.contains(listaEscalafones.get(index))) {
                  listaEscalafonesModificar.add(listaEscalafones.get(index));
               }
            }
         } else {
            filtrarListaEscalafones.get(index).setSubcategoria(subCategoriaSelecionada);
            if (!listaEscalafonesCrear.contains(filtrarListaEscalafones.get(index))) {
               if (listaEscalafonesModificar.isEmpty()) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(index));
               } else if (!listaEscalafonesModificar.contains(filtrarListaEscalafones.get(index))) {
                  listaEscalafonesModificar.add(filtrarListaEscalafones.get(index));
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
         RequestContext.getCurrentInstance().update("form:datosEscalafon");
         permitirIndex = true;
      } else if (tipoActualizacion == 1) {
         nuevaEscalafon.setSubcategoria(subCategoriaSelecionada);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEsc");
      } else if (tipoActualizacion == 2) {
         duplicarEscalafon.setSubcategoria(subCategoriaSelecionada);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEsc");
      }
      filtrarLovSubCategorias = null;
      subCategoriaSelecionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      /*
        RequestContext.getCurrentInstance().update("form:SubCategoriaDialogo");
        RequestContext.getCurrentInstance().update("form:lovSubCategoria");
        RequestContext.getCurrentInstance().update("form:aceptarSCat");*/
      context.reset("form:lovSubCategoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovSubCategoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').hide()");
   }

   public void cancelarCambioSubCategoria() {
      filtrarLovSubCategorias = null;
      subCategoriaSelecionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovSubCategoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovSubCategoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').hide()");
   }

   public void listaValoresBoton() {
      if (index >= 0) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("form:CategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CategoriaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:SubCategoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('SubCategoriaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEscalafonExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Escalafones_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEscalafonExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Escalafones_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      infoRegistro = "Cantidad de registros : " + filtrarListaEscalafones.size();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaEscalafones != null) {
         if (secRegistro != null) {
            int resultado = administrarRastros.obtenerTabla(secRegistro, "ESCALAFONES");
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
      } else if (administrarRastros.verificarHistoricosTabla("ESCALAFONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }
   //GETTERS AND SETTERS

   public List<Escalafones> getListaEscalafones() {
      try {
         if (listaEscalafones == null) {
            listaEscalafones = administrarEscalafones.listaEscalafones();
         }
         return listaEscalafones;
      } catch (Exception e) {
         System.out.println("Error...!! getListaEscalafones : " + e.toString());
         return null;
      }
   }

   public void setListaEscalafones(List<Escalafones> setListaEscalafones) {
      this.listaEscalafones = setListaEscalafones;
   }

   public List<Escalafones> getFiltrarListaEscalafones() {
      return filtrarListaEscalafones;
   }

   public void setFiltrarListaEscalafones(List<Escalafones> setFiltrarListaEscalafones) {
      this.filtrarListaEscalafones = setFiltrarListaEscalafones;
   }

   public Escalafones getNuevaEscalafon() {
      return nuevaEscalafon;
   }

   public void setNuevaEscalafon(Escalafones setNuevaEscalafon) {
      this.nuevaEscalafon = setNuevaEscalafon;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public List<SubCategorias> getLovSubCategorias() {
      lovSubCategorias = administrarEscalafones.lovSubCategorias();
      return lovSubCategorias;
   }

   public void setLovSubCategorias(List<SubCategorias> lovSubCategorias) {
      this.lovSubCategorias = lovSubCategorias;
   }

   public SubCategorias getSubCategoriaSelecionada() {
      return subCategoriaSelecionada;
   }

   public void setSubCategoriaSelecionada(SubCategorias subCategoriaSelecionada) {
      this.subCategoriaSelecionada = subCategoriaSelecionada;
   }

   public List<SubCategorias> getFiltrarLovSubCategorias() {
      return filtrarLovSubCategorias;
   }

   public void setFiltrarLovSubCategorias(List<SubCategorias> filtrarLovSubCategorias) {
      this.filtrarLovSubCategorias = filtrarLovSubCategorias;
   }

   public List<Escalafones> getListaEscalafonesModificar() {
      return listaEscalafonesModificar;
   }

   public void setListaEscalafonesModificar(List<Escalafones> listaEscalafonesModificar) {
      this.listaEscalafonesModificar = listaEscalafonesModificar;
   }

   public List<Escalafones> getListaEscalafonesCrear() {
      return listaEscalafonesCrear;
   }

   public void setListaEscalafonesCrear(List<Escalafones> listaEscalafonesCrear) {
      this.listaEscalafonesCrear = listaEscalafonesCrear;
   }

   public List<Escalafones> getListaEscalafonesBorrar() {
      return listaEscalafonesBorrar;
   }

   public void setListaEscalafonesBorrar(List<Escalafones> listaEscalafonesBorrar) {
      this.listaEscalafonesBorrar = listaEscalafonesBorrar;
   }

   public List<Categorias> getLovCategorias() {
      lovCategorias = administrarEscalafones.lovCategorias();
      return lovCategorias;
   }

   public void setLovCategorias(List<Categorias> setLovCategorias) {
      this.lovCategorias = setLovCategorias;
   }

   public List<Categorias> getFiltrarLovCategorias() {
      return filtrarLovCategorias;
   }

   public void setFiltrarLovCategorias(List<Categorias> setFiltrarLovCategorias) {
      this.filtrarLovCategorias = setFiltrarLovCategorias;
   }

   public Escalafones getEditarEscalafon() {
      return editarEscalafon;
   }

   public void setEditarEscalafon(Escalafones editarVRL) {
      this.editarEscalafon = editarVRL;
   }

   public Escalafones getDuplicarEscalafon() {
      return duplicarEscalafon;
   }

   public void setDuplicarEscalafon(Escalafones duplicarEsc) {
      this.duplicarEscalafon = duplicarEsc;
   }

   public Categorias getCategoriaSelecionada() {
      return categoriaSelecionada;
   }

   public void setCategoriaSelecionada(Categorias reformaLaboralSelecionada) {
      this.categoriaSelecionada = reformaLaboralSelecionada;
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

   public Escalafones getEscalafonTablaSeleccionado() {
      getListaEscalafones();
      if (listaEscalafones != null) {
         int tam = listaEscalafones.size();
         if (tam > 0) {
            escalafonTablaSeleccionado = listaEscalafones.get(0);
         }
      }
      return escalafonTablaSeleccionado;
   }

   public void setEscalafonTablaSeleccionado(Escalafones escalafonTablaSeleccionado) {
      this.escalafonTablaSeleccionado = escalafonTablaSeleccionado;
   }

   public String getInfoRegistroCategoria() {
      getLovCategorias();
      if (lovCategorias != null) {
         infoRegistroCategoria = "Cantidad de registros : " + lovCategorias.size();
      } else {
         infoRegistroCategoria = "Cantidad de registros : 0";
      }
      return infoRegistroCategoria;
   }

   public void setInfoRegistroCategoria(String infoRegistroCategoria) {
      this.infoRegistroCategoria = infoRegistroCategoria;
   }

   public String getInfoRegistroSubCategoria() {
      getLovSubCategorias();
      if (lovSubCategorias != null) {
         infoRegistroSubCategoria = "Cantidad de registros : " + lovSubCategorias.size();
      } else {
         infoRegistroSubCategoria = "Cantidad de registros : 0";
      }
      return infoRegistroSubCategoria;
   }

   public void setInfoRegistroSubCategoria(String infoRegistroSubCategoria) {
      this.infoRegistroSubCategoria = infoRegistroSubCategoria;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
