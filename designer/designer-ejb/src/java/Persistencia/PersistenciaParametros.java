/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.CambiosMasivos;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.ParametrosCambiosMasivosAux;
import InterfacePersistencia.PersistenciaParametrosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Parametros' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaParametros implements PersistenciaParametrosInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametros.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, Parametros parametro) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(parametro);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaParametros.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Parametros parametro) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(parametro));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaMotivosCambiosSueldos.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Parametros> parametrosComprobantes(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT p FROM Parametros p WHERE EXISTS (SELECT pi FROM ParametrosInstancias pi, UsuariosInstancias ui WHERE pi.instancia.secuencia = ui.instancia.secuencia AND ui.usuario.alias = :usuarioBD AND pi.parametro.secuencia = p.secuencia) ORDER BY p.empleado.codigoempleado");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Parametros> listaParametros = query.getResultList();
         return listaParametros;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaParametros.parametrosComprobantes" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Parametros> empleadosParametros(EntityManager em, String usuarioBD) {
      try {
         log.warn("Persistencia.PersistenciaParametros.empleadosParametros() usuarioBD: " + usuarioBD + ", em: " + em);
         em.clear();
         Query query = em.createQuery("SELECT p FROM Parametros p WHERE p.empleado IS NOT NULL AND p.usuario.alias = :usuarioBD", Parametros.class);
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         log.warn("query: " + query);
         List<Parametros> listaParametros = query.getResultList();
         return listaParametros;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaParametros.empleadosParametros" + e.getMessage());
         return null;
      }
   }

   @Override
   public void borrarParametros(EntityManager em, BigInteger secParametrosEstructuras) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createQuery("DELETE FROM Parametros p WHERE p.parametroestructura.secuencia = :secParametrosEstructuras");
         query.setParameter("secParametrosEstructuras", secParametrosEstructuras);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaParametros.borrarParametros. " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   // Para Cambios Masivos : 
   @Override
   public void crearCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(cambioMasivo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCambiosMasivos.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(cambioMasivo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCambiosMasivos.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(cambioMasivo));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaCambiosMasivos.borrar: " + e.getMessage());
      }
   }

   @Override
   public CambiosMasivos buscarCambioMasivoSecuencia(EntityManager em, BigInteger secuencia) {
      em.clear();
      try {
         return em.find(CambiosMasivos.class, secuencia);
      } catch (Exception e) {
         log.error("Error PersistenciaCambiosMasivos.buscarCambioMasivoSecuencia(): " + e.getMessage());
         return null;
      }
   }

   /**
    *
    * @param em
    * @return
    */
   @Override
   public List<CambiosMasivos> consultarCambiosMasivos(EntityManager em) {
//         em.clear();
//      try {
//         String q = "SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
//                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO)";
//         log.warn("q : " + q);
//         Query query = em.createNativeQuery(q, CambiosMasivos.class);
//         List<CambiosMasivos> lista = query.getResultList();
//         return lista;
//      } catch (Exception e) {
//         log.error("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e);
      return null;
//      }
   }

   /**
    *
    * @param em
    * @param usuario
    * @return
    */
   public ParametrosCambiosMasivos consultarParametroCambiosMasivos(EntityManager em, String usuario) {
      log.warn("Persistencia.PersistenciaParametros.consultarParametroCambiosMasivos()");
//         em.clear();
//      try {
//         String q = "SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + usuario + "'";
//         Query query = em.createNativeQuery(q);
//         log.warn("q : " + q);
//         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
//         return parametro;
//      } catch (Exception e) {
//         log.error("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e);
      return null;
//      }
   }

   @Override
   public List<CambiosMasivos> listcambiosmasivos(EntityManager em) {
      em.clear();
      try {
         String q = "SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO) \n"
                 + "ORDER BY ULTIMAMODIFICACION DESC";
         log.warn("q : " + q);
         Query query = em.createNativeQuery(q, CambiosMasivos.class);
         List<CambiosMasivos> lista = query.getResultList();
         if (lista != null) {
            if (!lista.isEmpty()) {
               for (int i = 0; i < lista.size(); i++) {
                  em.clear();
                  try {
                     String q2 = "SELECT P.NOMBRE ||' '|| P.PRIMERAPELLIDO ||' '|| P.SEGUNDOAPELLIDO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
                     if (i == 0) {
                        log.warn("q2 : " + q2);
                     }
                     Query query2 = em.createNativeQuery(q2);
                     String nom = (String) query2.getSingleResult();
                     lista.get(i).setNombreEmpleado(nom);
                  } catch (Exception e2) {
                     log.error("Error consultando Transients : " + e2);
                  }
                  em.clear();
                  try {
                     String q1 = "SELECT E.CODIGOEMPLEADO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
                     if (i == 0) {
                        log.warn("q1 : " + q1);
                     }
                     Query query1 = em.createNativeQuery(q1);
                     BigDecimal cod = (BigDecimal) query1.getSingleResult();
                     lista.get(i).setCodigoEmpleado(cod);
                  } catch (Exception e3) {
                     log.error("Error consultando Transients : " + e3);
                  }
               }
            }
         }
         return lista;
      } catch (Exception e) {
         log.error("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e.getMessage());
         return null;
      }
   }

   @Override
   public ParametrosCambiosMasivos parametrosCambiosMasivos(EntityManager em, String user) {
      log.warn("Persistencia.PersistenciaParametros.parametrosCambiosMasivos()");
      em.clear();
      try {
         String q = "SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + user + "' AND ROWNUM < 2";
         Query query = em.createNativeQuery(q, ParametrosCambiosMasivos.class);
         log.warn("q : " + q);
         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
         if (parametro != null) {
            if (parametro.getSecuencia() != null) {
               try {
                  BigInteger n = new BigInteger("0");

                  if (parametro.getAfiliaTerceroSucursal() == null) {
                     parametro.setAfiliaTerceroSucursal(n);
                  }
                  if (parametro.getAfiliaTipoEntidad() == null) {
                     parametro.setAfiliaTipoEntidad(n);
                  }
                  if (parametro.getCargoEstructura() == null) {
                     parametro.setCargoEstructura(n);
                  }
                  if (parametro.getLocaliEstructura() == null) {
                     parametro.setLocaliEstructura(n);
                  }
                  if (parametro.getNoveConcepto() == null) {
                     parametro.setNoveConcepto(n);
                  }
                  if (parametro.getNoveFormula() == null) {
                     parametro.setNoveFormula(n);
                  }
                  if (parametro.getNovePeriodicidad() == null) {
                     parametro.setNovePeriodicidad(n);
                  }
                  if (parametro.getNoveTercero() == null) {
                     parametro.setNoveTercero(n);
                  }
                  if (parametro.getRetiMotivoDefinitiva() == null) {
                     parametro.setRetiMotivoDefinitiva(n);
                  }
                  if (parametro.getRetiMotivoRetiro() == null) {
                     parametro.setRetiMotivoRetiro(n);
                  }
                  if (parametro.getSueldoMotivoCambioSueldo() == null) {
                     parametro.setSueldoMotivoCambioSueldo(n);
                  }
                  if (parametro.getSueldoTipoSueldo() == null) {
                     parametro.setSueldoTipoSueldo(n);
                  }
                  if (parametro.getSueldoUnidadPago() == null) {
                     parametro.setSueldoUnidadPago(n);
                  }
                  String q2 = "SELECT PM.SECUENCIA, \n"
                          + "(SELECT TE.NOMBRE FROM TIPOSENTIDADES TE WHERE TE.SECUENCIA = " + parametro.getAfiliaTipoEntidad() + ") AFILIATIPOENTIDAD, \n"
                          + "(SELECT TS.DESCRIPCION FROM TERCEROSSUCURSALES TS WHERE TS.SECUENCIA = " + parametro.getAfiliaTerceroSucursal() + ") AFILIATERCEROSUCURSAL, \n"
                          + "(SELECT E.NOMBRE FROM ESTRUCTURAS E WHERE E.SECUENCIA =" + parametro.getCargoEstructura() + ") CARGOESTRUCTURA, \n"
                          + "(SELECT MD.NOMBRE FROM MOTIVOSDEFINITIVAS MD WHERE MD.SECUENCIA =" + parametro.getRetiMotivoDefinitiva() + ") MOTIVODEFINITIVA, \n"
                          + "(SELECT C.DESCRIPCION FROM CONCEPTOS C WHERE C.SECUENCIA = " + parametro.getNoveConcepto() + ") NOVEDADCONCEPTO, \n"
                          + "(SELECT P.NOMBRE FROM PERIODICIDADES P WHERE P.SECUENCIA = " + parametro.getNovePeriodicidad() + ") NOVEDADPERIODICIDAD, \n"
                          + "(SELECT T.NOMBRE FROM TERCEROS T WHERE T.SECUENCIA = " + parametro.getNoveTercero() + ") NOVEDADTERCERO, \n"
                          + "(SELECT F.NOMBRELARGO FROM FORMULAS F WHERE F.SECUENCIA = " + parametro.getNoveFormula() + ") NOVEDADFORMULA, \n"
                          + "(SELECT MCS.NOMBRE FROM MOTIVOSCAMBIOSSUELDOS MCS WHERE MCS.SECUENCIA = " + parametro.getSueldoMotivoCambioSueldo() + ") MOTIVOCAMBIOSUELDO, \n"
                          + "(SELECT TSU.DESCRIPCION FROM TIPOSSUELDOS TSU WHERE TSU.SECUENCIA = " + parametro.getSueldoTipoSueldo() + ") TIPOSUELDO, \n"
                          + "(SELECT UN.NOMBRE FROM UNIDADES UN WHERE UN.SECUENCIA = " + parametro.getSueldoUnidadPago() + ") SUELDOUNIDADPAGO, \n"
                          + "(SELECT MR.NOMBRE FROM MOTIVOSRETIROS MR WHERE MR.SECUENCIA = " + parametro.getRetiMotivoRetiro() + ") MOTIVORETIRO, \n"
                          + "(SELECT EXT.NOMBRE FROM ESTRUCTURAS EXT WHERE SECUENCIA = " + parametro.getLocaliEstructura() + ") LOCALIESTRUCTURA \n"
                          + "FROM PARAMETROSCAMBIOSMASIVOS PM \n"
                          + "WHERE PM.USUARIOBD = user";
                  Query query2 = em.createNativeQuery(q2, ParametrosCambiosMasivosAux.class);
                  ParametrosCambiosMasivosAux objAux = (ParametrosCambiosMasivosAux) query2.getSingleResult();
                  if (objAux != null) {
                     if (objAux.getSecuencia() != null) {
                        parametro.setStr_afiliaTerceroSucursal(objAux.getStr_afiliaTerceroSucursal());
                        parametro.setStr_afiliaTipoEntidad(objAux.getStr_afiliaTipoEntidad());
                        parametro.setStr_cargoEstructura(objAux.getStr_cargoEstructura());
                        parametro.setStr_localiEstructura(objAux.getStr_localiEstructura());
                        parametro.setStr_noveConcepto(objAux.getStr_noveConcepto());
                        parametro.setStr_noveFormula(objAux.getStr_noveFormula());
                        parametro.setStr_novePeriodicidad(objAux.getStr_novePeriodicidad());
                        parametro.setStr_noveTercero(objAux.getStr_noveTercero());
                        parametro.setStr_retiMotivoDefinitiva(objAux.getStr_retiMotivoDefinitiva());
                        parametro.setStr_retiMotivoRetiro(objAux.getStr_retiMotivoRetiro());
                        parametro.setStr_sueldoMotivoCambioSueldo(objAux.getStr_sueldoMotivoCambioSueldo());
                        parametro.setStr_sueldoTipoSueldo(objAux.getStr_sueldoTipoSueldo());
                        parametro.setStr_sueldoUnidadPago(objAux.getStr_sueldoUnidadPago());
                     }
                  }

                  if (parametro.getAfiliaTerceroSucursal() == n) {
                     parametro.setAfiliaTerceroSucursal(null);
                  }
                  if (parametro.getAfiliaTipoEntidad() == n) {
                     parametro.setAfiliaTipoEntidad(null);
                  }
                  if (parametro.getCargoEstructura() == n) {
                     parametro.setCargoEstructura(null);
                  }
                  if (parametro.getLocaliEstructura() == n) {
                     parametro.setLocaliEstructura(null);
                  }
                  if (parametro.getNoveConcepto() == n) {
                     parametro.setNoveConcepto(null);
                  }
                  if (parametro.getNoveFormula() == n) {
                     parametro.setNoveFormula(null);
                  }
                  if (parametro.getNovePeriodicidad() == n) {
                     parametro.setNovePeriodicidad(null);
                  }
                  if (parametro.getNoveTercero() == n) {
                     parametro.setNoveTercero(null);
                  }
                  if (parametro.getRetiMotivoDefinitiva() == n) {
                     parametro.setRetiMotivoDefinitiva(null);
                  }
                  if (parametro.getRetiMotivoRetiro() == n) {
                     parametro.setRetiMotivoRetiro(null);
                  }
                  if (parametro.getSueldoMotivoCambioSueldo() == n) {
                     parametro.setSueldoMotivoCambioSueldo(null);
                  }
                  if (parametro.getSueldoTipoSueldo() == n) {
                     parametro.setSueldoTipoSueldo(null);
                  }
                  if (parametro.getSueldoUnidadPago() == n) {
                     parametro.setSueldoUnidadPago(null);
                  }
               } catch (Exception e2) {
                  log.error("ERROR al consultar @transients : " + e2);
               }
            }
         }
         return parametro;
      } catch (Exception e) {
         log.error("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e.getMessage());
         return null;
      }
   }

   /**
    *
    * @param em
    * @param parametro
    * @return
    */
   public boolean actualizarParametroCambioMasivo(EntityManager em, ParametrosCambiosMasivos parametro) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         log.warn("actualizarParametroCambioMasivo antes del MERGE 222");
         log.warn("parametro.getSecuencia(): " + parametro.getSecuencia());
         log.warn("parametro.getUsuarioBD(): " + parametro.getUsuarioBD());
         log.warn("\nparametro.getAfiliaTerceroSucursal(): " + parametro.getAfiliaTerceroSucursal());
         log.warn("parametro.getAfiliaTipoEntidad(): " + parametro.getAfiliaTipoEntidad());
         log.warn("parametro.getCargoEstructura(): " + parametro.getCargoEstructura());
         log.warn("parametro.getLocaliEstructura(): " + parametro.getLocaliEstructura());
         log.warn("parametro.getNoveConcepto(): " + parametro.getNoveConcepto());
         log.warn("parametro.getNoveFormula(): " + parametro.getNoveFormula());
         log.warn("parametro.getNovePeriodicidad(): " + parametro.getNovePeriodicidad());
         log.warn("parametro.getNoveTercero(): " + parametro.getNoveTercero());
         log.warn("parametro.getRetiMotivoDefinitiva(): " + parametro.getRetiMotivoDefinitiva());
         log.warn("parametro.getRetiMotivoRetiro(): " + parametro.getRetiMotivoRetiro());
         log.warn("parametro.getSueldoMotivoCambioSueldo(): " + parametro.getSueldoMotivoCambioSueldo());
         log.warn("parametro.getSueldoTipoSueldo(): " + parametro.getSueldoTipoSueldo());
         log.warn("parametro.getSueldoUnidadPago(): " + parametro.getSueldoUnidadPago());
         em.merge(parametro);
         tx.commit();
         return true;
      } catch (Exception e) {
         log.error("ERROR PersistenciaParametros.actualizarParametroCambioMasivo: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }
}
