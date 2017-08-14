package Administrar;

import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosLocalizaciones;
import Entidades.Proyectos;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasProrrateos;
import Entidades.VigenciasProrrateosProyectos;
import InterfaceAdministrar.AdministrarVigenciaLocalizacionInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
import InterfacePersistencia.PersistenciaProyectosInterface;
import InterfacePersistencia.PersistenciaVigenciasLocalizacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasProrrateosInterface;
import InterfacePersistencia.PersistenciaVigenciasProrrateosProyectosInterface;
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
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciaLocalizacion implements AdministrarVigenciaLocalizacionInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciaLocalizacion.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaMotivosLocalizacionesInterface persistenciaMotivosLocalizaciones;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaVigenciasLocalizacionesInterface persistenciaVigenciasLocalizaciones;
   @EJB
   PersistenciaProyectosInterface persistenciaProyectos;
   @EJB
   PersistenciaVigenciasProrrateosInterface persistenciaVigenciasProrrateos;
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   @EJB
   PersistenciaVigenciasProrrateosProyectosInterface persistenciaVigenciasProrrateosProyectos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   //Vigencias Prorrateos
//   List<VigenciasProrrateos> vigenciasProrrateos;
//   VigenciasProrrateos vigenciaProrrateo;
//   //Vigencias Localizaciones
//   List<VigenciasLocalizaciones> vigenciasLocalizaciones;
//   VigenciasLocalizaciones vigenciaLocalizacion;
//   //Vigencias Prorrateos Proyectos
//   List<VigenciasProrrateosProyectos> vigenciasProrrateosProyectos;
//   VigenciasProrrateosProyectos vigenciaProrrateoProyecto;
//   //Empleado
//   Empleados empleado;
//   //Listas Adicionales
//   List<MotivosLocalizaciones> motivosLocalizaciones;
//   List<Estructuras> estructuras;
//   List<Proyectos> proyectos;
//   List<CentrosCostos> centrosCostos;
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
   public List<VigenciasLocalizaciones> VigenciasLocalizacionesEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasLocalizaciones.buscarVigenciasLocalizacionesEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Localizaciones (VigenciasLocalizacionesEmpleado) : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVL(List<VigenciasLocalizaciones> listVLModificadas) {
      try {
         for (int i = 0; i < listVLModificadas.size(); i++) {
            if (listVLModificadas.get(i).getProyecto().getSecuencia() == null) {
               listVLModificadas.get(i).setProyecto(null);
            }
            if (listVLModificadas.get(i).getLocalizacion().getSecuencia() == null) {
               listVLModificadas.get(i).setLocalizacion(null);
            }
            if (listVLModificadas.get(i).getMotivo().getSecuencia() == null) {
               listVLModificadas.get(i).setLocalizacion(null);
            }
            persistenciaVigenciasLocalizaciones.editar(getEm(), listVLModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVL AdmiVigLoc : " + e.toString());
      }
   }

   @Override
   public void borrarVL(VigenciasLocalizaciones vigenciasLocalizaciones) {
      try {
         persistenciaVigenciasLocalizaciones.borrar(getEm(), vigenciasLocalizaciones);
      } catch (Exception e) {
         log.warn("Error borrarVL AdmiVigLoc : " + e.toString());
      }
   }

   @Override
   public void crearVL(VigenciasLocalizaciones vigenciasLocalizaciones) {
      try {
         persistenciaVigenciasLocalizaciones.crear(getEm(), vigenciasLocalizaciones);
      } catch (Exception e) {
         log.warn("Error crearVL AdmiVigLoc : " + e.toString());
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarEmpleado Adm: " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosLocalizaciones> motivosLocalizaciones() {
      try {
         return persistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error motivosLocalizaciones AdministrarVigenciasdLocalizaciones: " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> estructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.warn("Error estructuras AdministrarVigenciasdLocalizaciones: " + e.toString());
         return null;
      }
   }

   @Override
   public List<Proyectos> proyectos() {
      try {
         return persistenciaProyectos.proyectos(getEm());
      } catch (Exception e) {
         log.warn("Error proyectos AdministrarVigenciasdLocalizaciones: " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasProrrateos> VigenciasProrrateosVigencia(BigInteger secVigencia) {
      try {
         return persistenciaVigenciasProrrateos.buscarVigenciasProrrateosVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Localizaciones (VigenciasProrrateosVigencia): " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVP(List<VigenciasProrrateos> listVPModificadas) {
      try {
         for (int i = 0; i < listVPModificadas.size(); i++) {
            if (listVPModificadas.get(i).getProyecto().getSecuencia() == null) {
               listVPModificadas.get(i).setProyecto(null);
            }
            if (listVPModificadas.get(i).getCentrocosto().getSecuencia() == null) {
               listVPModificadas.get(i).setCentrocosto(null);
            }
            if (listVPModificadas.get(i).getViglocalizacion().getProyecto().getSecuencia() == null) {
               listVPModificadas.get(i).getViglocalizacion().setProyecto(null);
            }
            persistenciaVigenciasProrrateos.editar(getEm(), listVPModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVP AdmiVigLoc: " + e.toString());
      }
   }

   @Override
   public void borrarVP(VigenciasProrrateos vigenciasProrrateos) {
      try {
         persistenciaVigenciasProrrateos.borrar(getEm(), vigenciasProrrateos);
      } catch (Exception e) {
         log.warn("Error borrarVP AdmiVigLoc: " + e.toString());
      }
   }

   @Override
   public void crearVP(VigenciasProrrateos vigenciasProrrateos) {
      try {
         persistenciaVigenciasProrrateos.crear(getEm(), vigenciasProrrateos);
      } catch (Exception e) {
         log.warn("Error crearVP AdmiVigLoc: " + e.toString());
      }
   }

   @Override
   public List<CentrosCostos> centrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("Error centrosCostos Admi: " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasProrrateosProyectos> VigenciasProrrateosProyectosVigencia(BigInteger secVigencia) {
      try {
         return persistenciaVigenciasProrrateosProyectos.buscarVigenciasProrrateosProyectosVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error VigenciasProrrateosProyectosVigencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVPP(List<VigenciasProrrateosProyectos> listVPPModificadas) {
      try {
         for (int i = 0; i < listVPPModificadas.size(); i++) {
            if (listVPPModificadas.get(i).getProyecto().getSecuencia() == null) {
               listVPPModificadas.get(i).setProyecto(null);
            }
            if (listVPPModificadas.get(i).getVigencialocalizacion().getProyecto().getSecuencia() == null) {
               listVPPModificadas.get(i).getVigencialocalizacion().setProyecto(null);
            }
            persistenciaVigenciasProrrateosProyectos.editar(getEm(), listVPPModificadas.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarVPP AdmiVigLoc: " + e.toString());
      }
   }

   @Override
   public void borrarVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
      try {
         persistenciaVigenciasProrrateosProyectos.borrar(getEm(), vigenciasProrrateosProyectos);
      } catch (Exception e) {
         log.warn("Error borrarVPP AdmiVigLoc: " + e.toString());
      }
   }

   @Override
   public void crearVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
      try {
         persistenciaVigenciasProrrateosProyectos.crear(getEm(), vigenciasProrrateosProyectos);
      } catch (Exception e) {
         log.warn("Error crearVPP AdmiVigLoc: " + e.toString());
      }
   }
}
