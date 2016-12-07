/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import ClasesAyuda.ColumnasBusquedaAvanzada;
import Entidades.ColumnasEscenarios;
import Entidades.Empleados;
import Entidades.QVWEmpleadosCorte;
import Entidades.ResultadoBusquedaAvanzada;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author PROYECTO01
 */
public interface PersistenciaColumnasEscenariosInterface {

   public List<ColumnasEscenarios> buscarColumnasEscenarios(EntityManager em);

   /**
    *
    * @param em
    * @param listaEmpleadosResultados
    * @param campos
    * @return
    */
   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleado(EntityManager em, List<ResultadoBusquedaAvanzada> listaEmpleadosResultados, List<String> campos);

   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCortePorEmpleadoCodigo(EntityManager em, List<BigInteger> listaEmpleadosResultados);

   public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCortePorEmpleadoCodigoCompletos(EntityManager em, List<BigInteger> listaEmpleadosResultados, List<String> campos);

}
