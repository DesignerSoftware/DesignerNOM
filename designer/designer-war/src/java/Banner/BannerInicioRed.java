package Banner;

import java.io.Serializable;

public class BannerInicioRed implements Serializable{
    
    private String imagen, urlImagen;

    public BannerInicioRed(String imagen, String urlImagen) {
        this.imagen = imagen;
        this.urlImagen = urlImagen;
    }
    
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
