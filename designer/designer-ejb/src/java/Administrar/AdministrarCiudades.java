/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Ciudades;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Ciudades'.
 *
 * @author betelgeuse
 */


@Stateful
public class AdministrarCiudades implements AdministrarCiudadesInterface {

   private static Logger log = Logger.getLogger(AdministrarCiudades.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaCiudades'.
    */
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaDepartamentos'.
    */
   @EJB
   PersistenciaDepartamentosInterface persistenciaDepartamentos;
   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
    */
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
   public List<Ciudades> consultarCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarCiudades(List<Ciudades> listaCiudades) {
      try {
         Ciudades c;
         for (int i = 0; i < listaCiudades.size(); i++) {
            log.warn("Modificando...");
            if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {
               listaCiudades.get(i).setDepartamento(null);
               c = listaCiudades.get(i);
            } else {
               c = listaCiudades.get(i);
            }
            persistenciaCiudades.editar(getEm(), c);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public boolean borrarCiudades(List<Ciudades> listaCiudades) {
      try {
         boolean borradosTodos = true;
         for (int i = 0; i < listaCiudades.size(); i++) {
            if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {
               listaCiudades.get(i).setDepartamento(null);
            }
            if (!persistenciaCiudades.borrar(getEm(), listaCiudades.get(i))) {
               borradosTodos = false;
            }
         }
         return borradosTodos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

   @Override
   public void crearCiudades(List<Ciudades> listaCiudades) {
      try {
         for (int i = 0; i < listaCiudades.size(); i++) {
            log.warn("Creando...");
            if (listaCiudades.get(i).getDepartamento().getSecuencia() == null) {

               listaCiudades.get(i).setDepartamento(null);
               persistenciaCiudades.crear(getEm(), listaCiudades.get(i));
            } else {
               persistenciaCiudades.crear(getEm(), listaCiudades.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public int existeenUbicacionesGeograficas(BigInteger secCiudad) {
      try {
         return persistenciaUbicacionesGeograficas.existeCiudadporSecuencia(getEm(), secCiudad);
      } catch (Exception e) {
         log.error("ERROR: AdministrarCiudades. existeenUbicacionesGeograficas() : " + e);
         return 0;
      }
   }
}
