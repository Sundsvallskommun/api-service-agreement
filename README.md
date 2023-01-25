# Agreement

## Leverantör

Sundsvalls kommun

## Beskrivning
Agreement är en tjänst som ansvarar för att returnera avtal kopplade till en anläggning.


## Tekniska detaljer

### Starta tjänsten

|Variabel i .env-fil|Beskrivning|
|---|---|
|**DataWarehousReader**||
|`integration.datawarehousereader.url`|URL för endpoint till DataWarehouseReader-tjänsten i WSO2|
|`spring.security.oauth2.client.registration.datawarehousereader.client-id`|Klient-ID som ska användas för DataWarehousReader-tjänsten|
|`spring.security.oauth2.client.registration.datawarehousereader.client-secret`|Klient-secret som ska användas för DataWarehouseReader-tjänsten|
|`spring.security.oauth2.client.provider.datawarehousereader.token-uri`|URI till endpoint för att förnya token för DataWarehouseReader-tjänsten|


### Paketera och starta tjänsten
Applikationen kan paketeras genom:

```
./mvnw package
```
Kommandot skapar filen `api-service-agreement-<version>.jar` i katalogen `target`. Tjänsten kan nu köras genom kommandot `java -jar target/api-service-agreement-<version>.jar`.

### Bygga och starta med Docker
Exekvera följande kommando för att bygga en Docker-image:

```
docker build -f src/main/docker/Dockerfile -t api.sundsvall.se/ms-agreement:latest .
```

Exekvera följande kommando för att starta samma Docker-image i en container:

```
docker run -i --rm -p 8080:8080 api.sundsvall.se/ms-agreement
```

#### Kör applikationen lokalt

<div style='border: solid 1px #0085A9; border-radius: 0.5em; padding: 0.5em 1em; background-color: #D6E0E3; margin: 0 0 0.8em 0 '>
  För att köra applikationen lokalt måste du ha Docker Desktop installerat och startat på din dator.
</div>

Exekvera följande kommando för att bygga och starta en container i sandbox mode:  

```
docker-compose -f src/main/docker/docker-compose-sandbox.yaml build && docker-compose -f src/main/docker/docker-compose-sandbox.yaml up
```


## 
Copyright (c) 2021 Sundsvalls kommun
