/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Evalconvocatorias;
import InterfaceAdministrar.AdministrarEvalConvocatoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalConvocatoriasInterface;
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
public class AdministrarEvalConvocatorias implements AdministrarEvalConvocatoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalConvocatorias.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaEvalConvocatoriasInterface persistenciaevalconv;
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
   public void crear(List<Evalconvocatorias> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            persistenciaevalconv.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalConvocatorias.crear : " + e.toString());
      }
   }

   @Override
   public void borrar(List<Evalconvocatorias> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaevalconv.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalConvocatorias.borrar : " + e.toString());
      }
   }

   @Override
   public void editar(List<Evalconvocatorias> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            persistenciaevalconv.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarEvalConvocatorias.editar : " + e.toString());
      }
   }

   @Override
   public List<Evalconvocatorias> buscarEvalConvocatorias(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaevalconv.consultarEvalConvocatorias(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
