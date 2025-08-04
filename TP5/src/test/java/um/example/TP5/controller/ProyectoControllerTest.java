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
import um.example.TP5.entity.Proyecto;
import um.example.TP5.service.ProyectoService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProyectoController.class)
public class ProyectoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoService proyectoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        proyecto1 = new Proyecto();
        proyecto1.setId(1L);
        proyecto1.setNombre("Proyecto Alpha");
        proyecto1.setDescripcion("Sistema de gestión interna");
        proyecto1.setFechaInicio(LocalDate.of(2024, 1, 1));
        proyecto1.setFechaFin(LocalDate.of(2024, 12, 31));

        proyecto2 = new Proyecto();
        proyecto2.setId(2L);
        proyecto2.setNombre("Proyecto Beta");
        proyecto2.setDescripcion("Sistema de gestión externa");
        proyecto2.setFechaInicio(LocalDate.of(2024, 1, 1));
        proyecto2.setFechaFin(LocalDate.of(2024, 12, 31));
    }

    @Test
    void obtenerTodos_debeRetornarLista() throws Exception {
        List<Proyecto> proyectos = Arrays.asList(proyecto1, proyecto2);
        BDDMockito.given(proyectoService.obtenerTodos()).willReturn(proyectos);

        mockMvc.perform(get("/api/proyectos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].nombre", is("Proyecto Alpha")))
            .andExpect(jsonPath("$[1].nombre", is("Proyecto Beta")));
    }

    @Test
    void obtenerPorId_debeRetornarProyecto() throws Exception {
        BDDMockito.given(proyectoService.buscarPorId(1L)).willReturn(proyecto1);

        mockMvc.perform(get("/api/proyectos/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(proyecto1.getId()))
            .andExpect(jsonPath("$.nombre").value("Proyecto Alpha"));
    }

    @Test
    void obtenerPorNombre_debeRetornarProyecto() throws Exception {
        BDDMockito.given(proyectoService.buscarPorNombre("Proyecto Alpha")).willReturn(proyecto1);

        mockMvc.perform(get("/api/proyectos/nombre/{nombre}", "Proyecto Alpha"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Proyecto Alpha"));
    }

    @Test
    void crearProyecto_debeRetornarProyectoCreado() throws Exception {
        BDDMockito.given(proyectoService.guardar(any(Proyecto.class))).willReturn(proyecto1);

        mockMvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto1)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Proyecto Alpha"));
    }

    @Test
    void actualizarProyecto_debeRetornarProyectoActualizado() throws Exception {
        proyecto1.setNombre("Proyecto Alfa");
        BDDMockito.given(proyectoService.actualizar(anyLong(), any(Proyecto.class))).willReturn(proyecto1);

        mockMvc.perform(put("/api/proyectos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Proyecto Alfa"));
    }

    @Test
    void eliminarProyecto_debeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/proyectos/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerProyectosActivos_debeRetornarLista() throws Exception {
        List<Proyecto> activos = List.of(proyecto1);
        BDDMockito.given(proyectoService.buscarProyectosActivos()).willReturn(activos);

        mockMvc.perform(get("/api/proyectos/activos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Proyecto Alpha"));
    }


}
