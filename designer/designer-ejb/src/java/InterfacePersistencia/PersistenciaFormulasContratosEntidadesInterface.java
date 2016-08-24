/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.FormulasContratosEntidades;
import Entidades.Formulascontratos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaFormulasContratosEntidadesInterface {

    public void crear(EntityManager em,FormulasContratosEntidades formulasAseguradas);

    public void editar(EntityManager em,FormulasContratosEntidades formulasAseguradas);

    public void borrar(EntityManager em,FormulasContratosEntidades formulasAseguradas);

    public List<FormulasContratosEntidades> consultarFormulasContratosEntidades(EntityManager em);

    public FormulasContratosEntidades consultarFormulaContratoEntidad(EntityManager em,BigInteger secuencia);


    public List<FormulasContratosEntidades> consultarFormulasContratosEntidadesPorFormulaContrato(EntityManager em,BigInteger secFormulaContrato);
}
