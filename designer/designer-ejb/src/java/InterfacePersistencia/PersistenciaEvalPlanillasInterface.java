/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Evalplanillas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEvalPlanillasInterface {

    public void crear(EntityManager em, Evalplanillas evalplanilla);

    public void editar(EntityManager em, Evalplanillas evalplanilla);

    public void borrar(EntityManager em, Evalplanillas evalplanilla);

    public List<Evalplanillas> consultarEvalPlanilla(EntityManager em);
}
