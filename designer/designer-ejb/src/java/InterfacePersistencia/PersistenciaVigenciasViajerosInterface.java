/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VigenciasViajeros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaVigenciasViajerosInterface {

    public void crear(EntityManager em, VigenciasViajeros vigenciaViajero);

    public void editar(EntityManager em, VigenciasViajeros vigenciaViajero);

    public void borrar(EntityManager em, VigenciasViajeros vigenciaViajero);

    public VigenciasViajeros consultarTipoExamen(EntityManager em, BigInteger secuencia);

    public List<VigenciasViajeros> consultarVigenciasViajeros(EntityManager em );

    public List<VigenciasViajeros> consultarVigenciasViajerosPorEmpleado(EntityManager em, BigInteger secEmpleado);
}
