{{- define "authorization-server.labels" }}
  labels:
    date: {{ now | htmlDate }}
    version: {{ .Chart.Version }}
{{- end }}
