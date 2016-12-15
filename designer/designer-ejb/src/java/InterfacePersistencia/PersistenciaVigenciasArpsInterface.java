/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.VigenciasArps;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVigenciasArpsInterface {
    public void crear(EntityManager em,VigenciasArps vigarp);
    public void borrar(EntityManager em,VigenciasArps vigarp);
    public void editar(EntityManager em,VigenciasArps vigarp);
    public String actualARP(EntityManager em, BigInteger secEstructura, BigInteger secCargo, Date fechaHasta);
}
