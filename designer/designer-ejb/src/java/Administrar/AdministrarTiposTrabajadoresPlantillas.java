/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresPlantillasInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaLLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaNLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaRLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaTCInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaTSInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresPlantillasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposTrabajadoresPlantillas implements AdministrarTiposTrabajadoresPlantillasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTrabajadoresPlantillas.class);

   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTipoTrabajador;
   @EJB
   PersistenciaTiposTrabajadoresPlantillasInterface persistenciaTTPlantillas;
   @EJB
   PersistenciaPlantillasValidaTCInterface persistenciaPlantillasTC;
   @EJB
   PersistenciaPlantillasValidaTSInterface persistenciaPlantillasTS;
   @EJB
   PersistenciaPlantillasValidaRLInterface persistenciaPlantillasRL;
   @EJB
   PersistenciaPlantillasValidaLLInterface persistenciaPlantillasLL;
   @EJB
   PersistenciaPlantillasValidaNLInterface persistenciaPlantillasNL;

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
   public void crearTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTipoTrabajador.crear(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTipoTrabajador.editar(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTipoTrabajador.borrar(getEm(), tiposTrabajadores);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPlantillaTC(PlantillasValidaTC plantilla) {
      try {
         persistenciaPlantillasTC.crear(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPlantillaTC(PlantillasValidaTC plantilla) {
      try {
         persistenciaPlantillasTC.editar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPlantillaTC(PlantillasValidaTC plantilla) {
      try {
         persistenciaPlantillasTC.borrar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPlantillaTS(PlantillasValidaTS plantilla) {
      try {
         persistenciaPlantillasTS.crear(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPlantillaTS(PlantillasValidaTS plantilla) {
      try {
         persistenciaPlantillasTS.editar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPlantillaTS(PlantillasValidaTS plantilla) {
      try {
         persistenciaPlantillasTS.borrar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPlantillaRL(PlantillasValidaRL plantilla) {
      try {
         persistenciaPlantillasRL.crear(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPlantillaRL(PlantillasValidaRL plantilla) {
      try {
         persistenciaPlantillasRL.editar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPlantillaRL(PlantillasValidaRL plantilla) {
      try {
         persistenciaPlantillasRL.borrar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPlantillaLL(PlantillasValidaLL plantilla) {
      try {
         persistenciaPlantillasLL.crear(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPlantillaLL(PlantillasValidaLL plantilla) {
      try {
         persistenciaPlantillasLL.editar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPlantillaLL(PlantillasValidaLL plantilla) {
      try {
         persistenciaPlantillasLL.borrar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPlantillaNL(PlantillasValidaNL plantilla) {
      try {
         persistenciaPlantillasNL.crear(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editarPlantillaNL(PlantillasValidaNL plantilla) {
      try {
         persistenciaPlantillasNL.editar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPlantillaNL(PlantillasValidaNL plantilla) {
      try {
         persistenciaPlantillasNL.borrar(getEm(), plantilla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposTrabajadores> listaTT() {
      try {
         return persistenciaTTPlantillas.consultarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PlantillasValidaTC> listaPlantillaTC(BigInteger secTT) {
      try {
         return persistenciaTTPlantillas.consultarPlanillaTC(getEm(), secTT);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PlantillasValidaTS> listaPlantillaTS(BigInteger secTT) {
      try {
         return persistenciaTTPlantillas.consultarPlanillaTS(getEm(), secTT);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PlantillasValidaRL> listaPlantillaRL(BigInteger secTT) {
      try {
         return persistenciaTTPlantillas.consultarPlanillaRL(getEm(), secTT);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PlantillasValidaLL> listaPlantillaLL(BigInteger secTT) {
      try {
         return persistenciaTTPlantillas.consultarPlanillaLL(getEm(), secTT);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PlantillasValidaNL> listaPlantillaNL(BigInteger secTT) {
      try {
         return persistenciaTTPlantillas.consultarPlanillaNL(getEm(), secTT);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

    @Override
    public boolean ConsultarRegistrosSecundarios(BigInteger secuencia) {
       return persistenciaTTPlantillas.consultarRegistrosSecundarios(getEm(), secuencia);
    }
}
