/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PreguntasKioskos;
import InterfaceAdministrar.AdministrarPreguntasKioscosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPreguntasKioscosInterface;
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
public class AdministrarPreguntasKioscos implements AdministrarPreguntasKioscosInterface {

   private static Logger log = Logger.getLogger(AdministrarPreguntasKioscos.class);

   @EJB
   PersistenciaPreguntasKioscosInterface persistenciaPreguntasKioskos;
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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
   public void modificarPreguntasKioskos(List<PreguntasKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaPreguntasKioskos.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en modificarPreguntasKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public void borrarPreguntasKioskos(List<PreguntasKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaPreguntasKioskos.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en borrarPreguntasKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public void crearPreguntasKioskos(List<PreguntasKioskos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaPreguntasKioskos.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("error en crearPreguntasKioskos admi : " + e.getMessage());
      }
   }

   @Override
   public List<PreguntasKioskos> consultarPreguntasKioskos() {
      try {
         return persistenciaPreguntasKioskos.consultarPreguntasKioskos(getEm());
      } catch (Exception e) {
         log.warn("error en consultarPreguntasKioskos  admi : " + e.getMessage());
         return null;
      }
   }

   @Override
   public PreguntasKioskos consultarPreguntaKiosko(BigInteger secPreguntaKiosko) {
      try {
         return persistenciaPreguntasKioskos.consultarPreguntaKiosko(getEm(), secPreguntaKiosko);
      } catch (Exception e) {
         log.warn("error en consultarPreguntasKiosko : " + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contarPreguntasKioskos(BigInteger secuencia) {
      try {
         return persistenciaPreguntasKioskos.contarPreguntasKioskos(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("error en consultarPreguntasKiosko : " + e.getMessage());
         return null;
      }
   }

}
