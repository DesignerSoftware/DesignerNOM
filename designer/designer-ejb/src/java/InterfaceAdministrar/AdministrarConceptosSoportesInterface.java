/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.ConceptosSoportes;
import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarConceptosSoportesInterface {

    public void modificarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes);

    public void borrarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes);

    public void crearConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes);

    public List<ConceptosSoportes> consultarConceptosSoportes();

    public List<Operandos> consultarLOVOperandos();

    public List<Conceptos> consultarLOVConceptos();

    public List<Operandos> consultarLOVOperandosPorConcepto(BigInteger secConceptoSoporte);

    public BigInteger contarConceptosOperandos(BigInteger concepto, BigInteger operando);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
