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
import javax.persistence.EntityManagerFactory;
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

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;
   private Generales general;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
               if (this.em.isOpen()) {
                  this.em.close();
               }
            }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public boolean obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
      return emf != null;
   }

   @Override
   public ActualUsuario consultarActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + ".consultarActualUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String logoEmpresa() {
      try {
         String rutaLogo;
         general = persistenciaGenerales.obtenerRutas(getEm());
         if (general != null) {
            Empresas empresa = persistenciaEmpresas.consultarPrimeraEmpresaSinPaquete(getEm());
            if (empresa != null) {
               rutaLogo = general.getPathfoto() + empresa.getNit() + ".png";
            } else {
               rutaLogo = general.getPathfoto() + "sinLogo.png";
            }
         } else {
            return null;
         }
         return rutaLogo;
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + ".logoEmpresa() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String rutaFotoUsuario() {
      try {
         String rutaFoto;
         general = persistenciaGenerales.obtenerRutas(getEm());
         if (general != null) {
            rutaFoto = general.getPathfoto();
         } else {
            return null;
         }
         return rutaFoto;
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + ".rutaFotoUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cerrarSession(String idSesion) {
      try {
         if (getEm().isOpen()) {
            emf.close();
            administrarSesiones.borrarSesion(idSesion);
         }
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + ".cerrarSession() ERROR: " + e);
      }
   }

   @Override
   public DetallesEmpresas consultarDetalleEmpresaUsuario() {
      try {
         Short codigoEmpresa = persistenciaEmpresas.codigoEmpresa(getEm());
         log.warn("consultarDetalleEmpresaUsuario() Codigo empresa: " + codigoEmpresa);
         DetallesEmpresas detallesEmpresas = persistenciaDetallesEmpresas.buscarDetalleEmpresa(getEm(), codigoEmpresa);
         log.warn("consultarDetalleEmpresaUsuario() detallesempresas: " + detallesEmpresas);
         return detallesEmpresas;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDetalleEmpresaUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametrosUsuario() {
      log.warn("AdministrarTemplate.consultarParametrosUsuario");
      try {
         ActualUsuario au = consultarActualUsuario();
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), au.getAlias());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarParametrosUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarNombrePerfil() {
      try {
         log.warn("AdministrarTemplate.consultarNombrePerfil()");
         ActualUsuario au = consultarActualUsuario();
         return persistenciaPerfiles.consultarPerfil(getEm(), au.getPerfil()).getDescripcion();
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarNombrePerfil() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigDecimal consultarSMLV() {
      try {
         return persistenciaParametrosAnuales.consultarSMLV(getEm());
      } catch (Exception e) {
         log.warn("error en consultarSMLV admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarAuxTrans() {
      try {
         return persistenciaParametrosAnuales.consultarAuxTransporte(getEm());
      } catch (Exception e) {
         log.warn("error en consultarAuxTrans admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarUVT() {
      try {
         return persistenciaParametrosAnuales.consultarValorUVT(getEm());
      } catch (Exception e) {
         log.warn("error en consultarUVT admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarMinIBC() {
      try {
         return persistenciaParametrosAnuales.consultarvalorMinIBC(getEm());
      } catch (Exception e) {
         log.warn("error en consultarMinIBC admi " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarTopeSegSocial() {
      try {
         return persistenciaParametrosAnuales.consultarTopeMaxSegSocial(getEm());
      } catch (Exception e) {
         log.warn("error en consultarTopeSegSocial admi " + e.toString());
         return null;
      }
   }
}
