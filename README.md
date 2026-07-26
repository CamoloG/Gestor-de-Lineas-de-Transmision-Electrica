# ⚡ Gestor de Líneas de Transmisión Eléctrica

Aplicación de escritorio desarrollada en Java para la gestión, visualización y análisis de líneas de transmisión eléctrica y subestaciones del Sistema Interconectado Nacional (Colombia).

---

## 📌 Descripción del Proyecto

Este sistema permite administrar redes de transmisión eléctrica mediante operaciones **CRUD-S** (Crear, Leer, Actualizar, Eliminar y Buscar), procesamiento de archivos masivos de datos (PARATEC - XM) y cálculo automático de capacidades operativas.

El proyecto está diseñado aplicando principios avanzados de **Programación Orientada a Objetos (POO)** y arquitectura por capas (Modelo, Persistencia, Controlador y Vista).

---

## 📍 Configuración de Coordenadas

Para que el módulo geográfico (mapa) pueda graficar las subestaciones y líneas de transmisión importadas desde **PARATEC**, el sistema requiere un archivo de mapeo llamado `informacion.csv` ubicado en la carpeta `data/`.

> ⚠️ **REQUISITO IMPORTANTE:**  
> El nombre de la subestacion o generador en `informacion.csv` debe coincidir **exactamente (tal cual aparece en el archivo de PARATEC)** para que el sistema pueda enlazar sus coordenadas de latitud y longitud. Ya se encuentran las coordenadas previamente cargadas de la subarea Boyacá-Casanare de la pagina de PARATEC, asi que solo seria convertir el archivo .xlsx a .csv e importar para que aparezcan visualmente en el mapa.

#### Formato de `informacion.csv`
El archivo debe utilizar punto y coma (`;`) como separador y mantener la siguiente estructura de encabezado y datos:

```
NOMBRE;TIPO;LATITUD;LONGITUD
PAIPA;Subestacion;5.7821;-73.1189
SOCHAGOTA;Subestacion;5.7710;-73.1250
TERMOYOPAL;Generador;5.3120;-72.3890
```

## ✨ Características Principales

* **Gestión Completa (CRUD-S):** Control total sobre líneas de transmisión y subestaciones interconectadas.
* **Procesamiento de Archivos:** 
  * Importación y parsing de archivos oficiales de PARATEC (`.csv`).
  * Persistencia de datos propia con guardado y lectura automática.
  * Ruta de guardado/cargado de datos personalizable.
* **Cálculos Técnicos Integrados:** 
  * Determinación automática de Capacidad en Megavatios ($MW$) considerando factor de potencia ($pf = 0.95$).
  * Simulación de la capacidad total agregada del sistema eléctrico.
* **Validación de Datos:** Filtros de seguridad contra valores nulos, texto vacío o magnitudes numéricas inconsistentes.
* **Filtros Avanzados:** Búsqueda rápida por departamento y rango de voltaje nominal.
* **Geolocalización:** Representación de subestaciones a través de coordenadas geográficas (Latitud / Longitud).

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java (JDK 17+)
* **GUI:** JavaFX
* **Persistencia:** I/O de archivos en Java
* **Entorno de Desarrollo:** NetBeans IDE

---

## 🚀 Estructura del Proyecto

```
src/
 ├── modelo/         # Lógica de negocio (SistemaElectrico, LineaTransmision, Subestacion)
 ├── persistencia/   # Manejo de lectura/escritura de archivos (Archivo)
 └── controlador/    # Controladores y vistas de la interfaz gráfica
```

## 📊 Fuentes de Datos

* Datos de líneas de transmisión y subestaciones: 🔗 [PARATEC - XM Colombia](https://paratec.xm.com.co)
* Coordenadas: 🔗 [Google Maps](https://www.google.com/maps)

## 👥 Autores

Proyecto desarrollado de manera colaborativa por:

* **Samuel Otero (Backend)** - [@Samuel-Otero](https://github.com/Samuel-Otero)
* **Camilo Guzmán (Frontend)** - [@CamoloG](https://github.com/CamoloG)

