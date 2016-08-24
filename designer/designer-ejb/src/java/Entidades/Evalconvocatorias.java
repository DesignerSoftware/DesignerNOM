package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "EVALCONVOCATORIAS")
public class Evalconvocatorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Size(max = 1000)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicio;
    @Column(name = "FECHALIMITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechalimite;
    @Column(name = "FECHACIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacierre;
    @Size(max = 100)
    @Column(name = "OBJETIVOS")
    private String objetivos;
    @Size(max = 100)
    @Column(name = "CONCLUSIONES")
    private String conclusiones;
    @Column(name = "AGRUPADO")
    private Short agrupado;
    @JoinColumn(name = "NIVEL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Niveles nivel;
    @JoinColumn(name = "EVALVIGCONVOCATORIA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Evalvigconvocatorias evalvigconvocatoria;
    @JoinColumn(name = "ENFOQUE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Evalenfoques enfoque;
    @JoinColumn(name = "ESTRUCTURA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Estructuras estructura;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cargos cargo;

    public Evalconvocatorias() {
    }

    public Evalconvocatorias(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Evalconvocatorias(BigDecimal secuencia, String estado, Date fechainicio) {
        this.secuencia = secuencia;
        this.estado = estado;
        this.fechainicio = fechainicio;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechalimite() {
        return fechalimite;
    }

    public void setFechalimite(Date fechalimite) {
        this.fechalimite = fechalimite;
    }

    public Date getFechacierre() {
        return fechacierre;
    }

    public void setFechacierre(Date fechacierre) {
        this.fechacierre = fechacierre;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }

    public Short getAgrupado() {
        return agrupado;
    }

    public void setAgrupado(Short agrupado) {
        this.agrupado = agrupado;
    }

    public Niveles getNivel() {
        return nivel;
    }

    public void setNivel(Niveles nivel) {
        this.nivel = nivel;
    }

    public Evalvigconvocatorias getEvalvigconvocatoria() {
        return evalvigconvocatoria;
    }

    public void setEvalvigconvocatoria(Evalvigconvocatorias evalvigconvocatoria) {
        this.evalvigconvocatoria = evalvigconvocatoria;
    }

    public Evalenfoques getEnfoque() {
        return enfoque;
    }

    public void setEnfoque(Evalenfoques enfoque) {
        this.enfoque = enfoque;
    }

    public Estructuras getEstructura() {
        return estructura;
    }

    public void setEstructura(Estructuras estructura) {
        this.estructura = estructura;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof Evalconvocatorias)) {
            return false;
        }
        Evalconvocatorias other = (Evalconvocatorias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }
}
