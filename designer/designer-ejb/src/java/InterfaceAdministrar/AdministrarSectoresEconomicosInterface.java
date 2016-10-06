/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empresas;
import Entidades.SectoresEconomicos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarSectoresEconomicosInterface {
 public void obtenerConexion(String idSesion);
 public void crearSector(List<SectoresEconomicos> listaCrear);
 public void borrarSector(List<SectoresEconomicos> listaBorrar);
 public void editarSector(List<SectoresEconomicos> listaModiifcar);
 public List<SectoresEconomicos> consultarSectoresEconomicos();
 public List<SectoresEconomicos> consultarSectoresEconomicosPorEmpresa(BigInteger secEmpresa);
 public List<Empresas> consultarEmpresas();
}
