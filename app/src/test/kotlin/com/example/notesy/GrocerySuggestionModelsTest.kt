package com.example.notesy

import com.example.notesy.utils.grocery.SuggestedItem
import org.junit.Assert.assertEquals
import org.junit.Test

class GrocerySuggestionModelsTest {

    @Test
    fun `SuggestedItem key composes store and item with pipe`() {
        val item = SuggestedItem(storeName = "StoreX", itemName = "Milk")
        assertEquals("StoreX|Milk", item.key)
    }
}

