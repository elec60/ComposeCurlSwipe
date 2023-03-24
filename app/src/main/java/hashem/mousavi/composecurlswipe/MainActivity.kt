package hashem.mousavi.composecurlswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hashem.mousavi.composecurlswipe.ui.theme.ComposeCurlSwipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val images =
            mutableStateListOf(
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4
            )
        setContent {
            ComposeCurlSwipeTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CurlEffectScreen(
                        images = images,
                        onRemove = {
                            images.removeAt(it)
                        }
                    )
                }

            }
        }
    }
}

