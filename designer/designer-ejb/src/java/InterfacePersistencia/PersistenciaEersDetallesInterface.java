/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EersDetalles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaEersDetallesInterface {

    public List<EersDetalles> buscarEersDetallesPorEersCabecera(EntityManager em, BigInteger secuencia);

}
