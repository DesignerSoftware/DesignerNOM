package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PANTALLAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pantallas.findAll", query = "SELECT p FROM Pantallas p")})
public class Pantallas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @JoinColumn(name = "TABLA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Tablas tabla;
    @Size(max = 40)
    @Column(name = "LISTA")
    private String lista;
    @JoinColumn(name = "PANTALLAPADRE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Pantallas pantallapadre;
    @JoinColumn(name = "MODULO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Modulos modulo;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;

    public Pantallas() {
    }

    public Tablas getTabla() {
        return tabla;
    }

    public void setTabla(Tablas tabla) {
        this.tabla = tabla;
    }

    public Pantallas(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Pantallas(BigDecimal secuencia, String nombre) {
        this.secuencia = secuencia;
        this.nombre = nombre;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = " ";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLista() {
        if (lista == null) {
            lista = " ";
        }
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public Pantallas getPantallapadre() {
        return pantallapadre;
    }

    public void setPantallapadre(Pantallas pantallapadre) {
        this.pantallapadre = pantallapadre;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof Pantallas)) {
            return false;
        }
        Pantallas other = (Pantallas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Pantallas[ secuencia=" + secuencia + " ]";
    }
}
