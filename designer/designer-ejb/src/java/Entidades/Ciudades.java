package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CIUDADES")
public class Ciudades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SECUENCIA", nullable = true)
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 4)
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @JoinColumn(name = "DEPARTAMENTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Departamentos departamento;

    public Ciudades() {
    }

    public Ciudades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Ciudades(BigInteger secuencia, String nombre) {
        this.secuencia = secuencia;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if(nombre == null){
            nombre = "";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        System.out.println("Ciudades setNombre() nombre : " + nombre);
        if (nombre != null) {
            this.nombre = nombre.toUpperCase();
        } else {
            this.nombre = nombre;
        }
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
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
        if (!(object instanceof Ciudades)) {
            return false;
        }
        Ciudades other = (Ciudades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Ciudades[ secuencia=" + secuencia + " ]";
    }
}
