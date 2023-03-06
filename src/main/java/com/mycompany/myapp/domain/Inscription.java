package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.DemandeStand;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "partenaire")
    private String partenaire;

    @Column(name = "non")
    private String non;

    @Column(name = "prenon")
    private String prenon;

    @Column(name = "adresse_professionnelle")
    private String adresseProfessionnelle;

    @Column(name = "telephone_professionnelle")
    private String telephoneProfessionnelle;

    @Column(name = "email_professionnelle")
    private String emailProfessionnelle;

    @Enumerated(EnumType.STRING)
    @Column(name = "demande_stand")
    private DemandeStand demandeStand;

    @Column(name = "taille_stand")
    private String tailleStand;

    @Column(name = "autres")
    private String autres;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartenaire() {
        return this.partenaire;
    }

    public Inscription partenaire(String partenaire) {
        this.setPartenaire(partenaire);
        return this;
    }

    public void setPartenaire(String partenaire) {
        this.partenaire = partenaire;
    }

    public String getNon() {
        return this.non;
    }

    public Inscription non(String non) {
        this.setNon(non);
        return this;
    }

    public void setNon(String non) {
        this.non = non;
    }

    public String getPrenon() {
        return this.prenon;
    }

    public Inscription prenon(String prenon) {
        this.setPrenon(prenon);
        return this;
    }

    public void setPrenon(String prenon) {
        this.prenon = prenon;
    }

    public String getAdresseProfessionnelle() {
        return this.adresseProfessionnelle;
    }

    public Inscription adresseProfessionnelle(String adresseProfessionnelle) {
        this.setAdresseProfessionnelle(adresseProfessionnelle);
        return this;
    }

    public void setAdresseProfessionnelle(String adresseProfessionnelle) {
        this.adresseProfessionnelle = adresseProfessionnelle;
    }

    public String getTelephoneProfessionnelle() {
        return this.telephoneProfessionnelle;
    }

    public Inscription telephoneProfessionnelle(String telephoneProfessionnelle) {
        this.setTelephoneProfessionnelle(telephoneProfessionnelle);
        return this;
    }

    public void setTelephoneProfessionnelle(String telephoneProfessionnelle) {
        this.telephoneProfessionnelle = telephoneProfessionnelle;
    }

    public String getEmailProfessionnelle() {
        return this.emailProfessionnelle;
    }

    public Inscription emailProfessionnelle(String emailProfessionnelle) {
        this.setEmailProfessionnelle(emailProfessionnelle);
        return this;
    }

    public void setEmailProfessionnelle(String emailProfessionnelle) {
        this.emailProfessionnelle = emailProfessionnelle;
    }

    public DemandeStand getDemandeStand() {
        return this.demandeStand;
    }

    public Inscription demandeStand(DemandeStand demandeStand) {
        this.setDemandeStand(demandeStand);
        return this;
    }

    public void setDemandeStand(DemandeStand demandeStand) {
        this.demandeStand = demandeStand;
    }

    public String getTailleStand() {
        return this.tailleStand;
    }

    public Inscription tailleStand(String tailleStand) {
        this.setTailleStand(tailleStand);
        return this;
    }

    public void setTailleStand(String tailleStand) {
        this.tailleStand = tailleStand;
    }

    public String getAutres() {
        return this.autres;
    }

    public Inscription autres(String autres) {
        this.setAutres(autres);
        return this;
    }

    public void setAutres(String autres) {
        this.autres = autres;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", partenaire='" + getPartenaire() + "'" +
            ", non='" + getNon() + "'" +
            ", prenon='" + getPrenon() + "'" +
            ", adresseProfessionnelle='" + getAdresseProfessionnelle() + "'" +
            ", telephoneProfessionnelle='" + getTelephoneProfessionnelle() + "'" +
            ", emailProfessionnelle='" + getEmailProfessionnelle() + "'" +
            ", demandeStand='" + getDemandeStand() + "'" +
            ", tailleStand='" + getTailleStand() + "'" +
            ", autres='" + getAutres() + "'" +
            "}";
    }
}
