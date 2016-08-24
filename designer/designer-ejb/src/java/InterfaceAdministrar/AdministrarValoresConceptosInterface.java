/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.ValoresConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarValoresConceptosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void modificarValoresConceptos(List<ValoresConceptos> listaValoresConceptos);

    public void borrarValoresConceptos(List<ValoresConceptos> listaValoresConceptos);

    public void crearValoresConceptos(List<ValoresConceptos> listaValoresConceptos);

    public List<ValoresConceptos> consultarValoresConceptos();

    public List<Conceptos> consultarLOVConceptos();

    public BigInteger contarConceptoValorConcepto(BigInteger concepto);
}
