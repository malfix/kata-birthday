import java.time.LocalDate

data class User(
        private val name: String,
        internal val birthdate: LocalDate
)
