# Documentation de l'API DevUnity

> **Note:** Ce document a été généré automatiquement par analyse statique des contrôleurs Spring Boot.

## 🔹 Contrôleur : `CertificationController`

### `GET` /certification/student/questions
- **Méthode HTTP** : `GET`
- **URL** : `/certification/student/questions`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/student/questions
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /certification/student/submit
- **Méthode HTTP** : `POST`
- **URL** : `/certification/student/submit`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/certification/student/submit
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/student/certificates
- **Méthode HTTP** : `GET`
- **URL** : `/certification/student/certificates`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/student/certificates
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /certification/student/tab-violation
- **Méthode HTTP** : `POST`
- **URL** : `/certification/student/tab-violation`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/certification/student/tab-violation
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/student/status
- **Méthode HTTP** : `GET`
- **URL** : `/certification/student/status`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/student/status
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/questions/all
- **Méthode HTTP** : `GET`
- **URL** : `/certification/questions/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/questions/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /certification/questions/add
- **Méthode HTTP** : `POST`
- **URL** : `/certification/questions/add`
- **Paramètres** : Aucun
- **Body** : `CertificationQuestion q`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/certification/questions/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /certification/questions/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/certification/questions/update/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/certification/questions/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /certification/questions/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/certification/questions/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/certification/questions/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/sessions/all
- **Méthode HTTP** : `GET`
- **URL** : `/certification/sessions/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/sessions/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/sessions/suspicious
- **Méthode HTTP** : `GET`
- **URL** : `/certification/sessions/suspicious`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/sessions/suspicious
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /certification/certificates/all
- **Méthode HTTP** : `GET`
- **URL** : `/certification/certificates/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/certification/certificates/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `KanbanTaskController`

### `POST` /events/kanban/tasks
- **Méthode HTTP** : `POST`
- **URL** : `/events/kanban/tasks`
- **Paramètres** : Aucun
- **Body** : `KanbanTaskDTO dto`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/events/kanban/tasks
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /events/kanban/tasks/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/events/kanban/tasks/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/events/kanban/tasks/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /events/kanban/tasks/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/events/kanban/tasks/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/events/kanban/tasks/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/kanban/tasks/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/events/kanban/tasks/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/kanban/tasks/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/kanban/board/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/events/kanban/board/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/kanban/board/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/kanban/board/{userId}/column/{status}
- **Méthode HTTP** : `GET`
- **URL** : `/events/kanban/board/{userId}/column/{status}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/kanban/board/{userId}/column/{status}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PATCH` /events/kanban/tasks/{id}/move
- **Méthode HTTP** : `PATCH`
- **URL** : `/events/kanban/tasks/{id}/move`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PATCH http://[api-gateway]/events/kanban/tasks/{id}/move
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/kanban/daily-analysis/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/events/kanban/daily-analysis/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/kanban/daily-analysis/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CourseController`

### `POST` /courses/add
- **Méthode HTTP** : `POST`
- **URL** : `/courses/add`
- **Paramètres** : Aucun
- **Body** : `Course course`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/courses/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /courses/update
- **Méthode HTTP** : `PUT`
- **URL** : `/courses/update`
- **Paramètres** : Aucun
- **Body** : `Course course`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/courses/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /courses/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/courses/delete/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/courses/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /courses/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/courses/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/courses/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /courses
- **Méthode HTTP** : `GET`
- **URL** : `/courses`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/courses
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /courses/child/{childId}
- **Méthode HTTP** : `GET`
- **URL** : `/courses/child/{childId}`
- **Paramètres** :
  - `Long childId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/courses/child/{childId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CourseSyncController`

### `POST` /courses/sync
- **Méthode HTTP** : `POST`
- **URL** : `/courses/sync`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/courses/sync
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `EnrollmentController`

### `GET` /enrollments/all
- **Méthode HTTP** : `GET`
- **URL** : `/enrollments/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/enrollments/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /enrollments/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/enrollments/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/enrollments/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /enrollments/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/enrollments/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/enrollments/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /enrollments/course/{courseId}
- **Méthode HTTP** : `GET`
- **URL** : `/enrollments/course/{courseId}`
- **Paramètres** :
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/enrollments/course/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /enrollments/add/{userId}/{courseId}
- **Méthode HTTP** : `POST`
- **URL** : `/enrollments/add/{userId}/{courseId}`
- **Paramètres** : Aucun
- **Body** : `Enrollment enrollment`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/enrollments/add/{userId}/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /enrollments/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/enrollments/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/enrollments/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /enrollments/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/enrollments/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/enrollments/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /enrollments/enroll
- **Méthode HTTP** : `POST`
- **URL** : `/enrollments/enroll`
- **Paramètres** :
  - `Integer userId`
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/enrollments/enroll
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `LessonController`

### `GET` /lessons/all
- **Méthode HTTP** : `GET`
- **URL** : `/lessons/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/lessons/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /lessons/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/lessons/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/lessons/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /lessons/course/{courseId}
- **Méthode HTTP** : `GET`
- **URL** : `/lessons/course/{courseId}`
- **Paramètres** :
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/lessons/course/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /lessons/add
- **Méthode HTTP** : `POST`
- **URL** : `/lessons/add`
- **Paramètres** : Aucun
- **Body** : `Lesson lesson`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/lessons/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /lessons/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/lessons/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/lessons/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /lessons/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/lessons/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/lessons/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `QuizAttemptController`

