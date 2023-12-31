package br.com.fiap.NourishMe.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.fiap.NourishMe.models.Motivo;
import br.com.fiap.NourishMe.models.Refeicao;
import br.com.fiap.NourishMe.repository.MotivoRepository;
import br.com.fiap.NourishMe.repository.RefeicaoRepository;

@Configuration
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner{
    
    @Autowired
    RefeicaoRepository refeicaoRepository;

    @Autowired
    MotivoRepository motivoRepository;

    @Override
    public void run(String... args) throws Exception{

        Motivo m1 = new Motivo(1L, "Me senti mal depois de ir mal em uma prova");
        Motivo m2 = new Motivo(2L, "Almocei no horario certo hoje e estou me sentindo bem");
        motivoRepository.saveAll(List.of(m1,m2));

        refeicaoRepository.saveAll(List.of(
            Refeicao.builder().nome("Clara").nomeRefeicao("Lanche da Tarde").motivo(m1).build(),
            Refeicao.builder().id(2l).nome("Julio").nomeRefeicao("Alomoço").motivo(m2).build()

        ));
    
    }
}
