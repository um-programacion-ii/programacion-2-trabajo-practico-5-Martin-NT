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
import um.example.TP5.entity.Empleado;
import um.example.TP5.service.EmpleadoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Empleado empleado1;
    private Empleado empleado2;

    @BeforeEach
    void setUp() {
        empleado1 = new Empleado();
        empleado1.setId(1L);
        empleado1.setNombre("Martin");
        empleado1.setApellido("Navarro");
        empleado1.setEmail("martin.navarro@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("50000"));

        empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombre("Martina");
        empleado2.setApellido("Rizzotti");
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("60000"));
    }

    @Test
    void obtenerTodos_debeRetornarLista() throws Exception {
        List<Empleado> empleados = Arrays.asList(empleado1, empleado2);
        BDDMockito.given(empleadoService.obtenerTodos()).willReturn(empleados);

        mockMvc.perform(get("/api/empleados"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].nombre", is("Martin")))
            .andExpect(jsonPath("$[1].nombre", is("Martina")));
    }

    @Test
    void obtenerPorId_debeRetornarEmpleado() throws Exception {
        BDDMockito.given(empleadoService.buscarPorId(1L)).willReturn(empleado1);

        mockMvc.perform(get("/api/empleados/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(empleado1.getId()))
            .andExpect(jsonPath("$.nombre").value("Martin"));
    }

    @Test
    void crearEmpleado_debeRetornarEmpleadoCreado() throws Exception {
        BDDMockito.given(empleadoService.guardar(any(Empleado.class))).willReturn(empleado1);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado1)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Martin"));
    }

    @Test
    void actualizarEmpleado_debeRetornarEmpleadoActualizado() throws Exception {
        empleado1.setNombre("Martin Alberto");
        BDDMockito.given(empleadoService.actualizar(anyLong(), any(Empleado.class))).willReturn(empleado1);

        mockMvc.perform(put("/api/empleados/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Martin Alberto"));
    }

    @Test
    void eliminarEmpleado_debeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/empleados/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorDepartamento_debeRetornarLista() throws Exception {
        List<Empleado> empleados = List.of(empleado1);
        BDDMockito.given(empleadoService.buscarPorDepartamento("IT")).willReturn(empleados);

        mockMvc.perform(get("/api/empleados/departamento/{nombre}", "IT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Martin"));
    }

    @Test
    void buscarPorRangoSalario_debeRetornarLista() throws Exception {
        List<Empleado> empleados = List.of(empleado2);
        BDDMockito.given(empleadoService.buscarPorRangoSalario(new BigDecimal("50000"), new BigDecimal("70000")))
            .willReturn(empleados);

        mockMvc.perform(get("/api/empleados/salario")
                .param("min", "50000")
                .param("max", "70000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Martina"));
    }

    @Test
    void obtenerSalarioPromedioPorDepartamento_debeRetornarPromedio() throws Exception {
        BigDecimal promedio = new BigDecimal("55000");
        BDDMockito.given(empleadoService.obtenerSalarioPromedioPorDepartamento(1L)).willReturn(promedio);

        mockMvc.perform(get("/api/empleados/salario/promedio/{departamentoId}", 1L))
            .andExpect(status().isOk())
            .andExpect(content().string("55000"));
    }
}