### `GET` /quiz-attempts/all
- **Méthode HTTP** : `GET`
- **URL** : `/quiz-attempts/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quiz-attempts/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /quiz-attempts/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/quiz-attempts/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quiz-attempts/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /quiz-attempts/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/quiz-attempts/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quiz-attempts/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /quiz-attempts/quiz/{quizId}
- **Méthode HTTP** : `GET`
- **URL** : `/quiz-attempts/quiz/{quizId}`
- **Paramètres** :
  - `Long quizId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quiz-attempts/quiz/{quizId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /quiz-attempts/submit
- **Méthode HTTP** : `POST`
- **URL** : `/quiz-attempts/submit`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/quiz-attempts/submit
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /quiz-attempts/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/quiz-attempts/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/quiz-attempts/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `QuizController`

### `GET` /quizzes/all
- **Méthode HTTP** : `GET`
- **URL** : `/quizzes/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quizzes/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /quizzes/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/quizzes/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quizzes/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /quizzes/course/{courseId}
- **Méthode HTTP** : `GET`
- **URL** : `/quizzes/course/{courseId}`
- **Paramètres** :
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/quizzes/course/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /quizzes/add
- **Méthode HTTP** : `POST`
- **URL** : `/quizzes/add`
- **Paramètres** : Aucun
- **Body** : `Quiz quiz`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/quizzes/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /quizzes/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/quizzes/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Quiz quiz`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/quizzes/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /quizzes/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/quizzes/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/quizzes/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ApplicantController`

### `GET` /api/applicants
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/applicants/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/applicants
- **Méthode HTTP** : `POST`
- **URL** : `/api/applicants`
- **Paramètres** : Aucun
- **Body** : `Applicant applicant`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/applicants
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/applicants/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/applicants/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/applicants/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/applicants/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/applicants/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/applicants/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/applicants/{id}/user
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants/{id}/user`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants/{id}/user
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/applicants/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants/user/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/applicants/recruitment/{recruitmentId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants/recruitment/{recruitmentId}`
- **Paramètres** :
  - `Long recruitmentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants/recruitment/{recruitmentId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/applicants/interview/{interviewId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/applicants/interview/{interviewId}`
- **Paramètres** :
  - `Long interviewId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/applicants/interview/{interviewId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/applicants/upload-cv
- **Méthode HTTP** : `POST`
- **URL** : `/api/applicants/upload-cv`
- **Paramètres** :
  - `MultipartFile file`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/applicants/upload-cv
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/applicants/{applicantId}/analyze
- **Méthode HTTP** : `POST`
- **URL** : `/api/applicants/{applicantId}/analyze`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/applicants/{applicantId}/analyze
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CvAnalysisController`

### `POST` /api/cv-analysis/analyze
- **Méthode HTTP** : `POST`
- **URL** : `/api/cv-analysis/analyze`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/cv-analysis/analyze
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `InterviewController`

### `GET` /api/interviews
- **Méthode HTTP** : `GET`
- **URL** : `/api/interviews`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/interviews
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/interviews/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/interviews/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/interviews/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/interviews
- **Méthode HTTP** : `POST`
- **URL** : `/api/interviews`
- **Paramètres** : Aucun
- **Body** : `Interview interview`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/interviews
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/interviews/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/interviews/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/interviews/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/interviews/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/interviews/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/interviews/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/interviews/{id}/user
- **Méthode HTTP** : `GET`
- **URL** : `/api/interviews/{id}/user`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/interviews/{id}/user
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/interviews/recruitment/{recruitmentId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/interviews/recruitment/{recruitmentId}`
- **Paramètres** :
  - `Long recruitmentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/interviews/recruitment/{recruitmentId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/interviews/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/interviews/user/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/interviews/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `NotificationController`

### `GET` /notifications
- **Méthode HTTP** : `GET`
- **URL** : `/notifications`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/notifications
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /notifications/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/notifications/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/notifications/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /notifications/add
- **Méthode HTTP** : `POST`
- **URL** : `/notifications/add`
- **Paramètres** : Aucun
- **Body** : `Notification notification`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/notifications/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /notifications/update
- **Méthode HTTP** : `PUT`
- **URL** : `/notifications/update`
- **Paramètres** : Aucun
- **Body** : `Notification notification`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/notifications/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /notifications/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/notifications/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/notifications/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /notifications/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/notifications/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/notifications/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /notifications/user/{userId}/unread
- **Méthode HTTP** : `GET`
- **URL** : `/notifications/user/{userId}/unread`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/notifications/user/{userId}/unread
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /notifications/{id}/read
- **Méthode HTTP** : `PUT`
- **URL** : `/notifications/{id}/read`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/notifications/{id}/read
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RecruitmentController`

### `GET` /api/recruitments
- **Méthode HTTP** : `GET`
- **URL** : `/api/recruitments`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/recruitments
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/recruitments/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/recruitments/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/recruitments/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/recruitments
- **Méthode HTTP** : `POST`
- **URL** : `/api/recruitments`
- **Paramètres** : Aucun
- **Body** : `Recruitment recruitment`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/recruitments
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/recruitments/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/recruitments/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Recruitment recruitment`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/recruitments/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/recruitments/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/recruitments/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/recruitments/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `BadgeController`

### `GET` /badges
- **Méthode HTTP** : `GET`
- **URL** : `/badges`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/badges
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rewards/{childId}
- **Méthode HTTP** : `GET`
- **URL** : `/rewards/{childId}`
- **Paramètres** :
  - `Long childId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rewards/{childId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /badges/check/{childId}
- **Méthode HTTP** : `POST`
- **URL** : `/badges/check/{childId}`
- **Paramètres** :
  - `Long childId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/badges/check/{childId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `BadgeEvaluationController`

### `POST` /badges/evaluate/{userId}
- **Méthode HTTP** : `POST`
- **URL** : `/badges/evaluate/{userId}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/badges/evaluate/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /badges/student/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/badges/student/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/badges/student/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /badges/stats/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/badges/stats/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/badges/stats/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /badges/eligible/{userId}/{badgeId}
- **Méthode HTTP** : `GET`
- **URL** : `/badges/eligible/{userId}/{badgeId}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/badges/eligible/{userId}/{badgeId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /badges/assign/{userId}/{badgeId}
- **Méthode HTTP** : `POST`
- **URL** : `/badges/assign/{userId}/{badgeId}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/badges/assign/{userId}/{badgeId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ChallengeAttemptController`

### `POST` /challenge-attempts/start
- **Méthode HTTP** : `POST`
- **URL** : `/challenge-attempts/start`
- **Paramètres** :
  - `Long userId`
  - `Long challengeId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/challenge-attempts/start
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /challenge-attempts/start-by-type
- **Méthode HTTP** : `POST`
- **URL** : `/challenge-attempts/start-by-type`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/challenge-attempts/start-by-type
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenge-attempts/{attemptId}
- **Méthode HTTP** : `GET`
- **URL** : `/challenge-attempts/{attemptId}`
- **Paramètres** :
  - `Long attemptId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenge-attempts/{attemptId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenge-attempts/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/challenge-attempts/user/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenge-attempts/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenge-attempts/challenge/{challengeId}
- **Méthode HTTP** : `GET`
- **URL** : `/challenge-attempts/challenge/{challengeId}`
- **Paramètres** :
  - `Long challengeId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenge-attempts/challenge/{challengeId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /challenge-attempts/{attemptId}/complete
- **Méthode HTTP** : `PUT`
- **URL** : `/challenge-attempts/{attemptId}/complete`
- **Paramètres** :
  - `Long attemptId`
  - `Integer score`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/challenge-attempts/{attemptId}/complete
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /challenge-attempts/{attemptId}/expire
- **Méthode HTTP** : `PUT`
- **URL** : `/challenge-attempts/{attemptId}/expire`
- **Paramètres** :
  - `Long attemptId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/challenge-attempts/{attemptId}/expire
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ChallengeController`

### `POST` /challenges
- **Méthode HTTP** : `POST`
- **URL** : `/challenges`
- **Paramètres** : Aucun
- **Body** : `Challenge challenge`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/challenges
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /challenges/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/challenges/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Challenge challenge`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/challenges/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenges
- **Méthode HTTP** : `GET`
- **URL** : `/challenges`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenges
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenges/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/challenges/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenges/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /challenges/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/challenges/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/challenges/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenges/available
- **Méthode HTTP** : `GET`
- **URL** : `/challenges/available`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenges/available
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /challenges/sync
- **Méthode HTTP** : `POST`
- **URL** : `/challenges/sync`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/challenges/sync
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /challenges/{id}/submit-score
- **Méthode HTTP** : `POST`
- **URL** : `/challenges/{id}/submit-score`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/challenges/{id}/submit-score
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenges/{id}/attempts/{idUser}
- **Méthode HTTP** : `GET`
- **URL** : `/challenges/{id}/attempts/{idUser}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenges/{id}/attempts/{idUser}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /challenges/{id}/leaderboard
- **Méthode HTTP** : `GET`
- **URL** : `/challenges/{id}/leaderboard`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/challenges/{id}/leaderboard
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `StudentChallengesController`

### `GET` /studentChallenges/byUser/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/studentChallenges/byUser/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/studentChallenges/byUser/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /studentChallenges/startOrGet
- **Méthode HTTP** : `POST`
- **URL** : `/studentChallenges/startOrGet`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/studentChallenges/startOrGet
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /studentChallenges/{attemptId}
- **Méthode HTTP** : `GET`
- **URL** : `/studentChallenges/{attemptId}`
- **Paramètres** :
  - `Long attemptId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/studentChallenges/{attemptId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /studentChallenges/byChallenge/{challengeId}
- **Méthode HTTP** : `GET`
- **URL** : `/studentChallenges/byChallenge/{challengeId}`
- **Paramètres** :
  - `Long challengeId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/studentChallenges/byChallenge/{challengeId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /studentChallenges/{attemptId}/complete
- **Méthode HTTP** : `PUT`
- **URL** : `/studentChallenges/{attemptId}/complete`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/studentChallenges/{attemptId}/complete
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /studentChallenges/{attemptId}/expire
- **Méthode HTTP** : `PUT`
- **URL** : `/studentChallenges/{attemptId}/expire`
- **Paramètres** :
  - `Long attemptId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/studentChallenges/{attemptId}/expire
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /studentChallenges/startByType
- **Méthode HTTP** : `POST`
- **URL** : `/studentChallenges/startByType`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/studentChallenges/startByType
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `StudentChallengeSessionController`

### `POST` /sessions/start
- **Méthode HTTP** : `POST`
- **URL** : `/sessions/start`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/sessions/start
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /sessions/active
- **Méthode HTTP** : `GET`
- **URL** : `/sessions/active`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/sessions/active
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /sessions/{sessionId}
- **Méthode HTTP** : `GET`
- **URL** : `/sessions/{sessionId}`
- **Paramètres** :
  - `Long sessionId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/sessions/{sessionId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /sessions/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/sessions/user/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/sessions/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /sessions/{sessionId}/submit
- **Méthode HTTP** : `POST`
- **URL** : `/sessions/{sessionId}/submit`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/sessions/{sessionId}/submit
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /sessions/{sessionId}/complete
- **Méthode HTTP** : `POST`
- **URL** : `/sessions/{sessionId}/complete`
- **Paramètres** :
  - `Long sessionId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/sessions/{sessionId}/complete
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /sessions/{sessionId}/badges
- **Méthode HTTP** : `GET`
- **URL** : `/sessions/{sessionId}/badges`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/sessions/{sessionId}/badges
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /sessions/{sessionId}/updateGlobalScore
- **Méthode HTTP** : `POST`
- **URL** : `/sessions/{sessionId}/updateGlobalScore`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/sessions/{sessionId}/updateGlobalScore
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `UserController`

### `GET` /users
- **Méthode HTTP** : `GET`
- **URL** : `/users`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/users
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /users/email/{email}
- **Méthode HTTP** : `GET`
- **URL** : `/users/email/{email}`
- **Paramètres** :
  - `String email`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/users/email/{email}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /users/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/users/{id}`
- **Paramètres** :
  - `Integer id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/users/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /users/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/users/update/{id}`
- **Paramètres** :
  - `Integer id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/users/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ClassController`

### `GET` /classes/all
- **Méthode HTTP** : `GET`
- **URL** : `/classes/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/classes/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /classes/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/classes/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/classes/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /classes/name/{name}
- **Méthode HTTP** : `GET`
- **URL** : `/classes/name/{name}`
- **Paramètres** :
  - `String name`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/classes/name/{name}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /classes/add
- **Méthode HTTP** : `POST`
- **URL** : `/classes/add`
- **Paramètres** : Aucun
- **Body** : `ClassEntity classEntity`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/classes/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /classes/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/classes/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/classes/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /classes/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/classes/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/classes/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /classes/level/{level}
- **Méthode HTTP** : `GET`
- **URL** : `/classes/level/{level}`
- **Paramètres** :
  - `String level`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/classes/level/{level}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /classes/assign/{userId}/{classId}
- **Méthode HTTP** : `PUT`
- **URL** : `/classes/assign/{userId}/{classId}`
- **Paramètres** :
  - `long userId`
  - `Long classId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/classes/assign/{userId}/{classId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CourseSchedulingController`

### `POST` /schedules/course-planning/add
- **Méthode HTTP** : `POST`
- **URL** : `/schedules/course-planning/add`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/schedules/course-planning/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/course-planning/weekly/tutor
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/course-planning/weekly/tutor`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/course-planning/weekly/tutor
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/course-planning/weekly/student
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/course-planning/weekly/student`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/course-planning/weekly/student
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /schedules/course-planning/weekly-pdf/tutor
- **Méthode HTTP** : `POST`
- **URL** : `/schedules/course-planning/weekly-pdf/tutor`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/schedules/course-planning/weekly-pdf/tutor
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /schedules/course-planning/weekly-pdf/student
- **Méthode HTTP** : `POST`
- **URL** : `/schedules/course-planning/weekly-pdf/student`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/schedules/course-planning/weekly-pdf/student
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RoomController`

### `GET` /rooms/all
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rooms/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rooms/name/{name}
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/name/{name}`
- **Paramètres** :
  - `String name`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/name/{name}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /rooms/add
- **Méthode HTTP** : `POST`
- **URL** : `/rooms/add`
- **Paramètres** : Aucun
- **Body** : `Room room`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/rooms/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /rooms/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/rooms/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Room room`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/rooms/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /rooms/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/rooms/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/rooms/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rooms/available
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/available`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/available
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rooms/level/{level}
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/level/{level}`
- **Paramètres** :
  - `int level`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/level/{level}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rooms/capacity/{min}
- **Méthode HTTP** : `GET`
- **URL** : `/rooms/capacity/{min}`
- **Paramètres** :
  - `int min`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rooms/capacity/{min}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RoomScheduleComplaintController`

### `GET` /all
- **Méthode HTTP** : `GET`
- **URL** : `/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /{id}
- **Méthode HTTP** : `GET`
- **URL** : `/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /add/room/{roomId}
- **Méthode HTTP** : `POST`
- **URL** : `/add/room/{roomId}`
- **Paramètres** :
  - `Long roomId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/add/room/{roomId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /add/schedule/{scheduleId}
- **Méthode HTTP** : `POST`
- **URL** : `/add/schedule/{scheduleId}`
- **Paramètres** :
  - `Long scheduleId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/add/schedule/{scheduleId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PATCH` /{id}/answer
- **Méthode HTTP** : `PATCH`
- **URL** : `/{id}/answer`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PATCH http://[api-gateway]/{id}/answer
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /room/{roomId}
- **Méthode HTTP** : `GET`
- **URL** : `/room/{roomId}`
- **Paramètres** :
  - `Long roomId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/room/{roomId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedule/{scheduleId}
- **Méthode HTTP** : `GET`
- **URL** : `/schedule/{scheduleId}`
- **Paramètres** :
  - `Long scheduleId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedule/{scheduleId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /pending
- **Méthode HTTP** : `GET`
- **URL** : `/pending`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/pending
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ScheduleController`

### `GET` /schedules/all
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /schedules/add/room/{roomId}/class/{classId}
- **Méthode HTTP** : `POST`
- **URL** : `/schedules/add/room/{roomId}/class/{classId}`
- **Paramètres** : Aucun
- **Body** : `Schedule schedule`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/schedules/add/room/{roomId}/class/{classId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /schedules/update/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/schedules/update/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/schedules/update/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /schedules/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/schedules/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/schedules/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/professor/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/professor/{userId}`
- **Paramètres** :
  - `Long userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/professor/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/room/{roomId}
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/room/{roomId}`
- **Paramètres** :
  - `Long roomId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/room/{roomId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/class/{classId}
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/class/{classId}`
- **Paramètres** :
  - `Long classId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/class/{classId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/type/{type}
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/type/{type}`
- **Paramètres** :
  - `ScheduleType type`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/type/{type}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /schedules/between
- **Méthode HTTP** : `GET`
- **URL** : `/schedules/between`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/schedules/between
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `EventController`

### `POST` /events/add-with-image
- **Méthode HTTP** : `POST`
- **URL** : `/events/add-with-image`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/events/add-with-image
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /events/update-with-image/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/events/update-with-image/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/events/update-with-image/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/all
- **Méthode HTTP** : `GET`
- **URL** : `/events/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /events/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/events/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/events/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /events/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/events/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/events/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RegistrationEventController`

### `POST` /registrations/register
- **Méthode HTTP** : `POST`
- **URL** : `/registrations/register`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/registrations/register
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /registrations/my/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/registrations/my/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/registrations/my/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /registrations/{id}/ticket
- **Méthode HTTP** : `GET`
- **URL** : `/registrations/{id}/ticket`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/registrations/{id}/ticket
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /registrations/{id}/cancel
- **Méthode HTTP** : `DELETE`
- **URL** : `/registrations/{id}/cancel`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/registrations/{id}/cancel
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /registrations/all
- **Méthode HTTP** : `GET`
- **URL** : `/registrations/all`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/registrations/all
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /registrations/{id}/status
- **Méthode HTTP** : `PUT`
- **URL** : `/registrations/{id}/status`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/registrations/{id}/status
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `BusinessEnglishPathController`

### `POST` /paths
- **Méthode HTTP** : `POST`
- **URL** : `/paths`
- **Paramètres** : Aucun
- **Body** : `BusinessEnglishPath path`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/paths
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /paths/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/paths/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `BusinessEnglishPath path`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/paths/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /paths
- **Méthode HTTP** : `GET`
- **URL** : `/paths`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/paths
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /paths/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/paths/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/paths/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /paths/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/paths/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/paths/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CompanyOfferController`

### `POST` /company-offers
- **Méthode HTTP** : `POST`
- **URL** : `/company-offers`
- **Paramètres** : Aucun
- **Body** : `CompanyOffer companyOffer`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/company-offers
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /company-offers/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/company-offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `CompanyOffer companyOffer`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/company-offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /company-offers
- **Méthode HTTP** : `GET`
- **URL** : `/company-offers`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/company-offers
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /company-offers/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/company-offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/company-offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /company-offers/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/company-offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/company-offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /company-offers/admin/{id}/payment
- **Méthode HTTP** : `PUT`
- **URL** : `/company-offers/admin/{id}/payment`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/company-offers/admin/{id}/payment
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /company-offers/company/{companyId}/offer/{offerId}/request
- **Méthode HTTP** : `POST`
- **URL** : `/company-offers/company/{companyId}/offer/{offerId}/request`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/company-offers/company/{companyId}/offer/{offerId}/request
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /company-offers/admin/requests
- **Méthode HTTP** : `GET`
- **URL** : `/company-offers/admin/requests`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/company-offers/admin/requests
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /company-offers/student/{studentId}
- **Méthode HTTP** : `GET`
- **URL** : `/company-offers/student/{studentId}`
- **Paramètres** :
  - `Integer studentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/company-offers/student/{studentId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `EmployeeInvitationController`

### `POST` /invitations
- **Méthode HTTP** : `POST`
- **URL** : `/invitations`
- **Paramètres** : Aucun
- **Body** : `EmployeeInvitation invitation`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/invitations
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /invitations/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/invitations/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/invitations/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /invitations
- **Méthode HTTP** : `GET`
- **URL** : `/invitations`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/invitations
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /invitations/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/invitations/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/invitations/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /invitations/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/invitations/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/invitations/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /invitations/admin/company-offer/{companyOfferId}/emails
- **Méthode HTTP** : `PUT`
- **URL** : `/invitations/admin/company-offer/{companyOfferId}/emails`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/invitations/admin/company-offer/{companyOfferId}/emails
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /invitations/admin/pending
- **Méthode HTTP** : `GET`
- **URL** : `/invitations/admin/pending`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/invitations/admin/pending
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /invitations/admin/company-offer/{companyOfferId}/pending
- **Méthode HTTP** : `GET`
- **URL** : `/invitations/admin/company-offer/{companyOfferId}/pending`
- **Paramètres** :
  - `Long companyOfferId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/invitations/admin/company-offer/{companyOfferId}/pending
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /invitations/admin/company-offer/{companyOfferId}/approve
- **Méthode HTTP** : `POST`
- **URL** : `/invitations/admin/company-offer/{companyOfferId}/approve`
- **Paramètres** :
  - `Long companyOfferId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/invitations/admin/company-offer/{companyOfferId}/approve
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /invitations/admin/company-offer/{companyOfferId}/reject
- **Méthode HTTP** : `POST`
- **URL** : `/invitations/admin/company-offer/{companyOfferId}/reject`
- **Paramètres** :
  - `Long companyOfferId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/invitations/admin/company-offer/{companyOfferId}/reject
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /invitations/activate
- **Méthode HTTP** : `POST`
- **URL** : `/invitations/activate`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/invitations/activate
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `OfferController`

### `POST` /offers
- **Méthode HTTP** : `POST`
- **URL** : `/offers`
- **Paramètres** : Aucun
- **Body** : `Offer offer`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/offers
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /offers/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Offer offer`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /offers
- **Méthode HTTP** : `GET`
- **URL** : `/offers`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/offers
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /offers/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /offers/{id}/courses
- **Méthode HTTP** : `GET`
- **URL** : `/offers/{id}/courses`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/offers/{id}/courses
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /offers/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/offers/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/offers/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /offers/active
- **Méthode HTTP** : `GET`
- **URL** : `/offers/active`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/offers/active
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /offers/student/{studentId}
- **Méthode HTTP** : `GET`
- **URL** : `/offers/student/{studentId}`
- **Paramètres** :
  - `Long studentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/offers/student/{studentId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ActivityController`

### `POST` /api/activities/excursions
- **Méthode HTTP** : `POST`
- **URL** : `/api/activities/excursions`
- **Paramètres** : Aucun
- **Body** : `Excursion e) { return activityService.addExcursion(e`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/activities/excursions
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/activities/excursions
- **Méthode HTTP** : `PUT`
- **URL** : `/api/activities/excursions`
- **Paramètres** : Aucun
- **Body** : `Excursion e) { return activityService.updateExcursion(e`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/activities/excursions
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/excursions/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/excursions/{id}`
- **Paramètres** :
  - `Long id) { return activityService.findExcursion(id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/excursions/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/activities/excursions/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/activities/excursions/{id}`
- **Paramètres** :
  - `Long id) { activityService.deleteExcursion(id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/activities/excursions/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/excursions/club/{clubId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/excursions/club/{clubId}`
- **Paramètres** :
  - `Long clubId) { return activityService.excursionsByClub(clubId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/excursions/club/{clubId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/activities/excursions/register
- **Méthode HTTP** : `POST`
- **URL** : `/api/activities/excursions/register`
- **Paramètres** :
  - `Long memberId`
  - `Long excursionId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/activities/excursions/register
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/excursions/{id}/participants
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/excursions/{id}/participants`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/excursions/{id}/participants
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/activities/trainings
- **Méthode HTTP** : `POST`
- **URL** : `/api/activities/trainings`
- **Paramètres** : Aucun
- **Body** : `Training t) { return activityService.addTraining(t`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/activities/trainings
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/activities/trainings
- **Méthode HTTP** : `PUT`
- **URL** : `/api/activities/trainings`
- **Paramètres** : Aucun
- **Body** : `Training t) { return activityService.updateTraining(t`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/activities/trainings
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/trainings/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/trainings/{id}`
- **Paramètres** :
  - `Long id) { return activityService.findTraining(id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/trainings/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/activities/trainings/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/activities/trainings/{id}`
- **Paramètres** :
  - `Long id) { activityService.deleteTraining(id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/activities/trainings/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/trainings/club/{clubId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/trainings/club/{clubId}`
- **Paramètres** :
  - `Long clubId) { return activityService.trainingsByClub(clubId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/trainings/club/{clubId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/activities/trainings/register
- **Méthode HTTP** : `POST`
- **URL** : `/api/activities/trainings/register`
- **Paramètres** :
  - `Long memberId`
  - `Long trainingId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/activities/trainings/register
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/activities/trainings/{id}/participants
- **Méthode HTTP** : `GET`
- **URL** : `/api/activities/trainings/{id}/participants`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/activities/trainings/{id}/participants
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `AnalyticsController`

### `GET` /analytics
- **Méthode HTTP** : `GET`
- **URL** : `/analytics`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/analytics
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /analytics/overview
- **Méthode HTTP** : `GET`
- **URL** : `/analytics/overview`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/analytics/overview
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ChildController`

### `GET` /children
- **Méthode HTTP** : `GET`
- **URL** : `/children`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/children
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /children/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/children/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/children/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /children/add
- **Méthode HTTP** : `POST`
- **URL** : `/children/add`
- **Paramètres** : Aucun
- **Body** : `Map<String, Object> childData`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/children/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /children/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/children/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Map<String, Object> childData`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/children/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /children/update
- **Méthode HTTP** : `PUT`
- **URL** : `/children/update`
- **Paramètres** : Aucun
- **Body** : `Child child`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/children/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /children/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/children/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/children/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /children/parent/{parentId}
- **Méthode HTTP** : `GET`
- **URL** : `/children/parent/{parentId}`
- **Paramètres** :
  - `Long parentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/children/parent/{parentId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /children/level/{levelId}
- **Méthode HTTP** : `GET`
- **URL** : `/children/level/{levelId}`
- **Paramètres** :
  - `Long levelId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/children/level/{levelId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PATCH` /children/{id}/xp
- **Méthode HTTP** : `PATCH`
- **URL** : `/children/{id}/xp`
- **Paramètres** :
  - `Long id`
- **Body** : `Map<String, Integer> xpData`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PATCH http://[api-gateway]/children/{id}/xp
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `LeaderboardController`

### `GET` /leaderboard
- **Méthode HTTP** : `GET`
- **URL** : `/leaderboard`
- **Paramètres** :
  - `int limit`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/leaderboard
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ParentController`

### `GET` /parents
- **Méthode HTTP** : `GET`
- **URL** : `/parents`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/parents
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /parents/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/parents/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/parents/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /parents/add
- **Méthode HTTP** : `POST`
- **URL** : `/parents/add`
- **Paramètres** : Aucun
- **Body** : `Parent parent`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/parents/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /parents/update
- **Méthode HTTP** : `PUT`
- **URL** : `/parents/update`
- **Paramètres** : Aucun
- **Body** : `Parent parent`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/parents/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /parents/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/parents/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/parents/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /parents/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/parents/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/parents/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ProgressController`

### `GET` /progress
- **Méthode HTTP** : `GET`
- **URL** : `/progress`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /progress/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/progress/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /progress/add
- **Méthode HTTP** : `POST`
- **URL** : `/progress/add`
- **Paramètres** : Aucun
- **Body** : `Progress progress`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/progress/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /progress/update
- **Méthode HTTP** : `PUT`
- **URL** : `/progress/update`
- **Paramètres** : Aucun
- **Body** : `Progress progress`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/progress/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /progress/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/progress/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/progress/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /progress/child/{childId}
- **Méthode HTTP** : `GET`
- **URL** : `/progress/child/{childId}`
- **Paramètres** :
  - `Long childId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress/child/{childId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /progress/course/{courseId}
- **Méthode HTTP** : `GET`
- **URL** : `/progress/course/{courseId}`
- **Paramètres** :
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress/course/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /progress/child/{childId}/course/{courseId}
- **Méthode HTTP** : `GET`
- **URL** : `/progress/child/{childId}/course/{courseId}`
- **Paramètres** :
  - `Long childId`
  - `Long courseId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress/child/{childId}/course/{courseId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /progress/child/{childId}/summary
- **Méthode HTTP** : `GET`
- **URL** : `/progress/child/{childId}/summary`
- **Paramètres** :
  - `Long childId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/progress/child/{childId}/summary
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `QuestionController`

### `GET` /activities/{activityId}/questions
- **Méthode HTTP** : `GET`
- **URL** : `/activities/{activityId}/questions`
- **Paramètres** :
  - `Long activityId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/activities/{activityId}/questions
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /activities/{activityId}/submit
- **Méthode HTTP** : `POST`
- **URL** : `/activities/{activityId}/submit`
- **Paramètres** :
  - `Long activityId`
- **Body** : `Object attempt`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/activities/{activityId}/submit
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RewardController`

### `GET` /rewards
- **Méthode HTTP** : `GET`
- **URL** : `/rewards`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rewards
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rewards/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/rewards/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rewards/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /rewards/add
- **Méthode HTTP** : `POST`
- **URL** : `/rewards/add`
- **Paramètres** : Aucun
- **Body** : `Reward reward`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/rewards/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /rewards/update
- **Méthode HTTP** : `PUT`
- **URL** : `/rewards/update`
- **Paramètres** : Aucun
- **Body** : `Reward reward`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/rewards/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /rewards/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/rewards/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/rewards/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rewards/type/{type}
- **Méthode HTTP** : `GET`
- **URL** : `/rewards/type/{type}`
- **Paramètres** :
  - `TypeReward type`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rewards/type/{type}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rewards/available/{points}
- **Méthode HTTP** : `GET`
- **URL** : `/rewards/available/{points}`
- **Paramètres** :
  - `Integer points`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rewards/available/{points}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `AuthorController`

### `POST` /authors
- **Méthode HTTP** : `POST`
- **URL** : `/authors`
- **Paramètres** : Aucun
- **Body** : `Author a`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/authors
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /authors
- **Méthode HTTP** : `GET`
- **URL** : `/authors`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/authors
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /authors/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/authors/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/authors/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /authors/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/authors/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/authors/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /authors/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/authors/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/authors/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /authors/{id}/books
- **Méthode HTTP** : `GET`
- **URL** : `/authors/{id}/books`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/authors/{id}/books
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `BookController`

### `POST` /books
- **Méthode HTTP** : `POST`
- **URL** : `/books`
- **Paramètres** : Aucun
- **Body** : `Book book`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/books
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /books/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/books/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/books/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /books
- **Méthode HTTP** : `GET`
- **URL** : `/books`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/books
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /books/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/books/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/books/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /books/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/books/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/books/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `CategoryController`

### `POST` /categories
- **Méthode HTTP** : `POST`
- **URL** : `/categories`
- **Paramètres** : Aucun
- **Body** : `Category c`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/categories
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /categories
- **Méthode HTTP** : `GET`
- **URL** : `/categories`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/categories
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /categories/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/categories/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/categories/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /categories/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/categories/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/categories/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /categories/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/categories/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/categories/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /categories/{id}/books
- **Méthode HTTP** : `GET`
- **URL** : `/categories/{id}/books`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/categories/{id}/books
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ClubController`

### `POST` /api/clubs
- **Méthode HTTP** : `POST`
- **URL** : `/api/clubs`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/clubs
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/clubs
- **Méthode HTTP** : `PUT`
- **URL** : `/api/clubs`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/clubs
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/clubs/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/clubs/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/clubs/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/clubs
- **Méthode HTTP** : `GET`
- **URL** : `/api/clubs`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/clubs
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/clubs/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/clubs/{id}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/clubs/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `DiscountController`

### `GET` /discount/eligible
- **Méthode HTTP** : `GET`
- **URL** : `/discount/eligible`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/discount/eligible
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `InvoiceController`

### `GET` /invoice
- **Méthode HTTP** : `GET`
- **URL** : `/invoice`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/invoice
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `MembershipController`

### `POST` /api/memberships/join
- **Méthode HTTP** : `POST`
- **URL** : `/api/memberships/join`
- **Paramètres** :
  - `Long memberId`
  - `Long clubId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/memberships/join
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/memberships/club/{clubId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/memberships/club/{clubId}`
- **Paramètres** :
  - `Long clubId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/memberships/club/{clubId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/memberships/member/{memberId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/memberships/member/{memberId}`
- **Paramètres** :
  - `Long memberId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/memberships/member/{memberId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `OrderController`

### `POST` /orders
- **Méthode HTTP** : `POST`
- **URL** : `/orders`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/orders
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /orders/{orderId}/items
- **Méthode HTTP** : `POST`
- **URL** : `/orders/{orderId}/items`
- **Paramètres** :
  - `Long orderId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/orders/{orderId}/items
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /orders/{orderId}/pay
- **Méthode HTTP** : `POST`
- **URL** : `/orders/{orderId}/pay`
- **Paramètres** :
  - `Long orderId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/orders/{orderId}/pay
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /orders/{orderId}/cancel
- **Méthode HTTP** : `POST`
- **URL** : `/orders/{orderId}/cancel`
- **Paramètres** :
  - `Long orderId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/orders/{orderId}/cancel
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /orders
- **Méthode HTTP** : `GET`
- **URL** : `/orders`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/orders
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /orders/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/orders/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/orders/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /orders/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/orders/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/orders/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RentalController`

### `POST` /rentals
- **Méthode HTTP** : `POST`
- **URL** : `/rentals`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/rentals
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /rentals/{rentalId}/pay
- **Méthode HTTP** : `POST`
- **URL** : `/rentals/{rentalId}/pay`
- **Paramètres** :
  - `Long rentalId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/rentals/{rentalId}/pay
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /rentals/{id}/return
- **Méthode HTTP** : `POST`
- **URL** : `/rentals/{id}/return`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/rentals/{id}/return
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rentals
- **Méthode HTTP** : `GET`
- **URL** : `/rentals`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rentals
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /rentals/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/rentals/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/rentals/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /rentals/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/rentals/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/rentals/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `RequestController`

### `POST` /api/requests
- **Méthode HTTP** : `POST`
- **URL** : `/api/requests`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/requests
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/requests/{id}/accept
- **Méthode HTTP** : `PUT`
- **URL** : `/api/requests/{id}/accept`
- **Paramètres** :
  - `Long id`
  - `Long decidedByMemberId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/requests/{id}/accept
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/requests/{id}/reject
- **Méthode HTTP** : `PUT`
- **URL** : `/api/requests/{id}/reject`
- **Paramètres** :
  - `Long id`
  - `Long decidedByMemberId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/requests/{id}/reject
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/requests/club/{clubId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/requests/club/{clubId}`
- **Paramètres** :
  - `Long clubId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/requests/club/{clubId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/requests/member/{memberId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/requests/member/{memberId}`
- **Paramètres** :
  - `Long memberId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/requests/member/{memberId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `StockController`

### `GET` /api/stocks/{bookId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/stocks/{bookId}`
- **Paramètres** :
  - `Long bookId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/stocks/{bookId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/stocks/{bookId}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/stocks/{bookId}`
- **Paramètres** :
  - `Long bookId`
  - `int quantity`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/stocks/{bookId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/stocks/{bookId}/add
- **Méthode HTTP** : `POST`
- **URL** : `/api/stocks/{bookId}/add`
- **Paramètres** :
  - `Long bookId`
  - `int qty`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/stocks/{bookId}/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/stocks/{bookId}/remove
- **Méthode HTTP** : `POST`
- **URL** : `/api/stocks/{bookId}/remove`
- **Paramètres** :
  - `Long bookId`
  - `int qty`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/stocks/{bookId}/remove
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `TrainingManagementController`

### `POST` /api/training-management/pay/cash
- **Méthode HTTP** : `POST`
- **URL** : `/api/training-management/pay/cash`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/training-management/pay/cash
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/training-management/pay/cash/{paymentId}/confirm
- **Méthode HTTP** : `PUT`
- **URL** : `/api/training-management/pay/cash/{paymentId}/confirm`
- **Paramètres** :
  - `Long paymentId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/training-management/pay/cash/{paymentId}/confirm
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/training-management/pay/wallet
- **Méthode HTTP** : `POST`
- **URL** : `/api/training-management/pay/wallet`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/training-management/pay/wallet
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/training-management/complete/{participationId}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/training-management/complete/{participationId}`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/training-management/complete/{participationId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/training-management/status
- **Méthode HTTP** : `GET`
- **URL** : `/api/training-management/status`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/training-management/status
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `WalletController`

### `POST` /wallet/create/{userId}
- **Méthode HTTP** : `POST`
- **URL** : `/wallet/create/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/wallet/create/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /wallet/recharge
- **Méthode HTTP** : `POST`
- **URL** : `/wallet/recharge`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/wallet/recharge
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /wallet/pay
- **Méthode HTTP** : `POST`
- **URL** : `/wallet/pay`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/wallet/pay
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /wallet/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/wallet/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/wallet/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /wallet/transactions/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/wallet/transactions/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/wallet/transactions/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `DropoutFormController`

### `POST` /dropout-forms
- **Méthode HTTP** : `POST`
- **URL** : `/dropout-forms`
- **Paramètres** : Aucun
- **Body** : `DropoutFormRequest request`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/dropout-forms
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /dropout-forms
- **Méthode HTTP** : `GET`
- **URL** : `/dropout-forms`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/dropout-forms
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /dropout-forms/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/dropout-forms/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/dropout-forms/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /dropout-forms/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/dropout-forms/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/dropout-forms/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `AiChatController`

### `POST` /ai/chat
- **Méthode HTTP** : `POST`
- **URL** : `/ai/chat`
- **Paramètres** : Aucun
- **Body** : `Map<String, Object> body`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/ai/chat
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `AuthController`

### `POST` /auth/register
- **Méthode HTTP** : `POST`
- **URL** : `/auth/register`
- **Paramètres** : Aucun
- **Body** : `RegisterRequest request`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/auth/register
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /auth/login
- **Méthode HTTP** : `POST`
- **URL** : `/auth/login`
- **Paramètres** : Aucun
- **Body** : `LoginRequest request`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/auth/login
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /auth/refresh
- **Méthode HTTP** : `POST`
- **URL** : `/auth/refresh`
- **Paramètres** : Aucun
- **Body** : `Map<String, String> body`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/auth/refresh
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `SubjectController`

### `GET` /api/subjects
- **Méthode HTTP** : `GET`
- **URL** : `/api/subjects`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/subjects
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/subjects/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/subjects/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/subjects/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/subjects
- **Méthode HTTP** : `POST`
- **URL** : `/api/subjects`
- **Paramètres** : Aucun
- **Body** : `Subject subject`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/subjects
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/subjects/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/subjects/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `Subject subject`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/subjects/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/subjects/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/subjects/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/subjects/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `TestTentativeController`

### `GET` /api/test-tentatives
- **Méthode HTTP** : `GET`
- **URL** : `/api/test-tentatives`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/test-tentatives
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/test-tentatives/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/test-tentatives/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/test-tentatives/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/test-tentatives/corrections
- **Méthode HTTP** : `GET`
- **URL** : `/api/test-tentatives/corrections`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/test-tentatives/corrections
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/test-tentatives
- **Méthode HTTP** : `POST`
- **URL** : `/api/test-tentatives`
- **Paramètres** : Aucun
- **Body** : `TestTentative testTentative`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/test-tentatives
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/test-tentatives/{id}
- **Méthode HTTP** : `PUT`
- **URL** : `/api/test-tentatives/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : `TestTentative testTentative`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/test-tentatives/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/test-tentatives/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/test-tentatives/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/test-tentatives/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/test-tentatives/{id}/evaluate-paragraph
- **Méthode HTTP** : `POST`
- **URL** : `/api/test-tentatives/{id}/evaluate-paragraph`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/test-tentatives/{id}/evaluate-paragraph
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/test-tentatives/{id}/evaluate-oral
- **Méthode HTTP** : `POST`
- **URL** : `/api/test-tentatives/{id}/evaluate-oral`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/test-tentatives/{id}/evaluate-oral
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/test-tentatives/recommend-courses
- **Méthode HTTP** : `GET`
- **URL** : `/api/test-tentatives/recommend-courses`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/test-tentatives/recommend-courses
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /api/test-tentatives/{id}/get-recommendations
- **Méthode HTTP** : `POST`
- **URL** : `/api/test-tentatives/{id}/get-recommendations`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/test-tentatives/{id}/get-recommendations
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ChatController`

### `GET` /api/chat/history
- **Méthode HTTP** : `GET`
- **URL** : `/api/chat/history`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/chat/history
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/chat/history/topic/{topicId}
- **Méthode HTTP** : `GET`
- **URL** : `/api/chat/history/topic/{topicId}`
- **Paramètres** :
  - `Long topicId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/chat/history/topic/{topicId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `MessageController`

### `GET` /messages
- **Méthode HTTP** : `GET`
- **URL** : `/messages`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/messages
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /messages/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/messages/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/messages/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /messages/add
- **Méthode HTTP** : `POST`
- **URL** : `/messages/add`
- **Paramètres** : Aucun
- **Body** : `Message message`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/messages/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /messages/update
- **Méthode HTTP** : `PUT`
- **URL** : `/messages/update`
- **Paramètres** : Aucun
- **Body** : `Message message`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/messages/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /messages/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/messages/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/messages/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /messages/sender/{senderId}
- **Méthode HTTP** : `GET`
- **URL** : `/messages/sender/{senderId}`
- **Paramètres** :
  - `Integer senderId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/messages/sender/{senderId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /messages/receiver/{receiverId}
- **Méthode HTTP** : `GET`
- **URL** : `/messages/receiver/{receiverId}`
- **Paramètres** :
  - `Integer receiverId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/messages/receiver/{receiverId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /messages/conversation/{userId1}/{userId2}
- **Méthode HTTP** : `GET`
- **URL** : `/messages/conversation/{userId1}/{userId2}`
- **Paramètres** :
  - `Integer userId1`
  - `Integer userId2`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/messages/conversation/{userId1}/{userId2}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `ReportController`

### `GET` /reports
- **Méthode HTTP** : `GET`
- **URL** : `/reports`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/reports
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /reports/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/reports/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/reports/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `POST` /reports/add
- **Méthode HTTP** : `POST`
- **URL** : `/reports/add`
- **Paramètres** : Aucun
- **Body** : `Report report`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/reports/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /reports/update
- **Méthode HTTP** : `PUT`
- **URL** : `/reports/update`
- **Paramètres** : Aucun
- **Body** : `Report report`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/reports/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /reports/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/reports/delete/{id}`
- **Paramètres** :
  - `Long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/reports/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /reports/user/{userId}
- **Méthode HTTP** : `GET`
- **URL** : `/reports/user/{userId}`
- **Paramètres** :
  - `Integer userId`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/reports/user/{userId}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /reports/status/{status}
- **Méthode HTTP** : `GET`
- **URL** : `/reports/status/{status}`
- **Paramètres** :
  - `String status`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/reports/status/{status}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

## 🔹 Contrôleur : `TopicController`

### `POST` /api/topics/add
- **Méthode HTTP** : `POST`
- **URL** : `/api/topics/add`
- **Paramètres** : Aucun
- **Body** : `Topic topic`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X POST http://[api-gateway]/api/topics/add
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `PUT` /api/topics/update
- **Méthode HTTP** : `PUT`
- **URL** : `/api/topics/update`
- **Paramètres** : Aucun
- **Body** : `Topic topic`
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X PUT http://[api-gateway]/api/topics/update
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `DELETE` /api/topics/delete/{id}
- **Méthode HTTP** : `DELETE`
- **URL** : `/api/topics/delete/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X DELETE http://[api-gateway]/api/topics/delete/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/topics/{id}
- **Méthode HTTP** : `GET`
- **URL** : `/api/topics/{id}`
- **Paramètres** :
  - `long id`
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/topics/{id}
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

### `GET` /api/topics
- **Méthode HTTP** : `GET`
- **URL** : `/api/topics`
- **Paramètres** : Aucun
- **Body** : Aucun
- **Réponse** : `ResponseEntity<?>`
- **Codes d'erreur** : `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `500 Internal Server Error`

- **Exemple de requête** :
```bash
curl -X GET http://[api-gateway]/api/topics
```
- **Exemple de réponse (Succès)** :
```json
{
  "status": "success",
  "data": { ... }
}
```

