/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Jornadas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaJornadasInterface {

    public void crear(EntityManager em, Jornadas jornadas);

    public void editar(EntityManager em, Jornadas jornadas);

    public void borrar(EntityManager em, Jornadas jornadas);

    public Jornadas consultarJornada(EntityManager em, BigInteger secuencia);

    public List<Jornadas> consultarJornadas(EntityManager em);

    public BigInteger contarTarifasEscalafonesJornada(EntityManager em, BigInteger secuencia);

    public BigInteger contarJornadasLaboralesJornada(EntityManager em, BigInteger secuencia);
}
