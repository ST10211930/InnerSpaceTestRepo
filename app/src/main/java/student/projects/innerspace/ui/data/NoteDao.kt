package student.projects.innerspace.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert suspend fun insert(note: Note)
    @Query("SELECT * FROM notes") fun getAllNotes(): LiveData<List<Note>>
    @Delete suspend fun delete(note: Note)
}
