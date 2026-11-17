package com.example.ii3510_2627_animations

data class Listing(
    val id: Int,
    val title: String,
    val category: String,
    val price: Int,
    val symbol: String,
    val seller: String,
    val description: String
)

val sampleListings = listOf(
    Listing(1, "Kotlin essentials", "Books", 15, "📚", "Alex", "A practical Kotlin textbook with clear examples. A few notes in the margins, otherwise in great condition."),
    Listing(2, "City bicycle", "Transport", 75, "🚲", "Sam", "A comfortable bicycle for the trip to campus. Recently serviced, with a lock included. Collection near the library."),
    Listing(3, "Wireless headphones", "Tech", 30, "🎧", "Jordan", "Over-ear headphones with a soft fit and a rechargeable battery. Includes the charging cable."),
    Listing(4, "Desk lamp", "Home", 12, "💡", "Morgan", "An adjustable reading lamp for your desk. Warm light and a small footprint. Works perfectly."),
    Listing(5, "Calculus workbook", "Books", 8, "📖", "Jamie", "Exercises and worked examples for first-year calculus. Most pages are unused."),
    Listing(6, "Compact keyboard", "Tech", 22, "⌨️", "Taylor", "A quiet USB keyboard for studying in a shared room. Tested and cleaned. Cable included."),
    Listing(7, "Coffee maker", "Home", 20, "☕", "Casey", "A small coffee maker for early lectures. Easy to clean and ideal for a student kitchen."),
    Listing(8, "Skateboard", "Transport", 25, "🛹", "Riley", "A beginner-friendly board with smooth wheels. Some cosmetic scratches from normal use."),
    Listing(9, "Laptop stand", "Tech", 10, "💻", "Drew", "A folding stand that raises your screen to a comfortable height. Lightweight and easy to carry."),
    Listing(10, "Indoor plant", "Home", 6, "🌱", "Quinn", "A low-maintenance plant in a ceramic pot. A little green for your room or study space.")
)

val listingCategories = listOf("All", "Books", "Tech", "Home", "Transport")

fun filterListings(category: String): List<Listing> =
    sampleListings.filter { category == "All" || it.category == category }

// Only the first six cards stagger. Even a long list finishes revealing in 350 ms.
fun entranceDelay(index: Int, reducedMotion: Boolean): Long =
    // TODO 1a: return a bounded start delay. First item: 0 ms.
    // Items 1..5: 70 ms intervals. Later items: 350 ms. Reduced motion: 0 ms.
    0L

fun displayListings(category: String, sortByPrice: Boolean): List<Listing> =
    filterListings(category).let { listings ->
        if (sortByPrice) listings.sortedWith(compareBy<Listing> { it.price }.thenBy { it.id }) else listings
    }
