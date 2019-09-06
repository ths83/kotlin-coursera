package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val realDrivers = trips.map { it.driver }.toSet()
    return allDrivers.minus(realDrivers)
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    if (minTrips == 0) return allPassengers
    val allTripPassengers = trips.flatMap { it.passengers }.toList()
    val passengersByAppearences = allTripPassengers.groupingBy { it }.eachCount().mapValues { it.value }
    return passengersByAppearences.filterValues { it >= minTrips }.keys
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val allDriverPassengers = trips.filter { driver == it.driver }.flatMap { it.passengers }.toList()
    val passengersByAppearences = allDriverPassengers.groupingBy { it }.eachCount().mapValues { it.value }
    return passengersByAppearences.filterValues { it > 1 }.keys
}


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val map = allPassengers.map { it to mutableListOf(0F, 0F) }.toMap()
    trips.forEach { it.passengers.forEach { p -> map[p]?.set(0, map[p]?.get(0)?.plus(1F)!!) } }
    trips.filter { it.discount != null }.forEach { it.passengers.forEach { p -> map[p]?.set(1, map[p]?.get(1)?.plus(1F)!!) } }
    return map.filter { it.value[0] != 0F && (it.value[1].div(it.value[0])) > 0.5F }.keys
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val mappedValue = mutableMapOf<IntRange, Int>()
    val sortedDurations = trips.map { it.duration }.toList()
    sortedDurations.forEach {
        var min = 0
        var max = 9
        var range = min..max
        var bool = false
        while (!bool) {
            if (it in range) {
                bool = true
                if (mappedValue[range] == null) mappedValue[range] = 1
                else mappedValue[range] = mappedValue[range]?.plus(1)!!
            } else {
                min += 10
                max += 10
                range = min..max
            }
        }
    }
    return mappedValue.maxBy { it.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val totalAmountsByDriver = mutableMapOf<Driver, Double>()
    val allDriversByDescendingAmount = trips.sortedByDescending { it.cost }.map { it.driver }.toList()
    val twentyPercentBestDrivers = allDriversByDescendingAmount.subList(0, (allDriversByDescendingAmount.count() * 0.2).toInt()).toSet()
    trips.map {
        if (totalAmountsByDriver[it.driver] == null) totalAmountsByDriver[it.driver] = it.cost
        else totalAmountsByDriver[it.driver] = totalAmountsByDriver[it.driver]?.plus(it.cost)!!
    }

    return twentyPercentBestDrivers.sumByDouble { totalAmountsByDriver[it]!! } >= trips.sumByDouble { it.cost } * 0.8
}