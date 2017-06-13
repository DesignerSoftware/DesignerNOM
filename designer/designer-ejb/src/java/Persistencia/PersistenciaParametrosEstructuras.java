/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ParametrosEstructuras;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'ParametrosEstructuras' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaParametrosEstructuras implements PersistenciaParametrosEstructurasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    
    @Override
    public void editar(EntityManager em, ParametrosEstructuras parametroEstructura) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametroEstructura);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public BigInteger buscarEmpresaParametros(EntityManager em, String usuarioBD) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p.estructura.organigrama.empresa.secuencia FROM ParametrosEstructuras p WHERE p.usuario.alias = :usuarioBD ");
            query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            BigInteger secEmpresa = (BigInteger) query.getSingleResult();
            return secEmpresa;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaParametrosEstructuras.buscarEmpresaParametros()" + e.getMessage());
            return null;
        }
    }
    // 17/06/13
    @Override
    public BigInteger buscarEmpresaParametros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p.estructura.organigrama.empresa.secuencia FROM ParametrosEstructuras p WHERE p.usuario.alias = (SELECT vw.alias FROM ActualUsuario vw ) ");
            //query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            BigInteger secEmpresa = (BigInteger) query.getSingleResult();
            return secEmpresa;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaParametrosEstructuras.buscarEmpresaParametros()" + e.getMessage());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras buscarParametro(EntityManager em, String usuarioBD) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(pe) FROM ParametrosEstructuras pe WHERE pe.usuario.alias = :usuarioBD ");
            query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                query = em.createQuery("SELECT pe FROM ParametrosEstructuras pe WHERE pe.usuario.alias = :usuarioBD ");
                query.setParameter("usuarioBD", usuarioBD);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                ParametrosEstructuras parametroEstructura = (ParametrosEstructuras) query.getSingleResult();
                return parametroEstructura;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosEstructuras.estructurasComprobantes" + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionarEmpleados(EntityManager em, BigInteger secParametroEstructura) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call PARAMETROS_PKG.InsertarParametrosProceso(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secParametroEstructura);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("PersistenciaParametrosEstructuras.adicionarEmpleados: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Integer empleadosParametrizados(EntityManager em, BigInteger secProceso) {
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)\n"
                    + "FROM PARAMETROSESTRUCTURAS P \n"
                    + "WHERE P.PROCESO = ?\n"
                    + "AND P.USUARIO <> (SELECT SECUENCIA FROM USUARIOS WHERE ALIAS=USER)\n"
                    + "AND EXISTS (SELECT * FROM USUARIOSESTRUCTURAS UE, EMPRESAS E WHERE UE.USUARIO=P.USUARIO\n"
                    + "AND UE.EMPRESA = E.SECUENCIA)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secProceso);
            BigDecimal a = (BigDecimal) query.getSingleResult();
            Integer empeladosALiquidar = a.intValueExact();
            return empeladosALiquidar;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosEstructuras.empleadosParametrizados " + e.getMessage());
            return null;
        }
    }

    @Override
    public Integer diasDiferenciaFechas(EntityManager em, String fechaInicial, String fechaFinal) {
        try {
            em.clear();
            String sqlQuery = "SELECT DIAS360(to_date( ?, 'dd/MM/yyyy'), to_date( ?, 'dd/MM/yyyy')) Dias FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            BigDecimal a = (BigDecimal) query.getSingleResult();
            Integer empeladosALiquidar = a.intValueExact();
            return empeladosALiquidar;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosEstructuras.diasDiferenciaFechas " + e.getMessage());
            return null;
        }
    }
}
