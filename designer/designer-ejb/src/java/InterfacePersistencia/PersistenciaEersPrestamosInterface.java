/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EersPrestamos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEersPrestamosInterface {

    public void crear(EntityManager em,EersPrestamos eersPrestamos);   

    public void editar(EntityManager em,EersPrestamos eersPrestamos);

    public void borrar(EntityManager em,EersPrestamos eersPrestamos);

    public List<EersPrestamos> eersPrestamosEmpleado(EntityManager em,BigInteger secuenciaEmpleado);
}
