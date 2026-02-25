FROM alpine:latest AS builder

RUN mkdir -p /tmp/tempo/blocks /tmp/tempo/wal /tmp/tempo/generator/wal && \
    chown -R 10001:10001 /tmp/tempo

FROM grafana/tempo:2.10.0

COPY --from=builder --chown=10001:10001 /tmp/tempo /tmp/tempo
COPY observability/tempo.yml /etc/tempo/tempo.yml

CMD ["-config.file=/etc/tempo/tempo.yml"]