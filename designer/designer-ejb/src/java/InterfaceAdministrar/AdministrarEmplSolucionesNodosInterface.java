/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplSolucionesNodosInterface {
     public void obtenerConexion(String idSesion);
     public String crear(SolucionesNodos sn);
     public String editar(SolucionesNodos sn);
     public String borrar(SolucionesNodos sn);
     public List<SolucionesNodos> solucionesNodosEmpl(BigInteger secEmpleado);
     public List<Empleados> emplSolucionesNodos();
     public List<Conceptos> buscarConceptos();
     public List<Terceros> buscarTerceros();
     public List<Formulas> buscarFormulas();
     public List<Cuentas> buscarCuentas();
     public List<CentrosCostos> buscarCentrosCostos();
     public List<Procesos> buscarProcesos();
     public List<Periodicidades> buscarPeriodicidades();
     public BigDecimal buscarTipoTrabjadorXEmpl(BigInteger secEmpleado);
    
}
