package com.example.notesy

import com.example.notesy.data.api.NoteApiService
import com.example.notesy.data.model.AiSuggestRequest
import com.example.notesy.data.model.AiSuggestResponse
import com.example.notesy.data.repository.AiSuggestionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiSuggestionRepositoryTest {

    private class CapturingApi(var shouldThrow: Boolean = false, val response: AiSuggestResponse = AiSuggestResponse(emptyList())) : NoteApiService {
        var lastRequest: AiSuggestRequest? = null

        override suspend fun getFolders() = throw UnsupportedOperationException()
        override suspend fun createFolder(request: com.example.notesy.data.model.CreateFolderRequest) = throw UnsupportedOperationException()
        override suspend fun updateFolder(id: String, request: com.example.notesy.data.model.CreateFolderRequest) = throw UnsupportedOperationException()
        override suspend fun deleteFolder(id: String) = throw UnsupportedOperationException()
        override suspend fun getNotes() = throw UnsupportedOperationException()
        override suspend fun getNotesByFolder(folderId: String) = throw UnsupportedOperationException()
        override suspend fun createNote(request: com.example.notesy.data.model.CreateNoteRequest) = throw UnsupportedOperationException()
        override suspend fun updateNote(id: String, request: com.example.notesy.data.model.CreateNoteRequest) = throw UnsupportedOperationException()
        override suspend fun deleteNote(id: String) = throw UnsupportedOperationException()

        override suspend fun suggestGroceries(request: AiSuggestRequest): AiSuggestResponse {
            if (shouldThrow) throw RuntimeException("network")
            lastRequest = request
            return response
        }
    }

    @Test
    fun `suggestGroceries trims blanks and removes duplicates`() = runBlocking {
        val api = CapturingApi(response = AiSuggestResponse(listOf("  apple  ", "", "banana", " apple ")))
        val repo = AiSuggestionRepository(api)

        val result = repo.suggestGroceries(title = "T", content = "C")

        assertEquals(listOf("apple", "banana"), result)
    }

    @Test
    fun `suggestGroceries returns empty list on api exception`() = runBlocking {
        val api = CapturingApi(shouldThrow = true)
        val repo = AiSuggestionRepository(api)

        val result = repo.suggestGroceries(title = "T", content = "C")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `suggestGroceries combines title content and recent notes into request`() = runBlocking {
        val api = CapturingApi(response = AiSuggestResponse(emptyList()))
        val repo = AiSuggestionRepository(api)

        val recent = listOf("Recent A", "Recent B")
        repo.suggestGroceries(title = "MyTitle", content = "MyContent", recentNotes = recent)

        val req = api.lastRequest
        assertTrue(req != null)
        val text = req!!.noteText

        // basic checks that important sections are present
        assertTrue(text.contains("Title:"))
        assertTrue(text.contains("MyTitle"))
        assertTrue(text.contains("Content:"))
        assertTrue(text.contains("MyContent"))
        assertTrue(text.contains("Recent notes:"))
        assertTrue(text.contains("--- Note 1 ---"))
        assertTrue(text.contains("Recent A"))
        assertTrue(text.contains("--- Note 2 ---"))
        assertTrue(text.contains("Recent B"))
    }
}

