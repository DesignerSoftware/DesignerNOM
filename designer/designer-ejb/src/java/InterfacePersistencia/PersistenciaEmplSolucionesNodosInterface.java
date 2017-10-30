/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SolucionesNodos;
import Entidades.TiposTrabajadores;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEmplSolucionesNodosInterface {

    public String crear(EntityManager em, SolucionesNodos snodo);

    public String editar(EntityManager em, SolucionesNodos snodo);

    public String borrar(EntityManager em, SolucionesNodos snodo);

    public List<SolucionesNodos> solucionesNodosXEmpleado(EntityManager em, BigInteger secuenciaEmpleado);

    public BigDecimal tipottXEmpleado(EntityManager em, BigInteger secuenciaEmpleado);

}
