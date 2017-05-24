/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PreguntasKioskos;
import InterfaceAdministrar.AdministrarPreguntasKioscosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPreguntasKioscosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPreguntasKioscos implements AdministrarPreguntasKioscosInterface {

    @EJB
    PersistenciaPreguntasKioscosInterface persistenciaPreguntasKioskos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarPreguntasKioskos(List<PreguntasKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaPreguntasKioskos.editar(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("error en modificarPreguntasKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public void borrarPreguntasKioskos(List<PreguntasKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaPreguntasKioskos.borrar(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("error en borrarPreguntasKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public void crearPreguntasKioskos(List<PreguntasKioskos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaPreguntasKioskos.crear(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("error en crearPreguntasKioskos admi : " + e.getMessage());
        }
    }

    @Override
    public List<PreguntasKioskos> consultarPreguntasKioskos() {
        try {
            List<PreguntasKioskos> listPreguntasK = persistenciaPreguntasKioskos.consultarPreguntasKioskos(em);
            return listPreguntasK;
        } catch (Exception e) {
            System.out.println("error en consultarPreguntasKioskos  admi : " + e.getMessage());
            return null;
        }
    }

    @Override
    public PreguntasKioskos consultarPreguntaKiosko(BigInteger secPreguntaKiosko) {
        try {
            PreguntasKioskos preguntaK = persistenciaPreguntasKioskos.consultarPreguntaKiosko(em, secPreguntaKiosko);
            return preguntaK;
        } catch (Exception e) {
            System.out.println("error en consultarPreguntasKiosko : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contarPreguntasKioskos(BigInteger secuencia) {
        try {
            BigInteger conteo = persistenciaPreguntasKioskos.contarPreguntasKioskos(em, secuencia);
            return conteo;
        } catch (Exception e) {
            System.out.println("error en consultarPreguntasKiosko : " + e.getMessage());
            return null;
        }
    }

}
