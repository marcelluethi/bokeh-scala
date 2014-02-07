package org.continuumio.bokeh

import scala.reflect.ClassTag

import play.api.libs.json.{Json,Reads,Writes,Format}
import breeze.linalg.DenseVector

import org.continuumio.bokeh.macros.JsonImpl

object BokehJson {
    def writes[T]: Writes[T] = macro JsonImpl.writesImpl[T]
    def enum[T <: Enum]: Writes[T] = macro JsonImpl.enumImpl[T]
}

object Formats {
    implicit def DenseVectorWrites[T:Writes:ClassTag]: Writes[DenseVector[T]] = new Writes[DenseVector[T]] {
        def writes(vec: DenseVector[T]) =
            implicitly[Writes[Array[T]]].writes(vec.toArray)
    }

    implicit val LineJoinJSON = BokehJson.enum[LineJoin]
    implicit val LineDashJSON = BokehJson.enum[LineDash]
    implicit val LineCapJSON = BokehJson.enum[LineCap]
    implicit val FontStyleJSON = BokehJson.enum[FontStyle]
    implicit val TextAlignJSON = BokehJson.enum[TextAlign]
    implicit val BaselineJSON = BokehJson.enum[Baseline]
    implicit val DirectionJSON = BokehJson.enum[Direction]
    implicit val OrientationJSON = BokehJson.enum[Orientation]
    implicit val UnitsJSON = BokehJson.enum[Units]
    implicit val AngleUnitsJSON = BokehJson.enum[AngleUnits]
    implicit val DimensionJSON = BokehJson.enum[Dimension]
    implicit val LocationJSON = BokehJson.enum[Location]
    implicit val ColorJSON = BokehJson.enum[Color]

    /*
    implicit val AnnularWedgeJSON = BokehJson.writes[AnnularWedge]
    implicit val AnnulusJSON = BokehJson.writes[Annulus]
    implicit val ArcJSON = BokehJson.writes[Arc]
    implicit val BezierJSON = BokehJson.writes[Bezier]
    implicit val ImageJSON = BokehJson.writes[Image]
    implicit val ImageURIJSON = BokehJson.writes[ImageURI]
    implicit val ImageRGBAJSON = BokehJson.writes[ImageRGBA]
    implicit val LineJSON = BokehJson.writes[Line]
    implicit val MultiLineJSON = BokehJson.writes[MultiLine]
    implicit val OvalJSON = BokehJson.writes[Oval]
    implicit val PatchJSON = BokehJson.writes[Patch]
    implicit val PatchesJSON = BokehJson.writes[Patches]
    implicit val QuadJSON = BokehJson.writes[Quad]
    implicit val QuadraticJSON = BokehJson.writes[Quadratic]
    implicit val RayJSON = BokehJson.writes[Ray]
    implicit val RectJSON = BokehJson.writes[Rect]
    implicit val SegmentJSON = BokehJson.writes[Segment]
    implicit val TextJSON = BokehJson.writes[Text]
    implicit val WedgeJSON = BokehJson.writes[Wedge]

    implicit val LinearAxisJSON = BokehJson.writes[LinearAxis]
    */
    implicit val DatetimeAxisJSON = BokehJson.writes[DatetimeAxis]
    /*
    implicit val GridJSON = BokehJson.writes[Grid]

    implicit val CircleJSON = BokehJson.writes[Circle]
    implicit val SquareJSON = BokehJson.writes[Square]
    implicit val TriangleJSON = BokehJson.writes[Triangle]
    implicit val CrossJSON = BokehJson.writes[Cross]
    implicit val XmarkerJSON = BokehJson.writes[Xmarker]
    implicit val DiamondJSON = BokehJson.writes[Diamond]
    implicit val InvertedTriangleJSON = BokehJson.writes[InvertedTriangle]
    implicit val SquareXJSON = BokehJson.writes[SquareX]
    implicit val AsteriskJSON = BokehJson.writes[Asterisk]
    implicit val DiamondCrossJSON = BokehJson.writes[DiamondCross]
    implicit val CircleCrossJSON = BokehJson.writes[CircleCross]
    implicit val HexStarJSON = BokehJson.writes[HexStar]
    implicit val SquareCrossJSON = BokehJson.writes[SquareCross]
    implicit val CircleXJSON = BokehJson.writes[CircleX]
    */

    //implicit val PlotContextJSON = BokehJson.writes[PlotContext]

    /*
    implicit val PlotJSON = BokehJson.writes[Plot]
    implicit val GridPlotJSON = BokehJson.writes[GridPlot]

    implicit val Range1dJSON = BokehJson.writes[Range1d]
    implicit val DataRange1dJSON = BokehJson.writes[DataRange1d]

    implicit val GlyphJSON = BokehJson.writes[Glyph]

    implicit val ColumnDataSourceJSON = BokehJson.writes[ColumnDataSource]

    implicit val PanToolJSON = BokehJson.writes[PanTool]
    implicit val WheelZoomToolJSON = BokehJson.writes[WheelZoomTool]
    implicit val PreviewSaveToolJSON = BokehJson.writes[PreviewSaveTool]
    implicit val EmbedToolJSON = BokehJson.writes[EmbedTool]
    implicit val ResetToolJSON = BokehJson.writes[ResetTool]
    implicit val ResizeToolJSON = BokehJson.writes[ResizeTool]
    implicit val CrosshairToolJSON = BokehJson.writes[CrosshairTool]
    implicit val BoxZoomToolJSON = BokehJson.writes[BoxZoomTool]
    implicit val BoxSelectToolJSON = BokehJson.writes[BoxSelectTool]
    */
}
