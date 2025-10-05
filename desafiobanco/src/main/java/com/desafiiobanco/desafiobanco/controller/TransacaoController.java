package com.desafiiobanco.desafiobanco.controller;

import com.desafiiobanco.desafiobanco.record.Estatistica;
import com.desafiiobanco.desafiobanco.record.Transacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    private final List<Transacao> transacoes = new CopyOnWriteArrayList<>();
    @PostMapping("/transacao")
    public ResponseEntity<Void> criarTransacao(@RequestBody Transacao transacao){
        var adicionada=adicionarTransacao(transacao);
        if(adicionada){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return  new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    private boolean adicionarTransacao(Transacao transacao){
        if(transacao.valor() < 0 || transacao.dataHora().isAfter(OffsetDateTime.now())){
            return false;
        }
        transacoes.add(transacao);
        return  true;
    }
    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deletarTransacao (){
        transacoes.clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/estatistica")
    public ResponseEntity<Estatistica> obterEstatistica(){
        OffsetDateTime agora = OffsetDateTime.now();
        DoubleSummaryStatistics status60s=transacoes.stream()
                .filter(t -> t.dataHora().isAfter(agora.minusSeconds(60)))
                .mapToDouble(Transacao::valor)
                .summaryStatistics();

        Estatistica estatisticas = new Estatistica(
                status60s.getCount(),
                status60s.getSum(),
                status60s.getAverage(),
                status60s.getCount() == 0 ? 0 : status60s.getMin(),
                status60s.getCount() == 0 ? 0 : status60s.getMax()
        );
        return ResponseEntity.ok(estatisticas);
    }
}
