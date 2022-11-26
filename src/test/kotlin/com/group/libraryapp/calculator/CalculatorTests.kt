package com.group.libraryapp.calculator

fun main() {
    // CalculatorTests를 만들어준다.
    val calculatorTests = CalculatorTests()

    // addTest() 함수와 minus(), multiply(), divide() 함수를 실행시킨다.
    calculatorTests.addTest()
    calculatorTests.minusTest()
    calculatorTests.multiplyTest()
    calculatorTests.divideTest()
    calculatorTests.divideExceptionTest()
}

class CalculatorTests {
    // 테스트 클래스를 만들 때에는 test directory내에 같은 패키지를 사용하고,
    // 테스트 대상 클래스 뒤에 Test라는 이름을 붙이는 것이 관례이다

    fun addTest() {
        // 1단계 : 테스트하고 싶은 대상을 만든다
        val calculator = Calculator(5)

        // 2단계 : 테스트하고 싶은 기능을 호출한다
        calculator.add(3)

        // 3단계 : 테스트 결과와 예상 결과가 맞는지 확인
        val expectedCalculator = Calculator(8)
        if (calculator != expectedCalculator) {
            throw IllegalStateException()
        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        val expectedCalculator = Calculator(2)
        if (calculator != expectedCalculator) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        val expectedCalculator = Calculator(15)
        if (calculator != expectedCalculator) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(2)

        // then
        val expectedCalculator = Calculator(2)
        if (calculator != expectedCalculator) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        // given
        val calculator = Calculator(5)

        // when
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            // IllegalArgumentException 이 나오면 테스트 성공!

            // 우리가 원하는 message가 나오는지도 확인해볼 수 있다.
            if (e.message != "0으로 나눌 수 없습니다.") {
                throw IllegalStateException("메시지가 다릅니다.")
            }
            return
        } catch (e: Exception) {
            // 다른 예외가 나오면, 우리가 원하는 예외가 아니기 때문에 실패
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }
}
