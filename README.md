# android-movies-app

## Brief Summary of the app

This app is designed for helping users to discover movies. It uses themoviedb's APIs to fetch movie data. It has three core screens that provide the following functionality: a list of popular movies, details about a specific movie, and a search function for finding a movie by name. 

<img src="https://github.com/octoberv29/android-movies-app/assets/47453811/06000f72-c8ce-4c0f-962b-97d7043b83a5" width="250" alt="" title="" />
<img src="https://github.com/octoberv29/android-movies-app/assets/47453811/f011bac9-674a-4ece-95b6-f1348e1dea56" width="250" alt="" title="" />
<img src="https://github.com/octoberv29/android-movies-app/assets/47453811/e0027a2a-e226-44fb-b5a1-59fe77b0c418" width="250" alt="" title="" />

### List of key libraries used

- Android Architecture Components (ViewModel, LiveData, Paging, Navigation Graph, etc)
- Dagger
- RxJava
- Kotlin Coroutines and StateFlow
- Retrofit
- JUnit, Mockk

### Engineering practices used

This list includes some of the recommended engineering practices I used while implementing the app to demonstrate performance, readability, maintainability, testability, scalability, and simplicity:

1. Implemented MVVM architecture while following some of the  principles of Clean Architecture.
2. Used clearly defined data and ui layers with corresponding package names.
3. Used Dagger for dependency injection and used different scopes when it was needed.

data:

1. Data layer exposes data to the rest of the app using a repository.
2. Repository is main-safe, meaning all its methods can be called from the main thread.
3. Provided interface for repository class to be able to use fake repository in the tests
4. In `MoviesRepositoryImpl` and `GetMoviesRxPagingSource` passed Schedulers and Dispatchers as params to simplify testing

ui:

1. UI layer follows Unidirectional Data Flow (UDF) principle, where ViewModels expose UI state using the observer pattern and receive actions from the UI through method calls.
2. Used Android Architecture Component’s (AAC) ViewModel class for building all ViewModels in the app
3. All ViewModels don’t include references to android specific classes like Application, Context, Resources, etc.
4. Used lifecycle-aware UI state collection: LiveData for discovering movies and movie details screens and StateFlow for the search movie screen.
5. Used a single Activity application in combination with a Navigation Graph.
6. Used RxJava on discover and details functionality and Kotlin Coroutines on search functionality for network operations. Usually I would use only one technology, but to showcase the knowledge of both I decided to build different functionalities on different technologies.
7. For the ViewModels that utilised RxJava for networking operations cleared disposables in the onClear method and for ViewModel that used Coroutines used viewModelScope.
8. For showing movies data on discover screen used Paging 3 library provided in Android Architecture Component (AAC).

tests:

1. Wrote unit tests using JUnit 5 and Mockk for Repository, ViewModels, RxPagingSource. In some cases also used fakes and Dagger.
2. Also tested app using Accessibility Scanner for accessibility purposes.
3. Unfortunately, I faced some challenges when writing some of the tests, I made comments for them in the code.

### Some decisions

* Jetpack Compose vs. Views
  I decided to use Views for this app because I noticed a mention in the role description of being able to use Butterknife. However, Butterknife is currently deprecated, so I opted to use simple findViewById() instead. If there is a need to demonstrate Jetpack Compose knowledge, please let me know, and I can build the UI using Compose.

* RxJava vs. Kotlin Coroutines
  To demonstrate proficiency in both technologies, the discover movies screen utilises RxJava while the search movies screen utilises Kotlin Coroutines.

* LiveData vs. StateFlow
  The same reasoning as for RxJava vs. Kotlin Coroutines. LiveData was used in discover screen and StateFlow in search screen.
  
  
### What can be done in the future to make the existing functionality better:

1. Make an advanced error handling depending on different HTTP codes with different error messages and provide a single class or extension for it
2. Write more unit tests and UI tests
3. Enhance experience from using Paging library
