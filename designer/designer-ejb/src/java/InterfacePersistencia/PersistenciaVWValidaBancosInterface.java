/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.VWValidaBancos;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaVWValidaBancosInterface {
    
    public VWValidaBancos validarDocumentoVWValidaBancos(EntityManager em, BigInteger documento);
    
}
