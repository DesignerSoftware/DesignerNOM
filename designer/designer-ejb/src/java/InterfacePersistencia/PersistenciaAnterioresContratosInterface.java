/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.AnterioresContratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaAnterioresContratosInterface {

    public void crear(EntityManager em, AnterioresContratos anteriorContrato);

    public void editar(EntityManager em, AnterioresContratos anteriorContrato);

    public void borrar(EntityManager em, AnterioresContratos anteriorContrato);

    public List<AnterioresContratos> anterioresContratosPersona(EntityManager em, BigInteger secPersona);
}
