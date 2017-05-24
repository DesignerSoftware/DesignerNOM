/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PreguntasKioskos;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarPreguntasKioscosInterface {
    public void obtenerConexion(String idSesion);
    
    public void modificarPreguntasKioskos(List<PreguntasKioskos> lista);

    public void borrarPreguntasKioskos(List<PreguntasKioskos> lista);

    public void crearPreguntasKioskos(List<PreguntasKioskos> lista);

    public List<PreguntasKioskos> consultarPreguntasKioskos();

    public PreguntasKioskos consultarPreguntaKiosko(BigInteger secPreguntaKiosko);

    public BigInteger contarPreguntasKioskos(BigInteger secuencia);
    
}
