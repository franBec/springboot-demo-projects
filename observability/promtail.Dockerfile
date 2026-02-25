FROM grafana/promtail:3.5.10
COPY observability/promtail-config.yml /etc/promtail/config.yml
