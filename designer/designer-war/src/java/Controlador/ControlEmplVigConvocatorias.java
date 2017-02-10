/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.EvalResultadosConv;
import Entidades.Evalconvocatorias;
import Entidades.Evalvigconvocatorias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEvalConvocatoriasInterface;
import InterfaceAdministrar.AdministrarEvalResultadosConvInterface;
import InterfaceAdministrar.AdministrarEvalVigConvocatoriasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlEmplVigConvocatorias")
@SessionScoped
public class ControlEmplVigConvocatorias implements Serializable {

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarEvalResultadosConvInterface administrarResultadosConv;
   @EJB
   AdministrarEvalConvocatoriasInterface administrarEvalConvocatorias;
   @EJB
   AdministrarEvalVigConvocatoriasInterface administrarEvalVigConvocatorias;

   //lista vig convocatorias    
   private List<Evalvigconvocatorias> listEvalVigConvocatoriasModificar;
   private Evalvigconvocatorias evalVigParametros;
   //lista  convocatorias
   private List<Evalconvocatorias> listEvalConvocatorias;
   private List<Evalconvocatorias> listEvalConvocatoriasFiltrar;
   private Evalconvocatorias editarEvalConvocatorias;
   private Evalconvocatorias evalConvocatoriaSeleccionado;
   //lista evaluaciones
   private List<EvalResultadosConv> listEvalResultadosConv;
   private List<EvalResultadosConv> listEvalResultadosConvFiltrar;
   private List<EvalResultadosConv> listEvalResultadosConvCrear;
   private List<EvalResultadosConv> listEvalResultadosConvModificar;
   private List<EvalResultadosConv> listEvalResultadosConvBorrar;
   private EvalResultadosConv nuevoEvalResultadosConv;
   private EvalResultadosConv duplicarEvalResultadosConv;
   private EvalResultadosConv editarEvalResultadosConv;
   private EvalResultadosConv evalResultadosConvSeleccionado;
   //columnas convocatorias
   private Column enfoque, nomConvocatoria, fechaInicio, fechaLimite, Objetivos;
   //columnas resultados
   private Column nomPrueba, puntaje, resfechaInicio, resfechaFin;
   //otros
   private Empleados empleado;
   private int tipoActualizacion;
   private int bandera, promedio;
   private boolean aceptar;
   private boolean guardado;
   private int cualCelda, tipoLista;
   private boolean permitirIndex;
   private String altoTabla, altoTabla2, nombreTabla;
   private String infoRegistroConvocatorias;
   private String infoRegistroResultados, nombreArchivo, tablaImprimir;
   private DataTable tablaC;
   private boolean activarLOV;
   private BigInteger l;
   private int k, cualTabla;
   private Date fechaParametro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplVigConvocatorias() {
      altoTabla = "100";
      altoTabla2 = "100";
      listEvalConvocatorias = null;
      listEvalResultadosConv = null;
      aceptar = true;
      k = 0;
      cualCelda = -1;
      cualTabla = -1;
      tipoLista = 0;
      guardado = true;
      empleado = new Empleados();
      activarLOV = true;
      listEvalResultadosConvBorrar = new ArrayList<EvalResultadosConv>();
      listEvalResultadosConvCrear = new ArrayList<EvalResultadosConv>();
      listEvalResultadosConvModificar = new ArrayList<EvalResultadosConv>();
      editarEvalConvocatorias = new Evalconvocatorias();
      duplicarEvalResultadosConv = new EvalResultadosConv();
      editarEvalResultadosConv = new EvalResultadosConv();
      nuevoEvalResultadosConv = new EvalResultadosConv();
      nuevoEvalResultadosConv.setEvalconvocatoria(new Evalconvocatorias());
      nombreTabla = "Convocatorias";
      evalVigParametros = new Evalvigconvocatorias();
      evalVigParametros.setFechavigencia(new Date());
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "emplvigconvocatoria";
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

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEvalConvocatorias.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarResultadosConv.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger secuencia) {
      empleado = administrarResultadosConv.empleadoActual(secuencia);
      getListEvalConvocatorias();
      getListEvalResultadosConv();
      if (listEvalConvocatorias != null) {
         if (!listEvalConvocatorias.isEmpty()) {
            evalConvocatoriaSeleccionado = listEvalConvocatorias.get(0);
         }
      }
   }

