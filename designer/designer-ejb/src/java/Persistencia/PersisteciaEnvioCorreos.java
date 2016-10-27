package Persistencia;

import Entidades.Inforeportes;
import Entidades.envioCorreos;
import Entidades.envioCorreosAux;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersisteciaEnvioCorreos implements PersistenciaEnvioCorreosInterface {

    @Override
    public List<envioCorreos> consultarEnvios(EntityManager em, BigInteger secReporte) {
        System.out.println("Persistencia.PersisteciaEnvioCorreos.consultarEnvios()");
        System.out.println("secReporte:  " + secReporte);
        try {
            em.clear();
            String consulta = "SELECT ec.* \n"
                    + "FROM Inforeportes ir, envioCorreos ec\n"
                    + "WHERE NOT (ec.empleado IS NULL)\n"
                    + "AND ec.reporte = ir.secuencia "
                    + "AND ec.estado = 'NO ENVIADO' \n"
                    + "AND ir.secuencia = " + secReporte + "";
            Query query2 = em.createNativeQuery(consulta, envioCorreos.class);
            List<envioCorreos> listaEnvios = (List<envioCorreos>) query2.getResultList();
            if (listaEnvios != null) {
                em.clear();
                String consulta2 = "SELECT ec.secuencia, p.nombre||' '||p.primerapellido||' '||p.segundoapellido NOMBREEMPLEADO \n"
                        + "FROM Inforeportes ir, envioCorreos ec, empleados e, personas p\n"
                        + "WHERE\n"
                        + "ec.reporte = ir.secuencia\n"
                        + "AND ec.estado = 'NO ENVIADO'\n"
                        + "AND ec.empleado = e.CODIGOEMPLEADO\n"
                        + "AND e.persona = p.SECUENCIA\n"
                        + "AND ir.secuencia = " + secReporte + "";
                Query query = em.createNativeQuery(consulta2, envioCorreosAux.class);
                List<envioCorreosAux> listaEnvios2 = (List<envioCorreosAux>) query.getResultList();
                for (int i = 0; i < listaEnvios.size(); i++) {
                    for (int j = 0; j < listaEnvios2.size(); j++) {
                        if (listaEnvios.get(i).getSecuencia().equals(listaEnvios2.get(j).getSecuencia())) {
                            listaEnvios.get(i).setNombreEmpleado(listaEnvios2.get(j).getNombreEmpleado());
                            listaEnvios2.remove(listaEnvios2.get(j));
                            break;
                        }
                    }
                }
            }
            return listaEnvios;
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersisteciaEnvioCorreos.consultarEnvios() " + e);
            return null;
        }
    }

    @Override
    public Inforeportes buscarEnvioCorreoporSecuencia(EntityManager em, BigInteger secEnvioReporte) {
        try {
            System.out.println("buscarEnvioCorreoporSecuencia() secEnvioReporte: " + secEnvioReporte);
            em.clear();
            String sqlString = "SELECT * FROM inforeportes WHERE secuencia = " + secEnvioReporte;
            Query query = em.createNativeQuery(sqlString, Inforeportes.class);
            Inforeportes reporte = (Inforeportes) query.getSingleResult();
            return reporte;
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersisteciaEnvioCorreos.buscarEnvioCorreoporSecuencia(): " + e);
            return null;
        }
    }

//    @Override
//    public List<Empleados> buscarEmpleados(EntityManager em, BigInteger secEnvioRepEmp) {
//        System.out.println("Persistencia.PersisteciaEnvioCorreos.buscarEmpleados()");
//        System.out.println("secEnvioRepEmp:  " + secEnvioRepEmp);
//        try {
//            em.clear();
//            String consulta = "SELECT e.* \n"
//                    + "FROM Inforeportes ir, envioCorreos ec, Empleados e, Personas p\n"
//                    + "WHERE ec.reporte = ir.secuencia \n"
//                    + "AND ec.empleado = e.codigoempleado \n"
//                    + "AND e.persona = p.secuencia \n"
//                    + "AND ec.estado = 'NO ENVIADO' \n"
//                    + "AND ir.secuencia = " + secEnvioRepEmp + "";
//            Query query2 = em.createNativeQuery(consulta, Empleados.class);
//            List<Empleados> listEmpleados = (List<Empleados>) query2.getResultList();
//            return listEmpleados;
//        } catch (Exception e) {
//            System.out.println("Error Persistencia.PersisteciaEnvioCorreos.buscarEmpleados() " + e);
//            return null;
//        }
//    }
    @Override
    public void editar(EntityManager em, envioCorreos enviocorreos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enviocorreos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersisteciaEnvioCorreos.editar() " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, envioCorreos envio) {
        System.out.println("Persistencia.PersisteciaEnvioCorreos.borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(envio));
            tx.commit();

        } catch (Exception e) {
            System.out.println("Entre al catch");
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error Persistencia.PersisteciaEnvioCorreos.borrar() " + e);
            }
        }

    }

}
