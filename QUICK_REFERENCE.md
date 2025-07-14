# TMDB App - Quick Reference Guide

## 🚀 Quick Start

### Essential Imports
```kotlin
// Repositories
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.model.repository.ShowsRepository
import com.hari.tmdb.model.repository.SearchRepository

// Models
import com.hari.tmdb.model.LoadState
import com.hari.tmdb.model.LoadingState
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.Show

// Dependency Injection
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel // if using Hilt
```

### Basic ViewModel Setup
```kotlin
class MyViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    
    private val _state = MutableLiveData<LoadState<List<Movie>>>()
    val state: LiveData<LoadState<List<Movie>>> = _state
    
    fun loadData() {
        viewModelScope.launch {
            _state.value = LoadState.Loading
            try {
                // Your repository call here
            } catch (e: Exception) {
                _state.value = LoadState.Error(e)
            }
        }
    }
}
```

## 📊 Repository Methods Cheat Sheet

### MoviesRepository
| Method | Description | Return Type |
|--------|-------------|-------------|
| `getMovieGenres()` | Get all movie genres | `Flow<List<Genre>>` |
| `movieContents(id)` | Get movie details | `Flow<Movie>` |
| `discoverMovies(filters)` | Discover movies with filters | `Flow<List<Movie>>` |
| `moviesPagedList(category, scope)` | Get paginated movies | `Listing<Movie>` |
| `refreshMovieDetails(id)` | Refresh movie data | `suspend fun` |

### ShowsRepository
| Method | Description | Return Type |
|--------|-------------|-------------|
| `getLatestAiredShow()` | Get latest aired show | `Flow<Show>` |
| `popularShowsListing(scope)` | Get popular shows | `Listing<Show>` |
| `topRatedShowsListing(scope)` | Get top rated shows | `Listing<Show>` |
| `refreshPopularShows()` | Refresh popular shows | `LiveData<LoadingState>` |

### SearchRepository
| Method | Description | Return Type |
|--------|-------------|-------------|
| `search(query)` | Search movies | `Flow<List<Movie>>` |
| `keywords(query)` | Search keywords | `Flow<List<Keyword>>` |

## 🎨 UI State Management

### LoadState Usage
```kotlin
// Observe state
viewModel.movieState.observe(this) { state ->
    when (state) {
        is LoadState.Loading -> {
            progressBar.isVisible = true
            errorText.isVisible = false
        }
        is LoadState.Loaded -> {
            progressBar.isVisible = false
            displayMovies(state.value)
        }
        is LoadState.Error -> {
            progressBar.isVisible = false
            errorText.isVisible = true
            errorText.text = state.e.message
        }
    }
}
```

### Flow Collection in Fragment
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.moviesFlow.collect { movies ->
            adapter.submitList(movies)
        }
    }
}
```

## 🖼️ Image Loading Quick Reference

### Common Binding Adapters
```xml
<!-- Movie Poster -->
<ImageView
    app:tmdbPosterPath="@{movie.posterPath}"
    app:tmdbPosterSize="@{@string/tmdb_image_size_w342}" />

<!-- Profile Image -->
<ImageView
    app:tmdbProfilePath="@{person.profilePath}"
    app:circularCrop="@{true}" />

<!-- Backdrop Image -->
<ImageView
    app:tmdbBackdropPath="@{movie.backdropPath}"
    app:tmdbBackdropSize="@{@string/tmdb_image_size_w780}" />

<!-- YouTube Thumbnail -->
<ImageView
    app:youtubeThumbnail="@{video.key}" />
```

## 🔍 Search Implementation

### Simple Search
```kotlin
// In ViewModel
private val searchQuery = MutableStateFlow("")

val searchResults = searchQuery
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query ->
        if (query.isBlank()) flowOf(emptyList())
        else searchRepository.search(flowOf(query))
    }

fun search(query: String) {
    searchQuery.value = query
}
```

### Search with Keywords
```kotlin
val keywordResults = searchQuery
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query ->
        if (query.isBlank()) flowOf(emptyList())
        else searchRepository.keywords(flowOf(query))
    }
```

## 🎯 Dependency Injection Patterns

### Component Access
```kotlin
// In Application
class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}
```

### Fragment Injection
```kotlin
class MovieFragment : Fragment(), Injectable {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    
    private val viewModel by viewModels<MovieViewModel> { viewModelFactory }
}
```

## 🔄 Common Patterns

### Loading with Refresh
```kotlin
class MovieListViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {
    
    fun loadMovies(category: MovieCategory) {
        viewModelScope.launch {
            val refreshJob = async { repository.refreshMoviesByCategory(category) }
            val moviesFlow = repository.moviesPagedList(category, viewModelScope)
            
            // Observe refresh status
            refreshJob.await().observe(viewLifecycleOwner) { loadingState ->
                when (loadingState) {
                    is LoadingState.Loading -> showRefreshIndicator()
                    is LoadingState.Loaded -> hideRefreshIndicator()
                    is LoadingState.Error -> showError(loadingState.e)
                }
            }
        }
    }
}
```

### Error Handling
```kotlin
// Using ErrorGettable
val errorStates = listOf(state1, state2, state3)
val firstError = errorStates.firstErrorOrNull()

firstError?.let { error ->
    when (error) {
        is NetworkException -> showNetworkError()
        is TimeoutException -> showTimeoutError()
        else -> showGenericError(error.message)
    }
}
```

## 📱 Compose Components

### Basic Usage
```kotlin
@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Column {
            // Use the app's image loading
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.h6
            )
        }
    }
}
```

## 🛠️ Debugging Tips

### Logging Flow Data
```kotlin
repository.movieContents(movieId)
    .onEach { movie -> Log.d("MovieFlow", "Received: ${movie.title}") }
    .catch { error -> Log.e("MovieFlow", "Error", error) }
    .collect { movie -> updateUI(movie) }
```

### State Debugging
```kotlin
_state.observe(this) { state ->
    Log.d("State", "Current state: ${state::class.simpleName}")
    when (state) {
        is LoadState.Error -> Log.e("State", "Error: ${state.e.message}", state.e)
        // ... other states
    }
}
```

## 🔧 Common Issues & Solutions

### Issue: Flow not collecting
**Solution:** Make sure to use appropriate scope
```kotlin
// ❌ Wrong
GlobalScope.launch { flow.collect { } }

// ✅ Correct
viewLifecycleOwner.lifecycleScope.launch { flow.collect { } }
```

### Issue: Memory leaks with LiveData
**Solution:** Use lifecycle-aware observers
```kotlin
// ❌ Wrong
viewModel.data.observeForever { }

// ✅ Correct
viewModel.data.observe(viewLifecycleOwner) { }
```

### Issue: Dependency injection not working
**Solution:** Ensure proper module setup and component hierarchy
```kotlin
// Check that your fragment/activity implements Injectable
class MyFragment : Fragment(), Injectable

// Ensure proper component modules are included
@Component(modules = [RepositoryModule::class, ...])
```

## 📖 Related Documentation

- [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) - Complete API reference
- [README.md](./README.md) - Project overview and setup
- Individual module documentation in respective directories

---
*This quick reference is designed for developers already familiar with Android development patterns. For detailed explanations, see the complete API documentation.*