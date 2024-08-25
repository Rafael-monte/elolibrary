package com.example.elolibrary;

import com.example.elolibrary.controller.LivroController;
import com.example.elolibrary.controller.UsuarioController;
import com.example.elolibrary.dto.input.LivroInputDto;
import com.example.elolibrary.dto.input.UsuarioInputDto;
import com.example.elolibrary.dto.output.LivroOutputDto;
import com.example.elolibrary.dto.output.UsuarioOutputDto;
import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.service.LivroService;
import com.example.elolibrary.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class LivroControllerTest {

    @InjectMocks
    private LivroController livroController;

    @Mock
    private LivroService livroService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void aoChamarEndpointGetAll_RetornarTodosOsLivros() {
        Pageable pageable = PageRequest.of(0, 10);

        when(livroService.findAllByPage(pageable)).thenReturn(this.mockLivros());

        ResponseEntity<Page> result = (ResponseEntity<Page>) livroController.findAllByPage(0, 10);
        assertNotNull(result.getBody().getContent());
        assertEquals(3, result.getBody().getContent().size());
    }

    @Test
    void aoChamarEndpointCriarLivro_RetornarCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LivroInputDto livroInputDto = new LivroInputDto();
        livroInputDto.setTitulo("Livro 1");
        livroInputDto.setAutor("Testerson da Silva");
        livroInputDto.setIsbn("9783127323220");
        livroInputDto.setCategoria("Ação");
        livroInputDto.setDataPublicacao(LocalDate.parse("2023-05-21", formatter));
        ResponseEntity<?> result = (ResponseEntity<UsuarioOutputDto>) livroController.save(livroInputDto);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void aoChamarEndpointCriarLivroComISBNInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LivroInputDto livroInputDto = new LivroInputDto();
        livroInputDto.setTitulo("Livro 1");
        livroInputDto.setAutor("Testerson da Silva");
        livroInputDto.setIsbn("97831273232209308043");
        livroInputDto.setCategoria("Ação");
        livroInputDto.setDataPublicacao(LocalDate.parse("2023-05-21", formatter));
        String livroJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(livroInputDto);
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoChamarEndpointCriarLivroComCamposInvalidos_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LivroInputDto livroInputDto = new LivroInputDto();
        livroInputDto.setTitulo("");
        livroInputDto.setAutor(null);
        livroInputDto.setIsbn("9783127323207");
        livroInputDto.setCategoria("Ação");
        livroInputDto.setDataPublicacao(LocalDate.parse("2023-05-21", formatter));
        String livroJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(livroInputDto);
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andExpect(status().isBadRequest());
    }


    private Page<OutputDto<Livro>> mockLivros() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Livro> livros = List.of(
           new Livro(1L, "Teste Livro", "Teste teste", "9783127323207", LocalDate.parse("2024-04-03", dateTimeFormatter), "drama", true),
           new Livro(2L, "Teste Livro 2", "Teste teste 2", "9783127323208", LocalDate.parse("2024-05-03", dateTimeFormatter), "drama", true),
           new Livro(3L, "A volta dos que testaram", "Teste juniot", "9783127323209", LocalDate.parse("2024-08-04", dateTimeFormatter), "romance", true)
        );
        return new PageImpl<OutputDto<Livro>>(livros.stream().map(livro -> new LivroOutputDto().wrap(livro)).toList());
    }
}
