/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.Vacaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVacacionesInterface {
    
    public List<Vacaciones> periodoVacaciones(EntityManager em, BigInteger secuenciaEmpleado);
}
