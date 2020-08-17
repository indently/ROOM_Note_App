package com.federicocotogno.minimalnotes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.data.Note
import com.federicocotogno.minimalnotes.data.NoteViewModel
import com.federicocotogno.minimalnotes.utils.CurrentTimeStamp
import kotlinx.android.synthetic.main.fragment_new_note.*
import kotlinx.android.synthetic.main.fragment_update.*
import java.text.SimpleDateFormat
import java.util.*


class NewNoteFragment : Fragment() {

    private lateinit var myNoteViewModel: NoteViewModel
    private lateinit var timeStamp: String
    private lateinit var currentTimeStamp: CurrentTimeStamp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        currentTimeStamp = CurrentTimeStamp()

        //Initialise ViewModel
        myNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        fab_newNote.setOnClickListener {

            updateDatabase()
            fab_newNote.hideKeyboard()
        }
    }

    private fun updateDatabase() {
        //Gets text from the EditTexts
        var title = et_title.text.toString()
        var description = et_description.text.toString()

        //In case something is empty, it assigns it a default value
        if (title.isEmpty()) {
            title = "No title"
        }
        if (description.isEmpty()) {
            description = "No description"
        }

        //retrieves current timestamp
        timeStamp = currentTimeStamp.getCurrentTimeStamp()

        //Adds current info to the database
        val note = Note(0, title, description, timeStamp)
        myNoteViewModel.addNote(note)

        findNavController().navigate(R.id.action_newNoteFragment_to_listFragment)

    }

    //Hides the soft keyboard
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}