/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ClasesPensiones;
import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.Pensionados;
import Entidades.Personas;
import Entidades.Retirados;
import Entidades.TiposPensionados;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasTiposTrabajadores;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


/**
 *
 * @author AndresPineda
 */

public interface AdministrarVigenciasTiposTrabajadoresInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Metodo que obtiene el total de VigenciasTiposTrabajadores por un Empleado
     * @param secEmpleado Secuencia de un Empleado
     * @return listVTT Lista de VigenciasTiposTrabajadores por un Empleado
     */
    public List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadoresEmpleado(BigInteger secEmpleado);
    /**
     * Metodo que modifica una VigenciaTiposTrabajadores
     * @param listVTTModificadas Objeto a ser modificado
     */
    public void modificarVTT(List<VigenciasTiposTrabajadores> listVTTModificadas);
    /**
     * Metodo que borra una VigenciaTiposTrabajadores
     * @param vigenciasTiposTrabajadores Objeto a ser borrada
     */
    public void borrarVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores);
    /**
     * Metodo que crea una VigenciaTiposTrabajadores
     * @param vigenciasTiposTrabajadores Objeto a ser creada
     */
    public void crearVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores);
    /**
     * Metodo que obtiene un Empleado por medio de la secuencia
     * @param secuencia Secuencia del Empleado
     * @return empl Empleado que cumple con la secuencia dada
     */
    public Empleados buscarEmpleado(BigInteger secuencia);
    /**
     * Metodo que obtiene el total de TiposTrabajadores
     * @return listTT Lista de Tipos Trabajadores total
     */
    public List<TiposTrabajadores> tiposTrabajadores();
    /**
     * Metodo que obtiene el TipoTrabajador por medio del codigo
     * @param codTipoTrabajador Codigo de TipoTrabajador
     * @return tipoT Tipo Trabajador que cumple con el codigo dado
     */
    public TiposTrabajadores tipoTrabajadorCodigo(short codTipoTrabajador);
    /**
     * Metodo que crea un Retiro
     * @param retirado Objeto retiro a ser creado
     */
    public void crearRetirado(Retirados retirado);
    /**
     * Metodo que modifica un Retiro
     * @param retirado Objeto retiro a ser modificado
     */
    public void editarRetirado(Retirados retirado);
    /**
     * Metodo que borra un Retiro
     * @param retirado Objeto retiro a ser borrado
     */
    public void borrarRetirado(Retirados retirado);
    /**
     * Metodo que obtiene el total de Retiros de un Empleado
     * @param secEmpleado Secuencia de un Empleado
     * @return listRE Lista de Retiros que ha tenido un Empleado
     */
    public List<Retirados> retiradosEmpleado(BigInteger secEmpleado);
    /**
     * Metodo que obtiene un Retiro por medio de la secuencia de la Vigencia
     * @param secVigencia Secuencia de la Vigencia
     * @return retiroV Retiro que cumple la secuencia de la Vigencia dada
     */
    public Retirados retiroPorSecuenciaVigencia(BigInteger secVigencia);
    /**
     * Metodo que obtiene el total de los MotivosRetiros
     * @return listMR Lista de MotivosRetiros
     */
    public List<MotivosRetiros> motivosRetiros();
    /**
     * Metodo que obtiene un MotivoRetiro por medio del codigo
     * @param codMotivoRetiro Codigo del Motivo Retiro
     * @return motR Motivo Retiro que cumple con el codigo dado
     */
    public MotivosRetiros motivoRetiroCodigo(BigInteger codMotivoRetiro);
    /**
     * Metodo que obtiene el total de Tipos Pensionados
     * @return listTP Lista de Tipos Pensionados
     */
    public List<TiposPensionados> tiposPensionados();  
    /**
     * Metodo que obtiene el total de Clases Pensiones
     * @return listCP Lista de Clases Pensiones
     */
    public List<ClasesPensiones> clasesPensiones();
    /**
     * Metodo que obtiene una ClasePension por medio del codigo
     * @param codClasePension Codigo ClasePension
     * @return claseP Clase Pension que cumple con el codigo dado
     */
    public ClasesPensiones clasePensionCodigo(BigInteger codClasePension);
    /**
     * Metodo que obtiene la lista de Personas
     * @return listP Lista total de Personas
     */
    public List<Personas> listaPersonas();
    /**
     * Metodo que busca una Persona por la secuencia
     * @param secPersona Secuencia de la Persona
     * @return persona Persona que cumple la secuencia
     */
    public Personas personaSecuencia(BigInteger secPersona);
    /**
     * Metodo que crea una Pension
     * @param pension Pension a crear
     */
    public void crearPensionado(Pensionados pension);
    /**
     * Metodo que modifica una Pension
     * @param pension Pension a editar
     */
    public void editarPensionado(Pensionados pension);
    /**
     * Metodo que borra una Pension
     * @param pension Pension a borrar
     */
    public void borrarPensionado(Pensionados pension);
    /**
     * Metodo que obtiene la lista de Pensiones que tiene registrado un Empleado
     * @param secEmpleado Secuencia del Empleado
     * @return listPE Lista de Pensiones por la secuencia de un Empleado
     */
    public List<Pensionados> pensionadoEmpleado(BigInteger secEmpleado);
    /**
     * Metedo que obtiene la lista total de Pensionados
     * @return listP Lista de Pensionados
     */
    public List<Pensionados> listaPensionados();
    /**
     * Metodo que obtiene una Pension por la secuencia de la Vigencia
     * @param secVigencia Secuencia de la Vigencia
     * @return pension Pension que cumple con la secuencia de la Vigencia
     */
    public Pensionados pensionPorSecuenciaVigencia(BigInteger secVigencia);
    
}
