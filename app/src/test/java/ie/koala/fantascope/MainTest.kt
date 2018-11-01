package ie.koala.fantascope

import ie.koala.fantascope.model.Movie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class MainTest {
    @Test
    fun `01 check mockito is working`() {
        val c = Mockito.mock(Movie::class.java)
        `when`(c.id).thenReturn(3)

        val id = c.id
        assertEquals(3, id)
    }

    @Test
    fun `02 create a movie and check that the values are filled out correctly`() {
        val movie = Movie(
            false,
            "/tr6OExnBfjLADi9OyZoW308mPAp.jpg",
            listOf(
                18,
                35,
                10749
            ),
            74643,
            "en",
            "The Artist",
            "Hollywood, 1927: As silent movie star George Valentin wonders if the arrival of talking pictures will cause him to fade into oblivion, he sparks with Peppy Miller, a young dancer set for a big break.",
            6.943,
            "/i0mpvMIIjyubXsVKug9vX0lYpnd.jpg",
            "2011-06-26",
            "The Artist",
            false,
            7.4,
            1536
        )

        assertEquals("The Artist", movie.title)
    }

}