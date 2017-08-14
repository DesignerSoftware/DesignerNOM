/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'ParametrosEstados' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaParametrosEstados implements PersistenciaParametrosEstadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametrosEstados.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public Integer empleadosParaLiquidar(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM PARAMETROSESTADOS pe WHERE exists (select p.secuencia from parametros p, parametrosinstancias pi, usuariosinstancias ui , usuarios u where p.secuencia = pe.parametro and u.secuencia = p.usuario and pi.parametro = p.secuencia and ui.instancia = pi.instancia and u.alias = ? and proceso = (SELECT pe.proceso FROM PARAMETROSESTRUCTURAS pe, usuarios u where u.secuencia = pe.usuario and u.alias=?))";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, usuarioBD);
         query.setParameter(2, usuarioBD);
         BigDecimal a = (BigDecimal) query.getSingleResult();
         Integer empeladosALiquidar = a.intValueExact();
         return empeladosALiquidar;
      } catch (Exception e) {
         log.error("Error PersistenciaParametrosEstados.empleadosParaLiquidar " + e.getMessage());
         return null;
      }
   }

   @Override
   public Integer empleadosLiquidados(EntityManager em, String usuarioBD
   ) {
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM PARAMETROSESTADOS pe WHERE exists (select p.secuencia from parametros p, parametrosinstancias pi, usuariosinstancias ui , usuarios u where p.secuencia = pe.parametro and u.secuencia = p.usuario and pi.parametro = p.secuencia and ui.instancia = pi.instancia and u.alias = ? and proceso = (SELECT PROCESO FROM PARAMETROSESTRUCTURAS pe, usuarios u where u.secuencia = pe.usuario and u.alias=?)) and pe.estado = 'LIQUIDADO'";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, usuarioBD);
         query.setParameter(2, usuarioBD);
         BigDecimal a = (BigDecimal) query.getSingleResult();
         Integer empeladosALiquidar = a.intValueExact();
         return empeladosALiquidar;
      } catch (Exception e) {
         log.error("Error PersistenciaParametrosEstados.empleadosLiquidados " + e.getMessage());
         return null;
      }
   }

   @Override
   public void inicializarParametrosEstados(EntityManager em
   ) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery("call PARAMETROS_PKG.inicializarparametrosestados()");
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Entro en el Catch");
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaParametrosEstados.inicializarparametrosestados " + e.getMessage());
      }
   }

   @Override
   public String parametrosComprobantes(EntityManager em, BigInteger secuenciaParametro
   ) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT pe.estado FROM ParametrosEstados pe WHERE pe.parametros.secuencia = :secuenciaParametro");
         query.setParameter("secuenciaParametro", secuenciaParametro);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         String estadoParametro = (String) query.getSingleResult();
         return estadoParametro;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaParametrosEstados.parametrosComprobantes" + e.getMessage());
         return null;
      }
   }
}
