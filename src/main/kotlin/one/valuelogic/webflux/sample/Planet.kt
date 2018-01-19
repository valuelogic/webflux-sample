package one.valuelogic.webflux.sample

object Planets {

    val SOLAR_SYSTEM = listOf(
            Planet("Mercury", 0.38),
            Planet("Venus", 0.94),
            Planet("Earth", 1.0),
            Planet("Mars", 0.53))
}

data class Planet(val name: String, val radius: Double)