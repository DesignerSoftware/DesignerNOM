/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Declarantes;
import Entidades.RetencionesMinimas;
import Entidades.TarifaDeseo;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarDeclarantesInterface {

    public List<Declarantes> declarantesPersona(BigInteger secPersona);

    public void modificarDeclarantes(List<Declarantes> listaDeclarantesModificados);

    public void borrarDeclarantes(Declarantes declarantes);

    public void crearDeclarantes(Declarantes declarantes);

    public List<TarifaDeseo> retencionesMinimas();

    public List<RetencionesMinimas> retencionesMinimasLista();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
