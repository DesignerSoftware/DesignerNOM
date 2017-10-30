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

    public String crear(EntityManager em,Festivos festivos);

    public String editar(EntityManager em,Festivos festivos);

    public String borrar(EntityManager em,Festivos festivos);

    public List<Festivos> consultarFestivosPais(EntityManager em,BigInteger secPais);

}
