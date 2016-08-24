/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.MotivosTurnos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaMotivosTurnosInterface {

    public void crear(EntityManager em, MotivosTurnos motivosTurnos);

    public void editar(EntityManager em, MotivosTurnos motivosTurnos);

    public void borrar(EntityManager em, MotivosTurnos motivosTurnos);

    public List<MotivosTurnos> consultarMotivosTurnos(EntityManager em);

    public MotivosTurnos consultarMotivoTurnoPorSecuencia(EntityManager em, BigInteger secuencia);

}
