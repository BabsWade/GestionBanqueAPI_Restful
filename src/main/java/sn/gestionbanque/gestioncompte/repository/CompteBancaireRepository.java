package sn.gestionbanque.gestioncompte.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.gestionbanque.gestioncompte.model.CompteBancaire;

public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Long> {
}
