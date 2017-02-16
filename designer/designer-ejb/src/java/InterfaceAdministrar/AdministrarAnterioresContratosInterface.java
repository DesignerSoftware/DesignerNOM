/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.AnterioresContratos;
import Entidades.Cargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface AdministrarAnterioresContratosInterface {

    public void obtenerConexion(String idSesion);

    public void crearAnteriorContrato(List<AnterioresContratos> listaCrear);

    public void editarAnteriorContrato(List<AnterioresContratos> listaModificar);

    public void borrarAnteriorContrato(List<AnterioresContratos> listaBorrar);

    public List<AnterioresContratos> listaAnterioresContratos(BigInteger secPersona);

    public List<Cargos> lovCargos();

}
