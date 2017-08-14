/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.NormasLaborales;
import Entidades.VigenciasNormasEmpleados;
import InterfaceAdministrar.AdministrarVigenciaNormaLaboralInterface;
import InterfacePersistencia.PersistenciaNormasLaboralesInterface;
import InterfacePersistencia.PersistenciaVigenciasNormasEmpleadosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
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
 * @author John Pineda
 */
@Stateful
public class AdministrarVigenciaNormaLaboral implements AdministrarVigenciaNormaLaboralInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciaNormaLaboral.class);

   /**
    * CREACION DE LOS EJB
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaNormasLaboralesInterface persistenciaNormasLaborales;
   @EJB
   PersistenciaVigenciasNormasEmpleadosInterface persistenciaVigenciasNormasEmpleados;
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

   /**
    * Creacion de metodos
    */
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasNormasEmpleados> consultarVigenciasNormasEmpleadosPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasNormasEmpleados.buscarVigenciasNormasEmpleadosEmpl(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
      try {
         for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.editar(getEm(), listaVigenciasNormasEmpleados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
      try {
         for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.borrar(getEm(), listaVigenciasNormasEmpleados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
      try {
         for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.crear(getEm(), listaVigenciasNormasEmpleados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<NormasLaborales> lovNormasLaborales() {
      try {
         return persistenciaNormasLaborales.consultarNormasLaborales(getEm());
      } catch (Exception e) {
         log.error("ERROR EN AdministrarVigencianormaLaboral en NormasLabolares ERROR " + e);
         return null;
      }
   }
}
