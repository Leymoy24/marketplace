package com.example.marketplace.ui.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketplace.data.model.ProductUiModel

@Composable
fun EndlessGrid(
    lazyGridState: LazyGridState,
    listOfProducts: List<ProductUiModel>?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onReachedBottom: () -> Unit = {},
    onProductClicked: (product: ProductUiModel) -> Unit = {}
) {
    val buffer = 1

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == lazyGridState.layoutInfo.totalItemsCount - buffer
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) onReachedBottom()
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        if (isLoading) {
            items(count = 10) {
                ProductItemShimmerEffect()
            }
        } else {
            itemsIndexed(
                items = listOfProducts ?: listOf(),
                key = { _: Int, item: ProductUiModel ->
                    item.hashCode()
                }
            ) { _, item ->
                ProductItem(productUiModel = item) {
                    onProductClicked(item)
                }
            }
        }
    }
}