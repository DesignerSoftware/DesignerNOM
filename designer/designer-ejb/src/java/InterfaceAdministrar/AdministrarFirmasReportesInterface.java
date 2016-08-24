/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.FirmasReportes;
import Entidades.Personas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarFirmasReportesInterface {

    public void modificarFirmasReportes(List<FirmasReportes> listaFirmasReportes);

    public void borrarFirmasReportes(List<FirmasReportes> listaFirmasReportes);

    public void crearFirmasReportes(List<FirmasReportes> listaFirmasReportes);

    public List<FirmasReportes> consultarFirmasReportes();

    public FirmasReportes consultarTipoIndicador(BigInteger secMotivoDemanda);

    public List<Cargos> consultarLOVCargos();

    public List<Personas> consultarLOVPersonas();

    public List<Empresas> consultarLOVEmpresas();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
