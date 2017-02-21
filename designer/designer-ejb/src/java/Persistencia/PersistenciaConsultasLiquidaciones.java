/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ConsultasLiquidaciones;
import InterfacePersistencia.PersistenciaConsultasLiquidacionesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones de consulta
 * para las liquidaciones. <strong>Entidad Virtual</strong>
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaConsultasLiquidaciones implements PersistenciaConsultasLiquidacionesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
    */
   @Override
   public List<ConsultasLiquidaciones> liquidacionesCerradas(EntityManager em, String fechaInicial, String fechaFinal) {
      try {
         em.clear();
         String sqlQuery = "select rownum ID, T.EMPRESACODIGO, T.CORTE, T.PROCESO, T.CODIGO, T.TOTAL\n"
                 + "FROM\n"
                 + "(select --+ rule\n"
                 + "emp.codigo EMPRESACODIGO, co.fecha CORTE, p.descripcion PROCESO, p.codigo CODIGO, count(distinct cp.empleado) TOTAL\n"
                 + "from  empresas emp, procesos p, cortesprocesos cp, empleados e, comprobantes co\n"
                 + "where co.fecha between To_date( ?, 'dd/mm/yyyy') AND To_date( ?, 'dd/mm/yyyy')\n"
                 + "and cp.proceso=p.secuencia\n"
                 + "and e.secuencia = cp.empleado\n"
                 + "and co.secuencia=cp.comprobante\n"
                 + "and emp.secuencia=e.empresa\n"
                 + "and exists (select 'x' from solucionesnodos sn where sn.empleado=e.secuencia and sn.corteproceso=cp.secuencia)\n"
                 + "group by emp.codigo, p.descripcion, co.fecha, p.codigo\n"
                 + "order by p.codigo) T";
         Query query = em.createNativeQuery(sqlQuery, "ConsultasLiquidacionesEmpresa");
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         List<ConsultasLiquidaciones> listaLiquidacionesCerradas = query.getResultList();
         System.out.println("PersistenciaConsultasLiquidaciones liquidacionesCerradas() retorna : " + listaLiquidacionesCerradas);
         return listaLiquidacionesCerradas;
      } catch (Exception e) {
         System.out.println("Error PersistenciaConsultasLiquidaciones.liquidacionesCerradas. " + e);
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<ConsultasLiquidaciones> preNomina(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "SELECT ROWNUM ID, T.EMPRESACODIGO, T.CORTE, T.PROCESO, T.CODIGO, T.TOTAL, 'PRENOMINA' OBSERVACION\n"
                 + "FROM (SELECT\n"
                 + "     emp.codigo empresacodigo, sn.fechapago corte, p.descripcion proceso, p.codigo codigo, count(distinct empleado) total\n"
                 + "     from procesos p, empleados e, empresas emp, solucionesnodos sn\n"
                 + "     where p.secuencia=sn.proceso\n"
                 + "     and sn.estado='LIQUIDADO'\n"
                 + "     and e.secuencia=sn.empleado\n"
                 + "     and emp.secuencia=e.empresa\n"
                 + "     GROUP BY emp.codigo, sn.fechapago, p.descripcion, p.codigo\n"
                 + ")T";
         Query query = em.createNativeQuery(sqlQuery, "ConsultasLiquidacionesEmpresa");
         List<ConsultasLiquidaciones> listaLiquidacionesPreNomina = query.getResultList();
         return listaLiquidacionesPreNomina;
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("Error PersistenciaConsultasLiquidaciones.preNomina. " + e);
         return null;
      }
   }
}
