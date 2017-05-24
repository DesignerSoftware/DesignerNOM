/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import InterfacePersistencia.PersistenciaKioAdminInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaKioAdmin implements PersistenciaKioAdminInterface {

    @Override
    public ConexionesKioskos conexionesKioskos(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM CONEXIONESKIOSKOS CK WHERE EMPLEADO = ? ";
            Query query = em.createNativeQuery(sqlQuery, ConexionesKioskos.class);
            query.setParameter(1, secEmpleado);
            ConexionesKioskos listaCK;
            listaCK = (ConexionesKioskos) query.getSingleResult();
            return listaCK;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " error en conexionesKioskos()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Empleados> consultarEmpleadosCK(EntityManager em) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM EMPLEADOS E\n"
                    + " WHERE EXISTS (SELECT 'X' FROM CONEXIONESKIOSKOS CK WHERE CK.EMPLEADO=E.SECUENCIA)";
            Query query = em.createNativeQuery(sqlQuery, Empleados.class);
            List<Empleados> listaEmpleados;
            listaEmpleados = query.getResultList();
            return listaEmpleados;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " error en consultarEmpleadosCK()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void modificarck(EntityManager em, ConexionesKioskos ck) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ck);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaKioAdmin.modificarck: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void resetUsuario(EntityManager em, BigInteger secEmpleado) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "DELETE FROM CONEXIONESKIOSKOS WHERE EMPLEADO = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpleado);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " error en resetUsuario()");
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void unlockUsuario(EntityManager em, BigInteger secEmpleado) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "UPDATE CONEXIONESKIOSKOS SET ACTIVO='S' WHERE EMPLEADO = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secEmpleado);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " error en unlockUsuario()");
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
