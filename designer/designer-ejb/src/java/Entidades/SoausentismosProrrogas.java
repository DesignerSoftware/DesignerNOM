package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

@Entity
@SqlResultSetMapping(
        name = "prorrogas",
        entities = {
    @EntityResult(
            entityClass = SoausentismosProrrogas.class,
            fields = {
        @FieldResult(name = "id" , column = "ID"),
        @FieldResult(name = "causa", column = "CAUSA"),
        @FieldResult(name = "codigo", column = "CODIGO"),
        @FieldResult(name = "diagnostico", column = "DIAGNOSTICO"),
        @FieldResult(name = "fechainicial", column = "FECHAINICIAL"),
        @FieldResult(name = "fechafinal", column = "FECHAFINAL"),
        @FieldResult(name = "dia", column = "DIA"),
        @FieldResult(name = "hora", column = "HORA"),
        @FieldResult(name = "numerocertificado", column = "NUMEROCERTIFICADO"),
        @FieldResult(name = "fechamasuno", column = "FECHAMASUNO"),
            })
})
public class SoausentismosProrrogas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String causa;
    private String codigo;
    private String diagnostico;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechainicial;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechafinal;
    private BigInteger dia;
    private BigInteger hora;
    private String numerocertificado;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechamasuno;
    

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
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

    public BigInteger getDia() {
        return dia;
    }

    public void setDia(BigInteger dia) {
        this.dia = dia;
    }

    public BigInteger getHora() {
        return hora;
    }

    public void setHora(BigInteger hora) {
        this.hora = hora;
    }

    public String getNumerocertificado() {
        return numerocertificado;
    }

    public void setNumerocertificado(String numerocertificado) {
        this.numerocertificado = numerocertificado;
    }

    public Date getFechamasuno() {
        return fechamasuno;
    }

    public void setFechamasuno(Date fechamasuno) {
        this.fechamasuno = fechamasuno;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SoausentismosProrrogas)) {
            return false;
        }
        SoausentismosProrrogas other = (SoausentismosProrrogas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.SoausentismosProrrogas[ id=" + id + " ]";
    }
}
