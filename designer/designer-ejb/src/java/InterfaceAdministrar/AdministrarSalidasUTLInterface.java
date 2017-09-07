/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarSalidasUTLInterface {
    
    public void obtenerConexion(String idSesion);
    
    public List<File> consultarArchivosProceso();
    
    public List<File> consultarArchivosError();
    
    public String pathError();
    
    public String pathProceso();
    
}
