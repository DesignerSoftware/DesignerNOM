package Administrar;

import Entidades.ObjetosDB;
import Entidades.Rastros;
import Entidades.RastrosValores;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import InterfacePersistencia.PersistenciaRastrosInterface;
import InterfacePersistencia.PersistenciaRastrosTablasInterface;
import InterfacePersistencia.PersistenciaRastrosValoresInterface;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarRastros implements AdministrarRastrosInterface, Serializable {

    @EJB
    PersistenciaObjetosDBInterface persistenciaObjetosDB;
    @EJB
    PersistenciaRastrosTablasInterface persistenciaRastrosTablas;
    @EJB
    PersistenciaRastrosInterface persistenciaRastros;
    @EJB
    PersistenciaRastrosValoresInterface persistenciaRastrosValores;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    //1 = EL NOMBRE DE LA TABLA NO EXISTE EN OBJETOSDB
    //2 = RASTROS DETECTADOS PARA EL REGISTRO
    //3 = EL REGISTRO DE LA TABLA NO TIENE RASTROS
    //4 = LA TABLA NO EXISTE EN RASTROS TABLAS PERO TIENE REGISTROS EN RASTROS.
    //5 = LA TABLA NO EXISTE EN RASTROS TABLAS Y NO TIENE REGISTROS EN RASTROS.
    @Override
    public int obtenerTabla(BigInteger secRegistro, String nombreTabla) {
        ObjetosDB objetoDB = persistenciaObjetosDB.obtenerObjetoTabla(em, nombreTabla);
        if (objetoDB != null) {
            if (persistenciaRastrosTablas.verificarRastroTabla(em, objetoDB.getSecuencia())) {
                if (persistenciaRastros.verificarRastroRegistroTabla(em, secRegistro, nombreTabla)) {
                    return 2;
                } else {
                    return 3;
                }
            } else {
                if (persistenciaRastros.verificarRastroRegistroTabla(em, secRegistro, nombreTabla)) {
                    return 4;
                } else {
                    return 5;
                }
            }
        } else {
            return 1;
        }
    }

    @Override
    public boolean verificarHistoricosTabla(String nombreTabla) {
        if (persistenciaRastros.verificarRastroHistoricoTabla(em, nombreTabla)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Rastros> rastrosTabla(BigInteger secRegistro, String nombreTabla) {
        return persistenciaRastros.rastrosTabla(em, secRegistro, nombreTabla);
    }

    @Override
    public List<Rastros> rastrosTablaHistoricos(String nombreTabla) {
        return persistenciaRastros.rastrosTablaHistoricos(em, nombreTabla);
    }

    @Override
    public List<Rastros> rastrosTablaHistoricosEliminados(String nombreTabla) {
        return persistenciaRastros.rastrosTablaHistoricosEliminados(em, nombreTabla);
    }
    
    @Override
    public List<Rastros> rastrosTablaHistoricosEliminadosEmpleado(String nombreTabla) {
        return persistenciaRastros.rastrosTablaHistoricosEliminadosEmpleados(em, nombreTabla);
    }

    @Override
    public List<Rastros> rastrosTablaFecha(Date fechaRegistro, String nombreTabla) {
        return persistenciaRastros.rastrosTablaFecha(em, fechaRegistro, nombreTabla);
    }

    @Override
    public List<RastrosValores> valorRastro(BigInteger secRastro) {
        return persistenciaRastrosValores.rastroValores(em, secRastro);
    }
    
    @Override
    public boolean existenciaEmpleadoTabla(String nombreTabla){
        return persistenciaRastros.verificarEmpleadoTabla(em, nombreTabla);
    }
    
}
