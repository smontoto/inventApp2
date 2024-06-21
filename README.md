# InventApp
***
It is a simple application that allows the user to keep an inventory of their products (in this case to repair,
since it was developed for an electronics technical service).

Among its functionalities it is allowed:
- Register an article, including data and photos
- View a list of entered articles
- View a list of delivered items
- View the detail of an item entered / delivered
- Indicate the repair cost and share the details by whatsapp

## Architecture and technical characteristics
 - Kotlin Language 100% 
 - Clean Architecture
   - MVVM for presentation
   - Data package for data retrieving
   - Domain package for business logic
   - DI package for Dependency Injection
   - UI package for everything UI related
   - Testing with Mockk and JUnit


## Third party libraries used
### Glide 
To quickly load an image via a URL

### UploadService
To send files images to backend


