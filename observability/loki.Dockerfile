FROM alpine:latest AS builder

RUN mkdir -p /loki/chunks /loki/rules

FROM grafana/loki:3.5.10

COPY --from=builder --chown=10001:10001 /loki /loki
COPY observability/loki-config.yml /etc/loki/local-config.yaml

USER 10001