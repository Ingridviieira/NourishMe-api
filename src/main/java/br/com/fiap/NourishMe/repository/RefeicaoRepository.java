package br.com.fiap.NourishMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.NourishMe.models.Refeicao;

public interface RefeicaoRepository  extends JpaRepository<Refeicao, Long> {
    Page<Refeicao> findByNomeContaining(String busca, Pageable page);
    
}
