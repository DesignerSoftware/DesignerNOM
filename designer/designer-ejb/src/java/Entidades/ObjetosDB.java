package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "OBJETOSDB")
public class ObjetosDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 80)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 30)
    @Column(name = "CLASIFICACION")
    private String clasificacion;
    @Size(max = 1)
    @Column(name = "EXCLUIRASIGNACIONPERFIL")
    private String excluirasignacionperfil;
    @Size(max = 1)
    @Column(name = "AUTORIZADA")
    private String autorizada;
    @JoinColumn(name = "MODULO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Modulos modulo;
    @Transient
    private String estadoTipo;
    @Transient
    private String estadoClasificacion;

    public ObjetosDB() {
    }

    public ObjetosDB(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public ObjetosDB(BigInteger secuencia, String tipo, String nombre) {
        this.secuencia = secuencia;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = "";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = "";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClasificacion() {
        if (clasificacion == null) {
            clasificacion = "";
        }
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getExcluirasignacionperfil() {
        return excluirasignacionperfil;
    }

    public void setExcluirasignacionperfil(String excluirasignacionperfil) {
        this.excluirasignacionperfil = excluirasignacionperfil;
    }

    public String getAutorizada() {
        return autorizada;
    }

    public void setAutorizada(String autorizada) {
        this.autorizada = autorizada;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public String getEstadoTipo() {
        if (estadoTipo == null) {
            if (tipo == null) {
                estadoTipo = "FUNCTION";
            } else if (tipo.equalsIgnoreCase("FUNCTION")) {
                estadoTipo = "FUNCTION";
            } else if (tipo.equalsIgnoreCase("PACKAGE")) {
                estadoTipo = "PACKAGE";
            } else if (tipo.equalsIgnoreCase("PROCEDURE")) {
                estadoTipo = "PROCEDURE";
            } else if (tipo.equalsIgnoreCase("SEQUENCE")) {
                estadoTipo = "SEQUENCE";
            } else if (tipo.equalsIgnoreCase("TABLE")) {
                estadoTipo = "TABLE";
            } else if (tipo.equalsIgnoreCase("VIEW")) {
                estadoTipo = "VIEW";
            }
        }
        return estadoTipo;
    }

    public void setEstadoTipo(String estadoTipo) {
        this.estadoTipo = estadoTipo;
    }

    public String getEstadoClasificacion() {
        if (estadoClasificacion == null) {
            if (clasificacion == null) {
                estadoClasificacion = " ";
            } else if (clasificacion.equalsIgnoreCase("POR ASIGNAR")) {
                estadoClasificacion = "POR ASIGNAR";
            } else if (clasificacion.equalsIgnoreCase("CONFIGURACION")) {
                estadoClasificacion = "CONFIGURACION";
            } else if (clasificacion.equalsIgnoreCase("CONSTRUCCION")) {
                estadoClasificacion = "CONSTRUCCION";
            } else if (clasificacion.equalsIgnoreCase("DATOS")) {
                estadoClasificacion = "DATOS";
            } else if (clasificacion.equalsIgnoreCase("SISTEMA")) {
                estadoClasificacion = "SISTEMA";
            } else if (clasificacion.equalsIgnoreCase("NULL")) {
                estadoClasificacion = "NULL";
            }
        }
        return estadoClasificacion;
    }

    public void setEstadoClasificacion(String estadoClasificacion) {
        this.estadoClasificacion = estadoClasificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetosDB)) {
            return false;
        }
        ObjetosDB other = (ObjetosDB) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ObjetosDB[ secuencia=" + secuencia + " ]";
    }
}
