/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EmpresasOpcionesKioskos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEmpresasOpcionesKioskosInterface {

    public void crear(EntityManager em, EmpresasOpcionesKioskos empresaok);

    public void editar(EntityManager em, EmpresasOpcionesKioskos empresaok);

    public void borrar(EntityManager em, EmpresasOpcionesKioskos empresaok);

    public List<EmpresasOpcionesKioskos> consultarEmpresaOpKioskos(EntityManager em);
}
