package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTests @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll() // user도 저장하므로 deleteAll
        // OneToMany로 userLoanHistory로 들고 있는데,
        // deleteAll은 자식 테이블까지 찾아서 제거해주므로 userLoanHistory 제거 로직을 따로 하지 않아도 OK.
    }

    @Test
    @DisplayName("책 등록이 정상 동작한다.")
    fun saveBookTest() {
        // given - BookRequest 객체를 만들어준다. (생성자 만들어주기)
        val request = BookRequest("이상한 나라의 엘리스")

        // when
        bookService.saveBook(request)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("이상한 나라의 엘리스")
    }

    // 정상 동작하는 경우, exception이 나는 경우 2가지를 나눠서 테스트해준다.
    @Test
    @DisplayName("책 대출이 정상 동작한다.")
    fun savedBookTest() {
        // given - user와 book이 저장되어 있어야 함.
        bookRepository.save(Book("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("최태현", null))
        val request = BookLoanRequest("최태현", "이상한 나라의 앨리스")

        // when - 대출 서비스를 호출한다.
        bookService.loanBook(request)

        // then - 정상적으로 동작한 경우를 기대 - UserLoanHistory에 저장되어야 함
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("이상한 나라의 앨리스")

        // 대출을 한 유저가 일치하는지
        assertThat(result[0].user.id).isEqualTo(savedUser.id)

        // 반납했는지 여부 (isReturned) -> getter 만들어주기 / 빌리지 않은 책이어야 함
        assertThat(result[0].isReturn).isFalse
    }

    @Test
    @DisplayName("책이 대출되어 있다면, 신규 대출이 실패한다")
    fun loanBookFailTest() {
        // given
        bookRepository.save(Book("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("최태현", null))

        // 대여한 이력 만들기
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 앨리스", false))

        val request = BookLoanRequest("최태현", "이상한 나라의 앨리스")

        assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다")
    fun returnBookTest() {
        // given - 책 정보는 필요없음, 반납할 땐 user만 가져와서 반납이 일어나므로
        val savedUser = userRepository.save(User("최태현", null))

        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 앨리스", false))
        val request = BookReturnRequest("최태현", "이상한 나라의 앨리스")

        // when
        bookService.returnBook(request)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].isReturn).isTrue
    }
}
