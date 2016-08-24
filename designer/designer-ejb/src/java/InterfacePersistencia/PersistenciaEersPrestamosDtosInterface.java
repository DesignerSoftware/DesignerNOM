/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EersPrestamosDtos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEersPrestamosDtosInterface {

    public void crear(EntityManager em,EersPrestamosDtos eersPrestamosDtos);

    public void editar(EntityManager em,EersPrestamosDtos eersPrestamosDtos);
    
    public void borrar(EntityManager em,EersPrestamosDtos eersPrestamosDtos);
    
    public List<EersPrestamosDtos> eersPrestamosDtosEmpleado(EntityManager em,BigInteger secuenciaEersPrestamo);

}
