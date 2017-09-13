/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.VWActualesTiposTrabajadores;
import Entidades.VWActualesTiposTrabajadoresAux;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesTiposTrabajadores implements PersistenciaVWActualesTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesTiposTrabajadores.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
    */
   @Override
   public VWActualesTiposTrabajadores buscarTipoTrabajador(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.empleado = :empleado");
         query.setParameter("empleado", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores vwActualTT = (VWActualesTiposTrabajadores) query.getSingleResult();
         if (vwActualTT != null) {
            if (vwActualTT.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO,"
                       + " p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                       + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em\n"
                       + " WHERE e.persona = p.secuencia and em.secuencia = e.empresa and e.secuencia = v.empleado and v.secuencia = " + vwActualTT.getSecuencia(), VWActualesTiposTrabajadoresAux.class);
               VWActualesTiposTrabajadoresAux vwActualTTAux = (VWActualesTiposTrabajadoresAux) query2.getSingleResult();
               log.warn(this.getClass().getSimpleName() + ".buscarTipoTrabajador() vwActualTTAux: " + vwActualTTAux);
               if (vwActualTTAux != null) {
                  if (vwActualTTAux.getSecuencia() != null) {
                     if (vwActualTTAux.getSecuencia().equals(vwActualTT.getSecuencia())) {
                        vwActualTT.llenarTransients(vwActualTTAux);
                     }
                  }
               }
            }
         }
         return vwActualTT;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador()" + e.getMessage());
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores buscarTipoTrabajadorCodigoEmpl(EntityManager em, BigInteger codigo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.codigoEmpleado = :empleado");
         query.setParameter("empleado", codigo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores vwActualTT = (VWActualesTiposTrabajadores) query.getSingleResult();
         if (vwActualTT != null) {
            if (vwActualTT.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO,"
                       + " p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                       + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em\n"
                       + " WHERE e.persona = p.secuencia and em.secuencia = e.empresa and e.secuencia = v.empleado and v.secuencia = " + vwActualTT.getSecuencia(), VWActualesTiposTrabajadoresAux.class);
               VWActualesTiposTrabajadoresAux vwActualTTAux = (VWActualesTiposTrabajadoresAux) query2.getSingleResult();
               log.warn(this.getClass().getSimpleName() + ".buscarTipoTrabajadorCodigoEmpl() vwActualTTAux: " + vwActualTTAux);
               if (vwActualTTAux != null) {
                  if (vwActualTTAux.getSecuencia() != null) {
                     if (vwActualTTAux.getSecuencia().equals(vwActualTT.getSecuencia())) {
                        vwActualTT.llenarTransients(vwActualTTAux);
                     }
                  }
               }
            }
         }
         return vwActualTT;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaVWActualesTiposTrabajadores.buscarTipoTrabajadorCodigoEmpl() ERROR : " + e);
         return null;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> FiltrarTipoTrabajador(EntityManager em, String p_tipo) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            List<VWActualesTiposTrabajadores> vwAtualesTT = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador")
                    .setParameter("tipotrabajador", p_tipo)
                    .getResultList();
            if (vwAtualesTT != null) {
               if (!vwAtualesTT.isEmpty()) {
                  em.clear();
                  Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO, p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                          + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em, VWActualesTiposTrabajadores vwatt, TIPOSTRABAJADORES TT\n"
                          + " WHERE TT.SECUENCIA = vwatt.TIPOTRABAJADOR AND vwatt.EMPLEADO = E.SECUENCIA AND e.persona = p.secuencia\n"
                          + " and em.secuencia = e.empresa and e.secuencia = v.empleado AND TT.TIPO = '" + p_tipo + "'", VWActualesTiposTrabajadoresAux.class);
                  List<VWActualesTiposTrabajadoresAux> listaActualesTTAux = query2.getResultList();
                  log.warn(this.getClass().getSimpleName() + ".FiltrarTipoTrabajador() Ya consulo Transients");
                  if (listaActualesTTAux != null) {
                     if (!listaActualesTTAux.isEmpty()) {
                        log.warn(this.getClass().getSimpleName() + ".FiltrarTipoTrabajador() listaActualesTTAux.size(): " + listaActualesTTAux.size());
                        for (VWActualesTiposTrabajadoresAux recAux : listaActualesTTAux) {
                           for (VWActualesTiposTrabajadores recActualTT : vwAtualesTT) {
                              if (recAux.getSecuencia().equals(recActualTT.getSecuencia())) {
                                 recActualTT.llenarTransients(recAux);
                              }
                           }
                        }
                     }
                  }
               }
            }
            return vwAtualesTT;
         } else {
            log.warn("Error en PersistenciaVWActualesTiposTrabajadores.FiltrarTipoTrabajador. No recibió el parametro");
            return null;
         }
      } catch (Exception e) {
         log.error("PersistenciaVWActualesTiposTrabajadores.FiltrarTipoTrabajador() ERROR : " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores filtrarTipoTrabajadorPosicion(EntityManager em, String p_tipo, int posicion) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt, Empleados e"
                    + " WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador AND vwatt.empleado = e.secuencia");
            query.setParameter("tipotrabajador", p_tipo);
            query.setFirstResult(posicion);
            query.setMaxResults(1);
            VWActualesTiposTrabajadores vwActualTT = (VWActualesTiposTrabajadores) query.getSingleResult();
            if (vwActualTT != null) {
               if (vwActualTT.getSecuencia() != null) {
                  em.clear();
                  Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO,"
                          + " p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                          + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em\n"
                          + " WHERE e.persona = p.secuencia and em.secuencia = e.empresa and e.secuencia = v.empleado and v.secuencia = " + vwActualTT.getSecuencia(), VWActualesTiposTrabajadoresAux.class);
                  VWActualesTiposTrabajadoresAux vwActualTTAux = (VWActualesTiposTrabajadoresAux) query2.getSingleResult();
                  log.warn(this.getClass().getSimpleName() + ".filtrarTipoTrabajadorPosicion() vwActualTTAux: " + vwActualTTAux);
                  if (vwActualTTAux != null) {
                     if (vwActualTTAux.getSecuencia() != null) {
                        if (vwActualTTAux.getSecuencia().equals(vwActualTT.getSecuencia())) {
                           vwActualTT.llenarTransients(vwActualTTAux);
                        }
                     }
                  }
               }
            }
            return vwActualTT;
         } else {
            log.warn("Error en PersistenciaVWActualesTiposTrabajadores.filtrarTipoTrabajadorPosicion. " + "No recibió el parametro");
            return null;
         }
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public int obtenerTotalRegistrosTipoTrabajador(EntityManager em, String p_tipo) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            Query query = em.createQuery("SELECT COUNT(vwatt) FROM VWActualesTiposTrabajadores vwatt, Empleados e"
                    + " WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador AND vwatt.empleado = e.secuencia");
            query.setParameter("tipotrabajador", p_tipo);
            Long totalRegistros = (Long) query.getSingleResult();
            log.warn("Valor total Registros: " + totalRegistros);
