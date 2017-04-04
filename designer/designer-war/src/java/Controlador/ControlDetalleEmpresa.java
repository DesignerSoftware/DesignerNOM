package Controlador;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.DetallesEmpresas;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Personas;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetallesEmpresasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlDetalleEmpresa implements Serializable {

   @EJB
   AdministrarDetallesEmpresasInterface administrarDetalleEmpresa;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Detalles Empresas
   private List<DetallesEmpresas> listaDetallesEmpresas;
   private List<DetallesEmpresas> filtrarDetallesEmpresas;
   private DetallesEmpresas detalleSeleccionado;
   //LOV Empresas
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> filtrarLovEmpresas;
   //LOV Ciudades
   private List<Ciudades> lovCiudades;
   private Ciudades ciudadSeleccionada;
   private List<Ciudades> filtrarLovCiudades;
   //LOV Empleados
   private List<Empleados> lovEmpleados;
   private Empleados empleadoSeleccionado;
   private List<Empleados> filtrarLovEmpleados;
   //LOV Personas
   private List<Personas> lovPersonas;
   private Personas personaSeleccionada;
   private List<Personas> filtrarLovPersonas;
   //LOV Cargos
   private List<Cargos> lovCargos;
   private Cargos cargoSeleccionado;
   private List<Cargos> filtrarLovCargos;
   //Tipo Actualizacion
   private int tipoActualizacion;
   //Activo/Desactivo VP Crtl + F11
   private int bandera;
   //Columnas Tabla VP
   private Column detalleEmpresa, detalleTipoDocumento, detalleTipo, detalleDireccion, detalleCiudad;
   private Column detalleTelefono, detalleFax, detalleNombreRepresentante, detalleDocumentoRepresentane;
   private Column detalleCiudadDocumento, detalleTipoNit, detalleDigitoVerificacion, detalleGerenteGeneral;
   private Column detallePersonaFirma, detalleCargoFirma, detalleEmail, detalleTipoZona, detalleCIIU, detalleActEconomica;
   private Column detalleSubGerente, detalleArquitecto, detalleCargoArquitecto, detalleRepresentante, detallePlanilla;
   private Column detalleTipoPersona, detalleNaturalezaJ, detalleClaseAportante, detalleFormaPresentacion, detalleTipoAportante;
   private Column detalleTipoAccion, detalleFechaComercio, detalleAnosParafiscal;
   private Column detalleReformaExonera, detallePilaMultinea, detalleSolidaridadFosyga, detalleExoneraLnsTarifa, detalleReportaLnsTarifa;
   //Otros
   private boolean aceptar;
   //modificar
   private List<DetallesEmpresas> listDetallesEmpresasModificar;
   private boolean guardado;
   //crear |
   private List<DetallesEmpresas> listDetallesEmpresasCrear;
   public DetallesEmpresas nuevaDetalleEmpresa;
   private BigInteger l;
   private int k;
   //borrar 
   private List<DetallesEmpresas> listDetallesEmpresasBorrar;
   //editar celda
   private DetallesEmpresas editarDetalleEmpresa;
   //duplicar
   private DetallesEmpresas duplicarDetalleEmpresa;
   //Variables Autompletar      
   private String ciudad, empresa, gerente, representante, cargo, persona, ciudadDocumento, subGerente;
   private int cualCelda, tipoLista;
   private boolean cambiosPagina;
   //
   private Date fechaParametro;
   private Date auxFechaCamaraComercio;
   private String auxTipo, auxDireccion, auxTelefono, auxFax, auxNameRepre, auxDocRepre;
   private String altoTabla;
   //
   private String infoRegistro, infoRegistroEmpresa, infoRegistroCiudad, infoRegistroCiudadDocumento, infoRegistroGerente, infoRegistroPersona, infoRegistroCargo, infoRegistroSubGerente, infoRegistroRepresentante;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlDetalleEmpresa() {
      altoTabla = "300";
      //Otros
      tipoLista = 0;
      k = 0;
      tipoLista = 0;
      guardado = true;
      bandera = 0;
      aceptar = true;
      cualCelda = 0;
      detalleSeleccionado = null;
      cambiosPagina = true;
      //Lista BMC
      listDetallesEmpresasBorrar = new ArrayList<DetallesEmpresas>();
      listDetallesEmpresasModificar = new ArrayList<DetallesEmpresas>();
      listDetallesEmpresasCrear = new ArrayList<DetallesEmpresas>();
      //Editar
      editarDetalleEmpresa = new DetallesEmpresas();
      //LOV
      lovEmpleados = null;
      lovCiudades = null;
      lovEmpresas = null;
      lovCargos = null;
      lovPersonas = null;
      //Nuevo
      nuevaDetalleEmpresa = new DetallesEmpresas();
      //Duplicar
      duplicarDetalleEmpresa = new DetallesEmpresas();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarDetalleEmpresa.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
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
         String pagActual = "detalleempresa";
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
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public boolean validarFechaCamaraComercio(int i) {
      boolean retorno = true;
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      if (i == 0) {
         DetallesEmpresas auxiliar = new DetallesEmpresas();
         auxiliar = detalleSeleccionado;
         if (auxiliar.getFechacamaracomercio() != null) {
            if (auxiliar.getFechacamaracomercio().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaDetalleEmpresa.getFechacamaracomercio() != null) {
            if (nuevaDetalleEmpresa.getFechacamaracomercio().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarDetalleEmpresa.getFechacamaracomercio() != null) {
            if (duplicarDetalleEmpresa.getFechacamaracomercio().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarDatosNullDetalleEmpresa(int i) {
      boolean retorno = true;
      if (i == 0) {
         DetallesEmpresas detalle = new DetallesEmpresas();
         detalle = detalleSeleccionado;
         if (detalle.getRef_empresa() == null) {
            retorno = false;
         }
         if (detalle.getDireccion() == null) {
            retorno = false;
         } else if (detalle.getDireccion().isEmpty()) {
            retorno = false;
         }
         if (detalle.getRef_ciudad() == null) {
            retorno = false;
         }
         if (detalle.getTelefono() == null) {
            retorno = false;
         } else if (detalle.getTelefono().isEmpty()) {
            retorno = false;
         }
         if (detalle.getFax() == null) {
            retorno = false;
         } else if (detalle.getFax().isEmpty()) {
            retorno = false;
         }
         if (detalle.getNombrerepresentante() == null) {
            retorno = false;
         } else if (detalle.getNombrerepresentante().isEmpty()) {
            retorno = false;
         }
         if (detalle.getDocumentorepresentante() == null) {
            retorno = false;
         } else if (detalle.getDocumentorepresentante().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevaDetalleEmpresa.getRef_empresa() == null) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getDireccion() == null) {
            retorno = false;
         } else if (nuevaDetalleEmpresa.getDireccion().isEmpty()) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getRef_ciudad() == null) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getTelefono() == null) {
            retorno = false;
         } else if (nuevaDetalleEmpresa.getTelefono().isEmpty()) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getFax() == null) {
            retorno = false;
         } else if (nuevaDetalleEmpresa.getFax().isEmpty()) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getNombrerepresentante() == null) {
            retorno = false;
         } else if (nuevaDetalleEmpresa.getNombrerepresentante().isEmpty()) {
            retorno = false;
         }
         if (nuevaDetalleEmpresa.getDocumentorepresentante() == null) {
            retorno = false;
         } else if (nuevaDetalleEmpresa.getDocumentorepresentante().isEmpty()) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarDetalleEmpresa.getRef_empresa() == null) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getDireccion() == null) {
            retorno = false;
         } else if (duplicarDetalleEmpresa.getDireccion().isEmpty()) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getRef_ciudad() == null) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getTelefono() == null) {
            retorno = false;
         } else if (duplicarDetalleEmpresa.getTelefono().isEmpty()) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getFax() == null) {
            retorno = false;
         } else if (duplicarDetalleEmpresa.getFax().isEmpty()) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getNombrerepresentante() == null) {
            retorno = false;
         } else if (duplicarDetalleEmpresa.getNombrerepresentante().isEmpty()) {
            retorno = false;
         }
         if (duplicarDetalleEmpresa.getDocumentorepresentante() == null) {
            retorno = false;
         } else if (duplicarDetalleEmpresa.getDocumentorepresentante().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificacionesFechaCamaraComercio(DetallesEmpresas detalle, int c) {
      detalleSeleccionado = detalle;
      if (validarFechaCamaraComercio(0) == true) {
         cambiarIndice(detalleSeleccionado, c);
         modificarDetalleEmpresa(detalleSeleccionado);
      } else {
         detalleSeleccionado.setFechacamaracomercio(auxFechaCamaraComercio);
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
         RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
      }
   }

   public void modificarDetalleEmpresaSOneMenuCheckBox(int indice) {
      if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
         if (listDetallesEmpresasModificar.isEmpty()) {
            listDetallesEmpresasModificar.add(detalleSeleccionado);
         } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
            listDetallesEmpresasModificar.add(detalleSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
         }
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
   }

   public void modificarDetalleEmpresa(DetallesEmpresas detalle) {
      detalleSeleccionado = detalle;
      if (validarDatosNullDetalleEmpresa(0) == true) {
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else {
         detalleSeleccionado.setTelefono(auxTelefono);
         detalleSeleccionado.setDireccion(auxDireccion);
         detalleSeleccionado.setTipo(auxTipo);
         detalleSeleccionado.setFax(auxFax);
         detalleSeleccionado.setNombrerepresentante(auxNameRepre);
         detalleSeleccionado.setDocumentorepresentante(auxDocRepre);
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNull').show()");
      }
   }

   public void modificarDetalleEmpresa(DetallesEmpresas detalle, String confirmarCambio, String valorConfirmar) {
      detalleSeleccionado = detalle;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         detalleSeleccionado.setNombre_ciudad(ciudad);
         for (int i = 0; i < lovCiudades.size(); i++) {
            if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            detalleSeleccionado.setRef_ciudad(lovCiudades.get(indiceUnicoElemento).getSecuencia());
            detalleSeleccionado.setNombre_ciudad(lovCiudades.get(indiceUnicoElemento).getNombre());
            cambiosPagina = false;
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
         detalleSeleccionado.setNombre_empresa(empresa);
         for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            detalleSeleccionado.setRef_empresa(lovEmpresas.get(indiceUnicoElemento).getSecuencia());
            detalleSeleccionado.setNombre_empresa(lovEmpresas.get(indiceUnicoElemento).getNombre());
            cambiosPagina = false;
         } else {
            RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("GERENTE")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_gerentegeneral(gerente);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
               detalleSeleccionado.setRef_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:GerenteDialogo");
               RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            cambiosPagina = false;
            coincidencias = 1;
            detalleSeleccionado.setNombre_gerentegeneral(null);
            detalleSeleccionado.setRef_gerentegeneral(null);
         }
      } else if (confirmarCambio.equalsIgnoreCase("REPRESENTANTE")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_representantecir(representante);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_representantecir(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
               detalleSeleccionado.setRef_representantecir(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
               RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            cambiosPagina = false;
            detalleSeleccionado.setNombre_representantecir(null);
            detalleSeleccionado.setRef_representantecir(null);
         }
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_cargofirmaconstancia(cargo);
            for (int i = 0; i < lovCargos.size(); i++) {
               if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getNombre());
               detalleSeleccionado.setRef_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
               RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            cambiosPagina = false;
            detalleSeleccionado.setRef_cargofirmaconstancia(null);
            detalleSeleccionado.setNombre_cargofirmaconstancia(null);
         }
      } else if (confirmarCambio.equalsIgnoreCase("SUBGERENTE")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_subgerente(subGerente);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_subgerente(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
               detalleSeleccionado.setRef_subgerente(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
               RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            cambiosPagina = false;
            detalleSeleccionado.setRef_subgerente(null);
            detalleSeleccionado.setNombre_subgerente(null);
         }
      } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_personafirmaconstancia(persona);
            for (int i = 0; i < lovPersonas.size(); i++) {
               if (lovPersonas.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getNombreCompleto());
               detalleSeleccionado.setRef_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
               RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            cambiosPagina = false;
            detalleSeleccionado.setNombre_personafirmaconstancia(null);
            detalleSeleccionado.setRef_personafirmaconstancia(null);
         }
      } else if (confirmarCambio.equalsIgnoreCase("CIUDADDOCUMENTO")) {
         if (!valorConfirmar.isEmpty()) {
            detalleSeleccionado.setNombre_ciudaddocumentorepresentante(ciudadDocumento);
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               detalleSeleccionado.setNombre_ciudaddocumentorepresentante(lovCiudades.get(indiceUnicoElemento).getNombre());
               detalleSeleccionado.setRef_ciudaddocrepresentante(lovCiudades.get(indiceUnicoElemento).getSecuencia());
               cambiosPagina = false;
            } else {
               RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            cambiosPagina = false;
            detalleSeleccionado.setNombre_ciudaddocumentorepresentante(null);
            detalleSeleccionado.setRef_ciudaddocrepresentante(null);
         }
      }
      if (coincidencias == 1) {
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {

            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
   }

   public void valoresBackupAutocompletarDetalleEmpresa(int tipoNuevo, String Campo) {

      if (Campo.equals("CIUDAD")) {
         if (tipoNuevo == 1) {
            ciudad = nuevaDetalleEmpresa.getNombre_ciudad();
         } else if (tipoNuevo == 2) {
            ciudad = duplicarDetalleEmpresa.getNombre_ciudad();
         }
      } else if (Campo.equals("CIUDADDOCUMENTO")) {
         if (tipoNuevo == 1) {
            ciudadDocumento = nuevaDetalleEmpresa.getNombre_ciudaddocumentorepresentante();
         } else if (tipoNuevo == 2) {
            ciudadDocumento = duplicarDetalleEmpresa.getNombre_ciudaddocumentorepresentante();
         }
      } else if (Campo.equals("PERSONA")) {
         if (tipoNuevo == 1) {
            persona = nuevaDetalleEmpresa.getNombre_personafirmaconstancia();
         } else if (tipoNuevo == 2) {
            persona = duplicarDetalleEmpresa.getNombre_personafirmaconstancia();
         }
      } else if (Campo.equals("CARGO")) {
         if (tipoNuevo == 1) {
            cargo = nuevaDetalleEmpresa.getNombre_cargofirmaconstancia();
         } else if (tipoNuevo == 2) {
            cargo = duplicarDetalleEmpresa.getNombre_cargofirmaconstancia();
         }
      } else if (Campo.equals("REPRESENTANTE")) {
         if (tipoNuevo == 1) {
            representante = nuevaDetalleEmpresa.getNombre_representantecir();
         } else if (tipoNuevo == 2) {
            representante = duplicarDetalleEmpresa.getNombre_representantecir();
         }
      } else if (Campo.equals("GERENTE")) {
         if (tipoNuevo == 1) {
            gerente = nuevaDetalleEmpresa.getNombre_gerentegeneral();
         } else if (tipoNuevo == 2) {
            gerente = duplicarDetalleEmpresa.getNombre_gerentegeneral();
         }
      } else if (Campo.equals("EMPRESA")) {
         if (tipoNuevo == 1) {
            empresa = nuevaDetalleEmpresa.getNombre_empresa();
         } else if (tipoNuevo == 2) {
            empresa = duplicarDetalleEmpresa.getNombre_empresa();
         }
      } else if (Campo.equals("SUBGERENTE")) {
         if (tipoNuevo == 1) {
            subGerente = nuevaDetalleEmpresa.getNombre_subgerente();
         } else if (tipoNuevo == 2) {
            subGerente = duplicarDetalleEmpresa.getNombre_subgerente();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoDetalleEmpresa(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
         if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setNombre_empresa(empresa);
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setNombre_empresa(empresa);
         }
         for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_empresa(lovEmpresas.get(indiceUnicoElemento).getNombre());
               nuevaDetalleEmpresa.setRef_empresa(lovEmpresas.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaEmpresaDetalle");
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_empresa(lovEmpresas.get(indiceUnicoElemento).getNombre());
               duplicarDetalleEmpresa.setRef_empresa(lovEmpresas.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarEmpresaDetalle");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaEmpresaDetalle");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarEmpresaDetalle");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("GERENTE")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_gerentegeneral(gerente);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_gerentegeneral(gerente);
            }
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  nuevaDetalleEmpresa.setRef_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaGerenteDetalle");
               } else if (tipoNuevo == 2) {
                  duplicarDetalleEmpresa.setRef_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  duplicarDetalleEmpresa.setNombre_gerentegeneral(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarGerenteDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:GerenteDialogo");
               RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaGerenteDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarGerenteDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setNombre_gerentegeneral(null);
            nuevaDetalleEmpresa.setRef_gerentegeneral(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaGerenteDetalle");
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setNombre_gerentegeneral(null);
            duplicarDetalleEmpresa.setRef_gerentegeneral(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarGerenteDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("SUBGERENTE")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_subgerente(subGerente);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_subgerente(subGerente);
            }
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_subgerente(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  nuevaDetalleEmpresa.setRef_subgerente(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaSubGerenteDetalle");
               } else if (tipoNuevo == 2) {
                  duplicarDetalleEmpresa.setNombre_subgerente(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  duplicarDetalleEmpresa.setRef_subgerente(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarSubGerenteDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
               RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaSubGerenteDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarSubGerenteDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setRef_subgerente(null);
            nuevaDetalleEmpresa.setNombre_subgerente(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaSubGerenteDetalle");
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setRef_subgerente(null);
            duplicarDetalleEmpresa.setNombre_subgerente(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarSubGerenteDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("REPRESENTANTE")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_representantecir(representante);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_representantecir(representante);
            }
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_representantecir(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  nuevaDetalleEmpresa.setRef_representantecir(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaRepresentanteDetalle");
               } else {
                  duplicarDetalleEmpresa.setNombre_representantecir(lovEmpleados.get(indiceUnicoElemento).getPersona().getNombreCompleto());
                  duplicarDetalleEmpresa.setRef_representantecir(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarRepresentanteDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
               RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaRepresentanteDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarRepresentanteDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setRef_representantecir(null);
            nuevaDetalleEmpresa.setNombre_representantecir(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaRepresentanteDetalle");
         } else {
            duplicarDetalleEmpresa.setRef_representantecir(null);
            duplicarDetalleEmpresa.setNombre_representantecir(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarRepresentanteDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(cargo);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(cargo);
            }
            for (int i = 0; i < lovCargos.size(); i++) {
               if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getNombre());
                  nuevaDetalleEmpresa.setRef_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCargoFirmaDetalle");
               } else if (tipoNuevo == 2) {
                  duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getNombre());
                  duplicarDetalleEmpresa.setRef_cargofirmaconstancia(lovCargos.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCargoFirmaDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
               RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCargoFirmaDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCargoFirmaDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setRef_cargofirmaconstancia(null);
            nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCargoFirmaDetalle");
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setRef_cargofirmaconstancia(null);
            duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCargoFirmaDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_personafirmaconstancia(persona);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_personafirmaconstancia(persona);
            }
            for (int i = 0; i < lovPersonas.size(); i++) {
               if (lovPersonas.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getNombreCompleto());
                  nuevaDetalleEmpresa.setRef_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaPersonaFirmaDetalle");
               } else if (tipoNuevo == 2) {
                  duplicarDetalleEmpresa.setNombre_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getNombreCompleto());
                  duplicarDetalleEmpresa.setRef_personafirmaconstancia(lovPersonas.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarPersonaFirmaDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
               RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaPersonaFirmaDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarPersonaFirmaDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setNombre_personafirmaconstancia(null);
            nuevaDetalleEmpresa.setRef_personafirmaconstancia(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaPersonaFirmaDetalle");
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setNombre_personafirmaconstancia(null);
            duplicarDetalleEmpresa.setRef_personafirmaconstancia(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarPersonaFirmaDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("CIUDADDOCUMENTO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(ciudadDocumento);
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(ciudadDocumento);
            }
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(lovCiudades.get(indiceUnicoElemento).getNombre());
                  nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(lovCiudades.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDocumentoDetalle");
               } else if (tipoNuevo == 2) {
                  duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(lovCiudades.get(indiceUnicoElemento).getNombre());
                  duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(lovCiudades.get(indiceUnicoElemento).getSecuencia());
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCiudadDocumentoDetalle");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDocumentoDetalle");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCiudadDocumentoDetalle");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
            nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDocumentoDetalle");
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
            duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCiudadDocumentoDetalle");
         }
      } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         if (tipoNuevo == 1) {
            nuevaDetalleEmpresa.setNombre_ciudad(ciudad);
         } else if (tipoNuevo == 2) {
            duplicarDetalleEmpresa.setNombre_ciudad(ciudad);
         }
         for (int i = 0; i < lovCiudades.size(); i++) {
            if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaDetalleEmpresa.setNombre_ciudad(lovCiudades.get(indiceUnicoElemento).getNombre());
               nuevaDetalleEmpresa.setRef_ciudad(lovCiudades.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDetalle");
            } else if (tipoNuevo == 2) {
               duplicarDetalleEmpresa.setNombre_ciudad(lovCiudades.get(indiceUnicoElemento).getNombre());
               duplicarDetalleEmpresa.setRef_ciudad(lovCiudades.get(indiceUnicoElemento).getSecuencia());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa::duplicarCiudadDetalle");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDetalle");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa::duplicarCiudadDetalle");
            }
         }
      }
   }

   public void cambiarIndice(DetallesEmpresas detalle, int celda) {
      detalleSeleccionado = detalle;
      cualCelda = celda;
      empresa = detalleSeleccionado.getNombre_empresa();
      ciudad = detalleSeleccionado.getNombre_ciudad();
      ciudadDocumento = detalleSeleccionado.getNombre_ciudaddocumentorepresentante();
      gerente = detalleSeleccionado.getNombre_gerentegeneral();
      subGerente = detalleSeleccionado.getNombre_subgerente();
      persona = detalleSeleccionado.getNombre_personafirmaconstancia();
      cargo = detalleSeleccionado.getNombre_cargofirmaconstancia();
      representante = detalleSeleccionado.getNombre_representantecir();
      auxTipo = detalleSeleccionado.getTipo();
      auxDireccion = detalleSeleccionado.getDireccion();
      auxTelefono = detalleSeleccionado.getTelefono();
      auxFax = detalleSeleccionado.getFax();
      auxNameRepre = detalleSeleccionado.getNombrerepresentante();
      auxDocRepre = detalleSeleccionado.getDocumentorepresentante();
   }

   public void guardarSalir() {
      guardadoGeneral();
      salir();
   }

   public void cancelaralir() {
      cancelarModificacion();
      salir();
   }

   //GUARDAR
   public void guardadoGeneral() {
      guardarCambios();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarCambios() {
      try {
         if (guardado == false) {
            if (!listDetallesEmpresasBorrar.isEmpty()) {
               administrarDetalleEmpresa.borrarDetalleEmpresa(listDetallesEmpresasBorrar);
               listDetallesEmpresasBorrar.clear();
            }
            if (!listDetallesEmpresasCrear.isEmpty()) {
               administrarDetalleEmpresa.crearDetalleEmpresa(listDetallesEmpresasCrear);
               listDetallesEmpresasCrear.clear();
            }
            if (!listDetallesEmpresasModificar.isEmpty()) {
               administrarDetalleEmpresa.editarDetalleEmpresa(listDetallesEmpresasModificar);
               listDetallesEmpresasModificar.clear();
            }
            listaDetallesEmpresas = null;
            getListaDetallesEmpresas();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
            k = 0;
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            detalleSeleccionado = null;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listDetallesEmpresasBorrar.clear();
      listDetallesEmpresasCrear.clear();
      listDetallesEmpresasModificar.clear();
      detalleSeleccionado = null;
      k = 0;
      listaDetallesEmpresas = null;
      getListaDetallesEmpresas();
      contarRegistros();
      guardado = true;
      nuevaDetalleEmpresa = new DetallesEmpresas();
      nuevaDetalleEmpresa.setRef_ciudad(null);
      nuevaDetalleEmpresa.setNombre_ciudad(null);
      nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(null);
      nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
      nuevaDetalleEmpresa.setRef_empresa(null);
      nuevaDetalleEmpresa.setNombre_empresa(null);
      nuevaDetalleEmpresa.setRef_gerentegeneral(null);
      nuevaDetalleEmpresa.setNombre_gerentegeneral(null);
      nuevaDetalleEmpresa.setRef_representantecir(null);
      nuevaDetalleEmpresa.setNombre_representantecir(null);
      nuevaDetalleEmpresa.setRef_arquitecto(null);
      nuevaDetalleEmpresa.setNombre_arquitecto(null);
      nuevaDetalleEmpresa.setRef_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setRef_personafirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_personafirmaconstancia(null);
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
   }

   public void editarCelda() {
      if (detalleSeleccionado != null) {
         editarDetalleEmpresa = detalleSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaDE");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEmpresaDE");
            RequestContext.getCurrentInstance().execute("PF('editarTipoEmpresaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDireccionDE");
            RequestContext.getCurrentInstance().execute("PF('editarDireccionDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadDE");
            RequestContext.getCurrentInstance().execute("PF('editarCiudadDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefonoDE");
            RequestContext.getCurrentInstance().execute("PF('editarTelefonoDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFaxDE");
            RequestContext.getCurrentInstance().execute("PF('editarFaxDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreRepresentanteDE");
            RequestContext.getCurrentInstance().execute("PF('editarNombreRepresentanteDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDocumentoRepresentanteDE");
            RequestContext.getCurrentInstance().execute("PF('editarDocumentoRepresentanteDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadDocumentoDE");
            RequestContext.getCurrentInstance().execute("PF('editarCiudadDocumentoDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoNitDE");
            RequestContext.getCurrentInstance().execute("PF('editarTipoNitDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDigitoVerificacionDE");
            RequestContext.getCurrentInstance().execute("PF('editarDigitoVerificacionDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGerenteDE");
            RequestContext.getCurrentInstance().execute("PF('editarGerenteDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPersonaFirmaDE");
            RequestContext.getCurrentInstance().execute("PF('editarPersonaFirmaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 14) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoFirmaDE");
            RequestContext.getCurrentInstance().execute("PF('editarCargoFirmaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 15) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmailDE");
            RequestContext.getCurrentInstance().execute("PF('editarEmailDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 17) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCIIUDE");
            RequestContext.getCurrentInstance().execute("PF('editarCIIUDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 18) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarActEconomicaDE");
            RequestContext.getCurrentInstance().execute("PF('editarActEconomicaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 19) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSubGerenteDE");
            RequestContext.getCurrentInstance().execute("PF('editarSubGerenteDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 20) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarArquitectoDE");
            RequestContext.getCurrentInstance().execute("PF('editarArquitectoDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 21) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoArquitectoDE");
            RequestContext.getCurrentInstance().execute("PF('editarCargoArquitectoDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 22) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarRepresentanteDE");
            RequestContext.getCurrentInstance().execute("PF('editarRepresentanteDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 23) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNPlanillaDE");
            RequestContext.getCurrentInstance().execute("PF('editarNPlanillaDE').show()");
            cualCelda = -1;
         } else if (cualCelda == 30) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCamaraComercioDE");
            RequestContext.getCurrentInstance().execute("PF('editarFechaCamaraComercioDE').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevaDetalleEmpresa() {
      if (validarDatosNullDetalleEmpresa(1) == true) {
         if (validarFechaCamaraComercio(1) == true) {
            cambiosPagina = false;
            //CERRAR FILTRADO
            if (bandera == 1) {
               restaurarTabla();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaDetalleEmpresa.setSecuencia(l);
            if (listaDetallesEmpresas == null) {
               listaDetallesEmpresas = new ArrayList<DetallesEmpresas>();
            }
            listaDetallesEmpresas.add(nuevaDetalleEmpresa);
            listDetallesEmpresasCrear.add(nuevaDetalleEmpresa);
            detalleSeleccionado = listaDetallesEmpresas.get(listaDetallesEmpresas.indexOf(nuevaDetalleEmpresa));
            //
            nuevaDetalleEmpresa = new DetallesEmpresas();
            nuevaDetalleEmpresa.setRef_ciudad(null);
            nuevaDetalleEmpresa.setNombre_ciudad(null);
            nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(null);
            nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
            nuevaDetalleEmpresa.setRef_empresa(null);
            nuevaDetalleEmpresa.setNombre_empresa(null);
            nuevaDetalleEmpresa.setRef_gerentegeneral(null);
            nuevaDetalleEmpresa.setNombre_gerentegeneral(null);
            nuevaDetalleEmpresa.setRef_representantecir(null);
            nuevaDetalleEmpresa.setNombre_representantecir(null);
            nuevaDetalleEmpresa.setRef_arquitecto(null);
            nuevaDetalleEmpresa.setNombre_arquitecto(null);
            nuevaDetalleEmpresa.setRef_cargofirmaconstancia(null);
            nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(null);
            nuevaDetalleEmpresa.setRef_personafirmaconstancia(null);
            nuevaDetalleEmpresa.setNombre_personafirmaconstancia(null);
            //
            if (guardado == true) {
               guardado = false;
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleEmpresa').hide()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNull').show()");
      }
   }

   public void limpiarNuevaDetalleEmpresa() {
      nuevaDetalleEmpresa = new DetallesEmpresas();
      nuevaDetalleEmpresa.setRef_ciudad(null);
      nuevaDetalleEmpresa.setNombre_ciudad(null);
      nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(null);
      nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
      nuevaDetalleEmpresa.setRef_empresa(null);
      nuevaDetalleEmpresa.setNombre_empresa(null);
      nuevaDetalleEmpresa.setRef_gerentegeneral(null);
      nuevaDetalleEmpresa.setNombre_gerentegeneral(null);
      nuevaDetalleEmpresa.setRef_representantecir(null);
      nuevaDetalleEmpresa.setNombre_representantecir(null);
      nuevaDetalleEmpresa.setRef_arquitecto(null);
      nuevaDetalleEmpresa.setNombre_arquitecto(null);
      nuevaDetalleEmpresa.setRef_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setRef_personafirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_personafirmaconstancia(null);
   }

   public void cancelarNuevaDetalleEmpresa() {
      nuevaDetalleEmpresa = new DetallesEmpresas();
      nuevaDetalleEmpresa.setRef_ciudad(null);
      nuevaDetalleEmpresa.setNombre_ciudad(null);
      nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(null);
      nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
      nuevaDetalleEmpresa.setRef_empresa(null);
      nuevaDetalleEmpresa.setNombre_empresa(null);
      nuevaDetalleEmpresa.setRef_gerentegeneral(null);
      nuevaDetalleEmpresa.setNombre_gerentegeneral(null);
      nuevaDetalleEmpresa.setRef_representantecir(null);
      nuevaDetalleEmpresa.setNombre_representantecir(null);
      nuevaDetalleEmpresa.setRef_arquitecto(null);
      nuevaDetalleEmpresa.setNombre_arquitecto(null);
      nuevaDetalleEmpresa.setRef_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(null);
      nuevaDetalleEmpresa.setRef_personafirmaconstancia(null);
      nuevaDetalleEmpresa.setNombre_personafirmaconstancia(null);
   }

   public void verificarDuplicarDetalleEmpresa() {
      if (detalleSeleccionado != null) {
         duplicarDetalleEmpresaD();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarDetalleEmpresaD() {
      if (detalleSeleccionado != null) {
         duplicarDetalleEmpresa = new DetallesEmpresas();
         duplicarDetalleEmpresa.setRef_empresa(detalleSeleccionado.getRef_empresa());
         duplicarDetalleEmpresa.setNombre_empresa(detalleSeleccionado.getNombre_empresa());
         duplicarDetalleEmpresa.setTipodocumento(detalleSeleccionado.getTipodocumento());
         duplicarDetalleEmpresa.setTipo(detalleSeleccionado.getTipo());
         duplicarDetalleEmpresa.setDireccion(detalleSeleccionado.getDireccion());
         duplicarDetalleEmpresa.setRef_ciudad(detalleSeleccionado.getRef_ciudad());
         duplicarDetalleEmpresa.setNombre_ciudad(detalleSeleccionado.getNombre_ciudad());
         duplicarDetalleEmpresa.setTelefono(detalleSeleccionado.getTelefono());
         duplicarDetalleEmpresa.setFax(detalleSeleccionado.getFax());
         duplicarDetalleEmpresa.setNombrerepresentante(detalleSeleccionado.getNombrerepresentante());
         duplicarDetalleEmpresa.setDocumentorepresentante(detalleSeleccionado.getDocumentorepresentante());
         duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(detalleSeleccionado.getRef_ciudaddocrepresentante());
         duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(detalleSeleccionado.getNombre_ciudaddocumentorepresentante());
         duplicarDetalleEmpresa.setTiponit(detalleSeleccionado.getTiponit());
         duplicarDetalleEmpresa.setDigitoverificacion(detalleSeleccionado.getDigitoverificacion());
         duplicarDetalleEmpresa.setRef_gerentegeneral(detalleSeleccionado.getRef_gerentegeneral());
         duplicarDetalleEmpresa.setNombre_gerentegeneral(detalleSeleccionado.getNombre_gerentegeneral());
         duplicarDetalleEmpresa.setRef_personafirmaconstancia(detalleSeleccionado.getRef_personafirmaconstancia());
         duplicarDetalleEmpresa.setNombre_personafirmaconstancia(detalleSeleccionado.getNombre_personafirmaconstancia());
         duplicarDetalleEmpresa.setRef_cargofirmaconstancia(detalleSeleccionado.getRef_cargofirmaconstancia());
         duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(detalleSeleccionado.getNombre_cargofirmaconstancia());
         duplicarDetalleEmpresa.setEmail(detalleSeleccionado.getEmail());
         duplicarDetalleEmpresa.setZona(detalleSeleccionado.getZona());
         duplicarDetalleEmpresa.setCiiu(detalleSeleccionado.getCiiu());
         duplicarDetalleEmpresa.setActividadeconomica(detalleSeleccionado.getActividadeconomica());
         duplicarDetalleEmpresa.setRef_subgerente(detalleSeleccionado.getRef_subgerente());
         duplicarDetalleEmpresa.setNombre_subgerente(detalleSeleccionado.getNombre_subgerente());
         duplicarDetalleEmpresa.setRef_arquitecto(detalleSeleccionado.getRef_arquitecto());
         duplicarDetalleEmpresa.setNombre_arquitecto(detalleSeleccionado.getNombre_arquitecto());
         duplicarDetalleEmpresa.setCargoarquitecto(detalleSeleccionado.getCargoarquitecto());
         duplicarDetalleEmpresa.setRef_representantecir(detalleSeleccionado.getRef_representantecir());
         duplicarDetalleEmpresa.setNombre_representantecir(detalleSeleccionado.getNombre_representantecir());
         duplicarDetalleEmpresa.setPilaultimaplanilla(detalleSeleccionado.getPilaultimaplanilla());
         duplicarDetalleEmpresa.setTipopersona(detalleSeleccionado.getTipopersona());
         duplicarDetalleEmpresa.setNaturalezajuridica(detalleSeleccionado.getNaturalezajuridica());
         duplicarDetalleEmpresa.setClaseaportante(detalleSeleccionado.getClaseaportante());
         duplicarDetalleEmpresa.setFormapresentacion(detalleSeleccionado.getFormapresentacion());
         duplicarDetalleEmpresa.setTipoaportante(detalleSeleccionado.getTipoaportante());
         duplicarDetalleEmpresa.setTipoaccion(detalleSeleccionado.getTipoaccion());
         duplicarDetalleEmpresa.setFechacamaracomercio(detalleSeleccionado.getFechacamaracomercio());
         duplicarDetalleEmpresa.setAnosinicialesexentoprf(detalleSeleccionado.getAnosinicialesexentoprf());
         duplicarDetalleEmpresa.setCheckExoneraLnsTarifaAfpPatron(detalleSeleccionado.isCheckExoneraLnsTarifaAfpPatron());
         duplicarDetalleEmpresa.setCheckPilaSsaMultilineasln(detalleSeleccionado.isCheckPilaSsaMultilineasln());
         duplicarDetalleEmpresa.setCheckReformaExoneraIcbfSenaSalud(detalleSeleccionado.isCheckReformaExoneraIcbfSenaSalud());
         duplicarDetalleEmpresa.setCheckReportaLnsTarifaAfpEspecial(detalleSeleccionado.isCheckReportaLnsTarifaAfpEspecial());
         duplicarDetalleEmpresa.setCheckSolidaridadFosygaeExentoPrf(detalleSeleccionado.isCheckSolidaridadFosygaeExentoPrf());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleEmpresa').show()");
      }
   }

   public void confirmarDuplicarDetalleEmpresa() {
      if (validarDatosNullDetalleEmpresa(2) == true) {
         if (validarFechaCamaraComercio(2) == true) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarDetalleEmpresa.setSecuencia(l);
            cambiosPagina = false;
            k++;
            l = BigInteger.valueOf(k);
            duplicarDetalleEmpresa.setSecuencia(l);
            if (listaDetallesEmpresas == null) {
               listaDetallesEmpresas = new ArrayList<DetallesEmpresas>();
            }
            listaDetallesEmpresas.add(duplicarDetalleEmpresa);
            listDetallesEmpresasCrear.add(duplicarDetalleEmpresa);
            detalleSeleccionado = listaDetallesEmpresas.get(listaDetallesEmpresas.indexOf(duplicarDetalleEmpresa));
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
               restaurarTabla();
            }
            duplicarDetalleEmpresa = new DetallesEmpresas();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleEmpresa').hide()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNull').show()");
      }
   }

   public void cancelarDuplicadoDetalleEmpresa() {
      duplicarDetalleEmpresa = new DetallesEmpresas();
      duplicarDetalleEmpresa.setRef_ciudad(null);
      duplicarDetalleEmpresa.setNombre_ciudad(null);
      duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(null);
      duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
      duplicarDetalleEmpresa.setRef_empresa(null);
      duplicarDetalleEmpresa.setNombre_empresa(null);
      duplicarDetalleEmpresa.setRef_gerentegeneral(null);
      duplicarDetalleEmpresa.setNombre_gerentegeneral(null);
      duplicarDetalleEmpresa.setRef_representantecir(null);
      duplicarDetalleEmpresa.setNombre_representantecir(null);
      duplicarDetalleEmpresa.setRef_arquitecto(null);
      duplicarDetalleEmpresa.setNombre_arquitecto(null);
      duplicarDetalleEmpresa.setRef_cargofirmaconstancia(null);
      duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(null);
      duplicarDetalleEmpresa.setRef_personafirmaconstancia(null);
      duplicarDetalleEmpresa.setNombre_personafirmaconstancia(null);
   }

   public void limpiarDuplicadoDetalleEmpresa() {
      duplicarDetalleEmpresa = new DetallesEmpresas();
      duplicarDetalleEmpresa.setRef_ciudad(null);
      duplicarDetalleEmpresa.setNombre_ciudad(null);
      duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(null);
      duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(null);
      duplicarDetalleEmpresa.setRef_empresa(null);
      duplicarDetalleEmpresa.setNombre_empresa(null);
      duplicarDetalleEmpresa.setRef_gerentegeneral(null);
      duplicarDetalleEmpresa.setNombre_gerentegeneral(null);
      duplicarDetalleEmpresa.setRef_representantecir(null);
      duplicarDetalleEmpresa.setNombre_representantecir(null);
      duplicarDetalleEmpresa.setRef_arquitecto(null);
      duplicarDetalleEmpresa.setNombre_arquitecto(null);
      duplicarDetalleEmpresa.setRef_cargofirmaconstancia(null);
      duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(null);
      duplicarDetalleEmpresa.setRef_personafirmaconstancia(null);
      duplicarDetalleEmpresa.setNombre_personafirmaconstancia(null);
   }

   public void validarBorradoDetalleEmpresa() {
      if (detalleSeleccionado != null) {
         borrarDetalleEmpresa();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarDetalleEmpresa() {
      cambiosPagina = false;
      if (!listDetallesEmpresasModificar.isEmpty() && listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
         listDetallesEmpresasModificar.remove(detalleSeleccionado);
         listDetallesEmpresasBorrar.add(detalleSeleccionado);
      } else if (!listDetallesEmpresasCrear.isEmpty() && listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
         listDetallesEmpresasCrear.remove(detalleSeleccionado);
      } else {
         listDetallesEmpresasBorrar.add(detalleSeleccionado);
      }
      listaDetallesEmpresas.remove(detalleSeleccionado);
      if (tipoLista == 1) {
         filtrarDetallesEmpresas.remove(detalleSeleccionado);
      }
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      detalleSeleccionado = null;
      if (guardado == true) {
         guardado = false;
      }
   }

   public void activarCtrlF11() {
      filtradoDetalleEmpresa();
   }

   public void filtradoDetalleEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "280";
         detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleEmpresa");
         detalleEmpresa.setFilterStyle("width: 85% !important");
         detalleTipoDocumento = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoDocumento");
         detalleTipoDocumento.setFilterStyle("width: 85% !important");
         detalleTipo = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipo");
         detalleTipo.setFilterStyle("width: 85% !important");
         detalleDireccion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDireccion");
         detalleDireccion.setFilterStyle("width: 85% !important");
         detalleCiudad = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCiudad");
         detalleCiudad.setFilterStyle("width: 85% !important");
         detalleTelefono = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTelefono");
         detalleTelefono.setFilterStyle("width: 85% !important");
         detalleFax = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFax");
         detalleFax.setFilterStyle("width: 85% !important");
         detalleNombreRepresentante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleNombreRepresentante");
         detalleNombreRepresentante.setFilterStyle("width: 85% !important");
         detalleDocumentoRepresentane = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDocumentoRepresentane");
         detalleDocumentoRepresentane.setFilterStyle("width: 85% !important");
         detalleCiudadDocumento = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCiudadDocumento");
         detalleCiudadDocumento.setFilterStyle("width: 85% !important");
         detalleTipoNit = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoNit");
         detalleTipoNit.setFilterStyle("width: 85% !important");
         detalleDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDigitoVerificacion");
         detalleDigitoVerificacion.setFilterStyle("width: 85% !important");
         detalleGerenteGeneral = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleGerenteGeneral");
         detalleGerenteGeneral.setFilterStyle("width: 85% !important");
         detallePersonaFirma = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePersonaFirma");
         detallePersonaFirma.setFilterStyle("width: 85% !important");
         detalleCargoFirma = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCargoFirma");
         detalleCargoFirma.setFilterStyle("width: 85% !important");
         detalleEmail = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleEmail");
         detalleEmail.setFilterStyle("width: 85% !important");
         detalleTipoZona = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoZona");
         detalleTipoZona.setFilterStyle("width: 85% !important");
         detalleCIIU = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCIIU");
         detalleCIIU.setFilterStyle("width: 85% !important");
         detalleActEconomica = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleActEconomica");
         detalleActEconomica.setFilterStyle("width: 85% !important");
         detalleSubGerente = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleSubGerente");
         detalleSubGerente.setFilterStyle("width: 85% !important");
         detalleArquitecto = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleArquitecto");
         detalleArquitecto.setFilterStyle("width: 85% !important");
         detalleCargoArquitecto = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCargoArquitecto");
         detalleCargoArquitecto.setFilterStyle("width: 85% !important");
         detalleRepresentante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleRepresentante");
         detalleRepresentante.setFilterStyle("width: 85% !important");
         detallePlanilla = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePlanilla");
         detallePlanilla.setFilterStyle("width: 85% !important");
         detalleTipoPersona = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoPersona");
         detalleTipoPersona.setFilterStyle("width: 85% !important");
         detalleNaturalezaJ = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleNaturalezaJ");
         detalleNaturalezaJ.setFilterStyle("width: 85% !important");
         detalleClaseAportante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleClaseAportante");
         detalleClaseAportante.setFilterStyle("width: 85% !important");
         detalleFormaPresentacion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFormaPresentacion");
         detalleFormaPresentacion.setFilterStyle("width: 85% !important");
         detalleTipoAportante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoAportante");
         detalleTipoAportante.setFilterStyle("width: 85% !important");
         detalleTipoAccion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoAccion");
         detalleTipoAccion.setFilterStyle("width: 85% !important");
         detalleFechaComercio = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFechaComercio");
         detalleFechaComercio.setFilterStyle("width: 85% !important");
         detalleAnosParafiscal = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleAnosParafiscal");
         detalleAnosParafiscal.setFilterStyle("width: 85% !important");
         detalleReformaExonera = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleReformaExonera");
         detalleReformaExonera.setFilterStyle("width: 85% !important");
         detallePilaMultinea = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePilaMultinea");
         detallePilaMultinea.setFilterStyle("width: 85% !important");
         detalleSolidaridadFosyga = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleSolidaridadFosyga");
         detalleSolidaridadFosyga.setFilterStyle("width: 85% !important");
         detalleExoneraLnsTarifa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleExoneraLnsTarifa");
         detalleExoneraLnsTarifa.setFilterStyle("width: 85% !important");
         detalleReportaLnsTarifa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleReportaLnsTarifa");
         detalleReportaLnsTarifa.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
         tipoLista = 1;
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      altoTabla = "300";
      FacesContext c = FacesContext.getCurrentInstance();
      detalleEmpresa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleEmpresa");
      detalleEmpresa.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoDocumento = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoDocumento");
      detalleTipoDocumento.setFilterStyle("display: none; visibility: hidden;");
      detalleTipo = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipo");
      detalleTipo.setFilterStyle("display: none; visibility: hidden;");
      detalleDireccion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDireccion");
      detalleDireccion.setFilterStyle("display: none; visibility: hidden;");
      detalleCiudad = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCiudad");
      detalleCiudad.setFilterStyle("display: none; visibility: hidden;");
      detalleTelefono = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTelefono");
      detalleTelefono.setFilterStyle("display: none; visibility: hidden;");
      detalleFax = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFax");
      detalleFax.setFilterStyle("display: none; visibility: hidden;");
      detalleNombreRepresentante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleNombreRepresentante");
      detalleNombreRepresentante.setFilterStyle("display: none; visibility: hidden;");
      detalleDocumentoRepresentane = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDocumentoRepresentane");
      detalleDocumentoRepresentane.setFilterStyle("display: none; visibility: hidden;");
      detalleCiudadDocumento = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCiudadDocumento");
      detalleCiudadDocumento.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoNit = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoNit");
      detalleTipoNit.setFilterStyle("display: none; visibility: hidden;");
      detalleDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleDigitoVerificacion");
      detalleDigitoVerificacion.setFilterStyle("display: none; visibility: hidden;");
      detalleGerenteGeneral = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleGerenteGeneral");
      detalleGerenteGeneral.setFilterStyle("display: none; visibility: hidden;");
      detallePersonaFirma = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePersonaFirma");
      detallePersonaFirma.setFilterStyle("display: none; visibility: hidden;");
      detalleCargoFirma = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCargoFirma");
      detalleCargoFirma.setFilterStyle("display: none; visibility: hidden;");
      detalleEmail = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleEmail");
      detalleEmail.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoZona = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoZona");
      detalleTipoZona.setFilterStyle("display: none; visibility: hidden;");
      detalleCIIU = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCIIU");
      detalleCIIU.setFilterStyle("display: none; visibility: hidden;");
      detalleActEconomica = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleActEconomica");
      detalleActEconomica.setFilterStyle("display: none; visibility: hidden;");
      detalleSubGerente = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleSubGerente");
      detalleSubGerente.setFilterStyle("display: none; visibility: hidden;");
      detalleArquitecto = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleArquitecto");
      detalleArquitecto.setFilterStyle("display: none; visibility: hidden;");
      detalleCargoArquitecto = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleCargoArquitecto");
      detalleCargoArquitecto.setFilterStyle("display: none; visibility: hidden;");
      detalleRepresentante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleRepresentante");
      detalleRepresentante.setFilterStyle("display: none; visibility: hidden;");
      detallePlanilla = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePlanilla");
      detallePlanilla.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoPersona = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoPersona");
      detalleTipoPersona.setFilterStyle("display: none; visibility: hidden;");
      detalleNaturalezaJ = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleNaturalezaJ");
      detalleNaturalezaJ.setFilterStyle("display: none; visibility: hidden;");
      detalleClaseAportante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleClaseAportante");
      detalleClaseAportante.setFilterStyle("display: none; visibility: hidden;");
      detalleFormaPresentacion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFormaPresentacion");
      detalleFormaPresentacion.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoAportante = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoAportante");
      detalleTipoAportante.setFilterStyle("display: none; visibility: hidden;");
      detalleTipoAccion = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleTipoAccion");
      detalleTipoAccion.setFilterStyle("display: none; visibility: hidden;");
      detalleFechaComercio = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleFechaComercio");
      detalleFechaComercio.setFilterStyle("display: none; visibility: hidden;");
      detalleAnosParafiscal = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleAnosParafiscal");
      detalleAnosParafiscal.setFilterStyle("display: none; visibility: hidden;");
      detalleReformaExonera = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleReformaExonera");
      detalleReformaExonera.setFilterStyle("display: none; visibility: hidden;");
      detallePilaMultinea = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detallePilaMultinea");
      detallePilaMultinea.setFilterStyle("display: none; visibility: hidden;");
      detalleSolidaridadFosyga = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleSolidaridadFosyga");
      detalleSolidaridadFosyga.setFilterStyle("display: none; visibility: hidden;");
      detalleExoneraLnsTarifa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleExoneraLnsTarifa");
      detalleExoneraLnsTarifa.setFilterStyle("display: none; visibility: hidden;");
      detalleReportaLnsTarifa = (Column) c.getViewRoot().findComponent("form:datosDetalleEmpresa:detalleReportaLnsTarifa");
      detalleReportaLnsTarifa.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      bandera = 0;
      filtrarDetallesEmpresas = null;
      tipoLista = 0;
   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listDetallesEmpresasBorrar.clear();
      listDetallesEmpresasCrear.clear();
      listDetallesEmpresasModificar.clear();
      detalleSeleccionado = null;
      k = 0;
      listaDetallesEmpresas = null;
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      tipoActualizacion = -1;
      navegar("atras");
   }

   public void asignarIndex(DetallesEmpresas detalle, int dlg, int LND) {
      detalleSeleccionado = detalle;
      if (LND == 0) {
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
      } else if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:CiudadDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
      } else if (dlg == 2) {
         RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').show()");
      } else if (dlg == 3) {
         RequestContext.getCurrentInstance().update("form:GerenteDialogo");
         RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').show()");
      } else if (dlg == 4) {
         RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
         RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').show()");
      } else if (dlg == 5) {
         RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
         RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').show()");
      } else if (dlg == 6) {
         RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
         RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').show()");
      } else if (dlg == 7) {
         RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
         RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').show()");
      }
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_empresa(empresaSeleccionada.getNombre());
         detalleSeleccionado.setRef_empresa(empresaSeleccionada.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_empresa(empresaSeleccionada.getNombre());
         nuevaDetalleEmpresa.setRef_empresa(empresaSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaEmpresaDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_empresa(empresaSeleccionada.getNombre());
         duplicarDetalleEmpresa.setRef_empresa(empresaSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarEmpresaDetalle");
      }
      filtrarLovEmpresas = null;
      empresaSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      context.reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void cancelarCambioEmpresa() {
      filtrarLovEmpresas = null;
      empresaSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      context.reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void actualizarCiudad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_ciudad(ciudadSeleccionada.getNombre());
         detalleSeleccionado.setRef_ciudad(ciudadSeleccionada.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_ciudad(ciudadSeleccionada.getNombre());
         nuevaDetalleEmpresa.setRef_ciudad(ciudadSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_ciudad(ciudadSeleccionada.getNombre());
         duplicarDetalleEmpresa.setRef_ciudad(ciudadSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCiudadDetalle");
      }
      filtrarLovCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:CiudadDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudad");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovCiudad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
   }

   public void cancelarCambioCiudad() {
      filtrarLovCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CiudadDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudad");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovCiudad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
   }

   public void actualizarCiudadDocumento() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_ciudaddocumentorepresentante(ciudadSeleccionada.getNombre());
         detalleSeleccionado.setRef_ciudaddocrepresentante(ciudadSeleccionada.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_ciudaddocumentorepresentante(ciudadSeleccionada.getNombre());
         nuevaDetalleEmpresa.setRef_ciudaddocrepresentante(ciudadSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCiudadDocumentoDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_ciudaddocumentorepresentante(ciudadSeleccionada.getNombre());
         duplicarDetalleEmpresa.setRef_ciudaddocrepresentante(ciudadSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCiudadDocumentoDetalle");
      }
      filtrarLovCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudadDocumento");
      RequestContext.getCurrentInstance().update("form:aceptarCD");
      context.reset("form:lovCiudadDocumento:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').hide()");
   }

   public void cancelarCambioCiudadDocumento() {
      filtrarLovCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudadDocumento");
      RequestContext.getCurrentInstance().update("form:aceptarCD");
      context.reset("form:lovCiudadDocumento:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').hide()");
   }

   public void actualizarGerente() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_gerentegeneral(empleadoSeleccionado.getPersona().getNombreCompleto());
         detalleSeleccionado.setRef_gerentegeneral(empleadoSeleccionado.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_gerentegeneral(empleadoSeleccionado.getPersona().getNombreCompleto());
         nuevaDetalleEmpresa.setRef_gerentegeneral(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaGerenteDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_gerentegeneral(empleadoSeleccionado.getPersona().getNombreCompleto());
         duplicarDetalleEmpresa.setRef_gerentegeneral(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarGerenteDetalle");
      }
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:GerenteDialogo");
      RequestContext.getCurrentInstance().update("form:lovGerente");
      RequestContext.getCurrentInstance().update("form:aceptarG");
      context.reset("form:lovGerente:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGerente').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').hide()");
   }

   public void cancelarCambioGerente() {
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:GerenteDialogo");
      RequestContext.getCurrentInstance().update("form:lovGerente");
      RequestContext.getCurrentInstance().update("form:aceptarG");
      context.reset("form:lovGerente:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGerente').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').hide()");
   }

   public void actualizarSubGerente() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_subgerente(empleadoSeleccionado.getPersona().getNombreCompleto());
         detalleSeleccionado.setRef_subgerente(empleadoSeleccionado.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_subgerente(empleadoSeleccionado.getPersona().getNombreCompleto());
         nuevaDetalleEmpresa.setRef_subgerente(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaSubGerenteDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_subgerente(empleadoSeleccionado.getPersona().getNombreCompleto());
         duplicarDetalleEmpresa.setRef_subgerente(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarSubGerenteDetalle");
      }
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
      RequestContext.getCurrentInstance().update("form:lovSubGerente");
      RequestContext.getCurrentInstance().update("form:aceptarSG");
      context.reset("form:lovRepresentante:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovSubGerente').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').hide()");
   }

   public void cancelarCambioSubGerente() {
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
      RequestContext.getCurrentInstance().update("form:lovSubGerente");
      RequestContext.getCurrentInstance().update("form:aceptarSG");
      context.reset("form:lovRepresentante:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovSubGerente').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').hide()");
   }

   public void actualizarRepresentante() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_representantecir(empleadoSeleccionado.getPersona().getNombreCompleto());
         detalleSeleccionado.setRef_representantecir(empleadoSeleccionado.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_representantecir(empleadoSeleccionado.getPersona().getNombreCompleto());
         nuevaDetalleEmpresa.setRef_representantecir(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaRepresentanteDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_representantecir(empleadoSeleccionado.getPersona().getNombreCompleto());
         duplicarDetalleEmpresa.setRef_representantecir(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarRepresentanteDetalle");
      }
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
      RequestContext.getCurrentInstance().update("form:lovRepresentante");
      RequestContext.getCurrentInstance().update("form:aceptarR");
      context.reset("form:lovRepresentante:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovRepresentante').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').hide()");
   }

   public void cancelarCambioRepresentante() {
      filtrarLovEmpleados = null;
      empleadoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
      RequestContext.getCurrentInstance().update("form:lovRepresentante");
      RequestContext.getCurrentInstance().update("form:aceptarR");
      context.reset("form:lovRepresentante:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovRepresentante').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').hide()");
   }

   public void actualizarPersona() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_personafirmaconstancia(personaSeleccionada.getNombreCompleto());
         detalleSeleccionado.setRef_personafirmaconstancia(personaSeleccionada.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_personafirmaconstancia(personaSeleccionada.getNombreCompleto());
         nuevaDetalleEmpresa.setRef_personafirmaconstancia(personaSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaPersonaFirmaDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_personafirmaconstancia(personaSeleccionada.getNombreCompleto());
         duplicarDetalleEmpresa.setRef_personafirmaconstancia(personaSeleccionada.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarPersonaFirmaDetalle");
      }
      filtrarLovPersonas = null;
      personaSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
      RequestContext.getCurrentInstance().update("form:lovPersona");
      RequestContext.getCurrentInstance().update("form:aceptarPF");
      context.reset("form:lovPersona:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPersona').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').hide()");
   }

   public void cancelarCambioPersona() {
      filtrarLovPersonas = null;
      personaSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
      RequestContext.getCurrentInstance().update("form:lovPersona");
      RequestContext.getCurrentInstance().update("form:aceptarPF");
      context.reset("form:lovPersona:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPersona').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').hide()");
   }

   public void actualizarCargo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         detalleSeleccionado.setNombre_cargofirmaconstancia(cargoSeleccionado.getNombre());
         detalleSeleccionado.setRef_cargofirmaconstancia(cargoSeleccionado.getSecuencia());
         if (!listDetallesEmpresasCrear.contains(detalleSeleccionado)) {
            if (listDetallesEmpresasModificar.isEmpty()) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            } else if (!listDetallesEmpresasModificar.contains(detalleSeleccionado)) {
               listDetallesEmpresasModificar.add(detalleSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevaDetalleEmpresa.setNombre_cargofirmaconstancia(cargoSeleccionado.getNombre());
         nuevaDetalleEmpresa.setRef_cargofirmaconstancia(cargoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDetalleEmpresa:nuevaCargoFirmaDetalle");
      } else if (tipoActualizacion == 2) {
         duplicarDetalleEmpresa.setNombre_cargofirmaconstancia(cargoSeleccionado.getNombre());
         duplicarDetalleEmpresa.setRef_cargofirmaconstancia(cargoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDetalleEmpresa:duplicarCargoFirmaDetalle");
      }
      filtrarLovCargos = null;
      cargoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
      RequestContext.getCurrentInstance().update("form:lovCargoFirma");
      RequestContext.getCurrentInstance().update("form:aceptarCF");
      context.reset("form:lovCargoFirma:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCargoFirma').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').hide()");
   }

   public void cancelarCambioCargo() {
      filtrarLovCargos = null;
      cargoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
      RequestContext.getCurrentInstance().update("form:lovCargoFirma");
      RequestContext.getCurrentInstance().update("form:aceptarCF");
      context.reset("form:lovCargoFirma:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCargoFirma').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').hide()");
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node 
      String type = map.get("t"); // type attribute of node 
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      detalleSeleccionado = listaDetallesEmpresas.get(indice);
      cambiarIndice(detalleSeleccionado, columna);
   }

   public void listaValoresBoton() {
      if (detalleSeleccionado != null) {
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("form:CiudadDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("form:GerenteDialogo");
            RequestContext.getCurrentInstance().execute("PF('GerenteDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("form:PersonaFirmaDialogo");
            RequestContext.getCurrentInstance().execute("PF('PersonaFirmaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 14) {
            RequestContext.getCurrentInstance().update("form:CargoFirmaDialogo");
            RequestContext.getCurrentInstance().execute("PF('CargoFirmaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 19) {
            RequestContext.getCurrentInstance().update("form:SubGerenteDialogo");
            RequestContext.getCurrentInstance().execute("PF('SubGerenteDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 22) {
            RequestContext.getCurrentInstance().update("form:RepresentanteDialogo");
            RequestContext.getCurrentInstance().execute("PF('RepresentanteDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public void validarExportPDF() throws IOException {
      exportPDF_DE();
   }

   public void exportPDF_DE() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDE:datosDetalleExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "DetallesEmpresas_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarExportXLS() throws IOException {
      exportXLS_EM();
   }

   public void exportXLS_EM() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDE:datosDetalleExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DetallesEmpresas_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      detalleSeleccionado = null;
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      if (detalleSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(detalleSeleccionado.getSecuencia(), "DETALLESEMPRESAS");
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
      } else if (administrarRastros.verificarHistoricosTabla("DETALLESEMPRESAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //GET - SET 
   public List<DetallesEmpresas> getListaDetallesEmpresas() {
      if (listaDetallesEmpresas == null) {
         System.out.println("ControlDetalleEmpresa.getListaDetallesEmpresas() Va a consultar");
         listaDetallesEmpresas = administrarDetalleEmpresa.listaDetallesEmpresas();
         System.out.println("ControlDetalleEmpresa.getListaDetallesEmpresas() Ya consulto");
      }
      return listaDetallesEmpresas;
   }

   public void setListaDetallesEmpresas(List<DetallesEmpresas> setListaDetallesEmpresas) {
      this.listaDetallesEmpresas = setListaDetallesEmpresas;
   }

   public List<DetallesEmpresas> getFiltrarDetallesEmpresas() {
      return filtrarDetallesEmpresas;
   }

   public void setFiltrarDetallesEmpresas(List<DetallesEmpresas> setFiltrarListaDetallesEmpresas) {
      this.filtrarDetallesEmpresas = setFiltrarListaDetallesEmpresas;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarDetalleEmpresa.lovEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> setLovEmpresas) {
      this.lovEmpresas = setLovEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas setEmpresaSeleccionada) {
      this.empresaSeleccionada = setEmpresaSeleccionada;
   }

   public List<Empresas> getFiltrarLovEmpresas() {
      return filtrarLovEmpresas;
   }

   public void setFiltrarLovEmpresas(List<Empresas> setFiltrarLovEmpresas) {
      this.filtrarLovEmpresas = setFiltrarLovEmpresas;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarDetalleEmpresa.lovCiudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> setLovCiudades) {
      this.lovCiudades = setLovCiudades;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades setCiudadSeleccionada) {
      this.ciudadSeleccionada = setCiudadSeleccionada;
   }

   public List<Ciudades> getFiltrarLovCiudades() {
      return filtrarLovCiudades;
   }

   public void setFiltrarLovCiudades(List<Ciudades> setFiltrarLovCiudades) {
      this.filtrarLovCiudades = setFiltrarLovCiudades;
   }

   public List<DetallesEmpresas> getListDetallesEmpresasModificar() {
      return listDetallesEmpresasModificar;
   }

   public void setListDetallesEmpresasModificar(List<DetallesEmpresas> setListDetallesEmpresasModificar) {
      this.listDetallesEmpresasModificar = setListDetallesEmpresasModificar;
   }

   public DetallesEmpresas getNuevaDetalleEmpresa() {
      return nuevaDetalleEmpresa;
   }

   public void setNuevaDetalleEmpresa(DetallesEmpresas setNuevaDetalleEmpresa) {
      this.nuevaDetalleEmpresa = setNuevaDetalleEmpresa;
   }

   public List<DetallesEmpresas> getListDetallesEmpresasBorrar() {
      return listDetallesEmpresasBorrar;
   }

   public void setListDetallesEmpresasBorrar(List<DetallesEmpresas> setListDetallesEmpresasBorrar) {
      this.listDetallesEmpresasBorrar = setListDetallesEmpresasBorrar;
   }

   public DetallesEmpresas getEditarDetalleEmpresa() {
      return editarDetalleEmpresa;
   }

   public void setEditarDetalleEmpresa(DetallesEmpresas setEditarDetalleEmpresa) {
      this.editarDetalleEmpresa = setEditarDetalleEmpresa;
   }

   public DetallesEmpresas getDuplicarDetalleEmpresa() {
      return duplicarDetalleEmpresa;
   }

   public void setDuplicarDetalleEmpresa(DetallesEmpresas setDuplicarDetalleEmpresa) {
      this.duplicarDetalleEmpresa = setDuplicarDetalleEmpresa;
   }

   public List<DetallesEmpresas> getListDetallesEmpresasCrear() {
      return listDetallesEmpresasCrear;
   }

   public void setListDetallesEmpresasCrear(List<DetallesEmpresas> setListDetallesEmpresasCrear) {
      this.listDetallesEmpresasCrear = setListDetallesEmpresasCrear;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarDetalleEmpresa.lovEmpleados();
      }
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> setLovEmpleados) {
      this.lovEmpleados = setLovEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados setEmpleadoSeleccionado) {
      this.empleadoSeleccionado = setEmpleadoSeleccionado;
   }

   public List<Empleados> getFiltrarLovEmpleados() {
      return filtrarLovEmpleados;
   }

   public void setFiltrarLovEmpleados(List<Empleados> setFiltrarLovEmpleados) {
      this.filtrarLovEmpleados = setFiltrarLovEmpleados;
   }

   public List<Personas> getLovPersonas() {
      lovPersonas = administrarDetalleEmpresa.lovPersonas();
      return lovPersonas;
   }

   public void setLovPersonas(List<Personas> setLovPersonas) {
      this.lovPersonas = setLovPersonas;
   }

   public Personas getPersonaSeleccionada() {
      return personaSeleccionada;
   }

   public void setPersonaSeleccionada(Personas setPersonaSeleccionada) {
      this.personaSeleccionada = setPersonaSeleccionada;
   }

   public List<Personas> getFiltrarLovPersonas() {
      return filtrarLovPersonas;
   }

   public void setFiltrarLovPersonas(List<Personas> setFiltrarLovPersonas) {
      this.filtrarLovPersonas = setFiltrarLovPersonas;
   }

   public List<Cargos> getLovCargos() {
      lovCargos = administrarDetalleEmpresa.lovCargos();
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public List<Cargos> getFiltrarLovCargos() {
      return filtrarLovCargos;
   }

   public void setFiltrarLovCargos(List<Cargos> filtrarLovCargos) {
      this.filtrarLovCargos = filtrarLovCargos;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public DetallesEmpresas getDetalleSeleccionado() {
      return detalleSeleccionado;
   }

   public void setDetalleSeleccionado(DetallesEmpresas detalleSeleccionado) {
      this.detalleSeleccionado = detalleSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDetalleEmpresa");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public String getInfoRegistroCiudad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudad");
      infoRegistroCiudad = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad;
   }

   public void setInfoRegistroCiudad(String infoRegistroCiudad) {
      this.infoRegistroCiudad = infoRegistroCiudad;
   }

   public String getInfoRegistroCiudadDocumento() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudadDocumento");
      infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudadDocumento;
   }

   public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
      this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
   }

   public String getInfoRegistroGerente() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGerente");
      infoRegistroGerente = String.valueOf(tabla.getRowCount());
      return infoRegistroGerente;
   }

   public void setInfoRegistroGerente(String infoRegistroGerente) {
      this.infoRegistroGerente = infoRegistroGerente;
   }

   public String getInfoRegistroPersona() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPersona");
      infoRegistroPersona = String.valueOf(tabla.getRowCount());
      return infoRegistroPersona;
   }

   public void setInfoRegistroPersona(String infoRegistroPersona) {
      this.infoRegistroPersona = infoRegistroPersona;
   }

   public String getInfoRegistroCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCargoFirma");
      infoRegistroCargo = String.valueOf(tabla.getRowCount());
      return infoRegistroCargo;
   }

   public void setInfoRegistroCargo(String infoRegistroCargo) {
      this.infoRegistroCargo = infoRegistroCargo;
   }

   public String getInfoRegistroSubGerente() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovSubGerente");
      infoRegistroSubGerente = String.valueOf(tabla.getRowCount());
      return infoRegistroSubGerente;
   }

   public void setInfoRegistroSubGerente(String infoRegistroSubGerente) {
      this.infoRegistroSubGerente = infoRegistroSubGerente;
   }

   public String getInfoRegistroRepresentante() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovRepresentante");
      infoRegistroRepresentante = String.valueOf(tabla.getRowCount());
      return infoRegistroRepresentante;
   }

   public void setInfoRegistroRepresentante(String infoRegistroRepresentante) {
      this.infoRegistroRepresentante = infoRegistroRepresentante;
   }

}
