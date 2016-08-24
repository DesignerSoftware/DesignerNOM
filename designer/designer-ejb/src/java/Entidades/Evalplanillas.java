package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EVALPLANILLAS")
public class Evalplanillas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Column(name = "DIMENSION")
    private BigInteger dimension;
    @Size(max = 20)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHACREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERORESPUESTA")
    private short numerorespuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALORTOTAL")
    private long valortotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMEROPREGUNTA")
    private short numeropregunta;
    @Column(name = "IDEAL")
    private Short ideal;
    @Column(name = "PLANILLAORIGENCLONACION")
    private BigInteger planillaorigenclonacion;    
    @JoinColumn(name = "ESTRATEGIAMETA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Pdgmetas estrategiameta;
    @JoinColumn(name = "PDGESTRATEGIA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Pdgestrategias pdgestrategia;
    @JoinColumn(name = "EVALENFOQUE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Evalenfoques evalenfoque;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "COMPETENCIACARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Competenciascargos competenciacargo;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;

    public Evalplanillas() {
    }

    public Evalplanillas(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Evalplanillas(BigDecimal secuencia, String descripcion, Date fechacreacion, short numerorespuesta, long valortotal, short numeropregunta) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.fechacreacion = fechacreacion;
        this.numerorespuesta = numerorespuesta;
        this.valortotal = valortotal;
        this.numeropregunta = numeropregunta;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getDimension() {
        return dimension;
    }

    public void setDimension(BigInteger dimension) {
        this.dimension = dimension;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public short getNumerorespuesta() {
        return numerorespuesta;
    }

    public void setNumerorespuesta(short numerorespuesta) {
        this.numerorespuesta = numerorespuesta;
    }

    public long getValortotal() {
        return valortotal;
    }

    public void setValortotal(long valortotal) {
        this.valortotal = valortotal;
    }

    public short getNumeropregunta() {
        return numeropregunta;
    }

    public void setNumeropregunta(short numeropregunta) {
        this.numeropregunta = numeropregunta;
    }

    public Short getIdeal() {
        return ideal;
    }

    public void setIdeal(Short ideal) {
        this.ideal = ideal;
    }

    public BigInteger getPlanillaorigenclonacion() {
        return planillaorigenclonacion;
    }

    public void setPlanillaorigenclonacion(BigInteger planillaorigenclonacion) {
        this.planillaorigenclonacion = planillaorigenclonacion;
    }

    public Pdgmetas getEstrategiameta() {
        return estrategiameta;
    }

    public void setEstrategiameta(Pdgmetas estrategiameta) {
        this.estrategiameta = estrategiameta;
    }

    public Pdgestrategias getPdgestrategia() {
        return pdgestrategia;
    }

    public void setPdgestrategia(Pdgestrategias pdgestrategia) {
        this.pdgestrategia = pdgestrategia;
    }

    public Evalenfoques getEvalenfoque() {
        return evalenfoque;
    }

    public void setEvalenfoque(Evalenfoques evalenfoque) {
        this.evalenfoque = evalenfoque;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Competenciascargos getCompetenciacargo() {
        return competenciacargo;
    }

    public void setCompetenciacargo(Competenciascargos competenciacargo) {
        this.competenciacargo = competenciacargo;
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
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
        if (!(object instanceof Evalplanillas)) {
            return false;
        }
        Evalplanillas other = (Evalplanillas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Evalplanillas[ secuencia=" + secuencia + " ]";
    }

    
    
    
}
