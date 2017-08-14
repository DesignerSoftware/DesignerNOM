/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Aficiones;
import Entidades.Empresas;
import Entidades.Modulos;
import Entidades.Pantallas;
import Entidades.Tablas;
import InterfaceAdministrar.AdministrarCarpetaDesignerInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAficionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaTablasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;
import javax.persistence.NoResultException;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CarpetaDesigner'.
 *
 * @author -Felipphe-
 */
@Stateful
public class AdministrarCarpetaDesigner implements AdministrarCarpetaDesignerInterface {

   private static Logger log = Logger.getLogger(AdministrarCarpetaDesigner.class);

   @EJB
   PersistenciaModulosInterface persistenciaModulos;
   @EJB
   PersistenciaTablasInterface persistenciaTablas;
   @EJB
   PersistenciaPantallasInterface persistenciaPantallas;
   @EJB
   PersistenciaAficionesInterface persistenciaAficiones;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;

   public List<Modulos> listModulos;
   public List<Tablas> listTablas;
   public Pantallas pantalla;
   public List<Aficiones> listAficiones;
   public Aficiones aficion;
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Modulos> consultarModulos() {
      try {
         return persistenciaModulos.buscarModulos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Tablas> consultarTablas(BigInteger secuenciaMod) {
      try {
         return persistenciaTablas.buscarTablas(getEm(), secuenciaMod);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Pantallas consultarPantalla(BigInteger secuenciaTab) {
      try {
         return persistenciaPantallas.buscarPantalla(getEm(), secuenciaTab);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public String consultarNombrePantalla(BigInteger secuenciaTab) {
      try {
         Pantallas p = persistenciaPantallas.buscarPantalla(getEm(), secuenciaTab);
         return p.getNombre();
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Aficiones> consultarAficiones() {
      try {
         return persistenciaAficiones.buscarAficiones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Aficiones consultarAficion(BigInteger secuencia) {
      try {
         aficion = persistenciaAficiones.buscarAficion(getEm(), secuencia);
         return aficion;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarAficiones(List<Aficiones> listAficiones) {
      try {
         for (int i = 0; i < listAficiones.size(); i++) {
            aficion = listAficiones.get(i);
            persistenciaAficiones.editar(getEm(), aficion);
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Integer sugerirCodigoAficiones() {
      try {
         Integer max;
         Short respuesta;
         respuesta = persistenciaAficiones.maximoCodigoAficiones(getEm());
         max = respuesta.intValue();
         max = max + 1;
         return max;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearAficion(Aficiones aficion) {
      try {
         persistenciaAficiones.crear(getEm(), aficion);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarAficion(Aficiones aficion) {
      try {
         persistenciaAficiones.borrar(getEm(), aficion);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Aficiones consultarAficionCodigo(Short cod) {
      try {
         return persistenciaAficiones.buscarAficionCodigo(getEm(), cod);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarNombrePantallaPorEmpresa(Short codPantalla) throws Exception {
      List<Empresas> listaempresas;
      String nomPantalla = " ";
      BigInteger secEmpresa = BigInteger.ZERO;
      try {
         listaempresas = persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (NoResultException nre) {
         log.error("Error: AdministrarCarpetaDesigner consultarNombrePantallaPorEmpresa " + nre.getMessage());
         //return " ";
         throw new Exception("No hay empresas disponibles para el usuario.");
      }
      if (listaempresas != null) {
         if (!listaempresas.isEmpty()) {
            if (listaempresas.size() == 1) {
               secEmpresa = listaempresas.get(0).getSecuencia();
            } else {
               try {
                  secEmpresa = persistenciaParametrosEstructuras.buscarEmpresaParametros(getEm());
               } catch (NoResultException nre) {
                  throw new Exception("El usuario no tiene configurada la empresa.");
               }
            }
         }
      }
      try {
         nomPantalla = persistenciaPantallas.buscarPantallaPorCodigoEmpresa(getEm(), secEmpresa, codPantalla);
      } catch (NoResultException nre) {
         try {
            nomPantalla = persistenciaPantallas.buscarPantallaPorCodigo(getEm(), codPantalla);
         } catch (NoResultException nre2) {
            throw new Exception("No hay pantalla configurada para el código " + codPantalla);
         }
      }
      return nomPantalla;
   }

   @Override
   public List<Tablas> consultarTablas() {
      try {
         return persistenciaTablas.consultarTablas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
