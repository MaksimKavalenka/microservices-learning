{{- define "gateway.labels" }}
  labels:
    date: {{ now | htmlDate }}
    version: {{ .Chart.Version }}
{{- end }}
