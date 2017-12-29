/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.FondosRotatorios;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaFondoRotatorioInterface {

    public List<FondosRotatorios> consultarFondoRotatorio(EntityManager em);

    public String crear(EntityManager em, FondosRotatorios fondoRotatorio);

    public String editar(EntityManager em, FondosRotatorios fondoRotatorio);

    public String borrar(EntityManager em, FondosRotatorios fondoRotatorio);
}
