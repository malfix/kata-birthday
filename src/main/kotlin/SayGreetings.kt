import java.time.LocalDate
import java.time.Year

class SayGreetings(
        private val employeesRepository: EmployeesRepository,
        private val greetingsDelivery: GreetingsDelivery,
        private val clocker: Clocker) {
    fun execute() =
            employeesRepository.findAll()
                    .filter { user -> makeGreetings(clocker.today(), user.birthdate) }
                    .map { user -> greetingsDelivery.deliverGreetings(user) }

    private fun makeGreetings(today: LocalDate, birthdate: LocalDate) =
            isBirthday(today, birthdate) || isBirthdayForLeapYear(today, birthdate)

    private fun isBirthdayForLeapYear(date: LocalDate, birthdate: LocalDate) =
            !Year.isLeap(date.year.toLong()) &&
                    birthdate.month == date.month &&
                    birthdate.dayOfMonth == 29 &&
                    date.dayOfMonth == 28

    private fun isBirthday(date: LocalDate, birthdate: LocalDate) =
            birthdate.dayOfMonth == date.dayOfMonth &&
                    birthdate.month == date.month
}
