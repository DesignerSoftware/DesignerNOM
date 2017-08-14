/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import InterfaceAdministrar.AdministrarCausasAusentismosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarCausasAusentismos implements AdministrarCausasAusentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarCausasAusentismos.class);

   @EJB
   PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;

   @EJB
   PersistenciaCausasAusentismosInterface persistenciaCausasausentismos;

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
   public List<Causasausentismos> consultarCausasAusentismos() {
      try {
         List<Causasausentismos> listaCausasAusentismo;
         listaCausasAusentismo = persistenciaCausasausentismos.buscarCausasAusentismos(getEm());
         return listaCausasAusentismo;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Clasesausentismos> consultarClasesAusentismos() {
      try {
         List<Clasesausentismos> listaClasesAusentismos;
         listaClasesAusentismos = persistenciaClasesAusentismos.buscarClasesAusentismos(getEm());
         return listaClasesAusentismos;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarCausasAusentismos(List<Causasausentismos> listaCausasAusentismo) {
      try {
         for (int i = 0; i < listaCausasAusentismo.size(); i++) {
            log.warn("Modificando...CausasAusentismos");
            log.warn("posicion : " + i + " secuencia : " + listaCausasAusentismo.get(i).getSecuencia());
            if (listaCausasAusentismo.get(i).getCodigo() == (null)) {
               listaCausasAusentismo.get(i).setCodigo(null);
               persistenciaCausasausentismos.editar(getEm(), listaCausasAusentismo.get(i));
            } else if (listaCausasAusentismo.get(i).getClase().getSecuencia() == null) {
               listaCausasAusentismo.get(i).setClase(null);
            } else {
               persistenciaCausasausentismos.editar(getEm(), listaCausasAusentismo.get(i));
            }
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarCausasAusentismos(List<Causasausentismos> listaCausasAusentismo) {
      try {
         for (int i = 0; i < listaCausasAusentismo.size(); i++) {
            log.warn("Borrando..CausasAusentismos.");
            if (listaCausasAusentismo.get(i).getCodigo() == (null)) {
               listaCausasAusentismo.get(i).setCodigo(null);
               persistenciaCausasausentismos.borrar(getEm(), listaCausasAusentismo.get(i));
            } else if (listaCausasAusentismo.get(i).getClase().getSecuencia() == null) {
               listaCausasAusentismo.get(i).setClase(null);
            } else {
               persistenciaCausasausentismos.borrar(getEm(), listaCausasAusentismo.get(i));
            }
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearCausasAusentismos(List<Causasausentismos> listaCausasAusentismo) {
      try {
         for (int i = 0; i < listaCausasAusentismo.size(); i++) {
            log.warn("Creando. JornadasLaborales..");
            if (listaCausasAusentismo.get(i).getCodigo() == (null)) {
               listaCausasAusentismo.get(i).setCodigo(null);
               persistenciaCausasausentismos.crear(getEm(), listaCausasAusentismo.get(i));
            } else if (listaCausasAusentismo.get(i).getClase().getSecuencia() == null) {
               listaCausasAusentismo.get(i).setClase(null);
            } else {
               persistenciaCausasausentismos.crear(getEm(), listaCausasAusentismo.get(i));
            }
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
