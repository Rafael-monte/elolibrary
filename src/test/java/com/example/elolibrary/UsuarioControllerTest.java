package com.example.elolibrary;

import com.example.elolibrary.controller.UsuarioController;
import com.example.elolibrary.dto.input.UpdateUsuarioInputDto;
import com.example.elolibrary.dto.input.UsuarioInputDto;
import com.example.elolibrary.dto.output.UsuarioOutputDto;
import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void aoChamarEndpointGetAll_RetornarTodosOsUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);

        when(usuarioService.findAllByPage(pageable)).thenReturn(this.mockUsuarios());

        ResponseEntity<Page> result = (ResponseEntity<Page>) usuarioController.findAllByPage(0, 10);
        assertNotNull(result.getBody().getContent());
        assertEquals(2, result.getBody().getContent().size());
    }

    @Test
    void aoChamarEndpointCriarUsuario_RetornarCreated() {
        UsuarioInputDto usuario = new UsuarioInputDto();
        usuario.setEmail("teste@elolibrary.com");
        usuario.setNome("Teste");
        usuario.setSenha("123456");
        usuario.setTelefone("(44) 99732-7090");
        ResponseEntity<?> result = (ResponseEntity<UsuarioOutputDto>) usuarioController.save(usuario);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void aoChamarEndpointCriarUsuarioComEmailInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        UsuarioInputDto usuario = new UsuarioInputDto();
        usuario.setEmail("teste.com");
        usuario.setNome("Teste email invalido");
        usuario.setSenha("123456");
        usuario.setTelefone("(44) 99732-7090");
        String usuarioJson = new ObjectMapper().writeValueAsString(usuario);
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoChamarEndpointCriarUsuarioComTelefoneInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        UsuarioInputDto usuario = new UsuarioInputDto();
        usuario.setEmail("teste@elolibrary.com");
        usuario.setNome("Teste telefone invalido");
        usuario.setSenha("123456");
        usuario.setTelefone("(44) 99732-70908479328");
        String usuarioJson = new ObjectMapper().writeValueAsString(usuario);
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoChamarEndpointCriarUsuarioComNomeNuloESenhaVazia_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        UsuarioInputDto usuario = new UsuarioInputDto();
        usuario.setEmail("teste@elolibrary.com");
        usuario.setNome(null);
        usuario.setSenha("");
        usuario.setTelefone("(44) 99732-7090");
        String usuarioJson = new ObjectMapper().writeValueAsString(usuario);
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoEnviarInformacoesAtualizadasValidas_AtualizarUsuario() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Usuario usuarioAtualizado = new Usuario(1L,
                "Usuario Atualizado",
                "teste@elolibrary.com",
                LocalDate.parse("2024-05-03", dateTimeFormatter),
                "(44) 99843-0809",
                "password",
                Role.USER,
                true
        );

        UpdateUsuarioInputDto updateUsuarioInputDto = new UpdateUsuarioInputDto();
        updateUsuarioInputDto.setNome("Usuario Atualizado");
        updateUsuarioInputDto.setSenha("password");
        updateUsuarioInputDto.setTelefone("(44) 99843-0809");

        UsuarioOutputDto outputDto = new UsuarioOutputDto();
        outputDto.wrap(usuarioAtualizado);
        when(usuarioService.update(usuarioAtualizado, 1L)).thenReturn(new UsuarioOutputDto().wrap(usuarioAtualizado));

        ResponseEntity<UsuarioOutputDto> response = (ResponseEntity<UsuarioOutputDto>) usuarioController.update(1L, updateUsuarioInputDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void aoEnviarInformacoesAtualizadasComTelefoneInvalido_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        UpdateUsuarioInputDto updateUsuarioInputDto = new UpdateUsuarioInputDto();
        updateUsuarioInputDto.setNome("Usuario Atualizado");
        updateUsuarioInputDto.setSenha("password");
        updateUsuarioInputDto.setTelefone("(44) 99843-08090890");

        String usuarioJson = new ObjectMapper().writeValueAsString(updateUsuarioInputDto);

        mockMvc.perform(put("/api/usuarios/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aoEnviarInformacoesAtualizadasComCamposNulosEVazios_RetornarErro() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        UpdateUsuarioInputDto updateUsuarioInputDto = new UpdateUsuarioInputDto();
        updateUsuarioInputDto.setNome(null);
        updateUsuarioInputDto.setSenha("");
        updateUsuarioInputDto.setTelefone("(44) 99843-08090890");

        String usuarioJson = new ObjectMapper().writeValueAsString(updateUsuarioInputDto);

        mockMvc.perform(put("/api/usuarios/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isBadRequest());
    }

    private Page<OutputDto<Usuario>> mockUsuarios() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Usuario> usuarios = List.of(
                new Usuario(1L,
                        "Teste 1",
                        "teste1@elolibrary.com",
                        LocalDate.parse("2024-05-03", dateTimeFormatter),
                        "(44) 99831-7429",
                        "testeteste",
                        Role.USER,
                        true
                ),
                new Usuario(2L,
                        "Teste 2",
                        "teste2@elolibrary.com",
                        LocalDate.parse("2024-05-07", dateTimeFormatter),
                        "(44) 99931-7529",
                        "testetest2e",
                        Role.USER,
                        true
                )
        );
        return new PageImpl<OutputDto<Usuario>>(usuarios.stream().map(usr -> new UsuarioOutputDto().wrap(usr)).toList());
    }
}
