package Administrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import InterfaceAdministrar.AdministrarTerceroInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTercero implements AdministrarTerceroInterface {

   private static Logger log = Logger.getLogger(AdministrarTercero.class);

   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   //

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
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
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Terceros> obtenerListTerceros(BigInteger secuencia) {
      try {
         List<Terceros> listTerceros = persistenciaTerceros.lovTerceros(getEm(), secuencia);
         return listTerceros;
      } catch (Exception e) {
         log.warn("Error en obtenerListTerceros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarTercero(Terceros t) {
      try {
         persistenciaTerceros.editar(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en modificarTercero Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTercero(Terceros t) {
      try {
         persistenciaTerceros.borrar(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en borrarTercero Admi : " + e.toString());

      }
   }

   @Override
   public void crearTercero(Terceros t) {
      try {
         persistenciaTerceros.crear(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en crearTercero Admi : " + e.toString());

      }
   }

   @Override
   public List<TercerosSucursales> obtenerListTercerosSucursales(BigInteger secuencia) {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursalesPorTerceroSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error obtenerListTercerosSucursales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarTerceroSucursales(TercerosSucursales t) {
      try {
         persistenciaTercerosSucursales.editar(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en modificarTerceroSucursales Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTerceroSucursales(TercerosSucursales t) {
      try {
         persistenciaTercerosSucursales.borrar(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en borrarTerceroSucursales Admi : " + e.toString());
      }
   }

   @Override
   public void crearTerceroSucursales(TercerosSucursales t) {
      try {
         persistenciaTercerosSucursales.crear(getEm(), t);
      } catch (Exception e) {
         log.warn("Error en crearTerceroSucursales Admi : " + e.toString());
      }
   }

   @Override
   public List<Empresas> listEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error en listEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Ciudades> listCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("Error en listCiudades Admi : " + e.toString());
         return null;
      }
   }

}
