/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VWActualesIBCS;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVWActualesIBCSInterface {

    public VWActualesIBCS buscarIbcEmpleado(EntityManager em, BigInteger secuencia);
}
