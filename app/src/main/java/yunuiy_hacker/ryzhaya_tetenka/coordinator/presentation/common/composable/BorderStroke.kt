package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSimple
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp

fun Modifier.dashedBorder(border: BorderStroke, shape: Shape = RectangleShape, on: Dp, off: Dp) =
    dashedBorder(width = border.width, brush = border.brush, shape = shape, on, off)

fun Modifier.dashedBorder(width: Dp, color: Color, shape: Shape = RectangleShape, on: Dp, off: Dp) =
    dashedBorder(width, SolidColor(color), shape, on, off)

fun Modifier.dashedBorder(width: Dp, brush: Brush, shape: Shape, on: Dp, off: Dp): Modifier =
    composed(
        factory = {
            this.then(
                Modifier.drawWithCache {
                    val outline: Outline = shape.createOutline(size, layoutDirection, this)
                    val borderSize = if (width == Dp.Hairline) 1f else width.toPx()

                    var insetOutline: Outline? = null
                    var stroke: Stroke? = null
                    var pathClip: Path? = null
                    var inset = 0f
                    var insetPath: Path? = null
                    if (borderSize > 0 && size.minDimension > 0f) {
                        if (outline is Outline.Rectangle) {
                            stroke = Stroke(
                                borderSize, pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on.toPx(), off.toPx())
                                )
                            )
                        } else {
                            val strokeWidth = 1.2f * borderSize
                            inset = borderSize - strokeWidth / 2
                            val insetSize = Size(
                                size.width - inset * 2,
                                size.height - inset * 2
                            )
                            insetOutline = shape.createOutline(insetSize, layoutDirection, this)
                            stroke = Stroke(
                                strokeWidth, pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on.toPx(), off.toPx())
                                )
                            )
                            pathClip = if (outline is Outline.Rounded) {
                                Path().apply { addRoundRect(outline.roundRect) }
                            } else if (outline is Outline.Generic) {
                                outline.path
                            } else {
                                null
                            }

                            insetPath =
                                if (insetOutline is Outline.Rounded &&
                                    !insetOutline.roundRect.isSimple
                                ) {
                                    Path().apply {
                                        addRoundRect(insetOutline.roundRect)
                                        translate(Offset(inset, inset))
                                    }
                                } else if (insetOutline is Outline.Generic) {
                                    Path().apply {
                                        addPath(insetOutline.path, Offset(inset, inset))
                                    }
                                } else {
                                    null
                                }
                        }
                    }

                    onDrawWithContent {
                        drawContent()
                        if (stroke != null) {
                            if (insetOutline != null && pathClip != null) {
                                val isSimpleRoundRect = insetOutline is Outline.Rounded &&
                                        insetOutline.roundRect.isSimple
                                withTransform({
                                    clipPath(pathClip)
                                    if (isSimpleRoundRect) {
                                        translate(inset, inset)
                                    }
                                }) {
                                    if (isSimpleRoundRect) {
                                        val rrect = (insetOutline as Outline.Rounded).roundRect
                                        drawRoundRect(
                                            brush = brush,
                                            topLeft = Offset(rrect.left, rrect.top),
                                            size = Size(rrect.width, rrect.height),
                                            cornerRadius = rrect.topLeftCornerRadius,
                                            style = stroke
                                        )
                                    } else if (insetPath != null) {
                                        drawPath(insetPath, brush, style = stroke)
                                    }
                                }
                                clipRect {
                                    if (isSimpleRoundRect) {
                                        val rrect = (outline as Outline.Rounded).roundRect
                                        drawRoundRect(
                                            brush = brush,
                                            topLeft = Offset(rrect.left, rrect.top),
                                            size = Size(rrect.width, rrect.height),
                                            cornerRadius = rrect.topLeftCornerRadius,
                                            style = Stroke(
                                                Stroke.HairlineWidth,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    floatArrayOf(on.toPx(), off.toPx())
                                                )
                                            )
                                        )
                                    } else {
                                        drawPath(
                                            pathClip, brush = brush, style = Stroke(
                                                Stroke.HairlineWidth,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    floatArrayOf(on.toPx(), off.toPx())
                                                )
                                            )
                                        )
                                    }
                                }
                            } else {
                                val strokeWidth = stroke.width
                                val halfStrokeWidth = strokeWidth / 2
                                drawRect(
                                    brush = brush,
                                    topLeft = Offset(halfStrokeWidth, halfStrokeWidth),
                                    size = Size(
                                        size.width - strokeWidth,
                                        size.height - strokeWidth
                                    ),
                                    style = stroke
                                )
                            }
                        }
                    }
                }
            )
        },
        inspectorInfo = debugInspectorInfo {
            name = "border"
            properties["width"] = width
            if (brush is SolidColor) {
                properties["color"] = brush.value
                value = brush.value
            } else {
                properties["brush"] = brush
            }
            properties["shape"] = shape
        }
    )