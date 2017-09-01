/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCursosInterface;
import Entidades.TiposCursos;
import InterfacePersistencia.PersistenciaTiposCursosInterface;
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
 * @author user
 */
@Stateful
public class AdministrarTiposCursos implements AdministrarTiposCursosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCursos.class);

   @EJB
   PersistenciaTiposCursosInterface persistenciaTiposCursos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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
   public void modificarTiposCursos(List<TiposCursos> listaTiposCursos) {
      try {
         for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.editar(getEm(), listaTiposCursos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposCursos(List<TiposCursos> listaTiposCursos) {
      try {
         for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.borrar(getEm(), listaTiposCursos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposCursos(List<TiposCursos> listaTiposCursos) {
      try {
         for (int i = 0; i < listaTiposCursos.size(); i++) {
            persistenciaTiposCursos.crear(getEm(), listaTiposCursos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposCursos> consultarTiposCursos() {
      try {
         return persistenciaTiposCursos.consultarTiposCursos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposCursos consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaTiposCursos.consultarTipoCurso(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarCursosTipoCurso(BigInteger secuenciaVigenciasIndicadores) {
      try {
         log.error("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
         return persistenciaTiposCursos.contarCursosTipoCurso(getEm(), secuenciaVigenciasIndicadores);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarTiposCursos contarCursosTipoCurso ERROR :" + e);
         return null;
      }
   }
}
