package io.continuum.bokeh

trait HasFields { self =>
    type SelfType = self.type

    def typeName: String = Utils.getClassName(this)

    def fields: List[FieldRef]

    def fieldsToJson(all: Boolean = false): Js.Obj =  {
        Js.Obj(fields.collect {
            case FieldRef(name, field) if all || field.isDirty => (name, field.toJson)
        }: _*)
    }

    class Field[FieldType:Default:Json.Writer] extends AbstractField with ValidableField {
        type ValueType = FieldType

        def owner: SelfType = self

        def this(default: FieldType) = {
            this()
            setValue(Some(default))
        }

        def this(default: () => FieldType) = {
            this()
            set(Some(default()))
        }

        def defaultValue: Option[FieldType] = {
            Option(implicitly[Default[FieldType]].default)
        }

        protected var _value: Option[FieldType] = defaultValue
        protected var _dirty: Boolean = false

        final def isDirty: Boolean = _dirty

        def valueOpt: Option[FieldType] = _value

        def value: FieldType = valueOpt.get

        def setValue(value: Option[FieldType]) {
            value.foreach(validates)
            _value = value
        }

        def set(value: Option[FieldType]) {
            setValue(value)
            _dirty = true
        }

        final def :=(value: FieldType) {
            set(Some(value))
        }

        final def <<=(fn: FieldType => FieldType) {
            set(valueOpt.map(fn))
        }

        final def apply(value: FieldType): SelfType = {
            set(Some(value))
            owner
        }

        final def apply(): SelfType = {
            set(None)
            owner
        }

        override def toJson: Js.Value = Json.writeJs(valueOpt)
    }
}

trait Vectorization { self: HasFields =>
    class Vectorized[FieldType:Default:Json.Writer] extends Field[FieldType] {
        def this(value: FieldType) = {
            this()
            set(Some(value))
        }

        protected var _field: Option[Symbol] = None
        def fieldOpt: Option[Symbol] = _field
        def field: Symbol = _field.get

        def setField(field: Option[Symbol]) {
            _field = field
            _dirty = true
        }

        def apply(field: Symbol): SelfType = {
            setField(Some(field))
            owner
        }

        def apply[M[_], T <: FieldType](column: ColumnDataSource#Column[M, T]): SelfType = {
            setField(Some(column.name))
            owner
        }

        def apply[M[_], T <% FieldType](column: ColumnDataSource#Column[M, T]): SelfType = {
            setField(Some(column.name))
            owner
        }

        private case class Value(value: ValueType)
        private case class Field(field: Symbol)

        override def toJson: Js.Value = {
            fieldOpt.map(field => Json.writeJs(Field(field)))
                    .getOrElse(Json.writeJs(valueOpt.map(Value)))
        }
    }

    abstract class VectorizedWithUnits[FieldType:Default:Json.Writer, UnitsType <: Units with EnumType: Default] extends Vectorized[FieldType] {
        def defaultUnits: UnitsType = implicitly[Default[UnitsType]].default

        protected var _units: UnitsType = defaultUnits
        def units: UnitsType = _units

        def setUnits(units: UnitsType) {
            _units = units
            _dirty = true
        }

        def apply(units: UnitsType): SelfType = {
            setUnits(units)
            owner
        }

        def apply(value: FieldType, units: UnitsType): SelfType = {
            set(Some(value))
            setUnits(units)
            owner
        }

        def apply(field: Symbol, units: UnitsType): SelfType = {
            setField(Some(field))
            setUnits(units)
            owner
        }

        def apply[M[_], T <: FieldType](column: ColumnDataSource#Column[M, T], units: UnitsType): SelfType = {
            setUnits(units)
            apply(column)
        }

        def apply[M[_], T <% FieldType](column: ColumnDataSource#Column[M, T], units: UnitsType): SelfType = {
            setUnits(units)
            apply(column)
        }

        private case class Value(value: ValueType, units: UnitsType)
        private case class Field(field: Symbol, units: UnitsType)

        override def toJson: Js.Value = {
            fieldOpt.map(field => Json.writeJs(Field(field, units)))
                    .getOrElse(Json.writeJs(valueOpt.map(value => Value(value, units))))
        }
    }

    class Spatial[FieldType:Default:Json.Writer] extends VectorizedWithUnits[FieldType, SpatialUnits] {
        def this(value: FieldType) = {
            this()
            set(Some(value))
        }

        def this(units: SpatialUnits) = {
            this()
            setUnits(units)
        }

        def this(value: FieldType, units: SpatialUnits) = {
            this(value)
            setUnits(units)
        }
    }

    class Angular[FieldType:Default:Json.Writer] extends VectorizedWithUnits[FieldType, AngularUnits] {
        def this(value: FieldType) = {
            this()
            set(Some(value))
        }

        def this(units: AngularUnits) = {
            this()
            setUnits(units)
        }

        def this(value: FieldType, units: AngularUnits) = {
            this(value)
            setUnits(units)
        }
    }
}
