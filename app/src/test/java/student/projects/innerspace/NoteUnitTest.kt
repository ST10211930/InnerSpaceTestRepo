import org.junit.Assert.*
import org.junit.Test
import student.projects.innerspace.data.Note

class NoteUnitTest {
    @Test
    fun note_creation_isCorrect() {
        val note = Note(title = "My Day", content = "It was calm.")
        assertEquals("My Day", note.title)
        assertEquals("It was calm.", note.content)
    }
}