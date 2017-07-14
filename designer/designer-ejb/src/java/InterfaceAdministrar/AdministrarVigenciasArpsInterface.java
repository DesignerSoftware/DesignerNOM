/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Estructuras;
import Entidades.VigenciasArps;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciasArpsInterface {

   public void obtenerConexion(String idSesion);

   public void modificarVArp(VigenciasArps vigarp);

   public void borrarVArp(VigenciasArps vigarp);

   public void crearVArp(VigenciasArps vigarp);

   public String buscarPorcentaje(BigInteger estructura, BigInteger cargo, Date fecha);

   public int contarVigenciasARPsPorEstructuraYCargo(BigInteger estructura, BigInteger cargo);

   public List<VigenciasArps> consultarVigenciasArps();

   public List<Estructuras> consultarTodoEstructuras();

   public List<Cargos> consultarTodoCargos();
}
