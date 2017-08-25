/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.math.BigInteger;
import javax.persistence.Cacheable;

/**
 *
 * @author user
 */
@Cacheable(false)
public class NombresEmpleadosAux {

   private BigInteger codigoEmpleado;
   private String nombreEmpleado;

   public NombresEmpleadosAux() {
   }

   public NombresEmpleadosAux(BigInteger codigoEmpleado, String nombreEmpleado) {
      this.codigoEmpleado = codigoEmpleado;
      this.nombreEmpleado = nombreEmpleado;
   }

   public BigInteger getCodigoEmpleado() {
      return codigoEmpleado;
   }

   public void setCodigoEmpleado(BigInteger codigoEmpleado) {
      this.codigoEmpleado = codigoEmpleado;
   }

   public String getNombreEmpleado() {
      return nombreEmpleado;
   }

   public void setNombreEmpleado(String nombreEmpleado) {
      this.nombreEmpleado = nombreEmpleado;
   }

   public String toString() {
      return "Entidades.NombresEmpleadosAux[ " + codigoEmpleado + " , " + nombreEmpleado + "]";
   }
}
