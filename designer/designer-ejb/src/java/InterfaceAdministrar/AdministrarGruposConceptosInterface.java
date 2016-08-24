/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.GruposConceptos;
import Entidades.VigenciasGruposConceptos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarGruposConceptosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<GruposConceptos> buscarGruposConceptos();

    public void modificarGruposConceptos(List<GruposConceptos> listaGruposConceptosModificar);

    public void borrarGruposConceptos(GruposConceptos gruposConceptos);

    public void crearGruposConceptos(GruposConceptos gruposConceptos);

    public void modificarVigenciaGruposConceptos(List<VigenciasGruposConceptos> listaVigenciasGruposConceptosModificar);

    public void borrarVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos);

    public void crearVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos);

    public List<VigenciasGruposConceptos> buscarVigenciasGruposConceptos(BigInteger secuencia);
    
    public List<Conceptos> lovConceptos();

}
