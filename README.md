# Microservices Learning. Helm Charts
This repository contains packaged Helm charts provided by Maksim Kavalenka.

## Add Repository
`$ helm repo add microservices-learning https://maksimkavalenka.github.io/microservices-learning`  
`$ helm repo update`

## Install Packages
`$ helm install infrastructure microservices-learning/infrastructure`  
`$ helm install song-service microservices-learning/song-service -n microservices-learning --create-namespace`  
`$ helm install resource-service microservices-learning/resource-service -n microservices-learning`

## Uninstall Packages
`$ helm uninstall resource-service -n microservices-learning`  
`$ helm uninstall song-service -n microservices-learning`  
`$ helm uninstall infrastructure`  
`$ kubectl delete namespace microservices-learning`
