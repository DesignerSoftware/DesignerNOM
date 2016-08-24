/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Declarantes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaDeclarantesInterface {

    public void crear(EntityManager em,Declarantes declarantes);

    public void editar(EntityManager em,Declarantes declarantes);

    public void borrar(EntityManager em,Declarantes declarantes);

    public List<Declarantes> buscarDeclarantes(EntityManager em);

    public Declarantes buscarDeclaranteSecuencia(EntityManager em,BigInteger secuencia);

    public List<Declarantes> buscarDeclarantesPersona(EntityManager em,BigInteger secPersona);

}
