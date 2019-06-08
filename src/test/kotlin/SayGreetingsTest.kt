import org.jmock.AbstractExpectations
import org.jmock.Expectations
import org.jmock.auto.Mock
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import java.time.LocalDate

import java.util.Arrays.asList

class SayGreetingsTest {

    private lateinit var sayGreetings: SayGreetings

    @Rule
    @JvmField
    var context = JUnitRuleMockery()

    @Mock
    private lateinit var greetingsDelivery: GreetingsDelivery

    @Mock
    private lateinit var clocker: Clocker

    @Mock
    private lateinit var employeesRepository: EmployeesRepository

    private val FEB_29_DATE = LocalDate.of(1988, 2, 29)
    private val FEB_28_DATE_NOT_LEAP = LocalDate.of(1985, 2, 28)
    private val FEB_28_DATE_LEAP = LocalDate.of(1988, 2, 28)
    private val A_DATE = LocalDate.of(1988, 5, 4)
    private val ANOTER_DATE = LocalDate.of(1988, 5, 14)

    @Before
    fun setUp() {
        sayGreetings = SayGreetings(employeesRepository, greetingsDelivery, clocker)
    }


    @Test
    fun `send greetings`() {
        context.checking(object : Expectations() {
            init {

                val user = User("Username", A_DATE)

                allowing<EmployeesRepository>(employeesRepository).findAll()
                will(AbstractExpectations.returnValue(asList(user)))

                allowing<Clocker>(clocker).today()
                will(AbstractExpectations.returnValue(A_DATE))

                oneOf<GreetingsDelivery>(greetingsDelivery).deliverGreetings(user)
            }
        })

        sayGreetings.execute()
    }

    @Test
    fun `dont send greetings`() {
        context.checking(object : Expectations() {
            init {
                val user = User("Username", A_DATE)

                allowing<EmployeesRepository>(employeesRepository).findAll()
                will(AbstractExpectations.returnValue(asList(user)))

                allowing<Clocker>(clocker).today()
                will(AbstractExpectations.returnValue(ANOTER_DATE))

                never<GreetingsDelivery>(greetingsDelivery).deliverGreetings(user)
            }
        })

        sayGreetings.execute()
    }

    @Test
    fun `dont send greetings on 28 in a leap year`() {
        context.checking(object : Expectations() {
            init {
                val user = User("Username", FEB_29_DATE)

                allowing<EmployeesRepository>(employeesRepository).findAll()
                will(AbstractExpectations.returnValue(asList(user)))

                allowing<Clocker>(clocker).today()
                will(AbstractExpectations.returnValue(FEB_28_DATE_LEAP))

                exactly(0).of<GreetingsDelivery>(greetingsDelivery).deliverGreetings(user)
            }
        })

        sayGreetings.execute()
    }

    @Test
    fun `send greetings on 28 in a not leap year`() {
        context.checking(object : Expectations() {
            init {
                val user = User("Username", FEB_29_DATE)

                allowing<EmployeesRepository>(employeesRepository).findAll()
                will(AbstractExpectations.returnValue(asList(user)))

                allowing<Clocker>(clocker).today()
                will(AbstractExpectations.returnValue(FEB_28_DATE_NOT_LEAP))

                exactly(1).of<GreetingsDelivery>(greetingsDelivery).deliverGreetings(user)
            }
        })

        sayGreetings.execute()
    }
}
