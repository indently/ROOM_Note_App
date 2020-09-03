package com.federicocotogno.minimalnotes.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.data.Note
import com.federicocotogno.minimalnotes.data.viewmodel.NoteViewModel
import com.federicocotogno.minimalnotes.utils.CurrentTimeStamp
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var myNoteViewModel: NoteViewModel
    private lateinit var timeStamp: String
    private lateinit var currentTimeStamp: CurrentTimeStamp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentTimeStamp = CurrentTimeStamp()

        myNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        //Updates the EditTexts with current data
        et_update_title.setText(args.currentNote.title)
        et_update_description.setText(args.currentNote.note)

        fab_updateNote.setOnClickListener {
            updateDatabase()
            fab_updateNote.hideKeyboard()

        }
        setHasOptionsMenu(true)
    }

    private fun updateDatabase() {
        //Gets text from the EditTexts
        var title = et_update_title.text.toString()
        var description = et_update_description.text.toString()

        //In case something is empty, it assigns it a default value
        if (title.isEmpty()) {
            title = "No title"
        }
        if (description.isEmpty()) {
            description = "No description"
        }

        //retrieves current timestamp
        timeStamp = currentTimeStamp.getCurrentTimeStamp()

        //Updates current info to the database
        val note = Note(
            args.currentNote.id,
            title,
            description,
            timeStamp
        )
        myNoteViewModel.updateNote(note)

        //Navigate back
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteUser()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.apply {
            setTitle("Delete ${args.currentNote.title}?")
            setMessage("Are you sure you want to delete ${args.currentNote.title}?")
            setPositiveButton("Yes") { _, _ ->
                myNoteViewModel.deleteNote(args.currentNote)
                Toast.makeText(context, "${args.currentNote.title} deleted!", Toast.LENGTH_SHORT)
                    .show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("No") { _, _ -> }
        }
        dialogBuilder.create().show()
    }

}