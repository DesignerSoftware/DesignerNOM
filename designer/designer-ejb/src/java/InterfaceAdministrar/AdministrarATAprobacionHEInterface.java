/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.EersCabeceras;
import Entidades.EersDetalles;
import Entidades.EersFlujos;
import Entidades.Empleados;
import Entidades.Estructuras;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarATAprobacionHEInterface {

    public void obtenerConexion(String idSesion);

    public List<EersCabeceras> obtenerTotalesEersCabeceras();

    public List<EersCabeceras> obtenerEersCabecerasPorEmpleado(BigInteger secuencia);

    public void crearEersCabeceras(List<EersCabeceras> listaEC);

    public void editarEersCabeceras(List<EersCabeceras> listaEC);

    public void borrarEersCabeceras(List<EersCabeceras> listaEC);

    public List<EersDetalles> obtenerDetallesEersCabecera(BigInteger secuencia);

    public List<EersFlujos> obtenerFlujosEersCabecera(BigInteger secuencia);

    public List<Estructuras> lovEstructuras(BigInteger secuenciaEstado);

    public List<Empleados> lovEmpleados();

    public ActualUsuario obtenerActualUsuarioSistema();

}
