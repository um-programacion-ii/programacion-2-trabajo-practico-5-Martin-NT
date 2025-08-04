package um.example.TP5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import um.example.TP5.entity.Departamento;
import um.example.TP5.service.DepartamentoService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DepartamentoController.class)
public class DepartamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Departamento departamento1;
    private Departamento departamento2;

    @BeforeEach
    void setUp() {
        departamento1 = new Departamento();
        departamento1.setId(1L);
        departamento1.setNombre("IT");
        departamento1.setDescripcion("Departamento de Tecnología");

        departamento2 = new Departamento();
        departamento2.setId(2L);
        departamento2.setNombre("RRHH");
        departamento2.setDescripcion("Departamento de Recursos Humanos");
    }

    @Test
    void obtenerTodos_debeRetornarLista() throws Exception {
        List<Departamento> departamentos = Arrays.asList(departamento1, departamento2);
        BDDMockito.given(departamentoService.obtenerTodos()).willReturn(departamentos);

        mockMvc.perform(get("/api/departamentos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].nombre", is("IT")))
            .andExpect(jsonPath("$[1].nombre", is("RRHH")));
    }

    @Test
    void obtenerPorId_debeRetornarDepartamento() throws Exception {
        BDDMockito.given(departamentoService.buscarPorId(1L)).willReturn(departamento1);

        mockMvc.perform(get("/api/departamentos/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(departamento1.getId()))
            .andExpect(jsonPath("$.nombre").value("IT"));
    }

    @Test
    void obtenerPorNombre_debeRetornarDepartamento() throws Exception {
        BDDMockito.given(departamentoService.buscarPorNombre("IT")).willReturn(departamento1);

        mockMvc.perform(get("/api/departamentos/nombre/{nombre}", "IT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("IT"));
    }


    @Test
    void crearDepartamento_debeRetornarDepartamentoCreado() throws Exception {
        BDDMockito.given(departamentoService.guardar(any(Departamento.class))).willReturn(departamento1);

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento1)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("IT"));
    }

    @Test
    void actualizarDepartamento_debeRetornarDepartamentoActualizado() throws Exception {
        departamento1.setNombre("ITT");
        BDDMockito.given(departamentoService.actualizar(anyLong(), any(Departamento.class))).willReturn(departamento1);

        mockMvc.perform(put("/api/departamentos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("ITT"));
    }

    @Test
    void eliminarDepartamento_debeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/departamentos/{id}", 1L))
            .andExpect(status().isNoContent());
    }

}
