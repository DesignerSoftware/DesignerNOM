/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.LiquidacionesLogs;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaLiquidacionesLogsInterface {

    public void crear(EntityManager em, LiquidacionesLogs liquidacionesLogs);

    public void editar(EntityManager em, LiquidacionesLogs liquidacionesLogs);

    public void borrar(EntityManager em, LiquidacionesLogs liquidacionesLogs);

    public List<LiquidacionesLogs> consultarLiquidacionesLogs(EntityManager em);

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(EntityManager em, BigInteger secEmpleado);
    
    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorOperando(EntityManager em, BigInteger secOperando);
}
