package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PROYECTOS")
public class Proyectos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "NOMBREPROYECTO")
    private String nombreproyecto;
    @Size(max = 200)
    @Column(name = "DESCRIPCIONPROYECTO")
    private String descripcionproyecto;
    @Column(name = "MONTO")
    private BigInteger monto;
    @Column(name = "CANTIDADPERSONAS")
    private BigInteger cantidadpersonas;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Basic(optional = false)
    @NotNull
    @Size(max = 20)
    @Column(name = "CODIGO")
    private String codigo;
    @JoinColumn(name = "PRY_PLATAFORMA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private PryPlataformas pryPlataforma;
    @JoinColumn(name = "PRY_CLIENTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private PryClientes pryCliente;
    @JoinColumn(name = "TIPOMONEDA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Monedas tipomoneda;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;

    public Proyectos() {
    }

    public Proyectos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Proyectos(BigInteger secuencia, String nombreproyecto, String codigo) {
        this.secuencia = secuencia;
        this.nombreproyecto = nombreproyecto;
        this.codigo = codigo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombreproyecto() {
        return nombreproyecto;
    }

    public void setNombreproyecto(String nombreproyecto) {
        this.nombreproyecto = nombreproyecto;
    }

    public String getDescripcionproyecto() {
        if (descripcionproyecto != null) {
            return descripcionproyecto.toUpperCase();
        }
        return descripcionproyecto;
    }

    public void setDescripcionproyecto(String descripcionproyecto) {
        this.descripcionproyecto = descripcionproyecto.toUpperCase();
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public BigInteger getCantidadpersonas() {
        return cantidadpersonas;
    }

    public void setCantidadpersonas(BigInteger cantidadpersonas) {
        this.cantidadpersonas = cantidadpersonas;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public PryPlataformas getPryPlataforma() {
        return pryPlataforma;
    }

    public void setPryPlataforma(PryPlataformas pryPlataforma) {
        this.pryPlataforma = pryPlataforma;
    }

    public PryClientes getPryCliente() {
        return pryCliente;
    }

    public void setPryCliente(PryClientes pryCliente) {
        this.pryCliente = pryCliente;
    }

    public Monedas getTipomoneda() {
        return tipomoneda;
    }

    public void setTipomoneda(Monedas tipomoneda) {
        this.tipomoneda = tipomoneda;
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
        if (!(object instanceof Proyectos)) {
            return false;
        }
        Proyectos other = (Proyectos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Proyectos[ secuencia=" + secuencia + " ]";
    }
}
