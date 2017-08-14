/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Departamentos;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarDepartamentosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import InterfacePersistencia.PersistenciaPaisesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Departamentos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDepartamentos implements AdministrarDepartamentosInterface {

   private static Logger log = Logger.getLogger(AdministrarDepartamentos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDepartamentos'.
    */
   @EJB
   PersistenciaDepartamentosInterface persistenciaDepartamentos;
   @EJB
   PersistenciaPaisesInterface persistenciaPaises;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
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

   public void modificarDepartamentos(List<Departamentos> listaDepartamentos) {
      try {
         for (int i = 0; i < listaDepartamentos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaDepartamentos.editar(getEm(), listaDepartamentos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarDepartamentos(List<Departamentos> listaDepartamentos) {
      try {
         for (int i = 0; i < listaDepartamentos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaDepartamentos.borrar(getEm(), listaDepartamentos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearDepartamentos(List<Departamentos> listaDepartamentos) {
      try {
         for (int i = 0; i < listaDepartamentos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaDepartamentos.crear(getEm(), listaDepartamentos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Departamentos> consultarDepartamentos() {
      try {
         return persistenciaDepartamentos.consultarDepartamentos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Departamentos consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaDepartamentos.consultarDepartamento(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Paises> consultarLOVPaises() {
      try {
         return persistenciaPaises.consultarPaises(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarBienProgramacionesDepartamento(BigInteger secuenciaVigenciasIndicadores) {
      try {
         return persistenciaDepartamentos.contarBienProgramacionesDepartamento(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdministrarDepartamenos contarBienProgramacionesDepartamento ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarCapModulosDepartamento(BigInteger secuenciaVigenciasIndicadores) {
      try {
         return persistenciaDepartamentos.contarCapModulosDepartamento(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdministrarDepartamenos contarCapModulosDepartamento ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarSoAccidentesMedicosDepartamento(BigInteger secuenciaVigenciasIndicadores) {
      try {
         return persistenciaDepartamentos.contarSoAccidentesMedicosDepartamento(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdministrarDepartamenos contarSoAccidentesMedicosDepartamento ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarCiudadesDepartamento(BigInteger secuenciaVigenciasIndicadores) {
      try {
         return persistenciaDepartamentos.contarCiudadesDepartamento(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdministrarDepartamenos contarCiudadesDepartamento ERROR :" + e);
         return null;
      }
   }

}
