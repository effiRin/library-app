package com.group.libraryapp.calculator

class Calculator(
    var number: Int
    // 코틀린 클래스는 생성자와 함께 프로퍼티를 줄 수 있다.
    // 생성자에서 숫자 하나를 준 후, 이 숫자를 계속 업데이트한다.
) {
    // 사칙연산 함수 4가지를 만든다.
    fun add(operand: Int) { // operand는 피연산자를 뜻함
        this.number += operand
    }

    fun minus(operand: Int) {
        this.number -= operand
    }

    fun multiply(operand: Int) {
        this.number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0) {
            throw IllegalArgumentException("00으로 나눌 수 없습니다.")
        }

        this.number /= operand
    }
}
