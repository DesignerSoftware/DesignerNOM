/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SoPoblacionObjetivos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */

public interface PersistenciaSoPoblacionObjetivosInterface {

    public void crear(EntityManager em, SoPoblacionObjetivos soPoblacionObjetivos);

    public void editar(EntityManager em, SoPoblacionObjetivos soPoblacionObjetivos);

    public void borrar(EntityManager em, SoPoblacionObjetivos soPoblacionObjetivos);

    public SoPoblacionObjetivos buscarSoPoblacionObjetivo(EntityManager em, BigInteger secuencia);

    public List<SoPoblacionObjetivos> consultarSoPoblacionObjetivos(EntityManager em);
}
