package Administrar;

import Entidades.ActualUsuario;
import Entidades.DetallesEmpresas;
import Entidades.Empresas;
import Entidades.Generales;
import Entidades.ParametrosEstructuras;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTemplateInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaDetallesEmpresasInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaParametrosAnualesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author -Felipphe-
 */
@Stateful
@Local
public class AdministrarTemplate implements AdministrarTemplateInterface, Serializable {

   private static Logger log = Logger.getLogger(AdministrarTemplate.class);

   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaActualUsuario'.
    */
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   /**
    * Enterprise JavaBean.<br>
    * Representa la interfaz para acceder a las rutas de generales.
    */
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   /**
    * Enterprise JavaBean.<br>
    * Representa la interfaz para acceder a los datos de las empresas.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBean.<br>
    * Representa la interfaz para acceder a los datos de detallesempresas.
    */
   @EJB
   PersistenciaDetallesEmpresasInterface persistenciaDetallesEmpresas;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaPerfilesInterface persistenciaPerfiles;
   @EJB
   PersistenciaParametrosAnualesInterface persistenciaParametrosAnuales;

   private EntityManager em;
   private Generales general;

   @Override
   public boolean obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
      return em != null;
   }

   @Override
   public ActualUsuario consultarActualUsuario() {
      return persistenciaActualUsuario.actualUsuarioBD(em);
   }

   @Override
   public String logoEmpresa() {
      String rutaLogo;
      general = persistenciaGenerales.obtenerRutas(em);
      if (general != null) {
         Empresas empresa = persistenciaEmpresas.consultarPrimeraEmpresaSinPaquete(em);
         if (empresa != null) {
            rutaLogo = general.getPathfoto() + empresa.getNit() + ".png";
         } else {
            rutaLogo = general.getPathfoto() + "sinLogo.png";
         }
      } else {
         return null;
      }
      return rutaLogo;
   }

   @Override
   public String rutaFotoUsuario() {
      String rutaFoto;
      general = persistenciaGenerales.obtenerRutas(em);
      if (general != null) {
         rutaFoto = general.getPathfoto();
      } else {
         return null;
      }
      return rutaFoto;
   }

   @Override
   public void cerrarSession(String idSesion) {
      if (em.isOpen()) {
         em.getEntityManagerFactory().close();
         administrarSesiones.borrarSesion(idSesion);
      }
   }

   @Override
   public DetallesEmpresas consultarDetalleEmpresaUsuario() {
      try {
         Short codigoEmpresa = persistenciaEmpresas.codigoEmpresa(em);
         //log.warn("Codigo empresa: "+codigoEmpresa);
         DetallesEmpresas detallesEmpresas = persistenciaDetallesEmpresas.buscarDetalleEmpresa(em, codigoEmpresa);
         //log.warn("detallesempresas: "+detallesEmpresas);
         return detallesEmpresas;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametrosUsuario() {
      //log.warn("AdministrarTemplate.consultarParametrosUsuario");
      try {
         ParametrosEstructuras parametrosEstructuras = persistenciaParametrosEstructuras.buscarParametro(em, consultarActualUsuario().getAlias());
         return parametrosEstructuras;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public String consultarNombrePerfil() {
      return persistenciaPerfiles.consultarPerfil(em, consultarActualUsuario().getPerfil()).getDescripcion();
   }

   @Override
   public BigDecimal consultarSMLV() {
      try {
         BigDecimal smlv = persistenciaParametrosAnuales.consultarSMLV(em);
         return smlv;
      } catch (Exception e) {
         log.warn("error en consultarSMLV admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarAuxTrans() {
      try {
         BigDecimal auxtrans = persistenciaParametrosAnuales.consultarAuxTransporte(em);
         return auxtrans;
      } catch (Exception e) {
         log.warn("error en consultarAuxTrans admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarUVT() {
      try {
         BigDecimal uvt = persistenciaParametrosAnuales.consultarValorUVT(em);
         return uvt;
      } catch (Exception e) {
         log.warn("error en consultarUVT admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarMinIBC() {
      try {
         BigDecimal minibc = persistenciaParametrosAnuales.consultarvalorMinIBC(em);
         return minibc;
      } catch (Exception e) {
         log.warn("error en consultarMinIBC admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarTopeSegSocial() {
      try {
         BigDecimal segsocial = persistenciaParametrosAnuales.consultarTopeMaxSegSocial(em);
         return segsocial;
      } catch (Exception e) {
         log.warn("error en consultarTopeSegSocial admi " + e.toString());
         return null;
      }
   }
}
