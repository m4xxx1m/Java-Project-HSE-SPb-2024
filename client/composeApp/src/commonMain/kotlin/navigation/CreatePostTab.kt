package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ui.NewPostForm

object CreatePostTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.AddCircle)
            val title = "Create Post"
            val index: UShort = 2u
            return TabOptions(index, title, icon)
        }

    @Composable
    override fun Content() {
        NewPostForm()
    }
}
