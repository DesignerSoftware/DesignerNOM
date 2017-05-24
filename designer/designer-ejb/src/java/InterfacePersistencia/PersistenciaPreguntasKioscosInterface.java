/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PreguntasKioskos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaPreguntasKioscosInterface {

    public void crear(EntityManager em, PreguntasKioskos pregunta);

    public void editar(EntityManager em, PreguntasKioskos pregunta);

    public void borrar(EntityManager em, PreguntasKioskos pregunta);

    public List<PreguntasKioskos> consultarPreguntasKioskos(EntityManager em);

    public PreguntasKioskos consultarPreguntaKiosko(EntityManager em, BigInteger secuencia);

    public BigInteger contarPreguntasKioskos(EntityManager em, BigInteger secuencia);

}
