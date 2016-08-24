/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Cuadrillas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaCuadrillasInterface {

    public void crear(EntityManager em, Cuadrillas cuadrillas);

    public void editar(EntityManager em, Cuadrillas cuadrillas);

    public void borrar(EntityManager em, Cuadrillas cuadrillas);

    public Cuadrillas buscarCuadrillaPorSecuencia(EntityManager em, BigInteger secuencia);

    public List<Cuadrillas> buscarCuadrillas(EntityManager em);

    public void borrarProgramacionCompleta(EntityManager em);

    public List<Cuadrillas> buscarCuadrillasParaEmpleado(EntityManager em, BigInteger secuencia);

}
