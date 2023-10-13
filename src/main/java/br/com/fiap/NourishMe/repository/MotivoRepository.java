package br.com.fiap.NourishMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.NourishMe.models.Motivo;

public interface MotivoRepository extends JpaRepository<Motivo, Long>{
    
}
