package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    List<Especialidade> findByAtivoTrue();

    Optional<Especialidade> findByNome(String nome);

    boolean existsByNome(String nome);

}
