/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.NovedadesOperandos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaNovedadesOperandosInterface {
    
    public void crear(EntityManager em, NovedadesOperandos novedadesOperandos);

    public void editar(EntityManager em, NovedadesOperandos novedadesOperandos);

    public void borrar(EntityManager em, NovedadesOperandos novedadesOperandos);

    public List<NovedadesOperandos> novedadesOperandos(EntityManager em, BigInteger secuenciaOperando);
    
}