//            log.warn("Tipo: " + p_tipo);
            return totalRegistros.intValue();
         } else {
            log.warn("Error en PersistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador. " + "No recibió el parametro");
            return 0;
         }
      } catch (Exception e) {
         log.error("PersistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador() ERROR: " + e);
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> busquedaRapidaTrabajadores(EntityManager em) {
      try {
         em.clear();
         List<VWActualesTiposTrabajadores> vwAtualesTT = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt").getResultList();
         if (vwAtualesTT != null) {
            if (!vwAtualesTT.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO,"
                       + " p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                       + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em\n"
                       + " WHERE e.persona = p.secuencia and em.secuencia = e.empresa and e.secuencia = v.empleado", VWActualesTiposTrabajadoresAux.class);
               List<VWActualesTiposTrabajadoresAux> listaActualesTTAux = query2.getResultList();
               log.warn(this.getClass().getSimpleName() + ".busquedaRapidaTrabajadores() Ya consulo Transients");
               if (listaActualesTTAux != null) {
                  if (!listaActualesTTAux.isEmpty()) {
                     log.warn(this.getClass().getSimpleName() + ".busquedaRapidaTrabajadores() listaActualesTTAux.size(): " + listaActualesTTAux.size());
                     for (VWActualesTiposTrabajadoresAux recAux : listaActualesTTAux) {
                        for (VWActualesTiposTrabajadores recActualTT : vwAtualesTT) {
                           if (recAux.getSecuencia().equals(recActualTT.getSecuencia())) {
                              recActualTT.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return vwAtualesTT;
      } catch (Exception e) {
         log.error("PersistenciaVWActualesTiposTrabajadores.busquedaRapidaTrabajadores() ERROR: " + e);
         List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
         return vwActualesTiposTrabajadores;
      }
   }

   //VALIDACION ARCHIVO PLANO
   @Override
   public boolean verificarTipoTrabajador(EntityManager em, Empleados empleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado = :secuencia");
         query.setParameter("secuencia", empleado.getSecuencia());
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         String tipoEmpleado = (String) query.getSingleResult();
         return tipoEmpleado.equalsIgnoreCase("ACTIVO");
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.verificarTipoTrabajador");
         return false;
      }
   }

   @Override
   public String consultarTipoTrabajador(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado = :secuencia");
         query.setParameter("secuencia", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         String tipoEmpleado = (String) query.getSingleResult();
         return tipoEmpleado;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.consultarTipoTrabajador");
         return "";
      }
   }

   @Override
   public Date consultarFechaVigencia(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesTiposTrabajadores vw WHERE vw.empleado = :secuencia");
         query.setParameter("secuencia", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores objfecha = (VWActualesTiposTrabajadores) query.getSingleResult();
         log.warn("PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia objfecha : " + objfecha);
         Date fecha = objfecha.getFechaVigencia();
         log.warn("PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia fecha : " + fecha);
         return fecha;
      } catch (Exception e) {
         log.error("ERROR: PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia : " + e.getCause());
         return null;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> tipoTrabajadorEmpleado(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesTiposTrabajadores vw where vw.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VWActualesTiposTrabajadores> vwAtualesTT = query.getResultList();
         if (vwAtualesTT != null) {
            if (!vwAtualesTT.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT v.SECUENCIA, RETENCIONYSEGSOCXPERSONA, p.NUMERODOCUMENTO, e.CODIGOEMPLEADO, p.secuencia PERSONA, p.nombre NOMBREPERSONA, p.PRIMERAPELLIDO, p.SEGUNDOAPELLIDO\n"
                       + " FROM VWActualesTiposTrabajadores v, empleados e, personas p, empresas em, VWActualesTiposTrabajadores vwatt, TIPOSTRABAJADORES TT\n"
                       + " WHERE TT.SECUENCIA = vwatt.TIPOTRABAJADOR AND vwatt.EMPLEADO = E.SECUENCIA AND e.persona = p.secuencia\n"
                       + " and em.secuencia = e.empresa and e.secuencia = v.empleado AND TT.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')", VWActualesTiposTrabajadoresAux.class);
               List<VWActualesTiposTrabajadoresAux> listaActualesTTAux = query2.getResultList();
               log.warn(this.getClass().getSimpleName() + ".tipoTrabajadorEmpleado() Ya consulo Transients");
               if (listaActualesTTAux != null) {
                  if (!listaActualesTTAux.isEmpty()) {
                     log.warn(this.getClass().getSimpleName() + ".tipoTrabajadorEmpleado() listaActualesTTAux.size(): " + listaActualesTTAux.size());
                     for (VWActualesTiposTrabajadoresAux recAux : listaActualesTTAux) {
                        for (VWActualesTiposTrabajadores recActualTT : vwAtualesTT) {
                           if (recAux.getSecuencia().equals(recActualTT.getSecuencia())) {
                              recActualTT.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return vwAtualesTT;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado" + e);
         return null;
      }
   }
}
