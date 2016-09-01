/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVWActualesMvrsInterface {
    public BigDecimal buscarActualMVR(EntityManager em, BigInteger secuencia);
}
