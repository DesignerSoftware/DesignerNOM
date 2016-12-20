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
         System.out.println("Error PersistenciaParametros.crear: " + e);
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
         System.out.println("Error PersistenciaMotivosCambiosSueldos.borrar: " + e);
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
         System.out.println("PersistenciaParametros ListaParametros: " + listaParametros);
         return listaParametros;
      } catch (Exception e) {
         System.out.println("Exepcion en PersistenciaParametros.parametrosComprobantes" + e);
         return null;
      }
   }

   @Override
   public List<Parametros> empleadosParametros(EntityManager em, String usuarioBD) {
      try {
         System.out.println("Persistencia.PersistenciaParametros.empleadosParametros() usuarioBD: " + usuarioBD + ", em: " + em);
         em.clear();
         Query query = em.createQuery("SELECT p FROM Parametros p WHERE p.empleado IS NOT NULL AND p.usuario.alias = :usuarioBD", Parametros.class);
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         System.out.println("query: " + query);
         List<Parametros> listaParametros = query.getResultList();
         if (listaParametros != null) {
            System.out.println("listaParametros.size() : " + listaParametros.size());
         }
         return listaParametros;
      } catch (Exception e) {
         System.out.println("Exepcion en PersistenciaParametros.empleadosParametros" + e);
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
         System.out.println("PersistenciaParametros.borrarParametros. " + e);
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
         System.out.println("Error PersistenciaCambiosMasivos.crear: " + e);
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
         System.out.println("Error PersistenciaCambiosMasivos.editar: " + e);
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
         System.out.println("Error PersistenciaCambiosMasivos.borrar: " + e);
      }
   }

   @Override
   public CambiosMasivos buscarCambioMasivoSecuencia(EntityManager em, BigInteger secuencia) {
      em.clear();
      try {
         return em.find(CambiosMasivos.class, secuencia);
      } catch (Exception e) {
         System.out.println("Error PersistenciaCambiosMasivos.buscarCambioMasivoSecuencia(): " + e);
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
      System.out.println("Persistencia.PersistenciaParametros.consultarCambiosMasivos()");
//         em.clear();
//      try {
//         String q = "SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
//                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO)";
//         System.out.println("q : " + q);
//         Query query = em.createNativeQuery(q, CambiosMasivos.class);
//         List<CambiosMasivos> lista = query.getResultList();
//         return lista;
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e);
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
      System.out.println("Persistencia.PersistenciaParametros.consultarParametroCambiosMasivos()");
//         em.clear();
//      try {
//         String q = "SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + usuario + "'";
//         Query query = em.createNativeQuery(q);
//         System.out.println("q : " + q);
//         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
//         return parametro;
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e);
      return null;
//      }
   }

   @Override
   public List<CambiosMasivos> listcambiosmasivos(EntityManager em) {
      System.out.println("Persistencia.PersistenciaParametros.listcambiosmasivos()");
      em.clear();
      try {
         String q = "SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO)";
         System.out.println("q : " + q);
         Query query = em.createNativeQuery(q, CambiosMasivos.class);
         List<CambiosMasivos> lista = query.getResultList();
         if (lista != null) {
            if (!lista.isEmpty()) {
               for (int i = 0; i < lista.size(); i++) {
                  em.clear();
                  try {
//                     String q1 = "SELECT E.CODIGOEMPLEADO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
                     String q2 = "SELECT P.NOMBRE ||' '|| P.PRIMERAPELLIDO ||' '|| P.SEGUNDOAPELLIDO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
                     if (i == 0) {
//                        System.out.println("q1 : " + q1);
                        System.out.println("q2 : " + q2);
                     }
//                     Query query1 = em.createNativeQuery(q1, BigDecimal.class);
                     Query query2 = em.createNativeQuery(q2);
//                     BigDecimal cod = (BigDecimal) query1.getSingleResult();
                     String nom = (String) query2.getSingleResult();
//                     lista.get(i).setCodigoEmpleado(cod);
                     lista.get(i).setNombreEmpleado(nom);
                  } catch (Exception e2) {
                     System.out.println("Error consultando Transients : " + e2);
                  }
                  em.clear();
                  try {
                     String q1 = "SELECT E.CODIGOEMPLEADO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
//                     String q2 = "SELECT P.NOMBRE ||' '|| P.PRIMERAPELLIDO ||' '|| P.SEGUNDOAPELLIDO FROM EMPLEADOS E, PERSONAS P WHERE E.PERSONA = P.SECUENCIA AND E.SECUENCIA  = " + lista.get(i).getEmpleado();
                     if (i == 0) {
                        System.out.println("q1 : " + q1);
//                        System.out.println("q2 : " + q2);
                     }
                     Query query1 = em.createNativeQuery(q1);
//                     Query query2 = em.createNativeQuery(q2, String.class);
                     BigDecimal cod = (BigDecimal) query1.getSingleResult();
//                     String nom = (String) query2.getSingleResult();
                     lista.get(i).setCodigoEmpleado(cod);
//                     lista.get(i).setNombreEmpleado(nom);
                  } catch (Exception e3) {
                     System.out.println("Error consultando Transients : " + e3);
                  }
               }
            }
         }
         return lista;
      } catch (Exception e) {
         System.out.println("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e);
         return null;
      }
   }

   @Override
   public ParametrosCambiosMasivos parametrosCambiosMasivos(EntityManager em, String user) {
      System.out.println("Persistencia.PersistenciaParametros.parametrosCambiosMasivos()");
      em.clear();
      try {
         String q = "SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + user + "' AND ROWNUM < 2";
         Query query = em.createNativeQuery(q, ParametrosCambiosMasivos.class);
         System.out.println("q : " + q);
         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
         if (parametro != null) {
            if (parametro.getSecuencia() != null) {
               try {
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
                          + "WHERE usuariobd = 'NMCB'";
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
               } catch (Exception e2) {
                  System.out.println("ERROR al consultar @transients : " + e2);
               }
            }
         }
         return parametro;
      } catch (Exception e) {
         System.out.println("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e);
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
         em.merge(parametro);
         tx.commit();
         return true;
      } catch (Exception e) {
         System.out.println("ERROR PersistenciaParametros.actualizarParametroCambioMasivo: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }
}
