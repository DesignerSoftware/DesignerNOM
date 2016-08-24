/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Proyecciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaProyeccionesInterface {

    public void crear(EntityManager em, Proyecciones proyectos);

    public void editar(EntityManager em, Proyecciones proyectos);

    public void borrar(EntityManager em, Proyecciones proyectos);

    public List<Proyecciones> consultarProyecciones(EntityManager em, BigInteger secEmpleado);
}
