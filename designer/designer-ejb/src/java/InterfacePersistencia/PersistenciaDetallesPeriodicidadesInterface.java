/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.DetallesPeriodicidades;
import Entidades.Periodicidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaDetallesPeriodicidadesInterface {

    public void crear(EntityManager em, DetallesPeriodicidades detallep);

    public void editar(EntityManager em, DetallesPeriodicidades detallep);

    public void borrar(EntityManager em, DetallesPeriodicidades detallep);

    public List<DetallesPeriodicidades> buscarDetallesPeriodicidad(EntityManager em, BigInteger secuenciaPeriodicidad);
    
    public Periodicidades buscarPeriodicidadPorSecuencia(EntityManager em, BigInteger secuenciaPeriodicidad);
}
