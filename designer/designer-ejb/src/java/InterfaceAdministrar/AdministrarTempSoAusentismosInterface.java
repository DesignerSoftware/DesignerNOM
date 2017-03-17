/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import ClasesAyuda.ResultadoBorrarTodoNovedades;
import Entidades.ActualUsuario;
import Entidades.TempSoAusentismos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTempSoAusentismosInterface {
      public void obtenerConexion(String idSesion);

   public void crearTempSoAusentismos(List<TempSoAusentismos> listaTempSoAusentismos);

   public void modificarTempSoAusentismos(TempSoAusentismos tempNovedades);

   public void borrarTempSoAusentismos(TempSoAusentismos tempNovedades);

   public void borrarRegistrosTempSoAusentismos(String usuarioBD);

   public List<TempSoAusentismos> consultarTempSoAusentismos(String usuarioBD);
    
   public ActualUsuario actualUsuario();
   
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD);

   public void cargarTempSoAusentismos(Date fechaReporte, String nombreCorto, String usarFormula);

   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte);
    
   public String consultarRuta();
   
   public ResultadoBorrarTodoNovedades BorrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte);
   
   public BigInteger consultarParametrosEmpresa(String usuarioBD);
/*
    

   public boolean verificarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa);

   public boolean verificarConcepto(BigInteger codConcepto);

   public boolean verificarPeriodicidad(BigInteger codPeriodicidad);

   public boolean verificarTercero(BigInteger nitTercero);

   public boolean verificarTipoEmpleadoActivo(BigInteger codEmpleado, BigInteger secEmpresa);

   public Empleados consultarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa);

   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secEmpleado);

   public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secuencia);

   public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secuencia);

   public Conceptos verificarConceptoEmpresa(BigInteger codigoConcepto, BigInteger secEmpresa);

   public String determinarTipoConcepto(BigInteger secConcepto);

   public boolean verificarZonaT(BigInteger secConcepto, BigInteger secTS, BigInteger secTC, BigInteger secTT);

   public List<Formulas> consultarFormulasCargue();

   public Formulas consultarFormulaCargueInicial();

   public boolean verificarFormulaCargueConcepto(BigInteger secConcepto, BigInteger secFormula);

   public boolean verificarNecesidadTercero(BigInteger secuencia);

   public boolean verificarTerceroEmpresa(BigInteger nit, BigInteger secEmpresa);

    
     */
}
