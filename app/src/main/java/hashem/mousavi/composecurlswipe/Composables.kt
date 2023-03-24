package hashem.mousavi.composecurlswipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch


@Composable
fun CurlEffectScreen(images: List<Int>, onRemove: (Int) -> Unit) {

    var itemWidth by remember {
        mutableStateOf(0)
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    var progress by remember {
        mutableStateOf(0f)
    }
    val animatable = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()
    var itemIndexToRemove by remember {
        mutableStateOf(-1)
    }
    if (images.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            itemsIndexed(items = images) { index, item ->

                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    if (selectedIndex != index) {
                                        offset = Offset.Zero
                                        progress = 0f
                                    }
                                    if (selectedIndex == -1 && dragAmount.x > 0) {
                                        return@detectDragGestures
                                    }
                                    selectedIndex = index
                                    if (progress < 0.5f || dragAmount.x > 0) {
                                        offset += dragAmount
                                    }
                                    progress = -offset.x / itemWidth
                                }
                            )
                        }
                        .onSizeChanged {
                            itemWidth = it.width
                        }
                        .fillMaxWidth()
                        .height(120.dp)
                        .offset {
                            if (index == itemIndexToRemove) {
                                IntOffset(x = (itemWidth * 1.1f * animatable.value).toInt(), y = 0)
                            } else {
                                IntOffset.Zero
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxSize()
                            .background(
                                color = Color.Red.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterEnd),
                        onClick = {
                            scope.launch {
                                itemIndexToRemove = selectedIndex
                                animatable.animateTo(
                                    1f,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                                onRemove(selectedIndex)
                                itemIndexToRemove = -1
                                selectedIndex = -1
                                animatable.snapTo(0f)
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.scale(scale = progress * 3),
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    CurlItem(
                        modifier = Modifier
                            .clip(shape = if (selectedIndex == index) ClipShape(progress) else RectangleShape)
                            .fillMaxSize(),
                        imageRes = item
                    )
                    CurlItem(
                        modifier = Modifier
                            .clip(shape = if (selectedIndex == index) ClipShape(progress) else RectangleShape)
                            .fillMaxSize()
                            .offset {
                                IntOffset(
                                    x = itemWidth,
                                    y = 0
                                ) + if (selectedIndex == index) {
                                    IntOffset(
                                        x = (offset.x * 1.8f).toInt(),
                                        y = 0
                                    )
                                } else {
                                    IntOffset.Zero
                                }
                            }
                            .graphicsLayer {
                                scaleX = -1f
                                scaleY = -1f
                                rotationX = 180f
                            },
                        imageRes = item
                    )
                }
            }


        }
    } else {
        Text(text = "Empty", fontSize = 30.sp, color = Color.Black)
    }
}


@Composable
fun CurlItem(
    modifier: Modifier,
    imageRes: Int,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = imageRes),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

class ClipShape(private val progress: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            addRoundRect(
                roundRect = RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width * (1 - progress),
                    bottom = size.height
                )
            )
        }
        return Outline.Generic(path)
    }

}