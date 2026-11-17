package com.example.ii3510_2627_animations

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.ii3510_2627_animations.ui.theme.II3510_2627_AnimationsTheme

@Composable
fun CampusDemo() {
    var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }
    var category by rememberSaveable { mutableStateOf("All") }
    var sortByPrice by rememberSaveable { mutableStateOf(false) }
    var favouriteIds by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    val toggleFavourite: (Int) -> Unit = { id ->
        favouriteIds = if (id in favouriteIds) favouriteIds - id else favouriteIds + id
    }
    var reducedMotion by rememberSaveable { mutableStateOf(false) }
    var replay by rememberSaveable { mutableIntStateOf(0) }
    var hasLoaded by rememberSaveable { mutableStateOf(false) }
    var loading by remember { mutableStateOf(!hasLoaded) }
    // This state lives above LazyColumn and the screen transition. Scrolling or
    // returning from details must not trigger entrances a second time.
    var visibleIds by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    val listState = rememberLazyListState()

    LaunchedEffect(replay) {
        if (hasLoaded && replay == 0) return@LaunchedEffect
        listState.requestScrollToItem(0)
        visibleIds = emptyList()
        loading = true
        delay(450) // Simulated request. The data is entirely local.
        loading = false
        hasLoaded = true
        // The reveal schedule runs once per load, outside lazy item composition.
        sampleListings.forEachIndexed { index, listing ->
            delay(entranceDelay(index, reducedMotion) - entranceDelay(index - 1, reducedMotion))
            visibleIds = (visibleIds + listing.id).distinct()
        }
    }
    LaunchedEffect(reducedMotion) {
        if (reducedMotion && !loading && hasLoaded) visibleIds = sampleListings.map { it.id }
    }
    BackHandler(enabled = selectedId != null) { selectedId = null }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Column(Modifier.navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { selectedId = null; category = "All"; sortByPrice = false; replay++ },
                            modifier = Modifier.testTag("replay")
                        ) { Text("Replay") }
                        Spacer(Modifier.weight(1f))
                        Text("Reduced motion", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = reducedMotion,
                            onCheckedChange = { reducedMotion = it },
                            modifier = Modifier.testTag("reduced-motion")
                                .semantics { contentDescription = "Reduced motion" }
                        )
                    }
                    Text(
                        if (reducedMotion) "Motion off · instant transitions" else "70 ms stagger · 280 ms entrance",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.padding(horizontal = 24.dp, vertical = 18.dp)) {
                Text("CAMPUS FINDS", color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge, letterSpacing = 3.sp)
                Text("Good finds. New stories.", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                Text("A little less new. A lot more useful.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            }
            // A stable Boolean target prevents a data update from replacing the
            // entire list. The lambda target controls which screen is drawn.
            AnimatedContent(
                targetState = selectedId != null,
                transitionSpec = {
                    // TODO 3: combine fading and horizontal sliding in both directions.
                    // targetState=true enters details; false returns to the list.
                    // Reduced motion must use no enter or exit transition.
                    EnterTransition.None togetherWith ExitTransition.None
                }, label = "list-details", modifier = Modifier.weight(1f)
            ) { showDetails ->
                if (showDetails) {
                    // Retain the last detail while its exit transition runs.
                    var detailId by remember { mutableStateOf(selectedId) }
                    if (selectedId != null) detailId = selectedId
                    sampleListings.firstOrNull { it.id == detailId }?.let { listing ->
                        ListingDetails(listing = listing, reducedMotion = reducedMotion,
                            favourite = listing.id in favouriteIds,
                            onFavourite = { toggleFavourite(listing.id) }, onBack = { selectedId = null })
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listingCategories) { label ->
                                FilterChip(selected = category == label, onClick = { category = label },
                                    label = { Text(label) }, modifier = Modifier.testTag("category-$label"))
                            }
                        }
                        Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            AnimatedContent(
                                targetState = filterListings(category).size,
                                transitionSpec = {
                                    if (reducedMotion) {
                                        (EnterTransition.None togetherWith ExitTransition.None)
                                            .using(SizeTransform(sizeAnimationSpec = { _, _ -> tween(0) }))
                                    } else {
                                        (slideInVertically(tween(220)) { it } + fadeIn(tween(220))) togetherWith
                                            (slideOutVertically(tween(220)) { -it } + fadeOut(tween(150)))
                                    }
                                }, label = "listing-count", modifier = Modifier.weight(1f)
                            ) { count ->
                                Text("$count local listings", style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            TextButton(onClick = { sortByPrice = !sortByPrice; listState.requestScrollToItem(0) }, modifier = Modifier.testTag("sort")) {
                                Text(if (sortByPrice) "Price: low to high" else "Sort by price")
                            }
                        }
                        Box(Modifier.weight(1f)) {
                            // Keep one LazyColumn alive while filters change so
                            // animateItem can track insertion, removal and placement.
                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().testTag("listings"),
                                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                if (!loading) {
                                    items(displayListings(category, sortByPrice), key = { it.id }) { listing ->
                                        ListingCard(listing, listing.id in visibleIds, reducedMotion,
                                            favourite = listing.id in favouriteIds,
                                            onFavourite = { toggleFavourite(listing.id) },
                                            // TODO 2: use animateItem here, inside the lazy item scope.
                                            // Animate insertion, removal and placement. Respect reducedMotion.
                                            modifier = Modifier, onClick = { selectedId = listing.id })
                                    }
                                }
                            }
                            if (loading) {
                                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(Modifier.size(32.dp))
                                    Text("Finding your next favourite…", modifier = Modifier.padding(top = 14.dp),
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListingCard(listing: Listing, visible: Boolean, reducedMotion: Boolean,
                        favourite: Boolean, onFavourite: () -> Unit,
                        modifier: Modifier = Modifier, onClick: () -> Unit) {
    // TODO 1b: animate this value over 280 ms (0 ms for reduced motion).
    val progress = if (visible) 1f else 0f
    val offset = with(LocalDensity.current) { 24.dp.toPx() }
    Card(onClick = onClick, enabled = visible,
        modifier = modifier.fillMaxWidth().testTag("listing-${listing.id}").graphicsLayer {
            alpha = progress
            translationY = (1f - progress) * offset
        }, shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            ListingArtwork(listing, Modifier.size(76.dp))
            Column(Modifier.weight(1f).padding(start = 16.dp)) {
                Text(listing.category.uppercase(), style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary, letterSpacing = 1.sp)
                Text(listing.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(vertical = 4.dp))
                Text("€${listing.price}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            FavouriteButton(listing.id, favourite, reducedMotion, onFavourite, enabled = visible)
        }
    }
}

@Composable
private fun ListingArtwork(listing: Listing, modifier: Modifier = Modifier, large: Boolean = false) {
    val tint = when (listing.category) {
        "Books" -> Color(0xFFEAE6F4)
        "Tech" -> Color(0xFFDDECF5)
        "Home" -> Color(0xFFF8E8D7)
        else -> Color(0xFFDFEDE4)
    }
    Box(modifier.background(tint, RoundedCornerShape(if (large) 28.dp else 16.dp)),
        contentAlignment = Alignment.Center) {
        Text(listing.symbol, fontSize = if (large) 88.sp else 36.sp,
            modifier = Modifier.semantics { contentDescription = "${listing.category} illustration" })
    }
}

@Composable
private fun ListingDetails(listing: Listing, reducedMotion: Boolean, favourite: Boolean,
                           onFavourite: () -> Unit, onBack: () -> Unit) {
    var pickupExpanded by rememberSaveable(listing.id) { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(if (pickupExpanded) 180f else 0f,
        tween(if (reducedMotion) 0 else 220), label = "pickup-chevron")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
        .testTag("details"), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp), modifier = Modifier.testTag("back")) {
                Text("‹  Back to listings")
            }
            Spacer(Modifier.weight(1f))
            FavouriteButton(listing.id, favourite, reducedMotion, onFavourite)
        }
        ListingArtwork(listing, Modifier.fillMaxWidth().height(200.dp), large = true)
        Text(listing.category.uppercase(), style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp)
        Row(verticalAlignment = Alignment.Top) {
            Text(listing.title, style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("€${listing.price}", style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 12.dp))
        }
        Text(listing.description, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
        Text("Listed by ${listing.seller}", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { pickupExpanded = !pickupExpanded },
            contentPadding = PaddingValues(0.dp), modifier = Modifier.testTag("pickup-toggle")) {
            Text("Pickup details", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text("⌄", fontSize = 24.sp, modifier = Modifier.graphicsLayer { rotationZ = chevronRotation })
        }
        AnimatedVisibility(visible = pickupExpanded,
            // TODO 4: add expanding/fading and shrinking/fading transitions.
            // Keep both transitions disabled when reducedMotion is true.
            enter = EnterTransition.None,
            exit = ExitTransition.None) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.testTag("pickup-info")) {
                Text("Meet near the campus library", style = MaterialTheme.typography.titleSmall)
                Text("Pickup is available Monday to Friday, 12:00–18:00. Arrange a time with ${listing.seller} and inspect the item before collecting it.",
                    style = MaterialTheme.typography.bodyMedium)
                Text("Sample pickup instructions", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("This offline demo shows interface animations. Listings are fictional.",
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
    }
}

// Spring scaling demonstrates a small interaction animation. State is owned by
// the screen, so saving a listing carries across filtering and detail navigation.
@Composable
private fun FavouriteButton(id: Int, selected: Boolean, reducedMotion: Boolean,
                            onToggle: () -> Unit, enabled: Boolean = true) {
    val scale by animateFloatAsState(if (selected) 1.18f else 1f,
        animationSpec = if (reducedMotion) tween(0) else spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "favourite-$id-scale")
    val tint by animateColorAsState(
        if (selected) Color(0xFFC44555) else MaterialTheme.colorScheme.onSurfaceVariant,
        tween(if (reducedMotion) 0 else 220), label = "favourite-$id-color")
    IconToggleButton(checked = selected, onCheckedChange = { onToggle() }, enabled = enabled,
        modifier = Modifier.testTag("favourite-$id").semantics {
            contentDescription = if (selected) "Remove from favourites" else "Save to favourites"
        }) {
        Text(if (selected) "♥" else "♡", color = tint, fontSize = 28.sp,
            modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale })
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoPreview() {
    II3510_2627_AnimationsTheme { CampusDemo() }
}
