package com.federicocotogno.minimalnotes.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.federicocotogno.minimalnotes.R
import com.federicocotogno.minimalnotes.adapters.ListAdapter
import com.federicocotogno.minimalnotes.data.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var myNoteViewModel: NoteViewModel
    lateinit var adapter: ListAdapter
    private var orderedByTitle = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Floating action button
        loadData()

        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_newNoteFragment)
        }
        //Setup recycler view
        adapter = ListAdapter()
        rv_recyclerView.adapter = adapter
        rv_recyclerView.layoutManager = LinearLayoutManager(context)

        //Takes care of the viewModels
        viewModels()

        //Show the options menu in this fragment
        setHasOptionsMenu(true)

        //Adds swiping to the recycler view
        swipeRecyclerView()
    }

    private fun saveData() {
        val sp = activity?.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE) ?: return
        with(sp.edit()) {
            putBoolean("sortedBoolean", orderedByTitle)
            commit()
        }
    }

    private fun loadData() {
        val sp = activity?.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE) ?: return
        orderedByTitle = sp.getBoolean("sortedBoolean", false)

    }

    private fun viewModels() {
        //Initialise ViewModel
        myNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        //Keep sort selection updated
        myNoteViewModel.currentBoolean.observe(viewLifecycleOwner, Observer {
            orderedByTitle = it
            Log.d("Boolean", it.toString())

        })

        //Get the data at app start-up
        myNoteViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            if (!orderedByTitle) {
                adapter.setData(it)
                tv_sort_text.text = getString(R.string.sort_by_last_edited)

            }
        })

        //sort by Title
        myNoteViewModel.orderDataByTitle.observe(viewLifecycleOwner, Observer {
            //wait for user to make request to change data order
            if (orderedByTitle) {
                adapter.setData(it)
                tv_sort_text.text = getString(R.string.sort_by_title)
            }
        })
    }

    private fun swipeRecyclerView() {

        //Adds a swipe to the Recycler View to remove items more fluently
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //gets the not at the position
                    val note = adapter.notesList[viewHolder.adapterPosition]

                    Toast.makeText(
                        context,
                        "Note deleted!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //cool animation to cover up bad practise of updating ui for recycler
                    rv_recyclerView.animate().apply {
                        translationXBy(-2000f)
                        duration = 300
                    }.withEndAction {
                        rv_recyclerView.animate().apply {
                            translationXBy(2000f)
                            duration = 300
                            myNoteViewModel.deleteNote(note)
                        }.start()
                    }
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu_more, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> deleteAllNotes()
            R.id.nav_sort -> {
                sortNotes()
                saveData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortNotes() {
        if (!orderedByTitle) {
            //Sorts by title
            myNoteViewModel.currentBoolean.value = true
            adapter.setData(myNoteViewModel.orderDataByTitle.value!!)
            tv_sort_text.text = getString(R.string.sort_by_title)
        } else {
            //Sorts by timestamp
            myNoteViewModel.currentBoolean.value = false
            adapter.setData(myNoteViewModel.getAllData.value!!)
            tv_sort_text.text = getString(R.string.sort_by_last_edited)
        }
    }


    private fun deleteAllNotes() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.apply {
            setTitle("Delete all notes?")
            setMessage("Are you sure you want to delete everything?")
            setPositiveButton("Yes") { _, _ ->

                myNoteViewModel.deleteAllNotes()
                Toast.makeText(context, "Everything has been deleted!", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") { _, _ -> }
        }
        dialogBuilder.create().show()
    }
}