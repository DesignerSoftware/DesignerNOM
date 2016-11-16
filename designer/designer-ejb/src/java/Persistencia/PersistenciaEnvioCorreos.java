package Persistencia;

import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.EnvioCorreos;
import Entidades.EnvioCorreosAux;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import java.math.BigDecimal;
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
public class PersistenciaEnvioCorreos implements PersistenciaEnvioCorreosInterface {

    @Override
    public List<EnvioCorreos> consultarEnvios(EntityManager em, BigInteger secReporte) {
        System.out.println("Persistencia.PersisteciaEnvioCorreos.consultarEnvios()");
        System.out.println("secReporte:  " + secReporte);
        try {
            em.clear();
            String consulta = "SELECT ec.* \n"
                    + "FROM Inforeportes ir, EnvioCorreos ec \n"
                    + "WHERE ec.empleado IS NOT NULL \n"
                    + "AND ec.reporte = ir.secuencia "
                    + "AND ec.estado = 'NO ENVIADO' \n"
                    + "AND ir.secuencia = " + secReporte + "";
            Query query2 = em.createNativeQuery(consulta, EnvioCorreos.class);
            List<EnvioCorreos> listaEnvios = (List<EnvioCorreos>) query2.getResultList();
            if (listaEnvios != null) {
                em.clear();
                String consulta2 = "SELECT ec.secuencia, p.nombre||' '||p.primerapellido||' '||p.segundoapellido NOMBREEMPLEADO \n"
                        + "FROM Inforeportes ir, EnvioCorreos ec, empleados e, personas p\n"
                        + "WHERE\n"
                        + "ec.reporte = ir.secuencia\n"
                        + "AND ec.estado = 'NO ENVIADO'\n"
                        + "AND ec.empleado = e.CODIGOEMPLEADO\n"
                        + "AND e.persona = p.SECUENCIA\n"
                        + "AND ir.secuencia = " + secReporte + "";
                Query query = em.createNativeQuery(consulta2, EnvioCorreosAux.class);
                List<EnvioCorreosAux> listaEnvios2 = (List<EnvioCorreosAux>) query.getResultList();
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
//        System.out.println("Persistencia.PersistenciaEnvioCorreos.buscarEmpleados()");
//        System.out.println("secEnvioRepEmp:  " + secEnvioRepEmp);
//        try {
//            em.clear();
//            String consulta = "SELECT e.* \n"
//                    + "FROM Inforeportes ir, EnvioCorreos ec, Empleados e, Personas p\n"
//                    + "WHERE ec.reporte = ir.secuencia \n"
//                    + "AND ec.empleado = e.codigoempleado \n"
//                    + "AND e.persona = p.secuencia \n"
//                    + "AND ec.estado = 'NO ENVIADO' \n"
//                    + "AND ir.secuencia = " + secEnvioRepEmp + "";
//            Query query2 = em.createNativeQuery(consulta, Empleados.class);
//            List<Empleados> listEmpleados = (List<Empleados>) query2.getResultList();
//            return listEmpleados;
//        } catch (Exception e) {
//            System.out.println("Error Persistencia.PersistenciaEnvioCorreos.buscarEmpleados() " + e);
//            return null;
//        }
//    }
    @Override
    public void editar(EntityManager em, EnvioCorreos enviocorreos) {
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
    public void borrar(EntityManager em, EnvioCorreos envio) {
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

    @Override
    public List<Empleados> CorreoCodEmpleados(EntityManager em, BigDecimal emplDesde, BigDecimal emplHasta) {
        System.out.println("Persistencia.PersistenciaEnvioCorreos.CorreoCodEmpleados()");
        System.out.println("emplDesde: " + emplDesde);
        System.out.println("emplHasta: " + emplHasta);
        try {
            em.clear();
            String consulta = "SELECT e.* \n"
                    + "FROM ParametrosReportes pr, Empleados e, Personas p \n"
                    + "WHERE p.email IS NOT NULL \n"
                    + "AND pr.usuario = USER \n"
                    + "AND e.persona = p.secuencia \n"
                    + "AND e.codigoempleado BETWEEN " + emplDesde + "AND " + emplHasta + " ";

            Query query = em.createNativeQuery(consulta, Empleados.class);
            List<Empleados> correoCod = (List<Empleados>) query.getResultList();
            System.out.println("CorreoCod: " + correoCod);
            return correoCod;
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersisteciaEnvioCorreos.CorreoCodEmpleados(): " + e);
            return null;
        }
    }

    @Override
    public ConfiguracionCorreo consultarRemitente(EntityManager em, BigInteger secEmpresa) {
        System.out.println("Persistencia.PersistenciaEnvioCorreos.consultarRemitente()");
        try {
            em.clear();
            String consulta = "SELECT cc.* \n"
                    + "FROM ConfiguracionCorreos cc \n"
                    + "WHERE cc.empresa = " + secEmpresa + " ";
            Query query = em.createNativeQuery(consulta, ConfiguracionCorreo.class);
            ConfiguracionCorreo remitente = (ConfiguracionCorreo) query.getSingleResult();
            System.out.println("CorreoCod: " + remitente);
            return remitente;
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersistenciaEnvioCorreos.consultarRemitente(): " + e);
            return null;
        }
    }

    @Override
    public void insertarFalloCorreos(EntityManager em, EnvioCorreos enviocorreo) {
        System.out.println("Persistencia.PersistenciaEnvioCorreos.insertarFalloCorreos()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(enviocorreo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error Persistencia.PersistenciaEnvioCorreos.insertarFalloCorreos(): " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}