/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VigenciasTallas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaVigenciasTallasInterface {

    public void crear(EntityManager em, VigenciasTallas vigenciasTallas);

    public void editar(EntityManager em, VigenciasTallas vigenciasTallas);

    public void borrar(EntityManager em, VigenciasTallas vigenciasTallas);

    public List<VigenciasTallas> consultarVigenciasTallasPorPersona(EntityManager em, BigInteger secPersona);
}
