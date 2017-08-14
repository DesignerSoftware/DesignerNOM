package Administrar;

import Entidades.ClasesPensiones;
import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.Pensionados;
import Entidades.Personas;
import Entidades.Retirados;
import Entidades.TiposPensionados;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasTiposTrabajadores;
import InterfaceAdministrar.AdministrarVigenciasTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPensionadosInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
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
public class AdministrarVigenciasTiposTrabajadores implements AdministrarVigenciasTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasTiposTrabajadores.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaVigenciasTiposTrabajadoresInterface persistenciaVigenciasTiposTrabajadores;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
   @EJB
   PersistenciaRetiradosInterface persistenciaRetirados;
   @EJB
   PersistenciaPensionadosInterface persistenciaPensionados;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaTiposPensionadosInterface persistenciaTiposPensionados;
   @EJB
   PersistenciaClasesPensionesInterface persistenciaClasesPensiones;
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
   public List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadoresEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposTrabajadores.buscarVigenciasTiposTrabajadoresEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Tipos Trabajadores (vigenciasTiposTrabajadoresEmpleado) : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVTT(List<VigenciasTiposTrabajadores> listVTTModificadas) {
      try {
         for (int i = 0; i < listVTTModificadas.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasTiposTrabajadores.editar(getEm(), listVTTModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
      try {
         persistenciaVigenciasTiposTrabajadores.borrar(getEm(), vigenciasTiposTrabajadores);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
      try {
         persistenciaVigenciasTiposTrabajadores.crear(getEm(), vigenciasTiposTrabajadores);
      } catch (Exception e) {
         log.warn("Error crearVTT AdministrarVIgenciasTipoTrabajador : " + e.toString());
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> tiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error tiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

   public TiposTrabajadores tipoTrabajadorCodigo(short codTipoTrabajador) {
      try {
         return persistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort(getEm(), codTipoTrabajador);
      } catch (Exception e) {
         log.warn("Error tipoTrabajadorCodigo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearRetirado(Retirados retirado) {
      try {
         persistenciaRetirados.crear(getEm(), retirado);
      } catch (Exception e) {
         log.warn("Error crearRetirado Admi : " + e.toString());
      }
   }

   @Override
   public void editarRetirado(Retirados retirado) {
      try {
         persistenciaRetirados.editar(getEm(), retirado);
      } catch (Exception e) {
         log.warn("Error editarRetirado !!");
      }
   }

   @Override
   public void borrarRetirado(Retirados retirado) {
      try {
         persistenciaRetirados.borrar(getEm(), retirado);
      } catch (Exception e) {
         log.warn("Error borrarRetirado Admi : " + e.toString());
      }
   }

   @Override
   public List<Retirados> retiradosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaRetirados.buscarRetirosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error retiradosEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Retirados retiroPorSecuenciaVigencia(BigInteger secVigencia) {
      try {
         return persistenciaRetirados.buscarRetiroVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error retiroPorSecuenciaVigencia Admi : " + e.toString());
         return new Retirados();
      }
   }

   @Override
   public List<MotivosRetiros> motivosRetiros() {
      try {
         return persistenciaMotivosRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.warn("Error motivosRetiros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public MotivosRetiros motivoRetiroCodigo(BigInteger codMotivoRetiro) {
      try {
         return persistenciaMotivosRetiros.consultarMotivoRetiro(getEm(), codMotivoRetiro);
      } catch (Exception e) {
         log.warn("Error motivoRetiroCodigo Admi : " + e.toString());
         return null;
      }
   }

/////
   @Override
   public List<TiposPensionados> tiposPensionados() {
      try {
         return persistenciaTiposPensionados.consultarTiposPensionados(getEm());
      } catch (Exception e) {
         log.warn("Error tiposPensionados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ClasesPensiones> clasesPensiones() {
      try {
         return persistenciaClasesPensiones.consultarClasesPensiones(getEm());
      } catch (Exception e) {
         log.warn("Error clasesPensiones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ClasesPensiones clasePensionCodigo(BigInteger codClasePension) {
      try {
         return persistenciaClasesPensiones.consultarClasePension(getEm(), codClasePension);
      } catch (Exception e) {
         log.warn("Error AdministrarVigenciaTipoTrabajador clasePensionCodigo : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Personas> listaPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.warn("Error listaPersonas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Personas personaSecuencia(BigInteger secPersona) {
      try {
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("Error personaSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearPensionado(Pensionados pension) {
      try {
         persistenciaPensionados.crear(getEm(), pension);
      } catch (Exception e) {
         log.warn("Error crearPensionado AdministrarVigenciaTiposTrabajadores : " + e.toString());
      }
   }

   @Override
   public void editarPensionado(Pensionados pension) {
      try {
         persistenciaPensionados.editar(getEm(), pension);
      } catch (Exception e) {
         log.warn("Error editarPensionado Admi : " + e.toString());
      }
   }

   @Override
   public void borrarPensionado(Pensionados pension) {
      try {
         persistenciaPensionados.borrar(getEm(), pension);
      } catch (Exception e) {
         log.warn("Error borrarPensionado Admi : " + e.toString());
      }
   }

   @Override
   public List<Pensionados> pensionadoEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaPensionados.buscarPensionadosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error pensionadoEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Pensionados> listaPensionados() {
      try {
         return persistenciaPensionados.buscarPensionados(getEm());
      } catch (Exception e) {
         log.warn("Error listaPensionados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Pensionados pensionPorSecuenciaVigencia(BigInteger secVigencia) {
      try {
         return persistenciaPensionados.buscarPensionVigenciaSecuencia(getEm(), secVigencia);
      } catch (Exception e) {
         log.warn("Error pensionPorSecuenciaVigencia Admi : " + e.toString());
         return new Pensionados();
      }
   }

}