   public void modificarResultado(EvalResultadosConv resultado) {
      evalResultadosConvSeleccionado = resultado;
      if (tipoLista == 0) {
         if (!listEvalResultadosConvCrear.contains(evalResultadosConvSeleccionado)) {
            if (listEvalResultadosConvModificar.isEmpty()) {
               listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
            } else if (!listEvalResultadosConvModificar.contains(evalResultadosConvSeleccionado)) {
               listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else if (!listEvalResultadosConvCrear.contains(evalResultadosConvSeleccionado)) {
         if (listEvalResultadosConvModificar.isEmpty()) {
            listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
         } else if (!listEvalResultadosConvModificar.contains(evalResultadosConvSeleccionado)) {
            listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

//    public void modificarEvalVigConvocatoria() {
//       evalVigParametros.setCodigo(l);
//        
//        evalResultadosConvSeleccionado = resultado;
//        if (tipoLista == 0) {
//            if (!listEvalResultadosConvCrear.contains(evalResultadosConvSeleccionado)) {
//                if (listEvalResultadosConvModificar.isEmpty()) {
//                    listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
//                } else if (!listEvalResultadosConvModificar.contains(evalResultadosConvSeleccionado)) {
//                    listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
//                }
//                if (guardado == true) {
//                    guardado = false;
//                    RequestContext context = RequestContext.getCurrentInstance();
//                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                }
//            }
//        } else if (!listEvalResultadosConvCrear.contains(evalResultadosConvSeleccionado)) {
//            if (listEvalResultadosConvModificar.isEmpty()) {
//                listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
//            } else if (!listEvalResultadosConvModificar.contains(evalResultadosConvSeleccionado)) {
//                listEvalResultadosConvModificar.add(evalResultadosConvSeleccionado);
//            }
//            if (guardado == true) {
//                guardado = false;
//                RequestContext context = RequestContext.getCurrentInstance();
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            }
//        }
//    }
   public void cambiarIndiceConvocatorias(Evalconvocatorias convocatoria, int celda) {
      if (permitirIndex == true) {
         evalConvocatoriaSeleccionado = convocatoria;
         cualCelda = celda;
         nombreTabla = "Convocatorias";
         nombreArchivo = "ConvocatoriasXML";
         tablaImprimir = ":formExportar:datosConvocatoriasExportar";
         cualTabla = 1;
         if (cualCelda == 0) {
            evalConvocatoriaSeleccionado.getEnfoque().getDescripcion();
         } else if (cualCelda == 1) {
            evalConvocatoriaSeleccionado.getEvalvigconvocatoria().getCodigo();
         } else if (cualCelda == 2) {
            evalConvocatoriaSeleccionado.getFechainicio();
         } else if (cualCelda == 3) {
            evalConvocatoriaSeleccionado.getFechalimite();
         } else if (cualCelda == 4) {
            evalConvocatoriaSeleccionado.getObjetivos();
         }
      }
   }

   public void cambiarIndiceResultados(EvalResultadosConv resultados, int celda) {

      if (permitirIndex == true) {
         evalResultadosConvSeleccionado = resultados;
         cualCelda = celda;
         cualTabla = 2;
         nombreTabla = "Resultados";
         nombreArchivo = "ResultadosXML";
         tablaImprimir = ":formExportar:datosResultadosExportar";
         if (cualCelda == 0) {
            resultados.getNombreprueba();
         } else if (cualCelda == 1) {
            resultados.getPuntajeobtenido();
         } else if (cualCelda == 2) {
            resultados.getFechaperiododesde();
         } else if (cualCelda == 3) {
            resultados.getFechaperiodohasta();
         }
      }
   }

   public void guardarSalir() {
      guardarCambios();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificacion();
      salir();
   }

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listEvalResultadosConvBorrar.isEmpty()) {
               administrarResultadosConv.borrar(listEvalResultadosConvBorrar);
               listEvalResultadosConvBorrar.clear();
            }
            if (!listEvalResultadosConvCrear.isEmpty()) {
               administrarResultadosConv.crear(listEvalResultadosConvCrear);
               listEvalResultadosConvCrear.clear();
            }
            if (!listEvalResultadosConvModificar.isEmpty()) {
               administrarResultadosConv.editar(listEvalResultadosConvModificar);
               listEvalResultadosConvModificar.clear();
            }
            listEvalResultadosConv = null;
            listEvalConvocatorias = null;
            getListEvalResultadosConv();
            getListEvalConvocatorias();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosResultados();
            contarRegistroConvocatorias();
            evalConvocatoriaSeleccionado = null;
            evalResultadosConvSeleccionado = null;
         }

         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosConvocatorias");
         RequestContext.getCurrentInstance().update("form:datosResultados");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {

         enfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:enfoque");
         enfoque.setFilterStyle("display: none; visibility: hidden;");
         nomConvocatoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:nomConvocatoria");
         nomConvocatoria.setFilterStyle("display: none; visibility: hidden;");
         fechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaInicio");
         fechaInicio.setFilterStyle("display: none; visibility: hidden;");
         fechaLimite = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaLimite");
         fechaLimite.setFilterStyle("display: none; visibility: hidden;");
         Objetivos = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:Objetivos");
         Objetivos.setFilterStyle("display: none; visibility: hidden;");
         nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
         nomPrueba.setFilterStyle("display: none; visibility: hidden;");
         puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
         puntaje.setFilterStyle("display: none; visibility: hidden;");
         resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
         resfechaInicio.setFilterStyle("display: none; visibility: hidden;");
         resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
         resfechaFin.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConvocatorias");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listEvalConvocatoriasFiltrar = null;
         listEvalResultadosConvFiltrar = null;
         tipoLista = 0;
         altoTabla = "100";
         altoTabla2 = "100";
      }

      listEvalResultadosConvBorrar.clear();
      listEvalResultadosConvCrear.clear();
      listEvalResultadosConvModificar.clear();
      k = 0;
      listEvalConvocatorias = null;
      listEvalResultadosConv = null;
      evalConvocatoriaSeleccionado = null;
      evalResultadosConvSeleccionado = null;
      guardado = true;
      permitirIndex = true;
      getListEvalConvocatorias();
      getListEvalResultadosConv();
      contarRegistrosResultados();
      contarRegistroConvocatorias();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosConvocatorias");
      RequestContext.getCurrentInstance().update("form:datosResultados");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (evalConvocatoriaSeleccionado != null) {
         editarEvalConvocatorias = evalConvocatoriaSeleccionado;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEnfoqueD");
            RequestContext.getCurrentInstance().execute("PF('editarEnfoqueD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNomConvocatoriaD");
            RequestContext.getCurrentInstance().execute("PF('editarNomConvocatoriaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicioD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicioD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaLimiteD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaLimiteD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObjetivoD");
            RequestContext.getCurrentInstance().execute("PF('editarObjetivoD').show()");
            cualCelda = -1;
         }
      } else if (evalResultadosConvSeleccionado != null) {
         editarEvalResultadosConv = evalResultadosConvSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNomPruebaD");
            RequestContext.getCurrentInstance().execute("PF('editarNomPruebaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPuntajeD");
            RequestContext.getCurrentInstance().execute("PF('editarPuntajeD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicioR");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicioR').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinD').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      if (tipoLista == 0) {
         evalConvocatoriaSeleccionado = listEvalConvocatorias.get(indice);
      } else {
         evalConvocatoriaSeleccionado = listEvalConvocatoriasFiltrar.get(indice);
      }
      cambiarIndiceConvocatorias(evalConvocatoriaSeleccionado, columna);
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         EvalResultadosConv auxiliar = null;
         if (tipoLista == 0) {
            auxiliar = evalResultadosConvSeleccionado;
         }
         if (tipoLista == 1) {
            auxiliar = evalResultadosConvSeleccionado;
         }
         if (auxiliar.getFechaperiodohasta() != null) {
            if (auxiliar.getFechaperiododesde().after(fechaParametro) && auxiliar.getFechaperiododesde().before(auxiliar.getFechaperiodohasta())) {
               retorno = true;
            } else {
               retorno = false;
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
         }
         if (auxiliar.getFechaperiododesde() == null) {
            if (auxiliar.getFechaperiododesde().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 1) {
         if (nuevoEvalResultadosConv.getFechaperiodohasta() != null) {
            if (nuevoEvalResultadosConv.getFechaperiodohasta().after(fechaParametro) && nuevoEvalResultadosConv.getFechaperiododesde().before(nuevoEvalResultadosConv.getFechaperiodohasta())) {
               retorno = true;
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
               retorno = false;
            }
         } else if (nuevoEvalResultadosConv.getFechaperiododesde().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarEvalResultadosConv.getFechaperiodohasta() != null) {
            if (duplicarEvalResultadosConv.getFechaperiododesde().after(fechaParametro) && duplicarEvalResultadosConv.getFechaperiododesde().before(duplicarEvalResultadosConv.getFechaperiodohasta())) {
               retorno = true;
            } else {
               retorno = false;
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
         } else if (duplicarEvalResultadosConv.getFechaperiododesde().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarFechas(EvalResultadosConv resultado, int c) {
      EvalResultadosConv auxiliar = null;
      if (tipoLista == 0) {
         auxiliar = resultado;
      }
      if (tipoLista == 1) {
         auxiliar = resultado;
      }
      if (auxiliar.getFechaperiododesde() != null) {
         boolean retorno = false;
         if (auxiliar.getFechaperiodohasta() == null) {
            retorno = true;
         }
         if (auxiliar.getFechaperiodohasta() != null) {
            evalResultadosConvSeleccionado = resultado;
            retorno = validarFechasRegistro(0);
         }
         if (retorno == true) {
            cambiarIndiceResultados(resultado, c);
            modificarResultado(resultado);
         } else {
            if (tipoLista == 0) {
               evalResultadosConvSeleccionado.setFechaperiodohasta(evalResultadosConvSeleccionado.getFechaperiodohasta());
               evalResultadosConvSeleccionado.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());
            }
            if (tipoLista == 1) {
               evalResultadosConvSeleccionado.setFechaperiodohasta(evalResultadosConvSeleccionado.getFechaperiodohasta());
               evalResultadosConvSeleccionado.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosResultados");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         if (tipoLista == 0) {
            evalResultadosConvSeleccionado.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());
         }
         if (tipoLista == 1) {
            evalResultadosConvSeleccionado.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void agregarNuevoResultado() {
      if (nuevoEvalResultadosConv.getFechaperiododesde() != null) {
         if (validarFechasRegistro(1) == true) {
            if (bandera == 1) {
               altoTabla2 = "100";
               //CERRAR FILTRADO
               nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
               nomPrueba.setFilterStyle("display: none; visibility: hidden;");
               puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
               puntaje.setFilterStyle("display: none; visibility: hidden;");
               resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
               resfechaInicio.setFilterStyle("display: none; visibility: hidden;");
               resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
               resfechaFin.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosResultados");
               bandera = 0;
               listEvalResultadosConvFiltrar = null;
               tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoEvalResultadosConv.setSecuencia(l);
            nuevoEvalResultadosConv.setEmpleado(empleado);
            nuevoEvalResultadosConv.setEvalconvocatoria(new Evalconvocatorias());
            listEvalResultadosConvCrear.add(nuevoEvalResultadosConv);
            listEvalResultadosConv.add(nuevoEvalResultadosConv);
            evalResultadosConvSeleccionado = nuevoEvalResultadosConv;
            nuevoEvalResultadosConv = new EvalResultadosConv();
            getListEvalResultadosConv();
            contarRegistrosResultados();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosResultados");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroResultados').hide()");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            calcularPromedio();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarNuevoResultado() {
      nuevoEvalResultadosConv = new EvalResultadosConv();
   }

   public void duplicarResultado() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (evalResultadosConvSeleccionado != null) {
         duplicarEvalResultadosConv = new EvalResultadosConv();

         if (tipoLista == 0) {
            duplicarEvalResultadosConv.setNombreprueba(evalResultadosConvSeleccionado.getNombreprueba());
            duplicarEvalResultadosConv.setPuntajeobtenido(evalResultadosConvSeleccionado.getPuntajeobtenido());
            duplicarEvalResultadosConv.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());
            duplicarEvalResultadosConv.setFechaperiodohasta(evalResultadosConvSeleccionado.getFechaperiodohasta());
         }
         if (tipoLista == 1) {
            duplicarEvalResultadosConv.setNombreprueba(evalResultadosConvSeleccionado.getNombreprueba());
            duplicarEvalResultadosConv.setPuntajeobtenido(evalResultadosConvSeleccionado.getPuntajeobtenido());
            duplicarEvalResultadosConv.setFechaperiododesde(evalResultadosConvSeleccionado.getFechaperiododesde());
            duplicarEvalResultadosConv.setFechaperiodohasta(evalResultadosConvSeleccionado.getFechaperiodohasta());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarResultados");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroResultados').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      if (duplicarEvalResultadosConv.getFechaperiododesde() != null) {
         int repetido = 0;
         for (int i = 0; i < listEvalResultadosConv.size(); i++) {
            if (duplicarEvalResultadosConv.getFechaperiododesde().equals(listEvalResultadosConv.get(i).getFechaperiododesde())) {
               repetido++;
            }
         }
         if (repetido == 0) {
            if (validarFechasRegistro(2) == true) {
               k++;
               l = BigInteger.valueOf(k);
               duplicarEvalResultadosConv.setSecuencia(l);
//                    duplicarEvalResultadosConv.setPersona(empleado.getPersona());
               listEvalResultadosConv.add(duplicarEvalResultadosConv);
               listEvalResultadosConvCrear.add(duplicarEvalResultadosConv);
               evalResultadosConvSeleccionado = duplicarEvalResultadosConv;
               getListEvalResultadosConv();
               contarRegistrosResultados();
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().update("form:infoRegistro");
               RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigencias').hide()");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               if (bandera == 1) {
                  altoTabla2 = "100";
                  nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
                  nomPrueba.setFilterStyle("display: none; visibility: hidden;");
                  puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
                  puntaje.setFilterStyle("display: none; visibility: hidden;");
                  resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
                  resfechaInicio.setFilterStyle("display: none; visibility: hidden;");
                  resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
                  resfechaFin.setFilterStyle("display: none; visibility: hidden;");
                  RequestContext.getCurrentInstance().update("form:datosResultados");
                  bandera = 0;
                  listEvalResultadosConvFiltrar = null;
                  tipoLista = 0;
               }
               duplicarEvalResultadosConv = new EvalResultadosConv();
               calcularPromedio();
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarDuplicar() {
      duplicarEvalResultadosConv = new EvalResultadosConv();
   }

   public void borrarResultados() {
      if (evalResultadosConvSeleccionado != null) {
         if (!listEvalResultadosConvModificar.isEmpty() && listEvalResultadosConvModificar.contains(evalResultadosConvSeleccionado)) {
            int modIndex = listEvalResultadosConvModificar.indexOf(evalResultadosConvSeleccionado);
            listEvalResultadosConvModificar.remove(modIndex);
            listEvalResultadosConvBorrar.add(evalResultadosConvSeleccionado);
         } else if (!listEvalResultadosConvCrear.isEmpty() && listEvalResultadosConvCrear.contains(evalResultadosConvSeleccionado)) {
            int crearIndex = listEvalResultadosConvCrear.indexOf(evalResultadosConvSeleccionado);
            listEvalResultadosConvCrear.remove(crearIndex);
         } else {
            listEvalResultadosConvBorrar.add(evalResultadosConvSeleccionado);
         }
         listEvalConvocatorias.remove(evalResultadosConvSeleccionado);
         if (tipoLista == 1) {
            listEvalConvocatoriasFiltrar.remove(evalResultadosConvSeleccionado);
         }
         contarRegistrosResultados();
         RequestContext.getCurrentInstance().update("form:datosVigenciasDeportes");
         evalResultadosConvSeleccionado = null;
         calcularPromedio();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "80";
         altoTabla2 = "80";
         enfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:enfoque");
         enfoque.setFilterStyle("width: 85% !important");
         nomConvocatoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:nomConvocatoria");
         nomConvocatoria.setFilterStyle("width: 85% !important");
         fechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaInicio");
         fechaInicio.setFilterStyle("width: 85% !important");
         fechaLimite = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaLimite");
         fechaLimite.setFilterStyle("width: 85% !important");
         Objetivos = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:Objetivos");
         Objetivos.setFilterStyle("width: 85% !important");
         nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
         nomPrueba.setFilterStyle("width: 85% !important");
         puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
         puntaje.setFilterStyle("width: 85% !important");
         resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
         resfechaInicio.setFilterStyle("width: 85% !important");
         resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
         resfechaFin.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConvocatorias");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listEvalConvocatoriasFiltrar = null;
         listEvalResultadosConvFiltrar = null;
         tipoLista = 0;
         bandera = 1;
      } else if (bandera == 1) {
         altoTabla = "100";
         altoTabla2 = "100";
         enfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:enfoque");
         enfoque.setFilterStyle("width: 85% !important");
         nomConvocatoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:nomConvocatoria");
         nomConvocatoria.setFilterStyle("width: 85% !important");
         fechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaInicio");
         fechaInicio.setFilterStyle("width: 85% !important");
         fechaLimite = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaLimite");
         fechaLimite.setFilterStyle("width: 85% !important");
         Objetivos = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:Objetivos");
         Objetivos.setFilterStyle("width: 85% !important");
         nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
         nomPrueba.setFilterStyle("width: 85% !important");
         puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
         puntaje.setFilterStyle("width: 85% !important");
         resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
         resfechaInicio.setFilterStyle("width: 85% !important");
         resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
         resfechaFin.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConvocatorias");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listEvalConvocatoriasFiltrar = null;
         listEvalResultadosConvFiltrar = null;
         tipoLista = 0;
      }
   }

   public void salir() {
      if (bandera == 1) {
         altoTabla = "100";
         altoTabla2 = "100";
         enfoque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:enfoque");
         enfoque.setFilterStyle("width: 85% !important");
         nomConvocatoria = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:nomConvocatoria");
         nomConvocatoria.setFilterStyle("width: 85% !important");
         fechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaInicio");
         fechaInicio.setFilterStyle("width: 85% !important");
         fechaLimite = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:fechaLimite");
         fechaLimite.setFilterStyle("width: 85% !important");
         Objetivos = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConvocatorias:Objetivos");
         Objetivos.setFilterStyle("width: 85% !important");
         nomPrueba = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:nomPrueba");
         nomPrueba.setFilterStyle("width: 85% !important");
         puntaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:puntaje");
         puntaje.setFilterStyle("width: 85% !important");
         resfechaInicio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resfechaInicio");
         resfechaInicio.setFilterStyle("width: 85% !important");
         resfechaFin = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosResultados:resFechaFin");
         resfechaFin.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConvocatorias");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listEvalConvocatoriasFiltrar = null;
         listEvalResultadosConvFiltrar = null;
         tipoLista = 0;
      }

      listEvalResultadosConvBorrar.clear();
      listEvalResultadosConvCrear.clear();
      listEvalResultadosConvModificar.clear();
      evalResultadosConvSeleccionado = null;
      k = 0;
      listEvalConvocatorias = null;
      guardado = true;

   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void revisarDialogoGuardar() {

      if (!listEvalResultadosConvBorrar.isEmpty() || !listEvalResultadosConvCrear.isEmpty() || !listEvalResultadosConvModificar.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void limpiarExportar() {
      if (cualTabla == 1) {
         evalConvocatoriaSeleccionado = new Evalconvocatorias();
      } else if (cualTabla == 2) {
         limpiarNuevoResultado();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportPDF() throws IOException {
      if (cualTabla == 1) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConvocatoriasExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "ConvocatoriasPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 2) {

         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosResultadosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "ResultadosPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   public void exportXLS() throws IOException {
      if (cualTabla == 1) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConvocatoriasExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "ConvocatoriasXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 2) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosResultadosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "ResutladosXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistroConvocatorias();
   }

   public void eventoFiltrarResultados() {
      contarRegistrosResultados();
   }

   public void contarRegistroConvocatorias() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConvocatorias");
   }

   public void contarRegistrosResultados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroResultados");

   }

   public void calcularPromedio() {
      if (listEvalResultadosConv != null) {
         if (!listEvalResultadosConv.isEmpty()) {
            for (int i = 0; i < listEvalResultadosConv.size(); i++) {
               promedio = (listEvalResultadosConv.get(i).getPuntajeobtenido() / listEvalResultadosConv.size());
            }
         }
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = -1;
      if (nombreTabla != null) {
         if (nombreTabla.equals("Convocatorias")) {
            resultado = administrarRastros.obtenerTabla(evalConvocatoriaSeleccionado.getSecuencia(), "EVALCONVOCATORIAS");
         } else if (nombreTabla.equals("Resultados")) {
            resultado = administrarRastros.obtenerTabla(evalResultadosConvSeleccionado.getSecuencia(), "EVALRESULTADOSCONV");
         }

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
         } else if (administrarRastros.verificarHistoricosTabla("EVALCONVOCATORIAS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
         } else if (administrarRastros.verificarHistoricosTabla("EVALRESULTADOSCONV")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
         }
      }
   }

   ////GETS Y SETS///
   public List<Evalconvocatorias> getListEvalConvocatorias() {
      if (listEvalConvocatorias == null) {
         if (empleado != null) {
            listEvalConvocatorias = administrarEvalConvocatorias.buscarEvalConvocatorias(empleado.getSecuencia());
         }
      }
      return listEvalConvocatorias;
   }

   public void setListEvalConvocatorias(List<Evalconvocatorias> listEvalConvocatorias) {
      this.listEvalConvocatorias = listEvalConvocatorias;
   }

   public List<Evalconvocatorias> getListEvalConvocatoriasFiltrar() {
      return listEvalConvocatoriasFiltrar;
   }

   public void setListEvalConvocatoriasFiltrar(List<Evalconvocatorias> listEvalConvocatoriasFiltrar) {
      this.listEvalConvocatoriasFiltrar = listEvalConvocatoriasFiltrar;
   }

   public Evalconvocatorias getEditarEvalConvocatorias() {
      return editarEvalConvocatorias;
   }

   public void setEditarEvalConvocatorias(Evalconvocatorias editarEvalConvocatorias) {
      this.editarEvalConvocatorias = editarEvalConvocatorias;
   }

   public Evalconvocatorias getEvalConvocatoriaSeleccionado() {
      return evalConvocatoriaSeleccionado;
   }

   public void setEvalConvocatoriaSeleccionado(Evalconvocatorias evalConvocatoriaSeleccionado) {
      this.evalConvocatoriaSeleccionado = evalConvocatoriaSeleccionado;
   }

   public List<EvalResultadosConv> getListEvalResultadosConv() {
      if (listEvalResultadosConv == null) {
         if (empleado != null) {
            listEvalResultadosConv = administrarResultadosConv.buscarEvalResultadosConvocatorias(empleado.getSecuencia());
         }
      }
      return listEvalResultadosConv;
   }

   public void setListEvalResultadosConv(List<EvalResultadosConv> listEvalResultadosConv) {
      this.listEvalResultadosConv = listEvalResultadosConv;
   }

   public List<EvalResultadosConv> getListEvalResultadosConvFiltrar() {
      return listEvalResultadosConvFiltrar;
   }

   public void setListEvalResultadosConvFiltrar(List<EvalResultadosConv> listEvalResultadosConvFiltrar) {
      this.listEvalResultadosConvFiltrar = listEvalResultadosConvFiltrar;
   }

   public EvalResultadosConv getNuevoEvalResultadosConv() {
      return nuevoEvalResultadosConv;
   }

   public void setNuevoEvalResultadosConv(EvalResultadosConv nuevoEvalResultadosConv) {
      this.nuevoEvalResultadosConv = nuevoEvalResultadosConv;
   }

   public EvalResultadosConv getDuplicarEvalResultadosConv() {
      return duplicarEvalResultadosConv;
   }

   public void setDuplicarEvalResultadosConv(EvalResultadosConv duplicarEvalResultadosConv) {
      this.duplicarEvalResultadosConv = duplicarEvalResultadosConv;
   }

   public EvalResultadosConv getEditarEvalResultadosConv() {
      return editarEvalResultadosConv;
   }

   public void setEditarEvalResultadosConv(EvalResultadosConv editarEvalResultadosConv) {
      this.editarEvalResultadosConv = editarEvalResultadosConv;
   }

   public EvalResultadosConv getEvalResultadosConvSeleccionado() {
      return evalResultadosConvSeleccionado;
   }

   public void setEvalResultadosConvSeleccionado(EvalResultadosConv evalResultadosConvSeleccionado) {
      this.evalResultadosConvSeleccionado = evalResultadosConvSeleccionado;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTabla2() {
      return altoTabla2;
   }

   public void setAltoTabla2(String altoTabla2) {
      this.altoTabla2 = altoTabla2;
   }

   public String getInfoRegistroConvocatorias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConvocatorias");
      infoRegistroConvocatorias = String.valueOf(tabla.getRowCount());
      return infoRegistroConvocatorias;
   }

   public void setInfoRegistroConvocatorias(String infoRegistroConvocatorias) {
      this.infoRegistroConvocatorias = infoRegistroConvocatorias;
   }

   public String getInfoRegistroResultados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosResultados");
      infoRegistroResultados = String.valueOf(tabla.getRowCount());
      return infoRegistroResultados;
   }

   public void setInfoRegistroResultados(String infoRegistroResultados) {
      this.infoRegistroResultados = infoRegistroResultados;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public int getPromedio() {
      return promedio;
   }

   public void setPromedio(int promedio) {
      this.promedio = promedio;
   }

   public Evalvigconvocatorias getEvalVigParametros() {
      return evalVigParametros;
   }

   public void setEvalVigParametros(Evalvigconvocatorias evalVigParametros) {
      this.evalVigParametros = evalVigParametros;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public void setNombreArchivo(String nombreArchivo) {
      this.nombreArchivo = nombreArchivo;
   }

   public String getTablaImprimir() {
      return tablaImprimir;
   }

   public void setTablaImprimir(String tablaImprimir) {
      this.tablaImprimir = tablaImprimir;
   }

}
