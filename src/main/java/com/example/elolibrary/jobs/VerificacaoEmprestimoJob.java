package com.example.elolibrary.jobs;

import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import com.example.elolibrary.repository.EmprestimoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VerificacaoEmprestimoJob {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    /**
     * Marca os empréstimos que estão com status "EM_USO" como "ATRASADO" caso o dia de devolução dele for maior que o dia atual.
     * Roda diariamente.
     * */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void verificarEmprestimosVencidos() {
        LocalDate dataAtual = LocalDate.now();
        List<Emprestimo> emprestimosVencidos = emprestimoRepository.findByDataDevolucaoBeforeAndStatus(dataAtual, StatusEmprestimo.EM_USO);
        for (Emprestimo emprestimo : emprestimosVencidos) {
            emprestimo.setStatus(StatusEmprestimo.ATRASADO);
        }
        emprestimoRepository.saveAll(emprestimosVencidos);
    }


    /**
     * Apaga fisicamente todos os empréstimos marcados como "DEVOLVIDO"
     * Roda a cada 1º dia do mês
     * */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void deletarEmprestimosDevolvidos() {
        emprestimoRepository.deleteByStatus(StatusEmprestimo.DEVOLVIDO);
    }

}
