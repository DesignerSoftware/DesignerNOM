package Administrar;

import Entidades.ActualUsuario;
import Entidades.AportesEntidades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosAutoliq;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarParametroAutoliqInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAportesEntidadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaParametrosAutoliqInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInformesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarParametroAutoliq implements AdministrarParametroAutoliqInterface {

   private static Logger log = Logger.getLogger(AdministrarParametroAutoliq.class);

   @EJB
   PersistenciaParametrosAutoliqInterface persistenciaParametrosAutoliq;
   @EJB
   PersistenciaAportesEntidadesInterface persistenciaAportesEntidades;
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaParametrosInformesInterface persistenciaParametrosInformes;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;

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

   //@Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ParametrosAutoliq> consultarParametrosAutoliq() {
      try {
         return persistenciaParametrosAutoliq.consultarParametrosAutoliqPorEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error consultarParametrosAutoliq Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
      try {
         for (int i = 0; i < listaPA.size(); i++) {
            if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
               listaPA.get(i).setTipotrabajador(null);
            }
            if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
               listaPA.get(i).setEmpresa(null);
            }
            persistenciaParametrosAutoliq.crear(getEm(), listaPA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearParametrosAutoliq Admi : " + e.toString());
      }
   }

   @Override
   public void editarParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
      try {
         for (int i = 0; i < listaPA.size(); i++) {
            if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
               listaPA.get(i).setTipotrabajador(null);
            }
            if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
               listaPA.get(i).setEmpresa(null);
            }
            persistenciaParametrosAutoliq.editar(getEm(), listaPA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarParametrosAutoliq Admi : " + e.toString());
      }
   }

   @Override
   public void borrarParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
      try {
         for (int i = 0; i < listaPA.size(); i++) {
            if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
               listaPA.get(i).setTipotrabajador(null);
            }
            if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
               listaPA.get(i).setEmpresa(null);
            }
            persistenciaParametrosAutoliq.borrar(getEm(), listaPA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarParametrosAutoliq Admi : " + e.toString());
      }
   }

   @Override
   public List<AportesEntidades> consultarAportesEntidadesPorParametroAutoliq(BigInteger empresa, short mes, short ano) {
      try {
         return persistenciaAportesEntidades.consultarAportesEntidadesPorEmpresaMesYAnio(getEm(), empresa, mes, ano);
      } catch (Exception e) {
         log.warn("Error consultarAportesEntidadesPorParametroAutoliq Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void editarAportesEntidades(List<AportesEntidades> listAE) {
      try {
         for (int i = 0; i < listAE.size(); i++) {

            if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
               listAE.get(i).setTipoentidad(new TiposEntidades());
            } 
//            else if (listAE.get(i).getEmpleado() == null) {
//               log.warn("if 3 editarAportesEntidades");
//               listAE.get(i).setEmpleado(null);
//            }
            persistenciaAportesEntidades.editar(getEm(), listAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarAportesEntidades Admi : " + e.toString());
      }
   }

   @Override
   public void borrarAportesEntidades(List<AportesEntidades> listAE) {
      try {
         for (int i = 0; i < listAE.size(); i++) {

            if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
               listAE.get(i).setTipoentidad(null);
            }
//            else if (listAE.get(i).getEmpleado() == null) {
//               listAE.get(i).setEmpleado(null);
//            }
            persistenciaAportesEntidades.borrar(getEm(), listAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarAportesEntidades Admi : " + e.toString());
      }
   }

   @Override
   public void crearAportesEntidades(List<AportesEntidades> listaAE) {
      try {
         for (int i = 0; i < listaAE.size(); i++) {
            if (listaAE.get(i).getTipoentidad() != null) {
               if (listaAE.get(i).getTipoentidad().getSecuencia() == null) {
                  listaAE.get(i).setTipoentidad(new TiposEntidades());
               }
            }
//            if (listaAE.get(i).getEmpleado() != null) {
//               if (listaAE.get(i).getEmpleado() == null) {
//                  listaAE.get(i).setEmpleado(null);
//                  log.warn("if 3 crearAportesEntidades");
//               }
//            }

            persistenciaAportesEntidades.crear(getEm(), listaAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearAportesEntidades Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.consultarEmpleadosParametroAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEntidades> lovTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidadesParametroAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposEntidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTerceros.buscarTercerosParametrosAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovTerceros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String borrarAportesEntidadesProcesoAutomatico(BigInteger empresa, short mes, short ano) {
      try {
         return persistenciaAportesEntidades.borrarAportesEntidadesProcesoAutomatico(getEm(), empresa, mes, ano);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //@Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuario Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosInformes buscarParametroInforme(String usuario) {
      try {
         return persistenciaParametrosInformes.buscarParametroInformeUsuario(getEm(), usuario);
      } catch (Exception e) {
         log.warn("Error buscarParametroInforme Admi : " + e.toString());
         return null;
      }

   }

   @Override
   public void modificarParametroInforme(ParametrosInformes parametro) {
      try {
         persistenciaParametrosInformes.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroInforme Admi : " + e.toString());
      }
   }

   @Override
   public ParametrosEstructuras buscarParametroEstructura(String usuario) {
      try {
         ParametrosEstructuras parametro = persistenciaParametrosEstructuras.buscarParametro(getEm(), usuario);
         return parametro;
      } catch (Exception e) {
         log.warn("Error buscarParametroEstructura Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarParametroEstructura(ParametrosEstructuras parametro) {
      try {
         persistenciaParametrosEstructuras.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroEstructura Admi : " + e.toString());
      }
   }

   @Override
   public String ejecutarPKGActualizarNovedades(short ano, short mes, BigInteger secuencia) {
      try {
         String proceso = persistenciaAportesEntidades.ejecutarPKGActualizarNovedades(getEm(), secuencia, mes, ano);
         return proceso;
      } catch (Exception e) {
         log.warn("Error ejecutarPKGActualizarNovedades Admi : " + e.toString());
         return "ERROR_ADMINISTRAR";
      }
   }

   @Override
   public String ejecutarPKGInsertar(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaAportesEntidades.ejecutarPKGInsertar(getEm(), fechaIni, fechaFin, secTipoTrabajador, secuenciaEmpresa);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGActualizarNovedades Admi : " + e.toString());
         return "ERROR_ADMINISTRAR";
      }
   }

   @Override
   public String ejecutarPKGAcumularDiferencia(short ano, short mes, BigInteger secuencia) {
      try {
         return persistenciaAportesEntidades.ejecutarPKGActualizarNovedades(getEm(), secuencia, mes, ano);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<AportesEntidades> consultarAportesEntidadesPorEmpleado(BigInteger secEmpleado, short mes, short ano) {
      try {
         return persistenciaAportesEntidades.consultarAportesEntidadesPorEmpleado(getEm(), secEmpleado, mes, ano);
      } catch (Exception e) {
         log.warn("Error consultarAportesEntidadesPorParametroAutoliq Admi : " + e.toString());
         return null;
      }
   }

}
