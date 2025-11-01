package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByAtivoTrue();

    Optional<Profissional> findByRegistroClasse(String registroClasse);

    boolean existsByRegistroClasse(String registroClasse);

    @Query("SELECT p FROM Profissional p JOIN p.especialidades e WHERE e.id = :especialidadeId AND p.ativo = true")
    List<Profissional> findByEspecialidadeId(@Param("especialidadeId") Long especialidadeId);

    @Query("SELECT p FROM Profissional p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.ativo = true")
    List<Profissional> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);

}
