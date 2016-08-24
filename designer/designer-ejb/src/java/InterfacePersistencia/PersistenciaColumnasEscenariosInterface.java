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
    public List<ColumnasBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleado(EntityManager em,List<Empleados> listaEmpleadosResultados, List<String> campos);
    public List<ResultadoBusquedaAvanzada> buscarQVWEmpleadosCorteCodigoEmpleadoCodigo(EntityManager em,List<BigInteger> listaEmpleadosResultados, String campos);
    
}
