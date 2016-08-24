/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Clasesausentismos;
import Entidades.Tiposausentismos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarClasesAusentismosInterface {

    public void modificarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos);

    public void borrarClasesAusentismos(List<Clasesausentismos> listClasesAusentismos);

    public void crearClasesAusentismos(List<Clasesausentismos> listClasesAusentismos);

    public List<Clasesausentismos> consultarClasesAusentismos();

    public BigInteger contarCausasAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos);

    public BigInteger contarSoAusentismosClaseAusentismo(BigInteger secuenciaClasesAusentismos);

    public List<Tiposausentismos> consultarLOVTiposAusentismos();
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
