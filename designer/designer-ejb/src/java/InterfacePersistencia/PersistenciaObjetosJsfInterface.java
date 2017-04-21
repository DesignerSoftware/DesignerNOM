/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ObjetosJsf;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaObjetosJsfInterface {

    public void crear(EntityManager em, ObjetosJsf objetojsf);

    public void editar(EntityManager em, ObjetosJsf objetojsf);

    public void borrar(EntityManager em, ObjetosJsf objetojsf);

   public List<ObjetosJsf> consultarEnableObjetoJsf(EntityManager em, String perfil, String nomPantalla);
        
}
