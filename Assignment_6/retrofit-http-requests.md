# Retrofit HTTP Requests

> Android · Retrofit 2 — From HTTP request URL to Kotlin interface: GET, PUT, PATCH and DELETE with auth headers.

---

## GET Requests

### 1. Get all todos

**Request**
```
GET https://jsonplaceholder.typicode.com/todos
```

**Retrofit Interface**
```kotlin
@GET("todos")
suspend fun getTodos(): List<Todo>
```

> The base URL is set once in your `Retrofit.Builder`. The path here is relative to it. `suspend fun` means you call this inside a coroutine — no callback needed.

---

### 2. Get a single todo by ID (`@Path`)

**Request**
```
GET https://jsonplaceholder.typicode.com/todos/5
```

**Retrofit Interface**
```kotlin
@GET("todos/{id}")
suspend fun getTodoById(
    @Path("id") id: Int
): Todo
```

> `@Path` replaces `{id}` in the URL with the value you pass. Calling `getTodoById(5)` produces `/todos/5`.

---

### 3. Filter todos by userId (`@Query`)

**Request**
```
GET https://jsonplaceholder.typicode.com/todos?userId=1
```

**Retrofit Interface**
```kotlin
@GET("todos")
suspend fun getTodosByUser(
    @Query("userId") userId: Int
): List<Todo>
```

> `@Query` appends a key=value pair to the URL. Retrofit URL-encodes the value automatically. Passing `userId = 1` builds `/todos?userId=1`.

---

### 4. Paginated results — multiple query params

**Request**
```
GET https://jsonplaceholder.typicode.com/todos?_page=2&_limit=10
```

**Retrofit Interface**
```kotlin
@GET("todos")
suspend fun getTodosPaged(
    @Query("_page")  page:  Int,
    @Query("_limit") limit: Int
): List<Todo>
```

> Each `@Query` parameter maps to one key in the query string. Retrofit joins them with `&` automatically.

---

### 5. Authenticated request — Authorization header

**Request**
```
GET https://jsonplaceholder.typicode.com/todos
```

**Headers**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9…
Accept:        application/json
```

**Retrofit Interface**
```kotlin
@GET("todos")
suspend fun getTodosAuth(
    @Header("Authorization") token: String,
    @Header("Accept")        accept: String = "application/json"
): List<Todo>
```

> `@Header` injects a value as a request header at call time. For a fixed header on every call (like `Accept`), use an OkHttp `Interceptor` instead — keep the interface clean.

---

### 6. Combined — path + query + static headers

**Request**
```
GET https://jsonplaceholder.typicode.com/users/1/todos?completed=true
```

**Headers**
```
Accept:       application/json
X-Api-Version: 2
```

**Retrofit Interface**
```kotlin
@Headers("Accept: application/json",
         "X-Api-Version: 2")
@GET("users/{userId}/todos")
suspend fun getCompletedTodosForUser(
    @Path("userId")    userId:    Int,
    @Query("completed") completed: Boolean
): List<Todo>
```

> `@Headers` (plural) is a compile-time annotation for headers that never change. `@Header` (singular) is for headers whose values are passed at runtime per call.

---

## Write Methods

### 7. Replace a todo — full update (`PUT`)

**Request**
```
PUT https://jsonplaceholder.typicode.com/todos/1
```

**Headers**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9…
Content-Type:  application/json
```

**Body**
```json
{
  "userId": 1,
  "id": 1,
  "title": "buy groceries",
  "completed": true
}
```

**Retrofit Interface**
```kotlin
@PUT("todos/{id}")
suspend fun replaceTodo(
    @Path("id")              id:    Int,
    @Header("Authorization") token: String,
    @Body                    todo:  Todo
): Todo
```

> `PUT` replaces the entire resource. Every field must be included in the body — missing fields are set to null or their default. `@Body` serialises your data class to JSON via Gson or Moshi (set up in your `Retrofit.Builder`).

---

### 8. Update one field — partial update (`PATCH`)

**Request**
```
PATCH https://jsonplaceholder.typicode.com/todos/1
```

**Headers**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9…
Content-Type:  application/json
```

**Body — only changed fields**
```json
{
  "completed": true
}
```

**Retrofit Interface**
```kotlin
@PATCH("todos/{id}")
suspend fun patchTodo(
    @Path("id")              id:     Int,
    @Header("Authorization") token:  String,
    @Body                    fields: Map<String, Any>
): Todo
```

> `PATCH` sends only the fields you want to change — the server merges them into the existing record. Using `Map<String, Any>` as the body lets you include only the keys you need. Alternatively, create a dedicated `data class TodoPatch(val completed: Boolean?)` with `@SerializedName` if you use Gson.

---

### 9. Delete a todo (`DELETE`)

**Request**
```
DELETE https://jsonplaceholder.typicode.com/todos/1
```

**Headers**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9…
```

**Retrofit Interface**
```kotlin
@DELETE("todos/{id}")
suspend fun deleteTodo(
    @Path("id")              id:    Int,
    @Header("Authorization") token: String
): Response<Unit>
```

> `DELETE` carries no body. The server returns `204 No Content` on success, so wrapping the return type in `Response<Unit>` lets you inspect the HTTP status code without a body to parse. Check `response.isSuccessful` in your ViewModel.

---

## Quick Reference

| Annotation   | Use when…                                  | Example                                    |
|--------------|--------------------------------------------|--------------------------------------------|
| `@GET`       | Declaring a GET endpoint                   | `@GET("todos")`                            |
| `@Path`      | A segment of the URL changes per call      | `@Path("id") id: Int`                      |
| `@Query`     | Adding `?key=value` to the URL             | `@Query("page") page: Int`                 |
| `@QueryMap`  | Many optional query params at once         | `@QueryMap filters: Map<String, String>`   |
| `@Header`    | Header value varies per call               | `@Header("Authorization") token: String`   |
| `@Headers`   | Fixed headers, same for every call         | `@Headers("Accept: application/json")`     |
| `@PUT`       | Full resource replacement (all fields)     | `@PUT("todos/{id}")`                       |
| `@PATCH`     | Partial update (only changed fields)       | `@PATCH("todos/{id}")`                     |
| `@DELETE`    | Remove a resource by ID                    | `@DELETE("todos/{id}")`                    |
| `@Body`      | Send a JSON body (PUT / PATCH / POST)      | `@Body todo: Todo`                         |
