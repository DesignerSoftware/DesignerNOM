/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVacaciones implements PersistenciaVacacionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public List<Vacaciones> periodoVacaciones(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            List<Vacaciones> listaPeriodos;
            String sqlQuery = "SELECT V.*  FROM VACACIONES V, NOVEDADES N WHERE V.NOVEDAD=N.SECUENCIA AND V.DIASPENDIENTES > 0 AND N.TIPO = 'VACACION PENDIENTE' AND n.empleado = ? AND V.INICIALCAUSACION>=NVL(EMPLEADOCURRENT_PKG.FechaTipoContrato(N.empleado, SYSDATE),EMPLEADOCURRENT_PKG.FechaTipoContrato(N.empleado, (SELECT FECHA FROM REPORTECURRENT)))";
            Query query = em.createNativeQuery(sqlQuery, Vacaciones.class);
            query.setParameter(1, secuenciaEmpleado);
            listaPeriodos = query.getResultList();
            return listaPeriodos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVacaciones.periodoVacaciones" + e);
            return null;
        }
    }
}
