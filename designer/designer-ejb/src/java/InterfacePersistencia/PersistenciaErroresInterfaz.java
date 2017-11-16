/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Errores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaErroresInterfaz {

    public String crear(EntityManager em, Errores errores);

    public String editar(EntityManager em, Errores errores);

    public String borrar(EntityManager em, Errores errores);

    public Errores buscarErrores(EntityManager em, BigInteger secerrores);

    public List<Errores> buscarErrores(EntityManager em);

}
