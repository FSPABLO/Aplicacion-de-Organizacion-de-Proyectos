#  Examen 1 - Programación III 2025

Este proyecto es una aplicación de escritorio desarrollada en Java como parte del **Examen 1 del curso Programación III** en la Universidad. Consiste en un sistema de gestión de proyectos y tareas, estructurado con **arquitectura por capas** y el patrón **Modelo-Vista-Controlador (MVC)**.

---

##  Descripción del sistema

El sistema permite registrar proyectos, asignarles un encargado general, y asociarles tareas con diferentes atributos. Toda la información se almacena y recupera desde archivos **XML** utilizando JAXB.

Está diseñado para una única ventana principal, con dos secciones:

- Parte superior: lista de proyectos y formulario para crear nuevos.
- Parte inferior: lista de tareas del proyecto seleccionado, y formulario para agregar tareas.

---

##  Funcionalidades principales

-  **Carga y almacenamiento automático en XML** (proyectos, tareas, usuarios).
-  **Listado de proyectos al iniciar**.
-  **Creación de nuevos proyectos con ID único automático**.
-  **Visualización de tareas por proyecto seleccionado**.
-  **Creación de tareas con:**
  - Descripción
  - Fecha de finalización esperada
  - Prioridad: `Alta`, `Media`, `Baja`
  - Estado: `Abierta`, `En progreso`, `En revisión`, `Resuelta`
  - Responsable (usuario desde XML)
-  **Edición de tareas**: doble clic abre un formulario emergente para cambiar prioridad y estado.

---

##  Estructura técnica

- **Lenguaje**: Java (Swing)
- **Arquitectura**: por capas
- **Patrón**: Modelo-Vista-Controlador (MVC)
- **Persistencia**: Archivos XML vía JAXB
- **ID únicos**: Generados dinámicamente al leer el XML
- **Validación de entradas**: activa en todos los formularios

---

##  Interfaz gráfica

- La interfaz muestra todos los proyectos registrados al iniciar.
- Al seleccionar un proyecto, se muestra su lista de tareas y el formulario para agregar más.
- Al hacer doble clic sobre una tarea, se abre una ventana emergente para editarla.

---

##  Estructura del proyecto

```text
src/
├── dataAccesLayer/      # Almacenamiento XML
├── domainLayer/         # Clases de dominio (Proyect, Work, User)
├── presentationLayer/   # Controladores, vistas y modelos Swing (MVC)
├── serviceLayer/        # Lógica de negocio
├── utilities/           # Utilidades (enumeraciones, etc.)
