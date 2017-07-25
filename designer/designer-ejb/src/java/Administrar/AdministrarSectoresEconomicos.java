/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.SectoresEconomicos;
import InterfaceAdministrar.AdministrarSectoresEconomicosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaSectoresEconomicosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarSectoresEconomicos implements AdministrarSectoresEconomicosInterface {

   private static Logger log = Logger.getLogger(AdministrarSectoresEconomicos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaSectoresEconomicosInterface persistenciaSectores;
    @EJB
    PersistenciaEmpresasInterface persitenciaEmpresas;
    
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearSector(List<SectoresEconomicos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaSectores.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarSector(List<SectoresEconomicos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaSectores.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void editarSector(List<SectoresEconomicos> listaModiifcar) {
        for (int i = 0; i < listaModiifcar.size(); i++) {
            persistenciaSectores.editar(em, listaModiifcar.get(i));
        }
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        List<Empresas> listEmpresas = persitenciaEmpresas.buscarEmpresas(em);
        return listEmpresas;
    }

    @Override
    public List<SectoresEconomicos> consultarSectoresEconomicos() {
        List<SectoresEconomicos> listSectores = persistenciaSectores.buscarSectoresEconomicos(em);
        return listSectores;
    }

    @Override
    public List<SectoresEconomicos> consultarSectoresEconomicosPorEmpresa(BigInteger secEmpresa) {
        List<SectoresEconomicos> listSectores = persistenciaSectores.buscarSectoresEconomicosPorEmpresa(em,secEmpresa);
        return listSectores;
    }
}
