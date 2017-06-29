/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarDetalleVacacionInterface {
   
   public void obtenerConexion(String idSesion);
   
   public void modificarDetalleVacacion(NovedadesSistema novedadSistema);
   
   public List<NovedadesSistema> novedadsistemaPorEmpleadoYVacacion(BigInteger secEmpleado, BigInteger secVacacion);

   public BigDecimal consultarValorTotalDetalleVacacion(BigInteger secNovedadSistema);
   
   public List<Vacaciones> periodosEmpleado(BigInteger secuenciaEmpleado);
   
}
