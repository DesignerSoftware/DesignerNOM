/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaKioAdminInterface {

ConexionesKioskos conexionesKioskos(EntityManager em, BigInteger secEmpleado);

List<Empleados> consultarEmpleadosCK(EntityManager em);

public void modificarck(EntityManager em, ConexionesKioskos ck);

public void resetUsuario(EntityManager em, BigInteger secEmpleado);

public void unlockUsuario(EntityManager em, BigInteger secEmpleado);
    
}
