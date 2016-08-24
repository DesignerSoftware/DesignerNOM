/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Turnosrotativos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaTurnosRotativosInterface {

    public void crear(EntityManager em, Turnosrotativos turnosrotativos);

    public void editar(EntityManager em, Turnosrotativos turnosrotativos);

    public void borrar(EntityManager em, Turnosrotativos turnosrotativos);

    public Turnosrotativos buscarTurnoRotativoPorSecuencia(EntityManager em, BigInteger secuencia);

    public List<Turnosrotativos> buscarTurnosRotativos(EntityManager em);

    public List<Turnosrotativos> buscarTurnosRotativosPorCuadrilla(EntityManager em, BigInteger secuencia);

}
