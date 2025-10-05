package com.desafiiobanco.desafiobanco.record;

import java.time.OffsetDateTime;

public record Transacao(
        long valor,
        OffsetDateTime dataHora
) {
}
