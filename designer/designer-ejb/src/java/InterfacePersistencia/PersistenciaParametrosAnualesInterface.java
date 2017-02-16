/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import java.math.BigDecimal;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaParametrosAnualesInterface {
    
public BigDecimal consultarSMLV(EntityManager em);    

public BigDecimal consultarAuxTransporte(EntityManager em);    

public BigDecimal consultarValorUVT(EntityManager em);    

public BigDecimal consultarvalorMinIBC(EntityManager em);    

public BigDecimal consultarTopeMaxSegSocial(EntityManager em);    
    
}
