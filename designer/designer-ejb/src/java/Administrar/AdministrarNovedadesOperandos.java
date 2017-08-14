/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.NovedadesOperandos;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarNovedadesOperandosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaNovedadesOperandosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarNovedadesOperandos implements AdministrarNovedadesOperandosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesOperandos.class);

   @EJB
   PersistenciaNovedadesOperandosInterface persistenciaNovedadesOperandos;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
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

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<NovedadesOperandos> buscarNovedadesOperandos(BigInteger secuenciaOperando) {
      return persistenciaNovedadesOperandos.novedadesOperandos(getEm(), secuenciaOperando);
   }

   @Override
   public void borrarNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
      persistenciaNovedadesOperandos.borrar(getEm(), novedadesOperandos);
   }

   @Override
   public void crearNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
      try {
         persistenciaNovedadesOperandos.crear(getEm(), novedadesOperandos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
      try {
         persistenciaNovedadesOperandos.editar(getEm(), novedadesOperandos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Operandos> buscarOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
