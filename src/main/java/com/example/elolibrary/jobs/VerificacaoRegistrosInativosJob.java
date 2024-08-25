package com.example.elolibrary.jobs;

import com.example.elolibrary.repository.LivroRepository;
import com.example.elolibrary.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VerificacaoRegistrosInativosJob {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    /**
     * Apaga fisicamente todos os usuarios com a flag ativo definida como "false"
     * Roda a cada 1º dia do mês
     * */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void deletarFisicamenteUsuariosInativos() {
        usuarioRepository.deleteByAtivoFalse();
    }

    /**
     * Apaga fisicamente todos os livros com a flag ativo definida como "false"
     * Roda a cada 1º dia do mês
     * */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void deletarFisicamenteLivrosInativos() {
        livroRepository.deleteByAtivoFalse();
    }
}
