/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.TiposTrabajadores;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposTrabajadoresPlantillasInterface {

    public void obtenerConexion(String idSesion);

    public void crearTT(TiposTrabajadores tiposTrabajadores);

    public void editarTT(TiposTrabajadores tiposTrabajadores);

    public void borrarTT(TiposTrabajadores tiposTrabajadores);

    public void crearPlantillaTC(PlantillasValidaTC plantilla);

    public void editarPlantillaTC(PlantillasValidaTC plantilla);

    public void borrarPlantillaTC(PlantillasValidaTC plantilla);

    public void crearPlantillaTS(PlantillasValidaTS plantilla);

    public void editarPlantillaTS(PlantillasValidaTS plantilla);

    public void borrarPlantillaTS(PlantillasValidaTS plantilla);

    public void crearPlantillaRL(PlantillasValidaRL plantilla);

    public void editarPlantillaRL(PlantillasValidaRL plantilla);

    public void borrarPlantillaRL(PlantillasValidaRL plantilla);

    public void crearPlantillaLL(PlantillasValidaLL plantilla);

    public void editarPlantillaLL(PlantillasValidaLL plantilla);

    public void borrarPlantillaLL(PlantillasValidaLL plantilla);

    public void crearPlantillaNL(PlantillasValidaNL plantilla);

    public void editarPlantillaNL(PlantillasValidaNL plantilla);

    public void borrarPlantillaNL(PlantillasValidaNL plantilla);

    public List<TiposTrabajadores> listaTT();

    public List<PlantillasValidaTC> listaPlantillaTC(BigInteger secTT);

    public List<PlantillasValidaTS> listaPlantillaTS(BigInteger secTT);

    public List<PlantillasValidaRL> listaPlantillaRL(BigInteger secTT);

    public List<PlantillasValidaLL> listaPlantillaLL(BigInteger secTT);

    public List<PlantillasValidaNL> listaPlantillaNL(BigInteger secTT);
    
    public boolean ConsultarRegistrosSecundarios(BigInteger secuencia);

}
