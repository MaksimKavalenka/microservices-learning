{{- define "storage-service.labels" }}
  labels:
    date: {{ now | htmlDate }}
    version: {{ .Chart.Version }}
{{- end }}
