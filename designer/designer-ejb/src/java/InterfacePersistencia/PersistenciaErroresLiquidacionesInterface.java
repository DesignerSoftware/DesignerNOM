/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ErroresLiquidacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaErroresLiquidacionesInterface {

   public List<ErroresLiquidacion> consultarErroresLiquidacionPorEmpleado(EntityManager em, BigInteger semEmpleado);

   public List<ErroresLiquidacion> consultarErroresLiquidacion(EntityManager em);

   public void borrar(EntityManager em, ErroresLiquidacion erroresLiquidacion);

   public int BorrarTotosErroresLiquidaciones(EntityManager em);
}
