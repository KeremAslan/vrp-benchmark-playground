package common.basedomain

data class Coordinates(val latitude:Double, val longitude: Double): BaseCoordinate {
    override fun latitude():Double {
        return latitude //To change body of created functions use File | Settings | File Templates.
    }

    override fun longitude():Double {
        return longitude //To change body of created functions use File | Settings | File Templates.
    }
}

data class XyCoordinates(val x: Double, val y: Double): BaseCoordinate {
    override fun latitude():Double {
        return x
    }

    override fun longitude():Double{
        return y
    }
}

interface BaseCoordinate {
    fun latitude(): Double
    fun longitude(): Double
}