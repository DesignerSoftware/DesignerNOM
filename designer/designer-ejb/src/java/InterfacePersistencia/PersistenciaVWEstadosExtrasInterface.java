/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VWEstadosExtras;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVWEstadosExtrasInterface {

    public List<VWEstadosExtras> buscarVWEstadosExtras(EntityManager em, BigInteger secuencia);

}
