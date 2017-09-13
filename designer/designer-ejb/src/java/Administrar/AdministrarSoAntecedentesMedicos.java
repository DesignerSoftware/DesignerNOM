/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
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
public class AdministrarSoAntecedentesMedicos implements AdministrarSoAntecedentesMedicosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoAntecedentesMedicos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaSoAntecedentesMedicosInterface PersistenciaAntecedentesM;
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
   public void modificarAntecedentesM(List<SoAntecedentesMedicos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            PersistenciaAntecedentesM.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarAntecedentesM() ERROR: " + e);
      }
   }

   @Override
   public void crearAntecedentesM(List<SoAntecedentesMedicos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            PersistenciaAntecedentesM.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearAntecedentesM() ERROR: " + e);
      }
   }

   @Override
   public void borrarAntecedentesM(List<SoAntecedentesMedicos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            PersistenciaAntecedentesM.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarAntecedentesM() ERROR: " + e);
      }
   }

   @Override
   public List<SoTiposAntecedentes> consultarTiposAntecedentes() {
      try {
         return persistenciaTiposAntecedentes.listaTiposAntecedentes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposAntecedentes() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SoAntecedentes> consultarAntecedentes(BigInteger secTipoAntecedente) {
      try {
         return persistenciaAntecedentes.lovAntecedentes(getEm(), secTipoAntecedente);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarAntecedentes() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SoAntecedentesMedicos> consultarAntecedentesMedicos(BigInteger secPersona) {
      try {
         return PersistenciaAntecedentesM.listaAntecedentesMedicos(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarAntecedentesMedicos() ERROR: " + e);
         return null;
      }
   }
}
