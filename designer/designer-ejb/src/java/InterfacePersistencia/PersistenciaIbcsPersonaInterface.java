/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.IbcsPersona;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaIbcsPersonaInterface {
    
    public IbcsPersona buscarIbcPersona(EntityManager em, BigInteger secuencia);
    
}
