FROM grafana/grafana:11.6.11
COPY observability/grafana/datasources /etc/grafana/provisioning/datasources
COPY observability/grafana/dashboards/dashboards.yml /etc/grafana/provisioning/dashboards/dashboards.yml
COPY observability/grafana/dashboards/*.json /var/lib/grafana/dashboards/
