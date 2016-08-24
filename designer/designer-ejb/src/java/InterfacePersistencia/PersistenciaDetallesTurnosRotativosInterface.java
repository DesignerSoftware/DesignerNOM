/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.DetallesTurnosRotativos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaDetallesTurnosRotativosInterface {

    public void crear(EntityManager em, DetallesTurnosRotativos rotativos);

    public void editar(EntityManager em, DetallesTurnosRotativos rotativos);

    public void borrar(EntityManager em, DetallesTurnosRotativos rotativos);

    public DetallesTurnosRotativos buscarDetalleTurnoRotativoPorSecuencia(EntityManager em, BigInteger secuencia);

    public List<DetallesTurnosRotativos> buscarDetallesTurnosRotativos(EntityManager em);

    public List<DetallesTurnosRotativos> buscarDetallesTurnosRotativosPorTurnoRotativo(EntityManager em, BigInteger secuencia);

}
