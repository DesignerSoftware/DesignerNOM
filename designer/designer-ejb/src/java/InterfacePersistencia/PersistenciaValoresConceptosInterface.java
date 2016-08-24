/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ValoresConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaValoresConceptosInterface {

    public void crear(EntityManager em, ValoresConceptos valoresConceptos);

    public void editar(EntityManager em, ValoresConceptos valoresConceptos);

    public void borrar(EntityManager em, ValoresConceptos valoresConceptos);

    public List<ValoresConceptos> consultarValoresConceptos(EntityManager em );

    public ValoresConceptos consultarValorConcepto(EntityManager em, BigInteger secuencia);
     public BigInteger consultarConceptoValorConcepto(EntityManager em, BigInteger concepto);
}
