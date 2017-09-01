/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoAntecedentes;
import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarSoAntecedentes implements AdministrarSoAntecedentesInterface {

   private static Logger log = Logger.getLogger(AdministrarSoAntecedentes.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaSoAntecedentesInterface persistenciaAntecedentes;
   @EJB
   PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
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
   public void modificarAntecedente(List<SoAntecedentes> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaAntecedentes.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearAntecedente(List<SoAntecedentes> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaAntecedentes.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarAntecedente(List<SoAntecedentes> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAntecedentes.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SoAntecedentes> consultarAntecedentesPorTipo(BigInteger secTipoAntecedente) {
      try {
         return persistenciaAntecedentes.lovAntecedentes(getEm(), secTipoAntecedente);
      } catch (Exception e) {
         log.warn("error en consultarAntecedentesPorTipo : " + e.toString());
         return null;
      }
   }

   @Override
   public List<SoAntecedentes> consultarAntecedentes() {
      try {
         return persistenciaAntecedentes.listaAntecedentes(getEm());
      } catch (Exception e) {
         log.warn("error en consultarAntecedentes : " + e.toString());
         return null;
      }

   }

   @Override
   public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
      try {
         return persistenciaTiposAntecedentes.listaTiposAntecedentes(getEm());
      } catch (Exception e) {
         log.warn("error en consultarTiposAntecedentes");
         return null;
      }
   }

}
