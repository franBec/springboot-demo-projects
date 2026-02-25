FROM prom/prometheus:v3.9.1
COPY observability/prometheus.yml /etc/prometheus/prometheus.yml
