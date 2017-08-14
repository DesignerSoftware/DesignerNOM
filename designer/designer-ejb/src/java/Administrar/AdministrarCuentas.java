/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Cuentas;
import Entidades.Empresas;
import Entidades.Rubrospresupuestales;
import InterfaceAdministrar.AdministrarCuentasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaRubrosPresupuestalesInterface;
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
 * 'Cuentas'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarCuentas implements AdministrarCuentasInterface {

   private static Logger log = Logger.getLogger(AdministrarCuentas.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCuentas'.
    */
   @EJB
   PersistenciaCuentasInterface persistenciaCuentas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaRubrosPresupuestales'.
    */
   @EJB
   PersistenciaRubrosPresupuestalesInterface persistenciaRubrosPresupuestales;
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

   @Override
   public void crearCuentas(List<Cuentas> listCuentasCrear) {
      try {
         for (int i = 0; i < listCuentasCrear.size(); i++) {
            if (listCuentasCrear.get(i).getContracuentatesoreria().getSecuencia() == null) {
               listCuentasCrear.get(i).setContracuentatesoreria(null);
            }
            if (listCuentasCrear.get(i).getRubropresupuestal().getSecuencia() == null) {
               listCuentasCrear.get(i).setRubropresupuestal(null);
            }
            persistenciaCuentas.crear(getEm(), listCuentasCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en crearCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void modificarCuentas(List<Cuentas> listCuentasModificar) {
      try {
         for (int i = 0; i < listCuentasModificar.size(); i++) {

            if (listCuentasModificar.get(i).getRubropresupuestal() != null) {
               if (listCuentasModificar.get(i).getRubropresupuestal().getSecuencia() == null) {
                  listCuentasModificar.get(i).setRubropresupuestal(null);
               }
            }

            if (listCuentasModificar.get(i).getContracuentatesoreria().getSecuencia() == null) {
               listCuentasModificar.get(i).setContracuentatesoreria(null);
            }

            persistenciaCuentas.editar(getEm(), listCuentasModificar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en modificarCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCuentas(List<Cuentas> listCuentasBorrar) {
      try {
         for (int i = 0; i < listCuentasBorrar.size(); i++) {
            if (listCuentasBorrar.get(i).getRubropresupuestal() != null) {
               if (listCuentasBorrar.get(i).getRubropresupuestal().getSecuencia() == null) {
                  listCuentasBorrar.get(i).setRubropresupuestal(null);
               }
            }

            if (listCuentasBorrar.get(i).getContracuentatesoreria().getSecuencia() == null) {
               listCuentasBorrar.get(i).setContracuentatesoreria(null);
            }

            persistenciaCuentas.borrar(getEm(), listCuentasBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en borrarCuentas Admi : " + e.toString());
      }
   }

   @Override
   public List<Cuentas> consultarCuentasEmpresa(BigInteger secuencia) {
      try {
         List<Cuentas> listCuentas = persistenciaCuentas.buscarCuentasSecuenciaEmpresa(getEm(), secuencia);
         return listCuentas;
      } catch (Exception e) {
         log.warn("Error en listCuentasEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> consultarEmpresas() {
      try {
         List<Empresas> listEmpresas = persistenciaEmpresas.consultarEmpresas(getEm());
         return listEmpresas;
      } catch (Exception e) {
         log.warn("Error en listEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Rubrospresupuestales> consultarLOVRubros() {
      try {
         List<Rubrospresupuestales> listRubros = persistenciaRubrosPresupuestales.buscarRubros(getEm());
         return listRubros;
      } catch (Exception e) {
         log.warn("Error en listRubros Admi : " + e.toString());
         return null;
      }
   }
}
