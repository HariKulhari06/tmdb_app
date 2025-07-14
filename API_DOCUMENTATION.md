# TMDB App - Comprehensive API Documentation

## Table of Contents

1. [Overview](#overview)
2. [Core Application](#core-application)
3. [Repository APIs](#repository-apis)
4. [Data Models](#data-models)
5. [UI Components](#ui-components)
6. [Image Loading](#image-loading)
7. [Dependency Injection](#dependency-injection)
8. [Usage Examples](#usage-examples)

## Overview

The TMDB (The Movie Database) Android application is built using modern Android architecture patterns including:

- **Clean Architecture** with separate layers for data, domain, and presentation
- **Dagger 2** for dependency injection
- **Jetpack Compose** for modern UI components
- **Room** for local database storage
- **Retrofit** for network API calls
- **Coroutines & Flow** for asynchronous operations

## Core Application

### App Class

The main application class that initializes the DI framework and app components.

```kotlin
open class App : DaggerApplication(), AppComponentHolder
```

**Key Methods:**
- `onCreate()`: Initializes LeakCanary and app initializers
- `applicationInjector()`: Returns the main Dagger component
- `appComponent`: Lazy-initialized application component

**Usage:**
```kotlin
// Access the app component from any context
val app = applicationContext as App
val appComponent = app.appComponent
```

## Repository APIs

### MoviesRepository

Interface for all movie-related data operations.

```kotlin
interface MoviesRepository {
    suspend fun refreshFilters()
    suspend fun getMovieGenres(): Flow<List<Genre>>
    suspend fun getMovieFilters(): Flow<Filters>
    suspend fun movieContents(id: Int): Flow<Movie>
    suspend fun refreshMovieDetails(id: Int)
    suspend fun discoverMovies(filters: Flow<Filters>): Flow<List<Movie>>
    suspend fun refreshMoviesByCategory(movieCategory: MovieCategory): LiveData<LoadingState>
    fun moviesPagedList(movieCategory: MovieCategory, scope: CoroutineScope): Listing<Movie>
}
```

**Key Methods:**

#### `getMovieGenres(): Flow<List<Genre>>`
Returns a flow of movie genres.
```kotlin
// Usage in ViewModel
viewModelScope.launch {
    moviesRepository.getMovieGenres().collect { genres ->
        // Update UI with genres
    }
}
```

#### `movieContents(id: Int): Flow<Movie>`
Gets detailed movie information by ID.
```kotlin
// Usage
viewModelScope.launch {
    moviesRepository.movieContents(movieId).collect { movie ->
        // Update UI with movie details
    }
}
```

#### `discoverMovies(filters: Flow<Filters>): Flow<List<Movie>>`
Discovers movies based on filter criteria.
```kotlin
// Usage with filters
val filtersFlow = flowOf(Filters(genre = "Action", year = 2023))
moviesRepository.discoverMovies(filtersFlow).collect { movies ->
    // Display filtered movies
}
```

### ShowsRepository

Interface for TV shows data operations.

```kotlin
interface ShowsRepository {
    fun getLatestAiredShow(): Flow<Show>
    suspend fun refreshPopularShows(): LiveData<LoadingState>
    fun popularShowsListing(scope: CoroutineScope): Listing<Show>
    suspend fun refreshTopRatedShows(): LiveData<LoadingState>
    fun topRatedShowsListing(scope: CoroutineScope): Listing<Show>
    suspend fun refreshOnTvShows(): LiveData<LoadingState>
    fun onTvShowsListing(scope: CoroutineScope): Listing<Show>
    suspend fun refreshAiringTodayShows(): LiveData<LoadingState>
    fun airingTodayShowsListing(scope: CoroutineScope): Listing<Show>
}
```

**Key Methods:**

#### `popularShowsListing(scope: CoroutineScope): Listing<Show>`
Returns paginated list of popular TV shows.
```kotlin
// Usage in ViewModel
val popularShows = showsRepository.popularShowsListing(viewModelScope)
```

#### `getLatestAiredShow(): Flow<Show>`
Gets the most recently aired TV show.
```kotlin
// Usage
showsRepository.getLatestAiredShow().collect { latestShow ->
    // Update UI with latest show
}
```

### SearchRepository

Interface for search functionality.

```kotlin
interface SearchRepository {
    suspend fun search(query: Flow<String>): Flow<List<Movie>>
    suspend fun keywords(query: Flow<String>): Flow<List<Keyword>>
}
```

**Usage Examples:**
```kotlin
// Search movies
val queryFlow = MutableStateFlow("Avengers")
searchRepository.search(queryFlow).collect { searchResults ->
    // Display search results
}

// Search keywords
searchRepository.keywords(queryFlow).collect { keywords ->
    // Display keyword suggestions
}
```

## Data Models

### Core Entities

#### TmdbEntity
Base interface for all TMDB entities.
```kotlin
interface TmdbEntity {
    val id: Int
}
```

#### Entry Interfaces
```kotlin
interface Entry : TmdbEntity {
    val movieId: Int
}

interface PaginatedEntry : Entry {
    val page: Int
}

interface ShowPaginatedEntry : Entry {
    val page: Int
    val pageOrder: Int
    val showId: Int
}

interface MultipleEntry : Entry {
    val otherMovieId: Int
}
```

### State Management

#### LoadState
Represents the state of data loading operations.
```kotlin
sealed class LoadState<out T> : ErrorGettable {
    object Loading : LoadState<Nothing>()
    class Loaded<T>(val value: T) : LoadState<T>()
    class Error<T>(val e: Throwable) : LoadState<T>()
}
```

**Usage:**
```kotlin
when (loadState) {
    is LoadState.Loading -> showLoadingIndicator()
    is LoadState.Loaded -> displayData(loadState.value)
    is LoadState.Error -> showError(loadState.e)
}
```

#### LoadingState
Represents loading state without data.
```kotlin
sealed class LoadingState : ErrorGettable {
    object Loading : LoadingState()
    object Loaded : LoadingState()
    class Error(val e: Throwable) : LoadingState()
}
```

#### ResultState
Represents operation result with success/error states.
```kotlin
sealed class ResultState<T> : ErrorGettable {
    class Success<T>(val value: T) : ResultState<T>()
    class Error<T>(val e: Throwable) : ResultState<T>()
}
```

### Data Mapping

#### Mapper Interface
For transforming data between different types.
```kotlin
interface Mapper<F, T> {
    suspend fun map(from: F): T
}

interface IndexedMapper<F, T> {
    suspend fun map(index: Int, from: F): T
}
```

**Usage:**
```kotlin
class MovieApiToMovie : Mapper<ApiMovie, Movie> {
    override suspend fun map(from: ApiMovie): Movie {
        return Movie(
            id = from.id,
            title = from.title,
            // ... other mappings
        )
    }
}
```

## UI Components

### Compose Components

The app includes reusable Jetpack Compose components in the `corecomponent/compose` module.

#### Example Text Card Component
```kotlin
@Composable
fun textView() {
    Column(modifier = Modifier.padding(10.dp)) {
        val imageModifier = Modifier
            .preferredHeightIn(maxHeight = 180.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(5.dp))
        Image(
            imageResource(id = R.drawable.ic_baseline_android_24),
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Text(text = "Hari Singh Kulhari", style = MaterialTheme.typography.h6)
        Text(text = "Ranosys", style = MaterialTheme.typography.body2)
        Text(text = "Android Developer", style = MaterialTheme.typography.body2)
    }
}
```

**Usage:**
```kotlin
@Composable
fun MyScreen() {
    textView() // Displays the card component
}
```

## Image Loading

The `image-loading` module provides binding adapters for loading various types of images.

### Binding Adapters

#### YouTube Thumbnail Loading
```kotlin
@BindingAdapter("youtubeThumbnail")
fun ImageView.loadYoutubeThumbnail(key: String?) {
    load("https://img.youtube.com/vi/${key}/hqdefault.jpg")
}
```

**Usage in XML:**
```xml
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:youtubeThumbnail="@{videoKey}" />
```

#### Profile Image Loading
```kotlin
@BindingAdapter("tmdbProfilePath", "circularCrop", requireAll = false)
fun ImageView.loadCircularProfile(path: String?, isCircularCrop: Boolean?)
```

**Usage in XML:**
```xml
<ImageView
    app:tmdbProfilePath="@{profilePath}"
    app:circularCrop="@{true}" />
```

#### Other Image Loading Methods
- `loadPoster(path: String?, size: String?)` - For movie posters
- `loadBackdrop(path: String?, size: String?)` - For backdrop images
- `loadLogo(path: String?, size: String?)` - For logo images
- `loadImage(url: String?)` - For generic image loading

## Dependency Injection

### AppComponent

Main Dagger component that provides app-wide dependencies.

```kotlin
@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        MainActivityModule.MainActivityBuilder::class,
        DbComponentModule::class,
        TmdbApiModule::class,
        RepositoryComponentModule::class,
        ImageLoadingModule::class
    ]
)
interface AppComponent : AndroidInjector<App>, AppComponentInterface
```

### Component Factory
```kotlin
@Component.Factory
interface Factory {
    fun create(@BindsInstance application: Application): AppComponent
}
```

**Usage:**
```kotlin
// Creating the component
fun Application.createAppComponent() = DaggerAppComponent.factory().create(this)
```

### Module Structure

The app uses several Dagger modules:
- **AppModule**: Core app dependencies
- **DbComponentModule**: Database-related dependencies
- **TmdbApiModule**: API service dependencies
- **RepositoryComponentModule**: Repository implementations
- **ImageLoadingModule**: Image loading dependencies

## Usage Examples

### Complete ViewModel Example

```kotlin
class MovieDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    
    private val _movieState = MutableLiveData<LoadState<Movie>>()
    val movieState: LiveData<LoadState<Movie>> = _movieState
    
    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _movieState.value = LoadState.Loading
            try {
                moviesRepository.movieContents(movieId).collect { movie ->
                    _movieState.value = LoadState.Loaded(movie)
                }
            } catch (e: Exception) {
                _movieState.value = LoadState.Error(e)
            }
        }
    }
    
    fun refreshMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                moviesRepository.refreshMovieDetails(movieId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
```

### Fragment Integration Example

```kotlin
class MovieDetailFragment : Fragment(R.layout.movie_detail_fragment) {
    
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    
    private val viewModel: MovieDetailViewModel by viewModels { viewModelFactory }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val movieId = args.movieId
        viewModel.loadMovie(movieId)
        
        viewModel.movieState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Loading -> showLoading()
                is LoadState.Loaded -> displayMovie(state.value)
                is LoadState.Error -> showError(state.e)
            }
        }
    }
}
```

### Search Implementation Example

```kotlin
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    
    private val searchQuery = MutableStateFlow("")
    
    val searchResults: Flow<List<Movie>> = searchQuery
        .debounce(300) // Wait for user to stop typing
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                searchRepository.search(flowOf(query))
            }
        }
        .flowOn(Dispatchers.IO)
    
    fun updateQuery(query: String) {
        searchQuery.value = query
    }
}
```

## Error Handling

### Using ErrorGettable Interface

All state classes implement `ErrorGettable` for consistent error handling:

```kotlin
fun List<ErrorGettable>.firstErrorOrNull(): Throwable? {
    return mapNotNull { it.getErrorIfExists() }.firstOrNull()
}

// Usage
val states = listOf(loadState1, loadState2, loadState3)
val firstError = states.firstErrorOrNull()
firstError?.let { error ->
    // Handle the first error found
}
```

## Best Practices

1. **Use Flow for data streams**: All repository methods return `Flow` for reactive data
2. **Handle loading states**: Always observe `LoadState` for proper UI feedback
3. **Scope coroutines properly**: Use `viewModelScope` in ViewModels, `lifecycleScope` in Activities/Fragments
4. **Inject dependencies**: Use `@Inject` constructor injection for better testability
5. **Use sealed classes**: For representing different states and results
6. **Implement proper error handling**: Use `ErrorGettable` interface for consistent error management

## Module Dependencies

```
app
├── feature/movies
├── feature/shows  
├── feature/search
├── feature/settings
├── feature/authentication
├── feature/account
├── data/repository
├── data/api
├── data/db
├── data/local
├── data/model
├── corecomponent/compose
├── corecomponent/androidcomponents
└── image-loading
```

This documentation covers the main public APIs and components of the TMDB application. For specific implementation details, refer to the individual source files in each module.