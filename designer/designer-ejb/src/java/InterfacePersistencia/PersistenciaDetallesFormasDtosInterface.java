/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.DetallesFormasDtos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaDetallesFormasDtosInterface {

    public void crear(EntityManager em,DetallesFormasDtos detallesFormasDtos);

    public void editar(EntityManager em,DetallesFormasDtos detallesFormasDtos);
    
    public void borrar(EntityManager em,DetallesFormasDtos detallesFormasDtos);

    public List<DetallesFormasDtos> detallesFormasDescuentos(EntityManager em,BigInteger formaDto);
    
}
