/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Grupostiposentidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarGruposTiposEntidadesInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public void modificarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades);

    public void borrarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades);

    public void crearGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades);

    public List<Grupostiposentidades> consultarGruposTiposEntidades();

    public Grupostiposentidades consultarGrupoTipoEntidad(BigInteger secGruposTiposEntidades);

    public BigInteger contarTSgruposTiposEntidadesTipoEntidad(BigInteger secGruposTiposEntidades);

    public BigInteger contarTiposEntidadesGrupoTipoEntidad(BigInteger secGruposTiposEntidades);
}
