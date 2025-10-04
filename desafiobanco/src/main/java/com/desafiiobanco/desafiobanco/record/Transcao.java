package com.desafiiobanco.desafiobanco.record;

import java.time.OffsetDateTime;

public record Transcao(
        long valor,
        OffsetDateTime dataHora
) {
}
