package uca.es.iw.data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer {

    private final UserRepository userRepository;
    private final RecursosRepository recursosRepository;
    private final PonderacionesRepository ponderacionesRepository;
    private final ProyectoRepository proyectoRepository;

    public DatabaseInitializer(UserRepository userRepository,
                               RecursosRepository recursosRepository,
                               PonderacionesRepository ponderacionesRepository,
                               ProyectoRepository proyectoRepository) {
        this.userRepository = userRepository;
        this.recursosRepository = recursosRepository;
        this.ponderacionesRepository = ponderacionesRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Crear usuario "user" con rol USER
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setName("Usuario");
            normalUser.setEmail("hola@adios.com");
            normalUser.setHashedPassword("$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe"); // Contraseña "user" encriptada
            normalUser.setRoles(Set.of(Role.USER)); // Asignar rol USER
            normalUser.setProfilePicture(null);
            userRepository.save(normalUser);

            // Crear usuario "admin" con múltiples roles
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setName("Administrador");
            adminUser.setEmail("admin@admin.adm");
            adminUser.setHashedPassword("$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV."); // Contraseña "admin" encriptada
            adminUser.setRoles(Set.of(Role.USER, Role.ADMIN, Role.CIO, Role.PROMOTOR, Role.OTP)); // Asignar múltiples roles
            adminUser.setProfilePicture(null);
            userRepository.save(adminUser);

            // Generar otros datos aleatorios...
            for (int i = 1; i <= 20; i++) {
                User randomUser = new User();
                randomUser.setUsername("usuario" + i);
                randomUser.setName("Usuario " + i);
                randomUser.setEmail("usuario" + i + "@example.com");
                randomUser.setHashedPassword("$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe");
                randomUser.setProfilePicture(null);

                // Asignar roles aleatorios
                Set<Role> roles = Set.of(
                        Role.values()[new Random().nextInt(Role.values().length)]
                );
                randomUser.setRoles(roles);

                userRepository.save(randomUser);
            }

            // Crear entidad Recursos
            Recursos recursos = new Recursos();
            recursos.setPresupuestoRestante(5000000.0);
            recursos.setPresupuestoTotal(5000000.0);
            recursos.setRecursosHumanos(50);
            recursos.setRecursosHumanosRestantes(50);
            recursosRepository.save(recursos);

            // Crear entidad Ponderaciones
            Ponderaciones ponderaciones = new Ponderaciones();
            ponderaciones.setPonDisponibilidad(0.4);
            ponderaciones.setPonOportunidad(0.3);
            ponderaciones.setPonTecnica(0.3);
            ponderacionesRepository.save(ponderaciones);

            // Generar datos aleatorios para proyectos
            for (int i = 1; i <= 10; i++) {
                Proyecto proyecto = new Proyecto();
                proyecto.setTitulo("Proyecto " + i);
                proyecto.setNombreCorto("P" + i);
                proyecto.setMemoria(new byte[0]); // Placeholder
                proyecto.setNombreSolicitante("Solicitante " + i);
                proyecto.setCorreoSolicitante("solicitante" + i + "@example.com");
                proyecto.setUnidadSolicitante("Unidad " + i);
                proyecto.setPromotor("Promotor " + i);
                proyecto.setImportancia(new Random().nextInt(5) + 1);
                proyecto.setInteresados("Interesado A, Interesado B");
                proyecto.setFinanciacion(10000.0 + new Random().nextDouble() * 90000.0);
                proyecto.setAoe1(new Random().nextBoolean());
                proyecto.setAoe2(new Random().nextBoolean());
                proyecto.setAoe3(new Random().nextBoolean());
                proyecto.setAoe4(new Random().nextBoolean());
                proyecto.setAoe5(new Random().nextBoolean());
                proyecto.setAoe6(new Random().nextBoolean());
                proyecto.setAoe7(new Random().nextBoolean());
                proyecto.setAlcance("Alcance del proyecto " + i);
                proyecto.setFechaObjetivo(LocalDate.now().plusMonths(new Random().nextInt(12)));
                proyecto.setNormativa("Normativa " + i);
                proyecto.setEspecificaciones(new byte[0]); // Placeholder
                proyecto.setPresupuestos(new byte[0]); // Placeholder
                proyecto.setFechaCreado(LocalDate.now());
                proyecto.setCalOportunidad(new Random().nextDouble() * 10);
                proyecto.setCalTecnica(new Random().nextDouble() * 10);
                proyecto.setCalDisponibilidad(new Random().nextDouble() * 10);
                proyecto.setCalFinanciacion(new Random().nextDouble() * 10);
                proyecto.setCalFinal(null);
                proyecto.setEstado("ACTIVO");

                proyectoRepository.save(proyecto);
            }
        };
    }
}

