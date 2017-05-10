package Persistencia;

import Entidades.InterconSapBO;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless

public class PersistenciaInterconSapBO implements PersistenciaInterconSapBOInterface {

    @Override
    public void crear(EntityManager em, InterconSapBO interconSapBO) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(interconSapBO);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, InterconSapBO interconSapBO) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(interconSapBO);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, InterconSapBO interconSapBO) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(interconSapBO));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public InterconSapBO buscarInterconSAPBOSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM InterconSapBO i WHERE i.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            InterconSapBO intercon = (InterconSapBO) query.getSingleResult();
            return intercon;
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.buscarInterconSAPBOSecuencia: " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconSapBO> buscarInterconSAPBOParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal) {
        try {
            System.out.println("Entre al metodo intercon");
            em.clear();
            String sql = "select * from INTERCON_SAPBO i where fechacontabilizacion between \n"
                    + " ? and ? and FLAG = 'CONTABILIZADO' AND SALIDA <> 'NETO'\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql, InterconSapBO.class);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            List<InterconSapBO> intercon = query.getResultList();
            if (intercon != null) {
                System.out.println("Lista InterconSapBO intercon : "+intercon.size());
            } else {
                System.out.println("Lista Nula InterconSapBO");
            }
            return intercon;
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.buscarInterconSAPBOParametroContable: " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaMaxInterconSAPBO(EntityManager em) {
        try {
            em.clear();
            String sql = "select max(i.fechacontabilizacion) from intercon_SAPBO i where flag = 'ENVIADO'";
            Query query = em.createNativeQuery(sql);
            Date fecha = (Date) (query.getSingleResult());
            return fecha;
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.obtenerFechaMaxInterconSAPBO: " + e.toString());
            return null;
        }
    }
    
    @Override
    public void actualizarFlagProcesoAnularInterfaseContableSAPBO(EntityManager em, Date fechaIni, Date fechaFin) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE intercon_SAPBO SET FLAG = 'CONTABILIZADO'\n"
                    + "		  WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "		  AND FLAG = 'ENVIADO'";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.actualizarFlagProcesoAnularInterfaseContableSAPBO : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    

    @Override
    public void actualizarFlagProcesoAnularInterfaseContableSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE intercon_SAPBO SET FLAG = 'CONTABILIZADO'\n"
                    + "		  WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "		  AND FLAG = 'ENVIADO'";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.actualizarFlagProcesoAnularInterfaseContableSAPBOV8 : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBOV8(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBOV8(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso); 
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBOV8 : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBO(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBO(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso); 
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBO : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBOVHP(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBOVHP(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso); 
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBOVHP : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBO_VCA(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBO_VCA(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBO_VCA : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBO_PE(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBO_PE(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBO_PE : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBOPQ(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Ubicarnuevointercon_SAPBO(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            query.setParameter(2, fechaIni);
            query.setParameter(3, fechaFin);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejeuctarPKGUbicarnuevointercon_SAPBOPQ : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.Recontabilizacion(?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGRecontabilizacion : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarDeleteInterconSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "DELETE INTERCON_SAPBO WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "		    AND FLAG='CONTABILIZADO'\n"
                    + "        AND nvl(INTERCON_SAPBO.PROCESO,0) = NVL(?,nvl(INTERCON_SAPBO.PROCESO,0))\n"
                    + "        and exists (select 'x' from empleados e where e.secuencia=intercon_SAPBO.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarDeleteInterconSAPBOV8 : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void cerrarProcesoLiquidacion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE INTERCON_SAPBO I SET I.FLAG='ENVIADO'\n"
                    + "     WHERE  \n"
                    + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                    + "     and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.cerrarProcesoLiquidacion : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPBO(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_plano(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPBO : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_planoV8(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, descripcionProceso);
            query.setParameter(5, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPV8 : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPVCA(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_plano_VCA(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, descripcionProceso);
            query.setParameter(5, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPVCA : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPPE(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_plano_PE(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, descripcionProceso);
            query.setParameter(5, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPPE : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPPQ(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_plano_PERMAQUIM(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, descripcionProceso);
            query.setParameter(5, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPPQ : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    @Override
    public void ejecutarPKGCrearArchivoPlanoSAPHP(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "call INTERFASESAPBO$PKG.archivo_planoVHP(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, proceso);
            query.setParameter(4, descripcionProceso);
            query.setParameter(5, nombreArchivo);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.ejecutarPKGCrearArchivoPlanoSAPHP : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public int contarProcesosContabilizadosInterconSAPBO(EntityManager em, Date fechaInicial, Date fechaFinal) {
        try {
            em.clear();
            String sql = "select COUNT(*) INTERCON_SAPBO from  i where\n"
                    + " i.fechacontabilizacion between ? and ? \n"
                    + " and i.flag = 'CONTABILIZADO'";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            BigDecimal contador = (BigDecimal) query.getSingleResult();
            if (contador != null) {
                return contador.intValue();
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.contarProcesosContabilizadosInterconSAPBO. " + e.toString());
            return -1;
        }
    }

    @Override
    public void actualizarFlagInterconSapBoProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE CONTABILIZACIONES SET FLAG='GENERADO' WHERE FLAG='CONTABILIZADO'\n"
                    + " AND FECHAGENERACION BETWEEN ? AND ? \n"
                    + " and exists (select 'x' from vwfempleados e, solucionesnodos sn where sn.empleado=e.secuencia and sn.secuencia=contabilizaciones.solucionnodo\n"
                    + " and sn.proceso=nvl(?,sn.proceso))";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.actualizarFlagInterconSapBoProcesoDeshacer. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sql = "UPDATE INTERCON_SAPBO I SET I.FLAG='ENVIADO' WHERE  \n"
                    + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                    + "     and i.empresa_codigo=?"
                    + "   and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, proceso);
            query.setParameter(4, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.cerrarProcesoContabilizacion. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void eliminarInterconSapBO(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "DELETE INTERCON_SAPBO \n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                    + " AND FLAG='CONTABILIZADO'\n"
                    + " and INTERCON_SAPBO.empresa_codigo=?\n"
                    + " AND nvl(INTERCON_SAPBO.PROCESO,0) = NVL(?,nvl(INTERCON_SAPBO.PROCESO,0))\n"
                    + " and exists (select 'x' from empleados e where e.secuencia=INTERCON_SAPBO.empleado)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.setParameter(4, proceso);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.eliminarInterconSapBO. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void actualizarFlagInterconSapBO(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE INTERCON_SAPBO SET FLAG = 'CONTABILIZADO' \n"
                    + " WHERE FECHACONTABILIZACION BETWEEN ? AND ? \n"
                    + " AND FLAG = 'ENVIADO' \n"
                    + " AND INTERCON_SAPBO.empresa_codigo=? \n"
                    + " AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=INTERCON_SAPBO.EMPLEADO)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, empresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInterconSapBO.actualizarFlagInterconSapBO. " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
