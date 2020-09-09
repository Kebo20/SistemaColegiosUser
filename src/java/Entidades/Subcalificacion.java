/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "subcalificacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subcalificacion.findAll", query = "SELECT s FROM Subcalificacion s")
    , @NamedQuery(name = "Subcalificacion.findById", query = "SELECT s FROM Subcalificacion s WHERE s.id = :id")
    , @NamedQuery(name = "Subcalificacion.findByDescripcion", query = "SELECT s FROM Subcalificacion s WHERE s.descripcion = :descripcion")})
public class Subcalificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsubcalificacion")
    private List<Notas> notasList;
    @JoinColumn(name = "idcalificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Calificacion idcalificacion;

    public Subcalificacion() {
    }

    public Subcalificacion(Integer id) {
        this.id = id;
    }

    public Subcalificacion(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Notas> getNotasList() {
        return notasList;
    }

    public void setNotasList(List<Notas> notasList) {
        this.notasList = notasList;
    }

    public Calificacion getIdcalificacion() {
        return idcalificacion;
    }

    public void setIdcalificacion(Calificacion idcalificacion) {
        this.idcalificacion = idcalificacion;
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
        if (!(object instanceof Subcalificacion)) {
            return false;
        }
        Subcalificacion other = (Subcalificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Subcalificacion[ id=" + id + " ]";
    }
    
}
