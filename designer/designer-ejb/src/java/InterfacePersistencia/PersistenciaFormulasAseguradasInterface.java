/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.FormulasAseguradas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaFormulasAseguradasInterface {

    public void crear(EntityManager em,FormulasAseguradas formulasAseguradas);

    public void editar(EntityManager em,FormulasAseguradas formulasAseguradas);

    public void borrar(EntityManager em,FormulasAseguradas formulasAseguradas);

    public List<FormulasAseguradas> consultarFormulasAseguradas(EntityManager em);

    public FormulasAseguradas consultarFormulaAsegurada(EntityManager em,BigInteger secuencia);
}
