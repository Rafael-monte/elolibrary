package com.example.elolibrary;

import com.example.elolibrary.controller.LivroController;
import com.example.elolibrary.dto.input.EmprestimoInputDto;
import com.example.elolibrary.dto.input.EmprestimoUpdateInputDto;
import com.example.elolibrary.dto.input.UpdateUsuarioInputDto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import com.example.elolibrary.repository.EmprestimoRepository;
import com.example.elolibrary.repository.LivroRepository;
import com.example.elolibrary.repository.UsuarioRepository;
import com.example.elolibrary.service.EmprestimoService;
import com.example.elolibrary.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmprestimoControllerTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void aoEnviarInformacoesComDataDevolucaoMenorQueDataAtual_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        EmprestimoInputDto emprestimoInputDto = new EmprestimoInputDto();
        emprestimoInputDto.setIsbnLivro("978-0451524935");
        emprestimoInputDto.setEmailUsuario("luiz.almeida@example.com");
        emprestimoInputDto.setDataDevolucao(LocalDate.parse("2002-07-06", dateTimeFormatter));
        String emprestimoJSON = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(emprestimoInputDto);
        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoEnviarInformacoesComISBNInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        EmprestimoInputDto emprestimoInputDto = new EmprestimoInputDto();
        emprestimoInputDto.setIsbnLivro("978-0451524935948039084");
        emprestimoInputDto.setEmailUsuario("luiz.almeida@example.com");
        emprestimoInputDto.setDataDevolucao(LocalDate.parse("2500-07-06", dateTimeFormatter));
        String emprestimoJSON = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(emprestimoInputDto);
        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoEnviarInformacoesComEmailInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        EmprestimoInputDto emprestimoInputDto = new EmprestimoInputDto();
        emprestimoInputDto.setIsbnLivro("978-0451524935");
        emprestimoInputDto.setEmailUsuario("luiz.almeida@@examplecom");
        emprestimoInputDto.setDataDevolucao(LocalDate.parse("2500-07-06", dateTimeFormatter));
        String emprestimoJSON = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(emprestimoInputDto);
        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void aoEnviarInformacoesNulasEVazias_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        EmprestimoInputDto emprestimoInputDto = new EmprestimoInputDto();
        emprestimoInputDto.setIsbnLivro(null);
        emprestimoInputDto.setEmailUsuario("");
        emprestimoInputDto.setDataDevolucao(LocalDate.parse("2500-07-06", dateTimeFormatter));
        String emprestimoJSON = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(emprestimoInputDto);
        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoEnviarInformacoesComDatasInvalidas_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        EmprestimoUpdateInputDto emprestimoUpdateInputDto = new EmprestimoUpdateInputDto();
        emprestimoUpdateInputDto.setDataEmprestimo(LocalDate.parse("2025-07-06", dateTimeFormatter));
        emprestimoUpdateInputDto.setDataDevolucao(LocalDate.parse("2019-07-06", dateTimeFormatter));
        emprestimoUpdateInputDto.setStatus(StatusEmprestimo.ATRASADO);

        String emprestimoJSON = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(emprestimoUpdateInputDto);
        mockMvc.perform(put("/api/emprestimos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoJSON))
                .andExpect(status().isBadRequest());
    }

}
