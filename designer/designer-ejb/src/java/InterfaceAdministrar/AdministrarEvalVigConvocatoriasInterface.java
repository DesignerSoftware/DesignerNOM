/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Evalvigconvocatorias;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEvalVigConvocatoriasInterface {

    public void obtenerConexion(String idSesion);
    public void crear(List<Evalvigconvocatorias> listCrear);
    public void borrar(List<Evalvigconvocatorias> listBorrar);
    public void editar(List<Evalvigconvocatorias> listModificar);
    public List<Evalvigconvocatorias> buscarEvalVigConvocatorias();
}
