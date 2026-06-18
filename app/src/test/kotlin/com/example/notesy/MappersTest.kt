package com.example.notesy

import com.example.notesy.data.local.NoteEntity
import com.example.notesy.data.local.toEntity
import com.example.notesy.data.local.toNote
import com.example.notesy.data.local.FolderEntity
import com.example.notesy.data.local.toFolder
// import extension functions from mappers; call them as extension methods
import com.example.notesy.data.model.Note
import com.example.notesy.data.model.Folder
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `note to entity and back preserves fields`() {
        val note = Note(
            id = "id1",
            title = "Title",
            content = "Some content",
            folderId = "folder1",
            createdAt = "2026-01-01T00:00:00Z"
        )

        val entity: NoteEntity = note.toEntity(isSynced = true)
        val converted = entity.toNote()

        assertEquals(note.id, converted.id)
        assertEquals(note.title, converted.title)
        assertEquals(note.content, converted.content)
        assertEquals(note.folderId, converted.folderId)
        assertEquals(note.createdAt, converted.createdAt)
    }

    @Test
    fun `folder to entity and back preserves fields`() {
        val folder = Folder(id = "f1", name = "My Folder", createdAt = "2026-01-02T00:00:00Z")
        val entity: FolderEntity = folder.toEntity(isSynced = true)
        val converted = entity.toFolder()

        assertEquals(folder.id, converted.id)
        assertEquals(folder.name, converted.name)
        assertEquals(folder.createdAt, converted.createdAt)
    }
}



