package yangfentuozi.batteryrecorder.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 应用统一圆角形状定义
 * 使用 MD3 标准 RoundedCornerShape
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// ============================================
// 按钮组专用形状（Material 3 Expressive 设计）
// ============================================
val ButtonGridTopStart = RoundedCornerShape(
    topStart = 24.dp,
    topEnd = 6.dp,
    bottomStart = 6.dp,
    bottomEnd = 6.dp
)

val ButtonGridTopEnd = RoundedCornerShape(
    topStart = 6.dp,
    topEnd = 24.dp,
    bottomStart = 6.dp,
    bottomEnd = 6.dp
)

val ButtonGridBottomStart = RoundedCornerShape(
    topStart = 6.dp,
    topEnd = 6.dp,
    bottomStart = 24.dp,
    bottomEnd = 6.dp
)

val ButtonGridBottomEnd = RoundedCornerShape(
    topStart = 6.dp,
    topEnd = 6.dp,
    bottomStart = 6.dp,
    bottomEnd = 24.dp
)

// ============================================
// 拼接列组专用形状
// ============================================
val SplicedGroupSingle = RoundedCornerShape(16.dp)
val SplicedGroupTop = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = 6.dp,
    bottomEnd = 6.dp
)
val SplicedGroupBottom = RoundedCornerShape(
    topStart = 6.dp,
    topEnd = 6.dp,
    bottomStart = 16.dp,
    bottomEnd = 16.dp
)
val SplicedGroupMiddle = RoundedCornerShape(6.dp)

// ============================================
// 向后兼容对象 (deprecated, use AppShapes directly)
// ============================================
@Deprecated("Use AppShapes instead", ReplaceWith("AppShapes"))
object AppShape {
    val extraSmall = RoundedCornerShape(4.dp)
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val extraLarge = RoundedCornerShape(28.dp)

    object SplicedGroup {
        val single = SplicedGroupSingle
        val top = SplicedGroupTop
        val bottom = SplicedGroupBottom
        val middle = SplicedGroupMiddle
    }

    object ButtonGrid {
        val topStart = ButtonGridTopStart
        val topEnd = ButtonGridTopEnd
        val bottomStart = ButtonGridBottomStart
        val bottomEnd = ButtonGridBottomEnd
    }
}
