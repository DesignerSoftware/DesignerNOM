/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Festivos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaFestivosInterface {

    public void crear(EntityManager em,Festivos festivos);

    public void editar(EntityManager em,Festivos festivos);

    public void borrar(EntityManager em,Festivos festivos);

    public List<Festivos> consultarFestivosPais(EntityManager em,BigInteger secPais);

    public Festivos consultarPais(EntityManager em,BigInteger secPais);
}
