# Microservices Learning. Helm Charts
This repository contains packaged Helm charts provided by Maksim Kavalenka.

## Add Repository
`$ helm repo add microservices-learning https://maksimkavalenka.github.io/microservices-learning`  
`$ helm repo update`

## Install Packages
`$ helm install infrastructure microservices-learning/infrastructure`  
`$ helm install song-service microservices-learning/song-service`  
`$ helm install resource-service microservices-learning/resource-service`

## Uninstall Packages
`$ helm uninstall resource-service`  
`$ helm uninstall song-service`  
`$ helm uninstall infrastructure`
